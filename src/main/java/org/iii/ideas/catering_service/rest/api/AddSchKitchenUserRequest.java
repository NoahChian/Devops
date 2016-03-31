package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.SchKitchenUserContentBO;

public class AddSchKitchenUserRequest {
	private Integer countyId;
	private Integer areaId;
	private String schoolId;
	private String schoolCode;
	private String schoolName;
	private String password;
	private Integer enable;
	private SchKitchenUserContentBO contents;
	private Boolean addAccount = false;
	
	public Integer getCountyId() {
		return countyId;
	}
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public SchKitchenUserContentBO getContents() {
		return contents;
	}
	public void setContents(SchKitchenUserContentBO contents) {
		this.contents = contents;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolCode() {
		return schoolCode;
	}
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	public Boolean getAddAccount() {
		return addAccount;
	}
	public void setAddAccount(Boolean addAccount) {
		this.addAccount = addAccount;
	}


}
