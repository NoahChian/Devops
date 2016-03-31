package org.iii.ideas.catering_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/webSample/")
public class TilesSample {

	@RequestMapping(value = "tiles/", method = RequestMethod.GET)  
	public ModelAndView renderHtml() {
		// TODO Auto-generated method stub		
		System.out.print("renderHtml");
		ModelAndView model = new ModelAndView("admin/sample");
	  	model.addObject("msg", "aaa hello world");
		return model;
	}    
}
