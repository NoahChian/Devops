package org.iii.ideas.catering_service.rest.excel.create;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
//import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata;
//import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdataDAO;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata2;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdataDAO2;
import org.iii.ideas.catering_service.rest.api.QueryManageList;
//import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter2;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.rest.api.QueryStatistic;


public class DishCount implements IGenerateExcel {
	
	private Integer countyid;
	private Integer schoolid;
	private String startDate;
	private String endDate;
	
	public DishCount(String startDate, String endDate, Integer countyid, Integer schoolid) {
		this.countyid = countyid;
		this.schoolid = schoolid;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		List<Object[]> result=null;
		
		data.put("1",
				new Object[] { "縣市","學校","菜色總量" });
		
		QueryStatistic q1 = new QueryStatistic();
		String sqlStr = q1.getDishCountSql(startDate, endDate, Integer.toString(countyid), Integer.toString(schoolid));
		result= q1.queryJDBC(sqlStr);
			
//		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
//		Session session = sessionFactory.openSession();
//		Query query = session.createQuery(sqlStr);
//		List results = query.list();
		
		//vsBO = vsmbDAO.queryMenu( schoolid, countyid, begDate, endDate);
		
		int row = 2;
//		Iterator<ViewSchoolMenuWithBatchdata2> iterator = vsBO.iterator();
		
		for(int i=0 ; i <result.size();i++){
			Object[] temp = (Object[])result.get(i);			
			data.put(
			String.valueOf(row),
			temp);
			row++;
		}
		
//		while (iterator.hasNext()) {
//			ViewSchoolMenuWithBatchdata2 menu = iterator.next();
//			
//			String comboname ="";
//			if(menu.getKitchenname() != null && !"".equals(menu.getKitchenname())){
//				comboname = menu.getKitchenname();
//			} else if(menu.getRestaurantname() != null && !"".equals(menu.getRestaurantname())){
//				comboname = menu.getRestaurantname();
//			}
//			
//			data.put(
//					String.valueOf(row),
//					new Object[] {
//							menu.getMenudate(),
//							menu.getMenutype(),
//							menu.getDishname(),
//							menu.getIngredientname(),
//							menu.getStockdate(),
//							menu.getIngredientquantity() + " " +menu.getIngredientunit(),
//							menu.getSuppliername(),
//							comboname
//							});
//			row++;
//		}
//		session.close();
		return data;
	}
	
	private String setParameter(String sql,String key,Object value){
		if (value.getClass().toString().equals(String.class.toString())){
			value="'"+value+"'";
		}
		sql=sql.replaceFirst(":"+key.toUpperCase(), value.toString());
		return sql;
	}
}
