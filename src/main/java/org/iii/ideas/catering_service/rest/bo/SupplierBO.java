package org.iii.ideas.catering_service.rest.bo;

public class SupplierBO {

	private Integer supplierId;
	private String companyId;
	private String supplierName;
	private String ownner;
	private Integer kitchenId;
	private Integer countyId;
	private Integer areaId;
	private String address;
	private String tel;
	private String certification;
	private String suppliedType;
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getOwnner() {
		return ownner;
	}

	public void setOwnner(String ownner) {
		this.ownner = ownner;
	}

	public Integer getKitchenId() {
		return kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public Integer getCountyId() {
		return countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSuppliedType() {
		return suppliedType;
	}

	public void setSuppliedType(String suppliedType) {
		this.suppliedType = suppliedType;
	}

}
