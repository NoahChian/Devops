package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractSfSchoolproductset entity provides the base persistence definition of
 * the SfSchoolproductset entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractSfSchoolproductset implements
		java.io.Serializable {

	// Fields

	private Long id;
	private Integer schoolId;
	private Long productId;
	private Timestamp onShelfDate;
	private Timestamp offShelfDate;
	private String status;
	private String createUser;
	private Timestamp createDateTime;
	private String modifyUser;
	private Timestamp modifyDateTime;

	// Constructors

	/** default constructor */
	public AbstractSfSchoolproductset() {
	}

	/** minimal constructor */
	public AbstractSfSchoolproductset(Integer schoolId, Long productId,
			Timestamp onShelfDate, String status, String createUser,
			Timestamp createDateTime, String modifyUser,
			Timestamp modifyDateTime) {
		this.schoolId = schoolId;
		this.productId = productId;
		this.onShelfDate = onShelfDate;
		this.status = status;
		this.createUser = createUser;
		this.createDateTime = createDateTime;
		this.modifyUser = modifyUser;
		this.modifyDateTime = modifyDateTime;
	}

	/** full constructor */
	public AbstractSfSchoolproductset(Integer schoolId, Long productId,
			Timestamp onShelfDate, Timestamp offShelfDate, String status,
			String createUser, Timestamp createDateTime, String modifyUser,
			Timestamp modifyDateTime) {
		this.schoolId = schoolId;
		this.productId = productId;
		this.onShelfDate = onShelfDate;
		this.offShelfDate = offShelfDate;
		this.status = status;
		this.createUser = createUser;
		this.createDateTime = createDateTime;
		this.modifyUser = modifyUser;
		this.modifyDateTime = modifyDateTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSchoolId() {
		return this.schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public Long getProductId() {
		return this.productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Timestamp getOnShelfDate() {
		return this.onShelfDate;
	}

	public void setOnShelfDate(Timestamp onShelfDate) {
		this.onShelfDate = onShelfDate;
	}

	public Timestamp getOffShelfDate() {
		return this.offShelfDate;
	}

	public void setOffShelfDate(Timestamp offShelfDate) {
		this.offShelfDate = offShelfDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Timestamp getCreateDateTime() {
		return this.createDateTime;
	}

	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Timestamp getModifyDateTime() {
		return this.modifyDateTime;
	}

	public void setModifyDateTime(Timestamp modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}

}