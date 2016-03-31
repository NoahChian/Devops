package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.SupplierBO;

public class UpdateSupplierDetailResponse extends AbstractApiResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6465269258755721397L;
	private SupplierBO supplierBo;
	public SupplierBO getSupplierBo() {
		return supplierBo;
	}
	public void setSupplierBo(SupplierBO supplierBo) {
		this.supplierBo = supplierBo;
	}
}
