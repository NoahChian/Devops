package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RoleDAO {
	private static final Logger log = LoggerFactory
			.getLogger(RoleDAO.class);
	
	private static final String ROLE_ID="roleId";
	private static final String ROLE_TYPE="roletype";
	private static final String ROLE_RULE="rolerule";
	private static final String CREATE_TIME="createTime";
	private static final String UPDATE_TIME="updateTime";
	private static final String TABLENAME="role";
	
	public RoleDAO(){}
	
	public RoleDAO(Session dbSession){
		setDbSession(dbSession);
	}
	
	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}	
	
	private SessionFactory sessionFactory;
	private Session dbSession;
	
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
	
	
	public void save(Role transientInstance) {
		log.debug("saving role instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Role persistentInstance) {
		log.debug("deleting role instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	

	public void attachDirty(Role instance) {
		log.debug("attaching dirty role instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Role instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Role as model where model."
					+ propertyName + "= ?";
			Query queryObject = getDbSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
}
