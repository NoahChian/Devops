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
 * Schoolkitchen entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Schoolkitchen
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class SchoolkitchenDAO {
	private static final Logger log = LoggerFactory.getLogger(SchoolkitchenDAO.class);
	// property constants

	private SessionFactory sessionFactory;
	private Session dbSession;

	public SchoolkitchenDAO() {

	}

	public SchoolkitchenDAO(Session dbSession) {
		this.dbSession = dbSession;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setSession(Session session) {
		this.dbSession = session;
	}

	private Session getCurrentSession() {
		if (this.dbSession == null) {
			return sessionFactory.getCurrentSession();
		} else {
			return this.dbSession;
		}
	}

	protected void initDao() {
		// do nothing
	}

	public void save(Schoolkitchen transientInstance) {
		log.debug("saving Schoolkitchen instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Schoolkitchen persistentInstance) {
		log.debug("deleting Schoolkitchen instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Schoolkitchen findById(org.iii.ideas.catering_service.dao.SchoolkitchenId id) {
		log.debug("getting Schoolkitchen instance with id: " + id);
		try {
			Schoolkitchen instance = (Schoolkitchen) getCurrentSession().get("org.iii.ideas.catering_service.dao.Schoolkitchen", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Schoolkitchen instance) {
		log.debug("finding Schoolkitchen instance by example");
		try {
			List results = getCurrentSession().createCriteria("org.iii.ideas.catering_service.dao.Schoolkitchen").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Schoolkitchen instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from Schoolkitchen as model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all Schoolkitchen instances");
		try {
			String queryString = "from Schoolkitchen";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Schoolkitchen merge(Schoolkitchen detachedInstance) {
		log.debug("merging Schoolkitchen instance");
		try {
			Schoolkitchen result = (Schoolkitchen) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Schoolkitchen instance) {
		log.debug("attaching dirty Schoolkitchen instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Schoolkitchen instance) {
		log.debug("attaching clean Schoolkitchen instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SchoolkitchenDAO getFromApplicationContext(ApplicationContext ctx) {
		return (SchoolkitchenDAO) ctx.getBean("SchoolkitchenDAO");
	}

	public Schoolkitchen querySchoolkitchenById(Integer schoolId, Integer kitchenId) {
		String hql = "FROM Schoolkitchen sk WHERE sk.id.schoolId = :schoolId AND sk.id.kitchenId = :kitchenId";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		query.setParameter("schoolId", schoolId);
		if (query.list() != null && query.list().size() > 0) {
			return (Schoolkitchen) query.list().get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Schoolkitchen> querySchoolKitchenBySchoolIdList(List<Integer> schoolIdList,Integer kitchenId){
		String hql = "FROM Schoolkitchen sk WHERE sk.id.schoolId in (:schoolIds) AND sk.id.kitchenId = :kitchenId";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		query.setParameterList("schoolIds", schoolIdList);
		return query.list();
	}
	/**
	 * 以KitchenId查詢供餐對象學校list (輸出schoolId) 用於食材匯出
	 * @param kitchenId
	 * @return
	 */
	public List<Integer> querySchoolListByKitchen(Integer kitchenId){
		String hql = "SELECT sk.id.schoolId FROM Schoolkitchen sk WHERE sk.id.kitchenId = :kitchenId";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		return query.list();
	}
	
	public List<Integer> queryKitchenListBySchool(Integer schoolId){
		String hql = "SELECT sk.id.kitchenId FROM Schoolkitchen sk WHERE sk.id.schoolId = :schoolId";
		Query query = dbSession.createQuery(hql);
		query.setParameter("schoolId", schoolId);
		return query.list();
	}
	
	public List<String[]> querySchoolListByKitchenId(Integer kitchenId){
		String hql = "SELECT sk.id.schoolId,s.schoolName FROM Schoolkitchen sk , School s WHERE sk.id.schoolId = s.schoolId and sk.id.kitchenId = :kitchenId and sk.offered = 1 ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		return query.list();
	}
	/*由schoolId查kitchenId*/
	/*20151110 Chu*/
	
}
