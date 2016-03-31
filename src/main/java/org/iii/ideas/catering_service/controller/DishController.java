package org.iii.ideas.catering_service.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.criterion.Projections;
import org.iii.ideas.catering_service.dao.BatchdataDAO;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.dao.DishDAO;
import org.iii.ideas.catering_service.dao.Ingredient;
import org.iii.ideas.catering_service.dao.IngredientDAO;
import org.iii.ideas.catering_service.dao.Supplier;
import org.iii.ideas.catering_service.dao.SupplierDAO;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class DishController extends MultiActionController {
	
	private static DishDAO dishDAO;
	private static SupplierDAO supplierDAO;
	private static IngredientDAO ingredientDAO;
	
	public SupplierDAO getSupplierDAO() {
		return supplierDAO;
	}

	public void setSupplierDAO(SupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}

	public IngredientDAO getIngredientDAO() {
		return ingredientDAO;
	}

	public void setIngredientDAO(IngredientDAO ingredientDAO) {
		this.ingredientDAO = ingredientDAO;
	}

	public DishDAO getDishDAO() {
		return dishDAO;
	}

	public void setDishDAO(DishDAO dishDAO) {
		this.dishDAO = dishDAO;
	}
	
	public static ModelAndView addDish(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		HttpSession session = request.getSession();
		//20131213 Ric
				String kitchenId;
				if(session.getAttribute("account")==null){
					kitchenId="1";
				}else{
					kitchenId = session.getAttribute("account").toString();
				}
		String dishid = "";
		String DishName = "";
		String filename = "";
		
		
		//String customFieldValue = "";
		List<String> IngredientNameList = new ArrayList<String>();
		List<String> SupplierList = new ArrayList<String>();
//		List<String> BrandList = new ArrayList<String>();
		// add by Joshua 2014/10/23
		List<String> productNameList = new ArrayList<String>();
		List<String> manufacturerList = new ArrayList<String>();
		
		List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		for (FileItem item : items) {
		    if (item.isFormField()) {
		    	// Process normal fields here.
		    	if(item.getFieldName().equalsIgnoreCase("dishid")) 
	    		dishid = item.getString("UTF-8");
	    		//dishid = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("DishName"))
		    		DishName = item.getString("UTF-8");
	    		//DishName = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("IngredientName"))
		    		IngredientNameList.add(item.getString("UTF-8"));
	    		//IngredientNameList.add(new String(item.getString().getBytes("ISO-8859-1"),"UTF-8"));
		    	
		    	if(item.getFieldName().equalsIgnoreCase("Supplier"))
		    		SupplierList.add(item.getString("UTF-8"));
	    		//SupplierList.add(new String(item.getString().getBytes("ISO-8859-1"),"UTF-8"));
		    	if(item.getFieldName().equalsIgnoreCase("ProductName"))
		    		productNameList.add(item.getString("UTF-8"));
		    	
		    	if(item.getFieldName().equalsIgnoreCase("Manufacturer"))
		    		manufacturerList.add(item.getString("UTF-8"));
//		    	if(item.getFieldName().equalsIgnoreCase("Brand"))
//		    		BrandList.add(item.getString("UTF-8"));
	    		//BrandList.add(new String(item.getString().getBytes("ISO-8859-1"),"UTF-8"));
		    	
		    	//System.out.println("fieldName="+item.getFieldName());
		    	//System.out.println("fieldName="+item.getString());
		    } else {
		        // Process <input type="file"> here.
		       // System.out.println("Field name: " + item.getFieldName());
		       // System.out.println("Field value (file name): " + item.getName());
		        
		        //String relativeWebPath = "/uploads";
		        
		       // String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
		        //filename = FilenameUtils.getName(item.getName());
		        //File file = new File(absoluteDiskPath, filename);
		        //item.write(file);

		        //String fieldName = item.getFieldName();

		        String fileName = item.getName();

		        String contentType = item.getContentType();

		        //boolean isInMemory = item.isInMemory();

		       // long sizeInBytes = item.getSize();

		        //System.out.println("fieldName="+fieldName);

		       // System.out.println("fileName="+fileName);

		       // System.out.println("contentType="+contentType);

		       // System.out.println("isInMemory="+isInMemory);

		        //System.out.println("sizeInBytes="+sizeInBytes);

		        if (fileName != null && !"".equals(fileName)) 
		        {

		            fileName= FilenameUtils.getName(fileName);
	
		           // System.out.println("fileName saved="+fileName);
	
		         //   File uploadedFile = new File("c:\\temp", fileName);
		            
		        //    String fileXls = CateringServiceUtil.converTimestampToStr("yyyyMMddHHmmss", CateringServiceUtil.getCurrentTimestamp())+".xls";
		    		//String excelFileName = CateringServiceUtil.getConfig("uploadPath")+"tmp/"+fileName;
		    		  File uploadedFile = new File(CateringServiceUtil.getConfig("uploadPath")+"tmp/", fileName);
	
		            item.write(uploadedFile);
			    }
		    
		    }
		}
		
		//String UserName = new String(request.getParameter("UserName").getBytes("ISO-8859-1"),"UTF-8");
		//String KitchenName = new String(request.getParameter("KitchenName").getBytes("ISO-8859-1"),"UTF-8");
		//Integer KitchenType = Integer.parseInt(request.getParameter("KitchenType").toString().trim());
		/*File file ;
		
		 String contentType = request.getContentType();
	   if ((contentType.indexOf("multipart/form-data") >= 0)) {

	      DiskFileItemFactory factory = new DiskFileItemFactory();
	      // maximum size that will be stored in memory
	      factory.setSizeThreshold(102400);
	      // Location to save data that is larger than maxMemSize.
	      factory.setRepository(new File("c:\\temp"));

	      // Create a new file upload handler
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      // maximum file size to be uploaded.
	      upload.setSizeMax( 102400 );
	      try{ 
		         // Parse the request to get file items.
		         List fileItems = upload.parseRequest(request);

		         // Process the uploaded file items
		         Iterator i = fileItems.iterator();

		         while ( i.hasNext () ) 
		         {
		            FileItem fi = (FileItem)i.next();
		            if ( !fi.isFormField () )	
		            {
		            // Get the uploaded file parameters
		            String fieldName = fi.getFieldName();
		            String fileName = fi.getName();
		            boolean isInMemory = fi.isInMemory();
		            long sizeInBytes = fi.getSize();
		            // Write the file
		            if( fileName.lastIndexOf("\\") >= 0 ){
		            file = new File( "c:\\" + 
		            fileName.substring( fileName.lastIndexOf("\\"))) ;
		            }else{
		            file = new File( "c:\\" + 
		            fileName.substring(fileName.lastIndexOf("\\")+1)) ;
		            }
		            fi.write( file ) ;
		           
		            }
		         }
		         
		      }catch(Exception ex) {
		         System.out.println(ex);
		      }
		   }*/
		
	
		
		if(!dishid.equals("")) // update
		{
			// 先清除所有食材
			dishDAO.executeSql("delete from ingredient where DishId = " + dishid.toString());
			//schoolDAO.executeSql("delete from SchoolKitchen where KitchenId = " + kitchenId.toString());
			// 再新增
			for(int i = 0; i< IngredientNameList.size(); i++)
			{
				Ingredient ingredient = new Ingredient();
				ingredient.setDishId(Long.valueOf(dishid));
				ingredient.setBrand(""); // table此欄位為not null 所以設定空白值
				// 修正#12185 : 菜色內之食材非為固定廠商供應，應由食材維護時再作選擇
				if (NumberUtils.isNumber(SupplierList.get(i))) {
					ingredient.setSupplierId(Integer.parseInt(SupplierList.get(i)));
				} else {
					ingredient.setSupplierId(new Integer(0)); // set default value : 0
				}
				ingredient.setIngredientName(IngredientNameList.get(i));
				ingredient.setProductName(productNameList.size() > i ? productNameList.get(i) : "");
				ingredient.setManufacturer(manufacturerList.size() > i ? manufacturerList.get(i) : "");
				
				ingredientDAO.save(ingredient);
			}
			
			Dish dish = dishDAO.findById(Long.valueOf(dishid));
			dish.setDishName(DishName);
			dish.setDishShowName(DishName); //20140327 增加showname 欄位 KC
			dish.setPicturePath(filename);
			
			dishDAO.update(dish);
			
		}
		else	
		{
			Dish dish = new Dish();
			dish.setDishName(DishName);
			dish.setPicturePath(filename);
			dish.setKitchenId(Integer.parseInt(kitchenId));
			
			Long sn = dishDAO.save(dish);
			
			for(int i = 0; i< IngredientNameList.size(); i++)
			{
				Ingredient ingredient = new Ingredient();
				ingredient.setDishId(sn);
				ingredient.setBrand(""); // table此欄位為not null 所以設定空白值
				// 修正#12185 : 菜色內之食材非為固定廠商供應，應由食材維護時再作選擇
				if (NumberUtils.isNumber(SupplierList.get(i))) {
					ingredient.setSupplierId(Integer.parseInt(SupplierList.get(i)));
				} else {
					ingredient.setSupplierId(new Integer(0)); // set default value : 0
				}
				ingredient.setIngredientName(IngredientNameList.get(i));
				ingredient.setProductName(productNameList.size() > i ? productNameList.get(i) : "");
				ingredient.setManufacturer(manufacturerList.size() > i ? manufacturerList.get(i) : "");
				
				ingredientDAO.save(ingredient);
			}
		}
		

		return dishContent(request, response);
		
		
	}
	
	public static ModelAndView dishContent(HttpServletRequest request,HttpServletResponse response) throws Exception 
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

		String q_dishName="";
		String q_ingredientName="";

		if (session.getAttribute("searchDishName") != null && !session.getAttribute("searchDishName").toString().isEmpty()){
			q_dishName=session.getAttribute("searchDishName").toString();
		}
		if (session.getAttribute("searchIngredientName") != null && !session.getAttribute("searchIngredientName").toString().isEmpty()){
			q_ingredientName=session.getAttribute("searchIngredientName").toString();	
		}
		
		String searchPage="";
		if (request.getParameter("searchPage")!=null){
			searchPage=request.getParameter("searchPage").toString();
			goPage=searchPage;
		}
		
		HashMap<String,Object> dishMap = dishDAO.findDishAndIngredientByReq(kitchenId,q_dishName ,q_ingredientName, goPage,searchPage, startCount, limitCount);
		List<Object[]> dishList;
		
		int allCount =Integer.parseInt(String.valueOf(dishMap.get("alldishList")));  
		pageCounts = (allCount%limitCount == 0)? allCount/limitCount : (allCount/limitCount) + 1;
		
		if(dishMap.get("goPage") != null)
			goPage = (String)dishMap.get("goPage");
		
		
		//計算要跑哪幾頁出現
		int tmp = (Integer.parseInt(goPage) % 5 == 0)? (int)Math.floor((Integer.parseInt(goPage)/5)) : (int)Math.floor((Integer.parseInt(goPage)/5)+1);
		int runMin = (tmp == 1)? 1 : tmp * 5 - 4;
		int runMax = ((runMin + 4) < pageCounts)? runMin + 4 : pageCounts;
		System.out.println("***dishMap get dishList");
		dishList = (List<Object[]>)dishMap.get("dishList");		
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		responseMap.put("dishList",dishList);
		//responseMap.put("ingredientList",ingredientList);
		responseMap.put("nowPage", goPage);
		responseMap.put("allCount", allCount);
		responseMap.put("pageCounts", pageCounts);
		responseMap.put("runMin", runMin);
		responseMap.put("runMax", runMax);
		
		return new ModelAndView("listDish","responseMap",responseMap);
		
	}
	
	public static  ModelAndView editDish(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{

		HttpSession session = request.getSession();
		//20131213 Ric
				String kitchenId;
				if(session.getAttribute("account")==null){
					kitchenId="1";
				}else{
					kitchenId = session.getAttribute("account").toString();
				}
		String dishId = "";
		List<Supplier> supplierList = null;
		List<Ingredient> ingredientList = null;
		Dish dish = null;
		List<Dish> dishList = null;
		// add by Joshua 2014/10/22
		dishList = dishDAO.findByKitchenId(Integer.parseInt(kitchenId));
		// 修改
		if(request.getParameter("dishid") != null)
			dishId = new String(request.getParameter("dishid"));
		
		if(dishId != "")
		{
			supplierList =supplierDAO.findByKitchenId(Integer.parseInt(kitchenId));
			ingredientList = ingredientDAO.findByDishId(Long.valueOf(dishId));
			dish =dishDAO.findById(Long.valueOf(dishId));
		}

		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		
		responseMap.put("supplierList",supplierList);
		responseMap.put("ingredientList",ingredientList);
		responseMap.put("dish",dish);
		responseMap.put("dishList",dishList);
		
		return new ModelAndView("manageDish","responseMap",responseMap);
	}
	
	
	
	public static ModelAndView searchForm(HttpServletRequest request,HttpServletResponse response) throws Exception 
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

		String q_dishName=request.getParameter("dishName").toString();
		String q_ingredientName=request.getParameter("ingrediantName").toString();
		
		if (request.getParameter("dishName") != null && !request.getParameter("dishName").toString().isEmpty()){
			q_dishName=request.getParameter("dishName").toString().trim();
		}
		if (request.getParameter("ingrediantName") != null && !request.getParameter("ingrediantName").toString().isEmpty()){
			q_ingredientName=request.getParameter("ingrediantName").toString().trim();	
		}
		
		String searchPage="";
		if (request.getParameter("searchPage")!=null){
			searchPage=request.getParameter("searchPage").toString();
		}
		
		session.setAttribute("searchDishName",q_dishName);
		session.setAttribute("searchIngredientName",q_ingredientName);
		
		HashMap<String,Object> dishMap = dishDAO.findDishAndIngredientByReq(kitchenId,q_dishName ,q_ingredientName, goPage,searchPage, startCount, limitCount);
		List<Object[]> dishList;
		
		int allCount =Integer.parseInt(String.valueOf(dishMap.get("alldishList")));  
		pageCounts = (allCount%limitCount == 0)? allCount/limitCount : (allCount/limitCount) + 1;
		
		if(dishMap.get("goPage") != null)
			goPage = (String)dishMap.get("goPage");
		
		
		//計算要跑哪幾頁出現
		int tmp = (Integer.parseInt(goPage) % 5 == 0)? (int)Math.floor((Integer.parseInt(goPage)/5)) : (int)Math.floor((Integer.parseInt(goPage)/5)+1);
		int runMin = (tmp == 1)? 1 : tmp * 5 - 4;
		int runMax = ((runMin + 4) < pageCounts)? runMin + 4 : pageCounts;
		System.out.println("***dishMap get dishList");
		dishList = (List<Object[]>)dishMap.get("dishList");		
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		responseMap.put("dishList",dishList);
		//responseMap.put("ingredientList",ingredientList);
		responseMap.put("nowPage", goPage);
		responseMap.put("allCount", allCount);
		responseMap.put("pageCounts", pageCounts);
		responseMap.put("runMin", runMin);
		responseMap.put("runMax", runMax);
		
		return new ModelAndView("listDish","responseMap",responseMap);
		/*
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

		List<Dish> dishList = null;
		List<Ingredient> ingredientList = null;
		
		
		String dishName = "";
		String ingrediantName = "";
		
		dishName = new String(request.getParameter("dishName"));	
		ingrediantName = new String(request.getParameter("ingrediantName"));	

		session.setAttribute("searchDishName", dishName);	
		session.setAttribute("searchIngredientName", ingrediantName);	
		session.setAttribute("dishPage","");
		HashMap<String,Object> dishMap = dishDAO.dishManagerSearch(kitchenId, dishName, ingrediantName, request, goPage, startCount, limitCount);
		ingredientList = ingredientDAO.findByKitchenId(kitchenId);
		
		//get 筆數&頁數
		dishList = (List<Dish>)dishMap.get("alldishList");
		int allCount = dishList.size();
		
		pageCounts = (allCount%limitCount == 0)? allCount/limitCount : (allCount/limitCount) + 1;
		
		if(dishMap.get("goPage") != null)
			goPage = (String)dishMap.get("goPage");
		
		
		//計算要跑哪幾頁出現
		int tmp = (Integer.parseInt(goPage) % 5 == 0)? (int)Math.floor((Integer.parseInt(goPage)/5)) : (int)Math.floor((Integer.parseInt(goPage)/5)+1);
		int runMin = (tmp == 1)? 1 : tmp * 5 - 4;
		int runMax = ((runMin + 4) < pageCounts)? runMin + 4 : pageCounts;
		
		dishList = (List<Dish>)dishMap.get("dishList");
				
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		responseMap.put("dishList",dishList);
		responseMap.put("ingredientList",ingredientList);
		responseMap.put("nowPage", goPage);
		responseMap.put("allCount", allCount);
		responseMap.put("pageCounts", pageCounts);
		responseMap.put("runMin", runMin);
		responseMap.put("runMax", runMax);
		

		return new ModelAndView("listDish","responseMap",responseMap);
		*/
		
	}
	
	public  static ModelAndView delDish(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{

		HttpSession session = request.getSession();
		//20131213 Ric
				String kitchenId;
				if(session.getAttribute("account")==null){
					kitchenId="1";
				}else{
					kitchenId = session.getAttribute("account").toString();
				}
		String dishId = "";
		List<Supplier> supplierList = null;
		List<Ingredient> ingredientList = null;
		
		Dish dish = null;
		// 修改
		if(request.getParameter("dishid") != null){
			dishId = new String(request.getParameter("dishid"));
		}
		
		//刪除前先檢查菜單內是否有使用這道菜，若有關聯則不可刪除  20140328 KC
		BatchdataDAO batchdataDAO=new BatchdataDAO();
		batchdataDAO.setSessionFactory(HibernateUtil.buildSessionFactory());
		if (batchdataDAO.isDishIdExists(Integer.parseInt(kitchenId), Long.valueOf(dishId))){
			//throw new Exception("菜單內已有此菜色，不可刪除!");
			HashMap<String,Object> responseMap = new HashMap<String,Object>();
			responseMap.put("errorMsg", "刪除失敗! 學校菜單內已有此菜色，基本資料不可刪除!");
			
			return new ModelAndView("listDish","responseMap",responseMap);
		}
		
		
		if(dishId != "")
		{
			// 先清除所有食材
			dishDAO.executeSql("delete from ingredient where DishId = " + dishId.toString()  );
			//dish =dishDAO.findById(Integer.parseInt(dishId));
			//dishDAO.delete(dish);
			dishDAO.executeSql("delete from dish where DishId = " + dishId.toString() +" and kitchenid=" +kitchenId);
		}
		
		
		return dishContent(request, response);
	
	}

	
	public static ModelAndView createDish(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{
		HttpSession session = request.getSession();
		//20131213 Ric
				String kitchenId;
				if(session.getAttribute("account")==null){
					kitchenId="1";
				}else{
					kitchenId = session.getAttribute("account").toString();
				}
		String dishId = "";
		List<Supplier> supplierList = null;
		List<Ingredient> ingredientList = null;
		List<Dish> dishList = null;
		Dish dish = null;

		supplierList = supplierDAO.findByKitchenId(Integer.parseInt(kitchenId));
		dishList = dishDAO.findByKitchenId(Integer.parseInt(kitchenId));
		
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		
		responseMap.put("supplierList",supplierList);
		responseMap.put("ingredientList",ingredientList);
		responseMap.put("dishList",dishList);
		responseMap.put("dish",dish);
		
		return new ModelAndView("manageDish","responseMap",responseMap);
	}


	

}
