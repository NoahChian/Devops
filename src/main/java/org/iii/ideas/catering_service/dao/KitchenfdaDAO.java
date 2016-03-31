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

public class KitchenfdaDAO {
	private static final Logger log = LoggerFactory.getLogger(KitchenfdaDAO.class);
	// property constants
	public static final String KITCHEN_ID = "kitchenId";
	public static final String FDA_COMPANY_ID = "fdaCompanyId";
	public static final String UPDATE_TIME = "updatetime";
	public static final String UPDATE_USER = "updateuser";

	private SessionFactory sessionFactory;
	private Session dbSession;

	public KitchenfdaDAO() {
	}

	public KitchenfdaDAO(Session dbSession) {
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

	public static KitchenfdaDAO getFromApplicationContext(ApplicationContext ctx) {
		return (KitchenfdaDAO) ctx.getBean("KitchenfdaDAO");
	}

	public Kitchenfda queryKitchenfdaByKitchenId(Integer kitchenId) {
		String hql = "FROM Kitchenfda kf WHERE kf.kitchenId = :kitchenId";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		if (query.list() != null && query.list().size() > 0) {
			return (Kitchenfda) query.list().get(0);
		} else {
			return null;
		}
	}
}