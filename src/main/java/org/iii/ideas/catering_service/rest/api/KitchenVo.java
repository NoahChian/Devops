package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

public class KitchenVo  implements Serializable {
	private String kid;
	
	private String kitchenName;

	public KitchenVo(String kid, String kitchenName) {
		this.kid = kid;
		this.kitchenName = kitchenName;
	}
	
	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
}
