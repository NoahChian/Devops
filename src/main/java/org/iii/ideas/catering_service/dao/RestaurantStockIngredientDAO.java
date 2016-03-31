package org.iii.ideas.catering_service.dao;

// Generated 2015/11/2 下午 06:06:42 by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class RestaurantStockIngredient.
 * @see org.iii.ideas.catering_service.dao.RestaurantStockIngredient
 * @author Hibernate Tools
 */
public class RestaurantStockIngredientDAO {

	private static final Log log = LogFactory
			.getLog(RestaurantStockIngredientDAO.class);

	private final SessionFactory sessionFactory = getSessionFactory();

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

	public void persist(RestaurantStockIngredient transientInstance) {
		log.debug("persisting RestaurantStockIngredient instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(RestaurantStockIngredient instance) {
		log.debug("attaching dirty RestaurantStockIngredient instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RestaurantStockIngredient instance) {
		log.debug("attaching clean RestaurantStockIngredient instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(RestaurantStockIngredient persistentInstance) {
		log.debug("deleting RestaurantStockIngredient instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RestaurantStockIngredient merge(
			RestaurantStockIngredient detachedInstance) {
		log.debug("merging RestaurantStockIngredient instance");
		try {
			RestaurantStockIngredient result = (RestaurantStockIngredient) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public RestaurantStockIngredient findById(java.lang.Integer id) {
		log.debug("getting RestaurantStockIngredient instance with id: " + id);
		try {
			RestaurantStockIngredient instance = (RestaurantStockIngredient) sessionFactory
					.getCurrentSession()
					.get("org.iii.ideas.catering_service.dao.RestaurantStockIngredient",
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

	public List<RestaurantStockIngredient> findByExample(
			RestaurantStockIngredient instance) {
		log.debug("finding RestaurantStockIngredient instance by example");
		try {
			List<RestaurantStockIngredient> results = (List<RestaurantStockIngredient>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.RestaurantStockIngredient")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
