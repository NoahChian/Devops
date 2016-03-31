package org.iii.ideas.catering_service.rest.api;

public class QueryCounterByCondRequest {
	private String funcName;
	private String startDate;
	private String endDate;
	private Integer sid;
	private Integer countyId=0;
	private String countType="querySingleSchool";
	
	public Integer getSid() {
		return this.sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public String getStartDate() {
		return this.startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return this.endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getFuncName() {
		return this.funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
	public void setCountType(String countType){
		this.countType=countType;
	}
	
	public String getCountType(){
		return this.countType;
	}

	public Integer getCountyId(){
		return this.countyId;
	}
	public void setCountyId(Integer countyId){
		this.countyId=countyId;		
	}
	
	
}
