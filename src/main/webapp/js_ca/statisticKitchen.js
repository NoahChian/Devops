
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
			
			//下拉控制變數
			var queryType = $("#queryType").val();
			
			for(var i=0; i<result_content.kitchenRestaurantList.length; i++) {
				var tempData = result_content.kitchenRestaurantList[i];
				if(tempData.kitchenname != null){
					if(queryType == "kitchen"){
						new_drop += "<option class='kitchen' dc='kitchen' value=" + tempData.kitchenid + ">" + tempData.kitchenname + "</option>";
					}
				} else if(tempData.restaurantname != null)  {
					if(queryType == "restaurant"){
						new_drop += "<option class='restaurant' dc='restaurant' value=" + tempData.restaurantid  + ">("+tempData.sfstreetname+")"+ tempData.restaurantname + "</option>";
					}
				}
			}
//			new_drop += "<option value='-1'>全部-單日(起始日)</option>"; //能夠有全部供餐的選項
			new_drop += "</select>";
			
			$("#dropDown_kitchen").html(new_drop);
			
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
function queryCounty(){
	//request
	var request_data =	{
						 "method":"QueryCounties",
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
			var new_drop = "<select id=\"county_id\">";
			if(result_content.counties.length > 1){
				new_drop += "<option value='-1'>全部</option>"; //能夠有全部供餐的選項
			}
			for(var i=0; i<result_content.counties.length; i++) {
				var tempData = result_content.counties[i];
				new_drop += "<option value=" + tempData.cid + ">" + tempData.countiesName + "</option>";
			}
//			new_drop += "<option value='-1'>全部-單日(起始日)</option>"; //能夠有全部供餐的選項

			new_drop += "</select>";
			
			$("#dropDown_county").append(new_drop);
			
			$( "#county_id" ).combobox();
			
			//如果只有一個選項就鎖死
			if(result_content.counties.length ==1){
				$( "#county_id" ).parent().find("input").attr('disabled', true);
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
function query_Menu_List(page)
{
	crnPage = page;
	MSG.alertMsgs("loading", "查詢中...", 0);
	
	var countyId = -1;
	if($("#county_id").length > 0){
		//輸入欄位如果被清空,那下拉的值將不帶入
		if($("#county_id").parent().find(".custom-combobox").find('input').val() != ""){
			countyId = $("#county_id").val();
		}
	}
	
	var schoolId = -1;
	if($("#_schoolIdList").val() != ""){
		//輸入欄位如果被清空,那下拉的值將不帶入
		schoolId = $("#_schoolIdList").val();
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
	
	var queryType = $("#queryType").val();

	var request_data =	{
							"method":"QueryStatisticSchool",
							"args":{
								"queryItem":"statisticKitchen",
								"kitchenId":kitchenId,
								"restaurantId":restaurantId,
								"county":countyId,
								"schoolId":schoolId,
								"queryType":queryType,
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
			if(result_content.total==0){
				$(".blockUIImgs").hide();
				$(".blockUIMsgs").html("查詢範圍內沒有資料。");
				$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
			} else {
				var th=response_obj["result_content"]["header"];
				var total=response_obj["result_content"]["total"];
				var list=response_obj["result_content"]["result"];
				//printTableWithHeader(list,th,total);
				th = ["縣市  ","團膳 /餐廳名稱","受供餐學校數","受供餐學校"];
				util.setTableWithHeader(list,th,PAGE_TABLE);
				
				//縣市欄位調整寬度
				$("#resultTable").find("tr th:nth-child(1)").css("width","10%");
				$("#tiltleAct").show("slow");

	//==================
				//資料筆數
//				$("#query_rule").html("");
//				$("#query_rule").html("資料筆數:" +result_content.total );
			//頁數管理	
				var pageHtml = "";
				//資料筆數
				pageHtml  += "資料筆數:" +result_content.total +  "筆";
				$("#page").html("");
				$("#page").html(pageHtml);
	//==================
				//總共頁數
				var totalPage  = 0; //分頁若整除，不需+1
				if(result_content.total % row == 0){
				totalPage = parseInt(result_content.total / row ,10);
				}else{
					totalPage = parseInt(result_content.total / row ,10)+1;}
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
	//==================
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
function export_excel(){
	var countyId = 0;if($("#county_id").length > 0){countyId = $("#county_id").val();}
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
	var schoolId = 0;
	if($("#_schoolIdList").val() != ""){
		//輸入欄位如果被清空,那下拉的值將不帶入
		schoolId = $("#_schoolIdList").val();
	}
	
	var queryType = $("#queryType").val();
	
	var link = "/cateringservice/rest/API/XLS/statisticKitchen&" + countyId + "&" + kitchenId+ "&" + restaurantId + "&" + schoolId + "&" + queryType;	
	window.open(link,"_blank");
}

function change_date_range(){
	defaultYearMonthRange($("#ddlDateRange").val());
}