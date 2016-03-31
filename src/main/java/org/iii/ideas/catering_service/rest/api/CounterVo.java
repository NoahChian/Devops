package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

public class CounterVo  implements Serializable {
	private String counterDate;
	
	private Integer count;
	
	public void setDate(String date){
		this.counterDate=date;
	}
	public String getDate(){
		return this.counterDate;
	}
	
	public void setCount(Integer count){
		this.count=count;
	}	
	public Integer getCount(){
		return this.count;
	}
}
