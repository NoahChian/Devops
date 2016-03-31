package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.SupplierDAO;
import org.iii.ideas.catering_service.rest.bo.SupplierBO;
import org.iii.ideas.catering_service.util.BoUtil;

public class QuerySupplierDetail extends
		AbstractApiInterface<QuerySupplierDetailRequest, QuerySupplierDetailResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6856577536053080669L;

	@Override
	public void process() throws NamingException, ParseException {
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		int supplierId = this.requestObj.getSupplierId();
		int kitchenId = getKitchenId();
		
		try {
			SupplierDAO supplierDao = new SupplierDAO(dbSession);
			Supplier supplier = supplierDao.querySupplierById(supplierId,kitchenId);
			
			if(supplier == null){
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("查無此供應商資訊!");
				return;
			}
			
			SupplierBO bo = BoUtil.transSupplierToSupplierBo(supplier);

			this.responseObj.setSupplierBo(bo);
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
			
		} catch (Exception ex) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(ex.getMessage());
		}
	}

}
