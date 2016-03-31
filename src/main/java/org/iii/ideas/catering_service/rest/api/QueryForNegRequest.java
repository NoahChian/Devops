package org.iii.ideas.catering_service.rest.api;

public class QueryForNegRequest {
	private String supplyName; //供應商名稱
	private String ingredientName;//食材名稱
	private String startDate;//菜單日期(起)
	private String endDate;//菜單日期(訖)

	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	public String getIngredientName() {
		return ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	

}
