<%@ page import="org.iii.ideas.catering_service.util.AuthenUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="util.jsp" %> <!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js_ca/combobox.js"></script>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/listCertification.js"></script>
<style>
	.searchInput
	{
		width: 130px;
	}
</style>
</head>
<script>
$(document).ready(function(){	
	 queryKitchen();	//查詢業者
	//將查詢時間預設為當月的開始日與結束日
	defaultYearMonth();
});
</script>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">檢驗報告資訊</div>
		<div class="contents-wrap">
	        <div class="section-wrap has-border">
	          <div class="form-horizontal kitchen-info">
	             <form method="post" id="Certification">
					<input type="hidden" name="action" value="saerchInput">
						<table class="table-bordered table-striped" width="100%" >
							 <tbody>
								<tr>
									<td style="padding: 10px;" align="center">食材名稱</td>
							  		<td style="padding: 10px;">
							    		<input class="searchInput max255" type="text" name="ingredientName" id="ingredientName" size="25" value=''>	
							    	</td>
									<td style="padding: 10px;" align="center">業者/廚房 <font color="red">*</font></td>
							  		<td style="padding: 10px;">
							   			<span id="dropDown_kitchen">
							   		</td>
							   	</tr>
							    <tr>
									<td style="padding: 10px;" align="center">菜單日期 <font color="red">*</font></td>
							  		<td style="padding: 10px;" colspan="3">
							  			<div class="inline-wrap datetimepicker-start" style="width: 30%">
					                        <input type="text" class="form-control inline datetimepicker" id="startDate">
					                   </div>
							  			<!-- <input type="text" id="startDate"  class="dateMode"/> -->	
						                <div class="inline-wrap datetimepicker-to">to</div>
						      			<div class="inline-wrap datetimepicker-end" style="width: 30%">
					                    	<input type="text" class="form-control inline datetimepicker" id="endDate">
					                   </div>
								</tr>
								<tr>
									<td style="padding: 10px;" colspan="4" align="center" ><input class="btn btn-primary" style="margin: 0" type="button" onclick='CertificationList(1)' value="查詢"></td>
								</tr>
							</tbody>
					    </table>	
				</form>
				<h5 class="section-head with-border" style="height:35px; margin-bottom: 10px;">
					<div class="TITLE_TXT flo_l ">查詢結果</div>
					<div id="query_rule" class="TITLE_TXT flo_l"></div>
					<div class="TITLE_TXT flo_l "><span id="page" ></span></div>
					<div class="TITLE_TXT_BBT FL_R">
<!-- 						<div id="tiltleAct"> -->
<!-- 							<a href="#" onclick="export_excel()" style="float:right; margin: 0;" >匯出</a>  -->
<!-- 						</div> -->
					</div>
				</h5>	
				<div style="display:block;">
					<table class="table table-bordered table-striped" id="fcertificationList"  style="display:none;"></table>
				</div>
	          </div>
	        </div><!-- End of .section-wrap -->        
	    </div>
       
	<!-- 舊團膳套版 -->
	<div style="display:none">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
		<div class="TITLE_TXT flo_l">檢驗報告資訊</div>
		</div>
		
		<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B">
		<form method="post" id="Certification">
		<input type="hidden" name="action" value="saerchInput">
			<table width="100%" >
			 <tbody>
				<tr>
					<td bgcolor="#edf2e5" class="" align="center">食材名稱</td>
			  <td  bgcolor="#edf2e5" class="" >
			    <input class="searchInput max255" type="text" name="ingredientName" id="ingredientName" size="25" value=''>	</td>
			<td bgcolor="#edf2e5" class="" align="center">業者/廚房</td>
			  <td  bgcolor="#edf2e5" class="" >
			   <span id="dropDown_kitchen"></td>
			
			<tr>
				<td  bgcolor="#edf2e5" class="" align="center">菜單日期 *</td>
			  <td bgcolor="#edf2e5" class=" " colspan="3">
			  <input type="text" id="startDate"  class="dateMode"/>	
		      ~
					<input type="text" id="endDate"  class="dateMode" /></td>
			</tr>
			<tr><td colspan="4" align="center" class="BT_IN_BBTER"><input type="button" onclick='CertificationList(1)' value="查詢"></td></tr>
			</tbody></table>
				
		</form>
				 
		 </div>
		<div style="display:block;"><table class="component" id="fcertificationList"  style="display:none;">
				</table></div>
	</div>
</body>
</html>