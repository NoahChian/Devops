package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class UpdateKitchenRequest {
	private int kitchenId;
	private String kitchenName;
	private String tel;
	private String companyId;
	private String email;
	private String kitchenType;
	private String address;
	private String ownner;
	private String fax;
	private String nutritionist;
	private String chef;
	private String qualifier;
	private String haccp;
	private String insurement;
	private String manager;
 	private String manageremail;
	//private List <SchoolObject>school = new ArrayList<SchoolObject>();
	
	public int getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(int kitchenId) {
		this.kitchenId = kitchenId;
	}
	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getKitchenType() {
		return kitchenType;
	}
	public void setKitchenType(String kitchenType) {
		this.kitchenType = kitchenType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOwnner() {
		return ownner;
	}
	public void setOwnner(String ownner) {
		this.ownner = ownner;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getNutritionist() {
		return nutritionist;
	}
	public void setNutritionist(String nutritionist) {
		this.nutritionist = nutritionist;
	}
	public String getChef() {
		return chef;
	}
	public void setChef(String chef) {
		this.chef = chef;
	}
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	public String getHaccp() {
		return haccp;
	}
	public void setHaccp(String haccp) {
		this.haccp = haccp;
	}
	public String getInsurement() {
		return insurement;
	}
	public void setInsurement(String insurement) {
		this.insurement = insurement;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getManageremail() {
		return manageremail;
	}
	public void setManageremail(String manageremail) {
		this.manageremail = manageremail;
	}
//	public List <SchoolObject> getSchool() {
//		return school;
//	}
//	public void setSchool(List <SchoolObject> school) {
//		this.school = school;
//	}
}
