package org.iii.ideas.catering_service.rest.excel.create;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient2;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient2DAO;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class CSchoolIngredient implements IGenerateExcel {

	private static Logger log = Logger.getLogger(CSupplier.class);
	private int kid;
	private String begDate;
	private String endDate;
	public CSchoolIngredient(int kid, String begDate, String endDate) {
		this.kid = kid;
		this.begDate = begDate;
		this.endDate = endDate;
	}
	
	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		/** edit by Joshua 2014/10/15  */
		/*
		data.put("1", new Object[] { "供餐日期", "學校", "菜色名稱", "食材名稱", "進貨日期", "生產日期", "有效日期", "批號", "製造商",
				"供應商名稱","供應商統編", "食材認證標章", "認證號碼", "產品名稱", "重量(公斤)", "基改玉米", "基改黃豆", "加工品" });
		*/
		/** modify by Ellis 20141126
		 * 新版xls將統編去除*/
//		data.put("1", new Object[] { "供餐日期", "學校", "菜色名稱", "食材名稱", "進貨日期", "生產日期", "有效日期", "批號", "製造商",
//				"供應商名稱","食材認證標章", "認證號碼", "產品名稱", "重量(公斤)", "基改玉米", "基改黃豆", "加工品" });
		
		/** modify by Ellis 20150826
		 * 新版xls (認證標章 >> 驗證標章 ) */
		data.put("1", new Object[] { "供餐日期", "學校", "菜色名稱", "食材名稱", "進貨日期", "生產日期", "有效日期", "批號", "製造商",
				"供應商名稱","食材驗證標章", "驗證號碼", "產品名稱", "重量(公斤)", "基改玉米", "基改黃豆", "加工品" });
		/*
		String HQL = "select distinct b.menuDate, s.schoolName, d.dishName, i.ingredientName, i.stockDate, i.manufactureDate, "
				+ "i.expirationDate, i.lotNumber, i.manufacturer, i.supplierId, i.supplierCompanyId, i.sourceCertification, i.certificationId , "
				+ "i.productName, i.ingredientQuantity, i.ingredientAttr "
				+ "from School s,Batchdata b,Ingredientbatchdata i , Dish d , Schoolkitchen sk "
				+ "where s.schoolId = b.schoolId "
				+ " and b.batchDataId=i.batchDataId "
				+ " and i.dishId= d.dishId "
				+ " and b.kitchenId = :kitchenId "
				+ " and d.kitchenId = b.kitchenId "
				+ " and b.menuDate between :begDate and :endDate "
				+ " and s.schoolId = sk.id.schoolId "
				+ " order by b.menuDate, s.schoolName, d.dishName, i.ingredientName, i.stockDate, i.manufactureDate,i.expirationDate, i.lotNumber, i.brand, i.sourceCertification, i.certificationId";
		*/
		/*
		String HQL = "select distinct v2.menudate, v2.schoolname , v2.dishname,v2.ingredientName,v2.stockDate,v2.manufactureDate,"
				+ "expirationDate,lotNumber,manufacturer,supplierName,sourceCertification,certificationId,"
				+ "productName,ingredientQuantity,ingredientAttr "
				+ "from ViewDishAndIngredient2 v2 "
				+ "where kitchenid = :kitchenId"
				+ "and menudate between :begDate and :endDate ";
		
		log.debug("Download 學校食材 日期:" + begDate + "-" + endDate + " KitchenId:" + kid);
		Query ingredientFileQuery = session.createQuery(HQL);
		ingredientFileQuery.setParameter("kitchenId", kid);
		ingredientFileQuery.setParameter("begDate", begDate);
		ingredientFileQuery.setParameter("endDate", endDate);
		*/
		/**透過view取得食材資訊 無食材之菜色也會印出 modify by ellis 20141203
		 * */
		ViewDishAndIngredient2DAO vdai2DAO = new ViewDishAndIngredient2DAO(session);
		List<ViewDishAndIngredient2> ingredientFileFormat = vdai2DAO.queryIngredients(kid, begDate, endDate);
		Iterator<ViewDishAndIngredient2> ingredientFileIterator = ingredientFileFormat.iterator();
		int row = 2;
		while (ingredientFileIterator.hasNext()) {
			ViewDishAndIngredient2 obj = ingredientFileIterator.next();
			//基改欄位處理
			String gmcorn = "";
			String gmbean = "";
			String psfood = "";
			if(!CateringServiceUtil.isNull(obj.getIngredientAttr())){
				IngredientAttributeBO ingredientAttrBO = CateringServiceUtil.getIngredientAttrBo((Integer)obj.getIngredientAttr());
				gmcorn = ingredientAttrBO.getGmcorn() == 1 ? "Y" : "N";
				gmbean = ingredientAttrBO.getGmbean() == 1 ? "Y" : "N";
				psfood = ingredientAttrBO.getPsfood() == 1 ? "Y" : "N";
			}
			//日期處理
			String stockDate = obj.getStockDate() == null ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					(Timestamp) obj.getStockDate());
			String manufactureDate = obj.getManufactureDate() == null ? "" : CateringServiceUtil.converTimestampToStr(
					"yyyy/MM/dd", (Timestamp) obj.getManufactureDate());
			String expirationDate = obj.getExpirationDate() == null ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
					(Timestamp) obj.getExpirationDate());
			//重量處理
			
			//處理舊版無DishShowName可能為空的問題 add by ellis 2014/12/29
			
			String dishName = "";
			if("".equals(obj.getDishshowname())){
				dishName = obj.getDishname();
			}else{
				dishName = obj.getDishshowname();
			}
			String ingredientQuantity = StringUtils.isNotBlank((String) obj.getIngredientQuantity()) ? (String) obj.getIngredientQuantity() : "0"; 
			data.put(String.valueOf(row), new Object[] { obj.getMenudate(), obj.getSchoolname(), dishName, obj.getIngredientName(),
				stockDate, manufactureDate,expirationDate, obj.getLotNumber(), obj.getManufacturer(), obj.getSupplierName(), 
				obj.getSourceCertification(), obj.getCertificationId(), obj.getProductName(), ingredientQuantity, gmcorn, gmbean, psfood });
			
//			Object[] obj = ingredientFileIterator.next();
//			String stockDate = obj[4] == null ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
//					(Timestamp) obj[4]);
//			String manufactureDate = obj[5] == null ? "" : CateringServiceUtil.converTimestampToStr(
//					"yyyy/MM/dd", (Timestamp) obj[5]);
//			String expirationDate = obj[6] == null ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",
//					(Timestamp) obj[6]);
//			Integer supplierId = (Integer) obj[9];
//			String supplierCompanyId = (String) obj[10];
//			String ingredientQuantity = StringUtils.isNotBlank((String) obj[14]) ? (String) obj[14] : "0"; 
//			IngredientAttributeBO ingredientAttrBO = CateringServiceUtil.getIngredientAttrBo((Integer)obj[15]);
//			String gmcorn = ingredientAttrBO.getGmcorn() == 1 ? "Y" : "N";
//			String gmbean = ingredientAttrBO.getGmbean() == 1 ? "Y" : "N";
//			String psfood = ingredientAttrBO.getPsfood() == 1 ? "Y" : "N";
//			Supplier supplier = HibernateUtil.querySupplierById(session, kid, supplierId);
//			String companyId = "";
//			if (CateringServiceUtil.isEmpty(supplierCompanyId)) {
//				companyId = supplier == null ? "" : supplier.getCompanyId();
//			} else {
//				companyId = supplierCompanyId;
//			}
//			String supplierName =  "";
//			if (CateringServiceUtil.isNull(supplier)) {
//				supplierName = "";
//			} else {
//				supplierName = supplier.getSupplierName();
//			}
//			/*
//			 * data.put(String.valueOf(row), new Object[] { obj[0], obj[1], obj[2], obj[3], stockDate, manufactureDate,
//					expirationDate, obj[7], obj[8], companyId, obj[11], obj[12], obj[13], ingredientQuantity, gmcorn, gmbean, psfood });
//			 */
//
//			data.put(String.valueOf(row), new Object[] { obj[0], obj[1], obj[2], obj[3], stockDate, manufactureDate,
//				expirationDate, obj[7], obj[8],supplierName, obj[11], obj[12], obj[13], ingredientQuantity, gmcorn, gmbean, psfood });

			row++;
		}

		session.close();
		
		return data;
	}

}
