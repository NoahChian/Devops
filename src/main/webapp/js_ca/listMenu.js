
var MSG= new MsgsProcessing();
var util=new CateringServiceUtil();
var PAGE_TABLE="resultTable";
var row = 30; //每頁筆數
var crnPage = 1;//當前頁數
$(function() {
	$( "#start_date" ).datepicker({ dateFormat: "yy/mm/dd" });
	$( "#end_date" ).datepicker({ dateFormat: "yy/mm/dd" });
	});
function defaultYearMonth(){
	var currentTime = new Date();
	var year = currentTime.getFullYear();
	var month = currentTime.getMonth() + 1;
	
	//alert(year +","+ month);
	
	var start = year +"/"+ month +"/01";
	$("#start_date").val(start);
	
	var end = "";
	if(month==1 || month==3 || month==5 || month ==7 || month==8 || month ==10 || month==12){
		end = year +"/"+ month +"/31";
		$("#end_date").val(end);
	} else if (month==4 || month==6 || month==9 || month==11){
		end = year +"/"+ month +"/30";
		$("#end_date").val(end);
	} else if (month == 2){
		end = year +"/"+ month +"/28";
		$("#end_date").val(end);
	}
}
function querySchool(){
	//request
	var request_data =	{
			"method":"querySchoolList",
			"args":{
				"cid":0, //由API控制送出的權限判定
				"page":1 ,// 要一次拉出所有學校清單
				"perpage":9000, //寫死9000間學校
				"schoolId":$("#_schoolIdList").val()
			}
						};
	var response_obj = call_rest_api(request_data);
	
	//alert(response_obj);
	
	//response
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			var new_drop = "<select id=\"school_id\">";
			if(result_content.school.length > 1){
				new_drop += "<option value='-1'>全部</option>"; //能夠有全部學校的選項
			}
			for(var i=0; i<result_content.school.length; i++) {
				new_drop += "<option value=" + result_content.school[i].sid + ">" + result_content.school[i].schoolName + "</option>";
			}
//			new_drop += "<option value='-1'>全部-單日(起始日)</option>"; //能夠有全部學校的選項
			new_drop += "</select>";
			$("#dropDown_school").append(new_drop);
			
			//#11168 活動式資料查詢
			$( "#school_id" ).combobox();
			
			//如果只有一個選項就鎖死
			if(result_content.school.length ==1){
				$( "#school_id" ).parent().find("input").attr('disabled', true);
			}
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}
function queryKitchen(){
	
	//request
	var request_data =	{
						 "method":"QueryKitchenRestaurantList",
			 				"args":{
			 					"schoolId":$("#_schoolIdList").val()
				 			}
						};
	var response_obj = call_rest_api(request_data);
	
	//alert(response_obj);
	
	//response
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			var new_drop = "<select id=\"kitchen_id\">";
			if(result_content.kitchenRestaurantList.length > 1){
				new_drop += "<option value='-1'>全部</option>"; //能夠有全部供餐的選項
			}
			for(var i=0; i<result_content.kitchenRestaurantList.length; i++) {
				var tempData = result_content.kitchenRestaurantList[i];
				if(tempData.kitchenname != null){
					new_drop += "<option dc='kitchen' value=" + tempData.kitchenid + ">" + tempData.kitchenname + "</option>";
				} else if(tempData.restaurantname != null)  {
					new_drop += "<option dc='restaurant' value=" + tempData.restaurantid  + ">("+tempData.sfstreetname+")"+ tempData.restaurantname + "</option>";
				}
			}
//			new_drop += "<option value='-1'>全部-單日(起始日)</option>"; //能夠有全部供餐的選項
			new_drop += "</select>";
			
			$("#dropDown_kitchen").append(new_drop);
			
			//#11168 活動式資料查詢
			$( "#kitchen_id" ).combobox();
			
			//如果只有一個選項就鎖死
			if(result_content.kitchenRestaurantList.length ==1){
				$( "#kitchen_id" ).parent().find("input").attr('disabled', true);
			}
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
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

function query_Menu_List(page)
{
	crnPage = page;
	MSG.alertMsgs("loading", "查詢中...", 0);
	var schoolId = -1;
	if($("#_schoolIdList").val() != ""){
		schoolId = $("#_schoolIdList").val();
	} else if($("#school_id").length > 0){
		//輸入欄位如果被清空,那下拉的值將不帶入
		if($("#school_id").parent().find(".custom-combobox").find('input').val() != ""){
			schoolId = $("#school_id").val();
		}
	}
	
	var kitchenId = -1;var restaurantId = -1;
	if($("#kitchen_id").length > 0){
		//輸入欄位如果被清空,那下拉的值將不帶入
		if($(".custom-combobox").find('input').val() != ""){
			var dc = $('select#kitchen_id option:selected').attr("dc");
			if(dc=="kitchen"){
				kitchenId = $("#kitchen_id").val();
			} else if(dc=="restaurant"){
				restaurantId = $("#kitchen_id").val();
			}
		}
	}
	var startDate = $("#start_date").val();
	var endDate = $("#end_date").val();
	// 檢核日期 : 開始日期不可大於結束日期
	chkDate(startDate, endDate);
//	if(schoolId == -1){ endDate= $("#start_date").val();}else{endDate = $("#end_date").val();}
	var request_data =	{
							"method":"queryMenuList",
							"args":{
								"begDate":startDate, 
								"endDate":endDate, 
								"kitchenId":kitchenId, 
								"restaurantId":restaurantId, 
								"schoolId":schoolId, 
								"countyId":-1, 
								"queryLimit":0, 
								"page":crnPage ,// 當前第X頁
								"perpage":row
							}
						};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	//API呼叫正確
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1)
		{	
			if(Object.keys(result_content.menu).length==0){
				$(".blockUIImgs").hide();
				$(".blockUIMsgs").html("查詢範圍內沒有資料。");
				$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
			}else{
//				show_Menu(result_content, ["日期","廚房","學校","主食","主菜","主菜一","副菜一","副菜二","副菜三","蔬菜","湯品","附餐","熱量"], PAGE_TABLE);
				show_Menu(result_content, ["菜單日期","供餐類別","菜色名稱","食材","進貨日期","重量","食材供應商","供餐來源"], PAGE_TABLE);

			$.unblockUI();
			}
		}
		else
		{
			$(".blockUIImgs").hide();
			$(".blockUIMsgs").html("查詢發生錯誤，訊息為：" + result_content.msg);
			$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
			//MSG.alertMsgs("check", "查詢發生錯誤，訊息為：" + result_content.msg, 0);
			return 0;
		}
	}
	else
	{
		$(".blockUIImgs").hide();
		$(".blockUIMsgs").html("查詢發生無法預期的錯誤，錯誤訊息為：" + result_content.msg);
		$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
		//MSG.alertMsgs("check", "查詢發生無法預期的錯誤，錯誤訊息為：" + result_content.msg, 0);
		return 0;
	}
	
}
function show_Menu(result_content,header,tableName)
{
//清單內容
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<thead><tr align='center'>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr></thead>";
	var td_class="";
	for(var i in result_content.menu) {
		if(i%2==0){
			td_class =  "align='center'";
		}else{

			td_class =  "align='center'";
		}
		var menudate = "";
		var menutype = "";
		var dishname = "";
		var ingredientname = "";
		var stockdate = "";
		var ingredient = "";
		var suppliername = "";
		var comboname = "";
		
//		var kitchenname = "";
//		var restaurantname = "";
		
//		var restaurantid = "";
//		var schoolid = "";
//		var schoolcode = "";
//		var schoolname = "";
//		var kitchenid = "";
//		var ingredientunit = "";

		if(result_content.menu[i].menudate!=null){menudate=result_content.menu[i].menudate;}
		if(result_content.menu[i].menutype!=null){menutype=result_content.menu[i].menutype;}
		if(result_content.menu[i].dishname!=null){dishname=result_content.menu[i].dishname;}
		if(result_content.menu[i].ingredientname!=null){ingredientname=result_content.menu[i].ingredientname;}
		if(result_content.menu[i].stockdate!=null){stockdate=result_content.menu[i].stockdate;}
		if(result_content.menu[i].ingredientquantity!=null && result_content.menu[i].ingredientunit!=null ){
			ingredient=result_content.menu[i].ingredientquantity +" "+ result_content.menu[i].ingredientunit;
		}
		//if(result_content.menu[i].mainDish2id!=null){mainDish2id=result_content.menu[i].mainDish2id;}
		if(result_content.menu[i].suppliername!=null){suppliername=result_content.menu[i].suppliername;}
		
		if(result_content.menu[i].kitchenname!=null && result_content.menu[i].kitchenname!=""){
			comboname = result_content.menu[i].kitchenname;
		} else if(result_content.menu[i].restaurantname!=null && result_content.menu[i].restaurantname!=""){
			comboname = result_content.menu[i].restaurantname;
		}
				
		var group1 = " dt='group1' "; // rowspan for dishname		
		var strTR = "<tr>";
		strTR += "<td "+ td_class +  ">" + timeConverter(menudate) + "</td>";
		strTR += "<td "+ td_class +  ">" + menutype + "</td>";
		strTR += "<td "+ td_class + group1 +  ">" + dishname + "</td>";
		strTR += "<td "+ td_class +  ">" + ingredientname + "</td>";
		strTR += "<td "+ td_class +  ">" + stockdate + "</td>";
		strTR += "<td "+ td_class +  ">" + ingredient + "</td>";
		strTR += "<td "+ td_class +  ">" + suppliername + "</td>";
		strTR += "<td "+ td_class +  ">" + comboname + "</td>";
		strTR += "</tr>";
		html += strTR;
	}
	$("#" + tableName).html(html);
	
	
//	$('td[dt="group1"]').rowspan();
	
	//#11168 含食材checkbox如果未勾選,查詢結果則會不顯示以下欄位(食材,進貨日期,重量,食材供應商)
	if(!$('input[id="ingredientCheckbox"]').is(':checked')){
		//隱藏index3~6的欄位
		$("#resultTable").find('tr th:nth-child(4)').hide();
		$("#resultTable").find('tr th:nth-child(5)').hide();
		$("#resultTable").find('tr th:nth-child(6)').hide();
		$("#resultTable").find('tr th:nth-child(7)').hide();
		
		$("#resultTable").find('tr td:nth-child(4)').hide();
		$("#resultTable").find('tr td:nth-child(5)').hide();
		$("#resultTable").find('tr td:nth-child(6)').hide();
		$("#resultTable").find('tr td:nth-child(7)').hide();
		
		//全部的th css置中
		$("#resultTable").find('tr th').css('text-align','center');
	}

	$("#" + tableName).hide();
	$("#" + tableName).show("slow");
	$("#MAIN_TITLE_BAR").show("slow");
	$("#tiltleAct").show("slow");

//資料筆數
	$("#query_rule").html("");
	$("#query_rule").html("資料筆數:" +result_content.totalCol );
//頁數管理	
	var pageHtml = "";
	//資料筆數
	pageHtml  += "資料筆數:" +result_content.totalCol +  "筆"  ;
	pageHtml  += " | 第  "+ crnPage +" 頁 | ";
//總共頁數
var totalPage  = 0; //分頁若整除，不需+1
if(result_content.totalCol % row == 0){
totalPage = parseInt(result_content.totalCol / row ,10);
}else{
	totalPage = parseInt(result_content.totalCol / row ,10)+1;}
	pageHtml  += "共 "+ totalPage +" 頁 | ";
//頁面計算
	pageHtml += '前往<select style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="query_Menu_List(this.value)"> |';
		for(var j = 1; j<= totalPage ; j++) {
		if(j ==crnPage){
		pageHtml += '<option selected value="'+ j +'">'+ j +'</option>';
		}else{
		pageHtml += '<option value="'+ j +'">'+ j +'</option>';}
		}
	pageHtml += '</select>';
	$("#page").html("");
	$("#page").html(pageHtml);
}
function export_excel(){
	var schoolId = 0;
	
	if($("#_schoolIdList").val() != ""){
		schoolId = $("#_schoolIdList").val();
	} else if($("#school_id").length > 0){
		//輸入欄位如果被清空,那下拉的值將不帶入
		if($("#school_id").parent().find(".custom-combobox").find('input').val() != ""){
			schoolId = $("#school_id").val();
		}
	}
	
	var kitchenId = 0;var restaurantId = 0;
	if($("#kitchen_id").length > 0){
		if($(".custom-combobox").find('input').val() != ""){
			var dc = $('select#kitchen_id option:selected').attr("dc");
			if(dc=="kitchen"){
				kitchenId = $("#kitchen_id").val();
			} else if(dc=="restaurant"){
				restaurantId = $("#kitchen_id").val();
			}
		}
	}
	
	var startDate = $("#start_date").val();
	var endDate = $("#end_date").val();
	
	// 修正匯出資料與查詢資料不符,註解掉變更endDate
	//if(schoolId == -1){ endDate= $("#start_date").val();}else{endDate = $("#end_date").val();}
	
	startDate = startDate.replace("/", "-");
	startDate = startDate.replace("/", "-");
	
	endDate = endDate.replace("/", "-");
	endDate = endDate.replace("/", "-");
	
	var link = "/cateringservice/rest/API/XLS/menulist&" + startDate + "&" + endDate + "&" + kitchenId + "&" + restaurantId + "&" + schoolId + "&0&0";
	
	//alert(link);
	
	window.open(link,"_blank");
}

function timeConverter(UNIX_timestamp,type){
	if(UNIX_timestamp == null){
		return "";
	}
//	var a = new Date(UNIX_timestamp * 1000);
	var a = new Date(UNIX_timestamp);
//	var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
	var year = a.getFullYear();
//	var month = months[a.getMonth()];
	var month = "0" + (a.getMonth() + 1);
	var date = "0" + a.getDate();
	var hour = "0" + a.getHours();
	var min = "0" + a.getMinutes();
	var sec = "0" + a.getSeconds();

	if(type == "datetime"){
		var time = year + "/" + month.substr(-2) + '/' + date.substr(-2) + ' ' + hour.substr(-2) + ':' + min.substr(-2) + ':' + sec.substr(-2) ;
	} else {
		var time = year + "/" + month.substr(-2) + '/' + date.substr(-2);
	}
//	var time = year + "/" + month.substr(-2) + '/' + date.substr(-2) + ' ' + hour.substr(-2) + ':' + min.substr(-2) + ':' + sec.substr(-2) ;
	return time;
}
