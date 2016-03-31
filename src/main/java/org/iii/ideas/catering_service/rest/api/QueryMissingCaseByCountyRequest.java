package org.iii.ideas.catering_service.rest.api;

public class QueryMissingCaseByCountyRequest {
	private String queryDate;
	private String user_Name;

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

	public String getUser_Name() {
		return user_Name;
	}

	public void setUser_Name(String user_Name) {
		this.user_Name = user_Name;
	}
}
