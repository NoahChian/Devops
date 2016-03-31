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

public class QueryStatistic  extends AbstractApiInterface<QueryStatisticRequest, QueryStatisticResponse>{
	private HashMap<String, String> sql=new HashMap<String, String>();
	private HashMap<String, String[]> header=new HashMap<String, String[]>();
	private static Connection conn_jdbc; 
	private String[] resultHeader;
	private String SQL_COUNTY_COND=" and s.CountyId=:COUNTYID ";
	
	private Integer queryLimit;
	private int page;
	private int perpage;
	
	@Override
	public void process() throws NamingException{
		String queryType=this.requestObj.getQueryItem();
		String userName=this.getUsername();
		Integer userCountyId=AuthenUtil.getCountyNumByUsername(userName);
		Integer total=0;
		List<Object[]> result=null;
		//try{
			String sqlStr=getSQL(queryType);
			if (!CateringServiceCode.AUTHEN_SUPER_COUNTY.equals(userCountyId.toString())){
				sqlStr=setParameter(sqlStr, "countyId", userCountyId);
			}else{
				sqlStr=sqlStr.replace(SQL_COUNTY_COND, "");			
			}
			sqlStr=setParameter(sqlStr,"startDate",this.requestObj.getStartDate());
			sqlStr=setParameter(sqlStr,"endDate",this.requestObj.getEndDate());
			
			if("dishCount".equals(queryType)){
				//dishCount 有兩份起迄
				sqlStr = getDishCountSql(this.requestObj.getStartDate(),this.requestObj.getEndDate(),this.requestObj.getCounty(),this.requestObj.getSchoolId());
			}
			
			sqlStr=setParameter(sqlStr,"numberDateStart",this.requestObj.getStartDate().replace("/", ""));
			sqlStr=setParameter(sqlStr,"numberDateEnd",this.requestObj.getEndDate().replace("/", ""));
//			System.out.println(sqlStr);
			
			result=queryJDBC(sqlStr);
		//}catch(Exception ex){
			//throw ex;
		//}
		
		//String[] resultHeader=getHeader(queryType);
		int countHeader=-1;
		if (resultHeader!=null){
			for (short i =0; i<resultHeader.length;i++){
				if ("DishCount".equals(resultHeader[i])){
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
	
	
	public QueryStatistic (){		
		sql.put("dishCount", "SELECT ifnull(c.county,'') as county,s.SchoolName AS SchoolName,IF(COUNT(rmd.RDishId)='0',COUNT(d.DishId),COUNT(rmd.RDishId))AS DishCount FROM school s "
				+ " LEFT JOIN schoolfoodstreet sfs ON s.SchoolId=sfs.SchoolId "
				+ " LEFT JOIN restaurant r ON sfs.SFStreetId=r.SFStreetId "
				+ " LEFT JOIN restaurantmenu rm ON r.RestaurantId=rm.RestaurantId AND rm.`enable`='1' "
				+ " LEFT JOIN restaurantmenudish rmd ON rm.RMenuId=rmd.RMenuId AND rmd.`enable`='1' "
				+ " LEFT JOIN batchdata b ON b.SchoolId=s.SchoolId "
				+ " LEFT JOIN dishbatchdata db ON db.BatchDataId = b.BatchDataId "
				+ " LEFT JOIN dish d ON d.DishId=db.DishId "
				+ " LEFT JOIN county c ON c.CountyId=s.CountyId AND c.`Enable`='1' "
				+ " WHERE 1=1 :school :county "
				+ " AND(rmd.menudate BETWEEN :STARTDATE AND :ENDDATE "
				+ " OR b.menudate BETWEEN :STARTDATE AND :ENDDATE ) GROUP BY s.SchoolId; ");
		
		sql.put("kitchen005List", "select count(*) as count from kitchen where KitchenType='005' ");
		sql.put("kitchen006List", "select count(*) as count  from kitchen where KitchenType='006' ");		
		sql.put("ingredientCount", "select  k.KitchenName ,count(i.IngredientId) as count "
				+ " from dish d ,ingredient i,kitchen k "
				+ " where i.DishId=d.DishId and k.KitchenId=d.KitchenId  group by d.KitchenId" );
		
		sql.put("queryCountBySchool", "SELECT s.SchoolName as SchoolName , sum(c.count) as count FROM cateringservice.counter c ,school s "
				+ " where s.SchoolId= c.kitchenid and  (date between :NUMBERDATESTART and :NUMBERDATEEND )  "
				+ " and funcName='customerQueryCateringBySchoolAndDate' "
				+SQL_COUNTY_COND//+ " and s.CountyId=:COUNTYID  "
				+ "group by c.kitchenid order by SchoolName ASC ");
		
		
		sql.put("queryCountByDate", "SELECT c.date as date , sum(c.count) as count FROM cateringservice.counter c ,school s "
				+ " where s.SchoolId= c.kitchenid and  (date between :NUMBERDATESTART and :NUMBERDATEEND )  "
				+ " and funcName='customerQueryCateringBySchoolAndDate' "
				+SQL_COUNTY_COND//+ " and s.CountyId=:COUNTYID  "
				+ " group by c.date order by date ASC");
	}
	
	// #11542 菜色量統計報表
	public String getDishCountSql(String startDate,String endDate,String countyId,String schoolId) {
		String sqlStr = sql.get("dishCount");
		if(sqlStr == null){
			return sqlStr;
		}
		
		sqlStr=setParameter(sqlStr,"startDate",startDate);
		sqlStr=setParameter(sqlStr,"endDate",endDate);
		
		//dishCount 有兩份起迄
		sqlStr=setParameter(sqlStr,"startDate",startDate);
		sqlStr=setParameter(sqlStr,"endDate",endDate);

		if(!"-1".equals(schoolId)){
			sqlStr=sqlStr.replace(":school", " AND s.SchoolId='"+schoolId+"' ");
		} 
		if(!"-1".equals(countyId)){
			sqlStr=sqlStr.replace(":county", " AND c.CountyId='"+countyId+"' ");
		}
		sqlStr=sqlStr.replace(":school","").replace(":county","");
			
		return sqlStr;
	}

	public Integer getQueryLimit() {
		return queryLimit;
	}

	public void setQueryLimit(Integer queryLimit) {
		this.queryLimit = queryLimit;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerpage() {
		return perpage;
	}

	public void setPerpage(int perpage) {
		this.perpage = perpage;
	}
}
