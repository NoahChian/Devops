package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

class IngredientMenuObj  implements Serializable {
	private String date="";
	private String description ="";
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}