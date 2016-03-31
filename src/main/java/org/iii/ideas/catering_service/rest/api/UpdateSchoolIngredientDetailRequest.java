package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class UpdateSchoolIngredientDetailRequest {
	private Long dishId;
	private String menuDate;
	private int ingredientsCount;
	private Integer schoolId;
	private List<IngredientObject> ingredients = new ArrayList<IngredientObject>();
	public int getIngredientsCount() {
		return ingredientsCount;
	}
	public void setIngredientsCount(int ingredientsCount) {
		this.ingredientsCount = ingredientsCount;
	}
	public List<IngredientObject> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<IngredientObject> ingredients) {
		this.ingredients = ingredients;
	}
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
