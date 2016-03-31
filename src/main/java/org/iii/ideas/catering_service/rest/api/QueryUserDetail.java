package org.iii.ideas.catering_service.rest.api;

import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.UseraccountDAO;
import org.iii.ideas.catering_service.util.HibernateUtil;

public final class QueryUserDetail extends AbstractApiInterface<QueryUserDetailRequest, QueryUserDetailResponse> {

	@Override
	public void process() throws NamingException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		String username =  this.requestObj.getUsername();
		Criteria criteriaUA = dbSession.createCriteria(Useraccount.class);
		criteriaUA.add( Restrictions.eq("username", username) );
		int resStatus=0;
		List<Useraccount> users = criteriaUA.list();
		Iterator<Useraccount> iteratorUA = users.iterator();
		this.responseObj.setResStatus(0);
		this.responseObj.setMsg("找不到資料");
		if(iteratorUA.hasNext()) {
			Useraccount udetail = iteratorUA.next();
			this.responseObj.setUsername(udetail.getUsername());
			this.responseObj.setUsertype(udetail.getUsertype());
			this.responseObj.setName(udetail.getName());
			this.responseObj.setEmail(udetail.getEmail());
			Kitchen kitchen_Name = HibernateUtil.queryKitchenById(this.dbSession, udetail.getKitchenId());
			this.responseObj.setKitchenName(kitchen_Name.getKitchenName().toString());
			UseraccountDAO user =  new UseraccountDAO();
			user.openSessionFactory();
			List<Object[]> Userroles = user.getRoletypeByUsername(udetail.getUsername());
			Iterator<Object[]> iteratorUR = Userroles.iterator();
			while (iteratorUR.hasNext()) {
				Object[] obj=iteratorUR.next(); 
				String role_type = (String)obj[1];
				this.responseObj.getRoleList().add(role_type);	
			}
			user.closeSession();
	}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}
	
	

}
