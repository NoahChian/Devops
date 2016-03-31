package org.iii.ideas.catering_service.rest.bo;

public class SchKitchenUserContentBO {
	
	private String email;//Email
	private String role;//帳號角色
	private String type;//帳號型態
	

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
