package org.iii.ideas.catering_service.dao;

/**
 * County entity. @author MyEclipse Persistence Tools
 */
public class County extends AbstractCounty implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public County() {
	}

	/** full constructor */
	public County(String county, Integer enable, Integer countyId) {
		super(county, enable, countyId);
	}

}
