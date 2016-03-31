package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
//7.	主管機關需求-搜尋異常條件
public class QueryForNeg extends AbstractApiInterface<QueryForNegRequest, QueryForNegResponse>{

	@Override
	public void process() throws NamingException, HibernateException, ParseException {
/*
SELECT * FROM test.ingredientbatchdata igbd,menu m,supplier s
 where igbd.IngredientName like '%%'
   and igbd.SupplierId = s.SupplierId
   and m.MenuId=igbd.MenuId
   and m.MenuDate between '2013/01/01' and '2013/12/31'
   
      "supplyName": "第一家", 
"ingredientName": "雞腿",
        "startDate":"2013/09/01",
        "endDate":"2013/09/30"

 */
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		
		String supplyName =this.requestObj.getSupplyName();
		String ingredientName =this.requestObj.getIngredientName();
		//String begDate =this.requestObj.getStartDate();
		//String endDate =this.requestObj.getEndDate();
		String begDate = this.requestObj.getStartDate();
		String endDate = this.requestObj.getEndDate();
		begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
		endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
		
		log.debug("QueryForNeg condition supplierName:"+supplyName+" ingredientName:"+ingredientName+" beg:"+begDate+" endDate:"+endDate);
		if(CateringServiceUtil.isEmpty(begDate) || CateringServiceUtil.isEmpty(endDate) ){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認查詢參數無誤");
			return;
		}
		
		String HQL = "select distinct igbd.ingredientId,igbd.ingredientName,igbd.lotNumber,igbd.expirationDate,igbd.stockDate,igbd.manufactureDate,s.id.supplierId,s.supplierName"
				+ " FROM Ingredientbatchdata as igbd, Batchdata as m,Supplier as s "
				+ "where m.batchDataId =igbd.batchDataId "
				+ "and igbd.supplierId = s.id.supplierId  "
				+ "and igbd.ingredientName like :ingredientName "
				+ "and s.supplierName like :supplierName "
				+ "and m.menuDate between :begdate and :enddate ";
		
		Query query = dbSession.createQuery(HQL);
		query.setParameter("begdate", begDate );
		query.setParameter("enddate", endDate );
		query.setParameter("ingredientName","%"+ ingredientName+"%");
		query.setParameter("supplierName","%"+ supplyName+"%");
		List results = query.list();
        Iterator<Object[]> iterator = results.iterator();
      
        
        log.debug("QueryForNeg Size:"+results.size());
		while (iterator.hasNext()) {
			SupplierAndMenuDateObject sam = new SupplierAndMenuDateObject();
			Object[] obj = iterator.next();
			/*
			Ingredientbatchdata ingredientbatchdata = (Ingredientbatchdata) obj[0];
			Batchdata menu = (Batchdata) obj[1];
			Supplier supplier = (Supplier) obj[2];
			Integer ingredientId = ingredientbatchdata.getIngredientId();
			String tmpIngredientName = ingredientbatchdata.getIngredientName();
			String lotNumber = ingredientbatchdata.getLotNumber();
			Timestamp expirationDate = ingredientbatchdata.getExpirationDate();
			Timestamp stockDate = ingredientbatchdata.getStockDate();
			Timestamp manufactureDate = ingredientbatchdata.getManufactureDate();
			Integer supplierId = supplier.getId().getSupplierId();
			String supplierName = supplier.getSupplierName();
			*/
			Integer ingredientId =(Integer) obj[0];
			String tmpIngredientName =(String) obj[1];
			String lotNumber =(String) obj[2];
			Timestamp expirationDate =(Timestamp) obj[3];
			Timestamp stockDate = (Timestamp) obj[4];
			Timestamp manufactureDate = (Timestamp) obj[5];
			Integer supplierId =(Integer) obj[6];
			String supplierName =(String)  obj[7];
        	
			ForNegList fnl1 = new ForNegList();
			fnl1.setIngredientId(ingredientId);
			fnl1.setSupplierId(supplierId);
			fnl1.setSupplyName(supplierName);
			fnl1.setIngredientName(tmpIngredientName);
			fnl1.setLotNumber(lotNumber);
			if(expirationDate==null){
				fnl1.setExpirationDate("");
			}else{
				fnl1.setExpirationDate(CateringServiceUtil.converTimestampToStr("yyyy/MM/dd",  expirationDate   ));
			}
			if(stockDate==null){
				fnl1.setStockDate("");
			}else{	
				fnl1.setStockDate(CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", stockDate));
			}
			if(manufactureDate==null){
				fnl1.setManufactureDate("");
			}else{
				fnl1.setManufactureDate(CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", manufactureDate));
			}
			this.responseObj.getForNegList().add(fnl1);   	
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
