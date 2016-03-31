package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.News;
import org.iii.ideas.catering_service.dao.Newsattachfiles;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata2;
import org.iii.ideas.catering_service.rest.bo.ViewMenuBO;

public class QueryNewsAttachListResponse  extends AbstractApiResponse{

	private List <Newsattachfiles> newsattachfilesList = new ArrayList<Newsattachfiles>();

	public List<Newsattachfiles> getNewsattachfilesList() {
		return newsattachfilesList;
	}

	public void setNewsattachfilesList(List<Newsattachfiles> newsattachfilesList) {
		this.newsattachfilesList = newsattachfilesList;
	}
}
 