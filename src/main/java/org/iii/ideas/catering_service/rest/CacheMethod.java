package org.iii.ideas.catering_service.rest;

public class CacheMethod {
	private int type;
	private int sec;
	public CacheMethod(int _type,int _sec){
		this.setType(_type);
		this.setSec(_sec);
	}
	int getType() {
		return type;
	}
	void setType(int type) {
		this.type = type;
	}
	int getSec() {
		return sec;
	}
	void setSec(int sec) {
		this.sec = sec;
	}
}
