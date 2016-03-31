package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.Groups;

public class QueryGroupListResponse   extends AbstractApiResponse {
	private List <Groups> group = new ArrayList<Groups>();

	public List<Groups> getGroup() {
		return group;
	}

	public void setGroup(List<Groups> group) {
		this.group = group;
	}
}
