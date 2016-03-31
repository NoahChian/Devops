package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.Supplier;


public class QuerySupplierListResponse  extends AbstractApiResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1671888537921894418L;
	private List<Supplier> supplierList = new ArrayList<Supplier>();
	private Integer totalNum;
	
	public List<Supplier> getSupplierList() {
		return supplierList;
	}
	public void setSupplierList(List<Supplier> supplierList) {
		this.supplierList = supplierList;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	
}
