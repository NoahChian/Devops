package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.rest.bo.AcceptschoolkitchenBO;

public class QueryAcceptSchoolKitchenWaitApproveBySchoolResponse extends AbstractApiResponse {
	private static final long serialVersionUID = -1904135875588581166L;

	private List<AcceptschoolkitchenBO> acceptschoolkitchenwaitapprovebyschooList = new ArrayList<AcceptschoolkitchenBO>();

	public List<AcceptschoolkitchenBO> getAcceptschoolkitchenwaitapprovebyschooList() {
		return acceptschoolkitchenwaitapprovebyschooList;
	}

	public void setAcceptschoolkitchenwaitapprovebyschooList(
			List<AcceptschoolkitchenBO> acceptschoolkitchenwaitapprovebyschooList) {
		this.acceptschoolkitchenwaitapprovebyschooList = acceptschoolkitchenwaitapprovebyschooList;
	}

}
