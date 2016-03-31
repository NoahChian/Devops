package org.iii.ideas.catering_service.dao;

/**
 * SupplierId entity. @author MyEclipse Persistence Tools
 */
public class SupplierId extends AbstractSupplierId implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public SupplierId() {
	}

	/** full constructor */
	public SupplierId(Integer supplierId, Integer kitchenId) {
		super(supplierId, kitchenId);
	}

}
