package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class UpdateSchoolSeasoningDetailRequest {
	private int seasoningsCount;
	private String  menuDate;
	private Integer schoolId;
	private List<IngredientObject> seasonings = new ArrayList<IngredientObject>();
	public int getSeasoningsCount() {
		return seasoningsCount;
	}
	public void setSeasoningsCount(int seasoningsCount) {
		this.seasoningsCount = seasoningsCount;
	}
	public List<IngredientObject> getSeasonings() {
		return seasonings;
	}
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
