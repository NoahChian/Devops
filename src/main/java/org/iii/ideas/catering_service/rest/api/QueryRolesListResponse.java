package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryRolesListResponse   extends AbstractApiResponse {
	private List<RoleList> roleList = new ArrayList<RoleList>();

	public List<RoleList> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleList> roleList) {
		this.roleList = roleList;
	}
}
