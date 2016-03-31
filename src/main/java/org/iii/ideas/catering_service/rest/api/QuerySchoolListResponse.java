package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QuerySchoolListResponse   extends AbstractApiResponse {
	private List <SchoolObject>school = new ArrayList<SchoolObject>();
	private Integer totalCol;
	
	public List <SchoolObject > getSchool() {
		return school;
	}
	public void setSchool(List <SchoolObject > school) {
		this.school = school;
	}
	public Integer getTotalCol() {
		return totalCol;
	}
	public void setTotalCol(Integer totalCol) {
		this.totalCol = totalCol;
	}
}
