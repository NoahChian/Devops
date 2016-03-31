package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
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
import org.iii.ideas.catering_service.dao.SeasoningStockData;
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
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
//import org.iii.ideas.catering_service.util.HibernateUtil;


public final class QueryMissingCase extends AbstractApiInterface<QueryMissingCaseRequest, QueryMissingCaseResponse> {

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
		int kid = this.getKitchenId();
		String begDate = this.requestObj.getStartDate();
		String endDate = this.requestObj.getEndDate();
		begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
		endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));

		if (CateringServiceUtil.isEmpty(begDate) || CateringServiceUtil.isEmpty(endDate)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認日期區間填寫正確!!");
			return;
		}else{ // 20140613 Ric 查詢日期內無菜單資料
			Criteria criteriaBd = this.dbSession.createCriteria(Batchdata.class);
			criteriaBd.add(Restrictions.between("menuDate", begDate,endDate));
			criteriaBd.add(Restrictions.eq("kitchenId", kid));
			// #13568 缺漏查詢異常 修正原只查詢午餐的限制所造成的查詢異常 0 早餐 1午餐 2點心 3晚餐
			ArrayList<Integer> menuTypeList = new ArrayList<Integer>();
			menuTypeList.add(0);
			menuTypeList.add(1);
			menuTypeList.add(2);
			menuTypeList.add(3);
			criteriaBd.add(Restrictions.in("menuType", menuTypeList));
//			criteriaBd.add(Restrictions.eq("menuType", 1));
			criteriaBd.add(Restrictions.eq("enable", 1));
			List<Batchdata> bd = criteriaBd.list();
			if (bd.size() < 1 ) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("所查詢的日期範圍沒有菜單");
				return;
			}
		}
		
		// 檢查是否超過日期的查詢範圍大小 20140221 KC
		if (CateringServiceUtil.isOverQueryRange(begDate, endDate)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查詢日期範圍太大，請縮小查詢範圍");
			return;  
		}
		
		// 20140612 Ric 有菜色無食材
		List<Object[]> nullIngredient = HibernateUtil.queryNullIngredientBySchoolAndTime(this.dbSession, begDate, endDate, 0, kid);
		Iterator<Object[]> ni=nullIngredient.iterator();
		while(ni.hasNext()){
			Object[] row=ni.next();
			NullIngreduentList nil = new NullIngreduentList();
			nil.setDate((String) row[1]);
			nil.setNullIngreduents((String)row[3]);
			nil.setSchoolName((String)row[0]);
			String menuType = "" +  row[5];
			if ("0".equals(menuType)) {
				menuType = "早餐";
			} else if ("1".equals(menuType)) {
				menuType = "午餐";
			} else if ("2".equals(menuType)) {
				menuType = "點心";
			} else if ("3".equals(menuType)) {
				menuType = "晚餐";
			}
			// #13525 : 缺漏資料新增"餐別"欄位
			nil.setMenuType(menuType);
			this.responseObj.getNullIngreduentList().add(nil);
		}
		// 20140623 Ric 食材資料未完整填寫
		List<Object[]> nullIngreduentDataList = HibernateUtil.queryNullIngredientDataByKitchenAndTime(this.dbSession, begDate, endDate, kid);
		Iterator<Object[]> nd=nullIngreduentDataList.iterator();
		while(nd.hasNext()){
			Object[] row=nd.next();
			NullIngreduentDataList ndl = new NullIngreduentDataList();
			ndl.setDate((String) row[1]);
			ndl.setSchoolName((String)row[0]);
			ndl.setDishName((String)row[3]);
			ndl.setNullIngreduents((String)row[4]);
			this.responseObj.getNullIngreduentDataList().add(ndl);
		}
		// 20140612 Ric 無調味料
		List<Object[]> nullSeasioning = HibernateUtil.queryNullSeasoning(this.dbSession, begDate, endDate, 0, kid);
		Iterator<Object[]> ns=nullSeasioning.iterator();
		while(ns.hasNext()){
			Object[] row=ns.next();
			NullSeasoningList nsl = new NullSeasoningList();
			//20140617 Ric 增加超連結前往該日調味料編輯 // 20140620 marked
			// #13524 : 顯示時去除重複的school name
			String tmp = (String) row[0];
			String[] tmpArray = tmp.split(",");
			String schoolName = "";
			String prefix = "";
			if (tmpArray.length > 1) {
				HashSet<String> set = new HashSet<>();
				for (int i = 0; i < tmpArray.length; i++) {
					if (!set.contains(tmpArray[i])) {
						schoolName += prefix;
						prefix = ",";
						schoolName += tmpArray[i];
						set.add(tmpArray[i]);
					}
				}
			} else {
				schoolName = tmp;
			}
			nsl.setSchoolName(schoolName);
			//nsl.setSchoolName((String) row[0]);
			//nsl.setDate("<a href='../ingredients_detail/?menuDate="+(String)row[1]+"#seasoning_modify'>"+(String)row[1]+"</a>");
			nsl.setDate((String)row[1]);
			
			//若seasoningStcok也沒資料才寫入提醒 
			SeasoningStockDataDAO seasoningDAO = new SeasoningStockDataDAO(this.dbSession);
			if(seasoningDAO.queryNullSeasoning((String)row[1], kid)){
				this.responseObj.getNullSeasoningList().add(nsl);
			}
			
		}
		// 20140612 Ric 無菜色無照片
		List<Object[]> dishList = HibernateUtil.queryDishList(this.dbSession, begDate, endDate, kid);
		Iterator<Object[]> dl=dishList.iterator();
		while(dl.hasNext()){
			Object[] row=dl.next();
			NullDishpicList ndl = new NullDishpicList();
			Long did = Long.valueOf(row[0].toString());
			if ("0".equals(row[0].toString())){
				continue; //dishid=0的表示沒有圖片存檔 (介接來的)
			}
			try {
				// 暫時新舊方法同時並存判斷圖片是否存在   之後改掉
				boolean picExist = false;
				// add by Joshua 2014/10/21
				CateringServiceUtil cateringServiceUtil = new CateringServiceUtil(dbSession);
				/** Joshua 改寫圖片判斷方式 2014/10/16 */
				if(cateringServiceUtil.isDishImageFileNameExists(kid, did)){
					picExist = true;
//					ndl.setDishName((String)row[1]);
//					ndl.setDishId(did);
				}
				
				if(!picExist){  // 暫時寫法   先判斷以上新方法  找無檔案再以舊方法判斷
					if(CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kid, did, 130, 150,"png")))
					{
						picExist = true;
					}else if(CateringServiceUtil.fileExists(CateringServiceUtil.getDishImageFileName(kid, did, 130, 150, "jpg"))){
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
		
		
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
}
