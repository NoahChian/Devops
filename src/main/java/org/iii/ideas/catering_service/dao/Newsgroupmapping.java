package org.iii.ideas.catering_service.dao;

import java.util.Date;

/**
 * School entity. @author MyEclipse Persistence Tools
 */
public class Newsgroupmapping implements java.io.Serializable {
		
	private Integer newsgroupId;
	private Integer newsId;
	private Integer groupId;
	
	public static final String NEWS_GROUP_ID = "newsgroupId";	//String
	public static final String NEWS_ID = "newsId";	//String
	public static final String GROUP_ID = "groupId";	//String
	
	/** default constructor */
	public Newsgroupmapping() {
	}

	public Integer getNewsgroupId() {
		return newsgroupId;
	}

	public void setNewsgroupId(Integer newsgroupId) {
		this.newsgroupId = newsgroupId;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
}
