package org.iii.ideas.catering_service.dao;

// Generated 2015/11/2 �U�� 06:06:42 by Hibernate Tools 3.4.0.CR1

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Userrelation.
 * @see org.iii.ideas.catering_service.dao.Userrelation
 * @author Hibernate Tools
 */
public class UserrelationDAO {

	private static final Log log = LogFactory.getLog(UserrelationDAO.class);

//	private final SessionFactory sessionFactory = getSessionFactory();

	private SessionFactory sessionFactory;
	private Session dbSession;

	public UserrelationDAO() {

	}

	public UserrelationDAO(Session dbSession) {
		this.dbSession = dbSession;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	protected SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	private Session getCurrentSession() {
		// return sessionFactory.getCurrentSession();
		return this.dbSession;
	}
	
	public void persist(Userrelation transientInstance) {
		log.debug("persisting Userrelation instance");
		try {
			getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Userrelation instance) {
		log.debug("attaching dirty Userrelation instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Userrelation instance) {
		log.debug("attaching clean Userrelation instance");
		try {
			getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Userrelation persistentInstance) {
		log.debug("deleting Userrelation instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Userrelation merge(Userrelation detachedInstance) {
		log.debug("merging Userrelation instance");
		try {
			Userrelation result = (Userrelation) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Userrelation findById(java.lang.Integer id) {
		log.debug("getting Userrelation instance with id: " + id);
		try {
			Userrelation instance = (Userrelation)getCurrentSession().get(
							"org.iii.ideas.catering_service.dao.Userrelation",
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

	public List<Userrelation> findByExample(Userrelation instance) {
		log.debug("finding Userrelation instance by example");
		try {
			List<Userrelation> results = (List<Userrelation>) getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.Userrelation")
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
