package org.iii.ideas.catering_service.rest;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.iii.ideas.catering_service.dao.AreaDAO;
import org.iii.ideas.catering_service.rest.api.AbstractApiInterface;
import org.iii.ideas.catering_service.rest.api.AbstractApiResponse;
import org.iii.ideas.catering_service.rest.api.RestRequestFactory;
import org.iii.ideas.catering_service.rest.object.GenericWebRestRequest;
import org.iii.ideas.catering_service.rest.object.GenericWebRestResponse;
import org.iii.ideas.catering_service.service.CateringServiceLoader;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.UUIDGenerator;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;



@Component
public class WebRestApiImpl implements IWebRestApi {	
	private Logger log = Logger.getLogger(this.getClass());
	private MemcachedClient cacheClient =null;
	private AreaDAO areaDAO;
	//private AbstractApiInterface requestProcess;
	public static HashMap<String,CacheMethod> cacheMethodList = new HashMap<String, CacheMethod>();
	/*
    private void runCacheProcess(String args,CacheMethod cacheMethod) throws NamingException, ParseException{
    	Object ret = cacheClient.get(args);
		//確認是否有cache 資料
		if(ret == null){
			this.requestProcess.process();
			//設定cache 值到memcached 中
			AbstractApiResponse retObj = (AbstractApiResponse) this.requestProcess.getResponse();
			cacheClient.set(args, cacheMethod.getSec() , retObj);
		}else{
			//如果有cache 值,就直接用cache 值回應
			this.requestProcess.setResponse(ret);
			log.info("save cache arg:"+args);
		}
    }
    */
	
	@Override
	@ResponseBody
	public GenericWebRestResponse restAPI(HttpServletRequest request, HttpServletResponse response,
			@RequestBody GenericWebRestRequest webRestReq) {

		GenericWebRestResponse wrr = new GenericWebRestResponse();
		try {
			String uuid = UUIDGenerator.getUUID();
			HttpSession httpsession = request.getSession();
			RestRequestFactory factory = new RestRequestFactory();
			//取得api 的method 及參數
//			AbstractApiInterface requestProcess = factory.getRequestProcess(webRestReq.getMethod(),
//					webRestReq.getArgs(),uuid);
			
			//20140609 Raymond 多傳入Remote IP 參數
			AbstractApiInterface requestProcess = factory.getRequestProcess(webRestReq.getMethod(),
					webRestReq.getArgs(),uuid,CateringServiceUtil.getClientAddr(request));
			
			//this.requestProcess.setSessionId(httpsession.getId());
			String username = null;
			String usertype = null;
			Integer kitchenId = null;
			Integer county = null;
			
			boolean userLogin = false;
			//取得使用者的帳號
			if (httpsession.getAttribute("uName") != null) {
				username = (String) httpsession.getAttribute("uName");
				usertype = (String) httpsession.getAttribute("uType");
				kitchenId = Integer.valueOf((String) httpsession.getAttribute("account"));
				county = Integer.valueOf((String) httpsession.getAttribute("county"));
				userLogin=true;
			}
			log.info("session:"+uuid+" 使用者:"+username + " type:"+usertype+" method:"+webRestReq.getMethod());
			
			//logging to file  20140225   KC
			long t_before=(new Date()).getTime();

			String postArgs=new Gson().toJson(webRestReq);
			CateringServiceUtil.weblogToFile(webRestReq.getMethod(), username, request,"session:"+uuid+ "web entry success; "+postArgs);

			requestProcess.setAuth(username, usertype, kitchenId,county);
			
			//-----------------新增cache 功能
			//測試是否有enable cache server
			if( CateringServiceLoader.getCacheEnable() == CateringServiceCode.CACHE_ENABLE ){
				if(cacheMethodList.get(webRestReq.getMethod())==null){
					//如果不在CACHE 清單內就直接進系統查詢
					requestProcess.process();
				}else{
					MemcachedClient cacheClient = CateringServiceLoader.getCacheClient();
					CacheMethod cacheMethod = cacheMethodList.get(webRestReq.getMethod());

					String args = webRestReq.getMethod()+webRestReq.getArgs().toString();

					//userLogin 為使用者是否有登入,  type 為cache 種類
					if(userLogin==false && cacheMethod.getType() == CateringServiceCode.CACHE_ONLY_WITHOUT_AUTH){
						Object ret = cacheClient.get(args);
						//確認是否有cache 資料
						if(ret == null){
							requestProcess.process();
							//設定cache 值到memcached 中
							AbstractApiResponse retObj = (AbstractApiResponse) requestProcess.getResponse();
							cacheClient.set(args, cacheMethod.getSec() , retObj);
						}else{
							//如果有cache 值,就直接用cache 值回應
							requestProcess.setResponse(ret);
							log.info("save cache arg:"+args);
						}
						//只有login的api資料才會cache
					}else if(userLogin==true && cacheMethod.getType() == CateringServiceCode.CACHE_WITH_AUTH){
						Object ret = cacheClient.get(args);
						//確認是否有cache 資料
						if(ret == null){
							requestProcess.process();
							//設定cache 值到memcached 中
							AbstractApiResponse retObj = (AbstractApiResponse) requestProcess.getResponse();
							cacheClient.set(args, cacheMethod.getSec() , retObj);
						}else{
							//如果有cache 值,就直接用cache 值回應
							requestProcess.setResponse(ret);
							log.info("save cache arg:"+args);
						}
						//所有api資料都會cache
					}else if(cacheMethod.getType() == CateringServiceCode.CACHE_ALL_TIME){
						Object ret = cacheClient.get(args);
						//確認是否有cache 資料
						if(ret == null){
							requestProcess.process();
							//設定cache 值到memcached 中
							AbstractApiResponse retObj = (AbstractApiResponse) requestProcess.getResponse();
							cacheClient.set(args, cacheMethod.getSec() , retObj);
						}else{
							//如果有cache 值,就直接用cache 值回應
							requestProcess.setResponse(ret);
							log.info("save cache arg:"+args);
						}
					}else{
						requestProcess.process();
					}
				}
			//	this.cacheClient.shutdown();
			}else {
				//未enable cache 的流程
				requestProcess.process();				
			}
			
			//logging to file (加process結束點) 20140321   KC
			long t_after=(new Date()).getTime();
			CateringServiceUtil.weblogToFile(webRestReq.getMethod(), username, request,"session:"+uuid+ " web process end,time :"+String.valueOf(t_after-t_before)+" ms");
			
			requestProcess.closeSession();
			wrr.setResult_content(requestProcess.getResponse());
			wrr.setMethod(webRestReq.getMethod());
			wrr.setResourceMethod(request.getPathInfo());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(getStackTrace(e));
			wrr.setError_msg(getStackTrace(e));
			wrr.setResult("0");
		}
		return wrr;
	}
	
	@Override
	@ResponseBody
	public GenericWebRestResponse restAPIXml(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String webRestXmlReq) {
		log.info("get xml format:"+webRestXmlReq);
		JSONObject jsonObj = XML.toJSONObject(webRestXmlReq);
	    String json = jsonObj.toString();
	    log.info("Request from xml to json:"+json);
	    ObjectMapper mapper = new ObjectMapper();
	    
		//GenericWebRestXmlResponse wrr = new GenericWebRestXmlResponse();
		GenericWebRestResponse wrr = new GenericWebRestResponse();
		try {
			GenericWebRestRequest webRestReq = mapper.readValue(json, GenericWebRestRequest.class);//dennis
			HttpSession httpsession = request.getSession();
			RestRequestFactory factory = new RestRequestFactory();
			AbstractApiInterface requestProcess = factory.getRequestProcess(webRestReq.getMethod(),
					webRestReq.getArgs(), httpsession.getId(),request.getRemoteAddr());

			
			String username = null;
			String usertype = null;
			Integer kitchenId = null;
			Integer county = null;
			if (httpsession.getAttribute("uName") != null) {
				username = (String) httpsession.getAttribute("uName");
				usertype = (String) httpsession.getAttribute("uType");
				kitchenId = Integer.valueOf((String) httpsession.getAttribute("account"));
				county = Integer.valueOf((String) httpsession.getAttribute("county"));
			}
			
			requestProcess.setAuth(username, usertype, kitchenId,county);
			requestProcess.process();
			requestProcess.closeSession();
			wrr.setResult_content(requestProcess.getResponse());
			wrr.setMethod(webRestReq.getMethod());
			wrr.setResourceMethod(request.getPathInfo());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(getStackTrace(e));
//			wrr.setError_msg(getStackTrace(e));
			wrr.setError_msg("Wrong API!");
			wrr.setResult("0");
		}
		response.setContentType("application/json");
		return wrr;
	}

	private static final int BUFFER_SIZE = 4096;

	public void doDownloadExcel(@PathVariable("file_name") String fileName, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
		log.debug("Download file File:" + fileName);
		Integer kitchenId = null;

		HttpSession httpsession = request.getSession();

		if (httpsession.getAttribute("uName") != null) {
			kitchenId = Integer.valueOf(httpsession.getAttribute("account").toString());
			//20140416 Ric: 原本==0 會使主管機關無法進行Excel操作。改null
			if (kitchenId==null) {
				log.info("使用者無kitchen Id");
				return;
			}
		}else {
			log.info("使用者未登入");
			return;
		}
//		CreateExcelFile createExcelFile = new CreateExcelFile();
		CreateExcelFiles createExcelFile = new CreateExcelFiles();
		String username = httpsession.getAttribute("uName").toString();
		String usertype=httpsession.getAttribute("uType").toString();
		String downloadpath = createExcelFile.createFile(fileName, kitchenId, username,usertype);

		ServletContext context = request.getServletContext();

		String fullPath = downloadpath;
		File downloadFile = new File(fullPath);
		FileInputStream inputStream = new FileInputStream(downloadFile);

		// get MIME type of the file
		String mimeType = context.getMimeType(fullPath);
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		log.debug("Download File:"+downloadFile.getName()+" MIME type: " + mimeType);
		
		String outputFilename = downloadFile.getName();
		//outputFilename = outputFilename.replace(createExcelFile.getRequestFileType(), createExcelFile.getFilePrefixName());
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", outputFilename);
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();
	}

	public AreaDAO getAreaDAO() {
		return areaDAO;
	}

	public void setAreaDAO(AreaDAO areaDAO) {
		this.areaDAO = areaDAO;
	}

	@Override
	public GenericWebRestResponse doDownloadExcelByPost(HttpServletRequest request, HttpServletResponse response,
			@RequestBody  GenericWebRestRequest webRestReq) throws IOException,ParseException {
		// TODO Auto-generated method stub
		//log.debug("Download file File:" + fileName);
		GenericWebRestResponse wrr = new GenericWebRestResponse();
		//---
		try {
			String uuid = UUIDGenerator.getUUID();
			HttpSession httpsession = request.getSession();
			RestRequestFactory factory = new RestRequestFactory();
			AbstractApiInterface requestProcess = factory.getRequestProcess(webRestReq.getMethod(),
					webRestReq.getArgs(),uuid,CateringServiceUtil.getClientAddr(request));
			String username = null;
			String usertype = null;
			Integer kitchenId = null;
			Integer county = null;
			String excelFilePath="";
			String outputFilename="食材查詢報表"+(new Date()).getTime();

			boolean userLogin = false;
			//取得使用者的帳號
			if (httpsession.getAttribute("uName") != null) {
				username = (String) httpsession.getAttribute("uName");
				usertype = (String) httpsession.getAttribute("uType");
				kitchenId = Integer.valueOf((String) httpsession.getAttribute("account"));
				county = Integer.valueOf((String) httpsession.getAttribute("county"));
				userLogin=true;
			}
			log.info("session:"+uuid+" 使用者:"+username + " type:"+usertype+" method:"+webRestReq.getMethod());
			
			//logging to file  20140225   KC
			long t_before=(new Date()).getTime();

			String postArgs=new Gson().toJson(webRestReq);
			CateringServiceUtil.weblogToFile(webRestReq.getMethod(), username, request,"session:"+uuid+ "web entry success; "+postArgs);
			requestProcess.setAuth(username, usertype, kitchenId,county);
			requestProcess.process();
			
			AbstractApiResponse retObj = (AbstractApiResponse) requestProcess.getResponse();
			excelFilePath=retObj.getMsg();
			ServletContext context = request.getServletContext();
			String mimeType = context.getMimeType(excelFilePath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}			
			response.setContentType(mimeType);
			response.setContentLength((int) excelFilePath.length());

			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", outputFilename);
			response.setHeader(headerKey, headerValue);			
			// get output stream of the response
			
			//File downloadFile = new File(excelFilePath);
			//FileInputStream inputStream = new FileInputStream(excelFilePath);
			
			//OutputStream outStream = response.getOutputStream();
			
			//byte[] buffer = new byte[BUFFER_SIZE];
			//int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			//while ((bytesRead = inputStream.read(buffer)) != -1) {
			//	outStream.write(buffer, 0, bytesRead);
			//}

			//inputStream.close();
			//outStream.close();
			wrr.setResult_content(requestProcess.getResponse());
			wrr.setMethod(webRestReq.getMethod());
			wrr.setResourceMethod(request.getPathInfo());
		}catch(Exception ex){
			ex.printStackTrace();
			log.error(getStackTrace(ex));
			wrr.setError_msg(getStackTrace(ex));
			wrr.setResult("0");
		}
		
		return wrr;
	//---
	}

}


