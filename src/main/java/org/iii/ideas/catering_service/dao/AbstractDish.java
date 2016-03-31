package org.iii.ideas.catering_service.dao;

import java.util.List;

/**
 * AbstractDish entity provides the base persistence definition of the Dish
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractDish implements java.io.Serializable {

	// Fields

	private Long dishId;
	private Integer kitchenId;
	private String dishName;
	private String picturePath;
	private String dishShowName="";
	private List<Ingredient>  ingredient;
	// Constructors

	/** default constructor */
	public AbstractDish() {
	}

	/** old full constructor */
	public AbstractDish(Integer kitchenId, String dishName, String picturePath) {
		this.kitchenId = kitchenId;
		this.dishName = dishName;
		this.picturePath = picturePath;
	}
	
	/** full constructor
	 * add dishShowName 20140313 KC 
	 * */
	public AbstractDish(Integer kitchenId, String dishName, String picturePath,String dishShowName) {
		this.kitchenId = kitchenId;
		this.dishName = dishName;
		this.picturePath = picturePath;
		this.dishShowName=dishShowName;
	}

	// Property accessors

	public Long getDishId() {
		return this.dishId;
	}

	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}

	public Integer getKitchenId() {
		return this.kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public String getDishName() {
		return this.dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
		//暫時用，讓web舊API能自動把dishname存入 20140313 KC
		if ("".equals(this.dishShowName)){
		this.dishShowName=dishName; 
		}
	}

	public String getPicturePath() {
		return this.picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	public String getDishShowName(){
		return this.dishShowName;
	}
	public void setDishShowName(String dishShowName){
		this.dishShowName=dishShowName;
	}
	public List<Ingredient> getIngredient(){
		return this.ingredient;
	}
	
	public void setIngredient(List<Ingredient>  ingredient){
		this.ingredient=ingredient;
	}
	

}