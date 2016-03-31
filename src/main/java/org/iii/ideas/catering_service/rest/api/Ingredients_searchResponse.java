package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.rest.bo.Ingredients_searchBO;
import org.json.JSONArray;
import org.json.JSONObject;

public class Ingredients_searchResponse extends AbstractApiResponse {
	private static final long serialVersionUID = -1904135875588581166L;
	private List<Ingredients_searchBO> result = new ArrayList<Ingredients_searchBO>();

	public List<Ingredients_searchBO> getResult() {
		return result;
	}

	public void setResult(List<Ingredients_searchBO> result) {
		this.result = result;
	}
}
