<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script>
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
}
	function addNeg() {
		/*** 1.取得要管制項目的資訊***/

		var query_form = document.forms["addNeg"];
		var ingredientName = query_form.elements["ingredientName"].value;
		var supplyName = query_form.elements["supplyName"].value;
		var ingredientId = query_form.elements["ingredientId"].value;
		var supplierId = query_form.elements["supplierId"].value;
		var ingredientBatchId = query_form.elements["ingredientBatchId"].value;
		var expirationDate = query_form.elements["expirationDate"].value;
		var stockDate = query_form.elements["stockDate"].value;
		var manufactureDate = query_form.elements["manufactureDate"].value;
		var startDate = query_form.elements["startDate"].value;
		var endDate = query_form.elements["endDate"].value;
		var description = query_form.elements["description"].value;
		
		/*** 2.新增管制條件 ***/
		var request_data = {
			"method" : "addNeg",
			"args" : {
				//"ingredientName" : ingredientName,
				//"supplyName" : supplyName,
				"ingredientId" : ingredientId,
				"supplierId" : supplierId,
				"lotNumber" : ingredientBatchId,
				"expirationDate" : expirationDate,
				"stockDate" : stockDate,
				"manufactureDate" : manufactureDate,
				"startDate" : startDate,
				"endDate" : endDate,
				"description" : description
			}
		};
		var response_obj = call_rest_api(request_data);
		//判斷查詢是否成功
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				
				alert("新增成功");
				location.href = "../abnormal/";
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
		
	}

	 $(document).ready(function(){	
			
			//將查詢時間預設為當月的開始日與結束日
			defaultYearMonth();
		});
</script>
</head>
<body>
	<%
		String ingredientName = new String(request.getParameter("set_ingredientName"));
		String supplyName = new String(request.getParameter("set_supplyName"));
		String ingredientId = new String(request.getParameter("set_ingredientId"));
		String supplierId = new String(request.getParameter("set_supplierId"));
		String ingredientBatchId = new String(request.getParameter("set_ingredientBatchId"));
		String expirationDate = new String(request.getParameter("set_expirationDate"));
		String stockDate = new String(request.getParameter("set_stockDate"));
		String manufactureDate = new String(request.getParameter("set_manufactureDate"));
	%>
<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
<div class="TITLE_TXT flo_l BT_IN_BBTER" >異常食材管制設定</div>
		<div class="TITLE_TXT_BBT FL_R">
			<a href="#"  onclick="history.go(-1);return false">回前頁</a>
		</div>
</div>
	<form id="addNeg" enctype="multipart/form-data" method="post">
		<div id="writeProduct"  class="TAB_TY_B">
			<table class="component">
				<tr>
					<td>供應商名稱</td>
					<td><%=supplyName%> 
					<input type="hidden" id="supplyName" name="supplyName" value="<%=supplyName%>">
					<input type="hidden" id="supplierId" name="supplierId" value="<%=supplierId%>">
					<td>供應產品</td>
					<td><%=ingredientName%> 
					<input type="hidden" id="ingredientName" name="ingredientName" value="<%=ingredientName%>">
					<input type="hidden" id="ingredientId"
						name="ingredientId" value="<%=ingredientId%>"></td>
				</tr>
				<tr>
					<td  bgcolor="#edf2e5" >批號</td>
					<td bgcolor="#edf2e5" ><%=ingredientBatchId%>
					<input type="hidden" id="ingredientBatchId" name="ingredientBatchId" value="<%=ingredientBatchId%>"></td>
					<td bgcolor="#edf2e5" >有效日期</td>
					<td bgcolor="#edf2e5" ><%=expirationDate%>
					<input type="hidden" id="expirationDate" name="expirationDate" value="<%=expirationDate%>"></td>
				</tr>
				<tr>
					<td>進貨日期</td>
					<td><%=stockDate%>
					<input type="hidden" id="stockDate" name="stockDate" value="<%=stockDate%>"></td>
					<td>生產日期</td>
					<td><%=manufactureDate%>
					<input type="hidden" id="manufactureDate" name="manufactureDate" value="<%=manufactureDate%>"></td>
				</tr>
				<tr>
					<td bgcolor="#edf2e5" >管制起訖日</td>
					<td colspan='3'  bgcolor="#edf2e5" ><input type='text' id="startDate"
						class="dateAD dateMode" /> ~<input type='text'
						id="endDate" value=''  class="dateMode"> <span></span></td>
				</tr>
				<tr>
					<td><span style="color: red; float: right"></span>異常說明</td>
					<td colspan='3'><textArea id="description" name="description" style="font-size:12px;"
							 cols="80" rows="" maxlength="250"></textarea> <span></span></td>
				</tr>

				<tr>
					<td colspan="4" align="center" class="BT_IN_BBTER"><input type="button"
						id="productName" name="productName" onClick="addNeg()" value="確認" />
					</td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>