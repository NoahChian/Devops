package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractAcceptSwitch entity provides the base persistence definition of the
 * AcceptSwitch entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractAcceptSwitch implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer schoolId;
	private String acceptType;
	private Integer status;
	private String createUser;
	private Timestamp createDate;
	private String modifyUser;
	private Timestamp modifyDate;

	// Constructors

	/** default constructor */
	public AbstractAcceptSwitch() {
	}

	/** minimal constructor */
	public AbstractAcceptSwitch(Integer schoolId, String acceptType,
			Integer status, Timestamp createDate, Timestamp modifyDate) {
		this.schoolId = schoolId;
		this.acceptType = acceptType;
		this.status = status;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
	}

	/** full constructor */
	public AbstractAcceptSwitch(Integer schoolId, String acceptType,
			Integer status, String createUser, Timestamp createDate,
			String modifyUser, Timestamp modifyDate) {
		this.schoolId = schoolId;
		this.acceptType = acceptType;
		this.status = status;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSchoolId() {
		return this.schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public String getAcceptType() {
		return this.acceptType;
	}

	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Timestamp getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

}