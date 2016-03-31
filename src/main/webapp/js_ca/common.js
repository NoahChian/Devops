/**
 * all common method CateringServiceUtil for front web
 */
var fcloudQuerySourceidUrl = 'http://trace.twfoodtrace.org.tw/fcloud/index.do?action=querySourceId';
var fcloudProductLinkUrl = 'http://trace.twfoodtrace.org.tw/fcloud/index.do?action=getProductInfoByUser';
var fcloudCounpanyIdList = ['70390831','64217504'];
var indexPath="/";
function CateringServiceDateRangeObj() {
};
CateringServiceDateRangeObj.prototype.startDate = null;
CateringServiceDateRangeObj.prototype.endDate = null;

CateringServiceUtil = function() {
};
MsgsProcessing = function() {
};
/*******************************************************************************
 * 處理提示訊息alertMsgs(mode,msg,time)
 * 
 * @return mode:訊息模式 check 有確定按鈕的 /loading 上傳等待過場畫面/ showmsg 純訊息,需設time /
 *         unblock 取消block
 *         confirm 跳出是否確認，確認後會做上傳，否則不做動作
 *         confirmOK 跳出是否確認，確認後回傳true，否則不做動作
 *         msg:訊息內容 time:顯示時間(秒)
 */
MsgsProcessing.prototype.alertMsgs = function(mode, msg, time,yes_callback,no_callback) {
	console.log(yes_callback);
	if (typeof (mode) == "undefined") {
		$.blockUI({
			theme : true,
            title:    '系統提示訊息：', 
			message : '<div class="blockUI_css"><p id="fileMsg">'
					+ msg
					+ '</p><input id="btnCloseMsg" type="button" onclick="$.unblockUI();" value="確定"></div>',
					themedCSS: { 
				        top:    '10%'
				    }
		});
	}
	switch (mode) {
	case "check":
		$.blockUI({
			theme : true,
            title:    '系統提示訊息：', 
			message : '<div class="blockUI_css"><p>'
					+ msg
					+ '</p><input type="button" onclick="$.unblockUI();" value="確定"></div>',
			themedCSS: { 
		        top:    '10%'
		    }
		});
		break;
	case "checkAndReload":
		$.blockUI({
			theme : true,
            title:    '系統提示訊息：', 
			message : '<div class="blockUI_css"><p>'
					+ msg
					+ '</p><input type="button" onclick="$.unblockUI();location.reload();" value="確定"></div>',
					themedCSS: { 
				        top:    '10%'
				    }
		});
		break;
	case "loading":
		$.blockUI({
			theme : true,
            title:    '系統訊息：', 
			message : '<div class="blockUI_css"><img class="blockUIImgs" src="../../images/loading.gif" /><p class="blockUIMsgs">'+ msg + '</p></div>',
			themedCSS: { 
		        top:    '10%'
		    },
		    fadeIn: 0
		});
		if(yes_callback != undefined){
			yes_callback();
		}
		break;
	case "showmsg":
		$.blockUI({
			theme : true,
            title:    '系統提示訊息：',
			message : '<div class="blockUI_css"><p>'
					+ msg + '</p></div>',
					themedCSS: { 
				        top:    '10%'
				    }
		});
		if (!time == 0) {
			setTimeout($.unblockUI, time * 1000);
		} else {
			setTimeout($.unblockUI, 5000);
		}
		break;
	case "confirm":
		$.blockUI({
			theme : true,
            title:    '系統提示訊息：',
			message : '<div class="blockUI_css"><p>'
					+ msg
					+ '</p><input type="button" id="confirm_Yes" value="確定" /> <input type="button" id="confirm_No" value="取消" /></div>',
					themedCSS: { 
				        top:    '10%'
				    }
		});
		$('#confirm_Yes').click(function() {
			$.unblockUI();
			$("#overWrite").attr("value", 1);
			upload();
		});

		$('#confirm_No').click(function() {
			$.unblockUI();
			$("#file").val("");
			$("#overWrite").attr("value", 0);
			return;
		});
		break;
	case "confirmOK":
		$.blockUI({
			theme : true,
            title:    '確認訊息：',
			message : '<div class="TAB_TY_A blockUI_css"><p>'
					+ msg
					+ '</p><input type="button" id="confirm_Yes" value="確定" /> <input type="button" id="confirm_No" value="取消" /></div>',
					themedCSS: { 
				        top:    '10%'
				    }
		});
		$('#confirm_Yes').click(function() {
			$.unblockUI();
			$("#overWrite").attr("value", 1);
			//20150213 shine add 增加確定後的事件呼叫
			if(yes_callback != undefined){
				yes_callback();
			}
			return true;
		});

		$('#confirm_No').click(function() {
			$.unblockUI();
			$("#file").val("");
			$("#overWrite").attr("value", 0);
			//20150213 shine add 增加取消後的事件呼叫
			if(no_callback != undefined){
				no_callback();
			}			
			return false;
		});
		break;
	case "unblock":
		$.unblockUI();
		break;
	default:
		break;
	}
};
/*
 * 2014/06/13 Ric
 * 原本KC寫的，改至這裡統一使用
 */
function defaultYearMonthRange(dateRangeType) {
	var rangeType="7days";
	if (typeof(dateRangeType)!="undefined"){
		rangeType=dateRangeType;
	}
	var rangeDate=new CateringServiceDateRangeObj();
	rangeDate=util.getDateRange(rangeType);
	$( "#start_date" ).datepicker({ dateFormat: "yy/mm/dd" }).datepicker( "setDate", rangeDate.startDate);
	$( "#end_date" ).datepicker({ dateFormat: "yy/mm/dd" }).datepicker( "setDate", rangeDate.endDate);
}
// 檢查日期範圍天數
CateringServiceUtil.prototype.util_check_date_range = function(objDate_start,
		objDate_end, limitDays) {
	var diffSecs = objDate_end.getTime() - objDate_start.getTime() + 60000;

	if (diffSecs < 0) {
		alert("結束日期不得小於起始日期!");
		return false;
	} else if (diffSecs / (1000 * 60 * 60 * 24) > limitDays) {
		alert("查詢區間不得超過" + limitDays + "日!");
		return false;
	} else {
		return true;
	}
};

// 設定起始日期
CateringServiceUtil.prototype.getDateRange = function(dateRangeType) {
	var startDate = new Date();
	var ednDate = new Date();
	var today = new Date();
	if (typeof (dateRangeType) == "undefined") {
		dateRangeType = "";
	}
	switch (dateRangeType) {
	case "season1":
		startDate = new Date(today.getFullYear(), 0, 1);
		endDate = new Date(today.getFullYear(), 2, 31);
		break;
	case "season2":
		startDate = new Date(today.getFullYear(), 3, 1);
		endDate = new Date(today.getFullYear(), 5, 31);
		break;
	case "season3":
		startDate = new Date(today.getFullYear(), 6, 1);
		endDate = new Date(today.getFullYear(), 8, 30);
		break;
	break;
case "season4":
	startDate = new Date(today.getFullYear(), 9, 1);
	endDate = new Date(today.getFullYear(), 11, 31);
	break;
case "this.month":
	startDate = new Date(today.getFullYear(), today.getMonth(), 1);
	endDate = new Date(today.getFullYear(), today.getMonth() + 1, 0);
	break;
case "30days":
	startDate = new Date(today.getTime() - 30 * 24 * 60 * 60 * 1000);
	endDate = today;
	break;
case "7days": //20140624 Ric 7日會變成8日(含首尾)，所以改6 * 24 ..
	startDate = new Date(today.getTime() - 6 * 24 * 60 * 60 * 1000);
	endDate = today;
	break;
default:
	break;
}

var returnObj = new CateringServiceDateRangeObj();
returnObj.startDate = startDate;
returnObj.endDate = endDate;

return returnObj;
};
// 設定table內容
CateringServiceUtil.prototype.setTableWithHeader = function(jsonData, header,
		tableName) {
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<thead><tr>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr></thead>";
	for ( var item in jsonData) {
		var row = jsonData[item];
		var strTR = "<tbody><tr>";
		for ( var i in row) {
			strTR += "<td>" + row[i] + "</td>";
		}
		strTR += "</tr></tbody>";
		html += strTR;
	}
	$("#" + tableName).html(html);
};

CateringServiceUtil.prototype.setTableWithHeaderIndex= function(jsonData, headerAry,tableName){
	$("#" + tableName).empty();
	
	var html = "";
	var headerIndexAry=[];
	html += "<thead><tr>";
	for(var i =0; i<headerAry.length;i++){
		html += "<th>" + headerAry[i].text + "</th>";
		headerIndexAry[i]=headerAry[i].key;
	}

	html += "</tr></thead>";
	for ( var item in jsonData) {
		var row = jsonData[item];
		var strTR = "<tr>";
		for ( var i =0;i<headerIndexAry.length;i++) {
			strTR += "<td>" + row[headerIndexAry[i]] + "</td>";
		}
		strTR += "</tr>";
		html += strTR;
	}
	$("#" + tableName).html(html);
};

// 設定table內容 含說明標頭 20140624 Ric //寫的後需要再原本的function內補 $("div").remove(".tableDivHeader"); 解決執行多次會有多標頭出現
CateringServiceUtil.prototype.setTableWithHeader = function(jsonData, header,
		tableName, description) {
	if (typeof(description) != "undefined") {
		//$("div").remove(".tableDivHeader");
		var divheader = "";
		divheader += "<div class='tableDivHeader'>"
			+ description
			+ "</div>";
		$("#" + tableName).before(divheader);
	}
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<thead><tr>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr></thead>";
	for ( var item in jsonData) {
		var row = jsonData[item];
		var strTR = "<tr>";
		for ( var i in row) {
			strTR += "<td>" + row[i] + "</td>";
		}
		strTR += "</tr>";
		html += strTR;
	}
	$("#" + tableName).html(html);
};
// 設定table內容(含操作鈕)
CateringServiceUtil.prototype.setTableStringWithOperation = function(tableName,
		jsonData, header, total, opKey, opEdit, opDelete) {
	var domTableName = "";
	if (typeOf(tableName) != "undefined") {
		domTableName = tableName;
	} else {
		domTableName = "mytable";
	}
	var html = "<table id=\"" + domTableName + "\">";
	var thCount = header.length;
	html += "<tr>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr>";
	for ( var item in jsonData) {
		var row = jsonData[item];
		var strTR = "<tr>";
		for ( var i in row) {
			strTR += "<td>" + row[i] + "</td>";
		}
		strTR += "</tr>";
		html += strTR;
	}
	html += "<tr colspan=\"" + (thCount - 1) + "\"><td>總數</td><td>" + total
			+ "</td></tr>";
	html += "</table>";
	return html;
	// alert("test");
};

CateringServiceUtil.prototype.setTableTail = function(tableName, thValue,
		tdValue) {
	var objTable = $("#" + tableName);
	var tdCount = objTable.eq(0).children().length;
	objTable.append("<tr><td colspan =" + (tdCount - 1) + ">" + thValue
			+ "</td><td>" + tdValue + "</td></tr>");

};
// 組成下拉選單
CateringServiceUtil.prototype.setDropdownlist = function(jsonData, domName,
		textName, valueName) {
	var objName = "#" + domName;

	$(objName).children().remove();
	for ( var i in jsonData) {
		var value = "";
		var text = "";
		if (typeof (jsonData[i][textName]) != "undefined") {
			text = jsonData[i][textName];
		}
		if (typeof (valueName) != "undefined"
				&& typeof (jsonData[i][valueName]) != "undefined") {
			value = jsonData[i][valueName];
		}

		$(objName).append(
				$("<option></option>").attr("value", value).text(text));
	}
};

/**
 * 此為iServBilling UI中共同需要用到的javascript function
 * 以及公用的參數設定
 *
 * 提供的function有	:
 * 1.test()
 * 2.call_rest_api()
 * 
 * 
 * @author Eason
 * date：2013/05/15
 */
 
/**
 * 共用參數
 * @author 	Eason
 * @date	2013/05/15
 */
//iServBilling的後台REST API位置
//var iServBilling_rest_url = "http://175.98.115.21:15288/billing/rest/";
var iServBilling_rest_url = "/cateringservice/rest/API/";
//var iServBilling_rest_url = "http://175.98.115.21:14298/oneclickshoppingwall/rest/";

//本服務的存取token但尚未實做
var service_token = "iK89Odju";

 
 /**
 * 用以測試
 * @param	none
 * @return	none
 * @author 	Eason
 * @date	2013/04/25
 */
function test(){
	
	alert(iServBilling_rest_url);
}

/**
 * 所有用來呼叫iServbilling 後台REST API的動作，都會透過這個function來進行呼叫
 * 其中，後台REST API的位置(iServBilling_rest_url)與控制本服務操作權限的token(service_token)
 * 需要先進行設置
 * 
 * ***在這裡調用後端API的方式為同步呼叫，非ajax預設的非同步呼叫。請注意!***
 * 
 * @param	resource		要操作的資源
 * @param	method			操作的方式 和resource兩個決定所要呼叫的API URL
 * @param   request_data	POST回RESTAPI的資料
 * @return	response_obj 	以物件傳回呼叫REST API的操作結果
 * @author 	Eason
 * @date	2013/05/16
 */
function call_rest_api(request_data,apiUrl,method){

	//宣告傳回值
	var response_obj = new Object;
	
	//alert(iServBilling_rest_url+resource+'/'+method+'/');
	
	/*在這裡我關閉非同步的呼叫後端API(加上async: false)，改為同步的方式呼叫。
	 *才可以確保最終回傳參數(response_obj)的一致性。
	 *就是不會在ajax還沒做完就先return了
	 *ajax預設是非同步調用，搞了我兩個小時 */
	$.ajax(
	{
		url: apiUrl==undefined?iServBilling_rest_url:apiUrl,  //20150505 shine mod 如有傳入網址,則以傳入的為主
		//url: "http://175.98.115.21:15288/billing/rest/web/test/",
		//url: 'http://175.98.115.17:80/MUST/rest/Device/list',
		type: method==undefined?'POST':method,   //20150505 shine mod 如有傳入method,則以傳入的為主
		dataType: 'json',
		contentType: 'application/json',
		data: JSON.stringify(request_data),
		async: false,//關閉非同步的調用方式，改為同步	
		error: function(xhr)
		{
			//alert('呼叫rest api發生錯誤，請確認網路狀況與iserverbilling的後臺狀態');
			alert('網路連線忙碌中，請稍候再試');
		},
		/*** 3. ***/
		success: function(response) 
		{
			//將response轉成JSON格式
			
			var response_json = JSON.stringify(response);
			//alert(response_json);
			
			//將response_json再轉成物件
			response_obj = JSON.parse(response_json);
			//alert(response_obj.result_content.test_result);
			
			//20150505 shine mod 增加判斷是否有該欄位
			if(response_obj["result_content"] && response_obj["result_content"]["resStatus"]){
				//判斷是否有session timeout msg 20140922  KC
				if (response_obj["result_content"]["resStatus"]=="-2" || response_obj["result_content"]["resStatus"]==-2){
					if (location.pathname!=indexPath){
						alert("使用者未授權或登入逾時，將自動跳轉回首頁");
						window.location.href="http://"+location.host+indexPath;
					}
					
				}
			}
		},
		timeout: 10*60000 //查詢過久會導致頁面GG --chu--
	});
	
	return response_obj;
}

function call_rest_file(request_data,url){

	//宣告傳回值
	var response_obj = new Object;
	
	//alert(iServBilling_rest_url+resource+'/'+method+'/');
	
	/*在這裡我關閉非同步的呼叫後端API(加上async: false)，改為同步的方式呼叫。
	 *才可以確保最終回傳參數(response_obj)的一致性。
	 *就是不會在ajax還沒做完就先return了
	 *ajax預設是非同步調用，搞了我兩個小時 */
	$.ajax(
	{
		url: url,
		//url: "http://175.98.115.21:15288/billing/rest/web/test/",
		//url: 'http://175.98.115.17:80/MUST/rest/Device/list',
		type: 'POST',
		dataType: 'json',
		contentType: 'application/json',
		data: JSON.stringify(request_data),
		async: false,//關閉非同步的調用方式，改為同步	
		error: function(xhr)
		{
			//alert('呼叫rest api發生錯誤，請確認網路狀況與iserverbilling的後臺狀態');
			alert('網路連線忙碌中，請稍候再試');
		},
		/*** 3. ***/
		success: function(response) 
		{
			//將response轉成JSON格式
			
			var response_json = JSON.stringify(response);
			//alert(response_json);
			
			//將response_json再轉成物件
			response_obj = JSON.parse(response_json);
			//alert(response_obj.result_content.test_result);
	
		}
	});
	
	return response_obj;
}


/**
 * 改變傳入ID之區域的顯示狀況。
 *
 * @param	要被改變顯示狀況的區域(div)的id
 * @return	none
 * @author	Eason
 * @date	2013/05/06
 */
function toggle_div(div_id)
{
	//將傳入ID的區塊做顯示和隱藏的切換 參數為切換的速度
	$("#"+div_id).toggle("slow");
}


/*
 * 20140627 Ric
 * 設定各種情input最大寬度
 * 注意! 用JS產生的Table不會受到document.ready啟動，所以要個別設定 > 於JS產生html code處，貼上下列相同code
 */
function setInputLength(){
	$(".max4").attr("maxlength", 4);
	$(".max10").attr("maxlength", 10);
	$(".max40").attr("maxlength", 40);
	$(".max45").attr("maxlength", 45);
	$(".max50").attr("maxlength", 50);
	$(".max240").attr("maxlength", 240);
	$(".max255").attr("maxlength", 255);
	$(".max250").attr("maxlength", 250);
	$(".max250").attr("maxlength", 250);
	$('.dateMode').prop('readonly', true);	
	$('.numOnly').keyup(function () { 
	    this.value = this.value.replace(/[^0-9\.]/g,'');
	});
	//20140730 Ric  因應更版 Hide尚未公告欄位，屆時請用關鍵字"//20140730 Ric  因應更版"
	//$('.tmpV001').next('input').attr('type','hidden');
	//$('.tmpV001').next().hide();
	//$('.tmpV001').hide();
}

/*
 * 20141017 Joshua
 * 日期格式驗證
 */
function validDateFormat(date){
	var dateStr = date.val();
	var dateArr = dateStr.split("/");
	var yearRe = /^(0|[1-9][0-9]*)$/;  //年份規則
	var isDate = false;
	if(dateArr.length == 3){
		if(yearRe.test(dateArr[0])){
			isDate = true;
		}else{
			alert('年份開頭不可為0');
			date.focus();
			return;
		}
	}
	
	if(!isDate){
		alert('日期格式不正確\n請輸入西元年 YYYY/MM/DD 或 民國年YYY/MM/DD 格式');
		date.focus();
		return false;
	}else{
		return true;
	}
}

$(document).ready(function() {
	setInputLength();
});

function newPopup(url) {
    popupWindow = window.open(url, 'popUpWindow', 'height=500,width=800,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes');
}

