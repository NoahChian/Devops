package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

public class SupplyNameOption  implements Serializable {
	private String supplyName;
	private String supplyId;
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getSupplyId() {
		return supplyId;
	}
	public void setSupplyId(String supplyId) {
		this.supplyId = supplyId;
	}
}
