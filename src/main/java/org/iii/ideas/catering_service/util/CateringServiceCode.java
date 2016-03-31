package org.iii.ideas.catering_service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CateringServiceCode {
	public static final String DISHTYPE_MAINFOOD = "MainFoodId";
	public static final String DISHTYPE_MAINFOOD1 = "MainFood1Id";
	public static final String DISHTYPE_MAINDISH = "MainDishId";
	public static final String DISHTYPE_MAINDISH1 = "MainDish1Id";
	public static final String DISHTYPE_MAINDISH2 = "MainDish2Id";
	public static final String DISHTYPE_MAINDISH3 = "MainDish3Id";
	public static final String DISHTYPE_SUBDISH1 = "SubDish1Id";
	public static final String DISHTYPE_SUBDISH2 = "SubDish2Id";
	public static final String DISHTYPE_SUBDISH3 = "SubDish3Id";
	public static final String DISHTYPE_SUBDISH4 = "SubDish4Id";
	public static final String DISHTYPE_SUBDISH5 = "SubDish5Id";
	public static final String DISHTYPE_SUBDISH6 = "SubDish6Id";
	public static final String DISHTYPE_VEGETABLE = "VegetableId";
	public static final String DISHTYPE_SEASONING = "Seasoning";
	public static final String DISHTYPE_SOUP = "SoupId";
	public static final String DISHTYPE_DESSERT = "DessertId";
	public static final String DISHTYPE_DESSERT1 = "Dessert1Id";
	public static final String CODETYPE_DISHTYPE="DishType";
	public static final String CODETYPE_DISHTYPE2="DishType2";
	public static final String USERTYPE_KITCHEN="005";
	public static final String USERTYPE_SCHOOL="006";
	public static final String USERTYPE_SCHOOL_NO_KITCHEN="007";
	public static final String USERTYPE_SCHOOL_SHOP="009";
	
	public static final List<String> USERTYPE_COLLEGE_SCHOOL= new ArrayList<String>(
		    Arrays.asList("101", "102","103"));
	

	public static final String USERTYPE_GOV_CENTRAL="11";
	public static final String CODETYPE_SEASONING="調味料";
	
	public static int System_States = 1;
	
	public static final String FileTypeOfMenuExcel = "MENU";
	public static final String FileTypeOfIngredientExcel = "INGREDIENT";
	public static final String FileTypeOfDishExcel = "VEGETABLE";
	public static final String FileTypeOfSupplierExcel = "SUPPLIER";
	public static final String FileTypeOfDishIdImage = "DISHID";
	public static final String FileTypeOfDishImage = "DISH";
	public static final String FileTypeOfKitchenImage = "KITCHEN";
	public static final String FileTypeOfInspection = "INSPECT";
	public static final String FileTypeOfInspectionV2 = "INSPECT_V2";
	public static final String FileTypeOfNews = "NEWS";
	public static final String FileTypeOfSchoolKitchenAccountExcel = "SCHOOLKITCHENACCOUNT";
	
	public static final String AUTHEN_SUPER_COUNTY = "9999";
	public static final Integer AUTHEN_SUPER_COUNTY_INT=9999;
	public static final String AUTHEN_SCHOOL = "777";
	public static final Integer COUNTY_NUM = 40; //county 目前最大值是 40花蓮縣,由於怕跑到555 666 之類額外定義的 COUNTY_ID,所以加上此判斷
	
	public static final String AUTHEN_NO_COUNTY = "0";
	public static final String AUTHEN_SUPER_COUNTY_TYPE = "admin";
	
	public static final String QUERY_PARAMETER_ZERO = "0";
	public static final String QUERY_PARAMETER_NO_ENTER = "-1";
	
	public static final String AUTHEN_SUPER_KITCHEN = "0"; //不限制查詢kitchenId    ,KC
	
//	public static final List<String> EXCEL_HEADER_SUPPLIER=new ArrayList<String>(
//		    Arrays.asList("供應商名稱", "負責人", "公司統編","地址","電話","認證標章"));
	
	public static final List<String> EXCEL_HEADER_SUPPLIER=new ArrayList<String>(
		    Arrays.asList("供應商名稱", "負責人", "公司統編","地址","電話"));
	
	public static final List<String> EXCEL_HEADER_SUPPLIER_V2=new ArrayList<String>(
		    Arrays.asList("供應商統編*", "供應商名稱*", "聯絡人姓名*", "聯絡電話*", "縣市*", "鄉鎮市區*", "地址*", "備註"));
	
	public static final List<String> EXCEL_HEADER_DISH=new ArrayList<String>(
		    Arrays.asList("菜色名稱", "食材名稱", "供應商名稱", "產品名稱", "製造商名稱"));
	
	public static final List<String> EXCEL_HEADER_MENU=new ArrayList<String>(
		    Arrays.asList("學校", "日期", "主食一","主食二","主菜","主菜一","主菜二","主菜三",
		    		"副菜一","副菜二","副菜三","副菜四","副菜五","副菜六","蔬菜","湯品","附餐一","附餐二"
		    		,"全榖根莖","豆魚肉蛋","蔬菜","油脂與堅果種子","水果","乳品","熱量"));
	
	public static final List<String> EXCEL_HEADER_INGREDIENT=new ArrayList<String>(
		    Arrays.asList("供餐日期", "學校", "菜色名稱","食材名稱","進貨日期","生產日期","有效日期"
		    		,"批號","品牌(製造商)","供應商統編","食材認證標章","認證號碼")); 
	
	//20140724 新版食材EXCEL上傳表頭,新增欄位 "產品名稱","重量(公斤)","基改玉米","基改黃豆","加工品" 修改欄位名稱 "品牌(製造商)" > "製造商"
	public static final List<String> EXCEL_HEADER_INGREDIENT_V2=new ArrayList<String>(
		    Arrays.asList("供餐日期", "學校", "菜色名稱","食材名稱","進貨日期","生產日期","有效日期"
		    		,"批號","製造商","供應商統編","食材認證標章","認證號碼","產品名稱","重量(公斤)","基改玉米","基改黃豆","加工品"));
	
	//20141126 新版食材EXCEL上傳表頭,新增欄位 "供應商名稱" 設置為下拉式選單
		public static final List<String> EXCEL_HEADER_INGREDIENT_V3=new ArrayList<String>(
			    Arrays.asList("供餐日期", "學校", "菜色名稱","食材名稱","進貨日期","生產日期","有效日期"
			    		,"批號","製造商","供應商名稱","食材認證標章","認證號碼","產品名稱","重量(公斤)","基改玉米","基改黃豆","加工品"));
	//20150826  認證標章 >> 驗證標章
			public static final List<String> EXCEL_HEADER_INGREDIENT_V4=new ArrayList<String>(
				    Arrays.asList("供餐日期", "學校", "菜色名稱","食材名稱","進貨日期","生產日期","有效日期"
				    		,"批號","製造商","供應商名稱","食材驗證標章","驗證號碼","產品名稱","重量(公斤)","基改玉米","基改黃豆","加工品"));
			
	public static final List<String> EXCEL_HEADER_SCHKITCHENACCOUNT=new ArrayList<String>(
		    Arrays.asList("縣市別","市區別","學校名稱","學校編號","密碼","Email","帳號類型","帳號狀態")); 
	public static final int CACHE_DISABLE = 0;
	public static final int CACHE_ENABLE = 0x001;
	public static final int CACHE_TEST = -1;
	//不用login 自動cache
//	public static final int CACHE_WITHOUT_AUTH 		= 0b00000001;
	//必須要login 才cache
	public static final int CACHE_WITH_AUTH 		= 0b00000010;
	//只有在不login 時才cache
	public static final int CACHE_ONLY_WITHOUT_AUTH = 0b00000100;
	//只有在不login 時才cache
	public static final int CACHE_ALL_TIME 			= 0b00001000;
	
	//學校代碼系列  SCHOOL_TYPE_
	public static final int SCHOOL_TYPE_NURSERY		=0b00000001;	//幼兒
	public static final int SCHOOL_TYPE_ELEMENTARY	=0b00000010;	//小學
	public static final int SCHOOL_TYPE_JOUNIOR		=0b00000100;	//中學
	public static final int SCHOOL_TYPE_SENIOR		=0b00001000;	//高中
	public static final int SCHOOL_TYPE_COLLEAGE	=0b00010000;	//大學
	public static final int SCHOOL_TYPE_NATIONAL	=0b00100000;	//國立
	public static final int SCHOOL_TYPE_PRIVATE		=0b01000000;	//私立
	public static final int SCHOOL_TYPE_CITY		=0b10000000;	//市立 *不屬於國立與私立
	public static final String DEFAULT_IMAGE_RESIZE_EXTENSION = "jpg";
	
	//食材屬性系咧 INGREDIENT_ATTR
	public static final int INGREDIENT_ATTR_GMBEAM	=0b00000001;	//基改黃豆
	public static final int INGREDIENT_ATTR_GMCORN	=0b00000010;	//基改玉米
	public static final int INGREDIENT_ATTR_PSFOOD	=0b00000100;	//加工食品
	
	//外部使用API沒有權限的情況，可使用之IP白名單
	public static final List<String> WHITE_IP=new ArrayList<String>(
		    Arrays.asList("127.0.0.1","localhost","219.87.151.2")); 
	//改寫於property
	/*public static List<String> white_IP() {
		List<String> white_IP_List = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;
		try {
			 
			input = new FileInputStream("CateringServiceCode.properties");
	 
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			white_IP_List = (Arrays.asList(prop.getProperty("white_IP")));
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return white_IP_List;
	}*/
}