package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.iii.ideas.catering_service.dao.SeasoningStockData;
import org.iii.ideas.catering_service.rest.bo.SeasoningStockDataBO;

public class QuerySeasoningListResponse extends AbstractApiResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7205505829839372774L;
	private List<SeasoningStockDataBO> seasoningList = new ArrayList<SeasoningStockDataBO>();
	private Integer totalNum;
	
	public List<SeasoningStockDataBO> getSeasoningList() {
		return seasoningList;
	}
	public void setSeasoningList(List<SeasoningStockDataBO> seasoningList) {
		this.seasoningList = seasoningList;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

}
