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
<script src="../../js_ca/statisticDish.js"></script>

<%
		Integer countyId = 0;
		String countyType = "";
		String uType = "";
		String schoolName = "";
		
		if(session.getAttribute("account")!=null){
			countyId = AuthenUtil.getCountyNumByUsername(session.getAttribute("uName").toString());
			countyType = AuthenUtil.getCountyTypeByUsername(session.getAttribute("uName").toString());
			uType = (String) session.getAttribute("uType");
		}
%>
</head>
<script>

$(document).ready(function(){
	//#11542 管理者,縣市政府才可使用縣市
	queryCounty();
	querySchool();
	defaultYearMonth();
	$("#tiltleAct").hide();
	$("#MAIN_TITLE_BAR").hide();
	
	$("#county_id").parent().find(".custom-combobox").find('input').blur(function(){
		querySchool();
	});
});

</script>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">菜色量統計報表</div>
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
								學校
							</td>
							<td style="padding: 10px;" colspan="3">
								<span id="dropDown_school">
							</td>
						</tr>
						<tr>
							<td style="padding: 10px;" align="center">菜單日期 *</td>
							<td style="padding: 10px;" id="tdQueryCond">
								<div id="divQueryCond" class="lh_35 ">
										<div style="display:none;">
											學校<select id="ddlSchool" ></select>
										</div>
										<span class="class_countMenuNonIngre class_statistic">
											切換範圍&nbsp;
											<select id="ddlDateRange" onChange="change_date_range()">							
												<option value="7days">最近7天</option>
												<option value="30days">最近30天</option>
												<option value="this.month">本月</option>	
												<option value="season1">第一季</option>
												<option value="season2">第二季</option>
												<option value="season3">第三季</option>
												<option value="season4">第四季</option>
											</select>
										</span>
										<div class="class_countMenuNonIngre class_statistic class_orgi inline-wrap">
											日期(開始)&nbsp;<input type="text" id="start_date" class="datetimepicker" />
										</div>
										<div class="class_countMenuNonIngre class_statistic inline-wrap">
											日期(結束)&nbsp;<input type="text" id="end_date" class="datetimepicker"/>
										</div>
				
								</div>
							</td>
<!-- 							<td style="padding: 10px;" colspan="3"> -->
<!-- 								<div class="inline-wrap datetimepicker-start"> -->
<!-- 			                        <input type="text" class="form-control inline datetimepicker" id="start_date"> -->
<!-- 			                   </div> -->
<!-- 					  			<input type="text" id="startDate"  class="dateMode"/>	 -->
<!-- 				                <div class="inline-wrap datetimepicker-to">to</div> -->
<!-- 				      			<div class="inline-wrap datetimepicker-end"> -->
<!-- 			                    	<input type="text" class="form-control inline datetimepicker" id="end_date"> -->
<!-- 			                   </div> -->
<!-- 							</td> -->
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
						<% if ("11".equals(uType) || (uType.length() == 6)) { %>
						<tr>
							<td style="padding: 10px;" align="center">
								縣市
							</td>
							<td style="padding: 10px;" colspan="3">
								<span id="dropDown_county">
							</td>
						</tr>
						<% } %>
						<tr>
							<td bgcolor="#edf2e5" class="" align="center">
								學校
							</td>
							<td bgcolor="#edf2e5" class="" colspan="3">
								<span id="dropDown_school">
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