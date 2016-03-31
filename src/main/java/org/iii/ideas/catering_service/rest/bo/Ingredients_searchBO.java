package org.iii.ideas.catering_service.rest.bo;

import java.io.Serializable;

public class Ingredients_searchBO   implements Serializable {
	private String county; // 縣市名
	private String school_name; // 學校名稱
	private String menu_date; // 供餐日期
	private String kitchen_name; // 供餐業者
	private String kitchen_company; // 供餐業者統一編號
	private String ingredient_supplier; // 食材供應商名稱
	private String ingredient_name; // 食材名稱
	private String seasoning_supplier; // 調味料供應商名稱
	private String seasoning_name; // 調味料名稱

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getMenu_date() {
		return menu_date;
	}

	public void setMenu_date(String menu_date) {
		this.menu_date = menu_date;
	}

	public String getKitchen_name() {
		return kitchen_name;
	}

	public void setKitchen_name(String kitchen_name) {
		this.kitchen_name = kitchen_name;
	}

	public String getKitchen_company() {
		return kitchen_company;
	}

	public void setKitchen_company(String kitchen_company) {
		this.kitchen_company = kitchen_company;
	}

	public String getIngredient_supplier() {
		return ingredient_supplier;
	}

	public void setIngredient_supplier(String ingredient_supplier) {
		this.ingredient_supplier = ingredient_supplier;
	}

	public String getIngredient_name() {
		return ingredient_name;
	}

	public void setIngredient_name(String ingredient_name) {
		this.ingredient_name = ingredient_name;
	}

	public String getSeasoning_supplier() {
		return seasoning_supplier;
	}

	public void setSeasoning_supplier(String seasoning_supplier) {
		this.seasoning_supplier = seasoning_supplier;
	}

	public String getSeasoning_name() {
		return seasoning_name;
	}

	public void setSeasoning_name(String seasoning_name) {
		this.seasoning_name = seasoning_name;
	}

}