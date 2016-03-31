package org.iii.ideas.catering_service.dao;

/**
 * School entity. @author MyEclipse Persistence Tools
 */
public class School extends AbstractSchool implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public School() {
	}

	/** old- full constructor */
	public School(Integer schoolId, String schoolName, Integer countyId, Integer areaId,
			Integer enable) {
		super(schoolId, schoolName, countyId, areaId, enable);
	}
	/** full constructor 20140603 KC */
	public School(Integer schoolId, String schoolName, Integer countyId, Integer areaId,
			Integer enable,Integer schoolType) {
		super(schoolId, schoolName, countyId, areaId, enable,schoolType);
	} 
}
