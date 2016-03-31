package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

//queryMissingCaseV2 日期 學校 菜單 mType kitchenName 缺漏資料 廚房聯絡 chu----
public class nullBatchdataList {
	private String menuDate;
	private String shcoolName;
	private String offerState;
	private String mType;
	private String kitchenName;
	private String nullState;
	//private String nullIng;
	//private String nullIngDL;
	//private String nullSeas;
	//private String nullPic;
	private String kitchenContact;
	
	public String getMenuDate() {
		return menuDate;
	}

	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}

	public String getOfferState() {
		return offerState;
	}

	public void setOfferState(String offerState) {
		this.offerState = offerState;
	}

	public String getShcoolName() {
		return shcoolName;
	}

	public void setShcoolName(String shcoolName) {
		this.shcoolName = shcoolName;
	}

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
/*
	public String getNullIng() {
		return nullIng;
	}

	public void setNullIng(String nullIng) {
		this.nullIng = nullIng;
	}

	public String getNullIngDL() {
		return nullIngDL;
	}

	public void setNullIngDL(String nullIngDL) {
		this.nullIngDL = nullIngDL;
	}

	public String getNullSeas() {
		return nullSeas;
	}

	public void setNullSeas(String nullSeas) {
		this.nullSeas = nullSeas;
	}

	public String getNullPic() {
		return nullPic;
	}

	public void setNullPic(String nullPic) {
		this.nullPic = nullPic;
	}
*/
	public String getKitchenContact() {
		return kitchenContact;
	}

	public void setKitchenContact(String kitchenContact) {
		this.kitchenContact = kitchenContact;
	}

	public String getNullState() {
		return nullState;
	}

	public void setNullState(String nullState) {
		this.nullState = nullState;
	}

	public String getmType() {
		return mType;
	}

	public void setmType(String mType) {
		this.mType = mType;
	}

}
