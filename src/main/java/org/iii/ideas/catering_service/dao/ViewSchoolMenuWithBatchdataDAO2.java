package org.iii.ideas.catering_service.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
//import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter2;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ViewSchoolMenuWithBatchdataDAO2 {
	private static final Logger log = LoggerFactory.getLogger(ViewSchoolMenuWithBatchdataDAO2.class);
	// property constants
	
	public ViewSchoolMenuWithBatchdataDAO2(){
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
	public ViewSchoolMenuWithBatchdataDAO2(Session dbSession){
		setSession(dbSession);
	}
	private Session dbSession;
	private SessionFactory sessionFactory;
	/* 依照條件去找出所的結果-通常為Excel所用
	 * 先去依照帳號權限設定query條件
	 * 再依照輸入的參數去設定query條件
	 */
	public List<ViewSchoolMenuWithBatchdata2> queryMenu(String username ,String userType ,Integer kitchenId, Integer restaurantId,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewSchoolMenuParameter2 vmParameter, Integer queryLimit) {
		Criteria viewMenuCriteria = dbSession.createCriteria(ViewSchoolMenuWithBatchdata2.class);
		viewMenuCriteria= setQueryAuth2(username,userType, viewMenuCriteria);
		// 加入限制 不查詢大學(school code代碼長度4碼) , 故查詢5碼以上的school code
//		viewMenuCriteria.add(Restrictions.sqlRestriction("schoolcode REGEXP '[a-zA-Z0-9_]{5,}'"));
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewMenuCriteria= setQueryCriteria2(kitchenId,restaurantId ,schoolId, countyId,begDate,endDate,vmParameter,queryLimit,viewMenuCriteria);
		List<ViewSchoolMenuWithBatchdata2> viewMenu = (List<ViewSchoolMenuWithBatchdata2>) viewMenuCriteria.list();
		// 修正查詢結果內含null object的問題
		viewMenu.removeAll(Collections.singleton(null));
		return viewMenu;
	}
	/* 依照條件去找出所的結果-通常為API要分頁時所用
	 * 先去依照帳號權限設定query條件
	 * 再依照輸入的參數去設定query條件
	 * 最後設定要query的頁數
	 */
	public List<ViewSchoolMenuWithBatchdata2> queryMenuByPage(String username ,String userType ,Integer kitchenId ,Integer restaurantId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewSchoolMenuParameter2 vmParameter, Integer queryLimit, int page, int perPage) {
		Criteria viewMenuCriteria = dbSession.createCriteria(ViewSchoolMenuWithBatchdata2.class);
		// 加入限制 不查詢大學(school code代碼長度4碼) , 故查詢5碼以上的school code
//		viewMenuCriteria.add(Restrictions.sqlRestriction("schoolcode REGEXP '[a-zA-Z0-9_]{5,}'"));
		viewMenuCriteria= setQueryAuth2(username,userType, viewMenuCriteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewMenuCriteria= setQueryCriteria2(kitchenId, restaurantId, schoolId, countyId,begDate,endDate,vmParameter,queryLimit,viewMenuCriteria);
		//處理分頁
		if(page != 0 && perPage !=0){
			int startIndex = (page-1) * perPage;
			viewMenuCriteria.setFirstResult(startIndex);
			viewMenuCriteria.setMaxResults(perPage);
		}
		List<ViewSchoolMenuWithBatchdata2> viewMenu = (List<ViewSchoolMenuWithBatchdata2>) viewMenuCriteria.list();
		// 修正查詢結果內含null object的問題
		viewMenu.removeAll(Collections.singleton(null));
		return viewMenu;
	}
	
	public Criteria setQueryAuth(String username,String userType,Criteria viewMenuCriteria ){
		if (CateringServiceCode.USERTYPE_KITCHEN.equals(userType)){
			//005 團膳業者
			viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata.KITCHEN_ID, AuthenUtil.getKitchenIddByUsername(username)));
		}else if (CateringServiceCode.USERTYPE_SCHOOL.equals(userType)){
			//006自設廚房
			viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata.KITCHEN_ID, AuthenUtil.getKitchenIddByUsername(username)));
		}else if (CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)){
			//007受供餐學校
			viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata.SCHOOL_ID, AuthenUtil.getSchoolIdByUsername(username)));
		}else if (CateringServiceCode.USERTYPE_GOV_CENTRAL.equals(userType)){
			//主管機關-中央
		}else{
			int countyId = AuthenUtil.getCountyNumByUsername(username);
			
			//county 目前最大值是 40花蓮縣,由於怕跑到555 666 之類額外定義的 COUNTY_ID,所以加上此判斷
			if(countyId <= CateringServiceCode.COUNTY_NUM && countyId != 0){
				//主管機關-地方
				viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata.COUNTY_ID, countyId));
			}
		}
		return viewMenuCriteria;
	}
	
	public Criteria setQueryAuth2(String username,String userType,Criteria viewMenuCriteria ){
		if (CateringServiceCode.USERTYPE_KITCHEN.equals(userType)){
			//005 團膳業者
			viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata2.KITCHEN_ID, AuthenUtil.getKitchenIddByUsername(username)));
		}else if (CateringServiceCode.USERTYPE_SCHOOL.equals(userType)){
			//006自設廚房
			viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata2.KITCHEN_ID, AuthenUtil.getKitchenIddByUsername(username)));
		}else if (CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)){
			//007受供餐學校
			viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata2.SCHOOL_ID, AuthenUtil.getSchoolIdByUsername(username)));
		}else if (CateringServiceCode.USERTYPE_GOV_CENTRAL.equals(userType)){
			//主管機關-中央
		}else{
			int countyId = AuthenUtil.getCountyNumByUsername(username);
			
			//county 目前最大值是 40花蓮縣,由於怕跑到555 666 之類額外定義的 COUNTY_ID,所以加上此判斷
			if(countyId <= CateringServiceCode.COUNTY_NUM && countyId != 0){
				//主管機關-地方
				viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata.COUNTY_ID, countyId));
			}
		}
		return viewMenuCriteria;
	}
	
	public Criteria setQueryCriteria(Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewSchoolMenuParameter vmParameter, Integer queryLimit, Criteria viewMenuCriteria ){
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(kitchenId))){
		viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata.KITCHEN_ID, kitchenId));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(schoolId))){
		viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata.SCHOOL_ID, schoolId));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(countyId))){
		viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata.COUNTY_ID, countyId));
		}
		viewMenuCriteria.add(Restrictions.between(ViewSchoolMenuWithBatchdata.MENU_DATE, begDate,
				endDate));
		//HashMap vmP = vmParameter.getMap();
		//判斷其餘like欄位
		
		HashMap<String, Object> pa =vmParameter.getMap();
		for (Entry<String ,Object> item : pa.entrySet()) {
			viewMenuCriteria.add(Restrictions.like(item.getKey(),
					"%"+ item.getValue()+"%"));
		}
		if(!"0".equals(Integer.toString(queryLimit))){
		viewMenuCriteria.setMaxResults(queryLimit);
		}
		//加入order by menudate,kitchenid,schoolid
		viewMenuCriteria.addOrder(Order.asc(ViewSchoolMenuWithBatchdata.MENU_DATE));
		viewMenuCriteria.addOrder(Order.asc(ViewSchoolMenuWithBatchdata.KITCHEN_ID));
		viewMenuCriteria.addOrder(Order.asc(ViewSchoolMenuWithBatchdata.SCHOOL_ID));
		return viewMenuCriteria;
	}
	
	public Criteria setQueryCriteria2(Integer kitchenId ,Integer restaurantId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewSchoolMenuParameter2 vmParameter, Integer queryLimit, Criteria viewMenuCriteria ){
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(kitchenId))){
		viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata2.KITCHEN_ID, Integer.toString(kitchenId)));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(restaurantId))){
		viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata2.RESTAURANT_ID, Integer.toString(restaurantId)));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(schoolId))){
		viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata2.SCHOOL_ID, schoolId));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(countyId))){
		viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata2.COUNTY_ID, countyId));
		}
		
		Date startDate1 = null;
		Date endDate1 = null;
		try {
			startDate1 = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate);
			endDate1 = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate);
			
			Calendar c = Calendar.getInstance(); 
			c.setTime(endDate1); 
			c.add(Calendar.HOUR, 23);
			c.add(Calendar.MINUTE, 59);
			c.add(Calendar.SECOND, 59);
			endDate1 = c.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(startDate1 == null || endDate1 == null ){
//			throw new ParseException;
		}
		
		viewMenuCriteria.add(Restrictions.between(ViewSchoolMenuWithBatchdata2.MENU_DATE, startDate1,
				endDate1));
		//HashMap vmP = vmParameter.getMap();
		//判斷其餘like欄位
		
		HashMap<String, Object> pa =vmParameter.getMap();
		for (Entry<String ,Object> item : pa.entrySet()) {
			viewMenuCriteria.add(Restrictions.like(item.getKey(),
					"%"+ item.getValue()+"%"));
		}
		if(!"0".equals(Integer.toString(queryLimit))){
		viewMenuCriteria.setMaxResults(queryLimit);
		}
		//加入order by menudate,kitchenid,schoolid
		viewMenuCriteria.addOrder(Order.asc(ViewSchoolMenuWithBatchdata2.MENU_DATE));
		viewMenuCriteria.addOrder(Order.asc(ViewSchoolMenuWithBatchdata2.KITCHEN_ID));
		viewMenuCriteria.addOrder(Order.asc(ViewSchoolMenuWithBatchdata2.SCHOOL_ID));
		return viewMenuCriteria;
	}
	
	/**
	 * 20140818 Ric
	 * 查詢菜單總數量
	 * @return int
	 */
	public int queryTotelMenuCount(String username ,String userType ,Integer kitchenId ,Integer restaurantId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewSchoolMenuParameter2 vmParameter, Integer queryLimit){
		// Query結果因內含null Object,故總量可能不正確,所以改為呼叫queryMenu method
		/*Criteria viewMenuCriteria = dbSession.createCriteria(ViewSchoolMenuWithBatchdata2.class);
		viewMenuCriteria.setProjection(Projections.rowCount());
		viewMenuCriteria= setQueryAuth2(username,userType, viewMenuCriteria);
		viewMenuCriteria= setQueryCriteria2(kitchenId,restaurantId,schoolId,countyId,begDate,endDate,vmParameter,queryLimit,viewMenuCriteria);
		return ((Number)viewMenuCriteria.uniqueResult()).intValue();*/
		List<ViewSchoolMenuWithBatchdata2> viewMenu = queryMenu(username, userType, kitchenId, restaurantId, schoolId, countyId, begDate, endDate, vmParameter, queryLimit);
		if (viewMenu != null && viewMenu.size() > 0) {
			return viewMenu.size();
		} else {
			return 0;
		}
	}
	public static ViewSchoolMenuWithBatchdataDAO2 getFromApplicationContext(ApplicationContext ctx) {
		return (ViewSchoolMenuWithBatchdataDAO2) ctx.getBean("IngredientbatchdataDAO");
	}

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}
}
