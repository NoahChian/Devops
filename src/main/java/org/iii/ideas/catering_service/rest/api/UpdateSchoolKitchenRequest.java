package org.iii.ideas.catering_service.rest.api;

public class UpdateSchoolKitchenRequest {
	private Integer kitchenIdSC;
	private Integer schoolIdSC;
	private Integer skQuantity;
	private String act;
	private boolean offered;
	
	
	public Integer getKitchenIdSC() {
		return kitchenIdSC;
	}
	public void setKitchenIdSC(Integer kitchenIdSC) {
		this.kitchenIdSC = kitchenIdSC;
	}
	
	public Integer getSchoolIdSC() {
		return schoolIdSC;
	}
	public void setSchoolIdSC(Integer schoolIdSC) {
		this.schoolIdSC = schoolIdSC;
	}

	public Integer getSkQuantity(){
		return this.skQuantity;
	}
	public void setSkQuantity(Integer skQuantity){
		this.skQuantity=skQuantity;
	}
	
	public String getAct(){
		return this.act;
	}
	public void setAct(String act){
		this.act=act;
	}
	public boolean getOffered() {
		return offered;
	}
	public void setOffered(boolean offered) {
		this.offered = offered;
	}
	
	
}
