package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryKitchenList2Response   extends AbstractApiResponse{
	private List <KitchenList>kitchenList = new ArrayList<KitchenList>();

	public List <KitchenList> getKitchenList() {
		return kitchenList;
	}

	public void setKitchenList(List <KitchenList> kitchenList) {
		this.kitchenList = kitchenList;
	}

}

