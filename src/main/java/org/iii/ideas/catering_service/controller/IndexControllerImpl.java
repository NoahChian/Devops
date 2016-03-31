package org.iii.ideas.catering_service.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.iii.ideas.catering_service.common.Common;
import org.iii.ideas.catering_service.dao.County;
import org.iii.ideas.catering_service.dao.School;
import org.iii.ideas.catering_service.dao.Useraccount;
import org.iii.ideas.catering_service.dao.UseraccountDAO;
import org.iii.ideas.catering_service.dao.Userrole;
import org.iii.ideas.catering_service.rest.object.GenericWebRestRequest;
import org.iii.ideas.catering_service.util.AuthenUtil;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

@Component
public class IndexControllerImpl implements IIndexController {
	private Logger log = Logger.getLogger(this.getClass());


	@RequestMapping(value = "/custom/{type_name}/{file_name}/", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView setCustomPage(
			@PathVariable("file_name") String fileName,
			@PathVariable("type_name") String typeName,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
			response.setHeader("X-Frame-Options","");

		try {
			SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
			Session dbsession = sessionfactory.openSession();
			//System.out.println("fileName:"+fileName+"\t typeName:"+typeName);
			if ("county".equals(typeName)) {
				Criteria criteria = dbsession.createCriteria(County.class);
				criteria.add(Restrictions.eq("countyId",
						Integer.valueOf(fileName)));
				criteria.add(Restrictions.eq("enable", 1));
				criteria.setMaxResults(1);
				County row = (County) criteria.uniqueResult();
				if (row == null) {
					return new ModelAndView("custom/nopage");
				}

				// new ModelAndView("listDish","responseMap",responseMap);
				HashMap<String, String> responseMap = new HashMap<String, String>();
				responseMap.put("countyId", fileName);
				responseMap.put("countyName", row.getCounty());
				responseMap.put("basepath", "../../../../");
				return new ModelAndView("custom/calendar", "responseMap",
						responseMap);
			}

			if ("school".equals(typeName)) {
				//確認學校代碼是否存在
				Criteria criteria = dbsession.createCriteria(School.class);
				criteria.add(Restrictions.eq("schoolCode", fileName));
				criteria.add(Restrictions.eq("enable", 1));
				criteria.setMaxResults(1);
				School row = (School) criteria.uniqueResult();

				if (row == null) {
					return new ModelAndView("custom/nopage");
				}
				HashMap<String, String> responseMap = new HashMap<String, String>();
				responseMap.put("schoolName", row.getSchoolName());
				responseMap.put("schoolId", row.getSchoolId().toString());
				responseMap.put("basepath", "../../../../");

				// 如果是以POST方式傳入時，表示是由mschool點擊button，POST方式傳值。
				// 2015.01.05 Steven
				if(request.getMethod().equals("POST")) {
					String mData = request.getParameter("mData").toString().trim();
					responseMap.put("mData", mData);
				}

				return new ModelAndView("custom/school", "responseMap",
						responseMap);
			}

			//小型iframe add by ellis 20141224
			if ("mschool".equals(typeName)) {
				Criteria criteria = dbsession.createCriteria(School.class);
				criteria.add(Restrictions.eq("schoolCode", fileName));
				criteria.add(Restrictions.eq("enable", 1));
				criteria.setMaxResults(1);
				School row = (School) criteria.uniqueResult();
				if (row == null) {
					return new ModelAndView("custom/nopage");
				}
				HashMap<String, String> responseMap = new HashMap<String, String>();
				responseMap.put("schoolName", row.getSchoolName());
				responseMap.put("schoolId", row.getSchoolId().toString());
				responseMap.put("basepath", "../../../../");
				responseMap.put("schoolCode", fileName);
				return new ModelAndView("custom/mschool", "responseMap",
						responseMap);
			}

			//導入員生消費合作社
			if("customerQuerySF".equals(typeName)){ 
				Criteria criteria = dbsession.createCriteria(School.class);
				criteria.add( Restrictions.eq("schoolId", Integer.parseInt(fileName)));
				criteria.add( Restrictions.eq("enable", 1 ));
				criteria.setMaxResults(1);
				School row = (School) criteria.uniqueResult();
				if (row==null){
					return new ModelAndView("custom/nopage");
				}
				HashMap<String ,String> responseMap =new HashMap<String ,String>();
				responseMap.put("schoolName", row.getSchoolName());
				responseMap.put("schoolId", row.getSchoolId().toString());
				responseMap.put("schoolCode", row.getSchoolCode().toString());
				responseMap.put("basepath", "../../../../");
				return new ModelAndView("customerQuerySF","responseMap",responseMap);
			}
			
			//隱私權政策頁面
			if("privacy".equals(typeName)){
				return new ModelAndView("custom/privacy");
			}
			return new ModelAndView("custom/nopage");
		} catch (Exception ex) {
			return new ModelAndView("custom/nopage");
		}
	}

	
	
	//-----
	@Override
	@RequestMapping(value = "/{file_name}/", method = RequestMethod.GET)
	public ModelAndView setAllPage(@PathVariable("file_name") String fileName,HttpServletRequest request,HttpServletResponse response) {
		System.out.println(fileName);
		if(CateringServiceCode.System_States==1){
			try{
				if (fileName.equals("main")){
					return main( request, response);
				}
				if(fileName.equals("index")){
					return index( request, response); 
				}
				if (fileName.equals("logout")){
					return logout(request, response);
				}
				//Raymond 改用新版
	//			if(fileName.equals("listSupplier")){
	//				return SupplierController.supplierContent(request,response);
	//			}
				if(fileName.equals("manageDish")){
					return DishController.dishContent(request, response);
				}
				//啟用/關閉維護頁面
				if(fileName.equals("systemMaintainSwitch")){
					return systemMaintainSwitch(request, response);
				}
				/* 20140528 Ric版本更新
				if (fileName.equals("manageKitchen")){
					return KitchenController.kitchenContent(request, response);
				}
				*/
				return  new ModelAndView(fileName);
			}catch(Exception ex){
				return new ModelAndView("");
			}
		}else{
			//CateringServiceCode.System_States為0時，進入維護頁面。
			//僅有systemMaintainSwitch例外。
			if(fileName.equals("systemMaintainSwitch")){
				return systemMaintainSwitch(request, response);
			}else{
				return maintain(request, response);
			}
		}
	}
	//--------

	private ModelAndView index(HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
		ModelAndView model = new ModelAndView();
	  	//已登入
		HttpSession session = request.getSession();
		if(session.getAttribute("account") != null)
		{	//Mobile by Ric 
			String checkMoblie = request.getHeader("User-Agent");
			//ModelAndView model = null;
			if(checkMoblie.toLowerCase().indexOf("mobile") != -1 || checkMoblie.toLowerCase().indexOf("htc") != -1 && checkMoblie.toLowerCase().indexOf("ipad") == -1)
				model= new ModelAndView("redirect:/web/msearch/");	
			else
				model.setViewName("redirect:/web/main/");
			return model;
		}
		else
		{
			model.setViewName("index/index");
			return model;
		}
	}
	

	private ModelAndView main(HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
		//Mobile by Ric 
		ModelAndView model = new ModelAndView();
		String checkMoblie = request.getHeader("User-Agent");
		if(checkMoblie.toLowerCase().indexOf("mobile") != -1 || checkMoblie.toLowerCase().indexOf("htc") != -1 && checkMoblie.toLowerCase().indexOf("ipad") == -1)
			model= new ModelAndView("mobile/msearch");	
		else
			 model = new ModelAndView("main/index");
		return model;
		
	}
	/**
	 * 系統維護頁面顯示 add by ellis 20150504
	 * @param request
	 * @param response
	 * @return
	 */
	private ModelAndView maintain(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model= new ModelAndView("main/maintain");
		return model;
	}
	
	/**
	 * 系統維護頁面 切換開關頁 add by ellis 20150504
	 * @param request
	 * @param response
	 * @return
	 */
	private ModelAndView systemMaintainSwitch(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model= new ModelAndView("systemMaintainSwitch");
		return model;
	}
	
	@RequestMapping(value = "/main/login/",method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		ModelAndView model = new ModelAndView();
		response.setContentType("text/html;charset=UTF-8"); 
		HttpSession session = request.getSession();	
		
		String login_type = request.getParameter("logintype").trim();
		String license = "";
		String account = request.getParameter("usename").trim();
		String password = request.getParameter("userpwd").trim();
		Common common = new Common();
		String md5pwd = common.getEncoderByMd5(password);
		if(account.isEmpty())
		{
			response.getWriter().print("<script>alert('帳號為空值');history.go(-1)</script>");
			return null;
		}
		
		if(password.isEmpty())
		{
			response.getWriter().print("<script>alert('密碼為空值');history.go(-1)</script>");
			return null;
		}
		//----------------------------------------------------------
		
		SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
		Session dbsession = sessionfactory.openSession();
		Transaction tx = dbsession.beginTransaction();
		try {
			Criteria criteria = dbsession.createCriteria(Useraccount.class);
			criteria.add( Restrictions.eq("username", account) );
			criteria.add( Restrictions.eq("password", md5pwd) );
			List<Useraccount> queryObject = criteria.list();
			if(queryObject.size()==0){
				response.getWriter().print("<script>alert('帳號密碼錯誤，請重新登入！');history.go(-1)</script>");
				dbsession.close();
				
				return null;
			}else{
			Iterator<Useraccount> iterator = queryObject.iterator();
			Useraccount useraccount=null;
			while (iterator.hasNext()) {
				useraccount = iterator.next();
			}

				String uName = useraccount.getUsername();
				String uType = useraccount.getUsertype().toString();
				String uKitchenId = useraccount.getKitchenId().toString();
				String StrName = useraccount.getName().toString();
				String uEmail = "";
				if(!CateringServiceUtil.isNull(useraccount.getEmail())){
					uEmail = useraccount.getEmail().toString();
				}
				// 2014/06/20 如果帳號Enable = 0 則拒絕登入
				if ("0".equals(useraccount.getEnable().toString())) {
					response.getWriter()
							.print("<script>alert('帳戶權限不足，請連繫管理人員！');history.go(-1)</script>");
					dbsession.close();
					return null;
				}
				// --------------------get user role
				// //原本code是用kid去撈kitchen的kName
				// Criteria criteria2 = dbsession.createCriteria(Kitchen.class);
				// criteria2.add( Restrictions.eq("kitchenId",
				// Integer.parseInt(uKitchenId)) );
				Criteria criteria2 = dbsession.createCriteria(Userrole.class);
				criteria2.add(Restrictions.eq("username", uName));

				List<Userrole> queryObject2 = criteria2.list();
				if (queryObject2.size() == 0) {
					response.getWriter().print(
							"<script>alert('找不到帳戶權限資訊，請聯絡管理人員！');window.location.href='"
									+ request.getContextPath()
									+ "/web/main/'</script>");
					dbsession.close();

					return null;
				}
			
			//登入成功並儲存時間 add by Ellis 20151106 
			useraccount.setLastlogintime(CateringServiceUtil.getCurrentTimestamp());
			dbsession.save(useraccount);
			tx.commit();
			
			//Iterator<Kitchen> iterator2 = queryObject2.iterator();
			Userrole role=queryObject2.get(0);
			/*while (iterator2.hasNext()) {
				kitchen = iterator2.next();
			}*/
			//存入縣市值
			//帳戶名稱取得縣市編號
			Integer county = AuthenUtil.getCountyNumByUsername(uName);
			
			String roletype = role.getRoletype();
			//-------------------
			session.setAttribute("StrName", StrName);
			session.setAttribute("uName", uName);
			session.setAttribute("uType", uType);
			session.setAttribute("account", uKitchenId);
			session.setAttribute("roletype", roletype);
			session.setAttribute("county", county.toString());
			session.setAttribute("email", uEmail);
			/*
			 * 001 加工食品 002 連鎖餐飲 003 食品通路 004 農方供應商 005 團膳業者 006 自立廚房 else
			 * 12F 主管機關>地方>新北市
			 */
			System.out.println("uType: "+uType);
			System.out.println("roletype: "+roletype);
			
			//取得用戶所屬的SchoolId
			UseraccountDAO userDao = new UseraccountDAO(dbsession);
			List<Integer> schoolIdList = userDao.querySchoolIdListByUsername(uName);
			String idList = "";
			for(int i=0;i<schoolIdList.size();i++){
				if(idList.equals(""))
					idList = String.valueOf(schoolIdList.get(i));
				else
					idList += "," + String.valueOf(schoolIdList.get(i));
			}
//			String[] tmpList = schoolIdList.toArray((new String[]{}));
//			String idList = Arrays.toString(tmpList);
			session.setAttribute("schoolId", idList);
			
			if (uType.equalsIgnoreCase("001")
					|| uType.equalsIgnoreCase("002")
					|| uType.equalsIgnoreCase("003")
					|| uType.equalsIgnoreCase("004")
					|| uType.equalsIgnoreCase("005")
					|| uType.equalsIgnoreCase("006")
					|| uType.equalsIgnoreCase("101")
					|| uType.equalsIgnoreCase("102")
					|| uType.equalsIgnoreCase("103")) {
				// 偷懶 先這樣寫 為system admin轉址到service頁面 20140911 KC
				if ("kSys".equals(roletype)) {
					model.setViewName("redirect:/web/service/");
				} else {
					model.setViewName("redirect:/web/manageKitchen/");
				}

				/*
				 * if (request.getHeader("User-Agent") != null &&
				 * request.getHeader
				 * ("User-Agent").toLowerCase().indexOf("msie") != -1)
				 * response.getWriter().print(
				 * "<script>alert('您所使用的瀏覽器可能會影響部分功能的使用！\n建議您使用Google Chrome或Mozilla FileZilla瀏覽器。\n您可以點選系統頁面下方所提供之下載連結。');window.location.href='"
				 * +request.getContextPath()+
				 * "/web/manageKitchen/manageKitchen.do'</script>");
				 */
				} else if (uType.equalsIgnoreCase("11") || uKitchenId.equalsIgnoreCase("0")){
					model.setViewName("redirect:/web/kitchenIngredient/");
				} else if (uType.equalsIgnoreCase("007")){
					model.setViewName("redirect:/web/manageKitchen/");
				} else if (uType.equalsIgnoreCase("009")){
					model.setViewName("redirect:/web/manageListSchoolProductSet/");
				} else {
					model.setViewName("redirect:/web/main/");
				}
				
				// model.setViewName("redirect:/web/supplyinfo/");
				// model.setViewName("redirect:/web/manageKitchen/manageKitchen.do");
		
			}
		} catch (RuntimeException re) {
			//System.out.println("find by property name failed", re.me);
			System.out.println("find by property name failed"+ re.getMessage());
			throw re;
		}
		dbsession.close();
		
		return model;
		//----------------------------------------------------------
		//if(UseraccountDAO.checkLogin(account, md5pwd)==1)
		/*if(true)
		{
			//List<Useraccount> loginList = null;
			
			
			if(login_type.equals("kitchen")){
				useraccount
				license="1"; //Ric如果登入是團膳業者kitchenid暫訂1
				session.setAttribute("account", license);
				model.setViewName("redirect:/web/manageKitchen/manageKitchen.do");
				//return model;
			}else if(login_type.equals("govern")){
				license="2";//Ric如果登入是政府機關kitchenid暫訂2
				session.setAttribute("account", license);
				model.setViewName("redirect:/web/supplyinfo/");
				//return model;
			}
			return model;
		}else{
			response.getWriter().print("<script>alert('登入失敗');history.go(-1)</script>");
			return null;
		}*/
	}

	@RequestMapping(value = "/main/changePass/",method = RequestMethod.POST)
	public ModelAndView changePass(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		ModelAndView model = new ModelAndView();
		response.setContentType("text/html;charset=UTF-8"); 
		HttpSession session = request.getSession();	
		
		//String account = request.getParameter("usename").trim();
		
		String kitchenId = session.getAttribute("account").toString();
		String password = request.getParameter("old").trim();
		String newpassword = request.getParameter("newpass").trim();
		String newpasswordcheck = request.getParameter("newCheck").trim();
		if(kitchenId.isEmpty())
		{
			response.getWriter().print("<script>alert('尚未登入');window.location.href='"+request.getContextPath()+"/web/main/'</script>");
			return null;
		}
		if(password.isEmpty()||newpassword.isEmpty()||newpasswordcheck.isEmpty())
		{
			response.getWriter().print("<script>alert('密碼為空值');history.go(-1)</script>");
			return null;
		}if(!newpassword.equals(newpasswordcheck))
		{
			response.getWriter().print("<script>alert('新密碼輸入不同');history.go(-1)</script>");
			return null;
		}
		Common common =new Common();
		String md5pwd = common.getEncoderByMd5(password);
		String md5npwd = common.getEncoderByMd5(newpassword);
		/* if(account.isEmpty())
		{
			response.getWriter().print("<script>alert('帳號為空值');history.go(-1)</script>");
			return null;
		} 
		if(password.isEmpty())
		{
			response.getWriter().print("<script>alert('密碼為空值');history.go(-1)</script>");
			return null;
		}*/
		//----------------------------------------------------------
		
		SessionFactory sessionfactory = HibernateUtil.buildSessionFactory();
		Session dbsession = sessionfactory.openSession();
		try {
			Criteria criteria = dbsession.createCriteria(Useraccount.class);
			criteria.add( Restrictions.eq("kitchenId", Integer.parseInt(kitchenId)) );
			criteria.add( Restrictions.eq("password", md5pwd) );
			List<Useraccount> queryObject = criteria.list();
			if(queryObject.size()==0){
				response.getWriter().print("<script>alert('密碼錯誤，請重新輸入！');history.go(-1)</script>");
			return null;
			}else if(queryObject.size()!=0) { 
				Transaction tx = dbsession.beginTransaction();
				try{
					Useraccount newUserAccount = queryObject.get(0);
					//dbsession.evict(newUserAccount);
					newUserAccount.setPassword(md5npwd);
					dbsession.update(newUserAccount);
					tx.commit();
				}catch(Exception e){
					if(!tx.wasRolledBack()){
						tx.rollback();
					}
				}
				response.getWriter().print("<script>alert('修改成功，請重新登入！');window.location.href='"+request.getContextPath()+"/web/main/'</script>");
				session.invalidate();	
				return null;
				//model.setViewName("redirect:/web/main/");
				//return null;
			}else{model.setViewName("redirect:/web/main/");}
			
		} catch (RuntimeException re) {
			//System.out.println("find by property name failed", re.me);
			System.out.println("find by property name failed"+ re.getMessage());
			throw re;
		}
		dbsession.close();
	
		return model;
	}

	
	private  ModelAndView logout(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		HttpSession session = request.getSession();
		session.invalidate();	
		response.setContentType("text/html;charset=UTF-8"); 
		response.getWriter().print("<script>alert('已登出');window.location.href='http://" + request.getServerName() + ":" + request.getLocalPort() + "'</script>");
		return null;
	}

	//Raymond 改用新版本
//	@RequestMapping(value = "/listSupplier/",method = RequestMethod.POST)
//	public ModelAndView listSupplier(HttpServletRequest request,HttpServletResponse response) throws Exception{
//		String action = request.getParameter("action").trim();
//		if ("createSupplier".equals(action)){
//			return SupplierController.createSupplier(request, response);
//		}
//		if ("delSupplier".equals(action)){
//			return SupplierController.delSupplier(request, response);
//		}
//		if ("editSupplier".equals(action)){
//			return SupplierController.editSupplier(request, response);
//		}
//		
//		if ("addSupplier".equals(action)){
//			return SupplierController.addSupplier(request, response);
//		}
//		
//		if ("searchForm".equals(action)){
//			return SupplierController.searchForm(request, response);
//		}
//		return new ModelAndView("");
//	}
	
	@RequestMapping(value = "/manageDish/",method = RequestMethod.POST)
	public ModelAndView manageDish(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String action = request.getParameter("action").trim();
		if ("dishContent".equals(action)){
			return DishController.dishContent(request, response);
		}
		if ("editDish".equals(action)){
			return DishController.editDish(request, response);
		}
		if ("searchForm".equals(action)){
			return DishController.searchForm(request, response);
		}
		if ("addDish".equals(action)){
			return DishController.addDish(request, response);
		}
		if ("delDish".equals(action)){
			return DishController.delDish(request, response);
		}
		if ("createDish".equals(action)){
			return DishController.createDish(request, response);
		}
		
		return new ModelAndView("");
	}
	/* 20140528 Ric版本更新 
	@RequestMapping(value = "/manageKitchen/",method = RequestMethod.POST)
	public ModelAndView manageKitchen(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//20140424 Raymond 檢核
		String action = "";
		if (request.getParameter("action") != null)
			action = request.getParameter("action").trim();

		switch (action) {
		case "kitchenContent":
			return KitchenController.kitchenContent(request, response);
		case "updateKitchen":
			return KitchenController.updateKitchen(request, response);
		default:
			return KitchenController.kitchenContent(request, response);
		}
	}
	*/
	@RequestMapping(value = "/mcontents/", method ={ RequestMethod.POST })  //Ric  
	public ModelAndView mcontents() {
		ModelAndView model = new ModelAndView("mcontents");
		return model;
	}	
	
}
