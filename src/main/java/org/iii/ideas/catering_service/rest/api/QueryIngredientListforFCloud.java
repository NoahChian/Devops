package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient2;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient2DAO;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredientDAO;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter2;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public final class QueryIngredientListforFCloud extends AbstractApiInterface<QueryIngredientListforFCloudRequest, QueryIngredientListforFCloudResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		
		String userType=CateringServiceCode.USERTYPE_GOV_CENTRAL; 
		String username = "11-EDU"; //直接預設給教育部權限
		Integer kitchenId = -1;
		Integer schoolcode = -1;
		Integer countyId = -1;
		//Integer queryLimit = 0;
		kitchenId = this.requestObj.getKitchenId();
		schoolcode = this.requestObj.getSchoolcode();//提供給勾稽，傳入對外有意義的schoolcode，再轉換成schoolid。  modify by ellis 20150423
		countyId = this.requestObj.getCountyId();
		//queryLimit = this.requestObj.getLimit(); //不使用，以perpage限制
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
		
		//取得schoolid
		Integer schoolId = -1;
		if(schoolcode!=-1){
			SchoolDAO queryschool = new SchoolDAO(dbSession);
			School school  = queryschool.querySchoolBySchoolCode(schoolcode.toString());
			schoolId = school.getSchoolId();
		}
		//取得總頁面
		Integer totalCount = 0;
		// 需要比較字串 因此先將輸入int改String
		//傳入兩個limit，配合既有搜尋。 
		totalCount = vdaiDAO.queryTotelIngredientCount2(username, userType, kitchenId, schoolId, countyId, begDate, endDate, vdai, this.requestObj.getLimit());
		//取得分頁資料
		vdaiBO = vdaiDAO.queryIngredientsByPage2(username, userType, kitchenId, schoolId, countyId, begDate, endDate, vdai , this.requestObj.getLimit(),this.requestObj.getIndex(),this.requestObj.getLimit());
		this.responseObj.setIngredient2(vdaiBO);
		this.responseObj.setTotalCol(totalCount);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
}
