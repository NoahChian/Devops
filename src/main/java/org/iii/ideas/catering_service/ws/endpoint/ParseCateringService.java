package org.iii.ideas.catering_service.ws.endpoint;

import org.iii.ideas.catering_service.dao.UseraccountDAO;

public class ParseCateringService {
	private UseraccountDAO useraccountDao;

	public UseraccountDAO getUseraccountDao() {
		return useraccountDao;
	}

	public void setUseraccountDao(UseraccountDAO useraccountDao) {
		this.useraccountDao = useraccountDao;
	}
}
