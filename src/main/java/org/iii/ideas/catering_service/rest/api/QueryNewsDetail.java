package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.GroupsDAO;
import org.iii.ideas.catering_service.dao.News;
import org.iii.ideas.catering_service.dao.NewsDAO;
import org.iii.ideas.catering_service.dao.Newsgroupmapping;
import org.iii.ideas.catering_service.dao.NewsgroupmappingDAO;
import org.iii.ideas.catering_service.rest.bo.NewsBO;
import org.iii.ideas.catering_service.util.BoUtil;

public class QueryNewsDetail extends
		AbstractApiInterface<QueryNewsDetailRequest, QueryNewsDetailResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6856577536053080669L;

	@Override
	public void process() throws NamingException, ParseException {
		if(!this.isLogin()){
			this.responseObj.setResStatus(-2);
			this.responseObj.setMsg("使用者未授權");
			return;
		}
		
		int newsId = this.requestObj.getNewsId();
//		int kitchenId = getKitchenId();
		
		try {
			NewsDAO newsDao = new NewsDAO(dbSession);
			News news = newsDao.queryNewsByNewsId(newsId);
			
			if(news == null){
				this.responseObj.setResStatus(0);
				this.responseObj.setMsg("查無此公告資訊!");
				return;
			}
			
			NewsgroupmappingDAO newsgroupDao = new NewsgroupmappingDAO(dbSession);
			List <Newsgroupmapping> mappingList = newsgroupDao.queryNewsgroupmappingByNewsId(news.getNewsId());
			
			NewsBO bo = BoUtil.transNewsToNewsBo(news);
			
			String[] array;
			if(mappingList != null){
				array = new String[mappingList.size()];
				for(int i=0;i<mappingList.size();i++){
					array[i] = String.valueOf(mappingList.get(i).getGroupId());
				}
				bo.setGroupIdList(array);
			} else {
				array = new String[0];
				bo.setGroupIdList(array);
			}

			this.responseObj.setNewsBo(bo);
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("");
			
		} catch (Exception ex) {
			this.responseObj.setResStatus(0);
			this.responseObj.setMsg(ex.getMessage());
		}
	}

}
