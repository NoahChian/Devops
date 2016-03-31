/**
 * for managelist page
 */
var util=new CateringServiceUtil();
var PAGE_TABLE="resultTable";
$(document).ready(function(){	
	showmode();
});

//2014/06/13 Ric 轉到common.js
/*function defaultYearMonthRange(dateRangeType) {
	var rangeType="7days";
	if (typeof(dateRangeType)!="undefined"){
		rangeType=dateRangeType;
	}
	
	var rangeDate=new CateringServiceDateRangeObj();
	rangeDate=util.getDateRange(rangeType);
	$( "#start_date" ).datepicker({ dateFormat: "yy/mm/dd" }).datepicker( "setDate", rangeDate.startDate);
	$( "#end_date" ).datepicker({ dateFormat: "yy/mm/dd" }).datepicker( "setDate", rangeDate.endDate);
	//$( "#start_date" ).datepicker({ dateFormat: "yy/mm/dd" }).datepicker( "setDate", new Date() );
	//$( "#end_date" ).datepicker({ dateFormat: "yy/mm/dd" }).datepicker( "setDate", new Date() );
}
*/
//執行查詢

function runFunction() {
	 var funcType=$("#ddlQueryType").val();
	 var manageDateStart = $("#start_date").val();
	 var manageDateEnd=$("#end_date").val();
	 var schoolId=$("#ddlSchool").val();
	 switch (funcType){
		 case "countSchoolMenu":
			 createMenudataList(manageDateStart,funcType);
			 break;
		 case "countKitchenMenu":
			 createMenudataList(manageDateStart,funcType);
			 break;
		 case "countMenuNonIngre":
			 //var schoolId = $("#ddlSchool").val();
			 createNullIngredientList(manageDateStart,manageDateEnd,0);
			 break;
		 default:
			 getStatistic(funcType,manageDateStart,manageDateEnd,19);
			 
	 }
}
function createMenudataList(manageDate,manageFunc){
	if(manageDate==""|| manageDate == null){
		alert("請選擇日期");
		return;
	}
	var request_data = {
			"method":"queryManageList",
		    "args": {
		    	"manageDate" : manageDate,
				"manageFunc" : manageFunc, 				
		}
	};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			util.setTableWithHeader(result_content.returnList, ["業者/學校名稱"], PAGE_TABLE);
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
}

function createNullIngredientList(startDate,endDate,schoolId) {

		//request
		var schoolId = $("#ddlSchool").val();
		var startDate = $("#start_date").val();
		var endDate = $("#end_date").val();

		if (!util.util_check_date_range($("#start_date").datepicker("getDate"), $("#end_date").datepicker("getDate"))) {
			return;
		}


		var request_data = {
			"method" : "queryNullIngredientBySchoolAndTime",
			"args" : {
				//"sid" : schoolId,
				"startDate" : startDate,
				"endDate" : endDate
			}
		};
		var response_obj = call_rest_api(request_data);

		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == "1") { //success
				if (result_content.nullIngreduentList.length == 0) {
					alert("在此時間區間的資料不存在");
					return;
				} else {
					util.setTableWithHeader(result_content.nullIngreduentList, ["學校","日期","無食材菜色","供餐者"], PAGE_TABLE);
					
				}
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
				return;
			}
		} else {
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			return;
		}

	}
function getStatistic(queryType,startDate,endDate,county){

	var request_data =	{
			 "method":"QueryStatistic",
 				"args":{
					"queryItem":queryType,
					"startDate":startDate,
					"endDate":endDate,
					"county":county
 				}
			};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == "1"){
		var th=response_obj["result_content"]["header"];
		var total=response_obj["result_content"]["total"];
		var list=response_obj["result_content"]["result"];
		//printTableWithHeader(list,th,total);
		util.setTableWithHeader(list,th,PAGE_TABLE);
		util.setTableTail(PAGE_TABLE,"總數",total);
	}else{
		alert("查詢無資料");
	}

}
function querySchool() {
		//request
		//alert(ciid);
		var request_data = {
			"method" : "querySchoolList",
			"args" : {
				"cid" : "19"	
			}
		};
		var response_obj = call_rest_api(request_data);

		//response
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { //success
				util.setDropdownlist(result_content["school"], "ddlSchool", "schoolName", "sid");
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else {
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}
function showmode(){
	var manageFunc=$("#ddlQueryType").val();
	$("#divQueryCond").children().hide();
	
	switch (manageFunc){
	case "countSchoolMenu":
		$(".class_orgi").show();
		break;
	case "countKitchenMenu":
		$(".class_orgi").show();
		break;
	case "countMenuNonIngre":
		$(".class_countMenuNonIngre").show();
		//querySchool(); //查詢學校
		break;
	case "dishCount":
		break;
	case "ingredientCount":
		break;
	default:
		$(".class_statistic").show();
		
	}
	defaultYearMonthRange();
}

function change_date_range(){
	defaultYearMonthRange($("#ddlDateRange").val());
}