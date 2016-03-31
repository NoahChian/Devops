<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- full screen calendar -->
<script src="../../js/jquery-ui.min.js"></script>
<link href="../../css/fullcalendar.css" rel="stylesheet" type="text/css" />
<script src="../../js/fullcalendar.min.js"></script>



<style>
	body {
		margin-top: 40px;
		text-align: center;
		font-size: 13px;
		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
	}

	#calendar {
		width: 790px;
		margin: 0 auto;
	}
	
	#tabs {
		width: 702px;
		margin: 0 auto;
	}
</style>

<script>
	/*
	var drop_down_default = "<select><option value=\"0\">請選擇</option></select>";
	
	//縣市
	function counties(){
		var request_data =	{
				 "method":"customerQueryCounties",
	 				"args":{
	 					"condition":0
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				var drop_down_counties = "<select id=\"select_counties\" onChange=\"area()\">";
					drop_down_counties += "<option value=\"0\">請選擇</option>";
					for(var i=0; i<result_content.counties.length; i++) {
						drop_down_counties += "<option value=" + result_content.counties[i].cid + ">" + result_content.counties[i].countiesName + "</option>";
					}
				drop_down_counties += "</select>";
				$("#dropDown_counties").append(drop_down_counties);
				$("#dropDown_area").append(drop_down_default);
				$("#dropDown_school").append(drop_down_default);
				$("#dropDown_yearMonth").append(drop_down_default);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}

	}
	
	
	//縣市改變修改區域
	function area() {
		//alert("aa");
    	var counties = $("#select_counties").val();
    	//alert(counties);
    	var request_data =	{
				 "method":"customerQueryArea",
	 				"args":{
	 					"cid" : counties
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				var drop_down_area = "<select id=\"select_area\" onChange=\"school()\">";
					drop_down_area += "<option value=\"0\">請選擇</option>";
					for(var i=0; i<result_content.area.length; i++) {
						drop_down_area += "<option value=" +  result_content.area[i].aid + ">" + result_content.area[i].areaName + "</option>";
					}
				drop_down_area += "</select>";
				
				//清空
				$("#dropDown_area").html("");
				$("#dropDown_school").html("");
				$("#dropDown_yearMonth").html("");
				
				$("#dropDown_area").append(drop_down_area);
				$("#dropDown_school").append(drop_down_default);
				$("#dropDown_yearMonth").append(drop_down_default);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
    }
	
	//區域改變修改學校
	function school() {
    	var area = $("#select_area").val();
    	
    	//alert(area);
    	
    	var request_data =	{
				 "method":"customerQuerySchool",
	 				"args":{
	 					"aid": area
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		//alert(response_obj.toString());
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			
			//alert(result_content.resStatus);
			
			if(result_content.resStatus == 1){	//success
				var drop_down_school = "<select id=\"select_school\" onChange=\"menu()\">";
					drop_down_school += "<option value=\"0\">請選擇</option>";
					for(var i=0; i<result_content.school.length; i++) {
						drop_down_school += "<option value=" +  result_content.school[i].sid + ">" 
							+ result_content.school[i].schoolName + "</option>";
					}
				drop_down_school += "</select>";
				
				//清空
				$("#dropDown_school").html("");
				$("#dropDown_yearMonth").html("");
				
				$("#dropDown_school").append(drop_down_school);
				$("#dropDown_yearMonth").append(drop_down_default);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
    }
	
	
	//學校改變年月
	function menu() {
    	
		var sid = $("#select_school").val();
    	var request_data =	{
				 "method":"customerQueryMonthBySchool",
	 				"args":{
	 					"sid" : sid
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				var drop_down_date = "<select id=\"select_yearMonth\">";
					drop_down_date += "<option value=\"0\">請選擇</option>";
					for(var i=0; i<result_content.date.length; i++) {
						drop_down_date += "<option value=" +  result_content.date[i].yearMonth + ">" 
							+ result_content.date[i].yearMonth + "</option>";
					}
				drop_down_date += "</select>";
				
				$("#dropDown_yearMonth").html("");
				
				$("#dropDown_yearMonth").append(drop_down_date);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
    }
	
	//查詢某月學校的供餐廠商
	function queryMenu() {
		//var sid = $("#dropDown_school").val();
		//var date = $("#dropDown_yearMonth").val();
		
		//var sid = $("#select_school").attr("value");
		
		$("#calendar").html("");
		
		var s = document.getElementById("select_school");
		var sid = s.options[s.selectedIndex].value;
		
		var ym = document.getElementById("select_yearMonth");
		var query_date = ym.options[ym.selectedIndex].value;
		
		//alert(query_date);
		
		if(sid == 0){
			alert("您尚未選擇學校");
			return;
		} else if (query_date == 0) {
			alert("您尚未選擇月份");
			return;
		}
		
		//alert(sid +","+ query_date);
		
    	var request_data =	{
				 "method":"customerQueryCateringBySchoolAndDate",
	 				"args":{
	 					"sid" : sid,
	 					"date" : query_date
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success	
				$('#calendar').fullCalendar({
					header: {
						left: '',		//prev,next today
						center: 'title',
						right: ''		//month,agendaWeek,agendaDay
					},
					editable: false,
				    year: result_content.year,
				    month: result_content.month, // August
					events: result_content.events,
					
					eventClick: function(event) {
				        if (event.mid) {
				        	query_detail_data(event.mid);
				        }
				    }
				});
				
				//$("#dropDown_yearMonth").html("");
			
				//$("#dropDown_yearMonth").append(drop_down_date);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
    }
	
	//tabs元件
	$(function() {
		$( "#tabs" ).tabs();
		
		//tabs color
		$(".ui-tabs .ui-tabs-nav a").css("background-color","#99D85D");
		$(".ui-tabs .ui-tabs-selected a").css("background-color","#1A9660");
	});
	
	/*
	function detailData(){
		//午餐內容
		
		
		//食材資訊
		var food_info_div = "<table class=\"component\">";
			food_info_div += "<tr>";
				food_info_div += "<td class=\"componentTitle\">菜色</td>" +
								 "<td class=\"componentTitle\">原料</td>" +
							 	 "<td class=\"componentTitle\">品牌</td>" +
								 "<td class=\"componentTitle\">來源</td>" +
								 "<td class=\"componentTitle\">認證標章</td>";
			food_info_div += "</tr>";
		food_info_div +="</table>";
		$("#food_info").append(food_info_div);
		
		
		//調味料
		var seasoning_div = "<table class=\"component\">";
			seasoning_div += "<tr>";
				seasoning_div += "<td class=\"componentTitle\">原料</td>" +
							 	 "<td class=\"componentTitle\">品牌</td>" +
								 "<td class=\"componentTitle\">來源</td>" +
								 "<td class=\"componentTitle\">認證標章</td>";
			seasoning_div += "</tr>";
		seasoning_div +="</table>";
		$("#seasoning_info").append(seasoning_div);	
	}
	
	
	//查詢事件
	function query_detail_data(mid){
		
		//alert(mid);
		
		$("#lunch_content").html("");
		$("#food_info").html("");
		$("#seasoning_info").html("");
		$("#controller").html("");
		$("#query_detail_info").html("");

    	var request_data =	{
				 "method":"customerQueryMenuDetailInfo",
	 				"args":{
	 					"mid" : mid
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				
				//標題
				var new_div = "<div style=\"margin-bottom:10px\" class=\"componetContent\">" + result_content.schoolName 
					+ result_content.date + "營養午餐</div>";
				
				//營養份量
				new_div += "<div style=\"margin-bottom: 10px\">";
					new_div += "<table class=\"commonTable\">";
						new_div += "<tr>";
							new_div += "<td rowspan=\"9\" style=\"width:250px;height:210px\">";
								new_div += "<table width=\"250px\">";
									new_div += "<tr>";
										new_div += "<td class=\"componetContent\" colspan=\"2\" ><center>營養份量<center></td>";
									new_div += "</tr>";
									
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">全榖根莖類</td>";
										new_div += "<td class=\"tdContent\">"+result_content.Grains+"</td>";
									new_div += "</tr>";
					
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">蔬菜類</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.nutrition.vegetable +"份</td>";
									new_div += "</tr>";
					
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">油脂與堅果種子類</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.nutrition.oil +"份</td>";
									new_div += "</tr>";
									
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">蛋豆魚肉類</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.nutrition.meatBeans +"份</td>";
									new_div += "</tr>";
					
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">水果類</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.nutrition.fruit +"份</td>";
									new_div += "</tr>";
					
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">乳品類</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.nutrition.milk +"份</td>";
									new_div += "</tr>";
					
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">熱量</td>";			
										new_div += "<td class=\"tdContent\">"+ result_content.nutrition.calories +"大卡</td>";
									new_div += "</tr>";
								new_div += "</table>";
							new_div += "</td>";
						new_div += "</tr>";
		            	
						//供餐者資訊
						new_div += "<tr>";
							new_div += "<td rowspan=\"9\" style=\"width:250px;height:210px\">";
								new_div += "<table  width=\"250px\">";
									new_div += "<tr>";
										new_div += "<td class=\"componetContent\" colspan=\"2\"><center>供餐者資訊<center></td>";
									new_div += "</tr>";
						
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">供應商：</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.supplierInfo.supplierName +"</td>";
									new_div += "</tr>";
									
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">供應商地址：</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.supplierInfo.supplierAddress +"</td>";
									new_div += "</tr>";
									
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">供應商電話</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.supplierInfo.supplierPhone +"</td>";
									new_div += "</tr>";
						
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">負責人</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.supplierInfo.supplierLeader +"</td>";
									new_div += "</tr>";
									
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">營養師</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.supplierInfo.dietitians +"</td>";
									new_div += "</tr>";
									
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">品質認證體系</td>";
										new_div += "<td class=\"tdContent\">"+ result_content.supplierInfo.authenticate +"</td>";
									new_div += "</tr>";
									
									new_div += "<tr>";
										new_div += "<td class=\"tdTitle\">&nbsp;</td>";
										new_div += "<td class=\"tdContent\">&nbsp;</td>";
									new_div += "</tr>";
								new_div += "</table>";
							new_div += "</td>";		  
						new_div += "</tr>";
					new_div += "</table>";
				new_div += "</div>";
				
				$("#query_detail_info").append(new_div);
				
				//控制項(上一頁、前一日、後一日)
				var controller_div = "<div align=\"right\" onclick=\"controller()\" style=\"cursor:pointer\">回前一頁</div>";
				$("#controller").append(controller_div);
				
				//午餐內容
				var lunch_content_div = "<table  border=\"1\">";
					for(var k=0; k<result_content.lunchContent.length; k++){
						if(k%3 == 0 && k!=0) { lunch_content_div += "<tr>";	var count = 0;}
							lunch_content_div +="<td>";
								lunch_content_div +="<table>";
									lunch_content_div +="<tr><td>" + result_content.lunchContent[k].category + "</td></tr>";
									lunch_content_div +="<tr><td>" + result_content.lunchContent[k].foodName + "</td></tr>";
									lunch_content_div +="<tr><td>" + result_content.lunchContent[k].image + "</td></tr>";
								lunch_content_div +="</table>";
							lunch_content_div +="</td>";
							count ++;
						if(count%3 == 0 || result_content.lunchContent.length == (k+1)) { lunch_content_div += "</tr>"; }
					}
				lunch_content_div +="</table>";
				$("#lunch_content").append(lunch_content_div);

				//食材資訊
				var food_info_div = "<table class=\"component\">";
					food_info_div += "<tr>";
						food_info_div += "<td class=\"componentTitle\">菜色</td>" +
										 "<td class=\"componentTitle\">原料</td>" +
									 	 "<td class=\"componentTitle\">品牌</td>" +
										 "<td class=\"componentTitle\">來源</td>" +
										 "<td class=\"componentTitle\">認證標章</td>";
					food_info_div += "</tr>";
					
					for(var i=0; i<result_content.foodInfo.length; i++){
						food_info_div += "<tr>";
						food_info_div += "<td class=\"componentContent\">" + result_content.foodInfo[i].foodName + "</td>" +
										 "<td class=\"componentContent\">" + result_content.foodInfo[i].material + "</td>" +
									 	 "<td class=\"componentContent\">" + result_content.foodInfo[i].brand + "</td>" +
										 "<td class=\"componentContent\">" + result_content.foodInfo[i].source + "</td>" +
										 "<td class=\"componentContent\">" + result_content.foodInfo[i].authenticate + "</td>";
						food_info_div += "</tr>";
					}
				food_info_div +="</table>";
				$("#food_info").append(food_info_div);
				
				
				//調味料
				var seasoning_div = "<table class=\"component\">";
					seasoning_div += "<tr>";
						seasoning_div += "<td class=\"componentTitle\">原料</td>" +
									 	 "<td class=\"componentTitle\">品牌</td>" +
										 "<td class=\"componentTitle\">來源</td>" +
										 "<td class=\"componentTitle\">認證標章</td>";
					seasoning_div += "</tr>";
					
					for(var j=0; j<result_content.seasoning.length; j++){
						seasoning_div += "<tr>";
						seasoning_div += "<td class=\"componentContent\">" + result_content.seasoning[j].material + "</td>" +
										 "<td class=\"componentContent\">" + result_content.seasoning[j].brand + "</td>" +
									 	 "<td class=\"componentContent\">" + result_content.seasoning[j].source + "</td>" +
										 "<td class=\"componentContent\">" + result_content.seasoning[j].authenticate + "</td>";
						seasoning_div += "</tr>";
					}
					
				seasoning_div +="</table>";
				$("#seasoning_info").append(seasoning_div);

			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
		
		
		
		$("#dss").hide("slow");
		$("#calendar").hide("slow");
		
		$("#query_detail_info").show("slow");
		$("#tabs").show("slow");
		$("#controller").show("slow");
		
	}
	
	function controller(){
		$("#tabs").hide("slow");
		$("#controller").hide("slow");
		$("#query_detail_info").hide("slow");
		
		$("#dss").show("slow");
		$("#calendar").show("slow");
	}
	
	$(document).ready(function(){	
		//aaa();
		//calendar();	//full screen calendar
		//detailData();
		counties();	//drop down counties
		$("#tabs").hide();
	});
	*/
</script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<!--  <table id="dss" >
    	<tr>
      		<td colspan="9">>>菜單查詢:</td>
     	</tr>                  
		<tr>         
			<td align="left" valign="middle" nowrap="nowrap"><img src="../../images/arrow_green.jpg" width="9" height="18" />縣市別:</td>
			<td  align="left" valign="middle">
				<span id="dropDown_counties">
			</td>
			<td align="left" valign="middle" nowrap="nowrap"><img src="../../images/arrow_green.jpg" width="9" height="18" />市/區別:</td>
			
			<td align="left" valign="middle">
				<span id="dropDown_area">
			</td> 		                        
			<td align="left" valign="middle"><img src="../../images/arrow_green.jpg" width="9" height="18" />學校名稱:</td>

			<td align="left" valign="middle">
				<span id="dropDown_school">
			</td>
			<td align="left" valign="middle"><img src="../../images/arrow_green.jpg" width="9" height="18" />年/月:</td>
			<td align="left" valign="middle">
				<span id="dropDown_yearMonth">
        	</td>
        	<td>  
				<img src="../../images/bt_search3.jpg" width="35" height="19" onclick="queryMenu()" onkeypress="queryProduct()"/>                       
			</td> 		                        
		</tr>
     	<tr>
      		<td colspan="9"><br /></td>
     	</tr>
	</table>
	
	<div id='calendar'></div>
	
	<div id = "query_detail_info"></div>
	
	<div id='controller'></div>
	
	<br>
	
	<div id="tabs">
		<ul>
		    <li><a href="#tabs-1">午餐內容</a></li>
		    <li><a href="#tabs-2">食材資訊</a></li>
		    <li><a href="#tabs-3">調味料</a></li>
	  	</ul>
	  	<div id="tabs-1">
	    	<div id="lunch_content"></div>
	  	</div>
	  	<div id="tabs-2">
	    	<div id="food_info"></div>
	  	</div>
	  	<div id="tabs-3">
	   		<div id="seasoning_info"></div>
	   	</div>
	</div>
	 -->
</body>
</html>