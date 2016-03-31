package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;



public class UpdateSeasoningRequest {
	private String activeType;
	private Long seasoningstockId;
	private Long dishId;
	private Long kitchenId;
	private Long ingredientId;
	private String ingredientName;
	private String stockDate;
	private String manufactureDate;
	private String expirationDate;
	private String lotNumber;
	private Integer supplierId;
	private String sourceCertification;
	private String certificationId;
	private Integer menutype;
	private String supplierCompanyId;
	private String supplierName;
	private String productName;
	private String manufacturer;
	private String ingredientQuantity;
	private String ingredientUnit;
	private IngredientAttributeBO ingredientAttr;
	private String usestartdate;
	private String useenddate;
	
	public String getActiveType() {
		return activeType;
	}
	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}
	public Long getSeasoningstockId() {
		return seasoningstockId;
	}
	public void setSeasoningstockId(Long seasoningstockId) {
		this.seasoningstockId = seasoningstockId;
	}
	public Long getDishId() {
		return dishId;
	}
	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}
	public Long getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(Long kitchenId) {
		kitchenId = kitchenId;
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
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
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
	public Integer getMenutype() {
		return menutype;
	}
	public void setMenutype(Integer menutype) {
		this.menutype = menutype;
	}
	public String getSupplierCompanyId() {
		return supplierCompanyId;
	}
	public void setSupplierCompanyId(String supplierCompanyId) {
		this.supplierCompanyId = supplierCompanyId;
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
	public IngredientAttributeBO getIngredientAttr() {
		return ingredientAttr;
	}
	public void setIngredientAttr(IngredientAttributeBO ingredientAttr) {
		this.ingredientAttr = ingredientAttr;
	}
	public String getUsestartdate() {
		return usestartdate;
	}
	public void setUsestartdate(String usestartdate) {
		this.usestartdate = usestartdate;
	}
	public String getUseenddate() {
		return useenddate;
	}
	public void setUseenddate(String useenddate) {
		this.useenddate = useenddate;
	}
}
