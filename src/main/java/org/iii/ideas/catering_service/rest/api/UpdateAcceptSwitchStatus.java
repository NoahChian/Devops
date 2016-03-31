package org.iii.ideas.catering_service.rest.api;

import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchen;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchenDAO;
import org.iii.ideas.catering_service.dao.AcceptSwitch;
import org.iii.ideas.catering_service.dao.AcceptSwitchDAO;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.SchoolkitchenId;
import org.iii.ideas.catering_service.dao.SfSchoolproductset;
import org.iii.ideas.catering_service.dao.SfSchoolproductsetDAO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

/*
 * Update or Insert acceptSwitch的status,如停用審核則需Approve所有審核申請
 */
public class UpdateAcceptSwitchStatus extends	AbstractApiInterface<UpdateAcceptSwitchStatusRequest, UpdateAcceptSwitchStatusResponse> {
	private static final long serialVersionUID = -663285410782181994L;

	@Override
	public void process() throws NamingException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		Integer schoolId = this.requestObj.getSchoolId();
		String acceptType = this.requestObj.getAcceptType();
		Integer status = this.requestObj.getStatus();
		
		// AcceptType 開關類別 ( kitchenVerify: 供餐廚房審核開關 , productVerify:商品上架審核開關)
		if ("kitchenVerify".equals(acceptType) || "productVerify".equals(acceptType)) {
		} else {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查無此審核開關類別，請洽系統管理員");
			return;
		}
		// Status 狀態 0:停用 ,1:啟用
		if ("0".equals("" + status) || "1".equals("" + status)) {
		} else {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查無此審核開關狀態，請洽系統管理員");
			return;
		}
		
		// 變更AcceptSwitch的狀態值
		AcceptSwitchDAO acceptswitchDAO = new AcceptSwitchDAO(this.dbSession);
		AcceptSwitch acceptswitchRecord = new AcceptSwitch();
		acceptswitchRecord.setSchoolId(schoolId); // 搜尋條件 : SchoolId and AcceptType
		acceptswitchRecord.setAcceptType(acceptType);
		List<AcceptSwitch> acceptswitchList = acceptswitchDAO.findByExample(acceptswitchRecord);
		Transaction tx = this.dbSession.beginTransaction();
		if(acceptswitchList!=null && acceptswitchList.size() > 0){
			// Update Status
			AcceptSwitch acceptswitch = acceptswitchList.get(0);
			acceptswitch.setStatus(status);
			acceptswitch.setModifyUser(this.getUsername());
			acceptswitch.setModifyDate(CateringServiceUtil.getCurrentTimestamp());
			this.dbSession.saveOrUpdate(acceptswitch);
		} else {
			// Insert one AcceptSwitch record
			acceptswitchRecord.setStatus(status);
			acceptswitchRecord.setCreateUser(this.getUsername());
			acceptswitchRecord.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
			acceptswitchRecord.setModifyUser(this.getUsername());
			acceptswitchRecord.setModifyDate(CateringServiceUtil.getCurrentTimestamp());
			this.dbSession.saveOrUpdate(acceptswitchRecord);
		}
		
		// 停用審核
		if("0".equals("" + status)){
			// 停用除了變更AcceptSwitch的狀態值之外,還需Approve所有供餐審核申請或所有商品上架審核申請
			if ("kitchenVerify".equals(acceptType)) {
				// 供餐審核
				AcceptSchoolKitchenDAO acceptschoolkitchenDAO = new AcceptSchoolKitchenDAO(this.dbSession);
				AcceptSchoolKitchen acceptschoolkitchenRecord = new AcceptSchoolKitchen();
				acceptschoolkitchenRecord.setSchoolId(schoolId); // 搜尋條件 : SchoolId and Status = 0
				acceptschoolkitchenRecord.setStatus("0"); // 狀態(0:待審核, 1審核通過, 2:否決)
				List<AcceptSchoolKitchen> acceptschoolkitchenList = acceptschoolkitchenDAO.findByExample(acceptschoolkitchenRecord);
				if (acceptschoolkitchenList != null && acceptschoolkitchenList.size() > 0) {
					for (AcceptSchoolKitchen acceptschoolkitchen : acceptschoolkitchenList) {
						acceptschoolkitchen.setStatus("1"); // Approve供餐審核申請
						acceptschoolkitchen.setModifyUser(this.getUsername());
						acceptschoolkitchen.setModifyDateTime(CateringServiceUtil.getCurrentTimestamp());
						// 新增供餐廚房
						SchoolkitchenId schoolkitchenId = new SchoolkitchenId(acceptschoolkitchen.getSchoolId(), acceptschoolkitchen.getKitchenId());
						Schoolkitchen schoolkitchen = new Schoolkitchen(schoolkitchenId);
						schoolkitchen.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
						schoolkitchen.setQuantity(acceptschoolkitchen.getQuantity());
						this.dbSession.saveOrUpdate(acceptschoolkitchen);
						this.dbSession.saveOrUpdate(schoolkitchen);
					}
				}
			} else {
				// 商品上架審核
				SfSchoolproductsetDAO sfschoolproductsetDAO = new SfSchoolproductsetDAO(this.dbSession);
				SfSchoolproductset sfschoolproductsetRecord = new SfSchoolproductset();
				sfschoolproductsetRecord.setSchoolId(schoolId); // 搜尋條件 : SchoolId and Status = 0
				sfschoolproductsetRecord.setStatus("0"); // `Status` 狀態(0:待審核, 1審核通過, 2:否決)
				List<SfSchoolproductset> sfschoolproductsetList = sfschoolproductsetDAO.findByExample(sfschoolproductsetRecord);
				if (sfschoolproductsetList != null && sfschoolproductsetList.size() > 0) {
					for(SfSchoolproductset sfschoolproductset : sfschoolproductsetList){
						sfschoolproductset.setStatus("1"); // Approve商品上架審核申請
						sfschoolproductset.setModifyUser(this.getUsername());
						sfschoolproductset.setModifyDateTime(CateringServiceUtil.getCurrentTimestamp());
						this.dbSession.saveOrUpdate(sfschoolproductset);
					}
				}
			}
		}
		tx.commit();

		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}
}
