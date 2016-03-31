package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class DeleteAcceptschoolkitchen extends
		AbstractApiInterface<DeleteAcceptschoolkitchenRequest, DeleteAcceptschoolkitchenResponse> {
	private static final long serialVersionUID = -663285410782181994L;

	@Override
	public void process() throws NamingException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}

		Integer id = Integer.valueOf(this.requestObj.getAcceptschoolkitchenId());
		Transaction tx = dbSession.beginTransaction();
		HibernateUtil.deleteAcceptSchoolKitchenById(dbSession, id);// 刪除供餐申請記錄
		tx.commit();

		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}
}
