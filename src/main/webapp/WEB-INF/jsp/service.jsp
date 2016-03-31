<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js/jquery-1.9.1.js" type="text/javascript"></script>
<script src="../../js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>
<script src="../../js_ca/common.js" type="text/javascript"></script>
<script src="../../js_ca/service.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>

</head>



<body>
<!-- 四點設計套版 -->
<div class="contents-title">使用者紀錄查詢</div>
<div class="contents-wrap">
	<div id="query_bar" style="font-size: medium;font-weight: bolder;">
		<table class="table table-striped table-bordered  table-condensed">
		<tr>
			<td style="width: 110px;">使用者帳號︰</td>
			<td>
				<input type="text" id="txtAccount" name="txtAccount" class="searchInput max50">&nbsp;&nbsp;
				<button style="margin-top: 0px;" class="btn btn-primary" onclick="queryResult('query')">查詢</button>&nbsp;
				<button style="margin-top: 0px;" class="btn btn-primary" onclick="queryResult('query_ui')">查詢UI Log</button>
			</td>
		</tr>
		<tr>
			<td>問題類型︰</td>
			<td>
				<select style="width: 177px; border-color:#67c6a7; border-width:2px" id="ddl_QA_type">
					<option value="upload_menu">學校菜單上傳</option>
					<option value="upload_ingredient">食材上傳</option>
					<option value="upload_dish">基本菜色上傳</option>
					<option value="upload_dishImg">菜色圖片上傳</option>
					<option value="upload_kitchenImg">廚房圖片上傳</option>
					<option value="upload_supplier">供應商上傳</option>
					<option value="modify_web_menu">網頁學校菜單維護</option>
					<option value="modify_web_ingredient">網頁食材維護</option>
					<option value="admin_function">主管機關功能</option>
					<option value="login">帳號/登入問題</option>
					<option value="SCHOOLKITCHENACCOUNT_UPLOAD" />
					<option value="others">其他問題</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>問題描述︰</td>
			<td>
			<textarea id="txtQA" style="border-color:#67c6a7; border-width:2px; width: 100%;" rows="3"></textarea>
			</td>
		</tr>
		<tr>
			<td align="center" colspan="2">
			<button style="margin-top: 0px;" class="btn btn-primary" onclick="logging()">紀錄客服</button>
			</td>
		</tr>
		</table>
		</br>
		<div id=basicData>
			<table class="table table-striped table-bordered  table-condensed">
				<thead>
					<tr>
						<td colspan="2" align="center">帳號基本資料</td>
					</tr>
				</thead>
				<tr>
					<td>帳號名稱</td>
					<td>
						<span id="tb_account_uname"></span>
						(kid: <span id="tb_account_kid"></span>)
					</td>
				</tr>
				<tr>
					<td>供餐學校</td>
					<td>
						<div style="overflow:auto;">
						<span id="tb_account_schools"></span>
						</div>
					</td>
				</tr>
			</table>
		</div>
		</br>
		<table id="tbList" class="table table-striped table-bordered  table-condensed">
		</table>
	</div>
</div> <!-- End of contents-warp -->

<!-- 舊團膳系統套版 -->
<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B" style="width:60% ; float:left; display:none;">
	<table width="100%" >
	 <tbody>

		<tr>
		<tr>
			
			<td>		
				使用者帳號				
				<div class="dis_intb mgl_10 BT_IN_BBTER" >
					<input id="txtAccount" type="text">
					<input type="button" value="查詢 " onclick="queryResult('query')">
					<input type="button" value="查詢UI Log " onclick="queryResult('query_ui')">
				</div>
				
			</td>
			
		</tr>
		<tr>
			<td>
				<label for ="ddl_QA_type">問題類型</label>
				<select id="ddl_QA_type">
					<option value="upload_menu">學校菜單上傳</option>
					<option value="upload_ingredient">食材上傳</option>
					<option value="upload_dish">基本菜色上傳</option>
					<option value="upload_dishImg">菜色圖片上傳</option>
					<option value="upload_kitchenImg">廚房圖片上傳</option>
					<option value="upload_supplier">供應商上傳</option>
					<otion value="modify_web_menu">網頁學校菜單維護</option>
					<option value="modify_web_ingredient">網頁食材維護</option>
					<option value="admin_function">主管機關功能</option>
					<option value="login">帳號/登入問題</option>
					<option value="SCHOOLKITCHENACCOUNT_UPLOAD" />
					<option value="others">其他問題</option>
					
				</select>  
				<br/>
				<label for="txtQA">問題描述</label><input type="text" id="txtQA" style="width: 348px; "/>
				<div class="dis_intb mgl_10 BT_IN_BBTER"  style="text-align:right; width:100%;">
					<span id="msgLogging" style="color:#FA0300"></span>
					<input type="button" onclick="logging()" value="紀錄客服"/>
				</div>				
				
			</td>
		</tr>		

	 </tbody>
	</table>
</div>
	<div id="dss" class="GRA_ABR" style="width:39% ; float:left; display:none;">
	<div class="SECOND_TXT">
			<div class="TAB_TY_A" style="width:100%">
				<table id="tb_detail_nutrition" width="100%" border="1" class="tableCss2">
					<tr>
						<td colspan="2" align="center" bgcolor="#678948" class="TIT_A">帳號基本資料</td>				
					</tr>
					<tr>
						<td>帳號名稱</td>
						<td>
							<span id="tb_account_uname"></span>
							(kid: <span id="tb_account_kid"></span>)
						</td>
					</tr>
					<tr>
						<td>供餐學校</td>
						<td>
							<div style="overflow:auto; width:200px; height:100px;">
								<span id="tb_account_schools"></span>
							</div>
						</td>
					</tr>
														
				</table>
			</div>
	</div>
	</div>




<br/>

	<br/>
<!-- <table id="tbList"  class="tableCss2">
</table> -->
</body>
</html>