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
 * Home object for domain model class Schoolfoodstreet.
 * @see org.iii.ideas.catering_service.dao.Schoolfoodstreet
 * @author Hibernate Tools
 */
public class SchoolfoodstreetDAO {

	private static final Log log = LogFactory
			.getLog(SchoolfoodstreetDAO.class);

	private SessionFactory sessionFactory;
	private Session dbSession;

	public SchoolfoodstreetDAO() {

	}

	public SchoolfoodstreetDAO(Session dbSession) {
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

	public void persist(Schoolfoodstreet transientInstance) {
		log.debug("persisting Schoolfoodstreet instance");
		try {
			getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Schoolfoodstreet instance) {
		log.debug("attaching dirty Schoolfoodstreet instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Schoolfoodstreet instance) {
		log.debug("attaching clean Schoolfoodstreet instance");
		try {
			getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Schoolfoodstreet persistentInstance) {
		log.debug("deleting Schoolfoodstreet instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Schoolfoodstreet merge(Schoolfoodstreet detachedInstance) {
		log.debug("merging Schoolfoodstreet instance");
		try {
			Schoolfoodstreet result = (Schoolfoodstreet) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Schoolfoodstreet findById(java.lang.Integer id) {
		log.debug("getting Schoolfoodstreet instance with id: " + id);
		try {
			Schoolfoodstreet instance = (Schoolfoodstreet) getCurrentSession()
					.get("org.iii.ideas.catering_service.dao.Schoolfoodstreet",
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

	public List<Schoolfoodstreet> findByExample(Schoolfoodstreet instance) {
		log.debug("finding Schoolfoodstreet instance by example");
		try {
			List<Schoolfoodstreet> results = (List<Schoolfoodstreet>) getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.Schoolfoodstreet")
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
