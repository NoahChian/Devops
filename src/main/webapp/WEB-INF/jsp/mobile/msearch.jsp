<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	//Ric  get cookie
	
	var m_city=getCookie("m_City");
	var m_area=getCookie("m_Area");
	var m_school =getCookie("m_School");
	
	var drop_down_default = "<select name=\"City\" id=\"City\"><option value=\"0\">請選擇</option></select>";
	//縣市
	function city(){
		var request_data = {
			"method" : "customerQueryCounties",
			"args" : {
				"condition" : 1
			}
		};
		var response_obj = call_rest_api(request_data);

		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { //success
				var drop_down_counties = "<select name=\"City\" id=\"City\" onChange=\"area()\">";
				drop_down_counties += "<option value=\"0\">請選擇</option>";
				for (var i = 0; i < result_content.counties.length; i++) {

					drop_down_counties += "<option value=" + result_content.counties[i].cid + ">" + result_content.counties[i].countiesName + "</option>";
				}
				drop_down_counties += "</select>";
				$("#dropdown_City").append(drop_down_counties);
				$("#dropdown_Area").append(drop_down_default);
				$("#dropdown_School").append(drop_down_default);
				$("#dropdown_Kitchen").append(drop_down_default);
				$("#City").val(m_city);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else {
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}

	}

	//縣市改變修改區域
	function area(){
		//alert("aa");
		var counties = $("#City").val();
		//alert(counties);
		var request_data = {
			"method" : "customerQueryArea",
			"args" : {
				"cid" : counties
			}
		};
		var response_obj = call_rest_api(request_data);

		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { //success
				var drop_down_area = "<select name=\"Area\" id=\"Area\"  onChange=\"school()\">";
				drop_down_area += "<option value=\"0\">請選擇</option>";
				for (var i = 0; i < result_content.area.length; i++) {

					drop_down_area += "<option value=" +  result_content.area[i].aid + ">" + result_content.area[i].areaName + "</option>";
				}
				drop_down_area += "</select>";

				//清空
				$("#dropdown_Area").html("");
				$("#dropdown_School").html("");
				$("#dropdown_Kitchen").html("");

				$("#dropdown_Area").append(drop_down_area);
				$("#dropdown_School").append(drop_down_default);
				$("#dropdown_Kitchen").append(drop_down_default);
				
			$("#Area").val(m_area);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else {
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}

	//區域改變修改學校
	function school() {
		var area = $("#Area").val();
		var countiesName = $("#City").find('option:selected').text();
		var areaName = $("#Area").find('option:selected').text();

		//alert(area);

		var request_data = {
			"method" : "customerQuerySchool",
			"args" : {
				"aid" : area
			}
		};
		var response_obj = call_rest_api(request_data);

		//alert(response_obj.toString());

		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;

			//alert(result_content.resStatus);

			if (result_content.resStatus == 1) { //success
				//var drop_down_school = "<select name=\"School\" id=\"School\" onChange=\"todatDaty()\" >";
				var drop_down_school = "<select name=\"School\" id=\"School\" onChange=\"todatDaty()\" >";
				drop_down_school += "<option value=\"0\">請選擇</option>";
				for (var i = 0; i < result_content.school.length; i++) {
					var sch_Name1 = result_content.school[i].schoolName.replace(countiesName + "立", "");
					var sch_Name0 = sch_Name1.replace(countiesName, "");
					var sch_Name = sch_Name0.replace(areaName, "");

					drop_down_school += "<option value=" +  result_content.school[i].sid + ">" + sch_Name + "</option>";
				}
				drop_down_school += "</select>";
		
				//清空
				$("#dropdown_School").html("");
				$("#dropdown_Kitchen").html("");

				$("#dropdown_School").append(drop_down_school);
				$("#dropdown_Kitchen").append(drop_down_default);
				
			$("#School").val(m_school);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else {
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}
	function kitchen() {
		var sch = $("#School").val();
		var cal = $("#CALENDAR").val();
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
				"date" : cal
			}
		};
		var response_obj = call_rest_api(request_data);

		//alert(response_obj.toString());

		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;

			//alert(result_content.resStatus);

			if (result_content.resStatus == 1) { //success
				var drop_down_kitchen = "<select name=\"Kitchen\" id=\"Kitchen\" onChange=\"showButton()\"  >";
				//drop_down_kitchen += "<option value=\"0\">請選擇</option>";
				if (result_content.kitchen.length == 0) {
					drop_down_kitchen += "<option value=\"0\">本日無供餐資訊</option>";
				} else {
					drop_down_kitchen += "<option value=\"0\">請選擇</option>";
				}
				for (var i = 0; i < result_content.kitchen.length; i++) {
					if(i==0){
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
	function showButton() {

		var listVal = $("#Kitchen").val();
		/*
		if (listVal == 0) {
			$("#button_Submit").html("");
			$("#button_Submit").html("<a href=\"#\">請選擇供應商</a>");
		} else {
			*/
			$("#button_Submit").html("");
			$("#button_Submit").html("<a href=\"#\" onClick=\"searchMenu()\" >確定查詢</a>");
		/*
		}
		*/
	}
	function todatDaty() {
		//預設跳出今日
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
		$("#CALENDAR").val(today);
		$("#CALENDAR").attr("value", today);
		//kitchen();
		showButton();
	}
	function searchMenu() {
		var kit = $("#Kitchen").val();
		var cal = $("#CALENDAR").val();
		var sch = $("#School").val();
		var area = $("#Area").val();
		var city = $("#City").val();
		
		if(cal == "0" || sch == "0" || area == "0" || city == "0"){
			alert("請完成項目選擇!");
			return null;
		}
		
		if (kit == "" || kit == null || kit == "0") {
			alert("本日無供餐，請重新選擇日期!");
			return null;
		}
		
		//設置cookie 20140513 KC
		setCookie("m_City", $("#City").val());
		setCookie("m_Area", $("#Area").val());
		setCookie("m_School", $("#School").val());
		
		$("#searchMenu").submit();
	}
	
	$(document).ready(function() {
		city();
		area();
		school();
		todatDaty(); //Ric 2014/05/13 
		//kitchen(); //Ric 2014/05/19 
		//setCookies()
	});
	$(document).bind("mobileinit", function() {
		$.mobile.page.prototype.options.degradeInputs.date = true;
	});
	
	function getCookie(name) {
		if(typeof String.prototype.trim !== 'function') {
			  String.prototype.trim = function() {
			    return this.replace(/^\s+|\s+$/g, ''); 
			  };
			}
		name = name + "=";
		var ca = document.cookie.split(';');
		for(var i=0; i<ca.length; i++) {
			var c = ca[i].trim();
		  	if (c.indexOf(name)==0) return c.substring(name.length,c.length);
		}
		return "";
	}

	function setCookie(name,value) {
		var d = new Date();
		d.setTime(d.getTime()+(365*24*60*60*1000));
		var expires = "expires="+d.toGMTString();
		document.cookie = name+"="+value+"; "+expires;
	}	
</script>

	<div id="WRAPPER" class="over_hi">
		<div id="" class=" h_60px lh_60 tal_ch cont_title">查詢條件設定</div>
		<div id="" class="bw_line"></div>
		<form id="searchMenu" method="post" action="../mcontents/" onkeypress="return event.keyCode != 13;" data-ajax="false">
			<div id="" class="condition">
				<div id="" class=" tal_ch condition_data">									
					<div id="" class="condition_data_liner"></div>
					<ul>
						<li class="">縣∕市</li>
						<li class="tal_le"><span name="dropdown_City" id="dropdown_City"></li>
					</ul>
					<div id="" class="condition_data_liner"></div>
					<ul>
						<li class="">市∕區</li>
						<li class="tal_le"><span name="dropdown_Area" id="dropdown_Area"></li>
					</ul>
					<div id="" class="condition_data_liner"></div>
					<ul>
						<li class="">學校</li>
						<li class="tal_le"><span name="dropdown_School" id="dropdown_School"></li>
					</ul>
					<div id="" class="condition_data_liner"></div>
					<ul>
						<li class="">日期</li>
						<li class="tal_le"><input id="CALENDAR" type="date" size="12" name="CALENDAR" onChange="kitchen()" data-role="date" data-options='{"mode": "calbox", "calTodayButton": true}' /></li>
					</ul>
					<!-- <div id="" class="condition_data_liner"></div> -->
					<ul style="display:none">
						<li class="">供應商</li>
						<li class="tal_le"><span name="dropdown_Kitchen" id="dropdown_Kitchen"></li>
					</ul>
				</div>
			</div>
			<div id="" class="bw_line"></div>
			<div id="" class="h_94px lh_94 tal_ch pf_bbter">
				<span name="button_Submit" id="button_Submit">
			</div>
		</form>
		<div id="" class="bw_line"></div>
	</div>

