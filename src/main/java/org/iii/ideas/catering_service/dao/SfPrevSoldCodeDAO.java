package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * Home object for domain model class SfPrevSoldCode.
 * @see org.iii.ideas.catering_service.dao.SfPrevSoldCode
 * @author Hibernate Tools
 */
@Transactional
public class SfPrevSoldCodeDAO {
	private static final Logger log = LoggerFactory.getLogger(SfPrevSoldCodeDAO.class);
	// property constants
	public static final String ID = "codeId";
	public static final String SF_PREVSOLDCODE = "code";

	private SessionFactory sessionFactory;
	private Session dbSession;
	
	public SfPrevSoldCodeDAO(){}
	
	public SfPrevSoldCodeDAO(Session dbSession){
		this.dbSession = dbSession;
	}

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

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void save(SfPrevSoldCode transientInstance) {
		log.debug("saving Sf_Prevsoldcode instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SfPrevSoldCode persistentInstance) {
		log.debug("deleting Sf_Prevsoldcode instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SfPrevSoldCode findById(int id) {
		log.debug("getting Sf_Prevsoldcode instance with id: " + id);
		try {
			SfPrevSoldCode instance = (SfPrevSoldCode) getCurrentSession().get(
					"org.iii.ideas.catering_service.dao.SfPrevSoldCode", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SfPrevSoldCode instance) {
		log.debug("finding Sf_Prevsoldcode instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("org.iii.ideas.catering_service.dao.SfPrevSoldCode")
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
		log.debug("finding Sf_Prevsoldcode instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from sf_prevsoldcode as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCountyId(Object sfCodeId) {
		return findByProperty(ID, sfCodeId);
	}

	public List findBySf_Prevsoldcode(Object sfPrevsoldcode) {
		return findByProperty(SF_PREVSOLDCODE, sfPrevsoldcode);
	}

	public List findAll() {
		log.debug("finding all Sf_Prevsoldcode instances");
		try {
			String queryString = "from sf_prevsoldcode";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SfPrevSoldCode merge(SfPrevSoldCode detachedInstance) {
		log.debug("merging Sf_Prevsoldcode instance");
		try {
			SfPrevSoldCode result = (SfPrevSoldCode) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SfPrevSoldCode instance) {
		log.debug("attaching dirty Sf_Prevsoldcode instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SfPrevSoldCode instance) {
		log.debug("attaching clean Sf_Prevsoldcode instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SfPrevSoldCodeDAO getFromApplicationContext(ApplicationContext ctx) {
		return (SfPrevSoldCodeDAO) ctx.getBean("SfPrevSoldCodeDAO");
	}

	public SfPrevSoldCode getSfPrevSoldCodeByCode(String code){
		String hql = "FROM sf_prevsoldcode WHERE code = :code ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("code", code);

		if(query.list()!=null && query.list().size()>0){
			return (SfPrevSoldCode)query.list().get(0);
		}else{
			return null;
		}
	}
}