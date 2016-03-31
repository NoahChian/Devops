<%@page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="util.jsp" %> <!-- include js的自訂公用函式    20140219 KC -->
<%@ page import="java.util.HashMap" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<%
String qschoolName="";
String qschoolId="";
String qschoolCode="";
String qbasepath="";
String basepath="../../";
if(request.getAttribute("responseMap") != null){
	HashMap<String,String> responseMap = (HashMap<String,String>)request.getAttribute("responseMap");
	if (responseMap.get("schoolName")!=null){
		qschoolName=(String) responseMap.get("schoolName");
	}
	if (responseMap.get("schoolId")!=null){
		qschoolId=(String) responseMap.get("schoolId");
	}
	if (responseMap.get("schoolCode")!=null){
		qschoolCode=(String) responseMap.get("schoolCode");
	}
	if (responseMap.get("basepath")!=null){
		qbasepath=(String) responseMap.get("basepath");
	}
}

%>

<script src="../../../../js/lightbox_js/jquery-1.11.0.min.js"></script>
<script src="../../../../js/lightbox_js/lightbox.min.js"></script>

<script src="../../../../js_ca/common.js"></script>
<script src="../../../../js/nodeJsSrv_util.js" type="text/javascript"></script>
<script src="../../../../js_ca/customerQuerySF.js" type="text/javascript"></script>

<link href="../../../../css/lightbox_css/lightbox.css" rel="stylesheet" />

<style type="text/css">
.qa_title_on {
	text-decoration: underline;
}
#gotop {
    display: none;
    position: fixed;
    right: 20px;
    bottom: 20px;    
    padding: 10px 15px;    
    font-size: 20px;
    color: white;
    cursor: pointer;
}
</style>

</head>
<body>
<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
<div class="TITLE_TXT flo_l"><%=qschoolName%>　員生消費合作社商品清單</div>
<input type="hidden" id="sid" value="<%=qschoolId%>">
</div>

<div id="query_list">
	<div class="GRE_ABR h_30px lh_30" id="MAIN_TITLE_BAR">
		<div id="query_rule" class="TITLE_TXT flo_l"></div>
		<div class="TITLE_TXT flo_l "><span id="page" ></span></div>
	</div>
	<div id="MAINi"><div id="resultTable"></div></div>
</div>

<div id="gotop"><img name="gotopImg" id="gotopImg" src="../../../../images/up.png" width="48px" height="48px" /></div>

</body>
</html>
