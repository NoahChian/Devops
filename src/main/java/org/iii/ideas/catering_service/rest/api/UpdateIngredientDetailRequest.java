package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class UpdateIngredientDetailRequest {
	private String food;
	private Long mid;
	private int ingredientsCount;
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
	public String getFood() {
		return food;
	}
	public void setFood(String food) {
		this.food = food;
	}
	public Long getMid() {
		return mid;
	}
	public void setMid(Long batchdataId) {
		this.mid = batchdataId;
	}
}
