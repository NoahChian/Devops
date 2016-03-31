package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class CustomerQueryMenuDetailInfoResponse  extends AbstractApiResponse  {
	private int sid;
	private String schoolName;
	private String date;
	private String nowdate;
	private String userName;
	private int quantity;//供餐份數
	private CustomerQueryMenuDetailInfoNutrition nutrition = new CustomerQueryMenuDetailInfoNutrition();
	private CustomerQueryMenuDetailInfoSupplierInfo supplierInfo = new CustomerQueryMenuDetailInfoSupplierInfo();
	private List<CustomerQueryMenuDetailInfoLunchContent> lunchContent = new ArrayList<CustomerQueryMenuDetailInfoLunchContent>();
	private List<CustomerQueryMenuDetailInfoFoodInfo> foodInfo = new ArrayList<CustomerQueryMenuDetailInfoFoodInfo>();
	private List<CustomerQueryMenuDetailInfoSeasoning> seasoning = new ArrayList<CustomerQueryMenuDetailInfoSeasoning>();
	private String midBefore;
	private String midAfter;
	private boolean showMode = false;
	
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
	public String getMidBefore() {
		return midBefore;
	}
	public void setMidBefore(String midBefore) {
		this.midBefore = midBefore;
	}
	public String getMidAfter() {
		return midAfter;
	}
	public void setMidAfter(String midAfter) {
		this.midAfter = midAfter;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getNowdate() {
		return nowdate;
	}
	public void setNowdate(String nowdate) {
		this.nowdate = nowdate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isShowMode() {
		return showMode;
	}
	public void setShowMode(boolean showMode) {
		this.showMode = showMode;
	}
	
	//CustomerQueryMenuDetailInfoNutrition
	//CustomerQueryMenuDetailInfoSupplierInfo
	//CustomerQueryMenuDetailInfoLunchContent
	//CustomerQueryMenuDetailInfoFoodInfo
	//CustomerQueryMenuDetailInfoSeasoning
}
