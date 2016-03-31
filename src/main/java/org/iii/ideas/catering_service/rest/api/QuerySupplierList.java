package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.SupplierDAO;
/**
 * 查詢供應商列表
 * @author Raymond 20140527
 *
 */

public class QuerySupplierList extends AbstractApiInterface<QuerySupplierListRequest, QuerySupplierListResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6856577536053080669L;

	@Override
	public void process() throws NamingException, ParseException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}

		String supplierName = this.requestObj.getSupplierName();
		int pageIndex = this.requestObj.getPageNum(); // 頁數
		int pageLimit = this.requestObj.getPageLimit(); // 每頁顯示總行數
		int kitchenId = getKitchenId();
		int totalCount = 0;

		try {
			List<Supplier> supplierList = null;
			SupplierDAO supplierDao = new SupplierDAO(dbSession);

			totalCount = supplierDao.queryTotelSupplierCount(supplierName, kitchenId);

			if (totalCount != 0)
				supplierList = supplierDao.querySupplierListPager(supplierName, kitchenId, pageIndex, pageLimit);

			this.responseObj.setTotalNum(totalCount);
			this.responseObj.setSupplierList(supplierList);
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");

		} catch (Exception ex) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(ex.getMessage());
		}
	}

}
