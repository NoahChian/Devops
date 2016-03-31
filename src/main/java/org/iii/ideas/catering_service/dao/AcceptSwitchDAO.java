package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * AcceptSwitch entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.AcceptSwitch
 * @author MyEclipse Persistence Tools
 */
public class AcceptSwitchDAO {
	private static final Logger log = LoggerFactory.getLogger(AcceptSwitchDAO.class);
	// property constants
	public static final String SCHOOL_ID = "schoolId";
	public static final String ACCEPT_TYPE = "acceptType";
	public static final String STATUS = "status";
	public static final String CREATE_USER = "createUser";
	public static final String MODIFY_USER = "modifyUser";

	private SessionFactory sessionFactory;
	private Session dbSession;

	public AcceptSwitchDAO(){}
	
	public AcceptSwitchDAO(Session dbSession){
		this.dbSession = dbSession;
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
	
	public void save(AcceptSwitch transientInstance) {
		log.debug("saving AcceptSwitch instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(AcceptSwitch persistentInstance) {
		log.debug("deleting AcceptSwitch instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AcceptSwitch findById(java.lang.Integer id) {
		log.debug("getting AcceptSwitch instance with id: " + id);
		try {
			AcceptSwitch instance = (AcceptSwitch) getCurrentSession().get(
					"org.iii.ideas.catering_service.dao.AcceptSwitch", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(AcceptSwitch instance) {
		log.debug("finding AcceptSwitch instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.AcceptSwitch")
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
		log.debug("finding AcceptSwitch instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from AcceptSwitch as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySchoolId(Object schoolId) {
		return findByProperty(SCHOOL_ID, schoolId);
	}

	public List findByAcceptType(Object acceptType) {
		return findByProperty(ACCEPT_TYPE, acceptType);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByCreateUser(Object createUser) {
		return findByProperty(CREATE_USER, createUser);
	}

	public List findByModifyUser(Object modifyUser) {
		return findByProperty(MODIFY_USER, modifyUser);
	}

	public List findAll() {
		log.debug("finding all AcceptSwitch instances");
		try {
			String queryString = "from AcceptSwitch";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public AcceptSwitch merge(AcceptSwitch detachedInstance) {
		log.debug("merging AcceptSwitch instance");
		try {
			AcceptSwitch result = (AcceptSwitch) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(AcceptSwitch instance) {
		log.debug("attaching dirty AcceptSwitch instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AcceptSwitch instance) {
		log.debug("attaching clean AcceptSwitch instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}