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
 * Home object for domain model class SfCategory.
 * @see org.iii.ideas.catering_service.dao.SfCategory
 * @author Hibernate Tools
 */
public class SfCategoryDAO {

	private static final Log log = LogFactory.getLog(SfCategoryDAO.class);

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

	public void persist(SfCategory transientInstance) {
		log.debug("persisting SfCategory instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SfCategory instance) {
		log.debug("attaching dirty SfCategory instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SfCategory instance) {
		log.debug("attaching clean SfCategory instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SfCategory persistentInstance) {
		log.debug("deleting SfCategory instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SfCategory merge(SfCategory detachedInstance) {
		log.debug("merging SfCategory instance");
		try {
			SfCategory result = (SfCategory) sessionFactory.getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SfCategory findById(long id) {
		log.debug("getting SfCategory instance with id: " + id);
		try {
			SfCategory instance = (SfCategory) sessionFactory
					.getCurrentSession()
					.get("org.iii.ideas.catering_service.dao.SfCategory", id);
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

	public List findByExample(SfCategory instance) {
		log.debug("finding SfCategory instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.SfCategory")
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
		log.debug("finding SfCategory instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from sf_category as model where model."
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
		log.debug("finding all SfCategory instances");
		try {
			String queryString = "from sf_category";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}
