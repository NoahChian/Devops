package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Query;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class QueryMenuDescByTime  extends AbstractApiInterface<QueryMenuDescByTimeRequest, QueryMenuDescByTimeResponse>  {

	@Override
	public void process() throws NamingException, ParseException {

		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		if (this.getUserType()==null){
			//USERTYPE_SCHOOL_NO_KITCHEN
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		String userType=this.getUserType();
		
		
		String begDate = this.requestObj.getStartDate();
		String endDate = this.requestObj.getEndDate();
		if(CateringServiceUtil.isEmpty(begDate) || CateringServiceUtil.isEmpty(endDate)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認日期區間填寫正確!!");
			return;
		}
		begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
		endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
		
		//檢查是否超過日期的查詢範圍大小  20140221 KC
		if (CateringServiceUtil.isOverQueryRange(begDate,endDate)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查詢日期範圍太大，請縮小查詢範圍");
			return;
		}
		
		log.debug("QueryMenuDescByTime Beg:"+begDate+" and End:"+endDate);
		
		//純他校供應之學校，不加kitchenid 限制  回傳所有自己的學校    20140501 KC
		String sql_kitchen;
		if (!CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)){
			sql_kitchen=" and  b.kitchenId=:kitchenId ";
		}else{
			sql_kitchen="";
		}
		
		
//		String HQL = "select b.menuDate, b.kitchenId from Batchdata b where b.menuDate between :begDate and :endDate "+sql_kitchen+"  group by b.menuDate order by b.menuDate asc";
		//加入機制，僅搜尋午餐與enable為1的菜單
		String HQL = "select b.menuDate, b.kitchenId from Batchdata b where b.menuDate between :begDate and :endDate "+sql_kitchen+" and b.menuType = 1 and b.enable = 1  group by b.menuDate order by b.menuDate asc";

		
		Query query = dbSession.createQuery(HQL);
		
		query.setParameter("begDate", begDate);
		query.setParameter("endDate", endDate);
		//query.setParameter("srcType", null);
		
		//純他校供應之學校，不加kitchenid 限制  回傳所有自己的學校    20140501 KC
		if (!CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)){
			query.setParameter("kitchenId", this.getKitchenId());
		}
		
		
	
		
		List results = query.list();
        Iterator<Object[]> iterator = results.iterator();
        while(iterator.hasNext()){
        	Object[] objBatchdata = iterator.next();
        	String menuDate=(String) objBatchdata[0];
        	Integer menuKid=(Integer) objBatchdata[1];
			IngredientMenuObj menu = new IngredientMenuObj();
			menu.setDate(menuDate);
			
			//純他校供應之學校，傳入batchdata的kitchen id
			//List<Batchdata> batchdataList = HibernateUtil.queryBatchdataByMenuDate(this.dbSession, this.getKitchenId(), menuDate);
			List<Batchdata> batchdataList;
			if (!CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)){
					batchdataList = HibernateUtil.queryBatchdataByMenuDate(this.dbSession, this.getKitchenId(), menuDate);
			}else{
				batchdataList = HibernateUtil.queryBatchdataByMenuDate(this.dbSession, menuKid, menuDate);
			}
			

			
			Iterator<Batchdata> batchdataIterator = batchdataList.iterator();
			List<Long> dishIdList= new ArrayList<Long>();
			while(batchdataIterator.hasNext()){
				Batchdata tmpBatchdata = batchdataIterator.next();
				//判斷是否為台北市資料  20140428 KC
				if(tmpBatchdata.getMainFoodId()==0 && tmpBatchdata.getSrcType().equals("A")){
					continue;
				}
				dishIdList = HibernateUtil.getDishIdByBatchdata(dishIdList, tmpBatchdata);
			}
			String foodStr = HibernateUtil.getDishByDishIdList(this.dbSession,dishIdList);
			menu.setDescription(foodStr);
			this.responseObj.getMenu().add(menu);
		}
        this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
