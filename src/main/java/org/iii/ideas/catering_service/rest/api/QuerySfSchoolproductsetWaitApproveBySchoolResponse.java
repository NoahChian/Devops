package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.rest.bo.SfSchoolproductsetBO;

public class QuerySfSchoolproductsetWaitApproveBySchoolResponse extends AbstractApiResponse {
	private static final long serialVersionUID = -1904135875588581166L;
	private List<SfSchoolproductsetBO> sfschoolproductsetwaitapprovebyschoolList = new ArrayList<SfSchoolproductsetBO>();

	public List<SfSchoolproductsetBO> getSfschoolproductsetwaitapprovebyschoolList() {
		return sfschoolproductsetwaitapprovebyschoolList;
	}

	public void setSfschoolproductsetwaitapprovebyschoolList(
			List<SfSchoolproductsetBO> sfschoolproductsetwaitapprovebyschoolList) {
		this.sfschoolproductsetwaitapprovebyschoolList = sfschoolproductsetwaitapprovebyschoolList;
	}

}