package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * County entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.County
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class CountyDAO {
	private static final Logger log = LoggerFactory.getLogger(CountyDAO.class);
	// property constants
	public static final String COUNTY = "county";
	public static final String ENABLE = "enable";

	private SessionFactory sessionFactory;
	private Session dbSession;

	public CountyDAO() {
	}

	public CountyDAO(Session dbSession) {
		this.dbSession = dbSession;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public void save(County transientInstance) {
		log.debug("saving County instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(County persistentInstance) {
		log.debug("deleting County instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public County findById(java.lang.Integer id) {
		log.debug("getting County instance with id: " + id);
		try {
			County instance = (County) getCurrentSession().get("org.iii.ideas.catering_service.dao.County", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(County instance) {
		log.debug("finding County instance by example");
		try {
			List results = getCurrentSession().createCriteria("org.iii.ideas.catering_service.dao.County").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding County instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from County as model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCounty(Object county) {
		return findByProperty(COUNTY, county);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findAll() {
		log.debug("finding all County instances");
		try {
			String queryString = "from County";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public County merge(County detachedInstance) {
		log.debug("merging County instance");
		try {
			County result = (County) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(County instance) {
		log.debug("attaching dirty County instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(County instance) {
		log.debug("attaching clean County instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static CountyDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CountyDAO) ctx.getBean("CountyDAO");
	}

	public String getCountyNameById(Integer id) {
		County county = findById(id);
		if (county != null) {
			return county.getCounty();
		} else {
			return null;
		}
	}

	public County getCountyByName(String countyName) {
		String hql = "FROM County WHERE county = :county";
		Query query = dbSession.createQuery(hql);
		query.setParameter("county", countyName);
		if (query.list() != null && query.list().size() > 0) {
			County county = (County) query.list().get(0);
			return county;
		} else {
			return null;
		}
	}
	
	public List queryAllCountyAndCode(){
		String queryString = "select ct.countyId,ct.county,c.code from County ct, Code c where ct.county = c.name ";
		Query queryObject = this.dbSession.createQuery(queryString);
		return queryObject.list();
	}
}