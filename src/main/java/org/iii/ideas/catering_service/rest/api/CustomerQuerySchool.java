package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.School;

public class CustomerQuerySchool extends AbstractApiInterface<CustomerQuerySchoolRequest, CustomerQuerySchoolResponse>  {

	@Override
	public void process() throws NamingException, ParseException {
		Integer aid = Integer.valueOf( this.requestObj.getAid());
		
	    		
		Criteria criteria = dbSession.createCriteria(School.class).add( Restrictions.eq("areaId", aid )).add(Restrictions.eq("enable", 1 ));
		List schools = criteria.list();
		Iterator<School> iterator = schools.iterator();
		
		while (iterator.hasNext()) {
			School sc = iterator.next();
			SchoolObject so = new SchoolObject();
			so.setSchoolName(sc.getSchoolName());
			so.setSid(sc.getSchoolId());
			so.setSchoolCode(sc.getSchoolCode());
			this.responseObj.getSchool().add(so);
		}
		
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
		
		
	}

}
