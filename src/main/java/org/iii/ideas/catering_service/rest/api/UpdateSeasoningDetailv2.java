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
import org.iii.ideas.catering_service.dao.IngredientbatchdataDAO;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.service.IngredientBatchDataXlsService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.LogUtil;

public class UpdateSeasoningDetailv2 extends AbstractApiInterface<UpdateSeasoningDetailv2Request, UpdateSeasoningDetailv2Response> {


	@Override
	public void process() throws ParseException {

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
			String seasoningDetail = "";
			if (dish == null) {
				// 如果沒有調味料就自動新增一筆
				dish = new Dish();
				dish.setDishName(dishName);
				dish.setKitchenId(kitchenId);
				dish.setPicturePath("");
				this.dbSession.save(dish);
			}
			Long dishId = dish.getDishId();
			// 找出這一天的所有調味料batchdataId
			List<Batchdata> batchdataList = HibernateUtil.queryBatchdataByMenuDate(this.dbSession, kitchenId, menuDate);
			Iterator<Batchdata> iteratorBatchData = batchdataList.iterator();
			while (iteratorBatchData.hasNext()) {
				Long batchdataId = iteratorBatchData.next().getBatchDataId();
				// 清除所有調味料舊資料
				// HibernateUtil.deleteIngr	edientbatchdataByDishId(this.dbSession,
				// batchdataId, dishId);
				batchdataIdArray.add(batchdataId);
				log.debug("更新調味料的 BatchdataId:" + batchdataId);
			}
			// 取出前端所有調味料資料
			List<IngredientObject> ingredientBatchdata = this.requestObj.getSeasonings();
			
			log.debug("UpdateSeasoningDetailv2 Size:" + ingredientBatchdata.size());

			// 運用Excel上傳處理例外狀況的功能
			IngredientBatchDataXlsService ibdService = new IngredientBatchDataXlsService(dbSession);
			Iterator<Long> batchIdIterator = batchdataIdArray.iterator(); //20140530 調正new Iterator 位置 否則永遠只會跑第一個學校的調味料(在此迴圈外)
			// 新增ingredientbatchdata
			IngredientbatchdataDAO ibDao = new IngredientbatchdataDAO(dbSession);
			while (batchIdIterator.hasNext()) {
				Long batchdataId = batchIdIterator.next();
				Iterator<IngredientObject> iterator = ingredientBatchdata.iterator();
				// List 出所有前瑞回傳的食材
				while (iterator.hasNext()) {
					IngredientObject igdbCurrentObject = iterator.next();
					// 找出這個調味料的供應商資料
					Supplier supplier = HibernateUtil.getSupplier(this.dbSession, kitchenId, igdbCurrentObject.getSupplyName());
					if (supplier == null) {
						this.responseObj.setMsg("找不到供應商名稱:" + igdbCurrentObject.getSupplyName());
						this.responseObj.setResStatus(0);
						if (!tx.wasRolledBack()) {
							tx.rollback();
						}
						return;
					}
					// 找出這個調味料的原始資料,如果沒有這個菜色就新增一筆
					Ingredient ingredient = HibernateUtil.queryIngredientIdByDishAndIngredientname(this.dbSession, dishId, igdbCurrentObject.getName());
					if (ingredient == null) {
						// 如果食材不存在就新增食材
						//ingredient = HibernateUtil.saveIngredient(this.dbSession, dishId, igdbCurrentObject.getName(), igdbCurrentObject.getBrand(), supplier.getId().getSupplierId(), supplier.getCompanyId());
						//調用新方法 儲存製造商及產品名稱 add by ellis 20141119
						ingredient = HibernateUtil.saveIngredient(this.dbSession, dishId, igdbCurrentObject.getName(), igdbCurrentObject.getBrand(), supplier.getId().getSupplierId(), supplier.getCompanyId(),igdbCurrentObject.getProductName(),igdbCurrentObject.getManufacturer());
						
						log.debug("新增調味料到食材基本檔 :" + ingredient.getIngredientName());
					}

					int igdbAttr = CateringServiceUtil.getIngredientAttrVal(igdbCurrentObject.getIngredientAttribute());
					
					Ingredientbatchdata igdb = null;

					// Raymond 2014/05/12 先 查詢資料庫中有無此Ingredientbatchdata
					// key:batchdataId,dishId,ingredientId,stockDate,lotNumber
					igdb = ibDao.queryIngredientbatchdataUK(batchdataId, dishId, ingredient.getIngredientId(), igdbCurrentObject.getStockDate(), igdbCurrentObject.getIngredientLotNum(), supplier.getCompanyId(), igdbCurrentObject.getProductDate(),
							igdbCurrentObject.getValidDate());

					if (igdb != null) {
						// 修改
//						igdb = HibernateUtil.updateIngredientbatchdata(this.dbSession, igdb, ingredient.getIngredientId(), supplier.getId().getSupplierId(), igdbCurrentObject.getIngredientLotNum(), ingredient.getIngredientName(),
//								igdbCurrentObject.getBrand(), igdbCurrentObject.getStockDate(), igdbCurrentObject.getValidDate(), igdbCurrentObject.getProductDate(), igdbCurrentObject.getAuthenticateId(), igdbCurrentObject.getLabel(),
//								supplier.getCompanyId(), "", "");
						
						igdb = HibernateUtil.updateIngredientbatchdata(this.dbSession, igdb, ingredient.getIngredientId(), supplier.getId().getSupplierId(), igdbCurrentObject.getIngredientLotNum(), ingredient.getIngredientName(),
								igdbCurrentObject.getBrand(), igdbCurrentObject.getStockDate(), igdbCurrentObject.getValidDate(), igdbCurrentObject.getProductDate(), igdbCurrentObject.getAuthenticateId(), igdbCurrentObject.getLabel(),
								supplier.getCompanyId(), "", "",igdbAttr,igdbCurrentObject.getProductName(),igdbCurrentObject.getManufacturer(),igdbCurrentObject.getIngredientQuantity(),"");
					} else {
						// 新增(但新增期間會檢核是不是重複的食材 條件:食材名稱 供應商 批號 進貨日期)
//						igdb = HibernateUtil.saveIngredientbatchdata(this.dbSession, batchdataId, ingredient.getIngredientId(), supplier.getId().getSupplierId(), dishId, igdbCurrentObject.getIngredientLotNum(), ingredient.getIngredientName(),
//								igdbCurrentObject.getBrand(), igdbCurrentObject.getStockDate(), igdbCurrentObject.getValidDate(), igdbCurrentObject.getProductDate(), igdbCurrentObject.getAuthenticateId(), igdbCurrentObject.getLabel(),
//								supplier.getCompanyId(), "", "");
						
						igdb = HibernateUtil.saveIngredientbatchdata(this.dbSession, batchdataId, ingredient.getIngredientId(), supplier.getId().getSupplierId(), dishId, igdbCurrentObject.getIngredientLotNum(), ingredient.getIngredientName(),
								igdbCurrentObject.getBrand(), igdbCurrentObject.getStockDate(), igdbCurrentObject.getValidDate(), igdbCurrentObject.getProductDate(), igdbCurrentObject.getAuthenticateId(), igdbCurrentObject.getLabel(),
								supplier.getCompanyId(), "", "",igdbAttr,igdbCurrentObject.getProductName(),igdbCurrentObject.getManufacturer(),igdbCurrentObject.getIngredientQuantity(),"");
					}
					if (igdb != null) {
						log.debug("更新食材資料到BatchdataId:" + batchdataId);
						// Raymond 2014/05/12 新增IngredientBatchId到不刪除清單內
						ibdService.putModifiMap(batchdataId, dishId, igdb.getIngredientBatchId());
					}
					seasoningDetail+="IngredientBatchId:"+igdb.getIngredientBatchId()+",<br>";
					seasoningDetail+="DishId:"+igdb.getDishId()+",<br>";
					seasoningDetail+="IngredientId:"+igdb.getIngredientId()+",<br>";
					seasoningDetail+="食材名稱:"+igdb.getIngredientName()+",";
					seasoningDetail+="進貨日期:"+igdb.getStockDate()+",";
					seasoningDetail+="製造日期:"+igdb.getManufactureDate()+",";
					seasoningDetail+="有效日期:"+igdb.getExpirationDate()+",";
					seasoningDetail+="批號:"+igdb.getLotNumber()+",";
					seasoningDetail+="品牌:"+igdb.getBrand()+",";
					seasoningDetail+="產地:"+igdb.getOrigin()+",";
					seasoningDetail+="來源:"+igdb.getSource()+",";
					seasoningDetail+="SupplierId:"+igdb.getSupplierId()+",";
					seasoningDetail+="驗證標章:"+igdb.getSourceCertification()+",";
					seasoningDetail+="驗證號碼:"+igdb.getCertificationId()+",";
					seasoningDetail+="供應商統編:"+igdb.getSupplierCompanyId()+",";
					seasoningDetail+="供應商名稱:"+igdb.getSupplierName()+",";
					seasoningDetail+="品牌編號:"+igdb.getBrandNo()+",";
					seasoningDetail+="產品名稱:"+igdb.getProductName()+",";
					seasoningDetail+="製造商:"+igdb.getManufacturer()+",";
					seasoningDetail+="重量:"+igdb.getIngredientQuantity()+",";
					seasoningDetail+="單位:"+igdb.getIngredientUnit()+",";
					seasoningDetail+="食材屬性:"+igdb.getIngredientAttr();
					LogUtil.writeFileUploadLog(igdb.getBatchDataId().toString(), this.getUsername(), "更新調味料："+this.responseObj.getMsg()+seasoningDetail, igdb.getBatchDataId().toString(), "UI_Seasoning");
					
				}
			}

			// Raymond 2014/05/12 刪除沒在頁面上的食材
			ibdService.deleteUnmodifyRecord();

			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
			tx.commit();
		} catch (Exception e) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(e.getMessage());
			if (!tx.wasRolledBack()) {
				tx.rollback();
			}
		}
	}
}
