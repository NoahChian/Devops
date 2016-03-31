<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.HashMap" %>
<%
String qCountyId="";
String qCountyName="";
String qSchoolId="";
String qSchoolName="";
String basepath="../../";
if(request.getAttribute("responseMap") != null){
	HashMap<String,Object> responseMap = (HashMap<String,Object>)request.getAttribute("responseMap");
	//增加訊息回傳alert, 有錯誤直接跳轉  
	if (responseMap.get("errorMsg")!=null){
		String errorhtml="";
		errorhtml="<script>alert('"+responseMap.get("errorMsg").toString()+"');history.go(-1); </script>";
		out.write(errorhtml);
	}
	if (responseMap.get("countyId")!=null){
		qCountyId=(String) responseMap.get("countyId");
	}
	if (responseMap.get("schoolId")!=null){
		qSchoolId=(String) responseMap.get("schoolId");
	}
	if (responseMap.get("countyName")!=null){
		qCountyName=(String) responseMap.get("countyName");
	}
	if (responseMap.get("schoolName")!=null){
		qSchoolName=(String) responseMap.get("schoolName");
	}
	
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
var qCountyId="<%=qCountyId %>";
var qCountyName="<%=qCountyName %>";
var qSchoolId="<%=qSchoolId %>";
var qSchoolName="<%=qSchoolName %>";
var basepath="<%=basepath%>";
</script>
<!-- full screen calendar -->
<!--script src="../../js/jquery-ui.min.js"></script-->
<link href="<%=basepath%>css/fullcalendar.css" rel="stylesheet" type="text/css" />
<script src="<%=basepath%>js/fullcalendar.js"></script>
<script src="<%=basepath%>js/jquery.xdomainajax.js" type="text/javascript"></script>
<script src="<%=basepath%>js/system-setting.js"></script>
<script src="<%=basepath%>js_ca/common.js"></script>
<script src="<%=basepath%>js_ca/main_index.js"></script>



<style>
#calendar {
	width: 790px;
	margin: 0 auto;
}

#tabs {
	width: 790px;
	margin: 0 auto;
}

/**tabs 底色**/
.ui-tabs .ui-widget-header {
	background-color: #FFFFFF;
}

/**按下底色**/
.ui-tabs .ui-tabs-nav li.ui-tabs-active a,.ui-tabs .ui-tabs-nav li.ui-state-disabled a,.ui-tabs .ui-tabs-nav li.ui-tabs-loading a
	{
	font-weight: bold;
	/* color: #FFFFFF; */
}

.ui-widget-header {
	border: 0px;
}

/**按下底色**/
.ui-tabs .ui-tabs-selected a {
	background-color: #F7A91E;
	font-weight: bold;
}
</style>

<script>

var qCountyId="<%=qCountyId %>";
var qCountyName="<%=qCountyName %>";
var qSchoolId="<%=qSchoolId %>";
var qSchoolName="<%=qSchoolName %>";


$(document).ready(function(){
	counties();	//drop down counties
	area();
	school();
	//menu();
	queryMenu();
	
	$("#tabs").hide();
	$('.fancybox').fancybox();
	
	//showCertInfo();
});	
	
	//縣市
	function counties(){
		if (qSchoolName!=""){
			$("#dropDown_counties").attr("disabled",true);
			return;
		}
		$("#dropDown_counties").append("<option><%=qCountyName%></option>");
		$("#dropDown_counties").attr("disabled",true);

	}
	
	
	//縣市改變修改區域
	function area() {
		if (qSchoolName!=""){
			return;
		}
		var area = getCookie("area");
		if(area == ""){
			area = 131374;
		}
		
		//alert("aa");
    	var counties = <%=qCountyId%>;//$("#select_counties").val();
    	//alert(counties);
    	var request_data =	{
				 "method":"customerQueryArea",
	 				"args":{
	 					"cid" : counties
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				var drop_down_area = "<select id=\"select_area\" onChange=\"school()\">";
					//drop_down_area += "<option value=\"0\">請選擇</option>";
					for(var i=0; i<result_content.area.length; i++) {
						if(i==0){
						//if(result_content.area[i].aid == area){	//萬里區
							drop_down_area += "<option selected value=" +  result_content.area[i].aid + ">" + result_content.area[i].areaName + "</option>";
							continue;
						}
						drop_down_area += "<option value=" +  result_content.area[i].aid + ">" + result_content.area[i].areaName + "</option>";
					}
				drop_down_area += "</select>";
				
				//清空
				$("#dropDown_area").html("");
				//$("#dropDown_school").html("");
				//$("#dropDown_yearMonth").html("");
				
				$("#dropDown_area").append(drop_down_area);
				//$("#dropDown_school").append(drop_down_default);
				//$("#dropDown_yearMonth").append(drop_down_default);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
		}
    }

	//區域改變修改學校
function school() {	
		if (qSchoolName!=""){
			var drop_down_school = "<select id='select_school'>";
			drop_down_school+="<option selected value='<%=qSchoolId%>'><%=qSchoolName%></option>";
			drop_down_school+="</select>";
			$("#dropDown_school").html("");

			$("#dropDown_school").append(drop_down_school);
			menu();
			return;
		}
		
		var school = getCookie("school");
		if(school == ""){
			school = 1837478;
		}
		var area = $("#select_area").val();
    	//var countiesName = $("#select_counties").find('option:selected').text();
		//var areaName = $("#select_area").find('option:selected').text();
		
    	var request_data =	{
				 "method":"customerQuerySchool",
	 				"args":{
	 					"aid": area
	 				}
				};
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
			
			//alert(result_content.resStatus);
			var countiesName=$("#dropDown_counties").val();
			var areaName=$("#select_area").val();
			if(result_content.resStatus == 1){	//success
				var drop_down_school = "<select id=\"select_school\" onChange=\"menu()\">";
					//drop_down_school += "<option value=\"0\">請選擇</option>";
					for(var i=0; i<result_content.school.length; i++) {
						var sch_Name1 = result_content.school[i].schoolName.replace(countiesName+"立","");
						var sch_Name0 = sch_Name1.replace(countiesName,"");
						var sch_Name = sch_Name0.replace(areaName,"");
						//var sch_Name = result_content.school[i].schoolName;
						if(i==0){
							drop_down_school += "<option selected value=" 
								+ result_content.school[i].sid + ">" + sch_Name + "</option>";
							continue;
						}
						drop_down_school += "<option value=" +  result_content.school[i].sid + ">" + sch_Name + "</option>";
					}
				drop_down_school += "</select>";
				
				//清空
				$("#dropDown_school").html("");
				$("#dropDown_yearMonth").html("");
				
				$("#dropDown_school").append(drop_down_school);
				menu();
				//$("#dropDown_yearMonth").append(drop_down_default);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
		}
    }	

	//學校改變年月
	function menu() {
		
		var yearMonth = getCookie("yearMonth");
		if(yearMonth == ""){
			yearMonth = "2014/03";
		}
    	
		var sid = $("#select_school").val();
    	var request_data =	{
				 "method":"customerQueryMonthBySchool",
	 				"args":{
	 					"sid" : sid
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				var drop_down_date = "<select id=\"select_yearMonth\">";
					//drop_down_date += "<option value=\"0\">請選擇</option>";
					//if(result_content.date[0].yearMonth){drop_down_date += "<option selected value=" +  result_content.date[0].yearMonth + ">" + result_content.date[0].yearMonth + "</option>";}
					if (result_content.date.length==0){
						alert("此學校無菜單資料");
						$('#calendar').fullCalendar('removeEvents');
						$("#select_yearMonth").empty();
						return ;
					}
					
					for(var i=0; i<result_content.date.length; i++) {
						if(i==0){
							drop_down_date += "<option selected value=" 
								+  result_content.date[i].yearMonth + ">" 
								+ result_content.date[i].yearMonth + "</option>";
							continue;
						}
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
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
		}
    }
	//查詢某月學校的供餐廠商
	function queryMenu() {
		//check empty
		var s = document.getElementById("select_school");
		var sid = s.options[s.selectedIndex].value;
		
		var ym = document.getElementById("select_yearMonth");
		var query_date = ym.options[ym.selectedIndex].value;
		
		if(sid == 0){
			//show empty calender if no cookie
			var schoolcookie = getCookie("school");
			if(schoolcookie != ""){
			alert("您尚未選擇學校");
			return false;
			}
			$('#calendar').fullCalendar({
					header: {
						left: '',		//prev,next today
						center: 'title',
						right: ''		//month,agendaWeek,agendaDay
					},
					editable: false,
				   
					monthNames:['一月','二月', '三月', '四月', '五月', '六月', '七月','八月', '九月', '十月', '十一月', '十二月'],
					
				});
			return;
		} else if (query_date == 0) {
			//alert("您尚未選擇月份");
			alert("學校無菜單資料");
			$("#calendar").html("");
			$('#calendar').fullCalendar({
				header: {
					left: '',		//prev,next today
					center: 'title',
					right: ''		//month,agendaWeek,agendaDay
				},
				editable: false,
			   
				monthNames:['一月','二月', '三月', '四月', '五月', '六月', '七月','八月', '九月', '十月', '十一月', '十二月'],
				
			});
			return;
		}
		
		var counties = $("#select_counties").val();
		var area = $("#select_area").val();
		var school = $("#select_school").val();
		var yearMonth = $("#select_yearMonth").val();
		
		setCookie("counties", counties);
		setCookie("area", area);
		setCookie("school", school);
		setCookie("yearMonth", yearMonth);
		//alert(counties +","+ area +","+ school +","+ yearMonth);
		
		//var sid = $("#select_school").attr("value");
		
		$("#calendar").html("");
		
		
		
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
					monthNames:['一月','二月', '三月', '四月', '五月', '六月', '七月','八月', '九月', '十月', '十一月', '十二月'],
					
					eventRender: function(event, element) {
			            element.attr('title', event.tip);
			        },
					
			        eventClick: function(event) {
				        if (event.mid) {
				        	query_detail_data(event.mid, 1);
				        }
				    }
				});
				
				//$("#dropDown_yearMonth").html("");
			
				//$("#dropDown_yearMonth").append(drop_down_date);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
		}
    }
	
	//tabs元件
	$(function() {
		$( "#tabs" ).tabs();
		
		$(".nexttab").click(function() {
			$( "#tabs" ).tabs();
			$( "#tabs" ).tabs( "option", "active", 1 );
		});
		//tabs color
	//	$(".ui-tabs .ui-tabs-nav a").css("background-color","#99D85D");
	//	$(".ui-tabs .ui-tabs-selected a").css("background-color","#1A9660");
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
	*/
	
	function setCookie(name,value) {
		var d = new Date();
		d.setTime(d.getTime()+(365*24*60*60*1000));
		var expires = "expires="+d.toGMTString();
		document.cookie = name+"="+value+"; "+expires;
	}
	
	function nextTab() {
		/* $(".nexttab").click(function() {
		    var selected = $("#tabs").tabs("option", "selected");
		    $("#tabs").tabs("option", "selected", selected + 1);
		}); */
		$(function() {
		    $( "#tabs" ).tabs();
		    $( "#tabs" ).tabs( "option", "active", 1 );
		});
	}
	
	function getCookie(name) {
		if(typeof String.prototype.trim !== 'function') {
			  String.prototype.trim = function() {
			    return this.replace(/^\s+|\s+$/g, ''); 
			  }
			}
		var name = name + "=";
		var ca = document.cookie.split(';');
		for(var i=0; i<ca.length; i++) {
			var c = ca[i].trim();
		  	if (c.indexOf(name)==0) return c.substring(name.length,c.length);
		}
		return "";
	}
		
	function QueryCertificate(authenticate,authenticateID,labelId){
							var label = "#auth"+labelId;
							if($("#select_counties").val() != "17"){
								switch  (authenticate){
									case "CAS":
										//getCAS(authenticateID,label);
										break;
									case "吉園圃":
										getGAP(authenticateID,label);
										break;
								}
							}
	}
	//認證標章追朔 CAS追溯
	function  getCAS(qNo,label){
		if(qNo.length < 6 || qNo.length > 6 ){
		$(label).html("");
			$(label).html("<input type=\"hidden\" value=\"CAS序號格式錯誤:"+qNo+"\">");
			//alert("CAS序號格式錯誤");
		}else{
		 $.get("http://data.coa.gov.tw:8080/od/data/api/eir06/?$format=json&$top=500&$skip=0&$filter=pno+like+"+qNo,function(data){
			 if(data.responseText != ''){
				 $(label).html(data.responseText);
					var jsonContent=$(label).find("p").html();
					var myJsonObj = $.parseJSON(jsonContent);
					if(myJsonObj==""){			
						$(label).html("<input type=\"hidden\" value=\"CAS序號追尋沒有資料:"+qNo+"\">");
					}else{
						 $(label).html("<a target=\"_blank\" href=\"http://en.cas.org.tw/en/product/01.php?pno="+qNo+"\">CAS</a>");
					}
			 }
			else{
				$(label).html("<input type=\"hidden\" value=\"CAS序號追尋沒有資料:"+qNo+"\">");	
			}
			
		 }).fail(function (){
				$(label).html("<input type=\"hidden\" value=\"CAS公開資訊站無法連線\">");//alert("無法連接到CAS頁面")
			 });
		 }
	}

	//認證標章追朔 吉園圃追溯
	function getGAP(qNo,label){
	//$("#msg").html("查詢中..............");
	 $.get("http://agrapp.coa.gov.tw/GAPWEB/api/label_query/label/14"+qNo,function(data){
		 $(label).html(data.responseText);
		 //alert(data.responseText);
		var jsonContent=$(label).find("p").html();
		//alert(jsonContent);
		//alert($.parseJSON(jsonContent));
		var myJsonObj = $.parseJSON(jsonContent);
		//alert(myJsonObj);
		//alert(myJsonObj.farmerModel);
		//if (!data.responseText){
		if(!myJsonObj.farmerModel){
			//alert("吉園圃序號填寫有誤"+data.responseText);
			$(label).html("<input type=\"hidden\" value=\"吉園圃序號追尋沒有資料:"+qNo+"\">");
		}else{
			//$(label).html("<a class = \"fancybox fancybox.iframe\" target=\"_blank\" href=\"http://gap.afa.gov.tw/labelresult/?s="+qNo+"\">吉園圃</a>");
			$(label).html("<a  target=\"_blank\" href=\"http://gap.afa.gov.tw/labelresult/?s="+qNo+"\"><u>吉園圃</u></a>");
		}	
		
	 }).fail(function (){// alert("無法連接到GAP頁面")
		 $(label).html("<input type=\"hidden\" value=\"吉園圃公開資訊站無法連線\">");//alert("無法連接到CAS頁面")
		 });

	}
		 
	//(二階查詢)食品雲資料查詢
	function getTwfoodtrace(companyId,productName,dom){
		//20140522 Raymond 判斷有無在二階口袋company裡面 有的話才會去跟二階要資料
		
		if($.inArray(companyId,fcloudCounpanyIdList) < 0)
			return;
		
		var tmpProdName = productName;
		
		if(companyId === '70390831'){
			if(productName === '腿排丁')
				tmpProdName = '(基底)排丁';
			if(productName === '雞翅')
				tmpProdName = '三節翅W6';
			if(productName === '胸丁')
				tmpProdName = '(基底)雞丁';
		}
		if(companyId === '64217504'){
			if(productName === '青江菜')
				tmpProdName = '青江段';
		}

		var url = fcloudQuerySourceidUrl + '&companyId='+companyId+'&sourceName='+tmpProdName;
		$.get(url,function(result){
			if(result.responseText != ''){
				console.log(result.responseText);
				$(dom).html(result.responseText);
				var foodId = $(dom).find("p").html();
				if(foodId!='-1'){
					var link = "<a target='_blank' style='font-size:15px;' href='"+fcloudProductLinkUrl+"&sourceId="+foodId+"&companyId="+companyId+"&searchType=1'><u>" + productName + "</u></a>";
					$(dom).html(link);
				}
				else{
					$(dom).html(productName);
				}
				
			}
		}).fail(function (){
			console.log('fail');
		});
		
	}
	
	//查詢事件
	function query_detail_data(mid , check){

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
				/*
				if(check == "0"){
					if(result_content.midBefore == 0){
						alert("無前一日資料");
						return;
					} else if (result_content.midAfter == 0){
						alert("無後一日資料");
						return;
					}
				}
				*/
				
				$("#lunch_content").html("");
				$("#food_info").html("");
				$("#seasoning_info").html("");
				$("#controller").html("");
				$("#query_detail_info").html("");
			
				//標題
				var new_div = "<div id=\"MAIN_SECOND_BAR\" class=\"GRA_ABR h_40px lh_40\"><div class=\"SECOND_TXT\">" + result_content.schoolName 
					+" : "+ result_content.date + " 營養午餐</div></div><div id=\"MAIM_LINER_A\"></div>";
				
				//營養份量
				new_div += "<div id=\"TAB_TYP_A\" class=\"\">";
					new_div += "<div class=\"TAB_TY_A\">";
						new_div += "<table width=\"290\" border=\"1\">";
							new_div += "<tr>";
								new_div += "<td colspan=\"2\" align=\"center\" bgcolor=\"#678948\" class=\"TIT_A\">營養份量</td>";
							new_div += "</tr>";
							new_div += "<tr bgcolor=\"#fffff4\">";
							new_div += "<td align=\"right\">全榖根莖類</td>";
							new_div += "<td align=\"left\">"+result_content.nutrition.mainFood +"份</td>";
						new_div += "</tr>";
		
						new_div += "<tr bgcolor=\"#ffffff\">";
							new_div += "<td align=\"right\">蔬菜類</td>";
							new_div += "<td align=\"left\">"+ result_content.nutrition.vegetable +"份</td>";
						new_div += "</tr>";
		
						new_div += "<tr bgcolor=\"#fffff4\">";
							new_div += "<td align=\"right\" >油脂與堅果種子類</td>";
							new_div += "<td align=\"left\">"+ result_content.nutrition.oil +"份</td>";
						new_div += "</tr>";
						
						new_div += "<tr  bgcolor=\"#ffffff\">";
							new_div += "<td align=\"right\">蛋豆魚肉類</td>";
							new_div += "<td align=\"left\">"+ result_content.nutrition.meatBeans +"份</td>";
						new_div += "</tr>";
		
						new_div += "<tr bgcolor=\"#fffff4\">";
							new_div += "<td align=\"right\">水果類</td>";
							new_div += "<td align=\"left\">"+ result_content.nutrition.fruit +"份</td>";								
							new_div += "</tr>";
				
						new_div += "<tr bgcolor=\"#ffffff\">";
							new_div += "<td align=\"right\">乳品類</td>";
							new_div += "<td align=\"left\">"+ result_content.nutrition.milk +"份</td>";
						new_div += "</tr>";
			
						new_div += "<tr bgcolor=\"#fffff4\">";
							new_div += "<td align=\"right\">熱量</td>";			
							new_div += "<td align=\"left\">"+ result_content.nutrition.calories +"大卡</td>";								
						new_div += "</tr>";
						new_div += "</table>";
					new_div += "</div>";
				new_div += "<div class=\"SPP_A\"></div>";
		            	
						//供餐者資訊)
						new_div += "<div class=\"TAB_TY_A\">";
							new_div += "<table width=\"290\" border=\"1\">";
									
						if(result_content.supplierInfo.kitchenType=="006"){//自立廚房

							new_div += "<tr>";
							new_div += " <td colspan=\"2\" align=\"center\" bgcolor=\"#678948\" class=\"TIT_A\">供餐者資訊</td>";
						new_div += "</tr>";
			
						new_div += "<tr bgcolor=\"#fffff4\">";
							new_div += "<td align=\"right\"  width = '80px'>供應學校</td>";
							new_div += "<td align=\"left\">"+ result_content.supplierInfo.supplierName +"</td>";
						new_div += "</tr>";
						
						new_div += "<tr bgcolor=\"#ffffff\">";
							new_div += "<td align=\"right\">學校地址</td>";
							new_div += "<td align=\"left\">"+ result_content.supplierInfo.supplierAddress +"</td>";
						new_div += "</tr>";
						
						new_div += "<tr bgcolor=\"#fffff4\">";
							new_div += "<td align=\"right\">學校電話</td>";
							new_div += "<td align=\"left\">"+ result_content.supplierInfo.supplierPhone +"</td>";
						new_div += "</tr>";		
							
						new_div += "<tr bgcolor=\"#ffffff\">";
							new_div += "<td align=\"right\">營養師/<br>午餐秘書</td>";
							new_div += "<td align=\"left\">"+ result_content.supplierInfo.dietitians +"</td>";
						new_div += "</tr>";
						
									
						}else{ //業者與其他
							new_div += "<tr>";
							new_div += " <td colspan=\"2\" align=\"center\" bgcolor=\"#678948\" class=\"TIT_A\">供餐者資訊</td>";
						new_div += "</tr>";
			
						new_div += "<tr bgcolor=\"#fffff4\" >";
							new_div += "<td align=\"right\"  width = '80px'>供應商</td>";
							new_div += "<td align=\"left\">"+ result_content.supplierInfo.supplierName +"</td>";
						new_div += "</tr>";
						
						new_div += "<tr bgcolor=\"#ffffff\">";
							new_div += "<td align=\"right\">供應商地址</td>";
							new_div += "<td align=\"left\">"+ result_content.supplierInfo.supplierAddress +"</td>";
						new_div += "</tr>";
						
						new_div += "<tr bgcolor=\"#fffff4\">";
							new_div += "<td align=\"right\">供應商電話</td>";
							new_div += "<td align=\"left\">"+ result_content.supplierInfo.supplierPhone +"</td>";
						new_div += "</tr>";		
							
						new_div += "<tr bgcolor=\"#ffffff\">";
						new_div += "<td align=\"right\">營養師</td>";
						new_div += "<td align=\"left\">"+ result_content.supplierInfo.dietitians +"</td>";
					new_div += "</tr>";
					
					new_div += "<tr bgcolor=\"#fffff4\">";
						new_div += "<td align=\"right\">衛管人員</td>";
						new_div += "<td align=\"left\">"+ result_content.supplierInfo.supplierLeader +"</td>";
					new_div += "</tr>";
					/* 
					new_div += "<tr bgcolor=\"#ffffff\">";
						new_div += "<td align=\"right\" style=\"width:90px;\">品質認證體系</td>";
						new_div += "<td align=\"left\">"+ result_content.supplierInfo.authenticate +"</td>";
					new_div += "</tr>"; */
						}
						
								new_div += "</table>";
							new_div += "</div>";
						new_div += "</div><div id=\"MAIM_LINER_A\" class=\"\"></div>";
				
				$("#query_detail_info").append(new_div);
				
				//控制項(上一頁、前一日、後一日)
				var controller_div = "<div class=\"w_100ps h_44px tal_ch GOBK_BBT\"><a href=\"#\"  onclick=\"controller()\" >回前頁</a> ";
				if(result_content.midBefore != 0) {
					controller_div += "<a href=\"#\" onclick=\"query_detail_data(" + result_content.midBefore + ",0" +")\">前一日</a> ";
				}
				if(result_content.midAfter != 0){
					controller_div += "<a href=\"#\" onclick=\"query_detail_data(" + result_content.midAfter + ",0" + ")\">下一日</a></div><div id=\"MAIM_LINER_A\" ></div>";
				}
				
				//alert(result_content.midAfter);
				
				$("#controller").append(controller_div);
				
				//午餐內容
				var lunch_content_div = "<div class=\"MENU_BOX\"><ul>";
					for(var k=0; k<result_content.lunchContent.length; k++){
						// if(k%3 == 0 && k!=0) { lunch_content_div += "<tr>";	var count = 0;}
							lunch_content_div +="<li>";
							if(result_content.lunchContent[k].image){
									lunch_content_div +="<a href=\"#\" onclick=\"nextTab()\" ><img src=\"" + result_content.lunchContent[k].image + "\"  width=\"150\" height=\"130\" /></a>";}
									lunch_content_div +="<div class=\"menu_tit_txt\">" + result_content.lunchContent[k].category + "</div>";
									lunch_content_div +="<div class=\"menu_ini_txt\">" + result_content.lunchContent[k].foodName + "</div>";
									
							lunch_content_div +="</li>";
							//count ++;
						//if(count%3 == 0 || result_content.lunchContent.length == (k+1)) { lunch_content_div += "</tr>"; }
					}
					
					
				lunch_content_div +="</ul><div style=\"text-align:center;\">菜色圖片為示意圖</div></div>";
				$("#lunch_content").append(lunch_content_div);

				//食材資訊
				var food_info_div = "<div class=\"TAB_TY_C\"><table class=\"component TAB_TY_C\">";
					food_info_div += "<tr>";
					food_info_div += "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">菜色</td>" +
					 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">原料</td>" +
				 	 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">品牌</td>" +
					 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">認證標章</td>" +
					 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">供應商名稱</td>";
					food_info_div += "</tr>";
					
					for(var i=0; i<result_content.foodInfo.length; i++){
						//認證標章追朔
						if(result_content.foodInfo[i].authenticate != "" && result_content.foodInfo[i].authenticateId != ""){
							QueryCertificate(result_content.foodInfo[i].authenticate,result_content.foodInfo[i].authenticateId,i);}
					
						
						food_info_div += "<tr>";
						food_info_div += "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += result_content.foodInfo[i].foodName + "</td>" +
										 "<td  id=\"materialName"+i+"\" align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += result_content.foodInfo[i].material + "</td>" +
									 	 "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += result_content.foodInfo[i].brand + "</td>" +
										 "<td  id=\"materialCert"+i+"\" align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += "<label id=\"auth"+i+"\"></label><div id=\"result"+i+"\"></div></td>" +
										 "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += result_content.foodInfo[i].source + "</td>";
						food_info_div += "</tr>";
						
						//CAS認證資訊
						if(result_content.foodInfo[i].ingredientCertificateObject != null){
							//console.log(result_content.foodInfo[i].ingredientCertificateObject);
							createCasCertInfo(result_content.foodInfo[i].ingredientCertificateObject,'materialCert'+i);
						}
					}
				food_info_div +="</table></div>";
				$("#food_info").append(food_info_div);
				
				
				
				
				//調味料
				var seasoning_div = "<div class=\"TAB_TY_C\"><table class=\"component\">";
					seasoning_div += "<tr>";
						seasoning_div += "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">原料</td>" +
					 	 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">品牌</td>" +
						 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">認證標章</td>" +
						 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">來源</td>";
					seasoning_div += "</tr>";
					
					for(var j=0; j<result_content.seasoning.length; j++){
						//認證標章追朔
						if(result_content.seasoning[j].authenticate != "" && result_content.seasoning[j].authenticateId != ""){
							QueryCertificate(result_content.seasoning[j].authenticate,result_content.seasoning[j].authenticateId,"S"+j);}

						seasoning_div += "<tr>";
						seasoning_div += "<td id=\"seasoningName"+j+"\"  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2'>" ;} seasoning_div +=  result_content.seasoning[j].material + "</td>" +
						"<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2'>" ;} seasoning_div += result_content.seasoning[j].brand + "</td>" +
						"<td  id=\"seasoningCert"+j+"\" align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2'>" ;} seasoning_div +=  "<label id=\"authS"+j+"\"></label><div id=\"resultS"+j+"\"></div></td>" +
						"<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2'>" ;} seasoning_div +=  result_content.seasoning[j].source + "</td>";
						seasoning_div += "</tr>";
						
						//CAS認證資訊
						if(result_content.seasoning[j].ingredientCertificateObject != null){
							//console.log(result_content.foodInfo[i].ingredientCertificateObject);
							createCasCertInfo(result_content.seasoning[i].ingredientCertificateObject,'seasoningCert'+i);
						}
					}
					
				seasoning_div +="</table></div>";
				$("#seasoning_info").append(seasoning_div);
				
				//20140425 Raymond 如地區選擇台北市  不顯試認證標章資訊
				if($("#select_counties").val() != "17"){
					//assign a 給有CAS資訊的食材
					for(var i=0;i<result_content.foodInfo.length;i++){
						if(result_content.foodInfo[i].ingredientCertificateObject != null){
							var method = 'showCertInfo("materialCert' +i+ '")';
							var link = "<a style='font-size:15px;' href='#' onclick='"+method+";return false;'><u> CAS </u></a>";
							$('#materialCert' + i).append(link);
						}
					}
					
					//assign a 給有CAS資訊的調味料
					for(var i=0;i<result_content.seasoning.length;i++){
						if(result_content.seasoning[i].ingredientCertificateObject != null){
							var method = 'showCertInfo("seasoningCert' +i+ '")';
							var link = "<a style='font-size:15px;' href='#' onclick='"+method+";return false;'><u> CAS </u></a>";
							$('#seasoningCert' + i).append(link);
						}
					}
				}
				
				
				//GMP食材,調味料資料
				var materialList = $("td[id^='materialName']");
				for(var i=0;i<materialList.length;i++){
					getTwfoodtrace(result_content.foodInfo[i].supplierCompanyId,result_content.foodInfo[i].material,'#'+materialList[i].id);
				}
				var seasoningList = $("td[id^='seasoningName']");
				for(var i=0;i<seasoningList.length;i++){
					getTwfoodtrace(result_content.seasoning[i].supplierCompanyId,result_content.seasoning[i].material,'#'+seasoningList[i].id);
				}

			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
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
	
	function createCasCertInfo(certInfo,serno){
		var certDiv = '';
		certDiv += "<div class=\"TAB_TY_A\" id=\""+ "div" + serno +"\" title=\"CAS認證廠商資訊\">";
		certDiv += "<table  border=\"1\">";
			
			certDiv += "<tr bgcolor=\"#fffff4\">";
			certDiv += "<td align=\"right\">認證編號</td>";
			certDiv += "<td align=\"left\">"+certInfo.certNo +"</td>";
		certDiv += "</tr>";

		certDiv += "<tr bgcolor=\"#ffffff\">";
			certDiv += "<td align=\"right\">公司名稱</td>";
			certDiv += "<td align=\"left\">"+ certInfo.companyName +"</td>";
		certDiv += "</tr>";
		
		certDiv += "<tr bgcolor=\"#ffffff\">";
			certDiv += "<td align=\"right\">公司統一編號</td>";
			certDiv += "<td align=\"left\">"+ certInfo.companyId +"</td>";
		certDiv += "</tr>";
		
		certDiv += "<tr bgcolor=\"#ffffff\">";
			certDiv += "<td align=\"right\">負責人</td>";
			certDiv += "<td align=\"left\">"+ certInfo.director +"</td>";
		certDiv += "</tr>";
			
		certDiv += "<tr bgcolor=\"#ffffff\">";
			certDiv += "<td align=\"right\">地址</td>";
			certDiv += "<td align=\"left\">"+ certInfo.address +"</td>";
		certDiv += "</tr>";
		
		certDiv += "<tr bgcolor=\"#ffffff\">";
			certDiv += "<td align=\"right\">連絡電話</td>";
			certDiv += "<td align=\"left\">"+ certInfo.tel +"</td>";
		certDiv += "</tr>";
		
		certDiv += "<tr bgcolor=\"#ffffff\">";
			certDiv += "<td align=\"right\">傳真</td>";
			certDiv += "<td align=\"left\">"+ certInfo.fax +"</td>";
		certDiv += "</tr>";

		certDiv += "<tr bgcolor=\"#ffffff\">";
			certDiv += "<td align=\"right\">網站</td>";
			//certDiv += "<td align=\"left\">"+ certInfo.website +"</td>";
			certDiv += "<td align=\"left\"></td>";
		certDiv += "</tr>";
		
		certDiv += "</table>";
		certDiv += "</div>";
		
		$("#cert_info").append(certDiv);

	}

	
	function showCertInfo(linkid){
		$(".ui-dialog-content").dialog("close");
		$("#div"+linkid).dialog();
	}

	

</script>
</head>
<body>
	<div id="MAIN_TITLE_BAR" class="GRE_ABR h_40px lh_40">
		<div class="TITLE_TXT">菜單查詢</div>
	</div>
	<div id="dss" class="GRA_ABR">
		<div class="SECOND_TXT">
			<table class="lh_30" width="100%">
				<tr>
					<td colspan="8"></td>
				</tr>
				<tr class="h_40px lh_40">
					<td align="left" valign="middle" nowrap="nowrap">&nbsp;&nbsp;縣市別:&nbsp;</td>
					<td align="left" valign="middle"><select id="dropDown_counties"></select></td>
					<td align="left" valign="middle" nowrap="nowrap">&nbsp;&nbsp;市/區別:&nbsp;</td>

					<td align="left" valign="middle"><span id="dropDown_area"></td>
					<td align="left" valign="middle">&nbsp;&nbsp;學校名稱:&nbsp;</td>

					<td align="left" valign="middle"><span id="dropDown_school"></td>
					<td align="left" valign="middle">&nbsp;&nbsp;年/月:&nbsp;</td>
					<td align="left" valign="middle"><span id="dropDown_yearMonth"></td>

				</tr>
				<tr class="h_40px lh_40">
					<td colspan="8" align="center" class="BT_IN_BBTER">
						<div style="font-weight: bold;">
							<a href="#" onclick="queryMenu()" onkeypress="queryProduct()" />查詢</a>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="9"></td>
				</tr>
			</table>
		</div>
	</div>

	<div id='calendar'></div>

	<div id="query_detail_info"></div>

	<div id='controller'></div>

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

	<div id="cert_info" style="display: none"></div>
</body>
</html>