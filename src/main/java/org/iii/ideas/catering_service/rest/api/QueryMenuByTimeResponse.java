package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryMenuByTimeResponse   extends AbstractApiResponse{
	private List <MenuObject>menu = new ArrayList<MenuObject>();

	public List <MenuObject> getMenu() {
		return menu;
	}

	public void setMenu(List <MenuObject> menu) {
		this.menu = menu;
	}
}
