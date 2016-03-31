package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Query;
import org.iii.ideas.catering_service.dao.CounterDAO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class CustomerQueryKitchenBySchoolAndDate  extends AbstractApiInterface<CustomerQueryKitchenBySchoolAndDateRequest, CustomerQueryKitchenBySchoolAndDateResponse>{

	
	@Override
	public void process() throws NamingException, ParseException {
		// TODO Auto-generated method stub
		Integer schoolId = this.requestObj.getSid();
		String menuDate = this.requestObj.getDate();
		Integer menuType = this.requestObj.getMtype();
		String queryStr = "";
		
		if(menuType != null){
			queryStr = " and b.menuType = :menuType ";
			System.out.println("menuType: "+menuType);
		}

		Timestamp currentTS =  CateringServiceUtil.getCurrentTimestamp();
		String currentTSStr = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", currentTS);
		menuDate = menuDate.replace("-", "/");
		menuDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", menuDate));
		String HQL ="select b.batchDataId, k.kitchenName from Batchdata b, Kitchen k where b.kitchenId = k.kitchenId  "
				+ " and b.menuDate = :menuDate "
//				+ " and b.menuDate <= :currentDate "
				+ " and b.schoolId = :schoolId "
				+ queryStr
				+ " and b.enable = 1";
		Query query = dbSession.createQuery(HQL);
		
		query.setParameter("schoolId", schoolId);
		query.setParameter("menuDate", menuDate);
		if(menuType != null){
			query.setParameter("menuType", menuType);
			System.out.println("menuType: "+menuType);
		}
//		query.setParameter("currentDate", currentTSStr);
       
		List results = query.list();
        Iterator<Object[]> iterator = results.iterator();
        while(iterator.hasNext()){
			YearMonthObject ym = new YearMonthObject();
			Object[] obj = iterator.next();
			CustomerQueryKitchenObject kitchen = new CustomerQueryKitchenObject();
			kitchen.setKitchenName((String)obj[1]);
			kitchen.setMid((Long)obj[0]);
			this.responseObj.getKitchen().add(kitchen);
		}
        this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
		
		//logging to file  20140716 Ric
		CounterDAO dao= new CounterDAO();
		dao.openSessionFactory();
		try{
			dao.increaseCounterByFuncNameAndDate("customerQueryKitchenBySchoolAndDate", dao.getTodayString(),schoolId);
		}catch (Exception ex){
			CateringServiceUtil.weblogToFile("customerQueryKitchenBySchoolAndDate", "null", null, "sid:"+schoolId+", failed to add counter!");
		}
		dao.closeSession();
	}

}
