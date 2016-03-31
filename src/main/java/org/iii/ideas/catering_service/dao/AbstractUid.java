package org.iii.ideas.catering_service.dao;

/**
 * AbstractUid entity provides the base persistence definition of the Uid
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractUid implements java.io.Serializable {

	// Fields

	private Integer uid;

	// Constructors

	/** default constructor */
	public AbstractUid() {
	}

	/** full constructor */
	public AbstractUid(Integer uid) {
		this.uid = uid;
	}

	// Property accessors

	public Integer getUid() {
		return this.uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

}