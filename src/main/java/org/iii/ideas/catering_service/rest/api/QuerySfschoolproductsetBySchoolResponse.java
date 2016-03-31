package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.rest.bo.SfSchoolproductsetBO;

public class QuerySfschoolproductsetBySchoolResponse extends AbstractApiResponse {
	private static final long serialVersionUID = -1904135875588581166L;
	private List<SfSchoolproductsetBO> sfschoolproductsetList = new ArrayList<SfSchoolproductsetBO>();

	public List<SfSchoolproductsetBO> getSfschoolproductsetList() {
		return sfschoolproductsetList;
	}

	public void setSfschoolproductsetList(List<SfSchoolproductsetBO> sfschoolproductsetList) {
		this.sfschoolproductsetList = sfschoolproductsetList;
	}

}