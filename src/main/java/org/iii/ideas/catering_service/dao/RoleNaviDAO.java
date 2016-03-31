package org.iii.ideas.catering_service.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RoleNaviDAO {
	private static final Logger log = LoggerFactory.getLogger(RoleNaviDAO.class);
	
	public RoleNaviDAO(){}
	
	public RoleNaviDAO(Session dbSession){
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
		log.debug("finding Rolenavi instance with property: "
				+ propertyName + ", value: " + values);
		try {
			String queryString = "select id.naviId from Rolenavi where id."
					+ propertyName + " in (:roleId) ";
			Query queryObject = getDbSession().createQuery(queryString);
			
			queryObject.setParameterList("roleId", values);
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}	
}
