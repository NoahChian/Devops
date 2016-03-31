<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/tiles-jsp.tld" prefix="tiles" %>
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
	county = (String) session.getAttribute("county");
}
if (uName.length()>=4){
	if (!roletype.substring(0,4).equals("kSys")){
		out.print("無此頁面");
		return;
	}
}else{
	out.print("無此頁面");
	return;
} 
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta HTTP-equiv="Expires" content="0">
<meta HTTP-equiv="kiben" content="no-cache">
<meta name="description" content="<tiles:insertAttribute name="description"/>">
<!--  <meta http-equiv="refresh" content="0; url=http://lunch.twfoodtrace.org.tw/cateringservice/web/main/" /> -->
<title><tiles:insertAttribute name="title"/></title>

<script src="../../js/jquery-1.9.1.js" type="text/javascript"></script>
<script src="../../js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>	
<link href="../../css/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />	

<link rel="shortcut icon" href="../../images/auth/logo.ico" type="image/x-icon" />
<link href="../../css/integration.css" rel="stylesheet" type="text/css" />

<script src="../../js_ca/common.js"></script>

<% if(session.getAttribute("account")!=null){%> 
<!-- 
	<script src="../../js/datepicker/3.7.4/jquery.datepick.js" type="text/javascript"></script>	
	<script src="../../js/datepicker/3.7.4/jquery.datepick.tw.js" type="text/javascript"></script>
 -->	
	<script src="../../js/jquery-validate/jquery.validate.js" type="text/javascript" ></script>
	<script src="../../js/jquery-validate/localization/messages_tw.js" type="text/javascript" ></script>
	<script src="../../js/jquery.blockUI.js"></script>
<% }%>
<script src="../../js/fancybox/jquery.fancybox.js?v=2.1.5" type="text/javascript" ></script>
<script src="../../js/jquery.session.js" type="text/javascript" ></script>
<link rel="stylesheet" type="text/css" href="../../js/fancybox/jquery.fancybox.css?v=2.1.5" media="screen" />

<!-- calendar -->
<link href="../../css/jquery-ui.css" rel="stylesheet" type="text/css" />

<!-- 四點設計JS及CSS -->
<link href="../../css/screen.css" rel="stylesheet">

<!-- <script src="../../js/jquery-1.11.3.min.js" type="text/javascript" ></script>
<script src="../../js/jquery.placeholder.min.js" type="text/javascript" ></script> -->
<script src="../../js/selectivizr-min.js" type="text/javascript" ></script>
<script src="../../js/bootstrap.min.js" type="text/javascript" ></script>
<script src="../../js/moment.js" type="text/javascript" ></script>
<script src="../../js/bootstrap-datetimepicker.min.js" type="text/javascript" ></script>
<script src="../../js/bootstrap-datetimepicker-zh-tw.js" type="text/javascript" ></script> 
<script src="../../js/main.js" type="text/javascript" ></script>

<!-- 四點設計JS及CSS -->

<script src="../../js/jquery.md5.js"></script>
<script src="../../js/nodeJsSrv_util.js" type="text/javascript"></script>
<script src="../../js_ca/navi.js"></script>
   	
<style>
	body
	{
		overflow-y:scroll;
	}
	
    input.error { border: 1px dotted red; }
    select.error { border: 1px dotted red; }
    textarea.error { border: 1px dotted red; }
      
	.error { color: red; }
	
	.block { display: block; }

    
</style>
</head>

<body>
<center>

<div id="main" align="left">
	<div id="header">
		<tiles:insertAttribute name="header"/>
    </div>
    <div id="CONTENT">
    	<main>
		    <div class="container-fluid">
			    <div class="row">
			    	<div id="SUB_NAV">
			    		<div class="col-xs-12 sidebar-nav">
			    			<div class="sidebar-nav__title">功能選單</div>
								<nav>
							      <ul>
							        <li>
							          <a><i class="fa fa-cog"></i>系統管理</a>
							          <ul>
							            <li><a href="../service/"><i class="fa fa-search"></i>使用者紀錄查詢</a></li>
							          </ul>
							        </li>
							      </ul>
							    </nav>
						</div>
				    </div>
		        	<div id="MAINi">
			        	<div class="col-xs-12 contents">
			        		<tiles:insertAttribute name="content"/>
			        	</div>
			        </div>
		        </div>
		    </div>
    	</main>
    </div>
    <div id="footer" style="padding-top:10px;">
    	<tiles:insertAttribute name="footer"/>
    </div>
</div>
</center>
<div id="FOOTERx">
	<span class="FOOTER_JO_FG">
		<a href="http://lunch.twfoodtrace.org.tw/" target="_blank" >校園食材登錄平臺</a>
			最佳瀏覽環境為<a href="http://www.google.com/intl/zh-TW/chrome/" target="_blank" >Chrome</a>
		或<a href="http://moztw.org/firefox/" target="_blank">Firefox</a>。<br>
		系統操作過程有任何問題，請<a href="mailto:catstory@iii.org.tw">聯絡管理人員</a>
	</span>
</div>
<!-- <div id="FOOTERx">
	<span class="FOOTER_JO_FG">
		<a href="http://lunch.twfoodtrace.org.tw/" target="_blank" >食材登錄平臺</a>最佳瀏覽環境為<a href="http://www.google.com/intl/zh-TW/chrome/" target="_blank" >Chrome</a>或<a href="http://moztw.org/firefox/" target="_blank">Firefox</a>。系統操作過程有任何問題，請<a href="mailto:catstory@iii.org.tw">聯絡管理人員</a>。
	</span>
</div> -->
</body>
</html>