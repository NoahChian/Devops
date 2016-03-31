package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CustomerQueryAreaResponse   extends AbstractApiResponse  {
	private List<AreaObject> area = new ArrayList<AreaObject>();

	public List<AreaObject> getArea() {
		return area;
	}

	public void setArea(List<AreaObject> area) {
		this.area = area;
	}
}
