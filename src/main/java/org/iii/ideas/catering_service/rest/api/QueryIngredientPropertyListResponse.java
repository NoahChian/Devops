package org.iii.ideas.catering_service.rest.api;

import java.util.List;

import org.iii.ideas.catering_service.rest.bo.IngredientPropertyBO;

public class QueryIngredientPropertyListResponse extends AbstractApiResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6465269258755721397L;
	
	List<IngredientPropertyBO> ingredientPropertyList;

	public List<IngredientPropertyBO> getIngredientPropertyList() {
		return ingredientPropertyList;
	}

	public void setIngredientPropertyList(List<IngredientPropertyBO> ingredientPropertyList) {
		this.ingredientPropertyList = ingredientPropertyList;
	}
 	
}
