package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QuerySeasoningDetailv2Response extends AbstractApiResponse{
	private int seasoningsCount;
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

}
