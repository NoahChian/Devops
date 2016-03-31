package org.iii.ideas.catering_service.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter2;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ViewDishAndIngredient2DAO {
	private static final Logger log = LoggerFactory.getLogger(ViewDishAndIngredient2DAO.class);
	// property constants
	
	public ViewDishAndIngredient2DAO(){
		//this.sessionFactory = HibernateUtil.buildSessionFactory();
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public void setSession(Session session){
		this.dbSession=session;
	}
	protected void initDao() {
		// do nothing
	}
	public ViewDishAndIngredient2DAO(Session dbSession){
		setSession(dbSession);
	}
	private Session dbSession;
	private SessionFactory sessionFactory;
	public List<ViewDishAndIngredient2> queryIngredients(Integer kitchenId ,String begDate, String endDate) {
		Criteria viewIngredient2Criteria = dbSession.createCriteria(ViewDishAndIngredient2.class);
		
		viewIngredient2Criteria= setQueryCriteria(kitchenId,begDate,endDate,viewIngredient2Criteria);
		
		List<ViewDishAndIngredient2> viewIngredient2 = (List<ViewDishAndIngredient2>) viewIngredient2Criteria.list();
		return viewIngredient2;
	}
	
	public Criteria setQueryCriteria(Integer kitchenId ,String begDate, String endDate, Criteria viewIngredient2Criteria  ){
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(kitchenId))){
			viewIngredient2Criteria.add(Restrictions.eq(ViewDishAndIngredient2.KITCHEN_ID, kitchenId));
		}
//		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(schoolId))){
//			viewIngredient2Criteria.add(Restrictions.eq(ViewDishAndIngredient2.SCHOOL_ID, schoolId));
//		}
		
		viewIngredient2Criteria.add(Restrictions.between(ViewDishAndIngredient2.MENU_DATE, begDate,
				endDate));
		
		//避免soap介接資料與團膳業者相同時，影響線上食材匯出，固先以schoolkitchen紀錄之供餐對象學校為條件。
		SchoolkitchenDAO skDAO = new SchoolkitchenDAO(dbSession);
		List<Integer> schoollist = skDAO.querySchoolListByKitchen(kitchenId);
		viewIngredient2Criteria.add(Restrictions.in(ViewDishAndIngredient2.SCHOOL_ID, schoollist));
		viewIngredient2Criteria.add(Restrictions.eq(ViewDishAndIngredient2.MenuType, 1));
		viewIngredient2Criteria.add(Restrictions.eq(ViewDishAndIngredient2.Enable, 1));
		/*
		viewIngredient2Criteria.addOrder(Order.asc(ViewDishAndIngredient.MENU_DATE));
		viewIngredient2Criteria.addOrder(Order.asc(ViewDishAndIngredient.KITCHEN_ID));
		viewIngredient2Criteria.addOrder(Order.asc(ViewDishAndIngredient.SCHOOL_ID));
		viewIngredient2Criteria.addOrder(Order.asc(ViewDishAndIngredient.DISH_ID));
		viewIngredient2Criteria.addOrder(Order.asc(ViewDishAndIngredient.INGREDIENT_BATCH_ID));
		*/
		
		return viewIngredient2Criteria;
	}
	
	// 2015.01.30 By Steven 查詢『ViewDishAndIngredient2』這個view表
	public int queryTotelIngredientCount2(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter2 viParameter, Integer queryLimit){
		Criteria viewIngredient2Criteria = dbSession.createCriteria(ViewDishAndIngredient2.class);
		// 加入限制 不查詢大學(school code代碼長度4碼) , 故查詢5碼以上的school code
//		viewIngredient2Criteria.add(Restrictions.sqlRestriction("schoolcode REGEXP '[a-zA-Z0-9_]{5,}'"));
		viewIngredient2Criteria.setProjection(Projections.rowCount());
		viewIngredient2Criteria= setQueryAuth(username,userType, viewIngredient2Criteria);
		viewIngredient2Criteria= setQueryCriteria2(kitchenId,schoolId, countyId,begDate,endDate,viParameter,queryLimit,viewIngredient2Criteria);
		return ((Number)viewIngredient2Criteria.uniqueResult()).intValue();
	}
	
	public Criteria setQueryAuth(String username,String userType,Criteria viewIngredientCriteria ){
		if (CateringServiceCode.USERTYPE_KITCHEN.equals(userType)){
			//005 團膳業者
			viewIngredientCriteria.add(Restrictions.eq(ViewDishAndIngredient.KITCHEN_ID, AuthenUtil.getKitchenIddByUsername(username)));
		}else if (CateringServiceCode.USERTYPE_SCHOOL.equals(userType)){
			//006自設廚房
			viewIngredientCriteria.add(Restrictions.eq(ViewDishAndIngredient.KITCHEN_ID, AuthenUtil.getKitchenIddByUsername(username)));
		}else if (CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)){
			//007受供餐廚房
			viewIngredientCriteria.add(Restrictions.eq(ViewDishAndIngredient.SCHOOL_ID, AuthenUtil.getSchoolIdByUsername(username)));
		}else if (CateringServiceCode.USERTYPE_GOV_CENTRAL.equals(userType)){
			//主管機關-中央
		}else{
			//主管機關-地方
			int countyId = AuthenUtil.getCountyNumByUsername(username);
			
			if(countyId <= CateringServiceCode.COUNTY_NUM && countyId != 0){
				viewIngredientCriteria.add(Restrictions.eq(ViewDishAndIngredient.COUNTY_ID, countyId));
			}
		}
		return viewIngredientCriteria;
	}
	
	public Criteria setQueryCriteria2(Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter2 viParameter, Integer queryLimit, Criteria viewIngredientCriteria2  ){

		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(kitchenId))){
			viewIngredientCriteria2.add(Restrictions.eq(ViewDishAndIngredient2.KITCHEN_ID, kitchenId));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(schoolId))){
			viewIngredientCriteria2.add(Restrictions.eq(ViewDishAndIngredient2.SCHOOL_ID, schoolId));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(countyId))){
			viewIngredientCriteria2.add(Restrictions.eq(ViewDishAndIngredient2.COUNTY_ID, countyId));
		}
		viewIngredientCriteria2.add(Restrictions.between(ViewDishAndIngredient2.MENU_DATE, begDate,
				endDate));

		// 判斷其餘like欄位
		int nCondi = 0;
		HashMap<String, Object> pa =viParameter.getMap();
		for (Entry<String ,Object> item : pa.entrySet()) {
			viewIngredientCriteria2.add(Restrictions.like(item.getKey(), "%"+ item.getValue()+"%"));
		}
		if(!"0".equals(Integer.toString(queryLimit))){
			viewIngredientCriteria2.setMaxResults(queryLimit);
		}
		//加入order by menudate,kitchenid,schoolid
		viewIngredientCriteria2.addOrder(Order.asc(ViewDishAndIngredient2.MENU_DATE));
		viewIngredientCriteria2.addOrder(Order.asc(ViewDishAndIngredient2.KITCHEN_ID));
		viewIngredientCriteria2.addOrder(Order.asc(ViewDishAndIngredient2.SCHOOL_ID));
		viewIngredientCriteria2.addOrder(Order.asc(ViewDishAndIngredient2.DISH_ID));
		viewIngredientCriteria2.addOrder(Order.asc(ViewDishAndIngredient2.INGREDIENT_BATCH_ID));

		return viewIngredientCriteria2;
	}
	
	/* 依照條件去找出所的結果-通常為API要分頁時所用
	 * 先去依照帳號權限設定query條件
	 * 再依照輸入的參數去設定query條件
	 * 最後設定要query的頁數
	 */
	public List<ViewDishAndIngredient2> queryIngredientsByPage(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter2 viParameter, Integer queryLimit, int page, int perPage) {
		Criteria viewIngredientCriteria = dbSession.createCriteria(ViewDishAndIngredient.class);
		viewIngredientCriteria= setQueryAuth(username,userType, viewIngredientCriteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewIngredientCriteria= setQueryCriteria2(kitchenId,schoolId, countyId,begDate,endDate,viParameter,queryLimit,viewIngredientCriteria);
		//處理分頁
		if(page != 0 && perPage !=0){
			int startIndex = (page-1) * perPage;
			viewIngredientCriteria.setFirstResult(startIndex);
			viewIngredientCriteria.setMaxResults(perPage);
		}
		List<ViewDishAndIngredient2> viewIngredient = (List<ViewDishAndIngredient2>) viewIngredientCriteria.list();
		return viewIngredient;
	}
	
	public List<ViewDishAndIngredient2> queryIngredientsByPage2(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter2 viParameter, Integer queryLimit, int page, int perPage) {
		Criteria viewIngredient2Criteria = dbSession.createCriteria(ViewDishAndIngredient2.class);
		// 加入限制 不查詢大學(school code代碼長度4碼) , 故查詢5碼以上的school code
//		viewIngredient2Criteria.add(Restrictions.sqlRestriction("schoolcode REGEXP '[a-zA-Z0-9_]{5,}'"));
		viewIngredient2Criteria= setQueryAuth(username,userType, viewIngredient2Criteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewIngredient2Criteria= setQueryCriteria2(kitchenId,schoolId, countyId,begDate,endDate,viParameter,queryLimit,viewIngredient2Criteria);
		//處理分頁
		if(page != 0 && perPage !=0){
			int startIndex = (page-1) * perPage;
			viewIngredient2Criteria.setFirstResult(startIndex);
			viewIngredient2Criteria.setMaxResults(perPage);
		}
		List<ViewDishAndIngredient2> viewIngredient2 = (List<ViewDishAndIngredient2>) viewIngredient2Criteria.list();
		return viewIngredient2;
	}
	
	public List<ViewDishAndIngredient2> queryIngredients2(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter2 viParameter, Integer queryLimit) {
		Criteria viewIngredientCriteria = dbSession.createCriteria(ViewDishAndIngredient2.class);
		// 加入限制 不查詢大學(school code代碼長度4碼) , 故查詢5碼以上的school code
//		viewIngredientCriteria.add(Restrictions.sqlRestriction("schoolcode REGEXP '[a-zA-Z0-9_]{5,}'"));
		viewIngredientCriteria= setQueryAuth(username,userType, viewIngredientCriteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewIngredientCriteria= setQueryCriteria(kitchenId,schoolId, countyId,begDate,endDate,viParameter,queryLimit,viewIngredientCriteria);
		
		List<ViewDishAndIngredient2> viewIngredient = (List<ViewDishAndIngredient2>) viewIngredientCriteria.list();
		return viewIngredient;
	}
	
	public Criteria setQueryCriteria(Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter2 viParameter, Integer queryLimit, Criteria viewIngredientCriteria  ){

		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(kitchenId))){
			viewIngredientCriteria.add(Restrictions.eq(ViewDishAndIngredient.KITCHEN_ID, kitchenId));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(schoolId))){
			viewIngredientCriteria.add(Restrictions.eq(ViewDishAndIngredient.SCHOOL_ID, schoolId));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(countyId))){
			viewIngredientCriteria.add(Restrictions.eq(ViewDishAndIngredient.COUNTY_ID, countyId));
		}
		viewIngredientCriteria.add(Restrictions.between(ViewDishAndIngredient.MENU_DATE, begDate,
				endDate));

		// 判斷其餘like欄位
		int nCondi = 0;
		HashMap<String, Object> pa =viParameter.getMap();
		for (Entry<String ,Object> item : pa.entrySet()) {
			viewIngredientCriteria.add(Restrictions.like(item.getKey(), "%"+ item.getValue()+"%"));
		}
		// Excel匯出全部 不使用queryLimit
		/*if(!"0".equals(Integer.toString(queryLimit))){
			viewIngredientCriteria.setMaxResults(queryLimit);
		}*/
		//加入order by menudate,kitchenid,schoolid
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.MENU_DATE));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.KITCHEN_ID));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.SCHOOL_ID));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.DISH_ID));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient2.INGREDIENT_BATCH_ID));

		return viewIngredientCriteria;
	}
}
