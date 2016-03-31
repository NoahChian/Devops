package org.iii.ideas.catering_service.rest.excel.create;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class CAbnormalSearch implements IGenerateExcel {

	private String supplierName;

	private String ingedientName;

	private String begDate;

	private String endDate;

	public CAbnormalSearch(String ingedientName, String supplierName, String begDate, String endDate) {
		this.supplierName = supplierName;
		this.ingedientName = ingedientName;
		this.begDate = begDate;
		this.endDate = endDate;
	}

	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		Map<String, Object[]> data = new TreeMap<String, Object[]>();

		data.put("1", new Object[] { "食材編號", "供應商名稱", "品牌", "食材", "批號", "有效日期", "進貨日期", "生產日期", "團膳業者名稱", "供餐日期", "供餐學校", "菜色名稱", "異常說明" });

		StringBuffer queryString = new StringBuffer();
		queryString.append("select ").append("i.ingredientId,").append("s.supplierName, ").append("i.brand, ").append("i.ingredientName, ").append("i.lotNumber, ").append("i.expirationDate, ").append("i.stockDate, ").append("i.manufactureDate, ")
				.append("k.kitchenName, ").append("b.menuDate, ").append("sch.schoolName, ").append("d.dishName, ").append("n.description ").append("from ")
				.append("NegIngredient as n, Ingredientbatchdata as i, Batchdata as b, Supplier as s, School as sch, Dish as d, Kitchen k ").append("where ").append(" n.id.negIngredientId = i.ingredientId ").append("and s.id.supplierId = i.supplierId  ")
				.append("and i.batchDataId = b.batchDataId ").append("and b.kitchenId = k.id.kitchenId ").append("and b.schoolId = sch.id.schoolId ").append("and ( i.ingredientName like :ingName or '' = :ingName )")
				.append("and ( s.supplierName like :supplierName or '' = :supplierName )").append("and ( i.expirationDate between :begDate and :endDate ").append("or i.stockDate between :begDate and :endDate ")
				.append("or i.manufactureDate between :begDate and :endDate ) ").append("and i.dishId = d.id.dishId");

		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		try {
			Query query = session.createQuery(queryString.toString());
			query.setParameter("ingName", "%" + ingedientName + "%");
			query.setParameter("supplierName", "%" + supplierName + "%");
			query.setParameter("begDate", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
			query.setParameter("endDate", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));

			List<Object[]> result = query.list();

			int i = 2;
			for (Object[] obj : result) {

				// "食材編號", "供應商名稱","品牌", "食材", "批號", "有效日期", "進貨日期", "生產日期",
				// "團膳業者名稱", "供餐日期", "供餐學校", "菜色名稱", "異常說明"
				data.put(
						i + "",
						new Object[] { obj[0], obj[1], obj[2], obj[3], obj[4], obj[5] == null || StringUtils.isEmpty(obj[5].toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp) obj[5]),
								obj[6] == null || StringUtils.isEmpty(obj[6].toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp) obj[6]),
								obj[7] == null || StringUtils.isEmpty(obj[7].toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp) obj[7]), obj[8], obj[9], obj[10], obj[11], obj[12] });
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return data;
	}

}
