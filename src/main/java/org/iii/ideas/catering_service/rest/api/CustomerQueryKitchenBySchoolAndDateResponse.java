package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class CustomerQueryKitchenBySchoolAndDateResponse  extends AbstractApiResponse {
	private List<CustomerQueryKitchenObject> kitchen = new ArrayList<CustomerQueryKitchenObject>();

	public List<CustomerQueryKitchenObject> getKitchen() {
		return kitchen;
	}

	public void setKitchen(List<CustomerQueryKitchenObject> kitchen) {
		this.kitchen = kitchen;
	}
	
}
