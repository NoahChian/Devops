package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;


public class NullIngreduentDataList  implements Serializable {
	
	/**
	 * 20140612 Ric
	 * 有菜單無食材
	 */
	private static final long serialVersionUID = 843076116822746209L;
	private String schoolName;
	private String date;
	private String dishName;
	private String nullIngreduents;
	
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
	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	public String getNullIngreduents() {
		return nullIngreduents;
	}
	public void setNullIngreduents(String nullIngreduents) {
		this.nullIngreduents = nullIngreduents;
	}
}
