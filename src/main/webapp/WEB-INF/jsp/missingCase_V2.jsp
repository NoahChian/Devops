<%@ page import="org.iii.ideas.catering_service.util.AuthenUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="util.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- by Chu 151109-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script src="../../js_ca/combobox.js"></script>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/missingCase_V2.js" async></script>
<script src="../../js_ca/alasql/alasql.min.js"></script>
<script src="../../js_ca/alasql/xlsx.core.min.js"></script>


<%
		Integer countyId = 0;
		String countyType = "";
		String uType = "";
		if(session.getAttribute("account")!=null){
			countyId = AuthenUtil.getCountyNumByUsername(session.getAttribute("uName").toString());
			countyType = AuthenUtil.getCountyTypeByUsername(session.getAttribute("uName").toString());
			uType = (String) session.getAttribute("uType");
		}
%>

</head>
<script>
$(document).ready(function(){
	queryCounties();
	querySchool();
	$("#select_counties").parent().find(".custom-combobox").find('input').blur(function(){querySchool()});
});

function queryCounties(){
	var request_data =	{
			"method":"QueryCounties",
 				"args":{
 					"schoolId":$("#_schoolIdList").val(),
 					"condition": 0
 				}
			};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1){
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			var drop_down_counties = "<select id=\"select_counties\" onblur=\"querySchool()\">";
			if(result_content.counties.length > 1){
				drop_down_counties += "<option value='0'>請輸入縣市</option>"; 
			}
			for(var i=0; i<result_content.counties.length; i++) {
				var tempData = result_content.counties[i];
				drop_down_counties += "<option value=" + tempData.cid + ">" + tempData.countiesName + "</option>";
			}
			drop_down_counties += "</select>";
			$("#dropDown_counties").append(drop_down_counties);
			$("#select_counties").combobox();
			$("#select_counties").parent().find(".custom-combobox").find('input').css("width", "100%");
			
			//如果只有一個選項就鎖死
			if(result_content.counties.length ==1){
				$( "#select_counties" ).parent().find("input").attr('disabled', true);
			}else {
				$( "#select_counties" ).parent().find("input").attr('disabled', false);
			}
		}else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	}else{
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}

function querySchool(){
	var counties = $("#select_counties").val();
	console.log(counties);
	var request_data =	{
			 "method":"querySchoolList",
				"args":{
					"cid": counties, //由API控制送出的權限判定
					"page":1 ,// 要一次拉出所有學校清單
					"perpage":9000, //寫死9000間學校
					"schoolId":$("#_schoolIdList").val()
				}
			};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			var drop_down_school = "<select id=\"select_school\" >";
			if(result_content.school.length > 1){
				drop_down_school += "<option value='0'>請輸入學校</option>";
			}
			for(var i=0; i<result_content.school.length; i++) {
				drop_down_school += "<option value=" + result_content.school[i].sid + ">" + result_content.school[i].schoolName + "</option>";
			}
			drop_down_school += "</select>";
			$("#dropDown_school").html("");
			$("#dropDown_school").append(drop_down_school);
			$("#select_school").combobox();
			$("#select_school").parent().find(".custom-combobox").find('input').css("width", "100%");
			
			//如果只有一個選項就鎖死
			if(result_content.school.length ==1){
				$("#select_school").parent().find("input").attr('disabled', true);
			}else {
				$("#select_school").parent().find("input").attr('disabled', false);
			}
		}else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	}else{
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}


</script>

<body>
<!-- 四點設計套版 -->
	<div class="contents-title">團膳缺漏報表</div>
		<div class="contents-wrap">
	        <div class="section-wrap has-border query"> <!-- style=" height: 230px;" -->
	        <div class="container-fluid">
	        	<div class = "row">
	        		<table class="table-bordered table-striped" width="100%">
	        			<tbody>
	        				<tr>
		        				<td style="padding: 10px;" align="center">縣市：</td>
		        				<td style="padding: 10px;"> <span id="dropDown_counties"></td>
	        				</tr>
	        					<td style="padding: 10px;" align="center">學校：</td>
		        				<td style="padding: 10px;" > <span id="dropDown_school"></td>
	        				<tr>
	        					<td style="padding: 10px;" align="center">請輸入日期區間查詢：</td>
	        					<td style="padding: 10px;" >
	        						<div class="inline-wrap">
	        						<select style="margin-right: 10px" id="ddlDateRange" onChange="change_date_range()">
	        							<option value="7days">最近7天</option>
										<option value="30days">最近30天</option>
										<option value="this.month">本月</option>	
	        						</select>
	        						</div>
	        						<div class="inline-wrap datetimepicker-start" style="width: 35%">
	        							<input type="text" class="form-control inline datetimepicker" id="start_date">
	        						</div>
	        						<div class="inline-wrap datetimepicker-to" style="margin-bottom: 0px">to</div>
	        						<div class="inline-wrap datetimepicker-end" style="width: 35%; margin-right: 5px;">
		                    			<input type="text" class="form-control inline datetimepicker" id="end_date">
		                    		</div>
	        					</td>
	        				</tr>
	        				<tr>
	        					<td style="padding: 5px;" colspan="2" align="center">
	        						<input class="btn btn-primary" style="margin: 0 0 2px 0" onclick="loading()" id="search" value="查詢" type="button">
	        					</td>
	        				</tr>
	        			</tbody>
	        		</table>
	        	</div> <!-- end of row -->
	        </div> <!-- end of fluid -->
	        </div><!-- End of .section-wrap -->
	        <div class="section-wrap has-border" id="result" style="margin-top: 10px;">
	            <div id="query_list">
					<div align="right">
						<button id="table2Ex" button type="button" class="btn btn-primary" value="XLSX" onclick="export_excel()" style="margin-top: 0px;"><span class="glyphicon glyphicon-hand-down" aria-hidden="true"></span>&nbsp匯出</button> 
					</div>
	            	<table id="resultTable0" class="table table-bordered table-striped"></table>
					<table id="resultTable" class="table table-bordered table-striped"></table>
					<table id="resultTable1" class="table table-bordered table-striped"></table>
					<table id="resultTable2" class="table table-bordered table-striped"></table>
					<table id="resultTable3" class="table table-bordered table-striped"></table>
				</div>
	        </div>        
	    </div>
       
	<!-- 舊團膳套版 -->
	<div style="display:none">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
			<div class="TITLE_TXT flo_l">缺漏資料查詢</div>
		</div>
		<form method="post" id="selectFunction">
			<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B">
				<table width="100%">
					<tbody>
						<tr>
							<td>查詢條件</td>
							<td id="tdQueryCond">
								<div id="divQueryCond" class=" ">
									<span class="class_countMenuNonIngre class_statistic class_orgi">
										日期(開始)&nbsp;<input type="text" id="start_date"  class="dateMode" />
									</span> <span class="class_countMenuNonIngre class_statistic">
										日期(結束)&nbsp;<input type="text" id="end_date"  class="dateMode"/>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" style="text-align: center;">
								<div class="dis_intb mgl_10 BT_IN_BBTER">
									<input onclick="checkingMissingCase()" id="search" value="查詢"
										type="button">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
		<div id="query_list">
			<table id="resultTable" class="tableCss2"></table>
			<table id="resultTable1" class="tableCss2"></table>
			<table id="resultTable2" class="tableCss2"></table>
			<table id="resultTable3" class="tableCss2"></table>
		</div>
	</div>
</body>
</html>