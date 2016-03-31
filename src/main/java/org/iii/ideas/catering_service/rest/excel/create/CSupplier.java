package org.iii.ideas.catering_service.rest.excel.create;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class CSupplier implements IGenerateExcel {

	private static Logger log = Logger.getLogger(CSupplier.class);

	private int kid;

	private String searchFileType;

	public CSupplier(int kid, String searchFileType) {
		this.kid = kid;
		this.searchFileType = searchFileType;
	}

	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {

		Map<String, Object[]> data = new TreeMap<String, Object[]>();

		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		// 20141021 raymond 移除認證標章欄位
		// data.put("1", new Object[] { "供應商名稱", "負責人", "公司統編", "地址", "電話",
		// "認證標章" });
		data.put("1", new Object[] { "供應商名稱", "負責人", "公司統編", "地址", "電話" });
		Criteria criteria = session.createCriteria(Supplier.class);
		criteria.add(Restrictions.eq("id.kitchenId", kid));
		// criteria.addOrder( Order.asc("menuDate") );

		int row = 2;
		List<Supplier> supplier_list = criteria.list();
		Iterator<Supplier> iterator = supplier_list.iterator();
		while (iterator.hasNext()) {
			Supplier supplier = iterator.next();
			log.debug("func:" + searchFileType + " Row:" + String.valueOf(row));
			// 20141021 raymond 移除認證標章欄位
//			data.put(String.valueOf(row), new Object[] { supplier.getSupplierName(), supplier.getOwnner(), supplier.getCompanyId(), supplier.getSupplierAdress(), supplier.getSupplierTel(), supplier.getSupplierCertification() });
			data.put(String.valueOf(row), new Object[] { supplier.getSupplierName(), supplier.getOwnner(), supplier.getCompanyId(), supplier.getSupplierAdress(), supplier.getSupplierTel()});
			row++;
		}
		session.close();

		return data;
	}

}
