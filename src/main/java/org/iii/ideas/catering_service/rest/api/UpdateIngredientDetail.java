package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Menu;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.rest.bo.SchoolDataBO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.SchoolAndKitchenUtil;

public class UpdateIngredientDetail  extends AbstractApiInterface<UpdateIngredientDetailRequest, UpdateIngredientDetailResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		
		Transaction tx = this.dbSession.beginTransaction();
		List<Long> batchdataIdArray = new ArrayList<Long>();
		
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		int kid = this.getKitchenId();
		
		String food = this.requestObj.getFood();
		Long menuId = this.requestObj.getMid();
		//find out dishid
		Long dishId = HibernateUtil.queryDishIdByName(this.dbSession, kid, food);
		if(dishId==null){
			this.responseObj.setMsg("找不到此食材! :"+food);
			this.responseObj.setResStatus(0);
		}
		
		Menu menu = HibernateUtil.queryMenuById(this.dbSession, menuId);
		//查詢菜單是否存在
		Criteria criteriaBD = dbSession.createCriteria(Batchdata.class);
		criteriaBD.add(Restrictions.eq("kitchenId", menu.getKitchenId()));
		criteriaBD.add(Restrictions.eq("menuDate", menu.getMenuDate()));
		List<Batchdata> batchdataList = criteriaBD.list();
		if(batchdataList.size()==0){
			this.responseObj.setMsg("找不到此上傳之菜單! id:"+menuId);
			this.responseObj.setResStatus(0);
			if(!tx.wasRolledBack()){
				tx.rollback();
			}
			return;
		}
		
		//檢查菜單可允許的上傳時間 20140912 KC
		for(Batchdata bdata:batchdataList){
			try {
				if (!HibernateUtil.isMenuUploadTime(this.dbSession, bdata.getMenuDate().replace("/", "-"), bdata.getSchoolId())){
					SchoolDataBO schoolBo=SchoolAndKitchenUtil.querySchoolDataBySchoolId(this.dbSession, bdata.getSchoolId());
					String schoolName="";
					if (schoolBo !=null ){
						schoolName=schoolBo.getSchoolName();
					}
					this.responseObj.setResStatus(-1);
					this.responseObj.setMsg("已超過"+schoolName+" 的上傳時間限制! ");
					return;
				}
			} catch (Exception e) {
				this.responseObj.setResStatus(-1);
				this.responseObj.setMsg("暫時無法更新，請洽系統管理員");
				return;
			}
		}
		
		//列出所有在ingredientbatchdata 的batchdataid
		List<Long> updateIngredientbatchdata = new ArrayList<Long>();
		//查詢主batchdata 資訊
		Iterator<Batchdata> iteratorBatchData = batchdataList.iterator();
		while(iteratorBatchData.hasNext()){
			Long batchdataId = iteratorBatchData.next().getBatchDataId();
			HibernateUtil.deleteIngredientbatchdataByDishId(this.dbSession, batchdataId,dishId);
			batchdataIdArray.add(batchdataId);
		}
		
		//找出目前update 的所有ingredinet 資料
		List<IngredientObject> ingredientBatchdata = this.requestObj.getIngredients();
		Iterator<IngredientObject> iterator = ingredientBatchdata.iterator();  
        log.debug("UpdateIngredientDetail Size:"+ingredientBatchdata.size()+" menuId:"+menuId);
        //List 出所有前瑞回傳的食材
		while (iterator.hasNext()) {
			IngredientObject igdbCurrentObject = iterator.next(); 
			
			//找出目前資料的供應商資料
			Supplier supplier = HibernateUtil.getSupplier(this.dbSession, kid, igdbCurrentObject.getSupplyName());
			if(supplier==null){
				this.responseObj.setMsg("找不到供應商名稱:"+igdbCurrentObject.getSupplyName());
				this.responseObj.setResStatus(0);
				if(!tx.wasRolledBack()){
					tx.rollback();
				}
				return;
			}
			//---------依dishId 找出食材資訊---------
			if(CateringServiceUtil.isEmpty(igdbCurrentObject.getName())){
				this.responseObj.setMsg("食材名稱不可為空白");
				this.responseObj.setResStatus(0);
				if(!tx.wasRolledBack()){
					tx.rollback();
				}
				return;
			}
			Ingredient ingredinet = HibernateUtil.getIngredient(this.dbSession,dishId,  igdbCurrentObject.getName(), supplier.getId().getSupplierId());
			if(ingredinet==null){
				//如果食材不存在就新增食材
				ingredinet = new Ingredient();
				ingredinet.setBrand(igdbCurrentObject.getBrand());
				ingredinet.setDishId(dishId);
				ingredinet.setIngredientName(igdbCurrentObject.getName());
				ingredinet.setSupplierId(supplier.getId().getSupplierId());
				ingredinet.setSupplierCompanyId( supplier.getCompanyId()==null?"":supplier.getCompanyId() );
				this.dbSession.save(ingredinet);
			}
			//查出就的ingredientbatchdata 已存在的值
			
			Iterator<Long> batchIdIterator = batchdataIdArray.iterator();
			while(batchIdIterator.hasNext()){
				Long batchdataId = batchIdIterator.next();
				Ingredientbatchdata igdb = new Ingredientbatchdata();
				igdb.setBatchDataId(batchdataId);
				igdb.setBrand(ingredinet.getBrand());
				if(CateringServiceUtil.isEmpty(igdbCurrentObject.getStockDate())){
					this.responseObj.setMsg("進貨日期不可為空白:"+igdbCurrentObject.getName());
					this.responseObj.setResStatus(0);
					if(!tx.wasRolledBack()){
						tx.rollback();
					}
					return;
				}
				igdb.setStockDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", igdbCurrentObject.getStockDate()) );
				igdb.setExpirationDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", igdbCurrentObject.getValidDate())  );
				igdb.setManufactureDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", igdbCurrentObject.getProductDate())   );
				igdb.setCertificationId(igdbCurrentObject.getAuthenticateId());
				igdb.setIngredientId(ingredinet.getIngredientId());
				igdb.setIngredientName(ingredinet.getIngredientName());
				igdb.setLotNumber(igdbCurrentObject.getIngredientLotNum());
				igdb.setSourceCertification(igdbCurrentObject.getLabel());
				igdb.setOrigin("");
				igdb.setSource("");
				igdb.setSupplierId(supplier.getId().getSupplierId());
				igdb.setDishId(dishId); //問昌港
				this.dbSession.save(igdb);
			}
		}
	//	this.cloneMenuDetailInfo(this.dbSession, currentBatchDataId);
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
		if(!tx.wasCommitted()){
			tx.commit();
		}
		
		
	}
	
	public void cloneMenuDetailInfo(Session session,Long currentBatchDataId){
		//這裡未考慮到一天多餐的功能,要注意
		Batchdata tmpBatchdata = HibernateUtil.queryBatchdataById(session,currentBatchDataId);
		Integer currentSchoolId 	= tmpBatchdata.getSchoolId();
		String currentMenuDate 		= tmpBatchdata.getMenuDate();
		Integer currentKitchenId 	= tmpBatchdata.getKitchenId();
		List<Long> otherBatchdataIdList = new ArrayList<Long>();
		List<Long> otherIngredientbatchIdList = new ArrayList<Long>();
		List<Integer> otherSchoolKitchenList = new ArrayList<Integer>();
		List<Long> newBatchdataIdList = new ArrayList<Long>();
		//取得團饈業者其它學校資料
		String schoolkitchenHQL = "from Schoolkitchen s1,Schoolkitchen s2"
				+ " where s2.id.schoolId = :schoolId  and s1.id.kitchenId = :kitchenId and s1.id.schoolId != s2.id.schoolId"
				+ " and s1.id.kitchenId = s2.id.kitchenId";
		Query schoolkitchenQuery = this.dbSession.createQuery(schoolkitchenHQL);
		schoolkitchenQuery.setParameter("kitchenId", currentKitchenId);	
		schoolkitchenQuery.setParameter("schoolId", currentSchoolId);
		//batchdataQuery.setMaxResults(1);
		List schoolkitchenList = schoolkitchenQuery.list();//Schoolkitchen
		Iterator<Object[]> schoolkitchenIterator = schoolkitchenList.iterator();
		while (schoolkitchenIterator.hasNext()) {
			Object[] obj = schoolkitchenIterator.next();
			Schoolkitchen otherKitchenSchool = (Schoolkitchen) obj[0];
			otherSchoolKitchenList.add(otherKitchenSchool.getId().getSchoolId());
		}
		
		//取得團饈業者同一天其它菜單資料
		List<Batchdata> otherBatchdataObject = new ArrayList<Batchdata>();
		String batchdataHQL = "from Batchdata b1 , Batchdata b2 "
				+ "where b1.menuDate = b2.menuDate"
				+ "  and b1.kitchenId = b2.kitchenId  and b1.schoolId != b2.schoolId  "
				+ "	 and b2.menuDate  = :menuDate"
				+ "  and b2.batchDataId = :batchDataId";
		Query batchdataQuery = this.dbSession.createQuery(batchdataHQL);
		batchdataQuery.setParameter("menuDate", currentMenuDate);	
		batchdataQuery.setParameter("batchDataId",currentBatchDataId );
		List batchdataList = batchdataQuery.list();
		Iterator<Object[]> batchdataIterator = batchdataList.iterator();
		while (batchdataIterator.hasNext()) {
			Object[] obj = batchdataIterator.next();
			Batchdata otherBatchdata = (Batchdata) obj[0];
			otherBatchdataIdList.add(otherBatchdata.getBatchDataId());
			HibernateUtil.deleteBatchdataById(session, otherBatchdata.getBatchDataId());
		}
		
		//複製其它School menu的物件 
		Iterator<Integer> otherSchools = otherSchoolKitchenList.iterator();
		while (otherSchools.hasNext()) {
			Integer schoolId = otherSchools.next();
			session.evict(tmpBatchdata);
			tmpBatchdata.setBatchDataId(null);
			tmpBatchdata.setSchoolId(schoolId);
			session.save(tmpBatchdata);
			//HibernateUtil.saveBatchdata(session, tmpBatchdata)
			//把新的batchdata id 紀錄下來
			newBatchdataIdList.add(tmpBatchdata.getBatchDataId());
		}
		
			
		//取得團饈業者菜單食材資料
		String ingredientHQL = "from Ingredientbatchdata where batchDataId=:batchDataId";
		Query ingredientQuery = this.dbSession.createQuery(ingredientHQL);
		ingredientQuery.setParameter("batchDataId", currentBatchDataId);	
		//batchdataQuery.setMaxResults(1);
		List ingredinetList = ingredientQuery.list();
		Iterator<Ingredientbatchdata> ingredientIterator = ingredinetList.iterator();
		while (ingredientIterator.hasNext()) {
			Ingredientbatchdata otherIngredientbatchdata = (Ingredientbatchdata) ingredientIterator.next();
			otherIngredientbatchIdList.add(otherIngredientbatchdata.getIngredientBatchId());
			//新增食材到其他學校菜單
			Iterator<Long> otherBatchdatas = newBatchdataIdList.iterator();
			while (otherBatchdatas.hasNext()) {
				Long batchdataId = otherBatchdatas.next();
				session.evict(otherIngredientbatchdata);
				otherIngredientbatchdata.setBatchDataId(batchdataId);
				otherIngredientbatchdata.setIngredientBatchId(null);
				session.save(otherIngredientbatchdata);
			}
		}		
	}
}
