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

/**
 * A data access object (DAO) providing persistence and search support for
 * Kitchen entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Kitchen
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class KitchenDAO {
	private static final Logger log = LoggerFactory.getLogger(KitchenDAO.class);
	// property constants
	public static final String USER_NAME = "userName";
	public static final String PASSWORD = "password";
	public static final String KITCHEN_NAME = "kitchenName";
	public static final String KITCHEN_TYPE = "kitchenType";
	public static final String ADDRESS = "address";
	public static final String OWNNER = "ownner";
	public static final String TEL = "tel";
	public static final String FAX = "fax";
	public static final String NUTRITIONIST = "nutritionist";
	public static final String CHEF = "chef";
	public static final String HACCP = "haccp";
	public static final String QUALIFIER = "qualifier";
	public static final String INSUREMENT = "insurement";
	public static final String PICTURE_PATH = "picturePath";
	public static final String COMPANYID = "companyId";
	public static final String EMAIL="email";

	private SessionFactory sessionFactory;
	private Session dbSession;

	public KitchenDAO(){}
	
	public KitchenDAO(Session dbSession){
		this.dbSession = dbSession;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		if (this.dbSession==null){
			return sessionFactory.getCurrentSession();
		}else{
			return this.dbSession;
		}
	}

	protected void initDao() {
		// do nothing
	}
	public  void setSession(Session session){
		this.dbSession=session;
	}

	public void save(Kitchen transientInstance) {
		log.debug("saving Kitchen instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Kitchen persistentInstance) {
		log.debug("deleting Kitchen instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Kitchen findById(java.lang.Integer id) {
		log.debug("getting Kitchen instance with id: " + id);
		try {
			Kitchen instance = (Kitchen) getCurrentSession().get(
					"org.iii.ideas.catering_service.dao.Kitchen", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public List findByLogin(String UserName, String Password) {
		log.debug("finding all Kitchen instances");
		try {
			Criteria kitchenCriteria = getCurrentSession().createCriteria(Kitchen.class);
			kitchenCriteria.add(Restrictions.eq(USER_NAME, UserName));
			kitchenCriteria.add(Restrictions.eq(PASSWORD, Password));
		
			return (List<Kitchen>)kitchenCriteria.list();
			
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public void update(Kitchen transientInstance) {
		log.debug("updating Kitchen instance");
		try {
			getCurrentSession().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	
	public void update(Session session,Kitchen transientInstance) {
		log.debug("updating Kitchen instance");
		try {
			session.update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	public List findByExample(Kitchen instance) {
		log.debug("finding Kitchen instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.Kitchen")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Kitchen instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Kitchen as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByKitchenName(Object kitchenName) {
		return findByProperty(KITCHEN_NAME, kitchenName);
	}

	public List findByKitchenType(Object kitchenType) {
		return findByProperty(KITCHEN_TYPE, kitchenType);
	}

	public List findByAddress(Object address) {
		return findByProperty(ADDRESS, address);
	}

	public List findByOwnner(Object ownner) {
		return findByProperty(OWNNER, ownner);
	}

	public List findByTel(Object tel) {
		return findByProperty(TEL, tel);
	}

	public List findByFax(Object fax) {
		return findByProperty(FAX, fax);
	}

	public List findByNutritionist(Object nutritionist) {
		return findByProperty(NUTRITIONIST, nutritionist);
	}

	public List findByChef(Object chef) {
		return findByProperty(CHEF, chef);
	}

	public List findByHaccp(Object haccp) {
		return findByProperty(HACCP, haccp);
	}

	public List findByQualifier(Object qualifier) {
		return findByProperty(QUALIFIER, qualifier);
	}

	public List findByInsurement(Object insurement) {
		return findByProperty(INSUREMENT, insurement);
	}

	public List findByPicturePath(Object picturePath) {
		return findByProperty(PICTURE_PATH, picturePath);
	}
	
	public List findByEmail(Object email) {
		return findByProperty(EMAIL, email);
	}

	public List findAll() {
		log.debug("finding all Kitchen instances");
		try {
			String queryString = "from Kitchen";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Kitchen merge(Kitchen detachedInstance) {
		log.debug("merging Kitchen instance");
		try {
			Kitchen result = (Kitchen) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Kitchen instance) {
		log.debug("attaching dirty Kitchen instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Kitchen instance) {
		log.debug("attaching clean Kitchen instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public Kitchen saveOrUpdate(Kitchen kitchen){
		try{
			dbSession.saveOrUpdate(kitchen);
			log.debug("save successful");
		}catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
		return kitchen;
	}

	public static KitchenDAO getFromApplicationContext(ApplicationContext ctx) {
		return (KitchenDAO) ctx.getBean("KitchenDAO");
	}
	
	/**
	 * Raymond 20140504 查詢廚房 by kitchenName
	 * @param kitchenName
	 * @return Kitchen
	 */
	public Kitchen queryKitchenByKitchenName(String kitchenName){
		String hql = "FROM Kitchen k WHERE k.kitchenName = :kitchenName";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenName", kitchenName);
		if(query.list()!=null&&query.list().size()>0){
			return (Kitchen)query.list().get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * Raymond 20140504 查詢廚房 by companyId ,kitchenType
	 * @param companyId
	 * @param kitchenType
	 * @return Kitchen
	 */
	public Kitchen queryKitchenByCompanyIdAndKitchentype(String companyId,String kitchenType){
		String hql = "FROM Kitchen k WHERE k.companyId = :companyId AND k.kitchenType = :kitchenType";
		Query query = dbSession.createQuery(hql);
		query.setParameter("companyId", companyId);
		query.setParameter("kitchenType", kitchenType);
		if(query.list()!=null&&query.list().size()>0){
			return (Kitchen)query.list().get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * Raymond 20140506 更新廚房狀態
	 * @param kitchen
	 * @param kitchenType(enable:1/disable:0)
	 * @return Kitchen
	 */
	public Kitchen updateSchoolStatus(Kitchen kitchen,Integer enable){
		kitchen.setEnable(enable);
		dbSession.saveOrUpdate(kitchen);
		return kitchen;
	}
	
	/**
	 * Raymond 20140509 查詢廚房 by companyId
	 * @param companyId
	 * @return Kitchen
	 */
	public Kitchen queryKitchenByCompanyId(String companyId){
		String hql = "FROM Kitchen k WHERE k.companyId = :companyId "
				+ "ORDER BY k.enable desc,k.kitchenType ";
		//優先查到enable並且kitchenType較小的資料
		Query query = dbSession.createQuery(hql);
		query.setParameter("companyId", companyId);
		if(query.list()!=null&&query.list().size()>0){
			return (Kitchen)query.list().get(0);
		}else{
			return null;
		}
	}
	
	public List<Kitchen> queryKitchenByKitchenIdList(List<Integer> kidList){
		String kid = "";
		for(int i =0;i<kidList.size();i++){
			if(i !=0){
				kid+=",";
			}
			kid +=kidList.get(i).toString();
		}
		
		String hql = "FROM Kitchen k WHERE k.kitchenId in ("+kid
				+ ") ORDER BY k.enable desc,k.kitchenType ";
		//優先查到enable並且kitchenType較小的資料
		Query query = dbSession.createQuery(hql);
//		query.setParameter("KitchenId", kidList);
		if(query.list()!=null&&query.list().size()>0){
			return (List<Kitchen>)query.list();
		}else{
			return null;
		}
	}
	
	public Kitchen queryKitchenByKitchenId(Integer kid){
			
		String hql = "FROM Kitchen k WHERE k.kitchenId = :kitchenId ";
		//優先查到enable並且kitchenType較小的資料
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kid);
		if(query.list()!=null&&query.list().size()>0){
			return (Kitchen) query.list().get(0);
		}else{
			return null;
		}
	}
}