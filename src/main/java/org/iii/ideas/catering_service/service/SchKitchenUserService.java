package org.iii.ideas.catering_service.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
//import org.hibernate.Transaction;
import org.iii.ideas.catering_service.code.XlsTitleCode;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.dao.AccountDao;
import org.iii.ideas.catering_service.dao.Area;
import org.iii.ideas.catering_service.dao.AreaDAO;
import org.iii.ideas.catering_service.dao.CodeDAO;
import org.iii.ideas.catering_service.dao.County;
import org.iii.ideas.catering_service.dao.CountyDAO;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.KitchenDAO;
//import org.iii.ideas.catering_service.dao.School;
//import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.UseraccountDAO;
import org.iii.ideas.catering_service.rest.bo.SchKitchenUserBO;
import org.iii.ideas.catering_service.rest.bo.SchKitchenUserContentBO;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
//import org.iii.ideas.catering_service.util.DateUtil;
import org.iii.ideas.catering_service.util.ExcelUtil;
import org.iii.ideas.catering_service.util.LogUtil;

public class SchKitchenUserService {
	protected Logger log = Logger.getLogger(this.getClass());
	private Session dbSession;
	// 成功筆數
	private int success = 0;
	// 失敗筆數
	private int fail = 0;

	private Integer county = 0;

	public SchKitchenUserService() {

	}

	public SchKitchenUserService(Session dbSession) {
		this.dbSession = dbSession;
	}

	public String uploadSchKitUserFromXls(String fileName, Integer county, String loginUserName, String loginUserType) throws Exception {
		this.county = county;
		InputStream ExcelFileToRead = new FileInputStream(fileName);
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row;
		String rowErrMsg = "";
		List<String> xlsTitle = new ArrayList<String>();

//		String currenTimeString = DateUtil.getSystemDate("yyyy/MM/dd HH:mm:ss");
		Iterator<Row> rows = sheet.rowIterator();
		// 開始處理Excel row data
		int rowCount = 0;
		int columnCount = 0;
		while (rows.hasNext()) {
			rowCount ++;
			row = (XSSFRow) rows.next();
			SchKitchenUserBO bo = new SchKitchenUserBO();
			String cellErrMsg = "";
			int rowNum = row.getRowNum() + 1; //實際對應Excel的行數
			// 將excel欄位塞入SchKitchenUserBO
			if (rowCount == 1) {
				columnCount = row.getLastCellNum();
			}
			for (int i = 0; i < columnCount; i++) {
				XSSFCell cell = (XSSFCell) row.getCell(i, Row.RETURN_BLANK_AS_NULL);// 特別處理空值(blank)狀況
				if (row.getRowNum() == 0) {
					xlsTitle.add(ExcelUtil.getCellValue(cell));
				} else {
					String titleName = xlsTitle.get(i);
					try {
						// 基本檢核欄位並將各欄位值放入bo
						bo = validateSchKitUserXlxCellValue(bo, titleName, ExcelUtil.getCellValue(cell));
					} catch (Exception ex) {
						// 儲存Cell錯誤訊息
						cellErrMsg = LogUtil.putErrorMsg(cellErrMsg, ex.getMessage());
					}

				}
			}

			if (!CateringServiceUtil.isEmpty(cellErrMsg)) {
				rowErrMsg = LogUtil.putErrorMsg(rowErrMsg, "第:" + rowNum + "行 [" + cellErrMsg + "]</br>");
				fail++;
			} else {
				// 跳過第一筆表頭
				if (row.getRowNum() > 0) {
					try {
						// 開始寫入資料庫
						String err = insertSchKitUserXlsRowData(bo, loginUserName, loginUserType);
						if (err == null) {
							success++;
						} else {
							fail++;
							rowErrMsg = LogUtil.putErrorMsg(rowErrMsg, "第:" + rowNum + "行 [" + err + "]</br>");
						}

					} catch (Exception ex) {
						fail++;
						rowErrMsg = LogUtil.putErrorMsg(rowErrMsg, "第:" + rowNum + "行 [" + ex.getMessage() + "]</br>");
					}
				}
			}
		}

		return rowErrMsg;

	}

	// 處理學校廚房帳號Excel row data
	public String insertSchKitUserXlsRowData(SchKitchenUserBO bo, String loginUserName, String loginUserType) {
		String errMsg = "";
		String countyCode = "";
//		String schoolCode = "";
//		String shortUsername = "";
		String username = "";

		// 檢查學校是否存在
//		SchoolDAO schoolDao = new SchoolDAO(dbSession);
//		if (schoolDao.querySchoolBySchoolCode(bo.getSchoolCode()) != null) {
//			errMsg = LogUtil.putErrorMsg(errMsg, "學校代碼:此學校代碼已經存在");
//		}else{
//			//先去除縣市名區域名 免得重覆   20140519 KC
//			String shortName=bo.getSchoolName();	
//			shortName=shortName.replace(bo.getAreaName(), ""); 
//			shortName=shortName.replace(bo.getCountyName(), ""); 
//			
//			String fullSchName = bo.getCountyName()+bo.getAreaName()+shortName;
//			School school = schoolDao.querySchoolBySchoolName(fullSchName);
//			if (school != null) {
//				errMsg = LogUtil.putErrorMsg(errMsg, "學校名稱:此學校名稱已經存在");
//			}else{
//				bo.setSchoolName(fullSchName);
//			}
//		}

		// 取縣市對應代碼
		CountyDAO countyDao = new CountyDAO(dbSession);
		County county = countyDao.getCountyByName(bo.getCountyName());

		CodeDAO codeDao = new CodeDAO(dbSession);
		if (county != null) {
			// 檢核是否為新增管轄縣市帳號 && #13522 權限判斷，若為管理者帳號(type=11)，應可自由新增各縣市之學校帳號資料。
			if (county.getCountyId() != this.county && !CateringServiceCode.USERTYPE_GOV_CENTRAL.equals(loginUserType)) {
				errMsg = LogUtil.putErrorMsg(errMsg, "縣市別:無法新增非管轄縣市帳號");
			} else {
				bo.setCountyId(county.getCountyId());
				// 取縣市對應代碼
				countyCode = codeDao.getCodeByTypeAndName(String.valueOf(bo.getCountyId()), "county");
			}
		} else {
			errMsg = LogUtil.putErrorMsg(errMsg, "縣市別:查無此縣市別");
		}

		// 市區別
		AreaDAO areaDao = new AreaDAO(dbSession);
		Area area = areaDao.getAreaByName(bo.getAreaName(),bo.getCountyId());
		if (area != null) {
			bo.setAreaId(area.getAreaId());
		} else {
			errMsg = LogUtil.putErrorMsg(errMsg, "市區別:查無此市區別");
		}

		// 取得account(縣市代碼+學校代碼)
		// 組schoolCode 如不滿六位 左補0
//		schoolCode = String.format("%06d", bo.getSchoolId());
		if (!isEmpty(countyCode)) {
			username = countyCode.concat(bo.getSchoolCode());
			//bo.setSchoolCode(schoolCode);
			bo.setUsername(username);
		} 

		// 補處理schoolName,這一段邏輯在listSchool.js,從網頁新增時前端處理,但Excel新增則無
		if (bo.getSchoolCode().length() >= 6 || "009".equals(bo.getContents().getType())) {
			String schoolName = bo.getCountyName() + bo.getAreaName() + bo.getSchoolName();
			bo.setSchoolName(schoolName);
		}
		
		// 檢核帳號是否存在,如已存在則不能新增
		Useraccount user = null;
		UseraccountDAO userDAO = new UseraccountDAO(dbSession);
		
		if(CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(bo.getContents().getType())){
			user = userDAO.queryUseraccountByUsername(username+"-SHOP");
		}else{
			user = userDAO.queryUseraccountByUsername(username);
		}
		if(!CateringServiceUtil.isNull(user)){
			errMsg = LogUtil.putErrorMsg(errMsg, "帳號已經存在");
		}
		
		// 檢核學校廚房帳號是否存在,如已存在則不能新增
		KitchenDAO kitDao = new KitchenDAO(dbSession);
		String kitchenName = bo.getSchoolName();
		if(CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(bo.getContents().getType())){
			kitchenName = bo.getSchoolName() +"-員生消費合作社";
		}
		Kitchen kitchen = kitDao.queryKitchenByKitchenName(kitchenName);
		if(kitchen != null){
			errMsg = LogUtil.putErrorMsg(errMsg, "新增帳號失敗！該學校廚房帳號已存在，無法重覆開立。");
		}
		
		if (!isEmpty(errMsg))
			return errMsg;
		

//		bo.getContents().setRole("kSch");
		// Transaction tx = dbSession.beginTransaction();
		// 開始存資料
		AccountDao accountDao = new AccountDao(dbSession);
		try {
			bo.setCreateUser(loginUserName); //建立者
			bo = accountDao.updateSchoolKitchenAccount(bo, "add");
		} catch (Exception ex) {
			// tx.rollback();
			return ex.getMessage();
		}
		// tx.commit();

		return null;
	}

	// 初步檢核Excel欄位
	public SchKitchenUserBO validateSchKitUserXlxCellValue(SchKitchenUserBO bo, String titleName, String cellValue) throws Exception {
		Common com = new Common();
		if (bo.getContents() == null) {
			SchKitchenUserContentBO contents = new SchKitchenUserContentBO();
			bo.setContents(contents);
		}

		if (cellValue != null){
			cellValue = cellValue.trim();
		}
		titleName = titleName.trim();
		if (XlsTitleCode.SCHKITUSER_COUNTY.equals(titleName)) {
			if (cellValue != null && cellValue.length() > 0) {
				bo.setCountyName(cellValue);
			} else {
				throw new Exception("縣市別不可為空");
			}
		} else if (XlsTitleCode.SCHKITUSER_AREA.equals(titleName)) {
			if (cellValue != null && cellValue.length() > 0) {
				bo.setAreaName(cellValue);
			} else {
				throw new Exception("市區別不可為空");
			}
		} else if (XlsTitleCode.SCHKITUSER_SCHOOLNAME.equals(titleName)) {
			if (cellValue != null && cellValue.length() > 0) {
				bo.setSchoolName(cellValue);
			} else {
				throw new Exception("學校名稱不可為空");
			}
		} else if (XlsTitleCode.SCHKITUSER_SCHOOLCODE.equals(titleName)) {
			// #13522 學校編號要限制只能4碼或6碼的數字, 管理者輸入什麼編號,資料庫就要存該編號不需要補0
			if (cellValue != null && cellValue.length() > 0) {
				if (cellValue.length() == 4 || cellValue.length() == 6) {
					bo.setSchoolCode(cellValue);
				} else {
					throw new Exception("學校編號需為4碼或6碼");
				}
			} else {
				throw new Exception("學校編號不可為空");
			}
		}
		// 使用者可選擇不新增帳號,以下4欄位非必須輸入
		else if (XlsTitleCode.SCHKITUSER_PASSWORD.equals(titleName)) {
			if (cellValue != null && cellValue.length() > 0) {
				bo.setPassword(com.getEncoderByMd5(cellValue));
			} else {
				bo.setPassword("");
			}
		} else if (XlsTitleCode.SCHKITUSER_EMAIL.equals(titleName)) {
			if (cellValue != null && cellValue.length() > 0) {
				bo.getContents().setEmail(cellValue);
			} else {
				bo.getContents().setEmail("");
			}
		} else if (XlsTitleCode.SCHKITUSER_ACCOUNTTYPE.equals(titleName)) {
			if (cellValue != null && cellValue.length() > 0) {
				String type = transAccountType(cellValue);
				if (!isEmpty(type)) {
					// 如帳號類型不為空值,表示使用者選擇新增帳號
					bo.setAddAccount(true);
					bo.getContents().setType(type);
					if ("006".equals(type)) {
						bo.getContents().setRole("kSch");
					} else if ("007".equals(type)) {
						bo.getContents().setRole("kSch");
					} else if ("009".equals(type)) {
						bo.getContents().setRole("kSHOP");
					} else if ("101".equals(type)) {
						bo.getContents().setRole("101");
					}
				} else {
					throw new Exception("帳號類型錯誤");
				}
			} else {
				// 如帳號類型為空值,表示使用者選擇不新增帳號
				bo.setAddAccount(false);
			}
		} else if (XlsTitleCode.SCHKITUSER_STATUS.equals(titleName)) {
			if (cellValue != null && cellValue.length() > 0) {
				int enable = transAccountStatus(cellValue);
				if (enable != -1) {
					bo.setEnable(enable);
				} else {
					throw new Exception("帳號狀態錯誤");
				}
			} else {
				bo.setEnable(1);
			}
		}
		return bo;
	}

	private Integer transAccountStatus(String status) {
		switch (status) {
		case "啟用":
			return 1;
		case "停用":
			return 0;
		default:
			return -1;
		}
	}

	private String transAccountType(String type) {
		switch (type) {
		case "自設廚房":
			return "006";
		case "受供餐學校":
			return "007";
		case "員生消費合作社":
			return "009";
		case "大專院校":
			return "101";
		default:
			return "";
		}
	}

	private boolean isEmpty(String str) {
		if (CateringServiceUtil.isEmpty(str))
			return true;
		else
			return false;
	}

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
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

	public Integer getCounty() {
		return county;
	}

	public void setCounty(Integer county) {
		this.county = county;
	}
}
