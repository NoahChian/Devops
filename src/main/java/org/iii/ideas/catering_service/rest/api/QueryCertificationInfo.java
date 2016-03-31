package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Query;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.Restaurant;
import org.iii.ideas.catering_service.dao.RestaurantStockIngredient;
import org.iii.ideas.catering_service.dao.Restaurantingredient;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.service.IngredientService;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryCertificationInfo
		extends
		AbstractApiInterface<QueryCertificationInfoRequest, QueryCertificationInfoResponse> {

	@Override
	public void process() throws NamingException, ParseException {

		/*
		 * select * from menu m , ingredientbatchdata igbd, ingredient
		 * igd,kitchen k,supplier s where m.MenuId = igbd.MenuId and
		 * igbd.IngredientId = igd.IngredientId and m.KitchenId = k.KitchenId
		 * and igbd.SupplierId = s.SupplierId
		 */
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		String uName=this.getUsername();
		String uType=this.getUserType();
		
		String ingredientName = this.requestObj.getIngredientName();
		int kitchenId = Integer.parseInt(this.requestObj.getKitchenId());
		int restaurantId = Integer.parseInt(this.requestObj.getRestaurantId());
		String begDate = this.requestObj.getStartDate();
		String endDate = this.requestObj.getEndDate();
		
//		begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
//		endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
		
		//他校供應的限制 KC  (自設廚房的話用原本的kid就夠了)
		String subHql="";
		if (CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(this.getUserType())){
			subHql=" and b.schoolId=:sid ";
		}
		
		Integer queryLimit = 0;
		
		queryLimit = this.requestObj.getQueryLimit();
		
		int page =this.requestObj.getPage();
		int perPage = this.requestObj.getPerpage();
				
		String HQL="";
		Query query;
		List results = null;
		if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(kitchenId))){
			

			HQL ="FROM Ingredientbatchdata igdb,Supplier s,Kitchen k,Batchdata b  "
					+ "where igdb.supplierId = s.id.supplierId "
					+ "and igdb.batchDataId=b.batchDataId "
					+ "and b.kitchenId = k.kitchenId  "
					+ "and k.kitchenId = :kitchenId "
					+ "and b.menuDate between :begdate and :enddate "
					+ "and igdb.ingredientName like :ingredientName "
					+ subHql 
					+ "order by b.menuDate ";
			
			query = dbSession.createQuery(HQL);
			query.setParameter("begdate", begDate);
			query.setParameter("enddate", endDate);
			query.setParameter("kitchenId", kitchenId);
			query.setParameter("ingredientName", "%"+ingredientName+"%");
			
			//限制他校供應的學校只能查自己  20140505 KC 
			if (CateringServiceCode.USERTYPE_SCHOOL_NO_KITCHEN.equals(uType)){
				Integer userSid=AuthenUtil.getSchoolIdByUsername(uName);
				query.setParameter("sid", userSid);
			}
			results = query.list();
		} else if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(restaurantId))){
			//TODO 餐廳
			
			HQL ="FROM RestaurantStockIngredient rsi,Restaurantingredient ri,Restaurant r,Supplier s "
					+ "where rsi.ingredientId = ri.ringredientId "
					+ "and rsi.restaurantId = r.restaurantId "
					+ "and ri.supplierCompanyId = s.companyId "
					+ "and rsi.restaurantId = s.id.kitchenId and s.suppliedType = 1 "
					+ "and rsi.restaurantId = :restaurantId "
					+ "and rsi.useDate between :begdate and :enddate "
					+ "and ri.ringredientName like :ingredientName "
					+ "order by rsi.useDate ";
			
			query = dbSession.createQuery(HQL);
			query.setParameter("begdate", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd HH:mm:ss", begDate + " 00:00:00"));
			query.setParameter("enddate", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd HH:mm:ss", endDate + " 23:59:59"));
			query.setParameter("restaurantId", restaurantId);
			query.setParameter("ingredientName", "%"+ingredientName+"%");
			
			results = query.list();
		}
		
        Iterator<Object[]> iterator = results.iterator();		
		int i =0;
		while (iterator.hasNext()) {
//			SupplierAndMenuDateObject sam = new SupplierAndMenuDateObject();
			Object[] obj = iterator.next();
			if(page != 0 && perPage !=0){
				int startIndex = (page-1) * perPage;
				int endIndex = startIndex + perPage;
				if(i>=startIndex && i<endIndex){
					if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(kitchenId))){
						genCertification(obj);
					} else if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(restaurantId))){
						genCertificationRest(obj);
					}
				}
			} else {
				if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(kitchenId))){
					genCertification(obj);
				} else if(!CateringServiceCode.QUERY_PARAMETER_NO_ENTER.equals(Integer.toString(restaurantId))){
					genCertificationRest(obj);
				}
			}
			i++;
		}
		this.responseObj.setTotalCol(i);
	}
	
	public void genCertificationRest(Object[]obj) throws ParseException{
		RestaurantStockIngredient rsi = (RestaurantStockIngredient) obj[0];
		Restaurantingredient ri = (Restaurantingredient) obj[1];
		Restaurant r = (Restaurant) obj[2];
		Supplier s = (Supplier) obj[3];
		
//		 rsi.UseDate,r.RestaurantName,s.SupplierName,ri.RIngredientName,
//			rsi.lotNumber,rsi.IngredientAttr,rsi.expirationDate,rsi.stockDate,rsi.manufactureDate
    	
    	CertificationList cl1 = new CertificationList();
    	
    	if(rsi.getUseDate()!=null){
    		Timestamp ts = new Timestamp(rsi.getUseDate().getTime());
			cl1.setMenuDate( CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", ts))  ;
    	}

		cl1.setIngredientId(Long.valueOf(rsi.getIngredientId()));
		cl1.setKitchenName(r.getRestaurantName());
		cl1.setKitchenId(r.getRestaurantId());
		cl1.setSupplyName(s.getSupplierName());
		cl1.setIngredientName(ri.getRingredientName());
//		cl1.setIngredientBatchId(); //TODO
		cl1.setIngredientAttribute( CateringServiceUtil.getIngredientAttrBo(rsi.getIngredientAttr()));
		if(rsi.getExpirationDate()!=null){
    		Timestamp ts = new Timestamp(rsi.getExpirationDate().getTime());
			cl1.setExpirationDate( CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", ts))  ;
		}
		if(rsi.getStockDate()!=null){
    		Timestamp ts = new Timestamp(rsi.getStockDate().getTime());
			cl1.setStockDate( CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", ts));
		}
		if(rsi.getManufactureDate()!=null){
    		Timestamp ts = new Timestamp(rsi.getManufactureDate().getTime());
			cl1.setManufactureDate( CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", ts));
		}
		cl1.setCertificationId(ri.getCertificationNo());//TODO
		cl1.setLotNumber(rsi.getLotNumber()!=null ? rsi.getLotNumber():"");
		
		IngredientService ingredientService = new IngredientService(dbSession);
		
		String fileName = r.getRestaurantId() + "-"+ ri.getSupplierCompanyId() + "-"+ rsi.getIngredientId();
		String downloadPath = ingredientService.getIngredientInspectionDownloadPath(fileName);
		if (!CateringServiceUtil.isEmpty(downloadPath)) {
			cl1.setFileExist("1");
			cl1.setFileName(fileName);
		} else {
			cl1.setFileExist("0");
		}
		
		this.responseObj.getCertificationList().add(cl1);
	}
	

	public void genCertification(Object[]obj) throws ParseException{
		Ingredientbatchdata igdb = (Ingredientbatchdata) obj[0];
		Supplier supplier = (Supplier) obj[1];
    	Kitchen kitchen = (Kitchen) obj[2];
    	Batchdata batchdata = (Batchdata) obj[3];
    	
    	CertificationList cl1 = new CertificationList();
		cl1.setIngredientId(igdb.getIngredientId());
		cl1.setMenuDate(batchdata.getMenuDate());
		cl1.setKitchenName(kitchen.getKitchenName());
		cl1.setKitchenId(kitchen.getKitchenId());
		cl1.setSupplyName(supplier.getSupplierName());
		cl1.setIngredientName(igdb.getIngredientName());
		cl1.setIngredientBatchId(igdb.getIngredientBatchId());
		cl1.setIngredientAttribute( CateringServiceUtil.getIngredientAttrBo(igdb.getIngredientAttr()));
		if(igdb.getExpirationDate()!=null){
			cl1.setExpirationDate( CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", igdb.getExpirationDate()))  ;
		}
		if(igdb.getStockDate()!=null){
			cl1.setStockDate( CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", igdb.getStockDate()));
		}
		if(igdb.getManufactureDate()!=null){
			cl1.setManufactureDate( CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", igdb.getManufactureDate()));
		}
		cl1.setCertificationId(igdb.getCertificationId());
		cl1.setLotNumber(igdb.getLotNumber());
		
		IngredientService ingredientService = new IngredientService(dbSession);
		String fileName = String.valueOf(igdb.getIngredientBatchId());
		String downloadPath = ingredientService.getIngredientInspectionDownloadPath(fileName);
		if (!CateringServiceUtil.isEmpty(downloadPath)) {
			cl1.setFileExist("1");
			cl1.setFileName(fileName);
		} else {
			cl1.setFileExist("0");
		}
		
		this.responseObj.getCertificationList().add(cl1);
	}
}