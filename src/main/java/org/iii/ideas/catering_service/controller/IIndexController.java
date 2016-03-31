package org.iii.ideas.catering_service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public interface IIndexController {
	//http://{server}:{port}/billing/web/
	
	@RequestMapping(value ="/{file_name}/", method = RequestMethod.GET)  
	public ModelAndView setAllPage(@PathVariable("file_name") String fileName,HttpServletRequest request,HttpServletResponse response);
	/*
	@RequestMapping(value = "/index/", method = RequestMethod.GET)  
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response);
	
	@RequestMapping(value = "/main/", method = RequestMethod.GET)  
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response);

	@RequestMapping(value = "/ingredients_management/", method = RequestMethod.GET)  
	public ModelAndView ingredients_management();
	@RequestMapping(value = "/ingredients_detail/", method = RequestMethod.GET)  
	public ModelAndView ingredients_detail();
	@RequestMapping(value = "/ingredients_modify/", method = RequestMethod.GET)  
	public ModelAndView ingredients_modify();
	@RequestMapping(value = "/seasoning_modify/", method = RequestMethod.GET)  
	public ModelAndView seasoning_modify();
	@RequestMapping(value = "/singleSch_detail/", method = RequestMethod.GET)  
	public ModelAndView singleSch_detail();
	@RequestMapping(value = "/singleSch_modify/", method = RequestMethod.GET)  
	public ModelAndView singleSch_modify();
	
	@RequestMapping(value = "/supplyinfo/", method = RequestMethod.GET)//Ric  
	public ModelAndView supplyinfo();
	@RequestMapping(value = "/certification/", method = RequestMethod.GET)//Ric  
	public ModelAndView certification();
	@RequestMapping(value = "/abnormal/", method = RequestMethod.GET)//Ric  
	public ModelAndView abnormal();
	@RequestMapping(value = "/govern/", method = RequestMethod.GET)//Ric  
	public ModelAndView govern();
	@RequestMapping(value = "/addgovern/", method = RequestMethod.POST)//Ric  
	public ModelAndView addgovern();
	@RequestMapping(value = "/abnormalsearch/", method = RequestMethod.GET)//Ric  
	public ModelAndView abnormalsearch();
	@RequestMapping(value = "/kitchenIngredient/", method = RequestMethod.GET)//Ric  
	public ModelAndView kitchenIngredient();
	
	@RequestMapping(value = "/school_Menu/", method = RequestMethod.GET)  
	public ModelAndView school_Menu();
	
	@RequestMapping(value = "/counter/", method = RequestMethod.GET)  
	public ModelAndView counter();
	
	@RequestMapping(value = "/singleSch_manage/", method = RequestMethod.GET)  
	public ModelAndView singleSch_manage();
	
	@RequestMapping(value = "/dishImageUpload/", method = RequestMethod.GET)  
	public ModelAndView dishImageUpload();
	
	@RequestMapping(value = "/managelist/", method = { RequestMethod.GET, RequestMethod.POST })  
	public ModelAndView managelist();

	@RequestMapping(value = "/userManage/", method = { RequestMethod.GET, RequestMethod.POST })  
	public ModelAndView userManage();
	@RequestMapping(value = "/kitchenManage/", method = { RequestMethod.GET, RequestMethod.POST })  
	public ModelAndView kitchenManage();
	@RequestMapping(value = "/listKitchen/", method = { RequestMethod.GET, RequestMethod.POST })  
	public ModelAndView listKitchen();
	@RequestMapping(value = "/listUser/", method = { RequestMethod.GET, RequestMethod.POST })  
	public ModelAndView listUser();
	@RequestMapping(value = "/listSchool/", method = { RequestMethod.GET, RequestMethod.POST })  
	public ModelAndView listSchool();

	@RequestMapping(value = "/msearch/", method = RequestMethod.GET)//Ric  
	public ModelAndView msearch();
	@RequestMapping(value = "/mcontents/", method = RequestMethod.GET)//Ric  
	public ModelAndView mcontents();
	@RequestMapping(value = "/news/", method = RequestMethod.GET)//Ric  
	public ModelAndView news();
	
	@RequestMapping(value = "/nullIngredient/", method = RequestMethod.GET)//Raymond
	public ModelAndView nullIngredient();
*/
	
}


