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
 * Home object for domain model class Restaurantdish.
 * @see org.iii.ideas.catering_service.dao.Restaurantdish
 * @author Hibernate Tools
 */
public class RestaurantdishDAO {

	private static final Log log = LogFactory.getLog(RestaurantdishDAO.class);

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

	public void persist(Restaurantdish transientInstance) {
		log.debug("persisting Restaurantdish instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Restaurantdish instance) {
		log.debug("attaching dirty Restaurantdish instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Restaurantdish instance) {
		log.debug("attaching clean Restaurantdish instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Restaurantdish persistentInstance) {
		log.debug("deleting Restaurantdish instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Restaurantdish merge(Restaurantdish detachedInstance) {
		log.debug("merging Restaurantdish instance");
		try {
			Restaurantdish result = (Restaurantdish) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Restaurantdish findById(java.lang.Integer id) {
		log.debug("getting Restaurantdish instance with id: " + id);
		try {
			Restaurantdish instance = (Restaurantdish) sessionFactory
					.getCurrentSession()
					.get("org.iii.ideas.catering_service.dao.Restaurantdish",
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

	public List<Restaurantdish> findByExample(Restaurantdish instance) {
		log.debug("finding Restaurantdish instance by example");
		try {
			List<Restaurantdish> results = (List<Restaurantdish>) sessionFactory
					.getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.Restaurantdish")
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
