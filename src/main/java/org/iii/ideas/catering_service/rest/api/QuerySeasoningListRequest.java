package org.iii.ideas.catering_service.rest.api;

public class QuerySeasoningListRequest {
	private String seasoningName;
	private Integer pageNum;
	private Integer pageLimit;
	private String searchDate;
	public String getSeasoningName() {
		return seasoningName;
	}
	public void setSeasoningName(String seasoningName) {
		this.seasoningName = seasoningName;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(Integer pageLimit) {
		this.pageLimit = pageLimit;
	}
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
}
