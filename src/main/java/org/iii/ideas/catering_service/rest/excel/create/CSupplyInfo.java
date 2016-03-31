package org.iii.ideas.catering_service.rest.excel.create;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

//import com.google.common.annotations.Beta;

public class CSupplyInfo implements IGenerateExcel {
	private String brandName;
	private String supplierName;

	private String begDate;

	private String endDate;
	private Integer countyId;
	private String utype; // 增加傳入usertype 20140501 KC
	private String uName; // 增加uname KC

	public CSupplyInfo(String supplierName, String brandName, String begDate, String endDate, String uName, String uType) {
		this.brandName = brandName;
		this.supplierName = supplierName;
		this.begDate = begDate;
		this.endDate = endDate;
		this.countyId = AuthenUtil.getCountyNumByUsername(uName);
		this.utype = uType;
		this.uName = uName;
	}

	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {

		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		try {
			String username = null;
			String usertype = null;
			Integer kitchenId = null;
		} catch (Exception ex) {

		}

		String userType = "";

		// 表頭
		data.put("1", new Object[] { "供應商名稱", "編號", "品牌", "資料建立日期", "盒餐業者名稱/自立午餐學校名稱", "聯絡電話", "地址", "食材", "批號", "有效日期", "進貨日期", "生產日期", "學校" });

		// 資料
		StringBuffer queryString = new StringBuffer();
		queryString.append("select distinct ").append("i.supplierName,").append("i.ingredientId,").append("i.brand, ").append("date_format(b.uploadDateTime, '%Y/%m/%d'),").append("k.kitchenName, ").append("k.tel, ").append("k.address, ")
				.append("i.ingredientName, ").append("b.lotNumber, ").append("i.expirationDate, ").append("i.stockDate, ").append("i.manufactureDate, ").append("c.schoolName ").append("from ")
				.append(" Ingredientbatchdata i, Batchdata b, Kitchen k,School c ").append("where ")
				// .append(" i.supplierId = s.id.supplierId ")
				.append(" i.batchDataId = b.batchDataId  ").append("and b.kitchenId = k.kitchenId ").append("and c.schoolId = b.schoolId ");

		// .append("and ( i.supplierName like :supplierName or '' = :bupplierName)")
		// .append("and ( i.brand like :brandName or '' = :brandName)")
		// 2014604 Raymond 判斷有無值再
		if (!CateringServiceUtil.isEmpty(supplierName))
			queryString.append("and i.supplierName like :supplierName ");
		if (!CateringServiceUtil.isEmpty(brandName))
			queryString.append("and i.brand like :brandName ");
		queryString.append("and b.menuDate between :begDate and :endDate ");
		/*
		 * 如果是0(找不照權限) 就找不到結果CateringServiceCode.AUTHEN_NO_COUNTY 如果是9999
		 * 就是最高權限，不丟出對應的條件 CateringServiceCode.AUTHEN_SUPER_COUNTY 其他結果 為丟出各縣市代碼
		 */
		if (CateringServiceCode.AUTHEN_NO_COUNTY.equals(countyId.toString())) {
			// 沒有county 也就是沒權限
			queryString.append("and c.countyId = '0'");
		} else if (CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(countyId.toString())) {
			// 中央部會
			// queryString.append("select distinct ")
		} else if (CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(this.utype)) {
			// 他校供應的學校們
			queryString.append("and b.schoolId = :sid ");
		} else if (CateringServiceCode.USERTYPE_SCHOOL.equals(this.utype)) {
			// 自設廚房
			queryString.append("and b.kitchenId = :kid ");

		} else {
			// 地方政府
			queryString.append("and c.countyId = :countyId");
		}

		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();

		try {
			Query query = session.createQuery(queryString.toString());

			// 2014604 Raymond 判斷有無值再給Parameter
			if (!CateringServiceUtil.isEmpty(supplierName))
				query.setParameter("supplierName", "%" + supplierName + "%");
			if (!CateringServiceUtil.isEmpty(brandName))
				query.setParameter("brandName", "%" + brandName + "%");

			if (!CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(countyId.toString()) && !CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(this.utype) && !CateringServiceCode.USERTYPE_SCHOOL.equals(this.utype)) {
				query.setParameter("countyId", countyId);
			}

			String[] begDateArray = begDate.split("/");
			String[] endDateArray = endDate.split("/");
			begDateArray[1] = String.format("%02d", Integer.parseInt(begDateArray[1]));
			endDateArray[1] = String.format("%02d", Integer.parseInt(endDateArray[1]));

			query.setParameter("begDate", StringUtils.join(begDateArray, "/"));
			query.setParameter("endDate", StringUtils.join(endDateArray, "/"));

			if (CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(this.utype)) {
				// 他校供應的學校們
				Integer userSid = AuthenUtil.getSchoolIdByUsername(this.uName);
				query.setParameter("sid", userSid);

			} else if (CateringServiceCode.USERTYPE_SCHOOL.equals(this.utype)) {
				// 自設廚房
				Integer userKid = AuthenUtil.getKitchenIddByUsername(this.uName);
				query.setParameter("kid", userKid);
			}

			List<Object[]> result = query.list();

			int i = 2;
			for (Object[] obj : result) {

				// "供應商名稱","編號", "品牌", "資料建立日期", "盒餐業者名稱/自立午餐學校名稱", "聯絡電話",
				// "地址", "食材", "批號", "有效日期", "進貨日期", "生產日期"
				data.put(
						i + "",
						new Object[] { obj[0], obj[1], obj[2],
								obj[3],
								// CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
								// (Timestamp)obj[3]),
								obj[4], obj[5], obj[6], obj[7], obj[8], obj[9] == null || StringUtils.isEmpty(obj[9].toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp) obj[9]),
								obj[10] == null || StringUtils.isEmpty(obj[10].toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp) obj[10]),
								obj[11] == null || StringUtils.isEmpty(obj[11].toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp) obj[11]), obj[12] });
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
