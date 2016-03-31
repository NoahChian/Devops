package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

//import org.apache.commons.lang.StringUtils;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.dao.AccountDao;
import org.iii.ideas.catering_service.dao.AreaDAO;
import org.iii.ideas.catering_service.dao.CodeDAO;
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
import org.iii.ideas.catering_service.util.LogUtil;

public class AddSchKitchenUser extends AbstractApiInterface<AddSchKitchenUserRequest, AddSchKitchenUserResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4038816461109320369L;

	@Override
	public void process() throws NamingException {

		Integer areaId = null;
		Integer countyId = null;
		String countyCode = null;
		String email = null;
		String schoolName = null;
//		String schoolId = null;
		String password = null;
		String username = null;
		String countyName = null;
		String schoolCode = null;
		Integer enable = null;
		Common com = new Common();

		// 檢核登入
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}

		// 檢核空值 2014/05/19 另外分開處理 加強錯誤訊息
		this.responseObj = validateColumn(requestObj, responseObj);
		if(this.responseObj.getResStatus() == 0){
			return;
		}
		
		
//		if (requestObj.getAreaId() == null || requestObj.getContents() == null || CateringServiceUtil.isEmpty(requestObj.getContents().getType()) || requestObj.getCountyId() == null
//				|| CateringServiceUtil.isEmpty(requestObj.getPassword()) || requestObj.getSchoolCode() == null || CateringServiceUtil.isEmpty(requestObj.getSchoolName()) || requestObj.getEnable() == null) {
//			this.responseObj.setResStatus(0);
//			this.responseObj.setMsg("資料傳遞有誤,請確認必填欄位");
//			return;
//		}
		

		areaId = requestObj.getAreaId();
		countyId = requestObj.getCountyId();
		email = requestObj.getContents().getEmail();
		schoolName = requestObj.getSchoolName();
//		schoolId = requestObj.getSchoolId();
		password = com.getEncoderByMd5(requestObj.getPassword());
		enable = requestObj.getEnable();
		schoolCode = requestObj.getSchoolCode();

		// 檢核是否為新增自己縣市的學校帳號
		if (this.getCounty() != countyId
				&& ! CateringServiceCode.USERTYPE_GOV_CENTRAL.equals(this.getUserType())) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("不可新增非管轄區域之學校帳號");
			return;
		}
		// #13522 學校編號要限制只能4碼或6碼的數字,管理者輸入什麼編號,資料庫就要存該編號不需要補0
		if (schoolCode.length() == 4 || schoolCode.length() == 6) {
		} else {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("學校編號需為4碼或6碼");
			return;
		}
		// 組schoolCode 如不滿六位 左補0
//		schoolCode = StringUtils.leftPad(schoolCode, 6, "0");

		// Start transaction
		Transaction tx = dbSession.beginTransaction();

		// 檢查學校是否存在
//		SchoolDAO schoolDao = new SchoolDAO(this.dbSession);
//		School school = schoolDao.querySchoolBySchoolCode(schoolCode);
//		if (schoolDao.querySchoolBySchoolCode(schoolCode) != null) {
//			this.responseObj.setResStatus(0);
//			this.responseObj.setMsg("學校已經存在");
//			return;
//		}
//		if(school == null){
//			school = schoolDao.querySchoolBySchoolName(schoolName);
//		}
				
		// 取縣市對應代碼
		CodeDAO codeDao = new CodeDAO(dbSession);
		countyCode = codeDao.getCodeByTypeAndName(String.valueOf(countyId), "county");
		countyName = codeDao.getCodeMsgByStatusCode(countyCode, "city");

		AreaDAO areaDao = new AreaDAO(dbSession);
		String areaName = areaDao.getAreaNameById(areaId);

		// 取得account(縣市代碼+學校代碼)

		username = countyCode.concat(schoolCode);
		
		Useraccount user = null;
		UseraccountDAO userDAO = new UseraccountDAO(dbSession);
		
		if(CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(this.requestObj.getContents().getType())){
			user = userDAO.queryUseraccountByUsername(username+"-SHOP");
		}else{
			user = userDAO.queryUseraccountByUsername(username);
		}
		if(!CateringServiceUtil.isNull(user)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("帳號已經存在");
			return;
		}
		
		//TODO 可能要加上判斷 006 007(2選1) 009可以被創造
//		if (school != null && !CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(this.requestObj.getContents().getType())) {
//			this.responseObj.setResStatus(0);
//			this.responseObj.setMsg("學校已經存在");
//			return;
//		}
		
		KitchenDAO kitDao = new KitchenDAO(dbSession);
		String kitchenName = schoolName;
		if(CateringServiceCode.USERTYPE_SCHOOL_SHOP.equals(requestObj.getContents().getType())){
			kitchenName = schoolName +"-員生消費合作社";
		}
		Kitchen kitchen = kitDao.queryKitchenByKitchenName(kitchenName);
		if(kitchen != null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("新增帳號失敗！該學校廚房帳號已存在，無法重覆開立。");
			return;
		}
		
		// 將資料塞入 BO 內準備新增
		SchKitchenUserBO scBo = new SchKitchenUserBO();
		SchKitchenUserContentBO scContentBo = new SchKitchenUserContentBO();
		scBo.setAreaId(areaId);
		scBo.setAreaName(areaName);
		scBo.setCountyId(countyId);
		scBo.setCountyName(countyName);
		scBo.setPassword(password);
		scBo.setSchoolCode(schoolCode);
		// scBo.setSchoolId(schoolId);
		scBo.setSchoolName(schoolName);
		scBo.setUsername(username);
		scBo.setEnable(enable);
		scContentBo.setEmail(email);
		scContentBo.setRole(requestObj.getContents().getRole());
		scContentBo.setType(requestObj.getContents().getType());
		scBo.setContents(scContentBo);
		scBo.setAddAccount(requestObj.getAddAccount());
		scBo.setCreateUser(this.getUsername()); //建立者

		// 開始存資料
		AccountDao accountDao = new AccountDao(dbSession);
		try {
			accountDao.updateSchoolKitchenAccount(scBo, "add");
			tx.commit();
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(ex.getMessage());
		}
		
		this.responseObj.setSchoolName(scBo.getSchoolName());
		this.responseObj.setUserName(scBo.getUsername());
	}
	
	private AddSchKitchenUserResponse  validateColumn(AddSchKitchenUserRequest req, AddSchKitchenUserResponse res){
		String errMsg = "";
		
		if (requestObj.getCountyId() == null) {
			errMsg = LogUtil.putErrorMsg(errMsg, "請選擇縣市別");
		}
		
		if (requestObj.getAreaId() == null ) {
			errMsg = LogUtil.putErrorMsg(errMsg, "請選擇市區別");
		}
		
		if (requestObj.getContents() == null) {
			errMsg = LogUtil.putErrorMsg(errMsg, "資料傳遞有誤,請確認必填欄位");
		}
		
		if(requestObj.getAddAccount()){
			if (requestObj.getContents() != null && CateringServiceUtil.isEmpty(requestObj.getContents().getType())) {
				errMsg = LogUtil.putErrorMsg(errMsg, "請選擇帳號類型");
			}
		}
		
		if (requestObj.getAddAccount() && CateringServiceUtil.isEmpty(requestObj.getPassword())) {
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入密碼");
		}
		
		if (CateringServiceUtil.isEmpty(requestObj.getSchoolCode())){
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入學校編號");
		}
		
		if (CateringServiceUtil.isEmpty(requestObj.getSchoolName())) {
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入學校名稱");
		}
		
		if (requestObj.getEnable() == null) {
			errMsg = LogUtil.putErrorMsg(errMsg, "請選擇帳號狀態");
		}
		
		if(!CateringServiceUtil.isEmpty(errMsg)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(errMsg);
		}else{
			this.responseObj.setResStatus(1);
		}
		
		return res;
	}


	

}
