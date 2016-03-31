package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.Menu;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class QueryIngredientDetail  extends AbstractApiInterface<QueryIngredientDetailRequest, QueryIngredientDetailResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		Transaction tx = this.dbSession.beginTransaction();
		int kid = this.getKitchenId();
		Long menuId = this.requestObj.getBatchdataId();
		String food = this.requestObj.getFood();
		
		Long dishId = HibernateUtil.queryDishIdByName(this.dbSession, kid, food);
		
		//找不到ingredient 時就自動自ingredient新增
		if (HibernateUtil.getIngredientBatchData(this.dbSession, menuId, dishId)==null){
			List<Ingredient> igdList = HibernateUtil.queryIngredientByDishId(this.dbSession, dishId);
			Iterator<Ingredient> igdIterator = igdList.iterator();  
			while (igdIterator.hasNext()) {
				Ingredient igd = igdIterator.next();
				Ingredientbatchdata igdb = new Ingredientbatchdata();
				igdb.setBatchDataId(menuId);
				igdb.setBrand(igd.getBrand());
				igdb.setIngredientId(igd.getIngredientId());
				igdb.setIngredientName(igd.getIngredientName());
				igdb.setCertificationId("");
				igdb.setSourceCertification("");
				igdb.setLotNumber(CateringServiceUtil.defaultLotNumber);
				igdb.setSupplierId(igd.getSupplierId());
				igdb.setSupplierCompanyId( igd.getSupplierCompanyId() );
				igdb.setDishId(dishId); //問昌港
				this.dbSession.save(igdb);
			}
		}
		
		Menu firstMenu = HibernateUtil.queryMenuById(this.dbSession, menuId);
		
		List<Batchdata> batchdataList = HibernateUtil.queryBatchdataByMenuDate(this.dbSession, kid, firstMenu.getMenuDate());
		if(batchdataList.size()==0){
			if(!tx.wasRolledBack()){
				tx.rollback();
			}
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("在學校菜單中找不到菜色資料");
		}
		Long firstBatchdataId = batchdataList.get(0).getBatchDataId();
		
		
		String sqlStatement = "from Menu m,Batchdata b,Ingredientbatchdata i "
+" where m.id.kitchenId = b.kitchenId "
+ "and m.id.menuDate = b.menuDate "
+ "and b.batchDataId = i.batchDataId "
+ "and b.batchDataId = :batchdataId "
+ "and i.dishId = :dishId "
+ "and m.menuId = :menuId";
		Query query = dbSession.createQuery(sqlStatement);
		query.setParameter("dishId", dishId);
		query.setParameter("menuId", menuId);
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
				//更新認證標章清單20140903
		//-------------------
		List<String> supplyNameOption = new ArrayList<String>();
		Criteria criteriaSP = dbSession.createCriteria(Supplier.class).add( Restrictions.eq("id.kitchenId", kid ));
		List Suppliers = criteriaSP.list();
		Iterator<Supplier> iteratorSP = Suppliers.iterator();
		while(iteratorSP.hasNext()){
			Supplier sc = iteratorSP.next();	
			supplyNameOption.add(sc.getSupplierName());
		}
		//-------------------
        Iterator<Object[]> iterator = results.iterator();  
		while (iterator.hasNext()) {
			SupplierAndMenuDateObject sam = new SupplierAndMenuDateObject();
			Object[] obj = iterator.next();
			Menu menu = (Menu) obj[0];
			Batchdata batchdata = (Batchdata) obj[1];
			Ingredientbatchdata igdb = (Ingredientbatchdata) obj[2];
			Kitchen kitchen = HibernateUtil.queryKitchenById(this.dbSession, menu.getKitchenId());
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
			igdObj.setReportFileName(igdb.getSourceCertification());
			igdObj.setStockDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getStockDate()) );
			if(supplier==null){
				igdObj.setSupplyName("");
			}else{
				igdObj.setSupplyName( supplier.getSupplierName());
			}
			igdObj.setIngredientBatchId(String.valueOf( igdb.getIngredientBatchId() ));
			igdObj.setSupplyNameOption(supplyNameOption);
			igdObj.setValidDate(HibernateUtil.converTimestampToStr("yyyy/MM/dd", igdb.getExpirationDate()));
			
			try {
				if(CateringServiceUtil.fileExists(CateringServiceUtil.getInspectionFileName(kid, igdb.getIngredientBatchId(), "pdf")) || CateringServiceUtil.fileExists(CateringServiceUtil.getInspectionFileName(kid, igdb.getIngredientBatchId(), "pdf"))){
					igdObj.setReportFileName("1");
				}
			} catch (Exception e) {
				igdObj.setReportFileName("1");
			}
			
			this.responseObj.getIngredients().add(igdObj); 	
		}
		if(!tx.wasCommitted()){
			tx.commit();
		}
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
		
	}

}
