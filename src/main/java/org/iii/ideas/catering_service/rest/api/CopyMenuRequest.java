package org.iii.ideas.catering_service.rest.api;

public class CopyMenuRequest {
	private int sidOld;
	private String startDateOld;
	private String endDateOld;
	private int sidNew;
	private String startDateNew;
	private String endDateNew;
	public int getSidOld() {
		return sidOld;
	}
	public void setSidOld(int sidOld) {
		this.sidOld = sidOld;
	}
	public String getStartDateOld() {
		return startDateOld;
	}
	public void setStartDateOld(String startDateOld) {
		this.startDateOld = startDateOld;
	}
	public String getEndDateOld() {
		return endDateOld;
	}
	public void setEndDateOld(String endDateOld) {
		this.endDateOld = endDateOld;
	}
	public int getSidNew() {
		return sidNew;
	}
	public void setSidNew(int sidNew) {
		this.sidNew = sidNew;
	}
	public String getStartDateNew() {
		return startDateNew;
	}
	public void setStartDateNew(String startDateNew) {
		this.startDateNew = startDateNew;
	}
	public String getEndDateNew() {
		return endDateNew;
	}
	public void setEndDateNew(String endDateNew) {
		this.endDateNew = endDateNew;
	}
}
