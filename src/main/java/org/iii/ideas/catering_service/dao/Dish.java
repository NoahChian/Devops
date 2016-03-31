package org.iii.ideas.catering_service.dao;

/**
 * Dish entity. @author MyEclipse Persistence Tools
 */
public class Dish extends AbstractDish implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Dish() {
	}

	/** old full constructor */
	public Dish(Integer kitchenId, String dishName, String picturePath) {
		super(kitchenId, dishName, picturePath);
	}
	
	/** full constructor
	 * add dishShowName 20140313 KC 
	 * */
	public Dish(Integer kitchenId, String dishName, String picturePath,String dishShowName) {
		super(kitchenId, dishName, picturePath,dishShowName);
	}

}
