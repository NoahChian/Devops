package org.iii.ideas.catering_service.dao;

public abstract class AbstractFdaCompanyReglist implements java.io.Serializable {
	private Integer id;
	private String company_Name;
	private String businessId;
	private String address;
	private String fdaCompanyId;
	private String regType;
	
	public AbstractFdaCompanyReglist(){
	}
	
	public AbstractFdaCompanyReglist(Integer id, String company_Name, String businessId, String address, String fdaCompanyId, String regType){
		this.id = id;
		this.company_Name = company_Name;
		this.businessId = businessId;
		this.address = address;
		this.fdaCompanyId = fdaCompanyId;
		this.regType = regType;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCompany_Name() {
		return this.company_Name;
	}

	public void setCompany_Name(String company_Name) {
		this.company_Name = company_Name;
	}
	
	
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getFdaCompanyId() {
		return this.fdaCompanyId;
	}

	public void setFdaCompanyId(String fdaCompanyId) {
		this.fdaCompanyId = fdaCompanyId;
	}
	
	public String getRegType() {
		return this.regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}
}