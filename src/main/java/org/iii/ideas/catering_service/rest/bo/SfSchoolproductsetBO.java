package org.iii.ideas.catering_service.rest.bo;

public class SfSchoolproductsetBO {
	private Long id;
	private Integer schoolId;
	private Long productId;
	private String productName;
	private String onShelfDate;
	private String offShelfDate;
	private String status;
	private String createUser;
	private String createDateTime;
	private String modifyUser;
	private String modifyDateTime;
	private String suppliercompanyName;
	private String manufacturerName;
	private String packageType;
	private String certification;
	private String certificationId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOnShelfDate() {
		return onShelfDate;
	}

	public void setOnShelfDate(String onShelfDate) {
		this.onShelfDate = onShelfDate;
	}

	public String getOffShelfDate() {
		return offShelfDate;
	}

	public void setOffShelfDate(String offShelfDate) {
		this.offShelfDate = offShelfDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getModifyDateTime() {
		return modifyDateTime;
	}

	public void setModifyDateTime(String modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}

	public String getSuppliercompanyName() {
		return suppliercompanyName;
	}

	public void setSuppliercompanyName(String suppliercompanyName) {
		this.suppliercompanyName = suppliercompanyName;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public String getCertificationId() {
		return certificationId;
	}

	public void setCertificationId(String certificationId) {
		this.certificationId = certificationId;
	}

}
