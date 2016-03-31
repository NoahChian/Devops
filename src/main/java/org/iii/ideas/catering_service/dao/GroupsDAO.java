package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * Group entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Groups
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class GroupsDAO {
	private static final Logger log = LoggerFactory.getLogger(GroupsDAO.class);
	private SessionFactory sessionFactory;
	private Session dbSession;

	public GroupsDAO() {
	};

	public GroupsDAO(Session dbSession) {
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
		log.debug("finding Groups instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from Groups as model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
//	public List findByNewsId(Object newsId) {
//		return findByProperty(News.NEWS_ID, newsId);
//	}
	
//public List queryGroupsByNewsId(Integer newsId) {
//	String hql = "FROM Groups s WHERE s.newsId = :newsId";
//	Query query = dbSession.createQuery(hql);
//	query.setParameter("newsId", newsId);
//	if (query.list() != null && query.list().size() > 0) {
//		return query.list();
//	} else {
//		return null;
//	}
//}

//	public List findBySchoolId(Object schoolId) {
//		return findByProperty(News.SCHOOL_ID, schoolId);
//	}
	
//	public News querySchoolByUsername(String username) {
//		String hql = "FROM News s WHERE s.username = :username";
//		Query query = dbSession.createQuery(hql);
//		query.setParameter("username", username);
//		if (query.list() != null && query.list().size() > 0) {
//			return (News) query.list().get(0);
//		} else {
//			return null;
//		}
//	}

	public static GroupsDAO getFromApplicationContext(ApplicationContext ctx) {
		return (GroupsDAO) ctx.getBean("GroupDAO");
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
	
	public void save(Session session, News transientInstance) {
		log.debug("saving News instance");
		try {
			session.save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void save(News transientInstance) {
		log.debug("saving News instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/*
	 * public void delete(News persistentInstance) {
	 * log.debug("deleting News instance"); try {
	 * getCurrentSession().delete(persistentInstance);
	 * log.debug("delete successful"); } catch (RuntimeException re) {
	 * log.error("delete failed", re); throw re; } }
	 */
	public void delete(Session session, News persistentInstance) {
		log.debug("deleting News instance");
		try {
			session.delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
}