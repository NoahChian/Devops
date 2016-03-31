package org.iii.ideas.catering_service.rest.api;

import javax.naming.NamingException;

import org.hibernate.Transaction;
import org.iii.ideas.catering_service.rest.bo.NewsBO;
import org.iii.ideas.catering_service.service.NewsService;
import org.iii.ideas.catering_service.service.SupplierService;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class DeleteNews extends AbstractApiInterface<DeleteNewsRequest, DeleteNewsResponse> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1192754778015688402L;

	@Override
	public void process() throws NamingException {
		
		NewsBO bo = new NewsBO();
		bo.setModifyUser(this.getUsername());
		
		if (!this.isLogin()) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		int newsId  = Integer.valueOf(this.requestObj.getNewsId());
		
		if (newsId==0) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("公告資訊錯誤");
			return;
		}
		
		Transaction tx = dbSession.beginTransaction();

		NewsService newsService = new NewsService(dbSession);
		
		// 開始新增/修改資料
		try {
			newsService.deleteNews(newsId, bo);
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(ex.getMessage());
		}

		this.responseObj.setResStatus(1);
		this.responseObj.setMsg("");
	}

}
