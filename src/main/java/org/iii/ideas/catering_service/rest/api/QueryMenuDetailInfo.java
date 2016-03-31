package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.DishBatchData;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public final class QueryMenuDetailInfo extends AbstractApiInterface<QueryMenuDetailInfoRequest, QueryMenuDetailInfoResponse> {

	@Override
	public void process() throws NamingException {
		// select * from menu where MenuDate between menuId = [mid]
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		Long mid = Long.valueOf( this.requestObj.getMid());
		Criteria criteria = dbSession.createCriteria(Batchdata.class);
		criteria.add( Restrictions.eq("batchDataId", mid) );
		criteria.add( Restrictions.eq("enable", 1) );
		int resStatus=0;
		
		List Batchdatas = criteria.list();
		Iterator<Batchdata> iterator = Batchdatas.iterator();
		this.responseObj.setResStatus(0);
		this.responseObj.setMsg("找不到資料");
		while (iterator.hasNext()) {
			Batchdata menu = iterator.next();
			this.responseObj.setMid( String.valueOf(menu.getBatchDataId()));
			this.responseObj.setSchoolName( HibernateUtil.querySchoolNameById(dbSession, menu.getSchoolId()));
			
			
			// 2015.04.20
			// 如果batchdata table中的原16菜色欄位皆為0時，則是因使用NodeJs的新程式功能所照成的結果。
			// 故須使用新方式進行查詢，否則則以原模式進行查詢。
			if( menu.getMainFoodId()==0 && menu.getMainFood1id()==0 && menu.getMainDishId()==0 && menu.getMainDish1id()==0 && 
				menu.getMainDish2id()==0 && menu.getMainDish3id()==0 && menu.getSubDish1id()==0 && menu.getSubDish2id()==0 && 
				menu.getSubDish3id()==0 && menu.getSubDish4id()==0 && menu.getSubDish5id()==0 && menu.getSubDish6id()==0 && 
				menu.getSoupId()==0 && menu.getVegetableId()==0 && menu.getDessertId()==0 && menu.getDessert1id()==0) {
				
				HashMap dishBatchDataInfo = new HashMap();
				dishBatchDataInfo.put("BatchDataId", menu.getBatchDataId());
				// 依DishType & DishOrder ASC排序
				HashMap orderByData = new HashMap();
				orderByData.put("DishType", "ASC");
				orderByData.put("DishOrder", "ASC");
				List dishBatchDataLst = HibernateUtil.getObjectListByFieldId(dbSession, 
						DishBatchData.class, dishBatchDataInfo, orderByData);
				
				HashMap<String, ArrayList> batchDataHshMap = new HashMap<String, ArrayList>();
				ArrayList<DishBatchData> aryBatchData_DishType1 = new ArrayList<DishBatchData>();
				ArrayList<DishBatchData> aryBatchData_DishType2 = new ArrayList<DishBatchData>();
				ArrayList<DishBatchData> aryBatchData_DishType3 = new ArrayList<DishBatchData>();
				ArrayList<DishBatchData> aryBatchData_DishType4 = new ArrayList<DishBatchData>();
				ArrayList<DishBatchData> aryBatchData_DishType5 = new ArrayList<DishBatchData>();
				ArrayList<DishBatchData> aryBatchData_DishType6 = new ArrayList<DishBatchData>();

				for(int i=0; i<dishBatchDataLst.size(); i++) {
					DishBatchData dishBatchDataObj = (DishBatchData) dishBatchDataLst.get(i);
					
					// 將DishBatchData中的菜色，放入相對應的欄位。
					switch(dishBatchDataObj.getDishType()) {
					case "1":
						aryBatchData_DishType1.add(dishBatchDataObj);
						batchDataHshMap.put("MainFood", aryBatchData_DishType1);
						break;
					case "2":
						aryBatchData_DishType2.add(dishBatchDataObj);
						batchDataHshMap.put("MainDish", aryBatchData_DishType2);
						break;
					case "3":
						aryBatchData_DishType3.add(dishBatchDataObj);
						batchDataHshMap.put("SubDish", aryBatchData_DishType3);
						break;
					case "4":
						aryBatchData_DishType4.add(dishBatchDataObj);
						batchDataHshMap.put("Vegetable", aryBatchData_DishType4);
						break;
					case "5":
						aryBatchData_DishType6.add(dishBatchDataObj);
						batchDataHshMap.put("Dessert", aryBatchData_DishType6);
						break;
					case "6":
						aryBatchData_DishType5.add(dishBatchDataObj);
						batchDataHshMap.put("Soup", aryBatchData_DishType5);
						break;
					}
				}

				// 初始化各欄位
				this.responseObj.setMain("");
				this.responseObj.setMain2("");
				this.responseObj.setMajor("");
				this.responseObj.setMajor1("");
				this.responseObj.setMajor2("");
				this.responseObj.setMajor3("");
				this.responseObj.setSide1("");
				this.responseObj.setSide2("");
				this.responseObj.setSide3("");
				this.responseObj.setSide4("");
				this.responseObj.setSide5("");
				this.responseObj.setSide6("");
				this.responseObj.setSoup("");
				this.responseObj.setVegetableName("");
				this.responseObj.setMeals("");
				this.responseObj.setMeals2("");
				
				// 將此ArrayList中的值反解成對應BatchData table欄位的代碼
				HashMap<String, DishBatchData> procHashMap = CateringServiceUtil.decDishType2Field(batchDataHshMap);
				for(String key: procHashMap.keySet()) {
					System.out.println("[修改] proc key:"+key+"\tvalue:"+((DishBatchData) procHashMap.get(key)).getDishName());
					switch(key) {
					case "MainFoodId":
						this.responseObj.setMain( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "MainFood1Id":
						this.responseObj.setMain2( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "MainDishId":
						this.responseObj.setMajor( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "MainDish1Id":
						this.responseObj.setMajor1( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "MainDish2Id":
						this.responseObj.setMajor2( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "MainDish3Id":
						this.responseObj.setMajor3( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "SubDish1Id":
						this.responseObj.setSide1( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SubDish2Id":
						this.responseObj.setSide2( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SubDish3Id":
						this.responseObj.setSide3( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SubDish4Id":
						this.responseObj.setSide4( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SubDish5Id":
						this.responseObj.setSide5( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SubDish6Id":
						this.responseObj.setSide6( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SoupId":
						this.responseObj.setSoup( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "VegetableId":
						this.responseObj.setVegetableName( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "DessertId":
						this.responseObj.setMeals( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "Dessert1Id":
						this.responseObj.setMeals2( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
					}
				}

			} else {
				
				this.responseObj.setMain(menu.getMainFoodId()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainFoodId()): "");
				this.responseObj.setMain2(menu.getMainFood1id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainFood1id()): "");
				this.responseObj.setMajor(menu.getMainDishId()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainDishId()): "");
				this.responseObj.setMajor1(menu.getMainDish1id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainDish1id()): "");
				this.responseObj.setMajor2(menu.getMainDish2id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainDish2id()): "");
				this.responseObj.setMajor3(menu.getMainDish3id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainDish3id()): "");
				this.responseObj.setSide1(menu.getSubDish1id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish1id()): "");
				this.responseObj.setSide2(menu.getSubDish2id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish2id()): "");
				this.responseObj.setSide3(menu.getSubDish3id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish3id()): "");
				this.responseObj.setSide4(menu.getSubDish4id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish4id()): "");
				this.responseObj.setSide5(menu.getSubDish5id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish5id()): "");
				this.responseObj.setSide6(menu.getSubDish6id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish6id()): "");
				this.responseObj.setMeals(menu.getDessertId()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getDessertId()): "");
				this.responseObj.setMeals2(menu.getDessert1id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getDessert1id()): "");
				this.responseObj.setSoup(menu.getSoupId()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSoupId()): "");
				this.responseObj.setVegetableName(menu.getVegetableId()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getVegetableId()): "");
				
			}
			


			
//			this.responseObj.setMain(HibernateUtil.queryDishNameById( dbSession, menu.getMainFoodId()  ));//check
//			this.responseObj.setMain2(HibernateUtil.queryDishNameById( dbSession, menu.getMainFood1id()  ));//check
//			this.responseObj.setMajor(HibernateUtil.queryDishNameById( dbSession, menu.getMainDishId() ));//check
//			this.responseObj.setMajor1(HibernateUtil.queryDishNameById( dbSession, menu.getMainDish1id()  ));
//			this.responseObj.setMajor2(HibernateUtil.queryDishNameById( dbSession, menu.getMainDish2id() ));
//			this.responseObj.setMajor3(HibernateUtil.queryDishNameById( dbSession, menu.getMainDish3id() ));
//			this.responseObj.setMeals(HibernateUtil.queryDishNameById( dbSession, menu.getDessertId()));//check
//			this.responseObj.setSide1(HibernateUtil.queryDishNameById( dbSession, menu.getSubDish1id()  ));
//			this.responseObj.setSide2(HibernateUtil.queryDishNameById( dbSession, menu.getSubDish2id()  ));
//			this.responseObj.setSide3(HibernateUtil.queryDishNameById( dbSession, menu.getSubDish3id()  ));
//			this.responseObj.setSide4(HibernateUtil.queryDishNameById( dbSession, menu.getSubDish4id()  ));
//			this.responseObj.setSide5(HibernateUtil.queryDishNameById( dbSession, menu.getSubDish5id()  ));
//			this.responseObj.setSide6(HibernateUtil.queryDishNameById( dbSession, menu.getSubDish6id()  ));
//			this.responseObj.setMeals(HibernateUtil.queryDishNameById( dbSession, menu.getDessertId()  ));
//			this.responseObj.setMeals2(HibernateUtil.queryDishNameById( dbSession, menu.getDessert1id()  ));
			this.responseObj.setVegetable(menu.getTypeVegetable());
			this.responseObj.setGrains(menu.getTypeGrains());
//			this.responseObj.setSoup(HibernateUtil.queryDishNameById( dbSession, menu.getSoupId()  ));
//			this.responseObj.setVegetableName(HibernateUtil.queryDishNameById( dbSession, menu.getVegetableId()  ));
			this.responseObj.setFruit(menu.getTypeFruit());
			this.responseObj.setCalorie(menu.getCalorie());
			this.responseObj.setMeatBeans(menu.getTypeMeatBeans()  );//check
			this.responseObj.setMilk(menu.getTypeMilk());
			this.responseObj.setOil(menu.getTypeOil());
			this.responseObj.setMenuDate(menu.getMenuDate());
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
		
	}
	
	

}
