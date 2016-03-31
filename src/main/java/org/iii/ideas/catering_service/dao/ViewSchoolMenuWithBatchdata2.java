package org.iii.ideas.catering_service.dao;

import java.util.Date;

/**
 * ViewSchoolMenuWithBatchdata2 entity. @author MyEclipse Persistence Tools
 */

public class ViewSchoolMenuWithBatchdata2 implements java.io.Serializable {

	private String id; //pk 如果沒有此欄位,會造成hibernate-mapping錯誤.
	private Date menudate;
	private Integer schoolid;
	private String schoolcode;
	private String schoolname;
	private String menutype;
	private String dishname;
	private String ingredientname;
	private String stockdate;
	private String ingredientquantity;
	private String ingredientunit;
	private String suppliername;
	private String kitchenid;
	private String kitchenname;
	private String restaurantid;
	private String restaurantname;
	private Integer countyid;
	private String typegrains;
	private String typeoil;
	private String typevegetable;
	private String typemilk;
	private String typefruit;
	private String typemeatbeans;
	private String calorie;
	
	public static final String MENU_DATE = "menudate"; // String
	public static final String SCHOOL_ID = "schoolid"; // String
	public static final String SCHOOL_CODE = "schoolcode"; // String
	public static final String SCHOOL_NAME = "schoolname"; // String
	public static final String MENU_TYPE = "menutype"; // String
	public static final String DISH_NAME = "dishname"; // String
	public static final String INGREDIENT_NAME = "ingredientname"; // String
	public static final String STOCK_DATE = "stockdate"; // String
	public static final String INGREDIENT_QUANTITY = "ingredientquantity"; // String
	public static final String INGREDIENT_UNIT = "ingredientunit"; // String
	public static final String SUPPLIER_NAME = "suppliername"; // String
	public static final String KITCHEN_ID = "kitchenid"; // String
	public static final String KITCHEN_NAME = "kitchenname"; // String
	public static final String RESTAURANT_ID = "restaurantid"; // String
	public static final String RESTAURANT_NAME = "restaurantname"; // String
	public static final String COUNTY_ID = "countyid"; // String
	public static final String TYPEGRAINS = "typegrains"; // String
	public static final String TYPEOIL = "typeoil"; // String
	public static final String TYPEVEGETABLE = "typevegetable"; // String
	public static final String TYPEMILK = "typemilk"; // String
	public static final String TYPEFRUIT = "typefruit"; // String
	public static final String TYPEMEATBEANS = "typemeatbeans"; // String
	public static final String CALORIE = "calorie"; // String
		
	/** default constructor */
	public ViewSchoolMenuWithBatchdata2() {
	}
	
	/** minimal constructor */
	public ViewSchoolMenuWithBatchdata2(Date menudate, String kitchenid,
			Integer schoolid, String schoolcode, Integer countyid,
			String schoolname) {
		this.menudate = menudate;
		this.kitchenid = kitchenid;
		this.schoolid = schoolid;
		this.schoolcode = schoolcode;
		this.countyid = countyid;
		this.schoolname = schoolname;
//		this.batchdataid = batchdataid;
	}
	
	public Date getMenudate() {
		return menudate;
	}

	public void setMenudate(Date menudate) {
		this.menudate = menudate;
	}

	public Integer getSchoolid() {
		return schoolid;
	}

	public void setSchoolid(Integer schoolid) {
		this.schoolid = schoolid;
	}

	public String getSchoolcode() {
		return schoolcode;
	}

	public void setSchoolcode(String schoolcode) {
		this.schoolcode = schoolcode;
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public String getMenutype() {
		return menutype;
	}

	public void setMenutype(String menutype) {
		this.menutype = menutype;
	}

	public String getDishname() {
		return dishname;
	}

	public void setDishname(String dishname) {
		this.dishname = dishname;
	}

	public String getIngredientname() {
		return ingredientname;
	}

	public void setIngredientname(String ingredientname) {
		this.ingredientname = ingredientname;
	}

	public String getStockdate() {
		return stockdate;
	}

	public void setStockdate(String stockdate) {
		this.stockdate = stockdate;
	}

	public String getIngredientquantity() {
		return ingredientquantity;
	}

	public void setIngredientquantity(String ingredientquantity) {
		this.ingredientquantity = ingredientquantity;
	}

	public String getIngredientunit() {
		return ingredientunit;
	}

	public void setIngredientunit(String ingredientunit) {
		this.ingredientunit = ingredientunit;
	}

	public String getSuppliername() {
		return suppliername;
	}

	public void setSuppliername(String suppliername) {
		this.suppliername = suppliername;
	}

	public String getKitchenid() {
		return kitchenid;
	}

	public void setKitchenid(String kitchenid) {
		this.kitchenid = kitchenid;
	}

	public String getKitchenname() {
		return kitchenname;
	}

	public void setKitchenname(String kitchenname) {
		this.kitchenname = kitchenname;
	}
	
	public Integer getCountyid() {
		return countyid;
	}

	public void setCountyid(Integer countyid) {
		this.countyid = countyid;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRestaurantname() {
		return restaurantname;
	}

	public void setRestaurantname(String restaurantname) {
		this.restaurantname = restaurantname;
	}

	public String getRestaurantid() {
		return restaurantid;
	}

	public void setRestaurantid(String restaurantid) {
		this.restaurantid = restaurantid;
	}	
	
	public String getTypegrains() {
		return typegrains;
	}

	public void setTypegrains(String typegrains) {
		this.typegrains = typegrains;
	}

	public String getTypeoil() {
		return typeoil;
	}

	public void setTypeoil(String typeoil) {
		this.typeoil = typeoil;
	}

	public String getTypevegetable() {
		return typevegetable;
	}

	public void setTypevegetable(String typevegetable) {
		this.typevegetable = typevegetable;
	}

	public String getTypemilk() {
		return typemilk;
	}

	public void setTypemilk(String typemilk) {
		this.typemilk = typemilk;
	}

	public String getTypefruit() {
		return typefruit;
	}

	public void setTypefruit(String typefruit) {
		this.typefruit = typefruit;
	}

	public String getTypemeatbeans() {
		return typemeatbeans;
	}

	public void setTypemeatbeans(String typemeatbeans) {
		this.typemeatbeans = typemeatbeans;
	}

	public String getCalorie() {
		return calorie;
	}

	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}
}