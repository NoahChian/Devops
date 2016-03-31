package org.iii.ideas.test_case;

import java.util.Date;



import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.iii.ideas.catering_service.dao.Code;
import org.iii.ideas.catering_service.dao.CodeDAO;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.SchoolkitchenId;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.Userrole;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;


public class AddNewAccount {
	public static String INIT_PASSWORD="cee8d6b7ce52554fd70354e37bbf44a2";
	public static String INIT_EMAIL="";
	public static String INIT_TEL="";
	public static String INIT_ADDR="";
	public static String INIT_FAX="";
	public static String INIT_CHIEF="";
	public static String INIT_INSUREMENT="insurement";
	public static String INIT_NUTRITIONEIST="nutritionist";
	public static String INIT_QUALIFIER="qualifier";
	public static String TYPE_KITCHEN="005";
	public static String TYPE_SCHOOL="006";
	public static String TYPE_SCHOOL_NO_KITCHEN="007";
	private static  SessionFactory 	sessionFactory;
	
	public static void main(String args[]) throws Exception{
		/*String userAccount=args[0];
		String userName=args[1];
		Integer userCountyId=Integer.valueOf(args[2]);
		String userType=args[3];		
		*/
		//SessionFactory sessionFactory=HibernateUtil.buildSessionFactory();
		

		try{
			Configuration configuration=new Configuration();  
			  configuration.configure();  
			  ServiceRegistry sr= new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();  
			  sessionFactory=configuration.buildSessionFactory(sr);  

			
	      }catch (Throwable ex) { 
	         System.err.println("Failed to create sessionFactory object." + ex);
	         throw new ExceptionInInitializerError(ex); 
	      }
		System.out.println("entry");
		
		Session session = sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		
		insertAccount(session,"Z654321","連江縣東引鄉東引國小(測試)","Z",TYPE_SCHOOL,654321);
		insertAccount(session,"Z654322","連江縣東引鄉東引國中(測試)","Z",TYPE_SCHOOL,654322);
		/*insertAccount(session,"F014X04","新北市永和區秀朗國民小學附設幼兒園","F",TYPE_SCHOOL,8000001);
		insertAccount(session,"F014X72","新北市樹林區彭福國民小學附設幼兒園","F",TYPE_SCHOOL,8000002);
		insertAccount(session,"F014X61","新北市金山區金山國民.小學附設幼兒園","F",TYPE_SCHOOL,8000003);
		insertAccount(session,"F014X9I","新北市立永和幼兒園","F",TYPE_SCHOOL,8000004);
		insertAccount(session,"F014X9I-04","新北市立永和幼兒園民生分班","F",TYPE_SCHOOL,8000005);
		insertAccount(session,"F014X9I-01","新北市立永和幼兒園福和分班","F",TYPE_SCHOOL,8000006);
		insertAccount(session,"F014X9I-05","新北市立永和幼兒園博愛分班","F",TYPE_SCHOOL,8000007);
		insertAccount(session,"F014X9I-02","新北市立永和幼兒園忠孝分班","F",TYPE_SCHOOL,8000008);
		insertAccount(session,"F014X9I-03","新北市立永和幼兒園大同分班","F",TYPE_SCHOOL,8000009);
		insertAccount(session,"F014X9I-06","新北市立永和幼兒園保生分班","F",TYPE_SCHOOL,8000010);
		insertAccount(session,"F014X9H","新北市立三重幼兒園","F",TYPE_SCHOOL,8000011);
		insertAccount(session,"F014X9H-01","新北市立三重幼兒園厚德分班","F",TYPE_SCHOOL,8000012);
		insertAccount(session,"F014X9H-02","新北市立三重幼兒園崇德分班","F",TYPE_SCHOOL,8000013);
		*/
		/*他校供應
		AuthenUtil.setSessionFatory(sessionFactory);

		
		String hql_school="from School s where s.countyId=:cid and enable='1' ";
		Query queryObj=session.createQuery(hql_school);
		queryObj.setParameter("cid", 19);
		List<School> schoolList=queryObj.list();
		Iterator<School> ir=schoolList.iterator();
		while(ir.hasNext()){
			School row=ir.next();
			String hql_account="from Useraccount a where a.username=:name ";
			Query q=session.createQuery(hql_account);
			//String countyCode=AuthenUtil.getCountyTypeByCountryNum(row.getCountyId());
			//q.setParameter("name", countyCode+String.format("%06d", row.getSchoolId()));
			q.setParameter("name", "F"+row.getSchoolCode());
			System.out.println("####"+"F"+row.getSchoolCode());
			List tmp=q.list();
			Iterator tir=tmp.iterator();
			if (tir.hasNext()){				
				//System.out.println("===="+schoolAccount);
				
			}else{
				String schoolAccount="F"+row.getSchoolCode();
				insertAccount(session,schoolAccount,row.getSchoolName(),"F",TYPE_SCHOOL_NO_KITCHEN,row.getSchoolId());
				//System.out.println("***"+row.getSchoolName());
			}
			
		}
		*/
		System.out.println("leave");
	
		/*
		CodeDAO codedao=new CodeDAO();
		codedao.setSession(session);
		List<Code> codeList=codedao.findCodeListByType("county");
		Iterator<Code> ir=codeList.iterator();
		while(ir.hasNext()){
			Code county=ir.next();
			String kitchenAccount=county.getCode()+county.getName()+TYPE_KITCHEN+"T";
			String schoolAccount=county.getCode()+county.getName()+TYPE_SCHOOL+"T";
			insertAccount(session,kitchenAccount,kitchenAccount,county.getCode(),TYPE_KITCHEN);
			insertAccount(session,schoolAccount,schoolAccount,county.getCode(),TYPE_SCHOOL);
		}
		*/
		tx.commit();
		session.close();
		
	}
	
	private static void insertAccount(Session session,String userAccount,String userName,String userCountyId,String userType,Integer sid) throws Exception{

		
		Kitchen kitchen=new Kitchen();
		kitchen.setKitchenId(0);
		kitchen.setAddress(INIT_ADDR);
		kitchen.setChef(INIT_CHIEF);
		kitchen.setCompanyId(userAccount);
		kitchen.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
		kitchen.setEmail(INIT_EMAIL);
		kitchen.setEndDate(CateringServiceUtil.getCurrentTimestamp());
		kitchen.setFax(INIT_FAX);
		kitchen.setHaccp("");
		kitchen.setInsurement(INIT_INSUREMENT);
		kitchen.setKitchenName(userName);
		kitchen.setNutritionist(INIT_NUTRITIONEIST);
		kitchen.setOwnner(INIT_CHIEF);
		kitchen.setQualifier(INIT_QUALIFIER);
		kitchen.setTel(INIT_TEL);
		kitchen.setKitchenType(userType);
		kitchen.setPicturePath("");
		kitchen.setEnable(1);
	
		session.save(kitchen);
						
		Useraccount useraccount=new Useraccount();
		useraccount.setEmail(INIT_EMAIL);
		useraccount.setName(userName);
		useraccount.setUsername(userAccount);
		useraccount.setPassword(INIT_PASSWORD);
		useraccount.setUsertype(userType);
		useraccount.setKitchenId(kitchen.getKitchenId());
		useraccount.setEnable(1);
		session.save(useraccount);
		
		String roletype="";
		
		if (TYPE_SCHOOL.equals(userType) || TYPE_SCHOOL_NO_KITCHEN.equals(userType)){
			roletype="kSch";
		}else if (TYPE_KITCHEN.equals(userType)){
			roletype="kCom";
		}else{
			throw new Exception("type wrong");
		}
		
		
		
		Userrole userrole=new Userrole();
		userrole.setUsername(userAccount);
		userrole.setRoletype(roletype);
		userrole.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
		userrole.setUpdateTime(CateringServiceUtil.getCurrentTimestamp());
		session.save(userrole);
		
		Schoolkitchen sk =new Schoolkitchen();
		SchoolkitchenId skId=new SchoolkitchenId();
		skId.setKitchenId(kitchen.getKitchenId());
		skId.setSchoolId(sid);
		sk.setId(skId);
		sk.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
		session.save(sk);
		
		System.out.println("設定帳號:"+userAccount);
		
	}
	
	
}
