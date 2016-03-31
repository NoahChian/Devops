package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.rest.bo.AcceptschoolkitchenBO;

public class QueryAcceptSchoolKitchenWaitApproveByKitchenResponse extends AbstractApiResponse {
	private static final long serialVersionUID = -1904135875588581166L;

	private List<AcceptschoolkitchenBO> askList = new ArrayList<AcceptschoolkitchenBO>();

	public List<AcceptschoolkitchenBO> getAskList() {
		return askList;
	}

	public void setAskList(List<AcceptschoolkitchenBO> askList) {
		this.askList = askList;
	}

}
