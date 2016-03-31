package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.dao.SeasoningStockData;
import org.iii.ideas.catering_service.dao.SeasoningStockDataDAO;
import org.iii.ideas.catering_service.rest.bo.SeasoningStockDataBO;
import org.iii.ideas.catering_service.util.BoUtil;
import org.iii.ideas.catering_service.util.CateringServiceUtil;

/**
 * 查詢新版調味料清單
 * @author Ellis 20150108
 *
 */
public class QuerySeasoningList extends AbstractApiInterface<QuerySeasoningListRequest, QuerySeasoningListResponse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8567310821356600845L;

	@Override
	public void process() throws NamingException, ParseException {
		// TODO Auto-generated method stub
//		if (!this.isLogin()) {
//			this.responseObj.setResStatus(-2);
//			this.responseObj.setMsg("使用者未授權");
//			return;
//		}
//		String seasoningName = this.requestObj.getSeasoningName();
//		int pageIndex = this.requestObj.getPageNum(); // 頁數
//		int pageLimit = this.requestObj.getPageLimit(); // 每頁顯示總行數
//		String searchdate = this.requestObj.getSearchDate();
//		int kitchenId = getKitchenId();
//		//int totalCount = 0;
//		try {
//			List<SeasoningStockData> seasoningList = null;
//			ArrayList<SeasoningStockDataBO> seasoningBoList = new ArrayList<SeasoningStockDataBO>() ;
//			SeasoningStockDataDAO seasoningDAO = new SeasoningStockDataDAO(this.dbSession);
//			
//			seasoningList = seasoningDAO.querySeasoningListPager(seasoningName, kitchenId, pageIndex, pageLimit,searchdate);
//			if(seasoningList.size()!= 0){
//				for(int i =0;i<seasoningList.size();i++){
//					SeasoningStockDataBO obj = new SeasoningStockDataBO();
//					obj = BoUtil.transSeasoningStockDataToSeasoningStockDataBO(seasoningList.get(i));
//					seasoningBoList.add(obj);
//					//seasoningBoList.add(i,BoUtil.transSeasoningStockDataToSeasoningStockDataBO(seasoningList.get(i)));
//				}
//			}
//			this.responseObj.setTotalNum(seasoningList.size());
//			this.responseObj.setSeasoningList(seasoningBoList);
//			this.responseObj.setResStatus(1);
//			this.responseObj.setMsg("");
//		}catch (Exception ex) {
//			this.responseObj.setResStatus(0);
//			this.responseObj.setMsg(ex.getMessage());
//		}
		
	}

}
