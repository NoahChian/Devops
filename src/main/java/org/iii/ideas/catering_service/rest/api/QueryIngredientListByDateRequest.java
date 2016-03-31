package org.iii.ideas.catering_service.rest.api;

public class QueryIngredientListByDateRequest  extends AbstractApiResponse  {
	private String menuDate="";

	public String getMenuDate() {
		return menuDate;
	}

	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}
	
}
