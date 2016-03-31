package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.News;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata2;
import org.iii.ideas.catering_service.rest.bo.ViewMenuBO;

public class QueryNewsListResponse  extends AbstractApiResponse{

	private List <News> newList = new ArrayList<News>();

	public List<News> getNewList() {
		return newList;
	}

	public void setNewList(List<News> newList) {
		this.newList = newList;
	}

	private Integer totalCol;

	public Integer getTotalCol() {
		return totalCol;
	}

	public void setTotalCol(Integer totalCol) {
		this.totalCol = totalCol;
	}

	
}
 