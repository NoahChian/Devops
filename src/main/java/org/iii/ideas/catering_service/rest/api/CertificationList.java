package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;

class CertificationList  implements Serializable {

	private Long ingredientId;//食材編號
	private String menuDate;//菜單日期
	private String kitchenName;//團膳業者/自立午餐學校名稱
	private int kitchenId;//團膳業者/自立午餐學校名稱
	private String supplyName;//供應商名稱
	private String ingredientName;//食材名稱
	private Long ingredientBatchId;//食材批號
	private String expirationDate;//食材有效日期
	private String stockDate;//食材進貨日期
	private String manufactureDate;//食材生產日期
	private String certificationId;//檢驗報告
	private String fileExist;//檢驗報告是否存在
	private String fileName;//檢驗報告檔名
	private IngredientAttributeBO ingredientAttribute;
	
	private String lotNumber;

	public Long getIngredientId() {
		return ingredientId;
	}
	public void setIngredientId(Long ingredientId) {
		this.ingredientId = ingredientId;
	}

	public Long getIngredientBatchId() {
		return ingredientBatchId;
	}
	public void setIngredientBatchId(Long ingredientBatchId) {
		this.ingredientBatchId = ingredientBatchId;
	}
    
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
    
	public String getMenuDate() {
		return menuDate;
	}
	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}

	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public int getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(int kitchenId) {
		this.kitchenId = kitchenId;
	}
    
	public String getCertificationId() {
		return certificationId;
	}
	public void setCertificationId(String certificationId) {
		this.certificationId = certificationId;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getFileExist() {
		return fileExist;
	}
	public void setFileExist(String fileExist) {
		this.fileExist = fileExist;
	}
	public IngredientAttributeBO getIngredientAttribute() {
		return ingredientAttribute;
	}
	public void setIngredientAttribute(IngredientAttributeBO ingredientAttribute) {
		this.ingredientAttribute = ingredientAttribute;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}