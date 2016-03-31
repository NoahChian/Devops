package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;

public class IngredientObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1686095985114805250L;
	private String name = "";
	private String supplyName = "";
	private String supplierCompanyId = "";
	private List<String> supplyNameOption = new ArrayList<String>();
	private List<Map<String, String>> supplierOption = new ArrayList<Map<String, String>>();
	private String brand = "";
	private String label = "";
	private List<String> labelOption = new ArrayList<String>();
	private String authenticateId = "";
	private String stockDate = "";
	private String productDate = "";
	private String validDate = "";
	private String ingredientBatchId = "";
	private String ingredientId = "";
	private String ingredientLotNum = "";
	private String isReport = "";
	private String reportFileName = "";
	private String productName = "";
	private String manufacturer = "";
	private String ingredientQuantity = "";
	private String ingredientUnit = "公斤";
	private IngredientAttributeBO ingredientAttribute = new IngredientAttributeBO();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	public List<String> getSupplyNameOption() {
		return supplyNameOption;
	}

	public void setSupplyNameOption(List<String> supplyNameOption) {
		this.supplyNameOption = supplyNameOption;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<String> getLabelOption() {
		return labelOption;
	}

	public void setLabelOption(List<String> labelOption) {
		this.labelOption = labelOption;
	}

	public String getAuthenticateId() {
		return authenticateId;
	}

	public void setAuthenticateId(String authenticatedId) {
		this.authenticateId = authenticatedId;
	}

	public String getStockDate() {
		return stockDate;
	}

	public void setStockDate(String stockDate) {
		this.stockDate = stockDate;
	}

	public String getProductDate() {
		return productDate;
	}

	public void setProductDate(String productDate) {
		this.productDate = productDate;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	public String getIngredientBatchId() {
		return ingredientBatchId;
	}

	public void setIngredientBatchId(String ingredientBatchId) {
		this.ingredientBatchId = ingredientBatchId;
	}

	public String getIsReport() {
		return isReport;
	}

	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}

	public String getIngredientLotNum() {
		return ingredientLotNum;
	}

	public void setIngredientLotNum(String ingredientLotNum) {
		this.ingredientLotNum = ingredientLotNum;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getIngredientQuantity() {
		return ingredientQuantity;
	}

	public void setIngredientQuantity(String ingredientQuantity) {
		this.ingredientQuantity = ingredientQuantity;
	}

	public String getIngredientUnit() {
		return ingredientUnit;
	}

	public void setIngredientUnit(String ingredientUnit) {
		this.ingredientUnit = ingredientUnit;
	}

	public IngredientAttributeBO getIngredientAttribute() {
		return ingredientAttribute;
	}

	public void setIngredientAttribute(IngredientAttributeBO ingredientAttribute) {
		this.ingredientAttribute = ingredientAttribute;
	}

	public List<Map<String, String>> getSupplierOption() {
		return supplierOption;
	}

	public void setSupplierOption(List<Map<String, String>> supplierOption) {
		this.supplierOption = supplierOption;
	}

	public String getIngredientId() {
		return ingredientId;
	}

	public void setIngredientId(String ingredientId) {
		this.ingredientId = ingredientId;
	}

	public String getSupplierCompanyId() {
		return supplierCompanyId;
	}

	public void setSupplierCompanyId(String supplierCompanyId) {
		this.supplierCompanyId = supplierCompanyId;
	}

}
