package org.iii.ideas.catering_service.rest.api;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
public class RestRequestFactory  { 
	
	/**
	 * Api Interface的路徑
	 */
	private static final String ABSTRACTAPIINTERFACE_PACKAGE_NAME = "org.iii.ideas.catering_service.rest.api";
	
	/**
	 * Pojo物件的路徑
	 */
	private static final String ABSTRACTAPIINTERFACE_POJO_PACKAGE_NAME = "org.iii.ideas.catering_service.rest.api";
	
	/**
	 * Request Pojo的名字
	 */
	private static final String REQUEST_POJO_NAME_PATTERN = "%sRequest";
	
	/**
	 * Response Pojo的名字
	 */
	private static final String RESPONSE_POJO_NAME_PATTERN = "%sResponse";
	
	AbstractApiInterface restObj;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	public AbstractApiInterface getRequestProcess(String requestName, Object args, String _sessionId ,String _remoteIp) throws Exception{
		
		log.debug("RestRequestFactory:"+requestName);
		
		//取得實做ApiInterface的Class名稱
		String clazzName = firstLetterUpper(requestName);
		
		//取得Request Object的Class名稱
		String reqPojoName = String.format(REQUEST_POJO_NAME_PATTERN, clazzName);
		
		//取得Response Object的Class名稱
		String respPojoName = String.format(RESPONSE_POJO_NAME_PATTERN, clazzName);
		
		
		Class requestPojo = null;
		Class responsePojo = null;
		
		try {
			restObj = (AbstractApiInterface)Class.forName(ABSTRACTAPIINTERFACE_PACKAGE_NAME.concat(".").concat(clazzName)).newInstance();
			requestPojo = Class.forName(ABSTRACTAPIINTERFACE_POJO_PACKAGE_NAME.concat(".").concat(reqPojoName));
			responsePojo = Class.forName(ABSTRACTAPIINTERFACE_POJO_PACKAGE_NAME.concat(".").concat(respPojoName));
			restObj.setRequest(args, requestPojo, responsePojo,requestName,_sessionId,_remoteIp);

		} catch (ClassNotFoundException ex) {
			throw new Exception("session:"+_sessionId+" Wrong API:"+requestName+" request:"+reqPojoName+ " response:"+respPojoName);
		}
			
		return restObj;
	}
	

	
	private static String firstLetterUpper(String string) {
		String response = StringUtils.EMPTY;
		response = string.substring(0, 1).toUpperCase() + string.substring(1);
		return response;
	}
}
