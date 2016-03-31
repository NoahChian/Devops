package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredient;
import org.iii.ideas.catering_service.dao.ViewDishAndIngredientDAO;
import org.iii.ideas.catering_service.rest.bo.ViewDishAndIngredientParameter;
import org.iii.ideas.catering_service.rest.excel.create.CreateExcelFile2;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public final class QueryIngredientList2 extends AbstractApiInterface<QueryIngredientList2Request, QueryIngredientList2Response> {
	private HashMap<String,String> header=new HashMap<String,String>();
	private static final int BUFFER_SIZE = 4096;
	
	public QueryIngredientList2(){
		setHeaderObj();
	}
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
		Integer kitchenId = this.getKitchenId();

		Integer queryLimit = 0;

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

		
		ArrayList<ViewDishAndIngredientParameter> queryCondList = new ArrayList<ViewDishAndIngredientParameter>();
		for (ViewDishAndIngredient obj :this.requestObj.getCond() ){
			ViewDishAndIngredientParameter vp=new ViewDishAndIngredientParameter();
			vp.setValueByViewDishAndIngredientObj(obj);
			queryCondList.add(vp);	
		}

		ViewDishAndIngredientDAO vdaiDAO = new ViewDishAndIngredientDAO(dbSession);
		List<ViewDishAndIngredient> vdaiBO = new ArrayList<ViewDishAndIngredient>();
		Integer totalCount = 0;
		
		if ("file".equals(this.requestObj.getFunc())){
			vdaiBO = vdaiDAO.queryIngredients(username, userType, begDate, endDate, queryCondList , 0,0,0);
			String filepath=createExcel(vdaiBO,kitchenId);
			this.responseObj.setFileKey(CateringServiceUtil.getFileKey(filepath));
			
		}else{
			//取得分頁資料
			vdaiBO = vdaiDAO.queryIngredients(username, userType, begDate, endDate, queryCondList , queryLimit,this.requestObj.getPage(),this.requestObj.getPerpage());
			//取得總筆數
			totalCount=vdaiDAO.queryIngredientsCount(username, userType, begDate, endDate, queryCondList);
			
			this.responseObj.setIngredient(vdaiBO);
			this.responseObj.setTotalCol(totalCount);
		}


		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
	public void setHeaderObj(){
		header.put("menudate", "日期");
		header.put("kitchenname", "廚房");
		header.put("schoolname", "學校");
		header.put("dishname", "菜色名稱");
		header.put("ingredientName", "食材名稱");
		header.put("brand", "品牌");
		header.put("supplierName", "供應商");
		header.put("sourceCertification", "認證標章");
		header.put("certificationId", "認證編號");
		header.put("lotNumber", "批號");
	}
	private String createExcel(List<ViewDishAndIngredient> viewIngredientList,int kitchenId) throws ParseException{
		
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		int rowCount=0;
		java.util.Iterator<ViewDishAndIngredient> ir=viewIngredientList.iterator();
		while(ir.hasNext()){
			ViewDishAndIngredient ingredient=ir.next();
			data.put(String.valueOf(rowCount++),new Object[]{
				ingredient.getMenudate(),
				ingredient.getKitchenname(),
				ingredient.getSchoolname(),
				ingredient.getDishname(),
				//ingredient.getDishname(),
				ingredient.getIngredientName(),
				ingredient.getStockDate()== null || StringUtils.isEmpty(ingredient.getStockDate().toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp)ingredient.getStockDate()),
				ingredient.getManufactureDate()== null || StringUtils.isEmpty(ingredient.getManufactureDate().toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp)ingredient.getManufactureDate()),
				ingredient.getExpirationDate()== null || StringUtils.isEmpty(ingredient.getExpirationDate().toString()) ? "" : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp)ingredient.getExpirationDate()),
				ingredient.getLotNumber(),
				ingredient.getBrand(),
				ingredient.getSupplierName(),
				ingredient.getSupplierCompanyId(),
				ingredient.getSourceCertification(),
				ingredient.getCertificationId()});
		}
				
		CreateExcelFile2 excelProducer=new CreateExcelFile2();
		excelProducer.setData(data);
		excelProducer.setDataHeader(header);
		excelProducer.setKitchenid(kitchenId);
		String filepath=excelProducer.createExcel();
		return filepath;
	}
}
