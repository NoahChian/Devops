package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

class NegList  implements Serializable {

	private int idneg_ingredientId;//異常狀況編號
	private String supplyName;//供應商名稱
	private int supplierId;//供應商編號
	private String ingredientName;//食材名稱
	private int ingredientId;//食材編號
//	private int ingredientBatchId;//食材批號
	private String expirationDate;//食材有效日期
	private String stockDate;//食材進貨日期
	private String manufactureDate;//食材生產日期
	private String beg_date;//管制日期(起)
	private String end_date;//管制日期(訖)
	private String description;//異常說明
	private String lotNumber;


	public int getIdneg_ingredientId() {
		return idneg_ingredientId;
	}
	public void setIdneg_ingredientId(int idneg_ingredientId) {
		this.idneg_ingredientId = idneg_ingredientId;
	}

	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public int getIngredientId() {
		return ingredientId;
	}
	public void setIngredientId(int ingredientId) {
		this.ingredientId = ingredientId;
	}
/*
	public int getIngredientBatchId() {
		return ingredientBatchId;
	}
	public void setIngredientBatchId(int ingredientBatchId) {
		this.ingredientBatchId = ingredientBatchId;
	}
  */  
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
    
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
    
	public String getStockDate() {
		return stockDate;
	}
	public void setStockDate(String stockDate) {
		this.stockDate = stockDate;
	}
    
	public String getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
    
	public String getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(String beg_date) {
		this.beg_date = beg_date;
	}
    
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
    
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
}