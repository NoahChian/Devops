package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

public class SupplierAndMenuDateObject  implements Serializable {
	private String title;
	private String start;
	private  String mid;
	private String tip;
	private int kitchenId;
	private int menuType;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public int getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(int kitchenId) {
		this.kitchenId = kitchenId;
	}
	public int getMenuType() {
		return menuType;
	}
	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

}
