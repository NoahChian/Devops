package org.iii.ideas.catering_service.rest.excel.create;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient2;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient2DAO;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredientDAO;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdataDAO;
import org.iii.ideas.catering_service.rest.api.QueryManageList;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter2;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class CIngredientList implements IGenerateExcel {
	
	private Integer kitchenid;
	private Integer schoolid;
	private Integer countyid;
	private Integer queryLimit;
	private String begDate;
	private String endDate;
	private String userType;
	private String username;
	private String dishname;
	private String ingredientName;
	private String brand;
	private String supplierName;
	private String manufacturer;
	
	public CIngredientList(String begDate, 
			String endDate,
			Integer kitchenid ,
			Integer schoolid ,
			Integer countyid, 
			String uName,
			String uType,
			Integer queryLimit ,
			String dishname,
			String ingredientName,
			String brand,
			String supplierName, 
			String manufacturer) {
		this.kitchenid = kitchenid;
		this.schoolid = schoolid;
		this.countyid = countyid;
		this.begDate = begDate;
		this.endDate = endDate;
		this.username=uName; //uName >> username
		this.userType=uType;
		this.queryLimit = queryLimit;
		this.dishname = dishname;
		this.ingredientName = ingredientName;
		this.brand = brand;
		this.supplierName = supplierName;
		this.manufacturer = manufacturer;
	}
	
	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1",
				new Object[] { "日期","廚房名稱","學校","菜色名稱","食材名稱","進貨日期","生產日期","有效日期","批號","品牌","供應商名稱","供應商統編","產品名稱","製造商名稱","認證標章","認證編號"});
		
		//設定必填欄位外的like判斷參數
		//額外查詢條件
		ViewDishAndIngredientParameter2 vdai = new ViewDishAndIngredientParameter2();
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(dishname)){vdai.setDishname(dishname);}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(ingredientName)){vdai.setIngredientName(ingredientName);}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(brand)){vdai.setBrand(brand);}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(supplierName)){vdai.setSupplierName(supplierName);}
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(manufacturer)){vdai.setManufacturer(manufacturer);}

		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		ViewDishAndIngredient2DAO vdaiDAO = new ViewDishAndIngredient2DAO(session);
		List<ViewDishAndIngredient2> vdaiBO = new ArrayList<ViewDishAndIngredient2>();
		vdaiBO = vdaiDAO.queryIngredients2(username, userType, kitchenid, schoolid, countyid, begDate, endDate, vdai , queryLimit);
		
		int row = 2;
		Iterator<ViewDishAndIngredient2> iterator = vdaiBO.iterator();
		while (iterator.hasNext()) {
			ViewDishAndIngredient2 ingredient = iterator.next();
			data.put(
					String.valueOf(row),
					new Object[] {
						ingredient.getMenudate(),
						ingredient.getKitchenname(),
						ingredient.getSchoolname(),
						ingredient.getDishname(),
						//ingredient.getDishname(),
						ingredient.getIngredientName(),
						ingredient.getStockDate()== null || StringUtils.isEmpty(ingredient.getStockDate().toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp)ingredient.getStockDate()),
						ingredient.getManufactureDate()== null || StringUtils.isEmpty(ingredient.getManufactureDate().toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp)ingredient.getManufactureDate()),
						ingredient.getExpirationDate()== null || StringUtils.isEmpty(ingredient.getExpirationDate().toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp)ingredient.getExpirationDate()),
						ingredient.getLotNumber(),
						ingredient.getBrand(),
						ingredient.getSupplierName(),
						ingredient.getSupplierCompanyId(),
						ingredient.getProductName(),
						ingredient.getManufacturer(),
						ingredient.getSourceCertification(),
						ingredient.getCertificationId()});
			row++;
		}
		session.close();
		return data;
	}

}
