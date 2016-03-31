var MSG = new MsgsProcessing();
var util = new CateringServiceUtil();

// 初始化
$(document).ready(function() {
	listSfSchoolproductsetWaitApproveBySchool();
});

function listSfSchoolproductsetWaitApproveBySchool(){
	// 查詢是否需要進行審核
	var req_data = {
		"method" : "queryAcceptSwitch",
		"args" : {
			"schoolId" : schoolId,
			"acceptType" : "productVerify"
		}
	};
	var resp_obj = call_rest_api(req_data);
	if (resp_obj.result == 1) {
		var result = resp_obj.result_content;
		listLength = result.acceptswitchList.length;
	}
	sfCloseValue = "checked";
	sfOpenValue = "";
	if(listLength > 0){
		status = result.acceptswitchList[0].status;
		if("1" == status){
			sfCloseValue = "";
			sfOpenValue = "checked";
		}
	}
	var tooltipMsg = "若勾選則合作社商品進行上架時需要經過審核確認才可上架";
	var ask_div = "";
	ask_div += "<br><br><br><br><br><br><br><br>";
	ask_div += "<div style=\"float:left\"><h4>商品上架審核</h4></div>";
	ask_div += "<div data-tooltip=\" "
			+ tooltipMsg
			+ "\" style=\"position:relative; bottom:0px; float:right\">商品上架審核<input type=\"radio\" onclick=\"opensfAcceptSwitch();\" name=\"sfApprove\" value=\"sfOpen\" "
			+ sfOpenValue
			+ ">開啟<input type=\"radio\" onclick=\"closesfAcceptSwitch();\" name=\"sfApprove\" value=\"sfClose\" "
			+ sfCloseValue + ">關閉</div>";
	ask_div += "<table class=\"table table-bordered table-striped\" width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  id=\"askCustomFields\">";
	ask_div += "<thead><tr>";
	ask_div += "<td class=\"text-center\">"
	+ " 	序號</td>";
	ask_div += "<td class=\"text-center\">"
	+ " 	商品名稱</td>";
	ask_div += "<td class=\"text-center\">"
	+ " 	供應商名稱</td>";
	ask_div += "<td class=\"text-center\">"
	+ " 	製造商名稱</td>";
	ask_div += "<td class=\"text-center\">"
	+ " 	包裝資訊</td>";
	ask_div += "<td class=\"text-center\">"
	+ " 	認證標章</td>";
	ask_div += "<td class=\"text-center\">"
	+ " 	認證編號</td>";
	ask_div += "<td class=\"text-center\">"
	+ " 	審核動作</td>";
	ask_div += "</tr></thead>";
	var request_data =	{
		"method":"querySfSchoolproductsetWaitApproveBySchool",
		"args":{
			"schoolId":schoolId
		}
	};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1){
		var result_content = response_obj.result_content;
		listLength = result_content.sfschoolproductsetwaitapprovebyschoolList.length;		
		for(var j=0;j<listLength;j++){
			var one = 1;
			var id = j+one;			
			ask_div += " <tr valign=\"top\" align=\"left\">";
			ask_div += " <td width=\"6%\" ";
			if(j%2 == 1){
				ask_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				ask_div +="class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += "<input type=\"hidden\" readonly=\"readonly\" id=\"customFieldValue\" name=\"customFieldValue\" value=\""+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].id+"\" placeholder=\"Input Name\" style=\"border: none;\"/><center>"+ id +"</center></td>";
			ask_div += " <td width=\"10%\" ";
			if(	j%2 == 1){
				ask_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				ask_div +="class='componetContent2 componentContentLeftLine' >";
			}
			ask_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].productName+"\" placeholder=\"Input Value\" style=\"border: none;\" /><center>"+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].productName+"</center></td>";
			ask_div += " <td width=\"12%\" ";
			if(j%2 == 1){
				ask_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
			}else if(j%2 == 0){
				ask_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
			}
			ask_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].suppliercompanyName+"\" placeholder=\"Input Value\" style=\"border: none;\" /><center>"+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].suppliercompanyName+"</center></td>";
			ask_div += " <td width=\"12%\" ";
			if(j%2 == 1){
				ask_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
			}else if(j%2 == 0){
				ask_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
			}
			ask_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].manufacturerName+"\" placeholder=\"Input Value\" style=\"border: none;\" /><center>"+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].manufacturerName+"</center></td>";
			ask_div += " <td width=\"10%\" ";
			if(j%2 == 1){
				ask_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
			}else if(j%2 == 0){
				ask_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
			}
			ask_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].packageType+"\" placeholder=\"Input Value\" style=\"border: none;\" /><center>"+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].packageType+"</center></td>";
			ask_div += " <td width=\"6%\" ";
			if(j%2 == 1){
				ask_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
			}else if(j%2 == 0){
				ask_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
			}
			ask_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].certification+"\" placeholder=\"Input Value\" style=\"border: none;\" /><center>"+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].certification+"</center></td>";
			ask_div += " <td width=\"10%\" ";
			if(j%2 == 1){
				ask_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
			}else if(j%2 == 0){
				ask_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
			}
			ask_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].certificationId+"\" placeholder=\"Input Value\" style=\"border: none;\" /><center>"+ result_content.sfschoolproductsetwaitapprovebyschoolList[j].certificationId+"</center></td>";
			ask_div += " <td width=\"34%\" ";
				if(j%2 == 1){
					ask_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
				}else if(j%2 == 0){
					ask_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
				}
				ask_div += "<button onclick=\"approveSfSchoolproductset($(this)," + result_content.sfschoolproductsetwaitapprovebyschoolList[j].id + ")\" class=\"btn btn-primary\"><i class=\"fa fa-check\"></i>核可</button> ";
				ask_div += "<button onclick=\"rejectSfSchoolproductset($(this)," + result_content.sfschoolproductsetwaitapprovebyschoolList[j].id + ")\" class=\"btn btn-primary\"><i class=\"fa fa-times-circle\"></i>否決</button> </td>";
			ask_div += " </tr>";
		}		
	}
	ask_div += " </td></tr></table>	";
	// 沒有待審核事項時, 要顯示無待審核資料的訊息
	if (listLength == 0) {
		ask_div += "<h4 align=\"center\">無待審核資料</h4>";
	}

	$("#sfSchoolproductsetWaitApproveBySchoolList").html("");
	$("#sfSchoolproductsetWaitApproveBySchoolList").append(ask_div);
	$("#sfSchoolproductsetWaitApproveBySchoolList").show();
}

/*
 * 開啟商品上架審核
 */
function opensfAcceptSwitch() {
	if ("checked" == sfOpenValue) {
		MSG.alertMsgs("check", "商品上架審核已開啟,無需再開啟", 0);
		return;
	}
	var answer = confirm("請問是否要開啟商品上架審核？");
	var openStatus = 1; // Status 狀態 0:停用 ,1:啟用
	if (answer) {
		var request_data = {
			"method" : "updateAcceptSwitchStatus",
			"args" : {
				"schoolId" : schoolId,
				"acceptType" : "productVerify",
				"status" : openStatus
			}
		};
		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { // success
				MSG.alertMsgs("check", "商品上架審核已開啟", 0);
				listSfSchoolproductsetWaitApproveBySchool();
			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
			}
		} else {
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg,
					0);
		}
	} else {
		listSfSchoolproductsetWaitApproveBySchool();
	}
}

/*
 * 關閉商品上架審核
 */
function closesfAcceptSwitch() {
	if ("checked" == sfCloseValue) {
		MSG.alertMsgs("check", "商品上架審核已關閉,無需再關閉", 0);
		return;
	}
	var answer = confirm("請問是否要關閉商品上架審核？");
	var closeStatus = 0;
	if (answer) {
		var request_data = {
			"method" : "updateAcceptSwitchStatus",
			"args" : {
				"schoolId" : schoolId,
				"acceptType" : "productVerify",
				"status" : closeStatus
			}
		};
		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { // success
				MSG.alertMsgs("check", "商品上架審核已關閉", 0);
				listSfSchoolproductsetWaitApproveBySchool();
			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
			}
		} else {
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg,
					0);
		}
	} else {
		listSfSchoolproductsetWaitApproveBySchool();
	}
}

/*
 * 核可商品上架審核
 */
function approveSfSchoolproductset(element, id) {
	var answer = confirm("請問是否要核可？");
	if (answer) {
		var request_data = {
		"method" : "updateSfSchoolproductsetStatus",
		"args" : {
			"sfschoolproductsetId" : id,
			"sfschoolproductsetDecision" : "Accept"
		}
		};
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				MSG.alertMsgs("check", "商品上架已核可" , 0);				
				$(element).parent().parent().remove();
				listSfSchoolproductsetWaitApproveBySchool();
			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);				
			}
		} else{
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);			
		}
	} else {
		return;
	}
}

/*
 * 否決商品上架審核
 */
function rejectSfSchoolproductset(element, id) {
	var answer = confirm("請問是否要否決？");
	if (answer) {
		var request_data = {
				"method" : "updateSfSchoolproductsetStatus",
				"args" : {
					"sfschoolproductsetId" : id,
					"sfschoolproductsetDecision" : "Reject"
				}
		};
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				MSG.alertMsgs("check", "商品上架已否決" , 0);				
				$(element).parent().parent().remove();
				listSfSchoolproductsetWaitApproveBySchool();
			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);				
			}
		} else{
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);			
		}
	} else {
		return;
	}
}

