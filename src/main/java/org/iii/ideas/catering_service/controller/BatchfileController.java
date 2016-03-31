package org.iii.ideas.catering_service.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;








import org.iii.ideas.catering_service.dao.Ingredient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.iii.ideas.catering_service.dao.Batchdata;
import org.iii.ideas.catering_service.dao.BatchfileDAO;
import org.iii.ideas.catering_service.dao.Batchfile;
import org.iii.ideas.catering_service.dao.BatchfileObject;
import org.iii.ideas.catering_service.dao.Ingredientbatchdata;

public class BatchfileController extends MultiActionController {
	
	private BatchfileDAO batchfileDAO;
	
	public ModelAndView batchfileContent(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		HttpSession session = request.getSession();
		//20131213 Ric
				String kitchenId;
				if(session.getAttribute("account")==null){
					kitchenId="1";
				}else{
					kitchenId = session.getAttribute("account").toString();
				}
		String goPage = "1";	//預設頁面
		int startCount = 0; 	//起始筆數
		int limitCount = 10; 	//每頁筆數
		int pageCounts = 0;		//總頁數

		List<Batchfile> batchfileList = null;
		HashMap<String,Object> batchfileMap = batchfileDAO.batchfileManagerIndex(kitchenId, "", "", "", request, goPage, startCount, limitCount);
		
		
		//get 筆數&頁數
		batchfileList = (List<Batchfile>)batchfileMap.get("allbatchfileList");
		int allCount = batchfileList.size();
		
		pageCounts = (allCount%limitCount == 0)? allCount/limitCount : (allCount/limitCount) + 1;
		
		if(batchfileMap.get("goPage") != null)
			goPage = (String)batchfileMap.get("goPage");
		

		
		
		//計算要跑哪幾頁出現
		int tmp = (Integer.parseInt(goPage) % 10 == 0)? (int)Math.floor((Integer.parseInt(goPage)/10)) : (int)Math.floor((Integer.parseInt(goPage)/10)+1);
		int runMin = (tmp == 1)? 1 : tmp * 10 - 9;
		int runMax = ((runMin + 9) < pageCounts)? runMin + 9 : pageCounts;
		
		batchfileList = (List<Batchfile>)batchfileMap.get("batchfileList");
				
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		responseMap.put("batchfileList",batchfileList);
		responseMap.put("nowPage", goPage);
		responseMap.put("allCount", allCount);
		responseMap.put("pageCounts", pageCounts);
		responseMap.put("runMin", runMin);
		responseMap.put("runMax", runMax);
		
		//----------add by dennis 20131212-----------
		List<BatchfileObject> listBatchdata = new ArrayList<BatchfileObject>();
		for(Batchfile batchfile : batchfileList){
			Batchdata batchdata= this.getBatchfileDAO().getBatchDataByIngredinetBatchId(batchfile.getIngredientBatchId());
			Ingredientbatchdata ingredinetbatchdata= this.getBatchfileDAO().getIngredinetBatchDataByIngredinetBatchId(batchfile.getIngredientBatchId());
			BatchfileObject batchfileObject = new BatchfileObject();
			batchfileObject.setBatchfile(batchfile);
			batchfileObject.setBatchdata(batchdata);
			batchfileObject.setIngredinetbatchdata(ingredinetbatchdata);
			listBatchdata.add(batchfileObject);
		}
		//-----------------------------------------------------

		//return new ModelAndView("listBatchfile","responseMap",responseMap); //disable by dennis 20131212
		return new ModelAndView("listBatchdata","responseMap",responseMap); //add by dennis 20131212
		
	}
	
	public ModelAndView searchContent(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		HttpSession session = request.getSession();
		//20131213 Ric
				String kitchenId;
				if(session.getAttribute("account")==null){
					kitchenId="1";
				}else{
					kitchenId = session.getAttribute("account").toString();
				}
		String goPage = "1";	//預設頁面
		int startCount = 0; 	//起始筆數
		int limitCount = 10; 	//每頁筆數
		int pageCounts = 0;		//總頁數

		List<Batchfile> batchfileList = null;
		String schoolName = "";
		String startDate = "";
		String endDate = "";
		
		schoolName = new String(request.getParameter("schoolName").getBytes("ISO-8859-1"),"UTF-8");	
		startDate = new String(request.getParameter("startDate").getBytes("ISO-8859-1"),"UTF-8");	
		endDate = new String(request.getParameter("endDate").getBytes("ISO-8859-1"),"UTF-8");	
		
		HashMap<String,Object> batchfileMap = batchfileDAO.batchfileManagerIndex(kitchenId, schoolName, startDate, endDate, request, goPage, startCount, limitCount);
		
		
		//get 筆數&頁數
		batchfileList = (List<Batchfile>)batchfileMap.get("allbatchfileList");
		int allCount = batchfileList.size();
		
		pageCounts = (allCount%limitCount == 0)? allCount/limitCount : (allCount/limitCount) + 1;
		
		if(batchfileMap.get("goPage") != null)
			goPage = (String)batchfileMap.get("goPage");
		
		
		//計算要跑哪幾頁出現
		int tmp = (Integer.parseInt(goPage) % 10 == 0)? (int)Math.floor((Integer.parseInt(goPage)/10)) : (int)Math.floor((Integer.parseInt(goPage)/10)+1);
		int runMin = (tmp == 1)? 1 : tmp * 10 - 9;
		int runMax = ((runMin + 9) < pageCounts)? runMin + 9 : pageCounts;
		
		batchfileList = (List<Batchfile>)batchfileMap.get("batchfileList"); 
		
		//-------------------------------------------
				
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		responseMap.put("batchfileList",batchfileList);
		responseMap.put("nowPage", goPage);
		responseMap.put("allCount", allCount);
		responseMap.put("pageCounts", pageCounts);
		responseMap.put("runMin", runMin);
		responseMap.put("runMax", runMax);
		

		return new ModelAndView("listBatchfile","responseMap",responseMap);//disable by dennis 20131212
		
		//return new ModelAndView("listBatchData","responseMap",responseMap);//add by dennis 20131212
	}

	public BatchfileDAO getBatchfileDAO() {
		return batchfileDAO;
	}

	public void setBatchfileDAO(BatchfileDAO batchfileDAO) {
		this.batchfileDAO = batchfileDAO;
	}

}
