package org.iii.ideas.catering_service.rest.bo;

import java.util.HashMap;

import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata2;

public class ViewSchoolMenuParameter2 {
	private HashMap<String, Object> menuParameterMap = new HashMap<String, Object>();

	// private String ingredientName;
	
	public void setMenudate(String menudate) {
		setHashmap(ViewSchoolMenuWithBatchdata2.MENU_DATE, menudate);
	}

	public String getMenudate() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.MENU_DATE);
	}
	
	public void setSchoolid(String schoolid) {
		setHashmap(ViewSchoolMenuWithBatchdata2.SCHOOL_ID, schoolid);
	}

	public String getSchoolid() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.SCHOOL_ID);
	}
	
	public void setSchoolcode(String schoolcode) {
		setHashmap(ViewSchoolMenuWithBatchdata2.SCHOOL_CODE, schoolcode);
	}

	public String getSchoolcode() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.SCHOOL_CODE);
	}

	public void setSchoolname(String schoolname) {
		setHashmap(ViewSchoolMenuWithBatchdata2.SCHOOL_NAME, schoolname);
	}

	public String getSchoolname() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.SCHOOL_NAME);
	}
	
	public void setMenutype(String menutype) {
		setHashmap(ViewSchoolMenuWithBatchdata2.MENU_TYPE, menutype);
	}

	public String getMenutype() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.MENU_TYPE);
	}
	
	public void setDishname(String dishname) {
		setHashmap(ViewSchoolMenuWithBatchdata2.DISH_NAME, dishname);
	}

	public String getDishname() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.DISH_NAME);
	}
	
	public void setIngredientname(String ingredientname) {
		setHashmap(ViewSchoolMenuWithBatchdata2.INGREDIENT_NAME, ingredientname);
	}

	public String getIngredientname() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.INGREDIENT_NAME);
	}
	
	public void setStockdate(String stockdate) {
		setHashmap(ViewSchoolMenuWithBatchdata2.STOCK_DATE, stockdate);
	}

	public String getStockdate() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.STOCK_DATE);
	}
	
	public void setIngredientquantity(String ingredientquantity) {
		setHashmap(ViewSchoolMenuWithBatchdata2.INGREDIENT_QUANTITY, ingredientquantity);
	}

	public String getIngredientquantity() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.INGREDIENT_QUANTITY);
	}
	
	public void setIngredientunit(String ingredientunit) {
		setHashmap(ViewSchoolMenuWithBatchdata2.INGREDIENT_UNIT, ingredientunit);
	}

	public String getIngredientunit() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.INGREDIENT_UNIT);
	}
	
	public void setSuppliername(String suppliername) {
		setHashmap(ViewSchoolMenuWithBatchdata2.SUPPLIER_NAME, suppliername);
	}

	public String getSuppliername() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.SUPPLIER_NAME);
	}
	
	public void setKitchenid(String kitchenid) {
		setHashmap(ViewSchoolMenuWithBatchdata2.KITCHEN_ID, kitchenid);
	}

	public String getKitchenid() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.KITCHEN_ID);
	}
	
	public void setKitchenname(String kitchenname) {
		setHashmap(ViewSchoolMenuWithBatchdata2.KITCHEN_NAME, kitchenname);
	}

	public String getKitchenname() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.KITCHEN_NAME);
	}
	
	public void setRestaurantname(String restaurantname) {
		setHashmap(ViewSchoolMenuWithBatchdata2.RESTAURANT_NAME, restaurantname);
	}

	public String getRestaurantid() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.RESTAURANT_ID);
	}
	
	public void setCountyid(String countyid) {
		setHashmap(ViewSchoolMenuWithBatchdata2.COUNTY_ID, countyid);
	}

	public String getCountyid() {
		return (String) menuParameterMap.get(ViewSchoolMenuWithBatchdata2.COUNTY_ID);
	}
	
	private void setHashmap(String key, Object value) {
		if (menuParameterMap.containsKey(key)) {
			menuParameterMap.put(key, value); // The method put will replace the value of an existing key and will create it if doesn't exist.
		} else {
			menuParameterMap.put(key, value);
		}
	}

	public HashMap<String, Object> getMap() {
		return menuParameterMap;
	}
}
