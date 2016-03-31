package org.iii.ideas.catering_service.dao;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * Supplier entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Supplier
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class SupplierDAO {
	private static final Logger log = LoggerFactory.getLogger(SupplierDAO.class);
	// property constants
	public static final String SUPPLIER_NAME = "supplierName";
	public static final String OWNNER = "ownner";
	public static final String COMPANY_ID = "companyId";
	public static final String COUNTY_ID = "countyId";
	public static final String AREA_ID = "areaId";
	public static final String SUPPLIER_ADRESS = "supplierAdress";
	public static final String SUPPLIER_TEL = "supplierTel";
	public static final String SUPPLIER_CERTIFICATION = "supplierCertification";
	public static final String SUPPLIER_CITY = "supplierCity";
	public static final String SUPPLIER_AREA = "supplierArea";
	public static final String KITCHEN_ID = "id.kitchenId";

	private SessionFactory sessionFactory;
	private Session dbSession;

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

	public SupplierDAO() {
	}

	public SupplierDAO(Session dbSession) {
		setSession(dbSession);
	}

	protected void initDao() {
		// do nothing
	}

	public void save(Session session, Supplier transientInstance) {
		log.debug("saving Supplier instance");
		try {
			session.save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void save(Supplier transientInstance) {
		log.debug("saving Supplier instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/*
	 * public void delete(Supplier persistentInstance) {
	 * log.debug("deleting Supplier instance"); try {
	 * getCurrentSession().delete(persistentInstance);
	 * log.debug("delete successful"); } catch (RuntimeException re) {
	 * log.error("delete failed", re); throw re; } }
	 */
	public void delete(Session session, Supplier persistentInstance) {
		log.debug("deleting Supplier instance");
		try {
			session.delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Supplier findById(org.iii.ideas.catering_service.dao.SupplierId id) {
		log.debug("getting Supplier instance with id: " + id);
		try {
			Supplier instance = (Supplier) getCurrentSession().get("org.iii.ideas.catering_service.dao.Supplier", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * public Supplier findById(java.lang.Integer id) {
	 * log.debug("getting Supplier instance with id: " + id); try { Supplier
	 * instance = (Supplier) getCurrentSession().get(
	 * "org.iii.ideas.catering_service.dao.Supplier", id); return instance; }
	 * catch (RuntimeException re) { log.error("get failed", re); throw re; } }
	 */
	public List findByExample(Supplier instance) {
		log.debug("finding Supplier instance by example");
		try {
			List results = getCurrentSession().createCriteria("org.iii.ideas.catering_service.dao.Supplier").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Supplier instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from Supplier as model where model." + propertyName + "= ?";
			Query queryObject = this.getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySupplierName(String supplierName) {

		try {
			String queryString = "from Supplier where supplierName =:supplierName";
			Query queryObject = this.getCurrentSession().createQuery(queryString);
			queryObject.setParameter("supplierName", supplierName);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("findBySupplierName failed", re);
			throw re;
		}
		// return findByProperty(SUPPLIER_NAME, supplierName);
	}

	public List findByOwnner(Object ownner) {
		return findByProperty(OWNNER, ownner);
	}

	public List findByCompanyId(Object companyId) {
		return findByProperty(COMPANY_ID, companyId);
	}

	public List findByCountyId(Object countyId) {
		return findByProperty(COUNTY_ID, countyId);
	}

	public List findByAreaId(Object areaId) {
		return findByProperty(AREA_ID, areaId);
	}

	public List findBySupplierAdress(Object supplierAdress) {
		return findByProperty(SUPPLIER_ADRESS, supplierAdress);
	}

	public List findBySupplierTel(Object supplierTel) {
		return findByProperty(SUPPLIER_TEL, supplierTel);
	}

	public List findBySupplierCertification(Object supplierCertification) {
		return findByProperty(SUPPLIER_CERTIFICATION, supplierCertification);
	}

	public List findAll() {
		log.debug("finding all Supplier instances");
		try {
			String queryString = "from Supplier";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Supplier merge(Supplier detachedInstance) {
		log.debug("merging Supplier instance");
		try {
			Supplier result = (Supplier) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Supplier instance) {
		log.debug("attaching dirty Supplier instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Supplier instance) {
		log.debug("attaching clean Supplier instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SupplierDAO getFromApplicationContext(ApplicationContext ctx) {
		return (SupplierDAO) ctx.getBean("SupplierDAO");
	}

	public List findByKitchenId(int kitchenid) {
		log.debug("getting Supplier instance with KitchenId: " + kitchenid);
		try {
			// String HQL =
			// "from Supplier where id.kitchenId= :kitchenId order by convert(supplierName useing big5)";
			String HQL = "from Supplier where id.kitchenId= :kitchenId order by supplierName";
			Query query = getCurrentSession().createQuery(HQL);
			query.setParameter("kitchenId", kitchenid);
			List<Supplier> allSupplierList = (List<Supplier>) query.list();
			/*
			 * Criteria supplierCriteria =
			 * getCurrentSession().createCriteria(Supplier.class);
			 * supplierCriteria.add(Restrictions.eq(KITCHEN_ID, kitchenid));
			 * supplierCriteria.addOrder(Order.desc(SUPPLIER_NAME));
			 * List<Supplier> allSupplierList =
			 * (List<Supplier>)supplierCriteria.list();
			 */
			return allSupplierList;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public HashMap supplierManagerIndex(String kitchenid, HttpServletRequest request, String goPage, int startCount, int limitCount) {
		HashMap<String, Object> supplierMap = new HashMap<String, Object>();

		HttpSession session = request.getSession();
		Criteria supplierCriteria = getCurrentSession().createCriteria(Supplier.class);
		if (session.getAttribute("searchSupplierName") != null) {
			String suppilerName = session.getAttribute("searchSupplierName").toString();
			supplierCriteria.add(Restrictions.sqlRestriction(" SupplierName like ?", "%" + suppilerName + "%", StandardBasicTypes.STRING));
			supplierCriteria.add(Restrictions.sqlRestriction(" KitchenId like ?", kitchenid, StandardBasicTypes.STRING));

		} else {
			supplierCriteria.add(Restrictions.sqlRestriction(" KitchenId like ?", kitchenid, StandardBasicTypes.STRING));
			// supplierCriteria.add(Restrictions.gt(KITCHEN_ID, kitchenid));
		}
		List<Supplier> allSupplierList = (List<Supplier>) supplierCriteria.list();

		// where limit (分頁)
		String supplyPage = (String) session.getAttribute("supplyPage");
		if (request.getParameter("searchPage") != null) {
			goPage = request.getParameter("searchPage");
			session.setAttribute("supplyPage", goPage);
			if (!goPage.equals("1"))
				startCount = (Integer.parseInt(goPage) - 1) * limitCount;

			supplierCriteria.setFirstResult(startCount).setMaxResults(limitCount);
		} else {
			if (supplyPage != null && supplyPage != "") {
				goPage = supplyPage;
				if (!goPage.equals("1"))
					startCount = (Integer.parseInt(goPage) - 1) * limitCount;
				supplierCriteria.setFirstResult(startCount).setMaxResults(limitCount);
			} else {
				supplierCriteria.setFirstResult(0).setMaxResults(limitCount);
			}
		}
		supplierCriteria.addOrder(Order.asc(COMPANY_ID));
		List<Supplier> supplierList = supplierCriteria.list();

		supplierMap.put("allsupplierList", allSupplierList);
		supplierMap.put("goPage", goPage);
		supplierMap.put("supplierList", supplierList);
		return supplierMap;
	}

	public HashMap supplierManagerSearch(String kitchenid, String suppilerName, HttpServletRequest request, String goPage, int startCount, int limitCount) {
		HashMap<String, Object> supplierMap = new HashMap<String, Object>();

		Criteria supplierCriteria = getCurrentSession().createCriteria(Supplier.class);
		// supplierCriteria.add(Restrictions.gt(KITCHEN_ID, kitchenid));
		supplierCriteria.add(Restrictions.sqlRestriction(" SupplierName like ?", "%" + suppilerName + "%", StandardBasicTypes.STRING));
		supplierCriteria.add(Restrictions.sqlRestriction(" KitchenId like ?", kitchenid, StandardBasicTypes.STRING));

		// @SuppressWarnings("deprecation")
		// Criterion criterion1 = Expression.like("name", "%"+name.trim()+"%");

		List<Supplier> allSupplierList = (List<Supplier>) supplierCriteria.list();

		// where limit (分頁)
		if (request.getParameter("searchPage") != null) {
			goPage = request.getParameter("searchPage");

			if (!goPage.equals("1"))
				startCount = (Integer.parseInt(goPage) - 1) * limitCount;

			supplierCriteria.setFirstResult(startCount).setMaxResults(limitCount);
		} else
			supplierCriteria.setFirstResult(0).setMaxResults(limitCount);

		supplierCriteria.addOrder(Order.asc(COMPANY_ID));
		List<Supplier> supplierList = supplierCriteria.list();

		supplierMap.put("allsupplierList", allSupplierList);
		supplierMap.put("goPage", goPage);
		supplierMap.put("supplierList", supplierList);
		return supplierMap;
	}

	public void executeSql(String strSql) {
		log.debug(strSql);
		try {
			getCurrentSession().createSQLQuery(strSql).executeUpdate();
			log.debug("executeSql successful");
		} catch (RuntimeException re) {
			log.error("executeSql failed", re);
			throw re;
		}

	}

	/*
	 * public void update(Supplier transientInstance) {
	 * log.debug("updating Supplier instance"); try {
	 * getCurrentSession().update(transientInstance);
	 * log.debug("update successful"); } catch (RuntimeException re) {
	 * log.error("update failed", re); throw re; } }
	 */
	public void update(Session session, Supplier transientInstance) {
		log.debug("updating Supplier instance");
		try {
			session.update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	public boolean isUniqSupplier(Integer kitchenId, String companyId, String supplierName) {
		String hql = "from Supplier where kitchenId=:kitchenId and ( supplierName=:supplierName or  companyId=:companyId)";
		Query queryObj = this.getCurrentSession().createQuery(hql);
		queryObj.setParameter("kitchenId", kitchenId);
		queryObj.setParameter("supplierName", supplierName);
		queryObj.setParameter("companyId", companyId);
		if (queryObj.list().size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	//查詢供應商資料by 分頁與名稱
	@SuppressWarnings("unchecked")
	public List<Supplier> querySupplierListPager(String supplierName, Integer kitchenId, Integer pageIndex, Integer pageLimitNum) {
		int startIndex = (pageIndex - 1 ) * pageLimitNum;
		
		String hql = "FROM Supplier a WHERE a.id.kitchenId = :kitchenId ";
		if (!CateringServiceUtil.isEmpty(supplierName)) {
			hql += "AND a.supplierName LIKE :supplierName ";
		}

		hql += "ORDER BY a.supplierName ASC ";

		Query query = dbSession.createQuery(hql);
		if (!CateringServiceUtil.isEmpty(supplierName)) {
			query.setParameter("supplierName", "%" + supplierName + "%");
		}
		
		query.setParameter("kitchenId", kitchenId);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageLimitNum);

		return (List<Supplier>)query.list();
	}
	
	//查詢供應商資料總數量
	public int queryTotelSupplierCount(String supplierName,Integer kitchenId){
		String hql = "SELECT COUNT(*) FROM Supplier a WHERE a.id.kitchenId = :kitchenId ";
		if (!CateringServiceUtil.isEmpty(supplierName)) {
			hql += "AND a.supplierName LIKE :supplierName ";
		}
		
		Query query = dbSession.createQuery(hql);
		if (!CateringServiceUtil.isEmpty(supplierName)) {
			query.setParameter("supplierName", "%" + supplierName + "%");
		}
		query.setParameter("kitchenId", kitchenId);
		
		return ((Number)query.uniqueResult()).intValue();
	}
	
	//查詢supplier by id
	public Supplier querySupplierById(Integer supplierId,Integer kitchenId){
		String hql = "FROM Supplier a WHERE a.id.kitchenId = :kitchenId AND a.id.supplierId = :supplierId ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		query.setParameter("supplierId", supplierId);
		
		if(query.list()!=null && query.list().size() > 0)
			return (Supplier)query.list().get(0);
		else
			return null;
	}
	
	// 查詢supplier by company id
	public Supplier querySupplierByCompanyId(String companyId, Integer kitchenId) {
		String hql = "FROM Supplier a WHERE a.companyId = :companyId AND a.id.kitchenId = :kitchenId ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("companyId", companyId);
		query.setParameter("kitchenId", kitchenId);

		if (query.list() != null && query.list().size() > 0)
			return (Supplier) query.list().get(0);
		else
			return null;
	}
	
	// 查詢supplier by supplier name
	public Supplier querySupplierBySupplierName(String supplierName, Integer kitchenId) {
		String hql = "FROM Supplier a WHERE a.supplierName = :supplierName AND a.id.kitchenId = :kitchenId ";
		
		Query query = dbSession.createQuery(hql);
		query.setParameter("supplierName", supplierName);
		query.setParameter("kitchenId", kitchenId);

		if (query.list() != null && query.list().size() > 0)
			return (Supplier) query.list().get(0);
		else
			return null;
		
	}
	
	//查詢Supplier List
	public String [] querySupplierList(int kid) {
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		String HQL = "select supplierName from Supplier where kitchenId = :kitchenId "
				+ "and (supplierName is not null and supplierName <> '')  order by supplierName";
		Query SupplierFileQuery = session.createQuery(HQL);
		SupplierFileQuery.setParameter("kitchenId", kid);
		
		//Map<String, Object[]> data = new TreeMap<String, Object[]>();
		String [] list = new String[SupplierFileQuery.list().size()] ;
		List SupplierFileFormat = SupplierFileQuery.list();
		Iterator<Object> SupplierFileIterator = SupplierFileFormat.iterator();
		int i = 0;
		while (SupplierFileIterator.hasNext()) {
			Object obj = SupplierFileIterator.next();
			list [i] = obj.toString();
			i++;
			//list[i] = obj;
		}
		session.close();
		return list;// 供應商List
	}
}