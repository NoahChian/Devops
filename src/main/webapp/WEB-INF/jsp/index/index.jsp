<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
</head>

<style>
	#main {
		margin:4px auto;
  		width:250px;
        height:200px;
		border:1px solid gray;

	}
	
</style>

<div id="main">
	<div id="header">
		<h2>團膳服務</h2>
	</div>
	 
	<div id="container" style="clear: both">
	  			
		<form action="<%=request.getContextPath() %>/web/index/login/" method="post" id="loginForm">			
					
			<label>帳號：</label>					
			<input type="text" name="account" class="required"  ><br>
			
			<label>密碼：</label>				
			<input type="password" name="password" class="required"  ><br>
			
			<!--label>識別碼</label>
			<input type="text" name="checkCode" class="required">
			<img id="checkCode" src="<%=request.getContextPath() %>/fcloud/index/checkCode" style="margin-left: 18px;height: 40px;width: 80px"/> 
			<label style="color:#0000ee;cursor: pointer;text-decoration: underline" onclick="updateCheckCode()">更新識別碼</label-->
			
			<div id="lower">						
				<input type="submit" value="登入">				
			</div>			
		</form>			
	</div>
</div>	
