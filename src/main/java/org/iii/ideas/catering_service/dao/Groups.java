package org.iii.ideas.catering_service.dao;

import java.util.Date;

/**
 * School entity. @author MyEclipse Persistence Tools
 */
public class Groups implements java.io.Serializable {
	
	private Integer groupId;
	private String groupName;
	private Integer sort;
	
	public static final String GROUP_ID = "groupId";	//String
	public static final String GROUP_NAME = "groupName";	//String
	public static final String SORT = "sort";	//String
	
	/** default constructor */
	public Groups() {
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
