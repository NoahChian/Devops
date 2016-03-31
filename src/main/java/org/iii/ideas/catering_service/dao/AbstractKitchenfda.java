package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

public abstract class AbstractKitchenfda implements java.io.Serializable {
	private Integer kitchenId;
	private String fdaCompanyId;
	private Timestamp updatetime;
	private String updateuser;

	// Constructors

	/** default constructor */
	public AbstractKitchenfda() {
	}

	/** minimal constructor */
	public AbstractKitchenfda(Integer kitchenId, String fdaCompanyId, Timestamp updatetime, String updateuser) {
		this.kitchenId = kitchenId;
		this.fdaCompanyId = fdaCompanyId;
		this.updatetime = updatetime;
		this.updateuser = updateuser;
	}

	// Property accessors

	public Integer getKitchenId() {
		return this.kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public String getFdaCompanyId() {
		return this.fdaCompanyId;
	}

	public void setFdaCompanyId(String fdaCompanyId) {
		this.fdaCompanyId = fdaCompanyId;
	}

	public Timestamp getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public String getUpdateuser() {
		return updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}
}