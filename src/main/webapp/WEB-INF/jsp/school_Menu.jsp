<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="util.jsp" %>    <!-- include js的自訂公用函式    20140219 KC -->
<%
String uType = "";
String uName = "";
String account = "";
String StrName = "";
String roletype = "";
String county = "";
if(session.getAttribute("account")!=null){
uType = (String) session.getAttribute("uType");
uName = (String) session.getAttribute("uName");
account = (String) session.getAttribute("account");
StrName = (String) session.getAttribute("StrName");
roletype = (String) session.getAttribute("roletype");
county = (String) session.getAttribute("county");}
%>

<script>
	var utype="<%=uType %>";
	var MSG= new MsgsProcessing();

	/**
		2014/10/27  add by Joshua
		輸入欄位過濾掉特殊字元
	*/
// 	document.onchange = checkSpecialCharacters;
	function checkSpecialCharacters(){
		var inputFields = $("#dataList").find("input[type=text]");
		var inputFieldsCount = $("#dataList").find("input[type=text]").length;
		for(i = 0; i < inputFieldsCount ; i++){
			if(!isValidStr(inputFields[i].value)){
				alert('欄位名稱不可包含特殊字元 \" > < \' % ; &');
				return true;
			}
		}
	}
	
	//add by Joshua 2014/11/10 特殊字元判斷
	 function isValidStr(str){
		return !/[%&\[\]\\';|\\"<>]/g.test(str);
	 }
	
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
					alert("請於「廚房基本資料維護」新增供餐學校! \n 並確認新增後有點選「確認」");
					location.href = "../manageKitchen/"; 
					return false;
				}
				var new_drop = "<select id=\"school_id\" class='form-control' style=\"margin-bottom: 16px;\">";
				for(var i=0; i<result_content.school.length; i++) {
					new_drop += "<option value=" + result_content.school[i].sid + ">" + result_content.school[i].schoolName + "</option>";
				}
				new_drop += "</select>";
				$("#dropDown_school").append(new_drop);
				
				var new_drop1 = "<select id=\"school_id1\" class='form-control' style=\"margin-bottom: 16px;\">";
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
			if($("#start_date1").val()==""||$("#start_date1").val()==null){
				alert("請填寫複製日期!");
				return null;
			}
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
		$("#excel_upload").toggle("slow");
		//$("#query_table").hide();
		//$("#excelUpload").hide();
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
		
		for(var i=1; i<=10; i++){	//hide all
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
			var head_div = "";					
			head_div += "<div class=\"form-group\" style=\"margin-left: 0\">";				
				head_div += " <input type=\"submit\" id=\"copy\" status=\"0\" sidOld=\"\" startDateOld=\"\" endDateOld=\"\" onclick=\"copy()\" value=\"複製\" class=\"btn btn-primary\" style=\"margin-right: 10px\">";
				head_div += " <input type=\"button\" id=\"export\" onclick=\"export_excel()\" schoolId=\"" + schoolId + "\" startDate=\""+ startDate 
					+"\" endDate=\"" + endDate + "\" value=\"匯出\" class=\"btn btn-primary\" style=\"margin-right: 10px\">";
			head_div += "</div>";
			head_div += "<table class=\"table table-bordered table-striped\">";
			head_div += "<thead>";
			head_div += "<tr>";
				head_div +=	"<td class=\"text-center\">日期</td>" +
							"<td class=\"text-center\">學校</td>" +
							"<td class=\"text-center\">主食</td>" +
							"<td class=\"text-center\">主菜</td>" +
							"<td class=\"text-center\">主菜一</td>" +
							"<td class=\"text-center\">主菜二</td>" +
							"<td class=\"text-center\">副菜一</td>" +
							"<td class=\"text-center\">副菜二</td>" +
							"<td class=\"text-center\">副菜三</td>" +
							//"<td bgcolor=\"#a1c66a\" class=\"TIT_A\">副菜四</td>" +
							"<td class=\"text-center\">蔬菜</td>" +
							"<td class=\"text-center\">湯品</td>" +
							"<td class=\"text-center\">熱量</td>" +
							"<td class=\"text-center\">選項</td>";
				head_div += "</tr>";
				head_div += "</thead>";
				
			var result_content = response_obj.result_content;
			
			
			if(result_content.resStatus == "1"){	//success
				if(result_content.menu.length == 0){
					$("#menu_list").hide();
					alert("該學校在此時間區間的資料不存在");
					return;
				} else {
					var new_div = "<tbody>";
					var today=new Date();
					var limitDate=new Date(today.getFullYear()+"/"+(today.getMonth()+1)+"/"+today.getDate()+" "+today.getHours()+":"+today.getMinutes()+":00");				
					var act_html="";
					
					for(var i=0; i<result_content.menu.length; i++) {
						
						//判斷是否超過上傳時間，超過者不給修改鈕   20140220 KC  加入type=007的也不給修改鈕 20140505 KC
						var itemDate=new Date(result_content.menu[i].menuDate+" "+ENV_UPLOAD_DAY_LIMIT);
						
						if (itemDate.getTime()<limitDate.getTime() || utype=="007"){
							act_html="";
						}else{
							act_html="<button onclick=query_menu_detail_info('"+result_content.menu[i].mid+"') class=\"btn btn-primary\" style=\"width:48px\"><i class=\"fa fa-pencil\"></i></a>"+
										" <button onclick=delete_menu('"+result_content.menu[i].mid+"') class=\"btn btn-primary\"  style=\"width:48px\"><i class=\"fa fa-trash-o\"></i></a>";
						}
						
						
						//if(i % 2 == 0) {
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
									   "<td class=\"text-nowrap\">" + 
									   //"<a href=\"#\" onclick=query_menu_detail_info('"+result_content.menu[i].mid+"')>修改</a>"+
									   	//  " <a href=\"#\" onclick=delete_menu('"+result_content.menu[i].mid+"')>刪除</a>"+
									   	act_html+
									   "</td>";
							new_div += "</tr>";
							/*
						} else {
							new_div += "<tr>";
							new_div += "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].menuDate + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].schoolName + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].main + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].major + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].major1 + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].major2 + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].side1 + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].side2 + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].side3 + "</td>" +
									   //"<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].side4 + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].vegetable + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].soup + "</td>" +
									   "<td bgcolor=\"#FFFFFF\" class=\"componetContent2\">" + result_content.menu[i].calorie + "</td>" +
									   "<td width=\"120px\" bgcolor=\"#FFFFFF\" class=\"\" align=\"right\">" + 
									   	 // "<a href=\"#\" onclick=query_menu_detail_info('"+result_content.menu[i].mid+"')>修改</a>"+
									   	  //" <a href=\"#\" onclick=delete_menu('"+result_content.menu[i].mid+"')>刪除</a>"+
									   	  act_html+
									   "</td>";
							new_div += "</tr>";
						}
						*/
						
						//每20筆放入不同DIV
						if((i+1) % row == 0){
							new_div += "</tbody>";
							
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
				/*
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
				*/
				
				//20150209 shine mod 將產生菜單頁面欄位動作放到generateMenuForm function裡
				var new_div = generateMenuForm(mid,result_content);
				
				$("#queryList").html("");
				$("#queryList").append(new_div);
				
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
	
	function generateMenuForm(mid,result_content){
		//沒有傳入資料時,則使用空白資料
		if(result_content==null){
			result_content = {
					"resStatus":1,
					"msg":"",
					"mid":"",
					"menuDate":"",
					"schoolName":"",
					"main":"",
					"main2":"",
					"major":"",
					"major1":"",
					"major2":"",
					"major3":"",
					"side1":"",
					"side2":"",
					"side3":"",
					"side4":"",
					"side5":"",
					"side6":"",
					"vegetableName":"",
					"soup":"",
					"meals":"",
					"meals2":"",
					"grains":"",
					"vegetable":"",
					"calorie":"",
					"oil":"",
					"meatBeans":"",
					"fruit":"",
					"milk":""				
			};
			mid = 0;
		};
		
		var new_div = "<div id=\"queryList\" class=\"TAB_TY_B\">";
		new_div += "<div id=\"MAIN_TITLE_BAR\" class=\"\">";
		new_div += "<div class=\"contents-title\">菜單維護 <a href=\"#\" onclick=\"backToList()\" style=\"float:right;\" class=\"btn btn-primary\">回前頁</a></div></div>";
			new_div += "<table width=\"100%\" class=\"table table-bordered table-striped\" id=\"dataList\">";
				new_div += "<tr>";
					if(result_content.schoolName != ""){
						new_div += "<td>學校名稱</td>";
						new_div += "<td colspan=\"1\"><span id=\"school_name\">" + result_content.schoolName + "</span></td>";
						new_div += "<td>日期</td>";
						new_div += "<td colspan=\"1\"><span id=\"menu_date\">" + result_content.menuDate + "</span><span></span></td>";
					}else{
						new_div += "<td>學校名稱</td>";
						var schoolSelect = $("#school_id").html();
						new_div += "<td colspan=\"1\"><select id=\"school_name\">" + schoolSelect +"</select></td>";
						new_div += "<td>日期</td>";
						new_div += "<td colspan=\"1\"><input type=\"text\" id=\"menu_date\" class=\"dateMode\"><span></span></td>";
					}
					new_div += "</tr>";
			
					new_div += "<tr>";
					new_div += "<td bgcolor=\"#edf2e5\"><span style=\"color:red;float:right\">*</span>主食一</td>";
					new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"main_food\" name=\"mainfood\"  value=\"" + result_content.main + "\" size=\"20\"></td>";
					new_div += "<td bgcolor=\"#edf2e5\">主食二</td>";
					new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"main_food2\" name=\"mainfood\" value=\"" + result_content.main2 + "\" size=\"20\"></td>";
					new_div += "</tr>";
				
					new_div += "<tr>";
						//new_div += "<td>主菜<span style=\"color:red;float:right\">*</span></td>";
						new_div += "<td>主菜</td>";
						new_div += "<td><input type=\"text\" id=\"major_food\" name=\"productName\"  value=\"" + result_content.major + "\" size=\"20\"></td>";
						new_div += "<td>主菜一</td>";
						new_div += "<td><input type=\"text\" id=\"major1\" name=\"productName\"  value=\"" + result_content.major1 + "\" size=\"20\"></td>";
					new_div += "</tr>";
			
					new_div += "<tr>";
						new_div += "<td bgcolor=\"#edf2e5\">主菜二</td>";
						new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"major2\" name=\"productName\" value=\"" + result_content.major2 + "\"></td>";
						new_div += "<td bgcolor=\"#edf2e5\">主菜三</td>";
						new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"major3\" name=\"productName\"  value=\"" + result_content.major3 + "\"></td>";
					new_div += "</tr>";
			
					new_div += "<tr>";
						new_div += "<td>副菜一</td>";
						new_div += "<td><input type=\"text\" id=\"side1\" name=\"productName\"  value=\"" + result_content.side1 + "\" size=\"20\"></td>";
						new_div += "<td>副菜二</td>";
						new_div += "<td><input type=\"text\" id=\"side2\" name=\"productName\"  value=\"" + result_content.side2 + "\" size=\"20\"></td>";
					new_div += "</tr>";
			
					new_div += "<tr>";
						new_div += "<td bgcolor=\"#edf2e5\">副菜三</td>";
						new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"side3\" name=\"productName\"  value=\"" + result_content.side3 + "\" size=\"20\"></td>";
						new_div += "<td bgcolor=\"#edf2e5\">副菜四</td>";
						new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"side4\" name=\"productName\"  value=\"" + result_content.side4 + "\" size=\"20\"></td>";
					new_div += "</tr>";
				
					new_div += "<tr>";
						new_div += "<td>副菜五</td>";
						new_div += "<td><input type=\"text\" id=\"side5\" name=\"productName\"  value=\"" + result_content.side5 + "\" size=\"20\"></td>";
						new_div += "<td>副菜六</td>";
						new_div += "<td><input type=\"text\" id=\"side6\" name=\"productName\"  value=\"" + result_content.side6 + "\" size=\"20\"></td>";
					new_div += "</tr>";
     		
					new_div += "<tr>";
						new_div += "<td bgcolor=\"#edf2e5\">蔬菜</td>";
						new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"vegetable_name\" name=\"productName\" value=\"" + result_content.vegetableName + "\" size=\"20\"></td>";
						new_div += "<td bgcolor=\"#edf2e5\">湯品</td>";
						new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"soup\" name=\"productName\" value=\"" + result_content.soup + "\" size=\"20\"></td>";
					new_div += "</tr>"; 
     		
					new_div += "<tr>";
						new_div += "<td>附餐一</td>";
						new_div += "<td><input type=\"text\" id=\"meals\" name=\"productName\" value=\"" + result_content.meals + "\"><span></span></td>";
						new_div += "<td>附餐二</td>";
					new_div += "<td><input type=\"text\" id=\"meals2\" name=\"productName\" value=\"" + result_content.meals2 + "\"><span></span></td>";
     			
					new_div += "</tr>";
      
					new_div += "<tr>";
						new_div += "<td bgcolor=\"#edf2e5\"><span style=\"color:red;float:right\">*</span>全穀根莖類/份</td>";
						new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"valley\" name=\"productName\" value=\"" + result_content.grains + "\" onkeyup=\"return ValidateNumber(this,value)\"><span></span></td>";
						new_div += "<td bgcolor=\"#edf2e5\"><span style=\"color:red;float:right\">*</span>蔬菜類/份</td>";
						new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"vegetable\" name=\"productName\"  value=\"" + result_content.vegetable + "\" onkeyup=\"return ValidateNumber(this,value)\"><span></span></td>";
					new_div += "</tr>";
			
					new_div += "<tr>";
						new_div += "<td><span style=\"color:red;float:right\">*</span>油脂及堅果類/份</td>";
						new_div += "<td><input type=\"text\" id=\"oil\" name=\"productName\"  value=\"" + result_content.oil + "\" onkeyup=\"return ValidateNumber(this,value)\"><span></span></td>";
						new_div += "<td><span style=\"color:red;float:right\">*</span>豆魚肉蛋類/份</td>";
						new_div += "<td><input type=\"text\" id=\"meat\" name=\"productName\"  value=\"" + result_content.meatBeans + "\" onkeyup=\"return ValidateNumber(this,value)\"><span></span></td>";
					new_div += "</tr>";
			
					new_div += "<tr>";
						new_div += "<td bgcolor=\"#edf2e5\"><span style=\"color:red;float:right\">*</span>水果類/份</td>";
						new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"fruit\" name=\"productName\"  value=\"" + result_content.fruit + "\" onkeyup=\"return ValidateNumber(this,value)\"><span></span></td>";
						new_div += "<td bgcolor=\"#edf2e5\"><span style=\"color:red;float:right\">*</span>低脂乳品類/份</td>";	
						new_div += "<td bgcolor=\"#edf2e5\"><input type=\"text\" id=\"milk\" name=\"productName\"  value=\"" + result_content.milk + "\" onkeyup=\"return ValidateNumber(this,value)\"><span></span></td>";
					new_div += "</tr>";
			
					new_div += "<tr>";
					new_div += "</tr>";
			
					new_div += "<tr>";
						new_div += "<td><span style=\"color:red;float:right\">*</span>熱量/大卡</td>";
						new_div += "<td colspan='3'><input type=\"text\" id=\"calorie\" name=\"productName\" value=\"" + result_content.calorie + "\"  onkeyup=\"return ValidateNumber(this,value)\"><span></span></td>";
					new_div += "</tr>";
			
					new_div += "<tr>";
						new_div += "<td colspan=\"4\" align=\"center\">";
						if(result_content.schoolName != ""){
							new_div += "<input type=\"button\" value=\"確定\" id=\"update_menu_button\" onclick=\"update_menu()\" mid=\"" + mid + "\"  class=\"btn btn-primary\">";
						}else{
							new_div += "<input type=\"button\" value=\"確定\" id=\"add_menu_button\" onclick=\"add_menu()\" mid=\"0\" class=\"btn btn-primary\">";
						}
						new_div += "</td>";
					new_div += "</tr>";
				new_div += "</table>";
			new_div += "</div>";
			
		return new_div;
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
	
	/*
	function uploadFile(){
		alert("上傳成功");
		
		$("#excel_upload").hide("slow");
		$("#excelUpload").show("slow");

	}
	*/
	
	function checkMenuForm(){
		if(checkSpecialCharacters()){
			return;
		}
		if($("#main_food").val() == "") {
			alert("請填入主食一");
			return;
		
// 		} else if ($("#major_food").val() == "") {
// 			alert("請填入主菜");
// 			return;
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
			alert("請填入低脂乳品類/份");
			return;
		} else if ($("#calorie").val() == "") {
			alert("請填入熱量/大卡");
			return;
		}
		
		return true;
	}
	
	function update_menu(){
		if(!checkMenuForm()){
			return;
		}
				
		if($("#school_id option").length>1){
			/* 
			var msg= new MsgsProcessing();
			msg.alertMsgs("confirmOK","是否將此動作套用全部學校?",0,function(){				
				console.log("call yes_callback");
				do_update_menu("all");
			},function(){
				console.log("call no_callback");
				do_update_menu("");
			}); 
			*/
			generateSchoolMenu();
		}else{
			do_update_menu("");
		}
	}

	function do_update_menu(updateAll){
		var mid = $("#update_menu_button").attr("mid");
		
		//alert(mid +","+ $("#main_food").attr("value") +","+ $("#major_food").attr("value") +","+
				//$("#major1").attr("value") +","+ $("#major2").attr("value") +","+ $("#side1").attr("value")
				//+","+ $("#side2").attr("value") +","+ $("#side3").attr("value") +","+ $("#side4").attr("value") 
				//+","+ $("#vegetable_name").attr("value") +","+ $("#soup").attr("value") +","+ $("#meals").attr("value") 
				//+","+ $("#calorie").attr("value") +","+ $("#valley").attr("value") +","+ $("#vegetable").attr("value") 
				//+","+ $("#oil").val() +","+ $("#meat").attr("value") +","+ $("#fruit").val() +","+ $("#milk").attr("value"));
		
		//alert($("#major2").val());
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
	 					"milk": $("#milk").val(),
	 					"applyToSchool": updateAll
		 			}
				};
		
		
		/*var request_data =	{
				 "method":"updateMenuDetailInfo",
	 				"args":{	
	 					"mid": mid,
	 					"main": $("#main_food").attr("value"),
	 					"major": $("#major_food").attr("value"),
	 					"major1": $("#major1").attr("text"),
	 					"major2": $("#major2").attr("text"),
	 					"side1": $("#side1").attr("value"),
	 					"side2": $("#side2").attr("value"),
	 					"side3": $("#side3").attr("value"),
	 					"side4": $("#side4").attr("value"),
	 					"vegetableName": $("#vegetable_name").attr("value"),
	 					"soup": $("#soup").attr("value"),
	 					"meals": $("#meals").attr("value"),
	 					"calorie": $("#calorie").attr("value"),
	 					"grains": $("#valley").attr("value"),
	 					"vegetable": $("#vegetable").attr("value"),
	 					"oil": $("#oil").attr("value"),
	 					"meatBeans": $("#meat").attr("value"),
	 					"fruit": $("#fruit").attr("value"),
	 					"milk": $("#milk").attr("value")
		 			}
				};
		*/
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
	
	function add_menu(){
		if(!checkMenuForm()){
			return;

		}				
		if($("#school_id option").length>1){
				var msg= new MsgsProcessing();
				msg.alertMsgs("confirmOK","是否將此動作套用全部學校?",0,function(){				
				console.log("call yes_callback");
				do_add_menu(true);
			},function(){
				console.log("call no_callback");
				do_add_menu(false);
			});
		}else{
			do_add_menu(false);
		}
	}

	function do_add_menu(addAllSchool){
		var mid = $("#update_menu_button").attr("mid");
		
		var schoolName = "";
		
		if(addAllSchool){
			var tmp = new Array();
			$.each($("#school_name option"),function(idx,obj){
				tmp.push($(obj).text());
			});
			schoolName = tmp.join(",");
		}else{
			schoolName = $("#school_name option:selected").text();
		}
		console.log("addAllSchool=>" + addAllSchool);
		console.log("schoolName=>" + schoolName);
		
		var request_data =	{
				 "method":"addMenuDetailInfo",
	 				"args":{	
	 					"mid": mid,
	 					"schoolName":schoolName,
	 					"menuDate":$("#menu_date").val(),
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
		
		var msg= new MsgsProcessing();
		msg.alertMsgs("loading","處理中");
		
		var response_obj = call_rest_api(request_data);
		msg.alertMsgs("unblock");
		//response
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				
				createMenuList();	//重新查詢
				
				$("#queryList").hide();
				$("#menu_list").show();
				//20150212 shine add 顯示查詢區塊
				$("#query_rule").parent().show();
				$("#query_table").show();
				
				alert("菜單新增成功");
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
		if(!$("#file").val()){
	    	MSG.alertMsgs('check', '請選擇檔案', 0);
	    	return false;
	    }/* Create a FormData instance */
	    var formData = new FormData();
		   MSG.alertMsgs('loading', '', 0);
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
	    	    if (obj.retMsg==""){
	    	    	if( $("#overWrite").val()==1){
	    	    		MSG.alertMsgs('checkAndReload', '菜單已完成覆蓋，請至「食材資料維護」檢查與更新對應的食材。', 0);
	    	    	}else{
	    	 	   		MSG.alertMsgs('checkAndReload', '檔案上傳成功，請至「食材資料維護」完成每日食材資料', 0);
	    	    	}
	    	    }else{
	    	    	if( $("#overWrite").val()==1){
	    	    		MSG.alertMsgs('checkAndReload', obj.retMsg+'<br>菜單完成覆蓋後，請至「食材資料維護」檢查與更新對應的食材。', 0);
	    	    	}else{
	    	 	   		MSG.alertMsgs('checkAndReload', obj.retMsg+'<br>菜單上傳完成後，請至「食材資料維護」完成每日食材資料', 0);
	    	    	}
	    	    }
	    	    $("#file").val("");
	        	 $("#overWrite").attr("value", 0);
	        } else if (obj.retStatus == 0) {
	    	 	   MSG.alertMsgs('check', '檔案上傳失敗，原因為' + obj.retMsg , 0);
	        	 $("#file").val("");
	        	 $("#overWrite").attr("value", 0);
	        } else if (obj.retStatus == -1) {
				MSG.alertMsgs('confirm', '菜單重複，請問是否要覆蓋？若覆蓋菜單，系統將刪除當日對應之食材訊息。', 0);
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
	function ValidateNumber(e, pnumber)
{
    if (!/^\d+[.]?[1-9]?$/.test(pnumber))
    {
        var newValue = /\d+[.]?[1-9]?/.exec(e.value);         
        if (newValue != null)         
        {             
            e.value = newValue;        
        }      
        else     
        {          
            e.value = "";    
        } 
    }
    
    return false;
}
function backToList(){
	$("#menu_list").show();
	$("#menu_list1").show();
	$("#queryList").hide("slow");
	
	//20150212 shine add 顯示查詢區塊
	$("#query_rule").parent().show();
	$("#query_table").show();
}

function webKeyInMenu(){
	var new_div = generateMenuForm();
	$("#queryList").html("");
	$("#queryList").append(new_div);
	
	$("#menu_date").datepicker({ 
		dateFormat: "yy/mm/dd"		
	}).datepicker("setDate", new Date());
	
	//20150212 shine add 隱藏查詢區塊
	$("#query_rule").parent().hide();
	$("#query_table").hide();	
	$("#menu_list1").hide();
	
	$("#queryList").show();
	$("#menu_list").hide("slow");
};

function generateSchoolMenu(){
	var form = $("<div id='selectSchoolForm' style='height:180px;overflow-y:auto'></div>");
	var school = $("#school_id option");
	
	var item = $("<div><input type='checkbox' value='all'>全部學校</div>");;
	$(form).append(item);
	for(var i=0;i<school.length;i++){
		var opt = $(school[i]);
		var isDefault = (opt.val() == $("#school_id").val());
		item = $("<div><input type='checkbox' value='" + opt.val() + "' " + (isDefault?"disabled=true":"") + " >" + opt.text() + (isDefault?"(預設更新)":"") + "</div>");

		$(form).append(item);
	}
			
	$(form).dialog({
		title: '請選擇要同步更新的學校',
		height: 450,
		width: 400,
		buttons: [
		  {
			text: "確定",           
            click: function() {
              var choose = "";
              var aryChoose = new Array();
              $.each($("#selectSchoolForm input[type='checkbox']"),function(idx,obj){
            	if($(obj).prop("checked")){
            		aryChoose.push($(obj).val());
            	}            	
              });
              choose = aryChoose.join(",");
              if(choose.indexOf("all") != -1){
            	  choose = "all";
              }
			  console.log(choose);

              do_update_menu(choose);
              $(this).dialog("close");
              $("#selectSchoolForm").remove();
            }     
		  },
          {
            text: "取消",            
            click: function() {
              $( this ).dialog( "close" );
              $("#selectSchoolForm").remove();
            }                 
          }
        ]
	});	
	
	$("#selectSchoolForm input[type='checkbox']").click(function(evt){
		var checkBox = $(evt.srcElement==undefined?evt.target:evt.srcElement);
		
		//check if "all" 		
		if(checkBox.prop("checked")){
			//check 
			if(checkBox.val() == "all"){
				$("#selectSchoolForm input[type='checkbox']").prop("checked",true);
			}else{
				
			}
		}else{
			//uncheck "all" 			
			if(checkBox.val() == "all"){
				$("#selectSchoolForm input[type='checkbox']:enabled").prop("checked",false);	
			}else{
				$("#selectSchoolForm input[type='checkbox'][value='all']").prop("checked",false);
			}
		}
	});
	
	//預設全選
	$("#selectSchoolForm input[type='checkbox']").prop("checked",true);
}

$(document).ready(function(){	
	$("#excel_upload").hide();
	$("#queryList").hide();
	$("#copy_to").hide();
	querySchool();	//查詢學校
	defaultYearMonth();
	
	//createMenuList();
});
</script>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<style type="text/css">
.table td button{
	min-width:48px;
	margin-right:2px; 
} 

.queryList{
	position: absolute;
	top: 0px;
	left: 0px;
 	width: 100%;
}
</style>
<title></title>
</head>
<body>
<div style="display:none;position: absolute; top:0; left:0; height:100%; width:100%;opacity: 0.5; z-index:9999; background-color:#666;text-align:center; color:#fff; line-height:500px;" id="uploading" >檔案寫入中... 請勿進行操作。</div>

<div class="contents-title">學校菜單維護</div>
<div class="contents-wrap">
	<div id="excelUpload">
		<a class="btn btn-primary" style="margin: 0px 10px 10px 0px;" href="#" id="excelUpload" onclick="showExcelUpload()">菜單Excel匯入</a>
        <a class="btn btn-primary" style="margin: 0px 10px 10px 0px;" href="#" id="webKeyIn" onclick="webKeyInMenu()">線上新增菜單</a>
        <a class="btn btn-primary" style="margin: 0px 10px 10px 0px;" href="../../images/files/menuExcelExample.xlsx">下載範例檔</a>
	</div>		
	<div id="excel_upload" class="TAB_TY_B" style="display: none;">
		<form method="post" action="/cateringservice/file/upload" enctype="multipart/form-data">
			<input type="hidden" id="func" name="func" value="Menu" /> 
			<input type="hidden" id="overWrite" name="func" value="0" />
			<table class="component">
				<tr>
					<td class="componetContent2 componentContentRightLine">選擇檔案</td>
					<td class="componetContent2"><input id="file" type="file" name="excelFile"> (上傳檔案格式為Excel檔)</td>
					<td class="componetContent2"><input class="btn btn-primary" onclick="upload()" value="上傳檔案"/></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="MAIN_TITLE_BAR" class="">
		<div id="query_rule" class="">請輸入查詢條件</div>
		<div class="">
			<div id="page"></div>
			<!-- 
			<a href="#" onClick="page()">新增供應商</a>
			<a href="#" onClick="showUpload()">excel上傳</a>
			<a href="#" onClick="export_excel()">excel下載</a>
			-->
		</div>
	</div>
	<div class="section-wrap has-border">
		<div class="form-horizontal kitchen-info">
			<div id="query_table">
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
			<div id="copy_to" style="margin-top: 10px;">
				<div class="dis_intb LOGIN_US_PS">
					<h6 style="margin: 0;">學校：</h6>&nbsp;<span id="dropDown_school1">
				</div>
				<h6>請輸入開始日期：</h6>
	           	<div class="inline-wrap datetimepicker-start" style="width: 30%">
	                <input type="text" class="form-control inline datetimepicker" id="start_date1">
	            </div>
			</div>
			<div id="menu_list1"></div>
			<div id="menu_list2"></div>
			<div id="menu_list3"></div>
			<div id="menu_list4"></div>
			<div id="menu_list5"></div>			
			<div id="queryList" class="queryList"></div>	
		</div>
	</div>
</div>
</body>
</html>