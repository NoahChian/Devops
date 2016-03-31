package org.iii.ideas.catering_service.rest.excel.create;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.dao.News;
import org.iii.ideas.catering_service.dao.NewsDAO;
import org.iii.ideas.catering_service.dao.ViewSchoolMenuWithBatchdata2;
import org.iii.ideas.catering_service.rest.bo.ViewSchoolMenuParameter2;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;

public class NewsList implements IGenerateExcel {

	private Integer newsId;
	private String newsTitle;
	private Integer category;
	private String startDate;
	private String endDate;
	private String queryType;
	private String username;
	private String userType;
	private Integer queryLimit;

	// startDate + "&"+ endDate + "&"+ newsId + "&" + newsTitle + "&" + category
	// + "&" + queryType;
	public NewsList(String startDate, String endDate, Integer newsId, String newsTitle, Integer category,
			String queryType, String uName, String uType, Integer queryLimit) throws ParseException {
		this.startDate = startDate;// CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd",
									// startDate);
		this.endDate = endDate;// CateringServiceUtil.convertStrToTimestamp("yyyy/MM/dd",
								// endDate);
		this.newsId = newsId;
		this.newsTitle = newsTitle;
		this.category = category;
		this.queryType = queryType;
		this.username = uName;
		this.userType = uType;
		this.queryLimit = queryLimit;
	}

	@Override
	public Map<String, Object[]> generateExcelData() throws ParseException {

		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		List<Object[]> result = null;

		data.put("1", new Object[] { "標題", "內容", "來源標題", "來源網址", "發佈日期", "顯示日期", "修改日期" });

		ViewSchoolMenuParameter2 smp = new ViewSchoolMenuParameter2();
		// smp.setSchoolname("");
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		NewsDAO vsmbDAO = new NewsDAO(session);
		List<News> vsBO = new ArrayList<News>();
		vsBO = vsmbDAO.queryMenu(username, userType, newsId, newsTitle, category, queryType, startDate, endDate, smp,
				queryLimit);

		int row = 2;

		Iterator<News> iterator = vsBO.iterator();

		while (iterator.hasNext()) {
			News menu = iterator.next();

			String startEndDate = menu.getStartDate() + " ~ ";
			if (!CateringServiceUtil.isNull(menu.getEndDate())) {
				startEndDate += menu.getEndDate();
			}

			data.put(String.valueOf(row), 
					new Object[] { 
						menu.getNewsTitle(), 
						menu.getContent(), 
						menu.getSourceTitle(),
						menu.getSourceLink(), 
						menu.getPublishDate(), 
						startEndDate,
						menu.getModifyDate()
						});
			row++;
		}
		session.close();
		return data;
	}
}
