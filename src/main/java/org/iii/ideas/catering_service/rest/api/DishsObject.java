package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

class DishsObject  implements Serializable {
	private Long dishId;
	private String name;
	public Long getDishId() {
		return dishId;
	}
	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}