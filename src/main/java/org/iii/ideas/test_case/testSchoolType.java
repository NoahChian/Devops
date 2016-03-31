/*
 * 20140603 KC
 * 測試 schooltype用
 * 將想查詢的學校類型(幼兒園/小學/中學/高中/國立/私立/市立/等代碼(CateringServiceCode.SCHOOL_TYPE_* )
 * 相加後帶入dao.querySchoolListByTypeAndCounty()參數中
 * 就可以查出交集的 school集合
 */

package org.iii.ideas.test_case;

import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.util.CateringServiceCode;

public class testSchoolType {
	private static  SessionFactory 	sessionFactory;
	public static void main(String args[]) throws Exception{
		try{
			Configuration configuration=new Configuration();  
			  configuration.configure();  
			  ServiceRegistry sr= new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();  
			  sessionFactory=configuration.buildSessionFactory(sr);  

			
	      }catch (Throwable ex) { 
	         System.err.println("Failed to create sessionFactory object." + ex);
	         throw new ExceptionInInitializerError(ex); 
	      }
		
		SchoolDAO dao =new SchoolDAO();
		dao.setSession(sessionFactory.openSession());
		//要查詢的學校代碼集合
		Integer querySchoolType=CateringServiceCode.SCHOOL_TYPE_ELEMENTARY+CateringServiceCode.SCHOOL_TYPE_JOUNIOR;
		List <School> result=dao.querySchoolListByTypeAndCounty(querySchoolType, 27);
		if (result ==null || result.size()==0){
			System.out.println("=======沒有資料============");
			System.exit(0);
		}
		Iterator<School> ir=result.iterator();
		
		while(ir.hasNext()){
			System.out.println(ir.next().getSchoolName());	
		}
		
	}

}
