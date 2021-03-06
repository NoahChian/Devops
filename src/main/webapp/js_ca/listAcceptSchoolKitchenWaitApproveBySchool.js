var MSG = new MsgsProcessing();
var util = new CateringServiceUtil();

// 初始化
$(document).ready(function() {
	listAcceptSchoolKitchenWaitApproveBySchool();
});

/*
 * 顯示供餐廚房審核頁面
 */
function listAcceptSchoolKitchenWaitApproveBySchool(){
	// 查詢是否需要進行審核
	var req_data = {
		"method" : "queryAcceptSwitch",
		"args" : {
			"schoolId" : schoolId,
			"acceptType" : "kitchenVerify"
		}
	};
	var resp_obj = call_rest_api(req_data);
	if (resp_obj.result == 1) {
		var result = resp_obj.result_content;
		listLength = result.acceptswitchList.length;
	}
	closeValue = "checked";
	openValue = "";
	if(listLength > 0){
		status = result.acceptswitchList[0].status;
		if("1" == status){
			closeValue = "";
			openValue = "checked";
		}
	}
	var tooltipMsg = "若勾選則當被團膳業者設定供餐時需要經過審核確認才可上傳菜單資訊";
	var ask_div = "";
		ask_div += "<div style=\"float:left\"><h4>供餐廚房審核</h4></div>";
		ask_div += "<div data-tooltip=\" "
			+ tooltipMsg
			+ "\" style=\"position:relative; bottom:0px; float:right\">供餐廚房審核<input type=\"radio\" onclick=\"openAcceptSwitch();\" name=\"approve\" value=\"open\" "
			+ openValue
			+ ">開啟<input type=\"radio\" onclick=\"closeAcceptSwitch();\" name=\"approve\" value=\"close\" "
			+ closeValue + ">關閉</div>";
		ask_div += " <table class=\"table table-bordered table-striped\" width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  id=\"askCustomFields\">";
		ask_div += " <thead><tr>";
		ask_div += "<td class=\"text-center\">"
		+ " 	序號</td>";
		ask_div += "<td class=\"text-center\">"
		+ " 	廚房名稱</td>";
		ask_div += "<td class=\"text-center\">"
		+ " 	供應人數(份數)</td>";	
		ask_div += "<td class=\"text-center\">"
		+ " 	連絡資訊</td>";
		ask_div += "<td class=\"text-center\">"
		+ " 	審核動作</td>";
		ask_div += " </tr></thead>";
	var request_data =	{
			 "method":"queryAcceptSchoolKitchenWaitApproveBySchool",
				"args":{	
					"kitchenId":kitchenId					
				}
			};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1){
		var result_content = response_obj.result_content;
		listLength = result_content.acceptschoolkitchenwaitapprovebyschooList.length;		
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
			ask_div += "<input type=\"hidden\" readonly=\"readonly\" id=\"customFieldValue\" name=\"customFieldValue\" value=\""+ result_content.acceptschoolkitchenwaitapprovebyschooList[j].id+"\" placeholder=\"Input Name\" style=\"border: none;\"/><center>"+ id +"</center></td>";
			ask_div += " <td width=\"20%\" ";
			if(	j%2 == 1){
				ask_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				ask_div +="class='componetContent2 componentContentLeftLine' >";
			}
			ask_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.acceptschoolkitchenwaitapprovebyschooList[j].kitchenName+"\" placeholder=\"Input Value\" style=\"border: none;\" /><center>"+ result_content.acceptschoolkitchenwaitapprovebyschooList[j].kitchenName+"</center></td>";
			ask_div += " <td width=\"14%\" ";
			if(j%2 == 1){
				ask_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
			}else if(j%2 == 0){
				ask_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
			}
			ask_div += '<input id="quantityAsk_'+ result_content.acceptschoolkitchenwaitapprovebyschooList[j].id+'" readonly=\"readonly\" type="text" class="form-control" style="width:50px;" value=" ';			
			ask_div += result_content.acceptschoolkitchenwaitapprovebyschooList[j].quantity +'" /></td>';
			ask_div += " <td width=\"26%\" ";
			if(	j%2 == 1){
				ask_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				ask_div +="class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += "<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.acceptschoolkitchenwaitapprovebyschooList[j].id+"\" placeholder=\"Input Value\" style=\"border: none;\" /><left>";
			ask_div += "負責人 : ";
			ask_div += result_content.acceptschoolkitchenwaitapprovebyschooList[j].kitchenOwnner + " <br> ";
			ask_div += "地址 : ";
			ask_div += result_content.acceptschoolkitchenwaitapprovebyschooList[j].kitchenAddress + " <br> ";
			ask_div += "電話 : ";
			ask_div += result_content.acceptschoolkitchenwaitapprovebyschooList[j].kitchenTel + "</center></td>";
			ask_div += " <td width=\"34%\" ";	
				if(j%2 == 1){
					ask_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
				}else if(j%2 == 0){
					ask_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
				}
				ask_div += "<button onclick=\"approveAcceptSchoolKitchen($(this)," + result_content.acceptschoolkitchenwaitapprovebyschooList[j].id + ")\" class=\"btn btn-primary\"><i class=\"fa fa-check\"></i>核可</button> ";
				ask_div += "<button onclick=\"rejectAcceptSchoolKitchen($(this)," + result_content.acceptschoolkitchenwaitapprovebyschooList[j].id + ")\" class=\"btn btn-primary\"><i class=\"fa fa-times-circle\"></i>否決</button> </td>";
			ask_div += " </tr>";
		}		
	}
	ask_div += " </td></tr></table>	";
	// 沒有待審核事項時, 要顯示無待審核資料的訊息
	if (listLength == 0) {
		ask_div += "<h4 align=\"center\">無待審核資料</h4>";
	}

	$("#acceptSchoolKitchenWaitApproveBySchoolList").html("");
	$("#acceptSchoolKitchenWaitApproveBySchoolList").append(ask_div);
	$("#acceptSchoolKitchenWaitApproveBySchoolList").show();
}

/*
 * 開啟供餐廚房審核
 */
function openAcceptSwitch() {
	if ("checked" == openValue) {
		MSG.alertMsgs("check", "供餐廚房審核已開啟,無需再開啟", 0);
		return;
	}
	var answer = confirm("請問是否要開啟供餐廚房審核？");
	var openStatus = 1; // Status 狀態 0:停用 ,1:啟用
	if (answer) {
		var request_data = {
			"method" : "updateAcceptSwitchStatus",
			"args" : {
				"schoolId" : schoolId,
				"acceptType" : "kitchenVerify",
				"status" : openStatus
			}
		};
		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { // success
				MSG.alertMsgs("check", "供餐廚房審核已開啟", 0);
				listAcceptSchoolKitchenWaitApproveBySchool();
			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
			}
		} else {
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg,
					0);
		}
	} else {
		listAcceptSchoolKitchenWaitApproveBySchool();
	}
}

/*
 * 關閉供餐廚房審核
 */
function closeAcceptSwitch() {
	if ("checked" == closeValue) {
		MSG.alertMsgs("check", "供餐廚房審核已關閉,無需再關閉", 0);
		return;
	}
	var answer = confirm("請問是否要關閉供餐廚房審核？");
	var closeStatus = 0;
	if (answer) {
		var request_data = {
			"method" : "updateAcceptSwitchStatus",
			"args" : {
				"schoolId" : schoolId,
				"acceptType" : "kitchenVerify",
				"status" : closeStatus
			}
		};
		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { // success
				MSG.alertMsgs("check", "供餐廚房審核已關閉", 0);
				listAcceptSchoolKitchenWaitApproveBySchool();
			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
			}
		} else {
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg,
					0);
		}
	} else {
		listAcceptSchoolKitchenWaitApproveBySchool();
	}
}

/*
 * 核可供餐廚房審核
 */
function approveAcceptSchoolKitchen(element, id) {
	var answer = confirm("請問是否要核可？");
	if (answer) {
		var request_data = {
		"method" : "updateAcceptschoolkitchenStatus",
		"args" : {
			"acceptschoolkitchenId" : id,
			"acceptschoolkitchenDecision" : "Accept"
		}
		};
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				MSG.alertMsgs("check", "供餐廚房已核可" , 0);				
				$(element).parent().parent().remove();
				listAcceptSchoolKitchenWaitApproveBySchool();
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
 * 否決供餐廚房審核
 */
function rejectAcceptSchoolKitchen(element, id) {
	var answer = confirm("請問是否要否決？");
	if (answer) {
		var request_data = {
				"method" : "updateAcceptschoolkitchenStatus",
				"args" : {
					"acceptschoolkitchenId" : id,
					"acceptschoolkitchenDecision" : "Reject"
				}
		};
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				MSG.alertMsgs("check", "供餐廚房已否決" , 0);				
				$(element).parent().parent().remove();
				listAcceptSchoolKitchenWaitApproveBySchool();
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

