package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KitchenList  implements Serializable {
	private int kitchenId;
	private String kitchenName;
	private String tel;
	private String companyId;
	private String email;
	private List<String> schoolList = new ArrayList<String>();
	private Integer enable ;
	
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
	public List<String> getSchoolList() {
		return schoolList;
	}
	public void setSchoolList(List<String> schools) {
		this.schoolList = schools;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
}
