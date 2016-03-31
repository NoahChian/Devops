package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * NegIngredient entity. @author MyEclipse Persistence Tools
 */
public class NegIngredient extends AbstractNegIngredient implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public NegIngredient() {
	}

	/** minimal constructor */
	public NegIngredient(NegIngredientId id, Integer supplierId) {
		super(id, supplierId);
	}

	/** full constructor */
	public NegIngredient(NegIngredientId id, Integer supplierId,
			Timestamp begDate, Timestamp endDate, Timestamp createtime,
			Timestamp manufactureDate, Timestamp expirationDate,
			String lotNumber, String description) {
		super(id, supplierId, begDate, endDate, createtime, manufactureDate,
				expirationDate, lotNumber, description);
	}

}
