<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.iii.ideas.catering_service.util.CateringServiceUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- 20140604 javascript 移至/ja_ca/navi.js -->
<script src="../../js/jquery.md5.js"></script>
<script src="../../js/nodeJsSrv_util.js" type="text/javascript"></script>
<script src="../../js_ca/navi.js"></script>
<style type="text/css">
<!--
-->
</style>
</head>
<body>

	<%
		//String CompanyName = (String) session
		//		.getAttribute("usercompanyName");
		//String manufacturerName = (String) session
		//		.getAttribute("usermanufacturerName");
		//String contactPerson = (String) session
		//		.getAttribute("contactPerson");
		//String contactDept = (String) session.getAttribute("contactDept");
		String uType = "";
		String uName = "";
		String account = "";
		String StrName = "";
		String roletype = "";
		String county = "";
		if (session.getAttribute("account") != null) {
			uType = (String) session.getAttribute("uType");
			uName = (String) session.getAttribute("uName");
			account = (String) session.getAttribute("account");
			StrName = (String) session.getAttribute("StrName");
			roletype = (String) session.getAttribute("roletype");
			county = (String) session.getAttribute("county");
		}

		// 判斷是國中國小還是幼兒園
		String uNameType = CateringServiceUtil.getSchoolLevel(uName);
		
		// 顯示『自設』還是『受供餐』
		String displayNaviWel = "";
		if( (roletype.equals("kCom") || roletype.equals("kSch")) && !"007".equals(uType) && !"009".equals(uType) ) {
			displayNaviWel = "食品業者 | 學校廚房";
		} else if( roletype.equals("kSch") && "007".equals(uType) ) {
			displayNaviWel = "受供餐學校";
		}
		
		
		//Common common = new Common();
		//String key = "tj;4u06";
		//if (session.getAttribute("usertype") == "2") {
	%>
	
<%-- 2015.05.04 注解--%>
	<%
		//加入他校供應的學校選單 20140501 KC
		if (roletype.equals("kSch") && "007".equals(uType) && !"PRESCHOOL".equals(uNameType)) {
	%>
	<div class="LOGIN_BBTER_B_IN GRE_ABR">
		<a href="#">受供餐學校</a>
	</div>
	<div class="NAV_ZONE_B">
		<%=StrName%><br />您好!
		<div class="NAV_ZONE_ATIT"><a href="http://175.98.115.58/docs/SchoolQueryAccount.pdf" target="_blank">操作教學檔下載</a></div>
		<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
			<a href="#" onclick="changePassDiv()">修改密碼</a><a href="#" onclick="logout()">登 出</a>
		</div>

	</div>
	<!--  修改密碼 -->
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
	<div class="NAV_ZONE_A_TITLE GRE_ABR">
		<div class="TITLE_TXT">學校菜單查詢</div>
	</div>
	<div class="NAV_ZONE_A">

		<div id="SPPIO"></div>
		<div class="NAV_ZONE_ATIT">菜色資料維護</div>
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('nomenudate')">不供餐日期管裡</a>
		</div>
		<div class="NAV_ZONE_ATIT">菜單與食材查詢</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../read_Menu/">學校菜單查詢</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../read_ingredients/">食材資料查詢</a>
		</div>
		<div class="NAV_ZONE_ATIT">食品安全資料管理</div>
		<!-- <div class="NAV_ZONE_AMIN">
			<a href="../supplyinfo/">食材供應商供貨資訊</a>
		</div>
		 -->
		<div class="NAV_ZONE_AMIN">
			<a href="../certification/">檢驗報告資訊</a>
		</div>

	</div>

	<!-- 主管機關選單 20131204(Ric) -->
	<%
		}
	%>

 
	<%
		//加入非他校供應的判斷 20140501 KC 
		if ((roletype.equals("kCom") || roletype.equals("kSch")) && !"007".equals(uType) && !"009".equals(uType) && !"PRESCHOOL".equals(uNameType) ) {
	%>
	<div class="LOGIN_BBTER_B_IN GRE_ABR">
		<a href="#">食品業者 | 學校廚房</a>
	</div>
	<div class="NAV_ZONE_B">
		<%=StrName%><br />您好!
	<div class="NAV_ZONE_ATIT"><a href="http://175.98.115.58/docs/SystemUserManualppt.pdf" target="_blank">操作教學檔下載</a></div>
		<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
			<a href="#" onclick="changePassDiv()">修改密碼</a><a href="#" onclick="logout()">登 出</a>
		</div>

	</div>
	<!--[if (IE 7) | (IE 8) | (IE 9)]>
	<div class="NAV_ZONE_A_TITLE GRE_ABR">
					<div class="TITLE_TXT">系統提示訊息</div></div>
	<div class="NAV_ZONE_B">
					<span style="color:red;">您目前使用的瀏覽器可能無法支援本系統功能，請更新IE.10以上或選用下列瀏覽器</span>
				
				<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
				<a  href="http://www.google.com/intl/zh-TW/chrome/" target="_blank"  >Chrome</a>
				<a href="http://moztw.org/firefox/" target="_blank">Firefox</a></div>
				
				</div>	
	<![endif]-->
	<!--  修改密碼 -->
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
	<div class="NAV_ZONE_A_TITLE GRE_ABR">
		<div class="TITLE_TXT">功能選單</div>
	</div>
	<div class="NAV_ZONE_A">
		<div class="NAV_ZONE_ATIT">業者 | 廚房管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../manageKitchen/">廚房基本資料管理</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../listSupplier/" 	>供應商維護</a>
		</div>

		<div id="SPPIO"></div>

		<div class="NAV_ZONE_ATIT">菜單與食材管理</div>
		
		<div class="NAV_ZONE_AMIN">
			<a href="../school_Menu/">學校菜單維護</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../ingredients_management/">食材資料維護</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../singleSch_manage/">單一學校食材維護</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../manageDish/">菜色資料維護</a>
		</div>
		
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('seasoningstockdata')">調味料維護</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../ingredientproperty/">食材檢驗報告</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../missingCase/">缺漏資料查詢</a> 
		</div>
		<div id="SPPIO"></div>
		
<!-- 		<div class="NAV_ZONE_ATIT">食品安全資料管理1</div> -->
<!-- 		<div class="NAV_ZONE_AMIN"> -->
<!-- 			<a href="../certification/">檢驗報告資訊</a> -->
<!-- 		</div> -->
		
		
<!-- 		<div class="NAV_ZONE_ATIT">食品安全資料管理</div> -->
<!-- 		<div class="NAV_ZONE_AMIN"> -->
<!-- 			<a href="../certification/">檢驗報告資訊</a> -->
<!-- 		</div> -->
		<%
			
				if ("006".equals(uType)) {
		%>
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('nomenudate')">不供餐日期管裡</a>
		</div>
		<div class="NAV_ZONE_ATIT">食品安全資料管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../certification/">檢驗報告資訊</a>
		</div>
		<%
			}
		%>
		
		<div id="SPPIO"></div>
		<!--div class="NAV_ZONE_ATIT">系統資料管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../missingCase/">缺漏資料查詢</a>
		</div-->
		
		<!-- 
						<div class="NAV_ZONE_AMIN"><a href="../dishImageUpload/">菜色圖檔維護</a></div>
						 -->

		<%
			// 排除團膳業者不給這個選單  20140505 KC 
				if (!"005".equals(uType) && !"006".equals(uType) && !"009".equals(uType)) {
		%>
		<div class="NAV_ZONE_ATIT">食品安全資料管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../kitchenIngredient/">廚房使用食材資訊</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../supplyinfo/">食材供應商供貨資訊</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../certification/">檢驗報告資訊</a>
		</div>
		
		<%
			}
		%>

	</div>
	<!-- 主管機關選單 20131204(Ric) -->
	<%
		}
	%>
	
	<%-- 國小附幼(受供餐)、本園、附幼(自設廚房)、幼兒園分班(受供餐)  2015.05.01 Steven 開始 --%>
	<%
	 	if( "PRESCHOOL".equals(uNameType) ) {
	%>
	<div class="LOGIN_BBTER_B_IN GRE_ABR">
		<a href="#"><%=displayNaviWel%></a>
	</div>
	<div class="NAV_ZONE_B">
		<%=StrName%><br />您好!
		<div class="NAV_ZONE_ATIT"><a href="http://175.98.115.58/docs/SchoolQueryAccount.pdf" target="_blank">操作教學檔下載</a></div>
		<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
			<a href="#" onclick="changePassDiv()">修改密碼</a><a href="#" onclick="logout()">登 出</a>
		</div>

	</div>
	<div class="NAV_ZONE_A_TITLE GRE_ABR">
		<div class="TITLE_TXT">功能選單</div>
	</div>
	<div class="NAV_ZONE_A">
		<div class="NAV_ZONE_ATIT">業者 | 廚房管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../manageKitchen/">廚房基本資料管理</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../listSupplier/" 	>供應商維護</a>
		</div>
		<div id="SPPIO"></div>
		<div class="NAV_ZONE_ATIT">幼兒園菜單與食材管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('dishBatch')">幼兒園學校菜單維護</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('ingredient')">幼兒園食材資料維護</a>
		</div>
		<div id="SPPIO"></div>

		<div class="NAV_ZONE_ATIT">菜單與食材管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../manageDish/">菜色資料維護</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('nomenudate')">不供餐日期管裡</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('seasoningstockdata')">調味料維護</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../ingredientproperty/">食材檢驗報告</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../missingCase/">缺漏資料查詢</a>
		</div>
		<div id="SPPIO"></div>
		
		<div class="NAV_ZONE_ATIT">食品安全資料管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../certification/">檢驗報告資訊</a>
		</div>
		
	</div>
	<%
		}
	%>
	<%-- 國小附幼(受供餐)、本園、附幼(自設廚房)、幼兒園分班(受供餐)  2015.05.01 Steven 結束 --%>
	
	<%
		//加入員生消費合作社清單 add by Ellis 20150305
		if ("009".equals(uType)) {
	%>
	<div class="LOGIN_BBTER_B_IN GRE_ABR">
		<a href="#">員生消費合作社</a>
	</div>
	<div class="NAV_ZONE_B">
		<%=StrName%><br />您好!
	<div class="NAV_ZONE_ATIT"><a href="http://175.98.115.58/docs/SchoolQueryAccount.pdf" target="_blank">操作教學檔下載</a></div>
		<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
			<a href="#" onclick="changePassDiv()">修改密碼</a><a href="#" onclick="logout()">登 出</a>
		</div>

	</div>
	<!--  修改密碼 -->
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
	<div class="NAV_ZONE_A_TITLE GRE_ABR">
		<div class="TITLE_TXT">功能選單</div>
		
	</div>
		<div id="SPPIO"></div>
	<div class="NAV_ZONE_A">
		<div class="NAV_ZONE_ATIT">員生消費合作社</div>
		<!-- 20150519 shine add 增加廠商管理、商品管理、消費者服務功能 -->
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('companymanagement')">廠商管理</a>
		</div>		
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('productmanagement')">商品管理</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../manageListSchoolProductSet/">商品上下架管理</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('customerservice')">消費者服務</a>
		</div>		
	</div>
		<div id="SPPIO"></div>
	<%
		}
	%>
	
	
	<%
		if (roletype.equals("kGov1") || roletype.equals("admin") ||roletype.equals("kGov2") ) {
	%>
	<div class="LOGIN_BBTER_B_IN GRE_ABR">
		<a href="#">主管機關</a>
	</div>
	<div class="NAV_ZONE_B">
		主管單位<br /><%=StrName%>
		
		<%
		if (!roletype.equals("admin")){
			if (roletype.equals("kGov1")  ) {
		%>
<div class="NAV_ZONE_ATIT"><a href="http://175.98.115.58/docs/AdminUserManual_edu.pdf" target="_blank">操作教學檔下載</a></div>
		<%
			}else{
		%>
<div class="NAV_ZONE_ATIT"><a href="http://175.98.115.58/docs/AdminUserManual_health.pdf" target="_blank">操作教學檔下載</a></div>
		<%
			}
		}
		%>
		<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
			<a href="#" onclick="changePassDiv()">修改密碼</a><a href="#" onclick="logout()">登 出</a>
		</div>
	</div>
	<!--  修改密碼 -->
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
	<div class="NAV_ZONE_A_TITLE GRE_ABR">
		<div class="TITLE_TXT">主管機關</div>
	</div>
	<div class="NAV_ZONE_A">
		<div class="NAV_ZONE_ATIT">食品安全資料管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../kitchenIngredient/">廚房使用食材資訊</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../supplyinfo/">食材供應商供貨資訊</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../certification/">檢驗報告資訊</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../read_Menu/">學校菜單查詢</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../read_ingredients/">食材資料查詢</a>
		</div>
		<% if("11-Ric".equals(uName)){ %>
		<div class="NAV_ZONE_AMIN">
			<a href="../read_ingredients2/">(進階)食材資料查詢</a>
		</div>		
		<% } %>
		<!--  
		<div class="NAV_ZONE_AMIN">
			<a href="../abnormal/">異常資料設定</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../abnormalsearch/">異常資料查詢</a>
		</div>
		 -->
		<div class="NAV_ZONE_ATIT">數據統計管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../managelist/">管理功能清單</a>
		</div>
		
		<!--  20140825 學校清單功能開啟 -->
		<!--  20140828改為權限kGov1(教育局)和admin(管理者)才能用-->		
		<%
			//if ( (roletype.equals("admin") ||roletype.equals("kGov1")) ) {
			//改為只有教育局 和 11-ric進的來  20140924 KC
			if ( ("11-Ric".equals(uName) ||roletype.equals("kGov1")) ) {	
		%>
		<div class="NAV_ZONE_ATIT">帳戶權限管理</div>
			<div class="NAV_ZONE_AMIN">
				<a href="../listSchool/">學校管理</a>
			</div>
		
		<% //if (roletype.equals("admin") && "11-Ric".equals(uName)){
			//改為只有11-ric進的來 20140924 KC 
				if ("11-Ric".equals(uName)){
		%>
					<div class="NAV_ZONE_AMIN">
						<a href="../listKitchen/">廚房管理</a>
					</div>
					<div class="NAV_ZONE_AMIN">
						<a href="../listUser/">帳戶權限管理</a>
					</div>
				<% 
			}
		%>				
		<%
			}
		%>
	
		<!-- 20150519 shine add 增加統計報表及合作社查詢功能 -->
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('statisticsReport')">登錄統計報表</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="#" onclick="preSchoolMenuButton('schoolproductsearch')">員生消費合作社查詢</a>
		</div>			
</div>

	<%
		}
	%>
	<!-- 主管機關選單 20131204(Ric) -->
	<%
		if (roletype.equals("kGov3")) {
	%>
	<div class="LOGIN_BBTER_B_IN GRE_ABR">
		<a href="#">其他單位</a>
	</div>
	<div class="NAV_ZONE_B">
		查詢與檢視單位<br /><%=StrName%>
		<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
			<a href="#" onclick="logout()">登 出</a>
		</div>
	</div>
	<div class="NAV_ZONE_A_TITLE GRE_ABR">
		<div class="TITLE_TXT">其他單位</div>
	</div>
	<div class="NAV_ZONE_A">
		<div class="NAV_ZONE_ATIT">食品安全資料管理</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../kitchenIngredient/">廚房使用食材資訊</a>
		</div>
		<div class="NAV_ZONE_AMIN">
			<a href="../supplyinfo/">食材供應商供貨資訊</a>
		</div>
	</div>

	<%
		}
	%>
	<!-- loginAfter -->
	<%
		//	int userType = (Integer) session.getAttribute("usertype");
		if (!roletype.equals("kCom") && !roletype.equals("kSch") && !roletype.equals("kGov1") && !roletype.equals("kGov2") && !roletype.equals("admin") && !roletype.equals("kSHOP")) {
	%>

	<div id="nologinArea">
		<div class="LOGIN_BBTER_B GRE_ABR">
			<a href="#" onclick="showLoginCompany()">食品業者 | 學校廚房</a>
		</div>
		<div class="LOGIN_BBTER_A GRE_ABR">
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
			<div class="NAV_ZONE_B">

				<div class="LOGIN_US_PS">
					帳號 <input class="user max50" name="usename" size="15" type="text">
				</div>
				<div class="LOGIN_US_PS">
					密碼 <input class="user max255" name="userpwd" style="width: 120px" type="password" onKeyDown="javascript:(function() {if(event.keyCode==13) loginIn()})();">
				</div>

				<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
					<a href="#" onclick="cancelLogin()">取 消</a><a href="javascript:loginIn();">登 入</a>
				</div>
				<div class="w_100ps h_30px tal_ch font_12 LOGIN_JO_FG">
					<a href="#" onclick="showForgotPwd()">忘記密碼</a>
					<!--  <a href="http://www.twfoodtrace.org.tw/company_join.php">加入會員</a>
						|
						<a href="http://www.twfoodtrace.org.tw/forgotten_pass.php">忘記密碼</a> -->
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
			<div class="NAV_ZONE_B">
				<div class="LOGIN_US_PS">
					帳號 <input class="user max50" name="usename" size="15" type="text">
				</div>
				<div class="LOGIN_US_PS">
					密碼 <input class="user max255" name="userpwd" style="width: 120px" type="password">
				</div>

				<div class="w_100ps h_44px tal_ch GOBK_BBT mgt_10">
					<a href="#" onclick="cancelLogin()">取 消</a><a href="javascript:{loginIn();loginInForm2.submit();}">登 入</a>
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

	<%
		}
	%>
</body>
</html>