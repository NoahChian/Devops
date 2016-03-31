package org.iii.ideas.catering_service.rest.bo;

public class IngredientPropertyBO {
	
	private Long ingredientId;
	private String ingredientName;
	private Integer supplierId;
	private String supplierName;
	private String supplierCompanyId;
	private String stockDate;
	private String lotNumber;
	private String dishName;
	private FileBO inspectionFile;
	private IngredientAttributeBO ingredientAttribute;




	public String getIngredientName() {
		return ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierCompanyId() {
		return supplierCompanyId;
	}

	public void setSupplierCompanyId(String supplierCompanyId) {
		this.supplierCompanyId = supplierCompanyId;
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

	public FileBO getInspectionFile() {
		return inspectionFile;
	}

	public void setInspectionFile(FileBO inspectionFile) {
		this.inspectionFile = inspectionFile;
	}

	public IngredientAttributeBO getIngredientAttribute() {
		return ingredientAttribute;
	}

	public void setIngredientAttribute(IngredientAttributeBO ingredientAttribute) {
		this.ingredientAttribute = ingredientAttribute;
	}

	public Long getIngredientId() {
		return ingredientId;
	}

	public void setIngredientId(Long ingredientId) {
		this.ingredientId = ingredientId;
	}

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}


	

	
}
