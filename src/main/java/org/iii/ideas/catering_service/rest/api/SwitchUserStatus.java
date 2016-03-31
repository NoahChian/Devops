package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.UseraccountDAO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class SwitchUserStatus extends AbstractApiInterface<SwitchUserStatusRequest, SwitchUserStatusResponse> {

	@Override
	public void process() throws NamingException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		String username = this.requestObj.getUsername();
		
		Transaction tx = dbSession.beginTransaction();
		UseraccountDAO uaDao = new UseraccountDAO(dbSession);
		Useraccount user = uaDao.queryUseraccountByUsername(username);
		if(CateringServiceUtil.isNull(user)){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("此帳號不存在。");
		}
		if(user.getEnable()==1){
			user.setEnable(0);
		}else{
			user.setEnable(1);
		}
		dbSession.save(user);
		tx.commit();
		
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

}
