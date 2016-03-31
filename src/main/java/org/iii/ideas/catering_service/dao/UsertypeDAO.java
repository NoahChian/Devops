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
public class UsertypeDAO {
	private static final Logger log = LoggerFactory
			.getLogger(UsertypeDAO.class);
	// property constants
	public UsertypeDAO(){}

	public UsertypeDAO(Session dbSession) {
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

	public void save(Usertype transientInstance) {
		log.debug("saving Usertype instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Usertype persistentInstance) {
		log.debug("deleting Usertype instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}


	public List findByExample(Usertype instance) {
		log.debug("finding Usertype instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.Usertype")
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
		log.debug("finding Usertype instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Usertype as model where model."
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
		log.debug("finding all Usertype instances");
		try {
			String queryString = "from Usertype";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Usertype merge(Usertype detachedInstance) {
		log.debug("merging Usertype instance");
		try {
			Usertype result = (Usertype) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Usertype instance) {
		log.debug("attaching dirty Usertype instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Usertype instance) {
		log.debug("attaching clean Usertype instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static UsertypeDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (UsertypeDAO) ctx.getBean("UsertypeDAO");
	}
	
	public List<Usertype> queryAllUsertypeList(){
		String queryString = "FROM Usertype ";
	
		Query queryObject = this.dbSession.createQuery(queryString);
					return (List<Usertype>)queryObject.list();
	}
	
	public List<Usertype> queryAllUsertypeListforKitchenList(){
		String queryString = "FROM Usertype where (id like '00%' or id like '10%') and id <> '000' ";
	
		Query queryObject = this.dbSession.createQuery(queryString);
					return (List<Usertype>)queryObject.list();
	}
}