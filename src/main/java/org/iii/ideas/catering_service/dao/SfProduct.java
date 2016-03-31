package org.iii.ideas.catering_service.dao;

/**
 * Area entity. @author MyEclipse Persistence Tools
 */
public class SfProduct extends AbstractSfProduct implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public SfProduct() {
	}

	/** full constructor */
	public SfProduct(String name, long supplierCompanyId, long manufacturerId,
			String preservedMethod, String soldway, String packages) {
		super(name, supplierCompanyId, manufacturerId, preservedMethod, soldway, packages);
	}
	public SfProduct(String name, long supplierCompanyId, long manufacturerId,
			String preservedMethod, String soldway, String packages,
			String certification, String certificationId,
			String appliedNoByJdf, String barCode) {
		super(name, supplierCompanyId, manufacturerId, preservedMethod, soldway, 
				packages, certification, certificationId, appliedNoByJdf, barCode);
	}
}
