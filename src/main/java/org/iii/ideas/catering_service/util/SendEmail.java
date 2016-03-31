package org.iii.ideas.catering_service.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.iii.ideas.catering_service.rest.WebRestApiImpl;

public class SendEmail {
	
	private static final Logger logger = Logger.getLogger(WebRestApiImpl.class);
	
	private static String protocol = "pop3";
	private static String host = "mail.iii.org.tw";
	private static String user = "catering";
	private static String password = "Wj06g04!@#";
	
//	private static String host = "mail2.ideas.iii.org.tw";
//	private static String user = "shoppingwall";
//	private static String password = "sgwall123!";
	
	public boolean sendEmail(String emailAddress, String subject, String content) {
		try{
		//寄送電子郵件確認信
			Properties props = new Properties();
	        props.setProperty("mail.transport.protocol", protocol);
	        props.setProperty("mail.host", host);
	        props.setProperty("mail.user", user);
	        props.setProperty("mail.password", password);
	
	        Session mailSession = Session.getDefaultInstance(props, null);
	        Transport transport = mailSession.getTransport();
	
	        // 產生整封 email 的主體 message
	        MimeMessage message = new MimeMessage(mailSession);
	
	        // 設定主旨
	        message.setSubject(subject,"utf-8");
	
	        // 文字部份，注意 img src 部份要用 cid:接下面附檔的header
	        MimeBodyPart textPart = new MimeBodyPart();
	        StringBuffer html = new StringBuffer();
	        html.append(content);
	        textPart.setContent(html.toString(), "text/html; charset=utf-8");
	
	        Multipart email = new MimeMultipart();
	        email.addBodyPart(textPart);
	
	        message.setContent(email);
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
	        transport.connect();
	        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
	        transport.close();
		} catch (Exception e) {
			MDC.put("type","寄送電子郵件");
			MDC.put("msg", "使用者("+ emailAddress +")寄送(" + subject + ")內容(" + content + ")失敗");
			logger.info("");
			
			return false;
		}
		return true;
	}
}
