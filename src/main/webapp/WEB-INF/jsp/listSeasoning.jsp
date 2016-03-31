<%@page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="util.jsp"%>
<!-- include js的自訂公用函式    -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/listSeasoning.js"></script>

<script>
	
</script>
</head>
<body>
	<div id="listForm">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
			<div class="TITLE_TXT flo_l ">調味料清單</div>
			<div class="TITLE_TXT_BBT FL_R">
				<div id="tiltleAct" style="float: right;">
					<a href="#" onclick="addSeasoning()" >新增</a>
					<%--
					<a href="#" onclick="excelUpload()" >EXCEL上傳</a>
					<a href="#" onClick="exportExcel()" >EXCEL下載</a>
					<a href="../../images/files/seasoningExcelExample.xlsx" >下載範例檔</a>
					 --%>
				</div>
			</div>
		</div>
		<%--
		<!-- Excel 上傳 -->
		<div id="excel_upload" class="TAB_TY_B" style="display: none;">
			<form method="post" action="/cateringservice/file/upload" enctype="multipart/form-data">
				<input type="hidden" id="func" name="func" value="seasoning" /> <input type="hidden" id="overWrite" name="func" value="0" />
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
 		--%>
		<div id="MAIN_SECOND_BAR" class="h_35px lh_35 GRA_ABR">
			<!-- form name="searchForm" method="post" action="../listseasoning/"-->
				<div class="dis_intb SECOND_TXT_INP">請輸入查詢條件︰</div>
			<div class="dis_intb LOGIN_US_PS">
				查詢日期&nbsp;<input type="text" id="search_date" class="dateMode"/>
			</div>
			<div class="dis_intb LOGIN_US_PS">
					調味料名稱&nbsp;<input class="hasDatepicker max255" name="searchName" id="searchName" type="text">
				</div>
				<div class="dis_intb mgl_10 BT_IN_BBTER">
					&nbsp;&nbsp;<input type="button" onclick="searchSeasoningClick()" value="查詢" type="button">
				</div>
			<!-- /form--->
		</div>
		<br>
		<!--  調味料清單 -->
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
		<input type="hidden" id="seasoningId">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
			<div class="TITLE_TXT flo_l ">新增與修改調味料資訊</div>
			<span class="TITLE_TXT_BBT"><a onclick="goBack()" style="float: right;">回前頁</a></span>
		</div>
		<table class="tableCss2" id="table">
			<tr>
				<td>調味料名稱 <font color="red">*</font></td>
				<td><input id="ingredientName" type="text" class="max255"></td>
				<td>進貨日期<font color="red">*</font></td>
				<td><input id="stockDate" type="text"class="dateAD dateMode"></td>
			</tr>
			<tr>
				<td>生產日期 </td>
				<td><input id="manufactureDate" type="text" class="dateAD max10"></td>
				<td>有效日期 </td>
				<td><input id="expirationDate" type="text" class="dateAD max10"></td>
			</tr>
			<tr>
				<td>開始使用日期<font color="red">*</font></td>
				<td><input id="usestartdate" type="text" class="dateAD max10"></td>
				<td>結束使用日期<font color="red">*</font> </td>
				<td><input id="useenddate" type="text" class="dateAD max10"></td>
			</tr>
			<tr>
				<td>供應商 <font color="red">*</font></td>
				<td><span id="supplierListV"></span></td>
				<td>製造商 </td>
				<td><input id="manufacturer" type="text" class="max255"></td>
			</tr>
			<tr>
				<td>認證標章</td>
				<td><span id="sourceCertificationV"></span></td>
				<td>認證號碼</td>
				<td><input id="certificationId" type="text" class="max255"></td>
			</tr>
			<tr>
				<td>批號</td>
				<td><input id="lotNumber" type="text" class="max255"></td>
				<td>產品名稱</td>
				<td><input id="productName" type="text" class="max255"></td>
			</tr>
			<tr>
				<td>重量</td>
				<td><input id="ingredientQuantity" type="text" class="max255"></td>
				<td>單位</td>
				<td><span id="ingredientUnitV"></span></td>
			</tr>
			<tr>
				<td>屬性</td>
				<td colspan='3'><span id="ingredientAttr"></span></td>
			</tr>
			<tr>
				<td class="BT_IN_BBTER" colspan="2" align="center">
				<input type="hidden" id="seasoningstockId" name ="seasoningstockId" value=""/>
				<input type="hidden" id="dishId" name ="dishId" value=""/>
				<input type="hidden" id="kitchenId" name ="kitchenId" value=""/>
				<input type="hidden" id="ingredientId" name ="ingredientId" value=""/>
				<input type="button" onclick="saveSeasoningDetail()" value="儲存"></td>
			</tr>
		</table>
	</div>
</body>
</html>