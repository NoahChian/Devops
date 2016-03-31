package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;


public abstract class AbstractSeasoningStockData implements
		java.io.Serializable {

	/**
	 * 
	 */
	//private static final long serialVersionUID = 415507301224848138L; 用途未知
	// Fields
	private Long seasoningStockId;
	private Integer kitchenId;
	private Integer restaurantId;
	private String SeasoningName;
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
	private Integer menuType;
	private String supplierCompanyId;
	private String supplierName;
	private String brandNo;
	private String productName;
	private String manufacturer;
	private String ingredientQuantity;
	private String ingredientUnit;
	private Integer ingredientAttr;
	private Timestamp useStartDate;
	private Timestamp useeEndDate;
	private String lastUpdateId;
	private Timestamp lastUpdateDate;
	private Integer Enable;
	
	
	// Constructors

	/** default constructor */
	public AbstractSeasoningStockData() {
	}


	public AbstractSeasoningStockData(Long seasoningStockId, Integer kitchenId,
			String SeasoningName,
			Timestamp stockDate, Timestamp manufactureDate,
			Timestamp expirationDate, String lotNumber, String brand,
			String origin, String source, Integer supplierId,
			String sourceCertification, String certificationId,
			Integer menuType, String supplierCompanyId, String supplierName,
			String brandNo, String productName, String manufacturer,
			String ingredientQuantity, String ingredientUnit,
			Integer ingredientAttr, Timestamp useStartDate,
			Timestamp useeEndDate, String lastUpdateId,
			Timestamp lastUpdateDate, Integer enable) {
		super();
		this.seasoningStockId = seasoningStockId;
		this.kitchenId = kitchenId;
		this.SeasoningName = SeasoningName;
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
		this.menuType = menuType;
		this.supplierCompanyId = supplierCompanyId;
		this.supplierName = supplierName;
		this.brandNo = brandNo;
		this.productName = productName;
		this.manufacturer = manufacturer;
		this.ingredientQuantity = ingredientQuantity;
		this.ingredientUnit = ingredientUnit;
		this.ingredientAttr = ingredientAttr;
		this.useStartDate = useStartDate;
		this.useeEndDate = useeEndDate;
		this.lastUpdateId = lastUpdateId;
		this.lastUpdateDate = lastUpdateDate;
		Enable = enable;
	}


	public Long getSeasoningStockId() {
		return seasoningStockId;
	}


	public void setSeasoningStockId(Long seasoningStockId) {
		this.seasoningStockId = seasoningStockId;
	}


	public Integer getKitchenId() {
		return kitchenId;
	}


	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}


	public Integer getRestaurantId() {
		return restaurantId;
	}


	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}


	public String getSeasoningName() {
		return SeasoningName;
	}


	public void setSeasoningName(String seasoningName) {
		SeasoningName = seasoningName;
	}


	public Timestamp getStockDate() {
		return stockDate;
	}


	public void setStockDate(Timestamp stockDate) {
		this.stockDate = stockDate;
	}


	public Timestamp getManufactureDate() {
		return manufactureDate;
	}


	public void setManufactureDate(Timestamp manufactureDate) {
		this.manufactureDate = manufactureDate;
	}


	public Timestamp getExpirationDate() {
		return expirationDate;
	}


	public void setExpirationDate(Timestamp expirationDate) {
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


	public Integer getMenuType() {
		return menuType;
	}


	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
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


	public String getBrandNo() {
		return brandNo;
	}


	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
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


	public Integer getIngredientAttr() {
		return ingredientAttr;
	}


	public void setIngredientAttr(Integer ingredientAttr) {
		this.ingredientAttr = ingredientAttr;
	}


	public Timestamp getUseStartDate() {
		return useStartDate;
	}


	public void setUseStartDate(Timestamp useStartDate) {
		this.useStartDate = useStartDate;
	}


	public Timestamp getUseeEndDate() {
		return useeEndDate;
	}


	public void setUseeEndDate(Timestamp useeEndDate) {
		this.useeEndDate = useeEndDate;
	}


	public String getLastUpdateId() {
		return lastUpdateId;
	}


	public void setLastUpdateId(String lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}


	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}


	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	public Integer getEnable() {
		return Enable;
	}


	public void setEnable(Integer enable) {
		Enable = enable;
	}
	

}