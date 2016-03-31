package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractViewDishAndIngredient entity provides the base persistence definition
 * of the ViewDishAndIngredient entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractViewDishAndIngredient2 implements
		java.io.Serializable {

	// Fields
	private String id;
	private String menudate;
	private Integer schoolid;
	private String schoolname;
	private String schoolcode;
	private Integer kitchenid;
	private String kitchenname;
	private Integer countyid;
	private Long dishbatchdataid;
	private String dishname;
	private String dishtype;
	private Timestamp updatedatetime;
	private String dishshowname;
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
	private String manufacturer;
	private String productName;
	private String ingredientQuantity;
	private Integer ingredientAttr;

	// Constructors

	/** default constructor */
	public AbstractViewDishAndIngredient2() {
	}

	/** minimal constructor */
//	public AbstractViewDishAndIngredient2(Long dishbatchdataid,
//			String dishname, String dishtype, Timestamp updatedatetime) {
//		this.dishbatchdataid = dishbatchdataid;
//		this.dishname = dishname;
//		this.dishtype = dishtype;
//		this.updatedatetime = updatedatetime;
//	}

	/** full constructor */
//	public AbstractViewDishAndIngredient2(String menudate, Integer schoolid,
//			String schoolname, Integer kitchenid, String kitchenname,
//			Integer countyid, Long dishbatchdataid, String dishname,
//			String dishtype, Timestamp updatedatetime, String dishshowname,
//			Long ingredientBatchId, Long batchDataId, Long dishId,
//			Long ingredientId, String ingredientName, Timestamp stockDate,
//			Timestamp manufactureDate, Timestamp expirationDate,
//			String lotNumber, String brand, String origin, String source,
//			Integer supplierId, String sourceCertification,
//			String certificationId, Integer menuId, String supplierCompanyId,
//			String supplierName, String brandNo,String manufacturer,
//			String productName,String ingredientQuantity,Integer ingredientAttr) {
//		this.menudate = menudate;
//		this.schoolid = schoolid;
//		this.schoolname = schoolname;
//		this.kitchenid = kitchenid;
//		this.kitchenname = kitchenname;
//		this.countyid = countyid;
//		this.dishbatchdataid = dishbatchdataid;
//		this.dishname = dishname;
//		this.dishtype = dishtype;
//		this.updatedatetime = updatedatetime;
//		this.dishshowname = dishshowname;
//		this.ingredientBatchId = ingredientBatchId;
//		this.batchDataId = batchDataId;
//		this.dishId = dishId;
//		this.ingredientId = ingredientId;
//		this.ingredientName = ingredientName;
//		this.stockDate = stockDate;
//		this.manufactureDate = manufactureDate;
//		this.expirationDate = expirationDate;
//		this.lotNumber = lotNumber;
//		this.brand = brand;
//		this.origin = origin;
//		this.source = source;
//		this.supplierId = supplierId;
//		this.sourceCertification = sourceCertification;
//		this.certificationId = certificationId;
//		this.menuId = menuId;
//		this.supplierCompanyId = supplierCompanyId;
//		this.supplierName = supplierName;
//		this.brandNo = brandNo;
//		this.manufacturer = manufacturer;
//		this.productName = productName;
//		this.ingredientQuantity = ingredientQuantity;
//		this.ingredientAttr = ingredientAttr;
//	}

	// Property accessors

	public String getMenudate() {
		return this.menudate;
	}

	public void setMenudate(String menudate) {
		this.menudate = menudate;
	}

	public Integer getSchoolid() {
		return this.schoolid;
	}

	public void setSchoolid(Integer schoolid) {
		this.schoolid = schoolid;
	}

	public String getSchoolname() {
		return this.schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public String getSchoolcode() {
		return schoolcode;
	}

	public void setSchoolcode(String schoolcode) {
		this.schoolcode = schoolcode;
	}

	public Integer getKitchenid() {
		return this.kitchenid;
	}

	public void setKitchenid(Integer kitchenid) {
		this.kitchenid = kitchenid;
	}

	public String getKitchenname() {
		return this.kitchenname;
	}

	public void setKitchenname(String kitchenname) {
		this.kitchenname = kitchenname;
	}

	public Integer getCountyid() {
		return this.countyid;
	}

	public void setCountyid(Integer countyid) {
		this.countyid = countyid;
	}

	public Long getDishbatchdataid() {
		return this.dishbatchdataid;
	}

	public void setDishbatchdataid(Long dishbatchdataid) {
		this.dishbatchdataid = dishbatchdataid;
	}

	public String getDishname() {
		return this.dishname;
	}

	public void setDishname(String dishname) {
		this.dishname = dishname;
	}

	public String getDishtype() {
		return this.dishtype;
	}

	public void setDishtype(String dishtype) {
		this.dishtype = dishtype;
	}

	public Timestamp getUpdatedatetime() {
		return this.updatedatetime;
	}

	public void setUpdatedatetime(Timestamp updatedatetime) {
		this.updatedatetime = updatedatetime;
	}

	public String getDishshowname() {
		return this.dishshowname;
	}

	public void setDishshowname(String dishshowname) {
		this.dishshowname = dishshowname;
	}

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
		return this.supplierCompanyId;
	}

	public void setSupplierCompanyId(String supplierCompanyId) {
		this.supplierCompanyId = supplierCompanyId;
	}

	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getBrandNo() {
		return this.brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getIngredientQuantity() {
		return ingredientQuantity;
	}

	public void setIngredientQuantity(String ingredientQuantity) {
		this.ingredientQuantity = ingredientQuantity;
	}

	public Integer getIngredientAttr() {
		return ingredientAttr;
	}

	public void setIngredientAttr(Integer ingredientAttr) {
		this.ingredientAttr = ingredientAttr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}