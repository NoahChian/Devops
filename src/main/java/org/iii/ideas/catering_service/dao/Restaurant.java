package org.iii.ideas.catering_service.dao;

// Generated 2015/11/2 下午 06:06:40 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * Restaurant generated by hbm2java
 */
public class Restaurant implements java.io.Serializable {

	private Integer restaurantId;
	private int sfstreetId;
	private String restaurantName;
	private String companyId;
	private String picturePath;
	private String tel;
	private String address;
	private String hours1Desc;
	private String hours1Value;
	private String hours2Desc;
	private String hours2Value;
	private String mealItems;
	private String manager;
	private String enable;
	private Date createDate;
	private String createUser;
	private Date modifyDate;
	private String modifyUser;

	public Restaurant() {
	}

	public Restaurant(int sfstreetId, String restaurantName, String companyId,
			String tel, String address, String hours1Desc, String hours1Value,
			String hours2Desc, String hours2Value, String manager,
			String enable, Date createDate, String createUser) {
		this.sfstreetId = sfstreetId;
		this.restaurantName = restaurantName;
		this.companyId = companyId;
		this.tel = tel;
		this.address = address;
		this.hours1Desc = hours1Desc;
		this.hours1Value = hours1Value;
		this.hours2Desc = hours2Desc;
		this.hours2Value = hours2Value;
		this.manager = manager;
		this.enable = enable;
		this.createDate = createDate;
		this.createUser = createUser;
	}

	public Restaurant(int sfstreetId, String restaurantName, String companyId,
			String picturePath, String tel, String address, String hours1Desc,
			String hours1Value, String hours2Desc, String hours2Value,
			String mealItems, String manager, String enable, Date createDate,
			String createUser, Date modifyDate, String modifyUser) {
		this.sfstreetId = sfstreetId;
		this.restaurantName = restaurantName;
		this.companyId = companyId;
		this.picturePath = picturePath;
		this.tel = tel;
		this.address = address;
		this.hours1Desc = hours1Desc;
		this.hours1Value = hours1Value;
		this.hours2Desc = hours2Desc;
		this.hours2Value = hours2Value;
		this.mealItems = mealItems;
		this.manager = manager;
		this.enable = enable;
		this.createDate = createDate;
		this.createUser = createUser;
		this.modifyDate = modifyDate;
		this.modifyUser = modifyUser;
	}

	public Integer getRestaurantId() {
		return this.restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getSfstreetId() {
		return this.sfstreetId;
	}

	public void setSfstreetId(int sfstreetId) {
		this.sfstreetId = sfstreetId;
	}

	public String getRestaurantName() {
		return this.restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getPicturePath() {
		return this.picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHours1Desc() {
		return this.hours1Desc;
	}

	public void setHours1Desc(String hours1Desc) {
		this.hours1Desc = hours1Desc;
	}

	public String getHours1Value() {
		return this.hours1Value;
	}

	public void setHours1Value(String hours1Value) {
		this.hours1Value = hours1Value;
	}

	public String getHours2Desc() {
		return this.hours2Desc;
	}

	public void setHours2Desc(String hours2Desc) {
		this.hours2Desc = hours2Desc;
	}

	public String getHours2Value() {
		return this.hours2Value;
	}

	public void setHours2Value(String hours2Value) {
		this.hours2Value = hours2Value;
	}

	public String getMealItems() {
		return this.mealItems;
	}

	public void setMealItems(String mealItems) {
		this.mealItems = mealItems;
	}

	public String getManager() {
		return this.manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
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
