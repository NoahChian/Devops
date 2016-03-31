<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<!--  !DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"-->
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tiles-jsp.tld" prefix="tiles" %>
<%
	String url=request.getContextPath();
	String domainName=request.getServerName();
	String  demoDisplay="";
	if ("lunch.twfoodtrace.org.tw".equals(domainName)){
		demoDisplay="none";
	}
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta HTTP-equiv="Expires" content="0">
<meta HTTP-equiv="kiben" content="no-cache">
<meta name="description" content="<tiles:insertAttribute name="description"/>">

<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title><tiles:insertAttribute name="title"/></title>
<link rel="shortcut icon" href="../../../../images/logo.ico" type="image/x-icon" />
<link href="../../../../css/integration_sf.css" rel="stylesheet" type="text/css" />

<script src="../../../../js/jquery-1.9.1.js" type="text/javascript"></script>
<script src="../../../../js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>	
<link href="../../../../css/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />	 
<% /*解決IE相容性問題  20140922 KC*/ %>
<script type="text/javascript">
    if (typeof (JSON) == 'undefined') { //沒有 JSON 時才讓 browser 下載 json2.js
        $('head').append($("<script type='text/javascript' src='../../../../js/json2.js' />"));
    }
</script>

<script src="../../../../js_ca/common.js"></script>


<% if(session.getAttribute("account")!=null){%> 
	<script src="../../../../js/datepicker/3.7.4/jquery.datepick.js" type="text/javascript"></script>	
	<script src="../../../../js/datepicker/3.7.4/jquery.datepick.tw.js" type="text/javascript"></script>
	<script src="../../../../js/jquery-validate/jquery.validate.js" type="text/javascript" ></script>
	<script src="../../../../js/jquery-validate/localization/messages_tw.js" type="text/javascript" ></script>
	<script src="../../../../js/jquery.blockUI.js"></script>
<% }%>
<script src="../../../../js/fancybox/jquery.fancybox.js?v=2.1.5" type="text/javascript" ></script>
<script src="../../../../js/jquery.session.js" type="text/javascript" ></script>
<link rel="stylesheet" type="text/css" href="../../../js/fancybox/jquery.fancybox.css?v=2.1.5" media="screen" />

<!-- calendar -->
<link href="../../../css/jquery-ui.css" rel="stylesheet" type="text/css" /-->

   	
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

<body ><div id="TOPER" class="GRE_ABR"></div>
<center>

<div id="main" align="left">
	<div id="header">
		<tiles:insertAttribute name="header"/>
    </div>
    <div id="CONTENT">
        <div id="MAINi" >
        	<tiles:insertAttribute name="content"/>
        </div>
    </div>
    <div id="footer" style="padding-top:10px;">
    	<tiles:insertAttribute name="footer"/>
    </div>
</div>
</center>
<div id="FOOTERx">
	<span class="FOOTER_JO_FG">
		<a href="http://lunch.twfoodtrace.org.tw/" target="_blank" >食材登錄平台</a>
			最佳瀏覽環境為<a href="http://www.google.com/intl/zh-TW/chrome/" target="_blank" >Chrome</a>
		或<a href="http://moztw.org/firefox/" target="_blank">Firefox</a>。<br>
		系統操作過程有任何問題，請來信至客服信箱：<a href="mailto:catering@iii.org.tw">catering@iii.org.tw</a>
		或 資策會客服電話：(02)6607-2122
	</span>
</div>
		
		
		  
</body>
</html>
