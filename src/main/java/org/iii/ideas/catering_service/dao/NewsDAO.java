package org.iii.ideas.catering_service.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter2;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * News entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.News
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class NewsDAO {
	private static final Logger log = LoggerFactory.getLogger(NewsDAO.class);
	private SessionFactory sessionFactory;
	private Session dbSession;

	public NewsDAO() {
	};

	public NewsDAO(Session dbSession) {
		this.dbSession = dbSession;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		if (this.dbSession == null) {
			return sessionFactory.getCurrentSession();
		} else {
			return this.dbSession;
		}
	}

	public void setSession(Session session) {
		this.dbSession = session;
	}

	protected void initDao() {
		// do nothing
	}
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding News instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from News as model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
//	public List findByNewsId(Object newsId) {
//		return findByProperty(News.NEWS_ID, newsId);
//	}
	
public News queryNewsByNewsId(Integer newsId) {
	String hql = "FROM News s WHERE s.newsId = :newsId";
	Query query = dbSession.createQuery(hql);
	query.setParameter("newsId", newsId);
	if (query.list() != null && query.list().size() > 0) {
		return (News) query.list().get(0);
	} else {
		return null;
	}
}

//	public List findBySchoolId(Object schoolId) {
//		return findByProperty(News.SCHOOL_ID, schoolId);
//	}
	
//	public News querySchoolByUsername(String username) {
//		String hql = "FROM News s WHERE s.username = :username";
//		Query query = dbSession.createQuery(hql);
//		query.setParameter("username", username);
//		if (query.list() != null && query.list().size() > 0) {
//			return (News) query.list().get(0);
//		} else {
//			return null;
//		}
//	}

	public static NewsDAO getFromApplicationContext(ApplicationContext ctx) {
		return (NewsDAO) ctx.getBean("NewsDAO");
	}
	
	public List executeQuery(String strSql) {

		List results = null;
		results = getCurrentSession().createSQLQuery(strSql).list();

		return results;

	}

	/*
	 * public void executeSql(String strSql) { log.debug(strSql); try {
	 * 
	 * getCurrentSession().createSQLQuery(strSql).executeUpdate();
	 * log.debug("executeSql successful"); } catch (RuntimeException re) {
	 * log.error("executeSql failed", re); throw re; }
	 * 
	 * }
	 */
	public void executeSql(Session session, String strSql) {
		log.debug(strSql);
		try {

			session.createSQLQuery(strSql).executeUpdate();
			log.debug("executeSql successful");
		} catch (RuntimeException re) {
			log.error("executeSql failed", re);
			throw re;
		}

	}
	public int queryTotelMenuCount(String username ,String userType ,Integer newsId ,String newsTitle ,Integer category, String queryType, 
			String startDate, String endDate, ViewSchoolMenuParameter2 vmParameter, Integer queryLimit){
		Criteria viewMenuCriteria = dbSession.createCriteria(News.class);
		viewMenuCriteria.setProjection(Projections.rowCount());
		viewMenuCriteria= setQueryAuth(username,userType, viewMenuCriteria);
		viewMenuCriteria= setQueryCriteria(newsId,newsTitle,category,queryType,startDate,endDate,vmParameter,queryLimit,viewMenuCriteria);
		return ((Number)viewMenuCriteria.uniqueResult()).intValue();
	}
	
	/* 依照條件去找出所的結果-通常為API要分頁時所用
	 * 先去依照帳號權限設定query條件
	 * 再依照輸入的參數去設定query條件
	 * 最後設定要query的頁數
	 */
	public List<News> queryMenuByPage(String username ,String userType ,Integer newsId ,String newsTitle ,Integer category, String queryType, 
			String startDate, String endDate, ViewSchoolMenuParameter2 vmParameter, Integer queryLimit, int page, int perPage) {
		Criteria viewMenuCriteria = dbSession.createCriteria(News.class);
		viewMenuCriteria= setQueryAuth(username,userType, viewMenuCriteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		viewMenuCriteria= setQueryCriteria(newsId,newsTitle,category,queryType,startDate,endDate,vmParameter,queryLimit,viewMenuCriteria);
		//處理分頁
		if(page != 0 && perPage !=0){
			int startIndex = (page-1) * perPage;
			viewMenuCriteria.setFirstResult(startIndex);
			viewMenuCriteria.setMaxResults(perPage);
		}
		List<News> viewMenu = (List<News>) viewMenuCriteria.list();
		return viewMenu;
	}
	
	public List<News> queryMenu(String username ,String userType ,Integer newsId ,String newsTitle ,Integer category, String queryType, 
			String startDate, String endDate, ViewSchoolMenuParameter2 vmParameter, Integer queryLimit) {
		Criteria newsCriteria = dbSession.createCriteria(News.class);
		newsCriteria= setQueryAuth(username,userType, newsCriteria);
		//201408018 把Criteria都丟出去，為了做分頁共用criteria條件
		newsCriteria= setQueryCriteria(newsId ,newsTitle , category, queryType, 
				startDate, endDate, vmParameter, queryLimit, newsCriteria);
		List<News> viewMenu = (List<News>) newsCriteria.list();
		return viewMenu;
	}
	
	public Criteria setQueryAuth(String username,String userType,Criteria viewMenuCriteria ){
//		if (CateringServiceCode.USERTYPE_KITCHEN.equals(userType)){
//			//005 團膳業者
//			viewMenuCriteria.add(Restrictions.eq(News.KITCHEN_ID, AuthenUtil.getKitchenIddByUsername(username)));
//		}else if (CateringServiceCode.USERTYPE_SCHOOL.equals(userType)){
//			//006自設廚房
//			viewMenuCriteria.add(Restrictions.eq(News.KITCHEN_ID, AuthenUtil.getKitchenIddByUsername(username)));
//		}else if (CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)){
//			//007受供餐學校
//			viewMenuCriteria.add(Restrictions.eq(News.SCHOOL_ID, AuthenUtil.getSchoolIdByUsername(username)));
//		}else if (CateringServiceCode.USERTYPE_GOV_CENTRAL.equals(userType)){
//			//主管機關-中央
//		}else{
//			int countyId = AuthenUtil.getCountyNumByUsername(username);
//			
//			//county 目前最大值是 40花蓮縣,由於怕跑到555 666 之類額外定義的 COUNTY_ID,所以加上此判斷
//			if(countyId <= CateringServiceCode.COUNTY_NUM){
//				//主管機關-地方
//				viewMenuCriteria.add(Restrictions.eq(ViewSchoolMenuWithBatchdata.COUNTY_ID, countyId));
//			}
//		}
		
		//20151123 和Cori討論後 管理者可刪改查其他人發的公告
//		viewMenuCriteria.add(Restrictions.eq(News.PUBLISH_USER, username));
		return viewMenuCriteria;
	}

	public Criteria setQueryCriteria(Integer newsId ,String newsTitle ,Integer category, String queryType, 
			String startDate, String endDate, ViewSchoolMenuParameter2 vmParameter, Integer queryLimit, Criteria newsCriteria ){
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(newsId))){
		newsCriteria.add(Restrictions.eq(News.NEWS_ID, newsId));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(newsTitle)){
		newsCriteria.add(Restrictions.like(News.NEWS_TITLE, newsTitle, MatchMode.ANYWHERE));
		}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(category))){
		newsCriteria.add(Restrictions.eq(News.CATEGORY, category));
		}
		
		try {
			if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(queryType)){
				if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(startDate) &&
						!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(endDate)){
					
					Date startDate1 = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", startDate);
					Date endDate1 = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate);
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
					Calendar c = Calendar.getInstance(); 
					c.setTime(endDate1); 
					c.add(Calendar.HOUR, 23);
					c.add(Calendar.MINUTE, 59);
					c.add(Calendar.SECOND, 59);
					endDate1 = c.getTime();
					
					if("startEndDate".equals(queryType)){
						newsCriteria.add(
								Restrictions.or(
								Restrictions.or(Restrictions.between(News.START_DATE, startDate1, endDate1), 
										Restrictions.between(News.END_DATE, startDate1, endDate1)),
								Restrictions.and(Restrictions.le(News.START_DATE, startDate1),Restrictions.ge(News.END_DATE,endDate1))
						));
					} else if("publishDate".equals(queryType)){
						newsCriteria.add(
								Restrictions.and(Restrictions.ge(News.PUBLISH_DATE, startDate1), 
										Restrictions.lt(News.PUBLISH_DATE, endDate1)
						));
					}

				} 
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		//HashMap vmP = vmParameter.getMap();
		//判斷其餘like欄位
		
		HashMap<String, Object> pa =vmParameter.getMap();
		for (Entry<String ,Object> item : pa.entrySet()) {
			newsCriteria.add(Restrictions.like(item.getKey(),
					"%"+ item.getValue()+"%"));
		}
		if(!"0".equals(Integer.toString(queryLimit))){
		newsCriteria.setMaxResults(queryLimit);
		}
		//加入order by menudate,kitchenid,schoolid
		newsCriteria.addOrder(Order.desc(News.NEWS_ID));
		return newsCriteria;
	}
	
	public void save(Session session, News transientInstance) {
		log.debug("saving News instance");
		try {
			session.save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void save(News transientInstance) {
		log.debug("saving News instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/*
	 * public void delete(News persistentInstance) {
	 * log.debug("deleting News instance"); try {
	 * getCurrentSession().delete(persistentInstance);
	 * log.debug("delete successful"); } catch (RuntimeException re) {
	 * log.error("delete failed", re); throw re; } }
	 */
	public void delete(Session session, News persistentInstance) {
		log.debug("deleting News instance");
		try {
			session.delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
}