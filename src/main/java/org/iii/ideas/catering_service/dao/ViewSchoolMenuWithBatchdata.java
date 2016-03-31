package org.iii.ideas.catering_service.dao;

import javax.persistence.Id;

/**
 * ViewSchoolMenuWithBatchdata entity. @author MyEclipse Persistence Tools
 */

public class ViewSchoolMenuWithBatchdata implements java.io.Serializable {


	//private Integer id;
	private String menudate;
	private Integer kitchenid;
	private String kitchenname;
	private Integer schoolid;
	private String schoolcode;
	private Integer countyid;
	private String schoolname;
	@Id
	private Long batchdataid;
	private String mainFoolId;
	private String mainFood1id;
	private String mainDishId;
	private String mainDish1id;
	private String mainDish2id;
	private String mainDish3id;
	private String subDish1id;
	private String subDish2id;
	private String subDish3id;
	private String subDish4id;
	private String subDish5id;
	private String subDish6id;
	private String vegetableId;
	private String soupId;
	private String dessertId;
	private String dessert1id;
	private String typeGrains;
	private String typeOil;
	private String typeVegetable;
	private String typeMilk;
	private String typeFruit;
	private String typeMeatBeans;
	private String calorie;
	private String srcType;
	public static final String MENU_DATE = "menudate";	//String
	public static final String KITCHEN_ID = "kitchenid";	//Integer
	public static final String KITCHEN_NAME = "kitchenname";	//String
	public static final String SCHOOL_ID = "schoolid";	//Integer
	public static final String SCHOOL_CODE = "schoolcode";	//String
	public static final String COUNTY_ID = "countyid";	//Integer
	public static final String BATCH_DATA_ID = "batchdataid";	//Long
	public static final String SCHOOL_NAME = "schoolname";
	public static final String MAIN_FOOD_ID = "mainFoolId";
	public static final String MAIN_FOOD1_ID = "mainFood1id";
	public static final String MAIN_DISH_ID = "mainDishId";
	public static final String MAIN_DISH1_ID = "mainDish1id";
	public static final String MAIN_DISH2_ID = "mainDish2id";
	public static final String MAIN_DISH3_ID = "mainDish3id";
	public static final String SUB_DISH1_ID = "subDish1id";
	public static final String SUB_DISH2_ID = "subDish2id";
	public static final String SUB_DISH3_ID = "subDish3id";
	public static final String SUB_DISH4_ID = "subDish4id";
	public static final String SUB_DISH5_ID = "subDish5id";
	public static final String SUB_DISH6_ID = "subDish6id";
	public static final String VEGETABLE_ID = "vegetableId";
	public static final String SOUP_ID = "soupId";
	public static final String DESSERT_ID = "dessertId";
	public static final String DESSERT1_ID = "dessert1Id";
	public static final String TYPE_GRAINS = "typeGrains";
	public static final String TYPE_OIL = "typeOil";
	public static final String TYPE_VEGETABLE = "typeVegetable";
	public static final String TYPE_MILK = "typeMilk";
	public static final String TYPE_FRUIT = "typeFruit";
	public static final String TYPE_MEAT_BEANS = "typeMeatBeans";
	public static final String CALLORI = "calorie";
	public static final String SRC_TYPE = "srcType";
	// Constructors

	/** default constructor */
	public ViewSchoolMenuWithBatchdata() {
	}

	/** minimal constructor */
	public ViewSchoolMenuWithBatchdata(String menudate, Integer kitchenid,
			Integer schoolid, String schoolcode, Integer countyid,
			String schoolname, Long batchdataid) {
		this.menudate = menudate;
		this.kitchenid = kitchenid;
		this.schoolid = schoolid;
		this.schoolcode = schoolcode;
		this.countyid = countyid;
		this.schoolname = schoolname;
		this.batchdataid = batchdataid;
	}

	/** full constructor */
	public ViewSchoolMenuWithBatchdata(String menudate, Integer kitchenid,
			Integer schoolid, String schoolcode, Integer countyid,
			String schoolname, Long batchdataid, String mainFoolId,
			String mainFood1id, String mainDishId, String mainDish1id,
			String mainDish2id, String mainDish3id, String subDish1id,
			String subDish2id, String subDish3id, String subDish4id,
			String subDish5id, String subDish6id, String vegetableId,
			String soupId, String dessertId, String dessert1id,
			String typeGrains, String typeOil, String typeVegetable,
			String typeMilk, String typeFruit, String typeMeatBeans,
			String calorie, String srcType) {
		this.menudate = menudate;
		this.kitchenid = kitchenid;
		this.schoolid = schoolid;
		this.schoolcode = schoolcode;
		this.countyid = countyid;
		this.schoolname = schoolname;
		this.batchdataid = batchdataid;
		this.mainFoolId = mainFoolId;
		this.mainFood1id = mainFood1id;
		this.mainDishId = mainDishId;
		this.mainDish1id = mainDish1id;
		this.mainDish2id = mainDish2id;
		this.mainDish3id = mainDish3id;
		this.subDish1id = subDish1id;
		this.subDish2id = subDish2id;
		this.subDish3id = subDish3id;
		this.subDish4id = subDish4id;
		this.subDish5id = subDish5id;
		this.subDish6id = subDish6id;
		this.vegetableId = vegetableId;
		this.soupId = soupId;
		this.dessertId = dessertId;
		this.dessert1id = dessert1id;
		this.typeGrains = typeGrains;
		this.typeOil = typeOil;
		this.typeVegetable = typeVegetable;
		this.typeMilk = typeMilk;
		this.typeFruit = typeFruit;
		this.typeMeatBeans = typeMeatBeans;
		this.calorie = calorie;
		this.srcType = srcType;
	}

	// Property accessors

	public String getMenudate() {
		return this.menudate;
	}

	public void setMenudate(String menudate) {
		this.menudate = menudate;
	}

	public Integer getKitchenid() {
		return this.kitchenid;
	}

	public void setKitchenid(Integer kitchenid) {
		this.kitchenid = kitchenid;
	}

	public Integer getSchoolid() {
		return this.schoolid;
	}

	public void setSchoolid(Integer schoolid) {
		this.schoolid = schoolid;
	}

	public String getSchoolcode() {
		return this.schoolcode;
	}

	public void setSchoolcode(String schoolcode) {
		this.schoolcode = schoolcode;
	}

	public Integer getCountyid() {
		return this.countyid;
	}

	public void setCountyid(Integer countyid) {
		this.countyid = countyid;
	}

	public String getSchoolname() {
		return this.schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public Long getBatchdataid() {
		return this.batchdataid;
	}

	public void setBatchdataid(Long batchdataid) {
		this.batchdataid = batchdataid;
	}

	public String getMainFoolId() {
		return this.mainFoolId;
	}

	public void setMainFoolId(String mainFoolId) {
		this.mainFoolId = mainFoolId;
	}

	public String getMainFood1id() {
		return this.mainFood1id;
	}

	public void setMainFood1id(String mainFood1id) {
		this.mainFood1id = mainFood1id;
	}

	public String getMainDishId() {
		return this.mainDishId;
	}

	public void setMainDishId(String mainDishId) {
		this.mainDishId = mainDishId;
	}

	public String getMainDish1id() {
		return this.mainDish1id;
	}

	public void setMainDish1id(String mainDish1id) {
		this.mainDish1id = mainDish1id;
	}

	public String getMainDish2id() {
		return this.mainDish2id;
	}

	public void setMainDish2id(String mainDish2id) {
		this.mainDish2id = mainDish2id;
	}

	public String getMainDish3id() {
		return this.mainDish3id;
	}

	public void setMainDish3id(String mainDish3id) {
		this.mainDish3id = mainDish3id;
	}

	public String getSubDish1id() {
		return this.subDish1id;
	}

	public void setSubDish1id(String subDish1id) {
		this.subDish1id = subDish1id;
	}

	public String getSubDish2id() {
		return this.subDish2id;
	}

	public void setSubDish2id(String subDish2id) {
		this.subDish2id = subDish2id;
	}

	public String getSubDish3id() {
		return this.subDish3id;
	}

	public void setSubDish3id(String subDish3id) {
		this.subDish3id = subDish3id;
	}

	public String getSubDish4id() {
		return this.subDish4id;
	}

	public void setSubDish4id(String subDish4id) {
		this.subDish4id = subDish4id;
	}

	public String getSubDish5id() {
		return this.subDish5id;
	}

	public void setSubDish5id(String subDish5id) {
		this.subDish5id = subDish5id;
	}

	public String getSubDish6id() {
		return this.subDish6id;
	}

	public void setSubDish6id(String subDish6id) {
		this.subDish6id = subDish6id;
	}

	public String getVegetableId() {
		return this.vegetableId;
	}

	public void setVegetableId(String vegetableId) {
		this.vegetableId = vegetableId;
	}

	public String getSoupId() {
		return this.soupId;
	}

	public void setSoupId(String soupId) {
		this.soupId = soupId;
	}

	public String getDessertId() {
		return this.dessertId;
	}

	public void setDessertId(String dessertId) {
		this.dessertId = dessertId;
	}

	public String getDessert1id() {
		return this.dessert1id;
	}

	public void setDessert1id(String dessert1id) {
		this.dessert1id = dessert1id;
	}

	public String getTypeGrains() {
		return this.typeGrains;
	}

	public void setTypeGrains(String typeGrains) {
		this.typeGrains = typeGrains;
	}

	public String getTypeOil() {
		return this.typeOil;
	}

	public void setTypeOil(String typeOil) {
		this.typeOil = typeOil;
	}

	public String getTypeVegetable() {
		return this.typeVegetable;
	}

	public void setTypeVegetable(String typeVegetable) {
		this.typeVegetable = typeVegetable;
	}

	public String getTypeMilk() {
		return this.typeMilk;
	}

	public void setTypeMilk(String typeMilk) {
		this.typeMilk = typeMilk;
	}

	public String getTypeFruit() {
		return this.typeFruit;
	}

	public void setTypeFruit(String typeFruit) {
		this.typeFruit = typeFruit;
	}

	public String getTypeMeatBeans() {
		return this.typeMeatBeans;
	}

	public void setTypeMeatBeans(String typeMeatBeans) {
		this.typeMeatBeans = typeMeatBeans;
	}

	public String getCalorie() {
		return this.calorie;
	}

	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}

	public String getSrcType() {
		return this.srcType;
	}

	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}

	public String getKitchenname() {
		return kitchenname;
	}

	public void setKitchenname(String kitchenname) {
		this.kitchenname = kitchenname;
	}

	//public Integer getId() {
	//	return id;
	//}

	//public void setId(Integer id) {
	//	this.id = id;
	//}


}