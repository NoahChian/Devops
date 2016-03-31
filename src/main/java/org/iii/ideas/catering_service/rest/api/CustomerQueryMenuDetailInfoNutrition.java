package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

/*
 * "mainFood":1,
            "vegetable":2,
            "oil":1,
            "MeatBeans":2,
            "fruit":0,
            "milk":0,
            "calories":360

 */
public class CustomerQueryMenuDetailInfoNutrition  implements Serializable {
	private String mainFood;
	private String vegetable;
	private String oil;
	private String meatBeans;
	private String milk;
	private String calories;
	private String fruit;
	public String getMainFood() {
		return mainFood;
	}
	public void setMainFood(String mainFood) {
		this.mainFood = mainFood;
	}
	public String getVegetable() {
		return vegetable;
	}
	public void setVegetable(String vegetable) {
		this.vegetable = vegetable;
	}
	public String getOil() {
		return oil;
	}
	public void setOil(String oil) {
		this.oil = oil;
	}
	public String getMeatBeans() {
		return meatBeans;
	}
	public void setMeatBeans(String meatBeans) {
		this.meatBeans = meatBeans;
	}
	public String getMilk() {
		return milk;
	}
	public void setMilk(String milk) {
		this.milk = milk;
	}
	public String getCalories() {
		return calories;
	}
	public void setCalories(String calories) {
		this.calories = calories;
	}
	public String getFruit() {
		return fruit;
	}
	public void setFruit(String fruit) {
		this.fruit = fruit;
	}
	

}
