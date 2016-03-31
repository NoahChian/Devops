package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

public class RateDetail implements Serializable{
	private String schoolName;
	private String kitchenName;
	private String Ownner;
	private String email;
	private String tel;
	
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	public String getOwnner() {
		return Ownner;
	}
	public void setOwnner(String ownner) {
		Ownner = ownner;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
}
