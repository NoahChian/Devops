<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script>
//20131210(Ric):解除異常管制
	 function deleteNeg(iI,sD,lN) {
		var idneg_ingredientId= "";
		var stockDate = "";
		var lotNumber = "";
		if(iI != null || iI!=""){ idneg_ingredientId= iI;}
		if(sD != null || sD!=""){ stockDate= sD;}
		if(lN != null || lN!=""){ lotNumber= lN;}
		var request_data = {
				"method":"deleteNeg",
			    "args": {
			    	"idneg_ingredientId": idneg_ingredientId,
			    	"stockDate": stockDate,
			    	"lotNumber": lotNumber
			}
		};

		var response_obj = call_rest_api(request_data);
		//判斷查詢是否成功
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				
				alert("解除成功");
				location.href = "../abnormal/";
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}
	function ListNeg() {
		// 取得異常清單
		var request_data = {
				"method":"listNeg",
			    "args": {
			        "listNegQuery": "true"
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
				new_div += "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\"> 供應商名稱 </td>"
						+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\"> 管制產品名稱 </td>"
						+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\"> 批號 </td>"
						+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\"> 有效日期 </td>"
						+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\"> 進貨日期 </td>"
						+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\"> 生產日期 </td>"
						+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\"> 管制日期 </td>"
						+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\"> 異常說明 </td>"
						+ "<td bgcolor=\"#a1c66a\" class=\"TIT_A\" align=\"center\"> 選項 </td>";
				new_div += "</tr>";
				for(var i=0; i<result_content.negList.length; i++) {
					if(i % 2 == 0) {
					new_div += "<tr>";
					new_div += "<td bgcolor=\"#edf2e5\" class=\"componetContentAdmi\">";
					if(result_content.negList[i].supplyName !=null ){		
					new_div += result_content.negList[i].supplyName
							+ "<input type=\"hidden\" value='"+result_content.negList[i].supplierId+"'>";}
					new_div +=	"</td>";
					new_div += "<td bgcolor=\"#edf2e5\" class=\"componetContentAdmi\">";
					if(result_content.negList[i].ingredientName !=null ){		
					new_div += result_content.negList[i].ingredientName
							+ "<input type=\"hidden\" value='"+result_content.negList[i].ingredientId+"'>";}		
					new_div +=	"</td>";	
					new_div += "<td bgcolor=\"#edf2e5\" class=\"componetContentAdmi\">";
					if(result_content.negList[i].lotNumber !=null ){				
					new_div += result_content.negList[i].lotNumber ;}
					new_div +="</td>";
					new_div += "<td bgcolor=\"#edf2e5\" class=\"componetContentAdmi\">";
					if(result_content.negList[i].expirationDate !=null) {		
					new_div +=  result_content.negList[i].expirationDate;}
					new_div +=  "</td>";
					new_div += "<td bgcolor=\"#edf2e5\" class=\"componetContentAdmi\">";
					if(result_content.negList[i].stockDate != null){		
					new_div += result_content.negList[i].stockDate ;}
					new_div +=	"</td>";
					new_div += "<td bgcolor=\"#edf2e5\" class=\"componetContentAdmi\">";
					if(result_content.negList[i].manufactureDate != null){		
					new_div += result_content.negList[i].manufactureDate ;}
					new_div += "</td>";
					new_div += "<td bgcolor=\"#edf2e5\" class=\"componetContentAdmi\">";
					if(result_content.negList[i].beg_date != null&&result_content.negList[i].beg_date != ""){	
						new_div +=  result_content.negList[i].beg_date ;}
					new_div +=  "~";
					if(result_content.negList[i].beg_date != null&& result_content.negList[i].end_date != null&&result_content.negList[i].beg_date != ""&& result_content.negList[i].end_date !=""){	
						new_div +=  "<br>";	}
					if(result_content.negList[i].end_date != null&&result_content.negList[i].end_date !=""){	
						new_div +=  result_content.negList[i].end_date;}
					new_div +="</td>";
					new_div += "<td bgcolor=\"#edf2e5\" class=\"componetContentAdmi\">"
							+ result_content.negList[i].description + "</td>";
					new_div += "<td bgcolor=\"#edf2e5\" class=\"componetContentAdmi BT_IN_BBTER\"><div align=\"center\"><input type=\"button\" value=\"解除管制\" onclick=deleteNeg('"
					+result_content.negList[i].idneg_ingredientId+"','"+result_content.negList[i].stockDate+"','"+result_content.negList[i].lotNumber+"')></div></td>";
					new_div += "</tr>";
					}else{
					new_div += "<tr>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"componetContentAdmi2\">";
					if(result_content.negList[i].supplyName !=null ){		
					new_div += result_content.negList[i].supplyName
							+ "<input type=\"hidden\" value='"+result_content.negList[i].supplierId+"'>";}
					new_div +=	"</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"componetContentAdmi2\">";
					if(result_content.negList[i].ingredientName !=null ){		
					new_div += result_content.negList[i].ingredientName
							+ "<input type=\"hidden\" value='"+result_content.negList[i].ingredientId+"'>";}		
					new_div +=	"</td>";	
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"componetContentAdmi2\">";
					if(result_content.negList[i].lotNumber !=null ){				
					new_div += result_content.negList[i].lotNumber ;}
					new_div +="</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"componetContentAdmi2\">";
					if(result_content.negList[i].expirationDate !=null) {		
					new_div +=  result_content.negList[i].expirationDate;}
					new_div +=  "</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"componetContentAdmi2\">";
					if(result_content.negList[i].stockDate != null){		
					new_div += result_content.negList[i].stockDate ;}
					new_div +=	"</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"componetContentAdmi2\">";
					if(result_content.negList[i].manufactureDate != null){		
					new_div += result_content.negList[i].manufactureDate ;}
					new_div += "</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"componetContentAdmi2\">";
					if(result_content.negList[i].beg_date != null&&result_content.negList[i].beg_date != ""){	
						new_div +=  result_content.negList[i].beg_date ;}
					new_div += "~";
					if(result_content.negList[i].beg_date != null&& result_content.negList[i].end_date != null&&result_content.negList[i].beg_date != ""&& result_content.negList[i].end_date !=""){	
						new_div +=  "<br>";	}					
					if(result_content.negList[i].end_date != null&&result_content.negList[i].end_date !=""){	
						new_div +=  result_content.negList[i].end_date;
					}
					new_div +="</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"componetContentAdmi2\">"
							+ result_content.negList[i].description + "</td>";
					new_div += "<td bgcolor=\"#FFFFFF\" class=\"componetContentAdmi2 BT_IN_BBTER\"><div align=\"center\"><input type=\"button\" value=\"解除管制\" onclick=deleteNeg('"
						+result_content.negList[i].idneg_ingredientId+"','"+result_content.negList[i].stockDate+"','"+result_content.negList[i].lotNumber+"')></div></td>";
					new_div += "</tr>";
					}
				}
				//new_div += "</table>";
				new_div += "";
				$("#negList").html("");
				$("#negList").append(new_div);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}
	$(document).ready(function() {
		ListNeg(); //列出異常狀況清單
	});
</script>
</head>
<body>
<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
<div class="TITLE_TXT flo_l">異常資料設定</div>
<div class="TITLE_TXT_BBT FL_R">
			<a href='../govern/'>新增管制食材</a>
		</div>
</div>
<table class="component" id="negList">
		</table>
</body>
</html>