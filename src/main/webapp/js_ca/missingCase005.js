// ----modified from missingCase by chu----
var MSG= new MsgsProcessing();
var util=new CateringServiceUtil();
var PAGE_TABLE0="resultTable0";
var dateRangeType = "7days";
$(document).ready(function(){	
	defaultYearMonthRange(dateRangeType);
	$("#start_date").click(function(){
		$("#ui-datepicker-div").css("display", "none");
	});
	$("#end_date").click(function(){
		$("#ui-datepicker-div").css("display", "none");	
	});
	$("#result").css("visibility","hidden");
//	$( "#start_date" ).datepicker({ dateFormat: "yy/mm/dd" }).datepicker( "setDate", new Date(new Date().getTime() - 1000 * 60 *60 * 24 * 50));
//	$( "#end_date" ).datepicker({ dateFormat: "yy/mm/dd" }).datepicker( "setDate", new Date(new Date().getTime() - 1000 * 60 *60 * 24 * 45));
	//checkingMissingCase(); //頁面開啟就呼叫API
});
//執行查詢
function loading(){//20151126 for UX, add loading msg---chu---
	MSG.alertMsgs("loading", "查詢中...", 0);
	setTimeout(function(){
		checkingMissingCase();
	},10);
}
function checkingMissingCase() {
	$("#query_list").find("table").empty();
	var sid = $("#select_school").val();
	var startDate = $("#start_date").val();
	var endDate = $("#end_date").val();
	$("div").remove(".tableDivHeader"); //處理CateringServiceUtil.prototype.setTableWithHeader 點選多次顯示多標頭問題
	//check
	if($("#select_school").parent().find(".custom-combobox").find('input').val() == "" || $("#select_school").val()==0){
		MSG.alertMsgs("showmsg", "請輸入查詢學校", 1);
		return;
	}//check
	if (!util.util_check_date_range($("#start_date").datepicker("getDate"), $("#end_date").datepicker("getDate"),31)) {
		$.unblockUI();
		return;
	}
	
	var request_data = {
		"method" : "QueryMissingCase_V2",
		"args" : {
			"startDate" : startDate,
			"endDate" : endDate,
			"sid" : sid,
		}
	};
	var response_obj = call_rest_api(request_data);

	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == "1" || result_content.resStatus == "0") { //success
			//清除頁面table result  20140923 KC
			$("#query_list").find("table").empty();
			
			if(result_content.nullBatchdataList.length==0){
				
			}else{
				util.setTableWithHeader(result_content.nullBatchdataList,["日期","學校","菜單","類型","供餐廚房","缺漏資料","廚房聯絡資訊"], PAGE_TABLE0, "<b>"+"缺漏資料</b>");
			}
			if (result_content.nullBatchdataList==0){
				MSG.alertMsgs("check", "日期區間查無缺漏資料" + result_content.msg, 0);
				$("#result").css("visibility","hidden");
				return;
			}
			$("#result").css("visibility","visible");
			$.unblockUI();
		} else {
			MSG.alertMsgs("check", "查詢結果提示：" + result_content.msg, 0);
			return;
		}
	} else {
		MSG.alertMsgs("check", "連線錯誤：" + response_obj.error_msg, 0);
		return;
	}
}

function export_excel(){
	var startDate = $("#start_date").val();
	var endDate = $("#end_date").val();
	var stext = $("#select_school>option:selected").text();//for table title
	var excel = alasql('SELECT * FROM HTML("#resultTable0",{headers:true})');
	alasql('SELECT * INTO XLSX("'+ startDate+ '至' + endDate + stext + '上線率報表' + '.xlsx",{headers:true, sheetid:"Sheet1"}) FROM ?', [excel]);
}

function change_date_range(){
	defaultYearMonthRange($("#ddlDateRange").val());
	return;
}
