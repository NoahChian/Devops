package org.iii.ideas.catering_service.rest.api;

import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchen;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchenDAO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class DeleteSchoolKitchen extends AbstractApiInterface<DeleteSchoolKitchenRequest, DeleteSchoolKitchenResponse> {

	@Override
	public void process() throws NamingException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}

		Integer kid = Integer.valueOf(this.requestObj.getKitchenIdSC());
		Integer sid = Integer.valueOf(this.requestObj.getSchoolIdSC());
		// Integer kid = 1;
		
		AcceptSchoolKitchenDAO dao = new AcceptSchoolKitchenDAO(this.dbSession);
		AcceptSchoolKitchen acceptschoolkitchen = new AcceptSchoolKitchen();
		acceptschoolkitchen.setKitchenId(kid);
		acceptschoolkitchen.setSchoolId(sid);
		List<AcceptSchoolKitchen> acceptschoolkitchenList = dao.findByExample(acceptschoolkitchen);

		Transaction tx = dbSession.beginTransaction();
		HibernateUtil.deleteSchoolKitchenFromSchoolKitchenById(dbSession, kid, sid);//刪除廚房的對應學校
		// 同時對審核通過的供餐記錄進行取消供餐
		for (AcceptSchoolKitchen record : acceptschoolkitchenList) {
			if ("1".equals(record.getStatus())) { // 0:待審核, 1審核通過, 2:否決
				record.setAction("2"); // 1: 申請供餐 , 2:取消供餐, 3:商品上架
				record.setModifyUser(this.getUsername());
				record.setModifyDateTime(CateringServiceUtil.getCurrentTimestamp());
				this.dbSession.saveOrUpdate(record);
			}
		}
		tx.commit();
		
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

}
