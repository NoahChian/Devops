package org.iii.ideas.catering_service.rest.api;

public class QuerySchoolIngredientListByDateRequest  extends AbstractApiResponse  {
	private String menuDate="";
	private Integer schoolId;

	public String getMenuDate() {
		return menuDate;
	}

	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	
}
