package org.iii.ideas.catering_service.rest.api;

import java.util.ArrayList;
import java.util.List;

public class QuerySchoolResponse {
	private int resStatus=1;
	private String msg="test";
	
	
	private List <SchoolObject>school = new ArrayList<SchoolObject>();
	
	public List <SchoolObject > getSchool() {
		return school;
	}
	public void setSchool(List <SchoolObject > school) {
		this.school = school;
	}
	
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
