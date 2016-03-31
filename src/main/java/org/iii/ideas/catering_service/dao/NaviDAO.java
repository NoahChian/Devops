package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class NaviDAO {
	private static final Logger log = LoggerFactory.getLogger(NaviDAO.class);
	
	public NaviDAO(){}
	
	public NaviDAO(Session dbSession){
		setDbSession(dbSession);
	}
	
	private SessionFactory sessionFactory;
	private Session dbSession;
	
	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}

	public void  openSessionFactory(){
		this.sessionFactory=HibernateUtil.buildSessionFactory();
		this.dbSession = sessionFactory.openSession();
	}	
	
	public void closeSession(){
		this.dbSession.close();
	}
		
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		if (this.dbSession==null){
			return sessionFactory.getCurrentSession();
		}else{
			return this.dbSession;
		}
	}
	
	public List findByProperty(String propertyName, List<Long> values) {
		log.debug("finding Navi instance with property: "
				+ propertyName + ", value: " + values);
		try {
			String queryString = "from Navi as model where model."
					+ propertyName + " in (:naviId) ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameterList("naviId", values);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}	
}
