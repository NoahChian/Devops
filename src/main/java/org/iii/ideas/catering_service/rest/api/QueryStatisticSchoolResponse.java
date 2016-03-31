package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryStatisticSchoolResponse  extends AbstractApiResponse{
	private List<Object[]> result;
	private Integer total;
	private String msg;
	private String[] header;
	
	public void setResult(List<Object[]> result){
		this.result=result;
	}
	public List<Object[]>  getResult(){
		return this.result;
	}
	
	public void setTotal(Integer total){
		this.total=total;
	}
	public Integer getTotal(){
		return this.total;
	}
	public void setMsg(String msg){
		this.msg=msg;
	}
	public String getMsg(){
		return this.msg;
	}
	
	
	public void setHeader(String[] header){
		this.header=header;
	}
	public String[] getHeader(){
		return this.header;
	}
	
}
