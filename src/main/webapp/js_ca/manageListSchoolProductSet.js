/**
 * for manageListShoolSet page
 */
//--------- [公用變數] ---------
var MSG = new MsgsProcessing();
var allSchoolProduct = [];

/**
 * 重抓表格
 */
function reloaded(){
	allSchoolProduct.length = 0;
	$('table#resultTable > tbody > tr:eq(0)').each(function () {
        var prodId = $.trim($(this).find("td:eq(0)").text());
        var status = $.trim($(this).find("input").attr("checked"));
        allSchoolProduct.push({"id": prodId, "status": status=="checked"?1:0});
    });
	$('table#resultTable > tbody > tr:gt(0)').each(function () {
        var prodId = $.trim($(this).find("td:eq(0)").text()); //1是抓取第二欄的文字
        var status = $.trim($(this).find("input").attr("checked"));
        allSchoolProduct.push({"id": prodId, "status": status=="checked"?1:0});
    });
    //console.log("[初始化]取得allSchoolProduct.length=="+allSchoolProduct.length);
};

$(document).ready(function(){
	//MSG.alertMsgs("loading", "資料處理中，請稍後...", 0);
	var displayRow = $("#displayRow").val()===undefined? row : $("#displayRow").val();
	var response_obj = redirectNodeJs();
	$('#productName').focus();
	
	// 取得目前全部產品的勾選狀態
	reloaded();
    //console.log("[初始化]取得的allSchoolProduct:"+JSON.stringify(allSchoolProduct));
});

/**
 * 重新指定導向，導至NodeJs平台。
 */
function redirectNodeJs() {
	//MSG.alertMsgs("loading", "資料處理中，請稍後...", 0);
	var productName = $("#productName").val();
	var displayRow = $("#displayRow").val()===undefined? row : $("#displayRow").val();
	var displayCrnPage = $("#displayCrnPage").val()===undefined? crnPage : $("#displayCrnPage").val();
	var sid = $("#sid").val();
	var request_data = {};
    var status = $("#ProductState").val();
	
	request_data =	{
			 "method":"querySfProductList",
				"args":{
				      "sid": sid,
				      "pageNum": displayCrnPage,
				      "pageLimit": displayRow,
				      "companyName":"",
				      "name":productName,
				      "category":"",
				      "barCode":"",
				      "status":status
				}
			};
	//console.log("input JSON:\n"+JSON.stringify(request_data));

	// 目前使用方法三
	var apUrl = "/SchoolProductSet/";
	console.log(url+apUrl);
	console.log(request_data);
	console.log("------------");
	
	var response_obj = new Object();
	ajaxCallJsonp(url+apUrl, request_data, function(datas) {
		var data = datas.data;

		if( data !== undefined ) {
			if(data.resStatus === 1){
				var th = ["編號", "產品名稱", "供應商",
							"製造商", "包裝型態" + "<br>" + "保存方式" + "<br>" + "販售方式",
							"認證類別" + "<br>" + "認證編號",
							"上架日", "下架日",
							"是否上架<input type='checkbox' id='selectAll' onclick='clkSelectAll()'>"];
				
				changePage(data, PAGE_TABLE, th);
				//var total=response_obj["result_content"]["totalNum"];
				//var list=response_obj["result_content"]["productList"];
				//changePage(response_obj["result_content"], crnPage, PAGE_TABLE, th);

				//util.setTableWithHeader(list,th,PAGE_TABLE);
				//util.setTableTail(PAGE_TABLE,"總數",total);

			} else {
				//20150526 shine mod 修改顯示訊息的方法,原先方式因不存在blockUIMsgs,所以不會顯示出來
				MSG.alertMsgs("check", "查無資料！", 0);
//				$(".blockUIImgs").hide();
//				$(".blockUIMsgs").html("查無任何資料！");
//				$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
			}
			reloaded();
		}
		
		response_obj = data;
	});
	console.log("getData:");
	console.log(response_obj);
	return response_obj;
}

//-------------------[換頁功能]-------------------
/**
 * 組『總筆數、目前頁碼、換頁下拉』及『顯示該畫面之各筆資料』等功能列
 * result_content : 取得的 JSON 內容
 * crnPage : 當前要顯示之頁數
 */
function changePage(result_content, tableName, header) {
	//MSG.alertMsgs("loading", "資料處理中，請稍後...", 0);
	var displayRow = $("#displayRow").val()===undefined? row : $("#displayRow").val();
	var displayCrnPage = $("#displayCrnPage").val()===undefined? crnPage : $("#displayCrnPage").val();
	//console.log("每頁顯示的列數:"+displayRow+"筆");

	//清單內容
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<thead><tr>";
	for ( var item in header) {
		html += "<th class=\"text-center\" >" + header[item] + "</th>";
	}
	html += "</tr></thead>";

	// 每頁顯示設定的筆數，如果該頁資料不足時，則顯示取得的數量之筆數。
	var rDisplayRow = displayRow==result_content.productList.length ? displayRow : result_content.productList.length;
	for(var i=0; i<rDisplayRow; i++) {

		// 判斷是否有打勾
		var checkStatus;
		if( (result_content.productList[i].onShelfDate=="" && result_content.productList[i].offShelfDate=="") || 
			(result_content.productList[i].onShelfDate!="" && result_content.productList[i].offShelfDate!="")) {
			checkStatus = "";
		} else {
			checkStatus = "checked";
		}
		var clsStyle = i%2==0 ? "componetContent" : "componetContent2";
		var prodId = result_content.productList[i].id;

		var strTR = "<tr>";
		strTR += "<td class=\"text-left\" width=\"3%\">" + result_content.productList[i].id  + "</td>";
		strTR += "<td class=\"text-left\" width=\"12%\">" + result_content.productList[i].name  + "</td>";
		strTR += "<td class=\"text-left\" width=\"19%\">" + result_content.productList[i].supplierCompanyName  + "</td>";
		strTR += "<td class=\"text-left\" width=\"19%\">" + result_content.productList[i].manufacturerName  + "</td>";
		strTR += "<td class=\"text-left\" width=\"14%\">";
		strTR += result_content.productList[i].packages + '<br>';
		strTR += result_content.productList[i].preservedMethod + '<br>';
		strTR += result_content.productList[i].soldway + "</td>";
		strTR += "<td class=\"text-left\" width=\"11%\">";
		strTR += result_content.productList[i].certification + '<br>';
		strTR += result_content.productList[i].certificationId + "</td>";
		// 上下架欄位內日期時間只要顯示到日即可, 不用時分秒
		var tmpOnShelfDate = result_content.productList[i].onShelfDate;
		var tmpDate = tmpOnShelfDate.split(" ");
		var onShelfDate = tmpDate[0];
		var tmpOffShelfDate = result_content.productList[i].offShelfDate;
		tmpDate = tmpOffShelfDate.split(" ");
		var offShelfDate = tmpDate[0];
		// 勾選上架的商品,存入sf_schoolproductset時, status欄位值為0,表示待審核, 同時上架日欄位則顯示”待審核”字樣
		var productsetStatus = result_content.productList[i].status;
		if(productsetStatus == "0"){
			onShelfDate = "待審核";
		} else if(productsetStatus == "2"){
			onShelfDate = "否決";
			checkStatus = "";
		}
		strTR += "<td class=\"text-left\" width=\"9%\">" + onShelfDate  + "</td>";
		strTR += "<td class=\"text-left\" width=\"9%\">" + offShelfDate  + "</td>";

		strTR += "<td class=\"text-left\" width=\"4%\"><input type='checkbox' id='productStatus_"+i+"' name='productStatus[]' "+checkStatus+" /></td>";
		strTR += "</tr>";
		html += strTR;
	}
	$("#" + tableName).html(html);
	$("#" + tableName).hide();
	$("#" + tableName).show("slow");

	// 資料筆數
	$("#query_rule").html("");
	$("#query_rule").html("資料筆數:" +result_content.totalNum );
	// 頁數管理
	var pageHtml = "";
	pageHtml  += "第  "+ displayCrnPage +" 頁 | ";
	// 總共頁數
	var totalPage  = 0; //分頁若整除，不需+1
	if(result_content.totalNum % displayRow == 0) {
		totalPage = parseInt(result_content.totalNum / displayRow ,10);
	} else {
		totalPage = parseInt(result_content.totalNum / displayRow ,10)+1;
	}
	pageHtml  += "共 "+ totalPage +" 頁 | ";
	// 頁面計算
	pageHtml += '前往第<select id="displayCrnPage" style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="redirectNodeJs()"> |';
		for(var j = 1; j<= totalPage ; j++) {
			if(j ==displayCrnPage) {
				pageHtml += '<option selected value="'+ j +'">'+ j +'</option>';
			} else {
				pageHtml += '<option value="'+ j +'">'+ j +'</option>';
			}
		}
	pageHtml += '</select>頁';

	// 每頁顯示n筆資料，目前有問題，先不作用。
	pageHtml += '　　每頁顯示<select id="displayRow" style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="redirectNodeJs()">';
	for(var j = 0; j<pageRow.length ; j++) {
		if(pageRow[j]==displayRow) {
			pageHtml += '<option selected value="'+ pageRow[j] +'">'+ pageRow[j] +'</option>';
			row = pageRow[j];
		} else {
			pageHtml += '<option value="'+ pageRow[j] +'">'+ pageRow[j] +'</option>';
			row = pageRow[j];
		}
	}
	pageHtml += '</select>筆資料';
	
	var strSubmitBtnModify = "<a href='#' style='margin:0' onclick='modifySchoolProductSet()'>變更上架商品</a>";
	
	$("#page").html("");
	$("#page").html(pageHtml);

	$("#displaySubmitBtnModify").html("");
	$("#displaySubmitBtnModify").html(strSubmitBtnModify);
	//$.unblockUI();
	reloaded();
}

//檢視商品上架申請記錄
function qryHalfYearSfSchoolproductset() {
	var sid = $("#sid").val();
	var request_data = {
		"method" : "queryHalfYearSfSchoolproductset",
		"args" : {
			"schoolId" : sid
		}
	};

	var halfYearAsk_div = "";
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1){
		var result_content = response_obj.result_content;
		listLength = result_content.halfyearsfschoolproductsetList.length;

		halfYearAsk_div += " <table class=\"table table-bordered table-striped\" width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  id=\"halfYearAskCustomFields\">";
		halfYearAsk_div += " <thead><tr>";
		halfYearAsk_div += " <td class=\"text-center\">"
			+ " 	序號</td>";
		halfYearAsk_div += " <td class=\"text-center\">"
			+ " 	產品名稱</td>";
		halfYearAsk_div += "<td class=\"text-center\">"
			+ " 	上架日</td>";
		halfYearAsk_div += "<td class=\"text-center\">"
			+ " 	下架日</td>";
		halfYearAsk_div += "<td class=\"text-center\">"
			+ " 	狀態</td>";
		halfYearAsk_div += "<td class=\"text-center\">"
			+ " 	申請日期</td>";
		halfYearAsk_div += " <td class=\"text-center\">"
			+ " 	最後更新者</td>";
		halfYearAsk_div += "<td class=\"text-center\">"
			+ " 	最後更新日期</td>";
		halfYearAsk_div += " </tr></thead>";
		for(var j=0;j<listLength;j++){
			halfYearAsk_div += " <tr valign=\"top\" align=\"left\">";
			halfYearAsk_div += " <td width=\"6%\" ";
			if(j%2 == 1){
				halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
			}
			halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearsfschoolproductsetList[j].id+" </td>";
			halfYearAsk_div += " <td width=\"15%\" ";
			if(	j%2 == 1){
				halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
			}
			halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearsfschoolproductsetList[j].productName+" </td>";
			halfYearAsk_div += " <td width=\"12%\" ";
			if(j%2 == 1){
				halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
			}
			halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearsfschoolproductsetList[j].onShelfDate+" </td>";
			halfYearAsk_div += " <td width=\"12%\" ";
			if(j%2 == 1){
				halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
			}
			halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearsfschoolproductsetList[j].offShelfDate+" </td>";
			halfYearAsk_div += " <td width=\"9%\" ";
			if(j%2 == 1){
				halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
			}
			halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearsfschoolproductsetList[j].status+" </td>";

			halfYearAsk_div += " <td width=\"17%\" ";
			if(j%2 == 1){
				halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
			}
			halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearsfschoolproductsetList[j].createDateTime+" </td>";
			halfYearAsk_div += " <td width=\"12%\" ";
			if(j%2 == 1){
				halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
			}
			halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearsfschoolproductsetList[j].modifyUser+" </td>";
			halfYearAsk_div += " <td width=\"17%\" ";
			if(j%2 == 1){
				halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
			}else if(j%2 == 0){
				halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
			}
			halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearsfschoolproductsetList[j].modifyDateTime+" </td>";

			halfYearAsk_div += " </tr>";
		}
		halfYearAsk_div += " </td></tr></table>	";
	}
	$("#halfYearAskList").html("");
	$("#halfYearAskList").append(halfYearAsk_div);
	$("#halfYearAskList").show();
	$("#sfSchoolproductsetModal").modal();
}

/**
 * 變更［員生消費合作社-商品清單］
 */
function modifySchoolProductSet() {
	//MSG.alertMsgs("loading", "變更［員生消費合作社-商品清單］，請稍後...", 0);
	var up=[];	// 上架的商品
	var dn=[];	// 下架的商品
	var a=$('#resultTable > tbody > tr').length;

	for(var i =0; i<a;i++){		
		if(($('#productStatus_'+i).prop('checked')==true)&&(allSchoolProduct[i].status==0)){			
			up.push({"id": allSchoolProduct[i].id, "status": 1});			
		};
		if(($('#productStatus_'+i).prop('checked')==false)&&(allSchoolProduct[i].status==1)){			
			dn.push({"id": allSchoolProduct[i].id, "status": 0});			
		};
	};
	var jsonData = up.concat(dn);
	//console.log("實際要更新的資料:"+JSON.stringify(jsonData));
	if(jsonData.length==0) {
		MSG.alertMsgs("check", "無任何上下架商品資料被變更！", 0);
		return 0;
	}
	if(jsonData.length>0) {
		//20150720 shine mod 將重整動作改到API完成再去執行
		updateSchoolProductSet(jsonData,function(){
			// 重新載入畫面
			var response_obj = redirectNodeJs();
			reloaded();			
		});
	}
	//------+++ 重新載入畫面 不能放在這裡
	//var response_obj = redirectNodeJs();
	//reloaded();
	//+++------
}

/**
 * 實際更新 schoolProductSet table
 */
function updateSchoolProductSet(jsonData,callback) {
	var sid = $("#sid").val();
	var userName = $("#_uName").val();
	var request_data = {};
	request_data = {
			"method":"updateSfProductList",
				"args":{
				      "sid": sid,
				      "userName": userName,
					  "productList": jsonData
			   }
			};

	var apUrl = "/SchoolProductSet/";
	
	var response_obj = new Object();
	ajaxCallJsonp(url+apUrl, request_data, function(datas) {
		var data = datas.data;		
		if (data.resStatus == "1"){
			var response_obj = redirectNodeJs();
			MSG.alertMsgs("check", "校園販售商品更新成功！<br/>共上下架 "+jsonData.length+" 筆商品資料", 0);			
		}else{
			MSG.alertMsgs("check", "校園販售商品更新失敗！", 0);
		}
		
		response_obj = data;

		//20150720 shine add 將重整動作改到API完成再去執行
		if(typeof(callback) == 'function')
			callback();
	});
	
	return response_obj;
}

/**
 * 全選 / 取消全選
 */
function clkSelectAll() {
	if($("#selectAll").prop("checked"))
	{
		$("input[name='productStatus[]']").each(function() {
			$(this).prop("checked", true);
		});
	} else {
		$("input[name='productStatus[]']").each(function() {
			$(this).prop("checked", false);
		});          
	}
}

/**
 * 當使用者輸入字時將頁碼改變回1
 */
function changeSelPage() {
	$("#displayCrnPage > option").remove();
	$("#displayCrnPage").append('<option value="1">1</option>');
	if( $("#displayCrnPage").val()>1 ) {
		$("#displayCrnPage").val("1");
	}
}

$(function(){
	gotop();	// 浮動按鈕
	
    // 按下 Enter 鍵
    $('#productName').keyup(function(e) {
    	if(e.keyCode == '13'){
    		redirectNodeJs();
    	};
    });
});
