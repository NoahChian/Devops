package org.iii.ideas.catering_service.dao;

/**
 * School entity. @author MyEclipse Persistence Tools
 */
public class UserSchool implements java.io.Serializable {

	private String username;
	private Integer schoolId;
	
	public static final String USER_NAME = "username";	//String
	public static final String SCHOOL_ID = "schoolId";	//String
	
	/** default constructor */
	public UserSchool() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
}
