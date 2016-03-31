<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="util.jsp" %>    <!-- include js的自訂公用函式    20140219 KC -->
<%
String uType = "";
String uName = "";
String account = "";
String StrName = "";
String roletype = "";
String county = "";
if(session.getAttribute("account")!=null){
uType = (String) session.getAttribute("uType");
uName = (String) session.getAttribute("uName");
account = (String) session.getAttribute("account");
StrName = (String) session.getAttribute("StrName");
roletype = (String) session.getAttribute("roletype");
county = (String) session.getAttribute("county");}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
<script src="../../js_ca/SeasoningStock.js"></script>
</head>
<body>

</body>
</html>