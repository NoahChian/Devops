package org.iii.ideas.catering_service.rest.api;

public class AddSchKitchenUserResponse extends AbstractApiResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = -476670353079631246L;
	private String schoolName;
	private String userName;
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
