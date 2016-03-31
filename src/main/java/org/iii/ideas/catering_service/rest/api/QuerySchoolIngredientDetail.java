package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.rest.bo.IngredientAttributeBO;
import org.iii.ideas.catering_service.service.IngredientService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class QuerySchoolIngredientDetail  extends AbstractApiInterface<QuerySchoolIngredientDetailRequest, QuerySchoolIngredientDetailResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		String menuDate = this.requestObj.getMenuDate();
		menuDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", menuDate));
		Long dishId = this.requestObj.getDishId();
		Integer kitchenId = this.getKitchenId();
		Integer schoolId = this.requestObj.getSchoolId();
		//如果在batchdata 中找不到資料就return 0 筆
		List<Batchdata> batchdataList = HibernateUtil.querySchoolBatchdataByMenuDateAndDishId(this.dbSession, kitchenId, menuDate,schoolId, dishId);
		if(batchdataList.size() == 0){
			this.responseObj.setIngredientsCount(0);
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
			return;
		}
		
		//如果有資料就以第一筆為代表
		Long firstBatchdataId = batchdataList.get(0).getBatchDataId();
		Batchdata firstBatchdata = HibernateUtil.queryBatchdataById(this.dbSession, firstBatchdataId);
		
		String sqlStatement = "from Batchdata b,Ingredientbatchdata i "
+" where b.batchDataId = i.batchDataId "
+ "and   b.batchDataId = :batchdataId "
+ "and   i.dishId = :dishId ";
		Query query = dbSession.createQuery(sqlStatement);
		query.setParameter("dishId", dishId);
		query.setParameter("batchdataId", firstBatchdataId);
		
		List results = query.list();
		//-------------------
		List<String> labelOption = new ArrayList<String>();
		this.responseObj.setIngredientsCount(results.size());
		labelOption.add("");
		//labelOption.add("吉園圃");
		//labelOption.add("產銷履歷");
		//labelOption.add("有機");
		labelOption.add("CAS");
		labelOption.add("CAS_ORGANIC");
		labelOption.add("GAP");
		labelOption.add("GMP");
		labelOption.add("HALAL");
		labelOption.add("HACCP");
		labelOption.add("HEALTH");
		labelOption.add("ISO22000");
		labelOption.add("ISO9001");
		labelOption.add("MILK");
		labelOption.add("TAP");
		labelOption.add("SN"); //新增神農履歷 add by Ellis 20151218
		//更新認證標章清單20140903
		//查出所有供應商選項
		List<String> supplyNameOption = new ArrayList<String>();
		Criteria criteriaSP = dbSession.createCriteria(Supplier.class).add( Restrictions.eq("id.kitchenId", kitchenId ));
		List Suppliers = criteriaSP.list();
		Iterator<Supplier> iteratorSP = Suppliers.iterator();
		while(iteratorSP.hasNext()){
			Supplier sc = iteratorSP.next();	
			supplyNameOption.add(sc.getSupplierName());
		}
		//把所有ingredientbatchdata 放入xml中
        Iterator<Object[]> iterator = results.iterator();  
        log.debug("QuerySchoolIngredientDetail Size:"+results.size());
		while (iterator.hasNext()) {
			SupplierAndMenuDateObject sam = new SupplierAndMenuDateObject();
			Object[] obj = iterator.next();
			Batchdata batchdata = (Batchdata) obj[0];
			Ingredientbatchdata igdb = (Ingredientbatchdata) obj[1];
			Kitchen kitchen = HibernateUtil.queryKitchenById(this.dbSession, kitchenId);
			Supplier supplier = HibernateUtil.querySupplierById(this.dbSession, igdb.getSupplierId());
			IngredientObject igdObj = new IngredientObject();
			igdObj.setAuthenticateId(igdb.getCertificationId());
			igdObj.setBrand(igdb.getBrand());
			igdObj.setIngredientBatchId(kitchen.getHaccp());
			igdObj.setIsReport( "0"); //問昌港 數值那裡來
			igdObj.setLabel(igdb.getSourceCertification());
			igdObj.setLabelOption(labelOption);
			igdObj.setName(igdb.getIngredientName());
			//igdObj.setLotNum(igdb.getLotNumber());
			igdObj.setIngredientLotNum(igdb.getLotNumber());
			igdObj.setProductDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getManufactureDate()));
			igdObj.setStockDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getStockDate()) );
			if(supplier==null){
				igdObj.setSupplyName("");
			}else{
				igdObj.setSupplyName( supplier.getSupplierName());
			}
			igdObj.setIngredientBatchId(String.valueOf( igdb.getIngredientBatchId() ));
			igdObj.setSupplyNameOption(supplyNameOption);
			igdObj.setValidDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getExpirationDate()));
			
			//20140722 Raymond 新增回傳欄位  ingredientId supplyCompanyId
			igdObj.setIngredientId(String.valueOf(igdb.getIngredientId()));
			igdObj.setSupplierCompanyId(igdb.getSupplierCompanyId());
			//20140722查出食材屬性資訊
			if(igdb.getIngredientAttr()!=null)
				igdObj.setIngredientAttribute(CateringServiceUtil.getIngredientAttrBo(igdb.getIngredientAttr()));
			else
				igdObj.setIngredientAttribute(new IngredientAttributeBO());
			
			igdObj.setProductName(CateringServiceUtil.isEmpty(igdb.getProductName()) ? "" : igdb.getProductName());
			igdObj.setIngredientQuantity(CateringServiceUtil.isEmpty(igdb.getIngredientQuantity()) ? "" : igdb.getIngredientQuantity());
			igdObj.setIngredientUnit(CateringServiceUtil.isEmpty(igdb.getIngredientUnit()) ? "" : igdb.getIngredientUnit());
			
			try {
				IngredientService ingredientService = new IngredientService(dbSession);
				String downloadPath = ingredientService.getIngredientInspectionDownloadPath(String.valueOf(igdb.getIngredientBatchId()));
				if (!CateringServiceUtil.isEmpty(downloadPath)) {
					igdObj.setIsReport("1");
					igdObj.setReportFileName(downloadPath);
				} else {
					igdObj.setIsReport("0");
					igdObj.setReportFileName("");
				}

			} catch (Exception e) {
				igdObj.setIsReport("0");
				igdObj.setReportFileName("");
				e.printStackTrace();
			}
			
			igdObj.setManufacturer(CateringServiceUtil.isEmpty(igdb.getManufacturer()) ? "" : igdb.getManufacturer());
			this.responseObj.getIngredients().add(igdObj); 	
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
		
	}

}
