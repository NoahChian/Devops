package org.iii.ideas.catering_service.dao;

//Generated 2015/2/3 上午 09:25:59 by Hibernate Tools 3.4.0.CR1

/**
* SfCustomerserviceinfo generated by hbm2java
*/
public abstract class AbstractSfCustomerServiceInfo implements java.io.Serializable {

	private Long id;
	private long supplierCompanyId;
	private long manufacturerId;
	private String hotline;
	private String owner;
	private String contactStaff;
	private String contactTel;
	private String contactFax;

	public AbstractSfCustomerServiceInfo() {
	}

	public AbstractSfCustomerServiceInfo(long supplierCompanyId, long manufacturerId,
			String hotline, String owner, String contactStaff,
			String contactTel, String contactFax) {
		this.supplierCompanyId = supplierCompanyId;
		this.manufacturerId = manufacturerId;
		this.hotline = hotline;
		this.owner = owner;
		this.contactStaff = contactStaff;
		this.contactTel = contactTel;
		this.contactFax = contactFax;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getSupplierCompanyId() {
		return this.supplierCompanyId;
	}

	public void setSupplierCompanyId(long supplierCompanyId) {
		this.supplierCompanyId = supplierCompanyId;
	}

	public long getManufacturerId() {
		return this.manufacturerId;
	}

	public void setManufacturerId(long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getHotline() {
		return this.hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getContactStaff() {
		return this.contactStaff;
	}

	public void setContactStaff(String contactStaff) {
		this.contactStaff = contactStaff;
	}

	public String getContactTel() {
		return this.contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getContactFax() {
		return this.contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

}