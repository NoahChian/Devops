package org.iii.ideas.catering_service.util;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.iii.ideas.catering_service.dao.News;
import org.iii.ideas.catering_service.dao.SeasoningStockData;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.rest.bo.FileBO;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.rest.bo.IngredientPropertyBO;
import org.iii.ideas.catering_service.rest.bo.SeasoningStockDataBO;
import org.iii.ideas.catering_service.rest.bo.SupplierBO;
import org.iii.ideas.catering_service.rest.bo.NewsBO;

public class BoUtil {
	public static SupplierBO transSupplierToSupplierBo(Supplier supplier){
		if(supplier == null)
			return null;
		
		SupplierBO bo = new SupplierBO();
		bo.setSupplierId(supplier.getId().getSupplierId());
		bo.setKitchenId(supplier.getId().getKitchenId());
		bo.setAddress(supplier.getSupplierAdress());
		bo.setAreaId(supplier.getAreaId());
		bo.setCompanyId(supplier.getCompanyId());
		bo.setCountyId(supplier.getCountyId());
		bo.setOwnner(supplier.getOwnner());
		bo.setSupplierName(supplier.getSupplierName());
		bo.setTel(supplier.getSupplierTel());
		bo.setCertification(supplier.getSupplierCertification());
		
		return bo;
		
	}
	
	public static IngredientPropertyBO transIngredientPropertyMapToIngredientPropertyBO(Map<String,Object> dataMap) throws ParseException{
		IngredientPropertyBO ipBo = new IngredientPropertyBO();
		IngredientAttributeBO attrBo = new IngredientAttributeBO();
		FileBO fileBo = new FileBO();
		
		if(dataMap.containsKey("ingredientId"))
			ipBo.setIngredientId((Long)dataMap.get("ingredientId"));
		if(dataMap.containsKey("ingredientName"))
			ipBo.setIngredientName((String)dataMap.get("ingredientName"));
		//20140918 Raymond 目前調味料進貨日期可為空值 預防進貨日期為空查不到資料的狀況 若為此情況後端先給空白值
		if(dataMap.containsKey("stockDate")){
			if(!CateringServiceUtil.isNull(dataMap.get("stockDate")))
				ipBo.setStockDate(CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", (Timestamp)dataMap.get("stockDate")));
			else
				ipBo.setStockDate(" ");
		}
		if(dataMap.containsKey("lotNumber")){
			ipBo.setLotNumber((String)dataMap.get("lotNumber"));
		}
		if(dataMap.containsKey("supplierId"))
			ipBo.setSupplierId((Integer)dataMap.get("supplierId"));
		if(dataMap.containsKey("supplierName"))
			ipBo.setSupplierName((String)dataMap.get("supplierName"));
		if(dataMap.containsKey("supplierCompanyId"))
			ipBo.setSupplierCompanyId((String)dataMap.get("supplierCompanyId"));
		if(dataMap.containsKey("dishName"))
			ipBo.setDishName((String)dataMap.get("dishName"));
		
		if(dataMap.containsKey("ingredientAttr")){
			if(!CateringServiceUtil.isNull(dataMap.get("ingredientAttr"))){
				attrBo = CateringServiceUtil.getIngredientAttrBo((Integer)dataMap.get("ingredientAttr"));
			}else{
				attrBo = CateringServiceUtil.getIngredientAttrBo(0);
			}
		}
	
		if(dataMap.containsKey("downloadPath"))
			fileBo.setDownloadPath((String)dataMap.get("downloadPath"));
		
		ipBo.setIngredientAttribute(attrBo);
		ipBo.setInspectionFile(fileBo);
		
		return ipBo;
	}
	
//	public static SeasoningStockDataBO transSeasoningStockDataToSeasoningStockDataBO(SeasoningStockData ssd) throws ParseException{
//		SeasoningStockDataBO ssdBO = new SeasoningStockDataBO();
//		ssdBO.setSeasoningStockId(ssd.getSeasoningStockId());
//		ssdBO.setDishId(ssd.getDishId());
//		ssdBO.setIngredientId(ssd.getIngredientId());
//		ssdBO.setIngredientName(ssd.getIngredientName());
//		ssdBO.setStockDate(CateringServiceUtil.isNull(ssd.getStockDate()) ? null : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", ssd.getStockDate()));
//		ssdBO.setManufactureDate(CateringServiceUtil.isNull(ssd.getManufactureDate()) ? null : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", ssd.getManufactureDate()));
//		ssdBO.setExpirationDate(CateringServiceUtil.isNull(ssd.getExpirationDate()) ? null : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", ssd.getExpirationDate()));
//		ssdBO.setLotNumber(ssd.getLotNumber());
//		ssdBO.setBrand(ssd.getBrand());
//		ssdBO.setOrigin(ssd.getOrigin());
//		ssdBO.setSource(ssd.getSource());
//		ssdBO.setSupplierId(ssd.getSupplierId());
//		ssdBO.setSourceCertification(ssd.getSourceCertification());
//		ssdBO.setCertificationId(ssd.getCertificationId());
//		ssdBO.setMenuType(ssd.getMenuType());
//		ssdBO.setSupplierName(ssd.getSupplierName());
//		ssdBO.setBrandNo(ssd.getBrandNo());
//		ssdBO.setProductName(ssd.getProductName());
//		ssdBO.setManufacturer(ssd.getManufacturer());
//		ssdBO.setIngredientQuantity(ssd.getIngredientQuantity());
//		ssdBO.setIngredientUnit(ssd.getIngredientUnit());
//		ssdBO.setIngredientAttr(ssd.getIngredientAttr());
//		ssdBO.setUseStartDate(CateringServiceUtil.isNull(ssd.getUseStartDate()) ? null : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", ssd.getUseStartDate()));
//		ssdBO.setUseeEndDate(CateringServiceUtil.isNull(ssd.getUseeEndDate()) ? null : CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", ssd.getUseeEndDate()));
//		ssdBO.setLastUpdateId(ssd.getLastUpdateId());
//		ssdBO.setLastUpdateDate(CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.getCurrentTimestamp()));
//		return ssdBO;
//	}
	public static NewsBO transNewsToNewsBo(News news){
		if(news == null)
			return null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String startDate = sdf.format(news.getStartDate());
		String endDate = sdf.format(news.getEndDate());
		
		NewsBO bo = new NewsBO();
		
		bo.setStartDate(startDate);
		bo.setEndDate(endDate);
		bo.setCategory(news.getCategory());
		bo.setContent(news.getContent());
//		bo.setFile(null); //TODO 與上傳檔案關聯
//		bo.setGroupIdList(null); //TODO 與群組關聯
		bo.setModifyDate(news.getModifyDate());
		bo.setNewsId(news.getNewsId());
		bo.setNewsTitle(news.getNewsTitle());
		bo.setPriority(news.getPriority());
		bo.setPublishDate(news.getPublishDate());
		bo.setPublishUser(news.getPublishUser());
		bo.setSourceLink(news.getSourceLink());
		bo.setSourceTitle(news.getSourceTitle());
		return bo;
		
	}
}
