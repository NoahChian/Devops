package org.iii.ideas.catering_service.rest.api;

public class QueryIngredientPropertyResponse extends AbstractApiResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6465269258755721397L;
	private String schoolName;
	private String userName;
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
