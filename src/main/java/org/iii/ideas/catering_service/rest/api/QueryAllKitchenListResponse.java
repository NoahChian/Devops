package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QueryAllKitchenListResponse {
	
	private String resStatus;
	
	private String msg;
	
	private List<KitchenVo> kitchen; 
	
	
	public QueryAllKitchenListResponse() {
		kitchen = new ArrayList<KitchenVo>();
	}

	public List<KitchenVo> getKitchen() {
		return kitchen;
	}

	public void setKitchen(List<KitchenVo> kitchenList) {
		this.kitchen = kitchenList;
	}

	public String getResStatus() {
		return resStatus;
	}

	public void setResStatus(String resStatus) {
		this.resStatus = resStatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}


