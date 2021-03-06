package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QuerySchoolIngredientDetailResponse   extends AbstractApiResponse  {
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
	
}
