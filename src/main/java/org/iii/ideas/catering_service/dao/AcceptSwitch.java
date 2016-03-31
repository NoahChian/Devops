package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AcceptSwitch entity. @author MyEclipse Persistence Tools
 */
public class AcceptSwitch extends AbstractAcceptSwitch implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public AcceptSwitch() {
	}

	/** minimal constructor */
	public AcceptSwitch(Integer schoolId, String acceptType, Integer status,
			Timestamp createDate, Timestamp modifyDate) {
		super(schoolId, acceptType, status, createDate, modifyDate);
	}

	/** full constructor */
	public AcceptSwitch(Integer schoolId, String acceptType, Integer status,
			String createUser, Timestamp createDate, String modifyUser,
			Timestamp modifyDate) {
		super(schoolId, acceptType, status, createUser, createDate, modifyUser,
				modifyDate);
	}

}
