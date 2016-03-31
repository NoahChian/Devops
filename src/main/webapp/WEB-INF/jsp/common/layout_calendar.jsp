<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/tiles-jsp.tld" prefix="tiles" %>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta HTTP-equiv="Expires" content="0">
<meta HTTP-equiv="kiben" content="no-cache">
<meta name="description" content="<tiles:insertAttribute name="description"/>">
<!--  <meta http-equiv="refresh" content="0; url=http://lunch.twfoodtrace.org.tw/cateringservice/web/main/" /> -->
<title><tiles:insertAttribute name="title"/></title>
<link rel="shortcut icon" href="../../images/auth/logo.ico" type="image/x-icon" />
<!-- 
<link href="../../../../css/style3.css" rel="stylesheet" type="text/css" />
<link href="../../../../css/common.css" rel="stylesheet" type="text/css" />
<link href="../../../../css/css.css" rel="stylesheet" type="text/css" />
<link href="../../../../css/messageCenter.css" rel="stylesheet" type="text/css" />
-->

<link href="../../../../css/integration.css" rel="stylesheet" type="text/css" />
<!-- 
<link href="../../js/datepicker/3.7.4/redmond.datepick.css" rel="stylesheet" type="text/css" />
<link href="../../js/jquery-autocomplete/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<link href="../../js/jquery.treeview/jquery.treeview.css" rel="stylesheet"  type="text/css" />
-->

<script src="../../../../js/jquery-1.9.1.js" type="text/javascript"></script>
<script src="../../../../js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>	
<link href="../../../../css/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />	

<!-- script src="../../js/jquery-autocomplete/jquery.autocomplete.js" type="text/javascript" ></script-->
<!-- script src="../../js/jquery.treeview/lib/jquery.cookie.js" type="text/javascript"></script-->
<!--script src="../../js/jquery.treeview/jquery.treeview.js" type="text/javascript"></script-->     
<!--script src="../../js/iservbilling.js"></script-->   

<script src="../../../../js_ca/common.js"></script>

<script src="../../../../js/fancybox/jquery.fancybox.js?v=2.1.5" type="text/javascript" ></script>
<link rel="stylesheet" type="text/css" href="../../../../js/fancybox/jquery.fancybox.css?v=2.1.5" media="screen" />
  	
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

<body >
<center>
<div id="main" align="left" style="width:800px;">



        	<tiles:insertAttribute name="content"/>



</div>
</body>
</html>