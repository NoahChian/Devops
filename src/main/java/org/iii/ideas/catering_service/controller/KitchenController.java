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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.Area;
import org.iii.ideas.catering_service.dao.AreaDAO;
import org.iii.ideas.catering_service.dao.County;
import org.iii.ideas.catering_service.dao.CountyDAO;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.KitchenDAO;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.SchoolDAO;
import org.iii.ideas.catering_service.dao.UidDAO;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;

public class KitchenController extends MultiActionController {

	private static KitchenDAO kitchenDAO;
	private static CountyDAO countyDAO;
	private static SchoolDAO schoolDAO;
	private static UidDAO uidDAO;
	private static AreaDAO areaDAO;
	public SchoolDAO getSchoolDAO() {
		return schoolDAO;
	}



	public void setSchoolDAO(SchoolDAO schoolDAO) {
		this.schoolDAO = schoolDAO;
	}



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



	public static ModelAndView kitchenContent(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		HttpSession session = request.getSession();
		//20131213 Ric
		String kitchenId;
		if (session.getAttribute("account") == null) {
			kitchenId = "1";
		} else {
			kitchenId = session.getAttribute("account").toString();
		}
		Kitchen kitchen = (Kitchen)kitchenDAO.findById(Integer.parseInt(kitchenId));
				
		HashMap<String,Object> responseMap = new HashMap<String,Object>();
		responseMap.put("kitchen",kitchen);
		
		List<County> county = countyDAO.findAll();
		List<Area> area = areaDAO.findAll();
		
		responseMap.put("countyList", county);
		responseMap.put("areaList", area);
		
		// Raymond 20140429 schoolId 改抓 schoolCode 於頁面上顯示
		List<Object> schoolList = schoolDAO.executeQuery("select school.SchoolId,school.SchoolCode, school.SchoolName from schoolkitchen, school where schoolkitchen.SchoolId = school.SchoolId and schoolkitchen.KitchenId = " + kitchenId);
		//Ric 20131215
		responseMap.put("schoolList", schoolList);
		
		return new ModelAndView("manageKitchen","responseMap",responseMap);

	}
	
	public static ModelAndView updateKitchen(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		HttpSession session = request.getSession();
		//20131213 Ric
				String kitchenId;
				if(session.getAttribute("account")==null){
					kitchenId="1";
				}else{
					kitchenId = session.getAttribute("account").toString();
				}
		
		String Ownner = "";
		String Address = "";
		String Tel = "";
		String Fax = "";
		String Nutritionist = "";
		String Chef = "";
		String Qualifier = "";
		String HACCP = "";
		String Insurement = "";
		String PicturePath = "";
		String Email = "";
		
		String customFieldValue = "";
		List<String> schoolList = new ArrayList<String>();
		
		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session dbSession = sessionFactory.openSession();
		Transaction tx = dbSession.beginTransaction();
		
		List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		for (FileItem item : items) {
		    if (item.isFormField()) {
		        // Process normal fields here.
		    	if(item.getFieldName().equalsIgnoreCase("Ownner"))
		    		Ownner = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("Address"))
		    		 Address =  item.getString("UTF-8");
		    		//Address = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("Tel"))
		    		Tel = item.getString("UTF-8");
		    		//Tel = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("Fax"))
		    		Fax =  item.getString("UTF-8");
		    		//Fax = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("Nutritionist"))
		    		Nutritionist = item.getString("UTF-8");
		    		//Nutritionist = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("Chef"))
		    		Chef =  item.getString("UTF-8");
	    			//Chef = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("Qualifier"))
		    		Qualifier =  item.getString("UTF-8");
	    			//Qualifier = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("HACCP"))
		    		HACCP = item.getString("UTF-8");
	    			//HACCP = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("Email"))
		    		Email = item.getString("UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("Insurement"))
		    		Insurement = item.getString("UTF-8");
		    		//Insurement = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("customFieldValue"))
		    		customFieldValue = item.getString("UTF-8");
	    			//customFieldValue = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8");
		    	
		    	if(item.getFieldName().equalsIgnoreCase("customFieldValue[]"))
		    		schoolList.add(item.getString("UTF-8"));
	    			//schoolList.add(new String(item.getString().getBytes("ISO-8859-1"),"UTF-8"));
		    	
		    	System.out.println("fieldName="+item.getFieldName());
		    	System.out.println("fieldName="+item.getString());
		    } else {
		        // Process <input type="file"> here.
		        System.out.println("Field name: " + item.getFieldName());
		        System.out.println("Field value (file name): " + item.getName());
		        
		        String relativeWebPath = "/uploads";
		        
		        //String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
		       // String filename = FilenameUtils.getName(item.getName());
		        //File file = new File(absoluteDiskPath, filename);
		        //item.write(file);

		        String fieldName = item.getFieldName();

		        String fileName = item.getName();

		        String contentType = item.getContentType();

		        boolean isInMemory = item.isInMemory();

		        long sizeInBytes = item.getSize();

		        //System.out.println("fieldName="+fieldName);

		       // System.out.println("fileName="+fileName);

		        //System.out.println("contentType="+contentType);

		        //System.out.println("isInMemory="+isInMemory);

		        //System.out.println("sizeInBytes="+sizeInBytes);
		        /*
		        if (fileName != null && !"".equals(fileName)) 
		        {

		            fileName= FilenameUtils.getName(fileName);
	
		            System.out.println("fileName saved="+fileName);
		            
		            String strName = uidDAO.getNewUID();
	
		            File uploadedFile = new File(absoluteDiskPath, strName + ".jpg");
		            
		            PicturePath = strName + ".jpg";
	
		            item.write(uploadedFile);
			    }
		    	*/
		    }
		}
		
	
	
		
		if(!kitchenId.equals("")) // update
		{
			// ���斗��飛��
			schoolDAO.executeSql(dbSession,"delete from schoolkitchen where KitchenId = " + kitchenId.toString());
			// �憓�
			for(int i = 0; i< schoolList.size(); i++)
			{
				schoolDAO.executeSql(dbSession,"insert into schoolkitchen(SchoolId, KitchenId) values(" + schoolList.get(i) + ", " + kitchenId.toString() + ")");
			}
			
			//Kitchen kitchen = kitchenDAO.findById(Integer.parseInt(kitchenId));
			Kitchen kitchen = HibernateUtil.queryKitchenById(dbSession,Integer.valueOf( kitchenId ));
			kitchen.setAddress(Address);
			kitchen.setOwnner(Ownner);
			kitchen.setTel(Tel);
			kitchen.setFax(Fax);
			kitchen.setNutritionist(Nutritionist);
			kitchen.setChef(Chef);
			kitchen.setQualifier(Qualifier);
			kitchen.setHaccp(HACCP);
			kitchen.setInsurement(Insurement);
			kitchen.setPicturePath(PicturePath);
			kitchen.setEmail(Email);
			dbSession.update(kitchen);
			//kitchenDAO.update(dbSession,kitchen);
		}
		if(!tx.wasCommitted()){
			tx.commit();
		}
		dbSession.close();
		return kitchenContent(request, response);
		
		
	}
	
	public static void getSchoolJson(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		
		//String input = new String(java.net.URLDecoder.decode(request.getParameter("county"), "UTF-8").getBytes("ISO-8859-1"),"UTF-8");

		String input = request.getParameter("area");
		
		if(input != "" )
		{	
			List<School> schoolList = schoolDAO.findByAreaId(Integer.parseInt(input));
	
			String json = new Gson().toJson(schoolList); // anyObject = List<Bean>, Map<K, Bean>, Bean, String, etc..
			
			System.out.println(json);
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);
		}
	}

	public KitchenDAO getKitchenDAO() {
		return kitchenDAO;
	}

	public void setKitchenDAO(KitchenDAO kitchenDAO) {
		this.kitchenDAO = kitchenDAO;
	}



	public UidDAO getUidDAO() {
		return uidDAO;
	}



	public void setUidDAO(UidDAO uidDAO) {
		this.uidDAO = uidDAO;
	}


}
