package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.IngredientbatchdataDAO;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.service.IngredientService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class QueryIngredientDetailv2 extends AbstractApiInterface<QueryIngredientDetailv2Request, QueryIngredientDetailv2Response> {

	@Override
	public void process() throws NamingException, ParseException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		String menuDate = this.requestObj.getMenuDate();
		menuDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", menuDate));
		Long dishId = this.requestObj.getDishId();
		Integer kitchenId = this.getKitchenId();
		// 如果在batchdata 中找不到資料就return 0 筆
		/*
		 * List<Batchdata> batchdataList =
		 * HibernateUtil.queryBatchdataByMenuDateAndDishId(this.dbSession,
		 * kitchenId, menuDate, dishId); if(batchdataList.size() == 0){
		 * this.responseObj.setIngredientsCount(0);
		 * this.responseObj.setResStatus(1); this.responseObj.setMsg("");
		 * System.out.println("--------*****--------dishId>"+ dishId); return; }
		 * 
		 * //Integer firstBatchdataId = batchdataList.get(0).getBatchDataId();
		 * // 如果有資料就以第一筆為代表 Integer firstBatchdataId =
		 * batchdataList.get(0).getBatchDataId(); for (Batchdata batchdata :
		 * batchdataList) { Ingredientbatchdata ingredientbatchdata =
		 * HibernateUtil
		 * .queryIngredientbatchdataByBatchdataIdAndDish(this.dbSession,
		 * batchdata.getBatchDataId(), dishId); if(ingredientbatchdata!=null){
		 * firstBatchdataId = ingredientbatchdata.getBatchDataId();
		 * System.out.println("---------------->"+
		 * ingredientbatchdata.getBatchDataId()); break; } }
		 */
		// 如果在batchdata 中找不到資料就return 0 筆
		List<Batchdata> batchdataList = HibernateUtil.queryBatchdataByMenuDate(this.dbSession, kitchenId, menuDate);
		if (batchdataList.size() == 0) {
			this.responseObj.setIngredientsCount(0);
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
			return;
		}

		// 20141002 Raymond 儲存當日所有的BatchdataId
		List<Long> batchdataIdArray = new ArrayList<Long>();
		Iterator<Batchdata> iteratorBatchData = batchdataList.iterator();
		while (iteratorBatchData.hasNext()) {
			Long batchdataId = iteratorBatchData.next().getBatchDataId();
			batchdataIdArray.add(batchdataId);
		}

		// //20140526 Raymond 建立 batchdata id list
		// List<Integer> batchdataIdList = new ArrayList<Integer>();
		// for (Batchdata batchdata : batchdataList) {
		// batchdataIdList.add(batchdata.getBatchDataId());
		// }
		//
		// IngredientbatchdataDAO igdbDao = new
		// IngredientbatchdataDAO(dbSession);
		// List<Ingredientbatchdata> igdbList =
		// igdbDao.queryModifyIngredientbatchdataList(dishId, batchdataIdList);
		//
		// if(igdbList.size() == 0){
		// this.responseObj.setIngredientsCount(0);
		// this.responseObj.setResStatus(1);
		// this.responseObj.setMsg("");
		// return;
		// }

		// 如果有資料就以第一筆為代表
		Long firstBatchdataId = batchdataList.get(0).getBatchDataId();
		for (Batchdata batchdata : batchdataList) {
			if (batchdata.getMainFoodId() == 0) {
				continue;
			}// 20140521 Raymond 先排除台北市資料,目前台北市是用SOAP方式把資料傳入
			Ingredientbatchdata ingredientbatchdata = HibernateUtil.queryIngredientbatchdataByBatchdataIdAndDish(this.dbSession, batchdata.getBatchDataId(), dishId);
			if (ingredientbatchdata != null) {
				firstBatchdataId = ingredientbatchdata.getBatchDataId();
				// System.out.println("******************************************************:"
				// + firstBatchdataId);
				break;
			}
		}

		Batchdata firstBatchdata = HibernateUtil.queryBatchdataById(this.dbSession, firstBatchdataId);

		String sqlStatement = "from Batchdata b,Ingredientbatchdata i " + " where b.batchDataId = i.batchDataId " + "and   b.batchDataId = :batchdataId " + "and   i.dishId = :dishId order by i.ingredientBatchId";
		Query query = dbSession.createQuery(sqlStatement);
		query.setParameter("dishId", dishId);
		query.setParameter("batchdataId", firstBatchdataId);

		// System.out.println("***********>" + firstBatchdataId + "  dishId:" +
		// dishId);

		List results = query.list();
		// -------------------
		List<String> labelOption = new ArrayList<String>();
		this.responseObj.setIngredientsCount(results.size());
		// this.responseObj.setIngredientsCount(igdbList.size());
		labelOption.add("");
		// labelOption.add("吉園圃");
		// labelOption.add("產銷履歷");
		// labelOption.add("有機");
		labelOption.add("CAS");
		labelOption.add("CAS_ORGANIC");
		labelOption.add("GAP");
		labelOption.add("GMP");
		labelOption.add("HALAL");
		labelOption.add("HACCP");
		labelOption.add("HEALTH");
		labelOption.add("ISO22000");
		labelOption.add("ISO9001");
		labelOption.add("MILK");
		labelOption.add("TAP");
		labelOption.add("SN"); //新增神農履歷 add by Ellis 20151218
		// 更新認證標章清單20140903
		// 查出所有供應商選項
		// 20140722 Raymond 改為回傳List<Map<供應商名稱,供應商統編>>
		// List<String> supplyNameOption = new ArrayList<String>();
		// Criteria criteriaSP = dbSession.createCriteria(Supplier.class).add(
		// Restrictions.eq("id.kitchenId", kitchenId ));
		// List Suppliers = criteriaSP.list();
		// Iterator<Supplier> iteratorSP = Suppliers.iterator();
		// while(iteratorSP.hasNext()){
		// Supplier sc = iteratorSP.next();
		// supplyNameOption.add(sc.getSupplierName());
		// }
		// 20140722 Raymond 改為回傳List<Map<供應商名稱,供應商統編>>
		List<Map<String, String>> supplierOption = new ArrayList<Map<String, String>>();
		Criteria criteriaSP = dbSession.createCriteria(Supplier.class).add(Restrictions.eq("id.kitchenId", kitchenId));
		List Suppliers = criteriaSP.list();
		Iterator<Supplier> iteratorSP = Suppliers.iterator();
		while (iteratorSP.hasNext()) {
			Supplier sc = iteratorSP.next();
			Map<String, String> option = new HashMap<String, String>();
			option.put("supplierName", sc.getSupplierName());
			option.put("supplierCompanyId", sc.getCompanyId());
			supplierOption.add(option);
		}

		IngredientbatchdataDAO ibdDao = new IngredientbatchdataDAO(dbSession);
		
		// 把所有ingredientbatchdata 放入xml中
		Iterator<Object[]> iterator = results.iterator();
		// Iterator<Ingredientbatchdata> iterator = igdbList.iterator();
		log.debug("QueryIngredientDetailv2 Size:" + results.size());
		while (iterator.hasNext()) {
			SupplierAndMenuDateObject sam = new SupplierAndMenuDateObject();
			// Ingredientbatchdata igdb = iterator.next();
			
			Object[] obj = iterator.next();
			Batchdata batchdata = (Batchdata) obj[0];
			Ingredientbatchdata igdb = (Ingredientbatchdata) obj[1];
			Kitchen kitchen = HibernateUtil.queryKitchenById(this.dbSession, kitchenId);
			Supplier supplier = HibernateUtil.querySupplierById(this.dbSession, igdb.getSupplierId());
			IngredientObject igdObj = new IngredientObject();
			
			//20141002 Raymond 查詢食材總重量
			String ingredientQuantity = ibdDao.getTotalIngredientQuantity(batchdataIdArray, dishId, igdb.getIngredientId()); // Joshua edit 2014/10/17 將ingredientQuantity型態從float改成String			
			
			igdObj.setAuthenticateId(igdb.getCertificationId());
			igdObj.setBrand(igdb.getBrand());
			// igdObj.setIngredientBatchId(kitchen.getHaccp());
			// igdObj.setIngredientBatchId(String.valueOf(igdb.getIngredientBatchId()));
			igdObj.setLabel(igdb.getSourceCertification());
			igdObj.setLabelOption(labelOption);
			igdObj.setName(igdb.getIngredientName());
			// igdObj.setLotNum(igdb.getLotNumber());
			igdObj.setIngredientLotNum(igdb.getLotNumber());
			igdObj.setProductDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getManufactureDate()));

			// 20140722 Raymond 新增回傳欄位 ingredientId supplyCompanyId
			igdObj.setIngredientId(String.valueOf(igdb.getIngredientId()));
			igdObj.setSupplierCompanyId(igdb.getSupplierCompanyId());
			// 20140722查出食材屬性資訊
			if (igdb.getIngredientAttr() != null)
				igdObj.setIngredientAttribute(CateringServiceUtil.getIngredientAttrBo(igdb.getIngredientAttr()));
			else
				igdObj.setIngredientAttribute(new IngredientAttributeBO());

			igdObj.setProductName(CateringServiceUtil.isEmpty(igdb.getProductName()) ? "" : igdb.getProductName());
//			igdObj.setIngredientQuantity(CateringServiceUtil.isEmpty(igdb.getIngredientQuantity()) ? "" : igdb.getIngredientQuantity());
			igdObj.setIngredientQuantity(String.valueOf(ingredientQuantity));
			igdObj.setIngredientUnit(CateringServiceUtil.isEmpty(igdb.getIngredientUnit()) ? "" : igdb.getIngredientUnit());
			igdObj.setManufacturer(CateringServiceUtil.isEmpty(igdb.getManufacturer()) ? "" : igdb.getManufacturer());
			// igdObj.setReportFileName(igdb.getSourceCertification()); //直接丟給UI
			// download link
			// igdObj.setIsReport( "0"); //問昌港 數值那裡來 >>Ric改寫
			// 去資料夾裡面檢察這個kid,ingredientBatchDataId有沒有對應的PDF,JPG檔案
			try {
				// 20140722 Raymond 改用uploadfile table去判斷是否有檔案

				// if(CateringServiceUtil.fileExists(CateringServiceUtil.getInspectionFileName(kitchenId,
				// igdb.getIngredientBatchId(), "pdf"))){
				// igdObj.setIsReport("1");
				// igdObj.setReportFileName("<a target='_blank' href='../../file/SHOW/inspect|"+kitchenId+"|"+igdb.getIngredientBatchId()+"'>檢驗報告</a>");
				// }else
				// if(CateringServiceUtil.fileExists(CateringServiceUtil.getInspectionFileName(kitchenId,
				// igdb.getIngredientBatchId(), "jpg"))){
				// igdObj.setIsReport("1");
				// igdObj.setReportFileName("<a target='_blank' href='../../file/SHOW/inspect|"+kitchenId+"|"+igdb.getIngredientBatchId()+"'>檢驗報告</a>");
				// }else{
				// igdObj.setIsReport("0");
				// igdObj.setReportFileName("");
				// }

				IngredientService ingredientService = new IngredientService(dbSession);
				String downloadPath = ingredientService.getIngredientInspectionDownloadPath(String.valueOf(igdb.getIngredientBatchId()));
				if (!CateringServiceUtil.isEmpty(downloadPath)) {
					igdObj.setIsReport("1");
					igdObj.setReportFileName(downloadPath);
				} else {
					igdObj.setIsReport("0");
					igdObj.setReportFileName("");
				}

			} catch (Exception e) {
				igdObj.setIsReport("0");
				igdObj.setReportFileName("");
				e.printStackTrace();
			}

			igdObj.setStockDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getStockDate()));
			if (supplier == null) {
				igdObj.setSupplyName("");
			} else {
				igdObj.setSupplyName(supplier.getSupplierName());
			}
			igdObj.setIngredientBatchId(String.valueOf(igdb.getIngredientBatchId()));
			igdObj.setSupplierOption(supplierOption);
			igdObj.setValidDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getExpirationDate()));
			this.responseObj.getIngredients().add(igdObj);
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}
}
