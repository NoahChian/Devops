var MSG = new MsgsProcessing();
var util = new CateringServiceUtil();
var PAGE_TABLE = "resultTable";
var row = 20; // 每頁筆數
var crnPage = 1;// 當前頁數

// 初始化
$(document).ready(function() {
	querySeasoning(crnPage);
	defaultYearMonth();//將查詢時間預設為當月的開始日與結束日
});

// 查詢按鈕事件
function searchSeasoningClick() {
	querySeasoning(crnPage);
}

// 顯示Excel上傳區塊
function excelUpload() {
	$("#excel_upload").show("slow");
}
function defaultYearMonth(){
	var currentTime = new Date();
	var year = currentTime.getFullYear();
	var month = currentTime.getMonth() + 1;
	var date = currentTime.getDate();
	
	//alert(year +","+ month);
	
//	var start = year +"/"+ month +"/01";
	var start = year +"/"+ month +"/" +date;
		$("#search_date").val(start);
	
	var end = "";
	if(month==1 || month==3 || month==5 || month ==7 || month==8 || month ==10 || month==12){
		end = year +"/"+ month +"/31";
		$("#end_date").val(end);
	} else if (month==4 || month==6 || month==9 || month==11){
		end = year +"/"+ month +"/30";
		$("#end_date").val(end);
	} else if (month == 2){
		end = year +"/"+ month +"/28";
		$("#end_date").val(end);
	}
}
$(function() {
	$( "#search_date" ).datepicker({ dateFormat: "yy/mm/dd" });
	$( "#end_date" ).datepicker({ dateFormat: "yy/mm/dd" });
	});
// 查詢調味料事件
function querySeasoning(page) {
	crnPage = page;
	var searchName = $('#searchName').val();
	var searchDate = $('#search_date').val();
	var request_data = {
		"method" : "querySeasoningList",
		"args" : {
			"pageNum" : crnPage,// 當前第X頁
			"pageLimit" : row,
			"seasoningName" : searchName,
		    "searchDate":searchDate,

		}
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) { // API呼叫正確
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) {
			showSeasoning(result_content, [ "名稱 ", "進貨日期", "開始使用", "結束使用", "最後更新", "選項" ], PAGE_TABLE);
		} else {
			MSG.alertMsgs("check", "查詢發生錯誤，訊息為：" + result_content.msg, 0);
			return 0;
		}
	} else {
		MSG.alertMsgs("check", "查詢發生無法預期的錯誤，錯誤訊息為：" + response_obj.msg, 0);
		return 0;
	}

}

// 組列表 table
function showSeasoning(result_content, header, tableName) {
	// 清單內容
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<tr>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr>";

	var seasoningList = result_content.seasoningList;
	for ( var i in seasoningList) {
		var strTR = "<tr>";
		strTR += "<td width=\"12%\">" + seasoningList[i].ingredientName + "</td>";
		strTR += "<td width=\"20%\">" + seasoningList[i].stockDate + "</td>";
		strTR += "<td width=\"28%\">" + seasoningList[i].useStartDate + "</td>";
		strTR += "<td width=\"12%\">" + seasoningList[i].useeEndDate + "</td>";
		strTR += "<td width=\"13%\">" + seasoningList[i].lastUpdateDate + "</td>";
		strTR += "<td width=\"20%\"><a href=\"#\" onclick=editSeasoningDetail(" + seasoningList[i].seasoningStockId + ")>編輯</a> ";
		strTR += "<a href=\"#\" onclick=removeSeasoning(" + seasoningList[i].seasoningStockId + ")>刪除</a></td>";
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
	pageHtml += '前往<select style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="querySeasoning(this.value)"> |';
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

function addSeasoning() {
	$('#listForm').hide();
	$('#detailForm').show("slow");
	query_supplier_list();	//供應商清單處理
	query_certification_list(); //認證標章處理
	query_unit_list();	//單位處理
	query_attribute_radio();	//食材屬性處理
	//載入 calendar 套件
	$('#manufactureDate').datepicker({ dateFormat: "yy/mm/dd" });	//調味料製造日期
	$('#stockDate').datepicker({ dateFormat: "yy/mm/dd" });	//調味料進貨日期
	$('#expirationDate').datepicker({ dateFormat: "yy/mm/dd" });	//調味料生產日期
	$('#usestartdate').datepicker({ dateFormat: "yy/mm/dd" });	//調味料開始使用日期
	$('#useenddate').datepicker({ dateFormat: "yy/mm/dd" });	//調味料結束使用日期
	//$('#detailForm :input:not(:button)').val('');
	//$('#companyId').attr('readonly', false);
}

//編輯Supplier Detail
function editSeasoningDetail(seasoningId){
	$('#listForm').hide();
	$('#detailForm').show("slow");
	querySeasoningDetail(seasoningId);
}
// 回上頁
function goBack() {
	$('#detailForm :input:not(:button)').val('');
	$('#detailForm').hide();
	$('#listForm').show("slow");
	$('#companyId').css({"color":""});
}

// 儲存Seasoning detail 資料
function saveSeasoningDetail() {
	if(checkSpecialCharacters()){
		return;
	}
	var msg = validateInput();
	if(msg){
		MSG.alertMsgs("check", msg, 0);
		return;
	}
	
	var mode = '';
	if ($('#seasoningstockId').val())
		mode = 'UPDATE';
	else
		mode = 'ADD';

	var request_data = {
		"method" : "updateSeasoning",
		"args" : {
			"activeType" : mode,
			  "seasoningstockId" : $('#seasoningstockId').val(),
	          "dishId" : $('#dishId').val(),
	          //"kitchenId" : $('#kitchenId').val(),
		      "ingredientId" : $('#ingredientId').val(),
		      "ingredientName" : $('#ingredientName').val(),
		      "stockDate" : $('#stockDate').val(),
		      "manufactureDate" : $('#manufactureDate').val(),
		      "expirationDate" : $('#expirationDate').val(),
		      "lotNumber" : $('#lotNumber').val(),
		      "supplierId" : $('#supplierId').val(),
		      "sourceCertification" : $('#sourceCertification').val(),
		      "certificationId" : $('#certificationId').val(),
		      "productName" : $('#productName').val(),
		      "menutype":"1",
		      "supplierCompanyId":"",
		      "manufacturer" : $('#manufacturer').val(),
		      "ingredientQuantity" : $('#ingredientQuantity').val(),
		      "ingredientUnit" : $('#ingredientUnit').val(),
		      "ingredientAttr" : 
				{
				"gmbean" : $('#i_gmbean').val(),
				"gmcorn" : $('#i_gmcorn').val(),
				"psfood" : $('#i_psfood').val()
				},
		      "usestartdate" : $('#usestartdate').val(),
		      "useenddate" : $('#useenddate').val()
		}
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { // success
			MSG.alertMsgs("checkAndReload", "調味料："+result_content.ingredientName+"，儲存成功。", 0);
		} else {
			MSG.alertMsgs("check", "儲存失敗，訊息為：" + result_content.msg, 0);
		}
	} else {
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	}
}

// Query seasoning detail
function querySeasoningDetail(seasoningId) {
	var request_data = {
		"method" : "QuerySeasoningDetail",
		"args" : {
			"seasoningStockId" : seasoningId
		}
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { // success
			assignInputValue(result_content);
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
		}
	} else {
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	}
}
//列出供應商清單 Ric 2015/01/09
function query_supplier_list(id)
{
	var request_data =	{
							"method":"querySupplyOptionList",
							"args":{}
						};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	
		var supplierList = "";
		var result_content = response_obj.result_content;
		if(!id){
			id="";
		}
		if(result_content.resStatus == 1)
		{	
			supplierList +="<select name='supplierId' id='supplierId' >";
			for(var i in result_content.supplyNameOption)
			{
				//選項中不再顯示將預設的供應商選項，避免重複
				if(result_content.supplyNameOption[i].supplyId == id)
					supplierList +="<option value='"+ result_content.supplyNameOption[i].supplyId +"' selected>"+ result_content.supplyNameOption[i].supplyName +'</option>';
				else
					supplierList +="<option value='"+ result_content.supplyNameOption[i].supplyId +"'>"+ result_content.supplyNameOption[i].supplyName +'</option>';
			}
			supplierList += "</select>";
			$("#supplierListV").html("");
			$("#supplierListV").html(supplierList);
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
		}
	} else {
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	}
}
//列出認證標章清單 Ric 2015/01/09
function query_certification_list(certification){
	var opts = [""
             ,"CAS"
             ,"CAS_ORGANIC"
             ,"GAP"
             ,"GMP"
             ,"HALAL"
             ,"HACCP"
             ,"HEALTH"
             ,"ISO22000"
             ,"ISO9001"
             ,"MILK"
             ,"TGAP"
	            ];
	var list = "";
	list +="<select name='sourceCertification' id='sourceCertification' >";
	if(!certification)
		certification="";
	for(var i in opts)
	{
		//選項中不再顯示將預設的供應商選項，避免重複
		if(opts[i] == certification)
			list +="<option value='"+ opts[i] +"' selected>"+ opts[i] +'</option>';
		else
			list +="<option value='"+ opts[i] +"'>"+ opts[i] +'</option>';
	}
	list += "</select>";
	$("#sourceCertificationV").html("");
	$("#sourceCertificationV").html(list);
}
//列出重量單位清單 Ric 2015/01/09
function query_unit_list(unit){
	var opts = [""
                ,"公斤"
                ,"包"
                ,"支"
                ,"板"
                ];
	var list = "";
	list +="<select name='ingredientUnit' id='ingredientUnit' >";
	if(!unit)
		unit="";
	for(var i in opts)
	{
	//選項中不再顯示將預設的供應商選項，避免重複
	if(opts[i] == unit)
		list +="<option value='"+ opts[i] +"' selected>"+ opts[i] +'</option>';
	else
		list +="<option value='"+ opts[i] +"'>"+ opts[i] +'</option>';
	}
	list += "</select>";
	$("#ingredientUnitV").html("");
	$("#ingredientUnitV").html(list);
}
//置入屬性設定 Ric 2015/01/09
function query_attribute_radio(Attr){
	var check_gmbean = "";
	var check_gmcorn = "";
	var check_psfood = "";
	var gmbean = "0";
	var gmcorn = "0";
	var psfood = "0";
	if(Attr){
		gmbean = Attr.gmbean;
		gmcorn = Attr.gmcorn;
		psfood = Attr.psfood;
	}
	if ( gmbean== "1") {check_gmbean = "checked";}
	if ( gmcorn== "1") {check_gmcorn = "checked";}
	if ( psfood== "1") {check_psfood = "checked";}
	
	var list = "";
	list += "<input type='checkbox' class = 'i_gmbean' id='i_gmbean' value='"+ gmbean+ "' "+ check_gmbean+ ">"
	+ "<label for='i_gmbean"+ "'>基改黃豆</label>"
	+ "<input type='checkbox' class = 'i_gmcorn' id='i_gmcorn' value='"+ gmcorn+ "' "+ check_gmcorn+ ">"
	+ "<label for='i_gmcorn"+ "'>基改玉米</label>"
	+ "<input type='checkbox' class = 'i_psfood' id='i_psfood 'value='"+ psfood+ "' "+ check_psfood+ ">"
	+ "<label for='i_psfood"+ "'>加工品</label>";
	$("#ingredientAttr").html("");
	$("#ingredientAttr").html(list);
	$('input[type="checkbox"]').change(function() {
		this.value = this.checked ? 1 : 0;
	});
}
//塞入值給input
function assignInputValue(result_content) {
	var seasoningList = result_content.seasoningData;
	$('#ingredientName').val(seasoningList.ingredientName);
	$('#stockDate').val(seasoningList.stockDate);
	$('#manufactureDate').val(seasoningList.manufactureDate);
	$('#expirationDate').val(seasoningList.expirationDate);
	$('#usestartdate').val(seasoningList.useStartDate);
	$('#useenddate').val(seasoningList.useeEndDate);
	query_supplier_list(seasoningList.supplierId);	//供應商清單處理
	$('#manufacturer').val(seasoningList.manufacturer);
	query_certification_list(seasoningList.sourceCertification); //認證標章處理
	$('#certificationId').val(seasoningList.certificationId);
	$('#lotNumber').val(seasoningList.lotNumber);
	$('#productName').val(seasoningList.productName);
	$('#ingredientQuantity').val(seasoningList.ingredientQuantity);
	query_unit_list(seasoningList.ingredientUnit);	//單位處理
	query_attribute_radio(result_content.igredientAttrBO);	//食材屬性處理
	$('#seasoningstockId').val(seasoningList.seasoningStockId);
	$('#dishId').val(seasoningList.dishId);
	$('#kitchenId').val(seasoningList.kitchenId);
	$('#ingredientId').val(seasoningList.ingredientId);
	//載入 calendar 套件
	$('#manufactureDate').datepicker({ dateFormat: "yy/mm/dd" });	//調味料製造日期
	$('#stockDate').datepicker({ dateFormat: "yy/mm/dd" });	//調味料進貨日期
	$('#expirationDate').datepicker({ dateFormat: "yy/mm/dd" });	//調味料生產日期
	$('#usestartdate').datepicker({ dateFormat: "yy/mm/dd" });	//調味料開始使用日期
	$('#useenddate').datepicker({ dateFormat: "yy/mm/dd" });	//調味料結束使用日期
	
}


//刪除Supplier
function removeSeasoning(seasoningId){
	var answer = confirm("系統將同步刪除於<b>食材資料維護</b>中之調味料資料，請問是否確認刪除調味料？");
	if(!answer)
		return;
	
	var request_data = {
			"method" : "DeleteSeasoning",
			"args" : {
				"seasoningstockId" : seasoningId
			}
		};
		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { // success
				MSG.alertMsgs("checkAndReload", "刪除成功，可前往<b>食材資料維護</b>檢查是否正確。", 0);
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
	var file = document.getElementById("file");
	if(!$("#file").val()){
		MSG.alertMsgs('check', '請選擇檔案', 0);
		return false;
	}/* Create a FormData instance */
	var formData = new FormData();
		  MSG.alertMsgs('loading', '', 0);
	/* Add the file */ 
	formData.append("file", file.files[0]);
	formData.append("func", "seasoning"); //closed function Ric20150109
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

	if($('#ingredientName').val() === '')
		msg += '請輸入調味料名稱</br>';
	
	if($('#stockDate').val() === '')
		msg += '請輸入進貨日期</br>';
	
	if($('#supplierList').val() === '')
		msg += '請輸入供應商</br>';
	
	if($('#usestartdate').val() === '')
		msg += '請輸入開始使用日期</br>';
	
	if($('#useenddate').val() === '')
		msg += '請輸入結束使用日期</br>';
	
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

