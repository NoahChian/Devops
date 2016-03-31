package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

public class AreaObject  implements Serializable {
	private int aid;
	private String areaName;
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
