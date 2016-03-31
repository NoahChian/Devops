package org.iii.ideas.catering_service.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * Useraccount entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Useraccount
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class UseraccountDAO {
	private static final Logger log = LoggerFactory.getLogger(UseraccountDAO.class);
	// property constants
	public static final String PASSWORD = "password";
	public static final String USERTYPE = "usertype";
	public static final String KITCHEN_ID = "kitchenId";

	private SessionFactory sessionFactory;
	private Session dbSession;

	public UseraccountDAO() {

	}

	public UseraccountDAO(Session dbSession) {
		this.dbSession = dbSession;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		// return sessionFactory.getCurrentSession();
		return this.dbSession;
	}

	protected void initDao() {
		// do nothing
	}

	public void save(Useraccount transientInstance) {
		log.debug("saving Useraccount instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Useraccount persistentInstance) {
		log.debug("deleting Useraccount instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Useraccount findById(java.lang.String id) {
		log.debug("getting Useraccount instance with id: " + id);
		try {
			Useraccount instance = (Useraccount) getCurrentSession().get("org.iii.ideas.catering_service.dao.Useraccount", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Useraccount instance) {
		log.debug("finding Useraccount instance by example");
		try {
			List results = getCurrentSession().createCriteria("org.iii.ideas.catering_service.dao.Useraccount").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Useraccount instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from Useraccount as model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}

	public List findByUsertype(Object usertype) {
		return findByProperty(USERTYPE, usertype);
	}

	public List findByKitchenId(Object kitchenId) {
		return findByProperty(KITCHEN_ID, kitchenId);
	}

	public List findAll() {
		log.debug("finding all Useraccount instances");
		try {
			String queryString = "from Useraccount";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Useraccount merge(Useraccount detachedInstance) {
		log.debug("merging Useraccount instance");
		try {
			Useraccount result = (Useraccount) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Useraccount instance) {
		log.debug("attaching dirty Useraccount instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Useraccount instance) {
		log.debug("attaching clean Useraccount instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static UseraccountDAO getFromApplicationContext(ApplicationContext ctx) {
		return (UseraccountDAO) ctx.getBean("UseraccountDAO");
	}

	public void closeSession() {
		this.dbSession.close();
	}

	public void openSessionFactory() {
		this.sessionFactory = HibernateUtil.buildSessionFactory();
		this.dbSession = sessionFactory.openSession();
	}

	public List getUseraccountAndKitchenNameByKitchenId() {
		log.debug("get UseraccountAndKitchenName");
		try {
			String queryString = "SELECT ua.username,ua.usertype,ua.name,ua.email, k.kitchenName, r.roletype FROM Useraccount as ua ";
			queryString += ", Kitchen as k, Userrole as r where ua.kitchenId = k.kitchenId and r.username = ua.username ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			log.debug("get successful");
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List getUseraccountAndKitchenNameByKitchenId2(String countyId) {
		log.debug("get UseraccountAndKitchenName");
		try {
			String queryString = "SELECT ua.username,ua.usertype,ua.name,ua.email, k.kitchenName, r.roletype , r.createDate FROM Useraccount as ua ";
			queryString += ", Kitchen as k, Userrole as r where ua.kitchenId = k.kitchenId and r.username = ua.username ";
			if (!"admin".equals(countyId)) {// 如果不是主管機關才加
				queryString += " and ua.username like :county ";
			}
			// 新增以table Userrole的createDate為反向排序(日期越晚新增的顯示在上面)
			queryString += " order by r.createDate desc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			if (!"admin".equals(countyId)) {// 如果不是主管機關才加
				queryObject.setParameter("county", countyId + "%");
			}
			log.debug("get successful");
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	/**
     * 使用帳戶名稱查詢
     * @param countyId county id
     * @param name name
     * @return an users list
     */
	public List getUseraccountAndKitchenNameByKitchenId2(String countyId, String name, String type) {
		try {
			String queryString = "SELECT ua.username,ua.usertype,ua.name,ua.email, k.kitchenName, r.roletype , r.createDate , ut.name , ua.enable "
					+ "FROM Userrole as r , Usertype ut, Useraccount as ua , Kitchen as k where "
					+ " ua.kitchenId = k.kitchenId and r.username = ua.username "
					+ " and CASE WHEN substring(ua.usertype,1,2) = '12' "
					+ " THEN concat(substring(ua.usertype,1,2),'#',substring(ua.usertype,4,6))  "
					//+ " THEN ua.usertype = ut.id "
					+ " ELSE ua.usertype "
					+ " END = ut.id";
					//+ " and ( case when Left(ua.usertype,2) = '12' then concat(left(ua.usertype,2),'#',right(ua.usertype,3)) = ut.id else ua.usertype = ut.id  end )"
					
			if(!CateringServiceUtil.isEmpty(name) && !CateringServiceUtil.isNull(name) && !"all".equals(name)){
				queryString += " and ua.name like :name ";
			}
			
			//增加判斷type
			if(!CateringServiceUtil.isEmpty(type) && !CateringServiceUtil.isNull(type) && !"all".equals(type)){
				queryString += " and ua.usertype like :type ";
			}
			if (!"admin".equals(countyId)) {// 如果不是主管機關才加 
				queryString += " and ua.username like :county ";
			}
			// 新增以table Userrole的createDate為反向排序(日期越晚新增的顯示在上面)
			queryString += " order by r.createDate desc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			
			if(!CateringServiceUtil.isEmpty(name) && !CateringServiceUtil.isNull(name) && !"all".equals(name)){
				queryObject.setParameter("name", "%" + name + "%");
			}
			if(!CateringServiceUtil.isEmpty(type) && !CateringServiceUtil.isNull(type) && !"all".equals(type)){
				queryObject.setParameter("type", "%" + type + "%");	
			}
			
			if (!"admin".equals(countyId)) {// 如果不是主管機關才加
				queryObject.setParameter("county", countyId + "%");
			}
			log.debug("get successful");
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * *從QueryUserList的API中呼叫這個功能，丟username來 可先寫完Query
	 * string，下方為Hibernate用的code，每個colunms and tables name都要大小寫相符
	 * 帶入的變數值username可以有兩種寫法，用:uname比較好 ,Q:why?
	 * 
	 * Query是Hibernate把給的Query string，由之前在QueryUserList中開的session去連DB去處理
	 * 回傳的格式為list
	 */
	public List getRoleruleByUsername(String username) {
		log.debug("get Rolerule by username");
		try {
			String queryString = "SELECT r.roletype, r.rolerule FROM Role as r, Userrole as ur";
			// queryString +=
			// " where ur.username like '"+username+"' and ur.roletype like r.roletype"
			// ;
			queryString += " where ur.username = :uname and ur.roletype = r.roletype";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("uname", username);
			log.debug("get successful");
			//
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List getRoletypeByUsername(String username) {
		log.debug("get Roletype by username");
		try {
			String queryString = "SELECT username, roletype FROM  Userrole";
			queryString += " where username like '" + username + "'";
			Query queryObject = getCurrentSession().createQuery(queryString);
			log.debug("get successful");
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Useraccount queryUseraccountByUsernameAndUsertype(String username,String usertype){
		String hql = "FROM Useraccount u WHERE u.username = :username AND u.usertype = :usertype";
		Query query = dbSession.createQuery(hql);
		query.setParameter("username", username);
		query.setParameter("usertype", usertype);
		if(query.list()!=null&&query.list().size()>0){
			return (Useraccount)query.list().get(0);
		}else{
			return null;
		}
	}
	
	public Useraccount queryUseraccountByUsername(String username){
		String hql = "FROM Useraccount u WHERE u.username = :username ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("username", username);
		if(query.list()!=null&&query.list().size()>0){
			return (Useraccount)query.list().get(0);
		}else{
			return null;
		}
	}
	
	public Userrole queryUserroleByUsername(String username){
		String hql = "FROM Userrole u WHERE u.username = :username";
		Query query = dbSession.createQuery(hql);
		query.setParameter("username", username);
		if(query.list()!=null&&query.list().size()>0){
			return (Userrole)query.list().get(0);
		}else{
			return null;
		}
	}
	
	public Useraccount queryUseraccountByUsernameAndEmail(String username,String email){
		String hql = "FROM Useraccount u WHERE u.username = :username AND u.email = :email";
		Query query = dbSession.createQuery(hql);
		query.setParameter("username", username);
		query.setParameter("email", email);
		if(query.list()!=null&&query.list().size()>0){
			return (Useraccount)query.list().get(0);
		}else{
			return null;
		}
	}
	
	public Useraccount updateUserPassword(Useraccount useraccount,String md5Password){
		useraccount.setPassword(md5Password);
		dbSession.saveOrUpdate(useraccount);
		return useraccount;
	}

	public List<Integer> querySchoolIdListByUsername(String username){
		log.debug("querySchoolByUsername username:" + username);
		
		String hql = "FROM Useraccount u WHERE u.username = :username ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("username", username);
		
		String usertype = "";
		Integer kitchenId = 0;
		List<Integer> result = new ArrayList<Integer>();
		List<String> sqlList = new ArrayList<String>();
		
		try{
			Useraccount user =(Useraccount)query.list().get(0);			
			usertype = user.getUsertype();
			kitchenId = user.getKitchenId();
			
			RestaurantDAO rd = new RestaurantDAO(dbSession);
			Restaurant r = rd.findById(12);
			
			if(CateringServiceCode.USERTYPE_COLLEGE_SCHOOL.contains(usertype)){
				//找其附屬的school
				if(usertype.equals("101")){
					hql = "select distinct c.schoolId " +
					  " from Useraccount a , Userrelation b , School c" +  
					  " where a.username = b.child   " +
					  " and a.username = :username  " +
					  " and b.authorizeUserType = '101' " +  
					  " and a.enable = 1  " +
					  " and b.authorizeTargetType = '101' " + 
					  " and b.authorizeTarget = c.schoolId ";
					sqlList.add(hql);
				}else if(usertype.equals("102")){
					hql = " select distinct c.schoolId " +
						"   from Useraccount a , Userrelation b , School c " +
						"   where a.username = b.child   " +
						"    and a.username = :username   " +
						"    and b.authorizeUserType = '102'   " +
						"    and a.enable = 1  " +
						"    and b.authorizeTargetType = '101'  " +
						"    and b.authorizeTarget = c.schoolId ";
//						"  union    " +
					sqlList.add(hql);
					hql = "  select distinct c.schoolId " +
						"   from Useraccount a , Userrelation b , School c , Schoolfoodstreet d   " +
						"   where a.username = b.child   " +
						"    and a.username = :username   " +
						"    and b.authorizeUserType = '102'   " +
						"    and a.enable = 1  " +
						"    and b.authorizeTargetType = '102'  " +
						"    and b.authorizeTarget = d.sfstreetId  " +
						"    and c.schoolId = d.schoolId " ;
					sqlList.add(hql);						
				}else if(usertype.equals("103")){
					hql = " select  distinct c.schoolId " +
						"   from Useraccount a , Userrelation b , School c , Schoolfoodstreet d , Restaurant e  " +
						"   where a.username = b.child   " +
						"    and a.username = :username    " +
						"    and b.authorizeUserType = '103'   " +
						"    and a.enable = 1  " +
						"    and b.authorizeTargetType = '101'  " +
						"    and b.authorizeTarget = c.schoolId    " +
						"    and c.schoolId = d.schoolId    " +
						"    and d.sfstreetId = e.sfstreetId  ";
					sqlList.add(hql);
//						"  union    " +
					hql = "  select  distinct c.schoolId " +
						"   from Useraccount a , Userrelation b , School c , Schoolfoodstreet d , Restaurant e  " +
						"   where a.username = b.child   " +
						"    and a.username = :username   " +
						"    and b.authorizeUserType = '103'   " +
						"    and a.enable = 1  " +
						"    and b.authorizeTargetType = '102'  " +
						"    and b.authorizeTarget = d.sfstreetId  " +
						"    and c.schoolId = d.schoolId  " +
						"    and d.sfstreetId = e.sfstreetId  ";
					sqlList.add(hql);
//					hql = "  union    " +
					hql = "  select  distinct c.schoolId " +
						"   from Useraccount a , Userrelation b , School c , Schoolfoodstreet d , Restaurant e  " +
						"   where a.username = b.child   " +
						"    and a.username = :username " +
						"    and b.authorizeUserType = '103'   " +
						"    and a.enable = 1  " +
						"    and b.authorizeTargetType = '103'  " +
						"    and b.authorizeTarget = e.restaurantId  " +
						"    and c.schoolId = d.schoolId    " +
						"    and d.sfstreetId = e.sfstreetId  ";
					sqlList.add(hql);
				}
				
				for(int i=0;i<sqlList.size();i++){
					query = dbSession.createQuery(sqlList.get(i));
					query.setParameter("username", username);
					
					result.addAll(query.list());
				}
				
			}else{
				//從kitchen去找school
				SchoolkitchenDAO skDao = new SchoolkitchenDAO(dbSession);
				result = skDao.querySchoolListByKitchen(kitchenId);				
			}
			
			return result; 
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}		
	}	
}
