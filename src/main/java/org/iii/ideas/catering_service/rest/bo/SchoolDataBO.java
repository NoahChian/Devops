package org.iii.ideas.catering_service.rest.bo;

import java.util.ArrayList;
import java.util.List;

public class SchoolDataBO {
	private Integer schoolId;
	private String schoolCode;
	private String schoolName;
	private Integer enable;
	private String kitchenTypeOfSchool;
	private Integer offered;
	
	private List<KitchenBO> kitchens=new ArrayList<KitchenBO>();
	
	public void setSchoolId(Integer schoolId){
		this.schoolId=schoolId;
	}
	public Integer getSchoolId(){
		return this.schoolId;
	}
	
	public void setSchoolCode(String schoolCode){
		this.schoolCode=schoolCode;
	}
	public String getSchoolCode(){
		return this.schoolCode;
	}
	
	public void setSchoolName(String schoolName){
		this.schoolName=schoolName;
	}
	public String getSchoolName(){
		return this.schoolName;
	}
	
	public void setEnable(Integer enable){
		this.enable=enable;
	}
	public Integer getEnable(){
		return this.enable ;
	}
	public void setKitchens( List<KitchenBO> kitchens){
		this.kitchens=kitchens;
	}
	public  List<KitchenBO> getKitchens(){
		return this.kitchens;
	}
	public String getKitchenTypeOfSchool(){
		return this.kitchenTypeOfSchool;
	}
	public void setKitchenTypeOfSchool(String kitchenTypeOfSchool){
		this.kitchenTypeOfSchool=kitchenTypeOfSchool;
	}
	public Integer getOffered() {
		return offered;
	}
	public void setOffered(Integer offered) {
		this.offered = offered;
	}
}