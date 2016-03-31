package org.iii.ideas.catering_service.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.dao.SystemConfigDAO;

public class ApplicationConfig {
	private static SystemConfigDAO sysConfig;
	private static String SYSCONFIG_MENU_UPLOAD_LIMIT = "menuUploadLimit";
	private static String SYSCONFIG_REPORT_UPLOAD_LIMIT = "reportUploadLimit";
	private static String SYSCONFIG_FCLOUD_QUERY_SOURCEID_URL = "fcloudQuerySourceidUrl";
	private static String SYSCONFIG_FCLOUD_PRODUCT_LINK_URL = "fcloudProductLinkUrl";
	private static SessionFactory sessionFactory;
	/*
	public static void setSessionFactory(SessionFactory sessionFactory) {
		ApplicationConfig.sessionFactory = sessionFactory;
		sysConfig.setSessionFactory(sessionFactory);
	}*/
	/*
	public ApplicationConfig() {
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		if (sessionFactory!=null){
			sysConfig.setSessionFactory(sessionFactory);
		}
	}
	*/
	
	public ApplicationConfig(Session session) {
		sysConfig=new SystemConfigDAO();
		sysConfig.setSession(session);
	}
	public static String getUploadMenuLimitTime(String countyId) {
		return sysConfig.getValue(SYSCONFIG_MENU_UPLOAD_LIMIT, countyId);
	}

	public static String getUploadReportLimitTime(String countyId) {
		return sysConfig.getValue(SYSCONFIG_REPORT_UPLOAD_LIMIT, countyId);
	}

	public  static Boolean setUploadMenuLimitTime(String limitTime, String countyId) {
		return sysConfig.setValue(SYSCONFIG_MENU_UPLOAD_LIMIT, countyId, limitTime, sysConfig.CONFIG_ENABLE);
	}

	public static Boolean setUploadReportLimitTime(String limitTime, String countyId) {
		return sysConfig.setValue(SYSCONFIG_REPORT_UPLOAD_LIMIT, countyId, limitTime, sysConfig.CONFIG_ENABLE);
	}

	public static Boolean disableUploadReportLimitTime(String countyId) {
		return false;
	}

	public static String getFcloudQuerySourceidUrl() {
		return sysConfig.getValue(SYSCONFIG_FCLOUD_QUERY_SOURCEID_URL, "fcloudUrl");
	}

	public String getFcloudProductLinkUrl() {
		return sysConfig.getValue(SYSCONFIG_FCLOUD_PRODUCT_LINK_URL, "fcloudUrl");
	}

}
