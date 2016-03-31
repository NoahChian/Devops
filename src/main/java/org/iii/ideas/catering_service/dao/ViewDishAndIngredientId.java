package org.iii.ideas.catering_service.dao;

/**
 * SchoolkitchenId entity. @author MyEclipse Persistence Tools
 */

public class ViewDishAndIngredientId implements java.io.Serializable {
	// Constructors
	private Long dishbatchdataid;
	private Long ingredientBatchId;

	// Constructors

	/** default constructor */
	public ViewDishAndIngredientId() {
	}

	/** minimal constructor */
	public ViewDishAndIngredientId(Long dishbatchdataid,Long ingredientBatchId) {
		this.setDishbatchdataid(dishbatchdataid);
		this.setIngredientBatchId(ingredientBatchId);
	}

	public Long getDishbatchdataid() {
		return dishbatchdataid;
	}

	public void setDishbatchdataid(Long dishbatchdataid) {
		this.dishbatchdataid = dishbatchdataid;
	}

	public Long getIngredientBatchId() {
		return ingredientBatchId;
	}

	public void setIngredientBatchId(Long ingredientBatchId) {
		this.ingredientBatchId = ingredientBatchId;
	}


}
