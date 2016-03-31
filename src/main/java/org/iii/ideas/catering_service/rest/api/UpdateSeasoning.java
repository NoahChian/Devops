package org.iii.ideas.catering_service.rest.api;

import java.sql.Timestamp;
import java.text.ParseException;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.DishDAO;
import org.iii.ideas.catering_service.dao.SeasoningStockDataDAO;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class UpdateSeasoning extends AbstractApiInterface<UpdateSeasoningRequest, UpdateSeasoningResponse> {

	/**
	 * 新增、修改調味料API add by ellis 20150112
	 */
	private static final long serialVersionUID = 5877471301438776768L;

	@Override
	public void process() throws NamingException, ParseException {
		
//		if (!this.isLogin()) {
//			this.responseObj.setResStatus(-2);
//			this.responseObj.setMsg("使用者未授權");
//			return;
//		}
//		//從Request中取得資料
//		Long seasoningstockId = this.requestObj.getSeasoningstockId();
//		Integer KitchenId = this.getKitchenId();
//		Long ingredientId = this.requestObj.getIngredientId();
//		String ingredientName = this.requestObj.getIngredientName();
//		String stockDate = this.requestObj.getStockDate();
//		String manufactureDate = this.requestObj.getManufactureDate();
//		String expirationDate = this.requestObj.getExpirationDate();
//		String lotNumber = this.requestObj.getLotNumber();
//		Integer supplierId = this.requestObj.getSupplierId();
//		String sourceCertification = this.requestObj.getSourceCertification();
//		String certificationId = this.requestObj.getCertificationId();
//		Integer menuType = this.requestObj.getMenutype();
//		String supplierCompanyId = this.requestObj.getSupplierCompanyId();
//		String productName = this.requestObj.getProductName();
//		String manufacturer = this.requestObj.getManufacturer();
//		String ingredientQuantity =  this.requestObj.getIngredientQuantity();
//		String ingredientUnit = this.requestObj.getIngredientUnit();
//		IngredientAttributeBO ingredientAttrBo = this.requestObj.getIngredientAttr();
//		Integer ingredientAttr = ingredientAttrBo.getGmbean()+ingredientAttrBo.getGmcorn()+ingredientAttrBo.getPsfood();
//		String usestartdate = this.requestObj.getUsestartdate();
//		String useenddate = this.requestObj.getUseenddate();
//		Long dishId = this.requestObj.getDishId();
//		//判斷activemode以進行新增或是修改之動作
//		SeasoningStockDataDAO sDAO = new SeasoningStockDataDAO(dbSession);
//		try{
//			sDAO.checkSeasoningDate(stockDate, manufactureDate, expirationDate, usestartdate, useenddate);//檢驗日期是否合乎規則
//			switch(this.requestObj.getActiveType()){
//			case "ADD":
//				sDAO.addSeasoning(KitchenId,dishId, ingredientName, stockDate, manufactureDate, expirationDate, 
//						lotNumber, supplierId, sourceCertification, certificationId, menuType, 
//						supplierCompanyId, productName, manufacturer, ingredientQuantity, 
//						ingredientUnit, ingredientAttr, usestartdate, useenddate,this.getUsername());
//				this.responseObj.setResStatus(1);
//				this.responseObj.setMsg("");
//				this.responseObj.setIngredientName(ingredientName);
//				break;
//			case "UPDATE":
//				sDAO.updateSeasoning(KitchenId,seasoningstockId, dishId, ingredientId, ingredientName, stockDate, manufactureDate, 
//						expirationDate, lotNumber, supplierId, sourceCertification, certificationId, menuType, 
//						supplierCompanyId, productName, manufacturer, ingredientQuantity, 
//						ingredientUnit, ingredientAttr, usestartdate, useenddate, this.getUsername());
//				this.responseObj.setResStatus(1);
//				this.responseObj.setMsg("");
//				this.responseObj.setIngredientName(ingredientName);
//				break;
//			default:
//				this.responseObj.setResStatus(-1);
//				this.responseObj.setMsg("無效的指令");
//				break;
//			}
//		}catch(Exception ex){
//			this.responseObj.setResStatus(0);
//			this.responseObj.setMsg(ex.getMessage());
//		}
		
	}
}
