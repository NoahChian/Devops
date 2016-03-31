package org.iii.ideas.catering_service.dao;

/**
 * AbstractCounty entity provides the base persistence definition of the County
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCounty implements java.io.Serializable {

	// Fields

	private Integer countyId;
	private String county;
	private Integer enable;

	// Constructors

	/** default constructor */
	public AbstractCounty() {
	}

	/** full constructor */
	public AbstractCounty(String county, Integer enable,Integer countyId) {
		this.county = county;
		this.enable = enable;
		this.countyId = countyId;
	}

	// Property accessors

	public Integer getCountyId() {
		return this.countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public String getCounty() {
		return this.county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Integer getEnable() {
		return this.enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

}