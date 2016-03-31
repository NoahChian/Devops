package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/*
 * "material":"沙拉油",
            "brand":"得意的一天",
            "source":"新北市中和區",
            "authenticate":"CAS",
            "supplierCompanyId":"22608851"

 */
public class CustomerQueryMenuDetailInfoSeasoning  implements Serializable {
	private String material;
	private String brand;
	private String source;
	private String authenticate;
	private String authenticateId;
	private String supplierCompanyId;
	private String productName;//品牌名稱
	private String stockDate;//進貨日期
	private String manufactureDate;//製造日期
	private String expirationDate;//期效日期
	private String geneticallyModifiedFood;//基改食品  玉米,黃豆
	private String processedFood;//加工食品
	private double ingredientQuantity;//食材數量
	private String ingredientUnit="KG";//食材單位
	private String manufacture;//製造商
	private IngredientCertificateObject ingredientCertificateObject;
	private List<Map<String,String>> geneticallyModifiedFoodlist;
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAuthenticate() {
		return authenticate;
	}
	public void setAuthenticate(String authenticate) {
		this.authenticate = authenticate;
	}
	public String getAuthenticateId() {
		return authenticateId;
	}
	public void setAuthenticateId(String authenticateId) {
		this.authenticateId = authenticateId;
	}
	public String getSupplierCompanyId() {
		return supplierCompanyId;
	}
	public void setSupplierCompanyId(String supplierCompanyId) {
		this.supplierCompanyId = supplierCompanyId;
	}
	public IngredientCertificateObject getIngredientCertificateObject() {
		return ingredientCertificateObject;
	}
	public void setIngredientCertificateObject(IngredientCertificateObject ingredientCertificateObject) {
		this.ingredientCertificateObject = ingredientCertificateObject;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getStockDate() {
		return stockDate;
	}
	public void setStockDate(String stockDate) {
		this.stockDate = stockDate;
	}
	public String getManufactureDate() {
		return manufactureDate;
	}
	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getGeneticallyModifiedFood() {
		return geneticallyModifiedFood;
	}
	public void setGeneticallyModifiedFood(String geneticallyModifiedFood) {
		this.geneticallyModifiedFood = geneticallyModifiedFood;
	}
	public String getProcessedFood() {
		return processedFood;
	}
	public void setProcessedFood(String processedFood) {
		this.processedFood = processedFood;
	}
	public double getIngredientQuantity() {
		return ingredientQuantity;
	}
	public void setIngredientQuantity(double ingredientQuantity) {
		this.ingredientQuantity = ingredientQuantity;
	}
	public String getIngredientUnit() {
		return ingredientUnit;
	}
	public void setIngredientUnit(String ingredientUnit) {
		this.ingredientUnit = ingredientUnit;
	}
	public String getManufacture() {
		return manufacture;
	}
	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}
	public List<Map<String, String>> getGeneticallyModifiedFoodlist() {
		return geneticallyModifiedFoodlist;
	}
	public void setGeneticallyModifiedFoodlist(List<Map<String, String>> geneticallyModifiedFoodlist) {
		this.geneticallyModifiedFoodlist = geneticallyModifiedFoodlist;
	}
}
