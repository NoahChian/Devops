package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryManageListResponse  extends AbstractApiResponse {
	private List<List<String>> returnList = new ArrayList<List<String>>();

	public List<List<String>> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<List<String>> returnList) {
		this.returnList = returnList;
	}
}
