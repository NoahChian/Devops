package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
//import javax.persistence.Query;


import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.Criteria;
//import org.hibernate.SQLQuery;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Projections;
//import org.hibernate.criterion.Restrictions;
//import org.iii.ideas.catering_service.dao.Batchdata;
//import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
//import org.iii.ideas.catering_service.util.HibernateUtil;


public final class QueryNullIngredientBySchoolAndTime extends AbstractApiInterface<QueryNullIngredientBySchoolAndTimeRequest, QueryNullIngredientBySchoolAndTimeResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		// select * from menu where MenuDate between [begDate] and [endDate] and
		// SchoolId = [sid]
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		int kid = this.getKitchenId();
		//Integer sid = Integer.valueOf(this.requestObj.getSid());
		Integer countyId=AuthenUtil.getCountyNumByUsername(this.getUsername());
		String begDate = this.requestObj.getStartDate();
		String endDate = this.requestObj.getEndDate();

		if (CateringServiceUtil.isEmpty(begDate) || CateringServiceUtil.isEmpty(endDate)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認日期區間填寫正確!!");
			return;
		}
		begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
		endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));

		// 檢查是否超過日期的查詢範圍大小 20140221 KC
		/*if (CateringServiceUtil.isOverQueryRange(begDate, endDate)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查詢日期範圍太大，請縮小查詢範圍");
			return;  
		}*/

		//改用dishbatchdata 查 20140521 KC
		//20140606 Ric 改到HibernateUtil 
		/*String hql=" select s.schoolName, b.menuDate, k.kitchenName ,group_concat(d.DishName) ,b.batchDataId from Batchdata b,DishBatchData d,School s ,Kitchen k where "
				+ " b.batchDataId=d.BatchDataId and b.menuDate between :startDate and :endDate "
				+ " and b.schoolId=s.schoolId "
				+ " and d.DishId not in  (select dishId from Ingredientbatchdata  i where i.batchDataId=b.batchDataId ) "
				+ " and s.countyId=:countyId "
				+ " and k.kitchenId=b.kitchenId "
				+ " group by s.schoolName,b.menuDate,k.kitchenName,b.batchDataId "
				+ " order by b.menuDate asc ";
		
	    Query queryObj=dbSession.createQuery(hql);
		queryObj.setParameter("startDate",begDate);
		queryObj.setParameter("endDate", endDate);
		queryObj.setParameter("countyId", countyId);
		
		List<Object[]> result=queryObj.list();*/
		List<Object[]> result = HibernateUtil.queryNullIngredientBySchoolAndTime(this.dbSession, begDate, endDate, countyId, 0);
		Iterator<Object[]> ir=result.iterator();
		while(ir.hasNext()){
			Object[] row=ir.next();
			NullIngreduentObject nio = new NullIngreduentObject();
			nio.setDate((String) row[1]);
			nio.setNullIngreduents((String)row[3]);
			nio.setSchoolName((String)row[0]);
			nio.setKitchenName((String)row[2]);
			this.responseObj.getNullIngreduentList().add(nio);
		}
	
		
		/*	
		Criteria criteria = dbSession.createCriteria(Batchdata.class);
		criteria.add(Restrictions.between("menuDate", begDate, endDate));
		criteria.add(Restrictions.eq("schoolId", sid));
//		criteria.add(Restrictions.eq("kitchenId", kid));
		criteria.addOrder(Order.asc("menuDate"));

		List Menus = criteria.list();
		Iterator<Batchdata> iterator = Menus.iterator();		
		
		while (iterator.hasNext()) {
			MenuObject menuObj = new MenuObject();
			Batchdata menu = iterator.next();

			int batchDataId = menu.getBatchDataId();
			List<String> lostDish = new ArrayList<String>();
			String listIngredientString;

			if (!haveIngredient(menu.getMainFoodId(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getMainFoodId()));
			}

			if (!haveIngredient(menu.getMainFood1id(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getMainFood1id()));
			}

			if (!haveIngredient(menu.getMainDishId(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getMainDishId()));
			}

			if (!haveIngredient(menu.getMainDish1id(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getMainDish1id()));
			}

			if (!haveIngredient(menu.getMainDish2id(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getMainDish2id()));
			}

			if (!haveIngredient(menu.getMainDish3id(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getMainDish3id()));
			}

			if (!haveIngredient(menu.getSubDish1id(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish1id()));
			}

			if (!haveIngredient(menu.getSubDish2id(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish2id()));
			}

			if (!haveIngredient(menu.getSubDish3id(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish3id()));
			}

			if (!haveIngredient(menu.getSubDish4id(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish4id()));
			}

			if (!haveIngredient(menu.getSubDish5id(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish5id()));
			}

			if (!haveIngredient(menu.getSubDish6id(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish6id()));
			}

			if (!haveIngredient(menu.getSoupId(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getSoupId()));
			}

			if (!haveIngredient(menu.getVegetableId(), batchDataId)) {
				lostDish.add(HibernateUtil.queryDishNameById(dbSession, menu.getVegetableId()));
			}

			listIngredientString = transListStringToString(lostDish);
			System.out.println(listIngredientString);
			if (listIngredientString != null && !listIngredientString.equals("")) {
				NullIngreduentObject nio = new NullIngreduentObject();
				nio.setDate(menu.getMenuDate());
				nio.setNullIngreduents(listIngredientString);
				nio.setSchoolName(HibernateUtil.querySchoolNameById(dbSession, menu.getSchoolId()));
				this.responseObj.getNullIngreduentList().add(nio);
			}

		}
		
		*/
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
/*
	public boolean haveIngredient(int dishId, int batchDataId) {
		boolean result = true;
		if (dishId != 0) {
			Criteria criteria = dbSession.createCriteria(Ingredientbatchdata.class);
			criteria.add(Restrictions.eq("dishId", dishId));
			criteria.add(Restrictions.eq("batchDataId", batchDataId));
			criteria.setProjection(Projections.rowCount());
			if (((Number)criteria.uniqueResult()).intValue() != 0) {
				result = true;
			} else {
				result = false;
			}

		}
		return result;
	}
*/
	public String transListStringToString(List<String> list) {
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1)
				result += list.get(i).concat(",");
			else
				result += list.get(i);
		}
		return result;
	}
}
