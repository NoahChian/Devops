<%@page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="util.jsp"%>
<!-- include js的自訂公用函式    -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/resetpwd.js"></script>

<script>
	
</script>
</head>
<body>
	<!-- Detail Page -->
	<div>
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
			<div class="TITLE_TXT flo_l ">重新設定密碼</div>
		</div>
		<table class="tableCss2">
			<tr>
				<td>新密碼 <font color="red">*</font></td>
				<td><input id="pwd" type="password"></td>
			</tr>
			<tr>
				<td>確認新密碼 <font color="red">*</font></td>
				<td><input id="confPwd" type="password"></td>
			</tr>
			<tr>
				<td class="BT_IN_BBTER" colspan="2" align="center"><input type="button" onclick="updateUserPwd()" value="儲存"></td>
			</tr>
		</table>
	</div>

</body>
</html>