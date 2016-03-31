package org.iii.ideas.catering_service.rest.api;

public class UpdateSeasoningResponse extends AbstractApiResponse{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6797603730651356908L;
	private String ingredientName;
	
	public String getIngredientName() {
		return ingredientName;
	}
	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
	
}
