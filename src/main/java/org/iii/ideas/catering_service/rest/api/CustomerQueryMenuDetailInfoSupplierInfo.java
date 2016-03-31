package org.iii.ideas.catering_service.rest.api;

import java.io.Serializable;

/*
 * "supplierName":"統鮮股份有限公司",
            "supplierAddress":"新北市五股七路100號",
            "supplierPhone":"6670-1234",
            "supplierLeader":"陳中將",
            "dietitians":"王子祥",
            "authenticate":"HACCP"

 */
public class CustomerQueryMenuDetailInfoSupplierInfo  implements Serializable {
	private Integer supplierID;//test
	private String supplierName;
	private String supplierAddress;
	private String supplierPhone;
	private String supplierLeader;
	private String dietitians;
	private String authenticate;
	private String kitchenType;
	private String fdaCompanyName;
	private String fdaCompanyBusinessId;
	private String fdaCompanyAddress;
	private String fdaCompanyId;
	private String fdaCompanyRegType;
	
	public String getFdaCompanyName() {
		return fdaCompanyName;
	}
	public void setFdaCompanyName(String fdaCompanyName) {
		this.fdaCompanyName = fdaCompanyName;
	}
	public String getFdaCompanyBusinessId() {
		return fdaCompanyBusinessId;
	}
	public void setFdaCompanyBusinessId(String fdaCompanyBusinessId) {
		this.fdaCompanyBusinessId = fdaCompanyBusinessId;
	}
	public String getFdaCompanyAddress() {
		return fdaCompanyAddress;
	}
	public void setFdaCompanyAddress(String fdaCompanyAddress) {
		this.fdaCompanyAddress = fdaCompanyAddress;
	}
	public String getFdaCompanyId() {
		return fdaCompanyId;
	}
	public void setFdaCompanyId(String fdaCompanyId) {
		this.fdaCompanyId = fdaCompanyId;
	}
	public String getFdaCompanyRegType() {
		return fdaCompanyRegType;
	}
	public void setFdaCompanyRegType(String fdaCompanyRegType) {
		this.fdaCompanyRegType = fdaCompanyRegType;
	}
	
	public Integer getSupplierID() {
		return supplierID;
	}
	public void setSupplierID(Integer supplierID) {
		this.supplierID = supplierID;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierAddress() {
		return supplierAddress;
	}
	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}
	public String getSupplierPhone() {
		return supplierPhone;
	}
	public void setSupplierPhone(String supplierPhone) {
		this.supplierPhone = supplierPhone;
	}
	public String getSupplierLeader() {
		return supplierLeader;
	}
	public void setSupplierLeader(String supplierLeader) {
		this.supplierLeader = supplierLeader;
	}
	public String getDietitians() {
		return dietitians;
	}
	public void setDietitians(String dietitians) {
		this.dietitians = dietitians;
	}
	public String getAuthenticate() {
		return authenticate;
	}
	public void setAuthenticate(String authenticate) {
		this.authenticate = authenticate;
	}
	public String getKitchenType() {
		return kitchenType;
	}
	public void setKitchenType(String kitchenType) {
		this.kitchenType = kitchenType;
	}
}
