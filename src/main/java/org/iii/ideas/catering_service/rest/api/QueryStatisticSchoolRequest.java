package org.iii.ideas.catering_service.rest.api;

public class QueryStatisticSchoolRequest {

	private String queryItem="";
	private String kitchenId="";
	private String restaurantId="";
	private String county="";
	private String schoolId="";
	private String queryType="";
	
	private Integer queryLimit;
	private int page;
	private int perpage;
	
	public void setQueryItem(String pQueryItem){
		this.queryItem=pQueryItem;
	}
	public String getQueryItem(){
		return this.queryItem;
	}
	public void setCounty(String pCounty){
		this.county=pCounty;
	}
	public String getCounty(){
		return this.county;
	}
	public String getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(String kitchenId) {
		this.kitchenId = kitchenId;
	}
	public String getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
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
