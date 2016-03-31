package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

public class CountyObject  implements Serializable {
	private String cid;
	private String CountiesName;
	private String CountiesCode;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCountiesName() {
		return CountiesName;
	}
	public void setCountiesName(String countiesName) {
		CountiesName = countiesName;
	}
	public String getCountiesCode() {
		return CountiesCode;
	}
	public void setCountiesCode(String countiesCode) {
		CountiesCode = countiesCode;
	}
	
}
