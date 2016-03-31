<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="../../js_ca/navi2.js"></script>
<style type="text/css">
</style>
</head>
<body>
	<%
		String StrName = "";
		String roletype = "";
		if (session.getAttribute("account") != null) {
			StrName = (String) session.getAttribute("StrName");
			roletype = (String) session.getAttribute("roletype");
		}
	%>
	<input type="hidden" id="roletype" value="<%=roletype%>">
		
	<div id="nologinArea">
		<div class="LOGIN_BBTER_B GRE_ABR" id="comLogin">
			<a href="#" onclick="showLoginCompany()">食品業者 | 學校廚房</a>
		</div>
		<div class="LOGIN_BBTER_A GRE_ABR" id="govLogin">
			<a href="#" onclick="showLoginGoverment()">主管機關登入</a>
		</div>
	</div>
	<!-- 公司登入 -->
	<div id="loginCompany" style="display: none">
		<div class="LOGIN_BBTER_B GRE_ABR">
			<a href="#">食品業者 | 學校廚房</a>
		</div>
		<form action="<%=request.getContextPath()%>/web/main/login/" method="post" id="loginInForm" name="loginInForm">
			<input type="hidden" name="logintype" value="kitchen">
			<div class="NAV_ZONE_B" id="beforeLogin">
				<div class="LOGIN_US_PS">
					帳號 <input class="user max50" name="usename" id="usename" size="15" type="text">
				</div>
				<div class="LOGIN_US_PS">
					密碼 <input class="user max255" name="userpwd" style="width: 120px" type="password">
				</div>

				<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
					<a href="#" onclick="cancelLogin()">取 消</a><a href="javascript:loginInForm.submit();">登 入</a>
				</div>
				<div class="w_100ps h_30px tal_ch font_12 LOGIN_JO_FG">
					<a href="#" onclick="showForgotPwd()">忘記密碼</a>
				</div>
			</div>
		</form>
	</div>

	<!-- 主管機關登入  -->
	<div id="loginGoverment" style="display: none">
		<div class="LOGIN_BBTER_A GRE_ABR">
			<a href="#">主管機關登入</a>
		</div>
		<form action="<%=request.getContextPath()%>/web/main/login/" method="post" id="loginInForm2" name="loginInForm2">
			<input type="hidden" name="logintype" value="govern">
			<div class="NAV_ZONE_B" id="beforeLogin">
				<div class="LOGIN_US_PS">
					帳號 <input class="user max50" name="usename" size="15" type="text">
				</div>
				<div class="LOGIN_US_PS">
					密碼 <input class="user max255" name="userpwd" style="width: 120px" type="password">
				</div>

				<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
					<a href="#" onclick="cancelLogin()">取 消</a><a href="javascript:loginInForm2.submit();">登 入</a>
				</div>
				<div class="w_100ps h_30px tal_ch font_12 LOGIN_JO_FG">
					<a href="#" onclick="showForgotPwd()">忘記密碼</a>
					<!--  	<a href="http://www.twfoodtrace.org.tw/forgotten_pass.php">忘記密碼</a> -->
				</div>
			</div>
		</form>
	</div>

	<!-- 忘記密碼 -->
	<div id="forgotPwd" title="忘記密碼" style="display: none">
		<div class="NAV_ZONE_B">
			<div class="LOGIN_US_PS">
				帳           號  <input class="forgotPwd max50" id="userid" >
			</div>
			<div class="LOGIN_US_PS">
				E-mail  <input class="forgotPwd max45" id="useremail">
			</div>
			<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
				<a href="#" onclick="cancelResetPwd()">取 消</a><a href="#" onclick="postResetPwd()">確認</a>
			</div>
		</div>
	</div>

	<div class="NAV_ZONE_B" id="afterLogin" style="display: none">
		<label id="orgName"></label>
		<%=StrName%><br />您好!
		<div class="NAV_ZONE_ATIT" id="downloadPath"></div>
		<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
			<a href="#" onclick="changePassDiv()">修改密碼</a><a href="<%=request.getContextPath()%>/web/logout/">登 出</a>
		</div>
	</div>

	<div id="changePass" class="NAV_ZONE_B" style="display: none">
		<form action="<%=request.getContextPath()%>/web/main/changePass/" method="post" id="changePassF" name="changePassF">
			<input type="hidden" name="logintype" value="kitchen">
			<div class="LOGIN_US_PS">
				舊密碼 <input class="user max255" name="old" style="width: 110px" type="password">
			</div>
			<div class="LOGIN_US_PS">
				新密碼 <input class="user max255" name="newpass" style="width: 110px" type="password">
			</div>
			<div class="LOGIN_US_PS">
				確認密碼 <input class="user max255" name="newCheck" style="width: 110px" type="password">
			</div>
			<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
				<a href="#" onclick="cancelChangePass()">取 消</a><a href="javascript:changePassF.submit();">送出</a>
			</div>
			<div class="w_100ps h_30px tal_ch font_12 LOGIN_JO_FG"></div>
		</form>
	</div>
				
	<div class="NAV_ZONE_A_TITLE GRE_ABR" id="funcMenu" style="display: none;">
		<div class="TITLE_TXT">功能選單</div>
	</div>
	<div class="NAV_ZONE_A_TITLE GRE_ABR" id="authorities" style="display: none;">
		<div class="TITLE_TXT">主管機關</div>
	</div>
	<div id = "menuDetail" ></div>
		
</body>
</html>
