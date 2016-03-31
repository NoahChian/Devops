package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.UseraccountDAO;
import org.iii.ideas.catering_service.dao.UsertypeDAO;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryUserTypeList extends
		AbstractApiInterface<QueryUserTypeListRequest, QueryUserTypeListResponse> {

	@Override
	public void process() throws NamingException, ParseException {

		UsertypeDAO utDao = new UsertypeDAO(dbSession);
		if(CateringServiceUtil.isNull(this.requestObj.getType())){
			this.responseObj.setUsertypelist(utDao.queryAllUsertypeList());	
		}
		if("Kitchen".equals(this.requestObj.getType())){
			this.responseObj.setUsertypelist(utDao.queryAllUsertypeListforKitchenList());
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
