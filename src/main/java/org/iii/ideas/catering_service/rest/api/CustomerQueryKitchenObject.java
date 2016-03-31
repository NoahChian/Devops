package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

public class CustomerQueryKitchenObject  implements Serializable {
	private Long mid;
	private String kitchenName;
	public Long getMid() {
		return mid;
	}
	public void setMid(Long mid) {
		this.mid = mid;
	}
	public String getKitchenName() {
		return kitchenName;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
}
