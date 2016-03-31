package org.iii.ideas.catering_service.rest.api;

public class QueryCountiesRequest {
	private int condition;
	private String schoolId;
	
	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}
	
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
}
