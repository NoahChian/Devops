package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class UserroleDAO {
	private static final Logger log = LoggerFactory
			.getLogger(UserroleDAO.class);
	// property constants
	public UserroleDAO(){}

	public UserroleDAO(Session dbSession) {
		setDbSession(dbSession);
	}
	
	private Session dbSession;
	
	

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	protected void initDao() {
		// do nothing
	}

	public void save(Userrole transientInstance) {
		log.debug("saving Userrole instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Userrole persistentInstance) {
		log.debug("deleting Userrole instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Userrole findById(
			org.iii.ideas.catering_service.dao.UserroleId id) {
		log.debug("getting Userrole instance with id: " + id);
		try {
			Userrole instance = (Userrole) getCurrentSession().get(
					"org.iii.ideas.catering_service.dao.Userrole", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Userrole instance) {
		log.debug("finding Userrole instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.Userrole")
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
		log.debug("finding Userrole instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Userrole as model where model."
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
		log.debug("finding all Userrole instances");
		try {
			String queryString = "from Userrole";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Userrole merge(Userrole detachedInstance) {
		log.debug("merging Userrole instance");
		try {
			Userrole result = (Userrole) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Userrole instance) {
		log.debug("attaching dirty Userrole instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Userrole instance) {
		log.debug("attaching clean Userrole instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static UserroleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (UserroleDAO) ctx.getBean("UserroleDAO");
	}
}