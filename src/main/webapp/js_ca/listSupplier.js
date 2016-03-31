var MSG = new MsgsProcessing();
var util = new CateringServiceUtil();
var PAGE_TABLE = "resultTable";
var row = 20; // 每頁筆數
var crnPage = 1;// 當前頁數

// 初始化
$(document).ready(function() {
	querySupplier(crnPage);
});

// 查詢供應商按鈕事件
function searchSupplierClick() {
	querySupplier(crnPage);
}

// 顯示Excel上傳區塊
function excelUpload() {
	$("#excel_upload").show("slow");
}

function packageApiResult(response_obj){
	var tmp = {};
	var rtnResponse_obj = {};
	if(response_obj.result == 1){
		tmp= {
			"resStatus":1,
			"msg":"",
			"supplierList":[],
			"totalNum": 0
		};			
		
		if(typeof(response_obj.data.length) != 'undefined'){
			for(var i in response_obj.data){
				if(response_obj.data[i].Suppliers){
					for(var j in response_obj.data[i].Suppliers){
						var item = response_obj.data[i].Suppliers[j];
						tmp['supplierList'].push({
							"id":{
								"supplierId":item.SupplierId,
								"kitchenId": response_obj.data[i].RestaurantId
							},
							"supplierName":item.SupplierName,
							"ownner":item.Owner,
							"companyId":item.CompanyId,
							"countyId":item.CountyId,
							"areaId":item.AreaId,
							"supplierAdress":item.SupplierAddress,
							"supplierTel":item.SupplierTel,
							"supplierCertification":item.SupplierCertification,
							"supplierId":item.SupplierId
						});
						tmp["totalNum"] += 1;
					}
				}
			}
		}else{
			var item = response_obj.data;
			tmp['supplierBo'] = {				
				"supplierName":item.SupplierName,
				"ownner":item.Owner,
				"companyId":item.CompanyId,
				"countyId":item.CountyId,
				"areaId":item.AreaId,
				"supplierAdress":item.SupplierAddress,
				"supplierTel":item.SupplierTel,
				"address":item.SupplierAddress,
				"tel":item.SupplierTel,
				"supplierCertification":item.SupplierCertification,
				"supplierId":item.SupplierId,
				"kitchenId": $("#__uRestrantList").val()
			};
			tmp["totalNum"] += 1;
		}
		rtnResponse_obj = {
			result: response_obj.result,
			result_content: tmp
		}
		
	}
	console.log(rtnResponse_obj);
	return rtnResponse_obj;
}

// 查詢供應商事件
function querySupplier(page) {
	crnPage = page;
	var supplierName = $('#searchName').val();
	var response_obj;
	
	if(["103"].indexOf($("#_uType").val()) != -1){
		//餐廳時, 多傳餐廳ID
		var _url = url + "/supplier/?RestaurantId=" + $("#_uRestrantList").val() + "&SupplierName=" + $('#supplierName').val();
		var method = "GET";
		console.log(_url);			
		var request_data = {};	
		console.log(request_data);
		response_obj = call_rest_api(request_data,_url,method);
		console.log(response_obj);	
		
		response_obj = packageApiResult(response_obj);
	}else{
		var request_data = {
			"method" : "querySupplierList",
			"args" : {
				"pageNum" : crnPage,// 當前第X頁
				"pageLimit" : row,
				"supplierName" : supplierName
			}
		};
		response_obj = call_rest_api(request_data);
	}
	
	if (response_obj.result == 1) { // API呼叫正確
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) {
			showSupplier(result_content, [ "供應商統編", "供應商名稱", "地址", "負責人姓名", "連絡電話", "選項" ], PAGE_TABLE);
		} else {
			MSG.alertMsgs("check", "查詢發生錯誤，訊息為：" + result_content.msg, 0);
			return 0;
		}
	} else {
		MSG.alertMsgs("check", "查詢發生無法預期的錯誤，錯誤訊息為：" + response_obj.msg, 0);
		return 0;
	}

}

// 組供應商列表 table
function showSupplier(result_content, header, tableName) {
	// 清單內容
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<thead><tr>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr></thead>";

	var supplierList = result_content.supplierList;
	for ( var i in supplierList) {
		var strTR = "<tr>";
		strTR += "<td width=\"12%\">" + supplierList[i].companyId + "</td>";
		strTR += "<td width=\"20%\">" + supplierList[i].supplierName + "</td>";
		strTR += "<td width=\"28%\">" + supplierList[i].supplierAdress + "</td>";
		strTR += "<td width=\"12%\">" + supplierList[i].ownner + "</td>";
		strTR += "<td width=\"13%\">" + supplierList[i].supplierTel + "</td>";
		strTR += "<td width=\"20%\"><button class=\"btn btn-primary\" style=\"min-width:48%\" onclick=editSupplierDetail(" + supplierList[i].id.supplierId + ")><i class=\"fa fa-pencil\"></i></button> ";
		strTR += "<button class=\"btn btn-primary\" style=\"min-width:48%\" onclick=removeSupplier(" + supplierList[i].id.supplierId + ")><i class=\"fa fa-trash-o\"></i></button></td>";
		strTR += "</tr>";
		html += strTR;
	}

	$("#" + tableName).html(html);
	$("#" + tableName).hide();
	$("#" + tableName).show("slow");

	// 資料筆數
	$("#query_rule").html("");
	$("#query_rule").html("資料筆數:" + result_content.totalNum);
	// 頁數管理
	var pageHtml = "";
	pageHtml += "第  " + crnPage + " 頁 | ";
	// 總共頁數
	var totalPage = 0; // 分頁若整除，不需+1
	if (result_content.totalNum % row == 0) {
		totalPage = parseInt(result_content.totalNum / row, 10);
	} else {
		totalPage = parseInt(result_content.totalNum / row, 10) + 1;
	}
	pageHtml += "共 " + totalPage + " 頁 | ";
	// 頁面計算
	pageHtml += '前往<select style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="querySupplier(this.value)"> |';
	for (var j = 1; j <= totalPage; j++) {
		if (j == crnPage) {
			pageHtml += '<option selected value="' + j + '">' + j + '</option>';
		} else {
			pageHtml += '<option value="' + j + '">' + j + '</option>';
		}
	}
	pageHtml += '</select>';
	$("#page").html("");
	$("#page").html(pageHtml);
}

// 新增suuplier
function addSupplier() {
	$('#listForm').hide();
	$('#detailForm').show("slow");
	$('#detailForm :input:not(:button)').val('');
	$('#companyId').attr('readonly', false);
	//bindCounty(0);
}

// 回上頁
function goBack() {
	$('#detailForm :input:not(:button)').val('');
	$('#detailForm').hide();
	$('#listForm').show("slow");
	$('#companyId').css({"color":""});
}

// 載入select縣市
function bindCounty(cid) {
	var request_data = {
		"method" : "customerQueryCounties",
		"args" : {
			"condition" : 1
		}
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { // success
			for (var i = 0; i < result_content.counties.length; i++) {
				var value = result_content.counties[i].cid;
				var text = result_content.counties[i].countiesName;
				$("#county").append(new Option(text, value));
			}
			$("#county").change(function(e) {
				bindArea();
			});
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else {
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}

// 載入select 市/區別
function bindArea() {
	var counties = $("#county").val();
	var request_data = {
		"method" : "customerQueryArea",
		"args" : {
			"cid" : counties
		}
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { // success
			// 清空option
			$("#area").find('option').remove();
			$("#area").append(new Option("請選擇", 0));
			for (var i = 0; i < result_content.area.length; i++) {
				var value = result_content.area[i].aid;
				var text = result_content.area[i].areaName;
				$("#area").append(new Option(text, value));
			}
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else {
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}

// 儲存Supplier detail 資料
function saveSupplierDetail() {
	if(checkSpecialCharacters()){
		return;
	}
	var msg = validateInput();
	if(msg){
		MSG.alertMsgs("check", msg, 0);
		return;
	}
	
	var mode = '';
	if ($('#supplierId').val())
		mode = 'UPDATE';
	else
		mode = 'ADD';
	
	//20151023 shine add start 同步更新其下餐廳的供應商
	var suppliedType = "0";
	
	if(["101","102","103"].indexOf($("#_uType").val()) != -1){
		var _url = url + "/supplier/" + (mode=="UPDATE"?$('#supplierId').val():"");
		var method = (mode=="ADD")?"POST":"PUT";
		suppliedType = "1"; 
		console.log(_url);
		
		var request_data = {
			"action":"updateDown",
			"username": $("#_uName").val(),
			"CompanyId" : $('#companyId').val(),
			"SupplierName" : $('#supplierName').val(),
			"Owner" : $('#ownner').val(),
			"CountyId" : 0,
			"AreaId" : 0,
			"SupplierAddress" : $('#address').val(),
			"SupplierTel" : $('#tel').val()				
		};	
		console.log(request_data);
		response_obj = call_rest_api(request_data,_url,method);
		console.log(response_obj);		
		
		//餐廳的話就不用呼叫原本API更新
		if($("#_uType").val() == "103"){
			if (response_obj.result == 1) { // success
				//儲存帳號以下餐廳的供應商
				MSG.alertMsgs("checkAndReload", "儲存成功", 0);
			} else {
				MSG.alertMsgs("check", "儲存失敗，訊息為：" + result_content.msg, 0);
			}
			return;
		}
	}
	//20151023 shine add end 同步更新其下餐廳的供應商
	
	var request_data = {
		"method" : "updateSupplierDetail",
		"args" : {
			"activeType" : mode,
			"supplierBo" : {
				"supplierId" : $('#supplierId').val(),
				"companyId" : $('#companyId').val(),
				"supplierName" : $('#supplierName').val(),
				"ownner" : $('#ownner').val(),
//				"countyId" : $('#county').val(),
//				"areaId" : $('#area').val(),
				"countyId" : 0,
				"areaId" : 0,
				"address" : $('#address').val(),
				"tel" : $('#tel').val(),
				"suppliedType": suppliedType
			}
		}
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { // success
			//儲存帳號以下餐廳的供應商
			MSG.alertMsgs("checkAndReload", "儲存成功", 0);
		} else {
			MSG.alertMsgs("check", "儲存失敗，訊息為：" + result_content.msg, 0);
		}
	} else {
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	}
}

/*function saveSupplierDetail_origin() {
	if(checkSpecialCharacters()){
		return;
	}
	var msg = validateInput();
	if(msg){
		MSG.alertMsgs("check", msg, 0);
		return;
	}
	
	var mode = '';
	if ($('#supplierId').val())
		mode = 'UPDATE';
	else
		mode = 'ADD';

	var request_data = {
		"method" : "updateSupplierDetail",
		"args" : {
			"activeType" : mode,
			"supplierBo" : {
				"supplierId" : $('#supplierId').val(),
				"companyId" : $('#companyId').val(),
				"supplierName" : $('#supplierName').val(),
				"ownner" : $('#ownner').val(),
//				"countyId" : $('#county').val(),
//				"areaId" : $('#area').val(),
				"countyId" : 0,
				"areaId" : 0,
				"address" : $('#address').val(),
				"tel" : $('#tel').val()
			}
		}
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { // success
			MSG.alertMsgs("checkAndReload", "儲存成功", 0);
		} else {
			MSG.alertMsgs("check", "儲存失敗，訊息為：" + result_content.msg, 0);
		}
	} else {
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	}
}*/

// Query supplier detail
function querySupplierDetail(supplierId) {
	var response_obj
	//將餐廳進行特別處理
	if(["103"].indexOf($("#_uType").val()) != -1){
		var _url = url + "/supplier/" + supplierId;
		var method = "GET";
		console.log(_url);			
		var request_data = {};	
		console.log(request_data);
		response_obj = call_rest_api(request_data,_url,method);
		console.log(response_obj);	
		
		response_obj = packageApiResult(response_obj);
	}else{
		var request_data = {
			"method" : "querySupplierDetail",
			"args" : {
				"supplierId" : supplierId
			}
		};
		response_obj = call_rest_api(request_data);
	}
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { // success
			var supplierBo = result_content.supplierBo;
			if (supplierBo)
				assignInputValue(supplierBo);

		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
		}
	} else {
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	}
}

//塞入值給input
function assignInputValue(supplierBo) {
	$('#supplierId').val(supplierBo.supplierId);
	$('#companyId').val(supplierBo.companyId);
	$('#companyId').css({"color":"#888"});
	$('#supplierName').val(supplierBo.supplierName);
	$('#ownner').val(supplierBo.ownner);
//	$('#county').val(supplierBo.countyId);
//	bindArea(supplierBo.countyId);
//	$('#area').val(supplierBo.areaId);
	$('#address').val(supplierBo.address);
	$('#tel').val(supplierBo.tel);
}

//編輯Supplier Detail
function editSupplierDetail(supplierId){
	$('#listForm').hide();
	$('#detailForm').show("slow");
	$('#companyId').attr('readonly', true);
//	bindCounty(0);
	querySupplierDetail(supplierId);
}

//刪除Supplier
function removeSupplier(supplierId){
	var answer = confirm("請問是否要刪除供應商？");
	if(!answer)
		return;
	
	var request_data = {
			"method" : "deleteSupplier",
			"args" : {
				"supplierId" : supplierId
			}
		};
	
		//20151023 shine add start 同步更新其下餐廳的供應商
		if(["101","102","103"].indexOf($("#_uType").val()) != -1){
			var _url = url + "/supplier/" + supplierId;
			var method = "DELETE";
			console.log(_url);			
			var data = {
				"action":"updateDown",
				"username": $("#_uName").val(),
				"CompanyId" : $('#companyId').val(),
				"SupplierName" : $('#supplierName').val(),
				"Owner" : $('#ownner').val(),
				"CountyId" : 0,
				"AreaId" : 0,
				"SupplierAddress" : $('#address').val(),
				"SupplierTel" : $('#tel').val()				
			};	
			console.log(data);
			response_obj = call_rest_api(data,_url,method);
			console.log(response_obj);	
			
			//餐廳的話就不用呼叫原本API更新
			if($("#_uType").val() == "103"){
				if (response_obj.result == 1) { // success					
					MSG.alertMsgs("checkAndReload", "刪除成功", 0);
				} else {
					MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
				}
				return;
			}
		}
		//20151023 shine add end 同步更新其下餐廳的供應商
	
		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { // success							
				
				MSG.alertMsgs("checkAndReload", "刪除成功", 0);
			} else {
				MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			}
		} else {
			MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		}
}

//excel upload
var client = new XMLHttpRequest();
function upload(){
	//為大專身份時, 多傳其帳號下的餐廳ID,好一起塞供應商
	var restaurantIdList = "";
	var suppliedType = "0";
	if($("#_uType").val() == "101" || $("#_uType").val() == "102" || $("#_uType").val() == "103" ){
		var userName = $("#_uName").val();
		var _url = url + "/getRestaurantByUser/" + userName;
		var method = "GET";
		var request_data = {};
		suppliedType = "1";
		var response_obj = call_rest_api(request_data,_url,method);
		var itemList = response_obj.data;
		
		for(var i in itemList){
			if(restaurantIdList == "") 
				restaurantIdList = itemList[i].RestaurantId;
			else
				restaurantIdList += "," + itemList[i].RestaurantId ;
		}
		console.log(restaurantIdList);
	}
	var file = document.getElementById("file");
	if(!$("#file").val()){
		MSG.alertMsgs('check', '請選擇檔案', 0);
		return false;
	}/* Create a FormData instance */
	var formData = new FormData();
		  MSG.alertMsgs('loading', '', 0);
	/* Add the file */ 
	formData.append("file", file.files[0]);
	formData.append("func", "supplier" + (restaurantIdList==""?"":"|"+ suppliedType + "|" + restaurantIdList));
	formData.append("overWrite", "0");
	client.open("post","/cateringservice/file/upload", true);
	client.send(formData);  /* Send to server */ 
}
   
/* Check the response status */  
client.onreadystatechange  = function() {
	if (client.readyState == 4 && client.status == 200) {
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
};

//Excel下載
function exportExcel(){
	var link = "/cateringservice/rest/API/XLS/supplier";
	window.open(link,"_blank");
}

//檢核必填欄位空值
function validateInput(){
	var msg = '';

	if($('#companyId').val() === '')
		msg += '請輸入供應商統編</br>';
	
	if($('#supplierName').val() === '')
		msg += '請輸入供應商名稱</br>';
	
	if($('#ownner').val() === '')
		msg += '請輸入負責人姓名</br>';
	
//	if($('#county').val() === '0')
//		msg += '請選擇縣市別</br>';
//	
//	if($('#area').val() === '0')
//		msg += '請選擇市/區別</br>';
	
	if($('#address').val() === '')
		msg += '請輸入地址</br>';
	
	if($('#tel').val() === '')
		msg += '請輸入連絡電話</br>';
	
	return msg;
}

function checkSpecialCharacters(){
	var inputFields = $("table[id='table']").find("input[type=text]");
	var inputFieldsCount = $("table[id='table']").find("input[type=text]").length;
	for(i = 0; i < inputFieldsCount ; i++){
		if(!isValidStr(inputFields[i].value)){
			alert('欄位名稱不可包含特殊字元 \" > < \' % ; &');
			return true;
		}
	}
}

//add by Joshua 2014/11/10 特殊字元判斷
function isValidStr(str){
	return !/[%&\[\]\\';|\\"<>]/g.test(str);
}

