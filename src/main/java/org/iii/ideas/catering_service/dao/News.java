package org.iii.ideas.catering_service.dao;

import java.util.Date;

/**
 * School entity. @author MyEclipse Persistence Tools
 */
public class News implements java.io.Serializable {
	
	private Integer newsId;
	private String newsTitle;
	private String content;
	private Integer category;
	private Integer priority;
	private String sourceTitle;
	private String sourceLink;
	private String publishUser;
	private Date publishDate;
	private Date startDate;
	private Date endDate;
	private Date modifyDate;
	private String modifyUser;	
	
	public static final String NEWS_ID = "newsId";	//String
	public static final String NEWS_TITLE = "newsTitle";	//String
	public static final String CONTENT = "content";	//String
	public static final String CATEGORY = "category";	//String
	public static final String PRIORITY = "priority";	//String
	public static final String SOURCE_TITLE = "sourceTitle";	//String
	public static final String SOURCE_LINK = "sourceLink"; // String
	public static final String PUBLISH_USER = "publishUser"; // String
	public static final String PUBLISH_DATE = "publishDate"; // String
	public static final String START_DATE = "startDate"; // String
	public static final String END_DATE = "endDate"; // String
	public static final String MODIFY_DATE = "modifyDate"; // String
	
	/** default constructor */
	public News() {
	}

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getSourceTitle() {
		return sourceTitle;
	}

	public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}

	public String getSourceLink() {
		return sourceLink;
	}

	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}

	public String getPublishUser() {
		return publishUser;
	}

	public void setPublishUser(String publishUser) {
		this.publishUser = publishUser;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
}
