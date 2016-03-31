package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.rest.bo.AcceptschoolkitchenBO;

public class QueryHalfYearAcceptSchoolKitchenResponse extends AbstractApiResponse {
	private static final long serialVersionUID = -1904135875588581166L;
	private List<AcceptschoolkitchenBO> halfyearacceptschoolkitchenList = new ArrayList<AcceptschoolkitchenBO>();

	public List<AcceptschoolkitchenBO> getHalfyearacceptschoolkitchenList() {
		return halfyearacceptschoolkitchenList;
	}

	public void setHalfyearacceptschoolkitchenList(List<AcceptschoolkitchenBO> halfyearacceptschoolkitchenList) {
		this.halfyearacceptschoolkitchenList = halfyearacceptschoolkitchenList;
	}
}
