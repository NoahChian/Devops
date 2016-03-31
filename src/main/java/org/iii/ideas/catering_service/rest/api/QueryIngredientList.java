package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.ViewDishAndIngredient;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient2;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient2DAO;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredientDAO;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter2;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public final class QueryIngredientList extends AbstractApiInterface<QueryIngredientListRequest, QueryIngredientListResponse> {

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
		Integer schoolId = -1;
		Integer countyId = -1;
		Integer queryLimit = 0;
		kitchenId = this.requestObj.getKitchenId();
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
		/*if (CateringServiceUtil.isOverQueryRange(begDate,endDate)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查詢日期範圍太大，請縮小查詢範圍");
			return;
		}*/
		//設定必填欄位外的like判斷參數
		//額外查詢條件
		String dishname =  this.requestObj.getDishname();
		String ingredientName =  this.requestObj.getIngredientName();
		String brand =  this.requestObj.getBrand();
		String supplierName =  this.requestObj.getSupplierName();
		String manufacturer =  this.requestObj.getManufacturer();
		
		ViewDishAndIngredientParameter2 vdai = new ViewDishAndIngredientParameter2();
		//ViewSchoolMenuParameter smp =new ViewSchoolMenuParameter();
		if(!"".equals(dishname)){vdai.setDishname(dishname);}
		if(!"".equals(ingredientName)){vdai.setIngredientName(ingredientName);}
		if(!"".equals(brand)){vdai.setBrand(brand);}
		if(!"".equals(supplierName)){vdai.setSupplierName(supplierName);}
		if(manufacturer!=null && !"".equals(manufacturer)){vdai.setManufacturer(manufacturer);}
		ViewDishAndIngredient2DAO vdaiDAO = new ViewDishAndIngredient2DAO(dbSession);
		List<ViewDishAndIngredient2> vdaiBO = new ArrayList<ViewDishAndIngredient2>();
		//取得總頁面
		Integer totalCount = 0;
		// 需要比較字串 因此先將輸入int改String
		totalCount = vdaiDAO.queryTotelIngredientCount2(username, userType, kitchenId, schoolId, countyId, begDate, endDate, vdai, queryLimit);
		//取得分頁資料
		vdaiBO = vdaiDAO.queryIngredientsByPage2(username, userType, kitchenId, schoolId, countyId, begDate, endDate, vdai , queryLimit,this.requestObj.getPage(),this.requestObj.getPerpage());
		this.responseObj.setIngredient2(vdaiBO);
		this.responseObj.setTotalCol(totalCount);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
}
