package org.iii.ideas.catering_service.rest.api;


public class UpdateResetUserPasswordRequest extends AbstractApiResponse{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8933595701375445279L;
	private String username;
	private String email;
	private String ts;
	private String actType;
	private String password;
	private String valPassword;
	
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
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getActType() {
		return actType;
	}
	public void setActType(String actType) {
		this.actType = actType;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getValPassword() {
		return valPassword;
	}
	public void setValPassword(String valPassword) {
		this.valPassword = valPassword;
	}

}
