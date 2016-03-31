package org.iii.ideas.test_case;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.DishBatchData;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class AddDishbatchdataFromBatchdata {
	private static  SessionFactory sessionFactory;
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
		
		List<Batchdata> batchdataList=new ArrayList<Batchdata>();
		Session sessionM =sessionFactory.openSession();
		
		
		String hql="From Batchdata b where b.menuDate between :startDate and :endDate ";
		
		Query queryObj=sessionM.createQuery(hql);
		queryObj.setParameter("startDate", "2014/02/10");
		queryObj.setParameter("endDate", "2014/05/10");
		batchdataList=queryObj.list();	
		Iterator<Batchdata> ir= batchdataList.iterator();
		
		/*
		List<ViewDishAndIngredient> iblist=new ArrayList<ViewDishAndIngredient>();
		Session session =sessionFactory.openSession();
		String hql="From  ViewDishAndIngredient v where v.dishname is null ";
		Query queryObj=session.createQuery(hql);
		iblist=queryObj.list();
		Iterator<ViewDishAndIngredient> ir=iblist.iterator();
		*/
		
		while(ir.hasNext()){
			Session session=sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Batchdata row=ir.next();
			DishBatchData dishbatchdata=new DishBatchData();
			Long bid=row.getBatchDataId();
			String hql2=" From DishBatchData d where d.BatchDataId=:bid and d.DishType!='Seasoning'  ";
			Query queryObj2=session.createQuery(hql2);
			queryObj2.setParameter("bid", bid);
			Integer total=queryObj2.list().size();
			if (total>0){
				System.out.println("break bid:"+bid);
				session.close();
				continue;
			}
			
			//MainFoodId
			if (row.getMainFoodId()!=0){
				DishBatchData d1=new DishBatchData();
				String d1name=HibernateUtil.queryDishNameById(session, row.getMainFoodId());
				d1.setBatchDataId(bid);
				d1.setDishId(row.getMainFoodId());
				d1.setDishName(d1name);
				d1.setDishShowName(d1name);
				d1.setDishType("MainFoodId");
				d1.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d1);
			}
			
			
			//MainFood1Id
			if (row.getMainFood1id()!=0){
				DishBatchData d2=new DishBatchData();
				String d2name=HibernateUtil.queryDishNameById(session, row.getMainFood1id());
				d2.setBatchDataId(bid);
				d2.setDishId(row.getMainFood1id());
				d2.setDishName(d2name);
				d2.setDishShowName(d2name);
				d2.setDishType("MainFood1Id");
				d2.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d2);
			}
			
			//MainDishId
			if (row.getMainDishId()!=0){
				DishBatchData d3=new DishBatchData();
				String d3name=HibernateUtil.queryDishNameById(session, row.getMainDishId());
				d3.setBatchDataId(bid);
				d3.setDishId(row.getMainDishId());
				d3.setDishName(d3name);
				d3.setDishShowName(d3name);
				d3.setDishType("MainDishId");
				d3.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d3);
			}
			
			//MainDish1Id
			if (row.getMainDish1id()!=0){
				DishBatchData d4=new DishBatchData();
				String d4name=HibernateUtil.queryDishNameById(session, row.getMainDish1id());
				d4.setBatchDataId(bid);
				d4.setDishId(row.getMainDish1id());
				d4.setDishName(d4name);
				d4.setDishShowName(d4name);
				d4.setDishType("MainDish1Id");
				d4.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d4);
			}
			
			//MainDish2Id
			if (row.getMainDish2id()!=0){
				DishBatchData d5=new DishBatchData();
				String d5name=HibernateUtil.queryDishNameById(session, row.getMainDish2id());
				d5.setBatchDataId(bid);
				d5.setDishId(row.getMainDish2id());
				d5.setDishName(d5name);
				d5.setDishShowName(d5name);
				d5.setDishType("MainDish2Id");
				d5.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d5);
			}			
			
			//MainDish3Id
			if (row.getMainDish3id()!=0){
				DishBatchData d6=new DishBatchData();
				String d6name=HibernateUtil.queryDishNameById(session, row.getMainDish3id());
				d6.setBatchDataId(bid);
				d6.setDishId(row.getMainDish3id());
				d6.setDishName(d6name);
				d6.setDishShowName(d6name);
				d6.setDishType("MainDish3Id");
				d6.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d6);
			}				
			
			//SubDish1Id
			if (row.getSubDish1id()!=0){
				DishBatchData d7=new DishBatchData();
				String d7name=HibernateUtil.queryDishNameById(session, row.getSubDish1id());
				d7.setBatchDataId(bid);
				d7.setDishId(row.getSubDish1id());
				d7.setDishName(d7name);
				d7.setDishShowName(d7name);
				d7.setDishType("SubDish1Id");
				d7.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d7);
			}				
						
			//SubDish2Id
			if (row.getSubDish2id()!=0){
				DishBatchData d8=new DishBatchData();
				String d8name=HibernateUtil.queryDishNameById(session, row.getSubDish2id());
				d8.setBatchDataId(bid);
				d8.setDishId(row.getSubDish2id());
				d8.setDishName(d8name);
				d8.setDishShowName(d8name);
				d8.setDishType("SubDish2Id");
				d8.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d8);
			}				
			
			//SubDish3Id
			if (row.getSubDish3id()!=0){
				DishBatchData d9=new DishBatchData();
				String d9name=HibernateUtil.queryDishNameById(session, row.getSubDish3id());
				d9.setBatchDataId(bid);
				d9.setDishId(row.getSubDish3id());
				d9.setDishName(d9name);
				d9.setDishShowName(d9name);
				d9.setDishType("SubDish3Id");
				d9.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d9);
			}				
			
			//SubDish4Id
			if (row.getSubDish4id()!=0){
				DishBatchData d10=new DishBatchData();
				String d10name=HibernateUtil.queryDishNameById(session, row.getSubDish4id());
				d10.setBatchDataId(bid);
				d10.setDishId(row.getSubDish4id());
				d10.setDishName(d10name);
				d10.setDishShowName(d10name);
				d10.setDishType("SubDish4Id");
				d10.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d10);
			}				
						
			
			//SubDish5Id
			if (row.getSubDish5id()!=0){
				DishBatchData d11=new DishBatchData();
				String d11name=HibernateUtil.queryDishNameById(session, row.getSubDish5id());
				d11.setBatchDataId(bid);
				d11.setDishId(row.getSubDish5id());
				d11.setDishName(d11name);
				d11.setDishShowName(d11name);
				d11.setDishType("SubDish5Id");
				d11.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d11);
			}				

			
			//SubDish6Id
			if (row.getSubDish6id()!=0){
				DishBatchData d12=new DishBatchData();
				String d12name=HibernateUtil.queryDishNameById(session, row.getSubDish6id());
				d12.setBatchDataId(bid);
				d12.setDishId(row.getSubDish6id());
				d12.setDishName(d12name);
				d12.setDishShowName(d12name);
				d12.setDishType("SubDish6Id");
				d12.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d12);
			}	
	
			
			
			//VegetableId
			if (row.getVegetableId()!=0){
				DishBatchData d13=new DishBatchData();
				String d13name=HibernateUtil.queryDishNameById(session, row.getVegetableId());
				d13.setBatchDataId(bid);
				d13.setDishId(row.getVegetableId());
				d13.setDishName(d13name);
				d13.setDishShowName(d13name);
				d13.setDishType("VegetableId");
				d13.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d13);
			}	
			
			//SoupId
			if (row.getSoupId()!=0){
				DishBatchData d14=new DishBatchData();
				String d14name=HibernateUtil.queryDishNameById(session, row.getSoupId());
				d14.setBatchDataId(bid);
				d14.setDishId(row.getSoupId());
				d14.setDishName(d14name);
				d14.setDishShowName(d14name);
				d14.setDishType("SoupId");
				d14.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d14);
			}				
			
			
			//DessertId
			if (row.getDessertId()!=0){
				DishBatchData d15=new DishBatchData();
				String d15name=HibernateUtil.queryDishNameById(session, row.getDessertId());
				d15.setBatchDataId(bid);
				d15.setDishId(row.getDessertId());
				d15.setDishName(d15name);
				d15.setDishShowName(d15name);
				d15.setDishType("DessertId");
				d15.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d15);
			}				
			//Dessert1Id
			if (row.getDessert1id()!=0){
				DishBatchData d16=new DishBatchData();
				String d16name=HibernateUtil.queryDishNameById(session, row.getDessert1id());
				d16.setBatchDataId(bid);
				d16.setDishId(row.getDessert1id());
				d16.setDishName(d16name);
				d16.setDishShowName(d16name);
				d16.setDishType("DessertId");
				d16.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
				session.save(d16);
			}					
			
			System.out.println("update bid:"+bid);
			tx.commit();
			session.close();
			//dishbatchdata.setBatchDataId(row.getBatchDataId());
			//break;
		}
	
	}
}
