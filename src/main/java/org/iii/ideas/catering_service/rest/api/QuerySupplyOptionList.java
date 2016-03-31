package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Supplier;

public class QuerySupplyOptionList extends
		AbstractApiInterface<QuerySupplyOptionListRequest, QuerySupplyOptionListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		Criteria criteria = dbSession.createCriteria(Supplier.class);
		if(this.getUsername()!=null){
			criteria.add(Restrictions.eq("id.kitchenId",this.getKitchenId()));
		}
		List suppliers = criteria.list();
		Iterator<Supplier> iterator = suppliers.iterator();
		while (iterator.hasNext()) {
			SupplyNameOption supplierObj = new SupplyNameOption();
			Supplier supplier = iterator.next();
			supplierObj.setSupplyId(String.valueOf(supplier.getId().getSupplierId()));
			supplierObj.setSupplyName(supplier.getSupplierName());
			this.responseObj.getSupplyNameOption().add(supplierObj);
		}
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
