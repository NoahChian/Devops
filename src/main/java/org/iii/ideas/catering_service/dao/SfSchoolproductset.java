package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * SfSchoolproductset entity. @author MyEclipse Persistence Tools
 */
public class SfSchoolproductset extends AbstractSfSchoolproductset implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public SfSchoolproductset() {
	}

	/** minimal constructor */
	public SfSchoolproductset(Integer schoolId, Long productId,
			Timestamp onShelfDate, String status, String createUser,
			Timestamp createDateTime, String modifyUser,
			Timestamp modifyDateTime) {
		super(schoolId, productId, onShelfDate, status, createUser,
				createDateTime, modifyUser, modifyDateTime);
	}

	/** full constructor */
	public SfSchoolproductset(Integer schoolId, Long productId,
			Timestamp onShelfDate, Timestamp offShelfDate, String status,
			String createUser, Timestamp createDateTime, String modifyUser,
			Timestamp modifyDateTime) {
		super(schoolId, productId, onShelfDate, offShelfDate, status,
				createUser, createDateTime, modifyUser, modifyDateTime);
	}
}