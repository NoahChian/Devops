/**
 * for customerQuerySF page
 */

$(document).ready(function(){	
	getCustomerQuerySF();
});

//執行查詢，查詢該學校有賣的商品(Sf_SchoolProductSet)。
function getCustomerQuerySF(){
	var displayRow = $("#displayRow").val()===undefined? row : $("#displayRow").val();
	var displayCrnPage = $("#displayCrnPage").val()===undefined? crnPage : $("#displayCrnPage").val();
	var sid = $("#sid").val();

	var request_data =	{
			   "method":"customerQuerySF",
			   "args":{
			      "pageNum": displayCrnPage,
			      "pageLimit": displayRow,
			      "sid": sid
			   }
			};
	//var response_obj = call_rest_api(request_data);
	//if (response_obj["result_content"].resStatus == "1"){
	//	changePage(response_obj["result_content"], PAGE_TABLE);
	//}else{
	//	alert("查詢無資料");
	//}
	var apUrl = "/SchoolProductSet/";

	var response_obj = ajaxCallJsonp(url+apUrl, request_data, function(datas) {
		var data = datas.data;
		if( data !== undefined ) {
			if(data.resStatus === 1) {
				changePage(data, PAGE_TABLE);
			} else {
				alert("查詢無資料");
			}
		}
	});
}

//-------------------[換頁功能]-------------------
/**
 * 組『總筆數、目前頁碼、換頁下拉』及『顯示該畫面之各筆資料』等功能列
 * result_content : 取得的 JSON 內容
 * crnPage : 當前要顯示之頁數
 */
function changePage(result_content, tableName) {
	var displayRow = $("#displayRow").val()===undefined? row : $("#displayRow").val();
	var displayCrnPage = $("#displayCrnPage").val()===undefined? crnPage : $("#displayCrnPage").val();

	//清單內容
	$("#" + tableName).empty();
	var html = '';
	var imagePath = "";

	// 每頁顯示設定的筆數，如果該頁資料不足時，則顯示取得的數量之筆數。
	var rDisplayRow = displayRow==result_content.productList.length ? displayRow : result_content.productList.length;
	for(var i=0; i<rDisplayRow; i++) {
		
		html += '<div style="width:800px; margin:0 auto;" id="MAIN_TITLE_BAR" class="GRE_ABR h_40px lh_40">';
		html += '	<div class="TITLE_TXT">'+result_content.productList[i].product.name+'</div>';
		html += '</div>';
		
		// 組圖片路徑
		imagePath = "/cateringservice/file/SHOW/schoolFoodImg|"+result_content.countyId+"|"+result_content.productList[i].product.productId;
		html += '<div style="" class="query_detail_info">';
		html += '	<div id="MAIM_LINER_A"></div>';
		html += '	<div id="TAB_TYP_A" class="">';
		html += '	<div class="TAB_TY_A"><table border="1" width="290"><tbody>';
		html += '		<tr><td bgcolor="#678948" align="center" colspan="2" class="TIT_A">產品資訊</td></tr>';
		html += '		<tr bgcolor="#FFFFF4"><td align="right">產品圖片</td>';
		html += '			<td align="left"><a href="' +imagePath+ '" data-lightbox="roadtrip"><img src="' +imagePath+ '"  width="150px" height="130px" class="showMode" /></a></td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFF4"><td align="right">產品名稱</td>';
		html += '			<td align="left">'+result_content.productList[i].product.name+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">保存方式</td>';
		html += '			<td align="left">'+result_content.productList[i].product.preservedMethod+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">販售方式</td>';
		html += '			<td align="left">'+result_content.productList[i].product.soldway+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">包裝型態</td>';
		html += '			<td align="left">'+result_content.productList[i].product.packages+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">認證類別</td>';
		html += '			<td align="left">'+result_content.productList[i].product.certification+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">認證編號</td>';
		html += '			<td align="left">'+result_content.productList[i].product.certificationId+'</td>';
		html += '		</tr>';
		html += '	</tbody></table></div>';

		html += '	<div class="SPP_A"></div>';
		html += '	<div class="TAB_TY_A"><table border="1" width="290"><tbody>';
		html += '		<tr><td bgcolor="#678948" align="center" colspan="2" class="TIT_A">廠商資訊</td></tr>';
		html += '		<tr bgcolor="#FFFFF4"><td width="80px" align="right">公司名稱</td>';
		html += '			<td align="left">'+result_content.productList[i].company.com_name+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">地址</td>';
		html += '			<td align="left">'+result_content.productList[i].company.com_add+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">電話</td>';
		html += '			<td align="left">'+result_content.productList[i].company.com_tel+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">負責人</td>';
		html += '			<td align="left">'+result_content.productList[i].company.com_owner+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">工廠名稱</td>';
		html += '			<td align="left">'+result_content.productList[i].factory.fac_name+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">地址</td>';
		html += '			<td align="left">'+result_content.productList[i].factory.fac_add+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">消費者服務專線</td>';
		html += '			<td align="left">'+result_content.productList[i].customerService.cs_hotline+'</td>';
		html += '		</tr>';
		html += '		<tr bgcolor="#FFFFFF"><td align="right">申訴事件負責人</td>';
		html += '			<td align="left">'+result_content.productList[i].customerService.cs_owner+'</td>';
		html += '		</tr>';
		html += '	</tbody></table></div>';
		html += '	</div>';
		html += '	<div id="MAIM_LINER_A" class=""></div>';
		html += '</div>';
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
	//pageHtml += '前往<select style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="query_SfProduct(this.value)"> |';
	pageHtml += '前往第<select id="displayCrnPage" style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="getCustomerQuerySF()"> |';
		for(var j = 1; j<= totalPage ; j++) {
			if(j ==displayCrnPage) {
				pageHtml += '<option selected value="'+ j +'">'+ j +'</option>';
			} else {
				pageHtml += '<option value="'+ j +'">'+ j +'</option>';
			}
		}
	pageHtml += '</select>頁';

	// 每頁顯示n筆資料，目前有問題，先不作用。
	pageHtml += '　　每頁顯示<select id="displayRow" style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="getCustomerQuerySF()">';
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

	$("#page").html("");
	$("#page").html(pageHtml);
	hideDisplayArea();
}

function hideDisplayArea() {
	// 幫 div.TITLE_TXT 加上 hover 及 click 事件
	// 同時把兄弟元素 div.query_detail_info 隱藏起來
	$('#resultTable div.GRE_ABR').hover(function(){
		$(this).addClass('qa_title_on');
	}, function(){
		$(this).removeClass('qa_title_on');
	}).click(function(){
		// 當點到標題時，若答案是隱藏時則顯示它；反之則隱藏
		$(this).next('.query_detail_info').slideToggle();
	}).siblings('.query_detail_info').hide();

	$('#resultTable div.query_detail_info:first').show();
}

$(function(){
	gotop();	// 浮動按鈕
});

/**
 * 組『總筆數、目前頁碼、換頁下拉』及『顯示該畫面之各筆資料』等功能列
 * result_content : 取得的 JSON 內容
 * crnPage : 當前要顯示之頁數
 */
//function changePage(result_content, tableName, header, btnId) {
//	var displayRow = $("#displayRow").val()===undefined? row : $("#displayRow").val();
//	var displayCrnPage = $("#displayCrnPage").val()===undefined? crnPage : $("#displayCrnPage").val();
//	console.log("每頁顯示的列數:"+displayRow+"筆, 按鈕Id:"+btnId);
//
//	//清單內容
//	$("#" + tableName).empty();
//	var html = "";
//	// var thCount=header.length;
//	html += "<tr>";
//	for ( var item in header) {
//		html += "<th>" + header[item] + "</th/>";
//	}
//	html += "</tr>";
//
//	// 每頁顯示設定的筆數，如果該頁資料不足時，則顯示取得的數量之筆數。
//	var rDisplayRow = displayRow==result_content.productList.length ? displayRow : result_content.productList.length;
//	for(var i=0; i<rDisplayRow; i++) {
//
//		// 判斷是否有打勾
//		var checkStatus;
//		if( result_content.productList[i].onShelfDate!="" ) {
//			checkStatus = "checked";
//		} else {
//			checkStatus = "";
//		}
//		
//		var prodId = result_content.productList[i].id;
//
//		var strTR = "<tr>";
//		strTR += "<td>" + result_content.productList[i].id  + "</td>";
//		strTR += "<td>" + result_content.productList[i].name  + "</td>";
//		//strTR += "<td>" + result_content.productList[i].supplierCompanyId  + "</td>";
//		strTR += "<td>" + result_content.productList[i].supplierCompanyName  + "</td>";
//		//strTR += "<td>" + result_content.productList[i].manufacturerId  + "</td>";
//		strTR += "<td>" + result_content.productList[i].manufacturerName  + "</td>";
//		strTR += "<td>" + result_content.productList[i].preservedMethod  + "</td>";
//		strTR += "<td>" + result_content.productList[i].soldway  + "</td>";
//		strTR += "<td>" + result_content.productList[i].packages  + "</td>";
//		strTR += "<td>" + result_content.productList[i].certification  + "</td>";
//		strTR += "<td>" + result_content.productList[i].certificationId  + "</td>";
//		strTR += "<td>" + result_content.productList[i].appliedNoByJDF + "</td>";
//		//strTR += "<td>" + result_content.productList[i].barcode  + "</td>";
//		//strTR += "<td>" + result_content.productList[i].county  + "</td>";
//		strTR += "<td>" + result_content.productList[i].onShelfDate  + "</td>";
//		strTR += "<td>" + result_content.productList[i].offShelfDate  + "</td>";
//
//		strTR += "<td><input type='checkbox' id='productStatus_"+i+"' name='productStatus[]' onclick='changeCheckStatus("+prodId+", \""+checkStatus+"\", this)' "+checkStatus+" /></td>";
//		strTR += "</tr>";
//		html += strTR;
//	}
//	$("#" + tableName).html(html);
//	$("#" + tableName).hide();
//	$("#" + tableName).show("slow");
//
//	// 資料筆數
//	$("#query_rule").html("");
//	$("#query_rule").html("資料筆數:" +result_content.totalNum );
//	// 頁數管理
//	var pageHtml = "";
//	pageHtml  += "第  "+ displayCrnPage +" 頁 | ";
//	// 總共頁數
//	var totalPage  = 0; //分頁若整除，不需+1
//	if(result_content.totalNum % displayRow == 0) {
//		totalPage = parseInt(result_content.totalNum / displayRow ,10);
//	} else {
//		totalPage = parseInt(result_content.totalNum / displayRow ,10)+1;
//	}
//	pageHtml  += "共 "+ totalPage +" 頁 | ";
//	// 頁面計算
//	pageHtml += '前往第<select id="displayCrnPage" style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="redirectNodeJs(\''+btnId+'\')"> |';
//		for(var j = 1; j<= totalPage ; j++) {
//			if(j ==displayCrnPage) {
//				pageHtml += '<option selected value="'+ j +'">'+ j +'</option>';
//			} else {
//				pageHtml += '<option value="'+ j +'">'+ j +'</option>';
//			}
//		}
//	pageHtml += '</select>頁';
//
//	// 每頁顯示n筆資料，目前有問題，先不作用。
//	pageHtml += '　　每頁顯示<select id="displayRow" style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="redirectNodeJs(\''+btnId+'\')">';
//	for(var j = 0; j<pageRow.length ; j++) {
//		if(pageRow[j]==displayRow) {
//			pageHtml += '<option selected value="'+ pageRow[j] +'">'+ pageRow[j] +'</option>';
//			row = pageRow[j];
//		} else {
//			pageHtml += '<option value="'+ pageRow[j] +'">'+ pageRow[j] +'</option>';
//			row = pageRow[j];
//		}
//	}
//	pageHtml += '</select>筆資料';
//	
//	var strSubmitBtnModify = "<a href='#' onclick='modifySchoolProductSet()'>變更上架商品</a>";
//	
//	$("#page").html("");
//	$("#page").html(pageHtml);
//	
//	$("#displaySubmitBtnModify").html("");
//	$("#displaySubmitBtnModify").html(strSubmitBtnModify);
//}
