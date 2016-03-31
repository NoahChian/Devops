package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;
import java.text.ParseException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.iii.ideas.catering_service.util.HibernateUtil;

public abstract class AbstractApiInterface <T1, T2>  implements Serializable {
	protected Logger log = Logger.getLogger(this.getClass());
	protected T1 requestObj;
	protected T2 responseObj;
	protected SessionFactory sessionFactory=null;
	protected Session dbSession=null;
	private String username;
	private String userType;
	private Integer kitchenId;
	private Integer county;
	private String sessionId;
	private String methodName;
	private String remoteIp;

	public void setAuth(String user,String type,Integer id,Integer county){
		this.setUsername(user);
		this.setUserType(type);
		this.setKitchenId(id);
		this.setCounty(county);
	}
	
	public void setRequest(Object req,Class<T1> clazz1,Class<T2> clazz2,String _methodName,String _sessionId,String _remoteIp) throws InstantiationException, IllegalAccessException, Exception {
		ObjectMapper mapper = new ObjectMapper();
		this.requestObj = clazz1.newInstance();
		this.requestObj = mapper.convertValue(req, clazz1);
		this.responseObj = clazz2.newInstance();
		this.methodName = _methodName;
		this.sessionId = _sessionId;
		this.remoteIp = _remoteIp;
		
		//如果建立過sessionFactory 就不再建立
		if(this.sessionFactory==null){
			this.sessionFactory = HibernateUtil.buildSessionFactory();
		}
		dbSession = sessionFactory.openSession();		
	}
	
	public boolean isLogin(){
		if (this.getUsername() == null) {
			return false;
		}
		return true;
	}
	//close connection after finish api
	public void closeSession(){
		if(this.dbSession!=null){
			this.dbSession.close();
			this.dbSession=null;
			this.log.debug("session:"+this.getSessionId()+" api:"+this.methodName+": close session!");
		}
	}
	
	public Object  getResponse() {		
		return responseObj;
	}
	public void  setResponse(Object obj) {		
		this.responseObj=(T2) obj;
	}
	
	public abstract void process() throws NamingException, ParseException;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getKitchenId() {
		return kitchenId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	public Integer getCounty() {
		return county;
	}

	public void setCounty(Integer county) {
		this.county = county;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	



}
