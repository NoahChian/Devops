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
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.service.IngredientService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class QuerySeasoningDetailv2 extends AbstractApiInterface<QuerySeasoningDetailv2Request, QuerySeasoningDetailv2Response> {

	@Override
	public void process() throws NamingException, ParseException {

		// -------------------
		List<String> labelOption = new ArrayList<String>();
		// this.responseObj.setIngredientsCount(results.size());
		labelOption.add("");
		//labelOption.add("吉園圃");
		//labelOption.add("產銷履歷");
		//labelOption.add("有機");
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
		//更新認證標章清單20140903
		// -------------------

		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		List<Integer> batchdataIdArray = new ArrayList<Integer>();
		//Dish dish = new Dish();
		String menuDate = this.requestObj.getMenuDate();
		if (menuDate == null || menuDate.equals("")) {
			this.responseObj.setResStatus(-1);
			this.responseObj.setMsg("請選擇菜單日期");
			return;
		}
		menuDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", menuDate));
		Integer kitchenId = this.getKitchenId();
		String dishName = CateringServiceUtil.ColumnNameOfSeasoning;
		/*
		dish = HibernateUtil.queryDishByName(this.dbSession, kitchenId, dishName);
		
		if (dish == null) {
			// 如果沒有調味料就自動新增一筆
			dish = new Dish();
			dish.setDishName(dishName);
			dish.setKitchenId(kitchenId);
			dish.setPicturePath("");
			this.dbSession.save(dish);
		}
		Long dishId = dish.getDishId();
		 */
		// 如果在batchdata 中找不到資料就return 0 筆
		List<Batchdata> batchdataList = HibernateUtil.queryBatchdataByMenuDate(this.dbSession, kitchenId, menuDate);
		if (batchdataList.size() == 0) {
			this.responseObj.setSeasoningsCount(0);
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("本日無菜單資料!");
			return;
		}
		// 如果有資料就以第一筆為代表

		Long firstBatchdataId = batchdataList.get(0).getBatchDataId();
		
		/** 更改單日調味料維護之取值方法，
		 *  原本會先從Dish中抓出調味料的id再到DishBatchData中將屬於調味料之id撈出來，
		 *  新方法則直接從DishBatchData中直接撈出調味料id。
		 * 20141119 add by Ellis
		 * */ 
		Long dishId = HibernateUtil.queryDishIdfromBatchDataByBatchdataIdAndDishName(this.dbSession, firstBatchdataId, dishName);
		
		for (Batchdata batchdata : batchdataList) {
			Ingredientbatchdata ingredientbatchdata = HibernateUtil.queryIngredientbatchdataByBatchdataIdAndDish(this.dbSession, batchdata.getBatchDataId(), dishId);
			if (ingredientbatchdata != null) {
				firstBatchdataId = ingredientbatchdata.getBatchDataId();
				break;
			}
		}
		
				
		// Integer firstBatchdataId = batchdataList.get(0).getBatchDataId();
		Batchdata firstBatchdata = HibernateUtil.queryBatchdataById(this.dbSession, firstBatchdataId);

		String sqlStatement = "from Batchdata b,Ingredientbatchdata i " + " where b.batchDataId = i.batchDataId " + "and   b.batchDataId = :batchdataId " + "and   i.dishId = :dishId ";

		// 20140521 Raymond 先排除台北市資料,目前台北市是已SOAP方式把資料傳入,系統預設SOAP傳入srcType='A'
		sqlStatement += "AND (b.srcType is null or b.srcType = 'A') ";

		Query query = dbSession.createQuery(sqlStatement);
		query.setParameter("dishId", dishId);
		query.setParameter("batchdataId", firstBatchdataId);
		log.debug("QuerySeasoningDetailv2 dishId:" + dishId + " batchdataId:" + firstBatchdataId);

		List results = query.list();
		// -------------------
		this.responseObj.setSeasoningsCount(results.size());
		// 查出所有供應商選項
		// List<String> supplyNameOption = new ArrayList<String>();
		// Criteria criteriaSP = dbSession.createCriteria(Supplier.class).add(
		// Restrictions.eq("id.kitchenId", kitchenId));
		// List Suppliers = criteriaSP.list();
		// Iterator<Supplier> iteratorSP = Suppliers.iterator();
		// while (iteratorSP.hasNext()) {
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

		// 把所有ingredientbatchdata 放入xml中
		Iterator<Object[]> iterator = results.iterator();
		log.debug("QuerySeasoningDetailv2 Size:" + results.size());
		System.out.println("QuerySeasoningDetailv2 Size:" + results.size() + " kid:" + kitchenId + " SchoolId:" + batchdataList.get(0).getSchoolId() + " BatchdataId:" + firstBatchdataId);
		while (iterator.hasNext()) {
			SupplierAndMenuDateObject sam = new SupplierAndMenuDateObject();
			Object[] obj = iterator.next();
			Batchdata batchdata = (Batchdata) obj[0];
			Ingredientbatchdata igdb = (Ingredientbatchdata) obj[1];
			Kitchen kitchen = HibernateUtil.queryKitchenById(this.dbSession, kitchenId);
			Supplier supplier = HibernateUtil.querySupplierById(this.dbSession, igdb.getSupplierId());
			IngredientObject igdObj = new IngredientObject();
			igdObj.setAuthenticateId(igdb.getCertificationId());
			igdObj.setBrand(igdb.getBrand());
			igdObj.setIngredientBatchId(kitchen.getHaccp());
			//20140722 Raymond 新增回傳欄位  ingredientId supplyCompanyId
			igdObj.setIngredientId(String.valueOf(igdb.getIngredientId()));
			igdObj.setSupplierCompanyId(igdb.getSupplierCompanyId());
			//20140722查出食材屬性資訊
			if(igdb.getIngredientAttr()!=null)
				igdObj.setIngredientAttribute(CateringServiceUtil.getIngredientAttrBo(igdb.getIngredientAttr()));
			else
				igdObj.setIngredientAttribute(new IngredientAttributeBO());
			
			igdObj.setProductName(CateringServiceUtil.isEmpty(igdb.getProductName()) ? "" : igdb.getProductName());
			igdObj.setIngredientQuantity(CateringServiceUtil.isEmpty(igdb.getIngredientQuantity()) ? "" : igdb.getIngredientQuantity());
			igdObj.setIngredientUnit(CateringServiceUtil.isEmpty(igdb.getIngredientUnit()) ? "" : igdb.getIngredientUnit());
			igdObj.setManufacturer(CateringServiceUtil.isEmpty(igdb.getManufacturer()) ? "" : igdb.getManufacturer());
			
			// igdObj.setIsReport("0"); // 問昌港 數值那裡來
			// 2014/05/15Raymond
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

			igdObj.setLabel(igdb.getSourceCertification());
			igdObj.setLabelOption(labelOption);
			igdObj.setName(igdb.getIngredientName());
			// igdObj.setLotNum(igdb.getLotNumber());
			igdObj.setIngredientLotNum(igdb.getLotNumber());
			igdObj.setProductDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getManufactureDate()));
			// igdObj.setReportFileName(igdb.getSourceCertification());//2014/05/15Raymond
			igdObj.setStockDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getStockDate()));
			if (supplier == null) {
				igdObj.setSupplyName("");
			} else {
				igdObj.setSupplyName(supplier.getSupplierName());
			}
			igdObj.setIngredientBatchId(String.valueOf(igdb.getIngredientBatchId()));
			igdObj.setSupplierOption(supplierOption);
			igdObj.setValidDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getExpirationDate()));
			this.responseObj.getSeasonings().add(igdObj);
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
