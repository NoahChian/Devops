package org.iii.ideas.catering_service.dao;

/**
 * Area entity. @author MyEclipse Persistence Tools
 */
public class SfCompany extends AbstractSfCompany implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public SfCompany() {
	}

	/** full constructor */
	public SfCompany(String name, String address, String tel, String owner) {
		super(name, address, tel, owner);
	}

}
