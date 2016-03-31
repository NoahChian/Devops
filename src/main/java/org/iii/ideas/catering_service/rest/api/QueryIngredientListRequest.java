package org.iii.ideas.catering_service.rest.api;

public class QueryIngredientListRequest {
	private String begDate;
	private String endDate;
	private Integer kitchenId;
	private Integer schoolId;
	private Integer countyId;
	private Integer queryLimit;
	private String dishname;
	private String ingredientName;
	private String brand;
	private String supplierName;
	private String manufacturer;
	private int page;
	private int perpage;
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBegDate() {
		return begDate;
	}
	public void setBegDate(String begDate) {
		this.begDate = begDate;
	}
	public Integer getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getCountyId() {
		return countyId;
	}
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}
	public Integer getQueryLimit() {
		return queryLimit;
	}
	public void setQueryLimit(Integer queryLimit) {
		this.queryLimit = queryLimit;
	}
	public String getDishname() {
		return dishname;
	}
	public void setDishname(String dishname) {
		this.dishname = dishname;
	}
	public String getIngredientName() {
		return ingredientName;
	}
	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPerpage() {
		return perpage;
	}
	public void setPerpage(int perpage) {
		this.perpage = perpage;
	}
}
