package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Area;

public class CustomerQueryArea  extends AbstractApiInterface<CustomerQueryAreaRequest, CustomerQueryAreaResponse>  {

	
	@Override
	public void process() throws NamingException, ParseException {

				Integer cid = Integer.valueOf( this.requestObj.getCid());			
			
				
				Criteria criteria = dbSession.createCriteria(Area.class);
				criteria.add( Restrictions.eq("countyId", cid) );
				List Areas = criteria.list();
				Iterator<Area> iterator = Areas.iterator();
				while (iterator.hasNext()) {
					Area ar = iterator.next();
					log.debug("Area:"+ar.getArea());
					AreaObject areaObj = new AreaObject();
					areaObj.setAid(ar.getAreaId());
					areaObj.setAreaName(ar.getArea());
					this.responseObj.getArea().add(areaObj);
				}
				this.responseObj.setMsg("");
				this.responseObj.setResStatus(1);
				
		
	}

}
