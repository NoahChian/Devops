package org.iii.ideas.catering_service.dao;

/**
 * Useraccount entity. @author MyEclipse Persistence Tools
 */
public class Useraccount extends AbstractUseraccount implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public Useraccount() {
	}

	/** minimal constructor */
	public Useraccount(String username, String password, String usertype) {
		super(username, password, usertype);
	}

	/** full constructor */
	public Useraccount(String username, String password, String usertype,
			Integer kitchenId) {
		super(username, password, usertype, kitchenId);
	}
	/** full-Ric,20140310 constructor */
	public Useraccount(String username, String password, String usertype,
			Integer kitchenId, String name, String email) {
		super(username, password, usertype, kitchenId, name, email);
	}

}
