// 設定日期(開始)與日期(結束)的起始值
function initDate() {
	var currentTime = new Date();
	var year = currentTime.getFullYear();
	var month = currentTime.getMonth() + 1;
	var start = year + "/" + month + "/01";
	$("#start_date").val(start);
	var end = "";
	if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
		end = year + "/" + month + "/31";
		$("#end_date").val(end);
	} else if (month == 4 || month == 6 || month == 9 || month == 11) {
		end = year + "/" + month + "/30";
		$("#end_date").val(end);
	} else if (month == 2) {
		end = year + "/" + month + "/28";
		$("#end_date").val(end);
	}
}

// 檢核日期 : 開始日期不可大於結束日期
function chkDate(_start_date, _end_date) {		
	var _start = new Date(_start_date.substr(0, 4) + "-" + _start_date.substr(5, 2) + "-" + _start_date.substr(8, 2));
	var _end = new Date(_end_date.substr(0, 4) + "-" + _end_date.substr(5, 2) + "-"	+ _end_date.substr(8, 2));
	if(_start > _end){
		MSG.alertMsgs("check", "開始日期不可大於結束日期", 0);
		return;
	}	
}

// 查詢供餐廚房審核紀錄與商品上架審核紀錄
function queryApproveRecords() {
	MSG.alertMsgs("loading", "查詢中...", 0);
	var _approveItem = document.getElementById("approveItem").value;
	var _approveStatus = document.getElementById("approveStatus").value;
	var _start_date = document.getElementById("start_date").value;
    var _end_date = document.getElementById("end_date").value;
    // 檢核日期
	chkDate(_start_date, _end_date);
	if ("acceptschoolkitchen" == _approveItem) {
		// 只查詢供餐廚房審核紀錄(清空商品上架審核紀錄)
		$("#querysfschoolproductsetList").html("");
		$("#querysfschoolproductsetList").append("");
		$("#querysfschoolproductsetList").show();
		queryAcceptschoolkitchenRecords(_approveItem, _approveStatus, _start_date, _end_date);
	} else if ("sfschoolproductset" == _approveItem) {
		// 只查詢商品上架審核紀錄(清空供餐廚房審核紀錄及分隔區域)
		$("#sepreatespace").html("");
		$("#sepreatespace").append("");
		$("#sepreatespace").show();
		$("#queryacceptschoolkitchenList").html("");
		$("#queryacceptschoolkitchenList").append("");
		$("#queryacceptschoolkitchenList").show();
		querySfschoolproductsetRecords(_approveItem, _approveStatus, _start_date, _end_date);
	} else {
		// 查詢供餐廚房審核紀錄與商品上架審核紀錄
		queryAcceptschoolkitchenRecords(_approveItem, _approveStatus, _start_date, _end_date);
		var ask_div = "<br><br><br><br><br><br><br><br>";
		$("#sepreatespace").html("");
		$("#sepreatespace").append(ask_div);
		$("#sepreatespace").show();
		querySfschoolproductsetRecords(_approveItem, _approveStatus, _start_date, _end_date);
	}	
}

// 查詢供餐廚房審核紀錄
function queryAcceptschoolkitchenRecords(_approveItem, _approveStatus, _start_date, _end_date) {
	var request_data = {
		"method" : "queryAcceptSchoolKitchenBySchool",
		"args" : {
			"schoolId" : schoolId,
			"approveStatus" : _approveStatus,
			"startDate" : _start_date,
			"endDate" : _end_date
		}
	};
	
	var ask_div = "";
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1){
		var result_content = response_obj.result_content;
		listLength = result_content.acceptschoolkitchenList.length;

		ask_div += " <h5 class=\"section-head with-border\" style=\"height:35px; margin-bottom: 10px;\">";
		ask_div += " <div class=\"TITLE_TXT flo_l\">查詢結果</div>";
		ask_div += " <div class=\"TITLE_TXT flo_l\">資料筆數:" + listLength + "筆</div>";
		ask_div += " <div class=\"TITLE_TXT_BBT FL_R\">";
		if(listLength > 0){
			ask_div += " <a href=\"#\" onclick=\"export_acceptschoolkitchen_excel()\" style=\"float:right; margin: 0;\">匯出</a>";
		}		
		ask_div += " </div></h5>";

		ask_div += " <table class=\"table table-bordered table-striped\" width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  id=\"acceptschoolkitchenFields\">";
		ask_div += " <thead><tr>";
		ask_div += " <td class=\"text-center\">" + " 	序號</td>";
		ask_div += " <td class=\"text-center\">" + " 	學校名稱</td>";
		ask_div += " <td class=\"text-center\">" + " 	狀態</td>";
		ask_div += " <td class=\"text-center\">" + " 	動作</td>";
		ask_div += " <td class=\"text-center\">" + " 	申請日期</td>";
		ask_div += " <td class=\"text-center\">" + " 	最後更新者</td>";
		ask_div += " <td class=\"text-center\">" + " 	最後更新日期</td>";
		ask_div += " </tr></thead>";
		for (var j = 0; j < listLength; j++) {
			ask_div += " <tr valign=\"top\" align=\"left\">";
			ask_div += " <td width=\"6%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\" id=\"customFieldValue\" name=\"customFieldValue\" value=\"" + result_content.acceptschoolkitchenList[j].id + "\" placeholder=\"Input Name\" style=\"border: none;\"/>" + result_content.acceptschoolkitchenList[j].id + " </td>";
			ask_div += " <td width=\"26%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\"" + result_content.acceptschoolkitchenList[j].schoolName + "\" placeholder=\"Input Value\" style=\"border: none;\" />" + result_content.acceptschoolkitchenList[j].schoolName + "</td>";
			ask_div += " <td width=\"10%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.acceptschoolkitchenList[j].status + " </td>";
			ask_div += " <td width=\"10%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.acceptschoolkitchenList[j].action + " </td>";
			ask_div += " <td width=\"18%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.acceptschoolkitchenList[j].createDateTime + " </td>";
			ask_div += " <td width=\"12%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.acceptschoolkitchenList[j].modifyUser + " </td>";
			ask_div += " <td width=\"18%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.acceptschoolkitchenList[j].modifyDateTime + " </td>";
			ask_div += " </tr>";
		}								
		ask_div += " </td></tr></table>	";
		if (listLength == 0) {
			ask_div += "<h4 align=\"center\">無供餐廚房審核紀錄</h4>";
		}
		if ("acceptschoolkitchen" == _approveItem) {
			$.unblockUI();
		}						
	}
	$("#queryacceptschoolkitchenList").html("");
	$("#queryacceptschoolkitchenList").append(ask_div);
	$("#queryacceptschoolkitchenList").show();
}

// 查詢商品上架審核紀錄
function querySfschoolproductsetRecords(_approveItem, _approveStatus, _start_date, _end_date) {
	var request_data = {
		"method" : "querySfschoolproductsetBySchool",
		"args" : {
			"schoolId" : schoolId,
			"approveStatus" : _approveStatus,
			"startDate" : _start_date,
			"endDate" : _end_date
		}
	};
	
	var ask_div = "";
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1){
		var result_content = response_obj.result_content;
		listLength = result_content.sfschoolproductsetList.length;

		ask_div += " <h5 class=\"section-head with-border\" style=\"height:35px; margin-bottom: 10px;\">";
		ask_div += " <div class=\"TITLE_TXT flo_l\">查詢結果</div>";
		ask_div += " <div class=\"TITLE_TXT flo_l\">資料筆數:" + listLength + "筆</div>";
		ask_div += " <div class=\"TITLE_TXT_BBT FL_R\">";
		if(listLength > 0){
			ask_div += " <a href=\"#\" onclick=\"export_sfschoolproductset_excel()\" style=\"float:right; margin: 0;\">匯出</a>";
		}		
		ask_div += " </div></h5>";

		ask_div += " <table class=\"table table-bordered table-striped\" width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  id=\"sfschoolproductsetFields\">";
		ask_div += " <thead><tr>";
		ask_div += " <td class=\"text-center\">" + " 	序號</td>";
		ask_div += " <td class=\"text-center\">" + " 	產品名稱</td>";
		ask_div += " <td class=\"text-center\">" + " 	上架日</td>";
		ask_div += " <td class=\"text-center\">" + " 	下架日</td>";
		ask_div += " <td class=\"text-center\">" + " 	狀態</td>";
		ask_div += " <td class=\"text-center\">" + " 	申請日期</td>";
		ask_div += " <td class=\"text-center\">" + " 	最後更新者</td>";
		ask_div += " <td class=\"text-center\">" + " 	最後更新日期</td>";
		ask_div += " </tr></thead>";		
		for (var j = 0; j < listLength; j++) {
			ask_div += " <tr valign=\"top\" align=\"left\">";
			ask_div += " <td width=\"6%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.sfschoolproductsetList[j].id + " </td>";
			ask_div += " <td width=\"15%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.sfschoolproductsetList[j].productName + " </td>";
			ask_div += " <td width=\"12%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.sfschoolproductsetList[j].onShelfDate + " </td>";
			ask_div += " <td width=\"12%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.sfschoolproductsetList[j].offShelfDate + " </td>";
			ask_div += " <td width=\"9%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.sfschoolproductsetList[j].status + " </td>";
			ask_div += " <td width=\"17%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.sfschoolproductsetList[j].createDateTime + " </td>";
			ask_div += " <td width=\"12%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.sfschoolproductsetList[j].modifyUser + " </td>";
			ask_div += " <td width=\"17%\" ";
			if (j % 2 == 1) {
				ask_div += "class='componetContent componentContentLeftLine' >";
			} else if (j % 2 == 0) {
				ask_div += "class='componetContent2 componentContentLeftLine' >";
			}
			ask_div += " <input type=\"hidden\" readonly=\"readonly\"/>" + result_content.sfschoolproductsetList[j].modifyDateTime + " </td>";
			ask_div += " </tr>";
		}
		ask_div += " </td></tr></table>	";
		if (listLength == 0) {
			ask_div += "<h4 align=\"center\">無商品上架審核紀錄</h4>";
		}
		if ("acceptschoolkitchen" != _approveItem) {
			$.unblockUI();
		}
	}
	$("#querysfschoolproductsetList").html("");
	$("#querysfschoolproductsetList").append(ask_div);
	$("#querysfschoolproductsetList").show();	
}

// 匯出供餐廚房審核紀錄查詢結果
function export_acceptschoolkitchen_excel() {
	var _approveStatus = document.getElementById("approveStatus").value;
	var _start_date = document.getElementById("start_date").value;
	var _end_date = document.getElementById("end_date").value;
	// URL safe : replace '/' to '-'
	_start_date = _start_date.replace("/", "-");
	_start_date = _start_date.replace("/", "-");
	_end_date = _end_date.replace("/", "-");
	_end_date = _end_date.replace("/", "-");

	var link = "/cateringservice/rest/API/XLS/auditSchoolkitchen&" + schoolId + "&" + _approveStatus + "&" + _start_date + "&" + _end_date;
	window.open(link, "_blank");
}

// 匯出商品上架審核紀錄查詢結果
function export_sfschoolproductset_excel() {
	var _approveStatus = document.getElementById("approveStatus").value;
	var _start_date = document.getElementById("start_date").value;
	var _end_date = document.getElementById("end_date").value;
	// URL safe : replace '/' to '-'
	_start_date = _start_date.replace("/", "-");
	_start_date = _start_date.replace("/", "-");
	_end_date = _end_date.replace("/", "-");
	_end_date = _end_date.replace("/", "-");

	var link = "/cateringservice/rest/API/XLS/auditSchoolproductset&" + schoolId + "&" + _approveStatus + "&" + _start_date + "&" + _end_date;
	window.open(link, "_blank");
}

$(function(){
	gotop();	// 浮動按鈕	  
});
