package org.iii.ideas.catering_service.rest.api;

public class QueryAcceptSwitchRequest {
	private Integer schoolId;
	private String acceptType;

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public String getAcceptType() {
		return acceptType;
	}

	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}

}
