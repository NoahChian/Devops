package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryMissingCaseByCountyResponse  extends AbstractApiResponse{
	
	/**
	 * 
	 */
	private String queryDate;
	private List <MissingCaseByCountyList>missingCaseByCountyList = new ArrayList<MissingCaseByCountyList>();
	public List<MissingCaseByCountyList> getMissingCaseByCountyList() {
		return missingCaseByCountyList;
	}
	public void setMissingCaseByCountyList(List<MissingCaseByCountyList> missingCaseByCountyList) {
		this.missingCaseByCountyList = missingCaseByCountyList;
	}
	public String getQueryDate() {
		return queryDate;
	}
	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}
	
}
