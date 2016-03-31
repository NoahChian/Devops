package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractAcceptSchoolKitchen entity provides the base persistence definition
 * of the AcceptSchoolKitchen entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractAcceptSchoolKitchen implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Integer schoolId;
	private Integer kitchenId;
	private Integer quantity;
	private String status;
	private String action;
	private String createUser;
	private Timestamp createDateTime;
	private String modifyUser;
	private Timestamp modifyDateTime;

	// Constructors

	/** default constructor */
	public AbstractAcceptSchoolKitchen() {
	}

	/** full constructor */
	public AbstractAcceptSchoolKitchen(Integer schoolId, Integer kitchenId,
			Integer quantity, String status, String action, String createUser,
			Timestamp createDateTime, String modifyUser,
			Timestamp modifyDateTime) {
		this.schoolId = schoolId;
		this.kitchenId = kitchenId;
		this.quantity = quantity;
		this.status = status;
		this.action = action;
		this.createUser = createUser;
		this.createDateTime = createDateTime;
		this.modifyUser = modifyUser;
		this.modifyDateTime = modifyDateTime;
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

	public Integer getKitchenId() {
		return this.kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
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