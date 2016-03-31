package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.SchKitchenUserContentBO;

public class QuerySchKitchenUserDetailResponse extends AbstractApiResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6235942308542861759L;
	private Integer countyId;
	private Integer areaId;
	private Integer schoolId;
	private String schoolCode;//20140514 Raymond
	private String schoolName;
	private String password;
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
	public String getSchoolCode() {
		return schoolCode;
	}
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
}
