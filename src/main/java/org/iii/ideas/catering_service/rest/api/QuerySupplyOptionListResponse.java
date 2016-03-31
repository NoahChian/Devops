package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;


public class QuerySupplyOptionListResponse  extends AbstractApiResponse {
	private List<SupplyNameOption> supplyNameOption = new ArrayList<SupplyNameOption>();

	public List<SupplyNameOption> getSupplyNameOption() {
		return supplyNameOption;
	}

	public void setSupplyNameOption(List<SupplyNameOption> supplyNameOption) {
		this.supplyNameOption = supplyNameOption;
	}
}
