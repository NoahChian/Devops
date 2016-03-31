package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.NewsBO;
import org.iii.ideas.catering_service.rest.bo.SupplierBO;

public class QueryNewsDetailResponse extends AbstractApiResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1671888537921894418L;
	private NewsBO newsBo;
	
	public NewsBO getNewsBo() {
		return newsBo;
	}
	public void setNewsBo(NewsBO newsBo) {
		this.newsBo = newsBo;
	}
}
