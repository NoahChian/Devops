<%@page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="util.jsp"%>
<!-- include js的自訂公用函式    -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/listSupplier.js"></script>

<script>
	
</script>
</head>
<body>
	<!-- 四點設計套版 -->
	<div class="contents-title">供應商清單</div>
      <div class="contents-wrap">
        <div id="listForm"> 
          <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="addSupplier()">新增</button>
          <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="excelUpload()">EXCEL匯入</button>
          <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="exportExcel()">EXCEL下載</button>
          <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="window.location.href='../../images/files/supplierExcelExample.xlsx'">下載範例檔</button>
          	<!-- Excel 上傳 -->
			<div id="excel_upload" class="TAB_TY_B" style="display: none;">
				<form method="post" action="/cateringservice/file/upload" enctype="multipart/form-data">
					<input type="hidden" id="func" name="func" value="supplier" /> <input type="hidden" id="overWrite" name="func" value="0" />
					<table class="component">
						<tr>
							<td class="componetContent2 componentContentRightLine">選擇檔案</td>
							<td class="componetContent2"><input id="file" type="file" name="excelFile"> (上傳檔案格式為Excel檔)</td>
							<!-- <td class="componetContent2"><button class="btn btn-primary" onclick="upload()">上傳檔案</button></td> -->
							<!-- <button>改成<input>才可正常使用。 modify Ellis 20150806 -->
							<td class="componetContent2"><input class="btn btn-primary" type="button" onclick="upload()" value="上傳檔案"></td>
						</tr>
						
					</table>
				</form>
			</div>
          <div class="section-wrap has-border">
            <div class="form-horizontal">
              <div class="form-group">
                <div class="col-xs-6">
                  <input name="searchName" id="searchName" type="text" class="form-control" placeholder="請輸入供應商名稱查詢" />
                </div>
                <button class="btn btn-primary" style="margin-top: 0px" onclick="searchSupplierClick()">查詢</button>
              </div><!-- End of .form-group -->   
              	<!--  供應商清單 -->
				<div id="query_list">			
					<table id="resultTable" class="table table-bordered table-striped"></table>
				</div>
				<h5 class="section-head with-border" style="height:35px;">
					<div id="query_rule" class="TITLE_TXT flo_l"></div>
					<div class="TITLE_TXT flo_l">
						<span id="page"></span>
					</div>
				</h5>
            </div><!-- End of .form-horizontal -->
          </div><!-- End of .section-wrap -->
        </div><!-- End of #listForm -->
          <!-- Detail Page -->
			<div id="detailForm" style="display: none;">
				<button class="btn btn-primary" onclick="goBack()">回前頁</button>
				<input type="hidden" id="supplierId">
				<h5 class="section-head with-border">新增與修改供應商資訊</h5>
		          <div class="section-wrap has-border kitchen-info">
		            <div class="container-fluid">
		              <div class="row">
		                <table class="table table-bordered table-striped" id="table">
						<tr>
							<td>供應商統編 <font color="red">*</font></td>
							<td><input id="companyId" type="text" class="max255"></td>
						</tr>
						<tr>
							<td>供應商名稱 <font color="red">*</font></td>
							<td><input id="supplierName" type="text" class="max255"></td>
						</tr>
						<tr>
							<td>負責人姓名 <font color="red">*</font></td>
							<td><input id="ownner" type="text" class="max255"></td>
						</tr>
						<tr>
							<td >地址 <font color="red">*</font></td>
							<td><input id="address" style="width: 85%" type="text" class="max255"></td>
						</tr>
						<!-- <tr>
							<td rowspan="3">地址 <font color="red">*</font></td>
							<td>縣市別: <select id="county"><option value="0">請選擇</option></select></td>
						</tr>
						<tr>
							<td>市/區別: <select id="area"><option value="0">請選擇</option></select></td>
						</tr>
						<tr>
							<td>地址: <input id="address" style="width: 70%" type="text"></td>
						</tr> -->
						<tr>
							<td>連絡電話 <font color="red">*</font></td>
							<td><input id="tel" type="text" class="max255"></td>
						</tr>
						<tr>
							<td class="BT_IN_BBTER" colspan="2" align="center"><button class="btn btn-primary" onclick="saveSupplierDetail()">儲存</button></td>
						</tr>
					</table>
		              </div><!-- End of .row -->
		            </div><!-- End of .container-fluid -->
		          </div><!-- End of .section-wrap -->
			</div>
        </div>
        
    <!-- 舊團膳套版 -->
    <div style="display:none;">    
	<div id="listForm">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
			<div class="TITLE_TXT flo_l ">供應商清單</div>
			<div class="TITLE_TXT_BBT FL_R">
				<div id="tiltleAct" style="float: right;">
					<a href="#" onclick="addSupplier()" >新增</a>
					<a href="#" onclick="excelUpload()" >EXCEL上傳</a>
					<a href="#" onClick="exportExcel()" >EXCEL下載</a>
					<a href="../../images/files/supplierExcelExample.xlsx" >下載範例檔</a>
				</div>
			</div>
		</div>
		<!-- Excel 上傳 -->
		<div id="excel_upload" class="TAB_TY_B" style="display: none;">
			<form method="post" action="/cateringservice/file/upload" enctype="multipart/form-data">
				<input type="hidden" id="func" name="func" value="supplier" /> <input type="hidden" id="overWrite" name="func" value="0" />
				<table class="component">
					<tr>
						<td class="componetContent2 componentContentRightLine">選擇檔案</td>
						<td class="componetContent2"><input id="file" type="file" name="excelFile"> (上傳檔案格式為Excel檔)</td>
					</tr>
					<tr>
						<td colspan="2" class="BT_IN_BBTER" bgcolor="#ececec" align="center"><input type="button" onclick="upload()" value="上傳檔案"></td>
					</tr>
				</table>
			</form>
		</div>

		<div id="MAIN_SECOND_BAR" class="h_35px lh_35 GRA_ABR">
			<!-- form name="searchForm" method="post" action="../listSupplier/"-->
				<div class="dis_intb SECOND_TXT_INP">請輸入查詢條件︰</div>
				<div class="dis_intb LOGIN_US_PS">
					供應商名稱&nbsp;<input class="hasDatepicker max255" name="searchName" id="searchName" type="text">
				</div>
				<div class="dis_intb mgl_10 BT_IN_BBTER">
					&nbsp;&nbsp;<input type="button" onclick="searchSupplierClick()" value="查詢" type="button">
				</div>
			<!-- /form--->
		</div>
		<br>
		<!--  供應商清單 -->
		<div id="query_list">
			<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
				<div id="query_rule" class="TITLE_TXT flo_l"></div>
				<div class="TITLE_TXT flo_l ">
					<span id="page"></span>
				</div>
			</div>
			<table id="resultTable" class="tableCss2 BT_IN_BBTER "></table>
		</div>
	</div>

	<!-- Detail Page -->
	<div id="detailForm" style="display: none;">
		<input type="hidden" id="supplierId">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
			<div class="TITLE_TXT flo_l ">新增與修改供應商資訊</div>
			<span class="TITLE_TXT_BBT"><a onclick="goBack()" style="float: right;">回前頁</a></span>
		</div>
		<table class="tableCss2" id="table">
			<tr>
				<td>供應商統編 <font color="red">*</font></td>
				<td><input id="companyId" type="text" class="max255"></td>
			</tr>
			<tr>
				<td>供應商名稱 <font color="red">*</font></td>
				<td><input id="supplierName" type="text" class="max255"></td>
			</tr>
			<tr>
				<td>負責人姓名 <font color="red">*</font></td>
				<td><input id="ownner" type="text" class="max255"></td>
			</tr>
			<tr>
				<td >地址 <font color="red">*</font></td>
				<td><input id="address" style="width: 85%" type="text" class="max255"></td>
			</tr>
			<!-- <tr>
				<td rowspan="3">地址 <font color="red">*</font></td>
				<td>縣市別: <select id="county"><option value="0">請選擇</option></select></td>
			</tr>
			<tr>
				<td>市/區別: <select id="area"><option value="0">請選擇</option></select></td>
			</tr>
			<tr>
				<td>地址: <input id="address" style="width: 70%" type="text"></td>
			</tr> -->
			<tr>
				<td>連絡電話 <font color="red">*</font></td>
				<td><input id="tel" type="text" class="max255"></td>
			</tr>
			<tr>
				<td class="BT_IN_BBTER" colspan="2" align="center"><input type="button" onclick="saveSupplierDetail()" value="儲存"></td>
			</tr>
		</table>
	</div>
  </div>
</body>
</html>
