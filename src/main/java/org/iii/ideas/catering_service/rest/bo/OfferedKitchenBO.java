package org.iii.ideas.catering_service.rest.bo;

public class OfferedKitchenBO {
	private Integer kitchenId;
	private Integer schoolId;
	private String companyId ;
	private String kitchenName;
	private String LastOfferedDate;
	
	public Integer getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	public String getLastOfferedDate() {
		return LastOfferedDate;
	}
	public void setLastOfferedDate(String lastOfferedDate) {
		LastOfferedDate = lastOfferedDate;
	}
	
}
