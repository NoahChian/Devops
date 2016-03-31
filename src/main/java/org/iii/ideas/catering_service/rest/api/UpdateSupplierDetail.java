package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.code.ActiveTypeCode;
import org.iii.ideas.catering_service.rest.bo.SupplierBO;
import org.iii.ideas.catering_service.service.SupplierService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.LogUtil;

public class UpdateSupplierDetail extends AbstractApiInterface<UpdateSupplierDetailRequest, UpdateSupplierDetailResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -486243880820844589L;

	@Override
	public void process() throws NamingException {

		String actType = this.requestObj.getActiveType();
		SupplierBO supplierBo = this.requestObj.getSupplierBo();
		
		
		// 檢核登入
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		Integer kitchenId = getKitchenId();
		supplierBo.setKitchenId(kitchenId);
		
		// 檢核ACT Type
		if (CateringServiceUtil.isEmpty(actType)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("API動作錯誤");
			return;
		}
		
		//檢核傳入參數
		if (supplierBo==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("參數傳遞錯誤");
			return;
		}

		//基本空值檢核
		String errMsg = validateColumn(actType, supplierBo);
		if (!CateringServiceUtil.isEmpty(errMsg)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(errMsg);
			return;
		}
		
		
		
		Transaction tx = dbSession.beginTransaction();
		
		SupplierService supplierService = new SupplierService(dbSession);
		// 開始新增/修改資料
		try {
			supplierService.updateSupplierDetail(actType.trim().toLowerCase(), supplierBo);
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(ex.getMessage());
			return;
		}

		
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

	//檢核基本傳入參數
	private String validateColumn(String activeType,SupplierBO bo){
		String errMsg = "";
		
		if(ActiveTypeCode.UPDATE.equals(activeType.toLowerCase())){
			if(bo.getSupplierId() == null)
				errMsg = LogUtil.putErrorMsg(errMsg, "無此供應商");
		}
			
		if(CateringServiceUtil.isEmpty(bo.getCompanyId()))
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入供應商統編");
		
		if(CateringServiceUtil.isEmpty(bo.getSupplierName()))
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入供應商名稱");
		
		if(CateringServiceUtil.isEmpty(bo.getTel()))
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入連絡電話");
		
//		if(bo.getCountyId()==0)
//			errMsg = LogUtil.putErrorMsg(errMsg, "請選縣市別");
//		
//		if(bo.getAreaId()==0)
//			errMsg = LogUtil.putErrorMsg(errMsg, "請選擇市/區別");
		
		if(CateringServiceUtil.isEmpty(bo.getAddress()))
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入地址");
		
		return errMsg;
	}
	
}
