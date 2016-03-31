package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.rest.bo.OfferedKitchenBO;

public class QueryOfferedListResponse   extends AbstractApiResponse{
	private List <OfferedKitchenBO> kitchenBO = new ArrayList<OfferedKitchenBO>();
	
	public List<OfferedKitchenBO> getKitchenBO() {
		return kitchenBO;
	}

	public void setKitchenBO(List<OfferedKitchenBO> kitchenBO) {
		this.kitchenBO = kitchenBO;
	}

}

