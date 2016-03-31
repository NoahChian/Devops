package org.iii.ideas.catering_service.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.iii.ideas.catering_service.code.CertTypeCode;
import org.iii.ideas.catering_service.code.XlsTitleCode;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.BatchdataDAO;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.DishBatchData;
import org.iii.ideas.catering_service.dao.DishBatchDataDAO;
import org.iii.ideas.catering_service.dao.DishDAO;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.IngredientbatchdataDAO;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.SchoolkitchenDAO;
import org.iii.ideas.catering_service.dao.SeasoningStockData;
import org.iii.ideas.catering_service.dao.SeasoningStockDataDAO;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.SupplierDAO;
import org.iii.ideas.catering_service.file.ExcelParser;
import org.iii.ideas.catering_service.rest.bo.IngredientBatchDataBO;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.DateUtil;
import org.iii.ideas.catering_service.util.ExcelUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.LogUtil;

/**
 * IngredientBatchDataService 處理食材上傳檔案(Excel)
 * 
 * @author Raymond 2013/05/12
 * @version 1.0
 * 
 */
public class IngredientBatchDataXlsService {
	protected Logger log = Logger.getLogger(this.getClass());
	private Session dbSession;
	private Integer kitchenId;
	// 暫存Dish
	private Map<String, Dish> cacheDish = new HashMap<String, Dish>();// <dishName,Dish>
	// 暫存School
	private Map<String, School> cacheSchool = new HashMap<String, School>();// <schoolName,School>
	// 暫存Supplier
	private Map<String, Supplier> cacheSupplier = new HashMap<String, Supplier>();// <companyId,Supplier>
	// 暫存Ingredient
	private Map<String, Ingredient> cacheIngredient = new HashMap<String, Ingredient>();// <dishId_ingredientName,Ingredient>
	// 已處理清單
	private Map<Long, Map<Long, List<Long>>> modifyMap = new HashMap<Long, Map<Long, List<Long>>>();// <Map<BatchId,Map<DishId,List<IngredientBatchdataId>>>
	// 暫存BatchDataList
	private Map<String, List<Batchdata>> cacheBatchDataList = new HashMap<String, List<Batchdata>>();// Key=KitchenId_MenuDate_schoold(如果有schoolId才會有,如為存廚房當日所有學校菜單Key=KitchenId_MenuDate)
	// 成功筆數
	private int success = 0;
	// 失敗筆數
	private int fail = 0;
	// 20140724是否為新版excel
	private boolean isVer2 = false;
	
	// 20141126是否為新版excel (供應商&學校下拉式選單)
	private boolean isVer3 = false;
	
	// 20150826 是否為新版excel (認證標章 >> 驗證標章 )
		private boolean isVer4 = false;

	// 此次新增的batchdataid
	private ArrayList<String> batchdataIdList = null;
	
	private boolean hasSeasoning = false;

	public IngredientBatchDataXlsService() {
	}

	public IngredientBatchDataXlsService(Session dbSession) {
		this.dbSession = dbSession;
	}

	/**
	 * 處理Excel食材上傳檔案資料
	 * 
	 * @param fileName
	 * @param kitchenId
	 * @return
	 * @throws Exception
	 */
	public String uploadXlsFile(String fileName, Integer kitchenId) throws Exception {
		this.kitchenId = kitchenId;
		InputStream ExcelFileToRead = new FileInputStream(fileName);
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		String rowErrMsg = "";
		List<String> xlsTitle = new ArrayList<String>();

		String currenTimeString = DateUtil.getSystemDate("yyyy/MM/dd HH:mm:ss");

		Iterator<Row> rows = sheet.rowIterator();
		// 開始處理Excel row data
		while (rows.hasNext()) {
			row = (XSSFRow) rows.next();

			IngredientBatchDataBO bo = new IngredientBatchDataBO();
			if(isVer3 || isVer4){ //ver3 ver4 需先帶入kid 才可取供應商編號
				bo.setKitchenId(kitchenId);
			} 
			String cellErrMsg = "";
			int rowNum = row.getRowNum() + 1; // 實際對應Excel的行數
			// 將excel欄位塞入IngredientBatchDataBO

			int emptyCell = 0;

			// 判斷有無Cell資料 及
			if (row.getRowNum() != 0) {
				if (row.getLastCellNum() <= 0)
					continue;
			}

			for (int i = 0; i < row.getLastCellNum(); i++) {
				XSSFCell cell = (XSSFCell) row.getCell(i, Row.RETURN_BLANK_AS_NULL);// 特別處理空值(blank)狀況
				String cellValue = ExcelUtil.getCellValue(cell);
				
				if(cellValue != null){
					if(CateringServiceUtil.isVaildStr(cellValue)){
						throw new Exception("上傳檔案第"+ rowNum +" 行 資料  "+ cellValue +"  包含特殊字元 \" > < ' % ; & 或者資料有斷行");
					}
				}
				
				if (row.getRowNum() == 0) {
					xlsTitle.add(cellValue);
				} else {
					if (i >= xlsTitle.size())
						continue;

					String titleName = xlsTitle.get(i);
//					String cellValue = ExcelUtil.getCellValue(cell);

					// 額外處理 供餐日期 菜色名稱 食材名稱 欄位判斷是否為空
					if (i == 0 || i == 2 || i == 3) {
						if (CateringServiceUtil.isEmpty(cellValue)) {
							emptyCell++;
						}
					}

					try {
						// 基本檢核欄位
						bo = validateXlxCellValue(bo, titleName, cellValue);
					} catch (Exception ex) {
						// 儲存Cell錯誤訊息
						cellErrMsg = putErrorMsg(cellErrMsg, ex.getMessage());
					}

				}
			}

			// 如果食材欄位 供餐日期 菜色名稱 食材名稱 為空 則跳過不處理
			if (emptyCell == 3)
				continue;

			// 額外邏輯檢核
			if (CateringServiceUtil.isEmpty(cellErrMsg)) {
				if (row.getRowNum() > 0) {
					// 20140814 Raymond 因DBsession 問題目前mark起來,待解決
					// cellErrMsg = putErrorMsg(cellErrMsg,
					// validateMenuUploadTime(bo)); // 檢核是否超過上傳日期
					// cellErrMsg = putErrorMsg(cellErrMsg,validateCASMeat(bo));
					// 檢核CAS肉類必填重量 目前先都回傳正確
					cellErrMsg = putErrorMsg(cellErrMsg, validateDateRole(bo)); // 檢核日期規則
					cellErrMsg = putErrorMsg(cellErrMsg, validateCertRole(bo)); // 檢核認證標章規則
				}
			}

			
			
			if (!CateringServiceUtil.isEmpty(cellErrMsg)) {
				rowErrMsg = putErrorMsg(rowErrMsg, "第:" + rowNum + "行 [" + cellErrMsg + "]</br>");
				fail++;
			} else {
				// 跳過第一筆表頭
				if (row.getRowNum() > 0) {
					try {
						// 開始寫入資料庫
						Ingredientbatchdata ingredientbatchdata = insertIngredientBatchdataFromXLS(bo);
						if (ingredientbatchdata != null) {
							addBatchdataIdList(ingredientbatchdata.getBatchDataId().toString());
							success++;
						} else {
							fail++;
							rowErrMsg = putErrorMsg(rowErrMsg, "第:" + rowNum + "行錯誤</br>");
						}

					} catch (Exception ex) {
						fail++;
						rowErrMsg = putErrorMsg(rowErrMsg, "第:" + rowNum + "行 [" + ex.getMessage() + "]</br>");
					}
				}
				
			}
		}
		//新版新增調味料
//		addNewSeasoningStcokData();
		// 刪除沒有被處理的食材
		deleteUnmodifyRecord();
		return rowErrMsg;
	}

	/**
	 * 新增Excel Row data 資訊
	 * 
	 * @param IngredientBatchDataBO
	 * @return
	 * @throws Exception
	 */
	public Ingredientbatchdata insertIngredientBatchdataFromXLS(IngredientBatchDataBO bo) throws Exception {
		Dish dish = null;
		School school = null;
		Supplier supplier = null;
		Ingredient ingredient = null;
		BatchdataDAO batchdataDao = new BatchdataDAO(dbSession);
		IngredientbatchdataDAO ibDao = new IngredientbatchdataDAO(dbSession);
		Ingredientbatchdata ingredientbatchdata = null;
		// 總重量
		float totalWeight = 0;

		// Source,Origin 先預設設為空
		bo.setOrigin("");
		bo.setSource("");

		// 取食材資訊
		dish = getDishByDishName(bo.getDishName());
		bo.setDishId(dish.getDishId());
		// 取供應商資訊
		// 由於ver3沒有統編欄位 因此要從供應商名稱去取編號出來
		if(isVer3 || isVer4){ 
			SupplierDAO sDAO = new SupplierDAO(dbSession);
			
			supplier = sDAO.querySupplierBySupplierName(bo.getSupplierName(), bo.getKitchenId());
			//supplier = HibernateUtil.getSupplier(dbsession, bo.getKitchenId(), ingredientName)
		}else{
			supplier = getSupplierByCompanyId(bo.getCompanyId());
			
		}
		if(!CateringServiceUtil.isNull(supplier)){
			bo.setSupplierId(supplier.getSupplierId());
			bo.setCompanyId(supplier.getCompanyId());
			bo.setSupplierName(supplier.getSupplierName());
		}else{
			throw new Exception("請先登打供應商資料："+bo.getSupplierName()); //ver3 若輸入無供應商清單內未擁有的供應商名稱，則提示錯誤。 modify by ellis 20150202
		}
		// 取學校資訊
		if (!CateringServiceUtil.isEmpty(bo.getSchoolName())) {
			school = getSchoolBySchoolName(bo.getSchoolName());
			bo.setSchoolId(school.getSchoolId());
		}

		// 取食材資訊
		ingredient = getIngredient(bo);
		bo.setIngredientId(ingredient.getIngredientId());

		// 取菜單資訊
		// 20140818 Raymond 把list 查詢結果存在Cache不再重複查詢菜單資訊
		List<Batchdata> batchdataList = getCacheBatchdataList(kitchenId, bo.getMenuDate(), bo.getSchoolId());
		// List<Batchdata> batchdataList = new ArrayList<Batchdata>();
		// if (school == null) {
		// batchdataList = batchdataDao.queryBatchdataList(kitchenId,
		// bo.getMenuDate());
		// } else {
		// batchdataList = batchdataDao.queryBatchdataList(kitchenId,
		// bo.getMenuDate(), bo.getSchoolId());
		// }

		// 檢核有無學校菜單
		if (batchdataList == null || batchdataList.size() == 0) {
			throw new Exception("查無此學校菜單資料  日期:" + bo.getMenuDate() + " 學校:" + bo.getSchoolName() + " 學校代號:" + bo.getSchoolId() + " 帳號ID:" + kitchenId);
		}

		String errMsg = "";

		Map<Integer, Float> schoolWeightRateMap = new HashMap<>();
		// 分算各學校分配的重量
		// 20141126 ver3保留ver2版重量分配功能
		if (isVer2 || isVer3) {
			if (!CateringServiceUtil.isEmpty(bo.getIngredientQuantity())) {
				totalWeight = Float.parseFloat(bo.getIngredientQuantity());
				schoolWeightRateMap = getSchoolWeightRate(batchdataList);
			}
		}

		// 開始處理有被需要被處理的菜單
		for (Batchdata batchdata : batchdataList) {
			// 取得此菜單中所有的菜色List
			List<Long> dishArray = HibernateUtil.getDishIdByBatchdata(batchdata);

			// 檢核要修改的菜色,是否有存在這筆菜單內
			if (!dishArray.contains(dish.getDishId()) && !CateringServiceUtil.ColumnNameOfSeasoning.equals(dish.getDishName())) {
				String schoolName = "";
				if (school == null)
					schoolName = HibernateUtil.querySchoolNameById(dbSession, batchdata.getSchoolId());
				else
					schoolName = school.getSchoolName();

				String errLog = "請確認此菜色[" + dish.getDishName() + "]存在於 學校:" + schoolName + " 日期:" + batchdata.getMenuDate() + " 菜單資料中!</br>";
				errMsg = LogUtil.putErrorMsg(errMsg, errLog);
				continue;
				// throw new Exception("請確認此菜色[" + dish.getDishName() +
				// "]存在於 學校:" + schoolName + " 日期:" + batchdata.getMenuDate() +
				// " 菜單資料中!</br>");
			}

			// 20140904 加入新增調味料至dishbatchdata
			if (CateringServiceUtil.ColumnNameOfSeasoning.equals(dish.getDishName())) {
				insertDishBatchData(batchdata.getBatchDataId(), dish.getDishId());
			}
			
			

			bo.setBatchdataId(batchdata.getBatchDataId());

			// 查詢資料庫中有無此Ingredientbatchdata
			ingredientbatchdata = ibDao.queryIngredientbatchdataUK(batchdata.getBatchDataId(), dish.getDishId(), ingredient.getIngredientId(), bo.getStockDate(), bo.getLotNumber(), supplier.getCompanyId(), bo.getManufactureDate(), bo.getExpirationDate());

			// 20140929 取得各學校分配重量,第二版EXCEL上傳才有的欄位
			// 20141126 ver3保留ver2版重量分配功能
			if (isVer2 || isVer3) {
				DecimalFormat df = new DecimalFormat("##.00");
				float rate = schoolWeightRateMap.get(batchdata.getSchoolId());
				float schoolQuantity = rate * totalWeight;
				bo.setIngredientQuantity(df.format(schoolQuantity));
			}

			// 新增或更新食材資料
			ingredientbatchdata = updateIngredientbatchdata(bo, ingredientbatchdata);
			
			
			// 紀錄被處理的食材	
			if (ingredientbatchdata != null)
				putModifiMap(ingredientbatchdata.getBatchDataId(), ingredientbatchdata.getDishId(), ingredientbatchdata.getIngredientBatchId());
		}
			
		
		if (!CateringServiceUtil.isEmpty(errMsg))
			throw new Exception(errMsg);

		return ingredientbatchdata;
	}

	/**
	 * 更新食材資訊
	 * 
	 * @param IngredientBatchDataBO
	 * @param Ingredientbatchdata
	 * @return Ingredientbatchdata
	 * @throws ParseException
	 */
	public Ingredientbatchdata updateIngredientbatchdata(IngredientBatchDataBO bo, Ingredientbatchdata igdb) throws ParseException {
		if (igdb == null) {
			igdb = new Ingredientbatchdata();
			igdb.setBatchDataId(bo.getBatchdataId());
			igdb.setDishId(bo.getDishId());
			igdb.setIngredientId(bo.getIngredientId());
			igdb.setSupplierId(bo.getSupplierId());
			igdb.setLotNumber(bo.getLotNumber());
			igdb.setStockDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", bo.getStockDate()));
		}
		igdb.setExpirationDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", bo.getExpirationDate()));
		igdb.setManufactureDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", bo.getManufactureDate()));
		igdb.setIngredientName(bo.getIngredientName());
		if (bo.getCertificationId() != null && bo.getSourceCertification() != null) {
			igdb.setCertificationId(bo.getCertificationId());
			igdb.setSourceCertification(bo.getSourceCertification());
		} else {
			igdb.setCertificationId("");
			igdb.setSourceCertification("");
		}

		int ingredientAttr = 0;
		if (bo.getIngredientAttribute() != null)
			ingredientAttr = CateringServiceUtil.getIngredientAttrVal(bo.getIngredientAttribute());

		igdb.setOrigin(bo.getOrigin());
		igdb.setSource(bo.getSource());
		igdb.setBrand(bo.getBrand() == null ? "" : bo.getBrand());
		igdb.setSupplierCompanyId(bo.getCompanyId());
		igdb.setSupplierName(bo.getSupplierName());
		igdb.setIngredientAttr(ingredientAttr);
		igdb.setProductName(bo.getProductName());
		igdb.setIngredientQuantity(bo.getIngredientQuantity());
		igdb.setManufacturer(bo.getManufacturer());
		igdb.setIngredientUnit("公斤");// 預設先給公斤
		dbSession.saveOrUpdate(igdb);

		return igdb;
	}

	/**
	 * 刪除暫存MAP之外的食材 暫存MAP中存的是有被新增/修改的食材
	 */
	public void deleteUnmodifyRecord() {
		for (Long batchdataId : modifyMap.keySet()) {
			Map<Long, List<Long>> modifyDishMap = modifyMap.get(batchdataId);
			for (Long dishId : modifyDishMap.keySet()) {
				List<Long> modifyIbList = modifyDishMap.get(dishId);
				if (modifyIbList != null && modifyIbList.size() > 0)
					HibernateUtil.removeOtherIngredientbatchdataByIdList(dbSession, modifyIbList, batchdataId, dishId);
			}
		}
	}

	/**
	 * 取菜色:先從cache map(cacheDish)查否之前已經先查過這個菜色,有的話就直接從map中把Dish取出,沒有的話才去資料庫query
	 * 
	 * @param dishName
	 * @return
	 * @throws Exception
	 */
	public Dish getDishByDishName(String dishName) throws Exception {
		Dish dish;

		if (cacheDish.containsKey(dishName)) {
			dish = cacheDish.get(dishName);
		} else {
			dish = HibernateUtil.queryDishByName(dbSession, kitchenId, dishName);
			if (dish != null) {
				cacheDish.put(dish.getDishName(), dish);
			} else {
				// 如果是缺少菜色=調味料 就直接新增一筆進菜色裡,但一般菜色找不到則回傳錯誤訊息
				if (dishName.equals(CateringServiceUtil.ColumnNameOfSeasoning)) {
					dish = new Dish();
					dish.setDishName(dishName);
					dish.setKitchenId(kitchenId);
					dish.setPicturePath("");
					dbSession.save(dish);
				} else {
					throw new Exception("請先登打菜色'" + dishName + "'資料!");
				}
			}
		}
		return dish;
	}

	/**
	 * 取供應商:先從cache
	 * map(cacheSupplier)查否之前已經先查過這個供應商,有的話就直接從map中把Supplier取出,沒有的話才去資料庫query
	 * 
	 * @param companyId
	 * @return
	 * @throws Exception
	 */
	public Supplier getSupplierByCompanyId(String companyId) throws Exception {
		Supplier supplier;
		if (cacheSupplier.containsKey(companyId)) {
			supplier = cacheSupplier.get(companyId);
		} else {
			supplier = HibernateUtil.querySupplierByCompanyId(dbSession, kitchenId, companyId);
			if (supplier == null) {
				throw new Exception("請先登打供應商資料  供應商統編:'" + companyId);
			}
			cacheSupplier.put(companyId, supplier);
		}
		return supplier;

	}

	/**
	 * 取供應商:先從cache
	 * map(cacheSchool)查否之前已經先查過這個學校,有的話就直接從map中把School取出,沒有的話才去資料庫query
	 * 
	 * @param schoolName
	 * @return
	 * @throws Exception
	 */
	public School getSchoolBySchoolName(String schoolName) throws Exception {
		School school;
		if (cacheSchool.containsKey(schoolName)) {
			school = cacheSchool.get(schoolName);
		} else {
			school = HibernateUtil.querySchoolByKitchenAndName(dbSession, kitchenId, schoolName);
			if (school == null) {
				throw new Exception("查無此學校資料請依學校名稱登打:'" + schoolName + " 請依[業者資料管理]之[學校名稱]輸入");
			}
			cacheSchool.put(schoolName, school);
		}
		return school;
	}

	/**
	 * 取供應商:先從cache map(cacheIngredient)查否之前已經先查過這個食材,有的話就直接從map中把Ingredient取出,
	 * 沒有的話才去資料庫query
	 * 
	 * @param IngredientBatchDataBO
	 * @return
	 */
	
	public Ingredient getIngredient(IngredientBatchDataBO bo) {
		Ingredient ingredient;
		String cacheKey = String.valueOf(bo.getDishId()) + "_" + String.valueOf(bo.getIngredientName());
		if (cacheIngredient.containsKey(cacheKey)) {
			ingredient = cacheIngredient.get(cacheKey);
		} else {
			ingredient = HibernateUtil.queryIngredientByName(dbSession, bo.getDishId(), bo.getIngredientName());
			// 若資料庫中無資料,就幫他新增一筆食材
			if (ingredient == null) {
				ingredient = new Ingredient();
				// ingredient.setIngredientId(0);
				ingredient.setBrand(bo.getBrand() == null ? "" : bo.getBrand());
				ingredient.setDishId(bo.getDishId());
				ingredient.setIngredientName(bo.getIngredientName());
				ingredient.setSupplierId(bo.getSupplierId());
				ingredient.setSupplierCompanyId(bo.getCompanyId());
				//增加製造商與產品名稱欄位填入 20141028 ellis
				ingredient.setManufacturer(bo.getManufacturer() == null ? "" : bo.getManufacturer());
				ingredient.setProductName(bo.getProductName() == null ? "" : bo.getProductName());
				dbSession.save(ingredient);
			}

			cacheIngredient.put(cacheKey, ingredient);
		}
		return ingredient;
	}

	/**
	 * 檢核認證資訊規則
	 * 
	 * @param bo
	 * @return
	 * @throws ParseException
	 */
	public String validateCertRole(IngredientBatchDataBO bo) throws ParseException {
		String errMsg = "";
		if (!CateringServiceUtil.isEmpty(bo.getSourceCertification())) {
			if (CateringServiceUtil.isEmpty(bo.getCertificationId())) {
				errMsg += "認證編號不可為空";
			}
		}

		if (!CateringServiceUtil.isEmpty(bo.getCertificationId())) {
			if (CateringServiceUtil.isEmpty(bo.getSourceCertification())) {
				errMsg += "食材認證標章不可為空";
			}
		}

		return errMsg;
	}

	/**
	 * 檢核時間規則
	 * 
	 * @param bo
	 * @return
	 * @throws ParseException
	 */
	public String validateDateRole(IngredientBatchDataBO bo) throws ParseException {
		String errMsg = "";

		// 預防空值錯誤發生
		if (bo.getMenuDate() == null || bo.getStockDate() == null)
			return "";

		// 供餐日期
		Timestamp menuDate = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", bo.getMenuDate());
		// 進貨日期
		Timestamp stockDate = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", bo.getStockDate());
		// 生產日期
		Timestamp manufactureDate = null;
		// 有效日期
		Timestamp expirationDate = null;

		if (stockDate != null) {
			if (stockDate.after(menuDate)) {
				errMsg = putErrorMsg(errMsg, "進貨日期不可大於供餐日期");
			}
		}

		if (!CateringServiceUtil.isEmpty(bo.getManufactureDate())) {
			manufactureDate = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", bo.getManufactureDate());
			if (manufactureDate.after(menuDate))
				errMsg = putErrorMsg(errMsg, "生產日期不可大於供餐日期");

			if (stockDate != null) {
				if (manufactureDate.after(stockDate))
					errMsg = putErrorMsg(errMsg, "生產日期不可大於進貨日期");
			}
		}

		if (!CateringServiceUtil.isEmpty(bo.getExpirationDate())) {
			expirationDate = HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", bo.getExpirationDate());
			if (expirationDate.before(menuDate))
				errMsg = putErrorMsg(errMsg, "有效日期不可小於供餐日期");

			if (stockDate != null) {
				if (expirationDate.before(stockDate))
					errMsg = putErrorMsg(errMsg, "有效日期不可小於進貨日期");
			}
		}

		if (manufactureDate != null && expirationDate != null) {
			if (expirationDate.before(manufactureDate))
				errMsg = putErrorMsg(errMsg, "有效日期不可小於生產日期");
		}

		return errMsg;
	}

	/**
	 * 檢核Excel欄位
	 * 
	 * @param bo
	 * @param titleName
	 * @param cellValue
	 * @return
	 * @throws Exception
	 */
	public IngredientBatchDataBO validateXlxCellValue(IngredientBatchDataBO bo, String titleName, String cellValue) throws Exception {
		String errMsg = "";

		if (cellValue != null)
			cellValue = cellValue.trim();
		switch (titleName.trim()) {
		case XlsTitleCode.INGREDIENT_MENUDATE:
			if (cellValue != null)
				cellValue = cellValue.replace("-", "/");
			errMsg = checkMenuDate(cellValue);
			if (CateringServiceUtil.isEmpty(errMsg)) {
				Timestamp dateTS;
				dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", cellValue.trim());
				String menuDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", dateTS);
				bo.setMenuDate(menuDate);
			} else {
				bo.setMenuDate(cellValue);
			}

			break;
		case XlsTitleCode.INGREDIENT_SCHOOLNAME:
			bo.setSchoolName(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_DISHNAME:
			if (CateringServiceUtil.isEmpty(cellValue))
				errMsg = "菜色名稱不可為空";
			else {
				if (cellValue.equals("調味料"))
					hasSeasoning = true;
			}
			bo.setDishName(cellValue);

			break;
		case XlsTitleCode.INGREDIENT_INGREDIENTNAME:
			if (CateringServiceUtil.isEmpty(cellValue))
				errMsg = "食材名稱不可為空";
			bo.setIngredientName(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_STOCKDATE:
			//if (!CateringServiceUtil.isEmpty(bo.getDishName()) && !bo.getDishName().equals("調味料")) {
			if (!CateringServiceUtil.isEmpty(bo.getDishName())) {
				if (cellValue != null)
					cellValue = cellValue.replace("-", "/");
				errMsg = checkStockDateFormat(cellValue);
			}
			bo.setStockDate(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_MANUFACTUREDATE:
			if (!CateringServiceUtil.isEmpty(cellValue)) {
				cellValue = cellValue.replace("-", "/");
				try {
					if (DateUtil.isFailDateString(cellValue, "yyyy/MM/dd")) {
						errMsg = "生產日期格式錯誤(例:2014/01/01)";
					} else {
						bo.setManufactureDate(cellValue);
					}
				} catch (Exception ex) {
					throw new Exception("生產日期格式錯誤");
				}

			} else {
				bo.setManufactureDate("");
			}
			break;
		case XlsTitleCode.INGREDIENT_EXPIRATIONDATE:
			if (!CateringServiceUtil.isEmpty(cellValue)) {
				cellValue = cellValue.replace("-", "/");
				try {
					if (DateUtil.isFailDateString(cellValue, "yyyy/MM/dd")) {
						errMsg = "到期日期格式錯誤";
					} else {
						bo.setExpirationDate(cellValue);
					}
				} catch (Exception ex) {
					throw new Exception("到期日期格式錯誤");
				}
			} else {
				bo.setExpirationDate("");
			}
			break;
		case XlsTitleCode.INGREDIENT_LOTNUMBER:
			String lotNum = CateringServiceUtil.isEmpty(cellValue) ? CateringServiceUtil.defaultLotNumber : cellValue;
			bo.setLotNumber(lotNum);
			break;
		case XlsTitleCode.INGREDIENT_BRAND:
			if (CateringServiceUtil.isEmpty(cellValue)) {
				bo.setBrand("");
				bo.setManufacturer("");
			} else {
				bo.setBrand(cellValue);
				bo.setManufacturer(cellValue);
			}
			break;
		case XlsTitleCode.INGREDIENT_COMPANYID:
			if (CateringServiceUtil.isEmpty(cellValue))
				errMsg = "供應商統編不可為空";
			bo.setCompanyId(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_SOURCECERTIFICATION:
			if (CateringServiceUtil.isEmpty(cellValue))
				bo.setSourceCertification("");
			else
				bo.setSourceCertification(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_CERTIFICATIONID:
			if (CateringServiceUtil.isEmpty(cellValue))
				bo.setCertificationId("");
			else
				bo.setCertificationId(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_SOURCECERTIFICATIONV2:
			if (CateringServiceUtil.isEmpty(cellValue))
				bo.setSourceCertification("");
			else
				bo.setSourceCertification(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_CERTIFICATIONIDV2:
			if (CateringServiceUtil.isEmpty(cellValue))
				bo.setCertificationId("");
			else
				bo.setCertificationId(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_PRODUCTNAME:
			if (CateringServiceUtil.isEmpty(cellValue))
				bo.setProductName("");
			else
				bo.setProductName(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_MANUFACTURER:
			if (CateringServiceUtil.isEmpty(cellValue))
				bo.setManufacturer("");
			else
				bo.setManufacturer(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_INGREDIENTQUANTITY:
			if (CateringServiceUtil.isEmpty(cellValue))
				bo.setIngredientQuantity("0");
			else {
				if (CateringServiceUtil.isNumeric(cellValue))
					bo.setIngredientQuantity(cellValue);
				else
					errMsg = "重量格式錯誤(須為數字)";
			}
			break;
		case XlsTitleCode.INGREDIENT_INGREDIENTUNIT:
			if (CateringServiceUtil.isEmpty(cellValue))
				bo.setCertificationId("");
			else
				bo.setCertificationId(cellValue);
			break;
		case XlsTitleCode.INGREDIENT_GMBEAN:
			if (!CateringServiceUtil.isEmpty(cellValue))
				if (cellValue.toUpperCase().equals("Y"))
					bo.getIngredientAttribute().setGmbean(1);
				else if (cellValue.toUpperCase().equals("N"))
					bo.getIngredientAttribute().setGmbean(0);
				else
					errMsg = "基改黃豆格式錯誤(需為Y或N)";
			else
				bo.getIngredientAttribute().setGmbean(0);
			break;

		case XlsTitleCode.INGREDIENT_GMCORN:
			if (!CateringServiceUtil.isEmpty(cellValue))
				if (cellValue.toUpperCase().equals("Y"))
					bo.getIngredientAttribute().setGmcorn(1);
				else if (cellValue.toUpperCase().equals("N"))
					bo.getIngredientAttribute().setGmcorn(0);
				else
					errMsg = "基改玉米格式錯誤(需為Y或N)";
			else
				bo.getIngredientAttribute().setGmcorn(0);
			break;

		case XlsTitleCode.INGREDIENT_PSFOOD:
			if (!CateringServiceUtil.isEmpty(cellValue))
				if (cellValue.toUpperCase().equals("Y"))
					bo.getIngredientAttribute().setPsfood(1);
				else if (cellValue.toUpperCase().equals("N"))
					bo.getIngredientAttribute().setPsfood(0);
				else
					errMsg = "加工品格式錯誤(需為Y或N)";
			else
				bo.getIngredientAttribute().setPsfood(0);
			break;
		case XlsTitleCode.INGREDIENT_COMPANYNAME:
			if (CateringServiceUtil.isEmpty(cellValue))
				bo.setSupplierName("");
			else
				bo.setSupplierName(cellValue);
			break;
		default:
			break;
		}

		if (!CateringServiceUtil.isEmpty(errMsg))
			throw new Exception(errMsg);
		return bo;
	}

	/**
	 * 檢查供餐日期
	 * 
	 * @param menuDate
	 * @return
	 * @throws Exception
	 */
	private String checkMenuDate(String menuDate) {

		String errMsg = "";

		// 檢查為空
		if (CateringServiceUtil.isEmpty(menuDate)) {
			errMsg = putErrorMsg(errMsg, "供餐日期不可為空");
		}
		// 檢查格式
		try {
			if (DateUtil.isFailDateString(menuDate, "yyyy/MM/dd")) {
				errMsg = putErrorMsg(errMsg, "供餐日期日期格式錯誤(例:2014/01/01)");

				/*
				 * 從server config 抓取值 true:不可以新增今日之前的食材 false:可以新增今日之前的食材
				 */
				// 抓取server config 值
				String limitString = CateringServiceUtil.getConfig("excel_limit_time");
				Timestamp dateTS;
				if (limitString.equals("false")) {
					dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", menuDate.trim());
				} else {
					dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd HH:mm:ss", menuDate.trim() + " " + limitString);
				}

				// 檢查日期不得晚於當天設定 (limit值抓環境變數)
				if (!"false".equals(limitString)) {
					if (DateUtil.isOverTime(dateTS, limitString)) {
						errMsg = putErrorMsg(errMsg, "請勿上傳歷史資料:每日菜單須於當日" + limitString + "前完成上傳，上傳內容不得超過規定上傳時間");
					}
				}
			}
		} catch (Exception ex) {
			errMsg = putErrorMsg(errMsg, "供餐日期日期格式錯誤");
		}
		return errMsg;
	}

	/**
	 * 檢查進貨日期
	 * 
	 * @param dateString
	 * @return
	 */
	private String checkStockDateFormat(String dateString) {
		String errMsg = "";
		// 檢查為空
		if (CateringServiceUtil.isEmpty(dateString)) {
			errMsg = putErrorMsg(errMsg, "進貨日期不可為空");
			return errMsg;
		}
		// 檢查格式
		try {
			if (DateUtil.isFailDateString(dateString, "yyyy/MM/dd")) {
				errMsg = putErrorMsg(errMsg, "進貨日期格式錯誤");
				return errMsg;
			}
		} catch (Exception e) {
			errMsg = putErrorMsg(errMsg, "進貨日期格式錯誤");
		}

		return errMsg;
	}

	/**
	 * 串接error message
	 * 
	 * @param errMsg
	 * @param errString
	 * @return
	 */
	private String putErrorMsg(String errMsg, String errString) {
		if (CateringServiceUtil.isEmpty(errMsg)) {
			errMsg = errString;
		} else {
			errMsg += "," + errString;
		}
		return errMsg;
	}

	/**
	 * 增加已經處理的食材至暫存MAP
	 * 
	 * @param batchDataId
	 * @param dishId
	 * @param ingredientBatchdataId
	 */
	public void putModifiMap(Long batchDataId, Long dishId, Long ingredientBatchdataId) {
		Map<Long, List<Long>> dishMap;
		List<Long> ibdataList;
		if (!modifyMap.containsKey(batchDataId)) {
			dishMap = new HashMap<Long, List<Long>>();
			ibdataList = new ArrayList<Long>();
			ibdataList.add(ingredientBatchdataId);
			dishMap.put(dishId, ibdataList);
			modifyMap.put(batchDataId, dishMap);
		} else {
			dishMap = modifyMap.get(batchDataId);
			if (!dishMap.containsKey(dishId)) {
				ibdataList = new ArrayList<Long>();
				ibdataList.add(ingredientBatchdataId);
				dishMap.put(dishId, ibdataList);
			} else {
				ibdataList = dishMap.get(dishId);
				if (!ibdataList.contains(ingredientBatchdataId)) {
					ibdataList.add(ingredientBatchdataId);
					dishMap.put(dishId, ibdataList);
				}
			}
			modifyMap.put(batchDataId, dishMap);
		}
		dishMap.put(dishId, ibdataList);
		modifyMap.put(batchDataId, dishMap);
	}

	/**
	 * 檢核上傳時間限制
	 * 
	 * @param bo
	 * @return
	 * @throws Exception
	 */
	public String validateMenuUploadTime(IngredientBatchDataBO bo) throws Exception {
		String errMsg = "";
		School school = null;
		ExcelParser ep = new ExcelParser();
		Timestamp nowTS = new Timestamp((new Date()).getTime());

		ep.setSession(dbSession);

		// 取學校資訊
		if (!CateringServiceUtil.isEmpty(bo.getSchoolName())) {
			school = getSchoolBySchoolName(bo.getSchoolName());
			bo.setSchoolId(school.getSchoolId());
		}

		// 取菜單資訊
		List<Batchdata> batchdataList = getCacheBatchdataList(kitchenId, bo.getMenuDate(), bo.getSchoolId());

		if (batchdataList == null) {
			errMsg = putErrorMsg(errMsg, "查無學校菜單資訊");
			return errMsg;
		}

		for (Batchdata batchdata : batchdataList) {
			String schoolName = "";
			if (school == null) {
				SchoolDAO schoolDao = new SchoolDAO(dbSession);
				school = schoolDao.querySchoolById(batchdata.getSchoolId());
				if (school == null) {
					errMsg = putErrorMsg(errMsg, "查無學校資訊");
					break;
				} else {
					// 塞入cache
					cacheSchool.put(school.getSchoolName(), school);
				}
			}
			schoolName = school.getSchoolName();

			if (!ep.isMenuUploadTime(bo.getMenuDate(), nowTS, school.getSchoolId())) {
				errMsg = putErrorMsg(errMsg, "供餐學校[" + schoolName + "] 菜單日期[" + bo.getMenuDate() + "] 食材: [" + bo.getIngredientName() + "] 已超過縣市上傳時間限制 ");
				break;
			}
		}

		return errMsg;
	}

	/**
	 * 檢核是否為CAS認證肉類
	 * 
	 * @param bo
	 * @return
	 * @throws ParseException
	 */
	public String validateCASMeat(IngredientBatchDataBO bo) throws ParseException {
		String errMsg = "";
		if (bo.getSourceCertification().equals(CertTypeCode.CAS)) {
			if (CateringServiceUtil.isMeat(bo.getIngredientName())) {
				// 這裡邏輯為,如果產品為CAS肉類,重量就會為必填欄位,目前邏輯先留著先不分配
				if (CateringServiceUtil.isEmpty(bo.getIngredientQuantity())) {
					// errMsg = "此食材為CAS肉類食品,請填寫重量";
				}
			}
		}
		return errMsg;
	}

	public boolean isOverCountyUploadTime(IngredientBatchDataBO bo) throws ParseException {
		boolean r = false;
		// 需要去抓取各縣市的上傳限制時間目前先留空

		if (CateringServiceUtil.isUploadTime(CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", bo.getMenuDate()), CateringServiceUtil.getCurrentTimestamp(), "")) {
			r = true;
		}

		return r;
	}

	/**
	 * 取得Batchdata中的資料,若沒有暫存則去資料庫查詢後塞入CacheBatchdata
	 * 
	 * @param kitchenId
	 * @param menuDate
	 * @param schoolId
	 * @return
	 */
	public List<Batchdata> getCacheBatchdataList(Integer kitchenId, String menuDate, Integer schoolId) {
		List<Batchdata> batchdataList = new ArrayList<Batchdata>();
		String mapKey = "";
		boolean isAllSchool = CateringServiceUtil.isNull(schoolId);

		// 組Cache Map key
		if (isAllSchool)
			mapKey = kitchenId + "_" + menuDate;
		else
			mapKey = kitchenId + "_" + menuDate + "_" + schoolId;

		// 查詢Cache Map中有無暫存資料,若沒有裁去資料庫中抓取資訊
		if (cacheBatchDataList.containsKey(mapKey)) {
			batchdataList = cacheBatchDataList.get(mapKey);
		} else {
			BatchdataDAO batchdataDao = new BatchdataDAO(dbSession);
			if (isAllSchool) {
				batchdataList = batchdataDao.queryBatchdataList(kitchenId, menuDate);
			} else {
				batchdataList = batchdataDao.queryBatchdataList(kitchenId, menuDate, schoolId);
			}
			cacheBatchDataList.put(mapKey, batchdataList);
		}
		return batchdataList;
	}

	/**
	 * 處理 dishbatchdata 調味料
	 * 
	 * @return
	 */
	private DishBatchData insertDishBatchData(Long batchDataId, Long dishId) {
		DishBatchDataDAO dao = new DishBatchDataDAO();
		dao.setSession(dbSession);
		DishBatchData dbd = dao.getSpecifiedDish(batchDataId, CateringServiceCode.DISHTYPE_SEASONING);
		if (dbd == null) {
			dbd = new DishBatchData();
			dbd.setBatchDataId(batchDataId);
			dbd.setDishId(dishId);
			dbd.setDishName("調味料");
			dbd.setDishShowName("調味料");
			dbd.setDishType(CateringServiceCode.DISHTYPE_SEASONING);
			dbd.setUpdateDateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			dbSession.save(dbd);
		}
		return dbd;
	}

	/**
	 * 取得學校分配之重量
	 * 
	 * @return Map<schoolId,分配之比例>
	 */
	public Map<Integer, Float> getSchoolWeightRate(List<Batchdata> batchDataList) {
		Map<Integer, Float> schWeightList = new HashMap<>();
		if (batchDataList.size() == 1) {
			Batchdata bd = batchDataList.get(0);
			schWeightList.put(bd.getSchoolId(), (float) 1);
		} else {
			List<Integer> schIdList = new ArrayList();
			Integer KitchenId = batchDataList.get(0).getKitchenId();
			for (Batchdata bd : batchDataList) {
				schIdList.add(bd.getSchoolId());
			}

			List<Schoolkitchen> skList = new ArrayList<>();
			SchoolkitchenDAO skDao = new SchoolkitchenDAO(dbSession);
			skList = skDao.querySchoolKitchenBySchoolIdList(schIdList, KitchenId);

			int totalQuantity = 0;
			float tmpQuantity = 0;
			if (skList != null) {
				for (Schoolkitchen sk : skList) {
					totalQuantity += sk.getQuantity();
				}
			}

			DecimalFormat df = new DecimalFormat("##.00");
			for (int i = 0; i < skList.size(); i++) {
				Schoolkitchen sk = skList.get(i);
				float rate = 0;
				if (i == skList.size() - 1)
					rate = 1 - tmpQuantity;
				else
					rate = Float.parseFloat(df.format(((float) sk.getQuantity() / (float) totalQuantity)));

				tmpQuantity += rate;
				schWeightList.put(sk.getId().getSchoolId(), rate);
			}

		}

		return schWeightList;
	}
	/**
	 * 新版調味料新增 add by ellis 20150303
	 * @param menuDate
	 * @param schoolId
	 * @throws ParseException
	 */
//	public void addNewSeasoningStcokData() throws ParseException{
//		
//		if(!CateringServiceUtil.isNull(batchdataIdList)){
//			for (int i=0;i<batchdataIdList.size();i++) {
//				String hql = "from Batchdata where BatchDataId = :batchdata";
//				Query query = dbSession.createQuery(hql);
//				query.setParameter("batchdata",batchdataIdList.get(i));
//				Batchdata batchdata = (Batchdata)query.uniqueResult();
//				DishDAO dishDao = new DishDAO(dbSession);
//				Dish sessoning = dishDao.queryDishByName(dbSession, batchdata.getKitchenId(),CateringServiceUtil.ColumnNameOfSeasoning);
//				SeasoningStockDataDAO sDAO = new SeasoningStockDataDAO(dbSession);
//				List<SeasoningStockData> seasoning;
//				seasoning = sDAO.querySeasoningByDishIdandDate(sessoning.getDishId(),batchdata.getMenuDate());
//				if(!CateringServiceUtil.isNull(seasoning)){
//					for(int j=0;j<seasoning.size();j++){
//						Ingredientbatchdata igdb = new Ingredientbatchdata();
//						igdb = sDAO.tranSeasoningdatastockToIngredientbatchdata(seasoning.get(j));
//						igdb.setBatchDataId(batchdata.getBatchDataId());
//						//判斷是否已經有此調味料，若無才新增
//						if(checkDuplicateSeasoningStcokData(seasoning.get(j),batchdata)){
//							sDAO.deleteSeasoningStockDatainIngredientbatchdatabyBatchdataIdAndIngredientId(batchdata.getBatchDataId(),seasoning.get(j));
//							dbSession.saveOrUpdate(igdb);
//							putModifiMap(batchdata.getBatchDataId(),igdb.getDishId(),igdb.getIngredientBatchId());
//						}
//					}
//				}
//			}
//		}
//		
//	}
	/**
	 * excel新增食材時，與seasoningStcokData之調味料重複檢驗
	 * @param seasoning
	 * @throws ParseException 
	 * @throws HibernateException 
	 */
//	public boolean checkDuplicateSeasoningStcokData(SeasoningStockData seasoning,Batchdata batchdata) throws HibernateException, ParseException {
//		IngredientbatchdataDAO igDAO = new IngredientbatchdataDAO(dbSession);
//		String stockDate = "";
//		String ManufactureDate = "";
//		String ExpirationDate = "";
//		if(!CateringServiceUtil.isNull(seasoning.getStockDate())){
//			stockDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", seasoning.getStockDate());
//		}
//		if(!CateringServiceUtil.isNull(seasoning.getManufactureDate())){
//			ManufactureDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", seasoning.getManufactureDate());
//			}
//		if(!CateringServiceUtil.isNull(seasoning.getExpirationDate())){
//			ExpirationDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", seasoning.getExpirationDate());
//			}
//		
//		if(CateringServiceUtil.isNull(igDAO.queryIngredientbatchdataUK(batchdata.getBatchDataId(), seasoning.getIngredientId(),  seasoning.getDishId(),  stockDate,  seasoning.getLotNumber(),  seasoning.getSupplierCompanyId(),  ManufactureDate,  ExpirationDate))){
//			return true;
//		}else{
//			return false;
//		}
//	}
	/**
	 * 紀錄此次新增的batchdataId
	 * @param batchdataId
	 */
	public void addBatchdataIdList(String batchdataId){
		//第一筆 無條件新增
		if(CateringServiceUtil.isNull(batchdataIdList)){
			batchdataIdList = new ArrayList<String>();
			batchdataIdList.add(batchdataId);
			return;
		}
		//檢查該batchdataid是否已存在
		for(int i =0;i<batchdataIdList.size();i++){
			if(batchdataIdList.get(i).equals(batchdataId)){
				return;
			}
		}
		batchdataIdList.add(batchdataId);
	}
	
	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}

	public boolean isVer2() {
		return isVer2;
	}

	public void setVer2(boolean isVer2) {
		this.isVer2 = isVer2;
	}
	
	public boolean isVer3() {
		return isVer3;
	}

	public void setVer3(boolean isVer3) {
		this.isVer3 = isVer3;
	}
	
	public boolean isVer4() {
		return isVer4;
	}

	public void setVer4(boolean isVer4) {
		this.isVer4 = isVer4;
	}
}
