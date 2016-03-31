package org.iii.ideas.catering_service.rest.excel.create;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class CVegetable implements IGenerateExcel {

	private int kid;
	
	public CVegetable(int kid) {
		this.kid = kid;
	}
	
	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();

		data.put("1", new Object[] { "菜色名稱", "食材名稱", "供應商名稱", "產品名稱", "製造商名稱" });

		String HQL = "select d.dishName, i.ingredientName, i.supplierId, i.brand, i.supplierCompanyId, i.productName, i.manufacturer from Dish d, Ingredient i "
				+ "where d.dishId = i.dishId "
				+ "and d.kitchenId = :kitchenId "
				+ "order by d.dishName, i.ingredientName, i.brand";

		Query ingredientFileQuery = session.createQuery(HQL);
		ingredientFileQuery.setParameter("kitchenId", kid);
		List ingredientFileFormat = ingredientFileQuery.list();// Schoolkitchen
		Iterator<Object[]> ingredientFileIterator = ingredientFileFormat.iterator();
		int row = 2;
		while (ingredientFileIterator.hasNext()) {
			Object[] obj = ingredientFileIterator.next();
			String dishName = (String) (obj[0] == null ? "" : obj[0]);
			String ingredientName = (String) (obj[1] == null ? "" : obj[1]);
			String supplierCompanyId = (String) (obj[4] == null ? "" : obj[4]);
			Supplier supplier = HibernateUtil.querySupplierById(session, kid, (Integer) obj[2]);
			String supplierName = "";
			// 如果supplier id 存在就以它為主
			if (supplier != null) {
				supplierName = supplier.getSupplierName();
			} else {
				// 如果supplier id 不存在就以companyId為主
				if (!CateringServiceUtil.isEmpty(supplierCompanyId)) {
					supplier = HibernateUtil.querySupplierByCompanyId(session, kid, supplierCompanyId);
					if (supplier != null) {
						supplierName = supplier.getSupplierName();
					}
				}
			}

			// String supplierName = (String) (obj[2]==null?"": obj[2]);
			String brand = (String) (obj[3] == null ? "" : obj[3]);
			String productName = (String) (obj[5] == null ? "" : obj[5]);
			String manufacturer = (String) (obj[6] == null ? "" : obj[6]);
			data.put(String.valueOf(row), new Object[] { dishName, ingredientName, supplierName, productName, manufacturer });
			row++;
		}
		session.close();
		
		return data;
	}

}
