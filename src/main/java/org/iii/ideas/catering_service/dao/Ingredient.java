package org.iii.ideas.catering_service.dao;

/**
 * Ingredient entity. @author MyEclipse Persistence Tools
 */
public class Ingredient extends AbstractIngredient implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public Ingredient() {
	}

	/** full constructor */
	public Ingredient(Long dishId, Integer supplierId,
			String ingredientName, String brand, String supplierCompanyId) {
		super(dishId, supplierId, ingredientName, brand,supplierCompanyId);
	}

}
