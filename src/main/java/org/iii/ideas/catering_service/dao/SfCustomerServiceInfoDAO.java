package org.iii.ideas.catering_service.dao;

// Generated 2015/2/3 上午 09:26:02 by Hibernate Tools 3.4.0.CR1

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.iii.ideas.catering_service.util.HibernateUtil;

/**
 * Home object for domain model class SfCustomerServiceInfo.
 * @see org.iii.ideas.catering_service.dao.SfCustomerServiceInfo
 * @author Hibernate Tools
 */
public class SfCustomerServiceInfoDAO {

	private static final Log log = LogFactory.getLog(SfCustomerServiceInfoDAO.class);

	private Session dbSession;
	private SessionFactory sessionFactory = getSessionFactory();

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}
	
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
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

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(SfCustomerServiceInfo transientInstance) {
		log.debug("persisting SfCustomerServiceInfo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SfCustomerServiceInfo instance) {
		log.debug("attaching dirty SfCustomerServiceInfo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SfCustomerServiceInfo instance) {
		log.debug("attaching clean SfCustomerServiceInfo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SfCustomerServiceInfo persistentInstance) {
		log.debug("deleting SfCustomerServiceInfo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SfCustomerServiceInfo merge(SfCustomerServiceInfo detachedInstance) {
		log.debug("merging SfCustomerServiceInfo instance");
		try {
			SfCustomerServiceInfo result = (SfCustomerServiceInfo) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SfCustomerServiceInfo findById(java.lang.Long id) {
		log.debug("getting SfCustomerServiceInfo instance with id: " + id);
		try {
			SfCustomerServiceInfo instance = (SfCustomerServiceInfo) sessionFactory
					.getCurrentSession()
					.get("org.iii.ideas.catering_service.dao.SfCustomerServiceInfo",
							id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SfCustomerServiceInfo instance) {
		log.debug("finding SfCustomerServiceInfo instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.SfCustomerServiceInfo")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding SfCustomerServiceInfo instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from sf_customerserviceinfo as model where model."
					+ propertyName + "= ?";
		
			Query queryObject = getDbSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all SfCustomerServiceInfo instances");
		try {
			String queryString = "from sf_customerserviceinfo";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}
