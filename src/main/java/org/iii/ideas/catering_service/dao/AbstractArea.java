package org.iii.ideas.catering_service.dao;

/**
 * AbstractArea entity provides the base persistence definition of the Area
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractArea implements java.io.Serializable {

	// Fields

	private Integer areaId;
	private Integer countyId;
	private String area;

	// Constructors

	/** default constructor */
	public AbstractArea() {
	}

	/** full constructor */
	public AbstractArea(Integer countyId, String area) {
		this.countyId = countyId;
		this.area = area;
	}

	// Property accessors

	public Integer getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getCountyId() {
		return this.countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

}