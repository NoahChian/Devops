package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class CustomerQuerySchoolResponse   extends AbstractApiResponse {
	private List <SchoolObject>school = new ArrayList<SchoolObject>();
	
	public List <SchoolObject > getSchool() {
		return school;
	}
	public void setSchool(List <SchoolObject > school) {
		this.school = school;
	}
}
