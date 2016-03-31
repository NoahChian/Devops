var MSG = new MsgsProcessing();
var util = new CateringServiceUtil();
var PAGE_TABLE = "resultTable";
var dateRangeType = "7days";
$(document).ready(function() {
	defaultYearMonthRange(dateRangeType);
	$("#end_date").click(function(){
		$("#ui-datepicker-div").css("display", "none");	
	});
	if($.session.get("queryIngredientPropertyList_Date")){
		$("#end_date").val($.session.get("queryIngredientPropertyList_Date"));
		queryList();
	}
});
// 執行查詢
function queryList() {
	// var startDate = $("#start_date").val();
	var date = $("#end_date").val();

	var request_data = {
		"method" : "queryIngredientPropertyList",
		"args" : {
			"menuDate" : date
		}
	};
	var response_obj = call_rest_api(request_data);

	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == "1") { // success
			if (result_content.ingredientPropertyList.length == 0) {
				MSG.alertMsgs("check", "本日無食材資料。 " + result_content.msg, 0);
				return;
			} else {
				$.session.set("queryIngredientPropertyList_Date",date);
				//20140721 Ric Temp Hide
				ingredientProperty(result_content.ingredientPropertyList, ["菜色名稱","食材", "供應商名稱", "進貨日期", "批號", "屬性設定", "檢驗報告" ],	PAGE_TABLE);
				//20140721 Ric Temp Show
				//ingredientProperty(result_content.ingredientPropertyList, ["菜色名稱", "食材", "供應商名稱", "進貨日期", "批號","檢驗報告" ],	PAGE_TABLE);
			}
		} else {
			MSG.alertMsgs("check", "查詢結果提示：" + result_content.msg, 0);
			return;
		}
	} else {
		MSG.alertMsgs("check", "連線錯誤：" + response_obj.error_msg, 0);
		return;
	}
}

/*
 * 20140708 Ric 菜色清單表格產生
 */
function ingredientProperty(jsonData, header, tableName) {
	$("#" + tableName).empty();
	var menudate = $("#end_date").val();
	var html = "";
	html += "<thead><tr>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr></thead>";
	for (var i = 0; i < jsonData.length; i++) {
		var row = jsonData[i];
		var strTR = "";
		var check_gmbean = "";
		var check_gmcorn = "";
		var check_psfood = "";
		if (row.ingredientAttribute.gmbean == "1") {check_gmbean = "checked";}
		if (row.ingredientAttribute.gmcorn == "1") {check_gmcorn = "checked";}
		if (row.ingredientAttribute.psfood == "1") {check_psfood = "checked";}
		if (row.ingredientId != null && row.ingredientId != "") {
			strTR += "<tr id='ingredient" + i + "'>";
			strTR += "<td class='i_dishName'>" + row.dishName + "</td>";
			strTR += "<td class='i_ingredientName'>"
					//20140721 Ric Temp Show Start 
					+ "<input type= 'hidden' id='i_gmbean' value = '"+ row.ingredientAttribute.gmbean + "' />"
					+ "<input type= 'hidden' id='i_gmcorn' value = '"+ row.ingredientAttribute.gmcorn + "' />"
					+ "<input type= 'hidden' id='i_psfood' value = '"+ row.ingredientAttribute.psfood + "' />"
					//20140721 Ric Temp Show End */
					+ "<input type= 'hidden' class='i_ingredientid' value = '"
					+ row.ingredientId + "' />" + row.ingredientName + "</td>";
			strTR += "<td><input type= 'hidden' class='i_supplierid' value = '" + row.supplierId+ "' />"
					+ "<input type= 'hidden' class='i_supplierCompanyid' value = '"	+ row.supplierCompanyId + "' />" + row.supplierName	+ "</td>";
			strTR += "<td class='i_stockDate'>" + row.stockDate + "</td>";
			strTR += "<td class='i_lotNumber'>" + row.lotNumber + "</td>";
			//20140721 Ric Temp Hide
			
			 strTR += "<td>"
					+ "<input type='checkbox' class = 'i_gmbean' id='i_gmbean" + i+ "'value='"+ row.ingredientAttribute.gmbean+ "' "+ check_gmbean+ ">"
					+ "<label for='i_gmbean"+ i+ "'>基改黃豆</label>"
					+ "<input type='checkbox' class = 'i_gmcorn' id='i_gmcorn"+ i+ "'value='"+ row.ingredientAttribute.gmcorn+ "' "+ check_gmcorn+ ">"
					+ "<label for='i_gmcorn"+ i+ "'>基改玉米</label>"
					+ "<input type='checkbox' class = 'i_psfood' id='i_psfood"+ i+ "'value='"+ row.ingredientAttribute.psfood+ "' "+ check_psfood+ ">"
					+ "<label for='i_psfood"+ i+ "'>加工品</label>" + "</td>";
			
			strTR += "<td><input type= 'file' id='file"+ i+ "' name='file"+ i+ "' />"
					+ "<br><span class='btn btn-primary' style='margin: 0;'>"
					+ "<a style='color: #fff !important' href='javascript:uploadOneFile(\""+ menudate + "\",\"" + row.ingredientId + "\",\""+ row.supplierCompanyId + "\",\"" + row.lotNumber + "\",\""+ row.stockDate + "\",\"" + i + "\",\"ingredient\");'>上傳檔案</a></span>";
			if (row.inspectionFile.downloadPath) {
				strTR += "<span class='btn btn-primary' style='margin: 0;'>"
						+ "<a style='color: #fff !important' href='"+ row.inspectionFile.downloadPath+ "' target='_blank'>下載檔案</a></span>" 
						+ "<span class='btn btn-primary' style='margin: 0;'>"
						+ "<a style='color: #fff !important' href='javascript:deleteFile(\""+ menudate + "\",\"" + row.ingredientId + "\",\""+ row.supplierCompanyId + "\",\"" + row.lotNumber + "\",\""+ row.stockDate + "\");'>刪除</a></span>";
			}
			strTR += "</td>";
			strTR += "</tr>";
		}
		html += strTR;
	}
	html += "<tr>";
	html += "<td colspan='7' align='center'>"
			+ "<input type= 'hidden' id='menu_date' value = '"+ menudate+ "' />"
			//20140721 Ric Temp Hide
			+ " <span><input class='btn btn-primary' style='margin: 0' type='button' value='儲存本日所有食材' onclick='saveProperty(\""+ tableName+ "\",\"ingredient\")' /></span> "
			+ " <span><input class='btn btn-primary' style='margin: 0' type='button' value='上傳已選取之檔案' onclick='uploadAllFile(\""+ tableName + "\",\"ingredient\")' /></span> " 
			+ "</td/>";
	html += "</tr>";
	$("#" + tableName).html(html);
	totalTR = document.getElementById(tableName).getElementsByTagName('tr').length - 2;// 提供全部照片所計算的欄位數
																						// //-2去首尾
	// 設定check box內容值
	$('input[type="checkbox"]').change(function() {
		this.value = this.checked ? 1 : 0;
	});
};
/*
 * 20140625 Ric 上傳所有已選之菜單
 */
var totalTR;// 照片Table中tr數為全域，一讀入即取得(於產生照片清單Table時)，避免找不到Id
function saveProperty(tableid, para) { // 搜尋Table目標，擷取的參數
	var fileNum = 0; // 有檔案的數量
	var runSave = new Object;
	runSave.ingredientName = []; // 菜單日期
	runSave.menuDate = []; // 菜單日期
	runSave.ingredientId = []; // 食材ID
	runSave.supplierCompanyid = []; // 供應商ID
	runSave.stockDate = []; // 食材進貨日期
	runSave.lotNumber = []; // 食材批號
	runSave.gmbean = []; // 基因改造大豆
	runSave.gmcorn = []; // 基因改造玉米
	runSave.psfood = []; // 加工食品
	for (var i = 0; i < totalTR; i++) {
		runSave.ingredientName[fileNum] = $("#" + para + i + " .i_ingredientName").text();
		runSave.menuDate[fileNum] = $("#menu_date").val();
		runSave.ingredientId[fileNum] = $("#" + para + i + " .i_ingredientid").val();
		runSave.supplierCompanyid[fileNum] = $("#" + para + i + " .i_supplierCompanyid").val();
		runSave.stockDate[fileNum] = $("#" + para + i + " .i_stockDate").text();
		runSave.lotNumber[fileNum] = $("#" + para + i + " .i_lotNumber").text();
		runSave.gmbean[fileNum] = $("#" + para + i + " .i_gmbean").val();
		runSave.gmcorn[fileNum] = $("#" + para + i + " .i_gmcorn").val();
		runSave.psfood[fileNum] = $("#" + para + i + " .i_psfood").val();
		fileNum++;
	}
	savePropertyProcessing(runSave);
}
function savePropertyProcessing(runSave) {
	MSG.alertMsgs("loading", "資料更新中...", 0);
	var uploadMsgs = "";
	for (var i = 0; i < runSave.menuDate.length; i++) {
		var request_data = {
			"method" : "updateIngredientProperty",
			"args" : {
				"menuDate" : runSave.menuDate[i],
				"ingredientId" : runSave.ingredientId[i],
				"supplierCompanyId" : runSave.supplierCompanyid[i],
				"stockDate" : runSave.stockDate[i],
				"lotNumber" : runSave.lotNumber[i],
				"ingredientProperty" : {
					"gmbean" : runSave.gmbean[i],
					"gmcorn" : runSave.gmcorn[i],
					"psfood" : runSave.psfood[i]
				}
			}
		};
		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { // success
				//20140721 Ric 如果正確就不顯示
				uploadMsgs += runSave.ingredientName[i] + " 已更新。<br>";
			} else {
				uploadMsgs += runSave.ingredientName[i] + " 資料發生錯誤:"
						+ result_content.msg + "。<br>";
			}
		} else {
			// uploadMsgs+= runFile.ingredientName[i]+" 連線發生無法預期的錯誤!" +
			// result_content.msg + "。<br>";
			uploadMsgs += runSave.ingredientName[i] + " 連線發生錯誤!<br>";
		}
	}
	if(uploadMsgs==""){
		uploadMsgs += "已更新本日食材! ";
	}
	$(".blockUIImgs").hide();
	$(".blockUIMsgs").html(uploadMsgs);
//	$(".blockUIMsgs").html("檢查檔案上傳中...");
//	uploadAllFile( PAGE_TABLE ,"ingredient");
	$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();location.reload();" value="確定">');
}

/*
 * 2014/06/19 Ric 上傳菜色照片
 */
var client = new XMLHttpRequest();
var tagname = "";
function upload(menuDate, ingredientId, supplierCompanyId, lotNumber,
		stockDate, filetag, ingredientCategory) {
	tagname = "file" + filetag;
	var file = document.getElementById(tagname);
	if (!$("#" + tagname).val()) {
		MSG.alertMsgs('check', '請選擇檔案', 0);
		return false;
	}
	/* Create a FormData instance */
	var formData = new FormData();
	/* Add the file */
	formData.append("file", file.files[0]);
	formData.append("func", "inspect_v2|" + menuDate + "|" + ingredientId + "|"
			+ supplierCompanyId + "|" + lotNumber + "|" + stockDate  + "|" + ingredientCategory);
	formData.append("overWrite", "0");
	client.open("post", "/cateringservice/file/upload", false);
	client.send(formData); /* Send to server */
	// } 20140625 Ric改同步處理
	/* Check the response status */
	// client.onreadystatechange = function()
	// {
	if (client.readyState == 4 && client.status == 200) {
		var obj = JSON.parse(client.responseText);
		if (obj.retStatus == "1") {
			// MSG.alertMsgs("check", "檔案上傳成功", 0);
			// $("#pictr"+filetag).remove();
			// tr set id
			tagname = "";
			return ("檔案上傳成功");
		} else {
			// MSG.alertMsgs("check", "檔案上傳失敗，原因為" + obj.retMsg, 0);
			return ("檔案上傳失敗，" + obj.retMsg);
		}
	}
}
function uploadAllFile(tableid, para) { // 搜尋Table目標，擷取的參數
	var fileNum = 0; // 有檔案的數量
	var runFile = new Object;
	runFile.file = [];
	runFile.ingredientName = []; // 菜單日期
	runFile.menuDate = []; // 菜單日期
	runFile.ingredientId = []; // 食材ID
	runFile.supplierCompanyid = []; // 供應商統一編號
	runFile.stockDate = []; // 食材進貨日期
	runFile.lotNumber = []; // 食材批號
	runFile.ingredientCategory = []; //食材或調味料
	for (var i = 0; i < totalTR; i++) {
		if ($("#file" + i) != null) { // 取得file選取檔案的element
			if ($("#file" + i)[0].files[0] != null) {
				runFile.file[fileNum] = i;
				runFile.ingredientName[fileNum] = $("#" + para + i + " .i_ingredientName").text();
				runFile.menuDate[fileNum] = $("#menu_date").val();
				runFile.ingredientId[fileNum] = $("#" + para + i + " .i_ingredientid").val();
				runFile.supplierCompanyid[fileNum] = $("#" + para + i + " .i_supplierCompanyid").val();
				runFile.ingredientCategory[fileNum] = $("#" + para + i +" .i_ingredientCategory").val();
				//20140918 Raymond 目前調味料進貨日期可為空值 預防進貨日期為空查不到資料的狀況 若為此情況前端先給空白值
				if($("#" + para + i + " .i_stockDate").text() != null && $("#" + para + i + " .i_stockDate").text() != "")
					runFile.stockDate[fileNum] = $("#" + para + i + " .i_stockDate").text();
				else
					runFile.stockDate[fileNum] = " ";
				runFile.lotNumber[fileNum] = $("#" + para + i + " .i_lotNumber").text();
				fileNum++;
			}
		}
	}
	console.log(runFile)
	if (runFile.file.length == 0) {
		MSG.alertMsgs("check", "沒有選擇檔案", 0);
	} else {
		uploadFileProcessing(runFile);
	}
}
function uploadOneFile(menuDate, ingredientId, supplierCompanyId, lotNumber,
		stockDate, filetag, ingredientCategory) {
	tagname = "file" + filetag;
	var runFile = new Object;
	runFile.file = [];
	runFile.ingredientName = []; // 菜單日期
	runFile.menuDate = []; // 菜單日期
	runFile.ingredientId = []; // 食材ID
	runFile.supplierCompanyid = []; // 供應商統一編號
	runFile.stockDate = []; // 食材進貨日期
	runFile.lotNumber = []; // 食材批號
	runFile.ingredientCategory = []; //食材或調味料
	if ($("#" + tagname)[0].files[0] != null) {
		runFile.file[0] = filetag;
		runFile.ingredientName[0] = $("#ingredient" + filetag + " .i_ingredientName").text();
		runFile.menuDate[0] = menuDate;
		runFile.ingredientId[0] = ingredientId;
		runFile.supplierCompanyid[0] = supplierCompanyId;
		runFile.stockDate[0] = stockDate;
		runFile.lotNumber[0] = lotNumber;
		runFile.ingredientCategory[0] = ingredientCategory;
		uploadFileProcessing(runFile);
	} else {
		MSG.alertMsgs("check", "沒有選擇檔案", 0);
	}
}
function uploadFileProcessing(runFile) {
	MSG.alertMsgs("loading", "檔案檢查與上傳...", 0);
	var uploadMsgs = "";
	for (var i = 0; i < runFile.file.length; i++) {
		uploadMsgs += runFile.ingredientName[i]+ ":"+ upload(runFile.menuDate[i], 
						runFile.ingredientId[i],
						runFile.supplierCompanyid[i], 
						runFile.lotNumber[i],
						runFile.stockDate[i], 
						runFile.file[i], 
						runFile.ingredientCategory[i]) + "<br>";
	}
	$(".blockUIImgs").hide();
	$(".blockUIMsgs").html(uploadMsgs);
	$(".blockUIMsgs")
			.after(
					'<input type="button" onclick="$.unblockUI();location.reload();" value="確定">');
	// MSG.alertMsgs("check", uploadMsgs, 0);
	$("#uploadAllPic").val("上傳已選取之檔案");
}

function deleteFile(menuDate, ingredientId, supplierCompanyId, lotNumber, stockDate) {
	var request_data = {
		"method" : "deleteIngredientInspectionFile",
		"args" : {
			"menuDate" : menuDate,
			"ingredientId" : ingredientId,
			"supplierCompanyId" : supplierCompanyId,
			"stockDate" : stockDate,
			"lotNumber" : lotNumber
		}
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) {
		var result_content = response_obj.result_content;
		if (result_content.resStatus == "1") { // success
			MSG.alertMsgs("checkAndReload", "檔案刪除成功 " + result_content.msg, 0);
			return;
		} else {
			MSG.alertMsgs("check", "檔案處理異常：" + result_content.msg, 0);
			return;
		}
	} else {
		MSG.alertMsgs("check", "連線錯誤：" + response_obj.error_msg, 0);
		return;
	}
}