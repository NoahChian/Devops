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
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * NegIngredient entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.NegIngredient
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class NegIngredientDAO {
	private static final Logger log = LoggerFactory
			.getLogger(NegIngredientDAO.class);
	// property constants
	public static final String SUPPLIER_ID = "supplierId";
	public static final String LOT_NUMBER = "lotNumber";
	public static final String DESCRIPTION = "description";

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

	public void save(NegIngredient transientInstance) {
		log.debug("saving NegIngredient instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(NegIngredient persistentInstance) {
		log.debug("deleting NegIngredient instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public NegIngredient findById(
			org.iii.ideas.catering_service.dao.NegIngredientId id) {
		log.debug("getting NegIngredient instance with id: " + id);
		try {
			NegIngredient instance = (NegIngredient) getCurrentSession().get(
					"org.iii.ideas.catering_service.dao.NegIngredient", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(NegIngredient instance) {
		log.debug("finding NegIngredient instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.NegIngredient")
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
		log.debug("finding NegIngredient instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from NegIngredient as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySupplierId(Object supplierId) {
		return findByProperty(SUPPLIER_ID, supplierId);
	}

	public List findByLotNumber(Object lotNumber) {
		return findByProperty(LOT_NUMBER, lotNumber);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findAll() {
		log.debug("finding all NegIngredient instances");
		try {
			String queryString = "from NegIngredient";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public NegIngredient merge(NegIngredient detachedInstance) {
		log.debug("merging NegIngredient instance");
		try {
			NegIngredient result = (NegIngredient) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(NegIngredient instance) {
		log.debug("attaching dirty NegIngredient instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(NegIngredient instance) {
		log.debug("attaching clean NegIngredient instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static NegIngredientDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (NegIngredientDAO) ctx.getBean("NegIngredientDAO");
	}
}