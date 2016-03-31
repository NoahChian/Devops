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
 * A data access object (DAO) providing persistence and search support for Menu
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Menu
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class MenuDAO {
	private static final Logger log = LoggerFactory.getLogger(MenuDAO.class);
	// property constants
	public static final String KITCHEN_ID = "kitchenId";
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
	public static final String TYPE_GRAINS = "typeGrains";
	public static final String TYPE_OIL = "typeOil";
	public static final String TYPE_VEGETABLE = "typeVegetable";
	public static final String TYPE_MILK = "typeMilk";
	public static final String TYPE_FRUIT = "typeFruit";
	public static final String TYPE_MEAT_BEANS = "typeMeatBeans";
	public static final String CALORIE = "calorie";

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

	public void save(Menu transientInstance) {
		log.debug("saving Menu instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Menu persistentInstance) {
		log.debug("deleting Menu instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Menu findById(java.lang.Integer id) {
		log.debug("getting Menu instance with id: " + id);
		try {
			Menu instance = (Menu) getCurrentSession().get(
					"org.iii.ideas.catering_service.dao.Menu", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Menu instance) {
		log.debug("finding Menu instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("org.iii.ideas.catering_service.dao.Menu")
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
		log.debug("finding Menu instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Menu as model where model."
					+ propertyName + "= ?";
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

	public List findAll() {
		log.debug("finding all Menu instances");
		try {
			String queryString = "from Menu";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Menu merge(Menu detachedInstance) {
		log.debug("merging Menu instance");
		try {
			Menu result = (Menu) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Menu instance) {
		log.debug("attaching dirty Menu instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Menu instance) {
		log.debug("attaching clean Menu instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static MenuDAO getFromApplicationContext(ApplicationContext ctx) {
		return (MenuDAO) ctx.getBean("MenuDAO");
	}
}