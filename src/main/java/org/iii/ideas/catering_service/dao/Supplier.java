package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * Supplier entity. @author MyEclipse Persistence Tools
 */
public class Supplier extends AbstractSupplier implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Supplier() {
	}

	/** full constructor */

	public Supplier(SupplierId id, String supplierName, String ownner, String companyId, Integer countyId,
			Integer areaId, String supplierAdress, String supplierTel, String supplierCertification,
			String fdaCompanyId, Timestamp fdaUpdateTime, String fdaInfoUpdater) {
		super(id, supplierName, ownner, companyId, countyId, areaId, supplierAdress, supplierTel, supplierCertification,
				fdaCompanyId, fdaUpdateTime, fdaInfoUpdater);
		// TODO Auto-generated constructor stub
	}

}
