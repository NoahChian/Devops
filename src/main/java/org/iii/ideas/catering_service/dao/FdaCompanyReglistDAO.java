package org.iii.ideas.catering_service.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class FdaCompanyReglistDAO{
	private static final Logger Log = LoggerFactory.getLogger(FdaCompanyReglistDAO.class);
	
	public static final String ID = "id";
	public static final String COMPANY_NAME = "company_Name";
	public static final String BUSINESS_ID = "businessId";
	public static final String ADDRESS = "address";
	public static final String FDA_COMPANY_ID = "fdaCompanyId";
	public static final String REG_TYPE = "regType";
	
	private SessionFactory sessionFactory;
	private Session dbSession;
	
	public FdaCompanyReglistDAO() {
	}
	
	public FdaCompanyReglistDAO(Session dbSession){
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
	
	protected void initDao() {
		// do nothing
	}
	
	public void setSession(Session session) {
		this.dbSession = session;
	}
	
	public static FdaCompanyReglistDAO getFromApplicationContext(ApplicationContext ctx) {
		return (FdaCompanyReglistDAO) ctx.getBean("FdaCompanyReglistDAO");
	}
	
	public FdaCompanyReglist queryFdaCompanyReglistByFdaCompanyId(String fdaCompanyId) {
		String hql = "FROM FdaCompanyReglist fc WHERE fc.fdaCompanyId = :fdaCompanyId";
		Query query = dbSession.createQuery(hql);
		query.setParameter("fdaCompanyId", fdaCompanyId);
		if (query.list() != null && query.list().size() > 0) {
			return (FdaCompanyReglist) query.list().get(0);
		} else {
			return null;
		}
	}
}