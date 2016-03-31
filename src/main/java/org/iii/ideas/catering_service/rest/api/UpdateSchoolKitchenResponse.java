package org.iii.ideas.catering_service.rest.api;
public class UpdateSchoolKitchenResponse extends AbstractApiResponse {
	private String schoolCode;
	private Integer acceptschoolkitchenId;
	private String schoolName;

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public Integer getAcceptschoolkitchenId() {
		return acceptschoolkitchenId;
	}

	public void setAcceptschoolkitchenId(Integer acceptschoolkitchenId) {
		this.acceptschoolkitchenId = acceptschoolkitchenId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
}
