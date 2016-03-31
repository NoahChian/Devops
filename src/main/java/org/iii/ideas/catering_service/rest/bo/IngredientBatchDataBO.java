package org.iii.ideas.catering_service.rest.bo;


public class IngredientBatchDataBO {
	private Long batchdataId;
	private String menuDate;
	private Integer schoolId;
	private String schoolName;
	private Long dishId;
	private String dishName;
	private Long ingredientBatchdataId;
	private Long ingredientId;
	private String ingredientName;
	private String stockDate;
	private String manufactureDate;
	private String expirationDate;
	private String lotNumber;
	private String brand;
	private Integer supplierId;
	private String companyId;
	private String sourceCertification;
	private String certificationId;
	private Integer kitchenId;
	private String origin;
	private String source;
	private String supplierName;
	private String productName;
	private String manufacturer;
	private String ingredientQuantity = "0";
	private String ingredientUnit;
	private IngredientAttributeBO ingredientAttribute = new IngredientAttributeBO();
	
	public Long getBatchdataId() {
		return batchdataId;
	}
	public void setBatchdataId(Long batchdataId) {
		this.batchdataId = batchdataId;
	}
	public String getMenuDate() {
		return menuDate;
	}
	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public Long getDishId() {
		return dishId;
	}
	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}
	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	public Long getIngredientBatchdataId() {
		return ingredientBatchdataId;
	}
	public void setIngredientBatchdataId(Long ingredientBatchdataId) {
		this.ingredientBatchdataId = ingredientBatchdataId;
	}
	public Long getIngredientId() {
		return ingredientId;
	}
	public void setIngredientId(Long ingredientId) {
		this.ingredientId = ingredientId;
	}
	public String getIngredientName() {
		return ingredientName;
	}
	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
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
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getSourceCertification() {
		return sourceCertification;
	}
	public void setSourceCertification(String sourceCertification) {
		this.sourceCertification = sourceCertification;
	}
	public String getCertificationId() {
		return certificationId;
	}
	public void setCertificationId(String certificationId) {
		this.certificationId = certificationId;
	}
	public Integer getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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
	
	

}
