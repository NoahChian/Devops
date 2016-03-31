package org.iii.ideas.catering_service.dao;

/**
 * AbstractSupplierId entity provides the base persistence definition of the
 * SupplierId entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractSupplierId implements java.io.Serializable {

	// Fields

	private Integer supplierId;
	private Integer kitchenId;

	// Constructors

	/** default constructor */
	public AbstractSupplierId() {
	}

	/** full constructor */
	public AbstractSupplierId(Integer supplierId, Integer kitchenId) {
		this.supplierId = supplierId;
		this.kitchenId = kitchenId;
	}

	// Property accessors

	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getKitchenId() {
		return this.kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractSupplierId))
			return false;
		AbstractSupplierId castOther = (AbstractSupplierId) other;

		return ((this.getSupplierId() == castOther.getSupplierId()) || (this
				.getSupplierId() != null && castOther.getSupplierId() != null && this
				.getSupplierId().equals(castOther.getSupplierId())))
				&& ((this.getKitchenId() == castOther.getKitchenId()) || (this
						.getKitchenId() != null
						&& castOther.getKitchenId() != null && this
						.getKitchenId().equals(castOther.getKitchenId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getSupplierId() == null ? 0 : this.getSupplierId()
						.hashCode());
		result = 37 * result
				+ (getKitchenId() == null ? 0 : this.getKitchenId().hashCode());
		return result;
	}

}