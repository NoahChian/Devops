package org.iii.ideas.catering_service.rest.api;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.code.CertTypeCode;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.IngredientbatchdataDAO;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.rest.bo.SchoolDataBO;
import org.iii.ideas.catering_service.service.IngredientBatchDataXlsService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.LogUtil;
import org.iii.ideas.catering_service.util.SchoolAndKitchenUtil;

public class UpdateIngredientDetailv2 extends AbstractApiInterface<UpdateIngredientDetailv2Request, UpdateIngredientDetailv2Response> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6849065756925474197L;

	@Override
	public void process() throws NamingException, ParseException {
		Long dishId = this.requestObj.getDishId();
		Integer kitchenId = this.getKitchenId();
		String menuDate = this.requestObj.getMenuDate();
		Transaction tx = this.dbSession.beginTransaction();
		double totalWeight = 0; // 總重量
		Map<Integer, Float> schoolWeightRateMap = new HashMap<>();// 學校重量分配比例
		try {
			List<Long> batchdataIdArray = new ArrayList<Long>();
			if (!this.isLogin()) {
				if (!tx.wasRolledBack()) {
					tx.rollback();
				}
				this.responseObj.setResStatus(-2);
				this.responseObj.setMsg("使用者未授權");
				return;
			}

			// 查出所有在ingredientbatchdata 的batchdataid
			List<Batchdata> batchdataList = HibernateUtil.queryBatchdataByMenuDateAndDishId(this.dbSession, kitchenId, menuDate, dishId);
			if (batchdataList != null && batchdataList.size() > 0) {
			} else {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("菜單批次檔查無對應資料! ");
				return;
			}
			
			// 檢查每一個菜單可允許的上傳時間 20140912 KC
			for (Batchdata bdata : batchdataList) {
				if (!HibernateUtil.isMenuUploadTime(this.dbSession, menuDate.replace("/", "-"), bdata.getSchoolId())) {
					SchoolDataBO schoolBo = SchoolAndKitchenUtil.querySchoolDataBySchoolId(this.dbSession, bdata.getSchoolId());
					String schoolName = "";
					if (schoolBo != null) {
						schoolName = schoolBo.getSchoolName();
					}
					this.responseObj.setResStatus(-1);
					this.responseObj.setMsg("已超過" + schoolName + " 的上傳時間限制! ");
					return;
				}
			}

			// 先清除源始的ingredientbatchdata 的資料(為新的資料做新增準備)
			List<Integer> updateIngredientbatchdata = new ArrayList<Integer>();
			Iterator<Batchdata> iteratorBatchData = batchdataList.iterator();
			while (iteratorBatchData.hasNext()) {
				Long batchdataId = iteratorBatchData.next().getBatchDataId();
				// HibernateUtil.deleteIngredientbatchdataByDishId(this.dbSession,
				// batchdataId, dishId);
				batchdataIdArray.add(batchdataId);
			}

			// 運用Excel上傳處理例外狀況的功能
			IngredientBatchDataXlsService ibdService = new IngredientBatchDataXlsService(dbSession);
			// 取得學校重量分配比例
			schoolWeightRateMap = ibdService.getSchoolWeightRate(batchdataList);

			// 抓出目前update 的所有ingredinet 資料
			List<IngredientObject> ingredientBatchdata = this.requestObj.getIngredients();
			Iterator<IngredientObject> iterator = ingredientBatchdata.iterator();
			log.debug("UpdateIngredientDetailv2 Size:" + ingredientBatchdata.size() + " date:" + menuDate + " dishId:" + dishId);
			// List 出所有前瑞回傳的食材
			// for (Long batchdataId : batchdataIdArray) {
			for (Batchdata batchdata : batchdataList) {
				// while (iterator.hasNext()) {
				for (IngredientObject igdbCurrentObject : ingredientBatchdata) {
					// IngredientObject igdbCurrentObject = iterator.next();
					// 取得原有資料IngredientBatchId
					Long ibdId = (long) 0;
					if (!igdbCurrentObject.getIngredientBatchId().isEmpty())
						ibdId = Long.valueOf(igdbCurrentObject.getIngredientBatchId());

					// 找出目前資料的供應商資料
					Supplier supplier = HibernateUtil.getSupplier(this.dbSession, kitchenId, igdbCurrentObject.getSupplyName());
					if (supplier == null) {
						this.responseObj.setMsg("找不到供應商名稱:" + igdbCurrentObject.getSupplyName());
						this.responseObj.setResStatus(0);
						if (!tx.wasRolledBack()) {
							tx.rollback();
						}
						return;
					}
					// ---------依dishId 找出食材資訊---------
					Ingredient ingredient = null;
					ingredient = HibernateUtil.queryIngredientByName(this.dbSession, dishId, igdbCurrentObject.getName());
					// 如果相同食材相同就採用更新
					// 更新時寫入製造商與產品名稱 20141027 ellis
					if (ingredient != null) {
						this.dbSession.evict(ingredient);
						ingredient.setBrand(igdbCurrentObject.getBrand());
						ingredient.setIngredientName(igdbCurrentObject.getName());
						ingredient.setSupplierId(supplier.getId().getSupplierId());
						ingredient.setSupplierCompanyId(supplier.getCompanyId() == null ? "" : supplier.getCompanyId());
						ingredient.setManufacturer(igdbCurrentObject.getManufacturer() == null ? "" : igdbCurrentObject.getManufacturer());
						ingredient.setProductName(igdbCurrentObject.getProductName() == null ? "" : igdbCurrentObject.getProductName());
						
						this.dbSession.update(ingredient);
					} else {
						ingredient = HibernateUtil.getIngredient(this.dbSession, dishId, igdbCurrentObject.getName(), supplier.getId().getSupplierId());
						if (ingredient == null) {
							// 如果食材不存在就新增食材
							ingredient = new Ingredient();
							//調用新saveIngredient，傳入製造商與產品名稱 20141027 ellis
							ingredient = HibernateUtil.saveIngredient(this.dbSession, dishId, igdbCurrentObject.getName(), igdbCurrentObject.getBrand(), supplier.getId().getSupplierId(), supplier.getCompanyId(),igdbCurrentObject.getProductName(),igdbCurrentObject.getManufacturer());
						}
					}

					// 判斷是否為CAS肉類,如果是重量必須為必填
					if (igdbCurrentObject.getLabel().equals(CertTypeCode.CAS)) {
						if (CateringServiceUtil.isMeat(igdbCurrentObject.getName())) {
							// 這裡邏輯為,如果產品為CAS肉類,重量就會為必填欄位,目前邏輯先留著先不分配
						}
					}

					// 分算食材重量
					if (!CateringServiceUtil.isEmpty(igdbCurrentObject.getIngredientQuantity())) {
						totalWeight = Double.parseDouble(igdbCurrentObject.getIngredientQuantity());
//						totalWeight = Float.parseFloat(igdbCurrentObject.getIngredientQuantity()); Joshua edit 2014/10/17
					} else {
						totalWeight = 0;
					}

					DecimalFormat df = new DecimalFormat("##.00");
					float rate = schoolWeightRateMap.get(batchdata.getSchoolId());
					double schoolQuantity = rate * totalWeight;
					String schoolQty = schoolQuantity != 0 ? df.format(schoolQuantity) : "0" ;

					// 處理頁面上IngredientBatch(新增/修改)
					Ingredientbatchdata igdb = null;
					IngredientbatchdataDAO ibDao = new IngredientbatchdataDAO(dbSession);
					String ingredientDetail = "";
					// Raymond 2014/05/12 先 查詢資料庫中有無此Ingredientbatchdata
					// key:batchdataId,dishId,ingredientId,stockDate,lotNumber
					igdb = ibDao.queryIngredientbatchdataUK(batchdata.getBatchDataId(), dishId, ingredient.getIngredientId(), igdbCurrentObject.getStockDate(), igdbCurrentObject.getIngredientLotNum(), supplier.getCompanyId(), igdbCurrentObject.getProductDate(),
							igdbCurrentObject.getValidDate());

					int igdbAttr = CateringServiceUtil.getIngredientAttrVal(igdbCurrentObject.getIngredientAttribute());

					// Raymond 2014/05/12 mark 用頁面上是否有傳進IngredientBatchId
					// 來判定是否為修改
					if (igdb != null) {
						// 修改
						// igdb =
						// HibernateUtil.updateIngredientbatchdata(this.dbSession,
						// igdb,ingredient.getIngredientId(),
						// supplier.getId().getSupplierId(),
						// igdbCurrentObject.getIngredientLotNum(),
						// ingredient.getIngredientName(),
						// igdbCurrentObject.getBrand(),
						// igdbCurrentObject.getStockDate(),
						// igdbCurrentObject.getValidDate(),
						// igdbCurrentObject.getProductDate(),
						// igdbCurrentObject.getAuthenticateId(),
						// igdbCurrentObject.getLabel(),
						// supplier.getCompanyId(), "", "");

						igdb = HibernateUtil.updateIngredientbatchdata(this.dbSession, igdb, ingredient.getIngredientId(), supplier.getId().getSupplierId(), igdbCurrentObject.getIngredientLotNum(), ingredient.getIngredientName(),
								igdbCurrentObject.getBrand(), igdbCurrentObject.getStockDate(), igdbCurrentObject.getValidDate(), igdbCurrentObject.getProductDate(), igdbCurrentObject.getAuthenticateId(), igdbCurrentObject.getLabel(),
								supplier.getCompanyId(), "", "", igdbAttr, igdbCurrentObject.getProductName(), igdbCurrentObject.getManufacturer(), schoolQty, "");
					} else {
						// 新增(但新增期間會檢核是不是重複的食材 條件:食材名稱 供應商 批號 進貨日期)
						// igdb =
						// HibernateUtil.saveIngredientbatchdata(this.dbSession,
						// batchdataId, ingredient.getIngredientId(),
						// supplier.getId().getSupplierId(), dishId,
						// igdbCurrentObject.getIngredientLotNum(),
						// ingredient.getIngredientName(),
						// igdbCurrentObject.getBrand(),
						// igdbCurrentObject.getStockDate(),
						// igdbCurrentObject.getValidDate(),
						// igdbCurrentObject.getProductDate(),
						// igdbCurrentObject.getAuthenticateId(),
						// igdbCurrentObject.getLabel(),
						// supplier.getCompanyId(), "", "");

						igdb = HibernateUtil.saveIngredientbatchdata(this.dbSession, batchdata.getBatchDataId(), ingredient.getIngredientId(), supplier.getId().getSupplierId(), dishId, igdbCurrentObject.getIngredientLotNum(), ingredient.getIngredientName(),
								igdbCurrentObject.getBrand(), igdbCurrentObject.getStockDate(), igdbCurrentObject.getValidDate(), igdbCurrentObject.getProductDate(), igdbCurrentObject.getAuthenticateId(), igdbCurrentObject.getLabel(),
								supplier.getCompanyId(), "", "", igdbAttr, igdbCurrentObject.getProductName(), igdbCurrentObject.getManufacturer(), schoolQty, "");
					}
					if (igdb != null) {
						log.debug("更新食材資料到BatchdataId:" + batchdata.getBatchDataId());
						// modifyIbId.add(igdb.getIngredientBatchId());
						// Raymond 2014/05/12 新增IngredientBatchId到不刪除清單內
						ibdService.putModifiMap(batchdata.getBatchDataId(), dishId, igdb.getIngredientBatchId());
					}
					ingredientDetail+="IngredientBatchId:"+igdb.getIngredientBatchId()+",<br>";
					ingredientDetail+="DishId:"+igdb.getDishId()+",<br>";
					ingredientDetail+="IngredientId:"+igdb.getIngredientId()+",<br>";
					ingredientDetail+="食材名稱:"+igdb.getIngredientName()+",";
					ingredientDetail+="進貨日期:"+igdb.getStockDate()+",";
					ingredientDetail+="製造日期:"+igdb.getManufactureDate()+",";
					ingredientDetail+="有效日期:"+igdb.getExpirationDate()+",";
					ingredientDetail+="批號:"+igdb.getLotNumber()+",";
					ingredientDetail+="品牌:"+igdb.getBrand()+",";
					ingredientDetail+="產地:"+igdb.getOrigin()+",";
					ingredientDetail+="來源:"+igdb.getSource()+",";
					ingredientDetail+="SupplierId:"+igdb.getSupplierId()+",";
					ingredientDetail+="驗證標章:"+igdb.getSourceCertification()+",";
					ingredientDetail+="驗證號碼:"+igdb.getCertificationId()+",";
					ingredientDetail+="供應商統編:"+igdb.getSupplierCompanyId()+",";
					ingredientDetail+="供應商名稱:"+igdb.getSupplierName()+",";
					ingredientDetail+="品牌編號:"+igdb.getBrandNo()+",";
					ingredientDetail+="產品名稱:"+igdb.getProductName()+",";
					ingredientDetail+="製造商:"+igdb.getManufacturer()+",";
					ingredientDetail+="重量:"+igdb.getIngredientQuantity()+",";
					ingredientDetail+="單位:"+igdb.getIngredientUnit()+",";
					ingredientDetail+="食材屬性:"+igdb.getIngredientAttr();
					LogUtil.writeFileUploadLog(igdb.getBatchDataId().toString(), this.getUsername(), "更新食材："+this.responseObj.getMsg()+ingredientDetail, igdb.getBatchDataId().toString(), "UI_Ingredient");
					
				}

				// Raymond 2014/05/12 mark 刪除食材(依據不刪除list:modifyIbId 之外的食材)
				// if (modifyIbId.size() > 0 && batchdataId != 0 && dishId !=
				// null) {
				// HibernateUtil.removeOtherIngredientbatchdataByIdList(this.dbSession,
				// modifyIbId, batchdataId, dishId);
				// }
			}

			// Raymond 2014/05/12 刪除沒在頁面上的食材
			ibdService.deleteUnmodifyRecord();
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
			if (!tx.wasCommitted()) {
				tx.commit();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(e.getMessage());
			if (!tx.wasRolledBack()) {
				tx.rollback();
			}
		}
	}
}
