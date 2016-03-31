package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.UploadFileDAO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class CheckFileExist extends AbstractApiInterface<CheckFileExistRequest, CheckFileExistResponse> {

	
	@Override
	public void process() throws NamingException  {
		// select * from menu where MenuDate between menuId = [mid]
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		int kid = this.getKitchenId();
		try {
			CateringServiceUtil cateringServiceUtil = new CateringServiceUtil(dbSession);
			if("kitchen".equals(this.requestObj.getPageName())){
				boolean fileExists =  cateringServiceUtil.checkKitchenPicExist(kid); 
				if(fileExists)
				{
					this.responseObj.setResStatus(1);
					this.responseObj.setMsg("");
				}else{
					this.responseObj.setResStatus(0);
					this.responseObj.setMsg("檢查無檔案!");
				}
				//HibernateUtil.checkKitchenPicExist(dbSession, kid);
			}
			if("dish".equals(this.requestObj.getPageName())){
				
				Long dishid = Long.valueOf(this.requestObj.getDishId());
				boolean fileExists =  cateringServiceUtil.isDishImageFileNameExists(kid, dishid); 
				if(fileExists)
				{
					this.responseObj.setResStatus(1);
					this.responseObj.setMsg("");
				}else{
					this.responseObj.setResStatus(0);
					this.responseObj.setMsg("檢查無檔案!");
				}
				//HibernateUtil.checkKitchenPicExist(dbSession, kid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
