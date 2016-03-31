package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.CounterDAO;
import org.iii.ideas.catering_service.dao.County;
import org.iii.ideas.catering_service.dao.CountyDAO;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.ViewKitchenUnionRestaurant;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryCountiesV2 extends
		AbstractApiInterface<QueryCountiesV2Request, QueryCountiesV2Response> {

	@Override
	public void process() throws NamingException, ParseException {
		CountyDAO cDao = new CountyDAO(this.dbSession);
		
		List counties = cDao.queryAllCountyAndCode();

		for(int i=0;i<counties.size();i++){
			Object[] obj = (Object[]) counties.get(i);
			CountyObject ct = new CountyObject();
			ct.setCid(String.valueOf(obj[0]));
			ct.setCountiesName((String)obj[1]);
			ct.setCountiesCode((String)obj[2]);
			this.responseObj.getCounties().add(ct);
		}
			
			
//		this.responseObj.setCounties(counties);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);

	}

}
