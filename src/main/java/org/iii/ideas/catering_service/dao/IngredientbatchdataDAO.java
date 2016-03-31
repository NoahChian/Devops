package org.iii.ideas.catering_service.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.text.rtf.RTFEditorKit;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * Ingredientbatchdata entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Ingredientbatchdata
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class IngredientbatchdataDAO {
	private static final Logger log = LoggerFactory.getLogger(IngredientbatchdataDAO.class);
	// property constants
	public static final String BATCH_DATA_ID = "batchDataId";
	public static final String DISH_ID = "dishId";
	public static final String INGREDIENT_ID = "ingredientId";
	public static final String INGREDIENT_NAME = "ingredientName";
	public static final String LOT_NUMBER = "lotNumber";
	public static final String BRAND = "brand";
	public static final String ORIGIN = "origin";
	public static final String SOURCE = "source";
	public static final String SUPPLIER_ID = "supplierId";
	public static final String SOURCE_CERTIFICATION = "sourceCertification";
	public static final String CERTIFICATION_ID = "certificationId";
	public static final String MENU_ID = "menuId";
	public static final String SUPPLIER_COMPANY_ID = "supplierCompanyId";
	public static final String SUPPLIER_NAME = "supplierName";
	public static final String BRAND_NAME = "brandNo";

	public IngredientbatchdataDAO(){
		
	}
	
	public IngredientbatchdataDAO(Session dbSession){
		this.dbSession = dbSession;
	}
	
	private Session dbSession;
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public void save(Ingredientbatchdata transientInstance) {
		log.debug("saving Ingredientbatchdata instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Ingredientbatchdata persistentInstance) {
		log.debug("deleting Ingredientbatchdata instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Ingredientbatchdata findById(java.lang.Integer id) {
		log.debug("getting Ingredientbatchdata instance with id: " + id);
		try {
			Ingredientbatchdata instance = (Ingredientbatchdata) getCurrentSession().get("org.iii.ideas.catering_service.dao.Ingredientbatchdata", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Ingredientbatchdata> findByIdAndBatchDataId(java.lang.Integer ingredientId, java.lang.Integer batchDataId) {
		log.debug("getting Ingredientbatchdata instance with ingredientId: " + ingredientId + " and batchDataId =: " + batchDataId);
		try {
			String queryString = "from Ingredientbatchdata as model where model.ingredientId= :ingredientId and model.batchDataId = :batchDataId";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("ingredientId", ingredientId);
			queryObject.setParameter("batchDataId", batchDataId);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by ingredientId and batchDataId failed", re);
			throw re;
		}
	}
	
	public int queryIentbatchdataCount(java.lang.Integer ingredientId, java.lang.Integer batchDataId){
		log.debug("getting Ingredientbatchdata count with ingredientId: " + ingredientId + " and batchDataId =: " + batchDataId);
		try {
			String queryString = "select count(*) from Ingredientbatchdata as model where model.ingredientId= :ingredientId and model.batchDataId = :batchDataId";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("ingredientId", ingredientId);
			queryObject.setParameter("batchDataId", batchDataId);
			int result = ((Number)queryObject.uniqueResult()).intValue();
			return result;
		} catch (RuntimeException re) {
			log.error("find by ingredientId and batchDataId failed", re);
			throw re;
		}
	}
	//查詢ingredinetBatchdata 是否有重覆資料
	public int queryIentbatchdataUk(
			java.lang.Integer batchDataId,
			java.lang.Integer ingredientId, 
			java.lang.Long dishId,
			java.lang.String stockDate,  //null
			java.lang.String manufactureDate, //null
			java.lang.String expirationDate, //null
			java.lang.String logNumber,
			java.lang.String supplierCompanyId
			){
		log.debug("getting queryIentbatchdataUk count with ingredientId: " + ingredientId + " and batchDataId =: " + batchDataId);
		try {
			String queryString = "select count(*) from Ingredientbatchdata as model where"
					+ "     model.ingredientId= :ingredientId "
					+ " and model.batchDataId = :batchDataId "
					+ " and model.dishId = :dishId "
					+ " and model.stockDate = :stockDate "
					+ " and model.manufactureDate = :manufactureDate "
					+ " and model.expirationDate = :expirationDate "
					+ " and model.logNumber = :logNumber "
					+ " and model.supplierCompanyId = :supplierCompanyId ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("ingredientId", ingredientId);
			queryObject.setParameter("batchDataId", batchDataId);
			queryObject.setParameter("dishId", dishId);
			queryObject.setParameter("stockDate", stockDate);
			queryObject.setParameter("manufactureDate", manufactureDate);
			queryObject.setParameter("expirationDate", expirationDate);
			queryObject.setParameter("logNumber", logNumber);
			queryObject.setParameter("supplierCompanyId", supplierCompanyId);
			int result = ((Number)queryObject.uniqueResult()).intValue();
			return result;
		} catch (RuntimeException re) {
			log.error("find by queryIentbatchdataUk failed", re);
			throw re;
		}
	}

	public List findByExample(Ingredientbatchdata instance) {
		log.debug("finding Ingredientbatchdata instance by example");
		try {
			List results = getCurrentSession().createCriteria("org.iii.ideas.catering_service.dao.Ingredientbatchdata").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Ingredientbatchdata instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from Ingredientbatchdata as model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByBatchDataId(Object batchDataId) {
		return findByProperty(BATCH_DATA_ID, batchDataId);
	}

	public List findByDishId(Object dishId) {
		return findByProperty(DISH_ID, dishId);
	}

	public List findByIngredientId(Object ingredientId) {
		return findByProperty(INGREDIENT_ID, ingredientId);
	}

	public List findByIngredientName(Object ingredientName) {
		return findByProperty(INGREDIENT_NAME, ingredientName);
	}

	public List findByLotNumber(Object lotNumber) {
		return findByProperty(LOT_NUMBER, lotNumber);
	}

	public List findByBrand(Object brand) {
		return findByProperty(BRAND, brand);
	}

	public List findByOrigin(Object origin) {
		return findByProperty(ORIGIN, origin);
	}

	public List findBySource(Object source) {
		return findByProperty(SOURCE, source);
	}

	public List findBySupplierId(Object supplierId) {
		return findByProperty(SUPPLIER_ID, supplierId);
	}

	public List findBySourceCertification(Object sourceCertification) {
		return findByProperty(SOURCE_CERTIFICATION, sourceCertification);
	}

	public List findByCertificationId(Object certificationId) {
		return findByProperty(CERTIFICATION_ID, certificationId);
	}

	public List findByMenuId(Object menuId) {
		return findByProperty(MENU_ID, menuId);
	}

	public List findAll() {
		log.debug("finding all Ingredientbatchdata instances");
		try {
			String queryString = "from Ingredientbatchdata";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Ingredientbatchdata merge(Ingredientbatchdata detachedInstance) {
		log.debug("merging Ingredientbatchdata instance");
		try {
			Ingredientbatchdata result = (Ingredientbatchdata) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Ingredientbatchdata instance) {
		log.debug("attaching dirty Ingredientbatchdata instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Ingredientbatchdata instance) {
		log.debug("attaching clean Ingredientbatchdata instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	
	public Ingredientbatchdata queryIngredientbatchdata(Integer batchDataId,Long dishId,Integer ingredientId,String stockDate,String lotNumber) throws HibernateException, ParseException{
		String hql = "FROM Ingredientbatchdata a "
				+ "WHERE a.dishId = :dishId "
				+ "AND a.batchDataId = :batchDataId "
				+ "AND a.ingredientId = :ingredientId "
				+ "AND a.stockDate = :stockDate "
				+ "AND a.lotNumber = :lotNumber";
		Query query = dbSession.createQuery(hql);
		query.setParameter("batchDataId", batchDataId);
		query.setParameter("dishId", dishId);
		query.setParameter("ingredientId", ingredientId);
		query.setParameter("stockDate", HibernateUtil.convertStrToTimestamp("yyyy/MM/dd",stockDate));
		query.setParameter("lotNumber", lotNumber);
		if(query.list()!=null && query.list().size()>0){
			return (Ingredientbatchdata)query.list().get(0);
		}
		else{
			return null;
		}
	}
	
	public Ingredientbatchdata queryIngredientbatchdataUK(Long batchDataId,Long dishId,Long ingredientId,String stockDate,String lotNumber,String supplierCompanyId,String manufactureDate,String expirationDate ) throws HibernateException, ParseException{
		String hql = "FROM Ingredientbatchdata a "
				+ "WHERE a.dishId = :dishId "
				+ "AND a.batchDataId = :batchDataId "
				+ "AND a.ingredientId = :ingredientId "
				+ "AND a.lotNumber = :lotNumber "
				+ "AND a.supplierCompanyId = :supplierCompanyId ";
				
		
		//判斷是否為空值
		if(CateringServiceUtil.isEmpty(manufactureDate))
			hql += "AND a.manufactureDate IS NULL ";
		else
			hql += "AND a.manufactureDate = :manufactureDate ";
		
		if(CateringServiceUtil.isEmpty(expirationDate))
			hql += "AND a.expirationDate IS NULL ";
		else
			hql += "AND a.expirationDate = :expirationDate ";
		
		if(CateringServiceUtil.isEmpty(stockDate))
			hql += "AND a.stockDate IS NULL ";
		else
			hql += "AND a.stockDate = :stockDate ";
		
		Query query = dbSession.createQuery(hql);
		query.setParameter("batchDataId", batchDataId);
		query.setParameter("dishId", dishId);
		query.setParameter("ingredientId", ingredientId);
		query.setParameter("lotNumber", lotNumber);
		query.setParameter("supplierCompanyId", supplierCompanyId);
		if(!CateringServiceUtil.isEmpty(stockDate))
			query.setParameter("stockDate", HibernateUtil.convertStrToTimestamp("yyyy/MM/dd",stockDate));
		if(!CateringServiceUtil.isEmpty(manufactureDate))
			query.setParameter("manufactureDate", HibernateUtil.convertStrToTimestamp("yyyy/MM/dd",manufactureDate));
		if(!CateringServiceUtil.isEmpty(expirationDate))
			query.setParameter("expirationDate", HibernateUtil.convertStrToTimestamp("yyyy/MM/dd",expirationDate));
		if(query.list()!=null && query.list().size()>0){
			return (Ingredientbatchdata)query.list().get(0);
		}
		else{
			return null;
		}
	}
	
	public Ingredientbatchdata queryIngredientbatchdata(Integer id){
		String hql = "FROM Ingredientbatchdata a "
				+ "WHERE a.ingredientBatchId :ingredientBatchId ";
		Query query = dbSession.createQuery(hql);
		query.setParameter("ingredientBatchId", id);
		if(query.list()!=null && query.list().size()>0){
			return (Ingredientbatchdata)query.list().get(0);
		}
		else{
			return null;
		}
	}
	
	//查詢食材編輯頁面菜色的所有食材資訊
	@SuppressWarnings("unchecked")
	public List<Ingredientbatchdata> queryModifyIngredientbatchdataList(Long dishId,List<Integer> batchdataIdList){
		String hql = "FROM Ingredientbatchdata a WHERE a.batchDataId in (:batchdataIdList) "
				+ "AND a.dishId = :dishId "
				+ "GROUP BY a.ingredientId,a.stockDate,a.lotNumber,a.supplierId ";
		Query query = dbSession.createQuery(hql);
		query.setParameterList("batchdataIdList", batchdataIdList);
		query.setParameter("dishId", dishId);
		return (List<Ingredientbatchdata>)query.list();
	}
	
	/**
	 * 查詢廚房當天所有食材列表
	 * @param kitchenId
	 * @param menuDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>>queryIngredientPropertyList(Integer kitchenId,String menuDate){
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT distinct new map( ");
//			hql.append("a.batchDataId as batchDataId, ");
			hql.append("a.kitchenId as kitchenId, ");
//			hql.append("a.schoolId as schoolId, ");
			hql.append("a.menuDate as menuDate, ");
			hql.append("b.dishId as dishId, ");
			hql.append("b.dishName as dishName, ");
			hql.append("c.ingredientName as ingredientName, ");
			hql.append("c.supplierCompanyId as supplierCompanyId, ");
			hql.append("c.supplierName as supplierName, ");
			hql.append("c.supplierId as supplierId, ");
			hql.append("c.stockDate as stockDate, ");
			hql.append("c.lotNumber as lotNumber, ");
			hql.append("c.ingredientId as ingredientId, ");
			hql.append("c.ingredientAttr as ingredientAttr, ");
			hql.append("c.ingredientBatchId as ingredientBatchId )");
		hql.append("FROM  ");
			hql.append("Batchdata a, ");
			hql.append("Dish b, ");
			hql.append("Ingredientbatchdata c ");
		hql.append("WHERE ");
			hql.append("a.batchDataId = c.batchDataId ");
			hql.append("and b.dishId = c.dishId ");
			hql.append("and a.batchDataId = c.batchDataId ");
			hql.append("and a.kitchenId = :kitchenId ");
			hql.append("and a.menuDate = :menuDate ");
			//調味料進貨日期非必填，所以要判斷調味料進貨日期為空也要顯示  20140917 KC
			//hql.append("and c.stockDate is not null ");
			hql.append("and (( c.stockDate is null and b.dishName= :seasoningName ) or  (c.stockDate is not null) )");
		hql.append("GROUP BY  c.ingredientId ");
		hql.append("ORDER BY  b.dishName ASC ");
			
			Query query = dbSession.createQuery(hql.toString());
			query.setParameter("kitchenId", kitchenId);
			query.setParameter("menuDate", menuDate);
			query.setParameter ("seasoningName" ,CateringServiceUtil.ColumnNameOfSeasoning);
		
		return (List<Map<String,Object>>)query.list();
	}
	
	/**
	 * 查出所有有用到此食材的ingredientbatchdata
	 * @param kitchenId
	 * @param menuDate
	 * @param ingredientId
	 * @param supplierCompanyId
	 * @param lotNumber
	 * @param stockDate
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public List<Long>queryIngredientbatchIdList(Integer kitchenId,String menuDate,Long ingredientId,String supplierCompanyId,String lotNumber,String stockDate) throws ParseException{
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT distinct c.ingredientBatchId ");
		hql.append("FROM  ");
			hql.append("Batchdata a, ");
//			hql.append("Dish b, ");
			hql.append("Ingredientbatchdata c ");
		hql.append("WHERE ");
			hql.append("a.batchDataId = c.batchDataId ");
//			hql.append("and b.dishId = c.dishId ");
			hql.append("and a.batchDataId = c.batchDataId ");
			hql.append("and a.kitchenId = :kitchenId ");
			hql.append("and a.menuDate = :menuDate ");			
			hql.append("and c.ingredientId = :ingredientId ");
			hql.append("and c.supplierCompanyId = :supplierCompanyId ");
			hql.append("and c.lotNumber = :lotNumber ");
			//20140918 Raymond 目前調味料進貨日期可為空值 預防進貨日期為空查不到資料的狀況 前端會先塞空白字串進來 這邊要做Trim處理
			if(!CateringServiceUtil.isEmpty(stockDate.trim()))
				hql.append("and c.stockDate = :stockDate ");
			else
				hql.append("and c.stockDate is null ");
			
			Query query = dbSession.createQuery(hql.toString());
			query.setParameter("kitchenId", kitchenId);
			query.setParameter("menuDate",menuDate);
			query.setParameter("ingredientId", ingredientId);
			query.setParameter("supplierCompanyId", supplierCompanyId);
			query.setParameter("lotNumber", lotNumber);
			//20140918 Raymond 目前調味料進貨日期可為空值 預防進貨日期為空查不到資料的狀況 前端會先塞空白字串進來 這邊要做Trim處理
			if(!CateringServiceUtil.isEmpty(stockDate.trim()))
				query.setParameter("stockDate", HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", stockDate));
						
		
		return (List<Long>)query.list();
	}
	
	public int updateIngredientAttr(List<Long> ingredientBatchDataIdList,int attr){
		String hql = "UPDATE Ingredientbatchdata SET ingredientAttr = :ingredientAttr WHERE ingredientBatchId IN :ingredientBatchDataIdList";
		Query query = dbSession.createQuery(hql);
		query.setParameter("ingredientAttr", attr);
		query.setParameterList("ingredientBatchDataIdList", ingredientBatchDataIdList);
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> queryIngredientBatchdataBySupplierId(int kitchenId,int supplierId){
		List<Long> list = new ArrayList<Long>();
		String hql = "SELECT b.ingredientBatchId FROM Batchdata a ,Ingredientbatchdata b WHERE a.batchDataId = b.batchDataId "
				+ "AND a.kitchenId = :kitchenId "
				+ "AND b.supplierId = :supplierId ";
		
		Query query = dbSession.createQuery(hql);
		query.setParameter("supplierId", supplierId);
		query.setParameter("kitchenId", kitchenId);
		list = (List<Long>)query.list();
		return list;
	}
	
	public String getTotalIngredientQuantity(List<Long>BatchdataIdList,Long dishId,Long ingredientId){
		String hql = "SELECT SUM(COALESCE(i.ingredientQuantity, 0)) FROM Ingredientbatchdata i "
				+ "WHERE i.batchDataId IN (:batchDataIdList) "
				+ "AND i.dishId = :dishId "
				+ "AND i.ingredientId = :ingredientId";
		Query query = dbSession.createQuery(hql);
		query.setParameter("dishId", dishId);
		query.setParameter("ingredientId", ingredientId);
		query.setParameterList("batchDataIdList", BatchdataIdList);
		String tmpQty = (String) query.uniqueResult();
		float quantity = 0;
		if(tmpQty!=null)
			quantity = Float.valueOf(tmpQty);
		return tmpQty;  // Joshua edit 2014/10/17 直接回傳string 資料
	}

	public List<Ingredientbatchdata> queryIngredientBatchdataListByIngredientId(Long dishId,Long ingredientId){
		Criteria criteria = dbSession.createCriteria(Ingredientbatchdata.class);
		criteria.add(Restrictions.eq("dishId", dishId));
		criteria.add(Restrictions.eq("ingredientId", ingredientId));
		
//		String hql = "FROM Ingredientbatchdata a "
//				+ "WHERE a.ingredientId = :ingredientId "
//				+ "AND a.dishId = :dishId";	
//		
//		Query query = dbSession.createQuery(hql);
//		query.setParameter("ingredientId", ingredientId);
//		query.setParameter("dishId", dishId);
		if(criteria.list()!=null && criteria.list().size()>0){
			return criteria.list();
		}
//		if(query.list()!=null && query.list().size()>0){
//			return query.list();
//		}
		else{
			return null;
		}
	}
	
	public static IngredientbatchdataDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IngredientbatchdataDAO) ctx.getBean("IngredientbatchdataDAO");
	}

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}
}
