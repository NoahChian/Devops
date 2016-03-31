package org.iii.ideas.catering_service.rest.api;

public class QueryStatisticRequest {

	private String queryItem="";
	private String startDate="";
	private String endDate="";
	private String county="";
	private String schoolId = "";
	
	private Integer queryLimit;
	private int page;
	private int perpage;
	
	public void setQueryItem(String pQueryItem){
		this.queryItem=pQueryItem;
	}
	public String getQueryItem(){
		return this.queryItem;
	}
	
	public void setStartDate(String pStartDate){
		this.startDate=pStartDate;
	}
	public String getStartDate(){
		return this.startDate;
	}
	
	public void setCounty(String pCounty){
		this.county=pCounty;
	}
	public String getCounty(){
		return this.county;
	}
	
	public void setEndDate(String pEndDate){
		this.endDate=pEndDate;
	}
	public String getEndDate(){
		return this.endDate;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
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
