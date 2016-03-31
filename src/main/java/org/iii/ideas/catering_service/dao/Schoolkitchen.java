package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * Schoolkitchen entity. @author MyEclipse Persistence Tools
 */
public class Schoolkitchen extends AbstractSchoolkitchen implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public Schoolkitchen() {
	}

	/** minimal constructor */
	public Schoolkitchen(SchoolkitchenId id) {
		super(id);
	}

	/** full constructor */
	public Schoolkitchen(SchoolkitchenId id, Timestamp createDate,
			Timestamp endDate) {
		super(id, createDate, endDate);
	}

}
