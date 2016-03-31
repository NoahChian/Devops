package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Menu;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class UpdateSeasoningDetail extends
		AbstractApiInterface<UpdateSeasoningDetailRequest, UpdateSeasoningDetailResponse> {

	@Override
	public void process() throws ParseException {

		Transaction tx = this.dbSession.beginTransaction();
		List<Long> batchdataIdArray = new ArrayList<Long>();

		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		int kid = this.getKitchenId();
		Long menuId = this.requestObj.getMid();
		Menu menu = HibernateUtil.queryMenuById(this.dbSession, menuId);
		// 查詢菜單是否存在
		Criteria criteriaBD = dbSession.createCriteria(Batchdata.class);
		criteriaBD.add(Restrictions.eq("kitchenId", menu.getKitchenId()));
		criteriaBD.add(Restrictions.eq("menuDate", menu.getMenuDate()));
		List<Batchdata> batchdataList = criteriaBD.list();
		if (batchdataList.size() == 0) {
			this.responseObj.setMsg("找不到此上傳之菜單! id:" + menuId);
			this.responseObj.setResStatus(0);
			if(!tx.wasRolledBack()){
				tx.rollback();
			}
			return;
		}

		// 列出所有在ingredientbatchdata 的batchdataid
		List<Integer> updateIngredientbatchdata = new ArrayList<Integer>();
		// 查詢主batchdata 資訊
		Iterator<Batchdata> iteratorBatchData = batchdataList.iterator();
		while (iteratorBatchData.hasNext()) {
			Long batchdataId = iteratorBatchData.next().getBatchDataId();
			HibernateUtil.deleteIngredientbatchdataBySeasoning(this.dbSession, batchdataId);
			batchdataIdArray.add(batchdataId);
		}

		List<IngredientObject> ingredientBatchdata = this.requestObj.getSeasonings();
		Iterator<IngredientObject> iterator = ingredientBatchdata.iterator();
		log.debug("UpdateIngredientDetail Size:" + ingredientBatchdata.size());
		// List 出所有前瑞回傳的食材
		while (iterator.hasNext()) {
			IngredientObject igdbCurrentObject = iterator.next();
			// 找出這個調味料的供應商資料
			Supplier supplier = HibernateUtil.getSupplier(this.dbSession, kid, igdbCurrentObject.getSupplyName());
			if (supplier == null) {
				this.responseObj.setMsg("找不到供應商名稱:" + igdbCurrentObject.getSupplyName());
				this.responseObj.setResStatus(0);
				if(!tx.wasRolledBack()){
					tx.rollback();
				}
				return;
			}
			// 找出這個調味料的原始資料,如果沒有這個菜色就新增一筆
			String ingredientName = (igdbCurrentObject.getName().indexOf(CateringServiceUtil.ColumnNameOfSeasoning) == -1) ? CateringServiceUtil.ColumnNameOfSeasoning
					+ igdbCurrentObject.getName() : igdbCurrentObject.getName();
			Long dishId = HibernateUtil.queryDishIdByName(this.dbSession, this.getKitchenId(), ingredientName);
			Ingredient ingredinet = null;
			if (dishId == null) {
				Dish dish = new Dish();
				dish.setDishName(ingredientName);
				dish.setKitchenId(kid);
				dish.setPicturePath("");
				this.dbSession.save(dish);
				dishId = dish.getDishId();

				// 如果食材不存在就新增食材
				ingredinet = new Ingredient();
				ingredinet.setBrand(igdbCurrentObject.getBrand());
				ingredinet.setDishId(dishId);
				ingredinet.setIngredientName(ingredientName);
				ingredinet.setSupplierId(supplier.getId().getSupplierId());
				ingredinet.setSupplierCompanyId( supplier.getCompanyId()==null?"":supplier.getCompanyId() );
				this.dbSession.save(ingredinet);
				log.debug("new ingredinet id:" + ingredinet.getIngredientId());
			} else {
				List<Ingredient> ingredinetList = HibernateUtil.queryIngredientByDishId(this.dbSession, dishId);
				if (ingredinetList.size() == 0) {
					this.responseObj.setMsg("在菜色資料中找不到此調味料:" + igdbCurrentObject.getName());
					this.responseObj.setResStatus(0);
					if(!tx.wasRolledBack()){
						tx.rollback();
					}
					return;
				} else if (ingredinetList.size() > 1) {
					this.responseObj.setMsg("調味料在菜色資料中的食材不應該多筆:" + igdbCurrentObject.getName() + " 菜色ID:" + dishId);
					this.responseObj.setResStatus(0);
					if(!tx.wasRolledBack()){
						tx.rollback();
					}
					return;
				}
				Iterator<Ingredient> ingredinetIterator = ingredinetList.iterator();
				// List 出所有前瑞回傳的食材
				while (ingredinetIterator.hasNext()) {
					ingredinet = ingredinetIterator.next();
				}
			}
			
			Iterator<Long> batchIdIterator = batchdataIdArray.iterator();
			while(batchIdIterator.hasNext()){
				Long batchdataId = batchIdIterator.next();
				Ingredientbatchdata igdb = new Ingredientbatchdata();
				igdb.setBatchDataId(batchdataId);
				igdb.setBrand(ingredinet.getBrand());
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
				igdb.setSupplierCompanyId( supplier.getCompanyId()==null?"":supplier.getCompanyId() );
				this.dbSession.save(igdb);
			}
			
			/*
			if (CateringServiceUtil.isEmpty(igdbCurrentObject.getIngredientBatchId())) {
				// 在IngredientBatch中找不到此資料
				Ingredientbatchdata igdb = new Ingredientbatchdata();
				igdb.setBatchDataId(batchdata.getBatchDataId());
				igdb.setBrand(ingredinet.getBrand());
				igdb.setStockDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", igdbCurrentObject.getStockDate()));
				igdb.setExpirationDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd",
						igdbCurrentObject.getValidDate()));
				igdb.setManufactureDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd",
						igdbCurrentObject.getProductDate()));
				igdb.setCertificationId(igdbCurrentObject.getAuthenticateId());
				igdb.setIngredientId(ingredinet.getIngredientId());
				igdb.setIngredientName(ingredientName);
				igdb.setLotNumber(igdbCurrentObject.getIngredientLotNum());
				igdb.setSourceCertification(igdbCurrentObject.getLabel());
				igdb.setOrigin("");
				igdb.setSource(supplier.getSupplierName());
				igdb.setSupplierId(supplier.getId().getSupplierId());
				igdb.setDishId(dishId); // 問昌港
				this.dbSession.save(igdb);
				updateIngredientbatchdata.add(igdb.getIngredientBatchId());
				log.debug(">>>>>>UpdateSeasoningDetail 新增食材 ingredient batchdata ID:" + igdb.getBatchDataId()
						+ " DishID:" + dishId);
			} else {
				String HQL = "update Ingredientbatchdata set batchDataId=:batchDataId,brand=:brand,stockDate=:stockDate,expirationDate=:expirationDate,"
						+ "certificationId=:certificationId,ingredientId=:ingredientId,ingredientName=:ingredientName,"
						+ "lotNumber=:lotNumber,origin=:origin,source=:source,sourceCertification=:sourceCertification,manufactureDate=:manufactureDate,"
						+ "supplierId=:supplierId,dishId=:dishId where ingredientBatchId=:ingredientBatchId";
				Query query = dbSession.createQuery(HQL);
				query.setParameter("batchDataId", batchdata.getBatchDataId());
				query.setParameter("brand", ingredinet.getBrand());
				query.setParameter("stockDate",
						HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", igdbCurrentObject.getStockDate()));
				query.setParameter("expirationDate",
						HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", igdbCurrentObject.getValidDate()));
				query.setParameter("certificationId", igdbCurrentObject.getAuthenticateId());
				query.setParameter("ingredientId", ingredinet.getIngredientId());
				query.setParameter("ingredientName", ingredinet.getIngredientName());
				query.setParameter("lotNumber", igdbCurrentObject.getIngredientLotNum());
				query.setParameter("origin", "");
				query.setParameter("source", supplier.getSupplierName());
				query.setParameter("sourceCertification", igdbCurrentObject.getLabel());
				query.setParameter("supplierId", supplier.getId().getSupplierId());
				query.setParameter("dishId", dishId);
				query.setParameter("manufactureDate",
						HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", igdbCurrentObject.getProductDate()));
				query.setParameter("ingredientBatchId", Integer.valueOf(igdbCurrentObject.getIngredientBatchId()));
				query.executeUpdate();
				updateIngredientbatchdata.add(Integer.valueOf(igdbCurrentObject.getIngredientBatchId()));
				log.debug(">>>>>>UpdateSeasoningDetail 更新食材 HQL" + ingredinet.getIngredientName());
			}
			*/
		}
		// delete 不在ingredientbatchdataId的資料
		/*
		String HQL = "delete from Ingredientbatchdata where ingredientBatchId not in(:ingredientBatchIds)  and batchDataId=:batchDataId";
		Query query = dbSession.createQuery(HQL);
		query.setParameterList("ingredientBatchIds", updateIngredientbatchdata);
		query.setParameter("batchDataId", batchdata.getBatchDataId());
		query.executeUpdate();
		this.cloneMenuDetailInfo(this.dbSession, menuId);
		*/

		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

		tx.commit();

	}

	public void cloneMenuDetailInfo(Session session, Long currentBatchDataId) {
		// 這裡未考慮到一天多餐的功能,要注意
		Batchdata tmpBatchdata = HibernateUtil.queryBatchdataById(session, currentBatchDataId);
		Integer currentSchoolId = tmpBatchdata.getSchoolId();
		String currentMenuDate = tmpBatchdata.getMenuDate();
		Integer currentKitchenId = tmpBatchdata.getKitchenId();
		List<Long> otherBatchdataIdList = new ArrayList<Long>();
		List<Long> otherIngredientbatchIdList = new ArrayList<Long>();
		List<Integer> otherSchoolKitchenList = new ArrayList<Integer>();
		List<Long> newBatchdataIdList = new ArrayList<Long>();
		log.debug("cloneMenuDetailInfo original SchoolId:" + currentSchoolId);
		// 取得團饈業者其它學校資料
		String schoolkitchenHQL = "from Schoolkitchen s1,Schoolkitchen s2"
				+ " where s2.id.schoolId = :schoolId  and s1.id.kitchenId = :kitchenId and s1.id.schoolId != s2.id.schoolId"
				+ " and s1.id.kitchenId = s2.id.kitchenId";
		Query schoolkitchenQuery = this.dbSession.createQuery(schoolkitchenHQL);
		schoolkitchenQuery.setParameter("kitchenId", currentKitchenId);
		schoolkitchenQuery.setParameter("schoolId", currentSchoolId);
		List schoolkitchenList = schoolkitchenQuery.list();// Schoolkitchen
		Iterator<Object[]> schoolkitchenIterator = schoolkitchenList.iterator();
		while (schoolkitchenIterator.hasNext()) {
			Object[] obj = schoolkitchenIterator.next();
			Schoolkitchen otherKitchenSchool = (Schoolkitchen) obj[0];
			otherSchoolKitchenList.add(otherKitchenSchool.getId().getSchoolId());
			log.debug("get Other SchoolId:" + otherKitchenSchool.getId().getSchoolId());
		}

		// 取得團饈業者同一天其它菜單資料
		List<Batchdata> otherBatchdataObject = new ArrayList<Batchdata>();
		String batchdataHQL = "from Batchdata b1 , Batchdata b2 " + "where b1.menuDate = b2.menuDate"
				+ "  and b1.kitchenId = b2.kitchenId  and b1.schoolId != b2.schoolId  "
				+ "	 and b2.menuDate  = :menuDate" + "  and b2.batchDataId = :batchDataId";
		Query batchdataQuery = this.dbSession.createQuery(batchdataHQL);
		batchdataQuery.setParameter("menuDate", currentMenuDate);
		batchdataQuery.setParameter("batchDataId", currentBatchDataId);
		List batchdataList = batchdataQuery.list();
		Iterator<Object[]> batchdataIterator = batchdataList.iterator();
		while (batchdataIterator.hasNext()) {
			Object[] obj = batchdataIterator.next();
			Batchdata otherBatchdata = (Batchdata) obj[0];
			otherBatchdataIdList.add(otherBatchdata.getBatchDataId());
			HibernateUtil.deleteBatchdataById(session, otherBatchdata.getBatchDataId());
			log.debug("cloneMenuDetailInfo 找出其它調味料  Clone BatchdataID:" + otherBatchdata.getBatchDataId());
		}

		// 複製其它School menu的物件
		Iterator<Integer> otherSchools = otherSchoolKitchenList.iterator();
		while (otherSchools.hasNext()) {
			Integer schoolId = otherSchools.next();
			session.evict(tmpBatchdata);
			tmpBatchdata.setBatchDataId(null);
			tmpBatchdata.setSchoolId(schoolId);
			session.save(tmpBatchdata);
			// 把新的batchdata id 紀錄下來
			log.debug("cloneMenuDetailInfo 調味料  Clone BatchdataID:" + tmpBatchdata.getBatchDataId() + " schoolId:"
					+ schoolId);
			newBatchdataIdList.add(tmpBatchdata.getBatchDataId());
		}

		// 取得團饈業者菜單食材資料
		String ingredientHQL = "from Ingredientbatchdata where batchDataId=:batchDataId";
		Query ingredientQuery = this.dbSession.createQuery(ingredientHQL);
		ingredientQuery.setParameter("batchDataId", currentBatchDataId);
		List ingredinetList = ingredientQuery.list();
		Iterator<Ingredientbatchdata> ingredientIterator = ingredinetList.iterator();
		while (ingredientIterator.hasNext()) {
			Ingredientbatchdata otherIngredientbatchdata = (Ingredientbatchdata) ingredientIterator.next();
			log.debug("cloneMenuDetailInfo 調味料  Insert BatchdataID:"
					+ otherIngredientbatchdata.getIngredientBatchId());
			// 新增食材到其他學校菜單
			Iterator<Long> otherBatchdatas = newBatchdataIdList.iterator();
			while (otherBatchdatas.hasNext()) {
				Long batchdataId = otherBatchdatas.next();
				session.evict(otherIngredientbatchdata);
				otherIngredientbatchdata.setBatchDataId(batchdataId);
				otherIngredientbatchdata.setIngredientBatchId(null);
				session.save(otherIngredientbatchdata);
				log.debug("cloneMenuDetailInfo 調味料 Insert BatchdataID:" + batchdataId + " IngredientBatchId:"
						+ otherIngredientbatchdata.getIngredientBatchId());
			}
		}
	}

}
