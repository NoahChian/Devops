package org.iii.ideas.catering_service.dao;


import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetPwdLogDAO {
	private static final Logger log = LoggerFactory.getLogger(KitchenDAO.class);

	private SessionFactory sessionFactory;
	private Session dbSession;

	public ResetPwdLogDAO() {
	}

	public ResetPwdLogDAO(Session dbSession) {
		this.dbSession = dbSession;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setSession(Session session) {
		this.dbSession = session;
	}

	public Resetpwdlog queryResetPwdLogLastRecord(String username) {
		String hql = "FROM Resetpwdlog a WHERE a.username = :username ORDER BY a.logId DESC ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("username", username);
		
		if(query.list()!=null && query.list().size() > 0){
			return (Resetpwdlog)query.list().get(0);
		}else{
			return null;
		}
	}
	
	public Resetpwdlog queryResetPwdLogRecordByTsAndStatus(String username,String ts,String status) {
		String hql = "FROM Resetpwdlog a WHERE a.username = :username AND a.ts = :ts AND a.status = :status ORDER BY a.logId DESC ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("username", username);
		query.setParameter("ts", ts);
		query.setParameter("status", status);
		
		if(query.list()!=null && query.list().size() > 0){
			return (Resetpwdlog)query.list().get(0);
		}else{
			return null;
		}
	}
	
	public Resetpwdlog queryAfterLogRecord(String username,Date time) {
		String hql = "FROM Resetpwdlog a WHERE a.username = :username and a.createTime >= :time  ORDER BY a.logId DESC ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("username", username);
		query.setParameter("time", time);
		
		if(query.list()!=null && query.list().size() > 0){
			return (Resetpwdlog)query.list().get(0);
		}else{
			return null;
		}
	}

}
