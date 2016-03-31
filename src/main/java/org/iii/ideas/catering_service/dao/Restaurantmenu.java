package org.iii.ideas.catering_service.dao;

// Generated 2015/11/2 下午 06:06:40 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * Restaurantmenu generated by hbm2java
 */
public class Restaurantmenu implements java.io.Serializable {

	private Integer rmenuId;
	private int restaurantId;
	private int enable;
	private Date createDate;
	private String createUser;
	private Date modifyDate;
	private String modifyUser;

	public Restaurantmenu() {
	}

	public Restaurantmenu(int restaurantId, int enable, Date createDate,
			String createUser) {
		this.restaurantId = restaurantId;
		this.enable = enable;
		this.createDate = createDate;
		this.createUser = createUser;
	}

	public Restaurantmenu(int restaurantId, int enable, Date createDate,
			String createUser, Date modifyDate, String modifyUser) {
		this.restaurantId = restaurantId;
		this.enable = enable;
		this.createDate = createDate;
		this.createUser = createUser;
		this.modifyDate = modifyDate;
		this.modifyUser = modifyUser;
	}

	public Integer getRmenuId() {
		return this.rmenuId;
	}

	public void setRmenuId(Integer rmenuId) {
		this.rmenuId = rmenuId;
	}

	public int getRestaurantId() {
		return this.restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getEnable() {
		return this.enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

}