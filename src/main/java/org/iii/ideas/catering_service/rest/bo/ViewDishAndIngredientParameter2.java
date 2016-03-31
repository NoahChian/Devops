package org.iii.ideas.catering_service.rest.bo;

import java.util.HashMap;

import org.iii.ideas.catering_service.dao.ViewDishAndIngredient2;

public class ViewDishAndIngredientParameter2 {
	private HashMap<String, Object> ingredientListParameterMap = new HashMap<String, Object>();

	// private String ingredientName;
	
	public void setSchoolname(String schoolname) {
		setHashmap(ViewDishAndIngredient2.SCHOOL_NAME, schoolname);
	}

	public String getSchoolname() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.SCHOOL_NAME);
	}

	public void setKitchenname(String kitchenname) {
		setHashmap(ViewDishAndIngredient2.KITCHEN_NAME, kitchenname);
	}

	public String getKitchenname() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.KITCHEN_NAME);
	}

	public void setDishname(String dishname) {
		setHashmap(ViewDishAndIngredient2.DISH_NAME, dishname);
	}

	public String getDishname() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.DISH_NAME);
	}

	public void setDishtype(String dishtype) {
		setHashmap(ViewDishAndIngredient2.DISH_TYPE, dishtype);
	}

	public String getDishtype() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.DISH_TYPE);
	}

	public void setUpdatedatetime(String updatedatetime) {
		setHashmap(ViewDishAndIngredient2.UPDATE_DATE_TIME, updatedatetime);
	}

	public String getUpdatedatetime() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.UPDATE_DATE_TIME);
	}

	public void setDishshowname(String dishshowname) {
		setHashmap(ViewDishAndIngredient2.DISH_SHOW_NAME, dishshowname);
	}

	public String getDishshowname() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.DISH_SHOW_NAME);
	}

	public void setBatchDataId(String batchDataId) {
		setHashmap(ViewDishAndIngredient2.BATCH_DATA_ID, batchDataId);
	}

	public String getBatchDataId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.BATCH_DATA_ID);
	}

	public void setDishId(String dishId) {
		setHashmap(ViewDishAndIngredient2.DISH_ID, dishId);
	}

	public String getDishId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.DISH_ID);
	}

	public void setIngredientId(String ingredientId) {
		setHashmap(ViewDishAndIngredient2.INGREDIENT_ID, ingredientId);
	}

	public String getIngredientId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.INGREDIENT_ID);
	}

	public void setIngredientName(String ingredientName) {
		setHashmap(ViewDishAndIngredient2.INGREDIENT_NAME, ingredientName);
	}

	public String getIngredientName() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.INGREDIENT_NAME);
	}

	public void setStockDate(String stockDate) {
		setHashmap(ViewDishAndIngredient2.STOCK_DATA, stockDate);
	}

	public String getStockDate() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.STOCK_DATA);
	}

	public void setManufactureDate(String manufactureDate) {
		setHashmap(ViewDishAndIngredient2.MANUFACTURE_DATE, manufactureDate);
	}

	public String getManufactureDate() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.MANUFACTURE_DATE);
	}

	public void setExpirationDate(String expirationDate) {
		setHashmap(ViewDishAndIngredient2.EXPIRATION_DATE, expirationDate);
	}

	public String getExpirationDate() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.EXPIRATION_DATE);
	}

	public void setLotNumber(String lotNumber) {
		setHashmap(ViewDishAndIngredient2.LOT_NUMBER, lotNumber);
	}

	public String getLotNumber() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.LOT_NUMBER);
	}

	public void setBrand(String brand) {
		setHashmap(ViewDishAndIngredient2.BRAND, brand);
	}

	public String getBrand() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.BRAND);
	}

	public void setOrigin(String origin) {
		setHashmap(ViewDishAndIngredient2.ORIGIN, origin);
	}

	public String getOrigin() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.ORIGIN);
	}

	public void setSource(String source) {
		setHashmap(ViewDishAndIngredient2.SOURCE, source);
	}

	public String getSource() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.SOURCE);
	}

	public void setSupplierId(String supplierId) {
		setHashmap(ViewDishAndIngredient2.SUPPLIER_ID, supplierId);
	}

	public String getSupplierId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.SUPPLIER_ID);
	}

	public void setSourceCertification(String sourceCertification) {
		setHashmap(ViewDishAndIngredient2.SOURCE_CERTIFICATION, sourceCertification);
	}

	public String getSourceCertification() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.SOURCE_CERTIFICATION);
	}

	public void setCertificationId(String certificationId) {
		setHashmap(ViewDishAndIngredient2.CERTIFICATION_ID, certificationId);
	}

	public String getCertificationId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.CERTIFICATION_ID);
	}

	public void setMenuId(String menuId) {
		setHashmap(ViewDishAndIngredient2.MENU_ID, menuId);
	}

	public String getMenuId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.MENU_ID);
	}

	public void setSupplierCompanyId(String supplierCompanyId) {
		setHashmap(ViewDishAndIngredient2.SUPPLIER_COMPANY_ID, supplierCompanyId);
	}

	public String getSupplierCompanyId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.SUPPLIER_COMPANY_ID);
	}

	public void setSupplierName(String supplierName) {
		setHashmap(ViewDishAndIngredient2.SUPPLIER_NAME, supplierName);
	}

	public String getSupplierName() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.SUPPLIER_NAME);
	}

	public void setBrandNo(String brandNo) {
		setHashmap(ViewDishAndIngredient2.BRAND_NO, brandNo);
	}

	public String getBrandNo() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.BRAND_NO);
	}

//	public void setFilepath(String filepath) {
//		setHashmap(ViewDishAndIngredient2.FILE_PATH, filepath);
//	}

//	public String getFilepath() {
//		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.FILE_PATH);
//	}

	public void setDishbatchdataid(String dishbatchdataid) {
		setHashmap(ViewDishAndIngredient2.DISH_BATCH_DATA_ID, dishbatchdataid);
	}

	public String getDishbatchdataid() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.DISH_BATCH_DATA_ID);
	}

	public void setIngredientBatchId(String ingredientBatchId) {
		setHashmap(ViewDishAndIngredient2.INGREDIENT_BATCH_ID, ingredientBatchId);
	}

	public String getIngredientBatchId() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.INGREDIENT_BATCH_ID);
	}
	//----------+++
	public void setManufacturer(String manufacturer) {
		setHashmap(ViewDishAndIngredient2.MANUFACTURER, manufacturer);
	}

	public String getManufacturer() {
		return (String) ingredientListParameterMap.get(ViewDishAndIngredient2.MANUFACTURER);
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
	public void setValueByViewDishAndIngredient2Obj(ViewDishAndIngredient2 obj){
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
