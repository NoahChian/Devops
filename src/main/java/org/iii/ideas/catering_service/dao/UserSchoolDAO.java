package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
public class UserSchoolDAO {
	private static final Logger log = LoggerFactory.getLogger(UserSchoolDAO.class);
	// property constants	
	public static final String USER_NAME = "username";
	public static final String SCHOOL_ID = "schoolId";

	private SessionFactory sessionFactory;
	private Session dbSession;

	public UserSchoolDAO() {
	};

	public UserSchoolDAO(Session dbSession) {
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
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding userschool instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from UserSchool as model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySchoolId(Object schoolId) {
		return findByProperty(SCHOOL_ID, schoolId);
	}
	
	public List findByUsername(Object username) {
		return findByProperty(USER_NAME, username);
	}
	
//	public UserSchool querySchoolByUsername(String username) {
//		String hql = "FROM userschool s WHERE s.username = :username";
//		Query query = dbSession.createQuery(hql);
//		query.setParameter("username", username);
//		if (query.list() != null && query.list().size() > 0) {
//			return (UserSchool) query.list().get(0);
//		} else {
//			return null;
//		}
//	}

	public static UserSchoolDAO getFromApplicationContext(ApplicationContext ctx) {
		return (UserSchoolDAO) ctx.getBean("UserSchoolDAO");
	}
	
	public List executeQuery(String strSql) {

		List results = null;
		results = getCurrentSession().createSQLQuery(strSql).list();

		return results;

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