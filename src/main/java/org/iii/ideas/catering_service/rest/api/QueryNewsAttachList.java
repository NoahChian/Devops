package org.iii.ideas.catering_service.rest.api;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import org.iii.ideas.catering_service.dao.News;
import org.iii.ideas.catering_service.dao.NewsDAO;
import org.iii.ideas.catering_service.dao.Newsattachfiles;
import org.iii.ideas.catering_service.dao.NewsattachfilesDAO;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter2;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public final class QueryNewsAttachList extends AbstractApiInterface<QueryNewsAttachListRequest, QueryNewsAttachListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		// select * from menu where MenuDate between [startDate] and [endDate] and SchoolId = [sid]
		if (this.getUsername() == null) {
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		Integer newsId = -1;
		
		newsId = this.requestObj.getNewsId();
				
		NewsattachfilesDAO nafDAO = new NewsattachfilesDAO(dbSession);
		List<Newsattachfiles> nafBO = new ArrayList<Newsattachfiles>();
		//取得總頁面
		Integer totalCount = 0;
		// 需要比較字串 因此先將輸入int改String
		//取得分頁資料
		nafBO = nafDAO.getNewsattachfilesList(newsId);
		
		this.responseObj.setNewsattachfilesList(nafBO);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
}
