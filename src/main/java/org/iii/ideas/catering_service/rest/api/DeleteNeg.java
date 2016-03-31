package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;

import javax.naming.NamingException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class DeleteNeg extends AbstractApiInterface<DeleteNegRequest, DeleteNegResponse>{

	@Override
	public void process() throws NamingException, HibernateException, ParseException {
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		Integer idneg_ingredientId =Integer.valueOf(this.requestObj.getIdneg_ingredientId());
		log.info("DeleteNeg idneg_ingredientId:"+idneg_ingredientId);
		Transaction tx = dbSession.beginTransaction();
		Query query = dbSession.createQuery("delete from NegIngredient n "
				+ "where n.id.negIngredientId = :negIngredientId "
				+ "and n.id.stockDate = :stockDate "
				+ "and n.lotNumber = :lotNumber");
		query.setParameter("negIngredientId", idneg_ingredientId);
		query.setParameter("stockDate", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", this.requestObj.getStockDate())  );
		query.setParameter("lotNumber", this.requestObj.getLotNumber());
		query.executeUpdate();
		tx.commit();
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

}
