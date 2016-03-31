package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata2;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdataDAO;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdataDAO2;
import org.iii.ideas.catering_service.rest.bo.ViewMenuBO;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter2;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public final class QueryMenuList extends AbstractApiInterface<QueryMenuListRequest, QueryMenuListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		// select * from menu where MenuDate between [begDate] and [endDate] and SchoolId = [sid]
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		String userType=this.getUserType();
		String username = this.getUsername();
		Integer kitchenId = -1;
		Integer restaurantId = -1;
		Integer schoolId = -1;
		Integer countyId = -1;
		Integer queryLimit = 0;
		kitchenId = this.requestObj.getKitchenId();
		restaurantId = this.requestObj.getRestaurantId();
		schoolId = this.requestObj.getSchoolId();
		countyId = this.requestObj.getCountyId();
		queryLimit = this.requestObj.getQueryLimit();
		String begDate = this.requestObj.getBegDate();
		String endDate = this.requestObj.getEndDate();
		if(CateringServiceUtil.isEmpty(begDate) || CateringServiceUtil.isEmpty(endDate)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認日期區間填寫正確!!");
			return;
		}
		begDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", begDate));
		endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
		//檢查是否超過日期的查詢範圍大小  20140221 KC
		if (CateringServiceUtil.isOverQueryRange(begDate,endDate)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查詢日期範圍太大，請縮小查詢範圍");
			return;
		}
		//設定必填欄位外的like判斷參數
		ViewSchoolMenuParameter2 smp =new ViewSchoolMenuParameter2();
		//smp.setSchoolname("");
		
		ViewSchoolMenuWithBatchdataDAO2 vsmbDAO2 = new ViewSchoolMenuWithBatchdataDAO2(dbSession);
		List<ViewSchoolMenuWithBatchdata2> vsBO = new ArrayList<ViewSchoolMenuWithBatchdata2>();
		//取得總頁面
		Integer totalCount = 0;
		// 需要比較字串 因此先將輸入int改String
		totalCount = vsmbDAO2.queryTotelMenuCount(username, userType, kitchenId, restaurantId, schoolId, countyId, begDate, endDate, smp, queryLimit);
		//取得分頁資料
		vsBO = vsmbDAO2.queryMenuByPage(username, userType, kitchenId, restaurantId, schoolId, countyId, begDate, endDate, smp , queryLimit,this.requestObj.getPage(),this.requestObj.getPerpage());
		this.responseObj.setMenu(vsBO);
		this.responseObj.setTotalCol(totalCount);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
}
