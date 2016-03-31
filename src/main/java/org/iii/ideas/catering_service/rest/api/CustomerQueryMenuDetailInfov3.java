package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Query;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerQueryMenuDetailInfov3 extends AbstractApiInterface<CustomerQueryMenuDetailInfov3Request, CustomerQueryMenuDetailInfov3Response> {
	private static final Logger log = LoggerFactory.getLogger(CustomerQueryMenuDetailInfov3.class);

	@Override
	public void process() throws NamingException, ParseException {
		Long mid = this.requestObj.getMid();
		String hql="from Ingredientbatchdata where batchDataId=:mid and ingredientId=0 and dishId=0";
		Query queryObj=dbSession.createQuery(hql);
		queryObj.setParameter("mid", mid);
		List<Ingredientbatchdata> ingredientBatchdataList = queryObj.list();
		this.responseObj.setResStatus(1);
		this.responseObj.setResult(ingredientBatchdataList);
	}


}
