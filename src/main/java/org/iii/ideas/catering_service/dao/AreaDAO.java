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
 * A data access object (DAO) providing persistence and search support for Area
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Area
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class AreaDAO {
	private static final Logger log = LoggerFactory.getLogger(AreaDAO.class);
	// property constants
	public static final String COUNTY_ID = "countyId";
	public static final String AREA = "area";

	private SessionFactory sessionFactory;
	private Session dbSession;
	
	public AreaDAO(){}
	
	public AreaDAO(Session dbSession){
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

	public void save(Area transientInstance) {
		log.debug("saving Area instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Area persistentInstance) {
		log.debug("deleting Area instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Area findById(java.lang.Integer id) {
		log.debug("getting Area instance with id: " + id);
		try {
			Area instance = (Area) getCurrentSession().get(
					"org.iii.ideas.catering_service.dao.Area", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Area instance) {
		log.debug("finding Area instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("org.iii.ideas.catering_service.dao.Area")
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
		log.debug("finding Area instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Area as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCountyId(Object countyId) {
		return findByProperty(COUNTY_ID, countyId);
	}

	public List findByArea(Object area) {
		return findByProperty(AREA, area);
	}

	public List findAll() {
		log.debug("finding all Area instances");
		try {
			String queryString = "from Area";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Area merge(Area detachedInstance) {
		log.debug("merging Area instance");
		try {
			Area result = (Area) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Area instance) {
		log.debug("attaching dirty Area instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Area instance) {
		log.debug("attaching clean Area instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static AreaDAO getFromApplicationContext(ApplicationContext ctx) {
		return (AreaDAO) ctx.getBean("AreaDAO");
	}
	
	public String getAreaNameById(Integer id){
		String hql = "FROM Area WHERE areaId = :areaId";
		Query query = dbSession.createQuery(hql);
		query.setParameter("areaId", id);
		List result = query.list();
		if(result.size() != 0){
			Area area = (Area)result.get(0);
			if(area!=null){
				return area.getArea();
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public Area getAreaByName(String areaName,Integer countyId){
		String hql = "FROM Area WHERE area = :area AND countyId = :countyId ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("area", areaName);
		query.setParameter("countyId", countyId);
		if(query.list()!=null && query.list().size()>0){
			return (Area)query.list().get(0);
		}else{
			return null;
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}