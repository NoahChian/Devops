package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Menu;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class QueryMenuByTime extends AbstractApiInterface<QueryMenuByTimeRequest, QueryMenuByTimeResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		// select * from menu where MenuDate between [begDate] and [endDate] and SchoolId = [sid]
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		int kid = this.getKitchenId();

		String begDate = this.requestObj.getStartDate();
		String endDate = this.requestObj.getEndDate();
		begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
		endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));

		//檢查是否超過日期的查詢範圍大小  20140221 KC
		if (CateringServiceUtil.isOverQueryRange(begDate,endDate)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查詢日期範圍太大，請縮小查詢範圍");
			return;
		}
		
		String currentDate = "";

		Criteria criteria = dbSession.createCriteria(Menu.class);
		criteria.add(Restrictions.between("menuDate", begDate, endDate));
		criteria.add(Restrictions.eq("kitchenId", kid));
		criteria.addOrder(Order.asc("menuDate"));
		// criteria.add( Restrictions.eq("id.schoolId", sid) );
		// criteria.add( Restrictions.between("menuDate", begDate, endDate) );

		List Menus = criteria.list();
		Iterator<Menu> iterator = Menus.iterator();
		while (iterator.hasNext()) {
			MenuObject menuObj = new MenuObject();
			Menu menu = iterator.next();
			menuObj.setMid(String.valueOf(menu.getMenuId()));
			menuObj.setMenuDate(menu.getMenuDate());
			menuObj.setSchoolName("");
			menuObj.setMain(HibernateUtil.queryDishNameById(dbSession, menu.getMainFoodId()));
			menuObj.setMain2(HibernateUtil.queryDishNameById(dbSession, menu.getMainFood1id()));
			menuObj.setMajor(HibernateUtil.queryDishNameById(dbSession, menu.getMainDishId()));
			menuObj.setMajor1(HibernateUtil.queryDishNameById(dbSession, menu.getMainDish1id()));
			menuObj.setMajor2(HibernateUtil.queryDishNameById(dbSession, menu.getMainDish2id()));
			menuObj.setMajor3(HibernateUtil.queryDishNameById(dbSession, menu.getMainDish3id()));
			menuObj.setSide1(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish1id()));
			menuObj.setSide2(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish2id()));
			menuObj.setSide3(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish3id()));
			menuObj.setSide4(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish4id()));
			menuObj.setSide5(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish5id()));
			menuObj.setSide6(HibernateUtil.queryDishNameById(dbSession, menu.getSubDish6id()));
			menuObj.setSoup(HibernateUtil.queryDishNameById(dbSession, menu.getSoupId()));
			menuObj.setVegetable(HibernateUtil.queryDishNameById(dbSession, menu.getVegetableId()));
			menuObj.setMeals(HibernateUtil.queryDishNameById(dbSession, menu.getDessertId()));
			menuObj.setCalorie(menu.getCalorie());
			if (!currentDate.equals(menu.getMenuDate())) {
				this.responseObj.getMenu().add(menuObj);
				currentDate = menu.getMenuDate();
			}
		}
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);

	}

}
