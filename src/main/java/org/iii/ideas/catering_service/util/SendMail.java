package org.iii.ideas.catering_service.util;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendMail {

	private MimeMessage  mailMessage=null;
	private Session mailSession;
	private Properties props;
	
	public static final String  MAIL_TYPE_TEXT="text";
	public static final String  MAIL_TYPE_HTML="html";
	public static final String  MAIL_PROTOCOL_SMTP="smtp";
	public static final String  MAIL_PROTOCOL_SMTPSSL="smtpssl";
	

	
	public void setSendServer(String smtpServer,String smtpPort,String username,String password,String protocol) throws Exception{
		this.props=null;
		this.mailMessage=null;
		this.mailSession=null;
		
		switch (protocol){
			case MAIL_PROTOCOL_SMTPSSL:
				this.setSMTPSSL( smtpServer, smtpPort, username, password);
				break;
			case MAIL_PROTOCOL_SMTP:
				this.setSMTP( smtpServer, smtpPort, username, password);
				break;
			default:
				this.setSMTP( smtpServer, smtpPort, username, password);
		}
	}
	

	private void setSMTPSSL(String smtpServer,String smtpPort,String username,String password) throws Exception{
			
		  final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		  Properties mailProp= new Properties();
		 
		  mailProp.put("mail.smtps.host", smtpServer);
		  mailProp.put("mail.smtps.socketFactory.class", SSL_FACTORY);
		  mailProp.put("mail.smtps.socketFactory.fallback", "true");
		  mailProp.put("mail.smtps.port", smtpPort);
		  mailProp.put("mail.smtps.socketFactory.port", smtpPort);		
		  mailProp.put("mail.transport.protocol", "smtps");
		  mailProp.put("mail.smtps.auth", "true");
		  mailProp.put("mail.smtps.ssl.enable", "true");  
		  mailProp.put("mail.debug", "true");
		  mailProp.put("mail.smtps.ssl.checkserveridentity", "false");
		  mailProp.put("mail.smtps.ssl.trust", "*");
		  this.props=mailProp;

		this.setSession(username, password);
		this.setMessageObj();
	}
	
	private void setSMTP(String smtpServer,String smtpPort,String username,String password) 
			throws Exception{
		Properties mailProp= new Properties();
		mailProp.put("mail.smtp.host", smtpServer);
		mailProp.put("mail.smtp.port", smtpPort);
		mailProp.put("mail.smtp.starttls.enable", "true");
		mailProp.put("mail.transport.protocol", "smtp");
		mailProp.put("mail.smtp.auth", "true");
		mailProp.put("mail.debug", "true");
		this.props=mailProp;
		this.setSession(username, password);
		this.setMessageObj();
	}
	
	private void setSession(String username,String password){
		  final String pUser = username;
		  final String pPwd = password;
		  
		  this.mailSession = Session.getDefaultInstance(this.props, new Authenticator(){
			  @Override
		      protected PasswordAuthentication getPasswordAuthentication() {
		          return new PasswordAuthentication(pUser, pPwd);
		      }});
	}
	private void setMessageObj() throws Exception{
		if (this.mailMessage!=null){
			throw new Exception("mail message object has been set");
		}
		
		this.mailMessage=new MimeMessage(this.getMailSession());
	}
	
	public void setFrom(String senderMail) throws Exception{
		this.setFrom(senderMail,senderMail);
		
	}
	public void setFrom(String senderMail,String senderName) throws Exception{
		if (!senderMail.contains("@")){
			throw new Exception("not valid sender email address");
		}
		this.getMailMessage().setFrom(new InternetAddress(senderMail,senderName));	
	}
	
	private MimeMessage getMailMessage() throws Exception{
		if (this.mailMessage==null){
			throw new Exception();
			//this.mailMessage=new MimeMessage(this.getMailSession());
		}
		
		return this.mailMessage;
	}
	
	
	private Session getMailSession() throws Exception{
		if (this.mailSession==null){
			throw new Exception("mail session is null");
		}
		
		return this.mailSession;
	}
	
	private void addMailRecipient(String recipientMail,String recipientName,RecipientType receiverType) throws Exception{
		this.mailMessage.addRecipient(receiverType, new InternetAddress(recipientMail,recipientName));
	}
	
	public void setTo(String recipientMail,String recipientName)throws Exception{
		if (!recipientMail.contains("@")){
			throw new Exception("not valid recipientMail address");
		}
		//this.mailMessage.setRecipient(RecipientType.TO, new InternetAddress(recipientMail,recipientName));
		//this.mailMessage.addRecipient(RecipientType.TO, new InternetAddress(recipientMail,recipientName));
		addMailRecipient(recipientMail, recipientName,RecipientType.TO);
	}

	public void setTo(String recipientMail) throws Exception{
		this.setTo(recipientMail,recipientMail);
		
	}
	
	public void setTo(List<String> recipientMailList) throws Exception{
		Iterator<String> irMail=recipientMailList.iterator();
		while(irMail.hasNext()){
			String mail=irMail.next();
			if (!mail.contains("@")){
				continue;
			}
			this.setTo(mail);
		}
	}
	
	public void setCC(String recipientMail,String recipientName) throws Exception{
		if (!recipientMail.contains("@")){
			throw new Exception("not valid recipientMail address");
		}
		addMailRecipient(recipientMail, recipientName,RecipientType.CC);
	}
	
	public void setCC(String recipientMail) throws Exception{
		this.setCC(recipientMail, recipientMail);
	}
	
	
	public void setCC(List<String> recipientMailList) throws Exception{
		Iterator<String> irMail=recipientMailList.iterator();
		while(irMail.hasNext()){
			String mail=irMail.next();
			if (!mail.contains("@")){
				continue;
			}
			this.setCC(mail);
		}
	}
	
	public void setBCC(String recipientMail,String recipientName) throws Exception{
		if (!recipientMail.contains("@")){
			throw new Exception("not valid recipientMail address");
		}
		addMailRecipient(recipientMail, recipientName,RecipientType.BCC);
	}
	
	public void setBCC(String recipientMail) throws Exception{
		this.setBCC(recipientMail, recipientMail);
	}
	
	
	public void setBCC(List<String> recipientMailList) throws Exception{
		Iterator<String> irMail=recipientMailList.iterator();
		while(irMail.hasNext()){
			String mail=irMail.next();
			if (!mail.contains("@")){
				continue;
			}
			this.setBCC(mail);
		}
	}
	
	
	
	
	public void setSubject(String subject) throws Exception{
		if (subject.length()>1000){
			subject=subject.substring(1000);
		}
		this.getMailMessage().setSubject(subject,"utf-8");	
	}
	
	public void setContent(String content,String contentType) throws Exception{
		
		switch (contentType.toLowerCase()){
			case MAIL_TYPE_TEXT:
				System.out.print("content type : text");
				getMailMessage().setContent(content,"text/plain; charset=\"utf-8\"");
				//this.mailMessage.setText(content);
				break;
			case MAIL_TYPE_HTML:
				System.out.print("content type : html");
				setHtmlBody(content);
				break;
			default:
				System.out.print("content type : default");
				getMailMessage().setText(content);
		}
	}
	//public void setAttachment(){}

	public void setHtmlBody(String content) throws Exception{
		//System.out.println("****************");
		mailMessage.setContent(content,"text/html; charset=UTF-8");
		//System.out.println("&&&&"+mailMessage.getContent()+"&&&");
	}
	

	public void send()throws Exception{
		if (mailMessage==null){
			throw new Exception();
		}
		Transport transport =this.getMailSession().getTransport();
		mailMessage.setSentDate(new Date());
		if (transport==null){
			throw new Exception();
		}
		
		try{			
			transport.connect();
			//System.out.println("start send ...."+this.getMailMessage().getRecipients(RecipientType.TO));
			transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
			//System.out.println("send success! ");
			transport.close();
		}catch (AddressException  ex){
			
			ex.printStackTrace();
		}catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
		
	};

	
}
