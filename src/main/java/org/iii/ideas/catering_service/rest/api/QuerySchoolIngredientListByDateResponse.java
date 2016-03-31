package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QuerySchoolIngredientListByDateResponse   extends AbstractApiResponse{
	private List <DishsObject>dishs = new ArrayList<DishsObject>();

	public List <DishsObject> getDishs() {
		return dishs;
	}

	public void setDishs(List <DishsObject> dishs) {
		this.dishs = dishs;
	}

}

