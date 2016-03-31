package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.CounterDAO;

public class QueryCounterByCond  extends AbstractApiInterface< QueryCounterByCondRequest, QueryCounterByCondResponse> {
	private QueryCounterByCondRequest refRequest;	
	private QueryCounterByCondResponse refResponse ;

	private String funcName;
	private String startDate;
	private String endDate;
	private Integer sid;
	private String myFunction;
	private Integer countyId;
	private CounterDAO dao =new CounterDAO();

	@Override
	public void process() throws NamingException, ParseException {
		refRequest= this.requestObj;	
		refResponse = this.responseObj;
		
		refResponse.setResStatus(0);
		refResponse.setMsg("查詢失敗");

		funcName=refRequest.getFuncName();
		startDate=refRequest.getStartDate();
		endDate=refRequest.getEndDate();
		sid=refRequest.getSid();
		myFunction=refRequest.getCountType();
		countyId=refRequest.getCountyId();
		
		dao.openSessionFactory();
		
		//select m.date,m.count ,m.kitchenId ,s.schoolName
		System.out.println("進入queryAllSchoolByCounty");
		List result =dao.listQueryCountByFuncAndCountyAndDateRange(funcName,countyId, startDate, endDate);
		

		/*Method myMethod;		
		try{
			myMethod=this.getClass().getMethod(myFunction);	
		}catch (Exception ex){
			System.out.println("無法抓到function");
		}*/
		if (myFunction.equals("queryAllSchoolByCounty")){
			queryAllSchoolByCounty();
		}else if(myFunction.equals("querySingleSchool")){
			querySingleSchool();
		}else{
			System.out.println("沒有對應的function ");
		}
		
		dao.closeSession();
		
		refResponse.setFuncName(funcName);
		refResponse.setResStatus(1);
		refResponse.setMsg("查詢成功");

	}
	//以縣市查學校區間日期
	private void queryAllSchoolByCounty() {
		//select m.date,m.count ,m.kitchenId ,s.schoolName
		System.out.println("進入queryAllSchoolByCounty");
		List result =dao.listQueryCountByFuncAndCountyAndDateRange(funcName,countyId, startDate, endDate);
		
		HashMap<String,List<String[]>> map=new HashMap<String,List<String[]>>();
		
		Iterator<Object[]> it=result.iterator();
		while(it.hasNext()){
			Object[] obj=it.next();
			System.out.println((String)obj[3]);

			String data[]={(String)obj[0], ((Integer)obj[1]).toString(),(String)obj[3]};
		
			if (map.get((String)obj[0])==null){
				List<String[]> ls=new ArrayList<String[]>();
				ls.add(data);
				map.put(((Integer)obj[2]).toString(),ls);
			}else{
				map.get(((Integer)obj[2]).toString()).add(data);
			}
			

			responseObj.setStatistic(map);
		}
		
	}
	
	
	
	
	//查單一學校
	private void querySingleSchool(){

		List result =dao.countByFuncNameAndDateRange(funcName, startDate, endDate, sid);
		
		Iterator<Object[]> it=result.iterator();
		while(it.hasNext()){
			Object[] obj=it.next();
			CounterVo item=new CounterVo();
			item.setDate((String) obj[0]);
			item.setCount((Integer) obj[1]);
			responseObj.getCountList().add(item);
		}

	}
/*	
	private void getAllSchoolByCountyId(Integer countyId){
		SchoolDAO dao=new SchoolDAO();
		
		List schools = dao.findByCountyId(countyId);

		Iterator<Object[]> iterator = schools.iterator();
		//List <String[]> ls=new ArrayList<String[]>();
		
		while (iterator.hasNext()) {
			Object[] sc = iterator.next();
			String data[]={((Integer)sc[0]).toString(),(String) sc[1]};

			this.responseObj.getData().add(data);
			
		}
		
	}
	
	*/
	
}
