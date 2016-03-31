<%@ page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="util.jsp"%>
<!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
#queryCond input[type=text]{
	width:100px;
}
</style>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/listIngredient2.js"></script>
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
	$(document).ready(function() {
	defaultYearMonth();
	$("#tiltleAct").hide();
	$("#MAIN_TITLE_BAR").hide();
	});
</script>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">食材資料多重查詢(OR)</div>
		<div class="contents-wrap">
	         <div class="section-wrap has-border kitchen-info">
	           <div class="container-fluid">
	             <div class="row">
	             	<div class="form-group">
		             	<label>日期區間：</label>
		             	<div class="inline-wrap datetimepicker-start" style="width: 30%">
	                        <input type="text" class="form-control inline datetimepicker" id="start_date">
	                    </div>
	                    <div class="inline-wrap datetimepicker-to">to</div>
	                    <div class="inline-wrap datetimepicker-end" style="width: 30%; margin-right: 5px;">
	                    	<input type="text" class="form-control inline datetimepicker" id="end_date">
	                    </div>
                    </div>
                    <div class="form-group">
	                    <div id="queryCond">
	                        <div class="cloneCondObj">
                                <div class="form-group">
                                    (<label>供應商名稱: </label><input type="text" class="supplierName" />and
                                    <label>品牌名稱: </label><input type="text" class="brandName" />and
                                    <label>食材名稱: </label><input type="text" class="ingredientName" />
                                    <input class='btn btn-primary' type="button" value="取消" onclick="deleteCond(this);" style="display:none; margin:0" class="deleteButton" />)or
                                </div>
                            </div>
                            <!-- template for 新增使用 不顯示 -->
							<div class="tempelateCondObj cloneCondObj" style="display:none">
								<div class="form-group">
									(<label>供應商名稱: </label><input type="text" class="supplierName" />and
									<label>品牌名稱: </label><input type="text" class="brandName" />and
									<label>食材名稱: </label><input type="text" class="ingredientName" />
									<input class='btn btn-primary' type="button" value="取消" onclick="deleteCond(this);" style="display:none; margin:0" class="deleteButton" />)or
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<input class='btn btn-primary' style='margin:0' type="button" value="新增查詢條件" onclick="addCondObj();"/>
						<input class='btn btn-primary' style='margin:0' type="button" value="送出查詢" onclick="initSubmitQuery('list');"/>
					</div>
					<!--  學校清單 -->
					<div id="query_list">
						<h5 class="section-head with-border" style="height:35px; margin-bottom: 1px;">
							查詢結果| 總筆數：<label id="lblTotalCount"></label> |  目前在  第   <select id="ddlPage" style="color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="changePage(this)"></select>頁
							<div class="BT_IN_BBTER" style="float: right; margin-top: 1px;">
								<a href="#" onclick="chkAndExport('file')">匯出</a>
							</div>
						</h5>
						<table id="resultTable" class="table table-bordered table-striped"></table>
					</div>
					<iframe style="display:none" id="fileDownload"></iframe>

					<!--  學校清單 -->
					<div id="query_list">
						<table id="resultTable" class="table table-bordered table-striped"></table>
					</div>
	             </div><!-- End of .row -->
	           </div><!-- End of .container-fluid -->
	         </div><!-- End of .section-wrap -->
		</div>
	<!-- 舊團膳套版 -->
	<div style="display:none">
		<div id="" class="GRE_ABR h_30px lh_30">
			<div  class="TITLE_TXT flo_l">食材資料多重查詢(OR)</div>
		</div>
		<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B">
			開始日期： <input type="text" id="start_date" /> 結束日期:  <input type="text" id="end_date" />
			<!-- <div id="queryCond">
				<div class="tempelateCondObj cloneCondObj">
					(<label>供應商名稱: </label><input type="text" class="supplierName" />and
					<label>品牌名稱: </label><input type="text" class="brandName" />and
					<label>食材名稱: </label><input type="text" class="ingredientName" />
					<input type="button" value="取消" onclick="deleteCond(this);" style="display:none;" class="deleteButton" />)or
				</div>
			</div> -->
		</div>
		<input type="button" value="新增查詢條件" onclick="addCondObj();"/>
		<input type="button" value="送出查詢" onclick="submitQuery('list');"/>
	
		<!--  學校清單 -->
		
		<div id="query_list">
			<div>
				查詢結果| 總筆數：<label id="lblTotalCount"></label> |  目前在  第   <select id="ddlPage" onchange="changePage(this)"></select>頁
				<a href="#" onclick="submitQuery('file')" style="float: right;">匯出</a>
			</div>
			<table id="resultTable" class="tableCss2"></table>
		</div>
		<iframe style="display:none" id="fileDownload"></iframe>
	</div>
</body>
</html>