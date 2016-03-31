package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerQueryMenuDetailInfoRequest  implements Serializable {
	private Long mid;
	private CustomerQueryMenuDetailInfoNutrition nutrition;
	private CustomerQueryMenuDetailInfoSupplierInfo supplierInfo;
	private List<CustomerQueryMenuDetailInfoLunchContent> lunchContent = new ArrayList<CustomerQueryMenuDetailInfoLunchContent>();
	private List<CustomerQueryMenuDetailInfoFoodInfo> foodInfo = new ArrayList<CustomerQueryMenuDetailInfoFoodInfo>();
	private List<CustomerQueryMenuDetailInfoSeasoning> seasoning = new ArrayList<CustomerQueryMenuDetailInfoSeasoning>();
	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public CustomerQueryMenuDetailInfoNutrition getNutrition() {
		return nutrition;
	}

	public void setNutrition(CustomerQueryMenuDetailInfoNutrition nutrition) {
		this.nutrition = nutrition;
	}

	public CustomerQueryMenuDetailInfoSupplierInfo getSupplierInfo() {
		return supplierInfo;
	}

	public void setSupplierInfo(CustomerQueryMenuDetailInfoSupplierInfo supplierInfo) {
		this.supplierInfo = supplierInfo;
	}

	public List<CustomerQueryMenuDetailInfoLunchContent> getLunchContent() {
		return lunchContent;
	}

	public void setLunchContent(List<CustomerQueryMenuDetailInfoLunchContent> lunchContent) {
		this.lunchContent = lunchContent;
	}

	public List<CustomerQueryMenuDetailInfoFoodInfo> getFoodInfo() {
		return foodInfo;
	}

	public void setFoodInfo(List<CustomerQueryMenuDetailInfoFoodInfo> foodInfo) {
		this.foodInfo = foodInfo;
	}

	public List<CustomerQueryMenuDetailInfoSeasoning> getSeasoning() {
		return seasoning;
	}

	public void setSeasoning(List<CustomerQueryMenuDetailInfoSeasoning> seasoning) {
		this.seasoning = seasoning;
	}
}
