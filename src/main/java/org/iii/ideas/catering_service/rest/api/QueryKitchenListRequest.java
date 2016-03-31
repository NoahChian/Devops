package org.iii.ideas.catering_service.rest.api;

public class QueryKitchenListRequest  extends AbstractApiResponse  {

	private String kitchenName;
	private String companyId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	
	
}
