package org.iii.ideas.catering_service.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.IngredientbatchdataDAO;
import org.iii.ideas.catering_service.dao.UploadFileDAO;
import org.iii.ideas.catering_service.dao.Uploadfile;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.rest.bo.IngredientPropertyBO;
import org.iii.ideas.catering_service.util.BoUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.TypeUtil;
import org.iii.ideas.catering_service.code.SourceTypeCode;

/**
 * 
 * @author 2014/06/26 Raymond
 * 
 */
public class IngredientService extends BaseService {
	protected Logger log = Logger.getLogger(this.getClass());

	public IngredientService() {
	};

	public IngredientService(Session dbSession) {
		setDbSession(dbSession);
	};

	public List<IngredientPropertyBO> getIngredientPropertyList(Integer kitchenId, String menuDate) throws ParseException {
		List<IngredientPropertyBO> ipList = new ArrayList<IngredientPropertyBO>();
		IngredientbatchdataDAO igdbDao = new IngredientbatchdataDAO(dbSession);

		List<Map<String, Object>> list = igdbDao.queryIngredientPropertyList(kitchenId, menuDate);
		for (Map<String, Object> map : list) {
			// 取檔案資訊
			map = getIngredientInspectionInfo(map);
			// 轉BO Object
			ipList.add(BoUtil.transIngredientPropertyMapToIngredientPropertyBO(map));
		}

		return ipList;
	}

	private Map<String, Object> getIngredientInspectionInfo(Map<String, Object> IngredientPropertyResultMap) {
		
		if (IngredientPropertyResultMap != null && IngredientPropertyResultMap.containsKey("ingredientBatchId")) {
			String idbId = String.valueOf(IngredientPropertyResultMap.get("ingredientBatchId"));
			String downloadPath = getIngredientInspectionDownloadPath(idbId);
			IngredientPropertyResultMap.put("downloadPath", downloadPath);
		}

		return IngredientPropertyResultMap;
	}
	
	public String getIngredientInspectionDownloadPath(String ingredientBatchId){
		UploadFileDAO ufDao = new UploadFileDAO(dbSession);
		Uploadfile uf = ufDao.getSingleUploadfile(SourceTypeCode.INGREDIENT_INSPECTION, ingredientBatchId);
		if (uf != null) {
			return "../../file/SHOW/inspect_v2|" + ingredientBatchId; // 檔名規則inspect|kitchenId|ingredientBatchDataId
		} else {
			return "";
		}
	}

	public boolean deleteIngredientInspection(Integer kitchenId, String menuDate, Long ingredientId, String supplierCompanyId, String lotNumber, String stockDate) throws ParseException {
		List<String> ibdIdList = new ArrayList<String>();
		boolean result = false;
		IngredientbatchdataDAO ibdDao = new IngredientbatchdataDAO(dbSession);
		UploadFileDAO ufDao = new UploadFileDAO(dbSession);
		ibdIdList = TypeUtil.longListToStringList(ibdDao.queryIngredientbatchIdList(kitchenId, menuDate, ingredientId, supplierCompanyId, lotNumber, stockDate));
		if (ibdIdList.size() > 0) {
			int res = ufDao.deleteMutilUploadFile(SourceTypeCode.INGREDIENT_INSPECTION, (List<String>) ibdIdList);
			if (res > 0)
				result = true;
		}
		return result;
	}

	public boolean updateIngredientAttrInteger(Integer kitchenId, String menuDate, Long ingredientId, String supplierCompanyId, String lotNumber, String stockDate, IngredientAttributeBO ingredientProperty) throws ParseException {
		List<Long> ibdIdList = new ArrayList<Long>();
		boolean result = false;
		IngredientbatchdataDAO ibdDao = new IngredientbatchdataDAO(dbSession);
		int attr = CateringServiceUtil.getIngredientAttrVal(ingredientProperty);
		ibdIdList = ibdDao.queryIngredientbatchIdList(kitchenId, menuDate, ingredientId, supplierCompanyId, lotNumber, stockDate);
		if (ibdIdList.size() > 0) {
			int res = ibdDao.updateIngredientAttr(ibdIdList, attr);
			if (res > 0)
				result = true;
		}
		return result;

	}

}
