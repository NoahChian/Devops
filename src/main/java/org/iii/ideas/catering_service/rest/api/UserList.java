package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.Kitchen;

public class UserList  implements Serializable {
	private String username;
	private String usertype;
	private String usertypename;
	private String name;
	private String email;
	private Integer enable;
	private List<String> roleList = new ArrayList<String>();
	private String kitchenName;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getUsertypename() {
		return usertypename;
	}
	public void setUsertypename(String usertypename) {
		this.usertypename = usertypename;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public List<String> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}
	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
}
