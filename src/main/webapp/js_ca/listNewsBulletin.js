
var MSG= new MsgsProcessing();
var util=new CateringServiceUtil();
var PAGE_TABLE="resultTable";
var row = 30; //每頁筆數
var crnPage = 1;//當前頁數
$(function() {
	$( "#start_date" ).datepicker({ dateFormat: "yy/mm/dd" });
	$( "#end_date" ).datepicker({ dateFormat: "yy/mm/dd" });
	});
function defaultYearMonth(){
	var currentTime = new Date();
	var year = currentTime.getFullYear();
	var month = currentTime.getMonth() + 1;
	
	//alert(year +","+ month);
	
	var start = year +"/"+ month +"/01";
	$("#start_date").val(start);
	
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

//查詢顯示的種類
function queryCategory(){
	//request
	var request_data =	{
			"method":"queryNewsCategoryList",
			"args":{}
						};
	var response_obj = call_rest_api(request_data);
	
	//alert(response_obj);
	
	//response
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			var new_drop = "<select id=\"category_id\" class=\"form-control\" >";
			if(result_content.newsCategoryList.length > 1){
				new_drop += "<option value='-1'>檢視所有</option>"; //能夠有全部的選項
			}
			for(var i=0; i<result_content.newsCategoryList.length; i++) {
				new_drop += "<option value=" + result_content.newsCategoryList[i].id + ">" + result_content.newsCategoryList[i].name + "</option>";
			}
//			new_drop += "<option value='-1'>全部-單日(起始日)</option>"; //能夠有全部學校的選項
			new_drop += "</select>";
			$("#listForm").find("span[id=dropDown_category]").html(new_drop);
			
			new_drop = new_drop.replace("<option value='-1'>檢視所有</option>","");
			$("#detailForm").find("span[id=dropDown_category]").html(new_drop);
//			$( "#category_id" ).combobox();
			
			//如果只有一個選項就鎖死
//			if(result_content.school.length ==1){
//				$( "#category_id" ).parent().find("input").attr('disabled', true);
//			}
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}
function query_News_List(page)
{
	crnPage = page;
	MSG.alertMsgs("loading", "查詢中...", 0);
	
	var newsId = -1;
	if($("#newsId").val() != "") {
		newsId = Number($("#newsId").val());
	}
	
	var newsTitle = -1;
	if($("#newsTitle").val() != "") {
		newsTitle = $("#newsTitle").val();
	}
	
	var category = -1;
	if($("#category_id").val() != "") {
		category = Number($("#category_id").val());
	}
	
	var queryType = "startEndDate";
//	var queryType = -1
//	if($("#queryType").val() != ""){
//		queryType = $("#queryType").val();
//	}
	
	var startDate = $("#start_date").val();
	var endDate = $("#end_date").val();

	var request_data =	{
							"method":"queryNewsList",
							"args":{
								"startDate":startDate,
								"endDate":endDate,
								"newsId":newsId,
								"newsTitle":newsTitle,
								"category":category,
								"queryType":queryType,
								"queryLimit":0, 
								"page":crnPage ,// 當前第X頁
								"perpage":row
							}
						};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	//API呼叫正確
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1)
		{	
			if(Object.keys(result_content.newList).length==0){
				$(".blockUIImgs").hide();
				$(".blockUIMsgs").html("查詢範圍內沒有資料。");
				$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
			}else{
				show_News(result_content, ["項次","類型","標題","公告期間","最後更新者",'功能操作'], PAGE_TABLE);
			$.unblockUI();
			}
		}
		else
		{
			$(".blockUIImgs").hide();
			$(".blockUIMsgs").html("查詢發生錯誤，訊息為：" + result_content.msg);
			$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
			//MSG.alertMsgs("check", "查詢發生錯誤，訊息為：" + result_content.msg, 0);
			return 0;
		}
	}
	else
	{
		$(".blockUIImgs").hide();
		$(".blockUIMsgs").html("查詢發生無法預期的錯誤，錯誤訊息為：" + result_content.msg);
		$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
		//MSG.alertMsgs("check", "查詢發生無法預期的錯誤，錯誤訊息為：" + result_content.msg, 0);
		return 0;
	}
	
}
function show_News(result_content,header,tableName)
{
//清單內容
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<thead><tr align='center'>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr></thead>";
	var td_class="";
	for(var i in result_content.newList) {
		if(i%2==0){
			td_class =  "align='center'";
		}else{

			td_class =  "align='center'";
		}
		
		var newsId = "";
		var newsTitle = "";
		var content = "";
		var publishDate = "";
		var startDate = "";
		var endDate = "";
		var modifyDate = "";
		var category = "";
		var modifyUser = "";
		
		if(result_content.newList[i].newsId!=null){newsId=result_content.newList[i].newsId;}
		if(result_content.newList[i].newsTitle!=null){newsTitle=result_content.newList[i].newsTitle;}
		if(result_content.newList[i].category!=null){category=result_content.newList[i].category;}
		if(result_content.newList[i].startDate!=null ){startDate=result_content.newList[i].startDate;}
		if(result_content.newList[i].endDate!=null ){endDate=result_content.newList[i].endDate;}
		if(result_content.newList[i].modifyDate!=null){modifyDate=result_content.newList[i].modifyDate;}
		if(result_content.newList[i].modifyUser!=null){modifyUser=result_content.newList[i].modifyUser;}
		
		var comboText = timeConverter(modifyDate,"datetime");
		if(modifyUser != null && modifyUser != ""){
			comboText = modifyUser + ", " + comboText;
		}
		
		var categoryText = "";
		if($("#detailForm #category_id option[value='"+category+"']")){
			categoryText = $("#detailForm #category_id option[value='"+category+"']").text();
		}
		
		//	["項次","類型","標題","公告期間","最後更新者",'功能操作']
		var strTR = "<tr>";
		strTR += "<td "+ td_class +  ">" + newsId + "</td>";
		strTR += "<td "+ td_class +  ">" + categoryText + "</td>";
		strTR += "<td "+ td_class +  ">" + toShortString(newsTitle,20) + "</td>";
		strTR += "<td "+ td_class +  ">" + timeConverter(startDate) + " ~ " + timeConverter(endDate)+ "</td>";
		strTR += "<td "+ td_class +  ">" + comboText + "</td>";
		strTR += "<td width=\"15%\"><button class=\"btn btn-primary\" style=\"min-width:48%\" onclick=editNewsDetail(" + newsId + ")><i class=\"fa fa-pencil\"></i></button> ";
		strTR += "<button class=\"btn btn-primary\" style=\"min-width:48%\" onclick=removeNews(" + newsId + ")><i class=\"fa fa-trash-o\"></i></button></td>";
		strTR += "</tr>";
		html += strTR;
	}
	$("#" + tableName).html(html);
	
	
//	$('td[dt="group1"]').rowspan();
	
	$("#" + tableName).hide();
	$("#" + tableName).show("slow");
	$("#MAIN_TITLE_BAR").show("slow");
	$("#tiltleAct").show("slow"); //匯出功能

//資料筆數
	$("#query_rule").html("");
	$("#query_rule").html("資料筆數:" +result_content.totalCol );
//頁數管理	
	var pageHtml = "";
	//資料筆數
	pageHtml  += "資料筆數:" +result_content.totalCol +  "筆"  ;
	pageHtml  += " | 第  "+ crnPage +" 頁 | ";
//總共頁數
var totalPage  = 0; //分頁若整除，不需+1
if(result_content.totalCol % row == 0){
totalPage = parseInt(result_content.totalCol / row ,10);
}else{
	totalPage = parseInt(result_content.totalCol / row ,10)+1;}
	pageHtml  += "共 "+ totalPage +" 頁 | ";
//頁面計算
	pageHtml += '前往<select style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="query_News_List(this.value)"> |';
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

//新增suuplier
function addNews() {
	$('#detailForm').css('margin-top',0); //沒加這行 上方會有空白
	$('#listForm').hide();
	$('#detailForm').show("slow");
	$('#detailForm :input:not(:button)').val('');
	$('#companyId').attr('readonly', false);
	
	//清空群組資料
	$(".chosen-select").val('').trigger("chosen:updated");
	
	queryGroup();
	$('#detailForm').find('#group_id').val(["1"]);//預設 公共群組
	$("#group_id").chosen();
}

function export_excel(){
	var newsId = 0;
	if($("#newsId").val() != "") {
		newsId = $("#newsId").val();
	}
	
	var newsTitle = 0;
	if($("#newsTitle").val() != "") {
		newsTitle = $("#newsTitle").val();
	}
	
	var category = 0;
	if($("#category_id").val() != "") {
		category = $("#category_id").val();
	}
	
	var queryType = "startEndDate";
//	var queryType = 0;
//	if($("#queryType").val() != ""){
//		queryType = $("#queryType").val();
//	}
	
	var startDate = $("#start_date").val();
	var endDate = $("#end_date").val();
	
	startDate = startDate.replace("/", "-");
	startDate = startDate.replace("/", "-");
	
	endDate = endDate.replace("/", "-");
	endDate = endDate.replace("/", "-");
	
	var link = "/cateringservice/rest/API/XLS/newslist&" + startDate + "&"+ endDate + "&"+ newsId + "&" + newsTitle + "&" + category + "&" + queryType + "&0";
	
	//alert(link);
	
	window.open(link,"_blank");
}

function timeConverter(UNIX_timestamp,type){
	if(UNIX_timestamp == null){
		return "";
	}
//	var a = new Date(UNIX_timestamp * 1000);
	var a = new Date(UNIX_timestamp);
//	var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
	var year = a.getFullYear();
//	var month = months[a.getMonth()];
	var month = "0" + (a.getMonth() + 1);
	var date = "0" + a.getDate();
	var hour = "0" + a.getHours();
	var min = "0" + a.getMinutes();
	var sec = "0" + a.getSeconds();

	if(type == "datetime"){
		var time = year + "/" + month.substr(-2) + '/' + date.substr(-2) + ' ' + hour.substr(-2) + ':' + min.substr(-2) + ':' + sec.substr(-2) ;
	} else {
		var time = year + "/" + month.substr(-2) + '/' + date.substr(-2);
	}
//	var time = year + "/" + month.substr(-2) + '/' + date.substr(-2) + ' ' + hour.substr(-2) + ':' + min.substr(-2) + ':' + sec.substr(-2) ;
	return time;
}

//回上頁
function goBack() {
	$('#detailForm :input:not(:button)').val('');
	$('#detailForm').hide();
	$('#listForm').show("slow");
	$('#companyId').css({"color":""});
}


function checkSpecialCharacters(){
	//來源網址 可能有中文 還是要存起來,所以排除sourceLink
	var inputFields = $("table[id='table']").find("input[type=text]").not( document.getElementById( "sourceLink" ) );
	var inputFieldsCount = $("table[id='table']").find("input[type=text]").length;
	for(i = 0; i < inputFieldsCount ; i++){
		if(inputFields[i] != undefined){
			if(!isValidStr(inputFields[i].value)){
				alert('欄位名稱不可包含特殊字元 \" \' % ; &');
				return true;
			}
		} 
//		else {
//			console.log(inputFields);
//			console.log(i);
//		}
	}
}
//儲存News detail 資料
function saveNewsDetail() {
//	if(checkSpecialCharacters()){
//		return;
//	}
	var msg = validateInput();
	if(msg){
		MSG.alertMsgs("check", msg, 0);
		return;
	}
	
	var mode = '';
	if ($('#detailForm').find("input[id=newsId]").val())
		mode = 'UPDATE';
	else
		mode = 'ADD';
	
	//20151023 shine add end 同步更新其下餐廳的供應商
	
	var newsContent = $('#detailForm').find('#newsContent').val();
	newsContent = newsContent.replace(/\r\n/g,"<br/>")
	
	var request_data = {
		"method" : "UpdateNewsDetail",
		"args" : {
			"activeType" : mode,
			"newsBo" : {
				"newsId" : $('#detailForm').find('#newsId').val(),
				"newsTitle" : $('#detailForm').find('#newsTitle').val(),
				"priority" : $('#detailForm').find('#priority').val(),
				"startDate" : $('#detailForm').find('#start_date').val(),
				"endDate" : $('#detailForm').find('#end_date').val(),
				"groupIdList" : $('#detailForm').find('#group_id').val(),
				"category" : $('#detailForm').find('#category_id').val(),
				"content" : newsContent,
				"file" : $('#detailForm').find('#fileupload').val(),
				"sourceTitle" : $('#detailForm').find('#sourceTitle').val(),
				"sourceLink" : $('#detailForm').find('#sourceLink').val()
			}
		}
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { // success
			//TODO 儲存成功以後,上傳相關附件.(綁一下newsId)
			$("#detailForm #newsId").val(result_content.newsId);
			if($("#fileupload").val()){
				upload();
//				MSG.alertMsgs("checkAndReload", "儲存成功", 0);
			} else {
				MSG.alertMsgs("checkAndReload", "儲存成功", 0);
			}
		} else {
			MSG.alertMsgs("check", "儲存失敗，訊息為：" + result_content.msg, 0);
		}
	} else {
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	}
}

//add by Joshua 2014/11/10 特殊字元判斷
function isValidStr(str){
//	return !/[%&\[\]\\';|\\"<>]/g.test(str);
	return !/[%&\[\]\\';|\\"]/g.test(str);
}

//檢核必填欄位空值
function validateInput(){
	var msg = '';

	if($("#detailForm").find("input[id=newsTitle]").val() === '')
		msg += '請輸入公告標題</br>';
	
	if($("#detailForm").find("textarea[id=newsContent]").val() === '')
		msg += '請輸入公告內容</br>';
	
	if($("#detailForm").find("select[id=category_id]").val() === '')
		msg += '請輸入公告種類</br>';
	
	if($("#detailForm").find("select[id=priority]").val() === '')
		msg += '請輸入優先等級</br>';
	
	if($("#detailForm").find("input[id=start_date]").val() === '')
		msg += '請輸入公告起日</br>';
	
	if($("#detailForm").find("input[id=end_date]").val() === '')
		msg += '請輸入公告訖日</br>';
	
	if($("#detailForm").find("input[id=end_date]").val() !== ''){
		var start =$("#detailForm").find("input[id=start_date]").val();
		var end =$("#detailForm").find("input[id=end_date]").val();
		if( end < start){
			msg += '公告訖日需大於等於公告起日</br>';
		}
	}
	
	if($("#detailForm").find("select[id=group_id]").val() === null)
		msg += '請輸入公告群組</br>';

	return msg;
}

//excel upload
var client = new XMLHttpRequest();
function upload(){
	var newsId = $("#detailForm #newsId").val();
	var file = document.getElementById("fileupload");
	
	var fileNumber = 5;
	if(($("#uploadedFiles tr").length -1) == fileNumber){
		MSG.alertMsgs('check', '附件檔案個數上限:'+fileNumber, 0);
		$("#fileupload").val("");
		return false;
	}
	
//	if(!$("#fileupload").val()){
//		MSG.alertMsgs('check', '請選擇檔案', 0);
//		return false;
//	}/* Create a FormData instance */
	var formData = new FormData();
		  MSG.alertMsgs('loading', '', 0);
	/* Add the file */ 
	formData.append("file", file.files[0]);
	formData.append("func", "NEWS" + ("|"+newsId));
	formData.append("overWrite", "0");
	client.open("post","/cateringservice/file/upload", true);
	client.send(formData);
}

function queryGroup(){
	//request
	var request_data =	{
			"method":"queryGroupList",
			"args":{}
	};
	var response_obj = call_rest_api(request_data);
	
	//alert(response_obj);
	
	//response
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			var new_drop = "<select id=\"group_id\" data-placeholder=\"請選擇至少一個公告的群組...\" style=\"width:350px;height:100%;\" multiple class=\"chosen-select\">";
			
			for(var i=0; i<result_content.group.length; i++) {
				new_drop += "<option value=" + result_content.group[i].groupId + ">" + result_content.group[i].groupName + "</option>";
			}
			new_drop += "</select>";
			$("#dropDown_group").html(new_drop);
			
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
		}		
	} else{
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	} //群組多選
}

//編輯News Detail
function editNewsDetail(newsId){
	$('#detailForm').css('margin-top',0); //沒加這行 上方會有空白
	$('#listForm').hide();
	$('#detailForm').show("slow");
	queryGroup();
	queryNewsIdDetail(newsId); //取得news資訊
	$("#group_id").chosen();
}

//Query news detail
function queryNewsIdDetail(newsId) {
	var response_obj
	//將餐廳進行特別處理
	var request_data = {
		"method" : "queryNewsDetail",
		"args" : {
			"newsId" : newsId
		}
	};
	response_obj = call_rest_api(request_data);

	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) { // success
			var newsBo = result_content.newsBo;
			if (newsBo)
				assignInputValue(newsBo);
			
			queryNewsAttachFiles(newsId); //取得附件清單
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
		}
	} else {
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	}
}

function assignInputValue(newsBo) {
	$(".chosen-select").val('').trigger("chosen:updated");

	$('#detailForm').find('#newsId').val(newsBo.newsId);
	$('#detailForm').find('#newsTitle').val(newsBo.newsTitle);
	$('#detailForm').find('#priority').val(newsBo.priority);
	$('#detailForm').find('#start_date').val(newsBo.startDate);
	if(newsBo.endDate != null){
		$('#detailForm').find('#end_date').val(newsBo.endDate);
	}
	$('#detailForm').find('#category_id').val(newsBo.category);
	$('#detailForm').find('#newsContent').val(newsBo.content);
//	$('#detailForm').find('#fileupload').val(newsBo.file); //TODO
	$('#detailForm').find('#group_id').val(newsBo.groupIdList);
	$('#detailForm').find('#sourceTitle').val(newsBo.sourceTitle);
	$('#detailForm').find('#sourceLink').val(newsBo.sourceLink);
	$('#detailForm').find('#publishUser').val(newsBo.publishUser);
//	console.log(newsBo.groupIdList);
//	$('#detailForm').find('#group_id').val(newsBo.groupIdList);
	
}

//刪除News
function removeNews(newsId){
	var answer = confirm("請問是否要刪除公告？");
	if(!answer)
		return;
	
	var request_data = {
			"method" : "deleteNews",
			"args" : {
				"newsId" : newsId
			}
		};
	
		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { // success							
				
				MSG.alertMsgs("check", "刪除成功", 0);
				query_News_List(1);
			} else {
				MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			}
		} else {
			MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		}
}

function toShortString(str,limitlength){
	var shortStr="";
	str = str.replace(/(<([^>]+)>)/ig,"");
	if(str.length > limitlength){
		shortStr = "<div title='"+str+"'>" + str.substring(0,limitlength) +"...</div>";
	}
	else{
		shortStr = "<div title='"+str+"'>" + str + "</div>";
	}
	return shortStr;
}

/* Check the response status */  
client.onreadystatechange  = function() {
	if (client.readyState == 4 && client.status == 200) {
		var obj = JSON.parse(client.responseText);
		if(obj.retStatus == 1){
			if (obj.retMsg==""){
				   MSG.alertMsgs('check', '檔案上傳成功', 0);
//				   MSG.alertMsgs('checkAndReload', '檔案上傳成功', 0);
			}else{
//				MSG.alertMsgs('check', obj.retMsg, 0);
				MSG.alertMsgs('checkAndReload', obj.retMsg, 0);
			}
			 //查詢該newsId的附件資訊,塞入filesDetail
			queryNewsAttachFiles($('#detailForm').find('#newsId').val());
			$("#fileupload").val("");
			$("#overWrite").attr("value", 0);
		} else if (obj.retStatus == 0) {
			   MSG.alertMsgs('check', '檔案上傳失敗，原因為' + obj.retMsg , 0);
			 $("#fileupload").val("");
			 $("#overWrite").attr("value", 0);
		} 
	}
	
//	$("#excel_upload").hide("slow");
};

function queryNewsAttachFiles(newsId){
	var request_data =	{
			"method":"queryNewsAttachList",
			"args":{
				"newsId":newsId
			}
		};
		var FILE_TABLE="uploadedFiles";
	
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
//			console.log(response_obj);
//			console.log(response_obj.result_content);
			show_File(result_content, ["檔名","操作"], FILE_TABLE);
		}
}

function show_File(result_content,header,tableName)
{
//清單內容
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<thead><tr align='center'>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr></thead>";
	
	var td_class="";
	for(var i in result_content.newsattachfilesList) {
		if(i%2==0){
			td_class =  "align='center'";
		}else{
			td_class =  "align='center'";
		}
		
		var fileId = "";
		var fileDesc = "";

		if(result_content.newsattachfilesList[i].id!=null){fileId=result_content.newsattachfilesList[i].id;}
		if(result_content.newsattachfilesList[i].fileDesc!=null){fileDesc=result_content.newsattachfilesList[i].fileDesc;}
		
		var strTR = "<tr>";
		strTR += "<td "+ td_class +  ">" + fileDesc + "</td>";
		strTR += "<td width=\"30%\"> ";
		strTR += "<button class=\"btn btn-primary\" style=\"min-width:20%\" onclick=removeFiles(" + fileId + ")><i class=\"fa fa-trash-o\"></i></button></td>";
		strTR += "</tr>";
		html += strTR;
	}
	$("#" + tableName).html(html);
}

//刪除News
function removeFiles(fileId){
	var answer = confirm("請問是否要刪除此檔案？");
	if(!answer)
		return;
	
	var request_data = {
//			"method" : "deleteNews",
			"method" : "deleteNewsFile",
			"args" : {
				"fileId" : fileId
			}
		};
	
		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { // success							
				queryNewsAttachFiles($('#detailForm').find('#newsId').val());
//				MSG.alertMsgs("checkAndReload", "刪除成功", 0);
				MSG.alertMsgs("check", "刪除成功", 0);
				
			} else {
				MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			}
		} else {
			MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		}
}