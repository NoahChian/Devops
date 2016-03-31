package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.service.IngredientService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class DeleteIngredientInspectionFile extends AbstractApiInterface<DeleteIngredientInspectionFileRequest, DeleteIngredientInspectionFileResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -486243880820844589L;

	@Override
	public void process() throws NamingException {
		String menuDate;
		Long ingredientId;
		String supplierCompanyId;
		String stockDate;
		String lotNumber;
		String errMsg = "";
		
		
		// 檢核登入
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		//檢核Request
		errMsg = validateRequestParam(this.requestObj);
		
		if(!CateringServiceUtil.isEmpty(errMsg)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(errMsg);
			return;
		}
		
		menuDate = this.requestObj.getMenuDate();
		ingredientId = this.requestObj.getIngredientId();
		supplierCompanyId = this.requestObj.getSupplierCompanyId();
		stockDate = this.requestObj.getStockDate();
		lotNumber = this.requestObj.getLotNumber(); 
		
		IngredientService idService = new IngredientService(dbSession);
		Transaction tx = dbSession.beginTransaction();
		try {
			idService.deleteIngredientInspection(getKitchenId(), menuDate, ingredientId, supplierCompanyId, lotNumber, stockDate);
			tx.commit();
		} catch (ParseException e) {
			tx.rollback();
			e.printStackTrace();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("刪除失敗,請洽系統管理員");
			return;
		}

		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

	private String validateRequestParam(DeleteIngredientInspectionFileRequest req){
		String errMsg = "";
		//菜單日期
		if(CateringServiceUtil.isEmpty(req.getMenuDate())){
			errMsg += "傳入參數有誤,菜單日期為空!";
		}
//		if(CateringServiceUtil.isEmpty(req.getStockDate())){
//			errMsg += "傳入參數有誤,進貨日期為空!";
//		}
		if(CateringServiceUtil.isEmpty(req.getSupplierCompanyId())){
			errMsg += "傳入參數有誤,供應商資訊為空!";
		}
		if(CateringServiceUtil.isNull(req.getIngredientId())){
			errMsg += "傳入參數有誤,食材資訊為空!";
		}

		return errMsg;
	}
	
}
