package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.code.ActiveTypeCode;
import org.iii.ideas.catering_service.code.LogStatusCode;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.service.UserAccountService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.LogUtil;

public class UpdateResetUserPassword extends AbstractApiInterface<UpdateResetUserPasswordRequest, UpdateResetUserPasswordResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5059183051577874434L;

	@Override
	public void process() throws NamingException, ParseException {
		String username = this.requestObj.getUsername();
		String email = this.requestObj.getEmail();
		String ts = this.requestObj.getTs();
		String actType = this.requestObj.getActType();
		String password = this.requestObj.getPassword();
		String valPassword = this.requestObj.getValPassword();
		String remoteIp = getRemoteIp();
		
		Long timestamp;

		// 基本欄位空值檢核
		if (CateringServiceUtil.isEmpty(username)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("資料傳遞錯誤(ERR001),請聯絡系統服務廠商!");
			return;
		}
		
		if (CateringServiceUtil.isEmpty(email)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("資料傳遞錯誤(ERR002),請聯絡系統服務廠商!");
			return;
		}
		
		if (CateringServiceUtil.isEmpty(ts)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("資料傳遞錯誤(ERR003),請聯絡系統服務廠商!");
			return;
		}
		
		if (CateringServiceUtil.isEmpty(actType)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("資料傳遞錯誤(ERR004),請聯絡系統服務廠商!");
			return;
		}
		

		try {
			timestamp = Long.valueOf(ts);
			if (timestamp == null) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("資料傳遞錯誤(ERR005),請聯絡系統服務廠商!");
				return;
			}

			UserAccountService uaService = new UserAccountService(dbSession);

			//驗證URL資訊 及 LOG 資訊
			String errMsg = "";
			errMsg = uaService.validatResetPwdUrlInfo(username, email, timestamp);
			if(!CateringServiceUtil.isEmpty(errMsg)){
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg(errMsg);
				LogUtil.writeResetPsswordLog(username, email, ts, remoteIp, LogStatusCode.RESET_PASSWORD_URL_ERR, errMsg);
				return;
			}
			
			//如果actType為validat 則直接回傳成功或失敗
			switch (actType){
			case ActiveTypeCode.VALIDAT:
				this.responseObj.setResStatus(1);
				this.responseObj.setMsg("");
				return;
			case ActiveTypeCode.UPDATE:
				//檢核空值
				if(CateringServiceUtil.isEmpty(password) || CateringServiceUtil.isEmpty(valPassword)){
					this.responseObj.setResStatus(0);
					this.responseObj.setMsg("密碼及驗證密碼不可為空!");
					return;
				}
				//驗證密碼是否相同
				if(!password.equals(valPassword)){
					this.responseObj.setResStatus(0);
					this.responseObj.setMsg("密碼與驗證密碼不相同!");
					return;
				}
				//開始更新
				Transaction tx = dbSession.beginTransaction();
				try{
					Useraccount ua = uaService.updateUserPassword(username, password);
					if (ua == null) {
						tx.rollback();
						this.responseObj.setResStatus(0);
						this.responseObj.setMsg("更新密碼失敗,請聯絡系統服務廠商!");
						return;
					} else {
						tx.commit();
						LogUtil.writeResetPsswordLog(username, ua.getEmail(), ts, remoteIp, LogStatusCode.RESET_PASSWORD_RESET_SUCCESS, "更新密碼成功");
						this.responseObj.setResStatus(1);
						this.responseObj.setMsg("更新密碼成功,請回首頁登入!");
						return;
					}
				} catch (Exception ex) {
					tx.rollback();
					ex.printStackTrace();
					this.responseObj.setMsg("系統發生錯誤");
					this.responseObj.setResStatus(0);
				}
			}
				
		} catch (Exception ex) {
			ex.printStackTrace();
			this.responseObj.setMsg("系統發生錯誤");
			this.responseObj.setResStatus(0);
		}
	}

}
