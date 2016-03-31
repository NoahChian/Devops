package org.iii.ideas.catering_service.rest.api;

public class CustomerQueryKitchenBySchoolAndDateRequest {
	private Integer sid;
	private String date;
	private Integer mtype;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getMtype() {
		return mtype;
	}
	public void setMtype(Integer mtype) {
		this.mtype = mtype;
	}
}
