package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.SupplierBO;


public class UpdateSupplierDetailRequest {
	private String activeType;
	private SupplierBO supplierBo;
	
	public String getActiveType() {
		return activeType;
	}
	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}
	public SupplierBO getSupplierBo() {
		return supplierBo;
	}
	public void setSupplierBo(SupplierBO supplierBo) {
		this.supplierBo = supplierBo;
	}

	
	
}
