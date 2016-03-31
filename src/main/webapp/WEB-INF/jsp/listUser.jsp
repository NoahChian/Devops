<%@page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="util.jsp" %> <!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js/jquery.twbsPagination.min.js"></script>
<script src="../../js_ca/combobox.js"></script>
<%
	String uType = "";
	String uName = "";
	String roletype = "";
	String countyType = "";
	if(session.getAttribute("account")!=null){
	uType = (String) session.getAttribute("uType");
	uName = (String) session.getAttribute("uName");
	roletype = (String) session.getAttribute("roletype");
	countyType = AuthenUtil.getCountyTypeByUsername(uName);
	}
%>

<script>
 
var row = 300;

var govern_type = "";//主管機關Type
function selectTypes(tv) {
	document.getElementById("userType").value = ""; //userType起始清空
	if (tv == "0") {
		$("#009only").hide();
		$("#unametype").html("");
		document.getElementById("userType").value = "";
		$("#no_kitchen").hide();
		$("#dropdown_kitchen").show();
		$("#dropCounties").html("");
		$("#dropCounties").hide();
		showuserType();
	} else if (tv == "12#001" || tv =="12#002") {
		$("#009only").hide();
		queryCounties();
		$("#unametype").html("");
		$("#no_kitchen").show("slow");
		$("#dropdown_kitchen").hide();
		$("#dropCounties").show();
		showuserType();
	} else if (tv == "11") {
		$("#009only").hide();
		$("#dropCounties").html("");
		$("#dropCounties").hide();
		var valuex = document.getElementById("selectType").value;
		$("#no_kitchen").show("slow");
		$("#dropdown_kitchen").hide();
		document.getElementById("userType").value = valuex;
		$("#unametype").html("");
		$("#unametype").append(valuex+"-");
		showuserType();
	}else if (tv=="000" || tv=="11#1") {
		$("#009only").hide();
		var valuex = document.getElementById("selectType").value;
		$("#no_kitchen").show("slow");
		$("#dropdown_kitchen").hide();
		document.getElementById("userType").value = valuex;
		$("#unametype").html("");
		$("#dropCounties").html("");
		$("#dropCounties").hide();
		showuserType();
	}else if (tv=="009") {
		$("#009only").show("slow")
		$("#dropCounties").html("");
		$("#unametype").html("");
		document.getElementById("userType").value = document.getElementById("selectType").value;
		$("#no_kitchen").hide();
		$("#dropdown_kitchen").show();
		$("#dropCounties").hide();
		showuserType();
	}else{
		$("#009only").hide();
		$("#dropCounties").html("");
		$("#unametype").html("");
		document.getElementById("userType").value = document.getElementById("selectType").value;
		$("#no_kitchen").hide();
		$("#dropdown_kitchen").show();
		$("#dropCounties").hide();
		showuserType();
	}
}

function govCounties(gg){
		if(gg=="0"){document.getElementById("userType").value = "";}
		document.getElementById("userType").value = "";
		var Stype= $("#selectType").val();
		document.getElementById("userType").value = Stype.replace("#", gg);
		$("#unametype").html("");
		$("#unametype").append( document.getElementById("userType").value +"-");
		showuserType();
}
function showuserType(){
	if(document.getElementById("userType").value=="11"){
		document.getElementById("username").value= document.getElementById("userType").value +"-"+  document.getElementById("unameuser").value;
		}else if(document.getElementById("selectType").value=="12#001" || document.getElementById("selectType").value=="12#002"){
			document.getElementById("username").value= document.getElementById("userType").value +"-"+  document.getElementById("unameuser").value;
		}else{
		document.getElementById("username").value= document.getElementById("unameuser").value
	}
}
function changePassword(){
$("#ori_pass").show();

}
function add_user_detail() {
	$("#User_list").hide("slow");
	$("#query_rule").hide();
	$("#query_bar").hide();
	$("#pagination").hide();
	$("#page").hide();
		var new_div = "<div id=\"queryList\" class=\"TAB_TY_B\">";
			//for(var i = 0 ; i<result_content.length; i++)					{
			new_div += " <table width=\"100%\" class=\"table table-bordered table-striped\">";
			new_div += " <tbody>";
			new_div += " <tr>";
			new_div += " <td class=\"componentContentRightLine\" width=\"80px\">帳戶身分 *</td>";
			new_div += " <td class=\"componentContentRightLine\"  colspan=\"2\" >";
			new_div += " <select name=\"selectType\" id=\"selectType\" onChange=\"selectTypes(this.value)\" >";
			new_div += "<option value=\"\">請選擇</option>";
			new_div += "</select>";
			new_div += " <select name=\"dropCounties\" id=\"dropCounties\" onChange='govCounties(this.value)'>";
			new_div += "<option value='0'>請選擇</option>";
			new_div += "</select>";
			new_div += "</td>";
			new_div += " <td  class=\"componentContentRightLine\" width=\"230px\">";
			new_div += " <input readonly type=\"text\" name=\"userType\" id=\"userType\" size=\"30\" value=\"\"></td>";
			new_div += " </tr>";
			new_div += " <tr>";
			new_div += " <td class=\"componentContentRightLine\" width=\"80px\">帳戶名稱 *</td>";
			new_div += " <td  class=\"componentContentRightLine\" width=\"230px\">";
			//new_div += " "+ result_content.kitchenName + "</td>";
			new_div += " <input type=\"text\" name=\"uname\" id=\"uname\" size=\"30\" value=\"\" class=\"max45\">		</td>";
			new_div += " <td  class=\"componentContentRightLine\" width=\"120px\">Email*</td>";
			new_div += " <td class=\"componentContentRightLine\" >";
			new_div += "  <input type=\"text\" name=\"email\" id=\"email\" size=\"30\" value=\"\" class=\"max45\">		</td>";
			new_div += " </tr>";
			new_div += " <tr>";
			new_div += " <td class=\"componentContentRightLine\" width=\"80px\">登入帳號 *</td>";
			new_div += " <td colspan=\"3\" class=\"componentContentRightLine\" >";
			new_div += " <span  name=\"unametype\" id=\"unametype\"></span><input type=\"text\" name=\"unameuser\" id=\"unameuser\" size=\"10\" value=\"\" onchange=\"showuserType()\" class=\"max40\" ><input type=\"hidden\" name=\"username\" id=\"username\" size=\"30\" value=\"\"><span id='009only'> -員生消費合作社</span></td>";
			new_div += " </tr>";
			new_div += " <tr>";
			new_div += " <td class=\"componentContentRightLine\">密碼 *</td>";
			new_div += " <td class=\"componentContentRightLine\" >"
					+ " <input class=\"max255\" type=\"password\" name=\"password\" id=\"password\" size=\"30\" value=\"\"></td>";
			new_div += " <td class=\"componentContentRightLine\">確認密碼 *</td>";
			new_div += " <td class=\"componentContentRightLine\" >"
					+ " <input class=\"max255\" type=\"password\" name=\"passwordcheck\" id=\"passwordcheck\" size=\"30\" value=\"\"></td>";
			new_div += " </tr>";
			new_div += " <tr>";
			new_div += " <td class=\"componentContentRightLine\">對應供餐廚房</td>";
			new_div += " <td class=\"componentContentRightLine\" colspan=\"3\" >"
					+ "<span  name=\"dropdown_kitchen\" id=\"dropdown_kitchen\" ></span><span  name=\"no_kitchen\" id=\"no_kitchen\"  style=\"color:red;\">主管機關無對應供餐廚房</span></td>";
			new_div += " </tr>";
			
			new_div += " <td colspan=\"4\">";
			new_div += " <div align=\"center\">"
					+ "  <input class='btn btn-primary' style='margin:0' type=\"button\" value=\"確認\" id=\"update_user_button\" onClick=\"addUser()\" >"
					+ "  <input class='btn btn-primary' style='margin:0' type=\"button\" value=\"取消\" onClick=\"goBack()\" >"
					+ " </div></td>";
			new_div += " </tr>";
			new_div += " </table>";
			$("#queryList").html("");
			$("#queryList").append(new_div);
			$("#queryList").show();
			$("#no_kitchen").hide();
			$("#goBack").show();
			/*
			*20140630 Ric
			* Setting maxlength and readonly
			*/
			setInputLength();
			$("#dropCounties").hide();
			kitchens();
			query_Select_Type();
	
}
function query_user_detail(username) {
	$("#User_list").hide("slow");
	$("#query_rule").hide();
	$("#query_bar").hide();
	$('#pagination').hide();
	$("#page").hide();
	var request_data = {
		"method" : "queryUserDetail",
		"args" : {
			"username" : username
		}
	};

	var response_obj = call_rest_api(request_data);

	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == "1") { //success
			var new_div = "<div id=\"queryList\" class=\"TAB_TY_B\">";
			//for(var i = 0 ; i<result_content.length; i++)					{
			new_div += "<table width=\"100%\" class=\"table table-bordered table-striped\">";
			new_div += " <tbody>";
			new_div += " <tr>";
			new_div += " <td class=\"componentContentRightLine\" width=\"80px\">帳戶身分 *</td>";
			new_div += " <td class=\"componentContentRightLine\"  colspan=\"3\" >";
			/*new_div += " <select name=\"selectType\" id=\"selectType\" onChange=\"selectTypes(this.value)\" >"
					+ "<option value=\"0\">請選擇</option>"
					+ "<option value=\"1\">團膳業者</option>"
					+ "<option value=\"2\">自立廚房</option>"
					+ "<option value=\"3\">主管機關</option>";
			new_div += "</select>";
			new_div += " <select name=\"gov_L2\" id=\"gov_L2\" onChange=\"govType2(this.value)\" >"
					+ "<option value=\"0\">請選擇</option>"
					+ "<option value=\"1\">中央、部</option>"
					+ "<option value=\"2\">地方縣市</option>"
					+ "<option value=\"3\">學校</option>";
			new_div += "</select>";
			new_div += " <select name=\"gov_L3\" id=\"gov_L3\" onChange=\"govType3(this.value)\" >"
					+ "<option value=\"0\">請選擇</option>"
					+ "<option value=\"A\">臺北巿</option>"
					+ "<option value=\"B\">臺中巿</option>"
					+ "<option value=\"C\">基隆巿</option>"
					+ "<option value=\"D\">臺南巿</option>"
					+ "<option value=\"E\">高雄巿</option>"
					+ "<option value=\"F\">新北市</option>"
					+ "<option value=\"G\">宜蘭縣</option>"
					+ "<option value=\"H\">桃園縣</option>"
					+ "<option value=\"I\">嘉義巿</option>"
					+ "<option value=\"J\">新竹縣</option>"
					+ "<option value=\"K\">苗栗縣</option>"
					+ "<option value=\"L\">臺中巿</option>"
					+ "<option value=\"M\">南投縣</option>"
					+ "<option value=\"N\">彰化縣</option>"
					+ "<option value=\"O\">新竹巿</option>"
					+ "<option value=\"P\">雲林縣</option>"
					+ "<option value=\"Q\">嘉義縣</option>"
					+ "<option value=\"R\">臺南巿</option>"
					+ "<option value=\"T\">屏東縣</option>"
					+ "<option value=\"U\">花蓮縣</option>"
					+ "<option value=\"V\">臺東縣</option>"
					+ "<option value=\"W\">金門縣</option>"
					+ "<option value=\"X\">澎湖縣</option>"
					+ "<option value=\"Z\">連江縣</option>";
			new_div += "</select>";
			new_div += " <select name=\"gov_L4\" id=\"gov_L4\" onChange=\"govType4(this.value)\" >"
					+ "<option value=\"0\">請選擇</option>"
					+ "<option value=\"000\">管理員</option>"
					+ "<option value=\"001\">衛生局</option>"
					+ "<option value=\"002\">教育局</option>";
			new_div += "</select>";
			new_div += "</td>";
			new_div += " <td  class=\"componentContentRightLine\" width=\"230px\">";
			*/
			new_div += " <input type=\"hidden\" name=\"userType\" id=\"userType\" size=\"30\" value=\""+ result_content.usertype  + "\">"+ result_content.usertype  + "</td>";
			new_div += " </tr>";
			new_div += " <tr>";
			new_div += " <td class=\"componentContentRightLine\" width=\"80px\">帳戶名稱*</td>";
			new_div += " <td  class=\"componentContentRightLine\" width=\"230px\">";
			//new_div += " "+ result_content.kitchenName + "</td>";
			new_div += " <input class=\"max45\" type=\"text\" name=\"uname\" id=\"uname\" size=\"30\" value=\""+ result_content.name  + "\">		</td>";
			new_div += " <td  class=\"componentContentRightLine\" width=\"120px\">Email*</td>";
			new_div += " <td class=\"componentContentRightLine\" >";
			//new_div += " "+ result_content.companyId +"</td>";
			new_div += "  <input  class=\"max45\"type=\"text\" name=\"email\" id=\"email\" size=\"30\" value=\""+ result_content.email  +"\">		</td>";
			new_div += " </tr>";
			new_div += " <tr>";
			new_div += " <td class=\"componentContentRightLine\" width=\"80px\">登入帳號*</td>";
			new_div += " <td  class=\"componentContentRightLine\" width=\"230px\">";
			new_div += " <input type=\"hidden\" name=\"username\" id=\"username\" size=\"30\" value=\""+ result_content.username + "\">	"+ result_content.username + "	</td>";
			new_div += " <td  class=\"componentContentRightLine\" width=\"120px\">新密碼(不改請空白)</td>";
			new_div += " <td class=\"componentContentRightLine\" >";
			new_div += "  <input  class=\"max255\" type=\"password\" name=\"password\" id=\"password\" size=\"30\" value=\"\" onchange=\"changePassword()\">	</td>";
			new_div += " </tr>";
			new_div += " <tr id=\"ori_pass\">";
			new_div += " <td class=\"componentContentRightLine\" width=\"80px\">輸入目前密碼*</td>";
			new_div += " <td  class=\"componentContentRightLine\" width=\"230px\">";
			new_div += " <input  class=\"max255\" type=\"password\" name=\"ori_password\" id=\"ori_password\" size=\"30\" value=\"\" ></td>";
			new_div += " <td  class=\"componentContentRightLine\" width=\"120px\">再次輸入新密碼</td>";
			new_div += " <td class=\"componentContentRightLine\" >";
			new_div += "  <input class=\"max255\" type=\"password\" name=\"passwordcheck\" id=\"passwordcheck\" size=\"30\" value=\"\">	</td>";
			new_div += " </tr>";
			
			new_div += " <tr>";
			new_div += " <td class=\"componentContentRightLine\">對應供餐廚房</td>";
			new_div += " <td class=\"componentContentRightLine\" colspan=\"3\" >"
					+ "<span  name=\"dropdown_kitchen\" id=\"dropdown_kitchen\" ></span><span  name=\"no_kitchen\" id=\"no_kitchen\"  style=\"color:red;\">主管機關無對應供餐廚房</span></td>";
			new_div += " </tr>";

			new_div += " <td colspan=\"4\">";
			new_div += " <div align=\"center\">"
				+ "  <input class='btn btn-primary' style='margin:0' type=\"button\" value=\"確認\" id=\"update_user_button\" onClick=\"updateUserDetail()\" >"
				+ "  <input class='btn btn-primary' style='margin:0' type=\"button\" value=\"取消\" onClick=\"goBack()\" >"
				+ " </div></td>";
			new_div += " </tr>";
			new_div += " </table>";
			//}
			$("#queryList").html("");
			$("#queryList").append(new_div);
			$("#queryList").show();
			$("#goBack").show();
			$("#ori_pass").hide();
			if(result_content.usertype=="11" || result_content.usertype =="12#001" || result_content.usertype =="12#002" || result_content.usertype =="000" || result_content.usertype =="11#1"){
				$("#no_kitchen").show("slow");
				$("#dropdown_kitchen").hide();
			}else{
				$("#no_kitchen").hide();
			}
			/*
			*20140630 Ric
			* Setting maxlength and readonly
			*/
			setInputLength();
		 
			$("#gov_L2").hide();
			$("#gov_L3").hide();
			$("#gov_L4").hide();
			kitchens(result_content.kitchenName);
	
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			return;
		}
	} else {
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		return;
	}
}

function queryCounties(){
	var request_data = {
			"method" : "QueryCountiesV2",
			"args" : {
			}
		};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { //success
			var newOpt = "<option value='0'>請選擇</option>";
			for(i=0;i<result_content.counties.length;i++){
				newOpt += "<option value="+result_content.counties[i].countiesCode+">"+result_content.counties[i].countiesName+"</option>";
			}
			$("#dropCounties").html("");
			$("#dropCounties").append(newOpt);
		}else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	}else {
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}
function kitchens(kitchenName) {
	var request_data = {
		"method" : "queryKitchenList",
		"args" : {}
	};
	var response_obj = call_rest_api(request_data);

	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { //success
			var drop_down_kitchens = "<select name=\"kitchenName\" id=\"kitchenName\">";
			drop_down_kitchens += "<option value=\"0\">請輸入廚房名稱</option>";
			for (var i = 0; i < result_content.kitchenList.length; i++) {
				if (result_content.kitchenList[i].kitchenName == kitchenName) {
					drop_down_kitchens += "<option selected value=" + result_content.kitchenList[i].kitchenId + ">"
							+ result_content.kitchenList[i].kitchenName
							+ "</option>";
				} else {
					drop_down_kitchens += "<option value=" + result_content.kitchenList[i].kitchenId + ">"
							+ result_content.kitchenList[i].kitchenName
							+ "</option>";
				}
			}
			drop_down_kitchens += "</select>";
			$("#dropdown_kitchen").html("");
			$("#dropdown_kitchen").append(drop_down_kitchens);
			$("#kitchenName").combobox();
			$("#kitchenName").combobox().parent().find(".custom-combobox").find('input').css({"width":"100%","border-color":"#67c6a7"});

		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else {
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}

}
function roles(rtype) {
	var request_data = {
		"method" : "queryRolesList",
		"args" : {}
	};
	var response_obj = call_rest_api(request_data);

	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { //success
			var drop_down_roles = "<select name=\"role\" id=\"role\">";
			drop_down_roles += "<option value=\"0\">請選擇</option>";
			for (var i = 0; i < result_content.roleList.length; i++) {
				if (result_content.roleList[i].roleList == rtype) {
					drop_down_roles += "<option selected value=" + result_content.roleList[i].roleList + ">"
							+ result_content.roleList[i].roleList
							+ "</option>";
				} else {
					drop_down_roles += "<option value=" + result_content.roleList[i].roleList + ">"
							+ result_content.roleList[i].roleList
							+ "</option>";
				}
			}
			drop_down_roles += "</select>";
			$("#dropdown_role").append(drop_down_roles);

		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else {
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}

}
function updateUserDetail() {
	if (($("#email").val() == "") || ($("#userType").val() == "")
			|| ($("#uname").val() == "") || ($("#username").val() == "")
			) {
		alert("請輸入必要欄位!");
		return;
	}
	if( $("#password").val() != ""){
		if($("#password").val() != $("#passwordcheck").val()) {
			alert("密碼確認錯誤!");
			return;
		}
		if($("#ori_password").val() == ""){
			alert("請輸入目前密碼!");
			return;
		}
		
	}
	
	
	var request_data = {
		"method" : "updateUser",
		"args" : {
			"usertype" : $("#userType").val(),
			"name" : $("#uname").val(),
			"username" : $("#username").val(),
			"password" : $("#password").val(),
			"ori_password" : $("#ori_password").val(),
			"email" : $("#email").val(),
			"roleList" : $("#role").val(),
			"kitchenId" : $("#kitchenName").val()

		}
	};

	var response_obj = call_rest_api(request_data);
	//response
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { //success
			alert("帳戶修改成功");
			window.location.reload();
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else {
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}
function addUser() {
	if (($("#email").val() == "") || ($("#userType").val() == "")
			|| ($("#uname").val() == "") || ($("#username").val() == "")
			|| ($("#password").val() == "")|| ($("#passwordcheck").val() == "")
			) {
		alert("請輸入必要欄位!");
		return;
	}
	if ($("#password").val() != $("#passwordcheck").val()) {
		alert("新密碼確認錯誤!");
		return;
	}
	if($("#selectType").val()=="12#001" || $("#selectType").val()=="12#002"){
		if($("#dropCounties").val()=="0"){
			alert("請選擇縣市!");
			return;
		}
	}
	var request_data = {
		"method" : "addUser",
		"args" : {
			"usertype" : $("#userType").val(),
			"name" : $("#uname").val(),
			"username" : $("#username").val(),
			"password" : $("#password").val(),
			"email" : $("#email").val(),
			"kitchenId" : $("#kitchenName").val()

		}
	};

	var response_obj = call_rest_api(request_data);

	
	//response
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { //success
			alert("新增帳戶: "+$("#username").val()+" 成功");
			location.assign("../listUser/");
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else {
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}
/* function deleteUser(uName){
		var answer = confirm("請問是否要變更帳戶狀態？");
    	if (answer) {
    		//request
    		var request_data =	{
    							 "method":"deleteUser",
    				 				"args":{	
    				 					"username":uName
    					 			}
    							};
    		var response_obj = call_rest_api(request_data);
    		
    		if(response_obj.result == 1)
    		{
    			var result_content = response_obj.result_content;
    			if(result_content.resStatus == 1){	//success
    				
    				alert("變更帳戶狀態成功");
					window.location.reload();
    			} else {
    				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
    			}
    		} else{
    			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
    		}
    	} else {
    		return;
    	}

	} */
function updateUser(uName){
	var answer = confirm("請問是否要變更帳戶狀態？");
	if(answer){
		var request_data =	{
				 "method":"SwitchUserStatus",
	 				"args":{	
	 					"username":uName
		 			}
			};
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){
				alert("變更帳戶狀態成功");
				window.location.reload();
			}else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		}else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}else {
		return;
	}
}
function show_User(result_content)
{		
	var total_page = parseInt(result_content.userList.length / row);
	var add = result_content.userList.length % row;
	
	if(add > 0) total_page++;
	
	var element=document.getElementById("query_rule");
	element.innerHTML = "資料筆數：" + result_content.userList.length + " |共 "+total_page+"頁";;
	
	var new_table = "";
	
	var new_head = '<table class=\"table table-bordered table-striped\" width="100%" >';
	new_head += "<thead><tr>";
	new_head +=	"<td align=\"center\" valign=\"middle\">身分</td>" +
	"<td align=\"center\" valign=\"middle\">名稱</td>" +
	"<td align=\"center\" valign=\"middle\">帳號</td>" +
	//"<td valign=\"middle\">Email</td>" +
	"<td align=\"center\" valign=\"middle\">帳戶權限</td>" +
	"<td align=\"center\" valign=\"middle\">供應廚房</td>" +
	"<td align=\"center\" valign=\"middle\" width=\"100px\">選項</td>";
	new_head += 	"</tr></thead>";
	
	var divData = new Array;
	var index;
	for(var i = 0 ; i<result_content.userList.length; i++)
	{
		
		if(i %2 ==0)
		{
			new_table += "<tr>";
			new_table += "<td width=\"6%\" class=\"componentContentRightLine\">"+ result_content.userList[i].usertype +"&nbsp;</td>" +	
						 "<td width=\"28%\" class=\"componentContentRightLine\">"+ result_content.userList[i].name +"</td>" +
						 "<td width=\"15%\" class=\"componentContentRightLine\">"+ result_content.userList[i].username +"</td>" +
						//  "<td class=\"componentContentRightLine\">"+ result_content.userList[i].email +"</td>" +
						"<td width=\"9%\" class=\"componentContentRightLine\">";
						 new_table +='<option>'+ result_content.userList[i].usertypename +'</option>';
						 new_table +="</td>" +
						 "<td width=\"28%\" class=\"componentContentRightLine\">"+ result_content.userList[i].kitchenName +"</td>" +
						 "<td width=\"14%\" class=\"componentContentRightLine\">"+
						 "<a href=\"#\" data-toggle='tooltip' title='編輯' class='btn btn-primary' style='min-width:48%; margin:0; margin-right:1px' onclick=query_user_detail('"+result_content.userList[i].username+"') ><i class='fa fa-pencil'></i></a>"+
						 "<a href=\"#\" data-toggle='tooltip' title='啟(停)用' class='btn btn-primary' style='min-width:48%; margin:0' onclick=updateUser(\"" + result_content.userList[i].username +"\")>";
						 if(result_content.userList[i].enable == '1'){
							 new_table +="<i class='fa fa-times'></i></a></td>";
						 }else{
							 new_table +="<i class='fa fa-check'></i></a></td>";
						 };
			new_table += "</tr>";
		}
		else
		{
			new_table += "<tr>";
			new_table += "<td width=\"6%\" class=\"componentContentRightLine\">"+ result_content.userList[i].usertype +"&nbsp;</td>" +	
						 "<td width=\"28%\" class=\"componentContentRightLine\">"+ result_content.userList[i].name +"</td>" +
						 "<td width=\"15%\" class=\"componentContentRightLine\">"+ result_content.userList[i].username +"</td>" +
						// "<td class=\"componentContentRightLine\">"+ result_content.userList[i].email +"</td>" +
						 "<td width=\"9%\" class=\"componentContentRightLine\">";
						 new_table +='<option>'+ result_content.userList[i].usertypename +'</option>'; 
						 new_table +="</td>" +
						 "<td width=\"28%\" class=\"componentContentRightLine\">"+ result_content.userList[i].kitchenName +"</td>" +
						 "<td width=\"14%\" class=\"componentContentRightLine\"><a href=\"#\" data-toggle='tooltip' title='編輯' class='btn btn-primary' style='min-width:48%; margin:0; margin-right:1px' onclick=query_user_detail('"+result_content.userList[i].username+"') ><i class='fa fa-pencil'></i></a><a href=\"#\" data-toggle='tooltip' title='啟(停)用' class='btn btn-primary' style='min-width:48%; margin:0' onclick=updateUser(\"" + result_content.userList[i].username +"\")>";
						 if(result_content.userList[i].enable == '1'){
							 new_table +="<i class='fa fa-times'></i></a></td>";
						 }else{
							 new_table +="<i class='fa fa-check'></i></a></td>";
						 };
			new_table += "</tr>";
		}
		
		if((i+1) % row == 0){
			index = Math.floor((i+1) / row);
			divData[index] = new_head+ new_table+ "<table>";
			new_table = " "; 
		}
		if((i+1) == result_content.userList.length && result_content.userList.length % row != 0){
			divData[total_page] = new_head+ new_table+ "<table>";
			new_div = " ";
		}
	
	}
	if(result_content.userList.length>0){
		pagination(divData, total_page);		
	}else{
		alert("查無資料");
		return;
	}
	
}

function pagination(divData, total_page){
	$('#pagination').twbsPagination({
        totalPages: total_page,
        visiblePages: 7,
        onPageClick: function(e, p){
        	$("#User_list").html("");
    		$("#User_list").append(divData[p]);
        }
    });	
}
	
function query_User()
{
	$("#page").html(""+"<a href=\"#\" style=\"margin:0\" onclick=add_user_detail() >新增</a>");
	$("#User_list").html("");
	//clean data of the pagination plugin
	$('#pagination').empty();
	$('#pagination').removeData("twbs-pagination");
	$('#pagination').unbind("page");
	var request_data ; 
	// 查詢帳戶內容 : 起始頁查詢全部,另可透過帳戶名稱查詢
	if($("#queryuserName").val() == ""){
		request_data =	{
			"method":"queryUserList",
			"args":{
				"name": "all",
				 "type": $("#accountType").val()
			}
		};
	} else {
		request_data =	{
			"method":"queryUserList",
			"args":{
				"name": $("#queryuserName").val(),
				"type": $("#accountType").val()  
			}
		};
	}
	
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	//API呼叫正確
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1)
		{	
			show_User(result_content);
		}
		else
		{
			alert("查詢帳戶權限發生錯誤，訊息為：" + result_content.msg);
			return 0;
		}
	}
	else
	{
		alert("查詢帳戶權限發生無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		return 0;
	}
	
}
function query_User_Type(){
	var newOpt = "";
	var request_data = {
			"method":"QueryUserTypeList",
			"args":{
			}
		};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	//API呼叫正確
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1)
		{	
			newOpt+= "<option value= all>全部</option>";
			for(i=0;i<result_content.usertypelist.length;i++){
				newOpt+= "<option value="+result_content.usertypelist[i].id+">"+result_content.usertypelist[i].name+"</option>";
			};
			$("#accountType").append(newOpt);
		}
		else
		{
			alert("查詢帳戶類型發生錯誤");
			return 0;
		}
	}
	else
	{
		alert("查詢帳戶類型發生無法預期的錯誤");
		return 0;
	}
}
function query_Select_Type(selectType){
	var newOpt = "";
	var request_data = {
			"method":"QueryUserTypeList",
			"args":{
			}
		};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	//API呼叫正確
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1)
		{	
			for(i=0;i<result_content.usertypelist.length;i++){
				if(result_content.usertypelist[i].id == selectType){
					newOpt+= "<option selected value="+result_content.usertypelist[i].id+">"+result_content.usertypelist[i].name+"</option>";
				}else{
					newOpt+= "<option value="+result_content.usertypelist[i].id+">"+result_content.usertypelist[i].name+"</option>";
				}
			};
			$("#selectType").append(newOpt);
		}
		else
		{
			alert("查詢帳戶類型發生錯誤");
			return 0;
		}
	}
	else
	{
		alert("查詢帳戶類型發生無法預期的錯誤");
		return 0;
	}
}
function goBack(){
	$("#goBack").hide();
	$("#queryList").hide();
	$("#no_kitchen").hide();
	$("#User_list").show("slow");
	$("#query_rule").show("slow");
	$("#query_bar").show("slow");
	$("#page").show("slow");
	$("#pagination").show("slow");
}
$(document).ready(function(){	
	$("#queryList").hide();
	$("#goBack").hide();
	query_User();
	query_User_Type();
});
</script>
</head>
<body>
	<!-- 四點設計套版 -->
	<div class="contents-title">帳戶權限管理</div>
      <div class="contents-wrap">
      	<!-- 關鍵字搜尋 -->
			<div id="query_bar" style="font-size: medium;font-weight: bolder;">
					<tr>
						<td>帳戶名稱︰</td>
						<td><input type="text" id="queryuserName" name="queryuserName" class="searchInput max50"></td>
						&nbsp;&nbsp;
						<td>帳戶類型︰</td>
						<td>
						<select id="accountType" style="border: 1px solid #67c6a7;"></select>
						&nbsp;&nbsp;
						<button style="margin-top: 0px;" class="btn btn-primary" onclick="query_User()">查詢</button>
						</td>
					</tr>
			</div>
      	<h5 class="section-head with-border" style="height:35px;">
	      	<div id="query_rule" class="TITLE_TXT flo_l"></div>
			<div class="TITLE_TXT_BBT FL_R">
				<div id="page">
				</div>
				<div id="goBack"><a href="#" style='margin:0' onclick=goBack() >回前頁</a>
				</div>
			</div>
		</h5>
		<div align="right">
			<ul class="pagination-sm" id="pagination">
			</ul>
      	</div>
      	<div id="User_list" class="TAB_TY_D"></div>		
		<div id="queryList" class="TAB_TY_B"></div>  
      </div>
        
    <!-- 舊團膳套版 -->
    <div style="display:none;"> 
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
				<div class="TITLE_TXT flo_l ">帳戶權限管理</div>
				<div id="query_rule" class="TITLE_TXT flo_l"></div>
				<div class="TITLE_TXT_BBT FL_R">
					<div id="page">
					</div>
					<div id="goBack"><a href="#" onclick=goBack() >回前頁</a>
					</div>
				</div>
		</div>	
		<div id="User_list" class="TAB_TY_D"></div>		
		<div id="queryList" class="TAB_TY_B"></div>	
	</div>
</body>
</html>