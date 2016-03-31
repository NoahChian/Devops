package org.iii.ideas.catering_service.dao;

/**
 * AbstractSchool entity provides the base persistence definition of the School
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractSchool implements java.io.Serializable {

	// Fields

	private Integer schoolId;
	private String schoolName;
	private Integer countyId;
	private Integer areaId;
	private Integer enable;
	private String schoolCode;
	private Integer schoolType;

	// Constructors

	/** default constructor */
	public AbstractSchool() {
	}

	/** old- full constructor */
	public AbstractSchool(Integer schoolId, String schoolName, Integer countyId, Integer areaId, Integer enable) {
		this.schoolId = schoolId;
		this.schoolName = schoolName;
		this.countyId = countyId;
		this.areaId = areaId;
		this.enable = enable;
	}

	/** full constructor 20140603 KC */
	public AbstractSchool(Integer schoolId, String schoolName, Integer countyId, Integer areaId, Integer enable, Integer schoolType) {
		this.schoolId = schoolId;
		this.schoolName = schoolName;
		this.countyId = countyId;
		this.areaId = areaId;
		this.enable = enable;
		this.schoolType = schoolType;
	}

	// Property accessors

	public Integer getSchoolId() {
		return this.schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return this.schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Integer getCountyId() {
		return this.countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public Integer getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getEnable() {
		return this.enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public String getSchoolCode() {
		return this.schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public Integer getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(Integer schoolType) {
		this.schoolType = schoolType;
	}

	

}