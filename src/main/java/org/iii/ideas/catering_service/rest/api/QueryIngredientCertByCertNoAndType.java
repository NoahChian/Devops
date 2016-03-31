package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.IngredientbatchdataDAO;
import org.springframework.beans.factory.annotation.Autowired;

public final class QueryIngredientCertByCertNoAndType extends AbstractApiInterface<QueryIngredientCertByCertNoAndTypeRequest, QueryIngredientCertByCertNoAndTypeResponse> {

	@Autowired
	private IngredientbatchdataDAO IngredientbatchdataDAO;

	@Override
	public void process() throws NamingException, ParseException {
		if(this.requestObj.getCertNo()==null){
			this.responseObj.setResStatus(-1);
		}
		
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}

	public boolean haveIngredient(int dishId, int batchDataId) {
		boolean result = true;
		if (dishId != 0) {
			Criteria criteria = dbSession.createCriteria(Ingredientbatchdata.class);
			criteria.add(Restrictions.eq("dishId", dishId));
			criteria.add(Restrictions.eq("batchDataId", batchDataId));
			criteria.setProjection(Projections.rowCount());
			if (((Number)criteria.uniqueResult()).intValue() != 0) {
				result = true;
			} else {
				result = false;
			}

		}
		return result;
	}

	public String transListStringToString(List<String> list) {
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1)
				result += list.get(i).concat(",");
			else
				result += list.get(i);
		}
		return result;
	}

	public IngredientbatchdataDAO getIngredientbatchdataDAO() {
		return IngredientbatchdataDAO;
	}

	public void setIngredientbatchdataDAO(IngredientbatchdataDAO ingredientbatchdataDAO) {
		IngredientbatchdataDAO = ingredientbatchdataDAO;
	}

	

}
