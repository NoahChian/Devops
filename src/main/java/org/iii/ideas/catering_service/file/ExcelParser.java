package org.iii.ideas.catering_service.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolkitchenDAO;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.SupplierId;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.UseraccountDAO;
import org.iii.ideas.catering_service.file.object.FileReturnObject;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.SchoolAndKitchenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExcelParser {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	private boolean overwrite = false;
	private Integer kitchenId = null;
	private String userName = "";
	private String userType = "";
	private Session session = null;
	private Long tmpDishId = (long)0;
	private Long tmpBatchDataId = (long)0;
	private boolean hasNextRow = true;
	List<Long> modifyIbId = new ArrayList<Long>();
	private HashMap<String, Long> dishMap = new HashMap<String, Long>();
	// 紀錄已新增過的ingredient 資料
	private HashMap<String, Ingredient> ingredientNameMap = new HashMap<String, Ingredient>();// key=
																								// dishid_ingredientName
	private HashMap<String, Dish> dishNameMap = new HashMap<String, Dish>();// key=dishName
	private HashMap<String, Supplier> supplierCompanyIdMap = new HashMap<String, Supplier>();// key=companyId
	// private HashMap<String, Ingredientbatchdata>
	// ingredientbatchdataCompanyIdMap = new HashMap<String,
	// Ingredientbatchdata>();//key=companyId
	private HashMap<String, School> schoolNameMap = new HashMap<String, School>();// key
																					// =
																					// schoolName
	private HashMap<String, List<Batchdata>> batchdataMenuMap = new HashMap<String, List<Batchdata>>();// key
																										// =
																										// schoolName
	private HashMap<Integer,String> uploadLimitMap =new HashMap<Integer ,String >(); //key=schoolid
	
	public Integer successRowCount = 0;


	// 建構式
	public ExcelParser() {
	}

	public void validExcelType(String filename, String funcName) throws Exception {
		if (!checkHeaderFormat(filename, funcName)) {
			throw new Exception("   上傳之excel檔，第一列標題欄不正確!");
		}
	}

	public void writeXLSFile() throws Exception {
		String fileXls = CateringServiceUtil.converTimestampToStr("yyyyMMddHHmmss", CateringServiceUtil.getCurrentTimestamp()) + ".xls";
		String excelFileName = CateringServiceUtil.getConfig("uploadPath") + "tmp/" + fileXls;
		String sheetName = "Sheet1";// name of sheet
		log.debug("Create Excel file:" + excelFileName);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);

		// iterating r number of rows
		for (int r = 0; r < 5; r++) {
			HSSFRow row = sheet.createRow(r);
			// iterating c number of columns
			for (int c = 0; c < 5; c++) {
				HSSFCell cell = row.createCell(c);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue("Cell " + r + " " + c);
			}
		}

		FileOutputStream fileOut = new FileOutputStream(excelFileName);
		// write this workbook to an Outputstream.
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

	public String getCellValue(XSSFCell cell) throws Exception {
		String cellValue = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			cellValue = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			throw new Exception("欄位格式有誤,請確認所有欄位為文字格式");
			// break;
		case Cell.CELL_TYPE_ERROR:
			throw new Exception("欄位格式有誤,請確認所有欄位為文字格式");
			// break;
		case Cell.CELL_TYPE_FORMULA:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cellValue = cell.getStringCellValue().trim();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			XSSFCell tmpCell = cell;
			if (DateUtil.isCellDateFormatted(tmpCell)) {
				try {
					cellValue = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", new Timestamp(cell.getDateCellValue().getTime()));
				} catch (Exception e) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cellValue = cell.getStringCellValue().trim();
				}
			} else if (DateUtil.isCellInternalDateFormatted(tmpCell)) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cellValue = cell.getStringCellValue().trim();
			} else {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cellValue = cell.getStringCellValue().trim();
				cellValue = CateringServiceUtil.excelNumber(cellValue, "#.#");
			}

			break;
		case Cell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue().trim();
			break;
		default:
			throw new Exception("不明欄位格式,請確認所有欄位為文字格式");
		}

		return cellValue;
	}

	// 學校菜單Excel 匯入
	public FileReturnObject impSchoolMenuXLSXFile(String filename) throws Exception {
		if (!CateringServiceUtil.fileExists(filename)) {
			throw new Exception("找不到上傳檔案:" + filename);
		}
		//取得檔案傳入的當前時間  20140721 KC
		Timestamp nowTS=new Timestamp((new Date()).getTime());
		
		UploadMenu uploadMenu = new UploadMenu(session);
		uploadMenu.setOverwrite(this.isOverwrite());
		FileReturnObject retObj = new FileReturnObject();
		retObj.setRetMsg("");
		retObj.setRetStatus(0);
		InputStream ExcelFileToRead = new FileInputStream(filename);
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		// DataFormat format = wb.createDataFormat();
		// CellStyle style = wb.createCellStyle();
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		int rowPoint = 0;
		// XSSFCell cell;
		
		try {
			rowPoint = 0;
			Iterator rows = sheet.rowIterator();
			List<String> rowTitle = new ArrayList();
			if (sheet.getPhysicalNumberOfRows() == 0) {
				throw new Exception("請確認Excel檔內只有一個sheet或資料放置於第一個sheet");
			}
			// read row
			while (rows.hasNext()) {
				row = (XSSFRow) rows.next();
				Iterator cells = row.cellIterator();
				// int cellPoint = 0;
				String SchoolName = "";
				boolean emptyLine = true;
				boolean allSch = false;
				Batchdata batchdata = new Batchdata();
				batchdata.setKitchenId(kitchenId);
				batchdata.setMenuDate("");
				// batchdata.setLotNumber("1");// 預設logNumber 為 1
				batchdata.setLotNumber(CateringServiceUtil.defaultLotNumber); // 改為抓default
				Long defaultDishId=(long) 0;
				ArrayList<String> checkDish = new ArrayList(); //用於檢查是否有重複菜色 add by ellis 20150907
				// 20140418
																				// KC
				batchdata.setMainFoodId(defaultDishId);
				batchdata.setMainFood1id(defaultDishId);
				batchdata.setMainDishId(defaultDishId);
				batchdata.setMainDish1id(defaultDishId);
				batchdata.setMainDish2id(defaultDishId);
				batchdata.setMainDish3id(defaultDishId);
				batchdata.setSubDish1id(defaultDishId);
				batchdata.setSubDish2id(defaultDishId);
				batchdata.setSubDish3id(defaultDishId);
				batchdata.setSubDish4id(defaultDishId);
				batchdata.setSubDish5id(defaultDishId);
				batchdata.setSubDish6id(defaultDishId);
				batchdata.setVegetableId(defaultDishId);
				batchdata.setSoupId(defaultDishId);
				batchdata.setDessertId(defaultDishId);
				batchdata.setDessert1id(defaultDishId);
				batchdata.setMenuType(1);
				batchdata.setEnable(1);
				batchdata.setModifyUser(this.getUserName());
				while (cells.hasNext()) {
					XSSFCell cell = (XSSFCell) cells.next();
					
					String cellValue = this.getCellValue(cell);
					if (cellValue == null) {
						cellValue = "";
					} else {
						cellValue = cellValue.trim();
						if(cellValue != null){
							if(CateringServiceUtil.isVaildStr(cellValue)){
								throw new Exception("上傳檔案第"+(rowPoint + 1) +" 行 資料  "+ cellValue +"  包含特殊字元 \" > < ' % ; & 或者資料有斷行");
							}
						}
					}

					if (rowPoint == 0) {
						rowTitle.add(cell.getColumnIndex(), cellValue);
					} else {
						// 讀取菜色資料
						String titleName = "";
						try {
							titleName = rowTitle.get(cell.getColumnIndex()).trim();
						} catch (Exception e) {
							titleName = "";
						}

						if (titleName.equals("學校")) {
							// 如果學校是空值就跳到下一行(excel)
							if (cellValue.trim().equals("")) {
								emptyLine = true;
							} else if(cellValue.trim().equals("全部學校")) {
								allSch = true;
								emptyLine = false;
							} else {
								emptyLine = false;
								batchdata.setSchoolId(this.getSchoolIdByKidAndschoolName(session, kitchenId, cellValue));
								SchoolName = cellValue;
							}
						} else if (titleName.equals("日期")) {
							if (!CateringServiceUtil.isEmpty(cellValue.trim())) {
								/*  判斷上傳時間逾時的部分 改為insert前判斷  20140721 KC
								String limitString = CateringServiceUtil.getConfig("excel_limit_time");
								Timestamp dateTS;
								if (limitString.equals("false")) {
									dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", cellValue.trim());
								} else {
									dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd HH:mm:ss", cellValue.trim() + " " + limitString);
								}
								// ---增加檢查日期不得晚於當天設定 (limit值抓環境變數) 20140220 KC

								if (!limitString.equals("false")) {
									if (this.isOverTime(dateTS, limitString)) {
										throw new Exception("請勿上傳歷史資料:每日菜單須於當日" + limitString + "前完成上傳，上傳內容不得超過規定上傳時間");
									}
								}
								
								batchdata.setMenuDate(CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", dateTS));
								*/
								Timestamp dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", cellValue.trim());
								batchdata.setMenuDate(CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", dateTS));
							} else {
								batchdata.setMenuDate("");
							}
						} else if (titleName.equals("主食一")) {
							batchdata.setMainFoodId(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("主食二")) {
							batchdata.setMainFood1id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("主菜")) {
							batchdata.setMainDishId(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("主菜一")) {
							batchdata.setMainDish1id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("主菜二")) {
							batchdata.setMainDish2id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("主菜三")) {
							batchdata.setMainDish3id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("副菜一")) {
							batchdata.setSubDish1id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("副菜二")) {
							batchdata.setSubDish2id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("副菜三")) {
							batchdata.setSubDish3id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("副菜四")) {
							batchdata.setSubDish4id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("副菜五")) {
							batchdata.setSubDish5id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("副菜六")) {
							batchdata.setSubDish6id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("蔬菜") && cell.getColumnIndex() < 18) {// 第二個預蔬菜應該在
																							// cell
																							// 20
																							// ,所以以18來確定它是第一個蔬菜//
																							// 由為有兩欄蔬菜資料,所以加一個id當分別欄
							batchdata.setVegetableId(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("湯品")) {
							batchdata.setSoupId(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("附餐一")) {
							batchdata.setDessertId(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("附餐二")) {
							batchdata.setDessert1id(queryDishIdByNameAndUserType(cellValue, titleName));
						} else if (titleName.equals("全榖根莖")) {
							batchdata.setTypeGrains(cellValue);
						} else if (titleName.equals("豆魚肉蛋")) {
							batchdata.setTypeMeatBeans(cellValue);
						} else if (titleName.equals("蔬菜") && cell.getColumnIndex() > 18) {
							batchdata.setTypeVegetable(cellValue);
						} else if (titleName.equals("油脂與堅果種子")) {
							batchdata.setTypeOil(cellValue);
						} else if (titleName.equals("水果")) {
							batchdata.setTypeFruit(cellValue);
						} else if (titleName.equals("乳品")) {
							batchdata.setTypeMilk(cellValue);
						} else if (titleName.equals("熱量")) {
							batchdata.setCalorie(cellValue);
						}
						//若List不為null則進入確認是否重複。
						if(!CateringServiceUtil.isNull(checkDish)){
							for(int i=0;i<checkDish.size();i++){
								if(checkDish.get(i).equals(cellValue)){
									throw new Exception("上傳檔案"+(rowPoint + 1) +" 行 存在重複之菜色 : ["+titleName+"]"+cellValue+"。");
								}
							}
						}
						//若有菜色便存入List以利後續判斷，只存欄位2-17 (主食一開始到附餐二)
						if(!CateringServiceUtil.isEmpty(cellValue) && cell.getColumnIndex() >1 && cell.getColumnIndex() < 18){
							checkDish.add(cellValue);
						}
						log.debug("row:" + rowPoint + " cell:" + cell.getColumnIndex() + " title:" + titleName + " value:" + cellValue);
					}
					// cellPoint++;
				}
				
				if (rowPoint > 0) {
						//學校欄位輸入全部學校的情況
					if (emptyLine == false) {
						if(allSch == true){
							SchoolkitchenDAO skDao = new SchoolkitchenDAO(session);
							Integer schoolId;
							List<String[]> offeredList = skDao.querySchoolListByKitchenId(kitchenId);
							for(int i =0;i<offeredList.size();i++){
								Object[] obj = offeredList.get(i);
								schoolId = (Integer) obj[0];
								SchoolName = obj[1].toString();
								Batchdata newBatchdata = new Batchdata();
								newBatchdata.setMenuDate(batchdata.getMenuDate());
								newBatchdata.setSchoolId(schoolId);
								newBatchdata.setMenuType(1);
								newBatchdata.setEnable(1);
								newBatchdata.setMainFoodId(batchdata.getMainFoodId());
								newBatchdata.setMainFood1id(batchdata.getMainFood1id());
								newBatchdata.setMainDishId(batchdata.getMainDishId());
								newBatchdata.setMainDish1id(batchdata.getMainDish1id());
								newBatchdata.setMainDish2id(batchdata.getMainDish2id());
								newBatchdata.setMainDish3id(batchdata.getMainDish3id());
								newBatchdata.setSubDish1id(batchdata.getSubDish1id());
								newBatchdata.setSubDish2id(batchdata.getSubDish2id());
								newBatchdata.setSubDish3id(batchdata.getSubDish3id());
								newBatchdata.setSubDish4id(batchdata.getSubDish4id());
								newBatchdata.setSubDish5id(batchdata.getSubDish5id());
								newBatchdata.setSubDish6id(batchdata.getSubDish6id());
								newBatchdata.setVegetableId(batchdata.getVegetableId());
								newBatchdata.setSoupId(batchdata.getSoupId());
								newBatchdata.setDessertId(batchdata.getDessertId());
								newBatchdata.setDessert1id(batchdata.getDessert1id());
								newBatchdata.setLotNumber(batchdata.getLotNumber());
								newBatchdata.setTypeGrains(batchdata.getTypeGrains());
								newBatchdata.setTypeMeatBeans(batchdata.getTypeMeatBeans());
								newBatchdata.setTypeVegetable(batchdata.getTypeVegetable());
								newBatchdata.setTypeOil(batchdata.getTypeOil());
								newBatchdata.setTypeFruit(batchdata.getTypeFruit());
								newBatchdata.setTypeMilk(batchdata.getTypeMilk());
								newBatchdata.setCalorie(batchdata.getCalorie());
								newBatchdata.setModifyUser(this.getUserName());

								if (!isMenuUploadTime(newBatchdata.getMenuDate(),nowTS,newBatchdata.getSchoolId())){
									throw new Exception("上傳檔案"+(rowPoint + 1) +" 行 超過菜單可上傳日期 ");			
								}
								retObj = insertSchoolMenuFromXLSX(uploadMenu, rowPoint, SchoolName, newBatchdata);
								if (!retObj.getRetMsg().equals("")) {
									return retObj;
								} else {
									successRowCount = successRowCount + 1; // 增加判斷處理成功的總列數
																			// 20140418
																			// KC
								}
								System.out.println("學校2: 全部學校"+ (i+1) + ":" + SchoolName + " 菜單上傳 第:" + (rowPoint + 1) + "行");
							}
//							System.out.print(a.get(0));
//							batchdata.setSchoolId(this.getSchoolIdByKidAndschoolName(session, kitchenId, cellValue));
						}else{
							//資料處理前 先檢查日期是否有超過時間  20140721 KC
							if (!isMenuUploadTime(batchdata.getMenuDate(),nowTS,batchdata.getSchoolId())){
								throw new Exception("上傳檔案"+(rowPoint + 1) +" 行 超過菜單可上傳日期 ");			
							}
							retObj = insertSchoolMenuFromXLSX(uploadMenu, rowPoint, SchoolName, batchdata);
							if (!retObj.getRetMsg().equals("")) {
								return retObj;
							} else {
								successRowCount = successRowCount + 1; // 增加判斷處理成功的總列數
																		// 20140418
																		// KC
							}
						}
						

					}
				}
				if(allSch == false){
					System.out.println("學校2:" + SchoolName + " 菜單上傳 第:" + (rowPoint + 1) + "行");
				}
				
				rowPoint++;
			}
			retObj.setRetMsg("");
			retObj.setRetStatus(1);
			return retObj;
		} catch (Exception e) {
			ExcelFileToRead.close();
			e.printStackTrace();
			throw new Exception("發生錯誤行數:" + (rowPoint + 1) + " 問題:\n" + e.getMessage());
		}
	}

	private FileReturnObject insertSchoolMenuFromXLSX(UploadMenu uploadMenu, int rowPoint, String SchoolName, Batchdata batchdata) throws Exception {
		batchdata.setKitchenId(kitchenId);
		FileReturnObject retObj = new FileReturnObject();
		retObj.setRetMsg("");
		retObj.setRetStatus(0);
		log.debug("新增Excel 行數:" + (rowPoint + 1));
		// 主 食和主菜為必填
		//if (batchdata.getMainFoodId() == 0 || batchdata.getMainDishId() == 0) {
		if (batchdata.getMainFoodId() == 0 ) { //僅需驗證主食 modify by ellis 20141211
			//String msg = "第" + String.valueOf(rowPoint + 1) + "行 主食、主菜為必填欄位";
			String msg = "第" + String.valueOf(rowPoint + 1) + "行 主食為必填欄位";
			log.debug(msg);
			retObj.setRetMsg(msg);
			retObj.setRetStatus(0);
			return retObj;
		}
		// 六大營養標示為必填
		/*
		 * if (CateringServiceUtil.isEmpty(batchdata.getTypeGrains()) ||
		 * CateringServiceUtil.isEmpty(batchdata.getTypeMeatBeans()) ||
		 * CateringServiceUtil.isEmpty(batchdata.getTypeVegetable()) ||
		 * CateringServiceUtil.isEmpty(batchdata.getTypeOil()) ||
		 * CateringServiceUtil.isEmpty(batchdata.getTypeFruit()) ||
		 * CateringServiceUtil.isEmpty(batchdata.getTypeMilk()) ||
		 * CateringServiceUtil.isEmpty(batchdata.getCalorie())) {
		 * 
		 * String msg = "第" + String.valueOf(rowPoint + 1) + "行  六大營養、熱量為必填欄位" +
		 * " 全榖根莖:" + batchdata.getTypeGrains() + " 豆魚肉蛋:" +
		 * batchdata.getTypeMeatBeans() + " 蔬菜:" + batchdata.getTypeVegetable()
		 * + " 油脂與堅果種子:" + batchdata.getTypeOil() + " 水果:" +
		 * batchdata.getTypeFruit() + " 乳品:" + batchdata.getTypeMilk() + " 埶量:"
		 * + batchdata.getCalorie();
		 * 
		 * log.debug(msg); retObj.setRetMsg(msg); retObj.setRetStatus(0); return
		 * retObj; }
		 */
		// 學校和日期為必填欄位
		if (batchdata.getMenuDate().equals("") || SchoolName.equals("")) {
			String msg = "第" + String.valueOf(rowPoint + 1) + "行  日期、學校為必填欄位";
			log.debug(msg);
			retObj.setRetMsg(msg);
			retObj.setRetStatus(0);
			return retObj;
		} 
		if (batchdata.getMenuDate().length()!=10 || !batchdata.getMenuDate().substring(0,3).equals("201")) {
			String msg = "第" + String.valueOf(rowPoint + 1) + "行  菜單日期格式有誤";
			log.debug(msg);
			retObj.setRetMsg(msg);
			retObj.setRetStatus(0);
			return retObj;
		} 
		else {
			log.debug("==>" + batchdata.getMenuDate() + " ---- " + SchoolName);
		}
		// 新增至database
		int ret = uploadMenu.insMenu(session, batchdata, SchoolName);
		if (ret == -1) {
			retObj.setRetMsg("學校:" + SchoolName + " 供應商ID:" + this.getKitchenId() + " 日期:" + batchdata.getMenuDate() + " 菜單已存在");
			retObj.setRetStatus(-1);
			return retObj;
		}
		return retObj;
	}

	// 學校Excel食材匯入
	private HashMap<Long, List<Long>> batchdataMap = new HashMap<Long, List<Long>>();

	public void impSchoolIngredientXLSXFile(Session session, String userType, int kitchenId, String filename) throws Exception {
		InputStream ExcelFileToRead = new FileInputStream(filename);
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		int rowPoint = 0;
		// XSSFCell cell;
		// batchdata -> dishId 紀錄這次
		batchdataMap = new HashMap<Long, List<Long>>();
		
		try {
			Iterator rows = sheet.rowIterator();
			rowPoint = 0;
			List<String> rowTitle = new ArrayList();
			if (sheet.getPhysicalNumberOfRows() == 0) {
				throw new Exception("請確認Excel檔內只有一個sheet或資料放置於第一個sheet");
			}
			// read row
			while (rows.hasNext()) {
				row = (XSSFRow) rows.next();
				Iterator cells = row.cellIterator();
				int cellPoint = 0;
				// read cell
				// Integer BatchDataId=batchdataId;
				String schoolName = "";
				String menuDate = "";
				// Long dishId = 0;
				// Integer ingredientId = null;
				String ingredientName = "";
				String stockDate = "";
				String manufactureDate = "";
				String expirationDate = "";
				String lotNumber = CateringServiceUtil.defaultLotNumber;
				String brand = "";
				String companyId = "";
				String sourceCertification = "";
				String certificationId = "";
				String origin = "";
				String source = "";
				String dishName = "";
				boolean emptyLine = true;

				while (cells.hasNext()) {
					XSSFCell cell = (XSSFCell) cells.next();
					String cellValue = this.getCellValue(cell);
					// System.out.println("食材上傳 第:" + (rowPoint + 1) + "行");//
					// dennis
					if (cellValue == null) {
						cellValue = "";
					} else {
						cellValue = cellValue.trim();
					}
					if (rowPoint == 0) {
						rowTitle.add(cellPoint, cellValue);
					} else {
						String titleName = "";
						try {
							titleName = rowTitle.get(cell.getColumnIndex()).trim();
						} catch (Exception e) {
							titleName = "";
						}
						log.debug(titleName + ":" + cellValue);
						if (titleName.equals("學校")) {
							schoolName = cellValue;
						} else if (titleName.equals("供餐日期")) {
							if (CateringServiceUtil.isEmpty(cellValue)) {
								emptyLine = true;
							} else {
								emptyLine = false;
							}

							if (!CateringServiceUtil.isEmpty(cellValue.trim())) {
								/* 判斷上傳時間逾時的部分 改為insert前判斷  20140721 KC
								String limitString = CateringServiceUtil.getConfig("excel_limit_time");
								// Timestamp dateTS =
								// CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd HH:mm:ss",
								// cellValue.trim()+" "+limitString);

								Timestamp dateTS;
								if (limitString.equals("false")) {
									dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", cellValue.trim());
								} else {
									dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd HH:mm:ss", cellValue.trim() + " " + limitString);
								}

								// ---增加檢查日期不得晚於當天設定 (limit值抓環境變數) 20140220 KC

								if (!"false".equals(limitString)) {
									if (this.isOverTime(dateTS, limitString)) {
										throw new Exception("請勿上傳歷史資料:每日菜單須於當日" + limitString + "前完成上傳，上傳內容不得超過規定上傳時間");
									}
								}
								
								menuDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", dateTS);
								*/
								Timestamp dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", cellValue.trim());
								menuDate=CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", dateTS);								
							} else {
								menuDate = cellValue;
							}

						} else if (titleName.equals("菜色名稱")) {
							dishName = cellValue;
						} else if (titleName.equals("食材名稱")) {
							ingredientName = cellValue;
						} else if (titleName.equals("進貨日期")) {
							// stockDate = cellValue;
							if (!CateringServiceUtil.isEmpty(cellValue.trim())) {
								Timestamp dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", cellValue.trim());
								stockDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", dateTS);
							} else {
								stockDate = cellValue;
							}
						} else if (titleName.equals("生產日期")) {
							// manufactureDate = cellValue;

							if (!CateringServiceUtil.isEmpty(cellValue.trim())) {
								Timestamp dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", cellValue.trim());
								manufactureDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", dateTS);
							} else {
								manufactureDate = cellValue;
							}
						} else if (titleName.equals("有效日期")) {
							// expirationDate = cellValue;
							if (!CateringServiceUtil.isEmpty(cellValue.trim())) {
								Timestamp dateTS = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", cellValue.trim());
								expirationDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", dateTS);
							} else {
								expirationDate = cellValue;
							}
						} else if (titleName.equals("批號")) {
							lotNumber = CateringServiceUtil.isEmpty(cellValue) ? CateringServiceUtil.defaultLotNumber : cellValue.trim();
						} else if (titleName.equals("品牌(製造商)")) {
							brand = cellValue;
						} else if (titleName.equals("供應商統編")) {
							companyId = cellValue;
						} else if (titleName.equals("食材認證標章")) {
							sourceCertification = cellValue;
						} else if (titleName.equals("認證號碼")) {
							certificationId = cellValue;
						}
					}
					cellPoint++;
				}
				if (rowPoint > 0) {
					if (emptyLine == false) {

						 if(!rows.hasNext()){
							hasNextRow = false;
						}
						log.info("食材上傳 第:" + (rowPoint + 1) + "行");						
						insertSchoolIngredientFromXLSX(session, kitchenId, schoolName, menuDate, ingredientName, stockDate, manufactureDate, expirationDate, lotNumber, brand, companyId, sourceCertification, certificationId, origin, source, dishName);
						successRowCount = successRowCount + 1; // 增加判斷處理成功的總列數
																// 20140418 KC
					}
				}
				System.out.println("學校:" + schoolName + " 食材上傳 第:" + (rowPoint + 1) + "行");
				rowPoint++;
			}
		} catch (Exception e) {
			ExcelFileToRead.close();
			e.printStackTrace();
			throw new Exception(" 錯誤行數:" + (rowPoint + 1) + " 問題:\n" + e.getMessage());
		}
	}

	private void insertSchoolIngredientFromXLSX(Session session, int kitchenId, String schoolName, String menuDate, String ingredientName, String stockDate, String manufactureDate, String expirationDate, String lotNumber, String brand, String companyId,
			String sourceCertification, String certificationId, String origin, String source, String dishName) throws Exception {
		if (ingredientName.trim().equals("")) {
			throw new Exception("食材名稱為空值 日期:[" + menuDate + "]");
		}
		Long dishId = null;

		Dish dish = dishNameMap.get(dishName);
		Supplier supplier = supplierCompanyIdMap.get(companyId);
		School school = schoolNameMap.get(schoolName);
		Ingredient ingredient = ingredientNameMap.get(dishId + "_" + ingredientName);

		if (dish == null) {
			dish = HibernateUtil.queryDishByName(session, kitchenId, dishName);
			// 如果是調味料就自動新增
			if (dish == null && dishName.equals(CateringServiceUtil.ColumnNameOfSeasoning)) {
				dish = new Dish();
				// seasoning.setDishId(dishId);
				dish.setDishName(dishName);
				dish.setKitchenId(kitchenId);
				dish.setPicturePath("");
				session.save(dish);
			} else if (dish == null) {
				throw new Exception("請先登打菜色'" + dishName + "'資料!");
			}
			dishNameMap.put(dishName, dish);
		}

		dishId = dish.getDishId();
		if (CateringServiceUtil.isEmpty(companyId)) {
			throw new Exception("請先登打供應商資料  供應商統編為空值!");
		}

		if (supplier == null) {
			supplier = HibernateUtil.querySupplierByCompanyId(session, kitchenId, companyId);
			if (supplier == null) {
				throw new Exception("請先登打供應商資料  供應商統編:'" + companyId);
			}
			supplierCompanyIdMap.put(companyId, supplier);
		}

		Integer schoolId = 0;
		// 如果沒有學校名稱就全部update
		if (!schoolName.equals("")) {
			if (school == null) {
				school = HibernateUtil.querySchoolByKitchenAndName(session, kitchenId, schoolName);
				if (school == null) {
					throw new Exception("查無此學校資料請依學校名稱登打:'" + schoolName + " 請依[廚房基本資料管理]之[學校名稱]輸入");
				}
				schoolNameMap.put(schoolName, school);
			}
			schoolId = school.getSchoolId();
		}

		// 如食材資料不存在就自動於食材table 新增一筆
		if (ingredient == null) {
			ingredient = HibernateUtil.queryIngredientByName(session, dishId, ingredientName);
		}
		if (ingredient == null) {
			ingredient = new Ingredient();
			ingredient.setBrand(brand);
			ingredient.setDishId(dishId);
			ingredient.setIngredientName(ingredientName);
			ingredient.setSupplierId(supplier.getId().getSupplierId());
			ingredient.setSupplierCompanyId(supplier.getCompanyId() == null ? "" : supplier.getCompanyId());
			session.save(ingredient);
			ingredientNameMap.put(dishId + "_" + ingredientName, ingredient);
		} /*
		 * else { ingredient.setBrand(brand); ingredient.setDishId(dishId);
		 * ingredient.setIngredientName(ingredientName);
		 * ingredient.setSupplierId(supplier.getId().getSupplierId());
		 * ingredient.setSupplierCompanyId(supplier.getCompanyId() == null ? ""
		 * : supplier.getCompanyId()); session.update(ingredient); }
		 */

		// 依據日期於所有團膳業者 中都新增一筆資料,如果沒有學校名稱就全部新增
		List results = null;
		String HQL = "";
		String uniqueMenu = schoolId + "_" + kitchenId + menuDate.trim();
		results = batchdataMenuMap.get(uniqueMenu);

		if (results == null) {
			if (schoolId == 0) {
				HQL = "from  Batchdata b  where b.kitchenId=:kitchenId and b.menuDate = :menuDate ";
			} else {
				HQL = "from  Batchdata b  where b.kitchenId=:kitchenId and b.menuDate = :menuDate and b.schoolId= :schoolId";
			}

			Query query = session.createQuery(HQL);
			query.setParameter("kitchenId", kitchenId);
			query.setParameter("menuDate", menuDate.trim());
			if (schoolId != 0) {
				query.setParameter("schoolId", schoolId);
			}
			results = query.list();
			batchdataMenuMap.put(uniqueMenu, results);
		}

		if (results.size() == 0) {
			throw new Exception("查無此學校菜單資料  日期:" + menuDate + " 學校:" + schoolName + " 學校代號:" + schoolId + " 帳號ID:" + kitchenId);
		}

		
		Iterator<Batchdata> iterator = results.iterator();
		log.debug("新增食材資料到學校 :" + schoolName + " 日期:" + menuDate);
		while (iterator.hasNext()) {
			// 查看在batchdata and ingredient 中是否有資料
			Batchdata batchdata = (Batchdata) iterator.next();
			// 確認這個ID 是否存在於這一天的菜單中
			List<Long> dishArray = HibernateUtil.getDishIdByBatchdata(batchdata);
			// if(!dishArray.contains(dish.getDishId())){
			if (!dishArray.contains(dish.getDishId()) && !CateringServiceUtil.ColumnNameOfSeasoning.equals(dish.getDishName())) { // 修改
																																	// 加入調味料除外判斷
																																	// 20140324
																																	// KC
				schoolName = HibernateUtil.querySchoolNameById(session, batchdata.getSchoolId());
				throw new Exception("請確認此菜色[" + dishName + "]存在於 學校:" + schoolName + " 日期:" + batchdata.getMenuDate() + " 菜單資料中!");
			}
			// 確認是否有自excel新增過資料
			if (batchdataMap.get(batchdata.getBatchDataId()) == null) {
				// 加入新增紀錄
				batchdataMap.put(batchdata.getBatchDataId(), new ArrayList<Long>());
				log.debug("****刪除菜單資料  BatchdataId" + batchdata.getBatchDataId());
			}
			
			// 先清除此batchdata dishId 的資料,再從excel 中新增
			if (batchdataMap.get(batchdata.getBatchDataId()).contains(dishId) != true) {
				// HibernateUtil.deleteIngredientbatchdataByDishId(session,
				// batchdata.getBatchDataId(), dishId);
				batchdataMap.get(batchdata.getBatchDataId()).add(dishId);
			}
			// 如果在食材檔中不存在就新增至ingredientbatchdata
			Object tmpIngredientbatchdata = HibernateUtil.queryIngredientbatchdataByBatchdataIdAndIngredient(session, batchdata.getBatchDataId(), dishId, ingredient.getIngredientId(), companyId);
			// 重覆的就不新增到ingredientbatchdata
			// 在ingredient 中新增一筆資料
			Ingredientbatchdata ingredientbatchdata = HibernateUtil.saveIngredientbatchdata(session, batchdata.getBatchDataId(), ingredient.getIngredientId(), supplier.getId().getSupplierId(), dishId, lotNumber, ingredientName, brand, stockDate,
					expirationDate, manufactureDate, certificationId, sourceCertification, companyId, origin, source);
			log.debug(schoolName + " 食材新增:" + dishName + " 日期:" + menuDate + " 食材:" + ingredientName + " ID:" + ingredientbatchdata.getIngredientBatchId());
			
			/*
			 * Raymond 20140502
			 * 比對上一批處理的菜單及菜色是否相同
			 * 如為同一筆菜色的食材 就不去處理刪除的食材 等到excel上同菜色的食材新增/修改完之後 在去刪除不再excel上的食材
			 * 若為最後一行excel也會去處理刪除食材
			 */
			
			if(dishId != tmpDishId && batchdata.getBatchDataId() != tmpBatchDataId || !hasNextRow){
				if(!hasNextRow){
					//如果為最後一筆資料 則要將此筆資料加入非例外list處理
					modifyIbId.add(ingredientbatchdata.getIngredientBatchId());
				}
				//判斷是否為第一筆資料(全域變數tmpDishId,tmpBatchDataId ==0)
				if(tmpDishId!=0 && tmpBatchDataId !=0){
					// 針對上一批相同菜單(batchdata) & 相同菜色(dish) 刪除不在excel上的食材
					if (modifyIbId.size() > 0 && batchdata.getBatchDataId() != null && dishId != null) {
						//刪除例外食材(不在modifyIbId List內的都會被刪除)
						//HibernateUtil.removeOtherIngredientbatchdataByIdList(session, modifyIbId, tmpBatchDataId, tmpDishId);
						//將modifyIbId清空
						modifyIbId.clear();
					}
				}
				tmpDishId = dishId;
				tmpBatchDataId = batchdata.getBatchDataId();
			}
			
			modifyIbId.add(ingredientbatchdata.getIngredientBatchId());
			
//			if (tmpIngredientbatchdata == null) {
//				// 在ingredient 中新增一筆資料
//				Ingredientbatchdata ingredientbatchdata = HibernateUtil.saveIngredientbatchdata(session, batchdata.getBatchDataId(), ingredient.getIngredientId(), supplier.getId().getSupplierId(), dishId, lotNumber, ingredientName, brand, stockDate,
//						expirationDate, manufactureDate, certificationId, sourceCertification, companyId, origin, source);
//				log.debug(schoolName + " 食材新增:" + dishName + " 日期:" + menuDate + " 食材:" + ingredientName + " ID:" + ingredientbatchdata.getIngredientBatchId());
//				
//				if(dishId == tmpDishId){
//					modifyIbId.add(ingredientbatchdata.getIngredientBatchId());
//				}else{
//					// 刪除食材
//					if (modifyIbId.size() > 0 && batchdata.getBatchDataId() != null && dishId != null) {
//						HibernateUtil.removeOtherIngredientbatchdataByIdList(session, modifyIbId, batchdata.getBatchDataId(), dishId);
//					}
//				}
//				
//			} else {
//				throw new Exception("Excel中同一食材相同供應商存在兩筆,請刪除相同資料或設定不同批號");
//			}
		}
	}

	// 供應商 Excel 匯入
	public void impSupplierXLSXFile(Session session, int kitchenId, String filename,String[] funcArg) throws Exception {
		InputStream ExcelFileToRead = new FileInputStream(filename);
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		log.info("--->impSupplierXLSXFile:" + filename);
		int rowPoint = 0;
		String suppliedType = "0";
		try {
			Iterator rows = sheet.rowIterator();
			rowPoint = 0;
			List<String> rowTitle = new ArrayList();
			List<String> companyNameList = new ArrayList();
			List<String> companyIdList = new ArrayList();
			// read row
			if (sheet.getPhysicalNumberOfRows() == 0) {
				throw new Exception("請確認Excel檔內只有一個sheet或資料放置於第一個sheet");
			}

//			UseraccountDAO account = new UseraccountDAO(session);
//			List<Useraccount> accountList = (List<Useraccount>)account.findByProperty("KitchenId", kitchenId);
//			String userType = "";
//			
//			if(accountList.size()>0){
//				userType = accountList.get(0).getUsertype();
//			}
			
			//多加參數, 可同時針對傳入的kitchenId一起新增供應商
			String[] otherRestaurantList = null;
			if(funcArg.length>1){
				suppliedType = "1".equals(funcArg[1])?"1":"0";
				otherRestaurantList = (String[])funcArg[2].split(",");
			}
			
			while (rows.hasNext()) {
				log.info("--->impSupplierXLSXFile read row:" + filename + " row:" + rowPoint);
				row = (XSSFRow) rows.next();
				Iterator cells = row.cellIterator();
				int cellPoint = 0;
				// read cell
				// Integer BatchDataId=batchdataId;
				boolean emptyLine = true;
				// --------new dao object----------
				Supplier supplier = new Supplier();
				SupplierId supplierId = new SupplierId();
				supplierId.setSupplierId(null);
				supplierId.setKitchenId(kitchenId);
				supplier.setId(supplierId);
				supplier.setSupplierCertification("");
				supplier.setSupplierTel("");
				supplier.setCompanyId("");
				supplier.setOwnner("");
				supplier.setSupplierAdress("");
				supplier.setSupplierName("");
				supplier.setSuppliedType(suppliedType);
				// --------new object----------
				
				String addr = "";
				while (cells.hasNext()) {
					XSSFCell cell = (XSSFCell) cells.next();
					String cellValue = this.getCellValue(cell);
					log.info("--->Row:" + rowPoint + " Cell:" + cell.getColumnIndex() + " value:" + cellValue);
					if(cellValue != null){
						if(CateringServiceUtil.isVaildStr(cellValue)){
							throw new Exception("上傳檔案第"+(rowPoint + 1) +" 行 資料  "+ cellValue +"  包含特殊字元 \" > < ' % ; & 或者資料有斷行");
						}
					}
					if (rowPoint == 0) {
						rowTitle.add(cellPoint, cellValue);
					} else {
						String titleName = "";
						try {
							titleName = rowTitle.get(cell.getColumnIndex()).trim();
						} catch (Exception e) {
							titleName = "";
						}
						log.debug(titleName + ":" + cellValue);
						if (cellValue == null) {
							cellValue = "";
						}
						if (titleName.equals("供應商名稱") || titleName.equals("供應商名稱*")) {
							if (CateringServiceUtil.isEmpty(cellValue)) {
								emptyLine = true;
							} else {
								emptyLine = false;
								supplier.setSupplierName(cellValue);
							}
						} else if (titleName.equals("負責人") || titleName.equals("聯絡人姓名*")) {
							supplier.setOwnner(cellValue);
						} else if (titleName.equals("公司統編") || titleName.equals("供應商統編*")) {
							supplier.setCompanyId(cellValue);
						} else if (titleName.equals("縣市*") || titleName.equals("鄉鎮市區*")) {
							addr += cellValue; //temp
						} else if (titleName.equals("地址")) {
							supplier.setSupplierAdress(cellValue);
						} else if (titleName.equals("地址*")) {
							if(!CateringServiceUtil.isEmpty(cellValue)){
								supplier.setSupplierAdress(addr);
							} else {
								//如果為空值,塞空值讓前方報錯
								supplier.setSupplierAdress(cellValue);
							}
							addr = "";
						} else if (titleName.equals("電話") || titleName.equals("聯絡電話*")) {
							supplier.setSupplierTel(cellValue);
						} else if (titleName.equals("認證") || titleName.equals("認證標章")) {
							if (CateringServiceUtil.isEmpty(cellValue)) {
								supplier.setSupplierCertification("");
							} else {
								supplier.setSupplierCertification(cellValue);
							}
						}
					}
					cellPoint++;
				}
				if (rowPoint > 0 && emptyLine == false) {
					log.info("=======>" + supplier.getSupplierName() + " " + supplier.getCompanyId() + " " + supplier.getSupplierAdress() + " " + supplier.getSupplierTel() + " " + supplier.getSupplierCertification());
					insertSupplierFromExcel(session, kitchenId, companyNameList, companyIdList, supplier);
					if(otherRestaurantList != null){
						for(int i=0;i<otherRestaurantList.length;i++){
							Integer newkitchenId = Integer.parseInt(otherRestaurantList[i]);
							insertSupplierFromExcel(session, newkitchenId, companyNameList, companyIdList, supplier);
						}
					}
					successRowCount = successRowCount + 1; // 增加判斷處理成功的總列數
															// 20140418 KC
				}

				rowPoint++;
			}
			
			ExcelFileToRead.close();

		} catch (Exception e) {
			ExcelFileToRead.close();
			e.printStackTrace();
			throw new Exception(" 錯誤行數:" + (rowPoint + 1) + " 問題:\n" + e.getMessage());
		}
	}

	private void insertSupplierFromExcel(Session session, int kitchenId, List<String> companyNameList, List<String> companyIdList, Supplier supplier) throws Exception {
		if (supplier.getSupplierAdress().equals("")) {
			throw new Exception("此Excel 中有供應商地址為空白 ");
		}
		if (supplier.getOwnner().equals("")) {
			throw new Exception("此Excel 中有供應商負責人為空白 ");
		}
		if (supplier.getSupplierTel().equals("")) {
			throw new Exception("此Excel 中有供應商電話為空白 ");
		}
		if (supplier.getSupplierName().equals("")) {
			throw new Exception("此Excel 中有供應商名稱為空白 ");
		}
		if (supplier.getCompanyId().equals("")) {
			throw new Exception("此Excel 中有供應商公司統編為空白 ");
		}
		Supplier tmpSupplier = HibernateUtil.querySupplierByCompanyId(session, kitchenId, supplier.getCompanyId());

		if (!companyIdList.contains(kitchenId + "-" + supplier.getCompanyId())) {
			companyIdList.add(kitchenId + "-" + supplier.getCompanyId());
		} else {
			throw new Exception("此Excel 檔出現兩筆相同公司統編 " + supplier.getCompanyId());
		}
		if (!companyNameList.contains(kitchenId + "-" + supplier.getSupplierName())) {
			companyNameList.add(kitchenId + "-" + supplier.getSupplierName());
		} else {
			throw new Exception("此Excel 檔出現兩筆相同公司名稱 " + supplier.getSupplierName());
		}

		if (tmpSupplier == null) {
			Supplier tmpSupplierName = HibernateUtil.querySupplierByName(session, kitchenId, supplier.getSupplierName());
			if (tmpSupplierName != null) {
				if (!tmpSupplierName.equals(supplier.getCompanyId())) {
					throw new Exception("供應商資料檔已有相同公司名稱 但 不同統編 " + supplier.getSupplierName());
				}
			}

			String hqlInsert = "insert into supplier (supplierName, ownner,companyId,supplierAdress,supplierTel,supplierCertification,kitchenId,suppliedType) values" + "(:supplierName, " + ":ownner," + ":companyId," + ":supplierAdress," + ":supplierTel,"
					+ ":supplierCertification," + ":kitchenId," + ":suppliedType)";
			// Query query = session.createQuery( hqlInsert );
			log.debug("Insert Supplier SQL:" + hqlInsert);
			log.debug("add cer:" + supplier.getSupplierCertification());
			session.createSQLQuery(hqlInsert).setParameter("supplierName", supplier.getSupplierName()).setParameter("ownner", supplier.getOwnner()).setParameter("companyId", supplier.getCompanyId())
					.setParameter("supplierAdress", supplier.getSupplierAdress()).setParameter("supplierTel", supplier.getSupplierTel()).setParameter("supplierCertification", supplier.getSupplierCertification()).setParameter("kitchenId", kitchenId).setParameter("suppliedType", supplier.getSuppliedType())
					.executeUpdate();
		} else {
			session.evict(tmpSupplier);
			tmpSupplier.setSupplierName(supplier.getSupplierName());
			tmpSupplier.setCompanyId(supplier.getCompanyId());
			tmpSupplier.setOwnner(supplier.getOwnner());
			tmpSupplier.setSupplierAdress(supplier.getSupplierAdress());
			tmpSupplier.setSupplierCertification(supplier.getSupplierCertification());
			tmpSupplier.setSupplierTel(supplier.getSupplierTel());
			tmpSupplier.setSuppliedType(supplier.getSuppliedType());
			session.update(tmpSupplier);
			log.debug("del cer:" + supplier.getSupplierCertification());
		}
	}

	// 菜色Excel檔匯入
	public void impVegetableXLSXFile(Session dbsession, int kitchenId, String filename) throws Exception {
		InputStream ExcelFileToRead = new FileInputStream(filename);
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		int rowPoint = 0;
		// XSSFCell cell;

		// 確認dishid 中今天新增了那些ingredient
		HashMap<Long, List<Long>> dishMap = new HashMap<Long, List<Long>>();
		try {
			Iterator rows = sheet.rowIterator();
			rowPoint = 0;
			List<String> rowTitle = new ArrayList();
			// read row
			if (sheet.getPhysicalNumberOfRows() == 0) {
				throw new Exception("請確認Excel檔內只有一個sheet或資料放置於第一個sheet");
			}
			while (rows.hasNext()) {
				row = (XSSFRow) rows.next();
				Iterator cells = row.cellIterator();
				int cellPoint = 0;
				// read cell
				// Integer BatchDataId=batchdataId;
				boolean emptyLine = true;
				// --------new dao object----------
				String dishName = "";
				String ingredientName = "";
				String supplierName = "";
				String brand = "";
				String productName= "";
				String manufacturer = "";
				// --------new object----------

				while (cells.hasNext()) {
					XSSFCell cell = (XSSFCell) cells.next();
					String cellValue = this.getCellValue(cell);
					if(cellValue != null){
						if(CateringServiceUtil.isVaildStr(cellValue)){
							throw new Exception("上傳檔案第"+(rowPoint + 1) +" 行 資料  "+ cellValue +"  包含特殊字元 \" > < ' % ; & + 或者資料有斷行");
						}
					}
//					cellValue = cellValue != null ? cellValue.replaceAll("[\"|>|<|'|%|;|&|+]", "") : null; //過濾資料含有html特殊字元
					// log.debug("==>>row:"+rowPoint+" cell:"+cellPoint
					// +" value:"+cellValue);
					// log.debug("**>>row:"+rowPoint+" cell:"+cell.getColumnIndex()
					// +" value:"+cellValue);
					if (rowPoint == 0) {
						rowTitle.add(cellPoint, cellValue.trim());
					} else {

						String titleName = "";
						try {
							titleName = rowTitle.get(cell.getColumnIndex()).trim();
						} catch (Exception e) {
							titleName = "";
						}
						log.debug(titleName + ":" + cellValue);
						if (titleName.equals("菜色名稱")) {
							if (CateringServiceUtil.isEmpty(cellValue)) {
								emptyLine = true;
							} else {
								emptyLine = false;
								dishName = cellValue == null ? "" : cellValue.trim();
							}
						} else if (titleName.equals("食材名稱")) {
							ingredientName = cellValue == null ? "" : cellValue;
						} else if (titleName.equals("供應商名稱")) {
							supplierName = cellValue == null ? "" : cellValue;
						} else if (titleName.equals("產品名稱")) {
							productName = cellValue == null ? "" : cellValue;
						} else if (titleName.equals("製造商名稱")) {
							manufacturer = cellValue == null ? "" : cellValue;
						}
					}
					cellPoint++;
				}
				if (rowPoint > 0 && emptyLine == false) {
					insertVegetableFromXLSX(dbsession, kitchenId, dishMap, dishName, ingredientName, supplierName, productName, manufacturer);
					successRowCount = successRowCount + 1; // 增加判斷處理成功的總列數
															// 20140418 KC
				}
				// 如要清除非必要ingredient 可利用 dishMap(目前保留中)
				// dishMap
				rowPoint++;
			}
		} catch (Exception e) {
			ExcelFileToRead.close();
			e.printStackTrace();
			// throw new Exception(e.getMessage()+" 錯誤行數:"+(rowPoint+1));
			throw new Exception(" 錯誤行數:" + (rowPoint + 1) + " 問題:\n" + e.getMessage());
		}
	}

	private void insertVegetableFromXLSX(Session dbsession, int kitchenId, HashMap<Long, List<Long>> dishMap, String dishName, String ingredientName, String supplierName, String productName, String manufacturer) throws Exception {
		if (dishName.equals("")) {
			throw new Exception("菜色名稱為空值");
		}
		if (ingredientName.equals("")) {
			throw new Exception("食材名稱為空值");
		}
		if (supplierName.equals("")) {
			throw new Exception("供應商名稱為空值");
		}
		/*
		 * if (brand.equals("")) { throw new Exception("品牌(製造商)為空值"); }
		 */

		// 確認供應商資料
		Supplier supplier = HibernateUtil.querySupplierByName(dbsession, kitchenId, supplierName);
		if (supplier == null) {
			throw new Exception("找不到供應商資料:" + supplierName);
		}
		Integer supplierId = supplier.getId().getSupplierId();

		// 新增菜色
		Dish dish = HibernateUtil.queryDishByName(dbsession, kitchenId, dishName);
		// 如果找不到這個菜色就新增一筆
		if (dish == null) {
			dish = new Dish();
			dish.setDishName(dishName);
			dish.setDishShowName(dishName);
			dish.setKitchenId(kitchenId);
			dish.setPicturePath("");
			System.out.println("新增菜色" + dishName);
			dbsession.save(dish);
		}
		Long dishId = dish.getDishId();
		// ---新增到mem中紀錄這資的匯入資料
		if (dishMap.get(dishId) == null) {
			List<Long> ingredientList = new ArrayList<Long>();
			dishMap.put(dishId, ingredientList);
		}

		// 新增食材
		Ingredient ingredient = HibernateUtil.queryIngredientByName(dbsession, dishId, ingredientName);
		if (ingredient == null) {
			ingredient = new Ingredient();
			ingredient.setBrand("");
			ingredient.setDishId(dishId);
			ingredient.setIngredientName(ingredientName);
			ingredient.setSupplierId(supplierId);
			ingredient.setSupplierCompanyId(supplier.getCompanyId());
			ingredient.setProductName(productName);
			ingredient.setManufacturer(manufacturer);
			dbsession.save(ingredient);
		} else {
			ingredient.setBrand("");
			// ingredient.setDishId(dishId);
			// ingredient.setIngredientName(ingredientName);
			ingredient.setSupplierId(supplierId);
			ingredient.setSupplierCompanyId(supplier.getCompanyId());
			ingredient.setProductName(productName);
			ingredient.setManufacturer(manufacturer);
			dbsession.update(ingredient);
		}
		// 紀錄下所有dish 這次新增的ingredientId
		if (dishMap.get(dishId).contains(ingredient.getIngredientId()) != true) {
			dishMap.get(dishId).add(ingredient.getIngredientId());
		}
	}

	// 產出Excel 檔主程式
	public void writeXLSXFile() throws Exception {
		String fileXls = CateringServiceUtil.converTimestampToStr("yyyyMMddHHmmss", CateringServiceUtil.getCurrentTimestamp()) + ".xls";
		String excelFileName = CateringServiceUtil.getConfig("uploadPath") + "tmp/" + fileXls;
		String sheetName = "Sheet1";// name of sheet
		log.debug("Write to Excel file:" + excelFileName);
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		// iterating r number of rows
		for (int r = 0; r < 5; r++) {
			XSSFRow row = sheet.createRow(r);
			// iterating c number of columns
			for (int c = 0; c < 5; c++) {
				XSSFCell cell = row.createCell(c);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue("Cell " + r + " " + c);
			}
		}

		FileOutputStream fileOut = new FileOutputStream(excelFileName);
		// write this workbook to an Outputstream.
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

	public Long getDishIdbyName(Session session, int kid, String name) throws Exception {
		if (name.trim().equals("")) {
			return (long)0;
		}
		String HQL = "from Dish d WHERE kitchenId = :kitchenid AND dishName = :dishname";
		Query query = session.createQuery(HQL);
		query.setParameter("kitchenid", kid);
		query.setParameter("dishname", name);
		List results = query.list();
		Iterator<Dish> iterator = results.iterator();
		while (iterator.hasNext()) {
			Dish obj = iterator.next();
			return obj.getDishId();
		}
		return null;
		// throw new Exception("此供應商找不到菜色,請於'菜色維護'中新增菜色:" + name);
	}

	public int getSchoolIdByKidAndschoolName(Session session, int kid, String name) throws Exception {
		String HQL = "from Kitchen k , Schoolkitchen sk , School s where sk.id.kitchenId = k.kitchenId and sk.id.schoolId = s.schoolId  and s.schoolName = :schoolName and k.kitchenId = :kitchenId "
				+ " and sk.offered = 1"; //增加現況供餐條件 20160107 add by Ellis
		Query query = session.createQuery(HQL);
		query.setParameter("schoolName", StringUtils.trim(name));
		query.setParameter("kitchenId", kid);
		List results = query.list();

		if (results.size() == 0) {
			Kitchen kitchen = HibernateUtil.queryKitchenById(session, kid);
			throw new Exception("此團膳業者或自設廚房找不到學校資料,請於'廚房基本資料管理'中新增/勾選供餐  學校:[" + name + "] 目前登入之團膳業者或自立廚房:" + kitchen.getKitchenName() + " ID:" + kid);
		}

		Iterator<Object[]> iterator = results.iterator();
		while (iterator.hasNext()) {
			Object[] obj = iterator.next();
			School school = (School) obj[2];
			return school.getSchoolId();
		}
		Kitchen kitchen = HibernateUtil.queryKitchenById(session, kid);
		// throw new Exception("此團膳業者或自立廚房找不到學校資料  學校:[" + name + "] 團膳業者或自立廚房:"
		// +
		// kitchen.getKitchenName()+" ID:"+kid);

		throw new Exception("此團膳業者或自設廚房找不到學校資料,請於'廚房基本資料管理'中新增/勾選供餐 學校:[" + name + "] 目前登入之團膳業者或自立廚房ID:" + kid);
	}

	public String getExtension(String fileName) {
		String extension = "";
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) {
			extension = fileName.substring(i + 1);
		}
		return extension.toLowerCase();
	}

	public int findBatchdata(Session session, int Kid, int Sid, String date, String lotNumber, int DishId, String ingredientName) throws Exception {
		String HQL = "from  Batchdata b,Ingredientbatchdata i where b.batchDataId = i.batchDataId and  b.kitchenId=:kitchenId and b.menuDate = :menuDate  and b.lotNumber = :lotNumber and b.schoolId = :schoolId and i.dishId=:dishId and i.ingredientName= :ingredientName";
		Query query = session.createQuery(HQL);
		query.setParameter("kitchenId", Kid);
		query.setParameter("menuDate", date);
		query.setParameter("schoolId", Sid);
		query.setParameter("lotNumber", lotNumber);
		query.setParameter("dishId", DishId);
		query.setParameter("ingredientName", ingredientName);
		List results = query.list();
		return results.size();
	}

	// 學校食材匯入
	/*
	 * public void insSchoolIngredient(Session session, String schoolName,
	 * String dishName, Integer IngredientId, String ingredientName, String
	 * StockDate, String ManufactureDate, String ExpirationDate, String
	 * LotNumber, String brand, String Origin, String Source, String companyId,
	 * String SourceCertification, String CertificationId, Integer kitchenId,
	 * String MenuDate) throws Exception {
	 * 
	 * Long dishId = this.getDishIdbyName(session, kitchenId, dishName); if
	 * (dishId == null) { throw new Exception("請先於'菜色維護'中登打菜色'" + dishName +
	 * "'資料"); } if (CateringServiceUtil.isEmpty(companyId)) { throw new
	 * Exception("請先登打供應商資料  供應商統編為空值!"); } Supplier supplier =
	 * HibernateUtil.querySupplierByCompanyId(session, kitchenId, companyId); if
	 * (supplier == null) { throw new Exception("請先於'供應商維護'中登打供應商資料  供應商統編:'" +
	 * companyId); } School school =
	 * HibernateUtil.querySchoolByKitchenAndName(session, kitchenId,
	 * schoolName); if (school == null) { throw new
	 * Exception("查無此學校資料請依學校名稱, 請先於'業者資料管理中'登打 學校:'" + schoolName); } LotNumber
	 * = CateringServiceUtil.isEmpty(LotNumber) ?
	 * CateringServiceUtil.defaultLotNumber : LotNumber.trim(); //
	 * 如食材資料不存在就自動於食材table 新增一筆 Ingredient ingredient =
	 * HibernateUtil.queryIngredientByName(session, dishId, ingredientName); if
	 * (ingredient == null) { ingredient = new Ingredient();
	 * ingredient.setBrand(brand); ingredient.setDishId(dishId);
	 * ingredient.setIngredientName(ingredientName);
	 * ingredient.setSupplierId(supplier.getId().getSupplierId());
	 * ingredient.setSupplierCompanyId(supplier.getCompanyId());
	 * session.save(ingredient); } // 依據日期於所有團膳業者 中都新增一筆資料 String HQL =
	 * "from  Batchdata b  where b.kitchenId=:kitchenId and b.menuDate = :menuDate and b.schoolId= :schoolId"
	 * ; Query query = session.createQuery(HQL); query.setParameter("kitchenId",
	 * kitchenId); query.setParameter("menuDate", MenuDate);
	 * query.setParameter("schoolId", school.getSchoolId()); List results =
	 * query.list(); Iterator<Batchdata> iterator = results.iterator();
	 * log.debug("新增食材資料到學校 :" + schoolName + " 日期:" + MenuDate); while
	 * (iterator.hasNext()) { // 查看在batchdata and ingredient 中是否有資料 Batchdata
	 * batchdata = (Batchdata) iterator.next(); Ingredientbatchdata
	 * ingredientbatchdata =
	 * HibernateUtil.queryIngredientbatchdataByBatchdataIdAndDish( session,
	 * batchdata.getBatchDataId(), dishId); if (ingredientbatchdata == null) {
	 * // 在ingredient 中新增一筆資料 ingredientbatchdata = new Ingredientbatchdata();
	 * ingredientbatchdata = HibernateUtil.saveIngredientbatchdata(session,
	 * batchdata.getBatchDataId(), ingredient.getIngredientId(),
	 * supplier.getId().getSupplierId(), dishId, LotNumber, ingredientName,
	 * brand, StockDate, ExpirationDate, ManufactureDate, CertificationId,
	 * SourceCertification, companyId, Origin, Source); log.debug(schoolName +
	 * " 食材新增:" + dishName + " 日期:" + MenuDate + " 食材:" + ingredientName +
	 * " ID:" + ingredientbatchdata.getIngredientBatchId()); } else {
	 * log.debug(schoolName + " 食材已有資料:" + ingredientName + " 日期:" + MenuDate +
	 * " 菜色:" + ingredientName + " ID:" + dishId + " !"); } } }
	 */
	public Long queryDishIdByNameAndUserType(String dishName, String titleName) throws Exception {
		if (dishName.trim().equals("")) {
			return (long)0;
		}
		Long dishId = this.dishMap.get(dishName);
		if (dishId != null) {
			return dishId;
		}

		Criteria criteria = this.getSession().createCriteria(Dish.class);
		criteria.add(Restrictions.eq("kitchenId", this.getKitchenId()));
		criteria.add(Restrictions.eq("dishName", dishName));
		List results = criteria.list();
		Iterator<Dish> iterator = results.iterator();

		while (iterator.hasNext()) {
			Dish obj = iterator.next();
			log.debug("queryDishIdByNameAndUserType Id:" + obj.getDishId());
			this.dishMap.put(dishName, obj.getDishId());
			return obj.getDishId();
		}
		/*
		 * // 如果為自立廚房就自動新增菜色資料 if
		 * (userType.equals(CateringServiceUtil.KitchenType006)) { Dish newDish
		 * = new Dish(); newDish.setDishName(dishName);
		 * newDish.setKitchenId(this.getKitchenId());
		 * newDish.setPicturePath(""); this.getSession().save(newDish); dishId =
		 * newDish.getDishId(); log.debug("queryDishIdByNameAndUserType newId:"
		 * + dishId); } else { Kitchen tmpKitchen =
		 * HibernateUtil.queryKitchenById(this.getSession(),
		 * this.getKitchenId()); throw new Exception("此業者/自立廚房找不到 " + titleName
		 * + ":[" + dishName + "] 業者/自立廚房:" + tmpKitchen.getKitchenName()); }
		 */
		// 20140402 改為無論是否為自立廚房excel upload 都會自動新增菜色
		Dish newDish = new Dish();
		newDish.setDishName(dishName);
		newDish.setKitchenId(this.getKitchenId());
		newDish.setPicturePath("");
		this.getSession().save(newDish);
		dishId = newDish.getDishId();
		log.debug("queryDishIdByNameAndUserType newId:" + dishId);

		this.dishMap.put(dishName, dishId);
		return dishId;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	public Integer getKitchenId() {
		return kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public boolean isMenuUploadTime(String menuDate,Timestamp nowTS,Integer schoolId) throws Exception {
		//先關掉 20140807 KC
		//return true;
		
		//改用util 判斷  20140721 KC
		
		String limitString="";
		if (uploadLimitMap.containsKey(schoolId)){
			limitString=uploadLimitMap.get(schoolId);
		}else{
			limitString=SchoolAndKitchenUtil.queryUploadLimitTimeBySchoolid(getSession(),schoolId);
			uploadLimitMap.put(schoolId, limitString);
		}
		if ("".equals(limitString)){
			return true;
		}
		return CateringServiceUtil.isUploadTime(CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", menuDate),nowTS, limitString);
	
		/*
		Date today = new Date();

		Timestamp todayLimit = CateringServiceUtil.convertStrToTimestamp("yyyy-MM-dd HH:mm:ss", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(today)));

		if (dateTS.getTime() > todayLimit.getTime()) {
			return false;
		} else {
			return true;
		}
		*/
	}

	// 增加檢核excel第一列標題格式 20140418 KC
	public Boolean checkHeaderFormat(String filename, String funcName) throws Exception {

		if (CateringServiceCode.FileTypeOfSupplierExcel.equals(funcName)) {
			boolean r = false;
			//V1 版本
			r = checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_SUPPLIER);
			
			//發現如果非第一版 在檢查是否為第二版
			if(!r){
				//V2版本
				r = checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_SUPPLIER_V2);
			}
			return r;
		}
		
		//20140724 Raymond 新增檢核新版食材上傳
		if (CateringServiceCode.FileTypeOfIngredientExcel.equals(funcName)) {
			boolean r = false;
			//V1 版本
			r = checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_INGREDIENT);
			
			//發現如果非第一版 在檢查是否為第二版
			if(!r){
				//V2版本
				r = checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_INGREDIENT_V2);
			}
			//檢查若不為前兩版本，則判定是否為第三版 add by ellis 20141126
			if (!r) {
				r = checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_INGREDIENT_V3);
			}
			
			if (!r) {
				r = checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_INGREDIENT_V4);
			}
			return r;
		}
		
		if (CateringServiceCode.FileTypeOfMenuExcel.equals(funcName)) {
			return checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_MENU);
		}
		if (CateringServiceCode.FileTypeOfDishExcel.equals(funcName)) {
			return checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_DISH);
		}
		if (CateringServiceCode.FileTypeOfSchoolKitchenAccountExcel.equals(funcName)) {
			return checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_SCHKITCHENACCOUNT);
		}
		return false;
		// throw new Exception("上傳發生錯誤!(1)");
	}

	private Boolean checkExcelTitle(String filename, List<String> headerList) throws Exception {
		InputStream ExcelFileToRead = new FileInputStream(filename);
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);

		// 檢查資料表
		if (sheet.getPhysicalNumberOfRows() == 0) {
			throw new Exception("請確認Excel檔內只有一個sheet或資料放置於第一個sheet");
		}

		XSSFRow row = sheet.getRow(0); // 取第一行

		Iterator cells = row.cellIterator();
		List<String> excelTitleList = new ArrayList<String>();

		// 取出excel中的欄位名稱
		while (cells.hasNext()) {
			XSSFCell cell = (XSSFCell) cells.next();
			String cellValue = this.getCellValue(cell);
			excelTitleList.add(cellValue);
		}

		// 比對欄位名稱是否都有在
		Iterator<String> irHeaderList = headerList.iterator();
		while (irHeaderList.hasNext()) {
			if (!excelTitleList.contains(irHeaderList.next())) {
				return false; // 只要有缺漏的欄位就直接丟回
			}
		}

		return true;
	}
	
	public boolean isIngredientV2(String filename) throws Exception{
		//V2版本
		return checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_INGREDIENT_V2);
	}
	
	public boolean isIngredientV3(String filename) throws Exception{
		//V3版本
		return checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_INGREDIENT_V3);
	}

	public boolean isIngredientV4(String filename) throws Exception{
		//V3版本
		return checkExcelTitle(filename, CateringServiceCode.EXCEL_HEADER_INGREDIENT_V4);
	}
}
