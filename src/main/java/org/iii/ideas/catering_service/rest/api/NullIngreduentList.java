package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;


public class NullIngreduentList  implements Serializable {
	
	/**
	 * 20140612 Ric
	 * 有菜單無食材
	 */
	private static final long serialVersionUID = -8657688286901435488L;
	private String schoolName;
	private String date;
	// #13525 : 缺漏資料新增"餐別"欄位
	private String menuType;
	private String nullIngreduents;
	
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNullIngreduents() {
		return nullIngreduents;
	}
	public void setNullIngreduents(String nullIngreduents) {
		this.nullIngreduents = nullIngreduents;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	
}
