package org.iii.ideas.catering_service.rest.excel.create;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class CRefersList implements IGenerateExcel {
	private int kid;
	private String referTarget;
	private List SupplierFileFormat = null;
	private List<School> SchoolFileFormat = null;
	private int dataMaxSize = 0 ;
	public CRefersList(int kid) {
		this.kid = kid;
		referTarget = "";
	}
	public CRefersList(int kid,String referTarget) {
		this.kid = kid;
		this.referTarget = referTarget;
	}

	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {

		Map<String, Object[]> data = new TreeMap<String, Object[]>();

		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		/**
		 * 新版供應商、學校下拉選單 20141203
		 * 根據傳入字串回傳下拉資訊，沒有傳送為
		 * */
		int row = 2; //從第二列開始輸出
		switch (referTarget){
		case "School" :
			data.put("1", new Object[] { "SchoolName" });
			SchoolFileFormat = getSchoolList(kid, session);
//			SchoolFileFormat.add(new School());
			data.put("2", new Object[] {"全部學校"});
			for (int i = 0; i < SchoolFileFormat.size(); i++) {
				data.put(
						String.valueOf(row+1),
						new Object[] {i < SchoolFileFormat.size() ? SchoolFileFormat.get(i).getSchoolName() : "" });
				row++;
			}
			break;
		case "Supplier" :
			data.put("1", new Object[] { "SupplierName" });
			SupplierFileFormat = getSupplierList(kid, session);
			for (int i = 0; i < SupplierFileFormat.size(); i++) {
				data.put(
						String.valueOf(row),
						new Object[] {i < SupplierFileFormat.size() ? SupplierFileFormat.get(i) : "" });
				row++;
			}
			break;
		case "":
			data.put("1", new Object[] { "SupplierName", "SchoolName" });
			SupplierFileFormat = getSupplierList(kid, session);
			SchoolFileFormat = getSchoolList(kid, session);
			SchoolFileFormat.add(new School());
			// 兩List取最大值
			if (SupplierFileFormat.size() > SchoolFileFormat.size()) {
				dataMaxSize = SupplierFileFormat.size();
			} else {
				dataMaxSize = SchoolFileFormat.size();
			}
			for (int i = 0; i < dataMaxSize; i++) {
				data.put(
						String.valueOf(row),
						new Object[] {
								i < SupplierFileFormat.size() ? SupplierFileFormat
										.get(i) : "",
								i < SchoolFileFormat.size() ? SchoolFileFormat.get(
										i).getSchoolName() : "" });
				row++;
			}
			break;
		default :
			data.put("1", new Object[] { "SupplierName", "SchoolName" });
			SupplierFileFormat = getSupplierList(kid, session);
			SchoolFileFormat = getSchoolList(kid, session);// 學校List
			SchoolFileFormat.add(new School());
			// 兩List取最大值
			if (SupplierFileFormat.size() > SchoolFileFormat.size()) {
				dataMaxSize = SupplierFileFormat.size();
			} else {
				dataMaxSize = SchoolFileFormat.size();
			}
			for (int i = 0; i < dataMaxSize; i++) {
				data.put(
						String.valueOf(row),
						new Object[] {
								i < SupplierFileFormat.size() ? SupplierFileFormat
										.get(i) : "",
								i < SchoolFileFormat.size() ? SchoolFileFormat.get(
										i).getSchoolName() : "" });
				row++;
			}
			break;
	}
		
		
		session.close();
		return data;
	}
	
	public List getSupplierList(int kid,Session session){
		String HQL = "select supplierName from Supplier where kitchenId = :kitchenId "
				+ "and (supplierName is not null and supplierName <> '')  order by supplierName";
		Query SupplierFileQuery = session.createQuery(HQL);
		SupplierFileQuery.setParameter("kitchenId", kid);
		return SupplierFileQuery.list();
	}
	
	public List<School> getSchoolList(int kid,Session session){
		return HibernateUtil.querySchoolListByKitchenId(session, kid);
	}
}
