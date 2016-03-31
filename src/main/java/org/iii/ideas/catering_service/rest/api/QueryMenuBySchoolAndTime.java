package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.DishBatchData;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public final class QueryMenuBySchoolAndTime extends AbstractApiInterface<QueryMenuBySchoolAndTimeRequest, QueryMenuBySchoolAndTimeResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		// select * from menu where MenuDate between [begDate] and [endDate] and SchoolId = [sid]
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		
		if (this.getUserType()==null){
			//USERTYPE_SCHOOL_NO_KITCHEN
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		String userType=this.getUserType();
		
		int kid = this.getKitchenId();
		Integer sid = Integer.valueOf(this.requestObj.getSid());
		
		String begDate = this.requestObj.getStartDate();
		String endDate = this.requestObj.getEndDate();
		
		if(CateringServiceUtil.isEmpty(begDate) || CateringServiceUtil.isEmpty(endDate)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認日期區間填寫正確!!");
			return;
		}
		begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
		endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));

		
		//檢查是否超過日期的查詢範圍大小  20140221 KC
		if (CateringServiceUtil.isOverQueryRange(begDate,endDate)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查詢日期範圍太大，請縮小查詢範圍");
			return;
		}
		
	
		Criteria criteria = dbSession.createCriteria(Batchdata.class);
		criteria.add(Restrictions.between("menuDate", begDate, endDate) );
		criteria.add(Restrictions.eq("schoolId", sid) );
		//只搜尋午餐、且enable為1的。 add by Ellis 20150525
		criteria.add(Restrictions.eq("menuType", 1) );
		//新增，只搜尋enable為1的菜色。
		criteria.add(Restrictions.eq("enable", 1) );
		criteria.addOrder(Order.asc("menuDate") );
		
		if (!CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)){
			criteria.add(Restrictions.eq("kitchenId", kid) );
		}
		
		
		List lstMenu = new ArrayList();;
		List Menus = criteria.list();
		Iterator<Batchdata> iterator = Menus.iterator();
		int count = 0;
		while (iterator.hasNext()) {
			MenuObject menuObj = new MenuObject();
			Batchdata menu = iterator.next();
			menuObj.setMid(String.valueOf(menu.getBatchDataId() ));
			menuObj.setMenuDate(menu.getMenuDate());
			menuObj.setSchoolName(HibernateUtil.querySchoolNameById(dbSession, menu.getSchoolId()) );

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
				menuObj.setMain("");
				menuObj.setMain2("");
				menuObj.setMajor("");
				menuObj.setMajor1("");
				menuObj.setMajor2("");
				menuObj.setMajor3("");
				menuObj.setSide1("");
				menuObj.setSide2("");
				menuObj.setSide3("");
				menuObj.setSide4("");
				menuObj.setSide5("");
				menuObj.setSide6("");
				menuObj.setSoup("");
				menuObj.setVegetable("");
				
				// 將此ArrayList中的值反解成對應BatchData table欄位的代碼
				HashMap<String, DishBatchData> procHashMap = CateringServiceUtil.decDishType2Field(batchDataHshMap);
				for(String key: procHashMap.keySet()) {
					switch(key) {
					case "MainFoodId":
						menuObj.setMain( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "MainFood1Id":
						menuObj.setMain2( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "MainDishId":
						menuObj.setMajor( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "MainDish1Id":
						menuObj.setMajor1( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "MainDish2Id":
						menuObj.setMajor2( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "MainDish3Id":
						menuObj.setMajor3( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;
						
					case "SubDish1Id":
						menuObj.setSide1( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SubDish2Id":
						menuObj.setSide2( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SubDish3Id":
						menuObj.setSide3( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SubDish4Id":
						menuObj.setSide4( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SubDish5Id":
						menuObj.setSide5( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SubDish6Id":
						menuObj.setSide6( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "SoupId":
						menuObj.setSoup( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					case "VegetableId":
						menuObj.setVegetable( ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "");
						break;

					}
				}

			} else {

				menuObj.setMain(menu.getMainFoodId()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainFoodId()): "");
				menuObj.setMain2(menu.getMainFood1id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainFood1id()): "");
				menuObj.setMajor(menu.getMainDishId()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainDishId()): "");
				menuObj.setMajor1(menu.getMainDish1id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainDish1id()): "");
				menuObj.setMajor2(menu.getMainDish2id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainDish2id()): "");
				menuObj.setMajor3(menu.getMainDish3id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getMainDish3id()): "");
				menuObj.setSide1(menu.getSubDish1id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish1id()): "");
				menuObj.setSide2(menu.getSubDish2id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish2id()): "");
				menuObj.setSide3(menu.getSubDish3id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish3id()): "");
				menuObj.setSide4(menu.getSubDish4id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish4id()): "");
				menuObj.setSide5(menu.getSubDish5id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish5id()): "");
				menuObj.setSide6(menu.getSubDish6id()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSubDish6id()): "");
				menuObj.setSoup(menu.getSoupId()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getSoupId()): "");
				menuObj.setVegetable(menu.getVegetableId()>0 ? HibernateUtil.queryDishNameById(dbSession,  menu.getVegetableId()): "");

			}
			
			
//			menuObj.setMain(HibernateUtil.queryDishNameById(dbSession, menu.getMainFoodId()  ));
//			menuObj.setMain2(HibernateUtil.queryDishNameById(dbSession, menu.getMainFood1id()  ));
//			menuObj.setMajor(HibernateUtil.queryDishNameById(dbSession, menu.getMainDishId()  ) );
//			menuObj.setMajor1(HibernateUtil.queryDishNameById(dbSession, menu.getMainDish1id()  ));
//			menuObj.setMajor2(HibernateUtil.queryDishNameById(dbSession, menu.getMainDish2id()  ));
//			menuObj.setMajor3(HibernateUtil.queryDishNameById(dbSession, menu.getMainDish3id()  ));
//			menuObj.setSide1(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish1id()  ));
//			menuObj.setSide2(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish2id()  ));
//			menuObj.setSide3(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish3id()  ));
//			menuObj.setSide4(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish4id()  ));
//			menuObj.setSide5(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish5id()  ));
//			menuObj.setSide6(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish6id()  ));
//			menuObj.setSoup(HibernateUtil.queryDishNameById(dbSession, menu.getSoupId()  ));
//			menuObj.setVegetable(HibernateUtil.queryDishNameById(dbSession, menu.getVegetableId()  ));
			
			menuObj.setCalorie(menu.getCalorie());
			//this.responseObj.getMenu().add(menuObj);
			lstMenu.add(menuObj);
		}

		this.responseObj.setMenu(lstMenu);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
}
