package org.iii.ideas.catering_service.rest.excel.create;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Criteria;
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

public class CMenuList implements IGenerateExcel {
	
	private Integer kitchenid;
	private Integer restaurantid;
	private Integer schoolid;
	private Integer countyid;
	private Integer queryLimit;
	private String begDate;
	private String endDate;
	private String userType;
	private String username;
	
	public CMenuList(String begDate, String endDate,Integer kitchenid,Integer restaurantid,Integer schoolid ,Integer countyid, String uName,String uType,Integer queryLimit ) {
		this.kitchenid = kitchenid;
		this.restaurantid = restaurantid;
		this.schoolid = schoolid;
		this.countyid = countyid;
		this.begDate = begDate;
		this.endDate = endDate;
		this.username=uName; //uName >> username
		this.userType=uType;
		this.queryLimit = queryLimit;
	}
	
	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		
		data.put("1",
				new Object[] { "菜單日期","供餐類別","菜色名稱","食材","進貨日期","重量","食材供應商","供餐來源" 
				,"全榖根莖類","豆魚肉蛋類","低脂乳品類","蔬菜類","水果類","油脂與堅果種子類","熱量" });
						
		ViewSchoolMenuParameter2 smp =new ViewSchoolMenuParameter2();
		//smp.setSchoolname("");
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		ViewSchoolMenuWithBatchdataDAO2 vsmbDAO = new ViewSchoolMenuWithBatchdataDAO2(session);
		List<ViewSchoolMenuWithBatchdata2> vsBO = new ArrayList<ViewSchoolMenuWithBatchdata2>();
		vsBO = vsmbDAO.queryMenu(username, userType, kitchenid, restaurantid, schoolid, countyid, begDate, endDate, smp , queryLimit);
		
		int row = 2;
		
		String menudate = "";
		String menutype = "";

		Iterator<ViewSchoolMenuWithBatchdata2> iterator = vsBO.iterator();
		while (iterator.hasNext()) {
			ViewSchoolMenuWithBatchdata2 menu = iterator.next();
			
			String comboname ="";
			
			if(menu.getKitchenname() != null && !"".equals(menu.getKitchenname())){
				comboname = menu.getKitchenname();
			} else if(menu.getRestaurantname() != null && !"".equals(menu.getRestaurantname())){
				comboname = menu.getRestaurantname();
			}
			
			boolean change = false;
			//如果與前一筆的時間還有供餐類別一樣
			if(menudate.equals(menu.getMenudate()) && menutype.equals(menu.getMenutype())){
				change = false;
			} else {
				change = true;
				//如果不一樣則取代
				if(menu.getMenudate() != null){
					menudate = menu.getMenudate()+"";
				}
				menutype = menu.getMenutype();
			}
			
			if(change){
				data.put(
						String.valueOf(row),
						new Object[] {
								menu.getMenudate(),
								menu.getMenutype(),
								menu.getDishname(),
								menu.getIngredientname(),
								menu.getStockdate(),
								menu.getIngredientquantity() + " " +menu.getIngredientunit(),
								menu.getSuppliername(),
								comboname,
								menu.getTypegrains(),
								menu.getTypemeatbeans(),
								menu.getTypemilk(),
								menu.getTypevegetable(),
								menu.getTypefruit(),
								menu.getTypeoil(),
								menu.getCalorie()
								});
			} else {
				data.put(
						String.valueOf(row),
						new Object[] {
								menu.getMenudate(),
								menu.getMenutype(),
								menu.getDishname(),
								menu.getIngredientname(),
								menu.getStockdate(),
								menu.getIngredientquantity() + " " +menu.getIngredientunit(),
								menu.getSuppliername(),
								comboname,
								"",
								"",
								"",
								"",
								"",
								"",
								""
								});
			}
			row++;
		}
		session.close();
		return data;
	}

}
