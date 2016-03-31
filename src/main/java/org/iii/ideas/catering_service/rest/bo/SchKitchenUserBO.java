package org.iii.ideas.catering_service.rest.bo;

public class SchKitchenUserBO {
	
	private String username;
	private Integer areaId;
	private Integer countyId;
	private String schoolName;
	private Integer schoolId;
	private String password;
	private String schoolCode;
	private String countyName;
	private String areaName;
	private Integer enable;
	private SchKitchenUserContentBO contents;
	private Boolean addAccount = false;
	private String createUser;
	
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getCountyId() {
		return countyId;
	}
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
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
	public String getSchoolCode() {
		return schoolCode;
	}
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public SchKitchenUserContentBO getContents() {
		return contents;
	}
	public void setContents(SchKitchenUserContentBO contents) {
		this.contents = contents;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Boolean getAddAccount() {
		return addAccount;
	}
	public void setAddAccount(Boolean addAccount) {
		this.addAccount = addAccount;
	}
}
