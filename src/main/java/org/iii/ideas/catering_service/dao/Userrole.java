package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * Schoolkitchen entity. @author MyEclipse Persistence Tools
 */
public class Userrole extends AbstractUserrole implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public Userrole() {
	}

	/** minimal constructor
	public Userrole(UserroleId id) {
		super(id); 
	}
 */
	/** full constructor 
	public Userrole(UserroleId id, Timestamp createDate,
			Timestamp updateTime) {
		super(id, createDate, updateTime);
	}*/
	public Userrole(String username, String roletype) {
		super(username,roletype);
	}
	public Userrole(String username, String roletype, Timestamp createDate,
			Timestamp updateTime) {
		super(username,roletype,createDate,updateTime);
	}
}
