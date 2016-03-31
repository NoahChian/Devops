package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractIngredientbatchdata entity provides the base persistence definition
 * of the Ingredientbatchdata entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractIngredientbatchdata implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 415507301224848138L;
	// Fields

	private Long ingredientBatchId;
	private Long batchDataId;
	private Long dishId;
	private Long ingredientId;
	private String ingredientName;
	private Timestamp stockDate;
	private Timestamp manufactureDate;
	private Timestamp expirationDate;
	private String lotNumber;
	private String brand;
	private String origin;
	private String source;
	private Integer supplierId;
	private String sourceCertification;
	private String certificationId;
	private Integer menuId;
	private String supplierCompanyId;
	private String supplierName;
	private String brandNo;
	private Integer ingredientAttr=0;
	private String productName;
	private String manufacturer;
	private String ingredientQuantity;
	private String ingredientUnit;
	
	
	// Constructors

	/** default constructor */
	public AbstractIngredientbatchdata() {
	}
	/** constructor for old code&schema */
	public AbstractIngredientbatchdata(Long batchDataId, Long dishId,
			Long ingredientId, String ingredientName, Timestamp stockDate,
			Timestamp manufactureDate, Timestamp expirationDate,
			String lotNumber, String brand, String origin, String source,
			Integer supplierId, String sourceCertification,
			String certificationId, Integer menuId,String supplierCompanyId) {
		this.batchDataId = batchDataId;
		this.dishId = dishId;
		this.ingredientId = ingredientId;
		this.ingredientName = ingredientName;
		this.stockDate = stockDate;
		this.manufactureDate = manufactureDate;
		this.expirationDate = expirationDate;
		this.lotNumber = lotNumber;
		this.brand = brand;
		this.origin = origin;
		this.source = source;
		this.supplierId = supplierId;
		this.sourceCertification = sourceCertification;
		this.certificationId = certificationId;
		this.menuId = menuId;
		this.supplierCompanyId = supplierCompanyId;
		
	}
	

	/**new full constructor  
	 * Add supplierName    20140306 KC  
	 * Add brandName       20140310 KC
	 * Add ingredientAttr  20140716 Raymond
	 * Add productName manufacturer ingredientQuantity ingredientUnit 20140722 Raymond
	 * */
	public AbstractIngredientbatchdata(Long batchDataId, Long dishId,
			Long ingredientId, String ingredientName, Timestamp stockDate,
			Timestamp manufactureDate, Timestamp expirationDate,
			String lotNumber, String brand, String origin, String source,
			Integer supplierId, String sourceCertification,
			String certificationId, Integer menuId,String supplierCompanyId,String supplierName,String brandNo,
			Integer ingredientAttr,String productName,String manufacturer ,String ingredientQuantity,String ingredientUnit) {
		this.batchDataId = batchDataId;
		this.dishId = dishId;
		this.ingredientId = ingredientId;
		this.ingredientName = ingredientName;
		this.stockDate = stockDate;
		this.manufactureDate = manufactureDate;
		this.expirationDate = expirationDate;
		this.lotNumber = lotNumber;
		this.brand = brand;
		this.origin = origin;
		this.source = source;
		this.supplierId = supplierId;
		this.sourceCertification = sourceCertification;
		this.certificationId = certificationId;
		this.menuId = menuId;
		this.supplierCompanyId = supplierCompanyId;
		this.supplierName=supplierName;
		this.brandNo=brandNo;
		this.ingredientAttr=ingredientAttr;
		this.productName=productName;
		this.manufacturer=manufacturer;
		this.ingredientQuantity=ingredientQuantity;
		this.ingredientUnit=ingredientUnit;
	}

	// Property accessors

	public Long getIngredientBatchId() {
		return this.ingredientBatchId;
	}

	public void setIngredientBatchId(Long ingredientBatchId) {
		this.ingredientBatchId = ingredientBatchId;
	}

	public Long getBatchDataId() {
		return this.batchDataId;
	}

	public void setBatchDataId(Long batchDataId) {
		this.batchDataId = batchDataId;
	}

	public Long getDishId() {
		return this.dishId;
	}

	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}

	public Long getIngredientId() {
		return this.ingredientId;
	}

	public void setIngredientId(Long ingredientId) {
		this.ingredientId = ingredientId;
	}

	public String getIngredientName() {
		return this.ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}

	public Timestamp getStockDate() {
		return this.stockDate;
	}

	public void setStockDate(Timestamp stockDate) {
		this.stockDate = stockDate;
	}

	public Timestamp getManufactureDate() {
		return this.manufactureDate;
	}

	public void setManufactureDate(Timestamp manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public Timestamp getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getLotNumber() {
		return this.lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSourceCertification() {
		return this.sourceCertification;
	}

	public void setSourceCertification(String sourceCertification) {
		this.sourceCertification = sourceCertification;
	}

	public String getCertificationId() {
		return this.certificationId;
	}

	public void setCertificationId(String certificationId) {
		this.certificationId = certificationId;
	}

	public Integer getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getSupplierCompanyId() {
		return supplierCompanyId;
	}

	public void setSupplierCompanyId(String supplierCompanyId) {
		this.supplierCompanyId = supplierCompanyId;
	}
	
	public String getSupplierName(){
		return this.supplierName;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	
	public String getBrandNo(){
		return this.brandNo;
	}
	public void setBrandNo(String brandNo){
		this.brandNo=brandNo;
		
	}
	public Integer getIngredientAttr() {
		return ingredientAttr;
	}
	public void setIngredientAttr(Integer ingredientAttr) {
		this.ingredientAttr = ingredientAttr;
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

}