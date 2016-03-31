package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;


public class UpdateIngredientPropertyRequest {
	private String menuDate;
	private Long ingredientId;
	private String supplierCompanyId;
	private String stockDate;
	private String lotNumber;
	private IngredientAttributeBO ingredientProperty;
	

	public String getMenuDate() {
		return menuDate;
	}

	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}

	public String getStockDate() {
		return stockDate;
	}

	public void setStockDate(String stockDate) {
		this.stockDate = stockDate;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public IngredientAttributeBO getIngredientProperty() {
		return ingredientProperty;
	}

	public void setIngredientProperty(IngredientAttributeBO ingredientProperty) {
		this.ingredientProperty = ingredientProperty;
	}

	public Long getIngredientId() {
		return ingredientId;
	}

	public void setIngredientId(Long ingredientId) {
		this.ingredientId = ingredientId;
	}

	public String getSupplierCompanyId() {
		return supplierCompanyId;
	}

	public void setSupplierCompanyId(String supplierCompanyId) {
		this.supplierCompanyId = supplierCompanyId;
	}

}
