package org.iii.ideas.catering_service.util;

import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.ServiceLog;
import org.iii.ideas.catering_service.dao.WsLog;
import org.iii.ideas.catering_service.dao.Resetpwdlog;

public class LogUtil {

	// Sessionfactory from HibernateUtil.java
	private static SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public static void writeErrorLog(WsLog log) {
		Session session = sessionFactory.getCurrentSession();
		session.save(log);
	}

	public static void writeFileUploadLog(String status,String userId,String errorMsg,String fileName,String funcName){
		WsLog log = new WsLog();
		log.setId(0);
		log.setMessageId("U0001");
		log.setAction(funcName + "_upload");
		log.setCompanyId(userId);
		log.setDescription(errorMsg);
		log.setSendTime(CateringServiceUtil.getCurrentTimestamp());
		log.setUpdateTime(CateringServiceUtil.getCurrentTimestamp());
		//log.setStatusCode(status);
		fileName=fileName.replace("/", "|");
		fileName=fileName.replace(".", "-");
		log.setStatusCode(fileName);  //改存檔名 20140509 KC
		saveLog(log);
	
	}
	
	public static void writeServiceLog( String qAccount,String  qUsername,
			String qType,String serviceAccount,String qContent){
		ServiceLog log=new ServiceLog();
		log.setDate(CateringServiceUtil.getCurrentTimestamp());
		log.setqAccount(qAccount);
		log.setqContent(qContent);
		log.setqType(qType);
		log.setqUsername(qUsername);
		log.setServiceAccount(serviceAccount);
		saveLog(log);
	}

	public static void saveLog(Object log) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(log);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
	}
	
	/**
	 * 串接error message
	 * 
	 * @param errMsg
	 * @param errString
	 * @return
	 */
	public static String putErrorMsg(String errMsg, String errString) {
		if (CateringServiceUtil.isEmpty(errMsg)) {
			errMsg = errString;
		} else {
			errMsg += "," + errString;
		}
		return errMsg;
	}
	
	/**
	 * 紀錄忘記密碼的LOG
	 * @param ResetPwdLogBO
	 */
	public static void writeResetPsswordLog(String username,String email,String ts,String sourceIp,String status,String description){
		Resetpwdlog log = new Resetpwdlog();
		log.setUsername(username);
		log.setEmail(email);
		log.setTs(ts);
		log.setSourceIp(sourceIp);
		log.setStatus(status);
		log.setDescription(description);
		log.setCreateTime(Calendar.getInstance().getTime());
		log.setUpdateTime(Calendar.getInstance().getTime());
		saveLog(log);
		
	}

}
