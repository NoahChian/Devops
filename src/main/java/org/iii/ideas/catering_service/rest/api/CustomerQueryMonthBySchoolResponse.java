package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class CustomerQueryMonthBySchoolResponse   extends AbstractApiResponse {
	private List<YearMonthObject> date = new ArrayList<YearMonthObject>();

	public List<YearMonthObject> getDate() {
		return date;
	}

	public void setDate(List<YearMonthObject> date) {
		this.date = date;
	}
}
