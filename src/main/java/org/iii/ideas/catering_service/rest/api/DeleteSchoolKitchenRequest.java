package org.iii.ideas.catering_service.rest.api;

public class DeleteSchoolKitchenRequest {
	private String kitchenIdSC;
	private String schoolIdSC;

	public String getKitchenIdSC() {
		return kitchenIdSC;
	}
	public void setKitchenIdSC(String kitchenIdSC) {
		this.kitchenIdSC = kitchenIdSC;
	}
	
	public String getSchoolIdSC() {
		return schoolIdSC;
	}
	public void setSchoolIdSC(String schoolIdSC) {
		this.schoolIdSC = schoolIdSC;
	}

}
