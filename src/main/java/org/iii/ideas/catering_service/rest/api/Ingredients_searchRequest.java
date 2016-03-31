package org.iii.ideas.catering_service.rest.api;

public class Ingredients_searchRequest {
	private String menuStartDate; // 供餐起始日期
	private String menuEndDate; // 供餐結束日期
	private Integer countyId; // 學校縣市代碼
	private String schoolName; // 學校名稱
	private String kitchenCompanyId; // 供餐業者統一編號
	private String kitchenName; // 供餐業者名稱
	private String igredientName; // 食材名稱
	private String igredientSupplier; // 食材供應商名稱
	private String seasoningName; // 調味料名稱
	private String seasoningSupplier; // 調味料供應商名稱
	private Integer limit; // 筆數限制

	public String getMenuStartDate() {
		return menuStartDate;
	}

	public void setMenuStartDate(String menuStartDate) {
		this.menuStartDate = menuStartDate;
	}

	public String getMenuEndDate() {
		return menuEndDate;
	}

	public void setMenuEndDate(String menuEndDate) {
		this.menuEndDate = menuEndDate;
	}

	public Integer getCountyId() {
		return countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getKitchenCompanyId() {
		return kitchenCompanyId;
	}

	public void setKitchenCompanyId(String kitchenCompanyId) {
		this.kitchenCompanyId = kitchenCompanyId;
	}

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public String getIgredientName() {
		return igredientName;
	}

	public void setIgredientName(String igredientName) {
		this.igredientName = igredientName;
	}

	public String getIgredientSupplier() {
		return igredientSupplier;
	}

	public void setIgredientSupplier(String igredientSupplier) {
		this.igredientSupplier = igredientSupplier;
	}

	public String getSeasoningName() {
		return seasoningName;
	}

	public void setSeasoningName(String seasoningName) {
		this.seasoningName = seasoningName;
	}

	public String getSeasoningSupplier() {
		return seasoningSupplier;
	}

	public void setSeasoningSupplier(String seasoningSupplier) {
		this.seasoningSupplier = seasoningSupplier;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
