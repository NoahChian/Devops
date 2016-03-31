package org.iii.ideas.catering_service.rest.api;

public class QueryKitchenRestaurantListRequest  extends AbstractApiResponse  {

	private String schoolId;

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

}
