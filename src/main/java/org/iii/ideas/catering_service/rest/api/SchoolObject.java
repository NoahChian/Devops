package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

class SchoolObject  implements Serializable {
	private int sid;
	private String schoolCode;//20140514 Raymond
	private String schoolName;
	private Integer enable;
	private int quantity=1;
	private boolean offered;
	
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public String getSchoolCode() {
		return schoolCode;
	}
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public boolean getOffered() {
		return offered;
	}
	public void setOffered(boolean offered) {
		this.offered = offered;
	}
}