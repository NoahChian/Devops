package org.iii.ideas.catering_service.rest.api;

public class QuerySchoolIngredientDetailRequest {
	private String menuDate;
	private Long dishId;
	private Integer schoolId;
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
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

}
