package org.iii.ideas.catering_service.ws.schemav2;

public final  class StatusCode {
	public static final String WS_MSG_SUCCESS="0";
	public static final String WS_MSG_AUTH_ERROR="001";
	public static final String WS_MSG_DATA_FORMAT_ERROR="002";
	public static final String WS_MSG_MESSAGEID_DUPLICATE="003";
	public static final String WS_MSG_SERVER_TIMEOUT="004";
	public static final String WS_MSG_WRONG_ACTION="005";
	
	public static final String WS_MSG_AUTH_NO_KITCHEN="200";
	public static final String WS_MSG_NO_SCHOOL="210";
	public static final String WS_MSG_MENU_NO_EXIST="220";
	public static final String WS_MSG_MENU_DUPLICATE="221";
	public static final String WS_MSG_DISH_NO_EXIST="230";
	public static final String WS_MSG_DISH_DUPLICATE="231";
	public static final String WS_MSG_SUPPLIER_NO_EXIST="240";
	public static final String WS_MSG_SUPPLIER_DUPLICATE="241";
	public static final String WS_MSG_INGREDIENT_ERROR="250";
	public static final String WS_MSG_ATTACHMENT_ERROR="260";
	public static final String WS_MSG_REQUIRED_FIELDS_ERROR="270";
	public static final String WS_MSG_OTHER="290";
	public static final String WS_LOG_TYPE="GTP_MSG";
	//2015 新增錯誤代碼
	public static final String WS_MSG_NO_MENUTYPE="222";

}
