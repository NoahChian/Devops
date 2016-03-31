<%@ page import="org.iii.ideas.catering_service.util.AuthenUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="util.jsp"%>

<!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js_ca/combobox.js"></script>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/statisticSchool.js"></script>

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
	//#11633 管理者,縣市政府才可使用縣市
	queryCounty();
	queryKitchen();
	defaultYearMonth();
	$("#tiltleAct").hide();
	$("#MAIN_TITLE_BAR").hide();
});

</script>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">學校數量與清單</div>
		<div class="contents-wrap">
	        <div class="section-wrap has-border">
	          <div class="form-horizontal kitchen-info">
				<table class="table-bordered table-striped" width="100%">
					<tbody>
<%-- 						<% if ("11".equals(uType) || (uType.length() == 6)) { %> --%>
						<tr>
							<td style="padding: 10px;" align="center">
								縣市
							</td>
							<td style="padding: 10px;" colspan="3">
								<span id="dropDown_county">
							</td>
						</tr>
<%-- 						<% } %> --%>
						<tr>
							<td style="padding: 10px;" align="center">
								自設廚房
							</td>
							<td style="padding: 10px;" colspan="3">
								<span id="dropDown_kitchen">
							</td>
						</tr>
						<tr>							
							<td style="padding: 10px;" colspan="4" align="center">
								<input class="btn btn-primary" style="margin: 0" type="button" onclick='query_Menu_List(1)' value="查詢">
							</td>
						</tr>
					</tbody>
				</table>
				<h5 class="section-head with-border" style="height:35px; margin-bottom: 10px;">
					<div class="TITLE_TXT flo_l ">查詢結果</div>
					<div id="query_rule" class="TITLE_TXT flo_l"></div>
					<div class="TITLE_TXT flo_l "><span id="page" ></span></div>
					<div class="TITLE_TXT_BBT FL_R">
						<div id="tiltleAct">
							<a href="#" onclick="export_excel()" style="float:right; margin: 0;" >匯出</a> 
						</div>
					</div>
				</h5>	
				<!--  學校清單 -->
				<div id="query_list">
					<table id="resultTable" class="table table-bordered table-striped"></table>
				</div>
		       </div>
	        </div><!-- End of .section-wrap -->        
	    </div>
       
	<!-- 舊團膳套版 -->
	<div style="display:none">
		<div id="" class="GRE_ABR h_30px lh_30">
				<div  class="TITLE_TXT flo_l">菜色量統計報表</div>
			</div>
			<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B">
				<table width="100%">
					<tbody>
						<tr>
							<td style="padding: 10px;" align="center">
								縣市
							</td>
							<td style="padding: 10px;" colspan="3">
								<span id="dropDown_county">
							</td>
						</tr>
						<tr>
							<td bgcolor="#edf2e5" class="" align="center">菜單日期 *</td>
							<td bgcolor="#edf2e5" class=" " colspan="3"><input type="text"
								id="start_date" class="datetimepicker" /> ~ <input type="text"
								id="end_date" class="datetimepicker" /></td>
						</tr>
						<tr>
							<td colspan="4" align="center" class="BT_IN_BBTER"><input
								type="button" onclick='query_Menu_List(1)' value="查詢"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
				<div class="TITLE_TXT flo_l ">查詢結果</div>
				<div id="query_rule" class="TITLE_TXT flo_l"></div>
				<div class="TITLE_TXT flo_l "><span id="page" ></span></div>
				<div class="TITLE_TXT_BBT FL_R">
					<div id="tiltleAct">
						<a href="#" onclick="export_excel()" style="float:right;" >匯出</a> 
					</div>
				</div>
			</div>
			<!--  學校清單 -->
			<div id="query_list">
				<table id="resultTable" class="component BT_IN_BBTER "></table>
			</div>
	</div>
</body>
</html>