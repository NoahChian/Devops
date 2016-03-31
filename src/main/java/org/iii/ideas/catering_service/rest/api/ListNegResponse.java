package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class ListNegResponse  extends AbstractApiResponse {
	
	
	private List <NegList>negList = new ArrayList<NegList>();
	
	public List <NegList> getNegList() {
		return negList;
	}
	public void setNegList(List <NegList> negList) {
		this.negList = negList;
	}
	
	
}
