package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractNegIngredientId entity provides the base persistence definition of
 * the NegIngredientId entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractNegIngredientId implements java.io.Serializable {

	// Fields

	private Integer negIngredientId;
	private Timestamp stockDate;

	// Constructors

	/** default constructor */
	public AbstractNegIngredientId() {
	}

	/** full constructor */
	public AbstractNegIngredientId(Integer negIngredientId, Timestamp stockDate) {
		this.negIngredientId = negIngredientId;
		this.stockDate = stockDate;
	}

	// Property accessors

	public Integer getNegIngredientId() {
		return this.negIngredientId;
	}

	public void setNegIngredientId(Integer negIngredientId) {
		this.negIngredientId = negIngredientId;
	}

	public Timestamp getStockDate() {
		return this.stockDate;
	}

	public void setStockDate(Timestamp stockDate) {
		this.stockDate = stockDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractNegIngredientId))
			return false;
		AbstractNegIngredientId castOther = (AbstractNegIngredientId) other;

		return ((this.getNegIngredientId() == castOther.getNegIngredientId()) || (this
				.getNegIngredientId() != null
				&& castOther.getNegIngredientId() != null && this
				.getNegIngredientId().equals(castOther.getNegIngredientId())))
				&& ((this.getStockDate() == castOther.getStockDate()) || (this
						.getStockDate() != null
						&& castOther.getStockDate() != null && this
						.getStockDate().equals(castOther.getStockDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getNegIngredientId() == null ? 0 : this.getNegIngredientId()
						.hashCode());
		result = 37 * result
				+ (getStockDate() == null ? 0 : this.getStockDate().hashCode());
		return result;
	}

}