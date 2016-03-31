package org.iii.ideas.catering_service.dao;

/**
 * SchoolkitchenId entity. @author MyEclipse Persistence Tools
 */
public class SchoolkitchenId extends AbstractSchoolkitchenId implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public SchoolkitchenId() {
	}

	/** full constructor */
	public SchoolkitchenId(Integer schoolId, Integer kitchenId) {
		super(schoolId, kitchenId);
	}

}
