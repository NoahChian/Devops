package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.rest.bo.CustomerQuerySFBO;

public class CustomerQuerySFResponse extends AbstractApiResponse {

	private Integer totalNum;
	private Integer sid;
	private List<CustomerQuerySFBO> productList = new ArrayList<CustomerQuerySFBO>();

	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public List<CustomerQuerySFBO> getProductList() {
		return productList;
	}
	public void setProductList(List<CustomerQuerySFBO> productList) {
		this.productList = productList;
	}
}
