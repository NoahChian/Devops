package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryForNegResponse  extends AbstractApiResponse {

	private List <ForNegList>forNegList = new ArrayList<ForNegList>();
	
	public List <ForNegList > getForNegList() {
		return forNegList;
	}
	public void setForNegList(List <ForNegList > forNegList) {
		this.forNegList = forNegList;
	}
	
}
