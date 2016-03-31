package org.iii.ideas.catering_service.dao;

import java.util.ArrayList;
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
 * Ingredient entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Ingredient
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class IngredientDAO {
	private static final Logger log = LoggerFactory.getLogger(IngredientDAO.class);
	// property constants
	public static final String DISH_ID = "dishId";
	public static final String SUPPLIER_ID = "supplierId";
	public static final String INGREDIENT_NAME = "ingredientName";
	public static final String BRAND = "brand";
	public static final String SUPPLIER_COMPANY_ID = "supplierCompanyId";
	private Session dbSession;

	public IngredientDAO() {

	}

	public IngredientDAO(Session dbSession) {
		this.setDbSession(dbSession);
	}

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	}

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

	public void save(Ingredient transientInstance) {
		log.debug("saving Ingredient instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Ingredient persistentInstance) {
		log.debug("deleting Ingredient instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Ingredient findById(java.lang.Integer id) {
		log.debug("getting Ingredient instance with id: " + id);
		try {
			Ingredient instance = (Ingredient) getCurrentSession().get("org.iii.ideas.catering_service.dao.Ingredient", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Ingredient instance) {
		log.debug("finding Ingredient instance by example");
		try {
			List results = getCurrentSession().createCriteria("org.iii.ideas.catering_service.dao.Ingredient").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Ingredient instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from Ingredient as model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByDishId(Object dishId) {
		return findByProperty(DISH_ID, dishId);
	}

	public List findBySupplierId(Object supplierId) {
		return findByProperty(SUPPLIER_ID, supplierId);
	}

	public List findByIngredientName(Object ingredientName) {
		return findByProperty(INGREDIENT_NAME, ingredientName);
	}

	public List findByBrand(Object brand) {
		return findByProperty(BRAND, brand);
	}

	public List findAll() {
		log.debug("finding all Ingredient instances");
		try {
			String queryString = "from Ingredient";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Ingredient merge(Ingredient detachedInstance) {
		log.debug("merging Ingredient instance");
		try {
			Ingredient result = (Ingredient) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Ingredient instance) {
		log.debug("attaching dirty Ingredient instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Ingredient instance) {
		log.debug("attaching clean Ingredient instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IngredientDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IngredientDAO) ctx.getBean("IngredientDAO");
	}

	public List findByKitchenId(String kitchenid) {
		log.debug("finding by KitchenId Ingredient instances");
		try {
			Criteria ingredientCriteria = getCurrentSession().createCriteria(Ingredient.class);

			Criteria dishCriteria = getCurrentSession().createCriteria(Dish.class);
			dishCriteria.add(Restrictions.eq("kitchenId", Integer.parseInt(kitchenid)));

			List<Dish> alldishList = (List<Dish>) dishCriteria.list();
			String sn = "";
			List<Long> dishList = new ArrayList<Long>();
			for (int i = 0; i < alldishList.size(); i++) {
				dishList.add(alldishList.get(i).getDishId());
			}

			/*
			 * if(sn.length() > 1) { sn=sn.substring(0, sn.length()-1);
			 * 
			 * }
			 */
			ingredientCriteria.add(Restrictions.in("dishId", dishList));
			return ingredientCriteria.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			// throw re;
			return null;
		}
	}

	
}