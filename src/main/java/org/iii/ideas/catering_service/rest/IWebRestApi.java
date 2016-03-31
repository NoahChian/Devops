package org.iii.ideas.catering_service.rest;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iii.ideas.catering_service.rest.object.GenericWebRestRequest;
import org.iii.ideas.catering_service.rest.object.GenericWebRestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public interface IWebRestApi {

	@RequestMapping(value = "/API/", method = RequestMethod.POST,produces = "application/json")  
	@ResponseBody
	public GenericWebRestResponse restAPI(HttpServletRequest request, HttpServletResponse response,
			@RequestBody GenericWebRestRequest webRestReq);
	
	
	@RequestMapping(value = "/APIXML/", method = RequestMethod.POST,produces = "application/json")  
	@ResponseBody
	public GenericWebRestResponse restAPIXml(HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String webRestReq);
	
	
	@RequestMapping(value = "/API/XLS/{file_name}", method = RequestMethod.GET)
	public void doDownloadExcel(@PathVariable("file_name") String fileName,HttpServletRequest request,
            HttpServletResponse response) throws IOException, ParseException;
	
	@RequestMapping(value = "/FILE/XLS/", method = RequestMethod.POST)
	public GenericWebRestResponse doDownloadExcelByPost(HttpServletRequest request,
            HttpServletResponse response,@RequestBody GenericWebRestRequest webRestReq) throws IOException, ParseException;

}