package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.SeasoningStockData;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.rest.bo.SeasoningStockDataBO;

public class QuerySeasoningDetailResponse extends AbstractApiResponse{
	private SeasoningStockDataBO seasoningData ;
	private IngredientAttributeBO igredientAttrBO;

	public SeasoningStockDataBO getSeasoningData() {
		return seasoningData;
	}

	public void setSeasoningData(SeasoningStockDataBO seasoningData) {
		this.seasoningData = seasoningData;
	}

	public IngredientAttributeBO getIgredientAttrBO() {
		return igredientAttrBO;
	}

	public void setIgredientAttrBO(IngredientAttributeBO igredientAttrBO) {
		this.igredientAttrBO = igredientAttrBO;
	}
	

}
