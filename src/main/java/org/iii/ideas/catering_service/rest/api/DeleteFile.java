package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.code.SourceTypeCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public class DeleteFile extends AbstractApiInterface<DeleteFileRequest, DeleteFileResponse> {
	@Override
	public void process() throws NamingException  {
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		int kid = this.getKitchenId();
		try {
			if("kitchen".equals(this.requestObj.getPageName())){
				String kitchenPath = "kitchenLogo/" + kid + "/";
				boolean success = false;
				if("rename".equals(this.requestObj.getMode())){
					 success =  CateringServiceUtil.deleteAndBackupFile(SourceTypeCode.CATERING_COMPANY_LOGO, String.valueOf(kid)); 
				}
				if("delete".equals(this.requestObj.getMode())){
					 success =  CateringServiceUtil.deleteFile(kitchenPath); 
				}
				if(success)
				{
					this.responseObj.setResStatus(1);
					this.responseObj.setMsg("");
				}else{
					this.responseObj.setResStatus(0);
					this.responseObj.setMsg("變更檔案發生錯誤!");
				}
				//HibernateUtil.checkKitchenPicExist(dbSession, kid);
			}
			if("dish".equals(this.requestObj.getPageName())){
				Long dishid = Long.valueOf(this.requestObj.getDishId());
				String dishPicPath = "dish/" + kid + "/" + kid + "_" + dishid;
				String dishPicPath2 = "dish/" + kid + "/" + kid + "_" + dishid + "_130_150";//消費者檢視會看到的檔案 kid_dishid_130_150.png 要額外作刪除動作
				boolean success = false;
				if("rename".equals(this.requestObj.getMode())){
					 success =  CateringServiceUtil.deleteAndBackupFile(SourceTypeCode.DISH_DATA_MAINTENANCE, String.valueOf(dishid)); 
//					 success =  CateringServiceUtil.deleteFile(dishPicPath2); 
				}
/**				Joshua edit 2014/10/14
				if("rename".equals(this.requestObj.getMode())){
					 success =  CateringServiceUtil.deleteAndBackupFile(dishPicPath); 
					 success =  CateringServiceUtil.deleteFile(dishPicPath2); 
				}
*/				
				if("delete".equals(this.requestObj.getMode())){
					 success =  CateringServiceUtil.deleteFile(dishPicPath); 
					 success =  CateringServiceUtil.deleteFile(dishPicPath2); 
				}
				if(success)
				{
					this.responseObj.setResStatus(1);
					this.responseObj.setMsg("");
				}else{
					this.responseObj.setResStatus(0);
					this.responseObj.setMsg("變更檔案發生錯誤!");
				}
				//HibernateUtil.checkKitchenPicExist(dbSession, kid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
