/*
 * 20140420 KC
 * 重整SupplierController 與 ManageSupplierController 合併
 * 原ManageSupplierController取消使用
 * */

package org.iii.ideas.catering_service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.County;
import org.iii.ideas.catering_service.dao.Area;
import org.iii.ideas.catering_service.dao.SupplierDAO;
import org.iii.ideas.catering_service.dao.CountyDAO;
import org.iii.ideas.catering_service.dao.AreaDAO;
import org.iii.ideas.catering_service.dao.SupplierId;
import org.iii.ideas.catering_service.util.HibernateUtil;


public class SupplierController extends MultiActionController {
	
	private static  SupplierDAO supplierDAO;
	private static CountyDAO countyDAO;
	private static AreaDAO areaDAO;
	
	
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
	
	public SupplierDAO getSupplierDAO() {
		return supplierDAO;
	}

	public void setSupplierDAO(SupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}

	public static  ModelAndView supplierContent(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		HttpSession session = request.getSession();
		//20131213 Ric
		String kitchenId;
		if(session.getAttribute("account")==null){
			kitchenId="1";
		}else{
			kitchenId = session.getAttribute("account").toString();
		}
		
		String goPage = "1";	//�身�
		int startCount = 0; 	//韏瑕�蝑
		int limitCount = 20; 	//瘥�蝑
		int pageCounts = 0;		//蝮賡���

		List<Supplier> supplierList = null;
//		System.out.print("eeee");
		HashMap<String,Object> supplierMap = supplierDAO.supplierManagerIndex(kitchenId, request, goPage, startCount, limitCount);
		
		//get 蝑&�
		supplierList = (List<Supplier>)supplierMap.get("allsupplierList");
		int allCount = supplierList.size();
		
		pageCounts = (allCount%limitCount == 0)? allCount/limitCount : (allCount/limitCount) + 1;
		
		if(supplierMap.get("goPage") != null)
			goPage = (String)supplierMap.get("goPage");
		
		
		//閮�閬��芸嗾���
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
	
	
	public static ModelAndView searchForm(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		HttpSession session = request.getSession();
		//String kitchenId = session.getAttribute("kitchenId").toString();
		//20131213 Ric
		String kitchenId;
		if(session.getAttribute("account")==null){
			kitchenId="1";
		}else{
			kitchenId = session.getAttribute("account").toString();
		}
		String goPage = "1";	//�身�
		int startCount = 0; 	//韏瑕�蝑
		int limitCount = 20; 	//瘥�蝑
		int pageCounts = 0;		//蝮賡���

		List<Supplier> supplierList = null;
		
		String supplierName = "";
		
		supplierName = new String(request.getParameter("supplierName"));	
		session.setAttribute("searchSupplierName", supplierName);	
		session.setAttribute("supplyPage","");
		HashMap<String,Object> supplierMap = supplierDAO.supplierManagerSearch(kitchenId, supplierName, request, goPage, startCount, limitCount);
		
		//get 蝑&�
		supplierList = (List<Supplier>)supplierMap.get("allsupplierList");
		int allCount = supplierList.size();
		
		pageCounts = (allCount%limitCount == 0)? allCount/limitCount : (allCount/limitCount) + 1;
		
		if(supplierMap.get("goPage") != null)
			goPage = (String)supplierMap.get("goPage");
		
		
		//閮�閬��芸嗾���
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
	
	

	public static ModelAndView delSupplier(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		HttpSession session = request.getSession();
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session dbSession = sessionFactory.openSession();
		Transaction tx = dbSession.beginTransaction();
		String kitchenId;
		if(session.getAttribute("account")==null){
			kitchenId="1";
		}else{
			kitchenId = session.getAttribute("account").toString();
		}
		String supplierid = "";
		
		if(request.getParameter("supplierid") != null)
		{
			supplierid = new String(request.getParameter("supplierid"));
			//Supplier obj = supplierDAO.findById(Integer.parseInt(supplierid));
			SupplierId sid = new SupplierId();
			sid.setSupplierId(Integer.valueOf(supplierid));
			sid.setKitchenId(Integer.valueOf(kitchenId));
			Supplier obj = supplierDAO.findById(sid);
			supplierDAO.delete(dbSession,obj);
		}
		if(!tx.wasCommitted()){
			tx.commit();
		}
		dbSession.close();
		return supplierContent(request, response);
	}
	
	
	public static ModelAndView addSupplier(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		HttpSession session = request.getSession();
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session dbSession = sessionFactory.openSession();
		Transaction tx = dbSession.beginTransaction();
		//20131213 Ric
				String kitchenId;
				if(session.getAttribute("account")==null){
					kitchenId="1";
				}else{
					kitchenId = session.getAttribute("account").toString();
				}
		String supplierid = "";
		
		if(request.getParameter("supplierid") != null)
				supplierid = new String(request.getParameter("supplierid"));
		

		String supplierName = new String(request.getParameter("supplierName"));
		String companyId = new String(request.getParameter("companyId"));
		//String supplierName = request.getParameter("supplierName").toString().trim();
		
		//String supplierCity = new String(request.getParameter("supplierCity").getBytes("ISO-8859-1"),"UTF-8");
		//String supplierArea = new String(request.getParameter("supplierArea").getBytes("ISO-8859-1"),"UTF-8");
		Integer countyId = Integer.parseInt(request.getParameter("countyId").toString().trim());
		Integer areaId = Integer.parseInt(request.getParameter("areaId").toString().trim());
		
		String ownner = new String(request.getParameter("ownner"));
		String supplierTel = new String(request.getParameter("supplierTel"));
		String supplierAdress = new String(request.getParameter("supplierAdress"));
		String supplierCertification = new String(request.getParameter("supplierCertification"));
		
		if(!supplierid.equalsIgnoreCase(""))
		{
			//Supplier obj = supplierDAO.findById(Integer.parseInt(supplierid));
			SupplierId sid = new SupplierId();
			sid.setSupplierId(Integer.valueOf(supplierid));
			sid.setKitchenId(Integer.valueOf(kitchenId));
			
			Supplier obj = supplierDAO.findById(sid);
			obj.setCompanyId(companyId);
			//obj.getId().setKitchenId(Integer.parseInt(kitchenId));//20131212  add by dennis
			//obj.setKitchenId(Integer.parseInt(kitchenId)); //20131212  disable by dennis
			obj.setOwnner(ownner);
			obj.setSupplierAdress(supplierAdress);
			obj.setAreaId(areaId);
			obj.setCountyId(countyId);
			obj.setSupplierName(supplierName);
			obj.setSupplierTel(supplierTel);
			obj.setSupplierCertification(supplierCertification);
			
			supplierDAO.update(dbSession,obj);
		}
		else
		{
			Supplier obj = new Supplier(); 
			SupplierId objId = new SupplierId();
			obj.setId(objId);
			obj.setCompanyId(companyId);
			obj.getId().setKitchenId(Integer.parseInt(kitchenId));//20131212  add by dennis
			//obj.setKitchenId(Integer.parseInt(kitchenId)); //20131212  disable by dennis
			obj.setOwnner(ownner);
			obj.setSupplierAdress(supplierAdress);
			obj.setAreaId(areaId);
			obj.setCountyId(countyId);
			obj.setSupplierName(supplierName);
			obj.setSupplierTel(supplierTel);
			obj.setSupplierCertification(supplierCertification);
			
			supplierDAO.save(dbSession,obj);
		}
	
		if(!tx.wasCommitted()){
			tx.commit();
		}
		dbSession.close();
	
		return supplierContent(request, response);
		
		//return editSupplier(request,response);
	}
	
	public static void autoCompanyComplete(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String input = request.getParameter("q");
		List<County> countyareaList = countyDAO.findAll();

		int companyLengh = countyareaList.size();
		for(int i=0;i<companyLengh;i++)
			response.getWriter().write(countyareaList.get(i).getCounty()+"\n");		
	}
	

	
	public static  ModelAndView editSupplier(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		String kitchenId;
		if(session.getAttribute("account")==null){
			kitchenId="1";
		}else{
			kitchenId = session.getAttribute("account").toString();
		}
		String supplierid = request.getParameter("supplierid");
		
		if(supplierid != null)
		{
			SupplierId sid = new SupplierId();
			sid.setSupplierId(Integer.valueOf(supplierid));
			sid.setKitchenId(Integer.valueOf(kitchenId));
			Supplier supplier = supplierDAO.findById(sid);
			
			List<County> countyList = countyDAO.findAll();
			List<Area> areaList = areaDAO.findAll();
			
			
			request.setAttribute("countyList",countyList);
			request.setAttribute("areaList",areaList);
			request.setAttribute("editSupplier",supplier);
		
			return new ModelAndView("manageSupplier");
		}
		else
			return supplierContent(request, response);
	}

	public static  ModelAndView createSupplier(HttpServletRequest request,HttpServletResponse response) throws Exception 
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


	

}
