package org.iii.ideas.catering_service.dao;

/**
 * RolenaviId entity. @author MyEclipse Persistence Tools
 */

public class RolenaviId implements java.io.Serializable {

	// Fields

	private Long roleId;
	private Long naviId;

	// Constructors

	/** default constructor */
	public RolenaviId() {
	}

	/** full constructor */
	public RolenaviId(Long roleId, Long naviId) {
		this.roleId = roleId;
		this.naviId = naviId;
	}

	// Property accessors

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getNaviId() {
		return this.naviId;
	}

	public void setNaviId(Long naviId) {
		this.naviId = naviId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RolenaviId))
			return false;
		RolenaviId castOther = (RolenaviId) other;

		return ((this.getRoleId() == castOther.getRoleId()) || (this
				.getRoleId() != null && castOther.getRoleId() != null && this
				.getRoleId().equals(castOther.getRoleId())))
				&& ((this.getNaviId() == castOther.getNaviId()) || (this
						.getNaviId() != null && castOther.getNaviId() != null && this
						.getNaviId().equals(castOther.getNaviId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		result = 37 * result
				+ (getNaviId() == null ? 0 : this.getNaviId().hashCode());
		return result;
	}

}