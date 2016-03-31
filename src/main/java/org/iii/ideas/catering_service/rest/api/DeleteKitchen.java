package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.KitchenDAO;
import org.iii.ideas.catering_service.service.SupplierService;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class DeleteKitchen extends AbstractApiInterface<DeleteKitchenRequest, DeleteKitchenResponse> {

	
	@Override
	public void process() throws NamingException {
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		Transaction tx = dbSession.beginTransaction();
		/*if(this.requestObj.getKitchenId()==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("刪除索引錯誤");
			return;
		}else{ 
			Integer kid = Integer.valueOf(this.requestObj.getKitchenId());
			Criteria criteriaBD = this.dbSession.createCriteria(Batchdata.class);
			criteriaBD.add(Restrictions.eq("kitchenId", kid));
			List<Batchdata> batchdata = criteriaBD.list();
			if (batchdata.size() > 0 ) {
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("此廚房與追溯資料存在關聯，無法刪除:"+ this.requestObj.getKitchenId());
				return;
			}
		}*/
		//刪除改為停用。 modfiy by Ellis 20160121
		Integer kid = Integer.valueOf(this.requestObj.getKitchenId());
		// Integer kid = 1;
		KitchenDAO kdao = new KitchenDAO(this.dbSession);
		Kitchen kitchen =  kdao.queryKitchenByKitchenId(kid);
		if(kitchen.getEnable()==1){
			kitchen.setEnable(0);
		}else{
			kitchen.setEnable(1);
		}
		dbSession.save(kitchen);
		tx.commit();
//		HibernateUtil.deleteBatchdataByKid(dbSession, kid);//刪除廚房的菜單
//		//supplierService.deleteSupplierByKitchenId(kid); //刪除對應的供應商
//		HibernateUtil.deleteAllSupplierByKid(dbSession, kid); //刪除對應的供應商
//		HibernateUtil.deleteKitchenFromSchoolKitchenById(dbSession, kid);//刪除廚房的對應學校
//		HibernateUtil.deleteKitchenById(dbSession, kid);//刪除廚房
	
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

}
