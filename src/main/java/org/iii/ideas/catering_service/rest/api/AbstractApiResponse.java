package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

import javax.naming.NamingException;

import org.codehaus.jackson.map.ObjectMapper;

public class AbstractApiResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3588262881386969602L;
	private int resStatus=0;
	private String msg="";
	public int getResStatus() {
		return resStatus;
	}
	public void setResStatus(int resStatus) {
		this.resStatus = resStatus;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
