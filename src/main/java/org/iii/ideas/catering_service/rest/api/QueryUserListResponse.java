package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryUserListResponse   extends AbstractApiResponse{
	private List <UserList>userList = new ArrayList<UserList>();

	public List <UserList> getUserList() {
		return userList;
	}

	public void setUserList(List <UserList> userList) {
		this.userList = userList;
	}

}

