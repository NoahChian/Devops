package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.Uploadfile;
import org.iii.ideas.catering_service.rest.bo.FileBO;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.rest.bo.IngredientPropertyBO;
import org.iii.ideas.catering_service.service.IngredientService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class QueryIngredientPropertyList extends AbstractApiInterface<QueryIngredientPropertyListRequest, QueryIngredientPropertyListResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -486243880820844589L;

	@Override
	public void process() throws NamingException {
		String menuDate = "";
		String errMsg = "";
		
		
		// 檢核登入
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		//檢核Request
		errMsg = validateRequestParam(this.requestObj);
		
		if(!CateringServiceUtil.isEmpty(errMsg)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(errMsg);
			return;
		}
		
		menuDate = this.requestObj.getMenuDate();
		
		IngredientService ingredientService = new IngredientService(dbSession);
		
		List<IngredientPropertyBO> ingredientPropertyList = new ArrayList<IngredientPropertyBO>();
		
		try {
			ingredientPropertyList = ingredientService.getIngredientPropertyList(getKitchenId(), menuDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("資料查詢錯誤");
			return;
		}

		this.responseObj.setIngredientPropertyList(ingredientPropertyList);
		//傳入假資料
//		this.responseObj.setIngredientPropertyList(makeFakeData());
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

	private String validateRequestParam(QueryIngredientPropertyListRequest req){
		String errMsg = "";
		//菜單日期
		if(CateringServiceUtil.isEmpty(req.getMenuDate())){
			errMsg += "傳入參數有誤,菜單日期為空!";
		}		
		return errMsg;
	}
	
	private List<IngredientPropertyBO> makeFakeData(){
		
		List<IngredientPropertyBO> boList = new ArrayList<IngredientPropertyBO>();
		IngredientPropertyBO bo1 = new IngredientPropertyBO();
		IngredientAttributeBO attrBo1 = new IngredientAttributeBO();
		FileBO fbo1 = new FileBO();
		bo1.setIngredientId((long)31966);
		bo1.setIngredientName("白米");
		bo1.setLotNumber("1");
		bo1.setStockDate("2014/06/04");
		bo1.setSupplierCompanyId("04908883");
		bo1.setSupplierId(100);
		bo1.setSupplierName("聯友糧食行");
		attrBo1.setGmbean(1);
		attrBo1.setGmcorn(0);
		attrBo1.setPsfood(1);
		bo1.setIngredientAttribute(attrBo1);
		fbo1.setDownloadPath("/file/SHOW/inspect%aklfjl713jkhfjksllsuue17");
		bo1.setInspectionFile(fbo1);
		
		
		IngredientPropertyBO bo2 = new IngredientPropertyBO();
		IngredientAttributeBO attrBo2 = new IngredientAttributeBO();
		FileBO fbo2 = new FileBO();
		bo2.setIngredientId((long)3164);
		bo2.setIngredientName("蝦仁");
		bo2.setLotNumber("1");
		bo2.setStockDate("2014/06/04");
		bo2.setSupplierCompanyId("16422414");
		bo2.setSupplierId(133);
		bo2.setSupplierName("博多國際流通股份有限公司");
		attrBo1.setGmbean(1);
		attrBo1.setGmcorn(1);
		attrBo1.setPsfood(1);
		bo2.setIngredientAttribute(attrBo2);
		fbo2.setDownloadPath("/file/SHOW/inspect%aklfjl713jkhfjksllsuue17");
		bo2.setInspectionFile(fbo2);
		
		boList.add(bo1);
		boList.add(bo2);
		return boList;
		
		
		
	}

	
}
