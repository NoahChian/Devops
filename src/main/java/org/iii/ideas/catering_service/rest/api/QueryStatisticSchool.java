package org.iii.ideas.catering_service.rest.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.cfg.Configuration;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;

public class QueryStatisticSchool  extends AbstractApiInterface<QueryStatisticSchoolRequest, QueryStatisticSchoolResponse>{
	private HashMap<String, String> sql=new HashMap<String, String>();
	private HashMap<String, String[]> header=new HashMap<String, String[]>();
	private static Connection conn_jdbc; 
	private String[] resultHeader;
	private String SQL_COUNTY_COND=" and s.CountyId=:COUNTYID ";
	
	@Override
	public void process() throws NamingException{
		String queryItem=this.requestObj.getQueryItem();
		String queryType=this.requestObj.getQueryType();
		
		String userName=this.getUsername();
		Integer userCountyId=AuthenUtil.getCountyNumByUsername(userName);
		Integer total=0;
		List<Object[]> result=null;
		//try{
			String sqlStr=getSQL(queryItem);
			
			if("statisticSchool".equals(queryItem)){
				sqlStr = getSchoolCountSql(this.requestObj.getCounty(),this.requestObj.getKitchenId(),this.requestObj.getRestaurantId(),this.requestObj.getSchoolId(),"-1");
			}
			
			if("statisticKitchen".equals(queryItem)){
				sqlStr = getKitchenCountSql(this.requestObj.getCounty(),this.requestObj.getKitchenId(),this.requestObj.getRestaurantId(),this.requestObj.getSchoolId(),queryType);
			}
			
			if("statisticSupplier".equals(queryItem)){
				sqlStr = getSupplierCountSql(this.requestObj.getCounty(),this.requestObj.getKitchenId(),this.requestObj.getRestaurantId(),this.requestObj.getSchoolId(),queryType);
			}
			
//			System.out.println(sqlStr);
			
			result=queryJDBC(sqlStr);
			
			if("statisticSchool".equals(queryItem)||"statisticKitchen".equals(queryItem)){
				result = transferData(result,"<br>");
			}
		//}catch(Exception ex){
			//throw ex;
		//}
		
		//String[] resultHeader=getHeader(queryType);
		int countHeader=-1;
		if (resultHeader!=null){
			for (short i =0; i<resultHeader.length;i++){
				if ("SchoolCount".equals(resultHeader[i])){
					countHeader=i;
					break;
				}
			}
		}
//		if (result!=null && countHeader!=-1){
//			for(short i =0; i<result.size(); i++){
//				total=total+Integer.parseInt((String)result.get(i)[countHeader]);
//			}
//		}
		
//		if(total == 0 && result !=null){
			total = result.size();
//		}
		
//		this.requestObj.getQueryLimit();
		int page =this.requestObj.getPage();
		int perPage = this.requestObj.getPerpage();
		List<Object[]> resultFinal = new ArrayList();
		
		if(result != null){
			if(page != 0 && perPage !=0){
				int startIndex = (page-1) * perPage;
				int endIndex = startIndex + perPage;
				
				if(endIndex >= result.size()){
					endIndex = result.size();
				}
				
				for(int i=startIndex;i<endIndex;i++){
					resultFinal.add(result.get(i));
				}
				
				if(resultFinal.size() != 0){
					result = resultFinal;
				}
			}
		}
			
		this.responseObj.setResult(result);
		this.responseObj.setTotal(total);
		this.responseObj.setResStatus(1);
		this.responseObj.setHeader(resultHeader);
		this.responseObj.setMsg("查詢成功");
	}
	
	private String getSQL(String qType){
		if (sql.containsKey(qType)){
			return sql.get(qType);
		}else{
			return null;
		}
		
	}
	
	private String setParameter(String sql,String key,Object value){
		if (value.getClass().toString().equals(String.class.toString())){
			value="'"+value+"'";
		}
		sql=sql.replaceFirst(":"+key.toUpperCase(), value.toString());
		return sql;
	}
	
	private String[] getHeader(String qType){
		if (header.containsKey(qType)){
			return header.get(qType);
		}else{
			return null;
		}	
	}
	
	public List<Object[]> queryJDBC(String sql){
		if (sql==null){
			return null;
		}
		try{
			Configuration config =new Configuration().configure();			
			String connstr=config.getProperty("connection.url");
			this.conn_jdbc=DriverManager.getConnection(connstr,config.getProperty("connection.username"),config.getProperty("connection.password"));
			Statement st=this.conn_jdbc.createStatement();
			ResultSet  resultSet= st.executeQuery(sql);
			List<Object[]> result=new ArrayList<Object[]>();
			ResultSetMetaData resultMeta=resultSet.getMetaData();
			resultHeader=new String[resultMeta.getColumnCount()];
			
			//System.out.println(resultSet.getFetchSize());
			for(short i =1; i<=resultMeta.getColumnCount();i++){
				resultHeader[i-1]=resultMeta.getColumnName(i).toString();
			}
			
			while(resultSet.next()){
				Object[] row=new Object[resultMeta.getColumnCount()];
				for(short i =1; i<=resultMeta.getColumnCount(); i++){
					row[i-1]=resultSet.getString(i);
				}
				result.add(row);
			}
			
			
			this.conn_jdbc.close();
			return result;
		}catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	
	public QueryStatisticSchool (){
		//c.CountyId,c.County AS County,k.KitchenId AS KitchenId,k.kitchenName AS KitchenName,"+
        //" s.SchoolId AS SchoolId,s.schoolname AS SchoolName
		sql.put("statisticSchool","	SELECT c.County AS County,k.kitchenName AS KitchenName,"+
            " s.schoolname AS SchoolName FROM schoolkitchen sk"+
            " INNER JOIN kitchen k ON k.KitchenId = sk.KitchenId AND k.enable = '1'"+
            " INNER JOIN school s ON sk.SchoolId = s.SchoolId AND length(s.SchoolCode) != 4"+
            " AND s.Enable = '1' LEFT JOIN county c ON s.CountyId = c.CountyId  :WHERE  ORDER BY k.KitchenId"
				);
		
		//c.CountyId,c.County AS County,"+
        //" IFNULL(r.RestaurantId, IFNULL(k.KitchenId, '')) AS KitchenId,"+
        //" IFNULL(r.RestaurantName, IFNULL(k.kitchenName, '')) AS KitchenName,"+
        //" s.SchoolId AS SchoolId, s.schoolname AS SchoolName
		sql.put("statisticKitchen"," SELECT c.County AS County,"+
                " IFNULL(r.RestaurantName, IFNULL(k.kitchenName, '')) AS KitchenName,"+
                " s.schoolname AS SchoolName FROM"+
                " schoolkitchen sk INNER JOIN kitchen k ON k.KitchenId = sk.KitchenId AND k.enable = '1'"+
                " INNER JOIN school s ON sk.SchoolId = s.SchoolId AND s.Enable = '1'"+
                " LEFT JOIN schoolfoodstreet sfs ON s.SchoolId = sfs.SchoolId " +
                " LEFT JOIN restaurant r ON sfs.SFStreetId = r.SFStreetId " +
                " LEFT JOIN county c ON s.CountyId = c.CountyId  :WHERE  ORDER BY k.KitchenId");
		
		//k.CompanyId=sp.CompanyId
		sql.put("statisticSupplierPart1","SELECT c.county AS County, sp.SupplierName AS SupplierName, s.schoolname AS SchoolName " +
				"FROM school s INNER JOIN schoolkitchen sk ON s.SchoolId=sk.SchoolId INNER JOIN kitchen k " +
				"ON k.KitchenId=sk.KitchenId INNER JOIN supplier sp ON k.KitchenId = sp.KitchenId INNER JOIN county c ON c.CountyId=s.CountyId ");
		
		sql.put("statisticSupplierPart2","SELECT distinct c.county AS County, sp.SupplierName AS SupplierName, s.schoolname AS SchoolName  " +
				"FROM school s INNER JOIN schoolfoodstreet sfs ON sfs.SchoolId=s.SchoolId INNER JOIN restaurant r " +
				"ON r.SFStreetId=sfs.SFStreetId INNER JOIN restaurantingredient ri ON ri.RestaurantId=r.RestaurantId INNER JOIN supplier sp " +
				"ON ri.SupplierCompanyId=sp.CompanyId INNER JOIN county c ON c.CountyId=s.CountyId ");
	}
	
	// #11633 學校數量與清單
	public String getSchoolCountSql(String countyId,String kitchenId,String restaurantId, String schoolId,String queryType) {
		String sqlStr = sql.get("statisticSchool");
		if(sqlStr == null){
			return sqlStr;
		}
		//"WHERE t2.CountyId = :COUNTYID and t1.KitchenId = :KITCHENID and t2.SchoolId = :SCHOOLID"
		
		sqlStr = setWhereForSchool(sqlStr,countyId,kitchenId,restaurantId,schoolId,queryType);
		return sqlStr;
	}
	
	// #11634 廚房數量與清單
	public String getKitchenCountSql(String countyId,String kitchenId,String restaurantId, String schoolId, String queryType) {
		String sqlStr = sql.get("statisticKitchen");
		if(sqlStr == null){
			return sqlStr;
		}
		//"WHERE t2.CountyId = :COUNTYID and t1.KitchenId = :KITCHENID and t2.SchoolId = :SCHOOLID"
		sqlStr = setWhereForKitchen(sqlStr,countyId,kitchenId,restaurantId,schoolId,queryType);
		return sqlStr;
	}
	
	// # 供應商數量與清單
	public String getSupplierCountSql(String countyId,String kitchenId,String restaurantId, String schoolId, String queryType) {
		String sqlStr1 = sql.get("statisticSupplierPart1");
		String sqlStr2 = sql.get("statisticSupplierPart2");
		String finalStr = "";

		if(sqlStr1 == null && sqlStr2 == null){
			return null;
		}
		//"WHERE t2.CountyId = :COUNTYID and t1.KitchenId = :KITCHENID and t2.SchoolId = :SCHOOLID"
		finalStr = setWhereForSupplier(sqlStr1,sqlStr2,countyId,kitchenId,restaurantId,schoolId,queryType);
		return finalStr;
	}
	
	public String setWhere(String sqlStr,String countyId,String kitchenId,String restaurantId,String schoolId,String queryType){
		String whereF = ""; //較內層的where for school
		
		if(!"-1".equals(schoolId)){
			whereF += "AND s.SchoolId = '"+schoolId+"' ";
			if(!"-1".equals(countyId)){
				countyId = String.valueOf(AuthenUtil.getCountyNumBySchoolId(Integer.valueOf(schoolId)));
			}
			whereF += "AND s.CountyId = '"+countyId+"' ";
		} else if(!"-1".equals(countyId)){
			whereF += "AND s.CountyId = '"+countyId+"' ";
		}
		
		if(!"-1".equals(kitchenId)){
			whereF += "AND k.KitchenId = '"+kitchenId+"' ";
		}
//		if(!"-1".equals(restaurantId)){
//			where += "AND t2.restaurantId = '"+restaurantId+"' ";
//			whereF += "AND t2.restaurantId = '"+restaurantId+"' ";
//		}
		
		if(!"-1".equals(queryType)){
			if("kitchen".equals(queryType)){
				whereF += "AND k.kitchenName is not null ";
			} else if ("restaurant".equals(queryType)){
				whereF += "AND r.RestaurantName is not null ";
			}
		}
		
		if(!"".equals(whereF)){
			whereF = whereF.replaceFirst("AND", "WHERE");
		}
		
		sqlStr = sqlStr.replace(":WHERE", whereF);
//		sqlStr += where;
		return sqlStr;
	}
	
	/*
	 * 組合query學校數量SQL語法的where部分
	 */
	public String setWhereForSchool(String sqlStr, String countyId, String kitchenId, String restaurantId, String schoolId, String queryType) {
		String whereF = ""; // 較內層的where for school
		if (!"-1".equals(schoolId)) {
			whereF += "AND s.SchoolId = '" + schoolId + "' ";
			if (!"-1".equals(countyId)) {
				countyId = String.valueOf(AuthenUtil.getCountyNumBySchoolId(Integer.valueOf(schoolId)));
			}
			whereF += "AND s.CountyId = '" + countyId + "' ";
		} else if (!"-1".equals(countyId)) {
			whereF += "AND s.CountyId = '" + countyId + "' ";
		}
		if (!"-1".equals(kitchenId)) {
			whereF += "AND k.KitchenId = '" + kitchenId + "' ";
		}
		if (!"-1".equals(queryType)) {
			if ("kitchen".equals(queryType)) {
				whereF += "AND k.kitchenName is not null ";
			} else if ("restaurant".equals(queryType)) {
				whereF += "AND r.RestaurantName is not null ";
			}
		}
		
		// #13611 主管機關查詢學校數量與清單 : 修正預期只出現學校之自設廚房(KitchenType = 006) 但連同團膳業者也出現的問題
		if(whereF.contains("s.SchoolId") || whereF.contains("k.KitchenId") || whereF.contains("k.kitchenName") || whereF.contains("r.RestaurantName")){
		} else {
			whereF += "AND k.KitchenType IN ('006')";
		}
		if (!"".equals(whereF)) {
			whereF = whereF.replaceFirst("AND", "WHERE");
		}
		sqlStr = sqlStr.replace(":WHERE", whereF);
		return sqlStr;
	}
	
	/*
	 * 組合query廚房數量SQL語法的where部分
	 */
	public String setWhereForKitchen(String sqlStr, String countyId, String kitchenId, String restaurantId, String schoolId, String queryType) {
		String whereF = ""; // 較內層的where for school
		if (!"-1".equals(schoolId)) {
			whereF += "AND s.SchoolId = '" + schoolId + "' ";
			if (!"-1".equals(countyId)) {
				countyId = String.valueOf(AuthenUtil.getCountyNumBySchoolId(Integer.valueOf(schoolId)));
			}
			whereF += "AND s.CountyId = '" + countyId + "' ";
		} else if (!"-1".equals(countyId)) {
			whereF += "AND s.CountyId = '" + countyId + "' ";
		}
		if (!"-1".equals(kitchenId)) {
			whereF += "AND k.KitchenId = '" + kitchenId + "' ";
		}
		if (!"-1".equals(queryType)) {
			if ("kitchen".equals(queryType)) {
				whereF += "AND k.kitchenName is not null ";
			} else if ("restaurant".equals(queryType)) {
				whereF += "AND r.RestaurantName is not null ";
			}
		}
		
		// #13612 縣市主管機關(user type長度6碼)查詢廚房數量及清單,預期只出現團膳業者(KitchenType = 005)
		if (("" + this.getUserType()).length() == 6) {
			whereF += "AND k.KitchenType IN ('005')";
		}
		if (!"".equals(whereF)) {
			whereF = whereF.replaceFirst("AND", "WHERE");
		}
		sqlStr = sqlStr.replace(":WHERE", whereF);
		return sqlStr;
	}

	public String setWhereForSupplier(String sqlStr1,String sqlStr2,String countyId,String kitchenId,String restaurantId,String schoolId,String queryType){
		String whereCommon = ""; //query KitchenId
		String where1 = ""; //query KitchenId
		String where2 = ""; //query RestaurantId
		//AND c.CountyId = '26' AND r.RestaurantId = '6' AND s.SchoolId = '1099'
		String finalStr ="";
		
		if(!"-1".equals(schoolId)){
			whereCommon += "AND s.SchoolId = '"+schoolId+"' ";
			
			if(!"-1".equals(countyId)){
				countyId = String.valueOf(AuthenUtil.getCountyNumBySchoolId(Integer.valueOf(schoolId)));
			}
			whereCommon += "AND c.CountyId = '"+countyId+"' ";
		} else if(!"-1".equals(countyId)){
			whereCommon += "AND c.CountyId = '"+countyId+"' ";
		}
		
		if(!"-1".equals(kitchenId)){
			where1 += "AND k.KitchenId = '"+kitchenId+"' ";
		}
		if(!"-1".equals(restaurantId)){
			where2 += "AND r.RestaurantId = '"+restaurantId+"' ";
		}
		
		where1 += whereCommon ;
		where2 += whereCommon ;

		if(!"".equals(where1)){
			where1 = where1.replaceFirst("AND", "WHERE");
		}
		
		if(!"".equals(where2)){
			where2 = where2.replaceFirst("AND", "WHERE");
		}
		
		if(!"-1".equals(queryType)){
			if("kitchen".equals(queryType)){
				finalStr = sqlStr1 + where1;
			} else if ("restaurant".equals(queryType)){
				finalStr = sqlStr2 + where2;
			}
		} else {
			finalStr = sqlStr1 + where1 + " UNION " + sqlStr2 + where2;
		}
		
		return finalStr;
	}
	
	public List<Object[]> transferData(List<Object[]> result,String enter){
		List<Object[]> finalResult = new ArrayList();
		if(result != null){
				String groupName ="";
				int schoolCount = 0;
				String schoolName ="";
				for(int i=0; i<result.size(); i++){
					Object[]temp = result.get(i);
					
					if(i==0){
						groupName = (String)temp[1];
					}
					
					if(!groupName.equals((String)temp[1])){
						//當廚房改變時,才塞入資料
						groupName = (String)temp[1];
						Object[] tempPre = result.get(i-1);							
						Object[]save = new Object[4];
						save[0] = tempPre[0];
						save[1] = tempPre[1];
						save[2] = schoolCount;
						save[3] = schoolName;
						finalResult.add(save);
						schoolName = (String)temp[2];
						schoolCount = 1;
					} else {
						schoolName += temp[2] + enter; 
						schoolCount++;
					}
					
					if(i == (result.size()-1)){
						Object[]save = new Object[4];
						save[0] = temp[0];
						save[1] = temp[1];
						save[2] = schoolCount;
						save[3] = schoolName;
						finalResult.add(save);
					}
				}
				
				if(finalResult.size() != 0){
					result = finalResult;
				}
			}
		return finalResult;
	}
}
