package org.iii.ideas.catering_service.rest.api;

public class QueryCertificationInfoRequest {
	private String ingredientName; //食材名稱
	private String kitchenId; //團膳業者/自立午餐學校名稱
	private String restaurantId; //餐廳
	private String startDate; //菜單日期(起)
	private String endDate; //菜單日期(訖)
	private Integer queryLimit;
	private int page;
	private int perpage;

	public String getIngredientName() {
		return ingredientName;
	}
	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
	
	public String getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(String kitchenName) {
		this.kitchenId = kitchenName;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
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
