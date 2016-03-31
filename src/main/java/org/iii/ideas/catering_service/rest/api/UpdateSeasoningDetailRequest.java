package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class UpdateSeasoningDetailRequest {
	private int seasoningsCount;
	private Long mid;
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
	public Long getMid() {
		return mid;
	}
	public void setMid(Long mid) {
		this.mid = mid;
	}
}
