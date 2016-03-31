package org.iii.ideas.catering_service.rest.api;


public class AddApplyForgotPasswordRequest extends AbstractApiResponse{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8933595701375445279L;
	private String username;
	private String email;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
