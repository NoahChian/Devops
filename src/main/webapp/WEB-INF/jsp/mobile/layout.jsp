<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/tiles-jsp.tld" prefix="tiles"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">
	<meta name="description" content="<tiles:insertAttribute name="description"/>">
		<title><tiles:insertAttribute name="title" /></title>
		<link rel="shortcut icon" href="/cateringservice/images/logo.ico" type="image/x-icon" />
<!-- 		<link href="../../css/mobile/css.css" rel="stylesheet" type="text/css" /> -->
		<link href="/cateringservice/css/mobile/css.css" rel="stylesheet" type="text/css" />
		<!-- Jquery Mobile -->
		<link rel="stylesheet" href="/cateringservice/css/jquery.mobile-1.3.2.min.css" />
		<script src="/cateringservice/js/jquery-1.9.1.min.js"></script>
		<script src="/cateringservice/js/jquery.mobile-1.3.2.min.js"></script>
		<script src="/cateringservice/js/iservbilling.js"></script>

		<style>
body {
	overflow-y: scroll;
}

input.error {
	border: 1px dotted red;
}

select.error {
	border: 1px dotted red;
}

textarea.error {
	border: 1px dotted red;
}

.error {
	color: red;
}

.block {
	display: block;
}
</style>
</head>

<body>
	<div data-role="page" id="page1">
		<tiles:insertAttribute name="page" />
		<div data-role="footer" data-position="fixed" data-tap-toggle="false" data-theme="b">
			<div data-role="navbar">
<!-- 				<ul> -->
<!-- 					<li><a id="a1" rel="external" href="http://trace.twfoodtrace.org.tw/fcloud/index.do#type1" class="ui-btn ui-corner-all ui-btn-inline" data-role="tab"><img src="../../images/mobile/navbar/icon1.png" width="32" height="32" alt="食品加工"><br>食品加工</a></li> -->
<!-- 					<li><a id="a2" rel="external" href="http://trace.twfoodtrace.org.tw/fcloud/index.do#type2" class="ui-btn ui-corner-all ui-btn-inline" data-role="tab"><img src="../../images/mobile/navbar/icon2.png" width="32" height="32" alt="超商鮮食"><br>超商鮮食</a></li> -->
<!-- 					<li><a id="a3" rel="external" href="http://trace.twfoodtrace.org.tw/fcloud/index.do#type3" class="ui-btn ui-corner-all ui-btn-inline" data-role="tab"><img src="../../images/mobile/navbar/icon3.png" width="32" height="32" alt="連鎖餐飲"><br>連鎖餐飲</a></li> -->
<!-- 					<li><a id="a4" href="#" class="ui-btn ui-corner-all ui-btn-inline" data-role="tab"><img src="../../images/mobile/navbar/m6.png" width="32" height="32" alt="校園團膳"><br>校園團膳</a></li> -->
<!-- 				</ul> -->
			</div>
		</div>
	</div>
	<div data-role="page" id="certCasPage">
		<div data-role="header" data-theme="b">
			<h1>CAS廠商認證資訊</h1>
		</div>
		<div id="certCasContent" data-role="content" data-theme="b"></div>
	</div>
</body>
</html>