package org.iii.ideas.catering_service.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for Dish
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Dish
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class DishDAO {
	private static final Logger log = LoggerFactory.getLogger(DishDAO.class);
	// property constants
	public static final String KITCHEN_ID = "kitchenId";
	public static final String DISH_NAME = "dishName";
	public static final String PICTURE_PATH = "picturePath";

	public DishDAO() {

	}

	public DishDAO(Session dbSession) {
		this.dbSession = dbSession;
	}

	private SessionFactory sessionFactory;

	private Session dbSession;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	/*
	 * public void save(Dish transientInstance) {
	 * log.debug("saving Dish instance"); try {
	 * getCurrentSession().save(transientInstance);
	 * log.debug("save successful"); } catch (RuntimeException re) {
	 * log.error("save failed", re); throw re; } }
	 */
	public Long save(Dish transientInstance) {
		log.debug("saving Dish instance");

		Long sn  ;
		try {
			sn = (Long) getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}

		return sn;
	}

	public void delete(Dish persistentInstance) {
		log.debug("deleting Dish instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Dish findById(Long dishId) {
		log.debug("getting Dish instance with id: " + dishId);
		try {
			Dish instance = (Dish) getCurrentSession().get("org.iii.ideas.catering_service.dao.Dish", dishId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Dish instance) {
		log.debug("finding Dish instance by example");
		try {
			List results = getCurrentSession().createCriteria("org.iii.ideas.catering_service.dao.Dish").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Dish instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from Dish as model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByKitchenId(Object kitchenId) {
		return findByProperty(KITCHEN_ID, kitchenId);
	}

	public List findByDishName(Object dishName) {
		return findByProperty(DISH_NAME, dishName);
	}

	public List findByPicturePath(Object picturePath) {
		return findByProperty(PICTURE_PATH, picturePath);
	}

	public List findAll() {
		log.debug("finding all Dish instances");
		try {
			String queryString = "from Dish";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Dish merge(Dish detachedInstance) {
		log.debug("merging Dish instance");
		try {
			Dish result = (Dish) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Dish instance) {
		log.debug("attaching dirty Dish instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Dish instance) {
		log.debug("attaching clean Dish instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static DishDAO getFromApplicationContext(ApplicationContext ctx) {
		return (DishDAO) ctx.getBean("DishDAO");
	}

	public HashMap dishManagerIndex(String kitchenid, HttpServletRequest request, String goPage, int startCount, int limitCount) {
		HashMap<String, Object> dishMap = new HashMap<String, Object>();

		HttpSession session = request.getSession();
		Criteria dishCriteria = getCurrentSession().createCriteria(Dish.class);
		dishCriteria.add(Restrictions.eq(KITCHEN_ID, Integer.parseInt(kitchenid)));
		// /////////////////////////
		if (session.getAttribute("searchDishName") != null) {
			String dishName = session.getAttribute("searchDishName").toString();
			dishCriteria.add(Restrictions.sqlRestriction(" dishName like ?", "%" + dishName + "%", StandardBasicTypes.STRING));
		}
		if (session.getAttribute("searchIngredientName") != null) {
			String ingrediantName = session.getAttribute("searchIngredientName").toString();
			// String sn = "";
			Criteria ingredientCriteria = getCurrentSession().createCriteria(Ingredient.class);
			ingredientCriteria.add(Restrictions.sqlRestriction(" IngredientName like ?", "%" + ingrediantName + "%", StandardBasicTypes.STRING));
			List<Ingredient> allIngredientList = (List<Ingredient>) ingredientCriteria.list();
			List<Long> dishIdArray = new ArrayList<Long>();
			for (int i = 0; i < allIngredientList.size(); i++) {
				dishIdArray.add(allIngredientList.get(i).getDishId());
				// sn += allIngredientList.get(i).getDishId();
				// sn += ",";
			}

			// if(sn.length() > 1)
			// sn=sn.substring(0, sn.length()-1);
			if (dishIdArray.size() > 0) {
				dishCriteria.add(Restrictions.in("dishId", dishIdArray));
			}
		}
		// /////////////////////////////

		// 改回傳總筆數回去，只是算個總筆數，用不著回傳全部資料!!!!! 20140323 KC
		// List<Dish> alldishList = (List<Dish>)dishCriteria.list();
		long allDishCount = (long) dishCriteria.setProjection(Projections.rowCount()).uniqueResult();
		dishCriteria.setProjection(null); // 清空剛剛的count row
		// where limit (分頁)
		String dishPage = (String) session.getAttribute("dishPage");
		if (request.getParameter("searchPage") != null) {
			goPage = request.getParameter("searchPage");
			session.setAttribute("dishPage", goPage);
			if (!goPage.equals("1"))
				startCount = (Integer.parseInt(goPage) - 1) * limitCount;

			dishCriteria.setFirstResult(startCount).setMaxResults(limitCount);
		} else {
			// if(dishPage != null && dishPage != ""){
			if (dishPage != null && !dishPage.isEmpty()) { // 修正空字串判斷 20140323
															// KC
				goPage = dishPage;
				if (!goPage.equals("1"))
					startCount = (Integer.parseInt(goPage) - 1) * limitCount;
				dishCriteria.setFirstResult(startCount).setMaxResults(limitCount);
			} else {
				dishCriteria.setFirstResult(0).setMaxResults(limitCount);
			}
		}
		dishCriteria.addOrder(Order.asc(DISH_NAME));
		System.out.println("=======================================================");
		List<Dish> dishList = dishCriteria.list();
		System.out.println("********************************************************");
		for (Dish element : dishList) {

			try {
				// add by Joshua 2014/10/21
				CateringServiceUtil cateringServiceUtil = new CateringServiceUtil(dbSession);
				// --dennis-- 20140206 新增補 PicturePath內容
				boolean fileExists = cateringServiceUtil.isDishImageFileNameExists(element.getKitchenId(), element.getDishId());
				if (fileExists) {
					element.setPicturePath(element.getDishId().toString());
				} else {
					element.setPicturePath("");
				}
				this.update(element);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(element);
		}

		// dishMap.put("alldishList", alldishList);
		dishMap.put("alldishList", allDishCount);
		dishMap.put("goPage", goPage);
		dishMap.put("dishList", dishList);
		return dishMap;
	}

	// 菜色抓資料 (20140323 重寫 by KC)
	public HashMap findDishAndIngredientByReq(String kitchenid, String searchDish, String searchIngredient, String goPage, String searchPage, int startCount, int limitCount) {
		HashMap<String, Object> dishMap = new HashMap<String, Object>();
		String q_dishName = "%";
		String q_ingredientName = "%";
		String hql_ingredientCond = "";
		String hql_dishCond = "";

		if (!searchDish.isEmpty()) {
			q_dishName = "%" + searchDish + "%";
			hql_dishCond = " and d.dishName like :p_dishName ";
		}

		if (!searchIngredient.isEmpty()) {
			q_ingredientName = "%" + searchIngredient + "%";
			hql_ingredientCond = " and i.ingredientName like :i_ingredientName ";
		}

		// 修正食材名稱會帶入null,現在為null時會帶入空字串 20140224 Raymond
		String hql = "select d.dishId,d.dishName,d.picturePath,group_concat(ifnull(i.ingredientName,'')) " + " from Dish d left join d.ingredient i " + " where d.kitchenId=:kitchenId " + hql_dishCond + hql_ingredientCond + " group by d.dishId order by d.dishId desc ";
		String hql_count = "";
		if (!searchIngredient.isEmpty()) {
			hql_count = " select count(distinct d.dishId)" + " from Dish d left join d.ingredient i " + " where d.kitchenId=:kitchenId  " + hql_ingredientCond + hql_dishCond;
		} else {
			hql_count = " select count( d.dishId)" + " from Dish d " + " where d.kitchenId=:kitchenId  " + hql_dishCond;
		}
		System.out.println(hql_count);

		Query queryCountObj = getCurrentSession().createQuery(hql_count);
		if (!searchDish.isEmpty()) {
			queryCountObj.setParameter("p_dishName", q_dishName);
		}

		if (!searchIngredient.isEmpty()) {
			queryCountObj.setParameter("i_ingredientName", q_ingredientName);
		}

		queryCountObj.setParameter("kitchenId", Integer.valueOf(kitchenid));

		Integer allDishCount = ((Number) queryCountObj.uniqueResult()).intValue();
		System.out.println("%%%%" + allDishCount.toString());

		// 頁數限制處理
		// String dishPage = (String)session.getAttribute("dishPage");

		Query queryObj = getCurrentSession().createQuery(hql);
		queryObj.setParameter("kitchenId", Integer.valueOf(kitchenid));
		if (!searchDish.isEmpty()) {
			queryObj.setParameter("p_dishName", q_dishName);
		}

		if (!searchIngredient.isEmpty()) {
			queryObj.setParameter("i_ingredientName", q_ingredientName);
		}

		Integer intPage = 1;
		try {
			intPage = Integer.parseInt(goPage);
		} catch (Exception e) {
		}

		/*
		 * if (intPage>1){ startCount = (Integer.parseInt(goPage)-1) *
		 * limitCount;
		 * queryObj.setFirstResult(startCount).setMaxResults(limitCount); }else{
		 * queryObj.setFirstResult(0).setMaxResults(limitCount); }
		 */
		// ----
		// where limit (分頁)
		if (searchPage != null && !searchPage.isEmpty()) {
			intPage = Integer.parseInt(searchPage);

			if (!searchPage.equals("1"))
				startCount = (intPage - 1) * limitCount;

			queryObj.setFirstResult(startCount).setMaxResults(limitCount);
		} else {
			queryObj.setFirstResult(0).setMaxResults(limitCount);
		}
		// ----
		List<Object[]> dishList = (List<Object[]>) queryObj.list();

		for (Object[] element : dishList) {
			// dishId,dishName,picturePath,group_concat(ingredientName) as iName
			try {
				// add by Joshua 2014/10/21
				CateringServiceUtil cateringServiceUtil = new CateringServiceUtil(getCurrentSession());
				boolean fileExists = cateringServiceUtil.isDishImageFileNameExists(Integer.valueOf(kitchenid), (Long) element[0]);
				if (fileExists) {
					element[2] = element[0].toString();
				} else {
					element[2] = "";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(element);
		}

		// dishMap.put("alldishList", alldishList);
		dishMap.put("alldishList", allDishCount);
		dishMap.put("goPage", goPage);
		dishMap.put("dishList", dishList);
		return dishMap;
	}

	public void update(Dish transientInstance) {
		log.debug("updating Dish instance");
		try {
			getCurrentSession().update(transientInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
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

	public List executeQuery(String strSql) {

		List results = null;

		results = getCurrentSession().createSQLQuery(strSql).list();

		return results;

	}

	public HashMap dishManagerSearch(String kitchenid, String dishName, String ingrediantName, HttpServletRequest request, String goPage, int startCount, int limitCount) {
		HashMap<String, Object> dishMap = new HashMap<String, Object>();

		Criteria dishCriteria = getCurrentSession().createCriteria(Dish.class);
		dishCriteria.add(Restrictions.eq(KITCHEN_ID, Integer.parseInt(kitchenid)));

		if (!dishName.equals(""))
			dishCriteria.add(Restrictions.sqlRestriction(" dishName like ?", "%" + dishName + "%", StandardBasicTypes.STRING));
			dishCriteria.addOrder(Order.desc("dishId"));

		if (!ingrediantName.equals("")) {
			// String sn = "";
			Criteria ingredientCriteria = getCurrentSession().createCriteria(Ingredient.class);
			ingredientCriteria.add(Restrictions.sqlRestriction(" IngredientName like ?", "%" + ingrediantName + "%", StandardBasicTypes.STRING));
			List<Ingredient> allIngredientList = (List<Ingredient>) ingredientCriteria.list();
			List<Long> dishIdArray = new ArrayList<Long>();
			for (int i = 0; i < allIngredientList.size(); i++) {
				dishIdArray.add(allIngredientList.get(i).getDishId());
				// sn += allIngredientList.get(i).getDishId();
				// sn += ",";
			}

			// if(sn.length() > 1)
			// sn=sn.substring(0, sn.length()-1);
			if (dishIdArray.size() > 0) {
				dishCriteria.add(Restrictions.in("dishId", dishIdArray));
			}
		}

		List<Dish> alldishList = (List<Dish>) dishCriteria.list();

		// where limit (分頁)
		if (request.getParameter("searchPage") != null) {
			goPage = request.getParameter("searchPage");

			if (!goPage.equals("1"))
				startCount = (Integer.parseInt(goPage) - 1) * limitCount;

			dishCriteria.setFirstResult(startCount).setMaxResults(limitCount);
		} else
			dishCriteria.setFirstResult(0).setMaxResults(limitCount);

		dishCriteria.addOrder(Order.asc(DISH_NAME));
		List<Dish> dishList = dishCriteria.list();
		dishMap.put("alldishList", alldishList);
		dishMap.put("goPage", goPage);
		dishMap.put("dishList", dishList);
		return dishMap;
	}
		
	public Dish queryDishByName(Session session, Integer kitchenId, String dishName) {
		try {
			Criteria criteria = session.createCriteria(Dish.class);
			criteria.add(Restrictions.eq("kitchenId", kitchenId));
			criteria.add(Restrictions.eq("dishName", dishName));
			List<Dish> queryObject = criteria.list();
			Dish dish = null;
			if (queryObject.size() == 0) {
				return null;
			}
			Iterator<Dish> iterator = queryObject.iterator();
			while (iterator.hasNext()) {
				dish = iterator.next();
			}
			return dish;
		} catch (RuntimeException re) {
			throw re;
		}

	}
	
	public Dish addNewDish(Dish dish){
		return (Dish) dbSession.save(dish);
	}

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}

}