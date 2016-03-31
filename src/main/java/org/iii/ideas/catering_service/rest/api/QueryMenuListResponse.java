package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata2;
import org.iii.ideas.catering_service.rest.bo.ViewMenuBO;

public class QueryMenuListResponse  extends AbstractApiResponse{

	private List<ViewSchoolMenuWithBatchdata2> menu = new ArrayList<ViewSchoolMenuWithBatchdata2>();
	private Integer totalCol;
	public List <ViewSchoolMenuWithBatchdata2> getMenu() {
		return menu;
	}

	public void setMenu(List<ViewSchoolMenuWithBatchdata2> vsBO) {
		this.menu = vsBO;
	}

	public Integer getTotalCol() {
		return totalCol;
	}

	public void setTotalCol(Integer totalCol) {
		this.totalCol = totalCol;
	}

	
}
 