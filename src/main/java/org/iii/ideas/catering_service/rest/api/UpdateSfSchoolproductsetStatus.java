package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.SfSchoolproductset;
import org.iii.ideas.catering_service.dao.SfSchoolproductsetDAO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

/*
 * 核准或否決商品上架申請
 */
public class UpdateSfSchoolproductsetStatus extends
		AbstractApiInterface<UpdateSfSchoolproductsetStatusRequest, UpdateSfSchoolproductsetStatusResponse> {
	private static final long serialVersionUID = -663285410782181994L;

	@Override
	public void process() throws NamingException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}

		Long id = this.requestObj.getSfschoolproductsetId();
		String decision = "" + this.requestObj.getSfschoolproductsetDecision();

		SfSchoolproductsetDAO dao = new SfSchoolproductsetDAO(this.dbSession);
		SfSchoolproductset sfschoolproductset = dao.findById(id);
		if (sfschoolproductset != null) {
			if ("Accept".equalsIgnoreCase(decision)) {
				sfschoolproductset.setStatus("1"); // 0:待審核, 1審核通過, 2:否決
				sfschoolproductset.setOnShelfDate(CateringServiceUtil.getCurrentTimestamp());
			} else if ("Reject".equalsIgnoreCase(decision)) {
				sfschoolproductset.setStatus("2"); // 0:待審核, 1審核通過, 2:否決
			}
			sfschoolproductset.setModifyUser(this.getUsername());
			sfschoolproductset.setModifyDateTime(CateringServiceUtil.getCurrentTimestamp());
			Transaction tx = this.dbSession.beginTransaction();
			this.dbSession.saveOrUpdate(sfschoolproductset);
			tx.commit();
		} else {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查無此商品上架申請資料，請洽系統管理員");
			return;
		}

		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}
}
