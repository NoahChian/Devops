package org.iii.ideas.catering_service.rest.api;

import java.util.Date;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.code.ActiveTypeCode;
import org.iii.ideas.catering_service.dao.News;
import org.iii.ideas.catering_service.rest.bo.NewsBO;
import org.iii.ideas.catering_service.rest.bo.SupplierBO;
import org.iii.ideas.catering_service.service.NewsService;
import org.iii.ideas.catering_service.service.SupplierService;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.LogUtil;

public class UpdateNewsDetail extends AbstractApiInterface<UpdateNewsDetailRequest, UpdateNewsDetailResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -486243880820844589L;

	@Override
	public void process() throws NamingException {

		String actType = this.requestObj.getActiveType();
		NewsBO newsBo = this.requestObj.getNewsBo();
		
		
		// 檢核登入
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		String userType=this.getUserType();
		if (!"11".equals(userType) && !userType.startsWith("12")) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
				
		if(ActiveTypeCode.ADD.equals(actType)){
			newsBo.setPublishUser(this.getUsername());
			newsBo.setModifyUser(this.getUsername());
		} else {
			newsBo.setPublishUser(this.getUsername());
			newsBo.setModifyUser(this.getUsername());
		}
		
		// 檢核ACT Type
		if (CateringServiceUtil.isEmpty(actType)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("API動作錯誤");
			return;
		}
		
		//檢核傳入參數
		if (newsBo==null){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("參數傳遞錯誤");
			return;
		}

		//基本空值檢核
		String errMsg = validateColumn(actType, newsBo);
		if (!CateringServiceUtil.isEmpty(errMsg)) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(errMsg);
			return;
		}
		
		
		
		Transaction tx = dbSession.beginTransaction();
		
		NewsService newsService = new NewsService(dbSession);
		News news;
		// 開始新增/修改資料
		try {
			news = newsService.updateNewsDetail(actType.trim().toLowerCase(), newsBo);
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(ex.getMessage());
			return;
		}
		
		this.responseObj.setNewsId(news.getNewsId());
		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");

	}

	//檢核基本傳入參數
	private String validateColumn(String activeType,NewsBO bo){
		String errMsg = "";
		
		if(ActiveTypeCode.UPDATE.equals(activeType.toLowerCase())){
			if(bo.getNewsId() == null)
				errMsg = LogUtil.putErrorMsg(errMsg, "無此公告");
		}
		
		if(CateringServiceUtil.isEmpty(bo.getNewsTitle()))
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入公告標題");
		
		if(CateringServiceUtil.isEmpty(String.valueOf(bo.getPriority())))
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入優先等級");
		
		if(CateringServiceUtil.isEmpty(bo.getStartDate()))
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入公告起日");
				
		if(CateringServiceUtil.isEmpty(String.valueOf(bo.getCategory())))
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入公告種類");
		
		if(CateringServiceUtil.isEmpty(bo.getContent()))
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入公告內容");
		
		if(CateringServiceUtil.isNull(bo.getGroupIdList()))
			errMsg = LogUtil.putErrorMsg(errMsg, "請輸入公告群組");
		
		return errMsg;
	}
	
}
