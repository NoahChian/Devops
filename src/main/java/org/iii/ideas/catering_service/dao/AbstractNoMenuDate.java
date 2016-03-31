package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractIngredient entity provides the base persistence definition of the
 * Ingredient entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractNoMenuDate implements java.io.Serializable {

	// Fields

	private Long nmDateId;
	private String schoolId;
	private Timestamp startdate;
	private Timestamp enddate;
	private Integer menuType;
	private Integer nmdescId;
	private String note;
	private String createUser;
	private Timestamp createDateTime;
	private String updateUser;
	private Timestamp updateDateTime;

	// Constructors

	/** default constructor */
	public AbstractNoMenuDate() {
	}

	/**full constructor* */
	public AbstractNoMenuDate(Long nmDateId, String schoolId,
			Timestamp startdate, Timestamp enddate,Integer menuType,
			Integer nmdescId,String note,String createUser,Timestamp createDateTime,
			String updateUser,Timestamp updateDateTime) {
		this.nmDateId = nmDateId;
		this.schoolId = schoolId;
		this.startdate = startdate;
		this.enddate = enddate;
		this.menuType = menuType;
		this.nmdescId = nmdescId;
		this.note = note;
		this.createUser = createUser;
		this.createDateTime = createDateTime;
		this.updateUser = updateUser;
		this.updateDateTime = updateDateTime;
	}

	public Long getNmDateId() {
		return nmDateId;
	}

	public void setNmDateId(Long nmDateId) {
		this.nmDateId = nmDateId;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public Timestamp getStartdate() {
		return startdate;
	}

	public void setStartdate(Timestamp startdate) {
		this.startdate = startdate;
	}

	public Timestamp getEnddate() {
		return enddate;
	}

	public void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public Integer getNmdescId() {
		return nmdescId;
	}

	public void setNmdescId(Integer nmdescId) {
		this.nmdescId = nmdescId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Timestamp getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Timestamp getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(Timestamp updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
	
	
}