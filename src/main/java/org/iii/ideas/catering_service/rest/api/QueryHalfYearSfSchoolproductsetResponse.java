package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.rest.bo.SfSchoolproductsetBO;

public class QueryHalfYearSfSchoolproductsetResponse extends AbstractApiResponse {
	private static final long serialVersionUID = -1904135875588581166L;
	private List<SfSchoolproductsetBO> halfyearsfschoolproductsetList = new ArrayList<SfSchoolproductsetBO>();

	public List<SfSchoolproductsetBO> getHalfyearsfschoolproductsetList() {
		return halfyearsfschoolproductsetList;
	}

	public void setHalfyearsfschoolproductsetList(List<SfSchoolproductsetBO> halfyearsfschoolproductsetList) {
		this.halfyearsfschoolproductsetList = halfyearsfschoolproductsetList;
	}

}