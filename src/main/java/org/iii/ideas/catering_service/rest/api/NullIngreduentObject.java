package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;


public class NullIngreduentObject  implements Serializable {
	
	private String schoolName;
	private String date;
	private String nullIngreduents;
	private String kitchenName;
	
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNullIngreduents() {
		return nullIngreduents;
	}
	public void setNullIngreduents(String nullIngreduents) {
		this.nullIngreduents = nullIngreduents;
	}
	public String getKitchenName() {
		return this.kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	
}
