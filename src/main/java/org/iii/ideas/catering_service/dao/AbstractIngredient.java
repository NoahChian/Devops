package org.iii.ideas.catering_service.dao;

/**
 * AbstractIngredient entity provides the base persistence definition of the
 * Ingredient entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractIngredient implements java.io.Serializable {

	// Fields

	private Long ingredientId;
	private Long dishId;
	private Integer supplierId;
	private String ingredientName;
	private String brand;
	private String supplierCompanyId;
	private String brandNo;
	private String productName;
	private String Manufacturer;
	private String TypeGrains ;
	private String TypeMeatbeans ;
	private String TypeVegetable ;
	private String TypeOil ;
	private String TypeFruit ;
	private String TypeMilk ;
	private String Calorie ;

	// Constructors

	/** default constructor */
	public AbstractIngredient() {
	}

	/**old full constructor* */
	public AbstractIngredient(Long dishId, Integer supplierId,
			String ingredientName, String brand,String supplierCompanyId) {
		this.dishId = dishId;
		this.supplierId = supplierId;
		this.ingredientName = ingredientName;
		this.brand = brand;
		this.supplierCompanyId= supplierCompanyId;
	}
	
	
	/**new full constructor* 
	 * 20140312 KC
	 * add brandNo欄位
	 * */
	public AbstractIngredient(Long dishId, Integer supplierId,
			String ingredientName, String brand,String supplierCompanyId,String brandNo) {
		this.dishId = dishId;
		this.supplierId = supplierId;
		this.ingredientName = ingredientName;
		this.brand = brand;
		this.supplierCompanyId= supplierCompanyId;
		this.brandNo=brandNo;
	}

	// Property accessors

	public Long getIngredientId() {
		return this.ingredientId;
	}

	public void setIngredientId(Long ingredientId) {
		this.ingredientId = ingredientId;
	}

	public Long getDishId() {
		return this.dishId;
	}

	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}

	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getIngredientName() {
		return this.ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSupplierCompanyId() {
		return supplierCompanyId;
	}

	public void setSupplierCompanyId(String supplierCompanyId) {
		this.supplierCompanyId = supplierCompanyId;
	}
	
	public String getBrandNo() {
		return this.brand;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getManufacturer() {
		return Manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		Manufacturer = manufacturer;
	}

	public String getTypeGrains() {
		return TypeGrains;
	}

	public void setTypeGrains(String typeGrains) {
		TypeGrains = typeGrains;
	}

	public String getTypeMeatbeans() {
		return TypeMeatbeans;
	}

	public void setTypeMeatbeans(String typeMeatbeans) {
		TypeMeatbeans = typeMeatbeans;
	}

	public String getTypeVegetable() {
		return TypeVegetable;
	}

	public void setTypeVegetable(String typeVegetable) {
		TypeVegetable = typeVegetable;
	}

	public String getTypeOil() {
		return TypeOil;
	}

	public void setTypeOil(String typeOil) {
		TypeOil = typeOil;
	}

	public String getTypeFruit() {
		return TypeFruit;
	}

	public void setTypeFruit(String typeFruit) {
		TypeFruit = typeFruit;
	}

	public String getTypeMilk() {
		return TypeMilk;
	}

	public void setTypeMilk(String typeMilk) {
		TypeMilk = typeMilk;
	}

	public String getCalorie() {
		return Calorie;
	}

	public void setCalorie(String calorie) {
		Calorie = calorie;
	}


}