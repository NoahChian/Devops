package org.iii.ideas.catering_service.dao;

public class FdaCompanyReglist extends AbstractFdaCompanyReglist implements java.io.Serializable {
	
	public FdaCompanyReglist() {
	}

	public FdaCompanyReglist(Integer id, String company_Name, String businessId, String address, String fdaCompanyId, String regType) {
		super(id, company_Name, businessId, address, fdaCompanyId, regType);
	}
}