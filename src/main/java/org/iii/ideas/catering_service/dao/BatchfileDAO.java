package org.iii.ideas.catering_service.dao;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * Batchfile entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see org.iii.ideas.catering_service.dao.Batchfile
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class BatchfileDAO {
	private static final Logger log = LoggerFactory
			.getLogger(BatchfileDAO.class);
	public static final String ROW_ID = "rowId";
	public static final String KITCHEN_ID = "kitchenId";
	public static final String SCHOOL_NAME = "schoolName";
	public static final String MENU_DATE = "menuDate";
	public static final String MAIN_FOOD = "mainFood";
	public static final String MAIN_DISH = "mainDish";
	public static final String MAIN_DISH1 = "mainDish1";
	public static final String MAIN_DISH2 = "mainDish2";
	public static final String SUB_DISH1 = "subDish1";
	public static final String SUB_DISH2 = "subDish2";
	public static final String SUB_DISH3 = "subDish3";
	public static final String SUB_DISH4 = "subDish4";
	public static final String VEGETABLE = "vegetable";
	public static final String SOUP = "soup";
	public static final String DESSERT = "dessert";
	public static final String TYPE_GRASS = "typeGrass";
	public static final String TYPE_MEAL = "typeMeal";
	public static final String TYPE_VEGETABLE = "typeVegetable";
	public static final String TYPE_NUT = "typeNut";
	public static final String TYPE_FRUIT = "typeFruit";
	public static final String TYPE_DAILY = "typeDaily";
	public static final String CALLORI = "callori";
	public static final String CONVERT_STATUS = "convertStatus";
	public static final String ERROR_MESSAGE = "errorMessage";
	// property constants
	public static final String INGREDIENT_BATCH_ID = "ingredientBatchId";
	public static final String INSPECT_FILE_PATH = "inspectFilePath";

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

	public void save(Batchfile transientInstance) {
		log.debug("saving Batchfile instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Batchfile persistentInstance) {
		log.debug("deleting Batchfile instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Batchfile findById(java.lang.Integer id) {
		log.debug("getting Batchfile instance with id: " + id);
		try {
			Batchfile instance = (Batchfile) getCurrentSession().get(
					"org.iii.ideas.catering_service.dao.Batchfile", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Batchfile instance) {
		log.debug("finding Batchfile instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"org.iii.ideas.catering_service.dao.Batchfile")
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
		log.debug("finding Batchfile instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Batchfile as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByIngredientBatchId(Object ingredientBatchId) {
		return findByProperty(INGREDIENT_BATCH_ID, ingredientBatchId);
	}

	public List findByInspectFilePath(Object inspectFilePath) {
		return findByProperty(INSPECT_FILE_PATH, inspectFilePath);
	}

	public List findAll() {
		log.debug("finding all Batchfile instances");
		try {
			String queryString = "from Batchfile";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Batchfile merge(Batchfile detachedInstance) {
		log.debug("merging Batchfile instance");
		try {
			Batchfile result = (Batchfile) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Batchfile instance) {
		log.debug("attaching dirty Batchfile instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Batchfile instance) {
		log.debug("attaching clean Batchfile instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static BatchfileDAO getFromApplicationContext(ApplicationContext ctx) {
		return (BatchfileDAO) ctx.getBean("BatchfileDAO");
	}

	public HashMap batchfileManagerIndex(String kitchenid, String schoolName,
			String startDate, String endDate, HttpServletRequest request,
			String goPage, int startCount, int limitCount) {
		HashMap<String, Object> batchfileMap = new HashMap<String, Object>();

		Criteria batchfileCriteria = getCurrentSession().createCriteria(
				Batchfile.class);
		batchfileCriteria.add(Restrictions.eq(KITCHEN_ID,
				Integer.parseInt(kitchenid)));
		batchfileCriteria.add(Restrictions.like(SCHOOL_NAME, schoolName));
		batchfileCriteria.add(Restrictions.between(MENU_DATE, startDate,
				endDate));

		List<Batchfile> allbatchfileList = (List<Batchfile>) batchfileCriteria
				.list();

		// where limit (分頁)
		if (request.getParameter("searchPage") != null) {
			goPage = request.getParameter("searchPage");

			if (!goPage.equals("1"))
				startCount = (Integer.parseInt(goPage) - 1) * limitCount;

			batchfileCriteria.setFirstResult(startCount).setMaxResults(
					limitCount);
		} else
			batchfileCriteria.setFirstResult(0).setMaxResults(limitCount);

		batchfileCriteria.addOrder(Order.asc(MENU_DATE));
		List<Batchfile> batchfileList = batchfileCriteria.list();

		batchfileMap.put("allbatchfileList", allbatchfileList);
		batchfileMap.put("goPage", goPage);
		batchfileMap.put("batchfileList", batchfileList);
		return batchfileMap;
	}

	public Ingredientbatchdata getIngredinetBatchDataByIngredinetBatchId(
			java.lang.Integer id) {
		// Integer id
		log.debug("getting Ingredientbatchdata instance with id: " + id);
		try {
			Ingredientbatchdata instance = (Ingredientbatchdata) getCurrentSession()
					.get("org.iii.ideas.catering_service.dao.Ingredientbatchdata",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Batchdata getBatchDataByIngredinetBatchId(java.lang.Integer id) {
		// Integer id
		log.debug("getting Ingredientbatchdata instance with id: " + id);
		try {
			Ingredientbatchdata instance = (Ingredientbatchdata) getCurrentSession()
					.get("org.iii.ideas.catering_service.dao.Ingredientbatchdata",
							id);
			Long BatchdataId = instance.getBatchDataId();
			try {
				Batchdata instanceBatchdata = (Batchdata) getCurrentSession()
						.get("org.iii.ideas.catering_service.dao.Batchdata",
								BatchdataId);
				return instanceBatchdata;
			} catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}

			// return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}

	}
}