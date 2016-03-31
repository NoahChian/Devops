package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
//import javax.persistence.Query;








import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.Schoolkitchen;
import org.iii.ideas.catering_service.dao.SchoolkitchenDAO;
import org.iii.ideas.catering_service.dao.SeasoningStockData;
import org.iii.ideas.catering_service.dao.SeasoningStockDataDAO;
import org.iii.ideas.catering_service.dao.CodeDAO;
import org.iii.ideas.catering_service.dao.NoMenuDateDAO;
//import org.hibernate.Session;
//import org.hibernate.Criteria;
//import org.hibernate.SQLQuery;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Projections;
//import org.hibernate.criterion.Restrictions;
//import org.iii.ideas.catering_service.dao.Batchdata;
//import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
//import org.iii.ideas.catering_service.util.HibernateUtil;


public final class QueryMissingCase_V2 extends AbstractApiInterface<QueryMissingCase_V2Request, QueryMissingCase_V2Response> {

	/**
	 * 20140612 Ric
	 * 缺漏資料查詢
	 * 有菜色無食材
	 * 無調味料
	 * 無菜色照片
	 */
	private static final long serialVersionUID = -898989873426899758L;
	@Override
	public void process() throws NamingException, ParseException {
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		String begDate = this.requestObj.getStartDate();
		String endDate = this.requestObj.getEndDate();
		int sid = this.requestObj.getSid();
		
		begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
		endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
		
		System.out.print(sid);
		System.out.print("+");
		System.out.print(begDate);
		System.out.print("+");
		System.out.print(endDate);
		
		//由SID找KID 檢查有無對應廚房 chu--
		SchoolkitchenDAO SK = new SchoolkitchenDAO();
		SK.setSession(dbSession);
		List<Integer> KK = SK.queryKitchenListBySchool(sid);
		System.out.print(KK);
	
		if (CateringServiceUtil.isEmpty(begDate) || CateringServiceUtil.isEmpty(endDate)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認日期區間填寫正確!!");
			return;
		}else{ // 20140613 Ric 查詢日期內無菜單資料 //20151119 chu query by kid--> sid
			Criteria criteriaBd = this.dbSession.createCriteria(Batchdata.class);
			criteriaBd.add(Restrictions.between("menuDate", begDate,endDate));
			criteriaBd.add(Restrictions.eq("schoolId", sid));
			criteriaBd.add(Restrictions.eq("menuType", 1));
			criteriaBd.add(Restrictions.eq("enable", 1));
			List<Batchdata> bd = criteriaBd.list();
			if (bd.size() < 1 ) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("所查詢的日期範圍沒有菜單");
				return;
			}
			if( KK.size()==0){
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("請確認學校與廚房資訊");//20151113 chu--怕查無廚房會爆炸
				return;
			}
		}
		
		// 檢查是否超過日期的查詢範圍大小 20140221 KC
		if (CateringServiceUtil.isOverQueryRange(begDate, endDate)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查詢日期範圍太大，請縮小查詢範圍");
			return;  
		}
		
		//多天 2015116 by chu 將日期轉為DATE格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date BEGD = sdf.parse(begDate);
		Date ENDD = sdf.parse(endDate);
		String DS = new String();
		
		//20151113 by chu for menuType
		CodeDAO TT = new CodeDAO();
		TT.setSession(dbSession);
		//20151123 chu 由SID查學校名稱作為預設值
		SchoolDAO SD = new SchoolDAO();
		SD.setSession(dbSession);
		School SCH =  SD.querySchoolById(sid);
		
		while(BEGD.before(ENDD) || BEGD.equals(ENDD)){ //區間第一天LOOP到最後一天
			DS = sdf.format(BEGD);
			nullBatchdataList nba = new nullBatchdataList();
			Criteria criteriaBd = this.dbSession.createCriteria(Batchdata.class);
			criteriaBd.add(Restrictions.eq("menuDate", DS));
			criteriaBd.add(Restrictions.eq("schoolId",sid ));
			List<Batchdata> bd = criteriaBd.list();
			String offerState = "";
			String nullState = "";//闕漏資訊
			String schoolNames ="";
			String kitchenNames ="";
			String kitchenContacts ="";
			Integer mType = 0;
			String type =" ";
			Integer check1 = 0;//check
			
			if (bd.size() < 1 ) {
				if(BEGD.getDay() == 0|| BEGD.getDay()==6){
					offerState+= "假日";
				}
				offerState+= "無";
				
				nba.setMenuDate(DS);
				nba.setShcoolName(SCH.getSchoolName());
				nba.setKitchenName(kitchenNames);
				nba.setmType("");
				nba.setNullState(nullState);
				nba.setKitchenContact(kitchenContacts);
				NoMenuDateDAO nmDAO  = new NoMenuDateDAO(this.dbSession);
				StringBuilder nmSchoolList = nmDAO.queryNoMenuDateBySid(String.valueOf(sid), DS);
				nba.setOfferState(offerState+" "+nmSchoolList.toString());
				this.responseObj.getNullBatchdataList().add(nba);
				System.out.print("[set null]");
			}else{
				offerState+= "有";
				
				while(mType<4){//menuType
					
					System.out.print("["+mType+"]");
					nullState = "";
					schoolNames ="";
					kitchenNames ="";
					kitchenContacts ="";
					type =" ";
					boolean check2 = false;//check
					
					//查詢無食材資料
					List<Object[]> nullIngredient = HibernateUtil.queryNullIngredientBySchoolAndTime_V2(this.dbSession, DS, DS, sid, mType);
					System.out.print("<"+nullIngredient.size()+">");
					if(nullIngredient.size()==0){}
					if(nullIngredient.size()>0){
						nullState+= "無食材資料. ";
						check2 = true;//該日該餐查得資料
					}
					
					//查詢食材資料為填寫完整
					List<Object[]> nullIngreduentDataList = HibernateUtil.queryNullIngredientDataByKitchenAndTime_V2(this.dbSession, DS, DS, sid, mType);
					System.out.print("<"+nullIngreduentDataList.size()+">");
					if(nullIngreduentDataList.size()==0){}
					if(nullIngreduentDataList.size()>0){
						nullState+= "食材資料未完整填寫. ";
						check2 = true;//該日該餐查得資料
					}
					
					//查詢無調味料資訊
					List<Object[]> nullSeasioning = HibernateUtil.queryNullSeasoning_V2(this.dbSession, DS, DS, sid, mType);
					boolean nullSeas = false;//check
					System.out.print("<"+nullSeasioning.size()+">");
					if(nullSeasioning.size()==0){}
					Iterator<Object[]> ns=nullSeasioning.iterator();
					SeasoningStockDataDAO seasoningDAO = new SeasoningStockDataDAO(this.dbSession);
					while(ns.hasNext()){
						Object[] row = ns.next();
						if(seasoningDAO.queryNullSeasoning((String)row[1], Integer.valueOf(row[2].toString()))){
							nullSeas = true;
						}
					}
					if(nullSeas){
						nullState+= "無調味料. ";
						check2 = true;//該日該餐查得資料
					}
					
					//查詢無菜色照片
					List<Object[]> dishList = HibernateUtil.queryDishList_V2(this.dbSession, DS, DS, sid, mType);
					String nullDishPicss ="";
					boolean picExist = false;
					if (dishList.size() < 1 ) {
						nullDishPicss +="";
					}else{
						Iterator<Object[]> dl=dishList.iterator();
						Object[] row=dl.next();
						Long did = Long.valueOf(row[0].toString());
						Integer kitid =  Integer.valueOf(row[4].toString());
						if ("0".equals(row[0].toString())){
							continue;
						}
						try {
							// 暫時新舊方法同時並存判斷圖片是否存在   之後改掉
							CateringServiceUtil cateringServiceUtil = new CateringServiceUtil(dbSession);
							if(cateringServiceUtil.isDishImageFileNameExists(kitid, did)){
								picExist = true;
							}
							if(!picExist){  // 暫時寫法   先判斷以上新方法  找無檔案再以舊方法判斷
								if(CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kitid, did, 130, 150,"png")))
								{
									picExist = true;
								}else if(CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kitid, did, 130, 150, "jpg"))){
									picExist = true;
								}else {
									picExist = false;
								}
							}
							
							if(!picExist){
								nullDishPicss +=row[1].toString()+ ",";
								nullState+= "無菜色照片. ";
								check2 = true;//該日該餐查得資料
							}else{
								nullDishPicss +="";
							}
							
						} catch (Exception e) {
							nullDishPicss +=row[1].toString()+ ",";
							e.printStackTrace();
							nullState+= "無菜色照片. ";
							check2 = true;//該日該餐查得資料
						}
					}
					
					if(check2){//若該餐該餐查得缺漏資料
						//查詢學校各餐對應之廚房與資料
						List<Object[]> kitchenContent = HibernateUtil.queryKitchenContent(this.dbSession, DS, DS, sid, mType);
						if(kitchenContent.size()==0){}
						Iterator<Object[]> kc = kitchenContent.iterator();
						while(kc.hasNext()){
							Object[] row =kc.next();
							schoolNames = ((String)row[0]);
							kitchenNames= (row[1].toString());
							kitchenContacts= (row[3].toString());
						}
						nullBatchdataList nba2 = new nullBatchdataList();//important! chu
						nba2.setMenuDate(DS);
						nba2.setShcoolName(schoolNames);
						nba2.setKitchenName(kitchenNames);
						nba2.setNullState(nullState);
						nba2.setKitchenContact(kitchenContacts);
						type = TT.getCodeMsgByStatusCode(mType.toString(), "MenuType");
						nba2.setmType(type);
						NoMenuDateDAO nmDAO  = new NoMenuDateDAO(this.dbSession);
						StringBuilder nmSchoolList = nmDAO.queryNoMenuDateBySid(String.valueOf(sid), DS);
						nba2.setOfferState(offerState+" "+nmSchoolList.toString());
						this.responseObj.getNullBatchdataList().add(nba2);
						check1++;
						System.out.print("[set]");
						System.out.print("["+type+"]");
					}
					mType++;//下一餐
				}
				if(check1==0){//若該日每餐查"無"缺漏資料 再SET 以免重複
					nba.setMenuDate(DS);
					nba.setShcoolName(SCH.getSchoolName());
					nba.setKitchenName(kitchenNames);
					nba.setmType("");
					nba.setNullState(nullState);
					nba.setKitchenContact(kitchenContacts);
					NoMenuDateDAO nmDAO  = new NoMenuDateDAO(this.dbSession);
					StringBuilder nmSchoolList = nmDAO.queryNoMenuDateBySid(String.valueOf(sid), DS);
					nba.setOfferState(offerState+" "+nmSchoolList.toString());
					this.responseObj.getNullBatchdataList().add(nba);
					System.out.print("[set null]");
				}
			}
			BEGD =new Date(BEGD.getTime() + (1000 * 60 * 60 * 24)); //加一天
		}
		//----------20151118 chu----------end
		
		/*--備用chu--
		// 20140612 Ric 有菜色無食材  //20151111 V2 chu
		List<Object[]> nullIngredient = HibernateUtil.queryNullIngredientBySchoolAndTime_V2(this.dbSession, begDate, endDate, sid, null);
		Iterator<Object[]> ni=nullIngredient.iterator();
		while(ni.hasNext()){
			Object[] row=ni.next();
			NullIngreduentList_V2 nil = new NullIngreduentList_V2();
			nil.setDate((String) row[1]);
			nil.setNullIngreduents((String)row[4]);
			nil.setSchoolName((String)row[0]);
			nil.setKitchenName((String)row[3]);
			String type = TT.getCodeMsgByStatusCode(row[2].toString(), "MenuType");
			nil.setMeunType(type);
			nil.setContact((String)row[5]);
			this.responseObj.getNullIngreduentList_V2().add(nil);
		}
		
		// 20140623 Ric 食材資料未完整填寫 //20151111 V2 chu
		List<Object[]> nullIngreduentDataList = HibernateUtil.queryNullIngredientDataByKitchenAndTime_V2(this.dbSession, begDate, endDate, sid, null);
		Iterator<Object[]> nd=nullIngreduentDataList.iterator();
		while(nd.hasNext()){
			Object[] row=nd.next();
			NullIngreduentDataList_V2 ndl = new NullIngreduentDataList_V2();
			ndl.setDate((String) row[1]);
			ndl.setKitchenName((String)row[3]);
			ndl.setSchoolName((String)row[0]);
			ndl.setDishName((String)row[4]);
			ndl.setNullIngreduents((String)row[5]);
			String type = TT.getCodeMsgByStatusCode(row[2].toString(), "MenuType");
			ndl.setMeunType(type);
			ndl.setContact((String)row[6]);
			this.responseObj.getNullIngreduentDataList_V2().add(ndl);
		}
		// 20140612 Ric 無調味料 //20151111 V2 chu
		List<Object[]> nullSeasioning = HibernateUtil.queryNullSeasoning_V2(this.dbSession, begDate, endDate, sid, null);
		Iterator<Object[]> ns=nullSeasioning.iterator();
		while(ns.hasNext()){
			Object[] row=ns.next();
			NullSeasoningList_V2 nsl = new NullSeasoningList_V2();
			//20140617 Ric 增加超連結前往該日調味料編輯 // 20140620 marked
			nsl.setSchoolName((String) row[0]);
			//nsl.setDate("<a href='../ingredients_detail/?menuDate="+(String)row[1]+"#seasoning_modify'>"+(String)row[1]+"</a>");
			nsl.setDate((String)row[1]);
			nsl.setKitchenName((String)row[3]);
			nsl.setContact((String)row[5]);
			
			//若seasoningStcok也沒資料才寫入提醒 
			SeasoningStockDataDAO seasoningDAO = new SeasoningStockDataDAO(this.dbSession);
			if(seasoningDAO.queryNullSeasoning((String)row[1], (Integer)row[2])){
				this.responseObj.getNullSeasoningList_V2().add(nsl);
			}
			
		}
		// 20140612 Ric 無菜色無照片 //20151111 V2 chu
		List<Object[]> dishList = HibernateUtil.queryDishList_V2(this.dbSession, begDate, endDate, sid, null);
		Iterator<Object[]> dl=dishList.iterator();
		while(dl.hasNext()){
			Object[] row=dl.next();
			NullDishpicList ndl = new NullDishpicList();
			Long did = Long.valueOf(row[0].toString());
			int kitid =  Integer.valueOf(row[4].toString());//該欄學校對應之廚房
			System.out.print(kitid);
			if ("0".equals(row[0].toString())){
				continue; //dishid=0的表示沒有圖片存檔 (介接來的)
			}
			try {
				// 暫時新舊方法同時並存判斷圖片是否存在   之後改掉
				boolean picExist = false;
				// add by Joshua 2014/10/21
				CateringServiceUtil cateringServiceUtil = new CateringServiceUtil(dbSession);
				/** Joshua 改寫圖片判斷方式 2014/10/16 *//*
				if(cateringServiceUtil.isDishImageFileNameExists(kitid, did)){
					picExist = true;
//					ndl.setDishName((String)row[1]);
//					ndl.setDishId(did);
				}
				
				if(!picExist){  // 暫時寫法   先判斷以上新方法  找無檔案再以舊方法判斷
					if(CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kitid, did, 130, 150,"png")))
					{
						picExist = true;
					}else if(CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kitid, did, 130, 150, "jpg"))){
						picExist = true;
					}else {
						picExist = false;
					}
				}
				
				if(!picExist){
					ndl.setDishName((String)row[1]);
					ndl.setDishId(did);
				}
				
			} catch (Exception e) {
				ndl.setDishName((String)row[1]);
				ndl.setDishId(did);
				e.printStackTrace();
			}
			// addit by Joshua 2014/10/22
			if(StringUtils.isNotBlank(ndl.getDishName())){
			this.responseObj.getNullDishpicList().add(ndl);}
		}
		*/
		
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
	
}
