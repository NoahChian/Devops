package org.iii.ideas.catering_service.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.SessionImpl;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter2;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.ChineseDateFormat.Field;

@Transactional
public class ViewDishAndIngredientDAO {
	private static final Logger log = LoggerFactory.getLogger(ViewDishAndIngredientDAO.class);
	// property constants
	
	public ViewDishAndIngredientDAO(){
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
	public ViewDishAndIngredientDAO(Session dbSession){
		setSession(dbSession);
	}
	private Session dbSession;
	private SessionFactory sessionFactory;
	/* 依照條件去找出所的結果-通常為Excel所用
	 * 先去依照帳號權限設定query條件
	 * 再依照輸入的參數去設定query條件
	 */
	public List<ViewDishAndIngredient> queryIngredients(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter viParameter, Integer queryLimit) {
		Criteria viewIngredientCriteria = dbSession.createCriteria(ViewDishAndIngredient.class);
		
		viewIngredientCriteria= setQueryAuth(username,userType, viewIngredientCriteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewIngredientCriteria= setQueryCriteria(kitchenId,schoolId, countyId,begDate,endDate,viParameter,queryLimit,viewIngredientCriteria);
		
		List<ViewDishAndIngredient> viewIngredient = (List<ViewDishAndIngredient>) viewIngredientCriteria.list();
		return viewIngredient;
	}
	/* 依照條件去找出所的結果-通常為API要分頁時所用
	 * 先去依照帳號權限設定query條件
	 * 再依照輸入的參數去設定query條件
	 * 最後設定要query的頁數
	 */
	public List<ViewDishAndIngredient> queryIngredientsByPage(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter viParameter, Integer queryLimit, int page, int perPage) {
		Criteria viewIngredientCriteria = dbSession.createCriteria(ViewDishAndIngredient.class);
		viewIngredientCriteria= setQueryAuth(username,userType, viewIngredientCriteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewIngredientCriteria= setQueryCriteria(kitchenId,schoolId, countyId,begDate,endDate,viParameter,queryLimit,viewIngredientCriteria);
		//處理分頁
		if(page != 0 && perPage !=0){
			int startIndex = (page-1) * perPage;
			viewIngredientCriteria.setFirstResult(startIndex);
			viewIngredientCriteria.setMaxResults(perPage);
		}
		List<ViewDishAndIngredient> viewIngredient = (List<ViewDishAndIngredient>) viewIngredientCriteria.list();
		return viewIngredient;
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
			viewIngredientCriteria.add(Restrictions.eq(ViewDishAndIngredient.COUNTY_ID, AuthenUtil.getCountyNumByUsername(username)));
		}
		return viewIngredientCriteria;
	}
	public Criteria setQueryCriteria(Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter viParameter, Integer queryLimit, Criteria viewIngredientCriteria  ){

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
		if(!"0".equals(Integer.toString(queryLimit))){
			viewIngredientCriteria.setMaxResults(queryLimit);
		}
		//加入order by menudate,kitchenid,schoolid
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.MENU_DATE));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.KITCHEN_ID));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.SCHOOL_ID));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.DISH_ID));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.INGREDIENT_BATCH_ID));

		return viewIngredientCriteria;
	}

	/*
	 * 20140908 KC
	 * 設置多重查詢條件 
	 * 
	 * */
	public Criteria setDisjunctionCriteria(Criteria viewIngredientCriteria,ArrayList<ViewDishAndIngredientParameter> condList){
		Disjunction paDisjunction = Restrictions.disjunction();  
		for (ViewDishAndIngredientParameter vp :condList){
			Conjunction  paConjunction =Restrictions.conjunction();
			HashMap<String, Object> pa =vp.getMap();
			for (Entry<String ,Object> item : pa.entrySet()) {
				if (!"".equals(item.getValue())){
					paConjunction.add(Restrictions.like(item.getKey(),"%"+ item.getValue()+"%"));
				}
			}
			paDisjunction.add(paConjunction);
		}
		
		viewIngredientCriteria.add(paDisjunction);
		return viewIngredientCriteria;
	}

	/*
	 * 20140908 KC
	 * 以多重條件查詢
	 * 
	 * */
	public List<ViewDishAndIngredient> queryIngredients(String username ,String userType ,String begDate, String endDate, ArrayList<ViewDishAndIngredientParameter> viParameterList, Integer queryLimit, int page, int perPage) {
		Criteria viewIngredientCriteria = dbSession.createCriteria(ViewDishAndIngredient.class);
		viewIngredientCriteria= setQueryAuth(username,userType, viewIngredientCriteria);
		viewIngredientCriteria= setDisjunctionCriteria(viewIngredientCriteria,viParameterList);
		viewIngredientCriteria.add(Restrictions.between(ViewDishAndIngredient.MENU_DATE, begDate,endDate));
		//處理分頁
		if(page != 0 && perPage !=0){
			int startIndex = (page-1) * perPage;
			viewIngredientCriteria.setFirstResult(startIndex);
			viewIngredientCriteria.setMaxResults(perPage);
		}
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.MENU_DATE));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.KITCHEN_ID));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.SCHOOL_ID));
		List<ViewDishAndIngredient> viewIngredient = (List<ViewDishAndIngredient>) viewIngredientCriteria.list();
		return viewIngredient;
	}
	public Integer queryIngredientsCount(String username ,String userType ,String begDate, String endDate, ArrayList<ViewDishAndIngredientParameter> viParameterList) {
		Criteria viewIngredientCriteria = dbSession.createCriteria(ViewDishAndIngredient.class);
		viewIngredientCriteria.setProjection(Projections.rowCount());
		viewIngredientCriteria= setQueryAuth(username,userType, viewIngredientCriteria);
		viewIngredientCriteria= setDisjunctionCriteria(viewIngredientCriteria,viParameterList);
		viewIngredientCriteria.add(Restrictions.between(ViewDishAndIngredient.MENU_DATE, begDate,endDate));
		return  ((Long)viewIngredientCriteria.uniqueResult()).intValue();
	}	

	/**
	 * 20140818 Ric
	 * 查詢菜單總數量
	 * @return int
	 */
	public int queryTotelIngredientCount(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter viParameter, Integer queryLimit){
		Criteria viewIngredientCriteria = dbSession.createCriteria(ViewDishAndIngredient.class);
		viewIngredientCriteria.setProjection(Projections.rowCount());
		viewIngredientCriteria= setQueryAuth(username,userType, viewIngredientCriteria);
		viewIngredientCriteria= setQueryCriteria(kitchenId,schoolId, countyId,begDate,endDate,viParameter,queryLimit,viewIngredientCriteria);
		return ((Number)viewIngredientCriteria.uniqueResult()).intValue();
	}
	
	/* 依照條件去找出所的結果-通常為API要分頁時所用
	 * 先去依照帳號權限設定query條件
	 * 再依照輸入的參數去設定query條件
	 * 最後設定要query的頁數
	 * -------------------
	 * 2015.01.30 By Steven
	 * 1. 改已『ViewDishAndIngredient2』這個view表做為查詢的table。
	 * 該method用於『食材資料查詢』之『供應商/製造商』之複合查詢
	 */
	public List<ViewDishAndIngredient2> queryIngredientsByPage2(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewDishAndIngredientParameter viParameter, Integer queryLimit, int page, int perPage) {
		Criteria viewIngredient2Criteria = dbSession.createCriteria(ViewDishAndIngredient2.class);
		viewIngredient2Criteria= setQueryAuth(username,userType, viewIngredient2Criteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewIngredient2Criteria= setQueryCriteria(kitchenId,schoolId, countyId,begDate,endDate,viParameter,queryLimit,viewIngredient2Criteria);
		//處理分頁
		if(page != 0 && perPage !=0){
			int startIndex = (page-1) * perPage;
			viewIngredient2Criteria.setFirstResult(startIndex);
			viewIngredient2Criteria.setMaxResults(perPage);
		}
		List<ViewDishAndIngredient2> viewIngredient2 = (List<ViewDishAndIngredient2>) viewIngredient2Criteria.list();
		return viewIngredient2;
	}

	public List<ViewDishAndIngredient2> queryIngredients2(String username ,String userType ,String begDate, String endDate, ArrayList<ViewDishAndIngredientParameter> viParameterList, Integer queryLimit, int page, int perPage) {
		Criteria viewIngredientCriteria = dbSession.createCriteria(ViewDishAndIngredient2.class);
		viewIngredientCriteria= setQueryAuth(username,userType, viewIngredientCriteria);
		viewIngredientCriteria= setDisjunctionCriteria(viewIngredientCriteria,viParameterList);
		viewIngredientCriteria.add(Restrictions.between(ViewDishAndIngredient.MENU_DATE, begDate,endDate));
		//處理分頁
		if(page != 0 && perPage !=0){
			int startIndex = (page-1) * perPage;
			viewIngredientCriteria.setFirstResult(startIndex);
			viewIngredientCriteria.setMaxResults(perPage);
		}
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.MENU_DATE));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.KITCHEN_ID));
		viewIngredientCriteria.addOrder(Order.asc(ViewDishAndIngredient.SCHOOL_ID));
		List<ViewDishAndIngredient2> viewIngredient = (List<ViewDishAndIngredient2>) viewIngredientCriteria.list();
		return viewIngredient;
	}
	public Integer queryIngredientsCount2(String username ,String userType ,String begDate, String endDate, ArrayList<ViewDishAndIngredientParameter> viParameterList) {
		Criteria viewIngredientCriteria = dbSession.createCriteria(ViewDishAndIngredient2.class);
		viewIngredientCriteria.setProjection(Projections.rowCount());
		viewIngredientCriteria= setQueryAuth(username,userType, viewIngredientCriteria);
		viewIngredientCriteria= setDisjunctionCriteria(viewIngredientCriteria,viParameterList);
		viewIngredientCriteria.add(Restrictions.between(ViewDishAndIngredient.MENU_DATE, begDate,endDate));
		return  ((Long)viewIngredientCriteria.uniqueResult()).intValue();
	}
	
	//+++------------------
	
	public static ViewDishAndIngredientDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ViewDishAndIngredientDAO) ctx.getBean("IngredientbatchdataDAO");
	}

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}
}
