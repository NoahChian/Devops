package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

public class MICRate implements Serializable {
	private String countyName;
	private String haveMenu;
	private String mustMenu;
	private String rateR2;
	private String totalSchool;
	private String noMenu;
	
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getHaveMenu() {
		return haveMenu;
	}
	public void setHaveMenu(String haveMenu) {
		this.haveMenu = haveMenu;
	}
	public String getMustMenu() {
		return mustMenu;
	}
	public void setMustMenu(String mustMenu) {
		this.mustMenu = mustMenu;
	}
	public String getRateR2() {
		return rateR2;
	}
	public void setRateR2(String rateR2) {
		this.rateR2 = rateR2;
	}
	public String getTotalSchool() {
		return totalSchool;
	}
	public void setTotalSchool(String totalSchool) {
		this.totalSchool = totalSchool;
	}
	public String getNoMenu() {
		return noMenu;
	}
	public void setNoMenu(String noMenu) {
		this.noMenu = noMenu;
	}
}
