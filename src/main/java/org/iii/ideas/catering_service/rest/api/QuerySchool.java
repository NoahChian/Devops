package org.iii.ideas.catering_service.rest.api;

import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Schoolkitchen;

public class QuerySchool extends AbstractApiInterface<QuerySchoolRequest, QuerySchoolResponse>{

	@Override
	public void process() throws NamingException {
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		Integer ciid = Integer.valueOf( this.requestObj.getCiid());
		
		Criteria criteriaSK = dbSession.createCriteria(Schoolkitchen.class).add( Restrictions.eq("id.kitchenId", ciid ));
		//增加僅顯示現況供餐之學校
		criteriaSK.add(Restrictions.eq("offered", 1));
		List schoolkitchens = criteriaSK.list();
		if(schoolkitchens.size()==0){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("找不到學校資料");
		}
		Iterator<Schoolkitchen> iteratorSK = schoolkitchens.iterator();
		
		while (iteratorSK.hasNext()) {
			Schoolkitchen sk = iteratorSK.next();
			Criteria criteriaSC = dbSession.createCriteria(School.class).add( Restrictions.eq("schoolId", sk.getId().getSchoolId() )).add(Restrictions.eq("enable", 1 ));
			List Schools = criteriaSC.list();
			Iterator<School> iteratorSC = Schools.iterator();

			while(iteratorSC.hasNext()){
				School sc = iteratorSC.next();
				SchoolObject sco = new SchoolObject();
				sco.setSid(sc.getSchoolId());
				sco.setSchoolName(sc.getSchoolName());
				this.responseObj.getSchool().add(sco);
			}
			
		}
		
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
