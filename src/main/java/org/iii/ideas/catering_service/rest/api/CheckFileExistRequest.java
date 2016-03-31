package org.iii.ideas.catering_service.rest.api;

public class CheckFileExistRequest {
	private String pageName;
	private String dishId;

	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getDishId() {
		return dishId;
	}
	public void setDishId(String dishId) {
		this.dishId = dishId;
	}

}
