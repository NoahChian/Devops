package org.iii.ideas.catering_service.rest.api;

public class QueryIngredientDetailRequest {
	private String food;
	private Long batchdataId;
//	private int mid;

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public Long getBatchdataId() {
		return batchdataId;
	}

	public void setBatchdataId(Long batchdataId) {
		this.batchdataId = batchdataId;
	}
}
