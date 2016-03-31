<%@ page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="util.jsp"%>

<!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <script src="../../js_ca/TableRowSpan.js"></script> -->
<script src="../../js_ca/combobox.js"></script>
<script src="../../js_ca/common.js"></script>
<script src="../../js/chosen.jquery.js"></script>
<!-- <script src="../../js/chosen.jquery.min.js"></script> -->
<link href="../../css/chosen.css" type="text/css" rel="stylesheet" />

<!-- <script src="../../js/upload/vendor/jquery.ui.widget.js"></script> -->
<!-- <script src="../../js/upload/jquery.iframe-transport.js"></script> -->
<!-- <script src="../../js/upload/jquery.fileupload.js"></script> -->

<!-- <link href="../../css/upload/dropzone.css" type="text/css" rel="stylesheet" /> -->
<!-- <script src="../../js/upload/myuploadfunction.js"></script> -->

<script src="../../js_ca/listNewsBulletin.js"></script>

<%
	Integer countyId = 0;
	String countyType = "";
	String uType = "";

	if (session.getAttribute("account") != null) {
		countyId = AuthenUtil.getCountyNumByUsername(session.getAttribute("uName").toString());
		countyType = AuthenUtil.getCountyTypeByUsername(session.getAttribute("uName").toString());
		uType = (String) session.getAttribute("uType");
	}
%>
</head>
<script>

$(document).ready(function(){	
<%-- 	<% if ("007".equals(uType) || "101".equals(uType) || "102".equals(uType) || "103".equals(uType)) { %> --%>
// 	queryKitchen();
<%-- 	<% }else{ %> --%>
// 	querySchool();
<%-- 	<% } %> --%>
	defaultYearMonth();
	queryCategory();
	queryGroup();
	query_News_List(1);
		
	$("#tiltleAct").hide();
	$("#MAIN_TITLE_BAR").hide();
	
	document.getElementById("fileupload").onchange = function() {
		saveNewsDetail();
	};
});

</script>
<body>
	<!-- 四點設計套版 -->
	<div class="contents-title">公告管理</div>
	<div class="contents-wrap">
		<div id="listForm" class="section-wrap has-border">
			<div class="form-horizontal kitchen-info">
				<table class="table-bordered table-striped" width="100%">
					<tbody>
						<tr>
							<td style="padding: 10px;" align="center">項次</td>
							<td style="padding: 10px;"><input type="number" id="newsId"
								class="form-control"></td>
							<td style="padding: 10px;" align="center">標題</td>
							<td style="padding: 10px;" colspan="3"><input type="text"
								id="newsTitle" class="form-control"></td>
						</tr>
						<tr>
							<!-- 							<td style="padding: 10px;" align="center"> -->
							<!-- 								日期類型 -->
							<!-- 							</td> -->
							<!-- 							<td style="padding: 10px;"> -->
							<!-- 								<select id="queryType" class="form-control"> -->
							<!-- 									<option value ="startEndDate">公告期間</option> -->
							<!-- 									<option value ="publishDate">發佈日期</option> -->
							<!-- 								<select> -->
							<!-- 							</td> -->

							<td style="padding: 10px;" align="center">種類</td>
							<td style="padding: 10px;"><span id="dropDown_category"></td>

							<td style="padding: 10px;" align="center">日期 <font
								color="red">*</font></td>
							<td style="padding: 10px;" colspan="3">
								<div class="inline-wrap datetimepicker-start">
									<input type="text" class="form-control inline datetimepicker"
										id="start_date">
								</div>
								<div class="inline-wrap datetimepicker-to">to</div>
								<div class="inline-wrap datetimepicker-end">
									<input type="text" class="form-control inline datetimepicker"
										id="end_date">
								</div>
							</td>
						</tr>
						<tr>
							<td style="padding: 10px;" colspan="6" align="center"><input
								class="btn btn-primary" style="margin: 0px 10px 10px 0px;"
								type="button" onclick='query_News_List(1)' value="查詢"> <input
								class="btn btn-primary" style="margin: 0px 10px 10px 0px;"
								type="button" onclick="addNews()" value="新增公告"></td>
						</tr>
					</tbody>
				</table>
				<h5 class="section-head with-border"
					style="height: 35px; margin-bottom: 10px;">
					<div class="TITLE_TXT flo_l ">查詢結果</div>
					<div id="query_rule" class="TITLE_TXT flo_l"></div>
					<div class="TITLE_TXT flo_l ">
						<span id="page"></span>
					</div>
					<div class="TITLE_TXT_BBT FL_R">
						<div id="tiltleAct">
							<a href="#" onclick="export_excel()"
								style="float: right; margin: 0;">匯出</a>
						</div>
					</div>
				</h5>
				<!--  學校清單 -->
				<div id="query_list">
					<table id="resultTable" class="table table-bordered table-striped"></table>
				</div>
			</div>
		</div>
		<!-- End of .section-wrap -->

		<div id="detailForm" style="display: none;">
			<button class="btn btn-primary" onclick="goBack()">回前頁</button>
			<input type="hidden" id="supplierId">
			<h5 class="section-head with-border">新增與修改公告訊息</h5>
			<div class="section-wrap has-border kitchen-info">
				<div class="container-fluid">
					<div class="row">
						<table class="table table-bordered table-striped" id="table">
							<tr>
								<td style="display: none;">發佈者</td>
								<td style="display: none;"><input id="publishUser"
									type="text" class="form-control"></td>
								<td style="display: none;">公告編號</td>
								<td style="display: none;"><input id="newsId" type="number"
									class="form-control"></td>
									
								<td>日期<font color="red">*</font></td>
								<td style="padding: 10px;" colspan="2">
									<div class="inline-wrap datetimepicker-start">
										<input type="text" class="form-control inline datetimepicker"
											id="start_date">
									</div>
									<div class="inline-wrap datetimepicker-to">to</div>
									<div class="inline-wrap datetimepicker-end">
										<input type="text" class="form-control inline datetimepicker"
											id="end_date">
									</div>
								</td>
								<td>優先等級 <font color="red">*</font></td>
								<td><select id="priority" class='form-control'>
										<option value="1">低</option>
										<option value="2">中</option>
										<option value="3">高</option>
										<select></td>
							</tr>
							<tr>
								<td>標題 <font color="red">*</font></td>
								<td colspan="2"><input id="newsTitle" type="text"
									class="max255 form-control"></td>
								<td>公告種類 <font color="red">*</font></td>
								<td><span id="dropDown_category"></td>
							</tr>
							<tr>
								<td>內容 <font color="red">*</font></td>
								<td colspan="4">
									<!-- 							<input id="newsContent" type="text" class="max255 form-control"> -->
									<textarea rows="8" class="form-control" id="newsContent"></textarea>
								</td>
							</tr>
							<tr>
								<td>附件</td>
								<td colspan="2"><input id="fileupload" type="file">
									<table id="uploadedFiles" class="table" style="width: 100%;">
										<tr>
											<th>檔名</th>
											<th>檔案大小</th>
											<!-- 										<th>檔案類型</th> -->
											<!-- 										<th>狀態</th> -->
										</tr>
									</table> <!-- 								<input id="uploaded-file" type="text">								 -->
									<!-- 								<input class="btn btn-primary" type="button" onclick="upload()" value="上傳檔案"> -->
								</td>
								<td>群組<font color="red">*</font></td>
								<td><span id="dropDown_group"></td>
							</tr>
							<tr>
								<td>來源標題</td>
								<td colspan="2"><input id="sourceTitle" type="text"
									class="max255 form-control"></td>
								<td>來源網址</td>
								<td><input id="sourceLink" type="text"
									class="max255 form-control"></td>
							</tr>
							<tr>
								<td class="BT_IN_BBTER" align="center" colspan="5"><button
										class="btn btn-primary" onclick="saveNewsDetail()">儲存</button></td>
							</tr>
						</table>
					</div>
					<!-- End of .row -->
				</div>
				<!-- End of .container-fluid -->
			</div>
			<!-- End of .section-wrap -->
		</div>

		<!-- 舊團膳套版 -->
		<div style="display: none">
			<div id="" class="GRE_ABR h_30px lh_30">
				<div class="TITLE_TXT flo_l">訊息公告管理</div>
			</div>
			<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B">
				<table width="100%">
					<tbody>
						<tr>
							<td bgcolor="#edf2e5" class="" align="center">
								<%
									if ("007".equals(uType) || "101".equals(uType) || "102".equals(uType) || "103".equals(uType)) {
								%> 供餐廚房/餐廳 <%
									} else {
								%> 學校 <%
									}
								%>
							</td>
							<td bgcolor="#edf2e5" class="" colspan="3">
								<%
									if ("007".equals(uType) || "101".equals(uType) || "102".equals(uType) || "103".equals(uType)) {
								%><span id="dropDown_kitchen"> <%
 	} else {
 %> <span id="dropDown_school"> <%
 	}
 %>
							</td>
						</tr>
						<tr>
							<td bgcolor="#edf2e5" class="" align="center">公告期間 *</td>
							<td bgcolor="#edf2e5" class=" " colspan="3"><input
								type="text" id="start_date" class="datetimepicker" /> ~ <input
								type="text" id="end_date" class="datetimepicker" /></td>
						</tr>
						<tr>
							<td colspan="4" align="center" class="BT_IN_BBTER"><input
								type="button" onclick='query_News_List(1)' value="查詢"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
				<div class="TITLE_TXT flo_l ">查詢結果</div>
				<div id="query_rule" class="TITLE_TXT flo_l"></div>
				<div class="TITLE_TXT flo_l ">
					<span id="page"></span>
				</div>
				<div class="TITLE_TXT_BBT FL_R">
					<div id="tiltleAct">
						<a href="#" onclick="export_excel()" style="float: right;">匯出</a>
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