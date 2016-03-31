package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.Navi;

public class QueryNaviAuthorityResponse extends AbstractApiResponse{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1904135875588581166L;

	private List<Navi> menuTree = new ArrayList<Navi>();
	
	private String userType;
	
	private String username;

	public List<Navi> getMenuTree() {
		return menuTree;
	}

	public void setMenuTree(List<Navi> menuTree) {
		this.menuTree = menuTree;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}



}
