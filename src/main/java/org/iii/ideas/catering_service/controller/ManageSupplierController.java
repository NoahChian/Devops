package org.iii.ideas.catering_service.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.Area;
import org.iii.ideas.catering_service.dao.AreaDAO;
import org.iii.ideas.catering_service.dao.County;
import org.iii.ideas.catering_service.dao.CountyDAO;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.SupplierDAO;
import org.iii.ideas.catering_service.dao.SupplierId;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


public class ManageSupplierController extends MultiActionController {
	
private SupplierDAO supplierDAO;
private CountyDAO countyDAO;
private AreaDAO areaDAO;

public CountyDAO getCountyDAO() {
	return countyDAO;
}

public void setCountyDAO(CountyDAO countyDAO) {
	this.countyDAO = countyDAO;
}

public AreaDAO getAreaDAO() {
	return areaDAO;
}

public void setAreaDAO(AreaDAO areaDAO) {
	this.areaDAO = areaDAO;
}


public ModelAndView supplierContent(HttpServletRequest request,HttpServletResponse response) throws Exception 
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
	int limitCount = 20; 	//每頁筆數
	int pageCounts = 0;		//總頁數

	List<Supplier> supplierList = null;
	
	HashMap<String,Object> supplierMap = supplierDAO.supplierManagerIndex(kitchenId, request, goPage, startCount, limitCount);
	
	//get 筆數&頁數
	supplierList = (List<Supplier>)supplierMap.get("allsupplierList");
	int allCount = supplierList.size();
	
	pageCounts = (allCount%limitCount == 0)? allCount/limitCount : (allCount/limitCount) + 1;
	
	if(supplierMap.get("goPage") != null)
		goPage = (String)supplierMap.get("goPage");
	
	
	//計算要跑哪幾頁出現
	int tmp = (Integer.parseInt(goPage) % 5 == 0)? (int)Math.floor((Integer.parseInt(goPage)/5)) : (int)Math.floor((Integer.parseInt(goPage)/5)+1);
	int runMin = (tmp == 1)? 1 : tmp * 5 - 4;
	int runMax = ((runMin + 4) < pageCounts)? runMin + 4 : pageCounts;
	
	supplierList = (List<Supplier>)supplierMap.get("supplierList");
			
	HashMap<String,Object> responseMap = new HashMap<String,Object>();
	responseMap.put("supplierList",supplierList);
	responseMap.put("nowPage", goPage);
	responseMap.put("allCount", allCount);
	responseMap.put("pageCounts", pageCounts);
	responseMap.put("runMin", runMin);
	responseMap.put("runMax", runMax);
	
	List<County> county = countyDAO.findAll();
	List<Area> area = areaDAO.findAll();
	
	responseMap.put("county", county);
	responseMap.put("area", area);
	

	return new ModelAndView("listSupplier","responseMap",responseMap);
	
	
}

	public ModelAndView createSupplier(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		HttpSession session = request.getSession();
		String kitchenId;
		if(session.getAttribute("account")==null){
			kitchenId="1";
		}else{
			kitchenId = session.getAttribute("account").toString();
		}
		List<County> countyList =countyDAO.findAll();
		List<Area> areaList =areaDAO.findAll();
		
		request.setAttribute("countyList",countyList);
		request.setAttribute("areaList",areaList);
		
		List<Supplier> supplierListNew = null;
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		supplierListNew =supplierDAO.findByKitchenId(Integer.parseInt(kitchenId));

		request.setAttribute("supplierListNew",supplierListNew);
		
		return new ModelAndView("manageSupplier");
	
	}

	
	public SupplierDAO getSupplierDAO() {
		return supplierDAO;
	}

	public void setSupplierDAO(SupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}
	

}
