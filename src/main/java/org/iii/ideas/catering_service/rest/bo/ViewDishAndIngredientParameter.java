package org.iii.ideas.catering_service.rest.bo;

import java.util.HashMap;

import org.iii.ideas.catering_service.dao.ViewDishAndIngredient;

public class ViewDishAndIngredientParameter {
	private HashMap<String, Object> ingredientListParameterMap = new HashMap<String, Object>();

	// private String ingredientName;
	
	public void setSchoolname(String schoolname) {
		setHashmap(ViewDishAndIngredient.SCHOOL_NAME, schoolname);
	}

	public String getSchoolname() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.SCHOOL_NAME);
	}

	public void setKitchenname(String kitchenname) {
		setHashmap(ViewDishAndIngredient.KITCHEN_NAME, kitchenname);
	}

	public String getKitchenname() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.KITCHEN_NAME);
	}

	public void setDishname(String dishname) {
		setHashmap(ViewDishAndIngredient.DISH_NAME, dishname);
	}

	public String getDishname() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.DISH_NAME);
	}

	public void setDishtype(String dishtype) {
		setHashmap(ViewDishAndIngredient.DISH_TYPE, dishtype);
	}

	public String getDishtype() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.DISH_TYPE);
	}

	public void setUpdatedatetime(String updatedatetime) {
		setHashmap(ViewDishAndIngredient.UPDATE_DATE_TIME, updatedatetime);
	}

	public String getUpdatedatetime() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.UPDATE_DATE_TIME);
	}

	public void setDishshowname(String dishshowname) {
		setHashmap(ViewDishAndIngredient.DISH_SHOW_NAME, dishshowname);
	}

	public String getDishshowname() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.DISH_SHOW_NAME);
	}

	public void setBatchDataId(String batchDataId) {
		setHashmap(ViewDishAndIngredient.BATCH_DATA_ID, batchDataId);
	}

	public String getBatchDataId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.BATCH_DATA_ID);
	}

	public void setDishId(String dishId) {
		setHashmap(ViewDishAndIngredient.DISH_ID, dishId);
	}

	public String getDishId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.DISH_ID);
	}

	public void setIngredientId(String ingredientId) {
		setHashmap(ViewDishAndIngredient.INGREDIENT_ID, ingredientId);
	}

	public String getIngredientId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.INGREDIENT_ID);
	}

	public void setIngredientName(String ingredientName) {
		setHashmap(ViewDishAndIngredient.INGREDIENT_NAME, ingredientName);
	}

	public String getIngredientName() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.INGREDIENT_NAME);
	}

	public void setStockDate(String stockDate) {
		setHashmap(ViewDishAndIngredient.STOCK_DATA, stockDate);
	}

	public String getStockDate() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.STOCK_DATA);
	}

	public void setManufactureDate(String manufactureDate) {
		setHashmap(ViewDishAndIngredient.MANUFACTURE_DATE, manufactureDate);
	}

	public String getManufactureDate() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.MANUFACTURE_DATE);
	}

	public void setExpirationDate(String expirationDate) {
		setHashmap(ViewDishAndIngredient.EXPIRATION_DATE, expirationDate);
	}

	public String getExpirationDate() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.EXPIRATION_DATE);
	}

	public void setLotNumber(String lotNumber) {
		setHashmap(ViewDishAndIngredient.LOT_NUMBER, lotNumber);
	}

	public String getLotNumber() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.LOT_NUMBER);
	}

	public void setBrand(String brand) {
		setHashmap(ViewDishAndIngredient.BRAND, brand);
	}

	public String getBrand() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.BRAND);
	}

	public void setOrigin(String origin) {
		setHashmap(ViewDishAndIngredient.ORIGIN, origin);
	}

	public String getOrigin() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.ORIGIN);
	}

	public void setSource(String source) {
		setHashmap(ViewDishAndIngredient.SOURCE, source);
	}

	public String getSource() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.SOURCE);
	}

	public void setSupplierId(String supplierId) {
		setHashmap(ViewDishAndIngredient.SUPPLIER_ID, supplierId);
	}

	public String getSupplierId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.SUPPLIER_ID);
	}

	public void setSourceCertification(String sourceCertification) {
		setHashmap(ViewDishAndIngredient.SOURCE_CERTIFICATION, sourceCertification);
	}

	public String getSourceCertification() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.SOURCE_CERTIFICATION);
	}

	public void setCertificationId(String certificationId) {
		setHashmap(ViewDishAndIngredient.CERTIFICATION_ID, certificationId);
	}

	public String getCertificationId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.CERTIFICATION_ID);
	}

	public void setMenuId(String menuId) {
		setHashmap(ViewDishAndIngredient.MENU_ID, menuId);
	}

	public String getMenuId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.MENU_ID);
	}

	public void setSupplierCompanyId(String supplierCompanyId) {
		setHashmap(ViewDishAndIngredient.SUPPLIER_COMPANY_ID, supplierCompanyId);
	}

	public String getSupplierCompanyId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.SUPPLIER_COMPANY_ID);
	}

	public void setSupplierName(String supplierName) {
		setHashmap(ViewDishAndIngredient.SUPPLIER_NAME, supplierName);
	}

	public String getSupplierName() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.SUPPLIER_NAME);
	}

	public void setBrandNo(String brandNo) {
		setHashmap(ViewDishAndIngredient.BRAND_NO, brandNo);
	}

	public String getBrandNo() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.BRAND_NO);
	}

	public void setFilepath(String filepath) {
		setHashmap(ViewDishAndIngredient.FILE_PATH, filepath);
	}

	public String getFilepath() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.FILE_PATH);
	}

	public void setDishbatchdataid(String dishbatchdataid) {
		setHashmap(ViewDishAndIngredient.DISH_BATCH_DATA_ID, dishbatchdataid);
	}

	public String getDishbatchdataid() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.DISH_BATCH_DATA_ID);
	}

	public void setIngredientBatchId(String ingredientBatchId) {
		setHashmap(ViewDishAndIngredient.INGREDIENT_BATCH_ID, ingredientBatchId);
	}

	public String getIngredientBatchId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.INGREDIENT_BATCH_ID);
	}
	//----------+++
	public void setManufacturer(String manufacturer) {
		setHashmap(ViewDishAndIngredient.MANUFACTURER, manufacturer);
	}

	public String getManufacturer() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient.MANUFACTURER);
	}
	//+++----------
	
	private void setHashmap(String key, Object value) {
		if (ingredientListParameterMap.containsKey(key)) {
			ingredientListParameterMap.put(key, value); // The method put will replace the value of an existing key and will create it if doesn't exist.
		} else {
			ingredientListParameterMap.put(key, value);
		}
	}

	public HashMap<String, Object> getMap() {
		return ingredientListParameterMap;
	}
	public void setValueByViewDishAndIngredientObj(ViewDishAndIngredient obj){
	/*	if (obj.getBatchDataId()!=null){
			this.setBatchDataId(obj.getBatchDataId().toString());
		}*/
		if(obj.getBrand()!=null){
			this.setBrand(obj.getBrand());
		}
		if (obj.getIngredientName()!=null){
			this.setIngredientName(obj.getIngredientName());
		}
		if (obj.getSupplierName()!=null){
			this.setSupplierName(obj.getSupplierName());
		}
		
	}
}
