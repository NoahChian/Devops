package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.SchoolAndKitchenUtil;

public class UpdateSchoolIngredientDetail extends
		AbstractApiInterface<UpdateSchoolIngredientDetailRequest, UpdateSchoolIngredientDetailResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		Long dishId = this.requestObj.getDishId();
		Integer kitchenId = this.getKitchenId();
		Integer schoolId = this.requestObj.getSchoolId();
		String menuDate = this.requestObj.getMenuDate();

		//判斷是否超過上傳限制時間  20140724 KC
		try {
			if(!CateringServiceUtil.isUploadTime(CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", menuDate)
					,new Timestamp( (new Date()).getTime()), SchoolAndKitchenUtil.queryUploadLimitTimeBySchoolid(dbSession, schoolId))){
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("超過上傳限制時間，無法更新! ");
				return;
			}
		} catch (ParseException e) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("超過上傳限制時間，無法更新! ");
			return;
		}		
		
		
		
		Transaction tx = this.dbSession.beginTransaction();
		try {
			List<Long> batchdataIdArray = new ArrayList<Long>();
			if (!this.isLogin()) {
				if(!tx.wasRolledBack()){
					tx.rollback();
				}
				this.responseObj.setResStatus(-2);
				this.responseObj.setMsg("使用者未授權");
				return;
			}

			List<Batchdata> batchdataList = HibernateUtil.querySchoolBatchdataByMenuDateAndDishId(this.dbSession,
					kitchenId, menuDate, schoolId, dishId);
			// 列出所有在ingredientbatchdata 的batchdataid
			List<Integer> updateIngredientbatchdata = new ArrayList<Integer>();
			// 先清除源始的ingredientbatchdata 的資料(為新的資料做新增準備)
			Iterator<Batchdata> iteratorBatchData = batchdataList.iterator();
			while (iteratorBatchData.hasNext()) {
				Long batchdataId = iteratorBatchData.next().getBatchDataId();
				HibernateUtil.deleteIngredientbatchdataByDishId(this.dbSession, batchdataId, dishId);
				batchdataIdArray.add(batchdataId);
			}

			// 找出目前update 的所有ingredinet 資料
			List<IngredientObject> ingredientBatchdata = this.requestObj.getIngredients();
			Iterator<IngredientObject> iterator = ingredientBatchdata.iterator();
			log.debug("UpdateIngredientDetailv2 Size:" + ingredientBatchdata.size() + " date:" + menuDate
					+ " dishId:" + dishId);
			// List 出所有前瑞回傳的食材
			while (iterator.hasNext()) {
				IngredientObject igdbCurrentObject = iterator.next();
				// 找出目前資料的供應商資料
				Supplier supplier = HibernateUtil.getSupplier(this.dbSession, kitchenId,
						igdbCurrentObject.getSupplyName());
				if (supplier == null) {
					this.responseObj.setMsg("找不到供應商名稱:" + igdbCurrentObject.getSupplyName());
					this.responseObj.setResStatus(0);
					if(!tx.wasRolledBack()){
						tx.rollback();
					}
					return;
				}
				// ---------依dishId 找出食材資訊---------
				Ingredient ingredient = null;
				ingredient = HibernateUtil
						.queryIngredientByName(this.dbSession, dishId, igdbCurrentObject.getName());
				// 如果相同食材相同就採用更新
				if (ingredient != null) {
					this.dbSession.evict(ingredient);
					ingredient.setBrand(igdbCurrentObject.getBrand());
					ingredient.setIngredientName(igdbCurrentObject.getName());
					ingredient.setSupplierId(supplier.getId().getSupplierId());
					ingredient.setSupplierCompanyId(supplier.getCompanyId() == null ? "" : supplier.getCompanyId());
					//20141028 ellis 新增製造商與產品名稱欄位
					ingredient.setProductName(igdbCurrentObject.getProductName() == null ? "" : igdbCurrentObject.getProductName());
					ingredient.setManufacturer(ingredient.getManufacturer() == null ? "" : igdbCurrentObject.getManufacturer());
					this.dbSession.update(ingredient);
				} else {
					ingredient = HibernateUtil.getIngredient(this.dbSession, dishId, igdbCurrentObject.getName(),
							supplier.getId().getSupplierId());
					if (ingredient == null) {
						// 如果食材不存在就新增食材
						/*
						ingredient = new Ingredient();
						ingredient.setBrand(igdbCurrentObject.getBrand());
						ingredient.setDishId(dishId);
						ingredient.setIngredientName(igdbCurrentObject.getName());
						ingredient.setSupplierId(supplier.getId().getSupplierId());
						ingredient.setSupplierCompanyId(supplier.getCompanyId() == null ? "" : supplier
								.getCompanyId());
						this.dbSession.save(ingredient);
						*/
						//20141028 ellis 新增製造商與產品名稱欄位
						ingredient = HibernateUtil.saveIngredient(this.dbSession, dishId, igdbCurrentObject.getName(),
								igdbCurrentObject.getBrand(), supplier.getId().getSupplierId(), supplier.getCompanyId(),igdbCurrentObject.getProductName(),igdbCurrentObject.getManufacturer());
					}
				}
				
				int igdbAttr = CateringServiceUtil.getIngredientAttrVal(igdbCurrentObject.getIngredientAttribute());
				// 查出就的ingredientbatchdata 已存在的值
				Iterator<Long> batchIdIterator = batchdataIdArray.iterator();
				while (batchIdIterator.hasNext()) {
					Long batchdataId = batchIdIterator.next();
					
//					Ingredientbatchdata igdb = HibernateUtil.saveIngredientbatchdata(this.dbSession, batchdataId,
//							ingredient.getIngredientId(), supplier.getId().getSupplierId(), dishId,
//							igdbCurrentObject.getIngredientLotNum(), ingredient.getIngredientName(),
//							igdbCurrentObject.getBrand(), igdbCurrentObject.getStockDate(),
//							igdbCurrentObject.getValidDate(), igdbCurrentObject.getProductDate(),
//							igdbCurrentObject.getAuthenticateId(), igdbCurrentObject.getLabel(),
//							supplier.getCompanyId(),"","");
					
					Ingredientbatchdata igdb = HibernateUtil.saveIngredientbatchdata(this.dbSession, batchdataId, ingredient.getIngredientId(), supplier.getId().getSupplierId(), dishId, igdbCurrentObject.getIngredientLotNum(), ingredient.getIngredientName(),
							igdbCurrentObject.getBrand(), igdbCurrentObject.getStockDate(), igdbCurrentObject.getValidDate(), igdbCurrentObject.getProductDate(), igdbCurrentObject.getAuthenticateId(), igdbCurrentObject.getLabel(),
						supplier.getCompanyId(), "", "",igdbAttr,igdbCurrentObject.getProductName(),igdbCurrentObject.getManufacturer(),igdbCurrentObject.getIngredientQuantity(),"");
					
					log.debug("更新食材資料到BatchdataId:" + batchdataId);
				}
			}
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
			if (!tx.wasCommitted()) {
				tx.commit();
			}
		} catch (Exception e) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(e.getMessage());
			if(!tx.wasRolledBack()){
				tx.rollback();
			}
		}
	}
}
