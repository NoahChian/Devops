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
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/missingCaseRate.js"></script>
<script src="../../js_ca/3dpie/amcharts/amcharts.js"></script>
<script src="../../js_ca/3dpie/amcharts/serial.js"></script>
<script src="../../js_ca/3dpie/amcharts/themes/light.js"></script>
<script src="../../js_ca/3dpie/amcharts/plugins/export/export.js"></script>
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

</script>
<style>
#chartdiv {
	width		: 100%;
	height		: 500px;
	font-size	: 8px;
}				
</style>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">上線率日報表</div>
		<div class="contents-wrap">
	        <div class="section-wrap has-border query"> <!-- style=" height: 230px;" -->
	        <div class="container-fluid">
	        	<div class = "row">
	        		<table class="table-bordered table-striped" width="100%">
	        			<tbody>
	        					<td style="padding: 10px;" align="center" width="20%">請選擇學校類別：</td>
	        					<td style="padding: 10px;" width="10%">
	        						<div class="inline-wrap">
<!-- 	        						<select style="margin-right: 10px" id="SchoolType"> -->
<!-- 	        							<option value="1">全部</option> -->
<!-- 										<option value="2">國中</option> -->
<!-- 										<option value="3">國小</option> -->
<!-- 										<option value="4">幼兒園</option>	 -->
<!-- 	        						</select> -->
									<input id='PreCheckbox' type='checkbox' Checked="ture" value="'K', 'W', 'X', 'Y', 'Z' ,">幼兒園<br>
									<input id='EleCheckbox' type='checkbox' Checked="ture" value="6 ,7 ,8 ,">國小<br>
									<input id='JunCheckbox' type='checkbox' Checked="ture" value="5 ,">國中<br>
									<input id='SenCheckbox' type='checkbox' Checked="ture" value="3 ,4 ,">高中   
	        						</div>
	        					</td>
	        					<td style="padding: 10px;" align="center" width="20%">請輸入查詢日期：</td>
	        					<td style="padding: 10px;">
	        						<div class="inline-wrap datetimepicker-start" style="width: 100%">
	        							<input type="text" class="form-control inline datetimepicker" id="start_date" placeholder="請輸入查詢日期">
	        						</div>
	        					</td>
	        				</tr>
	        				<tr>
	        					<td style="padding: 5px;" colspan="4" align="center">
	        						<input class="btn btn-primary" style="margin: 0 0 2px 0" onclick="loading()" id="search" value="查詢" type="button">
	        					</td>
	        				</tr>
	        			</tbody>
	        		</table>
	        	</div> <!-- end of row -->
	        </div> <!-- end of fluid -->
	        </div><!-- End of .section-wrap -->
	        <div class="section-wrap has-border" id="chart" style="margin-top: 10px;">
				<div id="chartdiv"></div>
	        </div>
	        <div class="section-wrap has-border" id="result" style="margin-top: 10px;">
	            <div id="query_list">
		        	<div id="export1" align="right">
						<button type="button" class="btn btn-primary" value="XLSX" onclick="export1()" style="margin-top: 0px;"><span class="glyphicon glyphicon-hand-down" aria-hidden="true"></span>&nbsp匯出</button> 
					</div>
	            	<table id="resultTable0" class="table table-bordered table-striped"></table>
					<div  id="export2" align="right">
					<hr>
						<button type="button" class="btn btn-primary" value="XLSX" onclick="export2()" style="margin-top: 0px;"><span class="glyphicon glyphicon-hand-down" aria-hidden="true"></span>&nbsp匯出</button> 
					</div>
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