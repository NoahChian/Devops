package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class UpdateSchoolSeasoningDetail extends
		AbstractApiInterface<UpdateSchoolSeasoningDetailRequest, UpdateSchoolSeasoningDetailResponse> {

	@Override
	public void process() throws ParseException {
		Integer schoolId = this.requestObj.getSchoolId();
		Transaction tx = this.dbSession.beginTransaction();
		List<Long> batchdataIdArray = new ArrayList<Long>();
		try {
			if (!this.isLogin()) {
				this.responseObj.setResStatus(-2);
				this.responseObj.setMsg("使用者未授權");
				return;
			}
			Integer kitchenId = this.getKitchenId();
			String menuDate = this.requestObj.getMenuDate();
			String dishName = CateringServiceUtil.ColumnNameOfSeasoning;
			Dish dish = HibernateUtil.queryDishByName(this.dbSession, kitchenId, dishName);
			if (dish == null) {
				// 如果沒有調味料就自動新增一筆
				dish = new Dish();
				dish.setDishName(dishName);
				dish.setKitchenId(kitchenId);
				dish.setPicturePath("");
				this.dbSession.save(dish);
			}
			Long dishId = dish.getDishId();

			// 找出這一天的所有調味料batchdata
			List<Batchdata> batchdataList = HibernateUtil.querySchoolBatchdataByMenuDate(this.dbSession, kitchenId,
					menuDate, schoolId);

			// 列出所有在ingredientbatchdata 的batchdataid 新增到 batchdataIdArray
			List<Integer> updateIngredientbatchdata = new ArrayList<Integer>();
			Iterator<Batchdata> iteratorBatchData = batchdataList.iterator();
			while (iteratorBatchData.hasNext()) {
				Long batchdataId = iteratorBatchData.next().getBatchDataId();
				HibernateUtil.deleteIngredientbatchdataByDishId(this.dbSession, batchdataId, dishId);
				batchdataIdArray.add(batchdataId);
				log.debug("更新調味料的 BatchdataId:" + batchdataId);
			}
			//依前瑞資料更新調味料
			List<IngredientObject> ingredientBatchdata = this.requestObj.getSeasonings();
			Iterator<IngredientObject> iterator = ingredientBatchdata.iterator();
			log.debug("UpdateSeasoningDetailv2 Size:" + ingredientBatchdata.size());
			// List 出所有前瑞回傳的食材
			while (iterator.hasNext()) {
				IngredientObject igdbCurrentObject = iterator.next();
				// 找出這個調味料的供應商資料
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
				// 找出這個調味料的原始資料,如果沒有這個菜色就新增一筆

				Ingredient ingredient = HibernateUtil.queryIngredientIdByDishAndIngredientname(this.dbSession,
						dishId, igdbCurrentObject.getName());

				if (ingredient == null) {
					// 如果食材不存在就新增食材
					/*ingredient = HibernateUtil.saveIngredient(this.dbSession, dishId, igdbCurrentObject.getName(),
							igdbCurrentObject.getBrand(), supplier.getId().getSupplierId(), supplier.getCompanyId());
					*/
					//調用新方法 儲存製造商及產品名稱 add by ellis 20141119
					ingredient = HibernateUtil.saveIngredient(this.dbSession, dishId, igdbCurrentObject.getName(),
							igdbCurrentObject.getBrand(), supplier.getId().getSupplierId(), supplier.getCompanyId(),igdbCurrentObject.getProductName(),igdbCurrentObject.getManufacturer());
					log.debug("新增調味料到食材基本檔 :" + igdbCurrentObject.getName());
				}
				
				int igdbAttr = CateringServiceUtil.getIngredientAttrVal(igdbCurrentObject.getIngredientAttribute());
				
				//更新至所有相關學校id
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
					
					
					log.debug("更新調味料的 BatchdataId:" + batchdataId + " 調味料:" + dishName);
				}
			}

			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
			tx.commit();
		} catch (Exception e) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(e.getMessage());
			if(!tx.wasRolledBack()){
				tx.rollback();
			}
		}
	}
}
