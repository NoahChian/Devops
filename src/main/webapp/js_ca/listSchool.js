
var MSG= new MsgsProcessing();
var util=new CateringServiceUtil();
var PAGE_TABLE="resultTable";
var row = 50; //每頁筆數
var crnPage = 1;//當前頁數
$(document).ready(function(){	
	query_School(crnPage);
});

function query_School(page)
{
	// #13522 學校管理介面增加可建立大專院校學校資料
	var queryMode = "";
//	var queryMode = "exceptUniversity";
	$("#query_rule").parent().show();
	crnPage = page;
	var request_data =	{
							"method":"querySchoolList",
							"args":{
								"cid":0, //由API控制送出的權限判定
								"page":crnPage ,// 當前第X頁
								"perpage":row,
								"queryschoolname":$("#queryschoolName").val(),
								"queryMode" : queryMode
							}
						};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	//API呼叫正確
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1)
		{	
			show_School(result_content, ["學校編號","學校名稱","選項"], PAGE_TABLE);
		}
		else
		{
			MSG.alertMsgs("check", "查詢發生錯誤，訊息為：" + result_content.msg, 0);
			return 0;
		}
	}
	else
	{
			MSG.alertMsgs("check", "查詢發生無法預期的錯誤，錯誤訊息為：" + result_content.msg, 0);
		return 0;
	}
	
}
function show_School(result_content,header,tableName)
{
//清單內容
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<thead><tr>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr></thead>";
	for(var i in result_content.school) {
		
		var enable = "";//啟用
		if(result_content.school[i].enable == "1"){
		enable ="啟用";
		}else {enable ="停用";}
		var strTR = "<tr>";
		strTR += "<td>" + result_content.school[i].schoolCode  + "</td>";
		strTR += "<td>" + result_content.school[i].schoolName  + "</td>";
		strTR += "<td width='20%'><button class='btn btn-primary' style='min-width:48%; margin:0' onclick=show_Sch_detail(" + result_content.school[i].sid +")><i class='fa fa-pencil'></i></button> ";
		if("啟用" == enable){
			strTR +=  "<button class='btn btn-primary' style='min-width:48%; margin:0' onclick=deleteSchool(" + result_content.school[i].sid +")><i class='fa fa-times'></i></button></td>";
		} else {
			strTR +=  "<button class='btn btn-primary' style='min-width:48%; margin:0' onclick=enableSchool(" + result_content.school[i].sid +")><i class='fa fa-check'></i></button></td>";
		}
		strTR += "</tr>";
		html += strTR;
	}
	$("#" + tableName).html(html);
	$("#" + tableName).hide();
	$("#" + tableName).show("slow");

//資料筆數
	$("#query_rule").html("");
	$("#query_rule").html("資料筆數:" +result_content.totalCol );
//頁數管理	
	var pageHtml = "";
	pageHtml  += "第  "+ crnPage +" 頁 | ";
//總共頁數
var totalPage  = 0; //分頁若整除，不需+1
if(result_content.totalCol % row == 0){
totalPage = parseInt(result_content.totalCol / row ,10);
}else{
	totalPage = parseInt(result_content.totalCol / row ,10)+1;}
	pageHtml  += "共 "+ totalPage +" 頁 | ";
//頁面計算
	pageHtml += '前往<select style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="query_School(this.value)"> |';
		for(var j = 1; j<= totalPage ; j++) {
		if(j ==crnPage){
		pageHtml += '<option selected value="'+ j +'">'+ j +'</option>';
		}else{
		pageHtml += '<option value="'+ j +'">'+ j +'</option>';}
		}
	pageHtml += '</select>';
	$("#page").html("");
	$("#page").html(pageHtml);
}

function excel_Upload(){
//$("#tiltleAct").hide();
$("#excel_upload").show("slow");
}
//excel upload
var client = new XMLHttpRequest();
function upload(){
	var file = document.getElementById("file");
	if(!$("#file").val()){
		MSG.alertMsgs('check', '請選擇檔案', 0);
		return false;
	}/* Create a FormData instance */
	var formData = new FormData();
		  MSG.alertMsgs('loading', '', 0);
	/* Add the file */ 
	formData.append("file", file.files[0]);
	//formData.append("func", $( "#func" ).val());
	//formData.append("overWrite", $("#overWrite").val());
	formData.append("func", "schoolkitchenaccount");
	formData.append("overWrite", "0");
	client.open("post","/cateringservice/file/upload", true);
	client.send(formData);  /* Send to server */ 
}
   
/* Check the response status */  
client.onreadystatechange  = function() {
	if (client.readyState == 4 && client.status == 200) {

		//alert(client.responseText);
		
		var obj = JSON.parse(client.responseText);

		 
		if(obj.retStatus == 1){
			if (obj.retMsg==""){
				   MSG.alertMsgs('checkAndReload', '檔案上傳成功', 0);
			}else{
				MSG.alertMsgs('checkAndReload', obj.retMsg, 0);
			}
			 $("#file").val("");
			 $("#overWrite").attr("value", 0);
		} else if (obj.retStatus == 0) {
			   MSG.alertMsgs('check', '檔案上傳失敗，原因為' + obj.retMsg , 0);
			 $("#file").val("");
			 $("#overWrite").attr("value", 0);
		} 
	}
	
	$("#excel_upload").hide("slow");
}
//顯示詳細資料
function show_Sch_detail(action) {
	clean_allinput();
	$("#query_rule").parent().hide();
	$('html, body').scrollTop(0); //畫面移到最上面
	
	if(action == "add"){
		addOrNot();
		$("#addAccount").parent().parent().show();
		$("#query_rule").hide();
		$("#resultTable").hide();
		$("#tiltleAct").hide();
		$("#query_bar").hide();
		$("#page").hide();
		//$("#update_school").hide();
		//顯示新增/更新
		$("#pw_titil").text("密碼*");
		$("#add_pw").show();
		$("#upd_pw").hide();
		$("#ori_pass").hide();
		$("#add_school_button").show();
		$("#update_school_button").hide();
		$("#school_detail").show();
		$("#goBack").show();
		var cid = 0;	
		var aid = 0;
		city(cid);	//drop down counties 不給預設值
		area(aid); }
	else{
		addOrNot();
		$("#addAccount").parent().parent().hide();
		var sid = action;
		var request_data =	{
								"method":"querySchKitchenUserDetail",
								"args":{
									"schoolId":sid
								}
							};
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1)
		{	//API呼叫正確
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1)
			{	
				update_Sch_detail(result_content);
			}
			else
			{
				MSG.alertMsgs("check", "查詢學校資料發生錯誤，訊息為：" + result_content.msg, 0);
				return 0;
			}
		}
		else
		{
				MSG.alertMsgs("check", "查詢學校資料發生無法預期的錯誤，錯誤訊息為：" + result_content.msg, 0);
			return 0;
		}
	}
}
function update_Sch_detail(result_content){
		$("#query_rule").hide();
		$("#resultTable").hide();
		$("#tiltleAct").hide();
		$("#query_bar").hide();
		$("#page").hide();
	//顯示新增/更新
	$("#pw_titil").text("密碼");
	$("#add_pw").hide();
	$("#upd_pw").show();
	$("#add_school_button").hide();
	$("#update_school_button").show();
	$("#school_detail").show();
	$("#goBack").show();
	
	$("#sid").val(result_content.schoolId) ;
	$("#schoolId").val(result_content.schoolCode) ;
	$("#schoolName").val(result_content.schoolName);
	$("#email").val(result_content.contents.email);
	city(result_content.countyId);	//drop down counties
	area(result_content.areaId);
	//帳號型態
	$("#sch_type").val(result_content.contents.type);
	//是否啟用
	$("#sch_Enable").val(result_content.enable);
	
	//如果不是大專大學就顯示區域吧 顆顆
	if(result_content.schoolCode.length >= 6){
		setCountyArea();
	}
	$("#schoolId").prop('readonly', true);
}

function updateSchool() {
	if (($("#schoolName").val() == "")) {
		MSG.alertMsgs("check", "請輸入必要欄位 : 學校名稱", 0);
		return;
	}
	if (($("#schoolId").val() == "")) {
		MSG.alertMsgs("check", "請輸入必要欄位 : 學校編號", 0);
		return;
	}
	if (($("#Area").val() == "0")) {
		MSG.alertMsgs("check", "請輸入必要欄位 : 市/區別", 0);
		return;
	}
	if (($("#City").val() == "0")) {
		MSG.alertMsgs("check", "請輸入必要欄位 : 縣市別", 0);
		return;
	}
	// 畫面上並沒有密碼欄位,不進行密碼驗證
//	if( $("#upd_password").val() != ""){
//		if($("#upd_password").val() != $("#passwordcheck").val()) {
//			MSG.alertMsgs("check", "密碼確認錯誤!", 0);
//			return;
//		}
//		if($("#ori_password").val() == ""){
//			MSG.alertMsgs("check", "請輸入目前密碼!", 0);
//			return;
//		}
//		
//	}
	
	var emailVal = $("#email").val();
	if ($("#email").val().length > 0) {
		if ("N/A" == $("#email").val()) {
			emailVal = "";
		} else {
			// 畫面上並沒有EMail欄位,不進行E-Mail驗證
//			if (!validateEmail($("#email").val())) {
//				MSG.alertMsgs("check", "E-Mail格式錯誤!", 0);
//				return false;
//			}
		}
	}
		
	var companyId = $("#schoolId").val();
	var schoolName = "";
	if($("#schoolId").val().length >= 6 || $("#sch_type").val() == "009"){
		schoolName += $('#City option:selected').text();
		schoolName += $('#Area option:selected').text();
	}
	schoolName += $("#schoolName").val();
	
	var sch_type = $("#sch_type").val();
	
	var request_data = {
		"method" : "updateSchKitchenUser",//updateSchool
		"args": {
        "countyId": $('#City').val(),
        "areaId": $('#Area').val(),
        "schoolId": $("#sid").val(),
        "schoolCode": $("#schoolId").val(),
      //  "CompanyId": companyId,
        "schoolName": schoolName,
        "old_password": $("#ori_password").val(),
        "new_password": $("#upd_password").val(),
		"enable" : $("#sch_Enable").val(),
        "contents":{
                "email": emailVal,
                "role": "kSch",
                "type": $("#sch_type").val()
            },
        "addAccount":document.getElementById("addAccount").checked
	}};

	var response_obj = call_rest_api(request_data);

	//response
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { //success
			var username = result_content.userName;
			if(sch_type=="009"){
				username += "-SHOP";
			}
			var check = document.getElementById("addAccount").checked;
			if(check){
				var checkMsg = '修改資料成功<table  border = "1" width="100%" bgcolor="#FFF"><tr><td>學校名稱</td><td>'+ result_content.schoolName + '</td></tr><tr><td>學校帳號<br>縣市+編號</td><td>'+ username +'</td></tr></table><br>';
			} else {
				var checkMsg = '修改資料成功<table  border = "1" width="100%" bgcolor="#FFF"><tr><td>學校名稱</td><td>'+ result_content.schoolName + '</td></tr></table><br>';
			}
			clean_allinput();
			MSG.alertMsgs("checkAndReload", checkMsg, 0);
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else {
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}	
}
var drop_down_default = "<select name=\"City\" id=\"City\"><option value=\"0\">請選擇</option></select>";
//縣市
function city(cid){
	var request_data =	{
			 "method":"queryCounties",
				"args":{
					"condition":0
				}
			};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			var drop_down_counties = "<select name=\"City\" id=\"City\" onChange=\"area()\">";
				for(var i=0; i<result_content.counties.length; i++) {
					if(cid!=0 && result_content.counties[i].cid==cid){
					drop_down_counties += "<option selected value=" + result_content.counties[i].cid + ">" + result_content.counties[i].countiesName + "</option>";
					}
					drop_down_counties += "<option value=" + result_content.counties[i].cid + ">" + result_content.counties[i].countiesName + "</option>";
				}
			drop_down_counties += "</select>";
			$("#dropdown_City").html("");
			$("#dropdown_City").append(drop_down_counties);
			$("#dropdown_Area").append(drop_down_default);
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}

}
//縣市改變修改區域
function area(aid) {
	var counties = $("#City").val();
	var request_data =	{
			 "method":"customerQueryArea",
				"args":{
					"cid" : counties
				}
			};
	var response_obj = call_rest_api(request_data);
	
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			var drop_down_area = "<select name=\"Area\" id=\"Area\" onchange='setCountyArea()'>";
				drop_down_area += "<option value=\"0\">請選擇</option>";
				for(var i=0; i<result_content.area.length; i++) {
					if(aid!=0 && result_content.area[i].aid == aid){
					drop_down_area += "<option selected value=" +  result_content.area[i].aid + ">" + result_content.area[i].areaName + "</option>";
					}else{
					drop_down_area += "<option value=" +  result_content.area[i].aid + ">" + result_content.area[i].areaName + "</option>";}
				}
			drop_down_area += "</select>";
			//清空
			$("#dropdown_Area").html("");				
			$("#dropdown_Area").append(drop_down_area);
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}
function setCountyArea(){
	var varCountyArea = "";
	varCountyArea += $('#City option:selected').text();
	varCountyArea += $('#Area option:selected').text();
	$("#schCountyArea").html("");$("#schCountyArea").append(varCountyArea);
} 
function addSchool() {
	if (($("#schoolName").val() == "") || (document.getElementById("addAccount").checked 
			&& $("#add_password").val() == "") || ($("#schoolId").val() == "") 
			|| ($("#Area").val() == "0") || ($("#City").val() == "0")
			) {
		MSG.alertMsgs("check", "請輸入必要欄位!", 0);
		return;
	} else {
		// #13522 學校編號要限制只能4碼或6碼的數字
		var schoolIdLength = $("#schoolId").val().length;
		if (schoolIdLength == 4 || schoolIdLength == 6) {} else {
			MSG.alertMsgs("check", "學校編號需為4碼或6碼!", 0);
			return;
		}
	var companyId = $("#schoolId").val();
	var schoolName = "";
	if($("#schoolId").val().length >= 6 || $("#sch_type").val() == "009"){
		schoolName += $('#City option:selected').text();
		schoolName += $('#Area option:selected').text();
	}
	schoolName += $("#schoolName").val();
	
	var sch_type = $("#sch_type").val();
	var role = "";
	if (document.getElementById("addAccount").checked) {
		switch(sch_type){
			case "006":
				role = "kSch";
				break;
			case "007":
				role = "kSch";
				break;
			case "009":
				role = "kSHOP";
				break;
			case "101":
				role = "101";
				break;
		}
	}
	
	var request_data = {
		"method" : "addSchKitchenUser",//addSchool
		"args": {
        "countyId": $('#City').val(),
        "areaId": $('#Area').val(),
        "schoolCode": $("#schoolId").val(),
      //  "CompanyId": companyId,
        "schoolName": schoolName,
        "password": $("#add_password").val(),
		"enable" : $("#sch_Enable").val(),
        "contents":{
                "email": $("#email").val(),
                "role": role,
                "type": sch_type
            },
        "addAccount":document.getElementById("addAccount").checked
	}};

	var response_obj = call_rest_api(request_data);

	//response
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { //success
		var username = result_content.userName;
		if(sch_type=="009"){
			username += "-SHOP";
		}
		var checkMsg = '新增資料成功<table  border = "1" width="100%" bgcolor="#FFF"><tr><td>學校名稱</td><td>'+ result_content.schoolName + '</td></tr><tr><td>學校帳號<br>縣市+編號</td><td>'+username +'</td></tr></table><br>';
			clean_allinput();
			MSG.alertMsgs("checkAndReload", checkMsg, 0);
		//location.assign("../listSchool/");
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else {
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}	
	}
}
function deleteSchool(sid){
		var answer = confirm("請問是否要停用？");
    	if (answer) {
    		//request
    		var request_data =	{
    							 "method":"deleteSchKitchenUser",
    				 				"args":{	
    				 					"schoolId":sid
    					 			}
    							};
    		var response_obj = call_rest_api(request_data);
    		
    		if(response_obj.result == 1)
    		{
    			var result_content = response_obj.result_content;
    			if(result_content.resStatus == 1){	//success
    				MSG.alertMsgs("checkAndReload", "帳號已停用", 0);
    				//alert("刪除成功");
					//window.location.reload();
    			} else {
    				MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
    				//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
    			}
    		} else{
    			MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
    			//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
    		}
    	} else {
    		return;
    	}

}
/**
 * 啟用學校
 * @param sid school id
 */
function enableSchool(sid) {
	var answer = confirm("請問是否要啟用？");
	if (answer) {
		//request
		var request_data = {
			"method": "deleteSchKitchenUser",
			"args": {
				"schoolId": sid
			}
		};
		var response_obj = call_rest_api(request_data);

		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { //success
				MSG.alertMsgs("checkAndReload", "帳號已啟用", 0);
			} else {
				MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			}
		} else {
			MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		}
	} else {
		return;
	}
}
function goBack(){
	$("#goBack").hide();
	$("#school_detail").hide();
	$("#resultTable").show("slow");
	$("#query_rule").show("slow");
	$("#tiltleAct").show("slow");
	$("#query_bar").show("slow");
	$("#page").show("slow");
	clean_allinput();
	
	$("#query_rule").parent().show();
}
function changePassword(){
$("#ori_pass").show();

}
//檢核email格式
function validateEmail(sEmail) {
	var filter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
	if (filter.test(sEmail)) {
		return true;
	}
	else {
		return false;
	}
}
function clean_allinput(){
//切換新增/修改需要清空區域再重新塞
$("#school_detail input[type=text]").val(''); 
$("#school_detail input[type=password]").val('');
$("#school_detail input[type=select]").val('');
$("#sch_type").val('006'); //預設自設廚房
$("#sch_Enable").val('1'); //預設啟用
$("#schCountyArea").html(""); //清空系統自動asign的學校-區域名稱
$("#schoolId").prop('readonly', false);
document.getElementById("addAccount").checked =false;
}

function addOrNot(){
	var check = document.getElementById("addAccount").checked;
	if(check){
		$("#sch_type").parent().parent().show();
		$("#upd_password").parent().parent().show();
	} else {
		$("#sch_type").parent().parent().hide();
		$("#upd_password").parent().parent().hide();
	}
}
