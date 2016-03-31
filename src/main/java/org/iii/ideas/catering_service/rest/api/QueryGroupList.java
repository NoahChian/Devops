package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.iii.ideas.catering_service.dao.GroupsDAO;
import org.iii.ideas.catering_service.dao.Groups;

public class QueryGroupList extends
		AbstractApiInterface<QueryGroupListRequest, QueryGroupListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
//		String userType = this.getUserType();
//		GroupDAO groupDao = new GroupDAO(dbSession);

		Criteria criteria = dbSession.createCriteria(Groups.class);
		criteria.addOrder(Order.asc(Groups.SORT));
		criteria.addOrder(Order.asc(Groups.GROUP_ID));
		
		List groups = criteria.list();
		Iterator<Groups> iterator = groups.iterator();

		while (iterator.hasNext()) {
			Groups sc = iterator.next();
			this.responseObj.getGroup().add(sc);
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}
}
