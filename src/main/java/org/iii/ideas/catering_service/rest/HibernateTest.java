package org.iii.ideas.catering_service.rest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(method=RequestMethod.GET, value="/owners/{ownerId}")
public class HibernateTest {
	private Logger log = Logger.getLogger(this.getClass());
	
	private static final Logger logger = Logger.getLogger(HibernateTest.class);
	
	public HibernateTest(){
		
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/pets/{petId}")
	public ModelAndView findPet(@PathVariable String ownerId, @PathVariable String petId, Model model) throws Exception {
		return null;
	}
}
