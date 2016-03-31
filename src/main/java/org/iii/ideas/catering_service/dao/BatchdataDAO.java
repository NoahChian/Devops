package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * Batchdata entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Batchdata
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class BatchdataDAO {
	private static final Logger log = LoggerFactory
			.getLogger(BatchdataDAO.class);
	// property constants
	public static final String KITCHEN_ID = "kitchenId";
	public static final String SCHOOL_ID = "schoolId";
	public static final String MENU_DATE = "menuDate";
	public static final String MAIN_FOOD_ID = "mainFoodId";
	public static final String MAIN_FOOD1ID = "mainFood1id";
	public static final String MAIN_DISH_ID = "mainDishId";
	public static final String MAIN_DISH1ID = "mainDish1id";
	public static final String MAIN_DISH2ID = "mainDish2id";
	public static final String MAIN_DISH3ID = "mainDish3id";
	public static final String SUB_DISH1ID = "subDish1id";
	public static final String SUB_DISH2ID = "subDish2id";
	public static final String SUB_DISH3ID = "subDish3id";
	public static final String SUB_DISH4ID = "subDish4id";
	public static final String SUB_DISH5ID = "subDish5id";
	public static final String SUB_DISH6ID = "subDish6id";
	public static final String VEGETABLE_ID = "vegetableId";
	public static final String SOUP_ID = "soupId";
	public static final String DESSERT_ID = "dessertId";
	public static final String DESSERT1ID = "dessert1id";
	public static final String LOT_NUMBER = "lotNumber";
	public static final String TYPE_GRAINS = "typeGrains";
	public static final String TYPE_OIL = "typeOil";
	public static final String TYPE_VEGETABLE = "typeVegetable";
	public static final String TYPE_MILK = "typeMilk";
	public static final String TYPE_FRUIT = "typeFruit";
	public static final String TYPE_MEAT_BEANS = "typeMeatBeans";
	public static final String CALORIE = "calorie";
	public static final String SRCTYPE="srcType";
	public static final String MENUTYPE = "menuType";
	public static final String ENABLE = "enable";

	//2014/05/06 Raymond
	public BatchdataDAO(){}	
	public BatchdataDAO(Session dbSession){
		this.dbSession = dbSession;
	}
	
	private SessionFactory sessionFactory;
	private Session dbSession;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		try{
			return sessionFactory.getCurrentSession();		
		}catch(Exception ex){
			return sessionFactory.openSession();
		}

	}

	protected void initDao() {
		// do nothing
	}

	public void save(Batchdata transientInstance) {
		log.debug("saving Batchdata instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Batchdata persistentInstance) {
		log.debug("deleting Batchdata instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Batchdata findById(java.lang.Integer id) {
		log.debug("getting Batchdata instance with id: " + id);
		try {
			Batchdata instance = (Batchdata) getCurrentSession().get(
					"org.iii.ideas.catering_service.dao.Batchdata", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Batchdata instance) {
		log.debug("finding Batchdata instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.Batchdata")
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
		log.debug("finding Batchdata instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Batchdata as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySrcType(Object srcType) {
		return findByProperty(SRCTYPE, srcType);
	}
	
	public List findByKitchenId(Object kitchenId) {
		return findByProperty(KITCHEN_ID, kitchenId);
	}

	public List findBySchoolId(Object schoolId) {
		return findByProperty(SCHOOL_ID, schoolId);
	}

	public List findByMenuDate(Object menuDate) {
		return findByProperty(MENU_DATE, menuDate);
	}

	public List findByMainFoodId(Object mainFoodId) {
		return findByProperty(MAIN_FOOD_ID, mainFoodId);
	}

	public List findByMainFood1id(Object mainFood1id) {
		return findByProperty(MAIN_FOOD1ID, mainFood1id);
	}

	public List findByMainDishId(Object mainDishId) {
		return findByProperty(MAIN_DISH_ID, mainDishId);
	}

	public List findByMainDish1id(Object mainDish1id) {
		return findByProperty(MAIN_DISH1ID, mainDish1id);
	}

	public List findByMainDish2id(Object mainDish2id) {
		return findByProperty(MAIN_DISH2ID, mainDish2id);
	}

	public List findByMainDish3id(Object mainDish3id) {
		return findByProperty(MAIN_DISH3ID, mainDish3id);
	}

	public List findBySubDish1id(Object subDish1id) {
		return findByProperty(SUB_DISH1ID, subDish1id);
	}

	public List findBySubDish2id(Object subDish2id) {
		return findByProperty(SUB_DISH2ID, subDish2id);
	}

	public List findBySubDish3id(Object subDish3id) {
		return findByProperty(SUB_DISH3ID, subDish3id);
	}

	public List findBySubDish4id(Object subDish4id) {
		return findByProperty(SUB_DISH4ID, subDish4id);
	}

	public List findBySubDish5id(Object subDish5id) {
		return findByProperty(SUB_DISH5ID, subDish5id);
	}

	public List findBySubDish6id(Object subDish6id) {
		return findByProperty(SUB_DISH6ID, subDish6id);
	}

	public List findByVegetableId(Object vegetableId) {
		return findByProperty(VEGETABLE_ID, vegetableId);
	}

	public List findBySoupId(Object soupId) {
		return findByProperty(SOUP_ID, soupId);
	}

	public List findByDessertId(Object dessertId) {
		return findByProperty(DESSERT_ID, dessertId);
	}

	public List findByDessert1id(Object dessert1id) {
		return findByProperty(DESSERT1ID, dessert1id);
	}

	public List findByLotNumber(Object lotNumber) {
		return findByProperty(LOT_NUMBER, lotNumber);
	}

	public List findByTypeGrains(Object typeGrains) {
		return findByProperty(TYPE_GRAINS, typeGrains);
	}

	public List findByTypeOil(Object typeOil) {
		return findByProperty(TYPE_OIL, typeOil);
	}

	public List findByTypeVegetable(Object typeVegetable) {
		return findByProperty(TYPE_VEGETABLE, typeVegetable);
	}

	public List findByTypeMilk(Object typeMilk) {
		return findByProperty(TYPE_MILK, typeMilk);
	}

	public List findByTypeFruit(Object typeFruit) {
		return findByProperty(TYPE_FRUIT, typeFruit);
	}

	public List findByTypeMeatBeans(Object typeMeatBeans) {
		return findByProperty(TYPE_MEAT_BEANS, typeMeatBeans);
	}

	public List findByCalorie(Object calorie) {
		return findByProperty(CALORIE, calorie);
	}
	
	public List findByMenuType(Object menuType) {
		return findByProperty(MENUTYPE, menuType);
	}
	
	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findAll() {
		log.debug("finding all Batchdata instances");
		try {
			String queryString = "from Batchdata";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Batchdata merge(Batchdata detachedInstance) {
		log.debug("merging Batchdata instance");
		try {
			Batchdata result = (Batchdata) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Batchdata instance) {
		log.debug("attaching dirty Batchdata instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Batchdata instance) {
		log.debug("attaching clean Batchdata instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

//	public static BatchdataDAO getFromApplicationContext(ApplicationContext ctx) {
//		return (BatchdataDAO) ctx.getBean("BatchdataDAO");
//	}

	public boolean isDishIdExists(Integer kitchenId, Long dishId){
		String hql ="select DishBatchDataId from DishBatchData where DishId=:dishId";
		/*
		String hql="select batchDataId from Batchdata where "
				+ " kitchenId=:kitchenId  and ("
				+ " mainFoodId=:dishId or "
				+ " mainFood1id=:dishId or "
				+ " mainDishId=:dishId or "
				+ " mainDish1id =:dishId or "
				+ " mainDish2id = :dishId or "
				+ " mainDish3id = :dishId or "
				+ " subDish1id = :dishId or "
				+ " subDish2id = :dishId or "
				+ " subDish3id = :dishId or "
				+ " subDish4id = :dishId or "
				+ " subDish5id = :dishId or "
				+ " subDish6id = :dishId or "
				+ " vegetableId = :dishId or "
				+ " soupId = :dishId or "
				+ " dessertId = :dishId or "
				+ " dessert1Id =:dishId )";*/
		
		Query queryObj=getCurrentSession().createQuery(hql);
		queryObj.setParameter("dishId", dishId);
		//queryObj.setParameter("kitchenId",kitchenId);
		queryObj.setMaxResults(1);
		Long result=(Long) queryObj.uniqueResult();
		if (result==null  ){
			return false;
		}else{
			return true;
		}

	}
	
	/**
	 * 2014/05/06 Raymond
	 * 查詢廚房 Batchdata 數量
	 * @param kitchenId
	 * @returnInteger
	 */
	public Integer queryBatchdataCount(Integer kitchenId){
		String hql = "SELECT COUNT(*) FROM Batchdata WHERE kitchenId = :kitchenId ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		if(query.uniqueResult()!=null)
			return ((Number)query.uniqueResult()).intValue();
		else
			return 0;
	}

	/**
	 * 2014/05/08
	 * 查詢Batchdata list
	 * @param kitchenId
	 * @param menuDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Batchdata> queryBatchdataList(Integer kitchenId,String menuDate){
		String hql = "from  Batchdata b  where b.kitchenId=:kitchenId and b.menuDate = :menuDate and b.menuType = :menuType and b.enable = :enable ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		query.setParameter("menuDate", menuDate);
		query.setParameter("menuType", 1);
		query.setParameter("enable", 1);
		if(query.list()!=null && query.list().size()>0){
			return (List<Batchdata>)query.list();
		}else{
			return null;
		}
	}
	
	/**
	 * 查詢Batchdata list 
	 * @param kitchenId
	 * @param menuDate
	 * @param schoolId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Batchdata> queryBatchdataList(Integer kitchenId,String menuDate,Integer schoolId){
		String hql = "from  Batchdata b  where b.kitchenId=:kitchenId and b.menuDate = :menuDate and b.schoolId= :schoolId and b.menuType = :menuType and b.enable = :enable ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		query.setParameter("menuDate", menuDate);
		query.setParameter("schoolId", schoolId);
		query.setParameter("menuType", 1);
		query.setParameter("enable", 1);
		if(query.list()!=null && query.list().size()>0){
			return (List<Batchdata>)query.list();
		}else{
			return null;
		}
	}
	
	public List<Long> queryBatchdataIdListbyPeriod(Integer kitchenId,String startdate,String enddate){
		
		String hql = "Select batchDataId from  Batchdata b  where b.kitchenId=:kitchenId and (b.menuDate between :startdate and :enddate) ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		query.setParameter("startdate", startdate);
		query.setParameter("enddate", enddate);
		if(query.list()!=null && query.list().size()>0){
			return (List<Long>)query.list();
		}else{
			return null;
		}
	}
	
	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}
	
	public String queryLastMenuDateByKidAndSid(Integer kitchenId,Integer schoolId){
		String hql = "select menuDate from Batchdata b  where b.kitchenId=:kitchenId and b.schoolId= :schoolId and b.menuType = :menuType and b.enable = :enable "
				+ " order by b.menuDate desc";
		Query query = dbSession.createQuery(hql);
		query.setParameter("kitchenId", kitchenId);
		query.setParameter("schoolId", schoolId);
		query.setParameter("menuType", 1);
		query.setParameter("enable", 1);
		if(query.list()!=null && query.list().size()>0){
			return (String) query.list().get(0);
		}else{
			return "無供餐紀錄";
		}
	}
}
