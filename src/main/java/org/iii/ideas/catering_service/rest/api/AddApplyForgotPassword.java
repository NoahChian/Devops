package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.code.LogStatusCode;
import org.iii.ideas.catering_service.service.MailService;
import org.iii.ideas.catering_service.service.UserAccountService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.LogUtil;

public class AddApplyForgotPassword extends AbstractApiInterface<AddApplyForgotPasswordRequest, AddApplyForgotPasswordResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5059183051577874434L;

	@Override
	public void process() throws NamingException, ParseException {
		String username = this.requestObj.getUsername();
		String email = this.requestObj.getEmail();
		String timeStamp = String.valueOf(System.currentTimeMillis());
		String remoteIp = getRemoteIp();

		// 基本欄位檢核
		if (CateringServiceUtil.isEmpty(username)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("帳戶名稱為必填欄位!");
			return;
		}
		if (CateringServiceUtil.isEmpty(email)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("帳戶E-Mail為必填欄位!");
			return;
		}

		try {
			UserAccountService uaService = new UserAccountService(dbSession);
			if (!uaService.validatApplyRecord(username, 10)) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("您在10秒內重複申請,請勿重複操作");
				return;
			}

			if (!uaService.validatUserMail(username, email)) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("帳號或E-Mail錯誤,請確認帳號或是E-Mail是否正確(E-Mail為申請時所填寫之E-Mail),如有疑問請聯絡系統服務廠商!");
				return;
			}

			MailService mailService = new MailService();

			boolean bool = true;
			try {
				mailService.sendForgotPasswdMail(username, email, timeStamp);
			} catch (Exception e) {
				e.printStackTrace();
				this.responseObj.setMsg("發送Mail失敗,請聯絡系統服務廠商!");
				this.responseObj.setResStatus(0);
				bool = false;
			}

			if (bool) {
				this.responseObj.setMsg("");
				this.responseObj.setResStatus(1);
				LogUtil.writeResetPsswordLog(username, email, timeStamp, remoteIp, LogStatusCode.RESET_PASSWORD_SEND_MAIL_SUCCESS, "傳送申請MAIL成功");
			} else {
				this.responseObj.setMsg("申請失敗,請聯絡系統服務廠商!");
				this.responseObj.setResStatus(0);
				LogUtil.writeResetPsswordLog(username, email, timeStamp, remoteIp, LogStatusCode.RESET_PASSWORD_SEND_MAIL_FAIL, "傳送申請MAIL錯誤");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			this.responseObj.setMsg("系統發生錯誤");
			this.responseObj.setResStatus(0);
		}
	}

}
