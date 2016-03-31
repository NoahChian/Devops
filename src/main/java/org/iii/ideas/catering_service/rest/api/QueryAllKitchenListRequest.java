package org.iii.ideas.catering_service.rest.api;

public class QueryAllKitchenListRequest {
	
	/**
	 * 業者類型
	 */
	private String[] kitchenType;

	public QueryAllKitchenListRequest() {
		kitchenType = null;
	}
	
	public String[] getKitchenType() {
		return kitchenType;
	}

	public void setKitchenType(String[] kitchenType) {
		this.kitchenType = kitchenType;
	}
	
	
}
