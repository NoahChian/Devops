package org.iii.ideas.catering_service.dao;

/**
 * AbstractSchoolkitchenId entity provides the base persistence definition of
 * the SchoolkitchenId entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractSchoolkitchenId implements java.io.Serializable {

	// Fields

	private Integer schoolId;
	private Integer kitchenId;

	// Constructors

	/** default constructor */
	public AbstractSchoolkitchenId() {
	}

	/** full constructor */
	public AbstractSchoolkitchenId(Integer schoolId, Integer kitchenId) {
		this.schoolId = schoolId;
		this.kitchenId = kitchenId;
	}

	// Property accessors

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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractSchoolkitchenId))
			return false;
		AbstractSchoolkitchenId castOther = (AbstractSchoolkitchenId) other;

		return ((this.getSchoolId() == castOther.getSchoolId()) || (this
				.getSchoolId() != null && castOther.getSchoolId() != null && this
				.getSchoolId().equals(castOther.getSchoolId())))
				&& ((this.getKitchenId() == castOther.getKitchenId()) || (this
						.getKitchenId() != null
						&& castOther.getKitchenId() != null && this
						.getKitchenId().equals(castOther.getKitchenId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSchoolId() == null ? 0 : this.getSchoolId().hashCode());
		result = 37 * result
				+ (getKitchenId() == null ? 0 : this.getKitchenId().hashCode());
		return result;
	}

}