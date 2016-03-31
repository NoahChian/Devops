package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.iii.ideas.catering_service.ws.schemav2.StatusCode;;
public class WsLogDAO{
	private static final Logger log = LoggerFactory.getLogger(KitchenDAO.class);
	public static final String ID="id";
	public static final String MESSAGEID="messageId";
	public static final String COMPANYID="companyId";
	public static final String STATUSCODE="statusCode";
	public static final String SENDTIME="sendTime";
	public static final String UPDATETIME="updateTime";
	public static final String DESCRIPTION ="description";
	public static final String ACTION="action";
	private SessionFactory sessionFactory;
	private Session dbSession;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		if (this.dbSession==null){
			return sessionFactory.getCurrentSession();
		}else{
			return  this.dbSession;
		}
	}
	public void setSession(Session session){
		this.dbSession=session;
	}

	protected void initDao() {
		// do nothing
	}

	public void save(WsLog transientInstance) {
		log.debug("saving Kitchen instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WsLog persistentInstance) {
		log.debug("deleting Kitchen instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public void update(WsLog transientInstance) {
		try {
			getCurrentSession().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	
	public void update(Session session,WsLog transientInstance) {
		try {
			session.update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding WsLog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from WsLog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	//檢查是否有重複 messageId
	public boolean isUniqSuccessMsgId(String messageId,String companyId){
		String hql=" from WsLog where messageId=:messageId and companyId =:companyId and statusCode=:statusCode";
		Query queryObj=this.getCurrentSession().createQuery(hql);
		queryObj.setParameter("messageId", messageId);
		queryObj.setParameter("companyId", companyId);
		queryObj.setParameter("statusCode", StatusCode.WS_MSG_SUCCESS);

		if (queryObj.list().size()==0){
			return true;
		}else{
			return false;
		}
	}
	//抓某串messageId的紀錄
	public List<WsLog> findLogByMessageId(String messageId,String companyId){
		String hql="from WsLog where messageId=:messageId and companyId:companyId  order by sendTime desc";
		try{
			Query queryObj=this.getCurrentSession().createQuery(hql);
			queryObj.setParameter("messageId", messageId);
			queryObj.setParameter("companyId", companyId);
			return queryObj.list();
		}catch (Exception ex){
			throw ex;
		}
	}

}
