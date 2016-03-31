package org.iii.ideas.catering_service.dao;

/**
 * Area entity. @author MyEclipse Persistence Tools
 */
public class SfCategory extends AbstractSfCategory implements java.io.Serializable {

	// Constructors
	/** default constructor */
	public SfCategory() {
	}

	/** full constructor */
	public SfCategory(String name) {
		super(name);
	}
	public SfCategory(long id, String name) {
		super(id, name);
	}
}
