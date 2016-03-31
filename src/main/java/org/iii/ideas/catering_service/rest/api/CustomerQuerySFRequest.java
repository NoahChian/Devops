package org.iii.ideas.catering_service.rest.api;

public class CustomerQuerySFRequest {
	private int pageNum;
	private int pageLimit;
	private int sid;
	private String onShelfDate;
	private String offShelfDate;
	
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getOnShelfDate() {
		return onShelfDate;
	}
	public void setOnShelfDate(String onShelfDate) {
		this.onShelfDate = onShelfDate;
	}
	public String getOffShelfDate() {
		return offShelfDate;
	}
	public void setOffShelfDate(String offShelfDate) {
		this.offShelfDate = offShelfDate;
	}
	
}
