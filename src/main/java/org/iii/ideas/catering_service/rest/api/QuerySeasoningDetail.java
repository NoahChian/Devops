package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.Menu;
import org.iii.ideas.catering_service.dao.SeasoningStockData;
import org.iii.ideas.catering_service.dao.SeasoningStockDataDAO;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.rest.bo.SeasoningStockDataBO;
import org.iii.ideas.catering_service.util.BoUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class QuerySeasoningDetail extends AbstractApiInterface<QuerySeasoningDetailRequest, QuerySeasoningDetailResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4510629835391043729L;

	@Override
	public void process() throws NamingException, ParseException {
		// TODO Auto-generated method stub
//		if (!this.isLogin()) {
//			this.responseObj.setResStatus(-2);
//			this.responseObj.setMsg("使用者未授權");
//			return;
//		}
//		Long seasoningId = this.requestObj.getseaseasoningStockId();
//		SeasoningStockDataDAO seasoningDAO = new SeasoningStockDataDAO(this.dbSession);
////		SeasoningStockData seasoningData = seasoningDAO.querySeasoningBySeasoningId(seasoningId);
//		SeasoningStockDataBO ssdBO = new SeasoningStockDataBO();
//		IngredientAttributeBO seasoningAttr = CateringServiceUtil.getIngredientAttrBo(seasoningData.getIngredientAttr());
//		ssdBO = BoUtil.transSeasoningStockDataToSeasoningStockDataBO(seasoningData);
//		try{
//			this.responseObj.setSeasoningData(ssdBO);
//			this.responseObj.setIgredientAttrBO(seasoningAttr);
//			this.responseObj.setResStatus(1);
//			this.responseObj.setMsg("");
//		}catch (Exception ex) {
//			this.responseObj.setResStatus(0);
//			this.responseObj.setMsg(ex.getMessage());
//		}
		
	}

}
