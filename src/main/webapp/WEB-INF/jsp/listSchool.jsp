<%@ page import="org.iii.ideas.catering_service.util.AuthenUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="util.jsp"%>
<!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/listSchool.js"></script>
<%
		Integer countyId = 0;
		String countyType = "";
		if(session.getAttribute("account")!=null){
			countyId = AuthenUtil.getCountyNumByUsername(session.getAttribute("uName").toString());
			countyType = AuthenUtil.getCountyTypeByUsername(session.getAttribute("uName").toString());
		}
%>
</head>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">縣市學校清單</div>
      <div class="contents-wrap">
        <div id="tiltleAct">
          <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="show_Sch_detail('add')">新增</button>
          <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="excel_Upload()">EXCEL上傳</button>
          <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="window.location.href='../../images/files/school_ExcelExample.xlsx'">下載範例檔</button>
          	<!-- Excel 上傳 -->
			<div id="excel_upload" class="TAB_TY_B" style="display: none;">
				<form method="post" action="/cateringservice/file/upload" enctype="multipart/form-data">
					<input type="hidden" id="func" name="func" value="SchoolKitchenAccount" />
					<input type="hidden" id="overWrite" name="func" value="0" />
					<table class="component">
						<tr>
							<td class="componetContent2 componentContentRightLine">選擇檔案</td>
							<td class="componetContent2"><input id="file" type="file" name="excelFile"> (上傳檔案格式為Excel檔)</td>
							<td class="componetContent2"><input class="btn btn-primary" onclick="upload()" value="上傳檔案"/></td>
						</tr>
					</table>
				</form>
			</div>
			<!-- 關鍵字搜尋 -->
			<div id="query_bar" class="h_50px lh_35 GRA_ABR" >
					<tr>
						<td>學校名稱︰</td>
						<td><input type="text" id="queryschoolName" name="queryschoolName" class="searchInput max255"></td>
						&nbsp;&nbsp;
						<td><button style="position:relative; bottom: 8px;" class="btn btn-primary" onclick="query_School(1)">查詢</button></td>
					</tr>
			</div>
		</div>
          <div class="section-wrap has-border">
            <div class="form-horizontal">
            	<h5 class="section-head with-border" style="height:35px; margin-bottom: 1px;">
					<div id="query_rule" class="TITLE_TXT flo_l"></div>
					<div class="TITLE_TXT flo_l">
						<span id="page"></span>
					</div>
				</h5> 
              	<!--  學校清單 -->
				<div id="query_list">
					<table id="resultTable" class="table table-bordered table-striped"></table>
				</div>				
            </div><!-- End of .form-horizontal -->
            
            
        <!--  修改學校 新增學校  -->
		<div id="school_detail" class="TAB_TY_B" style="display:none;">
			<button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="goBack()">回前頁</button>
			<div name="theForm">
				<table width="100%" class="table table-bordered table-striped">
					<tbody>
						<tr>
							<td class="componentContentRightLine">縣市別<font color="red">*</font></td>
							<td class="componentContentRightLine"><span name="dropdown_City" id="dropdown_City"></td>
							<td class="componentContentRightLine">市/區別<font color="red">*</font></td>
							<td class="componentContentRightLine"><span name="dropdown_Area" id="dropdown_Area"></td>
						</tr>
						<tr>
							<td class="componentContentRightLine"
								width="13%">學校名稱<font color="red">*</font></td>
							<td class="componentContentRightLine"
								width="37%"><span name="schCountyArea" id="schCountyArea"></span><input
								type="text" name="schoolName" id="schoolName" size="15" value="" class="max240"></td>
							<td class="componentContentRightLine"
								width="13%">學校編號<font color="red">*</font></td>
							<td class="componentContentRightLine">
							<input
								type="text" name="schoolId" id="schoolId" size="30" value="" class="max10">
							<input
								type="hidden" name="sid" id="sid" value=""></td>
						</tr>
						<tr>
							<td class="componentContentRightLine"
								width="90px">新增帳號</td>
							<td class="componentContentRightLine"
								width="230px">
								<input type="checkbox" id="addAccount" onchange="addOrNot()"> 新增廚房帳號<br>
							</td>
							<td class="componentContentRightLine"></td>
							<td class="componentContentRightLine"></td>
						</tr>
						<tr>
							<td class="componentContentRightLine"
								width="90px">帳號類型<font color="red">*</font></td>
							<td class="componentContentRightLine"
								width="230px">
								<select name="sch_type" id="sch_type">
									<option value="006">自設廚房</option>
									<option value="007">受供餐學校</option>
									<option value="009">員生消費合作社</option>
									<option value="101">大專院校</option>
								</select>
							</td>
							<td class="componentContentRightLine"
								width="120px">啟用<font color="red">*</font></td>
							<td class="componentContentRightLine">
								<select name="sch_Enable" id="sch_Enable">
									<option value="1">啟用</option>
									<option value="0">停用</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="componentContentRightLine"
								width="90px">密碼<font color="red">*</font></td>
							<td id="add_pw" class="componentContentRightLine"
								width="230px"><input type="password" name="add_password"
								id="add_password" size="30" value=""  class="max255"></td>
							<td id="upd_pw" class="componentContentRightLine"
								width="230px">(不修改請空白)<input type="password" name="upd_password"
								id="upd_password" size="30" value=""  onchange="changePassword()" class="max255"></td>
							<td class="componentContentRightLine"
								width="120px">Email</td>
							<td class="componentContentRightLine"><input
								type="text" name="email" id="email" size="30" value="" class="max255"></td>
						</tr>
						<tr id="ori_pass" style="display:none;">
							<td class="componentContentRightLine"
								width="90px">再次輸入密碼</td>
							<td class="componentContentRightLine"
								width="230px"><input
								type="password" name="passwordcheck" id="passwordcheck" size="30" value="" class="max255"></td>
							<td class="componentContentRightLine"
								width="120px">請輸入舊密碼</td>
							<td class="componentContentRightLine"><input type="password" name="ori_password"
								id="ori_password" size="30" value="" class="max255"></td>
						</tr>
						<tr>
							<td colspan="4" class="BT_IN_BBTER">
								<div align="center">
									<div id="add_school_button">
										<button class="btn btn-primary" style="margin:0" value="新增" onclick="addSchool()">新增</button>
									</div>
									<div id="update_school_button">
										<button class="btn btn-primary" style="margin:0" value="更新" onclick="updateSchool()">更新</button>
									</div>	
								</div>
							</td>
						</tr>
				</table>
			</div>
		</div>
            
            <div id="school_dehhhtail" class="TAB_TY_B" style="display:none;">
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
							<td>地址 <font color="red">*</font></td>
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
            
          </div><!-- End of .section-wrap -->
        </div><!-- End of #listForm -->
        

        </div>
		<!-- Excel 上傳 -->
		<div id="excel_upload" class="TAB_TY_B" style="display:none;">
			<form method= "post" action ="/cateringservice/file/upload" enctype="multipart/form-data" >
				<input type="hidden" id="func" name ="func" value="SchoolKitchenAccount"/>
				<input type="hidden" id="overWrite" name ="func" value="0"/>
	    		<table class="component">
					<tr>
						<td class="componetContent2 componentContentRightLine">選擇檔案</td>
						<td class="componetContent2">
							<input id="file" type="file" name="excelFile">    (上傳檔案格式為Excel檔)
						</td>
					</tr>
					<tr>
						<td colspan="2" class="BT_IN_BBTER" bgcolor="#ececec" align="center">
							<input type="button" onclick="upload()" value="上傳檔案">
						</td>
					</tr>
				</table>
	
			</form>
		</div>
	
	</div>
	<!-- Excel 上傳 -->
	<div id="excel_upload" class="TAB_TY_B" style="display:none;">
		<form method= "post" action ="/cateringservice/file/upload" enctype="multipart/form-data" >
			<input type="hidden" id="func" name ="func" value="SchoolKitchenAccount"/>
			<input type="hidden" id="overWrite" name ="func" value="0"/>
    		<table class="component">
				<tr>
					<td class="componetContent2 componentContentRightLine">選擇檔案</td>
					<td class="componetContent2">
						<input id="file" type="file" name="excelFile">    (上傳檔案格式為Excel檔)
					</td>
				</tr>
				<tr>
					<td colspan="2" class="BT_IN_BBTER" bgcolor="#ececec" align="center">
						<input type="button" onclick="upload()" value="上傳檔案">
					</td>
				</tr>
			</table>

		</form>
	</div>
	<!-- 關鍵字搜尋 -->
	<div id="query_bar" class="h_35px lh_35 GRA_ABR"  style="display:none;">
			<div class="dis_intb SECOND_TXT_INP">請輸入篩選條件︰</div>
			<div class="dis_intb LOGIN_US_PS">
				學校名稱&nbsp;<input class="max255" type="text" name="QuerySchoolName" id="QuerySchoolName" >
			</div>
			<div class="dis_intb mgl_10 BT_IN_BBTER">
				&nbsp;&nbsp;<input value="查詢" type="button" onclick="query_School(1)">
			</div>
	</div>
	<!--  學校清單 -->
	<div id="query_list">
		<table id="resultTable" class="tableCss2 BT_IN_BBTER "></table>
	</div>
	<!--  修改學校 新增學校  -->
	<div id="school_detail" class="TAB_TY_B" style="display:none;">
		<form name="theForm">
			<table width="100%" class="component">
				<tbody>
					<tr>
						<td bgcolor="#FFFFFF" class="componentContentRightLine">縣市區域<font color="red">*</font></td>
						<td colspan="3" bgcolor="#FFFFFF"
							class="componentContentRightLine">
							<table width="100%">
								<tr>
									<td>縣市別:</td>
									<td><span name="dropdown_City" id="dropdown_City"></td>
								</tr>
								<tr>
									<td>市/區別:</td>
									<td><span name="dropdown_Area" id="dropdown_Area"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td bgcolor="#edf2e5" class="componentContentRightLine"
							width="90px">帳號類型<font color="red">*</font></td>
						<td bgcolor="#edf2e5" class="componentContentRightLine"
							width="230px">
							<select name="sch_type" id="sch_type">
								<option value="006">自設廚房(006)</option>
								<option value="007">受供餐學校(007)</option>
								<option value="009">員生消費合作社(009)</option>
							</select>
							</td>
							<td bgcolor="#edf2e5" class="componentContentRightLine"
								width="120px">啟用<font color="red">*</font></td>
							<td bgcolor="#edf2e5" class="componentContentRightLine">
								<select name="sch_Enable" id="sch_Enable">
									<option value="1">啟用</option>
									<option value="0">停用</option>
								</select>
							</td>
						</tr>
						<tr>
							<td bgcolor="#edf2e5" class="componentContentRightLine"
								width="90px">學校名稱<font color="red">*</font></td>
							<td bgcolor="#edf2e5" class="componentContentRightLine"
								width="230px"><span name="schCountyArea" id="schCountyArea"></span><input
								type="text" name="schoolName" id="schoolName" size="15" value="" class="max240"></td>
							<td bgcolor="#edf2e5" class="componentContentRightLine"
								width="120px">學校編號<font color="red">*</font></td>
							<td bgcolor="#edf2e5" class="componentContentRightLine">
							<input
								type="text" name="schoolId" id="schoolId" size="30" value="" class="max10">
							<input
								type="hidden" name="sid" id="sid" value=""></td>
						</tr>
						<tr>
							<td bgcolor="#FFFFFF" class="componentContentRightLine"
								width="90px">密碼</td>
							<td id="add_pw" bgcolor="#FFFFFF" class="componentContentRightLine"
								width="230px"><input type="password" name="add_password"
								id="add_password" size="30" value=""  class="max255"></td>
							<td id="upd_pw" bgcolor="#FFFFFF" class="componentContentRightLine"
								width="230px">(不修改請空白)<input type="password" name="upd_password"
								id="upd_password" size="30" value=""  onchange="changePassword()" class="max255"></td>
							<td bgcolor="#FFFFFF" class="componentContentRightLine"
								width="120px">Email</td>
							<td bgcolor="#FFFFFF" class="componentContentRightLine"><input
								type="text" name="email" id="email" size="30" value="" class="max255"></td>
						</tr>
						<tr id="ori_pass"  style="display:none;">
							<td bgcolor="#FFFFFF" class="componentContentRightLine"
								width="90px">再次輸入密碼</td>
							<td bgcolor="#FFFFFF" class="componentContentRightLine"
								width="230px"><input
								type="password" name="passwordcheck" id="passwordcheck" size="30" value="" class="max255"></td>
							<td bgcolor="#FFFFFF" class="componentContentRightLine"
								width="120px">請輸入舊密碼</td>
							<td bgcolor="#FFFFFF" class="componentContentRightLine"><input type="password" name="ori_password"
								id="ori_password" size="30" value="" class="max255"></td>
						</tr>
							<td colspan="4" class="BT_IN_BBTER">
								<div align="center">
									<!-- <input type="button" value="新增" id="add_school_button" onClick="addSchool()">
									<input type="button" value="更新" id="update_school_button"	onClick="updateSchool()"> -->
								</div>
							</td>
						</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
