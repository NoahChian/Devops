package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.dao.AccountDao;
import org.iii.ideas.catering_service.dao.AreaDAO;
import org.iii.ideas.catering_service.dao.CodeDAO;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.rest.bo.SchKitchenUserBO;
import org.iii.ideas.catering_service.rest.bo.SchKitchenUserContentBO;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.LogUtil;

public class UpdateSchKitchenUser extends AbstractApiInterface<UpdateSchKitchenUserRequest, UpdateSchKitchenUserResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -486243880820844589L;

	@Override
	public void process() throws NamingException {

		Integer areaId = null;
		Integer countyId = null;
		String countyCode = null;
		String email = null;
		String schoolName = null;
		Integer schoolId = null;
		String oldPassword = null;
		String newPassword = null;
		String username = null;
		String countyName = null;
		String schoolCode = null;
		Integer enable = 1;
		Common com = new Common();

		// 檢核登入
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}

		// 檢核空值 2014/05/19 另外分開處理 加強錯誤訊息
		this.responseObj = validateColumn(requestObj, responseObj);
		if (!CateringServiceUtil.isEmpty(this.responseObj.getMsg())) {
			return;
		}
		
//		if (requestObj.getSchoolId() == null || requestObj.getAreaId() == null || requestObj.getContents() == null || CateringServiceUtil.isEmpty(requestObj.getContents().getType()) || requestObj.getCountyId() == null
//				|| requestObj.getSchoolCode() == null || CateringServiceUtil.isEmpty(requestObj.getSchoolName())) {
//			this.responseObj.setResStatus(0);
//			this.responseObj.setMsg("資料傳遞有誤");
//			return;
//		}

		// 檢核換密碼是否有輸入新密碼
		if (!CateringServiceUtil.isEmpty(requestObj.getOld_password())) {
			if (CateringServiceUtil.isEmpty(requestObj.getNew_password())) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("新密碼不可為空");
				return;
			}
		}

		// 檢核換密碼是否有輸入舊密碼
		if (!CateringServiceUtil.isEmpty(requestObj.getNew_password())) {
			if (CateringServiceUtil.isEmpty(requestObj.getOld_password())) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("舊密碼不可為空");
				return;
			}
		}

		areaId = requestObj.getAreaId();
		countyId = requestObj.getCountyId();
		email = requestObj.getContents().getEmail();
		schoolName = requestObj.getSchoolName();
		schoolId = requestObj.getSchoolId();
		enable = requestObj.getEnable();
		schoolCode = requestObj.getSchoolCode();

		if (!CateringServiceUtil.isEmpty(requestObj.getNew_password()))
			newPassword = com.getEncoderByMd5(requestObj.getNew_password());

		if (!CateringServiceUtil.isEmpty(requestObj.getOld_password()))
			oldPassword = com.getEncoderByMd5(requestObj.getOld_password());

		// 檢核新舊密碼相同
		if (newPassword != null && oldPassword != null) {
			if (newPassword.equals(oldPassword)) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("新舊密碼相同");
				return;
			}
		}

		// 檢核是否為修改自己縣市的學校帳號
		if (this.getCounty() != countyId 
				&& ! CateringServiceCode.USERTYPE_GOV_CENTRAL.equals(this.getUserType())) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("不可修改非管轄區域之學校帳號");
			return;
		}

		Transaction tx = dbSession.beginTransaction();

		// 檢查學校是否存在
		SchoolDAO schoolDao = new SchoolDAO(this.dbSession);
		School school = schoolDao.querySchoolById(schoolId);
		if (school == null) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("學校不存在");
			return;
		}

		// 取縣市對應代碼
		CodeDAO codeDao = new CodeDAO(dbSession);
		countyCode = codeDao.getCodeByTypeAndName(String.valueOf(countyId), "county");
		countyName = codeDao.getCodeMsgByStatusCode(countyCode, "city");

		AreaDAO areaDao = new AreaDAO(dbSession);
		String areaName = areaDao.getAreaNameById(areaId);

		// 取得account(縣市代碼+學校代碼)
		// 組schoolCode 如不滿六位 左補0
		// schoolCode = String.format("%06d", schoolId);
		username = countyCode.concat(schoolCode);

		AccountDao accountDao = new AccountDao(dbSession);
		// 如果有修改密碼 先檢核舊密碼是否相同
		if (newPassword != null && oldPassword != null) {
			if (accountDao.queryUserByPassword(username, oldPassword) == null) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("舊密碼錯誤");
				return;
			}
		}

		// 將資料塞入 BO 內準備修改
		SchKitchenUserBO scBo = new SchKitchenUserBO();
		SchKitchenUserContentBO scContentBo = new SchKitchenUserContentBO();
		scBo.setAreaId(areaId);
		scBo.setAreaName(areaName);
		scBo.setCountyId(countyId);
		scBo.setCountyName(countyName);
		scBo.setSchoolCode(schoolCode);
		scBo.setSchoolId(schoolId);
		scBo.setSchoolName(schoolName);
		scBo.setUsername(username);
		scBo.setEnable(enable);
		scContentBo.setEmail(email);
		scContentBo.setRole(requestObj.getContents().getRole());
		scContentBo.setType(requestObj.getContents().getType());
		// 有修改密碼才把新密碼存入BO內
		if (newPassword != null)
			scBo.setPassword(newPassword);
		scBo.setContents(scContentBo);
		scBo.setAddAccount(requestObj.getAddAccount());

		// 開始修改資料
		try {
			accountDao.updateSchoolKitchenAccount(scBo, "update");
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(ex.getMessage());
		}

		tx.commit();
		this.responseObj.setSchoolName(scBo.getSchoolName());
		this.responseObj.setUserName(scBo.getUsername());
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

	private UpdateSchKitchenUserResponse validateColumn(UpdateSchKitchenUserRequest req, UpdateSchKitchenUserResponse res){
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
		
		if (requestObj.getContents() != null && CateringServiceUtil.isEmpty(requestObj.getContents().getType())) {
			errMsg = LogUtil.putErrorMsg(errMsg, "請選擇帳號類型");
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
