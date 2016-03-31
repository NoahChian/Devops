package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractMenu entity provides the base persistence definition of the Menu
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractMenu implements java.io.Serializable {

	// Fields

	private Long menuId;
	private Integer kitchenId;
	private String menuDate;
	private Long mainFoodId;
	private Long mainFood1id;
	private Long mainDishId;
	private Long mainDish1id;
	private Long mainDish2id;
	private Long mainDish3id;
	private Long subDish1id;
	private Long subDish2id;
	private Long subDish3id;
	private Long subDish4id;
	private Long subDish5id;
	private Long subDish6id;
	private Long vegetableId;
	private Long soupId;
	private Long dessertId;
	private Long dessert1id;
	private String typeGrains;
	private String typeOil;
	private String typeVegetable;
	private String typeMilk;
	private String typeFruit;
	private String typeMeatBeans;
	private String calorie;
	private Timestamp uploadDateTime;

	// Constructors

	/** default constructor */
	public AbstractMenu() {
	}

	/** minimal constructor */
	/*
	public AbstractMenu(Integer kitchenId, String menuDate, Integer mainFoodId,
			Integer mainFood1id, Integer mainDishId, Integer mainDish1id,
			Integer mainDish2id, Integer mainDish3id, Integer subDish1id,
			Integer subDish2id, Integer subDish3id, Integer subDish4id,
			Integer subDish5id, Integer subDish6id, Integer vegetableId,
			Integer soupId, Integer dessertId, Integer dessert1id,
			String typeGrains, String typeOil, String typeVegetable,
			String typeMilk, String typeFruit, String typeMeatBeans,
			String calorie) {
		this.kitchenId = kitchenId;
		this.menuDate = menuDate;
		this.mainFoodId = mainFoodId;
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
	}
*/
	/** full constructor */
	/*
	public AbstractMenu(Integer kitchenId, String menuDate, Integer mainFoodId,
			Integer mainFood1id, Integer mainDishId, Integer mainDish1id,
			Integer mainDish2id, Integer mainDish3id, Integer subDish1id,
			Integer subDish2id, Integer subDish3id, Integer subDish4id,
			Integer subDish5id, Integer subDish6id, Integer vegetableId,
			Integer soupId, Integer dessertId, Integer dessert1id,
			String typeGrains, String typeOil, String typeVegetable,
			String typeMilk, String typeFruit, String typeMeatBeans,
			String calorie, Timestamp uploadDateTime) {
		this.kitchenId = kitchenId;
		this.menuDate = menuDate;
		this.mainFoodId = mainFoodId;
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
		this.uploadDateTime = uploadDateTime;
	}
*/
	// Property accessors

	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Integer getKitchenId() {
		return this.kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public String getMenuDate() {
		return this.menuDate;
	}

	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}

	public Long getMainFoodId() {
		return this.mainFoodId;
	}

	public void setMainFoodId(Long mainFoodId) {
		this.mainFoodId = mainFoodId;
	}

	public Long getMainFood1id() {
		return this.mainFood1id;
	}

	public void setMainFood1id(Long mainFood1id) {
		this.mainFood1id = mainFood1id;
	}

	public Long getMainDishId() {
		return this.mainDishId;
	}

	public void setMainDishId(Long mainDishId) {
		this.mainDishId = mainDishId;
	}

	public Long getMainDish1id() {
		return this.mainDish1id;
	}

	public void setMainDish1id(Long mainDish1id) {
		this.mainDish1id = mainDish1id;
	}

	public Long getMainDish2id() {
		return this.mainDish2id;
	}

	public void setMainDish2id(Long mainDish2id) {
		this.mainDish2id = mainDish2id;
	}

	public Long getMainDish3id() {
		return this.mainDish3id;
	}

	public void setMainDish3id(Long mainDish3id) {
		this.mainDish3id = mainDish3id;
	}

	public Long getSubDish1id() {
		return this.subDish1id;
	}

	public void setSubDish1id(Long subDish1id) {
		this.subDish1id = subDish1id;
	}

	public Long getSubDish2id() {
		return this.subDish2id;
	}

	public void setSubDish2id(Long subDish2id) {
		this.subDish2id = subDish2id;
	}

	public Long getSubDish3id() {
		return this.subDish3id;
	}

	public void setSubDish3id(Long subDish3id) {
		this.subDish3id = subDish3id;
	}

	public Long getSubDish4id() {
		return this.subDish4id;
	}

	public void setSubDish4id(Long subDish4id) {
		this.subDish4id = subDish4id;
	}

	public Long getSubDish5id() {
		return this.subDish5id;
	}

	public void setSubDish5id(Long subDish5id) {
		this.subDish5id = subDish5id;
	}

	public Long getSubDish6id() {
		return this.subDish6id;
	}

	public void setSubDish6id(Long subDish6id) {
		this.subDish6id = subDish6id;
	}

	public Long getVegetableId() {
		return this.vegetableId;
	}

	public void setVegetableId(Long vegetableId) {
		this.vegetableId = vegetableId;
	}

	public Long getSoupId() {
		return this.soupId;
	}

	public void setSoupId(Long soupId) {
		this.soupId = soupId;
	}

	public Long getDessertId() {
		return this.dessertId;
	}

	public void setDessertId(Long dessertId) {
		this.dessertId = dessertId;
	}

	public Long getDessert1id() {
		return this.dessert1id;
	}

	public void setDessert1id(Long dessert1id) {
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

	public Timestamp getUploadDateTime() {
		return this.uploadDateTime;
	}

	public void setUploadDateTime(Timestamp uploadDateTime) {
		this.uploadDateTime = uploadDateTime;
	}

}