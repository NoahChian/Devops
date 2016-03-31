package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryNullIngredientBySchoolAndTimeResponse  extends AbstractApiResponse{
	
	private List <NullIngreduentObject>nullIngreduentList = new ArrayList<NullIngreduentObject>();

	public List<NullIngreduentObject> getNullIngreduentList() {
		return nullIngreduentList;
	}

	public void setNullIngreduentList(List<NullIngreduentObject> nullIngreduentList) {
		this.nullIngreduentList = nullIngreduentList;
	}

	

	
}
