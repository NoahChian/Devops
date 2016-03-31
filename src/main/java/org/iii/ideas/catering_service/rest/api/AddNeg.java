package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.NegIngredient;
import org.iii.ideas.catering_service.dao.NegIngredientId;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class AddNeg extends AbstractApiInterface<AddNegRequest, AddNegResponse>{
	@Override
	public void process() throws NamingException, ParseException {
/*
 * SELECT * FROM test.neg_ingredient;INSERT INTO `test`.`neg_ingredient`
(`neg_ingredientId`,
`SupplierId`,
`beg_date`,
`end_date`,
`StockDate`,
`ManufactureDate`,
`ExpirationDate`,
`LotNumber`,
`Description`)
VALUES
(1,
12345678,
'2013/12/12',
'2013/12/14',
'2013/12/16',
'2013/12/10',
'2013/12/20',
1,
'Description');

 */
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		if(this.requestObj.getIngredientId()==null || this.requestObj.getStockDate()==null|| "".equals(this.requestObj.getStockDate())){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("進貨日期或食材ID不可為空 ");
			return;
		}
		
		
		Transaction tx = dbSession.beginTransaction();
		
		Integer ingredientId =Integer.valueOf(this.requestObj.getIngredientId());
		Ingredient negIntegredient = HibernateUtil.queryIngredientById(this.dbSession, ingredientId);
		
		Criteria criteriaSp = this.dbSession.createCriteria(NegIngredient.class);
		criteriaSp.add(Restrictions.eq("id.negIngredientId", ingredientId));
		criteriaSp.add(Restrictions.eq("id.stockDate", HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", this.requestObj.getStockDate()) ));
		List<Ingredientbatchdata> ingredientbatchdataIterator = criteriaSp.list();
		if (ingredientbatchdataIterator.size() > 0 ) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("資料已存在  食材:"+negIntegredient.getIngredientName()+" 進貨日期:"+this.requestObj.getStockDate());
			return;
		}
		//Integer supplierId =Integer.valueOf(this.requestObj.getSupplierId());
		//Integer ingredientBatchId =Integer.valueOf(this.requestObj.getIngredientBatchId());
		//String expirationDate =this.requestObj.getExpirationDate();
		//String description =this.requestObj.getDescription();

		NegIngredient neg = new NegIngredient();
		if(!CateringServiceUtil.isEmpty(requestObj.getStartDate())  ){
			neg.setBegDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", requestObj.getStartDate()) );
		}
		if(!CateringServiceUtil.isEmpty(requestObj.getEndDate())  ){
			neg.setEndDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", requestObj.getEndDate()) );
		}
		if(!CateringServiceUtil.isEmpty(requestObj.getManufactureDate())  ){
			neg.setManufactureDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", requestObj.getManufactureDate()) );
		}
		if(!CateringServiceUtil.isEmpty(this.requestObj.getExpirationDate())  ){
			neg.setExpirationDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", this.requestObj.getExpirationDate()) );
		}
		neg.setDescription(this.requestObj.getDescription());
		//Ingredientbatchdata igbd= HibernateUtil.queryIngredientbatchdataById(dbSession,Integer.valueOf(this.requestObj.get));
		
		Ingredient igbd = HibernateUtil.queryIngredientById(dbSession,Integer.valueOf( this.requestObj.getIngredientId()) );
		if(igbd==null){
			this.responseObj.setMsg("找不到此食材資料 學校食材ID:"+this.requestObj.getIngredientId());
			this.responseObj.setResStatus(0);
			return;
		}
		if(requestObj.getLotNumber()==null){
			neg.setLotNumber(CateringServiceUtil.defaultLotNumber);
		}else{
			neg.setLotNumber(requestObj.getLotNumber());
		}
		//neg.getId().setNegIngredientId(negIngredientId);
		NegIngredientId negIngredientId = new NegIngredientId();
		negIngredientId.setStockDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", this.requestObj.getStockDate()));
		negIngredientId.setNegIngredientId(ingredientId);
		neg.setId(negIngredientId);
	//	neg.setStockDate(HibernateUtil.convertStrToTimestamp("yyyy/MM/dd", this.requestObj.getStockDate()) );
		neg.setSupplierId(negIntegredient.getSupplierId());
	//	neg.setNegIngredientId(ingredientId);
		dbSession.save(neg);
		
		tx.commit();
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
	

}
