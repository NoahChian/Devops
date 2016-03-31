package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryCounterByCondResponse extends AbstractApiResponse {
	private String funcName;
	private List <CounterVo> counter = new ArrayList<CounterVo>();
	private HashMap<String ,List<String[]>> statistic=new HashMap<String ,List<String[]>>();
	private List<String[]> data =new ArrayList<String[]>();
	
	
	public void  setCountList(List <CounterVo> result){
		this.counter=result;
	}
	public List<CounterVo> getCountList(){
		return this.counter;
	}
	
	
	
	public void setFuncName(String funcName){
		this.funcName=funcName;
	}
	public String getFuncName(){
		return this.funcName;
	}
	
	public void setStatistic(HashMap<String ,List<String[]>> map){
		this.statistic=map;
	}
	public HashMap<String ,List<String[]>> getStatistic(){
		return this.statistic;
	}
	
	public void setData(List<String[]> data){
		this.data=data;
	}
	public List<String[]> getData(){
		return this.data;
	}
	
	
}
