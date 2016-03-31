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
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

import com.mchange.lang.IntegerUtils;


public class CKitchenIngredient implements IGenerateExcel {

	private int kid;
	private String startDate;
	private String endDate;
	private Integer countyId;
	private String uType;  //增加傳入usertype  20140505 KC
	private String uName;  //增加uname  KC
	
	
	public CKitchenIngredient(int kid, String startDate, String endDate, String uName,String uType) {
		this.kid = kid;
		this.startDate = startDate;
		this.endDate = endDate;
		this.countyId = AuthenUtil.getCountyNumByUsername(uName);
		this.uName=uName;
		this.uType=uType;
	}
	
	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		
		data.put("1",
				new Object[] { "食材編號", "菜單日期","品牌", "供應商名稱", "供應商統編","食材批號", "品牌商統編", "食材", "批號", "有效日期", "進貨日期", "生產日期", "認證標章", "認證編號", "學校" });
		
		String queryString =
					"select " + 
					"	i.ingredientId, " + 
					"   b.menuDate, " + 
					"   i.brand, " + 
					"   i.supplierName, " + 
					"   i.supplierCompanyId, " + 
					" i.lotNumber ,"+
					" i.brandNo, "+
					//"   s.supplierTel, " + 
					//"   s.supplierAdress, " + 
					"   i.ingredientName , " + 
					"   b.lotNumber, " + 
					"   i.expirationDate, " + 
					"   i.stockDate, " + 
					"   i.manufactureDate, " + 
					"   i.sourceCertification, " + 
					"   i.certificationId, " + 
					"   c.schoolName "
					+ "from "
						//+ "Ingredientbatchdata as i, Batchdata as b, Supplier as s, School as c"
						+ "Ingredientbatchdata as i, Batchdata as b,  School as c"
						+ " where "
							+ "i.batchDataId = b.batchDataId "
							//+ "and s.id.supplierId = i.supplierId "
							+ "and c.schoolId = b.schoolId "
							+ "and b.kitchenId = :kid  "
							+ "and b.menuDate between :begDate and :endDate ";
		/*  
		 * 如果是0(找不照權限) 就找不到結果 CateringServiceCode.AUTHEN_NO_COUNTY
		 * 如果是9999 就是最高權限，不丟出對應的條件CateringServiceCode.AUTHEN_SUPER_COUNTY
		 * 其他結果 為丟出各縣市代碼
		 */
		if(CateringServiceCode.AUTHEN_NO_COUNTY.equals(countyId.toString())){
			//沒有權限
			queryString += ("and c.countyId = '0'");
		}else if(CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(countyId.toString())){
			//全部的權限( all county)
			//queryString.append("select distinct ")
		}else if(CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(this.uType) ||CateringServiceCode.USERTYPE_SCHOOL.equals(this.uType) ){
			//他校供應的學校
			queryString += ("and b.schoolId =:sid");
		}else {
			queryString += ("and c.countyId = :countyId");
		}
		
		
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		
		Query query = session.createQuery(queryString);
		query.setParameter("kid", kid);
		if(!CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(countyId.toString())
				&& !CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(this.uType)
				&& !CateringServiceCode.USERTYPE_SCHOOL.equals(this.uType) ){
			query.setParameter("countyId", countyId);
		}else if(CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(this.uType) ||CateringServiceCode.USERTYPE_SCHOOL.equals(this.uType) ){
			Integer userSid=AuthenUtil.getSchoolIdByUsername(uName);
			query.setParameter("sid", userSid);
		}else{
			// nothing
			
		}
		String[] begDateArray = startDate.split("/");
		String[] endDateArray = endDate.split("/");
		begDateArray[1] = String.format("%02d", Integer.parseInt(begDateArray[1]));
		endDateArray[1] = String.format("%02d", Integer.parseInt(endDateArray[1]));
		
		query.setParameter("begDate", StringUtils.join(begDateArray, "/"));
		query.setParameter("endDate", StringUtils.join(endDateArray, "/"));
		
		List<Object[]> result = query.list();
		
		int i = 2;
		for (Object[] obj : result) {
				//"食材編號", "菜單日期", "品牌", "供應商名稱", "供應商統編", "聯絡電話", "地址", "食材批號", "品牌商統編", "有效日期", "進貨日期", "生產日期", "認證標章", "認證編號"
			data.put(i + "", new Object[] {
				
				obj[0], obj[1], obj[2], obj[3], obj[4], obj[5], obj[6], obj[7], obj[8], 
				obj[9] == null || StringUtils.isEmpty(obj[9].toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp)obj[9]),
				obj[10] == null || StringUtils.isEmpty(obj[10].toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp)obj[10]),
				obj[11] == null || StringUtils.isEmpty(obj[11].toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp)obj[11]),
				(!StringUtils.isEmpty((String)obj[12]) && !StringUtils.isEmpty((String)obj[12]) ? obj[12] : ""),
				(!StringUtils.isEmpty((String)obj[13]) && !StringUtils.isEmpty((String)obj[13]) ? obj[13] : ""), 
				obj[14]
			});
			i++;
		}
		
		session.close();
		return data;
	}

}
