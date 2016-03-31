package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryManageList  extends AbstractApiInterface<QueryManageListRequest, QueryManageListResponse>{

	@Override
	public void process() throws NamingException,HibernateException, ParseException {
		// TODO Auto-generated method stub
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		Integer countyId = AuthenUtil.getCountyNumByUsername(this.getUsername());
		
		String manageFunc =this.requestObj.getManageFunc();
		String manageDate =this.requestObj.getManageDate();
		//String begDate =this.requestObj.getStartDate();
		//String endDate =this.requestObj.getEndDate();
		manageDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", manageDate));
		
		log.debug("QueryManageList condition manageFunc:"+manageFunc+" manageDate:"+manageDate);
		if(CateringServiceUtil.isEmpty(manageDate) ){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認查詢參數無誤");
			return;
		}
		String HQL = "";
		if("countKitchenMenu".equals(manageFunc)){
		HQL = "SELECT distinct k.kitchenName"
				+ " FROM Batchdata b ,Kitchen k, School s "
				+ "where b.kitchenId = k.kitchenId "
				+ "and b.menuDate like :manageDate "
				+ " and s.schoolId = b.schoolId ";
		}else if("countSchoolMenu".equals(manageFunc)){
		HQL = "SELECT distinct s.schoolName"
				+ " FROM Batchdata b ,School s  "
				+ "where b.schoolId = s.schoolId "
				+ "and b.menuDate like :manageDate ";
		}
		/* //此功能會造成語意混淆，已於queryNullIngredientBySchoolAndTime 處理
		 * else if("countMenuNonIngre".equals(manageFunc)){
		HQL = "SELECT distinct s.schoolName"
				+ " FROM Batchdata b ,School s   "
				+ "where b.schoolId = s.schoolId "
				+ "and b.menuDate like :manageDate "
				+ "and b.batchDataId not in (select i.batchDataId from Ingredientbatchdata i where b.batchDataId = i.batchDataId ) ";
		}*/
		else{
		System.out.println("No function selected!");
		}
		if(CateringServiceCode.AUTHEN_NO_COUNTY.equals(countyId.toString())){
			return;
		}else if(CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(countyId.toString())){
			if("countKitchenMenu".equals(manageFunc)){
				HQL +=  " order by k.kitchenName desc ";
			}else if("countSchoolMenu".equals(manageFunc)){
				HQL +=  " order by s.schoolName desc ";
			}
		}else {
			if("countKitchenMenu".equals(manageFunc)){
				HQL += "and s.countyId = :countyId ";
				HQL +=  " order by k.kitchenName desc ";
			}else if("countSchoolMenu".equals(manageFunc)){
				HQL += "and s.countyId = :countyId ";
				HQL +=  " order by s.schoolName desc ";
			}
			
		}
		
		Query query = dbSession.createQuery(HQL);
		query.setParameter("manageDate", manageDate );
		if(!CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(countyId.toString())){
			query.setParameter("countyId", countyId);
		}
		List results = query.list();
        Iterator<String> iterator = results.iterator();
      
        
        log.debug("QueryManageList Size:"+results.size());
        
		while (iterator.hasNext()) {
			List <String> td = new ArrayList<String>();
			String obj = iterator.next();
			//for(int i = 0; i < obj.length; i++){
				td.add(obj);
			//}
			this.responseObj.getReturnList().add(td);
		}
		
			
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
