package org.iii.ideas.catering_service.rest.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.iii.ideas.catering_service.rest.bo.Ingredients_searchBO;

/*
 * 食材及供應商關鍵字查詢
 */
public class Ingredients_search extends AbstractApiInterface<Ingredients_searchRequest, Ingredients_searchResponse> {
	private static final long serialVersionUID = -9115574829368651090L;
	
	/**
     * 依查詢條件查詢食材及供應商資料
     */
	@SuppressWarnings("unchecked")
	@Override
	public void process() throws NamingException, ParseException {
		// 讀取傳入參數		
		String menuStartDate = this.requestObj.getMenuStartDate(); // 供餐起始日期
		String menuEndDate = this.requestObj.getMenuEndDate(); // 供餐結束日期
		Integer countyId = this.requestObj.getCountyId(); // 學校縣市代碼
		String schoolName = this.requestObj.getSchoolName(); // 學校名稱
		String kitchenCompanyId = this.requestObj.getKitchenCompanyId(); // 供餐業者統一編號
		String kitchenName = this.requestObj.getKitchenName(); // 供餐業者名稱
		String igredientName = this.requestObj.getIgredientName(); // 食材名稱
		String igredientSupplier = this.requestObj.getIgredientSupplier(); // 食材供應商名稱
		String seasoningName = this.requestObj.getSeasoningName(); // 調味料名稱
		String seasoningSupplier = this.requestObj.getSeasoningSupplier(); // 調味料供應商名稱
		Integer limit = this.requestObj.getLimit(); // 筆數限制
		
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		df.setLenient(false); // 嚴格檢核日期格式
		
		// 檢核必填欄位 : 供餐起始日期, 供餐結束日期
		Date dMenuStartDate = null;
		Date dMenuEndDate = null;
		if (menuStartDate != null && menuStartDate.length() > 0) {
			if (menuStartDate.length() != 10) {
				this.responseObj.setMsg("請輸入正確的供餐起始日期格式 : yyyy/MM/dd");
				this.responseObj.setResStatus(0);
				return;
			}
			try {
				menuStartDate = menuStartDate + " 00:00:00";
				dMenuStartDate = df.parse(menuStartDate);
			} catch (Exception e) {
				this.responseObj.setMsg("請輸入正確的供餐起始日期格式 : yyyy/MM/dd");
				this.responseObj.setResStatus(0);
				return;
			}
		} else {
			this.responseObj.setMsg("請輸入供餐起始日期");
			this.responseObj.setResStatus(0);
			return;
		}
		if (menuEndDate != null && menuEndDate.length() > 0) {
			if (menuEndDate.length() != 10) {
				this.responseObj.setMsg("請輸入正確的供餐結束日期格式 : yyyy/MM/dd");
				this.responseObj.setResStatus(0);
				return;
			}
			try {
				menuEndDate = menuEndDate + " 23:59:59";
				dMenuEndDate = df.parse(menuEndDate);
			} catch (Exception e) {
				this.responseObj.setMsg("請輸入正確的供餐結束日期格式 : yyyy/MM/dd");
				this.responseObj.setResStatus(0);
				return;
			}
		} else {
			this.responseObj.setMsg("請輸入供餐結束日期");
			this.responseObj.setResStatus(0);
			return;
		}
		// 檢核 : 供餐結束日期 > 供餐起始日期
		if(dMenuStartDate.after(dMenuEndDate)){
			this.responseObj.setMsg("供餐結束日期必需大於供餐起始日期");
			this.responseObj.setResStatus(0);
			return;
		}
		// 檢核非必填欄位格式 : 學校縣市代碼, 筆數限制(預設100筆,範圍1~1000筆)
		if (countyId != null) {
			if (!NumberUtils.isNumber("" + countyId)) {
				this.responseObj.setMsg("學校縣市代碼需為數值");
				this.responseObj.setResStatus(0);
				return;
			}
		}
		if (limit != null) {
			if (!NumberUtils.isNumber("" + limit)) {
				this.responseObj.setMsg("筆數限制需為數值");
				this.responseObj.setResStatus(0);
				return;
			} else {
				if (limit.intValue() > 1000) {
					// 設定筆數限制上限值
					limit = new Integer(1000);
				}
			}
		} else {
			// 設定筆數限制預設值
			limit = new Integer(100);
		}		
		// 檢核非必填欄位是否內含SQL語法,避免SQL INJECTION
		if (schoolName != null && schoolName.length() > 0) {
			if (containSQL(schoolName)) {
				this.responseObj.setMsg("學校名稱內含SQL語法");
				this.responseObj.setResStatus(0);
				return;
			}
		}
		if (kitchenCompanyId != null && kitchenCompanyId.length() > 0) {
			if (containSQL(kitchenCompanyId)) {
				this.responseObj.setMsg("供餐業者統一編號內含SQL語法");
				this.responseObj.setResStatus(0);
				return;
			}
		}
		if (kitchenName != null && kitchenName.length() > 0) {
			if (containSQL(kitchenName)) {
				this.responseObj.setMsg("供餐業者名稱內含SQL語法");
				this.responseObj.setResStatus(0);
				return;
			}
		}
		if (igredientName != null && igredientName.length() > 0) {
			if (containSQL(igredientName)) {
				this.responseObj.setMsg("食材名稱內含SQL語法");
				this.responseObj.setResStatus(0);
				return;
			}
		}
		if (igredientSupplier != null && igredientSupplier.length() > 0) {
			if (containSQL(igredientSupplier)) {
				this.responseObj.setMsg("食材供應商名稱內含SQL語法");
				this.responseObj.setResStatus(0);
				return;
			}
		}
		if (seasoningName != null && seasoningName.length() > 0) {
			if (containSQL(seasoningName)) {
				this.responseObj.setMsg("調味料名稱內含SQL語法");
				this.responseObj.setResStatus(0);
				return;
			}
		}
		if (seasoningSupplier != null && seasoningSupplier.length() > 0) {
			if (containSQL(seasoningSupplier)) {
				this.responseObj.setMsg("調味料供應商名稱內含SQL語法");
				this.responseObj.setResStatus(0);
				return;
			}
		}
		
		// 組合NATIVE SQL STATEMENT
		StringBuffer nativeSQL = new StringBuffer("");
		nativeSQL.append("SELECT c.county        AS county, ");
		nativeSQL.append("s.schoolname    AS school_name, ");
		nativeSQL.append("b.menudate      AS menu_date, ");
		nativeSQL.append("k.kitchenname   AS kitchen_name, ");
		nativeSQL.append("k.companyid     AS kitchen_company, ");
		nativeSQL.append("Ifnull(( CASE ");
		nativeSQL.append("WHEN ( d.dishname <> '調味料' ) THEN i.suppliername ");
		nativeSQL.append("end ), '') AS ingredient_supplier, ");
		nativeSQL.append("Ifnull(( CASE ");
		nativeSQL.append("WHEN ( d.dishname <> '調味料' ) THEN ");
		nativeSQL.append("i.ingredientname ");
		nativeSQL.append("end ), '') AS ingredient_name, ");
		nativeSQL.append("Ifnull(( CASE ");
		nativeSQL.append("WHEN ( d.dishname = '調味料' ) THEN i.suppliername ");
		nativeSQL.append("end ), '') AS seasoning_supplier, ");
		nativeSQL.append("Ifnull(( CASE ");
		nativeSQL.append("WHEN ( d.dishname = '調味料' ) THEN ");
		nativeSQL.append("i.ingredientname ");
		nativeSQL.append("end ), '') AS seasoning_name ");
		nativeSQL.append("FROM   ((((((dishbatchdata d ");
		nativeSQL.append("LEFT JOIN ingredientbatchdata i ");
		nativeSQL.append("ON (( ( i.batchdataid = d.batchdataid ) ");
		nativeSQL.append("AND ( ( ( d.dishbatchdataid = i.dishid ) ");
		nativeSQL.append("AND ( d.dishid = 0 ) ) ");
		nativeSQL.append("OR ( ( i.dishid = d.dishid ) ");
		nativeSQL.append("AND ( d.dishid <> 0 ) ) ) ))) ");
		nativeSQL.append("LEFT JOIN batchdata b ");
		nativeSQL.append("ON (( b.batchdataid = d.batchdataid ))) ");
		nativeSQL.append("LEFT JOIN school s ");
		nativeSQL.append("ON (( ( s.schoolid = b.schoolid ) ");
		nativeSQL.append("AND ( Substr(s.schoolcode, 4, 1) IN ( 5, 6, 7, 8 ) ) ");
		nativeSQL.append("AND ( s.enable = 1 ) ))) ");
		nativeSQL.append("JOIN county c ");
		nativeSQL.append("ON (( ( c.countyid = s.countyid ) ");
		nativeSQL.append("AND ( c.enable = 1 ) ))) ");
		nativeSQL.append("JOIN kitchen k ");
		nativeSQL.append("ON (( ( k.kitchenid = b.kitchenid ) ");
		nativeSQL.append("AND ( k.kitchenid <> 6 ) ))) ");
		nativeSQL.append("LEFT JOIN area a ");
		nativeSQL.append("ON (( a.areaid = s.areaid ))) ");
		nativeSQL.append("WHERE  ( ( i.ingredientname IS NOT NULL ) ");
		nativeSQL.append("AND ( NOT (( i.ingredientname LIKE '%供餐%' )) ) ");
		nativeSQL.append("AND ( NOT (( s.schoolname LIKE '%測試%' )) ) ) ");
		// 加入查詢條件 : 供餐日期區間
		nativeSQL.append("AND STR_TO_DATE(b.menudate,'%Y/%m/%d') BETWEEN ");
		nativeSQL.append(String.format("'%s' AND '%s' ", menuStartDate, menuEndDate));
		// 加入查詢條件 : 學校縣市代碼
		if (countyId != null) {
			nativeSQL.append(String.format("AND c.countyid = %d ", countyId));
		}
		// 加入查詢條件 : 學校名稱
		if (schoolName != null && schoolName.length() > 0) {
			nativeSQL.append("AND s.schoolname LIKE '%");
			nativeSQL.append(schoolName);
			nativeSQL.append("%' ");
		}
		// 加入查詢條件 : 供餐業者統一編號
		if (kitchenCompanyId != null && kitchenCompanyId.length() > 0) {
			nativeSQL.append("AND k.companyid = '");
			nativeSQL.append(kitchenCompanyId);
			nativeSQL.append("' ");
		}
		// 加入查詢條件 : 供餐業者名稱
		if (kitchenName != null && kitchenName.length() > 0) {
			nativeSQL.append("AND k.kitchenname LIKE '%");
			nativeSQL.append(kitchenName);
			nativeSQL.append("%' ");
		}
		// 加入查詢條件 : 食材名稱
		if (igredientName != null && igredientName.length() > 0) {
			nativeSQL.append("AND i.ingredientname LIKE '%");
			nativeSQL.append(igredientName);
			nativeSQL.append("%' ");
		}
		// 加入查詢條件 : 食材供應商名稱
		if (igredientSupplier != null && igredientSupplier.length() > 0) {
			nativeSQL.append("AND i.suppliername LIKE '%");
			nativeSQL.append(igredientSupplier);
			nativeSQL.append("%' ");
		}
		// 加入查詢條件 : 調味料名稱
		if (seasoningName != null && seasoningName.length() > 0) {
			nativeSQL.append("AND i.ingredientname LIKE '%");
			nativeSQL.append(seasoningName);
			nativeSQL.append("%' ");
		}
		// 加入查詢條件 : 調味料供應商名稱
		if (seasoningSupplier != null && seasoningSupplier.length() > 0) {
			nativeSQL.append("AND i.suppliername LIKE '%");
			nativeSQL.append(seasoningSupplier);
			nativeSQL.append("%' ");
		}
		// 加入查詢條件 : 筆數限制
		nativeSQL.append(String.format("LIMIT %d", limit));
		
		// 執行查詢
		List<Object[]> ingredients_searchBOList = null;
		try {
			Query query = dbSession.createSQLQuery(nativeSQL.toString());
			ingredients_searchBOList = query.list();
			
			for(int i=0;i<ingredients_searchBOList.size();i++){
				Ingredients_searchBO bo = new Ingredients_searchBO();
				Object[] record = ingredients_searchBOList.get(i);
				bo.setCounty((String)record[0]);
				bo.setSchool_name((String)record[1]);
				bo.setMenu_date((String)record[2]);
				bo.setKitchen_name((String)record[3]);
				bo.setKitchen_company((String)record[4]);
				bo.setIngredient_supplier((String)record[5]);
				bo.setIngredient_name((String)record[6]);
				bo.setSeasoning_supplier((String)record[7]);
				bo.setSeasoning_name((String)record[8]);

				this.responseObj.getResult().add(bo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.responseObj.setMsg("資料庫連線或查詢問題,請連絡系統管理員");
			this.responseObj.setResStatus(0);
			return;
		}
		// 回傳查詢結果

        this.responseObj.setMsg("success");
		this.responseObj.setResStatus(1);
	}
	
	/*
	 * 檢核傳入參數是否內含SQL語法
	 */
	private boolean containSQL(String inStr) {
		String tmpStr = inStr.toUpperCase();
		if (tmpStr.contains("SELECT ") || tmpStr.contains("DELETE ") || tmpStr.contains("UPDATE ")
				|| tmpStr.contains("CREATE ") || tmpStr.contains("TRUNCATE ") || tmpStr.contains("ALTER ")
				|| tmpStr.contains("DROP ") || tmpStr.contains("INSERT ")) {
			return true;
		} else {
			return false;
		}
	}
}