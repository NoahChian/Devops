package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.NewsBO;
import org.iii.ideas.catering_service.rest.bo.SupplierBO;


public class UpdateNewsDetailRequest {
	private String activeType;
	private NewsBO newsBo;
	
	public String getActiveType() {
		return activeType;
	}
	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}
	public NewsBO getNewsBo() {
		return newsBo;
	}
	public void setNewsBo(NewsBO newsBo) {
		this.newsBo = newsBo;
	}
}
