package org.iii.ideas.catering_service.util;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.Code;


public class AuthenUtil 
{
	/*
	public final static Integer COUNTY_ID = 0;
	public final static Integer KITCHEN_ID = 0;
	public final static String COUNTY_NAME = "";
	public final static String USER_NAME = "";
	public final static String NAME  = "";
	*/
	//Sessionfactory from HibernateUtil.java
	private static  SessionFactory sessionFactory = null;
	private static Session dbSession=null;
	
	//private static ServiceRegistry serviceRegistry = null;
	//private static Configuration configuration = null;

	// 拿掉 buildSessionFactory 統一用HibernateUtil的  20140506 KC
	/*
	public static SessionFactory buildSessionFactory() {
	
		
		if (configuration == null) {
			configuration = new Configuration();
			configuration.configure();
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
		}
		if (sessionFactory == null || sessionFactory.isClosed()) {
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		}
		return sessionFactory;
		
	}
*/
	public static void setSessionFatory(SessionFactory factory){
		if (sessionFactory==null ){
			sessionFactory=factory;
		}
	}
	
	public static  SessionFactory getSessionFactory(){
		if (sessionFactory==null){
			sessionFactory=HibernateUtil.buildSessionFactory();
		}
		return sessionFactory;
	
	}
	public static Session getSession(){
	/*	if(dbSession==null){
			dbSession=getSessionFactory().openSession();
		}
		return dbSession;*/
		return getSessionFactory().openSession();
	}
	public static void setSession(Session session){
		dbSession=session;
	}
	
	
	public static Integer getCountryNumByCountyType(String ctype){
		/* 將縣市編號轉變為縣市代碼
		 * 輸入為字串，輸出為數字
		 * 
		 */
		//SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
		//Session dbsession = sessionfactory.openSession();
		Session dbsession=getSession();//getSessionFactory().openSession();
		Integer county = 0;
		
		Criteria countyCode= dbsession.createCriteria(Code.class);
		countyCode.add(Restrictions.eq("code", ctype)); //帶入編碼
		countyCode.add(Restrictions.eq("type", "county"));//帶入代碼表型態
		List<Code> queryObject = countyCode.list();
		if (queryObject.size() == 0) {
			dbsession.close();
			return county;
		}
		Iterator<Code> iterator = queryObject.iterator();
		Code codes = null;
		while (iterator.hasNext()) {
			codes = iterator.next();
		}
		county =  Integer.valueOf(codes.getName()); 
		/*switch (ctype){
		case "A": county  = 17;break; //臺北市
		case "C": county  = 18;break; //基隆市
		case "F": county  = 19;break; //新北市
		case "Z": county  = 20;break; //連江縣
		case "G": county  = 21;break; //宜蘭縣
		case "B": county  = 27;break; //臺中市 沒有台中縣
		case "O": county  = 23;break; //新竹市
		case "J": county  = 24;break; //新竹縣
		case "H": county  = 25;break; //桃園縣
		case "K": county  = 26;break; //苗栗縣
		case "L": county  = 27;break; //臺中市
		case "N": county  = 28;break; //彰化縣
		case "M": county  = 29;break; //南投縣
		case "I": county  = 30;break; //嘉義市
		case "Q": county  = 31;break; //嘉義縣
		case "P": county  = 32;break; //雲林縣
		case "R": county  = 33;break; //臺南市
		case "E": county  = 34;break; //高雄市
		case "D": county  = 33;break; //臺南市	沒有台南縣	
		case "X": county  = 36;break; //澎湖縣
		case "W": county  = 37;break; //金門縣
		case "T": county  = 38;break; //屏東縣
		case "V": county  = 39;break; //台東縣
		case "U": county  = 40;break; //花蓮縣
		case "5": county  = 555;break; //業者
		//CateringServiceCode.USERTYPE_COLLEGE_SCHOOL.contain(uType): county  = 777;break
		//CateringServiceCode.AUTHEN_SCHOOL #11168 (由於學校不一定是6開頭,所以獨立出來寫)
		case "6": county  = 666;break; //學校
		}*/

		dbsession.close();
		return county;
	}
	public static String getCountyTypeByCountryNum(Integer cnum){
		/* 將縣市代碼轉變為縣市編號
		 * 輸入為字串，輸出為數字
		 * 
		 */
		//SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
		//Session dbsession = sessionfactory.openSession();
		Session dbsession=getSession();//getSessionFactory().openSession();
		String county = "0";
		Criteria countyCode= dbsession.createCriteria(Code.class);
		countyCode.add(Restrictions.eq("name", cnum.toString())); //帶入編碼
		//countyCode.add(Restrictions.eq("name", String.valueOf(cnum))); //帶入編碼
		countyCode.add(Restrictions.eq("type", "county"));//帶入代碼表型態
		List<Code> queryObject = countyCode.list();
		if (queryObject.size() == 0) {
			dbsession.close();
			return county;
		}
		Iterator<Code> iterator = queryObject.iterator();
		Code codes = null;
		while (iterator.hasNext()) {
			codes = iterator.next();
		}
		county =  codes.getCode(); 
		
		dbsession.close();
		return county;
	}

	public static Integer getCountyNumByUsername(String uname) {
	/* 以帳號取得縣市編號
	 * 用帳號取得使用者型態(uType)
	 * 長度為3，可為業者:回傳0；廚房:回傳帳號第1碼轉換
	 * 長度為6，可為主管機關:回傳uType第3碼轉換
	 */
		Integer countyNum = 0;
		if("".equals(uname)||uname == null){
			return countyNum;
		}
		//SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
		//Session dbsession = sessionfactory.openSession();
		Session dbsession=getSession();//getSessionFactory().openSession();
		Criteria criteria = dbsession.createCriteria(Useraccount.class);
		criteria.add(Restrictions.eq("username", uname));
		
		List<Useraccount> queryObject = criteria.list();
		if (queryObject.size() == 0) {
			dbsession.close();
			return countyNum;
		}
		Iterator<Useraccount> iterator = queryObject.iterator();
		Useraccount useraccount = null;
		while (iterator.hasNext()) {
			useraccount = iterator.next();
		}
		String uType =  useraccount.getUsertype(); 
		if(uType.length()==3){
			if(CateringServiceCode.USERTYPE_KITCHEN.equals(uType)){
			}
			if(CateringServiceCode.USERTYPE_SCHOOL.equals(uType) || CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(uType)){
				countyNum =getCountryNumByCountyType(useraccount.getUsername().substring(0,1));
			} 
			if(CateringServiceCode.USERTYPE_COLLEGE_SCHOOL.contains(uType)){
				countyNum = Integer.valueOf(CateringServiceCode.AUTHEN_SCHOOL);
			}
		}
		if(uType.length()==6){ //地方政府 6碼取第三碼
			countyNum =getCountryNumByCountyType(useraccount.getUsertype().substring(2,3));
		}
		if(uType.length()==2){ //系統管理員/中央機關
			if("11".equals(uType)){
				countyNum = 9999; //先用hardcode 之後應該要從DB撈出rolerule CateringServiceCode.AUTHEN_SUPER_COUNTY
			}
		}
		dbsession.close();
		return countyNum;
	}

	public static String getCountyTypeByUsername(String uname) {
		/* 以帳號取得縣市代碼
		 * 用帳號取得使用者型態(uType)
		 * 長度為3，可為業者:回傳0；廚房:回傳帳號第1碼
		 * 長度為6，可為主管機關:回傳uType第3碼
		 */

		
			//SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
			//Session dbsession = sessionfactory.openSession();
		Session dbsession=getSession();//getSessionFactory().openSession();
			Criteria criteria = dbsession.createCriteria(Useraccount.class);
			criteria.add(Restrictions.eq("username", uname));
			String countyType = "0";

			if("".equals(uname)||uname == null){
				return countyType;
			}
			List<Useraccount> queryObject = criteria.list();
			if (queryObject.size() == 0) {
				dbsession.close();
				return countyType;
			}
			Iterator<Useraccount> iterator = queryObject.iterator();
			Useraccount useraccount = null;
			while (iterator.hasNext()) {
				useraccount = iterator.next();
			}
			String uType =  useraccount.getUsertype(); 
			if(uType.length()==3){
				if("005".equals(uType)){
				}
				if("006".equals(uType)){
					countyType =useraccount.getUsername().substring(0,1);
				} 
			}
			if(uType.length()==6){
				countyType =useraccount.getUsertype().substring(2,3);
			}

			if(uType.length()==2){ //系統管理員/中央機關
				if("11".equals(uType)){
					countyType = CateringServiceCode.AUTHEN_SUPER_COUNTY_TYPE; //幫Ric改 回傳super_admin字串表示為中央主管機關身分 20140425 KC
				}
			}
			dbsession.close();
			return countyType;
		}
	public static String getCountyTypeBySchoolId(Integer sid) {
		/* 以學校編號取得縣市代碼
		 * 取出DB中對應Sid的CountyId去回傳CountyType
		 */
			//SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
			//Session dbsession = sessionfactory.openSession();
			Session dbsession=getSession();//getSessionFactory().openSession();
			Criteria criteria = dbsession.createCriteria(School.class);
			criteria.add(Restrictions.eq("schoolId", sid));
			String countyType = "0";

			if(sid == null){
				return countyType;
			}
			List<School> queryObject = criteria.list();
			if (queryObject.size() == 0) {
				dbsession.close();
				return countyType;
			}
			Iterator<School> iterator = queryObject.iterator();
			School school = null;
			while (iterator.hasNext()) {
				school = iterator.next();
			}
			countyType = getCountyTypeByCountryNum(school.getCountyId()); 
			
			dbsession.close();
			return countyType;
		}
	public static Integer getCountyNumBySchoolId(Integer sid) {
		/* 以學校編號取得縣市編號
		 * 
		 */
			//SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
			//Session dbsession = sessionfactory.openSession();
		Session dbsession=getSession();//getSessionFactory().openSession();
			Criteria criteria = dbsession.createCriteria(School.class);
			criteria.add(Restrictions.eq("schoolId", sid));
			Integer countyNum = 0;

			if(sid == null){
				return countyNum;
			}
			List<School> queryObject = criteria.list();
			if (queryObject.size() == 0) {
				dbsession.close();
				return countyNum;
			}
			Iterator<School> iterator = queryObject.iterator();
			School school = null;
			while (iterator.hasNext()) {
				school = iterator.next();
			}
			countyNum = school.getCountyId(); 
			
			dbsession.close();
			return countyNum;
		}

	//由帳號找出schoolid  KC
	public static Integer getSchoolIdByUsername(String uname){
		Integer sid=0;
		String schoolCode="";
		schoolCode=uname.substring(1);
		
		//SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
		//Session dbsession = sessionfactory.openSession();
		Session dbsession=getSession();//getSessionFactory().openSession();
		Criteria criteria = dbsession.createCriteria(School.class);
		criteria.add(Restrictions.eq("schoolCode", schoolCode));
		School result = (School) criteria.uniqueResult();
		dbsession.close();
		if (result==null) {
			return sid;
		}else{
			return result.getSchoolId();
		}
	}
	
	//由帳號找出kitchenid  KC
	public static Integer getKitchenIddByUsername(String uname){
		Integer kid=-1;		
		//SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
		//Session dbsession = sessionfactory.openSession();
		Session dbsession=getSession();//getSessionFactory().openSession();
		
		Criteria criteria = dbsession.createCriteria(Useraccount.class);
		criteria.add(Restrictions.eq("username", uname));
		Useraccount result = (Useraccount) criteria.uniqueResult();
		dbsession.close();
		if (result==null) {
			return kid;
		}else{
			return result.getKitchenId();
		}
	}
	
	
}
