/**
 * nodeJsSrv_util.js 為 nodeJs 程式用之公用程式及公用環境變數
 * 目前該js被引入在『61、62、63之API的jsp程式』及『index.jsp』之程式中。
 */
//--------- [公用變數] ---------
var MSG = new MsgsProcessing();
var util=new CateringServiceUtil();
var PAGE_TABLE="resultTable";
var row = 50; //每頁筆數
var pageRow = [10, 20, 50, 100, 200, 500];
var crnPage = 1;//當前頁數

var serverIp = location.hostname;
var serverPort = "80";
var url = "";

//=================================================
//[nodeJs用]
//此方法為直接由前端跨網取得資料
function ajaxCallJsonp(targetxx, reqxx, cb,isAsync,method){
	console.log("function [ajaxCallJsonp]");
	console.log(targetxx);
	console.log(reqxx);
	var xmlhttp = new XMLHttpRequest();
	//20150528 shine mod 增加isAsync參數來決定是為同步或非同步呼叫
	var asyncParam = isAsync==undefined?true:isAsync;
	if(method){
		xmlhttp.open(method, targetxx,asyncParam);
	}else{
		xmlhttp.open("POST", targetxx,asyncParam);
	}
	
	xmlhttp.onreadystatechange = function() {
	  var status;
	  var data;
	  if (xmlhttp.readyState == 4) {
	    status = xmlhttp.status;
	    if (status == 200) {
	      data = JSON.parse(xmlhttp.responseText);
	      console.log(data);
	      cb(data);
	    }
	  }
	};
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	xmlhttp.send(JSON.stringify(reqxx));
}

/**
 * 新版 ajax 寫法
 * 無法取得回傳值，有問題，暫不使用。
 */
/*
function _ajaxCallJsonp(target,options){
	//宣告傳回值
	var response_obj = new Object;

	$.ajax({
		url: target,
		method: "POST",
		dataType: "json",		//指定以jsonp方式執行
		contentType: 'application/json',
		data: JSON.stringify(options),
		success: function( jqXHR, res ) {
			response_obj = jqXHR;
		},
		fail: function( jqXHR, textStatus ) {
			$(".blockUIImgs").hide();
			$(".blockUIMsgs").html("查詢發生錯誤，訊息為：" + textStatus);
			$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
			return 0;
		}
	});

	return response_obj;
}
*/

/**
 * 新版 ajax 寫法
 * 無法取得回傳值，有問題，暫不使用。
 */
/*
function _ajaxCallJsonp(target,options){
	//宣告傳回值
	var response_obj = new Object;

	$.ajax({
		url: target,
		method: "POST",
		dataType: "json",		//指定以jsonp方式執行
		contentType: 'application/json',
		data: JSON.stringify(options),
		success: function( jqXHR, res ) {
			response_obj = jqXHR;
		},
		fail: function( jqXHR, textStatus ) {
			$(".blockUIImgs").hide();
			$(".blockUIMsgs").html("查詢發生錯誤，訊息為：" + textStatus);
			$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
			return 0;
		}
	});

	return response_obj;
}
*/

/**
 * 浮動按鈕(回最頂端)
 */
function gotop() {
    $("#gotop").click(function(){
        jQuery("html,body").animate({
            scrollTop:0
        },1000);
    });
    $(window).scroll(function() {
        if ( $(this).scrollTop() > 300){
            $('#gotop').fadeIn("fast");
        } else {
            $('#gotop').stop().fadeOut("fast");
        }
    });
}
