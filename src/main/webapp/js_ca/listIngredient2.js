var MSG= new MsgsProcessing();
var util=new CateringServiceUtil();
var PAGE_TABLE="resultTable";
var row = 100; //每頁筆數
var crnPage = 1;//當前頁數
var totalCount = 0; //總筆數
var headerObjAry=[
  {text:"日期",key:"menudate",width:"100"},
  {text:"廚房",key:"kitchenname"},
  {text:"學校",key:"schoolname"},
  {text:"菜色名稱",key:"dishname"},
  {text:"食材名稱",key:"ingredientName"},
  {text:"品牌",key:"brand"},
  {text:"供應商",key:"supplierName"},
  {text:"認證標章",key:"sourceCertification"},
  {text:"認證編號",key:"certificationId"},  
  {text:"批號",key:"lotNumber"}
];
function queryParamObj(){
	this.supplierName="";
	this.brand="";
	this.ingredientName="";
}


$(document).ready(function(){
	$( "#start_date" ).datepicker({ dateFormat: "yy/mm/dd" });
	$( "#end_date" ).datepicker({ dateFormat: "yy/mm/dd" });
});


function defaultYearMonth(){
	var dateRangeObj = new CateringServiceDateRangeObj();
	dateRangeObj=util.getDateRange("30days");
	$( "#start_date" ).datepicker("setDate",dateRangeObj.startDate);
	$( "#end_date" ).datepicker("setDate",dateRangeObj.endDate);
}

function addCondObj(){
	var obj=$(".tempelateCondObj").clone().removeClass("tempelateCondObj").css("display","inline").appendTo("#queryCond");
	$(obj).find(".deleteButton").css("display","");
	$(obj).find("input[type=text]").val("")
}
function deleteCond(obj){
	$(obj).parent().parent().remove();
}

//送出查詢
function query_Ingredient_List(page)
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
					"brand":brand,
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

function submitQuery(func){
	var queryCondAry=[];
	$("#queryCond").find(".cloneCondObj").each(function(){
		var p_suppliername=$(this).find(".supplierName").val();
		var p_brandname=$(this).find(".brandName").val();
		var p_ingredname=$(this).find(".ingredientName").val();
		if (p_suppliername=="" && p_brandname =="" && p_ingredname==""){
			return;
		}
		var param=new queryParamObj();
		param.supplierName=p_suppliername;
		param.brand=p_brandname;
		param.ingredientName=p_ingredname;
		queryCondAry.push(param);
	});
	var startDate=$("#start_date").val();
	var endDate=$("#end_date").val();
	var request_data =	{
			"method":"queryIngredientList2",
			"args":{
				"begDate":startDate, 
				"endDate":endDate, 
				"cond":queryCondAry,
				"page":crnPage,
				"perpage":row,
				"func":func
			}
		};
	//var response_obj = call_rest_api(request_data);
	MSG.alertMsgs("loading", "產生資料中，請稍後....", 0);
	$.ajax({
		  type: "POST",
		  async:false,
		  contentType:"application/json",
		  url: "/cateringservice/rest/API/",
		  data: JSON.stringify(request_data),
		  datatype:"json",
		  success:function(response){
			  var fileUrl='/cateringservice/file/USER/report/'+response["result_content"]["fileKey"]+"/";
			  if (func=="file"){
				 $("#fileDownload").attr('src',fileUrl);
				 //MSG.alertMsgs("confirm","檔案已成功匯出，如無自動下載，請點選<a href='"+fileUrl+"'>此處下載檔案</a>",2);
			  }else{
				util.setTableWithHeaderIndex(response["result_content"]["ingredient"], headerObjAry, PAGE_TABLE);
				totalCount=response["result_content"]["totalCol"];
				$("#lblTotalCount").html(totalCount);
				setPageList(totalCount,"ddlPage",row);
				//MSG.alertMsgs("unblock", "", 0);
			  }
			  MSG.alertMsgs("unblock", "", 0);
		  },
		  error:function(response){
			  MSG.alertMsgs("confirm","查詢失敗，請洽系統管理員",2);
		  }
	}); 


}

// 按下送出查詢時設定顯示頁面為第一頁
function initSubmitQuery(func){
	crnPage = 1;
	submitQuery(func);
}

// 檢核是否需要匯出(筆數大於0)
function chkAndExport(func) {
	if (totalCount > 0) {
		submitQuery(func);
	} else {
		MSG.alertMsgs("check", "查詢總筆數為0，無需匯出", 0);
	}
}

function setPageList(totalCount,ddlName,limitCount){
	$("#"+ddlName).empty();
	var totalPage=totalCount/limitCount;
	if(totalCount % limitCount >0){
		totalPage=totalPage+1;
	}
	for (var i =1; i<=totalPage;i++){
		$("#"+ddlName).append("<option value='"+i+"'>"+i+"</option>");
	}
	$("#"+ddlName).val(crnPage);
}
function changePage(obj){
	crnPage=$(obj).val();
	submitQuery();
}
