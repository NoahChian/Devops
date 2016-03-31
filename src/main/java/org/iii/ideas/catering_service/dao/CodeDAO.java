package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CodeDAO {
	private static final Logger log = LoggerFactory.getLogger(CodeDAO.class);

	private SessionFactory sessionFactory;
	private Session dbSession;

	public CodeDAO() {
	}

	public CodeDAO(Session dbSession) {
		this.dbSession = dbSession;
	}

	public void openSessionFactory() {
		this.sessionFactory = HibernateUtil.buildSessionFactory();
		this.dbSession = sessionFactory.openSession();
	}

	public void closeSession() {
		this.dbSession.close();
	}

	public void setSession(Session session) {
		this.dbSession = session;
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

	public void save(Code transientInstance) {
		log.debug("saving Counter instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Code persistentInstance) {
		log.debug("deleting dishbatchdata instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public void attachDirty(Code instance) {
		// log.debug("attaching dirty dishbatchdata instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Code> findCodeListByType(String type) {
		try {
			String hql = "from Code s where s.type=:type order by sort asc";
			Query queryObj = this.getCurrentSession().createQuery(hql);
			queryObj.setParameter("type", type);
			return queryObj.list();
		} catch (Exception ex) {
			log.error("query error: Code getCodeListByType:" + type);
			throw ex;
		}
	}

	// 找訊息
	public String getCodeMsgByStatusCode(String statusCode, String codeType) {
		String hql = "select name from Code where code=:code and type=:type order by sort asc";
		try {
			Query queryObj = this.getCurrentSession().createQuery(hql);
			queryObj.setParameter("code", statusCode);
			queryObj.setParameter("type", codeType);
			return (String) queryObj.uniqueResult();

		} catch (Exception ex) {
			throw ex;
		}
	}

	// 找訊息
	public String getCodeByTypeAndName(String codeName, String codeType) {
		String hql = "From Code where name=:name and type=:type order by sort asc";
		try {
			Query queryObj = this.getCurrentSession().createQuery(hql);
			queryObj.setParameter("name", codeName);
			queryObj.setParameter("type", codeType);
			if(queryObj.list()!=null && queryObj.list().size() > 0){
				Code code = (Code)queryObj.list().get(0);
				return code.getCode();
			}else{
				return null;
			}

		} catch (Exception ex) {
			throw ex;
		}
	}
}
