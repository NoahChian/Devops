package org.iii.ideas.catering_service.dao;

import java.sql.Timestamp;

public class Kitchenfda extends AbstractKitchenfda implements java.io.Serializable {
	
	public Kitchenfda() {
	}

	public Kitchenfda(Integer kitchenId, String fdaCompanyId, Timestamp updatetime, String updateuser) {
		super(kitchenId, fdaCompanyId, updatetime, updateuser);
	}
}