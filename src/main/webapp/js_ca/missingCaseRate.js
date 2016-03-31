// ----20151201 chu----
var MSG= new MsgsProcessing();
var util=new CateringServiceUtil();
var PAGE_TABLE0="resultTable0";
var PAGE_TABLE="resultTable";
var today = new Date();
var chart;
$(document).ready(function(){
	$("#start_date" ).datepicker({ dateFormat: "yy/mm/dd" }).datepicker( "setDate", today);//default date
	$("#start_date").click(function(){
		$("#ui-datepicker-div").css("display", "none");
	});
	$("#chart").hide();
	$("#result").css("visibility","hidden");
	$("#export2").css("visibility","hidden");
});
//binding Date, schoolType, schoolText when query, just for export file and name it correct--20151201chu--
var inDate = $("#start_date").val();
var schoolType = $("#SchoolType").val(); 
var stext = $("#SchoolType>option:selected").text();//for table title
var uType = $("#_uType").val();
//執行查詢
function loading(){
	//MSG.alertMsgs("loading", "查詢中...", 0);
	var inDate = $("#start_date").val();
	var schoolType = "";
	if($('input[id="PreCheckbox"]').is(':checked')){
		schoolType +=$("#PreCheckbox").val();
	}
	if($('input[id="EleCheckbox"]').is(':checked')){
		schoolType +=$("#EleCheckbox").val();
	}
	if($('input[id="JunCheckbox"]').is(':checked')){
		schoolType +=$("#JunCheckbox").val();
	}
	if($('input[id="SenCheckbox"]').is(':checked')){
		schoolType +=$("#SenCheckbox").val();
	}
	if(schoolType.length=='0'){
		MSG.alertMsgs("showmsg", "請至少選擇一項學校層級", 1);
		return;
	}
	schoolType = schoolType.substring(0,(schoolType.length-1));
	
	var stext = $("#SchoolType>option:selected").text();//for table title
	$("div").remove(".tableDivHeader");
	$("#export2").css("visibility","hidden");
	//check
	if ($("#start_date").val()==""|| $("#start_date").val()==0) {
		$("#result").css("visibility","hidden");
		$("#chart").hide();
		MSG.alertMsgs("showmsg", "請輸入日期", 1);
		return;
	}
	//check
	if ($("#start_date").datepicker("getDate") > today ) {
		$("#result").css("visibility","hidden");
		$("#chart").hide();
		MSG.alertMsgs("showmsg", "請確認日期", 1);
		return;
	}
	//API
	var request_data = {
			"method" : "MissingCaseRate",
			"args" : {
				"inDate" : inDate,
				"schoolType" : schoolType,
			}
		};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == "1" || result_content.resStatus == "0") {//success
			$("#query_list").find("table").empty();
			if(result_content.micrate.length==0){//查無
				$("#result").css("visibility","hidden");
				MSG.alertMsgs("showmsg", "請確認日期", 1);
				return;
			}else{
				util.setTableWithHeader(result_content.micrate,["縣市","當日實上線數","當日應上線數","上線率(%)","學校總數","當日不供餐數"], PAGE_TABLE0, "<b>"+inDate+stext+"上線率</b>");
				$("#result").css("visibility","visible");
				chart(result_content.micrate);
				$("#chart").show();
			}
			if(result_content.rateDetail.length==0){
				if(uType !="11"){//非中央就不玩CHART了					
					$("#chart").hide();
				}
			}else{
				util.setTableWithHeader(result_content.rateDetail,["學校名稱","供餐廚房","電子郵件","連絡電話","廚房聯絡人"], PAGE_TABLE, "<b>"+inDate+stext+"未上線學校資料</b>");
				$("#export2").css("visibility","visible");
				$("#chart").hide();
			}
			if (result_content.micrate==0 && result_content.rateDetail==0){
				$("#result").css("visibility","hidden");
				$("#export2").css("visibility","hidden");
				$("#chart").hide();
				MSG.alertMsgs("check", "查無資料" + result_content.msg, 0);
				return;
			}
		}else {
			MSG.alertMsgs("check", "查詢結果提示：" + result_content.msg, 0);
			return;
		}
	}else {
		MSG.alertMsgs("check", "連線錯誤：" + response_obj.error_msg, 0);
		return;
	}
}

//chart
function chart(data){ AmCharts.makeChart( "chartdiv", {
	"type": "serial",
	"theme": "light",
	"dataProvider": data,
	"gridAboveGraphs": true,
	"startDuration": 2,
	"valueAxes": [{
		"position": "left",
		"title": "上線率(%)",
		"minimum": 0.0, 
		"maximum": 100.0
	}],
	"graphs": [ {
		"balloonText": "<div>[[category]]:  上線率<b>[[value]]%</b></div><hr>" +
		"<div>實上線數:  [[haveMenu]]</div>" +
		"<div>應上線數:  [[mustMenu]]</div>" +
		"<div>學校總數:  [[totalSchool]]</div>" +
		"<div>不供餐數:  [[noMenu]]</div>",
		"fillAlphas": 0.5,
		"lineAlpha": 0.1,
		"type": "column",
		"valueField": "rateR2"
	} ],
	"chartCursor": {
		"categoryBalloonEnabled": false,
		"cursorAlpha": 0,
		"zoomable": false
	},
	"categoryField": "countyName",
	"categoryAxis": {
		"gridPosition": "start",
		"gridAlpha": 0,
		"tickPosition": "start",
		"tickLength": 20
	},
	"export": {
		"enabled": true,
		"menu": []
	},
	"listeners":[{
		"event": "clickGraphItem",
		"method": function(e) {
			forTest();
			console.log(e.item.category);
		}
	}]
});
}

//test chart click
function forTest(){
	console.log("有");
}
// from 士展大大  20151208 chu--暫不用
function exc(){
	var inDate = $("#start_date").val();
	var schoolType = $("#SchoolType").val(); 
	var stext = $("#SchoolType>option:selected").text();//for table title
	//check
	if ($("#start_date").val()==""|| $("#start_date").val()==0) {
		$("#result").css("visibility","hidden");
		$("#chart").hide();
		MSG.alertMsgs("showmsg", "請輸入日期", 1);
		return;
	}
	//check
	if ($("#start_date").datepicker("getDate") > today ) {
		$("#result").css("visibility","hidden");
		$("#chart").hide();
		MSG.alertMsgs("showmsg", "請確認日期", 1);
		return;
	}
	var request_data = {
			"method" : "MissingCaseRate",
			"args" : {
				"inDate" : inDate,
				"schoolType" : schoolType,
			}
		};
	
	var excel1 = [];
	var excel2 = [];
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == "1" || result_content.resStatus == "0") {//success
			if(result_content.micrate.length==0){//查無
				MSG.alertMsgs("showmsg", "匯出錯誤，請重新操作", 1);
				return;
			}else{
				console.log(result_content.micrate);
				console.log(result_content.micrate.length);
				var data = result_content.micrate;
				console.log(data);
				//data = data.data[0].rows;
				console.log(data[0]);
				console.log(data.length);
				console.log(data[0].countyName);
				for(i=0; i<data.length; i++){
					var temp = {
						'縣市': data[i].countyName,
						'當日實上線數': data[i].haveMenu,
						'當日應上線數': data[i].mustMenu,
						'上線率': data[i].rateR2,
						'學校總數': data[i].totalSchool,
						'當日不供餐數': data[i].noMenu,
					};
					excel1.push(temp);
				}
				console.log("E"+excel1);
				//alasql('SELECT * INTO XLSX("'+ inDate + stext + '上線率報表' + '.xlsx",{headers:true, sheetid:"Sheet1"}) FROM ?', [excel1]);
				//alasql('SELECT * INTO XLSX("schoolingredient_' + timetemp + '.xlsx",{headers:true, sheetid:"Sheet1"}) FROM ?', [excel]);
			}
			if(result_content.rateDetail.length>0){
				var data = result_content.rateDetail;
				//data = data[0].rows;
				for(i=0;i<data.length;i++){
					var temp = {
							'學校名稱': data[i].schoolName,
							'供餐廚房': data[i].kitchenName,
							'電子郵件': data[i].email,
							'聯絡電話': data[i].tel,
							'負責人': data[i].ownner,
					};
					excel2.push(temp);
				}
			}
			var excel = excel1.concat(excel2);
			alasql('SELECT * INTO XLSX("'+ inDate + stext + '上線率報表' + '.xlsx",{headers:true}) FROM ?', [excel]																														);
		}else {
			MSG.alertMsgs("check", "查詢結果提示：" + result_content.msg, 0);
			return;
		}
	}else {
		MSG.alertMsgs("check", "連線錯誤：" + response_obj.error_msg, 0);
		return;
	}
}
//now use this..20151209 chu 直接抓TABLE
function export1(){
	var inDate = $("#start_date").val();
	var stext = $("#SchoolType>option:selected").text();//for table title
	var excel = alasql('SELECT * FROM HTML("#resultTable0",{headers:true})');
	alasql('SELECT * INTO XLSX("'+ inDate + stext + '上線率報表' + '.xlsx",{headers:true, sheetid:"Sheet1"}) FROM ?', [excel]);
}
function export2(){
	var inDate = $("#start_date").val(); 
	var stext = $("#SchoolType>option:selected").text();//for table title
	var excel = alasql('SELECT * FROM HTML("#resultTable",{headers:true})');
	alasql('SELECT * INTO XLSX("'+ inDate + stext + '未上線報表' + '.xlsx",{headers:true, sheetid:"Sheet1"}) FROM ?', [excel]);
}