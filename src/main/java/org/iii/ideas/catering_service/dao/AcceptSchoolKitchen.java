package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AcceptSchoolKitchen entity. @author MyEclipse Persistence Tools
 */
public class AcceptSchoolKitchen extends AbstractAcceptSchoolKitchen implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public AcceptSchoolKitchen() {
	}

	/** full constructor */
	public AcceptSchoolKitchen(Integer schoolId, Integer kitchenId,
			Integer quantity, String status, String action, String createUser,
			Timestamp createDateTime, String modifyUser,
			Timestamp modifyDateTime) {
		super(schoolId, kitchenId, quantity, status, action, createUser,
				createDateTime, modifyUser, modifyDateTime);
	}

}
