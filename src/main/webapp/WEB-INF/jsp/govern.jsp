<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<!-- calendar -->
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
	function searchForNeg() {
		/*** 1.取得表格中要查詢的供應商名稱、食材名稱、菜單起迄日期 ***/

		var forNeg = document.forms["forNeg"];
		var supplyName = forNeg.elements["supplyName"].value;
		var ingredientName = forNeg.elements["ingredientName"].value;
		var startDate = forNeg.elements["startDate"].value;
		var endDate = forNeg.elements["endDate"].value;

		if (supplyName == "" && ingredientName == "") {
			alert("供應商名稱與食材名稱請勿皆空白。");
			return;
		}
		if (startDate == "" && endDate == "") {
			alert("請輸入起訖日期。");
			return;
		}

		/*** 2.取得異常條件陣列 ***/

		var request_data = {
			"method" : "queryForNeg",
			"args" : {
				"supplyName" : supplyName,
				"ingredientName" : ingredientName,
				"startDate" : startDate,
				"endDate" : endDate
			}
		};
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				//var new_div = "<table class=\"component\">";
				var new_div = "";
				new_div += "<tr>";
				new_div += "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\">編號</td>"
					+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\">供應商名稱</td>"
					+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\">供應產品</td>"
					+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\">批號</td>"
					+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\">有效日期</td>"
					+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\">進貨日期</td>"
					+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\">生產日期</td>"
					+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\">選項</td>";
			new_div += "</tr>";
			for(var i=0; i<result_content.forNegList.length; i++) {
				if(i % 2 == 0) {
				new_div += "<tr>";
				new_div += "<td bgcolor=\"#edf2e5\" class=\"\">"
						+ result_content.forNegList[i].ingredientId + "</td>";
				new_div += "<td bgcolor=\"#edf2e5\" class=\"\">"
						+ result_content.forNegList[i].supplyName
						+ "<input type=\"hidden\" value='"+result_content.forNegList[i].supplierId+"'></td>";
				new_div += "<td bgcolor=\"#edf2e5\" class=\"\">"
						+ result_content.forNegList[i].ingredientName
						+ "<input type=\"hidden\" value='"+result_content.forNegList[i].ingredientId+"'></td>";
				new_div += "<td bgcolor=\"#edf2e5\" class=\"\">"
						+ result_content.forNegList[i].lotNumber + "</td>";
				new_div += "<td bgcolor=\"#edf2e5\" class=\"\">"
						+ result_content.forNegList[i].expirationDate + "</td>";
				new_div += "<td bgcolor=\"#edf2e5\" class=\"\">"
						+ result_content.forNegList[i].stockDate + "</td>";
				new_div += "<td bgcolor=\"#edf2e5\" class=\"\">"
						+ result_content.forNegList[i].manufactureDate + "</td>";
				new_div += "<td bgcolor=\"#edf2e5\" class=\" BT_IN_BBTER\"><div align=\"center\"><input type=\"button\" value=\"加入管制食材\" onclick=goAddNegPage('"
						+ result_content.forNegList[i].ingredientName
						+ "','"
						+ result_content.forNegList[i].supplyName
						+ "','"
						+ result_content.forNegList[i].ingredientId
						+ "','"
						+ result_content.forNegList[i].supplierId
						+ "','"
						+ result_content.forNegList[i].lotNumber
						+ "','"
						+ result_content.forNegList[i].expirationDate
						+ "','"
						+ result_content.forNegList[i].stockDate
						+ "','"
						+ result_content.forNegList[i].manufactureDate +"')></div></td>";
				new_div += "</tr>";
				}else{
					new_div += "<tr>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"\">"
							+ result_content.forNegList[i].ingredientId + "</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"\">"
							+ result_content.forNegList[i].supplyName
							+ "<input type=\"hidden\" value='"+result_content.forNegList[i].supplierId+"'></td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"\">"
							+ result_content.forNegList[i].ingredientName
							+ "<input type=\"hidden\" value='"+result_content.forNegList[i].ingredientId+"'></td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"\">"
							+ result_content.forNegList[i].lotNumber + "</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"\">"
							+ result_content.forNegList[i].expirationDate + "</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"\">"
							+ result_content.forNegList[i].stockDate + "</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"\">"
							+ result_content.forNegList[i].manufactureDate + "</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\" BT_IN_BBTER\"><div align=\"center\"><input type=\"button\" value=\"加入管制食材\" onclick=goAddNegPage('"
							+ result_content.forNegList[i].ingredientName
							+ "','"
							+ result_content.forNegList[i].supplyName
							+ "','"
							+ result_content.forNegList[i].ingredientId
							+ "','"
							+ result_content.forNegList[i].supplierId
							+ "','"
							+ result_content.forNegList[i].lotNumber
							+ "','"
							+ result_content.forNegList[i].expirationDate
							+ "','"
							+ result_content.forNegList[i].stockDate
							+ "','"
							+ result_content.forNegList[i].manufactureDate +"')></div></td>";
					new_div += "</tr>";
					
					
					
				}
			}
			//new_div += "</table>";
			new_div += "";
			$("#forNeglist").html("");
			$("#forNeglist").append(new_div);
			$("#forNeglist").show();
		}
	}}
	function goAddNegPage(ingredientName, supplyName, ingredientId, supplierId,
			ingredientBatchId, expirationDate, stockDate, manufactureDate) {
		document.forms["setNegPage"]["set_ingredientName"].value = ingredientName;
		document.forms["setNegPage"]["set_supplyName"].value = supplyName;
		document.forms["setNegPage"]["set_ingredientId"].value = ingredientId;
		document.forms["setNegPage"]["set_supplierId"].value = supplierId;
		document.forms["setNegPage"]["set_ingredientBatchId"].value = ingredientBatchId;
		document.forms["setNegPage"]["set_expirationDate"].value = expirationDate;
		document.forms["setNegPage"]["set_stockDate"].value = stockDate;
		document.forms["setNegPage"]["set_manufactureDate"].value = manufactureDate;
		$("#setNegPage").submit();
	}
	
	 $(document).ready(function(){	
			
			//將查詢時間預設為當月的開始日與結束日
			defaultYearMonth();
		});
</script>
</head>
<body>
<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
<div class="TITLE_TXT flo_l">供應商與食材查詢</div>
<div class="TITLE_TXT_BBT FL_R">
			<a href="#"  onclick="history.go(-1);return false">回前頁</a>
		</div>
</div>

<div id="MAIN_SECOND_BAR" class=" lh_35 GRA_ABR TAB_TY_B">
	<form id="forNeg" method="post" action="">
<input type="hidden" name="action" value="saerchInput">
	<table width="100%" >
	 <tbody>
		<tr>
			<td bgcolor="#edf2e5" class="" align="center">供應商名稱</td>
	  <td  bgcolor="#edf2e5" class="" >
	    <input class="searchInput max255" type="text" name="supplyName" id="supplyName" size="25" value=''>	</td>
	<td bgcolor="#edf2e5" class="" align="center">食材名稱</td>
	  <td  bgcolor="#edf2e5" class="" >
	  <input class="searchInput max255" type="text" name="ingredientName" id="ingredientName" size="25" value=''></td>
	
	<tr>
		<td  bgcolor="#edf2e5" class="" align="center">起訖日期:</td>
	  <td bgcolor="#edf2e5" class=" " colspan="3">
	  <input type="text" id="startDate"  class="dateMode"/>	
      ~
			<input type="text" id="endDate"  class="dateMode" /></td>
	</tr>
	<tr><td colspan="4" align="center" class="BT_IN_BBTER"><input type=button
					onClick="searchForNeg()" value='查詢'></tr>
	</tbody></table>
		
</form> </div>
<div style="display:block;">
		<table class="component" id=forNeglist style="display:none;">	
	</table>
 </div>


	
	
	<form style="display: hidden" action="../addgovern/" method="POST"	id="setNegPage" name="setNegPage">
		<input type="hidden" id="set_ingredientName" name="set_ingredientName"	value="" /> 
		<input type="hidden" id="set_supplyName" name="set_supplyName" value="" /> 
		<input type="hidden" id="set_ingredientId" name="set_ingredientId"	value="" /> 
		<input type="hidden" id="set_supplierId" name="set_supplierId" value="" /> 
		<input type="hidden" id="set_ingredientBatchId" name="set_ingredientBatchId" value="" />
		<input type="hidden" id="set_expirationDate" name="set_expirationDate" value="" /> 
		<input type="hidden" id="set_stockDate" name="set_stockDate" value="" /> 
		<input type="hidden" id="set_manufactureDate" name="set_manufactureDate" value="" />
	</form>
</body>
</html>