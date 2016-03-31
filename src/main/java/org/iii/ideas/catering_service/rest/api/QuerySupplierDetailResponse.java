package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.SupplierBO;

public class QuerySupplierDetailResponse extends AbstractApiResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1671888537921894418L;
	private SupplierBO supplierBo;
	
	public SupplierBO getSupplierBo() {
		return supplierBo;
	}
	public void setSupplierBo(SupplierBO supplierBo) {
		this.supplierBo = supplierBo;
	}

}
