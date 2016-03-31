package org.iii.ideas.catering_service.dao;

/**
 * Area entity. @author MyEclipse Persistence Tools
 */
public class SfCustomerServiceInfo extends AbstractSfCustomerServiceInfo implements java.io.Serializable {

	// Constructors
	/** default constructor */
	public SfCustomerServiceInfo() {
	}

	/** full constructor */
	public SfCustomerServiceInfo(long supplierCompanyId, long manufacturerId,
			String hotline, String owner, String contactStaff,
			String contactTel, String contactFax) {
		super(supplierCompanyId, manufacturerId, hotline, owner, 
				contactStaff, contactTel, contactFax);
	}
}
