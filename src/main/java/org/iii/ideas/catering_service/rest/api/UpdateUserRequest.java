package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class UpdateUserRequest {
	private String username;
	private String password;
	private String ori_password;
	private String usertype;
	private String name;
	private String email;
	//private List<String> roleList = new ArrayList<String>();
	private String roleList ;
	private String kitchenId;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOri_password() {
		return ori_password;
	}
	public void setOri_password(String ori_password) {
		this.ori_password = ori_password;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
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
	/*
	public List<String> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}*/

	public String getRoleList() {
		return roleList;
	}
	public void setRoleList(String roleList) {
		this.roleList = roleList;
	}
	public String getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(String kitchenId) {
		this.kitchenId = kitchenId;
	}
}
