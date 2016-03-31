package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.AccountDao;
import org.iii.ideas.catering_service.rest.bo.SchKitchenUserBO;

public class QuerySchKitchenUserDetail extends AbstractApiInterface<QuerySchKitchenUserDetailRequest, QuerySchKitchenUserDetailResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4038816461109320369L;

	@Override
	public void process() throws NamingException {
	

		Integer schoolId = null;
		String schoolCode = null;

		// 檢核登入
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}

		// 檢核空值
		if (requestObj.getSchoolId() == null) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("資料傳遞有誤");
			return;
		}
		
		schoolId = requestObj.getSchoolId();
//		schoolCode = String.format("%06d", schoolId);
		AccountDao accountDao = new AccountDao(dbSession);
		//開始查資料 
		try{
			SchKitchenUserBO skuBo = accountDao.querySchKitchenUserDetail(schoolId);
			if(skuBo==null){
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("查詢資料錯誤");
				return;
			}
		
			this.responseObj.setEnable(skuBo.getEnable());
			this.responseObj.setAreaId(skuBo.getAreaId());
			this.responseObj.setCountyId(skuBo.getCountyId());
			this.responseObj.setSchoolCode(skuBo.getSchoolCode());
			this.responseObj.setSchoolId(skuBo.getSchoolId());
			this.responseObj.setSchoolName(skuBo.getSchoolName());
			this.responseObj.setContents(skuBo.getContents());
			
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
		}catch (Exception ex){
			ex.printStackTrace();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(ex.getMessage());
		}
	}
}
