package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;


public class NullSeasoningList  implements Serializable {
	
	/**
	 * 20140612 Ric
	 * 無調味料
	 */
	private static final long serialVersionUID = 1792021195107207350L;
	private String date;
	private String schoolName;

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
}
