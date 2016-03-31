package org.iii.ideas.catering_service.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.util.SendMail;
import org.iii.ideas.catering_service.util.SystemConfiguration;

/**
 * 
 * @author 2014/06/04 Raymond
 *
 */
public class MailService extends BaseService{
	protected Logger log = Logger.getLogger(this.getClass());
	
	private String mailUser;
	private String mailPassword;
	
	
	public MailService(){
		try {
			getMailUserInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	
	public MailService(Session dbSession){
		setDbSession(dbSession);
		try {
			getMailUserInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	
	public void getMailUserInfo() throws Exception{
		Properties prop = new Properties();
		String propFileName = "mail.properties";
		 
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        prop.load(inputStream);
        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
        
        this.mailUser = prop.getProperty("user");
        this.mailPassword = prop.getProperty("password");
	}
	
	
	/**
	 * 發送忘記密碼Mail
	 * @param username
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public void sendForgotPasswdMail(String username,String email,String timeStamp) throws Exception{
		// URL規則: u=username,e=email MD5 加密,t=系統發送時間timestamp
		Common com = new Common();
		String encodeUserName = URLEncoder.encode(username, "UTF-8");
		String encodeEmail = URLEncoder.encode(com.getEncoderByMd5(email), "UTF-8");
		String systemUrl = SystemConfiguration.getConfig("catering_url");
		String url = systemUrl + "/web/resetpwd/?u="+encodeUserName+"&e="+encodeEmail+"&t="+timeStamp ;
		String mailBody = "您好:<br><br>"
				+ "您可至下列連結網址重新設定您的密碼,"
				+ "提醒您!此連結網址會於重新設定完畢後或十分鐘後失效.<br><br>"
				+ "<a href=\""+ url +"\"><u>重設密碼連結請點我!</u></a><br><br>"
				+ "=== 此信件為系統發送信件,請勿回覆此信件 ! ===";
		
        
		SendMail sm = new SendMail();
		sm.setSendServer("mail.iii.org.tw", "465" , this.mailUser, this.mailPassword, "smtpssl");
		sm.setTo(email);
		sm.setFrom("catering@iii.org.tw");
		sm.setSubject("食材登錄平台使用者忘記密碼");
		sm.setContent(mailBody,"html");
		sm.send();
		
//		SendEmail sendMail = new SendEmail();
//		
//		
//		
//		return sendMail.sendEmail(email, "團膳系統使用者忘記密碼", mailBody);
	}
	

}
