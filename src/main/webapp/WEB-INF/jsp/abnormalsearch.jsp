<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="util.jsp" %> <!-- include js的自訂公用函式    20140219 KC -->    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<!-- calendar -->
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="../../js/webtoolkit.js"></script>
<style> 
	.searchInput
	{
		width: 130px;
	}
</style>
<script>//calendar
$(function() {
	$( "#startDate" ).datepicker({ dateFormat: "yy/mm/dd" });
	$( "#endDate" ).datepicker({ dateFormat: "yy/mm/dd" });
	//預設值為先寫死為2013/12/01~2013/12/31
	//$( "#start_date" ).attr("value", "2013/12/01");
	//$( "#end_date" ).attr("value", "2013/12/31")
	});
/*使用Titan的預設日期選擇function*/
function defaultYearMonth(){
	var currentTime = new Date();
	var year = currentTime.getFullYear();
	var month = currentTime.getMonth() + 1;
	
	//alert(year +","+ month);
	
	var start = year +"/"+ month +"/01";
	$("#startDate").val(start);
/*	
	var end = "";
	if(month==1 || month==3 || month==5 || month ==7 || month==8 || month ==10 || month==12){
		end = year +"/"+ month +"/31";
		$("#endDate").val(end);
	} else if (month==4 || month==6 || month==9 || month==11){
		end = year +"/"+ month +"/30";
		$("#endDate").val(end);
	} else if (month == 2){
		end = year +"/"+ month +"/28";
		$("#endDate").val(end);
	}
*/
	//重寫結束時間預設值  20140219 KC
	var monthDays=new Date(year,month,0).getDate();
	$("#endDate").val(year +"/"+ month +"/"+monthDays);	
}
function downloadfilexls(){
	//檢查查詢期間不得大於90天       20140219 KC
	if (!util_check_date_range($("#startDate").datepicker("getDate"),$("#endDate").datepicker("getDate"))){
		return;
	}
	
	//document.getElementById("downloadfile").value = "檔案準備中...";
	  var ingredientName = document.getElementById("ingredientName").value;
	  var supplierName = document.getElementById("supplierName").value;
	  var startDate = document.getElementById("startDate").value;
	  var endDate = document.getElementById("endDate").value;
	 
	startDate = startDate.replace("/", "-");
	startDate = startDate.replace("/", "-");
	
	endDate = endDate.replace("/", "-");
	endDate = endDate.replace("/", "-");
	
	var link = "/cateringservice/rest/API/XLS/abnormalsearch&" + ingredientName+ "&"+ supplierName + "&" + startDate + "&" + endDate;
	
	//alert(link);
	
	window.open(link,"_blank");
}
  $(document).ready(function(){	
		
		//將查詢時間預設為當月的開始日與結束日
		defaultYearMonth();
	});
</script>

</head>
<body>
<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
<div class="TITLE_TXT flo_l">異常資料查詢</div>
</div>

<div id="MAIN_SECOND_BAR" class="h_35px lh_35 GRA_ABR TAB_TY_B">
<form method="post" >
<input type="hidden" name="action" value="saerchInput">
	<table width="100%" >
	 <tbody>
		<tr>
			<td bgcolor="#ffffff" class="" align="center">食材名稱*</td>
	  <td  bgcolor="#ffffff" class="" >
	    <input class="searchInput max255" type="text" name=ingredientName" id="ingredientName" size="25" value=''>		</td>
	<td bgcolor="#ffffff" class="" align="center">供應商名稱</td>
	  <td  bgcolor="#ffffff" class="" >
	   <input class="searchInput max255" type="text" name="supplierName" id="supplierName" size="25" value=''></td>
	
	<tr>
		<td  bgcolor="#edf2e5" class="" align="center">起訖日期 *</td>
	  <td bgcolor="#edf2e5" class=" " colspan="3">
	  <input type="text" id="startDate"  class="dateMode" />	
      ~
			<input type="text" id="endDate"  class="dateMode"  /> (進貨、生產、有效日期)</td>
	</tr>
	<tr><td colspan="4" align="center" class="BT_IN_BBTER"><input type="button" onclick='downloadfilexls()' id="downloadfile" value="匯出excel報表"></td></tr>
	</tbody></table>
		
</form>
		
  </div>

</body>
</html>