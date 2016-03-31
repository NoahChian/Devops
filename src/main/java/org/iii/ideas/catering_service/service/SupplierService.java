package org.iii.ideas.catering_service.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.iii.ideas.catering_service.code.ActiveTypeCode;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.IngredientbatchdataDAO;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.SupplierDAO;
import org.iii.ideas.catering_service.dao.SupplierId;
import org.iii.ideas.catering_service.rest.bo.SupplierBO;

public class SupplierService extends BaseService{
	protected Logger log = Logger.getLogger(this.getClass());
	
	public SupplierService(){};
	
	public SupplierService(Session dbSession){
		setDbSession(dbSession);
	};
	
	
	public void updateSupplierDetail(String actType,SupplierBO bo) throws Exception{
		Supplier supplier = null;
		SupplierId id = new SupplierId();
		SupplierDAO supplierDao = new SupplierDAO(dbSession);
		
		//檢核供應商名稱是否重複
		supplier = supplierDao.querySupplierBySupplierName(bo.getSupplierName(), bo.getKitchenId());
		if(supplier!=null){
			if(ActiveTypeCode.UPDATE.equals(actType)){
				if(!supplier.getSupplierName().equals(bo.getSupplierName()))
					throw new Exception("供應商名稱已存在");
			}
			if(ActiveTypeCode.ADD.equals(actType)){
				throw new Exception("供應商名稱已存在");
			}
			
		}
			
		
		//update 會先用key查詢有無此供應商
		if(ActiveTypeCode.UPDATE.equals(actType)){
			supplier = supplierDao.querySupplierById(bo.getSupplierId(), bo.getKitchenId());
			if(supplier == null){
				throw new Exception("查無此供應商");
			}
		}
		
		//Add 會先檢查統編是否被使用
		if(ActiveTypeCode.ADD.equals(actType)){
			//檢核統編
			supplier = supplierDao.querySupplierByCompanyId(bo.getCompanyId(),bo.getKitchenId());
			if(supplier!=null)
				throw new Exception("供應商統編已存在");
			else
				supplier = new Supplier();
			
			id.setKitchenId(bo.getKitchenId());
			supplier.setId(id);
			supplier.setCompanyId(bo.getCompanyId());
		}
		
		supplier.setSupplierName(bo.getSupplierName());
		supplier.setCountyId(bo.getCountyId());
		supplier.setAreaId(bo.getAreaId());
		supplier.setSupplierAdress(bo.getAddress());
		supplier.setOwnner(bo.getOwnner());
		supplier.setSupplierTel(bo.getTel());
		supplier.setSuppliedType(bo.getSuppliedType());
		
		dbSession.saveOrUpdate(supplier);
		
	}
	
	public boolean isIngredientBatchDataUsed(int supplierId,int kitchenId) throws Exception{
		IngredientbatchdataDAO dao = new IngredientbatchdataDAO(dbSession);
		List<Long> list = dao.queryIngredientBatchdataBySupplierId(kitchenId, supplierId);
		if(list != null && list.size()>0)
			return true;
		else
			return false;
	}
	
	public void deleteSupplier(int supplierId,int kitchenId) throws Exception{
		SupplierDAO supplierDao = new SupplierDAO(dbSession);
		Supplier supplier = supplierDao.querySupplierById(supplierId, kitchenId);
		if(supplier == null){
			throw new Exception("查無此供應商");
		}
		
		dbSession.delete(supplier);
		
	}
	

}
