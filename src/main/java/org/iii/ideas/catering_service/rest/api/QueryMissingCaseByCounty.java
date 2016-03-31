package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;





//import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metamodel.relational.Size;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.NoMenuDate;
import org.iii.ideas.catering_service.dao.NoMenuDateDAO;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SeasoningStockDataDAO;
//import org.hibernate.Session;
//import org.hibernate.Criteria;
//import org.hibernate.SQLQuery;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Projections;
//import org.hibernate.criterion.Restrictions;
//import org.iii.ideas.catering_service.dao.Batchdata;
//import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
//import org.iii.ideas.catering_service.util.HibernateUtil;


public final class QueryMissingCaseByCounty extends AbstractApiInterface<QueryMissingCaseByCountyRequest, QueryMissingCaseByCountyResponse> {

	/**
	 * 20140731 Ric
	 * 用CountyId去取得供應該縣市所有學校的廚房之缺漏資料:
		 * 缺漏資料查詢
		 * 有菜色無食材
		 * 無調味料
		 * 無菜色照片
	 */
	@Override
	public void process() throws NamingException, ParseException {
		//boolean contains = CateringServiceCode.white_IP().contains(this.getRemoteIp());
		boolean contains = CateringServiceCode.WHITE_IP.contains(this.getRemoteIp());
		System.out.println(this.getRemoteIp());
		//System.out.println(CateringServiceCode.white_IP());
		if (this.getUsername() == null&&contains == false) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		String queryDate = this.requestObj.getQueryDate();
		queryDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", queryDate));
		if (CateringServiceUtil.isEmpty(queryDate)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認日期填寫正確!!");
			return;
		}
		this.responseObj.setQueryDate(queryDate);
		//帶入使用者名稱，轉換為縣市代碼
		Integer countyID = AuthenUtil.getCountyNumByUsername(this.requestObj.getUser_Name());
		List<Object[]> kitchenList = HibernateUtil.queryKitchenListByCountyId(this.dbSession, countyID);
		Iterator<Object[]> kl=kitchenList.iterator();
		if(kitchenList.size()==0){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查無廚房資料!");
		}
		while(kl.hasNext()){
			Object[] kit=kl.next();
			MissingCaseByCountyList mcl = new MissingCaseByCountyList();
			mcl.setKitchenName((String)kit[1]);
			int kid =  Integer.valueOf(kit[0].toString());
			//檢查廚房有沒有菜單
			Criteria criteriaBd = this.dbSession.createCriteria(Batchdata.class);
			criteriaBd.add(Restrictions.eq("menuDate", queryDate));
			criteriaBd.add(Restrictions.eq("kitchenId",kid ));
			List<Batchdata> bd = criteriaBd.list();
			if (bd.size() < 1 ) {
				mcl.setKitchenName((String)kit[1]+" 今日無菜單");
			}
//			if (kid == 9797){
//				System.out.println(kid);
//			}
			// 20140612 Ric 有菜色無食材
			List<Object[]> nullIngredient = HibernateUtil.queryNullIngredientBySchoolAndTime(this.dbSession, queryDate, queryDate, countyID, kid);
			String nullIngredients ="";
			if(nullIngredient.size()==0){
				//mcl.setNullIngredient("");
			}
			Iterator<Object[]> ni=nullIngredient.iterator();
			while(ni.hasNext()){
				Object[] row=ni.next();
				nullIngredients += row[0]+",";
			}
			mcl.setNullIngredient(nullIngredients);
			// 20140623 Ric 食材資料未完整填寫
			List<Object[]> nullIngredientDataList = HibernateUtil.queryNullIngredientDataByKitchenAndTime(this.dbSession, queryDate, queryDate, kid);
			String nullIngredientDataLists ="";
			if(nullIngredientDataList.size()==0){
				//mcl.setNullIngredientData("");
			}
			Iterator<Object[]> nd=nullIngredientDataList.iterator();
			while(nd.hasNext()){
				Object[] row=nd.next();
				nullIngredientDataLists += row[3].toString()+"("+row[0].toString()+"),";
			}
			mcl.setNullIngredientData(nullIngredientDataLists);
			// 20140612 Ric 無調味料
			List<Object[]> nullSeasioning = HibernateUtil.queryNullSeasoning(this.dbSession, queryDate, queryDate, countyID, kid);
			String nullSeasionings ="";
			if(nullSeasioning.size()==0){
				//mcl.setNullSeasoning("");
			}
			Iterator<Object[]> ns=nullSeasioning.iterator();
			//若seasoningStcok也沒資料才寫入提醒 
			SeasoningStockDataDAO seasoningDAO = new SeasoningStockDataDAO(this.dbSession);
			
			while(ns.hasNext()){
				Object[] row=ns.next();
				if(seasoningDAO.queryNullSeasoning((String)row[1], kid)){
					nullSeasionings += row[0].toString()+",";
				}
			}
			mcl.setNullSeasoning(nullSeasionings);
			// 20140612 Ric 無菜色無照片
			List<Object[]> dishList = HibernateUtil.queryDishList(this.dbSession, queryDate, queryDate, kid);
			
			String nullDishPicss ="";
			if (dishList.size() < 1 ) {
				//mcl.setNullDishpic("");
				nullDishPicss +="";
			}else{
				Iterator<Object[]> dl=dishList.iterator();
				while(dl.hasNext()){
					Object[] row=dl.next();
					//NullDishpicList ndl = new NullDishpicList();
					Long did = Long.valueOf(row[0].toString());
					if ("0".equals(row[0].toString())){  //20141001 KC
						continue; //dishid=0的表示沒有圖片存檔 (介接來的)
					}
					try {
						// 暫時新舊方法同時並存判斷圖片是否存在   之後改掉
						// add by Joshua 2014/10/21
						CateringServiceUtil cateringServiceUtil = new CateringServiceUtil(dbSession);
						boolean picExist = false;
						/** Joshua 改寫圖片判斷方式 2014/10/16 */
						if(cateringServiceUtil.isDishImageFileNameExists(kid, did)){
							picExist = true;
//							nullDishPicss +="";
						}else{
//							nullDishPicss +=row[1].toString()+ ",";   先註解  之後開放
						}
						
						if(!picExist){
							if(CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kid, did, 130, 150,"png")))
							{
								picExist = true;
//								nullDishPicss +="";
							}else if(CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kid, did, 130, 150, "jpg"))){
								picExist = true;
//								nullDishPicss +="";
							}else {
//								nullDishPicss +=row[1].toString()+ ",";
							}
						}
						
						if(!picExist){
							nullDishPicss +=row[1].toString()+ ",";
						}else{
							nullDishPicss +="";
						}
						
					} catch (Exception e) {
						nullDishPicss +=row[1].toString()+ ",";
						e.printStackTrace();
					}
				}
			}
			mcl.setNullDishpic(nullDishPicss);
			// 20150629 有無設定不供餐
			NoMenuDateDAO nmDAO  = new NoMenuDateDAO(this.dbSession);
			StringBuilder nmSchoolList = nmDAO.queryNoMenuDateByKid(kid,queryDate);
			
			mcl.setIsNoMenuDate(nmSchoolList.toString());
			
			this.responseObj.getMissingCaseByCountyList().add(mcl);
			}
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
}