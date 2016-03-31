package org.iii.ideas.catering_service.service;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.iii.ideas.catering_service.code.LogStatusCode;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.dao.ResetPwdLogDAO;
import org.iii.ideas.catering_service.dao.Resetpwdlog;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.UseraccountDAO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;



/**
 * 
 * @author 20140604 Raymond
 *
 */
public class UserAccountService extends BaseService {
	protected Logger log = Logger.getLogger(this.getClass());

	public UserAccountService() {
	};

	public UserAccountService(Session dbSession) {
		setDbSession(dbSession);
	};

	// 驗證user的mail是否與預設的相同
	public boolean validatUserMail(String username, String email) {
		UseraccountDAO uaDao = new UseraccountDAO(dbSession);
		Useraccount ua = uaDao.queryUseraccountByUsernameAndEmail(username, email);
		if (ua == null)
			return false;
		else
			return true;
	}

	// 驗證user的mail(MD5)是否與預設的相同
	public boolean validatUserMD5Mail(String username, String md5Mmail) {
		UseraccountDAO uaDao = new UseraccountDAO(dbSession);
		Useraccount ua = uaDao.queryUseraccountByUsername(username);
		if (ua == null)
			return false;
		else{
			Common com = new Common();
			String userMd5Mail = com.getEncoderByMd5(ua.getEmail());
			if(userMd5Mail.endsWith(md5Mmail))
				return true;
			else
				return false;
		}
	}
	
	//驗證傳入的timestamp + 10分鐘  是否有小於系統時間
	public boolean validatResetPwdLimitTime(Long timestamp){
		Timestamp ts = new Timestamp(timestamp);
		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);
		cal.add(Calendar.MINUTE, 10);
		
		Calendar sysCal = Calendar.getInstance();
		
		if(cal.before(sysCal))
			return false;
		else
			return true;
	}
	
	//更新 user account
	public Useraccount updateUserPassword(String username,String password) throws Exception{
		UseraccountDAO uaDao = new UseraccountDAO(dbSession);
		Common com = new Common();
		String md5Pwd = com.getEncoderByMd5(password);
		Useraccount ua = uaDao.queryUseraccountByUsername(username);
		if(ua!=null){
			uaDao.updateUserPassword(ua, md5Pwd);
			return ua;
			
		}else{
			return null;
		}
	}
	
	//驗證是否為相同TS申請
	public String validatApplyProcess(String username,String ts){
		ResetPwdLogDAO rplDao = new ResetPwdLogDAO(dbSession);
		Resetpwdlog log = rplDao.queryResetPwdLogLastRecord(username);
		if(log==null)
			return "您未有忘記密碼的申請紀錄,如有疑問,請洽系統服務廠商!";
		
		if(!log.getTs().equals(ts))
			return "此連結已經失效,請至信箱中檢查是否有新的系統信件,如有疑問,請洽系統服務廠商!";
		
		if(log.getStatus().equals("2"))
			return "密碼已完成變更,請回首頁使用新的密碼登入!";
		
		return "";
	}
	
	//驗證此筆交易是否已經完成
	public boolean validatProcessDone(String username,String ts){
		ResetPwdLogDAO rplDao = new ResetPwdLogDAO(dbSession);
		Resetpwdlog log = rplDao.queryResetPwdLogRecordByTsAndStatus(username, ts, LogStatusCode.RESET_PASSWORD_RESET_SUCCESS);
		
		if(log!=null)
			return false;
		else
			return true;
	}
	
	//驗證URL資訊
	public String validatResetPwdUrlInfo(String username,String md5Mail,Long ts){
		
		String errMsg = "";
		errMsg = validatApplyProcess(username,String.valueOf(ts));
		if(!CateringServiceUtil.isEmpty(errMsg))
			return errMsg;
		
		if(!validatProcessDone(username,String.valueOf(ts)))
			return "密碼已完成變更,請回首頁使用新的密碼登入!";
		
		if(!validatResetPwdLimitTime(ts))
			return "已超過操作時間,請再操作一次忘記密碼請流程!";
		
		if(!validatUserMD5Mail(username,md5Mail))
			return "傳遞資訊錯誤,請洽系統服務廠商!";
		
		return "";
	}
	
	//驗證XX秒內有無申請紀錄
	public boolean validatApplyRecord(String username,Integer second){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -second);
		ResetPwdLogDAO rplDao = new ResetPwdLogDAO(dbSession);
		Resetpwdlog log = rplDao.queryAfterLogRecord(username,calendar.getTime());
		if(log!=null)
			return false;
		else
			return true;
	}

}
