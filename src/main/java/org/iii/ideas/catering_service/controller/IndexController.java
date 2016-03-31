package org.iii.ideas.catering_service.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import java.util.List;
import org.iii.ideas.catering_service.dao.Kitchen;
import org.iii.ideas.catering_service.dao.KitchenDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;



public class IndexController extends MultiActionController {
	
	private KitchenDAO kitchenDAO;
	
	public ModelAndView indexContent(HttpServletRequest request,HttpServletResponse response) throws Exception 
	{	
		String UserName = "";
		String UserPassword = "";
		
		List<Kitchen> kitchen = null;
		
		HttpSession session = request.getSession();
		UserName = new String(request.getParameter("UserName").getBytes("ISO-8859-1"),"UTF-8");	
		UserPassword = new String(request.getParameter("UserPassword").getBytes("ISO-8859-1"),"UTF-8");	
		
		kitchen = kitchenDAO.findByLogin(UserName, UserPassword);
		
		// 登入
		if(kitchen.size() > 0)
		{
			session.setAttribute("kitchenId", kitchen.get(0).getKitchenId());
			session.setAttribute("KitchenType", kitchen.get(0).getKitchenType());
			session.setAttribute("isLogin", true);
		}
		else
		{
			session.setAttribute("kitchenId", "");
			session.setAttribute("KitchenType", "");
			session.setAttribute("isLogin", false);
		}
		
		return new ModelAndView("index");
		
	}
	
	public KitchenDAO getKitchenDAO() {
		return kitchenDAO;
	}

	public void setKitchenDAO(KitchenDAO kitchenDAO) {
		this.kitchenDAO = kitchenDAO;
	}
	

}
