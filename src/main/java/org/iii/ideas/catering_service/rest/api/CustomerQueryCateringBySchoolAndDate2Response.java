package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class CustomerQueryCateringBySchoolAndDate2Response   extends AbstractApiResponse  {
	private List <SupplierAndMenuDateObject> events = new ArrayList<SupplierAndMenuDateObject>();
	private String year;
	private String month;
	public List <SupplierAndMenuDateObject> getEvents() {
		return events;
	}
	public void setEvents(List <SupplierAndMenuDateObject> events) {
		this.events = events;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	
	public void setMonth(String month) {
		this.month = month;
	}
	
}
