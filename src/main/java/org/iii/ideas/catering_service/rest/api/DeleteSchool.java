package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class DeleteSchool extends AbstractApiInterface<DeleteSchoolRequest, DeleteSchoolResponse> {

	
	@Override
	public void process() throws NamingException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		Integer schoolId  = Integer.valueOf(this.requestObj.getSchoolId());
		
		Transaction tx = dbSession.beginTransaction();
		HibernateUtil.deleteSchoolBySchoolId(dbSession, schoolId);//刪除帳戶
		tx.commit();
		
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

}
