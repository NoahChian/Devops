package org.iii.ideas.catering_service.rest.excel.create;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.iii.ideas.catering_service.rest.api.QueryStatisticSchool;


public class StatisticSupplier implements IGenerateExcel {
	
	private Integer countyid;
	private Integer kitchenid;
	private Integer restaurantId;
	private Integer schoolId;
	private String queryType;

	
	public StatisticSupplier(Integer countyid, Integer kitchenid, Integer restaurantId, Integer schoolId, String queryType) {
		this.countyid = countyid;
		this.kitchenid = kitchenid;
		this.restaurantId = restaurantId;
		this.schoolId = schoolId;
		this.queryType = queryType;
	}
	
	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		List<Object[]> result=null;
		
		data.put("1",
				new Object[] { "縣市  ","供應商名稱","受供餐學校" });
		
		QueryStatisticSchool q1 = new QueryStatisticSchool();
		String sqlStr = q1.getSupplierCountSql(Integer.toString(countyid), Integer.toString(kitchenid), Integer.toString(restaurantId), Integer.toString(schoolId),queryType);
		result= q1.queryJDBC(sqlStr);
		
		int row = 2;
		
		for(int i=0 ; i <result.size();i++){
			Object[] temp = (Object[])result.get(i);			
			data.put(
			String.valueOf(row),
			temp);
			row++;
		}
		return data;
	}
}
