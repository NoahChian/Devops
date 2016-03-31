package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.service.SupplierService;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class DeleteSupplier extends AbstractApiInterface<DeleteSupplierRequest, DeleteSupplierResponse> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1192754778015688402L;

	@Override
	public void process() throws NamingException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		int supplierId  = Integer.valueOf(this.requestObj.getSupplierId());
		int kitchenId = getKitchenId();
		
		if (supplierId==0) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("供應商資訊錯誤");
			return;
		}
		
		
		
		Transaction tx = dbSession.beginTransaction();

		SupplierService supplierService = new SupplierService(dbSession);
		
			
		
		
		// 開始新增/修改資料
		try {
			//先判斷有無被使用
			if(supplierService.isIngredientBatchDataUsed(supplierId, kitchenId)){
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("目前仍有此家供應商所提供的食材資料");
				return;
			}
			
			supplierService.deleteSupplier(supplierId, kitchenId);
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(ex.getMessage());
		}

		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

}
