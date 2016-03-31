package org.iii.ideas.catering_service.dao;

/**
 * AbstractSchoolkitchenId entity provides the base persistence definition of
 * the SchoolkitchenId entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractUserroleId implements java.io.Serializable {

	// Fields

	private String username;
	private String roletype;

	// Constructors

	/** default constructor */
	public AbstractUserroleId() {
	}

	/** full constructor */
	public AbstractUserroleId(String username, String roletype) {
		this.username = username;
		this.roletype = roletype;
	}

	// Property accessors

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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractUserroleId))
			return false;
		AbstractUserroleId castOther = (AbstractUserroleId) other;

		return ((this.getUsername() == castOther.getUsername()) || (this
				.getUsername() != null && castOther.getUsername() != null && this
				.getUsername().equals(castOther.getUsername())))
				&& ((this.getRoletype() == castOther.getRoletype()) || (this
						.getRoletype() != null
						&& castOther.getRoletype() != null && this
						.getRoletype().equals(castOther.getRoletype())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUsername() == null ? 0 : this.getUsername().hashCode());
		result = 37 * result
				+ (getRoletype() == null ? 0 : this.getRoletype().hashCode());
		return result;
	}

}