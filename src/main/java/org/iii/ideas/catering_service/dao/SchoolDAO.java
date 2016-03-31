package org.iii.ideas.catering_service.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * School entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.School
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class SchoolDAO {
	private static final Logger log = LoggerFactory.getLogger(SchoolDAO.class);
	// property constants
	public static final String SCHOOL_NAME = "schoolName";
	public static final String COUNTY_ID = "countyId";
	public static final String AREA_ID = "areaId";
	public static final String ENABLE = "enable";

	private SessionFactory sessionFactory;
	private Session dbSession;

	public SchoolDAO() {
	};

	public SchoolDAO(Session dbSession) {
		this.dbSession = dbSession;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		if (this.dbSession == null) {
			return sessionFactory.getCurrentSession();
		} else {
			return this.dbSession;
		}
	}

	public void setSession(Session session) {
		this.dbSession = session;
	}

	protected void initDao() {
		// do nothing
	}

	public void save(School transientInstance) {
		log.debug("saving School instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(School persistentInstance) {
		log.debug("deleting School instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public School findById(java.lang.Integer id) {
		log.debug("getting School instance with id: " + id);
		try {
			School instance = (School) getCurrentSession().get("org.iii.ideas.catering_service.dao.School", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(School instance) {
		log.debug("finding School instance by example");
		try {
			List results = getCurrentSession().createCriteria("org.iii.ideas.catering_service.dao.School").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding School instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from School as model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySchoolName(Object schoolName) {
		return findByProperty(SCHOOL_NAME, schoolName);
	}

	public List findByCountyId(Object countyId) {
		return findByProperty(COUNTY_ID, countyId);
	}

	public List findByAreaId(Object areaId) {
		return findByProperty(AREA_ID, areaId);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findAll() {
		log.debug("finding all School instances");
		try {
			String queryString = "from School";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public School merge(School detachedInstance) {
		log.debug("merging School instance");
		try {
			School result = (School) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(School instance) {
		log.debug("attaching dirty School instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(School instance) {
		log.debug("attaching clean School instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SchoolDAO getFromApplicationContext(ApplicationContext ctx) {
		return (SchoolDAO) ctx.getBean("SchoolDAO");
	}

	public Boolean isSchoolInCounty(Integer schoolId, Integer countyId) {
		String hql = "select count(*) from School s where s.schoolId=:schoolId and  s.countyId=:countyId  and Enable='1'";
		Query queryObj = this.getCurrentSession().createQuery(hql);
		queryObj.setParameter("schoolId", schoolId);
		queryObj.setParameter("countyId", countyId);
		long result = (long) queryObj.uniqueResult();
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Integer getSchoolIdByCode(String schoolId) {
		String schoolCode = "";
		String hql = "select schoolId from School s where s.schoolCode=:sCode and Enable='1'";
		Query queryObj = this.getCurrentSession().createQuery(hql);
		if (!CateringServiceUtil.isNumeric(schoolId)) {
			schoolCode = schoolId;
			// find code by code
		} else {
			Integer intSchoolId = Integer.valueOf(schoolId);
			schoolCode = String.format("%06d", intSchoolId);
			// find code by id
		}
		queryObj.setParameter("sCode", schoolCode);
		queryObj.setMaxResults(1);
		Integer sid = (Integer) queryObj.uniqueResult();
		if (sid == null) {
			return null;
		} else {
			return sid;
		}

	}

	public List executeQuery(String strSql) {

		List results = null;
		results = getCurrentSession().createSQLQuery(strSql).list();

		return results;

	}

	/**
	 *  20140504 Raymond 
	 *  查詢學校By 學校名稱
	 * @param schoolName
	 * @return
	 */
	public School querySchoolBySchoolName(String schoolName) {
		String hql = "FROM School s WHERE s.schoolName = :schoolName";
		Query query = dbSession.createQuery(hql);
		query.setParameter("schoolName", schoolName);
		if (query.list() != null && query.list().size() > 0) {
			return (School) query.list().get(0);
		} else {
			return null;
		}
	}

	/**
	 *  20140504 Raymond 
	 *  查詢學校By schoolId
	 * @param schoolId
	 * @return
	 */
	public School querySchoolById(int schoolId) {
		String hql = "FROM School s WHERE s.schoolId = :schoolId";
		Query query = dbSession.createQuery(hql);
		query.setParameter("schoolId", schoolId);
		if (query.list() != null && query.list().size() > 0) {
			return (School) query.list().get(0);
		} else {
			return null;
		}
	}

	/**
	 *  20140504 Raymond 
	 *  查詢學校By schoolCode
	 * @param schoolCode
	 * @return School
	 */
	public School querySchoolBySchoolCode(String schoolCode) {
		String hql = "FROM School s WHERE s.schoolCode = :schoolCode";
		Query query = dbSession.createQuery(hql);
		query.setParameter("schoolCode", schoolCode);
		if (query.list() != null && query.list().size() > 0) {
			return (School) query.list().get(0);
		} else {
			return null;
		}
	}

	/**
	 * 20140504 Raymond 
	 * Save Or Update
	 * @param school
	 * @return
	 */
	
	public School saveOrUpdate(School school) {
		try {
			dbSession.saveOrUpdate(school);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
		return school;
	}

	/**
	 * 20140504 Raymond 
	 * 查詢學校總數量
	 * @param countyId
	 * @return int
	 */
	public int queryTotelSchoolCount(Integer countyId){
		String hql = "SELECT COUNT(*) FROM School WHERE enable = 1 ";
		if(countyId!=null){
			hql += "AND countyId = :countyId";
		}
		
		Query query = dbSession.createQuery(hql);
		if(countyId!=null){
			query.setParameter("countyId", countyId);
		}
		
		return ((Number)query.uniqueResult()).intValue();
	}
	
	/**
	 * 20150730 Ellis 
	 * 查詢學校總數量(帶入條件)
	 * @param countyId
	 * @param queryschoolname
	 * @return int
	 */
	public int queryTotelSchoolCountv2(Integer countyId,String queryschoolname){
		String hql = "SELECT COUNT(*) FROM School WHERE enable = 1 ";
		if(countyId!=null){
			hql += " AND countyId = :countyId ";
		}
		if(!CateringServiceUtil.isEmpty(queryschoolname) && !CateringServiceUtil.isNull(queryschoolname)){
			hql += " AND schoolname like :queryschoolname ";
		}
		
		Query query = dbSession.createQuery(hql);
		if(countyId!=null){
			query.setParameter("countyId", countyId);
		}
		if(!CateringServiceUtil.isEmpty(queryschoolname) && !CateringServiceUtil.isNull(queryschoolname)){
			query.setParameter("queryschoolname", "%"+queryschoolname+"%");
		}
		return ((Number)query.uniqueResult()).intValue();
	}
	
	/**
	 * 20140506 Raymond 
	 * 更新學校狀態
	 * @param school
	 * @param enable
	 * @return School
	 */
	public School updateSchoolStatus(School school,Integer enable){
		school.setEnable(enable);
		dbSession.saveOrUpdate(school);
		return school;
	}
	/**
	 * 20140603 KC 
	 * 判斷學校類型
	 * @param schoolType
	 * @param countyId
	 * @return List<School>
	 */
	public List<School> querySchoolListByTypeAndCounty(Integer schoolType,Integer countyId){
		String hql = "FROM School s WHERE bitwise_andTwo(s.schoolType , :schoolType )=:schoolType and countyId=:countyId ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("schoolType", schoolType);
		query.setParameter("countyId", countyId);
		return query.list();
		
	}
	
	/**
	 * 20141126 Ellis
	 * 透過kid取得供應學校List
	 * @param kid
	 * @return List<School>
	 */
	public List<School> querySchoolListByKitchenId(Integer kid){
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		List<School> schools = new ArrayList<School>();
		Criteria criteriaSK = session.createCriteria(Schoolkitchen.class).add(Restrictions.eq("id.kitchenId", kid));
		List schoolkitchens = criteriaSK.list();
		Iterator<Schoolkitchen> iteratorSK = schoolkitchens.iterator();
		while (iteratorSK.hasNext()) {
			Schoolkitchen sk = iteratorSK.next();
			Criteria criteriaSC = session.createCriteria(School.class).add(Restrictions.eq("schoolId", sk.getId().getSchoolId())).add(Restrictions.eq("enable", 1));
			List Schools = criteriaSC.list();
			Iterator<School> iteratorSC = Schools.iterator();
			while (iteratorSC.hasNext()) {
				schools.add(iteratorSC.next());
			}
		}
		return schools;
	}

	/*
	 * public void executeSql(String strSql) { log.debug(strSql); try {
	 * 
	 * getCurrentSession().createSQLQuery(strSql).executeUpdate();
	 * log.debug("executeSql successful"); } catch (RuntimeException re) {
	 * log.error("executeSql failed", re); throw re; }
	 * 
	 * }
	 */
	public void executeSql(Session session, String strSql) {
		log.debug(strSql);
		try {

			session.createSQLQuery(strSql).executeUpdate();
			log.debug("executeSql successful");
		} catch (RuntimeException re) {
			log.error("executeSql failed", re);
			throw re;
		}

	}
}