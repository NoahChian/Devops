
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
				"perpage":9000 //寫死9000間學校
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
			for(var i=0; i<result_content.school.length; i++) {
				new_drop += "<option value=" + result_content.school[i].sid + ">" + result_content.school[i].schoolName + "</option>";
			}
			new_drop += "<option value='-1'>全部</option>"; //能夠有全部學校的選項
			new_drop += "</select>";
			$("#dropDown_school").append(new_drop);
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
						 "method":"queryKitchenList2",
			 				"args":{	
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
			for(var i=0; i<result_content.kitchenList.length; i++) {
				new_drop += "<option value=" + result_content.kitchenList[i].kitchenId + ">" + result_content.kitchenList[i].kitchenName + "</option>";
			}
			new_drop += "</select>";
			$("#dropDown_kitchen").append(new_drop);
		} else {
			MSG.alertMsgs("check", "呼叫API發生錯誤，訊息為：" + result_content.msg, 0);
			//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		MSG.alertMsgs("check", "無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg, 0);
		//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}
function query_Ingredient_List(page)
{
	crnPage = page;
	MSG.alertMsgs("loading", "查詢中...", 0);
	var schoolId = -1;if($("#school_id").length > 0){schoolId = $("#school_id").val();}
	var kitchenId = -1;if($("#kitchen_id").length > 0){kitchenId = $("#kitchen_id").val();}
	var startDate = $("#start_date").val();
	var endDate = $("#end_date").val();
	var dishname ="" ;dishname= $("#dishname").val();
	var ingredientName ="";ingredientName = $("#ingredientName").val();
	var brand ="" ;brand= $("#brand").val();
	var supplierName ="";supplierName = $("#supplierName").val();
	var queryLimit ="0";queryLimit = $("#queryLimit").val();
	var countyId =-1;
	var request_data =	{
				"method":"queryIngredientList",
				"args":{
					"begDate":startDate, 
					"endDate":endDate, 
					"kitchenId":kitchenId, 
					"schoolId":schoolId, 
					"countyId":countyId, 
					"queryLimit":queryLimit, 
					"dishname":dishname,
					"ingredientName":ingredientName,
					"brand":supplierName,
					"supplierName":supplierName,
					"page":crnPage,// 當前第X頁
					"perpage":row
				}
						};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	//API呼叫正確
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1)
		{	
			if(Object.keys(result_content.ingredient).length==0){
				$(".blockUIImgs").hide();
				$(".blockUIMsgs").html("查詢範圍內沒有資料。");
				$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();" value="確定">');
			}else{
				show_Ingredient(result_content, ["日期","廚房","學校","菜色名稱","食材名稱","品牌","供應商","認證標章","認證編號","批號"], PAGE_TABLE);
				//show_Ingredient(result_content, ["日期","廚房","學校","菜色名稱","食材名稱","品牌","供應商","認證標章","認證編號","進貨日期","生產日期","有效期限"], PAGE_TABLE);
				$.unblockUI();
			}
		}
		else
		{
			$(".blockUIImgs").hide();
			$(".blockUIMsgs").html("查詢發生錯誤，訊息為：" + result_content.msg);
			$(".blockUIMsgs")
					.after(
							'<input type="button" onclick="$.unblockUI();" value="確定">');
			//MSG.alertMsgs("check", "查詢發生錯誤，訊息為：" + result_content.msg, 0);
			return 0;
		}
	}
	else
	{
		$(".blockUIImgs").hide();
		$(".blockUIMsgs").html("查詢發生無法預期的錯誤，錯誤訊息為：" + result_content.msg);
		$(".blockUIMsgs")
				.after(
						'<input type="button" onclick="$.unblockUI();" value="確定">');
		//MSG.alertMsgs("check", "查詢發生無法預期的錯誤，錯誤訊息為：" + result_content.msg, 0);
		return 0;
	}
	
}
function show_Ingredient(result_content,header,tableName)
{
//清單內容
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<tr bgcolor='#a1c66a' class='TIT_A' align='center'>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr>";
	var td_class="";
	for(var i in result_content.ingredient) {
		if(i%2==0){
			td_class =  "bgcolor='#edf2e5' class='componetContent' align='center'";
		}else{

			td_class =  "bgcolor='#FFFFFF' class='componetContent2' align='center'";
		}
		var menudate = "";
		var kitchenname = "";
		var schoolname = "";
		var dishname = "";
		var ingredientName = "";
		var brand = "";
		var supplierName = "";
		var sourceCertification = "";
		var certificationId = "";
		var lotnumber = "";
		var stockDate = "";
		var manufactureDate = "";
		var expirationDate = "";
		if(result_content.ingredient[i].menudate!=null){menudate=result_content.ingredient[i].menudate;}
		if(result_content.ingredient[i].kitchenname!=null){kitchenname=result_content.ingredient[i].kitchenname;}
		if(result_content.ingredient[i].schoolname!=null){schoolname=result_content.ingredient[i].schoolname;}
		if(result_content.ingredient[i].dishname!=null){dishname=result_content.ingredient[i].dishname;}
		if(result_content.ingredient[i].ingredientName!=null){ingredientName=result_content.ingredient[i].ingredientName;}
		if(result_content.ingredient[i].brand!=null){brand=result_content.ingredient[i].brand;}
		if(result_content.ingredient[i].supplierName!=null){supplierName=result_content.ingredient[i].supplierName;}
		if(result_content.ingredient[i].sourceCertification!=null){sourceCertification=result_content.ingredient[i].sourceCertification;}
		if(result_content.ingredient[i].certificationId!=null){certificationId=result_content.ingredient[i].certificationId;}
		if(result_content.ingredient[i].lotnumber!=null){lotnumber=result_content.ingredient[i].lotnumber;}
		//if(result_content.ingredient[i].stockDate!=null){stockDate=result_content.ingredient[i].stockDate;}
		//if(result_content.ingredient[i].manufactureDate!=null){manufactureDate=result_content.ingredient[i].manufactureDate;}
		//if(result_content.ingredient[i].expirationDate!=null){expirationDate=result_content.ingredient[i].expirationDate;}

		var strTR = "<tr>";
		strTR += "<td "+ td_class +  ">" + menudate + "</td>";
		strTR += "<td "+ td_class +  ">" + kitchenname + "</td>";
		strTR += "<td "+ td_class +  ">" + schoolname + "</td>";
		strTR += "<td "+ td_class +  ">" + dishname + "</td>";
		strTR += "<td "+ td_class +  ">" + ingredientName + "</td>";
		strTR += "<td "+ td_class +  ">" + brand + "</td>";
		strTR += "<td "+ td_class +  ">" + supplierName + "</td>";
		strTR += "<td "+ td_class +  ">" + sourceCertification + "</td>";
		strTR += "<td "+ td_class +  ">" + certificationId + "</td>";
		strTR += "<td "+ td_class +  ">" + lotnumber + "</td>";
		//strTR += "<td "+ td_class +  ">" + stockDate + "</td>";
		//strTR += "<td "+ td_class +  ">" + manufactureDate + "</td>";
		//strTR += "<td "+ td_class +  ">" + expirationDate + "</td>";

		strTR += "</tr>";
		html += strTR;
	}
	$("#" + tableName).html(html);
	$("#" + tableName).hide();
	$("#" + tableName).show("slow");
	$("#MAIN_TITLE_BAR").show("slow");
	$("#tiltleAct").show("slow");
	//處理總頁數與查詢筆數，若總數比查詢筆數大，則顯示查詢筆數
	var preTotalPage = result_content.totalCol;
	if(preTotalPage>$("#queryLimit").val()){
		preTotalPage = $("#queryLimit").val()
	}
//頁數管理	
	var pageHtml = "";
	//資料筆數
	pageHtml  += "資料總數:" +result_content.totalCol +  "筆 ";
	pageHtml  += " | 限制筆數:" +preTotalPage +  "筆 ";
	pageHtml  += " | 第  "+ crnPage +" 頁 | ";
//總共頁數
var totalPage  = 0; //分頁若整除，不需+1

if(preTotalPage % row == 0){
totalPage = parseInt(preTotalPage / row ,10);
}else{
	totalPage = parseInt(preTotalPage / row ,10)+1;}
	pageHtml  += "共 "+ totalPage +" 頁 | ";
//頁面計算
	pageHtml += '前往<select style=" color: #008000; border:none;outline:none;display: inline-block;appearance:none;cursor:pointer;-moz-appearance:none;-webkit-appearance:none;" onchange="query_Ingredient_List(this.value)"> |';
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
	var schoolId = 0;if($("#school_id").length > 0){schoolId = $("#school_id").val();}
	var kitchenId = 0;if($("#kitchen_id").length > 0){kitchenId = $("#kitchen_id").val();}
	var startDate = $("#start_date").val();
	var endDate = $("#end_date").val();
	var dishname ="0" ;
	var ingredientName ="0";
	var brand ="0";
	var supplierName ="0";
	var queryLimit ="0";
	var countyId =0;
	if($("#dishname").val()){dishname = $("#dishname").val();}
	if($("#ingredientName").val()){ingredientName = $("#ingredientName").val();}
	if($("#brand").val()){brand = $("#brand").val();}
	if($("#supplierName").val()){supplierName = $("#supplierName").val();}
	if($("#queryLimit").val()){queryLimit = $("#queryLimit").val();}
	startDate = startDate.replace("/", "-");
	startDate = startDate.replace("/", "-");
	
	endDate = endDate.replace("/", "-");
	endDate = endDate.replace("/", "-");
	
	var link = "/cateringservice/rest/API/XLS/ingredientlist&" +
	startDate + "&" + 
	endDate + "&" + 
	kitchenId + "&" + 
	schoolId + "&" + 
	countyId + "&" + 
	queryLimit + "&" + 
	dishname + "&" + 
	ingredientName + "&" + 
	brand + "&" + 
	supplierName;
	
	//alert(link);
	
	window.open(link,"_blank");
}
