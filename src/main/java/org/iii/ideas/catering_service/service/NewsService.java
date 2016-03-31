package org.iii.ideas.catering_service.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.iii.ideas.catering_service.code.ActiveTypeCode;
import org.iii.ideas.catering_service.dao.Groups;
import org.iii.ideas.catering_service.dao.GroupsDAO;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;
import org.iii.ideas.catering_service.dao.IngredientbatchdataDAO;
import org.iii.ideas.catering_service.dao.NewsDAO;
import org.iii.ideas.catering_service.dao.Newsattachfiles;
import org.iii.ideas.catering_service.dao.News;
import org.iii.ideas.catering_service.dao.NewsattachfilesDAO;
import org.iii.ideas.catering_service.dao.Newsgroupmapping;
import org.iii.ideas.catering_service.dao.NewsgroupmappingDAO;
import org.iii.ideas.catering_service.dao.News;
import org.iii.ideas.catering_service.dao.SupplierDAO;
import org.iii.ideas.catering_service.rest.bo.NewsBO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
//import org.iii.ideas.catering_service.dao.SupplierId;
//import org.iii.ideas.catering_service.rest.bo.SupplierBO;

public class NewsService extends BaseService{
	protected Logger log = Logger.getLogger(this.getClass());
	
	public NewsService(){};
	
	public NewsService(Session dbSession){
		setDbSession(dbSession);
	};
	
	public News updateNewsDetail(String actType,NewsBO bo) throws Exception{
		
		News news = null;
		NewsDAO newsDao = new NewsDAO(dbSession);
		NewsgroupmappingDAO ngmDao = new NewsgroupmappingDAO(dbSession);

		
		Date now = new Date();
		if(ActiveTypeCode.ADD.equals(actType)){
			bo.setPublishDate(now);
			bo.setModifyDate(now);
		} else {
			bo.setModifyDate(now);
		}
		
		//檢核公告是否存在
		news = newsDao.queryNewsByNewsId(bo.getNewsId());
		if(news!=null){
			if(ActiveTypeCode.ADD.equals(actType)){
				throw new Exception("公告編號已存在");
			} else {
				bo.setNewsId(bo.getNewsId());
			}
			
		} else {
			if(ActiveTypeCode.UPDATE.equals(actType)){
				throw new Exception("查無此公告編號");
			}
			news = new News();
		}
		
		news.setNewsTitle(bo.getNewsTitle());
		news.setContent(bo.getContent());
		news.setPriority(bo.getPriority());
		news.setSourceTitle(bo.getSourceTitle());
		news.setSourceLink(bo.getSourceLink());
		news.setPublishUser(bo.getPublishUser());
		news.setModifyUser(bo.getModifyUser());
		news.setCategory(bo.getCategory());		
		
		if(!CateringServiceUtil.isNull(bo.getPublishDate()))
			news.setPublishDate(bo.getPublishDate());
		
		news.setModifyDate(bo.getModifyDate());

		Date startDate = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", bo.getStartDate());
		Date endDate = CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd", bo.getEndDate());
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(endDate); 
		c.add(Calendar.HOUR, 23);
		c.add(Calendar.MINUTE, 59);
		c.add(Calendar.SECOND, 59);
		endDate = c.getTime();
		
		news.setStartDate(startDate);
		news.setEndDate(endDate);

//		news.setSupplierName(bo.getSupplierName());
//		news.setCountyId(bo.getCountyId());
//		news.setAreaId(bo.getAreaId());
//		news.setSupplierAdress(bo.getAddress());
//		news.setOwnner(bo.getOwnner());
//		news.setSupplierTel(bo.getTel());
		
		dbSession.saveOrUpdate(news);
		
		Object[]temp = bo.getGroupIdList();
		List<Object> groupIdList = Arrays.asList(temp);
		
		//關係全刪除
		List<Newsgroupmapping> ngmList = ngmDao.queryNewsgroupmappingByNewsId(news.getNewsId());
		if(ngmList != null){
			for(int i=0; i<ngmList.size(); i++){
				if(!groupIdList.contains(String.valueOf(ngmList.get(i).getGroupId()))){
					dbSession.delete(ngmList.get(i));
				};
			}
		}

		
		Newsgroupmapping ngm;
		for(int i=0; i<groupIdList.size(); i++){
			String tempStr = (String)groupIdList.get(i);
			ngm = new Newsgroupmapping();
			ngm.setGroupId(Integer.valueOf(tempStr));
			ngm.setNewsId(news.getNewsId());
			dbSession.saveOrUpdate(ngm);
		}
		return news; //為了拿到新增完成後的newsId
	}
	
	public boolean isIngredientBatchDataUsed(int supplierId,int kitchenId) throws Exception{
		IngredientbatchdataDAO dao = new IngredientbatchdataDAO(dbSession);
		List<Long> list = dao.queryIngredientBatchdataBySupplierId(kitchenId, supplierId);
		if(list != null && list.size()>0)
			return true;
		else
			return false;
	}
	
	public void deleteNews(int newsId, NewsBO bo) throws Exception{
		NewsDAO newsDao = new NewsDAO(dbSession);
		News news = newsDao.queryNewsByNewsId(newsId);
		if(news == null){
			throw new Exception("查無此公告");
		}
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                00, 00, 00);
		calendar.add(Calendar.DATE, -1);
		news.setEndDate(calendar.getTime());
		if(news.getStartDate().getTime() > (news.getEndDate().getTime())){
			news.setStartDate(calendar.getTime());
		}
		news.setModifyDate(now);
		news.setModifyUser(bo.getModifyUser());
		dbSession.update(news);
		
		//刪除其看的到的群組關聯
//		NewsgroupmappingDAO mappingDao = new NewsgroupmappingDAO(dbSession);
//		List<Newsgroupmapping> mapping = 
//				mappingDao.queryNewsgroupmappingByNewsId(newsId);
//		
//		if(mapping != null){
//			for(int i=0;i<mapping.size();i++){
//				dbSession.delete(mapping.get(i));
//			}
//		}
		
		//刪除附件
//		NewsattachfilesDAO ntfDao = new NewsattachfilesDAO(dbSession);
//		List<Newsattachfiles> nafList = ntfDao.getNewsattachfilesList(newsId);
//		if(nafList != null){
//			for(int i=0;i<nafList.size();i++){
//				deleteNewsFile(nafList.get(i).getId());
//			}
//		}
	}
	
	public void deleteNewsFile(int fileId) throws Exception{
		NewsattachfilesDAO nafDao = new NewsattachfilesDAO(dbSession);
		Newsattachfiles naf = nafDao.getNewsattachfiles(fileId);
		if(naf == null){
			throw new Exception("查無此公告附件");
		}
		dbSession.delete(naf);
		
		File nafDel = new File(naf.getFilePath() + naf.getName());
		boolean del = nafDel.delete();
	}
}
