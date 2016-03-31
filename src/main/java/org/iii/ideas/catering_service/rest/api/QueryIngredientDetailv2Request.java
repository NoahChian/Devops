package org.iii.ideas.catering_service.rest.api;

public class QueryIngredientDetailv2Request {
	private String menuDate;
	private Long dishId;
	public String getMenuDate() {
		return menuDate;
	}
	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}
	public Long getDishId() {
		return dishId;
	}
	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}

}
