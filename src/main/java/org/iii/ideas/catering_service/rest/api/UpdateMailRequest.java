package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class UpdateMailRequest {
	private String username;
	private String usertype;
	private String email;

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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
