package org.iii.ideas.catering_service.rest.api;

public class DeleteNegRequest {
	private String idneg_ingredientId; //異常狀況編號
	private String lotNumber;
	private String stockDate;

	public String getIdneg_ingredientId() {
		return idneg_ingredientId;
	}
	public void setIdneg_ingredientId(String idneg_ingredientId) {
		this.idneg_ingredientId = idneg_ingredientId;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getStockDate() {
		return stockDate;
	}
	public void setStockDate(String stockDate) {
		this.stockDate = stockDate;
	}
	
	
	

}
