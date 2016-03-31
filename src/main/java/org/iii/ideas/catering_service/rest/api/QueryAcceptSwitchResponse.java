package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.rest.bo.AcceptSwitchBO;

public class QueryAcceptSwitchResponse extends AbstractApiResponse {
	private static final long serialVersionUID = -1904135875588581166L;

	private List<AcceptSwitchBO> acceptswitchList = new ArrayList<AcceptSwitchBO>();

	public List<AcceptSwitchBO> getAcceptswitchList() {
		return acceptswitchList;
	}

	public void setAcceptswitchList(List<AcceptSwitchBO> acceptswitchList) {
		this.acceptswitchList = acceptswitchList;
	}

}
