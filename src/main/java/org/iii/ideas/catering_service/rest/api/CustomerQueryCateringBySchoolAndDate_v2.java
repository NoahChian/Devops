package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Query;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.CounterDAO;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class CustomerQueryCateringBySchoolAndDate_v2 extends AbstractApiInterface<CustomerQueryCateringBySchoolAndDate_v2Request, CustomerQueryCateringBySchoolAndDate_v2Response> {

	@Override
	public void process() throws NamingException, ParseException {
		int sid = this.requestObj.getSid();
		String date = this.requestObj.getDate();
		Integer kid = this.getKitchenId();

		if (this.getKitchenId() == null) {
			kid = -1;
		}
		String userType = "";
		if (this.getUserType() != null) {
			userType = this.getUserType();
		}

		String countyType = AuthenUtil.getCountyTypeByUsername(this.getUsername());

		String hql = "from Batchdata m, Kitchen k ,School s where m.kitchenId = k.kitchenId and s.schoolId=m.id.schoolId and (m.id.menuDate between :startDate and :endDate )  and m.id.schoolId=:sid";

		if (!CateringServiceCode.AUTHEN_SUPER_COUNTY_TYPE.equals(countyType) && CateringServiceCode.AUTHEN_SUPER_KITCHEN.equals(kid.toString())) { // 地方政府
			hql = hql + " and s.countyId=:cid ";
		}

		if (!CateringServiceCode.AUTHEN_SUPER_KITCHEN.equals(kid.toString()) && kid != -1 && !CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)) {
			// 一般登入身分 //20140501加入type=007判斷 KC
			hql = hql + " and m.kitchenId=:kid ";
		}

		hql = hql + " and m.enable = 1"; //20150505 shine add 加上Enable條件,以找出最後的菜單
		
		// 排序
		hql = hql + " order by m.id.menuDate desc, m.menuType ";  //20150507 shine mod 加上依菜單類別排序 

		Timestamp currentTS = CateringServiceUtil.getCurrentTimestamp();
		String currentTSStr = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", currentTS);
		String currentMonth = CateringServiceUtil.converTimestampToStr("yyyy/MM", currentTS);// 20140520
		
		if (CateringServiceUtil.isEmpty(date)) {
			date = currentMonth;
		}

		Query query = dbSession.createQuery(hql);

		query.setParameter("sid", sid);
/** 20141208 尚未登入仍可看到全部菜單 */
//		if (this.getUsername() == null) {
//			query.setParameter("startDate", date + "/01");
//			if (date.equals(currentMonth))// 20140520 Raymond 查詢年月與系統年月比較
//											// 相同則只會查詢到系統當天的日期
//				query.setParameter("endDate", currentTSStr);
//			else
//				query.setParameter("endDate", date + "/31");
//		} else {
			query.setParameter("startDate", date + "/01");
			query.setParameter("endDate", date + "/31");
//		}

		if (!CateringServiceCode.AUTHEN_SUPER_COUNTY_TYPE.equals(countyType) && CateringServiceCode.AUTHEN_SUPER_KITCHEN.equals(kid.toString())) { // 地方政府
			query.setParameter("cid", AuthenUtil.getCountryNumByCountyType(countyType));
		}
		if (!CateringServiceCode.AUTHEN_SUPER_KITCHEN.equals(kid.toString()) && kid != -1 && !CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(userType)) {
			// 一般登入身分 //20140501加入type=007判斷 KC
			query.setParameter("kid", kid);
		}

		List results = query.list();
		Iterator<Object[]> iterator = results.iterator();
		this.responseObj.setYear(date.substring(0, 4));
		this.responseObj.setMonth(String.valueOf((Integer.valueOf(date.substring(5, 7)) - 1)));
		while (iterator.hasNext()) {
			SupplierAndMenuDateObject sam = new SupplierAndMenuDateObject();
			Object[] obj = iterator.next();
			Batchdata menu = (Batchdata) obj[0];
			Kitchen kt = (Kitchen) obj[1];
			sam.setMid(menu.getBatchDataId().toString());
			sam.setStart(menu.getMenuDate());
			sam.setTitle(kt.getKitchenName());
			sam.setKitchenId(menu.getKitchenId());

			//20150504 shine add 增加記錄菜單類別
			sam.setMenuType(menu.getMenuType());
			
			try {
				String foodStr = "";
				/*
				 * String mainFood =
				 * HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getMainFoodId());
				 * foodStr+=(mainFood.equals(""))?"":mainFood; String
				 * mainFood1id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getMainFood1id());
				 * foodStr+=(mainFood1id.equals(""))?"":mainFood1id; String
				 * mainDishId = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getMainDishId());
				 * foodStr+=(mainDishId.equals(""))?"":mainDishId+","; String
				 * mainDish1Id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getMainDish1id());
				 * foodStr+=(mainDish1Id.equals(""))?"":mainDish1Id+","; String
				 * mainDish2Id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getMainDish2id());
				 * foodStr+=(mainDish2Id.equals(""))?"":mainDish2Id+","; String
				 * mainDish3Id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getMainDish3id());
				 * foodStr+=(mainDish3Id.equals(""))?"":mainDish3Id+","; String
				 * subDish1id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getSubDish1id());
				 * foodStr+=(subDish1id.equals(""))?"":subDish1id+","; String
				 * subDish2id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getSubDish2id());
				 * foodStr+=(subDish2id.equals(""))?"":subDish2id+","; String
				 * subDish3id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getSubDish3id());
				 * foodStr+=(subDish3id.equals(""))?"":subDish3id+","; String
				 * subDish4id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getSubDish4id());
				 * foodStr+=(subDish4id.equals(""))?"":subDish4id+","; String
				 * subDish5id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getSubDish5id());
				 * foodStr+=(subDish5id.equals(""))?"":subDish5id+","; String
				 * subDish6id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getSubDish6id());
				 * foodStr+=(subDish6id.equals(""))?"":subDish6id+","; String
				 * vegetable = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getVegetableId());
				 * foodStr+=(vegetable.equals(""))?"":vegetable+","; String
				 * dessert = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getDessertId());
				 * foodStr+=(dessert.equals(""))?"":dessert+","; String
				 * dessert1id = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getDessert1id());
				 * foodStr+=(dessert1id.equals(""))?"":dessert1id+","; String
				 * soup = HibernateUtil.queryDishNameById(this.dbSession,
				 * menu.getSoupId()); foodStr+=(soup.equals(""))?"":soup;
				 * foodStr = StringUtils.trimTrailingCharacter(foodStr, ',');
				 */
				sam.setTip(foodStr);

			} catch (Exception e) {
				sam.setTip("");
			}
			this.responseObj.getEvents().add(sam);
		}

		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);

		// logging to file 20140303 KC
		CounterDAO dao = new CounterDAO();
		dao.openSessionFactory();
		try {
			dao.increaseCounterByFuncNameAndDate("customerQueryCateringBySchoolAndDate", dao.getTodayString(), sid);
		} catch (Exception ex) {
			CateringServiceUtil.weblogToFile("customerQueryCateringBySchoolAndDate", "null", null, "sid:" + sid + ", failed to add counter!");
		}
		dao.closeSession();

	}
}
