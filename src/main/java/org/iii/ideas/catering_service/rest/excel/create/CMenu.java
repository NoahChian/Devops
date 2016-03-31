package org.iii.ideas.catering_service.rest.excel.create;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.DishBatchData;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class CMenu implements IGenerateExcel {
	
	private int kid;
	
	private String sid;
	
	private String begDate;
	
	private String endDate;
	
	public CMenu(int kid, String sid, String begDate, String endDate) {
		this.kid = kid;
		this.sid = sid;
		this.begDate = begDate;
		this.endDate = endDate;
	}
	
	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		
		data.put("1",
				new Object[] { "學校", "日期", "主食一", "主食二", "主菜", "主菜一", "主菜二", "主菜三", "副菜一", "副菜二", "副菜三", "副菜四",
						"副菜五", "副菜六", "蔬菜", "湯品", "附餐一", "附餐二", "全榖根莖", "豆魚肉蛋", "蔬菜", "油脂與堅果種子", "水果", "乳品",
						"熱量" });
		// int kitchenId = kid;//昌港

		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		
		Criteria criteria = session.createCriteria(Batchdata.class);
		criteria.add(Restrictions.between("menuDate", begDate, endDate));
		criteria.add(Restrictions.eq("schoolId", Integer.valueOf(sid)));
		criteria.add(Restrictions.eq("kitchenId", kid));
		criteria.add(Restrictions.eq("menuType", 1));
		criteria.add(Restrictions.eq("enable", 1));
		criteria.addOrder(Order.asc("menuDate")).addOrder(Order.asc("schoolId"));
		int row = 2;
		List<Batchdata> Menus = criteria.list();
		Iterator<Batchdata> iterator = Menus.iterator();
		while (iterator.hasNext()) {
			Batchdata menu = iterator.next();
			
			
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
				List dishBatchDataLst = HibernateUtil.getObjectListByFieldId(session, 
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
				String zMainFoodId="", zMainFood1Id="", zMainDishId="", zMainDish1Id="", zMainDish2Id="", zMainDish3Id="", 
					zSubDish1Id="", zSubDish2Id="", zSubDish3Id="", zSubDish4Id="", zSubDish5Id="", zSubDish6Id="", 
					zSoupId="", zVegetableId="", zDessertId="", zDessert1Id="";

				// 將此ArrayList中的值反解成對應BatchData table欄位的代碼
				HashMap<String, DishBatchData> procHashMap = CateringServiceUtil.decDishType2Field(batchDataHshMap);
				for(String key: procHashMap.keySet()) {
					System.out.println("[修改] proc key:"+key+"\tvalue:"+((DishBatchData) procHashMap.get(key)).getDishName());
					switch(key) {
					case "MainFoodId":
						zMainFoodId = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;
						
					case "MainFood1Id":
						zMainFood1Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;
						
					case "MainDishId":
						zMainDishId = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;
						
					case "MainDish1Id":
						zMainDish1Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;
						
					case "MainDish2Id":
						zMainDish2Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;
						
					case "MainDish3Id":
						zMainDish3Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;
						
					case "SubDish1Id":
						zSubDish1Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;

					case "SubDish2Id":
						zSubDish2Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;

					case "SubDish3Id":
						zSubDish3Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;

					case "SubDish4Id":
						zSubDish4Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;

					case "SubDish5Id":
						zSubDish5Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;

					case "SubDish6Id":
						zSubDish6Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;

					case "SoupId":
						zSoupId = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;

					case "VegetableId":
						zVegetableId = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;

					case "DessertId":
						zDessertId = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;
						
					case "Dessert1Id":
						zDessert1Id = ((DishBatchData) procHashMap.get(key)).getDishName().length()>0 ? ((DishBatchData) procHashMap.get(key)).getDishName(): "";
						break;
					}
				}
				
				data.put(
				String.valueOf(row),
				new Object[] { HibernateUtil.querySchoolNameById(session, menu.getSchoolId()),
						menu.getMenuDate(), 
						zMainFoodId, zMainFood1Id, zMainDishId, zMainDish1Id, zMainDish2Id, 
						zMainDish3Id, zSubDish1Id, zSubDish2Id, zSubDish3Id, zSubDish4Id, 
						zSubDish5Id, zSubDish6Id, zVegetableId, zSoupId, zDessertId, 
						zDessert1Id, 
						menu.getTypeGrains(),		// 全榖根莖
						menu.getTypeMeatBeans(),	// 豆魚肉蛋
						menu.getTypeVegetable(),	// 蔬菜
						menu.getTypeOil(),			// 油脂與堅果種子
						menu.getTypeFruit(),		// 水果
						menu.getTypeMilk(),			// 乳品
						menu.getCalorie() });
				

			} else {

				data.put(
						String.valueOf(row),
						new Object[] { HibernateUtil.querySchoolNameById(session, menu.getSchoolId()),
								menu.getMenuDate(), 
								menu.getMainFoodId()>0 ? HibernateUtil.queryDishNameById(session,  menu.getMainFoodId()): "",
								menu.getMainFood1id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getMainFood1id()): "",
								menu.getMainDishId()>0 ? HibernateUtil.queryDishNameById(session,  menu.getMainDishId()): "",
								menu.getMainDish1id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getMainDish1id()): "",
								menu.getMainDish2id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getMainDish2id()): "",
								menu.getMainDish3id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getMainDish3id()): "",
								menu.getSubDish1id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getSubDish1id()): "",
								menu.getSubDish2id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getSubDish2id()): "",
								menu.getSubDish3id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getSubDish3id()): "",
								menu.getSubDish4id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getSubDish4id()): "",
								menu.getSubDish5id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getSubDish5id()): "",
								menu.getSubDish6id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getSubDish6id()): "",
								menu.getVegetableId()>0 ? HibernateUtil.queryDishNameById(session,  menu.getVegetableId()): "",	// 蔬菜
								menu.getSoupId()>0 ? HibernateUtil.queryDishNameById(session,  menu.getSoupId()): "",			// 湯品
								menu.getDessertId()>0 ? HibernateUtil.queryDishNameById(session,  menu.getDessertId()): "",		// 附餐一
								menu.getDessert1id()>0 ? HibernateUtil.queryDishNameById(session,  menu.getDessert1id()): "",	// 附餐二
								menu.getTypeGrains(),		// 全榖根莖
								menu.getTypeMeatBeans(),	// 豆魚肉蛋
								menu.getTypeVegetable(),	// 蔬菜
								menu.getTypeOil(),			// 油脂與堅果種子
								menu.getTypeFruit(),		// 水果
								menu.getTypeMilk(),			// 乳品
								menu.getCalorie() });
				
			}
			
			
			
//			data.put(
//					String.valueOf(row),
//					new Object[] { HibernateUtil.querySchoolNameById(session, menu.getSchoolId()),
//							menu.getMenuDate(), HibernateUtil.queryDishNameById(session, menu.getMainFoodId()),
//							HibernateUtil.queryDishNameById(session, menu.getMainFood1id()),
//							HibernateUtil.queryDishNameById(session, menu.getMainDishId()),
//							HibernateUtil.queryDishNameById(session, menu.getMainDish1id()),
//							HibernateUtil.queryDishNameById(session, menu.getMainDish2id()),
//							HibernateUtil.queryDishNameById(session, menu.getMainDish3id()),
//							HibernateUtil.queryDishNameById(session, menu.getSubDish1id()),
//							HibernateUtil.queryDishNameById(session, menu.getSubDish2id()),
//							HibernateUtil.queryDishNameById(session, menu.getSubDish3id()),
//							HibernateUtil.queryDishNameById(session, menu.getSubDish4id()),
//							HibernateUtil.queryDishNameById(session, menu.getSubDish5id()),
//							HibernateUtil.queryDishNameById(session, menu.getSubDish6id()),
//							HibernateUtil.queryDishNameById(session, menu.getVegetableId()),// 蔬菜
//							HibernateUtil.queryDishNameById(session, menu.getSoupId()),// 湯品
//							HibernateUtil.queryDishNameById(session, menu.getDessertId()),// 附餐一
//							HibernateUtil.queryDishNameById(session, menu.getDessert1id()),// 附餐二
//							menu.getTypeGrains(),// 全榖根莖
//							menu.getTypeMeatBeans(),// 豆魚肉蛋
//							menu.getTypeVegetable(),// 蔬菜
//							menu.getTypeOil(),// 油脂與堅果種子
//							menu.getTypeFruit(),// 水果
//							menu.getTypeMilk(),// 乳品
//							menu.getCalorie() });
			row++;
		}
		session.close();
		
		return data;
		
	}

}
