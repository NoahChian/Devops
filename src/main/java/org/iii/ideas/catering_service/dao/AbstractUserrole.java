package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractSchoolkitchen entity provides the base persistence definition of the
 * Schoolkitchen entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractUserrole implements java.io.Serializable {

	// Fields

	private String username;
	private String roletype;
	private Timestamp createDate;
	private Timestamp updateTime;

	// Constructors

	/** default constructor */
	public AbstractUserrole() {
	}

	/** minimal constructor */
	/* public AbstractUserrole(UserroleId id) {
		this.id = id;
	}

*/	/** full constructor */
	/* public AbstractUserrole(UserroleId id, Timestamp createDate,
			Timestamp updateTime) {
		this.id = id;
		this.createDate = createDate;
		this.updateTime = updateTime;
	}*/
	public AbstractUserrole(String username, String roletype) {
		this.username = username;
		this.roletype = roletype;
	}
	public AbstractUserrole(String username, String roletype, Timestamp createDate,
			Timestamp updateTime) {
		this.username = username;
		this.roletype = roletype;
		this.createDate = createDate;
		this.updateTime = updateTime;
	}
	// Property accessors

	/*public UserroleId getId() {
		return this.id;
	}

	public void setId(UserroleId id) {
		this.id = id;
	}
*/
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getRoletype() {
		return this.roletype;
	}

	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}
	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}