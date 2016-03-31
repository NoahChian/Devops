// ----modified from missingCase by chu----
var MSG= new MsgsProcessing();
var util=new CateringServiceUtil();
var PAGE_TABLE0="resultTable0";
var PAGE_TABLE="resultTable";
var PAGE_TABLE1="resultTable1";
var PAGE_TABLE2="resultTable2";
var PAGE_TABLE3="resultTable3";
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
	var cid = $("#select_counties").val();
	var startDate = $("#start_date").val();
	var endDate = $("#end_date").val();
	$("div").remove(".tableDivHeader"); //處理CateringServiceUtil.prototype.setTableWithHeader 點選多次顯示多標頭問題
	if($("#select_counties").parent().find(".custom-combobox").find('input').val() == "" || $("#select_counties").val()==0){
		MSG.alertMsgs("showmsg", "請輸入查詢縣市", 1);
		return;
	}//check
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
/*
 * 2014/06/19 Ric
 * 上傳菜色照片
 */
var client = new XMLHttpRequest();
var tagname = ""; 
function upload(dishId,filetag){
	tagname = "file"+filetag;
	if(!$("#"+tagname).val()){
    	return ("請選擇檔案");
    }
   /* Create a FormData instance */
    var formData = new FormData();
   /* Add the file */ 
   formData.append("file", $("#"+tagname)[0].files[0]);
   formData.append("func", "dishid|"+dishId);
   formData.append("overWrite", "0");
   //ajax
   client.open("post","/cateringservice/file/upload", false);
   client.send(formData);  /* Send to server */ 
//} 20140625 Ric改同步處理
/* Check the response status */  
//client.onreadystatechange  = function() 
//{
   if (client.readyState == 4 && client.status == 200)  {
	var obj = JSON.parse(client.responseText);
     if(obj.retStatus == "1"){
    	// MSG.alertMsgs("check", "檔案上傳成功", 0); 
    	 $("#pictr"+filetag).remove();
    	 //tr set id
    	 tagname = "";
    	 return ("檔案上傳成功");
      } else {
     	// MSG.alertMsgs("check", "檔案上傳失敗，原因為" + obj.retMsg, 0); 
     	 return ("檔案上傳失敗，" + obj.retMsg);
      }
   }
}

/*
 * 20140619 Ric
 * 針對菜單菜色，如果第一個取出為空值則不顯示，否則可以上傳照片
 */
function uploadpic(jsonData, header,tableName,description) {
	if (typeof(description) != "undefined") {
	var divheader = "";
	divheader += "<div class='tableDivHeader'>"
		+ description
		+ "</div>";
	$("#" + tableName).before(divheader);
}
	$("#" + tableName).empty();
	var html = "";
	html += "<thead><tr>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr></thead>";
	for(var i=0; i<jsonData.length; i++) {
		var row = jsonData[i];
		var strTR = "";
		if(row.dishName!=null&&row.dishName!=""){
			strTR += "<tr id='pictr"+i+"'>";
			strTR += "<td>" + row.dishName + "</td>";
			strTR += "<td><input style='margin-top: 5px; float:left' type= 'file' id='file"+i+"' name='file"+i+"' /><input type= 'hidden' id='dish"+i+"' value = '"+ row.dishId +"' /><input type= 'hidden' id='dishName"+i+"' value = '"+ row.dishName +"' />"
      + "<input class='btn btn-primary' style='margin:0' type='button' value='上傳照片' onclick='uploadonepic(" +row.dishId + ","+i+");' /></td>";
			strTR += "</tr>";
		}
		html += strTR;
	}
	html += "<tr>";
	html += "<td colspan='3' align='center'><span><input class='btn btn-primary' style='margin:0' type='button' id='uploadAllPic' value='上傳已選取之照片' onclick='uploadallpic(\""+tableName+"\",\"dish\")' /></span></td/>";
	html += "</tr>";
	$("#" + tableName).html(html);
	totalTR= document.getElementById(tableName).getElementsByTagName('tr').length -2 ;//提供全部照片所計算的欄位數 //-2去首尾
};
/*
 * 20140625 Ric
 * 上傳所有已選之菜單
 */
var totalTR;//照片Table中tr數為全域，一讀入即取得(於產生照片清單Table時)，避免找不到Id 
function uploadallpic(tableid, para){									//搜尋Table目標，擷取的參數
	var fileNum = 0;													//有檔案的數量
	var runPic = new Object;
	runPic.file =[];
	runPic.dish =[];
	runPic.dishname = [];
	for(var i=0; i<totalTR; i++){
		//var picTag = $("#file"+i); 		
		//修改原本判斷dom is null的方式  20140923 KC
		if($("#file"+i).length!=0){	//取得file選取檔案的element
			if($("#file"+i)[0].files[0]!=null){
				runPic.file[fileNum]=i;
				runPic.dish[fileNum]=$("#"+para + i).val();				//菜色Id
				runPic.dishname[fileNum]=$("#"+para+ "Name"+i).val();		//菜色名稱
				fileNum++;
			}
		}else{
		}
	}
	if(runPic.file.length==0){
		MSG.alertMsgs("check", "沒有選擇檔案", 0);
	}else{
		uploadPicProcessing(runPic);
	}
}
function uploadonepic(dishId,filetag){
	tagname = "file"+filetag;
	var runPic = new Object;
	runPic.file =[];
	runPic.dish =[];
	runPic.dishname = [];
	if($("#"+tagname)[0].files[0]!=null){
		runPic.file[0]=filetag;
		runPic.dish[0]=dishId;	
		runPic.dishname[0]=$("#dishName"+filetag).val();
		uploadPicProcessing(runPic);
	}else{
		MSG.alertMsgs("check", "沒有選擇檔案", 0);
	}
}
function uploadPicProcessing(runPic){
	MSG.alertMsgs("loading", "檔案檢查與上傳...", 0);
	var uploadMsgs = "";
		for(var i=0; i<runPic.file.length; i++){
			uploadMsgs+= runPic.dishname[i] +":" + upload(runPic.dish[i],runPic.file[i]) + "<br>";
		}
		$(".blockUIImgs").hide();
		$(".blockUIMsgs").html(uploadMsgs);
		$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
		//MSG.alertMsgs("check", uploadMsgs, 0); 
		$("#uploadAllPic").val("上傳已選取之照片");
}
//暫時使用JQUERY套件匯出EXCEL檔 20151123 chu--------

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
