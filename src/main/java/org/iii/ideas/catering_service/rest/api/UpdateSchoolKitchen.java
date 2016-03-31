package org.iii.ideas.catering_service.rest.api;

import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchen;
import org.iii.ideas.catering_service.dao.AcceptSchoolKitchenDAO;
import org.iii.ideas.catering_service.dao.AcceptSwitch;
import org.iii.ideas.catering_service.dao.AcceptSwitchDAO;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.SchoolkitchenId;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.rest.bo.SchoolDataBO;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.iii.ideas.catering_service.util.SchoolAndKitchenUtil;

public class UpdateSchoolKitchen extends AbstractApiInterface<UpdateSchoolKitchenRequest, UpdateSchoolKitchenResponse> {

	@Override
	public void process() throws NamingException {
		
		Schoolkitchen sk ;
		//要回傳SchoolCode
		String schoolCode="";
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		if(this.requestObj.getKitchenIdSC()==null||this.requestObj.getSchoolIdSC()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("資料傳遞有誤");
			return;
		} 
		Criteria criteriaSK = this.dbSession.createCriteria(Schoolkitchen.class);
		criteriaSK.add(Restrictions.eq("id.kitchenId", this.requestObj.getKitchenIdSC()));
		criteriaSK.add(Restrictions.eq("id.schoolId", this.requestObj.getSchoolIdSC()));
		List<Schoolkitchen> skmatching = criteriaSK.list();
		// modify by Ellis 20160118 因不必判斷是否為受供餐學校，故更改僅搜尋School
		//SchoolDataBO schooldataBO = SchoolAndKitchenUtil.querySchoolDataBySchoolId(dbSession, this.requestObj.getSchoolIdSC());
		SchoolDAO sDao = new SchoolDAO(this.dbSession);
		School school = sDao.querySchoolById(this.requestObj.getSchoolIdSC());
		// 1. 新增供餐對象後, 將原先存schoolkitchen動作,改成先存到acceptSchoolKitchen, 其狀態為審核中
		if ("add".equals(this.requestObj.getAct())){ // 新增
			if (skmatching.size() > 0 ) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("不可重複新增學校");
				return;
			}
			
			if (school == null) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("查無此學校對應資料，請洽系統管理員");
				return;
			} else {
				// 記錄schoolCode
				schoolCode = school.getSchoolCode();
				// 可以新增自己的學校 20140829 KC
				boolean isNeedAddSchoolKitchen = false;
				if (!school.getSchoolCode().equals(this.getUsername().substring(1))) {
					//非007亦可新增 modify by Ellis 20160118
//					if (!CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(schooldataBO.getKitchenTypeOfSchool())) {
//						this.responseObj.setResStatus(0);
//						this.responseObj.setMsg("新增的學校並非他校供應或外訂團膳學校，無法新增。");
//						return;
//					}
					// 查詢是否開啟審核流程的開關,如啟用審核,則必需進行審核,如停用審核或查無資料,則直接新增供餐廚房
					AcceptSwitchDAO acceptswitchDAO = new AcceptSwitchDAO(this.dbSession);
					AcceptSwitch acceptswitchRecord = new AcceptSwitch();
					acceptswitchRecord.setSchoolId(this.requestObj.getSchoolIdSC());  // 搜尋條件 : SchoolId and AcceptType = kitchenVerify
					acceptswitchRecord.setAcceptType("kitchenVerify");
					List<AcceptSwitch> acceptswitchList = acceptswitchDAO.findByExample(acceptswitchRecord);
					if (acceptswitchList != null && acceptswitchList.size() > 0) {
						AcceptSwitch acceptswitch = acceptswitchList.get(0);
						if ("0".equals("" + acceptswitch.getStatus())) { // Status 狀態 0:停用 ,1:啟用
							isNeedAddSchoolKitchen = true;
						}
					} else {
						// 查無資料
						isNeedAddSchoolKitchen = true;
					}
				} else { // 供餐廚房當設定供餐給自己時, 應不需審核而自動通過
					isNeedAddSchoolKitchen = true;
				}
				if(isNeedAddSchoolKitchen){
					// 新增供餐廚房
					SchoolkitchenId schoolkitchenId = new SchoolkitchenId(this.requestObj.getSchoolIdSC(), this.requestObj.getKitchenIdSC());
					Schoolkitchen schoolkitchen = new Schoolkitchen(schoolkitchenId);
					schoolkitchen.setCreateDate(CateringServiceUtil.getCurrentTimestamp());
					schoolkitchen.setQuantity(this.requestObj.getSkQuantity());
					schoolkitchen.setOffered(1);
					Transaction tx = this.dbSession.beginTransaction();
					this.dbSession.saveOrUpdate(schoolkitchen);
					tx.commit();
					this.responseObj.setSchoolCode(schoolCode);
					this.responseObj.setResStatus(1);
					this.responseObj.setMsg("");
					return;
				}
			}
			
			// 檢核供餐申請記錄 如狀態為0:待審核或1審核通過 予以踢退
			Criteria criteriaASK = this.dbSession.createCriteria(AcceptSchoolKitchen.class);
			criteriaASK.add(Restrictions.eq("kitchenId", this.requestObj.getKitchenIdSC()));
			criteriaASK.add(Restrictions.eq("schoolId", this.requestObj.getSchoolIdSC()));
			List<AcceptSchoolKitchen> acceptschoolkitchenList = criteriaASK.list();
			if(acceptschoolkitchenList!=null && acceptschoolkitchenList.size() > 0){
				for(AcceptSchoolKitchen record : acceptschoolkitchenList){
					if("1".equals(record.getAction())){ // 1: 申請供餐 , 2:取消供餐, 3:商品上架
						if ("0".equals(record.getStatus())) {
							this.responseObj.setResStatus(0);
							this.responseObj.setMsg("供餐申請記錄審核中，無法新增。");
							return; 
						} else if("1".equals(record.getStatus())){
							this.responseObj.setResStatus(0);
							this.responseObj.setMsg("供餐申請記錄已審核通過，無法新增。");
							return; 
						}
					}
				}
			}
			// 儲存acceptSchoolKitchen
			AcceptSchoolKitchen ask = new AcceptSchoolKitchen();
			ask.setSchoolId(this.requestObj.getSchoolIdSC());
			ask.setKitchenId(this.requestObj.getKitchenIdSC());
			ask.setQuantity(this.requestObj.getSkQuantity());
			ask.setStatus("0"); // 0:待審核, 1審核通過, 2:否決
			ask.setAction("1"); // 1: 申請供餐 , 2:取消供餐, 3:商品上架
			ask.setCreateUser(this.getUsername());
			ask.setCreateDateTime(CateringServiceUtil.getCurrentTimestamp());
			ask.setModifyUser(this.getUsername());
			ask.setModifyDateTime(CateringServiceUtil.getCurrentTimestamp());
			Transaction tx = dbSession.beginTransaction();
			dbSession.saveOrUpdate(ask);
			tx.commit();
			this.responseObj.setAcceptschoolkitchenId(ask.getId());			 
		}else{ // 異動供餐份數
			if (skmatching.size() == 0 ) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("查無此學校! ");
				return;
			}
			sk = new Schoolkitchen();
			sk=skmatching.get(0);
			if(this.requestObj.getOffered()){
				sk.setOffered(1);
			}else{
				sk.setOffered(0);
			}
			// 儲存schoolkitchen
			Transaction tx = dbSession.beginTransaction();
			sk.setQuantity(this.requestObj.getSkQuantity());	
			dbSession.saveOrUpdate(sk);
			tx.commit();
		}
		
		this.responseObj.setSchoolCode(schoolCode);
		this.responseObj.setSchoolName(school.getSchoolName());
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg(""); 
	}

}
