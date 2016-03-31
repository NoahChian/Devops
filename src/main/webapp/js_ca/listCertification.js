var MSG= new MsgsProcessing();
var util=new CateringServiceUtil();
var PAGE_TABLE="fcertificationList";//"resultTable";
var row = 20; //每頁筆數
var crnPage = 1;//當前頁數
var kitchenRestaurantCount = 0; // 搜尋供餐業者或廚房之結果筆數

$(function() {
	$( "#startDate" ).datepicker({ dateFormat: "yy/mm/dd" });
	$( "#endDate" ).datepicker({ dateFormat: "yy/mm/dd" });
	$("#startDate").click(function(){
		$("#ui-datepicker-div").css("display", "none");	
	});
	$("#endDate").click(function(){
		$("#ui-datepicker-div").css("display", "none");	
	});
	//預設值為先寫死為2013/12/01~2013/12/31
	//$( "#start_date" ).attr("value", "2013/12/01");
	//$( "#end_date" ).attr("value", "2013/12/31")
	});
/*使用Titan的預設日期選擇function*/ 
function defaultYearMonth(){
	var currentTime = new Date();
	var year = currentTime.getFullYear();
	var month = currentTime.getMonth() + 1;
		
	var start = year +"/"+ month +"/01";
	$("#startDate").val(start);
	
	//重寫結束時間預設值  20140219 KC
	var monthDays=new Date(year,month,0).getDate();
	$("#endDate").val(year +"/"+ month +"/"+monthDays);	
}
function queryKitchen(){
	
	var request_data =	{
		"method":"QueryKitchenRestaurantList",//queryAllKitchenList
			"args":{
				"schoolId":$("#_schoolIdList").val()//kitchenType":["005","006"]
	 	}
	};
						
	var response_obj = call_rest_api(request_data);
	
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			//school
			var new_drop = "<select id=\"kitchen_id\" style=\"width:100%\">";
			kitchenRestaurantCount = result_content.kitchenRestaurantList.length;
			if(kitchenRestaurantCount == 0){
				new_drop += "<option>無供餐業者或廚房</option>";
			}
			if(result_content.kitchenRestaurantList.length > 1){
				new_drop += "<option value='-1'>請選擇</option>"; //能夠有全部供餐的選項
			}
			for(var i=0; i<result_content.kitchenRestaurantList.length; i++) {
				var tempData = result_content.kitchenRestaurantList[i];
				if(tempData.kitchenname != null){
					new_drop += "<option dc='kitchen' value=" + tempData.kitchenid + ">" + tempData.kitchenname + "</option>";
				} else if(tempData.restaurantname != null)  {
					new_drop += "<option dc='restaurant' value=" + tempData.restaurantid  + ">("+tempData.sfstreetname+")"+ tempData.restaurantname + "</option>";
				}
			}
			new_drop += "</select>";
			$("#dropDown_kitchen").append(new_drop);
			
			$( "#kitchen_id" ).combobox();
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
		}
	} else{
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	}
}
function CertificationList(page) {
	crnPage = page;
	var query_form = document.forms["Certification"];
	var ingredientName = query_form.elements["ingredientName"].value;
	var startDate = query_form.elements["startDate"].value;
	var endDate = query_form.elements["endDate"].value;
//	var kitchenId = query_form.elements["kitchen_id"].value; // name 改id
	
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
	
	if(kitchenRestaurantCount == 0){
		MSG.alertMsgs("check", "無供餐業者或廚房,無需查詢檢驗報告");
		return;
	}
	if(startDate=="" || endDate =="" || startDate == null || endDate==null){
		MSG.alertMsgs("check", "請選擇菜單日期");
		return;
	} else if (kitchenId == -1 && restaurantId== -1){
		MSG.alertMsgs("check", "請選擇業者/廚房");
		return;
	}
	
	//檢查查詢期間不得大於90天       20140219 KC
	if (!util_check_date_range($("#startDate").datepicker("getDate"),$("#endDate").datepicker("getDate"))){
		return;
	}
	
	MSG.alertMsgs("loading", "查詢中...", 0);

	var request_data = {//API change the var "kitchenName" to "kitchenId" should re check this part 
			"method":"queryCertificationInfo",
		    "args": {
		    	"ingredientName" : ingredientName,
				"kitchenId" : kitchenId,
				"restaurantId" : restaurantId,
				"startDate" : startDate,
				"endDate" : endDate, 
				"queryLimit":0, 
				"page":crnPage ,// 當前第X頁
				"perpage":row
		}
	};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{
		if(response_obj.result_content.certificationList.length == 0){
			$(".blockUIImgs").hide();
			$(".blockUIMsgs").html("查詢範圍內沒有資料。");
			$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
		} else {
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 0){	//success
				var new_div = "";
				new_div += "<thead><tr>";
				new_div += ""
					+ "<td align=\"center\">菜單日期</td>"
					+ "<td align=\"center\">廚房名稱</td>"
					+ "<td align=\"center\">供應商</td>"
					+ "<td align=\"center\">食材</td>"
					+ "<td align=\"center\">批號</td>"
					+ "<td align=\"center\">產品屬性</td>"
					+ "<td align=\"center\">有效日期</td>"
					+ "<td align=\"center\">進貨日期</td>"
					+ "<td align=\"center\">生產日期</td>"
					+ "<td align=\"center\">檢驗報告</td>";
				new_div += "</tr></thead>";
				for(var i=0; i<result_content.certificationList.length; i++) {
					var edate=result_content.certificationList[i].expirationDate;
					var sdate=result_content.certificationList[i].stockDate ;
					var mdate=result_content.certificationList[i].manufactureDate;
					var gmbean = "";
					var gmcorn = "";
					var psfood = "";
					if(edate==""||edate=="null"||edate==null){edate="";}
					if(sdate==""||sdate=="null"||sdate==null){sdate="";}
					if(mdate==""||mdate=="null"||mdate==null){mdate="";}
					if(result_content.certificationList[i].ingredientAttribute.gmbean==1){gmbean=" 基改黃豆 "}else{gmbean=""}
					if(result_content.certificationList[i].ingredientAttribute.gmbean==1){gmcorn=" 基改玉米 "}else{gmcorn=""}
					if(result_content.certificationList[i].ingredientAttribute.gmbean==1){psfood=" 加工食品 "}else{psfood=""}
						new_div += "<tr>";
						new_div += "<td>"
								+ result_content.certificationList[i].menuDate+"</td>";
						new_div += "<td>"
								+ result_content.certificationList[i].kitchenName + "</td>";
						new_div += "<td>"
								+ result_content.certificationList[i].supplyName + "</td>";
						new_div += "<td>"
								+ result_content.certificationList[i].ingredientName + "</td>";
						new_div += "<td>"
								+ result_content.certificationList[i].lotNumber + "</td>";
						new_div += "<td>"
							+ gmbean + gmcorn + psfood + "</td>";
						new_div += "<td>"
								+ edate + "</td>";
						new_div += "<td>"
								+ sdate + "</td>";
						new_div += "<td>"
								+ mdate + "</td>";
						new_div += "<td>";
						if(result_content.certificationList[i].fileExist=="1"){
							var dc = $('select#kitchen_id option:selected').attr("dc");
							if(dc=="kitchen"){
								new_div += "<a target='_blank' href='../../file/SHOW/inspect_v2|"
									+ result_content.certificationList[i].ingredientBatchId + "'>檢驗報告</a>";
							} else if(dc=="restaurant"){
								new_div += "<a target='_blank' href='../../file/SHOW/inspect_v2|"
									+ result_content.certificationList[i].fileName + "'>檢驗報告</a>";
							}
						}
						new_div += "</td></tr>";
					
				}
				new_div += "";
				$("#fcertificationList").html("");
				$("#fcertificationList").append(new_div);
				$("#fcertificationList").show();
				
				genPageInfo(result_content);
			} else {
				MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			}
			$.unblockUI();
		}
	} else{
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
	}
}

function genPageInfo(result_content){
	//資料筆數
	$("#query_rule").html("");
	$("#query_rule").html("資料筆數:" +result_content.totalCol );
	//頁數管理	
	var pageHtml = "";
	//資料筆數
//	pageHtml  += "資料筆數:" +result_content.totalCol +  "筆"  ;
	pageHtml  += " | 第  "+ crnPage +" 頁 | ";
	//總共頁數
	var totalPage  = 0; //分頁若整除，不需+1
	if(result_content.totalCol % row == 0){
		totalPage = parseInt(result_content.totalCol / row ,10);
	}else{
		totalPage = parseInt(result_content.totalCol / row ,10)+1;}
		pageHtml  += "共 "+ totalPage +" 頁 | ";
		//頁面計算
		pageHtml += '前往<select style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="CertificationList(this.value)"> |';
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