package org.iii.ideas.catering_service.rest.bo;

import org.iii.ideas.catering_service.dao.SfCompany;
import org.iii.ideas.catering_service.dao.SfCustomerServiceInfo;
import org.iii.ideas.catering_service.dao.SfProduct;

public class CustomerQuerySFBO {

	private CustomerQuerySFBoProduct product;
	private CustomerQuerySFBoCompany company;
	private CustomerQuerySFBoFactory factory;
	private CustomerQuerySFBoCustService customerService;
	public CustomerQuerySFBoProduct getProduct() {
		return product;
	}
	public void setProduct(CustomerQuerySFBoProduct product) {
		this.product = product;
	}
	public CustomerQuerySFBoCompany getCompany() {
		return company;
	}
	public void setCompany(CustomerQuerySFBoCompany company) {
		this.company = company;
	}
	public CustomerQuerySFBoFactory getFactory() {
		return factory;
	}
	public void setFactory(CustomerQuerySFBoFactory factory) {
		this.factory = factory;
	}
	public CustomerQuerySFBoCustService getCustomerService() {
		return customerService;
	}
	public void setCustomerService(CustomerQuerySFBoCustService customerService) {
		this.customerService = customerService;
	}
	
}
