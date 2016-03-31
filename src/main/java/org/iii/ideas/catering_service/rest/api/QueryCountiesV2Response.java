package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryCountiesV2Response  extends AbstractApiResponse  {
	private List<CountyObject> counties = new ArrayList<CountyObject>();

	public List<CountyObject> getCounties() {
		return counties;
	}

	public void setCounties(List<CountyObject> counties) {
		this.counties = counties;
	}
}
