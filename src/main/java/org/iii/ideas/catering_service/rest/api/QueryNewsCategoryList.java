package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.iii.ideas.catering_service.dao.Newscategory;

public class QueryNewsCategoryList extends
		AbstractApiInterface<QueryNewsCategoryListRequest, QueryNewsCategoryListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		Criteria criteria = this.dbSession.createCriteria(Newscategory.class);
		
		criteria.addOrder(Order.asc(Newscategory.ID));
		List results = criteria.list();
		
		Iterator<Newscategory> iterator = results.iterator();
		while (iterator.hasNext()) {
			Newscategory list = iterator.next();
			this.responseObj.getNewsCategoryList().add(list);
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
