package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Query;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.NegIngredient;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class ListNeg extends AbstractApiInterface<ListNegRequest, ListNegResponse>{

	@Override
	public void process() throws NamingException, ParseException {
/*
 * SELECT * FROM neg_ingredient nigbd,ingredientbatchdata igbd,menu m,supplier s
 where igbd.SupplierId = s.SupplierId
   and nigbd.neg_ingredientId = igbd.IngredientId
   and m.MenuId=igbd.MenuId
   and m.MenuDate between '2013/01/01' and '2013/12/31'
 */
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		Query query = dbSession.createQuery("FROM NegIngredient n,Ingredient as igbd,Supplier as s  "
				+ "where  igbd.supplierId = s.id.supplierId "
				+ "and n.supplierId = s.id.supplierId "
				+ "and n.id.negIngredientId = igbd.ingredientId "
				+ "");
		List results = query.list();
        Iterator<Object[]> iterator = results.iterator();
        
        log.debug("QueryForNeg Size:"+results.size());
		while (iterator.hasNext()) {
			SupplierAndMenuDateObject sam = new SupplierAndMenuDateObject();
			Object[] obj = iterator.next();
			Ingredient ingredient = (Ingredient) obj[1];
			Supplier supplier = (Supplier) obj[2];
			NegIngredient neg = (NegIngredient) obj[0];
			NegList nl = new NegList();
			log.debug("negbeg:"+neg.getBegDate());
			nl.setBeg_date(neg.getBegDate()==null?null:  HibernateUtil.converTimestampToStr("yyyy/MM/dd",neg.getBegDate()));
			nl.setEnd_date(neg.getEndDate()==null?null:  HibernateUtil.converTimestampToStr("yyyy/MM/dd",neg.getEndDate()));
			nl.setDescription(neg.getDescription());
			nl.setExpirationDate(neg.getExpirationDate()==null?null:  HibernateUtil.converTimestampToStr("yyyy/MM/dd",neg.getExpirationDate()) );
			nl.setManufactureDate(neg.getManufactureDate()==null?null:  HibernateUtil.converTimestampToStr("yyyy/MM/dd",neg.getManufactureDate()) );
			nl.setStockDate(neg.getId().getStockDate()==null?null:  HibernateUtil.converTimestampToStr("yyyy/MM/dd",neg.getId().getStockDate()) );
			nl.setIdneg_ingredientId(neg.getId().getNegIngredientId());
		//	nl.setIngredientBatchId(ingredientbatchdata.getIngredientBatchId());
			nl.setIngredientId(neg.getId().getNegIngredientId());
			//HibernateUtil.querySupplierById(session, ingredientbatchdata.getIngredientName())
			nl.setIngredientName(ingredient.getIngredientName());
			nl.setSupplierId(supplier.getId().getSupplierId());
			nl.setSupplyName(supplier.getSupplierName());
			nl.setLotNumber(neg.getLotNumber());
			
			this.responseObj.getNegList().add(nl);
		}
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
		
	}

}
