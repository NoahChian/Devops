package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchen;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchenDAO;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.SchoolkitchenId;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class UpdateAcceptschoolkitchenStatus extends
		AbstractApiInterface<UpdateAcceptschoolkitchenStatusRequest, UpdateAcceptschoolkitchenStatusResponse> {
	private static final long serialVersionUID = -663285410782181994L;
	
	@Override
	public void process() throws NamingException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}

		Integer id = this.requestObj.getAcceptschoolkitchenId();
		String decision = "" + this.requestObj.getAcceptschoolkitchenDecision();
		AcceptSchoolKitchenDAO dao = new AcceptSchoolKitchenDAO(this.dbSession);
		AcceptSchoolKitchen acceptschoolkitchen = dao.findById(id);
		if (acceptschoolkitchen != null) {
			if("Accept".equalsIgnoreCase(decision)){
				acceptschoolkitchen.setStatus("1"); // 0:待審核, 1審核通過, 2:否決
				acceptschoolkitchen.setModifyUser(this.getUsername());
				acceptschoolkitchen.setModifyDateTime(CateringServiceUtil.getCurrentTimestamp());
				// 當審核通過時更新acceptSchoolKitchen狀態並將acceptSchoolKitchen資訊複製到schoolkitchen中
				SchoolkitchenId schoolkitchenId = new SchoolkitchenId(acceptschoolkitchen.getSchoolId(),acceptschoolkitchen.getKitchenId());
				Schoolkitchen schoolkitchen = new Schoolkitchen(schoolkitchenId);
				schoolkitchen.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
				schoolkitchen.setQuantity(acceptschoolkitchen.getQuantity());
				Transaction tx = this.dbSession.beginTransaction();
				this.dbSession.saveOrUpdate(acceptschoolkitchen);
				this.dbSession.saveOrUpdate(schoolkitchen);
				tx.commit();
			} else if("Reject".equalsIgnoreCase(decision)){
				acceptschoolkitchen.setStatus("2"); // 0:待審核, 1審核通過, 2:否決
				acceptschoolkitchen.setModifyUser(this.getUsername());
				acceptschoolkitchen.setModifyDateTime(CateringServiceUtil.getCurrentTimestamp());
				Transaction tx = this.dbSession.beginTransaction();
				this.dbSession.saveOrUpdate(acceptschoolkitchen);
				tx.commit();
			}
		} else {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查無此供餐申請資料，請洽系統管理員");
			return;
		}

		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}
}
