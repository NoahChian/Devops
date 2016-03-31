package org.iii.ideas.catering_service.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.iii.ideas.catering_service.dao.Area;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.County;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.DishBatchData;
import org.iii.ideas.catering_service.dao.DishBatchDataDAO;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.Menu;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.SeasoningStockData;
import org.iii.ideas.catering_service.dao.SeasoningStockDataDAO;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.UploadFileDAO;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.Userrole;
import org.springframework.util.StringUtils;

public class HibernateUtil {
	private static Logger log = Logger.getLogger(HibernateUtil.class);
	private static SessionFactory sessionFactory = null;
	private static ServiceRegistry serviceRegistry = null;
	private static Configuration configuration = null;
	private static Integer yearConvert = 1911; // A.D to R.O.C or R.O.C to A.D

	public void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtil.sessionFactory = sessionFactory;
	}

	public static SessionFactory buildSessionFactory() {
		return sessionFactory;
	}

	public static String queryDishNameById(Session session, Long dishId) {

		try {
			Criteria criteria = session.createCriteria(Dish.class);
			criteria.add(Restrictions.eq("dishId", dishId));

			List<Dish> queryObject = criteria.list();
			if (queryObject.size() == 0) {
				String dishbatchdataName = queryDishNameFromDishbatchdata(session, dishId);
				return dishbatchdataName;
				// return "";
			}
			Iterator<Dish> iterator = queryObject.iterator();
			Dish dish = null;
			while (iterator.hasNext()) {
				dish = iterator.next();
			}
			return dish.getDishName();
		} catch (RuntimeException re) {
			log.debug("find by property name failed" + re.getMessage());
			throw re;
		}
	}

	// 查詢dishbatchdata 中的菜名
	public static String queryDishNameFromDishbatchdata(Session session, Long dishId) {
		try {
			Criteria criteria = session.createCriteria(DishBatchData.class);
			criteria.add(Restrictions.eq("DishBatchDataId", dishId));

			List<DishBatchData> queryObject = criteria.list();
			if (queryObject.size() == 0) {
				return "";
			}
			Iterator<DishBatchData> iterator = queryObject.iterator();
			DishBatchData dish = null;
			while (iterator.hasNext()) {
				dish = iterator.next();
			}
			return dish.getDishName();
		} catch (RuntimeException re) {
			return "";
			// log.debug("find by property name failed" + re.getMessage());
			// throw re;
		}
	}


	public static String queryDishShownameById(Session session, Long dishId) {

		try {
			Criteria criteria = session.createCriteria(Dish.class);
			criteria.add(Restrictions.eq("dishId", dishId));

			List<Dish> queryObject = criteria.list();
			if (queryObject.size() == 0) {
				return "";
			}
			Iterator<Dish> iterator = queryObject.iterator();
			Dish dish = null;
			while (iterator.hasNext()) {
				dish = iterator.next();
			}
			return dish.getDishShowName();
		} catch (RuntimeException re) {
			log.debug("find by property name failed" + re.getMessage());
			throw re;
		}
	}

	public static String queryDishImageById(Session session, Long dishId) {
		Dish dish = (Dish) session.get("org.iii.ideas.catering_service.dao.Dish", dishId);
		if (dish == null) {
			return "";
		}
		return dish.getPicturePath();
	}

	public static Dish queryDishById(Session session, Long dishId) {
		try {
			Criteria criteria = session.createCriteria(Dish.class);
			criteria.add(Restrictions.eq("dishId", dishId));

			List<Dish> queryObject = criteria.list();
			if (queryObject.size() == 0) {
				return null;
			}
			Iterator<Dish> iterator = queryObject.iterator();
			Dish dish = null;
			while (iterator.hasNext()) {
				dish = iterator.next();
			}
			return dish;
		} catch (RuntimeException re) {
			log.debug("find by property name failed" + re.getMessage());
			throw re;
		}
	}

	public static Long queryDishIdByName(Session session, Integer kitchenId, String dishName) {
		try {
			Criteria criteria = session.createCriteria(Dish.class);
			criteria.add(Restrictions.eq("kitchenId", kitchenId));
			criteria.add(Restrictions.eq("dishName", dishName));
			List<Dish> queryObject = criteria.list();

			if (queryObject.size() == 0) {
				return null;
			}
			Iterator<Dish> iterator = queryObject.iterator();
			Dish dish = null;
			while (iterator.hasNext()) {
				dish = iterator.next();
			}
			if (dish == null) {
				return null;
			}
			return dish.getDishId();
		} catch (RuntimeException re) {
			throw re;
		}

	}

	public static Dish queryDishByName(Session session, Integer kitchenId, String dishName) {
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

	public static String querySchoolNameById(Session session, Integer id) {
		Criteria criteriaSp = session.createCriteria(School.class);
		criteriaSp.add(Restrictions.eq("schoolId", id));
		criteriaSp.add(Restrictions.eq("enable", 1));
		List<School> schoolList = criteriaSp.list();
		if (schoolList.size() == 0) {
			return null;
		}
		School school = schoolList.get(0);
		return school.getSchoolName();
	}

	public static Ingredientbatchdata queryIngredientbatchdataById(Session session, Integer id) {
		log.debug("queryIngredientbatchdataById:" + id);
		Ingredientbatchdata obj = (Ingredientbatchdata) session.get("org.iii.ideas.catering_service.dao.Ingredientbatchdata", id);
		if (obj == null) {
			return null;
		}
		return obj;
	}

	public static Ingredientbatchdata queryIngredientbatchdataByIdAndBatchdataId(Session session, Long ingredientbatchdataId, Long batchdataId) {
		log.debug("queryIngredientbatchdataByIdAndBatchdataId:" + ingredientbatchdataId + "and :" + batchdataId);
		String hql = "FROM Ingredientbatchdata i WHERE i.batchDataId = :batchDataId AND i.ingredientBatchId = :ingredientBatchId";
		Query query = session.createQuery(hql);
		query.setParameter("batchDataId", batchdataId);
		query.setParameter("ingredientBatchId", ingredientbatchdataId);
		if (query.list() != null && query.list().size() > 0) {
			return (Ingredientbatchdata) query.list().get(0);
		} else {
			return null;
		}
	}

	public static Ingredientbatchdata queryIngredientbatchdataByBatchdataIdAndDish(Session session, Long batchdataId, Long dishId) {
		Criteria criteriaSp = session.createCriteria(Ingredientbatchdata.class);
		criteriaSp.add(Restrictions.eq("batchDataId", batchdataId));
		criteriaSp.add(Restrictions.eq("dishId", dishId));
		List<Ingredientbatchdata> ingredientbatchdataIterator = criteriaSp.list();
		if (ingredientbatchdataIterator.size() == 0) {
			return null;
		}
		return ingredientbatchdataIterator.get(0);
	}
	
	public static Long queryDishIdfromBatchDataByBatchdataIdAndDishName(Session session, Long batchdataId,String DishName) {
		Criteria criteriaSp = session.createCriteria(DishBatchData.class);
		criteriaSp.add(Restrictions.eq("BatchDataId", batchdataId));
		criteriaSp.add(Restrictions.eq("DishName", DishName));
		criteriaSp.addOrder(Order.desc("UpdateDateTime")); //排序，將最新的排在前面，取最新的調味料id
		List<DishBatchData> DishBatchDataIterator = criteriaSp.list();
		if (DishBatchDataIterator.size() == 0) {
			return null;
		}
		return DishBatchDataIterator.get(0).getDishId();
	}

	public static List<Ingredientbatchdata> queryIngredientbatchdataListByBatchdataIdAndDish(Session session, Long batchdataId, Long dishId) {
		Criteria criteriaSp = session.createCriteria(Ingredientbatchdata.class);
		criteriaSp.add(Restrictions.eq("batchDataId", batchdataId));
		criteriaSp.add(Restrictions.eq("dishId", dishId));
		List<Ingredientbatchdata> ingredientbatchdataIterator = criteriaSp.list();
		if (ingredientbatchdataIterator.size() == 0) {
			return null;
		}
		return ingredientbatchdataIterator;
	}

	public static Ingredientbatchdata queryIngredientbatchdataByBatchdataIdAndIngredient(Session session, Long batchdataId, Long dishId, Long ingredientId, String supplierCompanyId) {
		Criteria criteriaSp = session.createCriteria(Ingredientbatchdata.class);
		criteriaSp.add(Restrictions.eq("batchDataId", batchdataId));
		criteriaSp.add(Restrictions.eq("dishId", dishId));
		criteriaSp.add(Restrictions.eq("ingredientId", ingredientId));
		criteriaSp.add(Restrictions.eq("supplierCompanyId", supplierCompanyId));
		List<Ingredientbatchdata> ingredientbatchdataIterator = criteriaSp.list();
		if (ingredientbatchdataIterator.size() == 0) {
			return null;
		}
		return ingredientbatchdataIterator.get(0);
	}

	public static Ingredientbatchdata queryIngredientbatchdataByBatchdataIdAndIngredient(Session session, Long batchdataId, Long dishId, Long ingredientId, String supplierCompanyId, String lotNumber) {
		Criteria criteriaSp = session.createCriteria(Ingredientbatchdata.class);
		criteriaSp.add(Restrictions.eq("batchDataId", batchdataId));
		criteriaSp.add(Restrictions.eq("dishId", dishId));
		criteriaSp.add(Restrictions.eq("ingredientId", ingredientId));
		criteriaSp.add(Restrictions.eq("supplierCompanyId", supplierCompanyId));
		criteriaSp.add(Restrictions.eq("lotNumber", lotNumber));
		List<Ingredientbatchdata> ingredientbatchdataIterator = criteriaSp.list();
		if (ingredientbatchdataIterator.size() == 0) {
			return null;
		}
		return ingredientbatchdataIterator.get(0);
	}

	public static Supplier querySupplierById(Session session, Integer id) {
		Criteria criteriaSp = session.createCriteria(Supplier.class);
		criteriaSp.add(Restrictions.eq("id.supplierId", id));
		List<Supplier> supplierList = criteriaSp.list();
		if (supplierList.size() == 0) {
			return null;
		}
		Supplier supplier = supplierList.get(0);
		return supplier;
	}

	public static Supplier querySupplierByCompanyId(Session session, Integer kitchenId, String companyId) {
		/*
		 * Criteria criteriaSp = session.createCriteria(Supplier.class);
		 * criteriaSp.add(Restrictions.eq("id.kitchenId", kitchenId));
		 * log.debug(
		 * "1====>querySupplierByCompanyId kitchenId:"+kitchenId+" companyId:"
		 * +companyId); List<Supplier> supplierList = criteriaSp.list();
		 * log.debug
		 * ("2====>querySupplierByCompanyId kitchenId:"+kitchenId+" companyId:"
		 * +companyId); Iterator<Supplier> supplierIterator =
		 * supplierList.iterator(); while (supplierIterator.hasNext()) {
		 * Supplier supplier = supplierIterator.next();
		 * if(supplier.getCompanyId().equals(companyId)){ return supplier; } }
		 * return null;
		 * 
		 * if (supplierList.size() == 0) { return null; } Supplier supplier =
		 * supplierList.get(0); return supplier;
		 */

		String HQL = "from Supplier where id.kitchenId= :kitchenId and companyId = :companyId";
		Query query = session.createQuery(HQL);
		query.setParameter("kitchenId", kitchenId);
		query.setParameter("companyId", companyId);
		List<Supplier> supplierList = query.list();
		Iterator<Supplier> supplierIterator = supplierList.iterator();
		while (supplierIterator.hasNext()) {
			Supplier obj = supplierIterator.next();
			return obj;
		}
		return null;

	}

	public static Kitchen queryKitchenById(Session session, Integer id) {
		Criteria criteriaSp = session.createCriteria(Kitchen.class);
		criteriaSp.add(Restrictions.eq("kitchenId", id));
		List<Kitchen> supplierList = criteriaSp.list();
		if (supplierList.size() == 0) {
			return null;
		}
		Kitchen kitchen = supplierList.get(0);
		return kitchen;
	}

	public static Kitchen queryKitchenByName(Session session, String name) {
		Criteria criteriaSp = session.createCriteria(Kitchen.class);
		criteriaSp.add(Restrictions.eq("kitchenName", name));
		List<Kitchen> supplierList = criteriaSp.list();
		if (supplierList.size() == 0) {
			return null;
		}
		Kitchen kitchen = supplierList.get(0);
		return kitchen;
	}

	public static Kitchen queryKitchenByCompanyId(Session session, String companyId) {
		Criteria criteriaSp = session.createCriteria(Kitchen.class);
		criteriaSp.add(Restrictions.eq("companyId", companyId));
		List<Kitchen> supplierList = criteriaSp.list();
		if (supplierList.size() == 0) {
			return null;
		}
		Kitchen kitchen = supplierList.get(0);
		return kitchen;
	}

	/* 預防修改食材之日期輸入不正確格式
	 * 民國轉西元 西元轉民國
	 * yyy/mm/dd (民國)
	 * yyyy/mm/dd (西元)
	 */
	// fmt=> yyyy/mm/dd
	// dateStr=>2013/12/13
	//20141107 Ellis 更新判斷西元或民國年
	public static Timestamp convertStrToTimestamp(String fmt, String dateStr) throws ParseException {
		
		if (dateStr == null || dateStr.trim().equals("")) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
		Date parsedDate = dateFormat.parse(dateStr);
		
		int dateint = Integer.parseInt(dateFormat.format(parsedDate).toString().replaceAll("\\/|-",""));
		int newYear = dateint/10000;
		int newMonth = (dateint % 10000)/100;
		int newDay = (dateint % 100);
		if(String.valueOf(newYear).length()<4){
			newYear+=yearConvert;
		}
		//確認需求是民國年還是西元年
		if(fmt.equals("yyy/mm/dd")){
			newYear-=yearConvert;
		}
		dateStr = newYear+"/"+newMonth+"/"+newDay;
		parsedDate = dateFormat.parse(dateStr);
		Timestamp ts = new java.sql.Timestamp(parsedDate.getTime());
		/*
		String fmtStr[] = fmt.split("\\/|-");
		String dtStr[] = dateStr.split("\\/");
		if(fmtStr[0].length() == 3){
			if(dtStr[0].length() != 3){
				String year = String.valueOf(Integer.parseInt(dtStr[0]) - yearConvert);
				dateStr = year + "/" + dtStr[1] + "/" +dtStr[2];				
			}
		}else if(fmtStr[0].length() == 4){
			if(dtStr[0].length() != 4){
				String year = String.valueOf(Integer.parseInt(dtStr[0]) + yearConvert);
				dateStr = year + "/" + dtStr[1] + "/" +dtStr[2];
			}
		}
		log.debug("convertStrToTimestamp:" + dateStr + " fmt:" + fmt);
		*/
		return ts;
	}

	public static String converTimestampToStr(String fmt, Timestamp ts) throws ParseException {
		if (ts == null) {
			return "";
		}
		String Str = new SimpleDateFormat(fmt).format(ts);
		return Str;
	}

	public static String converTimestampToStr(String fmt, Timestamp ts, Locale locale) throws ParseException {
		String Str = new SimpleDateFormat(fmt, locale).format(ts);
		return Str;
	}

	public static Supplier getSupplier(Session dbsession, int kid, String ingredientName) {
		Criteria criteriaSp = dbsession.createCriteria(Supplier.class);
		criteriaSp.add(Restrictions.eq("supplierName", ingredientName));
		criteriaSp.add(Restrictions.eq("id.kitchenId", kid));
		List<Supplier> supplierList = criteriaSp.list();
		if (supplierList.size() == 0) {
			return null;
		}
		Supplier supplier = supplierList.get(0);
		return supplier;
	}

	public static Batchdata queryBatchdataByUK(Session dbsession, int kitchenId, int schoolId, String menuDate, String lotNum) {
		Criteria criteriaSp = dbsession.createCriteria(Batchdata.class);
		criteriaSp.add(Restrictions.eq("kitchenId", kitchenId));
		criteriaSp.add(Restrictions.eq("schoolId", schoolId));
		criteriaSp.add(Restrictions.eq("menuDate", menuDate));
		criteriaSp.add(Restrictions.eq("lotNumber", lotNum));
		//只搜尋午餐、且enable為1的。 add by Ellis 20150525
		criteriaSp.add(Restrictions.eq("menuType", 1) );
		criteriaSp.add(Restrictions.eq("enable", 1) );
		List<Batchdata> batchdataList = criteriaSp.list();
		if (batchdataList.size() == 0) {
			return null;
		}
		Batchdata batchdata = batchdataList.get(0);
		return batchdata;
	}
	
	public static Batchdata queryBatchdataByUKv2(Session dbsession, int kitchenId, int schoolId, String menuDate, String lotNum,String menuType) {
		Criteria criteriaSp = dbsession.createCriteria(Batchdata.class);
		criteriaSp.add(Restrictions.eq("kitchenId", kitchenId));
		criteriaSp.add(Restrictions.eq("schoolId", schoolId));
		criteriaSp.add(Restrictions.eq("menuDate", menuDate));
		criteriaSp.add(Restrictions.eq("lotNumber", lotNum));
		//只搜尋午餐、且enable為1的。 add by Ellis 20150525
		criteriaSp.add(Restrictions.eq("menuType", Integer.parseInt(menuType)) );
		criteriaSp.add(Restrictions.eq("enable", 1) );
		List<Batchdata> batchdataList = criteriaSp.list();
		if (batchdataList.size() == 0) {
			return null;
		}
		Batchdata batchdata = batchdataList.get(0);
		return batchdata;
	}

	public static School querySchoolByKitchenAndName(Session dbsession, int kitchenId, String schoolName) {
		String HQL = "from Schoolkitchen sk, School s where sk.id.schoolId = s.schoolId and sk.id.kitchenId = :kitchenId and s.schoolName =:schoolName";
		Query query = dbsession.createQuery(HQL);
		query.setParameter("kitchenId", kitchenId);
		query.setParameter("schoolName", schoolName);
		List<Object[]> schoolList = query.list();
		Iterator<Object[]> schoolIterator = schoolList.iterator();
		while (schoolIterator.hasNext()) {
			Object[] obj = schoolIterator.next();
			return (School) obj[1];
		}
		return null;
	}

	public static List<School> querySchoolListByKitchenId(Session dbsession, int kitchenId) {
		List<School> schools = new ArrayList<School>();
		Criteria criteriaSK = dbsession.createCriteria(Schoolkitchen.class).add(Restrictions.eq("id.kitchenId", kitchenId));
		List schoolkitchens = criteriaSK.list();
		Iterator<Schoolkitchen> iteratorSK = schoolkitchens.iterator();
		while (iteratorSK.hasNext()) {
			Schoolkitchen sk = iteratorSK.next();
			Criteria criteriaSC = dbsession.createCriteria(School.class).add(Restrictions.eq("schoolId", sk.getId().getSchoolId())).add(Restrictions.eq("enable", 1));
			List Schools = criteriaSC.list();
			Iterator<School> iteratorSC = Schools.iterator();
			while (iteratorSC.hasNext()) {
				schools.add(iteratorSC.next());
			}
		}
		return schools;
	}

	public static Batchdata getBatchdataByBatchdataId(Session dbsession, int mid) {
		Criteria criteriaSp = dbsession.createCriteria(Batchdata.class);
		criteriaSp.add(Restrictions.eq("batchDataId", mid));
		List<Batchdata> batchdataList = criteriaSp.list();
		if (batchdataList.size() == 0) {
			return null;
		}
		Batchdata batchdata = batchdataList.get(0);
		return batchdata;
	}

	public static List<Batchdata> queryBatchdataByMenuDate(Session dbsession, int kitchenId, String menuDate) {
		Criteria criteriaSp = dbsession.createCriteria(Batchdata.class);
		criteriaSp.add(Restrictions.eq("kitchenId", kitchenId));
		criteriaSp.add(Restrictions.eq("menuDate", menuDate));
		criteriaSp.add(Restrictions.eq("enable", 1));
		criteriaSp.add(Restrictions.eq("menuType", 1));
		List<Batchdata> batchdataList = criteriaSp.list();
		return batchdataList;
	}

	public static List<Batchdata> querySchoolBatchdataByMenuDate(Session dbsession, int kitchenId, String menuDate, Integer schoolId) {
		Criteria criteriaSp = dbsession.createCriteria(Batchdata.class);
		criteriaSp.add(Restrictions.eq("kitchenId", kitchenId));
		criteriaSp.add(Restrictions.eq("menuDate", menuDate));
		criteriaSp.add(Restrictions.eq("schoolId", schoolId));
		criteriaSp.add(Restrictions.eq("enable", 1));
		List<Batchdata> batchdataList = criteriaSp.list();
		return batchdataList;
	}

	public static List<Batchdata> queryBatchdataByMenuDateAndDishId(Session dbsession, int kitchenId, String menuDate, Long dishId) {
		String HQL = "from Batchdata b" + " where :dishId in ( " + "b.mainFoodId, " + "b.mainFood1id, " + "b.mainDishId, " + "b.mainDish1id, " + "b.mainDish2id, " + "b.mainDish3id, " + "b.subDish1id, " + "b.subDish2id, " + "b.subDish3id, "
				+ "b.subDish4id, " + "b.subDish5id, " + "b.subDish6id, " + "b.vegetableId, " + "b.soupId, " + "b.dessertId, " + "b.dessert1id ) " + "and b.menuDate = :menuDate " + "and b.kitchenId = :kitchenId " + "order by b.menuDate desc";
		Query query = dbsession.createQuery(HQL);
		query.setParameter("dishId", dishId);
		query.setParameter("menuDate", menuDate);
		query.setParameter("kitchenId", kitchenId);
		// System.out.println("queryBatchdataByMenuDateAndDishId:"+dishId);
		List<Batchdata> results = query.list();
		return results;
	}

	public static List<Batchdata> querySchoolBatchdataByMenuDateAndDishId(Session dbsession, int kitchenId, String menuDate, Integer schoolId, Long dishId) {
		String HQL = "from Batchdata b" + " where :dishId in ( " + "b.mainFoodId, " + "b.mainFood1id, " + "b.mainDishId, " + "b.mainDish1id, " + "b.mainDish2id, " + "b.mainDish3id, " + "b.subDish1id, " + "b.subDish2id, " + "b.subDish3id, "
				+ "b.subDish4id, " + "b.subDish5id, " + "b.subDish6id, " + "b.vegetableId, " + "b.soupId, " + "b.dessertId, " + "b.dessert1id ) " + "and b.menuDate  = :menuDate " + "and b.kitchenId = :kitchenId " + "and b.schoolId = :schoolId "
				+ "order by b.menuDate desc";
		Query query = dbsession.createQuery(HQL);
		query.setParameter("dishId", dishId);
		query.setParameter("menuDate", menuDate);
		query.setParameter("kitchenId", kitchenId);
		query.setParameter("schoolId", schoolId);
		List<Batchdata> results = query.list();
		return results;
	}

	public static Ingredientbatchdata getIngredientBatchData(Session dbsession, Long menuId, Long dishId) {
		Criteria criteriaIgdb = dbsession.createCriteria(Ingredientbatchdata.class);
		criteriaIgdb.add(Restrictions.eq("batchDataId", menuId));
		criteriaIgdb.add(Restrictions.eq("dishId", dishId));
		List<Ingredientbatchdata> ingredientbatchdataList = criteriaIgdb.list();
		if (ingredientbatchdataList.size() == 0) {
			return null;
		}
		Ingredientbatchdata oldIgdb = ingredientbatchdataList.get(0);
		return oldIgdb;
	}

	public static Ingredient getIngredient(Session dbsession, Long dishId, String ingredientName, Integer supplierId) {
		Criteria criteriaIgd = dbsession.createCriteria(Ingredient.class);
		criteriaIgd.add(Restrictions.eq("ingredientName", ingredientName));
		criteriaIgd.add(Restrictions.eq("supplierId", supplierId));
		criteriaIgd.add(Restrictions.eq("dishId", dishId));
		List<Ingredient> ingredientList = criteriaIgd.list();
		Ingredient ingredinet = null;
		if (ingredientList.size() == 0) {
			return null;
		} else {
			ingredinet = ingredientList.get(0);
		}
		return ingredinet;
	}

	public static List<Ingredient> queryIngredientByDishId(Session dbsession, Long dishId) {
		Criteria criteriaIgd = dbsession.createCriteria(Ingredient.class);
		criteriaIgd.add(Restrictions.eq("dishId", dishId));
		List<Ingredient> ingredientList = criteriaIgd.list();
		return ingredientList;
	}

	public static Ingredient queryIngredientById(Session dbsession, Integer ingredientId) {
		Criteria criteriaIgd = dbsession.createCriteria(Ingredient.class);
		criteriaIgd.add(Restrictions.eq("ingredientId", ingredientId));
		List<Ingredient> ingredientList = criteriaIgd.list();
		if (ingredientList.size() == 0) {
			return null;
		}
		return ingredientList.get(0);
	}

	public static Ingredient queryIngredientIdByDishAndIngredientname(Session dbsession, Long dishId, String ingredientName) {
		Criteria criteriaIgd = dbsession.createCriteria(Ingredient.class);
		criteriaIgd.add(Restrictions.eq("dishId", dishId));
		List<Ingredient> ingredientList = criteriaIgd.list();
		Iterator<Ingredient> ingredientIterator = ingredientList.iterator();
		while (ingredientIterator.hasNext()) {

			Ingredient idg = ingredientIterator.next();
			if (idg.getIngredientName().equals(ingredientName)) {
				return idg;
			}
		}
		return null;
	}

	public static Supplier querySupplierById(Session dbsession, Integer kitchenId, Integer supplierId) {
		Criteria criteriaIgd = dbsession.createCriteria(Supplier.class);
		criteriaIgd.add(Restrictions.eq("id.kitchenId", kitchenId));
		criteriaIgd.add(Restrictions.eq("id.supplierId", supplierId));
		List<Supplier> suppliers = criteriaIgd.list();
		Iterator<Supplier> supplierIterator = suppliers.iterator();
		while (supplierIterator.hasNext()) {
			return supplierIterator.next();
		}
		return null;
	}

	public static Supplier querySupplierByName(Session dbsession, Integer kitchenId, String supplierName) {
		Criteria criteriaIgd = dbsession.createCriteria(Supplier.class);
		criteriaIgd.add(Restrictions.eq("id.kitchenId", kitchenId));
		criteriaIgd.add(Restrictions.eq("supplierName", supplierName));
		List<Supplier> suppliers = criteriaIgd.list();
		Iterator<Supplier> supplierIterator = suppliers.iterator();
		while (supplierIterator.hasNext()) {
			return supplierIterator.next();
		}
		return null;
	}

	public static Supplier querySupplierByCompanyId(Session dbsession, Integer kitchenId, Integer companyId) {
		Criteria criteriaIgd = dbsession.createCriteria(Supplier.class);
		criteriaIgd.add(Restrictions.eq("id.kitchenId", kitchenId));
		criteriaIgd.add(Restrictions.eq("companyId", companyId));
		List<Supplier> suppliers = criteriaIgd.list();
		Iterator<Supplier> supplierIterator = suppliers.iterator();
		while (supplierIterator.hasNext()) {
			return supplierIterator.next();
		}
		return null;
	}

	public static Ingredient getSeasoning(Session dbsession, Long dishId, String ingredientName, Integer supplierId) {
		Criteria criteriaIgd = dbsession.createCriteria(Ingredient.class);
		criteriaIgd.add(Restrictions.eq("ingredientName", ingredientName));
		criteriaIgd.add(Restrictions.eq("supplierId", supplierId));
		List<Ingredient> ingredientList = criteriaIgd.list();
		Ingredient ingredinet = null;
		if (ingredientList.size() == 0) {
			return null;
		} else {
			ingredinet = ingredientList.get(0);
		}
		return ingredinet;
	}

	public static Area queryAreaById(Session session, Integer id) {
		Criteria criteriaIgd = session.createCriteria(Area.class);
		criteriaIgd.add(Restrictions.eq("areaId", id));
		List<Area> objList = criteriaIgd.list();
		Iterator<Area> iterator = objList.iterator();
		while (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}

	public static County queryCountyById(Session session, Integer id) {
		Criteria criteriaIgd = session.createCriteria(County.class);
		criteriaIgd.add(Restrictions.eq("countyId", id));
		List<County> objList = criteriaIgd.list();
		Iterator<County> iterator = objList.iterator();
		while (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}

	public static Batchdata queryBatchdataById(Session session, Long batchdataId) {
		Criteria criteriaIgd = session.createCriteria(Batchdata.class);
		criteriaIgd.add(Restrictions.eq("batchDataId", batchdataId));
		List<Batchdata> objList = criteriaIgd.list();
		Iterator<Batchdata> iterator = objList.iterator();
		while (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}

	public static void deleteBatchdataById(Session session, Long batchdataId) {
		HibernateUtil.deleteIngredientbatchdataByBatchdataId(session, batchdataId);
		Query query = session.createQuery("delete from Batchdata n where n.batchDataId = :id");
		query.setParameter("id", batchdataId);
		log.debug("deleteBatchdataById result:" + query.executeUpdate() + " ID:" + batchdataId);
		// 刪除dishbatchdata
		HibernateUtil.deleteDishbatchdataByBatchdataId(session, batchdataId);

	}

	public static void deleteSchoolBySchoolId(Session session, Integer id) {
		Query query = session.createQuery("delete from School s where s.schoolId = :id");
		query.setParameter("id", id);
		log.debug("deleteSchoolBySchoolId result:" + query.executeUpdate() + " ID:" + id);

	}

	public static void deleteKitchenById(Session session, Integer id) {
		// HibernateUtil.deleteIngredientbatchdataByBatchdataId(session, id);
		Query query = session.createQuery("delete from Kitchen n where n.kitchenId = :id");
		query.setParameter("id", id);
		log.debug("deleteKitchenById result:" + query.executeUpdate() + " ID:" + id);

	}

	public static void deleteKitchenFromSchoolKitchenById(Session session, Integer id) {
		// HibernateUtil.deleteIngredientbatchdataByBatchdataId(session, id);
		Query query = session.createQuery("delete from Schoolkitchen n where n.id.kitchenId = :id");
		query.setParameter("id", id);
		log.debug("deleteKitchenFromSchoolKitchenById result:" + query.executeUpdate() + " ID:" + id);

	}

	public static void deleteSchoolKitchenFromSchoolKitchenById(Session session, Integer kid, Integer sid) {
		Query query = session.createQuery("delete from Schoolkitchen n where n.id.kitchenId = :kid and n.id.schoolId = :sid ");
		query.setParameter("kid", kid);
		query.setParameter("sid", sid);
		log.debug("deleteSchoolKitchenFromSchoolKitchenById result:" + query.executeUpdate() + " kID:" + kid + " and sID:" + sid);

	}

	public static void deleteAcceptSchoolKitchenById(Session session, Integer id) {
		Query query = session.createQuery("delete from AcceptSchoolKitchen n where n.id = :id");
		query.setParameter("id", id);
		log.debug("deleteAcceptSchoolKitchenById result:" + query.executeUpdate() + " ID:" + id);

	}
	
	public static void deleteMenuByUk(Session session, Integer kitchenId, String menuDate) {
		Query query = session.createQuery("delete from Menu n where n.kitchenId = :kid and n.menuDate = :menuDate");
		query.setParameter("kid", kitchenId);
		query.setParameter("menuDate", menuDate);
		log.debug("deleteMenuByUk result:" + query.executeUpdate() + " ID:" + kitchenId + " Date:" + menuDate);
	}

	/*
	 * 20140408 KC ↓ deleteBatchdataByMenuDate 這個函式目前專案內沒人用 所以沒加delete
	 * dishbatchdata的部分!
	 */
	public static void deleteBatchdataByMenuDate(Session session, Integer kitchenId, String menuDate) {
		Query query = session.createQuery("delete from Batchdata n where n.kitchenId = :kid and n.menuDate = :menuDate");
		query.setParameter("kid", kitchenId);
		query.setParameter("menuDate", menuDate);
		log.debug("deleteMenuByUk result:" + query.executeUpdate() + " ID:" + kitchenId + " Date:" + menuDate);
	}

	public static void deleteBatchdataByUK(Session session, Integer kitchenId, Integer schoolId, String menuDate, String lotNumber) {

		Batchdata batchdata = queryBatchdataByUK(session, kitchenId, schoolId, menuDate, lotNumber);
		if (batchdata != null) {
			// deleteIngredientbatchdataByBatchdataId(session,batchdata.getBatchDataId());
			HibernateUtil.deleteBatchdataById(session, batchdata.getBatchDataId());

			log.debug("deleteMenuByUk  ID:" + kitchenId + " Date:" + menuDate + " SchoolId:" + schoolId + " lotNumber:" + lotNumber);
		}

	}
	
	public static void deleteBatchdataByUKv2(Session session, Integer kitchenId, Integer schoolId, String menuDate, String lotNumber,String menuType) {

		Batchdata batchdata = queryBatchdataByUKv2(session, kitchenId, schoolId, menuDate, lotNumber,menuType);
		if (batchdata != null) {
			// deleteIngredientbatchdataByBatchdataId(session,batchdata.getBatchDataId());
			HibernateUtil.deleteBatchdataById(session, batchdata.getBatchDataId());

			log.debug("deleteMenuByUk  ID:" + kitchenId + " Date:" + menuDate + " SchoolId:" + schoolId + " lotNumber:" + lotNumber);
		}

	}

	public static void deleteIngredientbatchdataByBatchdataId(Session session, Long id) {
		Query query = session.createQuery("delete from Ingredientbatchdata n where n.batchDataId = :id");
		query.setParameter("id", id);
		log.debug("deleteIngredientbatchdataByBatchdataId result:" + query.executeUpdate() + " ID:" + id);
	}

	public static void deleteIngredientbatchdataByDishId(Session session, Long batchdataId, Long dishId) {
		Query query = session.createQuery("delete from Ingredientbatchdata n where n.batchDataId = :batchDataId and   n.dishId = :dishId");
		query.setParameter("batchDataId", batchdataId);
		query.setParameter("dishId", dishId);
		log.debug("deleteIngredientbatchdataByDishId result:" + query.executeUpdate() + " ID:" + batchdataId + " dishId:" + dishId);
	}

	public static void deleteIngredientbatchdataBySeasoning(Session session, Long batchdataId) {
		Query query = session.createQuery("delete from Ingredientbatchdata n where n.batchDataId = :batchDataId and   n.ingredientName like '調味料%'");
		query.setParameter("batchDataId", batchdataId);
		log.debug("deleteIngredientbatchdataBySeasoning result:" + query.executeUpdate() + " ID:" + batchdataId);
	}

	public static void deleteRoleByUsername(Session session, String username) {
		Query query = session.createQuery("delete from Userrole n where n.username = :username");
		query.setParameter("username", username);
		log.debug("deleteRoleByUsername result:" + query.executeUpdate() + " username:" + username);
	}

	public static void deleteUseraccountByUsername(Session session, String username) {
		Query query = session.createQuery("delete from Useraccount n where n.username = :username");
		query.setParameter("username", username);
		log.debug("deleteUseraccountByUsername result:" + query.executeUpdate() + " username:" + username);
	}

	public static void saveIngredientbatchdataByBatchdataId(Session session, Long batchdataId) {
		Batchdata batchdata = HibernateUtil.queryBatchdataById(session, batchdataId);
		List<Long> dishArray = new ArrayList<Long>();
		// 找出這個菜單的所有dishId
		dishArray = HibernateUtil.getDishIdByBatchdata(dishArray, batchdata);
		Iterator<Long> iterator = dishArray.iterator();
		while (iterator.hasNext()) {
			Long dishId = iterator.next();
			// 找出這個菜色的所有食材資料
			saveIngredientbatchdataFromIngredient(session, batchdata.getBatchDataId(), dishId);

		}
		// 取得這個供應商的調味料dishId

		Long dishId = HibernateUtil.querySeasoningDishId(session, batchdata.getKitchenId());
		// 取消帶入前一天調味料機制，改由seasoningstockdata帶入 modify by ellis 20150128
//		SeasoningStockDataDAO sDAO = new SeasoningStockDataDAO(session);
//		List<SeasoningStockData> seasoning;
//		try {
//			seasoning = sDAO.querySeasoningByDishIdandDate(dishId,batchdata.getMenuDate());
//			if(!CateringServiceUtil.isNull(seasoning)){
//				for(int i=0;i<seasoning.size();i++){
//					Ingredientbatchdata igdb = new Ingredientbatchdata();
//					igdb = sDAO.tranSeasoningdatastockToIngredientbatchdata(seasoning.get(i));
//					igdb.setBatchDataId(batchdata.getBatchDataId());
//					session.save(igdb);
//				}
//			}
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// 取消帶前一天調味料機置，改由seasoningstockdata帶入 modify by ellis 20150302
		
		// 如果找不到資料就帶前一天的batchdataID,查詢它的調味料ID,如果沒有調味料就不新增(這裡考慮學校相)
		
//		String beforeMidHQL = "from Batchdata a,Batchdata b where a.schoolId = b.schoolId " + "and a.kitchenId = b.kitchenId " + "and a.menuDate < b.menuDate " + "and b.batchDataId=:batchDataId " + "order by a.menuDate desc";
//		Query beforeMidQuery = session.createQuery(beforeMidHQL);
//		beforeMidQuery.setParameter("batchDataId", batchdataId);
//		beforeMidQuery.setMaxResults(1);
//		List beforeMidList = beforeMidQuery.list();
//		Iterator<Object[]> beforeMids = beforeMidList.iterator();
//		if (beforeMids.hasNext()) {
//			Object[] obj = beforeMids.next();
//			Batchdata beforeBatchdata = (Batchdata) obj[0];
//			// 找出前一天的調味料資料
//			String sqlStatement = "from Batchdata b,Ingredientbatchdata i " + "where b.batchDataId = :batchDataId " + " and  b.batchDataId = i.batchDataId " + " and  i.dishId = :dishId ";
//			Query query = session.createQuery(sqlStatement);
//			query = session.createQuery(sqlStatement);
//			query.setParameter("batchDataId", beforeBatchdata.getBatchDataId());
//			query.setParameter("dishId", dishId);
//			List results = query.list();
//			// --------複製翦一天的調味料到今天-----------
//			Iterator<Object[]> seasoningIterator = results.iterator();
//			
//			UploadFileDAO ufDao = new UploadFileDAO(session);
//			while (seasoningIterator.hasNext()) {
//				Object[] seasoningObj = seasoningIterator.next();
//				Batchdata batchdataObj = (Batchdata) seasoningObj[0];
//				Ingredientbatchdata igdbObj = (Ingredientbatchdata) seasoningObj[1];
//				//20140918 Raymond 複製調味料時一併把檢驗報告複製過去 
//				Long sourceId = igdbObj.getIngredientBatchId(); //舊的IngredientBatchId
//				session.evict(igdbObj);
//				igdbObj.setBatchDataId(batchdataId); 
//				session.save(igdbObj);
//				Long targetId = igdbObj.getIngredientBatchId(); //新的IngredientBatchId
//				
//				//複製檢驗報告
//				ufDao.copyInspectReport(sourceId, targetId);
//				
//			}
//		}
		
	}

	public static Ingredient queryIngredientByName(Session session, Long dishId, String name) {
		Criteria criteriaIgd = session.createCriteria(Ingredient.class);
		criteriaIgd.add(Restrictions.eq("ingredientName", name));
		criteriaIgd.add(Restrictions.eq("dishId", dishId));
		List<Ingredient> ingredientList = criteriaIgd.list();
		Iterator<Ingredient> ingredientIterator = ingredientList.iterator();
		while (ingredientIterator.hasNext()) {
			Ingredient ingredient = ingredientIterator.next();
			return ingredient;
		}
		return null;
	}

	public static Menu queryMenuByUK(Session session, int kitchenId, String menuDate) {
		Criteria criteriaIgd = session.createCriteria(Menu.class);
		criteriaIgd.add(Restrictions.eq("menuDate", menuDate));
		criteriaIgd.add(Restrictions.eq("kitchenId", kitchenId));
		List<Menu> batchdataList = criteriaIgd.list();
		Iterator<Menu> batchdataIterator = batchdataList.iterator();
		while (batchdataIterator.hasNext()) {
			Menu menu = batchdataIterator.next();
			return menu;
		}
		return null;
	}

	public static Menu queryMenuById(Session session, Long menuId) {
		Criteria criteriaIgd = session.createCriteria(Menu.class);
		criteriaIgd.add(Restrictions.eq("menuId", menuId));
		List<Menu> batchdataList = criteriaIgd.list();
		Iterator<Menu> batchdataIterator = batchdataList.iterator();
		while (batchdataIterator.hasNext()) {
			Menu menu = batchdataIterator.next();
			return menu;
		}
		return null;
	}

	/*
	 * public static Menu cloneMenuFromBatchdata(Batchdata batchdata) { Menu
	 * menu = new Menu();
	 * 
	 * menu.setKitchenId(batchdata.getKitchenId());
	 * menu.setMenuDate(batchdata.getMenuDate());
	 * menu.setMainFoodId(batchdata.getMainFoodId());
	 * menu.setMainFood1id(batchdata.getMainFood1id());
	 * menu.setMainDishId(batchdata.getMainDishId());
	 * menu.setMainDish1id(batchdata.getMainDish1id());
	 * menu.setMainDish2id(batchdata.getMainDish2id());
	 * menu.setMainDish3id(batchdata.getMainDish3id());
	 * menu.setSubDish1id(batchdata.getSubDish1id());
	 * menu.setSubDish2id(batchdata.getSubDish2id());
	 * menu.setSubDish3id(batchdata.getSubDish3id());
	 * menu.setSubDish4id(batchdata.getSubDish4id());
	 * menu.setSubDish5id(batchdata.getSubDish5id());
	 * menu.setSubDish6id(batchdata.getSubDish6id());
	 * menu.setDessertId(batchdata.getDessertId());
	 * menu.setDessert1id(batchdata.getDessert1id());
	 * menu.setVegetableId(batchdata.getVegetableId());
	 * menu.setSoupId(batchdata.getSoupId());
	 * menu.setTypeFruit(batchdata.getTypeFruit());
	 * menu.setTypeGrains(batchdata.getTypeGrains());
	 * menu.setTypeMeatBeans(batchdata.getTypeMeatBeans());
	 * menu.setTypeMilk(batchdata.getTypeMilk());
	 * menu.setTypeOil(batchdata.getTypeOil());
	 * menu.setTypeVegetable(batchdata.getTypeVegetable());
	 * menu.setCalorie(batchdata.getCalorie()); return menu; }
	 */
	public static List<Long> addDishId2List(List<Long> dishIdList, Long dishId) {
		if (dishIdList.contains(dishId) == false && dishId != 0) {
			dishIdList.add(dishId);
		}
		return dishIdList;
	}

	public static List<Long> getDishIdByBatchdata(List<Long> dishIdList, Batchdata menu) {
		// 若主食有值則回傳batchdata內的dishid 不然就去抓dishbatchdata內的dishbatchdataid(為台北市用)
		if (menu.getMainFoodId() != 0) {
			dishIdList = addDishId2List(dishIdList, menu.getMainFoodId());
			dishIdList = addDishId2List(dishIdList, menu.getMainFood1id());
			dishIdList = addDishId2List(dishIdList, menu.getMainDishId());
			dishIdList = addDishId2List(dishIdList, menu.getMainDish1id());
			dishIdList = addDishId2List(dishIdList, menu.getMainDish2id());
			dishIdList = addDishId2List(dishIdList, menu.getMainDish3id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish1id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish2id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish3id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish4id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish5id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish6id());
			dishIdList = addDishId2List(dishIdList, menu.getVegetableId());
			dishIdList = addDishId2List(dishIdList, menu.getSoupId());
			dishIdList = addDishId2List(dishIdList, menu.getDessertId());
			dishIdList = addDishId2List(dishIdList, menu.getDessert1id());

		} else {
			DishBatchDataDAO dao = new DishBatchDataDAO();
			// HibernateUtil.sessionFactory=HibernateUtil.buildSessionFactory();
			Session session = HibernateUtil.sessionFactory.openSession();
			dao.setSession(session);
			List<DishBatchData> result = dao.getDishBatchDataByBatchId(menu.getBatchDataId());
			Iterator<DishBatchData> ir = result.iterator();

			while (ir.hasNext()) {
				DishBatchData row = ir.next();
				dishIdList.add(row.getDishBatchDataId());
			}
			session.close();

		}
		return dishIdList;
	}

	public static List<Long> getDishIdByBatchdata(Batchdata menu) {
		List<Long> dishIdList = new ArrayList<Long>();
		// 若主食有值則回傳batchdata內的dishid 不然就去抓dishbatchdata內的dishbatchdataid(為台北市用)
		if (menu.getMainFoodId() != 0) {
			dishIdList = addDishId2List(dishIdList, menu.getMainFoodId());
			dishIdList = addDishId2List(dishIdList, menu.getMainFood1id());
			dishIdList = addDishId2List(dishIdList, menu.getMainDishId());
			dishIdList = addDishId2List(dishIdList, menu.getMainDish1id());
			dishIdList = addDishId2List(dishIdList, menu.getMainDish2id());
			dishIdList = addDishId2List(dishIdList, menu.getMainDish3id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish1id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish2id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish3id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish4id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish5id());
			dishIdList = addDishId2List(dishIdList, menu.getSubDish6id());
			dishIdList = addDishId2List(dishIdList, menu.getVegetableId());
			dishIdList = addDishId2List(dishIdList, menu.getSoupId());
			dishIdList = addDishId2List(dishIdList, menu.getDessertId());
			dishIdList = addDishId2List(dishIdList, menu.getDessert1id());
		} else {
			DishBatchDataDAO dao = new DishBatchDataDAO();
			// HibernateUtil.sessionFactory=HibernateUtil.buildSessionFactory();

			// 改open session 20140423 KC
			dao.setSession(HibernateUtil.sessionFactory.openSession());
			List<DishBatchData> result = dao.getDishBatchDataByBatchId(menu.getBatchDataId());
			Iterator<DishBatchData> ir = result.iterator();
			while (ir.hasNext()) {
				DishBatchData row = ir.next();
				//20150505 shine mod 抓DishId欄位才對
				//dishIdList.add(row.getDishBatchDataId());
				dishIdList.add(row.getDishId());
			}

		}
		return dishIdList;
	}

	public static String getDishByDishIdList(Session session, List<Long> dishIdList) {
		String foodStr = "";
		Iterator<Long> iterator = dishIdList.iterator();
		while (iterator.hasNext()) {
			Long dishId = iterator.next();
			String foodName = HibernateUtil.queryDishNameById(session, dishId);
			foodStr += (foodName.equals("")) ? "" : foodName + ",";
		}
		foodStr = StringUtils.trimTrailingCharacter(foodStr, ',');
		return foodStr;
	}

	public static Long querySeasoningDishId(Session session, Integer kitchenId) {
		String dishName = CateringServiceUtil.ColumnNameOfSeasoning;
		Dish dish = HibernateUtil.queryDishByName(session, kitchenId, dishName);
		if (dish == null) {
			// 如果沒有調味料就自動新增一筆
			dish = new Dish();
			dish.setDishName(dishName);
			dish.setKitchenId(kitchenId);
			dish.setPicturePath("");
			session.save(dish);
		}
		return dish.getDishId();
	}

	public static void saveIngredientbatchdataFromIngredient(Session session, Long batchdataId, Long dishId) {
		Batchdata batchdata = HibernateUtil.queryBatchdataById(session, batchdataId);
		Criteria criteria = session.createCriteria(Ingredient.class);
		criteria.add(Restrictions.eq("dishId", dishId));
		List ingredientList = criteria.list();
		Iterator<Ingredient> ingredientIterator = ingredientList.iterator();
		while (ingredientIterator.hasNext()) {
			Ingredient newIngredient = ingredientIterator.next();
			Ingredientbatchdata igdb = new Ingredientbatchdata();
			// supplierId
			Supplier supplier = HibernateUtil.querySupplierById(session, batchdata.getKitchenId(), newIngredient.getSupplierId());

			igdb.setBatchDataId(batchdataId);
			igdb.setBrand(newIngredient.getBrand());
			igdb.setCertificationId("");
			igdb.setDishId(dishId);
			igdb.setIngredientId(newIngredient.getIngredientId());
			igdb.setIngredientName(newIngredient.getIngredientName());
			igdb.setLotNumber(batchdata.getLotNumber());
			igdb.setManufactureDate(null);
			//將品牌資料新增至製造商  Ellis 20141023
			//判斷產品名稱邏輯修正 Ellis 20141106
			igdb.setProductName(newIngredient.getProductName() == null ? "" : newIngredient.getProductName());
					
			if (CateringServiceUtil.isEmpty(newIngredient.getManufacturer())){
				igdb.setManufacturer(newIngredient.getBrand());
			}else{
				igdb.setManufacturer(newIngredient.getManufacturer() == null ? "" : newIngredient.getManufacturer());
			}
			
			//igdb.setManufacturer(newIngredient.getManufacturer() == null ? "" : newIngredient.getManufacturer());
			// String countyName ="";
			// String areaName ="";
			/** 因應食材ver3上傳，寫入supplierName add by Ellis 20141204*/
			if (supplier != null) {
				igdb.setSourceCertification("");
				igdb.setOrigin("");
				igdb.setSource("");
				igdb.setSupplierCompanyId(supplier.getCompanyId() == null ? "" : supplier.getCompanyId());
				igdb.setSupplierName(supplier.getSupplierName() == null ? "" : supplier.getSupplierName());
			} else {
				igdb.setSourceCertification("");
				igdb.setOrigin("");
				igdb.setSource("");
				igdb.setSupplierCompanyId("");
				igdb.setSupplierName("");
			}
			igdb.setSupplierId(newIngredient.getSupplierId());

			session.save(igdb);
		}
	}

	public static void saveUsernameRoleRelation(Session session, String username, String roletype) {
		Userrole user = new Userrole();
		user.setUsername(username);
		user.setRoletype(roletype);
		user.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
		session.save(user);
	}

	public static void updateUsernameRoleRelation(Session session, String username, String roletype) {
		HibernateUtil.deleteRoleByUsername(session, username);
		Userrole user = new Userrole();
		user.setUsername(username);
		user.setRoletype(roletype);
		user.setCreateDate(CateringServiceUtil.getCurrentTimestamp());// 應該要先撈出之前的createtime再存入
		user.setUpdateTime(CateringServiceUtil.getCurrentTimestamp());
		session.save(user);
	}
	
	public static Ingredient saveIngredient(Session session, Long dishId, String ingredientName, String brand, Integer supplierId, String supplierCompanyId) {
		Ingredient ingredient = new Ingredient();
		ingredient.setBrand(brand == null ? "" : brand);
		ingredient.setDishId(dishId);
		ingredient.setIngredientName(ingredientName);
		ingredient.setSupplierId(supplierId);
		ingredient.setSupplierCompanyId(supplierCompanyId == null ? "" : supplierCompanyId);
		session.save(ingredient);
		return ingredient;
	}
	//新增製造商及商品名稱欄位 20141027 ellis
	public static Ingredient saveIngredient(Session session, Long dishId, String ingredientName, String brand, Integer supplierId, String supplierCompanyId,String productName,String manufacturer) {
		Ingredient ingredient = new Ingredient();
		ingredient.setBrand(brand == null ? "" : brand);
		ingredient.setDishId(dishId);
		ingredient.setIngredientName(ingredientName);
		ingredient.setSupplierId(supplierId);
		ingredient.setSupplierCompanyId(supplierCompanyId == null ? "" : supplierCompanyId);
		ingredient.setProductName(productName == null ? "" : productName);
		ingredient.setManufacturer(manufacturer == null ? "" : manufacturer);
		
		session.save(ingredient);
		return ingredient;
	}

	public static Batchdata updateBatchdata(Session session, Batchdata batchdata) {
		List<Long> dishIdList = HibernateUtil.getDishIdByBatchdata(batchdata);
		Boolean isOnlyDishbatchdataFlag = (batchdata.getMainFoodId() == 0) ? true : false;
		// 確認調味料不會被刪除
		Dish dishSeasoning = HibernateUtil.queryDishByName(session, batchdata.getKitchenId(), CateringServiceUtil.ColumnNameOfSeasoning);
//		if (dishSeasoning != null) {
//			dishIdList.add(dishSeasoning.getDishId());
//		}

		if (dishIdList.size() > 0) {
			// 清楚非必要之dishId
			String HQL = "delete from Ingredientbatchdata where dishId not in :dishIdList and batchDataId=:batchDataId and dishId <> "+ dishSeasoning.getDishId();
			Query query = session.createQuery(HQL);
			query.setParameterList("dishIdList", dishIdList);
			query.setParameter("batchDataId", batchdata.getBatchDataId());
			query.executeUpdate();
		}

		// 若是只有存dishbatchdata的資料(如台北市) 則不主動處理食材部分 20140408KC
		// 從菜色中取得新的ingredientid
		if (!isOnlyDishbatchdataFlag) {
			for (Long dishId : dishIdList) {
				if (HibernateUtil.queryIngredientbatchdataByBatchdataIdAndDish(session, batchdata.getBatchDataId(), dishId) == null) {
					HibernateUtil.saveIngredientbatchdataFromIngredient(session, batchdata.getBatchDataId(), dishId);
				}
			}
		}

		// 特別處理soap端來的台北市資料 update時的資料落差,先刪再新增，但是還是沒有處理食材 20140408KC
		if (!isOnlyDishbatchdataFlag) {
			deleteDishbatchdataNotInDishlist(session, batchdata.getBatchDataId(), dishIdList);
		}

		HibernateUtil.saveBatchdataToDishbatchdata(session, batchdata);
		session.update(batchdata);
		return batchdata;
	}

	// Raymond 20140523 增加參數saveIngredientbatchdata 判斷是否需要新增菜色對應食材
	public static Batchdata updateBatchdata(Session session, Batchdata batchdata, boolean saveIngredientbatchdata) {
		List<Long> dishIdList = HibernateUtil.getDishIdByBatchdata(batchdata);
		Boolean isOnlyDishbatchdataFlag = (batchdata.getMainFoodId() == 0) ? true : false;
		// 確認調味料不會被刪除
		Dish dishSeasoning = HibernateUtil.queryDishByName(session, batchdata.getKitchenId(), CateringServiceUtil.ColumnNameOfSeasoning);
		if (dishSeasoning != null) {
			dishIdList.add(dishSeasoning.getDishId());
		}

		if (dishIdList.size() > 0) {
			// 清楚非必要之dishId
			String HQL = "delete from Ingredientbatchdata where dishId not in :dishIdList and batchDataId=:batchDataId";
			Query query = session.createQuery(HQL);
			query.setParameterList("dishIdList", dishIdList);
			query.setParameter("batchDataId", batchdata.getBatchDataId());
			query.executeUpdate();
		}

		// 20140523 Raymond mark
		// // 若是只有存dishbatchdata的資料(如台北市) 則不主動處理食材部分 20140408KC
		// // 從菜色中取得新的ingredientid
		// if (!isOnlyDishbatchdataFlag) {
		// for (Long dishId : dishIdList) {
		// if
		// (HibernateUtil.queryIngredientbatchdataByBatchdataIdAndDish(session,
		// batchdata.getBatchDataId(), dishId) == null) {
		// HibernateUtil.saveIngredientbatchdataFromIngredient(session,
		// batchdata.getBatchDataId(), dishId);
		// }
		// }
		// }
		//
		// // 特別處理soap端來的台北市資料 update時的資料落差,先刪再新增，但是還是沒有處理食材 20140408KC
		// if (!isOnlyDishbatchdataFlag) {
		// deleteDishbatchdataNotInDishlist(session, batchdata.getBatchDataId(),
		// dishIdList);
		// }

		// 20140523 Raymond 改用saveIngredientbatchdata 判斷是否要存Ingredientbatchdata
		// 從菜色中取得新的ingredientid
		if (saveIngredientbatchdata) {
			for (Long dishId : dishIdList) {
				if (HibernateUtil.queryIngredientbatchdataByBatchdataIdAndDish(session, batchdata.getBatchDataId(), dishId) == null) {
					HibernateUtil.saveIngredientbatchdataFromIngredient(session, batchdata.getBatchDataId(), dishId);
				}
			}
		}

		// 特別處理soap端來的台北市資料 update時的資料落差,先刪再新增，但是還是沒有處理食材 20140408KC
		if (saveIngredientbatchdata) {
			deleteDishbatchdataNotInDishlist(session, batchdata.getBatchDataId(), dishIdList);
		}

		HibernateUtil.saveBatchdataToDishbatchdata(session, batchdata);
		session.update(batchdata);
		return batchdata;
	}

	public static boolean removeOtherIngredientbatchdataByIdList(Session session, List<Long> modifyIbList, Long batchdataId, Long dishId) {
		String hql = "DELETE FROM Ingredientbatchdata i " + "WHERE i.ingredientBatchId NOT IN :idList " + "AND i.batchDataId = :batchDataId " + "AND i.dishId = :dishId";
		Query query = session.createQuery(hql);
		query.setParameterList("idList", modifyIbList);
		query.setParameter("batchDataId", batchdataId);
		query.setParameter("dishId", dishId);
		int result = query.executeUpdate();
		if (result > 0)
			return true;
		else
			return false;
	}

	public static Ingredientbatchdata saveIngredientbatchdata(Session session, Long batchdataId, Long ingredinetId, Integer supplierId, Long dishId, String lotNum, String ingredientName, String brand, String stockDate, String validDate,
			String productDate, String authenticateId, String sourceCertification, String companyId, String origin, String source) throws Exception {
		Ingredientbatchdata igdb = new Ingredientbatchdata();
		igdb.setBatchDataId(batchdataId);
		igdb.setBrand(brand);

		Batchdata batchdata = HibernateUtil.queryBatchdataById(session, batchdataId);

		// checkDateColumn 為true 時再檢查日期格式
		String dishName = HibernateUtil.queryDishNameById(session, dishId);
		// 如果是調呠料就不檢查
		if (!dishName.equals(CateringServiceUtil.ColumnNameOfSeasoning)) {
			if (!CateringServiceUtil.isEmpty(stockDate)) {
				CateringServiceUtil.checkIngredientDate(batchdata.getMenuDate(), stockDate, validDate, productDate);
			} else {
				throw new Exception("食材:" + ingredientName + " 進貨日期不可空白");
			}
		}

		// 有認證編就必須要有編號 反之亦然
		if (!CateringServiceUtil.isEmpty(sourceCertification)) {
			if (CateringServiceUtil.isEmpty(authenticateId)) {
				throw new Exception("食材:" + ingredientName + " 認證編號不可空白");
			}
		}

		if (!CateringServiceUtil.isEmpty(authenticateId)) {
			if (CateringServiceUtil.isEmpty(sourceCertification)) {
				throw new Exception("食材:" + ingredientName + " 認證不可空白");
			}
		}
		if (CateringServiceUtil.isEmpty(lotNum)) {
			lotNum = CateringServiceUtil.defaultLotNumber;
		}

		// if(queryIngredientBylotNum(session,batchdataId,ingredinetId,lotNum,supplierId)!=null){
		// throw new Exception("食材:" + ingredientName + " 重複輸入");
		// }

		// igdb.setIngredientBatchId(ingredientBatchdataId);
		// 找出 supplier 資料
		Supplier supplier = HibernateUtil.querySupplierById(session, supplierId);
		if (supplier == null) {
			throw new Exception("食材:" + ingredientName + " 找不到供應商資料  SupplierId:" + supplierId);
		}

		igdb.setDishId(dishId); // 問昌港
		igdb.setIngredientId(ingredinetId);
		igdb.setSupplierId(supplierId);
		igdb.setLotNumber(lotNum);
		// 先查出是否有這筆資料
		igdb = queryIngredientBylotNum(session, igdb);

		igdb.setStockDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", stockDate));
		igdb.setExpirationDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", validDate));
		igdb.setManufactureDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", productDate));
		igdb.setCertificationId(authenticateId == null ? "" : authenticateId);
		igdb.setIngredientName(ingredientName);
		igdb.setSourceCertification(sourceCertification);
		igdb.setSupplierName(supplier.getSupplierName());
		igdb.setOrigin(origin);
		igdb.setSource(source);
		igdb.setBatchDataId(batchdataId);
		igdb.setBrand(brand);
		igdb.setSupplierCompanyId(companyId == null ? "" : companyId);
		session.saveOrUpdate(igdb);
		return igdb;
	}

	// Raymond 2014/05/12
	public static Ingredientbatchdata updateIngredientbatchdata(Session session, Ingredientbatchdata igdb, Long ingredinetId, Integer supplierId, String lotNum, String ingredientName, String brand, String stockDate, String validDate, String productDate,
			String authenticateId, String sourceCertification, String companyId, String origin, String source) throws Exception {
		igdb.setBrand(brand);

		Batchdata batchdata = HibernateUtil.queryBatchdataById(session, igdb.getBatchDataId());

		// checkDateColumn 為true 時再檢查日期格式
		String dishName = HibernateUtil.queryDishImageById(session, igdb.getDishId());
		// 如果是調呠料就不檢查
		if (!dishName.equals(CateringServiceUtil.ColumnNameOfSeasoning)) {
			if (!CateringServiceUtil.isEmpty(stockDate)) {
				CateringServiceUtil.checkIngredientDate(batchdata.getMenuDate(), stockDate, validDate, productDate);
			} else {
				throw new Exception("食材:" + ingredientName + " 進貨日期不可空白");
			}
		}

		// 有認證編就必須要有編號 反之亦然
		if (!CateringServiceUtil.isEmpty(sourceCertification)) {
			if (CateringServiceUtil.isEmpty(authenticateId)) {
				throw new Exception("食材:" + ingredientName + " 認證編號不可空白");
			}
		}

		if (!CateringServiceUtil.isEmpty(authenticateId)) {
			if (CateringServiceUtil.isEmpty(sourceCertification)) {
				throw new Exception("食材:" + ingredientName + " 認證不可空白");
			}
		}
		if (CateringServiceUtil.isEmpty(lotNum)) {
			lotNum = CateringServiceUtil.defaultLotNumber;
		}
		// 找出 supplier 資料
		Supplier supplier = HibernateUtil.querySupplierById(session, supplierId);
		if (supplier == null) {
			throw new Exception("食材:" + ingredientName + " 找不到供應商資料  SupplierId:" + supplierId);
		}

		igdb.setIngredientId(ingredinetId);
		igdb.setSupplierId(supplierId);
		igdb.setLotNumber(lotNum);
		igdb.setStockDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", stockDate));
		igdb.setExpirationDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", validDate));
		igdb.setManufactureDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", productDate));
		igdb.setCertificationId(authenticateId == null ? "" : authenticateId);
		igdb.setIngredientName(ingredientName);
		igdb.setSourceCertification(sourceCertification);
		igdb.setOrigin(origin);
		igdb.setSource(source);
		igdb.setSupplierName(supplier.getSupplierName());
		igdb.setSupplierCompanyId(companyId == null ? "" : companyId);
		igdb.setBrand(brand);
		session.saveOrUpdate(igdb);
		return igdb;
	}

	/**
	 * Raymond 2014/07/22 新增多型saveIngredientbatchdata
	 * 新增參數ingredientAttr,productName
	 * ,manufacturer,ingredientQuantity,ingredientUnit
	 */
	public static Ingredientbatchdata saveIngredientbatchdata(Session session, Long batchdataId, Long ingredinetId, Integer supplierId, Long dishId, String lotNum, String ingredientName, String brand, String stockDate, String validDate,
			String productDate, String authenticateId, String sourceCertification, String companyId, String origin, String source, Integer ingredientAttr, String productName, String manufacturer, String ingredientQuantity, String ingredientUnit)
			throws Exception {
		Ingredientbatchdata igdb = new Ingredientbatchdata();
		igdb.setBatchDataId(batchdataId);
		igdb.setBrand(brand);

		Batchdata batchdata = HibernateUtil.queryBatchdataById(session, batchdataId);

		// checkDateColumn 為true 時再檢查日期格式
		String dishName = HibernateUtil.queryDishNameById(session, dishId);
		// 如果是調呠料就不檢查
		//20141208 調味料也要檢驗日期格式 modify by ellis
//		if (!dishName.equals(CateringServiceUtil.ColumnNameOfSeasoning)) {
			if (!CateringServiceUtil.isEmpty(stockDate)) {
				CateringServiceUtil.checkIngredientDate(batchdata.getMenuDate(), stockDate, validDate, productDate);
			} else {
				throw new Exception("食材:" + ingredientName + " 進貨日期不可空白");
			}
//		}

		// 有認證編就必須要有編號 反之亦然
		if (!CateringServiceUtil.isEmpty(sourceCertification)) {
			if (CateringServiceUtil.isEmpty(authenticateId)) {
				throw new Exception("食材:" + ingredientName + " 認證編號不可空白");
			}
		}

		if (!CateringServiceUtil.isEmpty(authenticateId)) {
			if (CateringServiceUtil.isEmpty(sourceCertification)) {
				throw new Exception("食材:" + ingredientName + " 認證不可空白");
			}
		}

		// 判斷重量是否為數字
		if (!CateringServiceUtil.isEmpty(ingredientQuantity)) {
			if (!CateringServiceUtil.isNumeric(ingredientQuantity)) {
				throw new Exception("食材:" + ingredientName + " 重量必須為數字");
			}
		}

		if (CateringServiceUtil.isEmpty(lotNum)) {
			lotNum = CateringServiceUtil.defaultLotNumber;
		}

		// if(queryIngredientBylotNum(session,batchdataId,ingredinetId,lotNum,supplierId)!=null){
		// throw new Exception("食材:" + ingredientName + " 重複輸入");
		// }

		// igdb.setIngredientBatchId(ingredientBatchdataId);
		// 找出 supplier 資料
		Supplier supplier = HibernateUtil.querySupplierById(session, supplierId);
		if (supplier == null) {
			throw new Exception("食材:" + ingredientName + " 找不到供應商資料  SupplierId:" + supplierId);
		}

		igdb.setDishId(dishId); // 問昌港
		igdb.setIngredientId(ingredinetId);
		igdb.setSupplierId(supplierId);
		igdb.setLotNumber(lotNum);
		// 先查出是否有這筆資料
		igdb = queryIngredientBylotNum(session, igdb);

		igdb.setStockDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", stockDate));
		igdb.setExpirationDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", validDate));
		igdb.setManufactureDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", productDate));
		igdb.setCertificationId(authenticateId == null ? "" : authenticateId);
		igdb.setIngredientName(ingredientName);
		igdb.setSourceCertification(sourceCertification);
		igdb.setSupplierName(supplier.getSupplierName());
		igdb.setOrigin(origin);
		igdb.setSource(source);
		igdb.setBatchDataId(batchdataId);
		igdb.setBrand(brand);
		igdb.setSupplierCompanyId(companyId == null ? "" : companyId);
		igdb.setIngredientAttr(ingredientAttr);
		igdb.setProductName(productName);
		igdb.setIngredientQuantity(ingredientQuantity);
		igdb.setManufacturer(manufacturer);
		igdb.setIngredientUnit("公斤");
		session.saveOrUpdate(igdb);
		return igdb;
	}

	public static Ingredientbatchdata updateIngredientbatchdata(Session session, Long ingredientbatchdataId, Long batchdataId, Long ingredinetId, Integer supplierId, Long dishId, String lotNum, String ingredientName, String brand, String stockDate,
			String validDate, String productDate, String authenticateId, String sourceCertification, String companyId, String origin, String source) throws Exception {
		Ingredientbatchdata igdb = new Ingredientbatchdata();
		igdb.setBatchDataId(batchdataId);
		igdb.setBrand(brand);

		Batchdata batchdata = HibernateUtil.queryBatchdataById(session, batchdataId);

		// checkDateColumn 為true 時再檢查日期格式
		String dishName = HibernateUtil.queryDishImageById(session, dishId);
		// 如果是調呠料就不檢查
		if (!dishName.equals(CateringServiceUtil.ColumnNameOfSeasoning)) {
			if (!CateringServiceUtil.isEmpty(stockDate)) {
				CateringServiceUtil.checkIngredientDate(batchdata.getMenuDate(), stockDate, validDate, productDate);
			} else {
				throw new Exception("食材:" + ingredientName + " 進貨日期不可空白");
			}
		}

		// 有認證編就必須要有編號 反之亦然
		if (!CateringServiceUtil.isEmpty(sourceCertification)) {
			if (CateringServiceUtil.isEmpty(authenticateId)) {
				throw new Exception("食材:" + ingredientName + " 認證編號不可空白");
			}
		}

		if (!CateringServiceUtil.isEmpty(authenticateId)) {
			if (CateringServiceUtil.isEmpty(sourceCertification)) {
				throw new Exception("食材:" + ingredientName + " 認證不可空白");
			}
		}
		if (CateringServiceUtil.isEmpty(lotNum)) {
			lotNum = CateringServiceUtil.defaultLotNumber;
		}

		// 找出 supplier 資料
		Supplier supplier = HibernateUtil.querySupplierById(session, supplierId);
		if (supplier == null) {
			throw new Exception("食材:" + ingredientName + " 找不到供應商資料  SupplierId:" + supplierId);
		}
		// if(queryIngredientBylotNum(session,batchdataId,ingredinetId,lotNum,supplierId)!=null){
		// throw new Exception("食材:" + ingredientName + " 重複輸入");
		// }

		// igdb.setIngredientBatchId(ingredientBatchdataId);
		// 先查出是否有這筆資料
		igdb = queryIngredientbatchdataByIdAndBatchdataId(session, ingredientbatchdataId, batchdataId);
		if (igdb == null)
			igdb = new Ingredientbatchdata();

		igdb.setDishId(dishId); // 問昌港
		igdb.setIngredientId(ingredinetId);
		igdb.setSupplierId(supplierId);
		igdb.setLotNumber(lotNum);
		// 先查出是否有這筆資料
		// igdb = queryIngredientBylotNum(session,igdb);

		igdb.setStockDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", stockDate));
		igdb.setExpirationDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", validDate));
		igdb.setManufactureDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", productDate));
		igdb.setCertificationId(authenticateId == null ? "" : authenticateId);
		igdb.setIngredientName(ingredientName);
		igdb.setSourceCertification(sourceCertification);
		igdb.setOrigin(origin);
		igdb.setSource(source);
		igdb.setSupplierCompanyId(companyId == null ? "" : companyId);
		igdb.setSupplierName(supplier.getSupplierName());
		// igdb.setBatchDataId(batchdataId);
		igdb.setBrand(brand);
		session.saveOrUpdate(igdb);
		return igdb;
	}

	/**
	 * Raymond 2014/07/22 新增多型updateIngredientbatchdata
	 * 新增參數ingredientAttr,productName
	 * ,manufacturer,ingredientQuantity,ingredientUnit
	 */
	public static Ingredientbatchdata updateIngredientbatchdata(Session session, Ingredientbatchdata igdb, Long ingredinetId, Integer supplierId, String lotNum, String ingredientName, String brand, String stockDate, String validDate, String productDate,
			String authenticateId, String sourceCertification, String companyId, String origin, String source, Integer ingredientAttr, String productName, String manufacturer, String ingredientQuantity, String ingredientUnit) throws Exception {
		igdb.setBrand(brand);

		Batchdata batchdata = HibernateUtil.queryBatchdataById(session, igdb.getBatchDataId());

		// checkDateColumn 為true 時再檢查日期格式
		String dishName = HibernateUtil.queryDishNameById(session, igdb.getDishId());
		// 如果是調呠料就不檢查
		//20141208 調味料也要檢驗日期格式 modify by ellis 
		//if (!dishName.equals(CateringServiceUtil.ColumnNameOfSeasoning)) { 
			if (!CateringServiceUtil.isEmpty(stockDate)) {
				CateringServiceUtil.checkIngredientDate(batchdata.getMenuDate(), stockDate, validDate, productDate);
			} else {
				throw new Exception("食材:" + ingredientName + " 進貨日期不可空白");
			}
		//}

		// 有認證編就必須要有編號 反之亦然
		if (!CateringServiceUtil.isEmpty(sourceCertification)) {
			if (CateringServiceUtil.isEmpty(authenticateId)) {
				throw new Exception("食材:" + ingredientName + " 認證編號不可空白");
			}
		}

		if (!CateringServiceUtil.isEmpty(authenticateId)) {
			if (CateringServiceUtil.isEmpty(sourceCertification)) {
				throw new Exception("食材:" + ingredientName + " 認證不可空白");
			}
		}
		// 判斷重量是否為數字
		if (!CateringServiceUtil.isEmpty(ingredientQuantity)) {
			if (!CateringServiceUtil.isNumeric(ingredientQuantity)) {
				throw new Exception("食材:" + ingredientName + " 重量必須為數字");
			}
		}

		if (CateringServiceUtil.isEmpty(lotNum)) {
			lotNum = CateringServiceUtil.defaultLotNumber;
		}
		// 找出 supplier 資料
		Supplier supplier = HibernateUtil.querySupplierById(session, supplierId);
		if (supplier == null) {
			throw new Exception("食材:" + ingredientName + " 找不到供應商資料  SupplierId:" + supplierId);
		}

		igdb.setIngredientId(ingredinetId);
		igdb.setSupplierId(supplierId);
		igdb.setLotNumber(lotNum);
		igdb.setStockDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", stockDate));
		igdb.setExpirationDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", validDate));
		igdb.setManufactureDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", productDate));
		igdb.setCertificationId(authenticateId == null ? "" : authenticateId);
		igdb.setIngredientName(ingredientName);
		igdb.setSourceCertification(sourceCertification);
		igdb.setOrigin(origin);
		igdb.setSource(source);
		igdb.setSupplierName(supplier.getSupplierName());
		igdb.setSupplierCompanyId(companyId == null ? "" : companyId);
		igdb.setBrand(brand);
		igdb.setIngredientAttr(ingredientAttr);
		igdb.setProductName(productName);
		igdb.setIngredientQuantity(ingredientQuantity);
		igdb.setManufacturer(manufacturer);
		igdb.setIngredientUnit("公斤");
		session.saveOrUpdate(igdb);
		return igdb;
	}

	public static Ingredientbatchdata queryIngredientBylotNum(Session session, Ingredientbatchdata igdb) {
		String hql = "FROM Ingredientbatchdata WHERE batchDataId = :batchDataId AND ingredientId = :ingredientId AND lotNumber = :lotNumber	" + "AND supplierId = :supplierId AND dishId = :dishId ";
		// + "AND stockDate = :stockDate "
		// +
		// "AND expirationDate = :expirationDate AND manufactureDate = :manufactureDate";
		Query query = session.createQuery(hql);

		query.setParameter("batchDataId", igdb.getBatchDataId());
		query.setParameter("ingredientId", igdb.getIngredientId());
		query.setParameter("lotNumber", igdb.getLotNumber());
		query.setParameter("supplierId", igdb.getSupplierId());
		query.setParameter("dishId", igdb.getDishId());
		// query.setParameter("stockDate", igdb.getStockDate());
		// query.setParameter("expirationDate", igdb.getExpirationDate());
		// query.setParameter("manufactureDate", igdb.getManufactureDate());

		if (query.list() != null && query.list().size() > 0) {
			return (Ingredientbatchdata) query.list().get(0);
		} else {
			return igdb;
		}
	}

	public static Useraccount queryUseraccountByName(Session session, String username) {
		Criteria criteria = session.createCriteria(Useraccount.class);
		criteria.add(Restrictions.eq("username", username));
		List<Useraccount> queryObject = criteria.list();
		if (queryObject.size() == 0) {
			return null;
		}
		return queryObject.get(0);
	}

	public static void validBatchdata(Batchdata batchdata) throws Exception {
		/*
		 * menu.getTypeGrains(),//全榖根莖 menu.getTypeMeatBeans(),//豆魚肉蛋
		 * menu.getTypeVegetable(),//蔬菜 menu.getTypeOil(),//油脂與堅果種子
		 * menu.getTypeFruit(),//水果 menu.getTypeMilk(),//乳品 menu.getCalorie()});
		 */
		String msg = "";
		if (!CateringServiceUtil.isNumeric(batchdata.getCalorie())) {
			msg += "熱量格式不為數字型態 \n";
		}
		if (!CateringServiceUtil.isNumeric(batchdata.getTypeFruit())) {
			msg += "水果格式不為數字型態  \n";
		}
		if (!CateringServiceUtil.isNumeric(batchdata.getTypeGrains())) {
			msg += "全榖根莖格式不為數字型態 \n";
		}
		if (!CateringServiceUtil.isNumeric(batchdata.getTypeMeatBeans())) {
			msg += "豆魚肉蛋格式不為數字型態 \n";
		}
		if (!CateringServiceUtil.isNumeric(batchdata.getTypeMilk())) {
			msg += "乳品格式不為數字型態 \n";
		}
		if (!CateringServiceUtil.isNumeric(batchdata.getTypeOil())) {
			msg += "油脂與堅果種子格式不為數字型態 \n";
		}
		if (!CateringServiceUtil.isNumeric(batchdata.getTypeVegetable())) {
			msg += "蔬菜格式不為數字型態 ";
		}
		if (!"".equals(msg)) {
			throw new Exception("六大營養標示:" + msg);
		}

	}

	public static Batchdata saveBatchdata(Session session, Batchdata batchdata) throws Exception {

		validBatchdata(batchdata);
		/* 20140814 KC
		// 20140523 Raymond 判斷菜單有無重複
		if (isBatchDataExist(session, batchdata)) {
			throw new Exception("學校" + batchdata.getSchoolId() + "," + batchdata.getMenuDate() + "日菜單已存在");
		}
		*/
		session.save(batchdata);
		saveBatchdataToDishbatchdata(session, batchdata);
		HibernateUtil.saveIngredientbatchdataByBatchdataId(session, batchdata.getBatchDataId());
		return batchdata;
	}

	// Raymond 20140523 增加參數saveIngredientbatchdata 判斷是否需要新增菜色對應食材
	public static Batchdata saveBatchdata(Session session, Batchdata batchdata, boolean saveIngredientbatchdata) throws Exception {
		validBatchdata(batchdata);
		// 20140523 Raymond 判斷菜單有無重複
		if (isBatchDataExist(session, batchdata)) {
			throw new Exception("學校" + batchdata.getSchoolId() + "," + batchdata.getMenuDate() + "日菜單已存在");
		}
		session.save(batchdata);
		saveBatchdataToDishbatchdata(session, batchdata);
		if (saveIngredientbatchdata)
			HibernateUtil.saveIngredientbatchdataByBatchdataId(session, batchdata.getBatchDataId());
		return batchdata;
	}

	public static void saveBatchdataToDishbatchdata(Session session, Batchdata batchdata) {
		HashMap<String, Long> dishIdMap = new HashMap<String, Long>();
		if (!CateringServiceUtil.isNull(batchdata.getMainFoodId())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_MAINFOOD, batchdata.getMainFoodId());
		}

		if (!CateringServiceUtil.isNull(batchdata.getMainFood1id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_MAINFOOD1, batchdata.getMainFood1id());
		}

		if (!CateringServiceUtil.isNull(batchdata.getMainDishId())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_MAINDISH, batchdata.getMainDishId());
		}

		if (!CateringServiceUtil.isNull(batchdata.getMainDish1id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_MAINDISH1, batchdata.getMainDish1id());
		}

		if (!CateringServiceUtil.isNull(batchdata.getMainDish2id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_MAINDISH2, batchdata.getMainDish2id());
		}

		if (!CateringServiceUtil.isNull(batchdata.getMainDish3id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_MAINDISH3, batchdata.getMainDish3id());
		}
		if (!CateringServiceUtil.isNull(batchdata.getSubDish1id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_SUBDISH1, batchdata.getSubDish1id());
		}

		if (!CateringServiceUtil.isNull(batchdata.getSubDish2id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_SUBDISH2, batchdata.getSubDish2id());
		}

		if (!CateringServiceUtil.isNull(batchdata.getSubDish3id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_SUBDISH3, batchdata.getSubDish3id());
		}

		if (!CateringServiceUtil.isNull(batchdata.getSubDish4id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_SUBDISH4, batchdata.getSubDish4id());
		}

		if (!CateringServiceUtil.isNull(batchdata.getSubDish5id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_SUBDISH5, batchdata.getSubDish5id());
		}

		if (!CateringServiceUtil.isNull(batchdata.getSubDish6id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_SUBDISH6, batchdata.getSubDish6id());
		}

		if (!CateringServiceUtil.isNull(batchdata.getVegetableId())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_VEGETABLE, batchdata.getVegetableId());
		}

		if (!CateringServiceUtil.isNull(batchdata.getDessertId())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_DESSERT, batchdata.getDessertId());
		}

		if (!CateringServiceUtil.isNull(batchdata.getDessert1id())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_DESSERT1, batchdata.getDessert1id());
		}

		if (!CateringServiceUtil.isNull(batchdata.getSoupId())) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_SOUP, batchdata.getSoupId());
		}
		
//		自動新增調味料
//		移除帶入調味料之規則 2015.09.07 modify ellis
		Long seasoningId = querySeasoningDishId(session, batchdata.getKitchenId());
		if (!CateringServiceUtil.isNull(seasoningId)) {
			dishIdMap.put(CateringServiceCode.DISHTYPE_SEASONING, seasoningId);
		}

		for (String key : dishIdMap.keySet()) {
			if (dishIdMap.get(key) == 0) {
				continue;
			}
			saveDishBatchdataByDishid(session, batchdata.getBatchDataId(), batchdata.getKitchenId(), key, dishIdMap.get(key));

		}
	}

	public static void copyBatchdata(Session session, Batchdata newBatchdata, String menuDate, Integer schoolId) throws Exception {
		validBatchdata(newBatchdata);
		Long oldBatchdataId = newBatchdata.getBatchDataId();
		session.evict(newBatchdata);
		newBatchdata.setMenuDate(menuDate);
		newBatchdata.setSchoolId(schoolId);
		session.save(newBatchdata);
		// 找食材的batchdata也同時copy 到新batchdataId上
		Criteria criteriaIngredient = session.createCriteria(Ingredientbatchdata.class);
		criteriaIngredient.add(Restrictions.eq("batchDataId", oldBatchdataId));
		List Ingredients = criteriaIngredient.list();
		Iterator<Ingredientbatchdata> iteratorIngredient = Ingredients.iterator();
		// 查詢是否有菜單就copy 一份到target 日期
		while (iteratorIngredient.hasNext()) {
			Ingredientbatchdata igdb = iteratorIngredient.next();
			session.evict(igdb);
			igdb.setBatchDataId(newBatchdata.getBatchDataId());
			igdb.setIngredientBatchId(null);
			session.save(igdb);
		}
	}

	/*
	 * public static Batchdata updateBatchdata(Session session,Batchdata
	 * batchdata){ session.update(batchdata);
	 * HibernateUtil.deleteIngredientbatchdataByBatchdataId(session,
	 * batchdata.getBatchDataId());
	 * HibernateUtil.saveIngredientbatchdataByBatchdataId(session,
	 * batchdata.getBatchDataId() ); return batchdata; }
	 */
	public static Dish saveNewDish(Session session, Dish dish) {
		session.save(dish);
		return dish;
	}

	// 更新菜色圖檔 20140311 KC
	public static Integer updateDishImgpathByDishId(Session session, Long dishId, String path) {
		String HQL = "update Dish set picturePath=:path where dishId=:dishId";
		try {
			Query query = session.createQuery(HQL);
			query.setParameter("dishId", dishId);
			query.setParameter("path", path);
			return query.executeUpdate();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return -1;
		}

	}

	public static void modifyOldDishTypeToNewType(DishBatchData dishbatchdata){
		switch (dishbatchdata.getDishType()){
		case "MainFoodId" :
			dishbatchdata.setDishType("1");
			dishbatchdata.setDishOrder(10);
			break;
		case "MainFood1Id" :
			dishbatchdata.setDishType("1");
			dishbatchdata.setDishOrder(11);
			break;
		case "MainDishId" :
			dishbatchdata.setDishType("2");
			dishbatchdata.setDishOrder(12);
			break;
		case "MainDish1Id" :
			dishbatchdata.setDishType("2");
			dishbatchdata.setDishOrder(13);
			break;
		case "MainDish2Id" :
			dishbatchdata.setDishType("2");
			dishbatchdata.setDishOrder(14);
			break;
		case "MainDish3Id" :
			dishbatchdata.setDishType("2");
			dishbatchdata.setDishOrder(15);
			break;
		case "SubDish1Id" :
			dishbatchdata.setDishType("3");
			dishbatchdata.setDishOrder(16);
			break;
		case "SubDish2Id" :
			dishbatchdata.setDishType("3");
			dishbatchdata.setDishOrder(17);
			break;
		case "SubDish3Id" :
			dishbatchdata.setDishType("3");
			dishbatchdata.setDishOrder(18);
			break;
		case "SubDish4Id" :
			dishbatchdata.setDishType("3");
			dishbatchdata.setDishOrder(19);
			break;
		case "SubDish5Id" :
			dishbatchdata.setDishType("3");
			dishbatchdata.setDishOrder(20);
			break;
		case "SubDish6Id" :
			dishbatchdata.setDishType("3");
			dishbatchdata.setDishOrder(21);
			break;
		case "VegetableId" :
			dishbatchdata.setDishType("4");
			dishbatchdata.setDishOrder(22);
			break;
		case "SoupId" :
			dishbatchdata.setDishType("6");
			dishbatchdata.setDishOrder(23);
			break;
		case "DessertId" :
			dishbatchdata.setDishType("5");
			dishbatchdata.setDishOrder(24);
			break;
		case "Dessert1Id" :
			dishbatchdata.setDishType("5");
			dishbatchdata.setDishOrder(25);
			break;
		case "Seasoning" :
			//調味料，暫時存"Seasoning"，以利判斷。
			dishbatchdata.setDishOrder(26);
			break;
		}
	}
	
	// 存dishbatchdata by dishid KC
	public static Integer saveDishBatchdataByDishid(Session session, Long batchdataId, Integer kitchenId, String dishType, Long dishId) {
		DishBatchData dishbatchdata;// =new DishBatchData();
		DishBatchDataDAO dao = new DishBatchDataDAO();
		dao.setSession(session);
		dishbatchdata = dao.getSpecifiedDish(batchdataId, dishType);
		if (dishbatchdata == null) {
			dishbatchdata = new DishBatchData();
			dishbatchdata.setDishBatchDataId(0);
			dishbatchdata.setBatchDataId(batchdataId);
		}

		try {
			dishbatchdata.setDishId(dishId);
			String dishName = HibernateUtil.queryDishNameById(session, dishId);
			String dishShowname = HibernateUtil.queryDishShownameById(session, dishId);
			dishbatchdata.setDishName(dishName);
			dishbatchdata.setDishShowName(dishShowname);
			dishbatchdata.setDishType(dishType);
			dishbatchdata.setUpdateDateTime(CateringServiceUtil.getCurrentTimestamp());
			//因應幼兒園功能，dishorder進行排列 modify 20150826
			modifyOldDishTypeToNewType(dishbatchdata);
			dao.save(dishbatchdata);
			// _printMsg("新表格菜色儲存成功");
		} catch (Exception ex) {
			// 表格更換過渡期暫時不做exception拋出 20140318 KC
			ex.printStackTrace();
			// _printMsg("新表格菜色無法存  "+dishInfo.getValue()+":"+ex.getMessage());
		}
		return 0;
	}

	// 刪除dishbatchdata KC
	public static void deleteDishbatchdataByDishbatchdataId(Session session, Integer dishbatchdataId) {
		DishBatchData dishbatchdata;// =new DishBatchData();
		DishBatchDataDAO dao = new DishBatchDataDAO();
		dao.setSession(session);
		try {
			dishbatchdata = dao.getDishbatchdataByDishbatchdataId(dishbatchdataId);
			dao.delete(dishbatchdata);
		} catch (Exception ex) {
			//
		}
	}

	public static void deleteDishbatchdataByBatchdataId(Session session, Long batchdataId) {
		DishBatchData dishbatchdata;// =new DishBatchData();
		DishBatchDataDAO dao = new DishBatchDataDAO();
		dao.setSession(session);
		try {
			dao.deleteDishBatchDataByBatchdataId(batchdataId);
		} catch (Exception ex) {
			//
		}
	}

	public static void deleteDishbatchdataNotInDishlist(Session session, Long batchdataId, List<Long> dishIdList) {
		DishBatchDataDAO dao = new DishBatchDataDAO();
		dao.setSession(session);
		try {
			dao.deleteDishbatchdataNotInDishbatchdataIdList(batchdataId, dishIdList);
		} catch (Exception ex) {
			throw ex;

		}
	}

	// 20140523 Raymond 查詢菜單是否存在
	public static boolean isBatchDataExist(Session session, Batchdata batchdata) {
		String hql = "FROM Batchdata a WHERE a.schoolId = :schoolId " 
				+ "AND a.kitchenId = :kitchenId " 
				+ "AND a.menuDate = :menuDate " 
				+ "AND a.lotNumber = :lotNumber "
				+ "AND a.menuType = :menuType ";

		Query query = session.createQuery(hql);
		query.setParameter("schoolId", batchdata.getSchoolId());
		query.setParameter("kitchenId", batchdata.getKitchenId());
		query.setParameter("menuDate", batchdata.getMenuDate());
		query.setParameter("lotNumber", batchdata.getLotNumber());
		//增加判斷菜單類型 (0:早點 1:午餐 2:點心) add by Ellis 20150930
		query.setParameter("menuType", batchdata.getMenuType());

		if (query.list() != null && query.list().size() > 0) {
			return true;// throw exception
		} else {
			return false;
		}
	}

	//20151120 chu 查詢缺漏資料BY "SID"----------------------start
	//查菜單無食材
	public static List<Object[]> queryNullIngredientDataByKitchenAndTime_V2(Session session, String begDate, String endDate,Integer sid, Integer mType){//, concat('負責人: ', k.ownner,'. ','電話: ', k.tel,'. ','E-MAIL: ',k.email,'. ') 
		String hql = " select s.schoolName, b.menuDate,b.menuType,k.kitchenName ,d.DishName ,group_concat(i.ingredientName),b.batchDataId from Batchdata b,DishBatchData d,School s ,Kitchen k,Ingredientbatchdata  i where  "
				+ "  b.batchDataId=d.BatchDataId and b.menuDate between :startDate and :endDate  "
				+ " and b.schoolId=s.schoolId  " + "and s.schoolId = :sid" + " and i.batchDataId=b.batchDataId "
				+ " and i.dishId = d.DishId " + " and  i.stockDate is null and b.enable = 1 "
				+ " and k.kitchenId=b.kitchenId"
				+ " and d.DishType <> 'Seasoning' and (b.srcType is null or b.srcType = '') "
				+ " and b.enable = 1 ";
		if (mType != null) {
			hql += " and b.menuType= :mType ";
		}
		hql+=" group by s.schoolName,b.menuDate,k.kitchenName,b.batchDataId,d.DishName " + " order by b.menuDate asc , s.schoolName   ";
		Query queryObj = session.createQuery(hql);
		queryObj.setParameter("startDate", begDate);
		queryObj.setParameter("endDate", endDate);
		queryObj.setParameter("sid", sid);
		if (mType != null) {
			queryObj.setParameter("mType", mType);
		}
		List<Object[]> result = queryObj.list();
		return result;
	}
	//食材資料未完整
	public static List<Object[]> queryNullIngredientBySchoolAndTime_V2(Session session, String begDate, String endDate, Integer sid, Integer mType) {
		String hql = " select s.schoolName, b.menuDate,b.menuType,k.kitchenName ,group_concat(d.DishName) "
				+ " ,b.batchDataId from Batchdata b,DishBatchData d,School s ,Kitchen k "
				+ " where b.batchDataId=d.BatchDataId and b.menuDate between :startDate and :endDate"
				+ " and b.schoolId=s.schoolId " + "and s.schoolId = :sid"
				+ " and d.DishId not in  (select dishId from Ingredientbatchdata  i where i.batchDataId=b.batchDataId ) "
				+ " and d.DishType <> 'Seasoning'"
				+ " and k.kitchenId=b.kitchenId"
				+ " and (b.srcType is null or b.srcType = '' ) "
				+ " and b.enable = 1 ";
		if (mType != null) {
			hql += " and b.menuType= :mType ";
		}
		hql+="group by s.schoolName,b.menuDate,k.kitchenName,b.batchDataId " + " order by b.menuDate asc ";
		Query queryObj = session.createQuery(hql);
		queryObj.setParameter("startDate", begDate);
		queryObj.setParameter("endDate", endDate);
		queryObj.setParameter("sid", sid);
		if (mType != null) {
			queryObj.setParameter("mType", mType);
		}
		List<Object[]> result = queryObj.list();
		return result;
	}
	//無調味料
	public static List<Object[]> queryNullSeasoning_V2(Session session, String begDate, String endDate, Integer sid, Integer mType) {
		String hql = " select group_concat(s.schoolName), b.menuDate, k.kitchenId, k.kitchenName, b.menuType FROM Batchdata b, School s, Kitchen k where "
				+ " b.schoolId=:sid and k.kitchenId = b.kitchenId and b.menuDate between :startDate and :endDate and b.enable = '1' "
				+ " and b.batchDataId not in (select batchDataId from Ingredientbatchdata "
				+ " where  dishId  in (select dishId from Dish where dishName = '"+CateringServiceCode.CODETYPE_SEASONING+"' and kitchenId = b.kitchenId)) " 
				+ " and s.schoolId = b.schoolId  and ( b.srcType is  null or b.srcType = '') ";
		if (mType != null) {
			hql += " and b.menuType= :mType ";
		}
		hql+=" group by b.menuDate order by b.menuDate asc  ";
		Query queryObj = session.createQuery(hql);
		queryObj.setParameter("startDate", begDate);
		queryObj.setParameter("endDate", endDate);
		queryObj.setParameter("sid", sid);
		if (mType != null) {
			queryObj.setParameter("mType", mType);
		}
		List<Object[]> result = queryObj.list();

		return result;
	}
	//DISH -查-> PIC
	public static List<Object[]> queryDishList_V2(Session session, String begDate, String endDate, Integer sid, Integer mType) {
		String hql = " select distinct dbt.DishId , dbt.DishName, b.menuDate, b.menuType, b.kitchenId, k.kitchenName FROM DishBatchData dbt, Batchdata b, Kitchen k where " 
				+ " b.menuDate between :startDate and :endDate " 
				+ " and b.schoolId = :sid"
				+ " and k.kitchenId = b.kitchenId"
				+ " and dbt.DishType <> 'Seasoning' " 
				+ " and dbt.BatchDataId = b.batchDataId and b.enable = 1";
		if (mType != null) {
			hql += " and b.menuType= :mType ";
		}
		Query queryObj = session.createQuery(hql);
		queryObj.setParameter("startDate", begDate);
		queryObj.setParameter("endDate", endDate);
		queryObj.setParameter("sid", sid);
		if (mType != null) {
			queryObj.setParameter("mType", mType);
		}
		List<Object[]> result = queryObj.list();
		
		if(result.size()<1){
			String hql2 = " select distinct d.dishId , d.dishName, b.menuDate, b.menuType, b.kitchenId, k.kitchenName FROM Ingredientbatchdata ibt, Batchdata b,Kitchen k, Dish d where " 
					+ " b.menuDate between :startDate and :endDate "
					+ " and b.schoolId = :sid "
					+ " and d.kitchenId = b.kitchenId "
					+ " and d.dishName <> '"+CateringServiceCode.CODETYPE_SEASONING+"' "
					+ " and ibt.dishId = d.dishId "
					+ " and ibt.batchDataId = b.batchDataId and b.enable = 1 ";
			if (mType != null) {
				hql2 += " and b.menuType= :mType ";
			}
			queryObj = session.createQuery(hql2);
			queryObj.setParameter("startDate", begDate);
			queryObj.setParameter("endDate", endDate);
			queryObj.setParameter("sid", sid);
			if (mType != null) {
				queryObj.setParameter("mType", mType);
			}
			List<Object[]> result2 = queryObj.list();
			return result2;
		}
		return result;

	}
	//由schoolId查每餐的kitchen資料
	public static List<Object[]> queryKitchenContent(Session session,String begDate, String endDate, Integer sid, Integer mType){
		String hql = "select s.schoolName, k.kitchenName, b.menuType, concat('負責人: ', k.ownner,' ','電話: ', k.tel,' ','E-MAIL: ',k.email,' ') FROM Batchdata b, Kitchen k, School s where"
				+ " b.menuDate between :startDate and :endDate"
				+ " and b.schoolId=:sid and s.schoolId=b.schoolId" 
				+ " and b.kitchenId = k.kitchenId"
				+ " and b.enable = 1 and b.menuType = :mType";
		Query queryObj = session.createQuery(hql);
		queryObj.setParameter("startDate", begDate);
		queryObj.setParameter("endDate", endDate);
		queryObj.setParameter("sid", sid);
		queryObj.setParameter("mType", mType);
		List<Object[]> result = queryObj.list();
		return result;
	}
	//20151120 chu---------------------------------------------------end
	
	// 20140606 Ric 檢查有菜色無食材    20140923 KC 加入排除介接縣市來的資料 (srcType=A
	// 20150828 Ellis 排除enable=0之菜單
	public static List<Object[]> queryNullIngredientBySchoolAndTime(Session session, String begDate, String endDate, Integer countyId, Integer kid) {
//		String hql = " select s.schoolName, b.menuDate, k.kitchenName ,group_concat(d.DishName) "
//				+ " ,b.batchDataId from Batchdata b,DishBatchData d,School s ,Kitchen k "
//				+ " where " + " b.batchDataId=d.BatchDataId and b.menuDate between :startDate and :endDate and d.DishName !=:seasoning "
//				+ " and b.schoolId=s.schoolId " + " "
//				+ " and d.DishId not in  (select dishId from Ingredientbatchdata  i where i.batchDataId=b.batchDataId ) "
//				+ " and k.kitchenId=b.kitchenId and b.srcType is null ";
		String hql = " select s.schoolName, b.menuDate, k.kitchenName ,group_concat(d.DishName) "
				// #13525 : 缺漏資料新增"餐別"欄位 b.menuType
				+ " ,b.batchDataId, b.menuType from Batchdata b,DishBatchData d,School s ,Kitchen k "
				+ " where b.batchDataId=d.BatchDataId and b.menuDate between :startDate and :endDate and d.DishName !=:seasoning "
				+ " and b.schoolId=s.schoolId " + " "
				+ " and d.DishId not in  (select dishId from Ingredientbatchdata  i where i.batchDataId=b.batchDataId ) "
				// + " and k.kitchenId=b.kitchenId and b.menuType= 1 "
				// #13411 : 新增 早點、點心、調味料及晚餐 於 「缺漏資料查詢」
				+ " and k.kitchenId=b.kitchenId and b.menuType in (0,1,2,3) "
				+ " and (b.srcType is null or b.srcType = '' ) "
				+ " and b.enable = 1 ";
		//if (countyId != null && countyId != 0) {
		if ((! CateringServiceCode.AUTHEN_SUPER_COUNTY_INT.equals(countyId) ) && countyId != null && countyId != 0) {
			hql += " and s.countyId=:countyId ";
		}else{
			System.out.println("test");
		}
		if (kid != null && kid != 0) {
			hql += " and b.kitchenId=:kid ";
		}
		hql += " group by s.schoolName,b.menuDate,k.kitchenName,b.batchDataId " + " order by b.menuDate asc ";
		Query queryObj = session.createQuery(hql);
		queryObj.setParameter("startDate", begDate);
		queryObj.setParameter("endDate", endDate);
		queryObj.setParameter("seasoning", CateringServiceUtil.ColumnNameOfSeasoning);
		//修原本錯的權限判斷  20140911 KC  20140916 KC 前端就是丟0來阿...
		if ((! CateringServiceCode.AUTHEN_SUPER_COUNTY_INT.equals(countyId) ) && countyId != null && countyId != 0) {
			queryObj.setParameter("countyId", countyId);
		}
		if (kid != null && kid != 0) {
			queryObj.setParameter("kid", kid);
		}

		List<Object[]> result = queryObj.list();

		return result;

	}

	// 20140612 Ric 檢查調味料       KC 加入排除介接縣市來的資料 (srcType=A
	public static List<Object[]> queryNullSeasoning(Session session, String begDate, String endDate,Integer countyId, Integer kid) {
//		String hql = " select group_concat(s.schoolName), b.menuDate FROM Batchdata b, School s where " + " b.kitchenId=:kid and b.menuDate between :startDate and :endDate "
//				+ " and b.batchDataId not in (select batchDataId from Ingredientbatchdata where  dishId  in (select dishId from Dish where dishName = '調味料' and kitchenId = :kid)) " 
//				+ " and s.schoolId = b.schoolId  and b.srcType is null  ";
		//
		String hql = " select group_concat(s.schoolName), b.menuDate FROM Batchdata b, School s where "
				// + " b.kitchenId=:kid and b.menuDate between :startDate and :endDate and b.enable = '1' and b.menuType = 1 "
				// #13411 : 新增 早點、點心、調味料及晚餐 於 「缺漏資料查詢」
				+ " b.kitchenId=:kid and b.menuDate between :startDate and :endDate and b.enable = '1' and b.menuType in (0,1,2,3) "
				+ " and b.batchDataId not in (select batchDataId from Ingredientbatchdata "
				+ " where  dishId  in (select dishId from Dish where dishName = '"+CateringServiceCode.CODETYPE_SEASONING+"' and kitchenId = :kid)) " 
				+ " and s.schoolId = b.schoolId  and ( b.srcType is  null or b.srcType = '') ";
		
		
		if (countyId != null && countyId != 0) {
			hql += " and s.countyId=:countyId ";
		}
		hql += " group by b.menuDate order by b.menuDate asc  ";
		
		Query queryObj = session.createQuery(hql);
		queryObj.setParameter("startDate", begDate);
		queryObj.setParameter("endDate", endDate);
		queryObj.setParameter("kid", kid);
		if (countyId != null && countyId != 0) {
			queryObj.setParameter("countyId", countyId);
		}
		List<Object[]> result = queryObj.list();

		return result;

	}

	// 20140612 Ric 輸出所有菜色編號與名稱
	public static List<Object[]> queryDishList(Session session, String begDate, String endDate, Integer kid) {
		String hql = " select distinct dbt.DishId , dbt.DishName FROM DishBatchData dbt, Batchdata b where " 
				+ " b.menuDate between :startDate and :endDate " + " and kitchenId = :kid and dbt.DishType <> 'Seasoning' " 
				// + " and dbt.BatchDataId = b.batchDataId and b.menuType = 1 and b.enable = 1 ";
				// #13411 : 新增 早點、點心、調味料及晚餐 於 「缺漏資料查詢」
				+ " and dbt.BatchDataId = b.batchDataId and b.menuType in (0,1,2,3) and b.enable = 1 ";
		Query queryObj = session.createQuery(hql);
		queryObj.setParameter("startDate", begDate);
		queryObj.setParameter("endDate", endDate);
		queryObj.setParameter("kid", kid);
		List<Object[]> result = queryObj.list();
		if(result.size()<1){
			String hql2 = " select distinct d.dishId , d.dishName FROM Ingredientbatchdata ibt, Batchdata b, Dish d where " 
					+ " b.menuDate between :startDate and :endDate "
					+ " and b.kitchenId = :kid "
					+ " and d.kitchenId = b.kitchenId "
					+ " and d.dishName <> '"+CateringServiceCode.CODETYPE_SEASONING+"' "
					+ " and ibt.dishId = d.dishId "
					// + " and ibt.batchDataId = b.batchDataId and b.menuType = 1 and b.enable = 1 ";
					// #13411 : 新增 早點、點心、調味料及晚餐 於 「缺漏資料查詢」
					+ " and ibt.batchDataId = b.batchDataId and b.menuType in (0,1,2,3) and b.enable = 1 ";
			queryObj = session.createQuery(hql2);
			queryObj.setParameter("startDate", begDate);
			queryObj.setParameter("endDate", endDate);
			queryObj.setParameter("kid", kid);
			List<Object[]> result2 = queryObj.list();
			return result2;
		}
		return result;

	}

	
	
	// 20140623 Ric 食材資料未完整填寫(沒有進貨日期)  20141001 KC 加入排除介接縣市來的資料 (srcType=A
	public static List<Object[]> queryNullIngredientDataByKitchenAndTime(Session session, String begDate, String endDate, Integer kid) {
//		String hql = " select s.schoolName, b.menuDate, k.kitchenName ,d.DishName ,group_concat(i.ingredientName)  ,b.batchDataId from Batchdata b,DishBatchData d,School s ,Kitchen k,Ingredientbatchdata  i where  "
//				+ "  b.batchDataId=d.BatchDataId and b.menuDate between :startDate and :endDate  " + " and b.schoolId=s.schoolId  " + " and i.batchDataId=b.batchDataId " + " and i.dishId = d.DishId " + " and  i.stockDate is null  "
//				+ " and k.kitchenId=b.kitchenId " + " and b.kitchenId=:kid  and d.DishType <> 'Seasoning' and b.srcType is null  " + " group by s.schoolName,b.menuDate,k.kitchenName,b.batchDataId,d.DishName " + " order by b.menuDate asc , s.schoolName   ";
		String hql = " select s.schoolName, b.menuDate, k.kitchenName ,d.DishName ,group_concat(i.ingredientName)  ,b.batchDataId from Batchdata b,DishBatchData d,School s ,Kitchen k,Ingredientbatchdata  i where  "
				+ "  b.batchDataId=d.BatchDataId and b.menuDate between :startDate and :endDate  "
				+ " and b.schoolId=s.schoolId  " + " and i.batchDataId=b.batchDataId "
				+ " and i.dishId = d.DishId " + " and  i.stockDate is null and b.enable = 1 "
				+ " and k.kitchenId=b.kitchenId "
				+ " and b.kitchenId=:kid  and d.DishType <> 'Seasoning' and (b.srcType is null or b.srcType = '') "
				+ " and b.enable = 1 "
				// + " and b.menuType = 1 "
				// #13411 : 新增 早點、點心、調味料及晚餐 於 「缺漏資料查詢」
				+ " and b.menuType in (0,1,2,3) "
				+ " group by s.schoolName,b.menuDate,k.kitchenName,b.batchDataId,d.DishName " + " order by b.menuDate asc , s.schoolName   ";
		Query queryObj = session.createQuery(hql);
		queryObj.setParameter("startDate", begDate);
		queryObj.setParameter("endDate", endDate);
		queryObj.setParameter("kid", kid);
		List<Object[]> result = queryObj.list();

		return result;

	}

	/*
	 * 20140711 Ric 透過Kid清除相關的菜單食材
	 */
	public static void deleteBatchdataAndIngredientBatchDataByKid(Session session, int kitchenId) {
		Criteria criteriaSp = session.createCriteria(Batchdata.class);
		criteriaSp.add(Restrictions.eq("kitchenId", kitchenId));
		List batchdataList = criteriaSp.list();
		Iterator<Batchdata> batchdataIterator = batchdataList.iterator();
		while (batchdataIterator.hasNext()) {
			Batchdata bd = batchdataIterator.next();
			HibernateUtil.deleteIngredientbatchdataByBatchdataId(session, bd.getBatchDataId());
			HibernateUtil.deleteDishbatchdataByBatchdataId(session, bd.getBatchDataId());

		}
	}

	/*
	 * 20140711 Ric
	 * 透過Kid刪除菜單，另外呼叫刪除菜單對應食材deleteBatchdataAndIngredientBatchDataByKid
	 */
	public static void deleteBatchdataByKid(Session session, int kitchenId) {
		Query query = session.createQuery("delete from Batchdata n where n.kitchenId = :id");
		query.setParameter("id", kitchenId);
		log.debug("deleteBatchdataByKid result:" + query.executeUpdate() + " ID:" + kitchenId);
		try {
			HibernateUtil.deleteBatchdataAndIngredientBatchDataByKid(session, kitchenId);
		} catch (Exception ex) {
			throw ex;

		}
	}

	/*
	 * 20140711 Ric 透過Kid刪除對應的所有供應商資料
	 */

	public static void deleteAllSupplierByKid(Session session, int kitchenId) {
		Query query = session.createQuery("delete from Supplier s where s.id.kitchenId = :id");
		query.setParameter("id", kitchenId);
		log.debug("deleteAllSupplierByKid result:" + query.executeUpdate() + " ID:" + kitchenId);

	}
	//20140729 Ric Email連同Userccount同步更新
	public static Integer updateUseraccountEmailByKitchenId(Session session, String email, int kitchenId) {
		String HQL = "update Useraccount set email=:email where kitchenId=:kitchenId";
		try {
			Query query = session.createQuery(HQL);
			query.setParameter("kitchenId", kitchenId);
			query.setParameter("email", email);
			return query.executeUpdate();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return -1;
		}

	}
	
	//20150930 Email更新時，多判斷帳號為何(因合作社與自設廚房的Kid是相同的，原本做法會蓋掉合作社Mail。)
		public static Integer updateUseraccountEmailByKitchenIdV2(Session session, String email, int kitchenId , String userName) {
			String HQL = "update Useraccount set email=:email where kitchenId=:kitchenId and username = :userName";
			try {
				Query query = session.createQuery(HQL);
				query.setParameter("kitchenId", kitchenId);
				query.setParameter("email", email);
				query.setParameter("userName", userName);
				return query.executeUpdate();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				return -1;
			}

		}
	/* 20140731 Ric 
	 * 從school中已county下找出所有供應的kitchen
	 */
	public static List<Object[]> queryKitchenListByCountyId(Session dbsession, Integer countyID) {
		String hql = " select distinct k.kitchenId, k.kitchenName from Kitchen k, Schoolkitchen sk, School s   "
				+ "  where s.countyId=:countyID  "
				+ " and sk.id.schoolId = s.schoolId and k.kitchenId = sk.id.kitchenId and k.kitchenType <> '007' and k.enable = 1 "
				+ " order by kitchenType  ";
		Query queryObj = dbsession.createQuery(hql);
		queryObj.setParameter("countyID", countyID);
		List<Object[]> result = queryObj.list();
		return result;
	}
	/*
	 * 檢查是否超過上傳時間  20140912 KC 
	 * */
	public static  Boolean isMenuUploadTime(Session session,String menuDate,Integer schoolId) throws Exception {
		Timestamp nowTS=CateringServiceUtil.getCurrentTimestamp();
		String limitString=SchoolAndKitchenUtil.queryUploadLimitTimeBySchoolid(session,schoolId);
		if ("".equals(limitString)){
			return true;
		}
		Boolean flagTimelimit= CateringServiceUtil.isUploadTime(CateringServiceUtil.convertStrToTimestamp("yyyy-MM-dd", menuDate),nowTS, limitString);
		return flagTimelimit;
	}

	//-------------+++ 預劃菜單
	//+----------------------------------+
	//+ 公用程式 
	//+ 此區之 method 為對資料表做查詢動作之公用程式
	//+ By Steven 2015.01.15
	//+----------------------------------+
	/**
	 * 依欄位Id 取得該DAO物件
	 * @param session
	 * @param objClass 物件之class
	 * @param byFieldIdName 欲查詢之欄位名稱(table中之欄位)
	 * @param fieldIdValue 欄位值
	 * @return Object Object，可適時的轉型成各自之dao物件。
	 */
	public static Object getObjectByFieldId(Session session, Class<?> objClass, String byFieldIdName, Integer fieldIdValue) {
		Object result = (Object) session.createCriteria(objClass).add(Restrictions.eq(byFieldIdName, fieldIdValue)).uniqueResult();
		return result;
	}
	/**
	 * 依欄位Id 取得該DAO物件
	 * @param session
	 * @param objClass 物件之class
	 * @param byFieldIdName 欲查詢之欄位名稱(table中之欄位)
	 * @param fieldIdValue 欄位值
	 * @return Object Object，可適時的轉型成各自之dao物件。
	 */
	public static Object getObjectByFieldId(Session session, Class<?> objClass, String byFieldIdName, Long fieldIdValue) {
		Object result = (Object) session.createCriteria(objClass).add(Restrictions.eq(byFieldIdName, fieldIdValue)).uniqueResult();
		return result;
	}

	/**
	 * 依某欄位，取出該DAO物件之List集合。
	 * @param session
	 * @param objClass 物件之class
	 * @param byFieldIdName 欲查詢之欄位名稱(table中之欄位)
	 * @param fieldIdValue 欄位值
	 * @return Object Object，可適時的轉型成各自之dao物件。
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> getObjectListByFieldId(Session session, Class<?> objClass, String byFieldIdName, Long fieldIdValue) {
		List<Object> result = session.createCriteria(objClass).add(Restrictions.eq(byFieldIdName, fieldIdValue)).list();
		return result;
	}

	/**
	 * 依某欄位(多條件型(即And))，取出該DAO物件之List集合。
	 * @param session
	 * @param objClass 物件之class
	 * @param conditionMap HashMap集合。conditionMap<欄位名稱, 欲查詢之值>
	 *        Key:為欲查詢table中欄位的名稱(因使用hibernate的Criteria故此欄位名稱大小寫須與xxx.hbm.xml設定檔中的id tag的name一致)。
	 *        Value:為欲查詢欄位的值。
	 * @return Object Object，可適時的轉型成各自之dao物件。
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> getObjectListByFieldId(Session session, Class<?> objClass, HashMap<String, ?> conditionMap) {
		Criteria criteria = session.createCriteria(objClass);
		for(String byFieldIdName: conditionMap.keySet()) {
			criteria.add(Restrictions.eq(byFieldIdName, conditionMap.get(byFieldIdName)));
		}
		List<Object> result = criteria.list();
		return result;
	}

	/**
	 * 依某欄位(多條件型(即And))，取出該DAO物件之List集合。
	 * @param session
	 * @param objClass 物件之class
	 * @param conditionMap HashMap集合。conditionMap<欄位名稱, 欲查詢之值>
	 *        Key: 為欲查詢table中欄位的名稱(因使用hibernate的Criteria故此欄位名稱大小寫須與xxx.hbm.xml設定檔中的id tag的name一致)。
	 *        Value: 為欲查詢欄位的值。
	 * @param orderByMap HashMap集合。orderByMap<欄位名稱, 排序方式>
	 *        Key: 為欲查詢table中欄位的名稱(因使用hibernate的Criteria故此欄位名稱大小寫須與xxx.hbm.xml設定檔中的id tag的name一致)。
	 *        Value: 為欲排序的類型(ASC/DESC)。
	 * @return Object Object，可適時的轉型成各自之dao物件。
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> getObjectListByFieldId(Session session, Class<?> objClass, HashMap<String, ?> conditionMap, HashMap<String, ?> orderByMap) {
		Criteria criteria = session.createCriteria(objClass);
		
		for(String byFieldIdName: conditionMap.keySet()) {
			criteria.add(Restrictions.eq(byFieldIdName, conditionMap.get(byFieldIdName)));
		}
		
		for(String orderByField: orderByMap.keySet()) {
			if(orderByMap.get(orderByField).toString().equalsIgnoreCase("ASC")) {
				criteria.addOrder(Order.asc(orderByField));
			} else if(orderByMap.get(orderByField).toString().equalsIgnoreCase("DESC")) {
				criteria.addOrder(Order.desc(orderByField));
			}
		}
		
		List<Object> result = criteria.list();
		return result;
	}	
	/**
	 * 處理分頁用之元件
	 * @param pageNum 欲顯示之頁數代碼
	 * @param pageLimit 每頁顯示幾筆資料
	 * @param processList 全部的資料
	 * @return List 該頁之各筆資料集
	 */
	public static HashMap<Integer, List> getObjList4PageResult(Integer pageNum, Integer pageLimit, 
			List<Object> processList) {
		int startCount = 0;
		int totalRow = processList.size();
		startCount = (pageNum - 1) * pageLimit;
		// 總共有幾頁
		int totalPage = totalRow%pageLimit==0 ? totalRow/pageLimit : totalRow/pageLimit+1;

		// 最後一頁顯示的筆數
		int remainder = totalRow%pageLimit==0 ? pageLimit : totalRow%pageLimit;
		
		HashMap<Integer, List> rtnHashMap = new HashMap<Integer, List>();
		List<Object> rtnList = new ArrayList<Object>();
		if( pageNum<totalPage ) {
			for(int i=startCount; i<startCount+pageLimit; i++) {
				rtnList.add((Object) processList.get(i));
			}
		} else if( pageNum==totalPage ) {
			for(int i=startCount; i<startCount+remainder; i++) {
				rtnList.add((Object) processList.get(i));
			}
		}

		rtnHashMap.put(pageNum, rtnList);
		
		//System.out.println("=======================================================");
		//System.out.println("顯示資料的頁碼:"+pageNum+", 每頁顯示"+pageLimit+"筆資料, 總共有"+totalRow+"筆資料");
		//System.out.println("總頁數:"+totalPage+", 開始筆數:"+startCount);
		List<Object> tmpLst = rtnHashMap.get(pageNum);
		//System.out.println("顯示資料筆數:"+tmpLst.size());
		//System.out.println("********************************************************");
		return rtnHashMap;
	}
	//+++------------- 預劃菜單
}
