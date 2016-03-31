package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

public class EduQueryMenuDetailInfoRequest  implements Serializable {
	private Long mid;
	private String requestAccount;
	private String token;
	
	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}



	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRequestAccount() {
		return requestAccount;
	}

	public void setRequestAccount(String requestAccount) {
		this.requestAccount = requestAccount;
	}

	
}
