package org.iii.ideas.catering_service.rest.api;

public class MissingCaseRateRequest {
	private String inDate;
	private String SchoolType;

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getSchoolType() {
		return SchoolType;
	}

	public void setSchoolType(String schoolType) {
		SchoolType = schoolType;
	}
}
