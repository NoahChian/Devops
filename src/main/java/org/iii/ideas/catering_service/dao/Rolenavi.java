package org.iii.ideas.catering_service.dao;

/**
 * Rolenavi entity. @author MyEclipse Persistence Tools
 */

public class Rolenavi implements java.io.Serializable {
	
	// Fields

	private RolenaviId id;

	// Constructors

	/** default constructor */
	public Rolenavi() {
	}

	/** full constructor */
	public Rolenavi(RolenaviId id) {
		this.id = id;
	}

	// Property accessors

	public RolenaviId getId() {
		return this.id;
	}

	public void setId(RolenaviId id) {
		this.id = id;
	}

}