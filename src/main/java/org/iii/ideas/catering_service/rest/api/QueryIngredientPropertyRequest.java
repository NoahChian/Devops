package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.SchKitchenUserContentBO;

public class QueryIngredientPropertyRequest {
	private Integer countyId;
	private Integer areaId;
	private Integer schoolId;
	private String schoolCode;
	private String schoolName;
	private String old_password;
	private String new_password;
	private Integer enable;
	private SchKitchenUserContentBO contents;
	
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
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public SchKitchenUserContentBO getContents() {
		return contents;
	}
	public void setContents(SchKitchenUserContentBO contents) {
		this.contents = contents;
	}
	public String getOld_password() {
		return old_password;
	}
	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}
	public String getNew_password() {
		return new_password;
	}
	public void setNew_password(String new_password) {
		this.new_password = new_password;
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


}
