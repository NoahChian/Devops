package org.iii.ideas.catering_service.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.SupplierDAO;
import org.iii.ideas.catering_service.rest.excel.create.CAbnormalSearch;
import org.iii.ideas.catering_service.rest.excel.create.CIngredientList;
import org.iii.ideas.catering_service.rest.excel.create.CKitchenIngredient;
import org.iii.ideas.catering_service.rest.excel.create.CMenu;
import org.iii.ideas.catering_service.rest.excel.create.CMenuList;
import org.iii.ideas.catering_service.rest.excel.create.CSchoolIngredient;
import org.iii.ideas.catering_service.rest.excel.create.CSupplier;
import org.iii.ideas.catering_service.rest.excel.create.CRefersList;
import org.iii.ideas.catering_service.rest.excel.create.CSupplyInfo;
import org.iii.ideas.catering_service.rest.excel.create.CVegetable;
import org.iii.ideas.catering_service.rest.excel.create.CreateExcelData;
import org.iii.ideas.catering_service.rest.excel.create.DishCount;
import org.iii.ideas.catering_service.rest.excel.create.NewsList;
import org.iii.ideas.catering_service.rest.excel.create.AuditSchoolkitchen;
import org.iii.ideas.catering_service.rest.excel.create.StatisticKitchen;
import org.iii.ideas.catering_service.rest.excel.create.StatisticSchool;
import org.iii.ideas.catering_service.rest.excel.create.AuditSchoolproductset;
import org.iii.ideas.catering_service.rest.excel.create.StatisticSupplier;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

/*
 * Excel 下載主程式
 */
public class CreateExcelFiles {
	private Logger log = Logger.getLogger(CreateExcelFiles.class);
	private String filePrefixName = "Default";
	private String requestFileType = "";
	
	public String createFile(String fileNameX, int kid, String uName,String uType) throws UnsupportedEncodingException, ParseException {
		String fPath = "";
		String fileguide = "";

		log.debug("Download Args:" + fileNameX);

		if (fileNameX.indexOf("menu") == -1 
			&& fileNameX.indexOf("newslist") == -1
			&& fileNameX.indexOf("audit") == -1
			&& fileNameX.indexOf("supplier") == -1
			&& fileNameX.indexOf("schoolingredient") == -1 
			&& fileNameX.indexOf("vegetable") == -1
			&& fileNameX.indexOf("kitchenIngredient") == -1 
			&& fileNameX.indexOf("supplyinfo") == -1 
			&& fileNameX.indexOf("abnormalsearch") == -1
			&& fileNameX.indexOf("menulist") == -1 //匯出學校菜單(can by sid,kid,cid,date)
			&& fileNameX.indexOf("ingredientlist") == -1 //匯出食材
			&& fileNameX.indexOf("statistic") == -1 //統計報表
				) {
			String fileName = fileNameX.replace("-", "/");
			byte[] decoded = Base64.decodeBase64(fileName);
			fileguide = new String(decoded, "UTF-8");
			// log.debug("加入參數: " + fileguide);
		} else {
			fileguide = fileNameX;
		}

		String[] names = fileguide.split("&");
		for (String name : names) {
			log.debug("Download Arg:" + name);
		}
		String filename = "";// 檔案名稱
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Data");
		
		
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		// This data needs to be written (Object[])
		// ///////////////////////RIC食材供應商供貨資訊(supplyInfo)
		String searchFileType = new String(names[0]);
		// String filePrefixName ="";
		this.requestFileType = searchFileType;
		log.debug("Download file type:" + searchFileType);

		CreateExcelData createExcelData = null;

		if (searchFileType.equals("supplyinfo")) {
			filename = new String(names[0]) + new String(names[1]);// 檔案名稱為下載類型與供應商名稱

			createExcelData = CreateExcelData.getInstance(new CSupplyInfo(names[1],names[2], names[3].replace("-", "/"),
					names[4].replace("-", "/"),uName,uType));

			this.setFilePrefixName("供應商Test1");

			// 異常資料查詢
		} else if (searchFileType.equals("abnormalsearch")) {

			createExcelData = CreateExcelData.getInstance(new CAbnormalSearch(names[1], names[2], names[3].replace(
					"-", "/"), names[4].replace("-", "/")));

			this.setFilePrefixName("供應商Test2");
		} else if (searchFileType.equals("supplier")) { // 供應商excel 下載

			createExcelData = CreateExcelData.getInstance(new CSupplier(kid, searchFileType));

			this.setFilePrefixName("供應商");
		} else if (searchFileType.equals("schoolingredient")) {// 學校食材Excel匯出
			
			// Create SupplierList to Sheet add by ellis 20141124
			XSSFSheet suppliersheet = workbook.createSheet("RefersList");
			
			createExcelData = CreateExcelData.getInstance(new CRefersList(kid));
			data = createExcelData.doGenerateData();
			int rownum = data.size();
			writeToSheet(rownum,suppliersheet,data);
			
			
			/* 建立下拉選單 add by Ellis 20141125*/
			//撈取供應商資料
			SupplierDAO sDAO = new SupplierDAO();
			//建立Supplier參照
			XSSFName rangesupplier = workbook.createName();
			rangesupplier.setRefersToFormula("RefersList!$A$2:$A$"+(sDAO.querySupplierList(kid).length+1));
			rangesupplier.setNameName("SupplierList");
			//建立School參照
			SchoolDAO scDAO = new SchoolDAO();
			XSSFName rangeschool = workbook.createName();
			rangeschool.setRefersToFormula("RefersList!$B$2:$B$"+(scDAO.querySchoolListByKitchenId(kid).size()+2)); //多參照一行空白
			rangeschool.setNameName("SchoolList");
			//宣告Helper用於嵌入資料驗證
			XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
			
			//將參照加入constraint
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
					dvHelper.createFormulaListConstraint("SupplierList");
			CellRangeAddressList addressSupplierList = new CellRangeAddressList(1, 65535, 9, 9);
			XSSFDataValidation validation_supplier = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressSupplierList);
			
			CellRangeAddressList addressSchoolList = new CellRangeAddressList(1, 65535, 1, 1);
			XSSFDataValidationConstraint dvConstraint2 = (XSSFDataValidationConstraint)
					dvHelper.createFormulaListConstraint("SchoolList");
			XSSFDataValidation validation_school = (XSSFDataValidation)dvHelper.createValidation(dvConstraint2, addressSchoolList);
			
			//資料格式鎖定
			validation_supplier.setShowErrorBox(true);
			validation_school.setShowErrorBox(true);
			sheet.addValidationData(validation_supplier);
			sheet.addValidationData(validation_school);
			//隱藏RefersList之sheet
			workbook.setSheetHidden(1, 1);
			
			//寫入食材
			String begDate = names[1].trim().replace("-", "/");
			String endDate = names[2].trim().replace("-", "/");

			begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
			endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
			
			createExcelData = CreateExcelData.getInstance(new CSchoolIngredient(kid, begDate, endDate));
			this.setFilePrefixName("學校食材");

		} else if (searchFileType.equals("vegetable")) {// 菜色資料匯出
			//建立供應商下拉式選單參照 20141203 add by ellis
			XSSFSheet suppliersheet = workbook.createSheet("RefersList");
			createExcelData = CreateExcelData.getInstance(new CRefersList(kid,"Supplier"));
			data = createExcelData.doGenerateData();
			int rownum = data.size();
			writeToSheet(rownum,suppliersheet,data);
			
			XSSFName rangesupplier = workbook.createName();
			rangesupplier.setRefersToFormula("RefersList!$A$2:$A$"+(data.size()+1));
			rangesupplier.setNameName("SupplierList");
			//宣告Helper用於嵌入資料驗證
			XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
			//將參照加入constraint
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
					dvHelper.createFormulaListConstraint("SupplierList");
			CellRangeAddressList addressSupplierList = new CellRangeAddressList(1, 65535, 2, 2);
			XSSFDataValidation validation_supplier = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressSupplierList);
			
			validation_supplier.setShowErrorBox(true);
			sheet.addValidationData(validation_supplier);
			workbook.setSheetHidden(1, 1);
			
			createExcelData = CreateExcelData.getInstance(new CVegetable(kid));
			this.setFilePrefixName("學校菜色");
		} else if (searchFileType.equals("menu")) {// 學校菜單Excel匯出
			//建立學校下拉式選單參照 20141203 add by ellis
			XSSFSheet suppliersheet = workbook.createSheet("RefersList");
			
			createExcelData = CreateExcelData.getInstance(new CRefersList(kid,"School"));
			data = createExcelData.doGenerateData();
			int rownum = data.size();
			writeToSheet(rownum,suppliersheet,data);
			
			XSSFName rangesupplier = workbook.createName();
			rangesupplier.setRefersToFormula("RefersList!$A$2:$A$"+(data.size()));
			rangesupplier.setNameName("SchoolList");
			//宣告Helper用於嵌入資料驗證
			XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
			//將參照加入constraint
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
					dvHelper.createFormulaListConstraint("SchoolList");
			CellRangeAddressList addressSchoolList = new CellRangeAddressList(1, 65535, 0, 0);
			XSSFDataValidation validation_school = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressSchoolList);
			
			validation_school.setShowErrorBox(true);
			sheet.addValidationData(validation_school);
			workbook.setSheetHidden(1, 1);
			
			String sid = names[1].trim();
			String begDate = names[2].trim().replace("-", "/");
			String endDate = names[3].trim().replace("-", "/");

			begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
			endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));

			createExcelData = CreateExcelData.getInstance(new CMenu(kid, sid, begDate, endDate));
			
			this.setFilePrefixName("學校菜單");

		} else if ("kitchenIngredient".equalsIgnoreCase(searchFileType)) {
			String begDate = names[2].trim().replace("-", "/");
			String endDate = names[3].trim().replace("-", "/");
			begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
			endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));

			createExcelData = CreateExcelData.getInstance(new CKitchenIngredient(Integer.parseInt(names[1]), begDate, endDate, uName,uType));

			this.setFilePrefixName("團膳業者使用食材資訊");
		} else if ("menulist".equalsIgnoreCase(searchFileType)) {
			//http://localhost:8080/cateringservice/rest/API/XLS/menulist&2014-06-01&2014-8-01&0&0&0&0 //沒有要輸入要補0
			String kitchenid = names[3].trim();	//要查詢的廚房
			String restaurantid = names[4].trim();	//要查詢的餐廳
			String schoolid= names[5].trim();	//要查詢的學校
			String countyid = names[6].trim();	//要查詢的縣市
			String queryLimit = names[7].trim();	//查詢限制量
			String begDate = names[1].trim().replace("-", "/");
			String endDate = names[2].trim().replace("-", "/");
			begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
			endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(kitchenid)){kitchenid=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(restaurantid)){restaurantid=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(schoolid)){schoolid=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(countyid)){countyid=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			createExcelData = CreateExcelData.getInstance(
					new CMenuList(begDate, endDate, 
							Integer.parseInt(kitchenid), Integer.parseInt(restaurantid), Integer.parseInt(schoolid), Integer.parseInt(countyid), 
							uName,uType, Integer.parseInt(queryLimit)));

			this.setFilePrefixName("學校菜單資訊");
		}  else if ("ingredientlist".equalsIgnoreCase(searchFileType)) {
			//http://localhost:8080/cateringservice/rest/API/XLS/ingredientlist&2014-06-01&2014-8-01&0&0&0&100&菜色&食材&品牌&供應商 //沒有要輸入要補0
			String kitchenid = names[3].trim();	//要查詢的廚房
			String schoolid= names[4].trim();	//要查詢的學校
			String countyid = names[5].trim();	//要查詢的縣市
			String queryLimit = names[6].trim();	//查詢限制量
			String begDate = names[1].trim().replace("-", "/");
			String endDate = names[2].trim().replace("-", "/");
			String dishname = names[7].trim();
			String ingredientName = names[8].trim();
			String brand = names[9].trim();
			String supplierName = names[10].trim();
			String manufacturer = names[11].trim();

			begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
			endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(kitchenid)){kitchenid=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(schoolid)){schoolid=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(countyid)){countyid=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(dishname)){dishname=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(ingredientName)){ingredientName=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(brand)){brand=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(supplierName)){supplierName=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(manufacturer)){manufacturer=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			createExcelData = CreateExcelData.getInstance(
					new CIngredientList(begDate, endDate, 
							Integer.parseInt(kitchenid), Integer.parseInt(schoolid), Integer.parseInt(countyid), 
							uName,uType, Integer.parseInt(queryLimit),dishname,ingredientName,brand,supplierName,manufacturer));

			this.setFilePrefixName("食材資訊");
		} else if ("statisticDishCount".equalsIgnoreCase(searchFileType)) {
			//TODO #11542 菜色量統計報表
			//http://localhost:8080/cateringservice/rest/API/XLS/dishCount&2014-06-01&2014-8-01&0&0&0&0 //沒有要輸入要補0
			String countyid = names[3].trim();	//要查詢的廚房
			String schoolid = names[4].trim();	//要查詢的餐廳
			String begDate = names[1].trim().replace("-", "/");
			String endDate = names[2].trim().replace("-", "/");
			begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
			endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(schoolid)){schoolid=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(countyid)){countyid=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			createExcelData = CreateExcelData.getInstance(
					new DishCount(begDate, endDate, Integer.parseInt(countyid),Integer.parseInt(schoolid)));

			this.setFilePrefixName("學校菜單資訊");
		} else if ("statisticSchool".equalsIgnoreCase(searchFileType)) {
			//TODO #11633 學校數量與清單
			//http://localhost:8080/cateringservice/rest/API/XLS/statisticSchool&countyId&kitchenId&restaurantId //沒有要輸入要補0
			String countyId = names[1].trim();	//要查詢的縣市
			String kitchenId = names[2].trim();	//要查詢的餐廳
			String restaurantId = names[3].trim();	//要查詢的餐廳
			String schoolId = names[4].trim();	//要查詢的學校
			String queryType = names[5].trim();	//要查詢的是廚房還是餐廳 (queryType : kitchen restaurant)

			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(countyId)){countyId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(kitchenId)){kitchenId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(restaurantId)){restaurantId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(schoolId)){schoolId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(queryType)){queryType=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			
			createExcelData = CreateExcelData.getInstance(
					new StatisticSchool(Integer.parseInt(countyId),Integer.parseInt(kitchenId),Integer.parseInt(restaurantId),Integer.parseInt(schoolId),queryType));

			this.setFilePrefixName(" 學校數量與清單");
		} else if ("statisticKitchen".equalsIgnoreCase(searchFileType)) {
			//TODO #11634 廚房數量與清單
			//http://localhost:8080/cateringservice/rest/API/XLS/statisticSchool&countyId&kitchenId&restaurantId&queryType //沒有要輸入要補0
			String countyId = names[1].trim();	//要查詢的縣市
			String kitchenId = names[2].trim();	//要查詢的餐廳
			String restaurantId = names[3].trim();	//要查詢的餐廳
			String schoolId = names[4].trim();	//要查詢的學校
			String queryType = names[5].trim();	//要查詢的是廚房還是餐廳 (queryType : kitchen restaurant)
			
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(countyId)){countyId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(kitchenId)){kitchenId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(restaurantId)){restaurantId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(schoolId)){schoolId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(queryType)){queryType=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}

			createExcelData = CreateExcelData.getInstance(
					new StatisticKitchen(Integer.parseInt(countyId),Integer.parseInt(kitchenId),Integer.parseInt(restaurantId),Integer.parseInt(schoolId),queryType));

			this.setFilePrefixName(" 廚房數量與清單");
		} else if ("statisticSupplier".equalsIgnoreCase(searchFileType)) {
			//TODO # 供應商數量與清單
			//http://localhost:8080/cateringservice/rest/API/XLS/statisticSchool&countyId&kitchenId&restaurantId&queryType //沒有要輸入要補0
			String countyId = names[1].trim();	//要查詢的縣市
			String kitchenId = names[2].trim();	//要查詢的餐廳
			String restaurantId = names[3].trim();	//要查詢的餐廳
			String schoolId = names[4].trim();	//要查詢的學校
			String queryType = names[5].trim();	//要查詢的是廚房還是餐廳 (queryType : kitchen restaurant)
			
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(countyId)){countyId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(kitchenId)){kitchenId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(restaurantId)){restaurantId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(schoolId)){schoolId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(queryType)){queryType=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}

			createExcelData = CreateExcelData.getInstance(
					new StatisticSupplier(Integer.parseInt(countyId),Integer.parseInt(kitchenId),Integer.parseInt(restaurantId),Integer.parseInt(schoolId),queryType));

			this.setFilePrefixName(" 供應商數量與清單");
		} else if ("newslist".equalsIgnoreCase(searchFileType)) {
			String startDate = names[1].trim().replace("-", "/");	//要查詢的起日
			String endDate = names[2].trim().replace("-", "/");	//要查詢的訖日
			String newsId = names[3].trim();	//要查詢的公告編號
			String newsTitle = names[4].trim();	//要查詢的公告標題
			String category = names[5].trim();	//要查詢的公告種類
			String queryType = names[6].trim();	//要查詢的日期類型(queryType : startEndDate publishDate)
			String queryLimit = names[7].trim();	//要查詢的數量限制

			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(startDate)){startDate=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(endDate)){endDate=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(newsId)){newsId=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(newsTitle)){newsTitle=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(category)){category=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(queryType)){queryType=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}
			if(CateringServiceCode.QUERY_PARAMETER_ZERO.equals(queryLimit)){queryLimit=CateringServiceCode.QUERY_PARAMETER_NO_ENTER;}

			createExcelData = CreateExcelData.getInstance(
					new NewsList(startDate,endDate,Integer.parseInt(newsId),newsTitle,Integer.parseInt(category),queryType,uName,uType, Integer.parseInt(queryLimit)));

			this.setFilePrefixName(" 公告管理");
		} else if ("auditSchoolkitchen".equalsIgnoreCase(searchFileType)) {
			// 匯出供餐廚房審核紀錄查詢結果
			Integer schoolId = new Integer(Integer.parseInt(names[1].trim()));
			String approveStatus = names[2].trim();
			String startDate = names[3].trim();
			String endDate = names[4].trim();
			createExcelData = CreateExcelData.getInstance(new AuditSchoolkitchen(schoolId, approveStatus,startDate, endDate));
			this.setFilePrefixName(" 供餐廚房審核紀錄");
		}  else if ("auditSchoolproductset".equalsIgnoreCase(searchFileType)) {
			// 匯出商品上架審核紀錄查詢結果
			Integer schoolId = new Integer(Integer.parseInt(names[1].trim()));
			String approveStatus = names[2].trim();
			String startDate = names[3].trim();
			String endDate = names[4].trim();
			createExcelData = CreateExcelData.getInstance(new AuditSchoolproductset(schoolId, approveStatus,startDate, endDate));
			this.setFilePrefixName(" 商品上架審核紀錄");
		} else {
			throw new UnsupportedEncodingException("不支援的操作");
		}

		data = createExcelData.doGenerateData();

		// Iterate over data and write to sheet
		int rownum = data.size();
		writeToSheet(rownum,sheet,data);
		//sheet.addValidationData(validation);
		//sheet.addValidationData(data_validation);
		/* 修改至獨立Function writeToSheet modify by ellis 20141124
		for (int loopi = 1; loopi <= rownum; loopi++) {
			Row row = sheet.createRow(loopi - 1);
			Object[] objArr = data.get(String.valueOf(loopi));
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if (obj instanceof String) {
					cell.setCellValue((String) obj);
				} else if (obj instanceof Integer) {
					cell.setCellValue(String.valueOf(obj));
					// cell.setCellValue((Integer) obj);
				}else if (obj instanceof Long) {  //20140812 KC 增加判斷long 因為uuid
					cell.setCellValue(String.valueOf(obj));
					// cell.setCellValue((Integer) obj);
				} 	
				else {
					cell.setCellValue((String) obj);
				}
			}
		}
		*/
		
		try {

			fPath = CateringServiceUtil.getDownloadPath(this.getRequestFileType());
			FileOutputStream out = new FileOutputStream(new File(fPath));
			workbook.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fPath;
	}
	
	public void writeToSheet(int rownum,XSSFSheet sheet, Map<String, Object[]> data){
		for (int loopi = 1; loopi <= rownum; loopi++) {
			Row row = sheet.createRow(loopi - 1);
			Object[] objArr = data.get(String.valueOf(loopi));
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				//設定type
				/*
				if(obj.toString().startsWith("IF")){ //開頭為IF視為函式，則type為CELL_TYPE_FORMULA
					cell.setCellType(Cell.CELL_TYPE_FORMULA);
				}else{
					cell.setCellType(Cell.CELL_TYPE_STRING);
				}
				*/
				//設定Value
				if (obj instanceof String) {
					/*
					if(obj.toString().startsWith("IF")){ //開頭為IF視為函式，setCellFormula
						cell.setCellFormula((String) obj);
					}else{
						cell.setCellValue((String) obj);
					}*/
					cell.setCellValue((String) obj);
				} else if (obj instanceof Integer) {
					cell.setCellValue(String.valueOf(obj));
					// cell.setCellValue((Integer) obj);
				}else if (obj instanceof Long) {  //20140812 KC 增加判斷long 因為uuid
					cell.setCellValue(String.valueOf(obj));
					// cell.setCellValue((Integer) obj);
				} else if (obj instanceof Date){
					cell.setCellValue(String.valueOf(obj));
				} else {
					cell.setCellValue((String) obj);
				}
			}
		}
	}
	
	public String getFilePrefixName() {
		return filePrefixName;
	}

	public void setFilePrefixName(String filePrefixName) {
		this.filePrefixName = filePrefixName;
	}

	public String getRequestFileType() {
		return requestFileType;
	}

	public void setRequestFileType(String requestFileType) {
		this.requestFileType = requestFileType;
	}
}
