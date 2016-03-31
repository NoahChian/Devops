package org.iii.ideas.catering_service.rest.api;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import org.iii.ideas.catering_service.dao.News;
import org.iii.ideas.catering_service.dao.NewsDAO;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter2;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

public final class QueryNewsList extends AbstractApiInterface<QueryNewsListRequest, QueryNewsListResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		// select * from menu where MenuDate between [startDate] and [endDate] and SchoolId = [sid]
		if (this.getUsername() == null) {
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
		
		String username = this.getUsername();
		
		Integer newsId = -1;
		String newsTitle;
		Integer category = -1;
		String queryType;
		String startDate;
		String endDate;
		
		Integer queryLimit = 0;
		
		newsId = this.requestObj.getNewsId();
		newsTitle = this.requestObj.getNewsTitle();
		category = this.requestObj.getCategory();
		
		//時間 (顯示日期 startEndDate/ 發佈日期 publishDate)
		queryType = this.requestObj.getQueryType();
		startDate = this.requestObj.getStartDate();
		endDate  = this.requestObj.getEndDate();
		
		queryLimit = this.requestObj.getQueryLimit();
		if(CateringServiceUtil.isEmpty(startDate) || CateringServiceUtil.isEmpty(endDate)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("請確認日期區間填寫正確!!");
			return;
		}
		startDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", startDate));
		endDate = CateringServiceUtil.converTimestampToStr("yyyy/MM/dd", CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", endDate));
		//檢查是否超過日期的查詢範圍大小  20140221 KC
		if (CateringServiceUtil.isOverQueryRange(startDate,endDate)){
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg("查詢日期範圍太大，請縮小查詢範圍");
			return;
		}
		//設定必填欄位外的like判斷參數
		ViewSchoolMenuParameter2 smp =new ViewSchoolMenuParameter2();
		//smp.setSchoolname("");
		
		NewsDAO newsDAO = new NewsDAO(dbSession);
		List<News> newsBO = new ArrayList<News>();
		//取得總頁面
		Integer totalCount = 0;
		// 需要比較字串 因此先將輸入int改String
		totalCount = newsDAO.queryTotelMenuCount(username, userType, newsId, newsTitle, category, queryType, startDate, endDate, smp, queryLimit);
		//取得分頁資料
		newsBO = newsDAO.queryMenuByPage(username, userType, newsId, newsTitle, category, queryType, startDate, endDate, smp , queryLimit,this.requestObj.getPage(),this.requestObj.getPerpage());
		
		this.responseObj.setNewList(newsBO);
		this.responseObj.setTotalCol(totalCount);
		this.responseObj.setMsg("");
		this.responseObj.setResStatus(1);
	}
}
