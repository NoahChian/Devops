<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="util.jsp" %> <!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>

	//calendar
	$(function() {
    	$( "#start_date" ).datepicker({ dateFormat: "yy/mm/dd" });
    	$( "#start_date1" ).datepicker({ dateFormat: "yy/mm/dd" });
    	$( "#end_date" ).datepicker({ dateFormat: "yy/mm/dd" });
    	
    	//$( "#start_date" ).attr("value", "2013/12/01");
    	//$( "#end_date" ).attr("value", "2013/12/31");
  	});
	
	function querySchool(){
		//request
		
		var ciid = <%=session.getAttribute("account")%>;
		//alert(ciid);
		
		var request_data =	{
							 "method":"querySchool",
				 				"args":{	
				 					"ciid":ciid
					 			}
							};
		var response_obj = call_rest_api(request_data);
		
		//alert(response_obj);
		
		//response
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				//school
				//Ric 20140423 return to manageKitchen if school is null
				if(result_content.school.length==0){
					alert("請於「業者資料管理」新增供餐學校! \n 並確認新增後有點選「確認」");
					location.href = "../manageKitchen/"; 
					return false;
				}
				var new_drop = "<select id=\"school_id\" class=\"form-control\" style=\"margin-bottom: 16px;\">";
				for(var i=0; i<result_content.school.length; i++) {
					if(result_content.school[i].sid == "1837478") {
						new_drop += "<option selected value=" + result_content.school[i].sid + ">" + result_content.school[i].schoolName + "</option>";
						continue;
					}
					new_drop += "<option value=" + result_content.school[i].sid + ">" + result_content.school[i].schoolName + "</option>";
				}
				new_drop += "</select>";
				$("#dropDown_school").append(new_drop);
				
				var new_drop1 = "<select id=\"school_id1\" class=\"form-control\" style=\"margin-bottom: 16px;\">";
				for(var i=0; i<result_content.school.length; i++) {
					if(result_content.school[i].sid == "1837478") {
						new_drop1 += "<option selected value=" + result_content.school[i].sid + ">" + result_content.school[i].schoolName + "</option>";
						continue;
					}
					new_drop1 += "<option value=" + result_content.school[i].sid + ">" + result_content.school[i].schoolName + "</option>";
				}
				new_drop1 += "</select>";
				$("#dropDown_school1").append(new_drop1);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}
	
	function copy(){
		//copy to
		$("#copy_to").show("slow");
		$("#search").hide();
		
		//狀態
		var status = $("#copy").attr("status");
  		
  		//清空開始與結束日期
  		//$("#start_date").val("");
  		//$("#end_date").val("");
  		$("#school_id").attr("disabled", "disabled");
  		$("#start_date").attr("disabled", "disabled");
  		$("#end_date").attr("disabled", "disabled");
		
		//alert($("#copy").attr("status"));
  		
		if(status == 0){
			//$("#copy").attr("sidOld", schoolId);
			//$("#copy").attr("startDateOld", startDate);
			//$("#copy").attr("endDateOld", endDate);
			$("#copy").attr("value","貼上");
			$("#copy").attr("status", "1");
		} else if (status == "1") {
			//抓old的資料
			//var sidOld = $("#copy").attr("sidOld");
			//var startDateOld = $("#copy").attr("startDateOld");
			//var endDateOld = $("#copy").attr("endDateOld");
			var sidOld = $("#school_id").val();
			var startDateOld = $("#start_date").val();
			var endDateOld = $("#end_date").val();
			var schoolId = $("#school_id1").val();
			var	startDate = $("#start_date1").val();
			var endDate = "2013/12/31";
			
			//回覆原始設定
			$("#school_id").removeAttr('disabled');
			$("#end_date").removeAttr('disabled');
			$("#start_date").removeAttr('disabled');
			$("#search").show("slow");
			$("#copy_to").hide("slow");
			$("#copy").attr("status", "0");
			$("#copy").attr("value", "複製");
			
			//alert(sidOld +","+ startDateOld +","+ endDateOld +","+ schoolId +","+ startDate +","+ endDate);return;
			
			//request
			var request_data = {
								 "method":"copyMenu",
					 				"args":{	
					 					"sidOld": sidOld,				//sidOld
					 					"startDateOld": startDateOld,	//startDateOld
					 					"endDateOld": endDateOld,		//endDateOld
					 					"sidNew": schoolId,				//schoolId
					 					"startDateNew": startDate,		//startDate
					 					"endDateNew": endDate			//endDate
						 			}
								};
			//alert(sidOld +","+ startDateOld +","+ endDateOld +","+ schoolId +","+ startDate +","+ endDate);
			
			//回復原始設定
			//$("#copy").attr("sidOld", "");
			//$("#copy").attr("startDateOld", "");
			//$("#copy").attr("endDateOld", "");
			
			
			
			var response_obj = call_rest_api(request_data);
			
			if(response_obj.result == 1)
			{
				var result_content = response_obj.result_content;
				if(result_content.resStatus == "1"){	//success
					alert("貼上成功");
				} else {
					alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
					return;
				}
			} else{
				alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
				return;
			}
		}
	}
	
	function showExcelUpload() {	//上傳EXCEL按鈕
		$("#excel_upload").show("slow");
		//$("#query_table").hide();
		$("#excelUpload").hide();
		//$("#menu_list").hide();
		
	}
	
	function export_excel(){
		var schoolId = $("#export").attr("schoolId");
		var startDate = $("#export").attr("startDate");
		var endDate = $("#export").attr("endDate");
		
		startDate = startDate.replace("/", "-");
		startDate = startDate.replace("/", "-");
		
		endDate = endDate.replace("/", "-");
		endDate = endDate.replace("/", "-");
		
		var link = "/cateringservice/rest/API/XLS/menu&" + schoolId + "&" + startDate + "&" + endDate;
		
		//alert(link);
		
		window.open(link,"_blank");
	}
	
	function page(now, last){
		
		$("#page").html("");
		
		for(var i=1; i<=5; i++){	//hide all
			$("#menu_list" + i).hide();
		}
		
		$("#menu_list" + now).show("slow");
		
		if(last == 2){
			if(now == 1){
				$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">第一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">1</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">2</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">下一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">最末頁</a>");
			} else if (now == 2){
				$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">第一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">上一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">1</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">2</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">最末頁</a>");
			}
		} else {
			if(now == last){
				$("#page").append("<a href=\"#\" onClick=\"page(1,"+last+")\">第一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now-1)+","+last+")\">上一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now-2)+","+last+")\">"+(now-2)+"</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now-1)+","+last+")\">"+(now-1)+"</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+now+","+last+")\">"+now+"</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+last+","+last+")\">最末頁</a>");
			} else if (now == 1){
				$("#page").append("<a href=\"#\" onClick=\"page(1,"+last+")\">第一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now)+","+last+")\">"+(now)+"</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now+1)+","+last+")\">"+(now+1)+"</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now+2)+","+last+")\">"+(now+2)+"</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now+1)+","+last+")\">下一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+last+","+last+")\">最末頁</a>");
			} else {
				$("#page").append("<a href=\"#\" onClick=\"page(1,"+last+")\">第一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now-1)+","+last+")\">上一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now-1)+","+last+")\">"+(now-1)+"</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+now+","+last+")\">"+now+"</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now+1)+","+last+")\">"+(now+1)+"</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+(now+1)+","+last+")\">下一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page("+last+","+last+")\">最末頁</a>");
			}
		}
		
	}
	
	//產生查詢列表
	function createMenuList() {
		
		
		//request
		var schoolId = $("#school_id").val();
		var startDate = $("#start_date").val();
		var endDate = $("#end_date").val();
		
		//檢查查詢期間不得大於90天       20140219 KC
		if (!util_check_date_range($("#start_date").datepicker("getDate"),$("#end_date").datepicker("getDate"))){
			return;
		}
		
		$("#page").html("");
		$("#menu_list1").html("");
		$("#menu_list2").html("");
		$("#menu_list3").html("");
		$("#menu_list4").html("");
		$("#menu_list5").html("");
		
		var row = 20;
		
		//alert(schoolId +","+ startDate +","+ endDate);
		
		var request_data =	{
								 "method":"queryMenuBySchoolAndTime",
					 				"args":{	
					 					"sid":schoolId,
					 					"startDate":startDate,
					 					"endDate":endDate
						 			}
								};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1) {
			
			//$('.TITLE_TXT flo_l').attr("value","查詢結果  - 查詢菜單筆數：" + response_obj.result_content.menu.length);
			var element=document.getElementById("query_rule");
			element.innerHTML = "查詢結果(20筆/頁)  - 查詢菜單筆數：" + response_obj.result_content.menu.length;
			
			
			var total_page = parseInt(response_obj.result_content.menu.length / row);
			var add = response_obj.result_content.menu.length % row;
			
			if(add > 0) total_page++;
			
			if(total_page == 2) {
				$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">第一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(1,2)\">1</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">2</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">下一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,2)\">最末頁</a>");
			} else if (total_page == 3) {
				$("#page").append("<a href=\"#\" onClick=\"page(1,3)\">第一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(1,3)\">1</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,3)\">2</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(3,3)\">3</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,3)\">下一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(3,3)\">最末頁</a>");
			} else if (total_page == 4) {
				$("#page").append("<a href=\"#\" onClick=\"page(1,4)\">第一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(1,4)\">1</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,4)\">2</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(3,4)\">3</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,4)\">下一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(4,4)\">最末頁</a>");
			} else if (total_page == 5) {
				$("#page").append("<a href=\"#\" onClick=\"page(1,5)\">第一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(1,5)\">1</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,5)\">2</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(3,5)\">3</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(2,5)\">下一頁</a>");
				$("#page").append("<a href=\"#\" onClick=\"page(5,5)\">最末頁</a>");
			}
		
			//組表格
			var head_div = "<table class=\"table table-bordered table-striped\">";
			head_div += "<thead>";
				//head_div += "<td colspan=\"14\">";
				//head_div += "<input type=\"submit\" id=\"copy\" status=\"0\" sidOld=\"\" startDateOld=\"\" endDateOld=\"\" onclick=\"copy()\" value=\"複製\">";
				//head_div += " <input type=\"button\" id=\"export\" onclick=\"export_excel()\" schoolId=\"" + schoolId + "\" startDate=\""+ startDate 
					//+"\" endDate=\"" + endDate + "\" value=\"匯出\">";
			//head_div += "</tr>";
			head_div += "<tr>";
				head_div +=	"<td>日期</td>" +
							"<td>學校</td>" +
							"<td>主食</td>" +
							"<td>主菜</td>" +
							"<td>主菜一</td>" +
							"<td>主菜二</td>" +
							"<td>副菜一</td>" +
							"<td>副菜二</td>" +
							"<td>副菜三</td>" +
							//"<td>副菜四</td>" +
							"<td>蔬菜</td>" +
							"<td>湯品</td>" +
							"<td>熱量</td>" +
							"<td>選項</td>";
				head_div += "</tr></thead>";
			
			var result_content = response_obj.result_content;
			
			
			if(result_content.resStatus == "1"){	//success
				if(result_content.menu.length == 0){
					$("#menu_list").hide();
					alert("該學校在此時間區間的資料不存在");
					return;
				} else {
					var new_div = "";
					
					
					var today=new Date();
					var limitDate=new Date(today.getFullYear()+"/"+(today.getMonth()+1)+"/"+today.getDate()+" "+today.getHours()+":"+today.getMinutes()+":00");				
					var act_html="";
			
					
					for(var i=0; i<result_content.menu.length; i++) {
						//判斷是否超過上傳時間，超過者不給修改鈕   20140220 KC
						var itemDate=new Date(result_content.menu[i].menuDate+" "+ENV_UPLOAD_DAY_LIMIT);
						
						if (itemDate.getTime()<limitDate.getTime()){
							act_html="";
						}else{
							act_html="<a class=\"btn btn-primary\" title=\"修改\" style=\"min-width: 37px; margin: 0;\" href=\"../singleSch_detail/?menuDate="+ result_content.menu[i].menuDate +"&schoolId="+schoolId+"&schoolName="+result_content.menu[i].schoolName+"\"><i class=\"fa fa-pencil\"></i></a>";
						}
						
						if(i % 2 == 0) {
							new_div += "<tr>";							
							new_div += "<td>" + result_content.menu[i].menuDate + "</td>" +
									   "<td>" + result_content.menu[i].schoolName + "</td>" +
									   "<td>" + result_content.menu[i].main + "</td>" +
									   "<td>" + result_content.menu[i].major + "</td>" +
									   "<td>" + result_content.menu[i].major1 + "</td>" +
									   "<td>" + result_content.menu[i].major2 + "</td>" +
									   "<td>" + result_content.menu[i].side1 + "</td>" +
									   "<td>" + result_content.menu[i].side2 + "</td>" +
									   "<td>" + result_content.menu[i].side3 + "</td>" +
									   //"<td>" + result_content.menu[i].side4 + "</td>" +
									   "<td>" + result_content.menu[i].vegetable + "</td>" +
									   "<td>" + result_content.menu[i].soup + "</td>" +
									   "<td>" + result_content.menu[i].calorie + "</td>" +
									   "<td>" + 
									   //"<a href=\"../singleSch_detail/?menuDate="+ result_content.menu[i].menuDate +"&schoolId="+schoolId+"&schoolName="+result_content.menu[i].schoolName+"\">修改</a>"+
									   act_html+
											   
											   //"<a href=\"#\" onclick=delete_menu('"+result_content.menu[i].mid+"')>刪除</a>"+
									   "</td>";
							new_div += "</tr>";
						} else {
							new_div += "<tr>";
							new_div += "<td>" + result_content.menu[i].menuDate + "</td>" +
									   "<td>" + result_content.menu[i].schoolName + "</td>" +
									   "<td>" + result_content.menu[i].main + "</td>" +
									   "<td>" + result_content.menu[i].major + "</td>" +
									   "<td>" + result_content.menu[i].major1 + "</td>" +
									   "<td>" + result_content.menu[i].major2 + "</td>" +
									   "<td>" + result_content.menu[i].side1 + "</td>" +
									   "<td>" + result_content.menu[i].side2 + "</td>" +
									   "<td>" + result_content.menu[i].side3 + "</td>" +
									   //"<td>" + result_content.menu[i].side4 + "</td>" +
									   "<td>" + result_content.menu[i].vegetable + "</td>" +
									   "<td>" + result_content.menu[i].soup + "</td>" +
									   "<td>" + result_content.menu[i].calorie + "</td>" +
									   "<td>" + 
									   act_html+
									   //"<a href=\"../singleSch_detail/?menuDate="+ result_content.menu[i].menuDate +"&schoolId="+schoolId+"&schoolName="+result_content.menu[i].schoolName+"\">修改</a>"+
									   //" <a href=\"#\" onclick=delete_menu('"+result_content.menu[i].mid+"')>刪除</a>"+
									   "</td>";
							new_div += "</tr>";
						}
						
						//每20筆放入不同DIV
						if((i+1) % row == 0){
							var page = (i+1) / row;
							if(page == 1){
								$("#menu_list1").append(head_div + new_div + "</table>");
								new_div = "";
							} else if (page == 2) {
								$("#menu_list2").append(head_div + new_div + "</table>");
								new_div = "";
							} else if (page == 3) {
								$("#menu_list3").append(head_div + new_div + "</table>");
								new_div = "";
							} else if (page == 4) {
								$("#menu_list4").append(head_div + new_div + "</table>");
								new_div = "";
							} else if (page == 5) {
								$("#menu_list5").append(head_div + new_div + "</table>");
								new_div = "";
							}
						}
						if((i+1) == result_content.menu.length && result_content.menu.length % row != 0){
							var menu = "menu_list" + total_page;
							$('#'+menu).append(head_div + new_div + "</table>");
							new_div = "";
						}
					}
				}
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
				return;
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			return;
		}
	
		//new_div +="</table>";
		
		/*** 2.將新增的資源計價內容附加上去 ***/
		$("#menu_list").html("");
		//$("#menu_list").append(new_div);
		$("#menu_list1").show();
		$("#menu_list2").hide();
		$("#menu_list3").hide();
		$("#menu_list4").hide();
		$("#menu_list5").hide();
		$("#queryList").hide();
	}
	
	
	function query_menu_detail_info(mid) {
		
		$("#menu_list1").hide();
		$("#menu_list2").hide();
		$("#menu_list3").hide();
		$("#menu_list4").hide();
		$("#menu_list5").hide();
		
		var request_data =	{
				 "method":"queryMenuDetailInfo",
	 				"args":{	
	 					"mid":mid
		 			}
				};
		
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == "1"){	//success
				$("#school_name").text(result_content.schoolName);
				$("#menu_date").text(result_content.menuDate);
				$("#main_food").attr("value", result_content.main);
				$("#main_food2").attr("value", result_content.main2);
				$("#major_food").attr("value", result_content.major);
				$("#major1").attr("value", result_content.major1);
				$("#major2").attr("value", result_content.major2);
				$("#major3").attr("value", result_content.major3);
				$("#side1").attr("value", result_content.side1);
				$("#side2").attr("value", result_content.side2);
				$("#side3").attr("value", result_content.side3);
				$("#side4").attr("value", result_content.side4);
				$("#side5").attr("value", result_content.side5);
				$("#side6").attr("value", result_content.side6);
				$("#vegetable_name").attr("value", result_content.vegetableName);
				$("#soup").attr("value", result_content.soup);
				$("#meals").attr("value", result_content.meals);
				$("#meals2").attr("value", result_content.meals2);
				$("#calorie").attr("value", result_content.calorie);
				$("#vegetable").attr("value", result_content.vegetable);
				$("#oil").attr("value", result_content.oil);
				$("#valley").attr("value", result_content.grains);
				$("#meat").attr("value", result_content.meatBeans);
				$("#fruit").attr("value", result_content.fruit);
				$("#milk").attr("value", result_content.milk);
				$("#update_menu_button").attr("mid", mid);
				$("#queryList").show();
				$("#menu_list").hide("slow");
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
				return;
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			return;
		}
	}
	
	function delete_menu(mid){
		var answer = confirm("請問是否要刪除菜單？");
    	if (answer) {
    		//request
    		var request_data =	{
    							 "method":"deleteMenuInfo",
    				 				"args":{	
    				 					"mid":mid
    					 			}
    							};
    		var response_obj = call_rest_api(request_data);
    		
    		if(response_obj.result == 1)
    		{
    			var result_content = response_obj.result_content;
    			if(result_content.resStatus == 1){	//success
    				
    				createMenuList();	//重新查詢
    				
    				$("#queryList").hide();
    				$("#menu_list").show();
    				alert("菜單刪除成功");
    			} else {
    				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
    			}
    		} else{
    			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
    		}
    	} else {
    		return;
    	}

	}
	
	function uploadFile(){
		alert("上傳成功");
		
		$("#excel_upload").hide("slow");
		$("#excelUpload").show("slow");
	}
	
	function update_menu(){
		
		if($("#main_food").val() == "") {
			alert("請填入主食一");
			return;
		} else if ($("#major_food").val() == "") {
			alert("請填入主菜");
			return;
		/*} 
		
		else if ($("#side1").val() == "") {
			alert("請填入副菜一");
			return;
		} else if ($("#vegetable_name").val() == "") {
			alert("請填入蔬菜");
			return;
		} else if ($("#soup").val() == "") {
			alert("請填入湯品");
			return;*/
		} else if ($("#valley").val() == "") {
			alert("請填入全穀根莖類/份");
			return;
		} else if ($("#vegetable").val() == "") {
			alert("請填入蔬菜類/份");
			return;
		} else if ($("#oil").val() == "") {
			alert("請填入油脂及堅果類/份");
			return;
		} else if ($("#meat").val() == "") {
			alert("請填入豆魚肉蛋類/份");
			return;
		} else if ($("#fruit").val() == "") {
			alert("請填入水果類/份");
			return;
		} else if ($("#milk").val() == "") {
			alert("請填入奶類/份");
			return;
		} else if ($("#calorie").val() == "") {
			alert("請填入熱量/大卡");
			return;
		}
		
		
		var mid = $("#update_menu_button").attr("mid");
		
		var request_data =	{
				 "method":"updateMenuDetailInfo",
	 				"args":{	
	 					"mid": mid,
	 					"main": $("#main_food").val(),
	 					"main2": $("#main_food2").val(),
	 					"major": $("#major_food").val(),
	 					"major1": $("#major1").val(),
	 					"major2": $("#major2").val(),
	 					"major3": $("#major3").val(),
	 					"side1": $("#side1").val(),
	 					"side2": $("#side2").val(),
	 					"side3": $("#side3").val(),
	 					"side4": $("#side4").val(),
	 					"side5": $("#side5").val(),
	 					"side6": $("#side6").val(),
	 					"vegetableName": $("#vegetable_name").val(),
	 					"soup": $("#soup").val(),
	 					"meals": $("#meals").val(),
	 					"meals2": $("#meals2").val(),
	 					"calorie": $("#calorie").val(),
	 					"grains": $("#valley").val(),
	 					"vegetable": $("#vegetable").val(),
	 					"oil": $("#oil").val(),
	 					"meatBeans": $("#meat").val(),
	 					"fruit": $("#fruit").val(),
	 					"milk": $("#milk").val()
		 			}
				};
		
		var response_obj = call_rest_api(request_data);

		//response
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				
				createMenuList();	//重新查詢
				
				$("#queryList").hide();
				$("#menu_list").show();
				alert("菜單修改成功");
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}

	var client = new XMLHttpRequest();
	function upload(){
		var file = document.getElementById("file");
	    /* Create a FormData instance */
	    var formData = new FormData();
	    
	    /* Add the file */ 
	    formData.append("file", file.files[0]);
	    formData.append("func", $( "#func" ).val());
	    formData.append("overWrite", $("#overWrite").val());
	    client.open("post","/cateringservice/file/upload", true);
	    client.send(formData);  /* Send to server */ 
	}
	   
	/* Check the response status */  
	client.onreadystatechange  = function() {
		if (client.readyState == 4 && client.status == 200) {
			
			//alert(client.responseText);
			
			var obj = JSON.parse(client.responseText);

	         
	        if(obj.retStatus == 1){
	        	 alert("檔案上傳成功");
	        	 $("#file").val("");
	        	 $("#overWrite").attr("value", 0);
	        } else if (obj.retStatus == 0) {
	        	 alert("檔案上傳失敗，原因為" + obj.retMsg);
	        	 $("#file").val("");
	        	 $("#overWrite").attr("value", 0);
	        } else if (obj.retStatus == -1) {
				var answer = confirm("菜單重複，請問是否要覆蓋？");
	        	if (answer) {
	        		$("#overWrite").attr("value", 1);
	        		upload();
	        	} else {
	        		$("#file").val("");
	        		$("#overWrite").attr("value", 0);
	        		return;
	        	}
	    	}
	    }
		
		$("#excel_upload").hide("slow");
		$("#excelUpload").show("slow");
	}
	
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

	$(document).ready(function(){	
		$("#excel_upload").hide();
		$("#queryList").hide();
		$("#copy_to").hide();
		querySchool();	//查詢學校
		defaultYearMonth();
	});
</script>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
</head>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">單一學校食材維護</div>
		<div class="contents-wrap">
	        <div class="section-wrap has-border">
	          <div class="form-horizontal kitchen-info">
				<div id="query_table" style="margin-bottom: 16px;">
					<div class="dis_intb LOGIN_US_PS">
						<h6 style="margin: 0;">學校：</h6>&nbsp;<span id="dropDown_school">
					</div>
					<h6>請輸入日期區間查詢：</h6>
	             	<div class="inline-wrap datetimepicker-start" style="width: 30%">
                        <input type="text" class="form-control inline datetimepicker" id="start_date">
                    </div>
                    <div class="inline-wrap datetimepicker-to" style="margin-bottom: 2px">to</div>
                    <div class="inline-wrap datetimepicker-end" style="width: 30%; margin-right: 5px;">
                    	<input type="text" class="form-control inline datetimepicker" id="end_date">
                    </div>
                    <input class="btn btn-primary" style="margin: 0 0 2px 0" onclick="createMenuList()" id="search" value="查詢" type="button">	
				</div>
				<div id="copy_to">
					<div class="dis_intb LOGIN_US_PS">
						<h6 style="margin: 0;">學校：</h6>&nbsp;<span id="dropDown_school1">
					</div>
					<div class="inline-wrap datetimepicker-start" style="width: 30%">
                        <input type="text" class="form-control inline datetimepicker" id="start_date1">
                    </div>
				</div>	
				      
				<div id="menu_list1"></div>
				<div id="menu_list2"></div>
				<div id="menu_list3"></div>
				<div id="menu_list4"></div>
				<div id="menu_list5"></div>		
				<div style="color:red"><br>！提醒您，於單一食材維護頁面中所編輯的食材，僅顯示於所選擇之學校，其餘學校不受影響。<br>修改後若於食材資料維護頁面進行更新，則此頁之修改會受到覆蓋。</div>	
				<div id="queryList" class="TAB_TY_B">
				</div>
	          </div>
	        </div><!-- End of .section-wrap -->        
	    </div>
       
	<!-- 舊團膳套版 -->
	<div style="display:none">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
			<div id="query_rule" class="TITLE_TXT flo_l">請輸入查詢條件</div>
			<div class="TITLE_TXT_BBT FL_R">
				<div id="page"></div>
			</div>
		</div>
		<div id="query_table" class="h_35px lh_35 GRA_ABR">
				<div class="dis_intb LOGIN_US_PS">
					學校&nbsp;<span id="dropDown_school">
				</div>
				<div class="dis_intb LOGIN_US_PS">
					日期(開始)&nbsp;<input type="text" id="start_date" class="dateMode"/>
				</div>
				<div class="dis_intb LOGIN_US_PS">
					日期(結束)&nbsp;<input type="text" id="end_date"  class="dateMode"/>
				</div>
				<div class="dis_intb mgl_10 BT_IN_BBTER">
					&nbsp;&nbsp;<input onclick="createMenuList()" id="search" value="查詢" type="button">
				</div>
		</div>
		
		<div id="copy_to">
				<div class="dis_intb LOGIN_US_PS">
					學校&nbsp;<span id="dropDown_school1">
				</div>
				<div class="dis_intb LOGIN_US_PS">
					日期(開始)&nbsp;<input type="text" id="start_date1" class="dateMode"/>
				</div>
		</div>	
		      
		<div id="menu_list1"></div>
		<div id="menu_list2"></div>
		<div id="menu_list3"></div>
		<div id="menu_list4"></div>
		<div id="menu_list5"></div>		
		<div style="color:red"><br>！提醒您，於單一食材維護頁面中所編輯的食材，僅顯示於所選擇之學校，其餘學校不受影響。<br>修改後若於食材資料維護頁面進行更新，則此頁之修改會受到覆蓋。</div>	
		<div id="queryList" class="TAB_TY_B">
		</div>
	</div>
</body>
</html>