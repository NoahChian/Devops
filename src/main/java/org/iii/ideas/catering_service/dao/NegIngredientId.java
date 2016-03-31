package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * NegIngredientId entity. @author MyEclipse Persistence Tools
 */
public class NegIngredientId extends AbstractNegIngredientId implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public NegIngredientId() {
	}

	/** full constructor */
	public NegIngredientId(Integer negIngredientId, Timestamp stockDate) {
		super(negIngredientId, stockDate);
	}

}
