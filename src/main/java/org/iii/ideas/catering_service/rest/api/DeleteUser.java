package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class DeleteUser extends AbstractApiInterface<DeleteUserRequest, DeleteUserResponse> {

	@Override
	public void process() throws NamingException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		String username = this.requestObj.getUsername();
		
		Transaction tx = dbSession.beginTransaction();
		HibernateUtil.deleteUseraccountByUsername(dbSession, username);//刪除帳戶
		HibernateUtil.deleteRoleByUsername(dbSession, username);//刪除對應角色權限
		tx.commit();
		
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

}
