package org.iii.ideas.catering_service.dao;

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
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ViewSchoolMenuWithBatchdataDAO {
	private static final Logger log = LoggerFactory.getLogger(ViewSchoolMenuWithBatchdataDAO.class);
	// property constants
	
	public ViewSchoolMenuWithBatchdataDAO(){
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
	public ViewSchoolMenuWithBatchdataDAO(Session dbSession){
		setSession(dbSession);
	}
	private Session dbSession;
	private SessionFactory sessionFactory;
	/* 依照條件去找出所的結果-通常為Excel所用
	 * 先去依照帳號權限設定query條件
	 * 再依照輸入的參數去設定query條件
	 */
	public List<ViewSchoolMenuWithBatchdata> queryMenu(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewSchoolMenuParameter vmParameter, Integer queryLimit) {
		Criteria viewMenuCriteria = dbSession.createCriteria(ViewSchoolMenuWithBatchdata.class);
		viewMenuCriteria= setQueryAuth(username,userType, viewMenuCriteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewMenuCriteria= setQueryCriteria(kitchenId,schoolId, countyId,begDate,endDate,vmParameter,queryLimit,viewMenuCriteria);
		List<ViewSchoolMenuWithBatchdata> viewMenu = (List<ViewSchoolMenuWithBatchdata>) viewMenuCriteria.list();
		return viewMenu;
	}
	/* 依照條件去找出所的結果-通常為API要分頁時所用
	 * 先去依照帳號權限設定query條件
	 * 再依照輸入的參數去設定query條件
	 * 最後設定要query的頁數
	 */
	public List<ViewSchoolMenuWithBatchdata> queryMenuByPage(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewSchoolMenuParameter vmParameter, Integer queryLimit, int page, int perPage) {
		Criteria viewMenuCriteria = dbSession.createCriteria(ViewSchoolMenuWithBatchdata.class);
		viewMenuCriteria= setQueryAuth(username,userType, viewMenuCriteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewMenuCriteria= setQueryCriteria(kitchenId,schoolId, countyId,begDate,endDate,vmParameter,queryLimit,viewMenuCriteria);
		//處理分頁
		if(page != 0 && perPage !=0){
			int startIndex = (page-1) * perPage;
			viewMenuCriteria.setFirstResult(startIndex);
			viewMenuCriteria.setMaxResults(perPage);
		}
		List<ViewSchoolMenuWithBatchdata> viewMenu = (List<ViewSchoolMenuWithBatchdata>) viewMenuCriteria.list();
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
			//主管機關-地方
			viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata.COUNTY_ID, AuthenUtil.getCountyNumByUsername(username)));
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
	/**
	 * 20140818 Ric
	 * 查詢菜單總數量
	 * @return int
	 */
	public int queryTotelMenuCount(String username ,String userType ,Integer kitchenId ,Integer schoolId, Integer countyId, 
			String begDate, String endDate, ViewSchoolMenuParameter vmParameter, Integer queryLimit){
		Criteria viewMenuCriteria = dbSession.createCriteria(ViewSchoolMenuWithBatchdata.class);
		viewMenuCriteria.setProjection(Projections.rowCount());
		viewMenuCriteria= setQueryAuth(username,userType, viewMenuCriteria);
		viewMenuCriteria= setQueryCriteria(kitchenId,schoolId, countyId,begDate,endDate,vmParameter,queryLimit,viewMenuCriteria);
		return ((Number)viewMenuCriteria.uniqueResult()).intValue();
	}
	public static ViewSchoolMenuWithBatchdataDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ViewSchoolMenuWithBatchdataDAO) ctx.getBean("IngredientbatchdataDAO");
	}

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}
}
