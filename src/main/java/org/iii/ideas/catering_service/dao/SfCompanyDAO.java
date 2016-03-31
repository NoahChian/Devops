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
 * Home object for domain model class SfCompany.
 * @see org.iii.ideas.catering_service.dao.SfCompany
 * @author Hibernate Tools
 */
@Transactional
public class SfCompanyDAO {
	private static final Logger log = LoggerFactory.getLogger(SfCompanyDAO.class);
	// property constants
	public static final String ID = "countyId";
	public static final String SF_COMPANY = "sf_company";

	private SessionFactory sessionFactory;
	private Session dbSession;
	
	public SfCompanyDAO(){}
	
	public SfCompanyDAO(Session dbSession){
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

	public void save(SfCompany transientInstance) {
		log.debug("saving Sf_Company instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SfCompany persistentInstance) {
		log.debug("deleting Sf_Company instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SfCompany findById(java.lang.Long id) {
		log.debug("getting Sf_Company instance with id: " + id);
		try {
			SfCompany instance = (SfCompany) getCurrentSession().get(
					"org.iii.ideas.catering_service.dao.Sf_Company", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SfCompany instance) {
		log.debug("finding Sf_Company instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("org.iii.ideas.catering_service.dao.Sf_Company")
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
		log.debug("finding Sf_Company instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Sf_Company as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCountyId(Object sfCompanyId) {
		return findByProperty(ID, sfCompanyId);
	}

	public List findBySf_Company(Object sfCompany) {
		return findByProperty(SF_COMPANY, sfCompany);
	}

	public List findAll() {
		log.debug("finding all Sf_Company instances");
		try {
			String queryString = "from Sf_Company";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SfCompany merge(SfCompany detachedInstance) {
		log.debug("merging Sf_Company instance");
		try {
			SfCompany result = (SfCompany) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SfCompany instance) {
		log.debug("attaching dirty Sf_Company instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SfCompany instance) {
		log.debug("attaching clean Sf_Company instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SfCompanyDAO getFromApplicationContext(ApplicationContext ctx) {
		return (SfCompanyDAO) ctx.getBean("Sf_CompanyDAO");
	}
	
	public String getSfCompanyNameById(Long id){
		String hql = "FROM Sf_Company WHERE id = :id";
		Query query = dbSession.createQuery(hql);
		query.setParameter("id", id);
		if(query.list()!=null){
			SfCompany sfCompany = (SfCompany)query.list().get(0);
			if(sfCompany!=null){
				return sfCompany.getName();
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public SfCompany getSfCompanyByName(String name,Long sfCompanyId){
		String hql = "FROM Sf_Company WHERE name = :name AND id = :sfCompanyId ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("name", name);
		query.setParameter("sfCompanyId", sfCompanyId);
		if(query.list()!=null && query.list().size()>0){
			return (SfCompany)query.list().get(0);
		}else{
			return null;
		}
	}
}