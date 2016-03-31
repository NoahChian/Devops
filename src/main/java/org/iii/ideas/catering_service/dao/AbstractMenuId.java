package org.iii.ideas.catering_service.dao;

/**
 * AbstractMenuId entity provides the base persistence definition of the MenuId
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractMenuId implements java.io.Serializable {

	// Fields

	private Integer kitchenId;
	private String menuDate;

	// Constructors

	/** default constructor */
	public AbstractMenuId() {
	}

	/** full constructor */
	public AbstractMenuId(Integer kitchenId, String menuDate) {
		this.kitchenId = kitchenId;
		this.menuDate = menuDate;
	}

	// Property accessors

	public Integer getKitchenId() {
		return this.kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public String getMenuDate() {
		return this.menuDate;
	}

	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractMenuId))
			return false;
		AbstractMenuId castOther = (AbstractMenuId) other;

		return ((this.getKitchenId() == castOther.getKitchenId()) || (this
				.getKitchenId() != null && castOther.getKitchenId() != null && this
				.getKitchenId().equals(castOther.getKitchenId())))
				&& ((this.getMenuDate() == castOther.getMenuDate()) || (this
						.getMenuDate() != null
						&& castOther.getMenuDate() != null && this
						.getMenuDate().equals(castOther.getMenuDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getKitchenId() == null ? 0 : this.getKitchenId().hashCode());
		result = 37 * result
				+ (getMenuDate() == null ? 0 : this.getMenuDate().hashCode());
		return result;
	}

}