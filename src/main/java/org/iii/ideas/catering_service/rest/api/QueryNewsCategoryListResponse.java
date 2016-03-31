package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.Newscategory;

public class QueryNewsCategoryListResponse   extends AbstractApiResponse{
	private List <Newscategory> newsCategoryList = new ArrayList<Newscategory>();

	public List <Newscategory> getNewsCategoryList() {
		return newsCategoryList;
	}

	public void setNewsCategoryList(List <Newscategory> newsCategoryList) {
		this.newsCategoryList = newsCategoryList;
	}

}

