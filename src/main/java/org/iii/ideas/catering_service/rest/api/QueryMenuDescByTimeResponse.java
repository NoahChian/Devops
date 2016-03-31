package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryMenuDescByTimeResponse    extends AbstractApiResponse{
	private List<IngredientMenuObj> menu = new ArrayList<IngredientMenuObj>();

	public List<IngredientMenuObj> getMenu() {
		return menu;
	}

	public void setMenu(List<IngredientMenuObj> menu) {
		this.menu = menu;
	}

}


 