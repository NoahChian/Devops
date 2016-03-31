package org.iii.ideas.catering_service.service;

import org.hibernate.Session;

public class BaseService {
	protected Session dbSession;

	public Session getDbSession() {
		return dbSession;
	}

	public void setDbSession(Session dbSession) {
		this.dbSession = dbSession;
	};
}
