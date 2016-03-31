<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../../js_ca/common.js"></script>
<script type="text/javascript" src="../../js/nodeJsSrv_util.js"></script>
<title></title>
<script>
<%
String Kitchen = new String(request.getParameter("Kitchen"));
String school = new String(request.getParameter("School"));
String period = new String(request.getParameter("CALENDAR"));
%>
var schoolProductData = [];

function offeringService(mid){
	//取得提供那些服務,產生服務下拉,預設以第1項資料去帶出查詢資料
	//http://10.10.20.125:3000/offering/service/64740275
	var sch = '<%=school%>';
	var cal = '<%=period%>';
	var offedServiceUrl = url + "/offering/service?SchoolId=" + sch + "&MenuDate=" + cal;
	var schoolProductUrl = url + "/schoolproductsearch?SchoolId=" + sch + "&Date=" + cal;
	var param = {"SchoolId":sch, "MenuDate":cal};
	
	console.log(offedServiceUrl);
	console.log(schoolProductUrl);
	
	ajaxCallJsonp(offedServiceUrl, param, function(data){
		if(data.result == 1){
			var data = data.data;
			var drop_down_service = "<select name=\"service\" id=\"service\" onChange='kitchen();'  >";
			
			if (data.length == 0) {
				drop_down_service += "<option value=\"0\">本日無供餐資訊</option>";
			} else {
				drop_down_service += "<option value=\"0\">請選擇</option>";
			}
			
			for(var i in data){
				var menuId = data[i].ServiceId;
				var menuBatchDataId = data[i].BatchDataId;
				var menuLabel = data[i].label;
				
				if(mid==menuBatchDataId){
					drop_down_service += "<option selected menuType="+ menuId +" value=" +  menuBatchDataId + ">" + menuLabel + "</option>";
				}
				else{
					drop_down_service += "<option menuType="+ menuId +" value=" +  menuBatchDataId + ">" + menuLabel + "</option>";
				}
			}
			drop_down_service += "</select>";

			//清空
			$("#dropdown_service").html("");

			$("#dropdown_service").append(drop_down_service);
		}else{
			alert('無法取得資料~請稍後再試!');
		}
	},false,"GET");
	
	ajaxCallJsonp(schoolProductUrl, param, function(data){
		if(data.result == 1){
			var data = data.data;
			
			if (data.length > 0) {
				schoolProductData = data;
				if(mid==-1){
					$("#service").append("<option selected menuType=\"5\" value=\"0\">合作社</option>");
				}
				else{
					$("#service").append("<option menuType=\"5\" value=\"0\">合作社</option>");
				}
			}
			/* for(var i in data){
				var menuId = data[i].ServiceId;
				var menuBatchDataId = data[i].BatchDataId;
				var menuLabel = data[i].label;
				
				if(mid==menuBatchDataId){
					drop_down_service += "<option selected menuType="+ menuId +" value=" +  menuBatchDataId + ">" + menuLabel + "</option>";
				}
				else{
					drop_down_service += "<option menuType="+ menuId +" value=" +  menuBatchDataId + ">" + menuLabel + "</option>";
				}
			} */

		}else{
			alert('無法取得資料~請稍後再試!');
		}
	},false,"GET");
} 

function kitchen(mid) {
	var sch = '<%=school%>';
	var cal = '<%=period%>';
	var mtype = $("#service").children("option:selected").attr("menutype");
	
	if(mtype == 5){
		console.log(schoolProductData);
		show_school_product_data();
	}
	else{
		$("#supplySelect").css("display", "block");
		if (cal == "" || cal == null) {
			var today = new Date();
			var dd = today.getDate();
			var mm = today.getMonth() + 1; //January is 0!
			var yyyy = today.getFullYear();
			if (dd < 10) {
				dd = '0' + dd
			}
			if (mm < 10) {
				mm = '0' + mm
			}
			today = yyyy + '/' + mm + '/' + dd;
			cal = today;

		}
		//alert(area);

		var request_data = {
			"method" : "customerQueryKitchenBySchoolAndDate",
			"args" : {
				"sid" : sch,
				"date" : cal,
			    "mtype": mtype
			}
		};
		var response_obj = call_rest_api(request_data);

		//alert(response_obj.toString());

		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;

			//alert(result_content.resStatus);

			if (result_content.resStatus == 1) { //success
				var drop_down_kitchen = "<select name=\"Kitchen\" id=\"Kitchen\" onChange='query_detail_data($(\"#Kitchen\").val())'  >";
				//drop_down_kitchen += "<option value=\"0\">請選擇</option>";
				if (result_content.kitchen.length == 0) {
					drop_down_kitchen += "<option value=\"0\">本日無供餐資訊</option>";
				} else {
					drop_down_kitchen += "<option value=\"0\">請選擇</option>";
				}
				for (var i = 0; i < result_content.kitchen.length; i++) {
					if(mid==result_content.kitchen[i].mid){
						drop_down_kitchen += "<option selected value=" +  result_content.kitchen[i].mid + ">" + result_content.kitchen[i].kitchenName + "</option>";
					}
					else{
						drop_down_kitchen += "<option value=" +  result_content.kitchen[i].mid + ">" + result_content.kitchen[i].kitchenName + "</option>";
					}
				}
				drop_down_kitchen += "</select>";

				//清空
				$("#dropdown_Kitchen").html("");

				$("#dropdown_Kitchen").append(drop_down_kitchen);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else {
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}
}

function show_school_product_data(){
	$("#lunch_content").html("");
	$("#food_info").html("");
	$("#seasoning_click").html("");
	$("#seasoning_click").css("display", "none");
	$("#controller").html("");
	$("#query_detail_click").html("");
	$("#query_detail_click").css("display", "none");
	$("#school_and_date").html("");
	$("#query_supply_click").html("");
	$("#query_supply_click").css("display", "none");
	
	var cal = '<%=period%>';
	var new_Titlte = "<div id='MAIN_SECOND_BAR'  class='tal_ch search_re_tit'>" + schoolProductData[0].SchoolName +" : <br>"
		+ cal + " 員生消費合作社</div><div class='bw_line'></div><div class='condition'><div class='tal_ch condition_data'><div class='condition_data_liner'></div><ul><li class=''>供餐類型：</li><li class='tal_le'><span name='dropdown_service' id='dropdown_service'></li></ul><div id='supplySelect'><div class='condition_data_liner'></div><ul><li class=''>供應商：</li><li class='tal_le'><span name='dropdown_Kitchen' id='dropdown_Kitchen'></li></ul></div></div></div><div class='bw_line'></div>";
	$("#school_and_date").append(new_Titlte);
	$("#supplySelect").css("display", "none");
	
	//控制項(上一頁、前一日、後一日)
	var controller_div = "<div class='h_94px lh_94 tal_ch pf_bbter'><a href='#'  onclick=\"javascript:location.href='../main/'\" >回前頁</a>&nbsp;";
	$("#controller").append(controller_div);
	$("#controller").css("float", "left");
	$("#controller").css("width", "100%");
	
	//合作社內容
	var lunch_content_div = " ";
		for(var k=0; k<schoolProductData.length; k++){
			lunch_content_div +=" ";
			lunch_content_div += "<script> $(document).ready(function() { $('#show_MD-"+k+"').click(function() { show_school_product_detail_data(schoolProductData["+k+"], "+k+", "+schoolProductData.length+") }); }); <\/script>";
			
			lunch_content_div +="<div class='tabs-1 w_80ps seafod_batxt' id='show_MD-"+k+"'><div style='text-align: right; width:50%; float: left;'><span>" + schoolProductData[k].CategoryName + "：</span></div>";
			lunch_content_div +="<div style='text-align: left; width:50%; float: left;'>" + schoolProductData[k].ProductName + "</div></div>";		
		}
	//lunch_content_div +="<div class='h_60px lh_60 tal_ch atch_txt tabs-1'>菜色圖片為示意圖</div>";
	$("#lunch_content").append(lunch_content_div);
	
	offeringService(-1);
}

function show_school_product_detail_data(schoolProductDetailData, currentIndex, dataLen){
	$("#lunch_content").html("");
	$("#food_info").html("");
	$("#seasoning_click").html("");
	$("#controller").html("");
	$("#query_detail_click").html("");
	$("#school_and_date").html("");
	$("#query_supply_click").html("");
	$('html, body').scrollTop(0);
	
	//控制項(上一頁、前一商品、後一商品)
	var preIndex;
	var nextIndex;
	var controller_div = "<div class='h_94px lh_94 tal_ch pf_bbter'><a href='#'  onclick='show_school_product_data()' >回前頁</a>&nbsp;";
	if(currentIndex > 0) {
		preIndex = currentIndex-1;
		controller_div += "<a href='#' onclick='show_school_product_detail_data(schoolProductData["+preIndex+"], "+preIndex+", "+dataLen+")'>前一項</a>&nbsp;";
	}
	if(currentIndex < dataLen-1){
		nextIndex = currentIndex+1;
		controller_div += "<a href='#' onclick='show_school_product_detail_data(schoolProductData["+nextIndex+"], "+nextIndex+", "+dataLen+")'>下一項</a></div><div class='bw_line' ></div>";
	}
	$("#school_and_date").append(controller_div);
	
	//合作社內容
	var lunch_content_div = "";

	lunch_content_div +="<div class='tabs-1 w_80ps tal_ch seafod_batxt' id='show_MD-BaseInfo'><span>"+schoolProductDetailData.ProductName+"：</span></div>";
	lunch_content_div +="<div><div id='detail-Nutrition' class='TAB_TY_A tal_ch'><table class='component TAB_TY_C'  width='100%'>";
	lunch_content_div += "<tr style='border: 1px solid;'>";
	lunch_content_div += "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>認證類別</td>" +
	 "<td align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Certification+"</td>"+
	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>認證編號</td>" +
 	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.CertificationId+"</td>" +
 	 "</tr><tr style='border: 1px solid;'>"+
	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>分類</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.CategoryName+"</td>" +
	 "</tr><tr style='border: 1px solid;'>"+
	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>包裝型態</td>" +
 	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Packages+"</td>" +
 	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>販售方式</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Soldway+"</td>"+
	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>保存方式</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.PreservedMethod+"</td></tr>";
	 lunch_content_div += "</table></div></div>";
	
	lunch_content_div +="<div class='tabs-1 w_80ps tal_ch seafod_batxt' id='show_MD-Nutrition'><span>營養標示：</span></div>";
	lunch_content_div +="<div><div id='detail-Nutrition' class='TAB_TY_A tal_ch'><table class='component TAB_TY_C'  width='100%'>";
	lunch_content_div += "<tr style='border: 1px solid;'>";
	lunch_content_div += "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>每一份量</td>" +
	 "<td align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Weight+"</td>"+
	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>本包裝含</td>" +
 	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Capacity+"</td>" +
 	 "</tr><tr style='border: 1px solid;'>"+
	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>熱量</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Nutrition.Calories+" 大卡</td>" +
	 "</tr><tr style='border: 1px solid;'>"+
	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>蛋白質</td>" +
 	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Nutrition.Protein+" 毫克</td>" +
 	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>脂肪</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Nutrition.Totalfat+" 毫克</td>"+
	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>飽和脂肪</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Nutrition.Satfat+" 毫克</td>"+
	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>反式脂肪</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Nutrition.Transfat+" 毫克</td>"+
	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>碳水化合物</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Nutrition.Carbohydrate+" 毫克</td>"+
	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>糖</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Nutrition.Sugar+" 毫克</td>"+
	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>鈉</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Nutrition.Sodium+" 毫克</td><tr>";
	 lunch_content_div += "</table></div></div>";
	
	lunch_content_div +="<div class='tabs-1 w_80ps tal_ch seafod_batxt' id='show_MD-Manufacturer'><span>生產廠商資料：</span></div>";
	lunch_content_div +="<div><div id='detail-Manufacturer' class='TAB_TY_A tal_ch'><table class='component TAB_TY_C'  width='100%'>";
	lunch_content_div += "<tr style='border: 1px solid;'>";
	lunch_content_div += "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>公司名稱</td>" +
	 "<td align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Manufacturer.Name+"</td>"+
	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>公司地址</td>" +
 	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Manufacturer.Address+"</td>" +
	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>負責人</td>" +
 	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Manufacturer.Owner+"</td>" +
 	 "</tr><tr style='border: 1px solid;'>"+
	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>聯絡電話</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.Manufacturer.Tel+"</td>" +
	 "</tr><tr style='border: 1px solid;'>"+
	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>消費者<br>服務專線</td>" +
 	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.CustomerService.Hotline+"</td>" +
 	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>申訴事件<br>負責人</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.CustomerService.Owner+"</td></tr>";
	 lunch_content_div += "</table></div></div>";

	lunch_content_div +="<div class='tabs-1 w_80ps tal_ch seafod_batxt' id='show_MD-SupplierCompany'><span>供應商資料：</span></div>";
	lunch_content_div +="<div><div id='detail-SupplierCompany' class='TAB_TY_A tal_ch'><table class='component TAB_TY_C'  width='100%'>";
	lunch_content_div += "<tr style='border: 1px solid;'>";
	lunch_content_div += "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>公司名稱</td>" +
	 "<td align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.SupplierCompany.Name+"</td>"+
	 "</tr><tr style='border: 1px solid;'>"+
	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>公司地址</td>" +
 	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.SupplierCompany.Address+"</td>" +
 	 "</tr><tr style='border: 1px solid;'>"+
 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>負責人</td>" +
 	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.SupplierCompany.Owner+"</td>" +
 	 "</tr><tr style='border: 1px solid;'>"+
	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>聯絡電話</td>" +
	 "<td  align='center' valign='middle' bgcolor='#ffffff'>"+schoolProductDetailData.SupplierCompany.Tel+"</td>" +
	 "</tr>";
	 lunch_content_div += "</table></div></div>";
	 
	$("#lunch_content").append(lunch_content_div);
}

function query_detail_data(mid){
	var request_data =	{
			 "method":"customerQueryMenuDetailInfo",
 				"args":{
 					"mid" :mid
 				}
			};
	var response_obj = call_rest_api(request_data);
	
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		
		if(result_content.resStatus == 1){	//success
			/*
			if(check == "0"){
				if(result_content.midBefore == 0){
					alert("無前一日資料");
					return;
				} else if (result_content.midAfter == 0){
					alert("無後一日資料");
					return;
				}
			}
			*/
			console.log(result_content);
			$("#lunch_content").html("");
			$("#food_info").html("");
			$("#seasoning_info").html("");
			$("#controller").html("");
			$("#query_detail_info").html("");
			$("#school_and_date").html("");
			$("#query_supply_info").html("");
			if(result_content.schoolName == "" || result_content.schoolName == null ){
			var new_Titlte = "<div id='MAIN_SECOND_BAR'  class='tal_ch search_re_tit'>請選擇學校<br><a href='#'  onclick='javascript:location.href='..\/main/'' >回前頁</a></div><div class='bw_line'></div>";
			}else{
			//標題
			var new_Titlte = "<div id='MAIN_SECOND_BAR'  class='tal_ch search_re_tit'>" + result_content.schoolName +" : <br>"
						+ result_content.date + " 供餐資訊</div><div class='bw_line'></div><div class='condition'><div class='tal_ch condition_data'><div class='condition_data_liner'></div><ul><li class=''>供餐類型：</li><li class='tal_le'><span name='dropdown_service' id='dropdown_service'></li></ul><div id='supplySelect'><div class='condition_data_liner'></div><ul><li class=''>供應商：</li><li class='tal_le'><span name='dropdown_Kitchen' id='dropdown_Kitchen'></li></ul></div></div></div><div class='bw_line'></div>";
			$("#school_and_date").append(new_Titlte);
			}
			//控制項(上一頁、前一日、後一日)
			var controller_div = "<div class='h_94px lh_94 tal_ch pf_bbter'><a href='#'  onclick=\"javascript:location.href='../main/'\" >回前頁</a>&nbsp;";
			if(result_content.midBefore != 0) {
				controller_div += "<a href='#' onclick='query_detail_data(" + result_content.midBefore  +")'>前一日</a>&nbsp;";
			}
			if(result_content.midAfter != 0){
				controller_div += "<a href='#' onclick='query_detail_data(" + result_content.midAfter  + ")'>下一日</a></div><div class='bw_line' ></div>";
			}
			$("#controller").append(controller_div);
			
		
			//午餐內容
			var lunch_content_div = " ";
				for(var k=0; k<result_content.lunchContent.length; k++){
					// if(k%3 == 0 && k!=0) { lunch_content_div += "<tr>";	var count = 0;}
						lunch_content_div +=" ";
						if(result_content.showMode){
							lunch_content_div += "<script> $(document).ready(function() { $('#show_MD-"+k+"').click(function() {$('#detail-"+k+"').slideToggle('fast');$('#menu-"+k+"').slideToggle('fast');}); }); <\/script>";	
						}else{
							lunch_content_div += "<script> $(document).ready(function() { $('#show_MD-"+k+"').click(function() {$('#detail-"+k+"').slideToggle('fast');}); }); <\/script>";
						}
						if(result_content.lunchContent[k].image){lunch_content_div +="<div><div id='menu-"+k+"'class='seafod_pix tabs-1' style='display:none;'><img src='" + result_content.lunchContent[k].image + "'  width='150' height='130' /></div></div>";}
						lunch_content_div +="<div class='tabs-1 w_80ps seafod_batxt' id='show_MD-"+k+"'><div style='text-align: right; width:40%;float: left;'><span>" + result_content.lunchContent[k].category + "：</span></div>";
						lunch_content_div +="<div style='text-align: left; width:60%;float: left;'>" + result_content.lunchContent[k].foodName + "</div></div>";
						var lunch_content_detail ="<div><div id='detail-"+k+"' class='TAB_TY_A tal_ch'  style='display:none;' ><table class='component TAB_TY_C'  width='100%'>";
						lunch_content_detail += "<tr>";
							lunch_content_detail += "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>原料</td>" +
						 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>品牌</td>" +
							 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>認證標章</td>" +
							 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>供應商名稱</td>";
							lunch_content_detail += "</tr>";
						
						for(var i=0; i<result_content.foodInfo.length; i++){
							if(result_content.foodInfo[i].foodName==result_content.lunchContent[k].foodName){
							lunch_content_detail += "<tr>";
							lunch_content_detail += "<td  align='center' valign='middle' ";if(i%2 == 1){lunch_content_detail +=" bgcolor='#ecf2f3'>"; }else if(i%2 == 0){lunch_content_detail +=" bgcolor='#ffffff'>" ;} lunch_content_detail += result_content.foodInfo[i].material + "</td>" +
						 	 "<td  align='center' valign='middle' ";if(i%2 == 1){lunch_content_detail +=" bgcolor='#ecf2f3'>"; }else if(i%2 == 0){lunch_content_detail +=" bgcolor='#ffffff'>" ;} lunch_content_detail += result_content.foodInfo[i].brand + "</td>" +
							 "<td  id='materialCert"+i+"' align='center' valign='middle' ";if(i%2 == 1){lunch_content_detail +=" bgcolor='#ecf2f3'>"; }else if(i%2 == 0){lunch_content_detail +=" bgcolor='#ffffff'>" ;} lunch_content_detail +=  "</td>" +
							 "<td  align='center' valign='middle' ";if(i%2 == 1){lunch_content_detail +=" bgcolor='#ecf2f3'>"; }else if(i%2 == 0){lunch_content_detail +=" bgcolor='#ffffff'>" ;} lunch_content_detail += result_content.foodInfo[i].source + "</td>";
							 lunch_content_detail += "</tr>";
							 }
							//CAS認證資訊
							if(result_content.foodInfo[i].ingredientCertificateObject != null){
								//console.log(result_content.foodInfo[i].ingredientCertificateObject);
								if($("#divmaterialCert"+i).length == 0){
									createCasCertInfo(result_content.foodInfo[i].ingredientCertificateObject,'materialCert'+i);
								}
							}
						}
						lunch_content_detail += "</table></div></div>";
						lunch_content_div +=lunch_content_detail+"";
						
						
				
						
						//count ++;
					//if(count%3 == 0 || result_content.lunchContent.length == (k+1)) { lunch_content_div += "</tr>"; }
				}
			//lunch_content_div +="<div class='h_60px lh_60 tal_ch atch_txt tabs-1'>菜色圖片為示意圖</div>";
			$("#lunch_content").append(lunch_content_div);

			
			//調味料
			var seasoning_div = "<div class='tabs-3 TAB_TY_A tal_ch search_tab_box'><table class='component TAB_TY_C' width='90%'>";
			seasoning_div += "<tr>";
				seasoning_div += "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>原料</td>" +
			 	 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>品牌</td>" +
				 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>認證標章</td>" +
				 "<td align='center' valign='middle' bgcolor='#669db1' class='componentTitle TIT_A sea_title'>來源</td>";
			seasoning_div += "</tr>";
			
			for(var j=0; j<result_content.seasoning.length; j++){
				seasoning_div += "<tr>";
				seasoning_div += "<td  align='center' valign='middle' ";if(i%2 == 1){seasoning_div +=" class='componetContent' bgcolor='#ecf2f3'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2' bgcolor='#ffffff'>" ;} seasoning_div +=  result_content.seasoning[j].material + "</td>" +
				"<td  align='center' valign='middle' ";if(i%2 == 1){seasoning_div +=" class='componetContent' bgcolor='#ecf2f3'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2' bgcolor='#ffffff'>" ;} seasoning_div += result_content.seasoning[j].brand + "</td>" +
				"<td  id='seasoningCert"+j+"' align='center' valign='middle' ";if(i%2 == 1){seasoning_div +=" class='componetContent' bgcolor='#ecf2f3'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2' bgcolor='#ffffff'>" ;} seasoning_div += "</td>" +
				"<td  align='center' valign='middle' ";if(i%2 == 1){seasoning_div +=" class='componetContent' bgcolor='#ecf2f3'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2' bgcolor='#ffffff'>" ;} seasoning_div +=  result_content.seasoning[j].source + "</td>";
				seasoning_div += "</tr>";
				
				//CAS認證資訊
				if(result_content.seasoning[j].ingredientCertificateObject != null){
					//console.log(result_content.foodInfo[i].ingredientCertificateObject);
					createCasCertInfo(result_content.seasoning[j].ingredientCertificateObject,'seasoningCert'+j);
				}
			}
			
		seasoning_div +="</table></div><div class='tabs-3 bw_line'></div>";	
			$("#seasoning_info").append(seasoning_div);
			
			//營養份量
			var new_div = "<div class='tabs-4 TAB_TY_A tal_ch search_tab_box'>";
				new_div += "";
					new_div += "<table width='90%' border='1'>";
					//	new_div += "<tr>";
					//		new_div += "<td colspan='2' align='center' bgcolor='#669db1' class='TIT_A sea_title'>營養份量</td>";
					//	new_div += "</tr>";
						new_div += "<tr bgcolor='#ffffff'>";
						new_div += "<td align='right' width='50%' >全榖根莖類：	</td>";
						new_div += "<td align='left' width='50%'>"+result_content.nutrition.mainFood +"份</td>";
					new_div += "</tr>";

					new_div += "<tr bgcolor='#ecf2f3'>";
						new_div += "<td align='right' width='50%'>蔬菜類：	</td>";
						new_div += "<td align='left' width='50%'>"+ result_content.nutrition.vegetable +"份</td>";
					new_div += "</tr>";

					new_div += "<tr bgcolor='#ffffff'>";
						new_div += "<td align='right'  width='50%'>油脂與堅果種子類：	</td>";
						new_div += "<td align='left' width='50%'>"+ result_content.nutrition.oil +"份</td>";
					new_div += "</tr>";
					
					new_div += "<tr  bgcolor='#ecf2f3'>";
						new_div += "<td align='right' width='50%'>蛋豆魚肉類：	</td>";
						new_div += "<td align='left' width='50%'>"+ result_content.nutrition.meatBeans +"份</td>";
					new_div += "</tr>";

					new_div += "<tr bgcolor='#ffffff'>";
						new_div += "<td align='right' width='50%'>水果類：	</td>";
						new_div += "<td align='left' width='50%'>"+ result_content.nutrition.fruit +"份</td>";								
						new_div += "</tr>";
			
					new_div += "<tr bgcolor='#ecf2f3'>";
						new_div += "<td align='right' width='50%'>乳品類：	</td>";
						new_div += "<td align='left' width='50%'>"+ result_content.nutrition.milk +"份</td>";
					new_div += "</tr>";
		
					new_div += "<tr bgcolor='#ffffff'>";
						new_div += "<td align='right' width='50%'>熱量：	</td>";			
						new_div += "<td align='left' width='50%'>"+ result_content.nutrition.calories +"大卡</td>";								
					new_div += "</tr>";
					new_div += "</table>";
				new_div += "</div>";
			new_div += "<div class='tabs-4 bw_line'></div>";
		$("#query_detail_info").append(new_div); 	
		//供餐者資訊
		var  new_Sup = "<div class='tabs-5 TAB_TY_A tal_ch search_tab_box'>";
			new_Sup += "<table width='90%' border='1'>";
					if(result_content.supplierInfo.kitchenType=="006"){
					//new_Sup += "<tr>";
					//	new_Sup += " <td colspan='2' align='center' bgcolor='#669db1' class='TIT_A sea_title'>供餐者資訊</td>";
					//new_Sup += "</tr>";
					new_Sup += "<tr>";
						new_Sup += " <td colspan='2' align='center' bgcolor='#669db1' class='TIT_A'>供餐者資訊</td>";
					new_Sup += "</tr>";
		
					new_Sup += "<tr bgcolor='#ffffff'>";
						new_Sup += "<td align='right'>供應學校：</td>";
						new_Sup += "<td align='left'>"+ result_content.supplierInfo.supplierName +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor='#ecf2f3'>";
						new_Sup += "<td align='right'>學校地址：</td>";
						new_Sup += "<td align='left'>"+ result_content.supplierInfo.supplierAddress +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor='#ffffff'>";
						new_Sup += "<td align='right'>學校電話：</td>";
						new_Sup += "<td align='left'>"+ result_content.supplierInfo.supplierPhone +"</td>";
					new_Sup += "</tr>";		
						
					new_Sup += "<tr bgcolor='#ecf2f3'>";
						new_Sup += "<td align='right'>營養師/午餐秘書：</td>";
						new_Sup += "<td align='left'>"+ result_content.supplierInfo.dietitians +"</td>";
					new_Sup += "</tr>";
					
								
					}else{ //業者與其他
							new_Sup += "<tr>";
							new_Sup += " <td colspan='2' align='center' bgcolor='#669db1' class='TIT_A'>供餐者資訊</td>";
						new_Sup += "</tr>";
					new_Sup += "<tr bgcolor='#ffffff'>";
						new_Sup += "<td align='right'>供應商：</td>";
						new_Sup += "<td align='left'>"+ result_content.supplierInfo.supplierName +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor='#ecf2f3'>";
						new_Sup += "<td align='right'>供應商地址：</td>";
						new_Sup += "<td align='left'>"+ result_content.supplierInfo.supplierAddress +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor='#ffffff'>";
						new_Sup += "<td align='right'>供應商電話：</td>";
						new_Sup += "<td align='left'>"+ result_content.supplierInfo.supplierPhone +"</td>";
					new_Sup += "</tr>";
		
					new_Sup += "<tr bgcolor='#ecf2f3'>";
						new_Sup += "<td align='right'>衛管人員：</td>";
						new_Sup += "<td align='left'>"+ result_content.supplierInfo.supplierLeader +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor='#ffffff'>";
						new_Sup += "<td align='right'>營養師：</td>";
						new_Sup += "<td align='left'>"+ result_content.supplierInfo.dietitians +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor='#ecf2f3'>";
						new_Sup += "<td align='right'>品質認證體系：</td>";
						new_Sup += "<td align='left'>"+ result_content.supplierInfo.authenticate +"</td>";
					new_Sup += "</tr>";
					}
				new_Sup += "</table>";
			new_Sup += "</div>";
		new_Sup += "</div><div  class='tabs-5 bw_line'></div>";

	$("#query_supply_info").append(new_Sup);
			
			
	//assign a 給有CAS資訊的食材
	for(var i=0;i<result_content.foodInfo.length;i++){
		if(result_content.foodInfo[i].ingredientCertificateObject != null){
			var method = 'openDialog("materialCert' +i+ '")';
			//var link = "<a style='font-size:15px;' href='#' onclick='"+method+";return false;'><u> CAS </u></a>";
			var link = "<a href='#' data-theme='b' onclick='"+method+";return false;' data-rel='dialog' >CAS</a>";
			$('#materialCert' + i).append(link);
		}
	}	
			
	//assign a 給有CAS資訊的調味料
	for(var i=0;i<result_content.seasoning.length;i++){
		if(result_content.seasoning[i].ingredientCertificateObject != null){
			var method = 'openDialog("seasoningCert' +i+ '")';
			//var link = "<a style='font-size:15px;' href='#' onclick='"+method+";return false;'><u> CAS </u></a>";
			var link = "<a href='#' data-theme='b' onclick='"+method+";return false;' data-rel='dialog' >CAS</a>";
			$('#seasoningCert' + i).append(link);
		}
	}		

		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
	
	
	
	//$("#dss").hide("slow");
	//$("#calendar").hide("slow");
	
	//$("#query_detail_info").show("slow");
	$("#tabs").show("slow");
	$("#controller").show("slow");
	
	offeringService(mid);
	kitchen(mid);
}

//Raymond 
	function showCertInfo(linkid){
		$(".ui-dialog-content").dialog("close");
		$("#div"+linkid).dialog();
	}

function createCasCertInfo(certInfo,serno){
		var certDiv = '';
		certDiv += "<div class=\"TAB_TY_A\" id=\""+ "div" + serno +"\" title=\"CAS認證廠商資訊\">";
		certDiv += "<table  border=\"1\">";
			
			certDiv += "<tr >";
			certDiv += "<td align=\"right\">認證編號</td>";
			certDiv += "<td align=\"left\">"+certInfo.certNo +"</td>";
		certDiv += "</tr>";

		certDiv += "<tr >";
			certDiv += "<td align=\"right\">公司名稱</td>";
			certDiv += "<td align=\"left\">"+ certInfo.companyName +"</td>";
		certDiv += "</tr>";
		
		certDiv += "<tr>";
			certDiv += "<td align=\"right\">公司統一編號</td>";
			certDiv += "<td align=\"left\">"+ certInfo.companyId +"</td>";
		certDiv += "</tr>";
		
		certDiv += "<tr >";
			certDiv += "<td align=\"right\">負責人</td>";
			certDiv += "<td align=\"left\">"+ certInfo.director +"</td>";
		certDiv += "</tr>";
			
		certDiv += "<tr >";
			certDiv += "<td align=\"right\">地址</td>";
			certDiv += "<td align=\"left\">"+ certInfo.address +"</td>";
		certDiv += "</tr>";
		
		certDiv += "<tr >";
			certDiv += "<td align=\"right\">連絡電話</td>";
			certDiv += "<td align=\"left\">"+ certInfo.tel +"</td>";
		certDiv += "</tr>";
		
		certDiv += "<tr >";
			certDiv += "<td align=\"right\">傳真</td>";
			certDiv += "<td align=\"left\">"+ certInfo.fax +"</td>";
		certDiv += "</tr>";

		certDiv += "<tr >";
			certDiv += "<td align=\"right\">網站</td>";
			//certDiv += "<td align=\"left\">"+ certInfo.website +"</td>";
			certDiv += "<td align=\"left\"></td>";
		certDiv += "</tr>";
		
		certDiv += "</table>";
		certDiv += "</div>";
		
		$("#cert_info").append(certDiv);

	}
	
	var menuTypeList = [];
	
	function queryPreSchool_data(schoolId,period){		
		/* 
		url: http://10.10.20.125:3000/offered/meal?SchoolId=64740275&period=2015-03-04&MenuType=1
		param:
			SchoolId:64740275
			period:2015-03-04
			MenuType:1			
		取BatchDateId後呼叫			
		url: http://10.10.20.125:3000/dish?BatchDataId=1427962944323645		
		*/			
		var offedMealUrl = url + "/offered/meal?SchoolId=" + schoolId + "&period=" + period;
		var param = {"SchoolId":schoolId , "period":period};
		var dishUrl = "";
		console.log(offedMealUrl);
		var res = ajaxCallJsonp(offedMealUrl, param, function(data){
			console.log(data);
			data = data.data;
			var batchDataId = data[0].BatchDataId;
			
			dishUrl = url + "/dish?BatchDataId=" + batchDataId;
			param = {"BatchDataId":batchDataId};
			console.log(dishUrl);
			
			var res1 = ajaxCallJsonp(dishUrl, param, function(data){
				data = data.data;
					
				console.log(data);
				
				showPreSchoolData(data);
			},true,"GET");
			
		},true,"GET");
	}
	
	function queryStore_date(){
		
	}

$(document).ready(function(){
	var school = '<%=school%>';
	var Kitchen = '<%=Kitchen%>';
	var period = '<%=period%>';
	console.log("school=>" + school);
	console.log("Kitchen=>" + Kitchen);
	console.log("period=>" + period);
		
	query_detail_data(<%=Kitchen%>);
	console.log($("#service").children("option:selected").attr("menutype"));
	$('#seasoning_click').click(function() {
		$('#seasoning_info').slideToggle("fast");
	});
	$('#query_detail_click').click(function() {
		$('#query_detail_info').slideToggle("fast");
	});
	$('#query_supply_click').click(function() {
		$('#query_supply_info').slideToggle("fast");
	});
});
</script>
</head>
<body>
	<div id="WRAPPER" class="over_hi">
		<div id="school_and_date"></div>
		<div id="tabs" class="tal_ch w_639ch">

			<div id="lunch_content"></div>

			<div id="seasoning_click" class=" w_100ps tal_ch search_re w_639ch">
				<ul>
					<li><span>調味料</span></li>
				</ul>
			</div>
			<div id="seasoning_info" style="display: none;"></div>
			<div id="query_detail_click" class=" w_100ps tal_ch search_re w_639ch">
				<ul>
					<li><span>營養份量</span></li>
				</ul>
			</div>
			<div id="query_detail_info" style="display: none;"></div>
			<div id="query_supply_click" class=" w_100ps tal_ch search_re w_639ch">
				<ul>
					<li><span>供餐者資訊</span></li>
				</ul>
			</div>
			<div id="query_supply_info" style="display: none;"></div>
		</div>
		<div id="controller"></div>
		<div id="cert_info" style="display: none"></div>
	</div>


	<script>
		function openDialog(linkid) {
			$("#certCasContent").html("");
			var html = $("#div" + linkid).html();
			$("#certCasContent").html(html);
			$.mobile.changePage("#certCasPage", {
				role : "dialog",
				transition : "flip",
				overlayTheme: "d"
			});
		}
	</script>

</body>
</html>