
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="util.jsp" %> <!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- calendar -->
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<%
String uType = "";
String uName = "";
String account = "";
String StrName = "";
String roletype = "";
String county = "";
if(session.getAttribute("account")!=null){
uType = (String) session.getAttribute("uType");
uName = (String) session.getAttribute("uName");
account = (String) session.getAttribute("account");
StrName = (String) session.getAttribute("StrName");
roletype = (String) session.getAttribute("roletype");
county = (String) session.getAttribute("county");}
%>

<script>

$(document).ready(function(){	
	//一開始先將上傳檔案的區域隱藏起來
	$("#excel_upload").hide();
	//將查詢時間預設為當月的開始日與結束日
	defaultYearMonth();
});
var row = 20;
var utype="<%=uType %>";
var MSG= new MsgsProcessing();
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
    formData.append("func", $( "#func" ).val());
    formData.append("overWrite", $("#overWrite").val());
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
    	 	   MSG.alertMsgs('check', '檔案上傳成功', 0);
    	    }else{
     	 	   MSG.alertMsgs('check', obj.retMsg, 0);
    	    } 
        	 $("#file").val("");
        	 //$("#overWrite").attr("value", 0);
        } else if (obj.retStatus == 0) {
  	 	   MSG.alertMsgs('check', obj.retMsg, 0);
        	 $("#file").val("");
        	 //$("#overWrite").attr("value", 0);
        } 
    }
	$("#excel_upload").hide("slow");
	$("#upload_button").show("slow");	//再把上傳按鈕顯示出來
}

/*使用Titan的預設日期選擇function*/
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
/*使用Titan的下載檔案修改為下載食材*/
function export_excel(){

	var startDate = $("#export").attr("startDate");
	var endDate = $("#export").attr("endDate");
	
	startDate = startDate.replace("/", "-");
	startDate = startDate.replace("/", "-");
	
	endDate = endDate.replace("/", "-");
	endDate = endDate.replace("/", "-");
	
	var link = "/cateringservice/rest/API/XLS/schoolingredient&" + startDate + "&" + endDate;
	
	//alert(link);
	
	window.open(link,"_blank");
}





	//calendar
	$(function() {
    	$( "#start_date" ).datepicker({ dateFormat: "yy/mm/dd" });
    	$( "#end_date" ).datepicker({ dateFormat: "yy/mm/dd" });
    	//預設值為先寫死為2013/12/01~2013/12/31
    	//$( "#start_date" ).attr("value", "2013/12/01");
    	//$( "#end_date" ).attr("value", "2013/12/31")
  	});

	
	function showExcelUpload() {	//上傳EXCEL按鈕
		$("#excel_upload").show('slow');
		//$("#query_table").hide();	//還是讓他顯示
		//$("#menu_list").hide();
		$("#upload_button").hide();
		
	}

/**
  * 顯示傳入的菜色資訊
  * 
  * @param	該業者的所有菜色資訊的物件
  * @return	
  * @author	Eason
  * @date	2014/01/06
  */
function show_menu(result_content)
{
	//alert(result_content.menu.length);
	
	var element=document.getElementById("query_rule");
	element.innerHTML = "查詢結果(20筆/頁)  - 查詢菜單筆數：" + result_content.menu.length;
			
			
	var total_page = parseInt(result_content.menu.length / row);
	var add = result_content.menu.length % row;
	
	if(add > 0) total_page++;
		
	if(total_page == 2) {
		$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">第一頁</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">1</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">2</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">下一頁</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">最末頁</a>");
	} else if (total_page == 3) {
		$("#page").append("<a href=\"#\" onClick=\"page(1,3)\">第一頁</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(1,3)\">1</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(2,3)\">2</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(3,3)\">3</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(2,3)\">下一頁</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(3,3)\">最末頁</a>");
	} else if (total_page == 4) {
		$("#page").append("<a href=\"#\" onClick=\"page(1,4)\">第一頁</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(1,4)\">1</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(2,4)\">2</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(3,4)\">3</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(2,4)\">下一頁</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(4,4)\">最末頁</a>");
	} else if (total_page == 5) {
		$("#page").append("<a href=\"#\" onClick=\"page(1,5)\">第一頁</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(1,5)\">1</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(2,5)\">2</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(3,5)\">3</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(2,5)\">下一頁</a>");
		$("#page").append("<a href=\"#\" onClick=\"page(5,5)\">最末頁</a>");
	}
	

	var new_table = "";
	
	//建立一個新的菜單table
	var new_head = '<table class=\"table table-bordered table-striped\" >';
	new_head += "<thead><tr>";
	new_head +=	"<td  >日期</td>" +
					"<td  >菜色</td>" +
					"<td  >選項</td>";
	new_head += 	"</tr></thead>";
	
	/// 對每一天的菜單產生一行來顯示 ///
	for(var i = 0 ; i<result_content.menu.length; i++)
	{
		
		if(i %2 ==0)
		{
			new_table += "<tr>";
			new_table += "<td  >"+ result_content.menu[i].date +"&nbsp;</td>" +	
						 "<td  >"+ result_content.menu[i].description +"</td>" ;
			if (utype!="007"){	
				new_table += "<td ><a class=\"btn btn-primary\" style=\"margin:0\" href=\"../ingredients_detail/?menuDate="+ result_content.menu[i].date +"\">修改</a></td>";
			}
			new_table += "</tr>";
		}
		else
		{
			new_table += "<tr>";
			new_table += "<td>"+ result_content.menu[i].date +"</td>" +	
						 "<td>"+ result_content.menu[i].description +"</td>" ;
			if (utype!="007"){			 
				new_table +=	"<td ><a class=\"btn btn-primary\" style=\"margin:0\" href=\"../ingredients_detail/?menuDate="+ result_content.menu[i].date +"\">修改</a></td>";
			}
			new_table += "</tr>";
			/*
			new_table += "<tr>";
			new_table += "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].menuDate +"</td>" +
						 "<!-- <td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].schoolName +"</td> -->" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].main +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].major +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].major1 +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].major2 +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].side1 +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].side2 +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].side3 +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].side4 +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].vegetable +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].soup +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">"+ result_content.menu[i].calorie +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componetContent2 BT_IN_BBTER\"><a href=\"../ingredients_detail/?mid="+ result_content.menu[i].mid +"&menuDate="+ result_content.menu[i].menuDate +"\">修改</a></td>";
						 // "<td class=\"componetContent2 componentContentRightLine\"><input type=\"button\" align=\"right\" value=\"食材維護\" onclick=\"self.location.href='../ingredients_detail/?mid="+ result_content.menu[i].mid +"&menuDate="+ result_content.menu[i].menuDate +"'\"/></td>";
			new_table += "</tr>";*/
		}
		
		//每20筆放入不同DIV
		if((i+1) % row == 0){
			var page = (i+1) / row;
			if(page == 1){
				$("#menu_list1").append(new_head +new_table + "</table>");
				new_table = "";
			} else if (page == 2) {
				$("#menu_list2").append(new_head + new_table + "</table>");
				new_table = "";
			} else if (page == 3) {
				$("#menu_list3").append(new_head + new_table + "</table>");
				new_table = "";
			} else if (page == 4) {
				$("#menu_list4").append(new_head + new_table + "</table>");
				new_table = "";
			} else if (page == 5) {
				$("#menu_list5").append(new_head + new_table + "</table>");
				new_table = "";
			}
		}
		if((i+1) == result_content.menu.length && result_content.menu.length % row != 0){
			var menu = "menu_list" + total_page;
			$('#'+menu).append(new_head + new_table + "</table>");
			new_div = "";
		}
		
	}
	
	//new_table +=	"</table>";
	
	//將動態產生的table加上去
	$("#menu_list").html("");			//先清除上次的查詢內容
	$("#menu_list1").show();
	$("#menu_list2").hide();
	$("#menu_list3").hide();
	$("#menu_list4").hide();
	$("#menu_list5").hide();
	$("#menu_list").append(new_table);
	
}

function page(now, last){
	
	$("#page").html("");
	
	for(var i=1; i<=10; i++){	//hide all
		$("#menu_list" + i).hide();
	}
	
	$("#menu_list" + now).show("slow");
	
	if(last == 2){
		if(now == 1){
			$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">第一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">1</a>");
			$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">2</a>");
			$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">下一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">最末頁</a>");
		} else if (now == 2){
			$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">第一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">上一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">1</a>");
			$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">2</a>");
			$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">最末頁</a>");
		}
	} else {
		if(now == last){
			$("#page").append("<a href=\"#\" onClick=\"page(1,"+last+")\">第一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now-1)+","+last+")\">上一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now-2)+","+last+")\">"+(now-2)+"</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now-1)+","+last+")\">"+(now-1)+"</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+now+","+last+")\">"+now+"</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+last+","+last+")\">最末頁</a>");
		} else if (now == 1){
			$("#page").append("<a href=\"#\" onClick=\"page(1,"+last+")\">第一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now)+","+last+")\">"+(now)+"</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now+1)+","+last+")\">"+(now+1)+"</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now+2)+","+last+")\">"+(now+2)+"</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now+1)+","+last+")\">下一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+last+","+last+")\">最末頁</a>");
		} else {
			$("#page").append("<a href=\"#\" onClick=\"page(1,"+last+")\">第一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now-1)+","+last+")\">上一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now-1)+","+last+")\">"+(now-1)+"</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+now+","+last+")\">"+now+"</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now+1)+","+last+")\">"+(now+1)+"</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+(now+1)+","+last+")\">下一頁</a>");
			$("#page").append("<a href=\"#\" onClick=\"page("+last+","+last+")\">最末頁</a>");
		}
	}
	
}

	
/**
 * 查詢該業者期間內的菜單內容
 * 
 * @param	
 * @return	
 * @author	Eason
 * @date	2013/12/18
 */
function query_menu()
{
	/// 1.取得欲查詢的時間區間 ///
	var start_date = $("#start_date").val();
	//alert(start_date);
	var end_date = $("#end_date").val();
	//alert(end_date);
	
	//檢查查詢期間不得大於90天       20140219 KC
	if (!util_check_date_range(new Date(start_date),new Date(end_date))){
		return;
	}
	
	
	$("#page").html("");
	$("#menu_list1").html("");
	$("#menu_list2").html("");
	$("#menu_list3").html("");
	$("#menu_list4").html("");
	$("#menu_list5").html("");
	
	/// 2.新增一個匯出資料的button ///
	var new_button = '<input id="export" class=\"btn btn-primary\" style=\"margin:0; margin-top: 15px;\" type="button" value="匯出Excel" enddate="'+ end_date +'" startdate="'+ start_date +'" onclick="export_excel()"></input>';
	//new_button +=	"</table>";
	//<input id="export" type="button" value="匯出" enddate="2014/1/31" startdate="2013/12/02" schoolid="14724" onclick="export_excel()"></input>
	
	//將動態產生的table加上去
	$("#DL_button").html("");			//先清除上次的查詢內容
	$("#DL_button").append(new_button);
	
	/// 3.查詢菜單內容 ///
	var request_data =	{
							"method":"queryMenuDescByTime",
							"args":{
								"startDate":start_date,
						          "endDate":end_date
							}
						};
	
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	//API呼叫正確
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1)
		{	//查詢菜單正確
			/// 4.顯示查詢到的菜單資訊 ///
			show_menu(result_content);
		}
		else
		{
			 $.blockUI({  theme:     true, message:  $('#uploading') });
				$("#uploading").html("");
		    	$("#uploading").html("查詢業者期間的菜單發生錯誤，訊息為："+ result_content.msg);
		    	$("#uploading").append("<br><input type=\"button\" onclick=\"$.unblockUI();\" value=\"確定\">");
			return 0;
		}
	}
	else
	{
		 $.blockUI({  theme:     true, message:  $('#uploading') });
			$("#uploading").html("");
	    	$("#uploading").html("查詢業者期間的菜單發生無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	    	$("#uploading").append("<br><input type=\"button\" onclick=\"$.unblockUI();\" value=\"確定\">");
		return 0;
	}
	
}	
	

</script>
</head>
<body>
	<div style="display:none; text-align:center;" id="uploading" ></div>
	<div class="contents-title">食材資料維護</div>
	<div class="contents-wrap">
		<a class="btn btn-primary" style="margin: 0px 10px 10px 0px;" href="#" id="upload_button" onclick="showExcelUpload()">食材Excel匯入</a>
		<a class="btn btn-primary" style="margin: 0px 10px 10px 0px;" href="../../images/files/ingredientExcelExample.xlsx">下載範例檔</a>
		<div id="excel_upload" class="TAB_TY_B">
			<form method= "post" action ="/cateringservice/file/upload" enctype="multipart/form-data" >
				<input type="hidden" id="func" name ="func" value="Ingredient"/>
	    		<table class="table table-bordered table-striped">
	    			<thead>
						<tr>
							<td colspan="2" align="center" class="componentTitle TIT_A">上傳食材資料</td>
						</tr>
					</thead>
					<tr>
						<td class="componetContent2 componentContentRightLine">選擇食材檔案</td>
						<td class="componetContent2">
							<input id="file" type="file" name="excelFile">    (上傳檔案格式為xlsx檔，大小限制2MB。)
							<a target="_blank" href="http://175.98.115.58/files/FileFormatConverters.exe">點此下載Excel轉檔套件</a></p>
						
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input class="btn btn-primary" style="margin-top: 0px" type="button" onclick="upload()" value="上傳檔案">
						</td>
					</tr>
				</table>
			</form>
		</div>
		<h5 id="query_rule" class="section-head with-border">請輸入查詢條件</h5>
		<div class="TITLE_TXT_BBT FL_R">
			<div id="page"></div>
		</div>
		<div class="section-wrap has-border">
			<div class="form-horizontal kitchen-info">
				<h6>請輸入日期區間查詢：</h6>
	           	<div class="inline-wrap datetimepicker-start" style="width: 30%">
	                <input type="text" class="form-control inline datetimepicker" id="start_date">
	            </div>
	            <div class="inline-wrap datetimepicker-to" style="margin-bottom: 2px">to</div>
	            <div class="inline-wrap datetimepicker-end" style="width: 30%; margin-right: 5px;">
	            	<input type="text" class="form-control inline datetimepicker" id="end_date">
	            </div>
	            <input class="btn btn-primary" style="margin: 0 0 2px 0" onclick="query_menu()" value="查詢" type="button"> 
				<div id="DL_button" class="form-group col-xs-12"></div>	
				<div id="menu_list1"></div>
				<div id="menu_list2"></div>
				<div id="menu_list3"></div>
				<div id="menu_list4"></div>
				<div id="menu_list5"></div>
			</div>
		</div>
	</div>
</body>