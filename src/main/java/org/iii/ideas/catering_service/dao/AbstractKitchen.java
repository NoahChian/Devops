package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractKitchen entity provides the base persistence definition of the
 * Kitchen entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractKitchen implements java.io.Serializable {

	// Fields

	private Integer kitchenId;
	private String kitchenName;
	private String kitchenType;
	private String address;
	private String ownner;
	private String tel;
	private String fax;
	private String nutritionist;
	private String chef;
	private String haccp;
	private String qualifier;
	private String insurement;
	private String picturePath;
	private Timestamp createDate;
	private Timestamp endDate;
	private String companyId;
	private String email;
	private Integer enable;
	private String manager;
	private String manageremail;

	// Constructors

	/** default constructor */
	public AbstractKitchen() {
	}

	/** minimal constructor */
	public AbstractKitchen(String kitchenName, String kitchenType,
			String address, String ownner, String tel, String fax,
			String nutritionist, String chef, String haccp, String qualifier,
			String insurement, String picturePath, Timestamp createDate,String companyId) {
		this.kitchenName = kitchenName;
		this.kitchenType = kitchenType;
		this.address = address;
		this.ownner = ownner;
		this.tel = tel;
		this.fax = fax;
		this.nutritionist = nutritionist;
		this.chef = chef;
		this.haccp = haccp;
		this.qualifier = qualifier;
		this.insurement = insurement;
		this.picturePath = picturePath;
		this.createDate = createDate;
		this.setCompanyId(companyId);
	}

	/** full constructor */
	public AbstractKitchen(String kitchenName, String kitchenType,
			String address, String ownner, String tel, String fax,
			String nutritionist, String chef, String haccp, String qualifier,
			String insurement, String picturePath, Timestamp createDate,
			Timestamp endDate,String companyId,String email) {
		this.kitchenName = kitchenName;
		this.kitchenType = kitchenType;
		this.address = address;
		this.ownner = ownner;
		this.tel = tel;
		this.fax = fax;
		this.nutritionist = nutritionist;
		this.chef = chef;
		this.haccp = haccp;
		this.qualifier = qualifier;
		this.insurement = insurement;
		this.picturePath = picturePath;
		this.createDate = createDate;
		this.endDate = endDate;
		this.setCompanyId(companyId);
		this.email=email;
	}

	// Property accessors

	public Integer getKitchenId() {
		return this.kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public String getKitchenName() {
		return this.kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public String getKitchenType() {
		return this.kitchenType;
	}

	public void setKitchenType(String kitchenType) {
		this.kitchenType = kitchenType;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOwnner() {
		return this.ownner;
	}

	public void setOwnner(String ownner) {
		this.ownner = ownner;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getNutritionist() {
		return this.nutritionist;
	}

	public void setNutritionist(String nutritionist) {
		this.nutritionist = nutritionist;
	}

	public String getChef() {
		return this.chef;
	}

	public void setChef(String chef) {
		this.chef = chef;
	}

	public String getHaccp() {
		return this.haccp;
	}

	public void setHaccp(String haccp) {
		this.haccp = haccp;
	}

	public String getQualifier() {
		return this.qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getInsurement() {
		return this.insurement;
	}

	public void setInsurement(String insurement) {
		this.insurement = insurement;
	}

	public String getPicturePath() {
		return this.picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	//add email column   20140305 KC
	public void setEmail(String email){
		this.email=email;
	}
	public String getEmail(){
		return this.email;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManageremail() {
		return manageremail;
	}

	public void setManageremail(String manageremail) {
		this.manageremail = manageremail;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}


}