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
<!-- �|�I�]�p�M�� -->
<div class="contents-title">�ϥΪ̬����d��</div>
<div class="contents-wrap">
	<div id="query_bar" style="font-size: medium;font-weight: bolder;">
		<table class="table table-striped table-bordered  table-condensed">
		<tr>
			<td style="width: 110px;">�ϥΪ̱b���J</td>
			<td>
				<input type="text" id="txtAccount" name="txtAccount" class="searchInput max50">&nbsp;&nbsp;
				<button style="margin-top: 0px;" class="btn btn-primary" onclick="queryResult('query')">�d��</button>&nbsp;
				<button style="margin-top: 0px;" class="btn btn-primary" onclick="queryResult('query_ui')">�d��UI Log</button>
			</td>
		</tr>
		<tr>
			<td>���D�����J</td>
			<td>
				<select style="width: 177px; border-color:#67c6a7; border-width:2px" id="ddl_QA_type">
					<option value="upload_menu">�Ǯյ��W��</option>
					<option value="upload_ingredient">�����W��</option>
					<option value="upload_dish">�򥻵��W��</option>
					<option value="upload_dishImg">���Ϥ��W��</option>
					<option value="upload_kitchenImg">�p�йϤ��W��</option>
					<option value="upload_supplier">�����ӤW��</option>
					<option value="modify_web_menu">�����Ǯյ����@</option>
					<option value="modify_web_ingredient">�����������@</option>
					<option value="admin_function">�D�޾����\��</option>
					<option value="login">�b��/�n�J���D</option>
					<option value="SCHOOLKITCHENACCOUNT_UPLOAD" />
					<option value="others">��L���D</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>���D�y�z�J</td>
			<td>
			<textarea id="txtQA" style="border-color:#67c6a7; border-width:2px; width: 100%;" rows="3"></textarea>
			</td>
		</tr>
		<tr>
			<td align="center" colspan="2">
			<button style="margin-top: 0px;" class="btn btn-primary" onclick="logging()">�����ȪA</button>
			</td>
		</tr>
		</table>
		</br>
		<div id=basicData>
			<table class="table table-striped table-bordered  table-condensed">
				<thead>
					<tr>
						<td colspan="2" align="center">�b���򥻸��</td>
					</tr>
				</thead>
				<tr>
					<td>�b���W��</td>
					<td>
						<span id="tb_account_uname"></span>
						(kid: <span id="tb_account_kid"></span>)
					</td>
				</tr>
				<tr>
					<td>���\�Ǯ�</td>
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

<!-- �¹ο��t�ήM�� -->
<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B" style="width:60% ; float:left; display:none;">
	<table width="100%" >
	 <tbody>

		<tr>
		<tr>
			
			<td>		
				�ϥΪ̱b��				
				<div class="dis_intb mgl_10 BT_IN_BBTER" >
					<input id="txtAccount" type="text">
					<input type="button" value="�d�� " onclick="queryResult('query')">
					<input type="button" value="�d��UI Log " onclick="queryResult('query_ui')">
				</div>
				
			</td>
			
		</tr>
		<tr>
			<td>
				<label for ="ddl_QA_type">���D����</label>
				<select id="ddl_QA_type">
					<option value="upload_menu">�Ǯյ��W��</option>
					<option value="upload_ingredient">�����W��</option>
					<option value="upload_dish">�򥻵��W��</option>
					<option value="upload_dishImg">���Ϥ��W��</option>
					<option value="upload_kitchenImg">�p�йϤ��W��</option>
					<option value="upload_supplier">�����ӤW��</option>
					<otion value="modify_web_menu">�����Ǯյ����@</option>
					<option value="modify_web_ingredient">�����������@</option>
					<option value="admin_function">�D�޾����\��</option>
					<option value="login">�b��/�n�J���D</option>
					<option value="SCHOOLKITCHENACCOUNT_UPLOAD" />
					<option value="others">��L���D</option>
					
				</select>  
				<br/>
				<label for="txtQA">���D�y�z</label><input type="text" id="txtQA" style="width: 348px; "/>
				<div class="dis_intb mgl_10 BT_IN_BBTER"  style="text-align:right; width:100%;">
					<span id="msgLogging" style="color:#FA0300"></span>
					<input type="button" onclick="logging()" value="�����ȪA"/>
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
						<td colspan="2" align="center" bgcolor="#678948" class="TIT_A">�b���򥻸��</td>				
					</tr>
					<tr>
						<td>�b���W��</td>
						<td>
							<span id="tb_account_uname"></span>
							(kid: <span id="tb_account_kid"></span>)
						</td>
					</tr>
					<tr>
						<td>���\�Ǯ�</td>
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