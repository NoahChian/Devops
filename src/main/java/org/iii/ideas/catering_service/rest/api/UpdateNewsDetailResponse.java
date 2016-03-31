package org.iii.ideas.catering_service.rest.api;

import org.iii.ideas.catering_service.rest.bo.NewsBO;
import org.iii.ideas.catering_service.rest.bo.SupplierBO;

public class UpdateNewsDetailResponse extends AbstractApiResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6465269258755721397L;
	
	private Integer newsId;
	
	public Integer getNewsId() {
		return newsId;
	}
	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

//	private NewsBO newsBo;
//	
//	public NewsBO getNewsBo() {
//		return newsBo;
//	}
//	public void setNewsBo(NewsBO newsBo) {
//		this.newsBo = newsBo;
//	}
}
