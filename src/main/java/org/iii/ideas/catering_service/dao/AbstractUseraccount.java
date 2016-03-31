package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

/**
 * AbstractUseraccount entity provides the base persistence definition of the
 * Useraccount entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractUseraccount implements java.io.Serializable {

	// Fields

	private String username;
	private String password;
	private String usertype;
	private Integer kitchenId;
	private String name;
	private String email;
	private Integer enable;
	private Timestamp lastlogintime;

	// Constructors

	/** default constructor */
	public AbstractUseraccount() {
	}

	/** minimal constructor */
	public AbstractUseraccount(String username, String password,
			String usertype) {
		this.username = username;
		this.password = password;
		this.usertype = usertype;
	}

	/** ori-full constructor */
	public AbstractUseraccount(String username, String password,
			String usertype, Integer kitchenId) {
		this.username = username;
		this.password = password;
		this.usertype = usertype;
		this.kitchenId = kitchenId;
	}
	/** full constructor */
	public AbstractUseraccount(String username, String password,
			String usertype, Integer kitchenId, String name, String email) {
		this.username = username;
		this.password = password;
		this.usertype = usertype;
		this.kitchenId = kitchenId;
		this.name = name;
		this.email = email;
	}

	// Property accessors

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsertype() {
		return this.usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public Integer getKitchenId() {
		return this.kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public Timestamp getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Timestamp lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

}