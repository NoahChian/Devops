<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../util.jsp" %> <!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta HTTP-equiv="Expires" content="0">
<meta HTTP-equiv="kiben" content="no-cache">
<!-- full screen calendar -->
<!--script src="../../js/jquery-ui.min.js"></script-->
<link href="../../css/fullcalendar.css" rel="stylesheet" type="text/css" />
<script src="../../js/fullcalendar.js"></script>
<script src="../../js/jquery.xdomainajax.js" type="text/javascript"></script>
<!-- script src="../../js/system-setting.js"></script-->
<script src="../../js/nodeJsSrv_util.js" type="text/javascript"></script>
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

/** 菜單種類顏色標示 **/
.marker{
	float: left;
	height: 16px;
    line-height: 16px;
    margin-top: 10px
}
.marker div{
	float:left;
	margin-left: 2px;
}

.marker div div{
	float:left;
	width: 16px;
	height: 16px;	
}

.marker .marker_breakfast div{
	background-color: rgb(134, 95, 125);
}
.marker .marker_lunch div{
	background-color: #567b71;
}
.marker .marker_dessert div{
	background-color: rgb(63, 98, 173);
}

.ui-button-text{
	font-size: 0%;
}
 
#fdalink {
	color: #ffffff; 
	PADDING-RIGHT: 1px; 
	PADDING-LEFT: 1px; 
	PADDING-BOTTOM: 0.5px; 
	PADDING-TOP: 0.5px; 
	background-color:#35d468; 
	height: 20px; width: 20px; 
	text-align: center; ; 
	border: #5DFF4F; 
	border-style: outset; 
	border-top-width: 1px; 
	border-right-width: 1px; 
	border-bottom-width: 1px; 
	border-left-width: 1px;
	}
#fdalink:link {
	text-decoration:none;
	} 
#fdalink:hover { 
	BORDER-RIGHT: #E0FF9B 1px outset; 
	PADDING-RIGHT: 1px; 
	BORDER-TOP: #E0FF9B 1px outset; 
	PADDING-LEFT: 1px; 
	PADDING-BOTTOM: 1px; 
	BORDER-LEFT: #E0FF9B 1px outset; 
	PADDING-TOP: 1px; 
	BORDER-BOTTOM: #E0FF9B 1px outset;
	background-color:#008080; 
	height: 20px; width: 20px; 
	text-align: center;
	text-decoration:underline;
	} 
</style>

<%
	String uName = "";
	int sessionSid = 0;
	String account = "";		// session("account")這是記錄kitchenId
	if(session.getAttribute("account")!=null){
		
		String tmpSessionAccount = (String) session.getAttribute("account");
		byte[] theBytes = tmpSessionAccount.getBytes();
		if(theBytes[0] >= 'A' && theBytes[0] <= 'Z') {
			uName = (String) session.getAttribute("uName");
			sessionSid = Integer.parseInt(uName.substring(1, 7));
			account = (String) session.getAttribute("account");
		}
	}
%>

<script>
	
	var cid = getCookie("counties");
	var aid = getCookie("area");
	var sid = getCookie("school");
	var query_date = getCookie("yearMonth");
		
	var drop_down_default = "<select><option value=\"0\">請選擇</option></select>";
	
	//縣市
	function counties(){
		var counties = getCookie("counties");
		var request_data =	{
				"method":"customerQueryCounties",
	 				"args":{
	 					"condition":1
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
						if(result_content.counties[i].cid == counties){	//新北市
							drop_down_counties += "<option selected value=" + result_content.counties[i].cid + ">" + result_content.counties[i].countiesName + "</option>";
							continue;
						}
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
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
		}

	}
	
	
	//縣市改變修改區域
	function area() {
		//清空
		$("#sf_link").html("");
		$("#dropDown_area").html("");
		$("#dropDown_school").html("");
		
		var area = getCookie("area");
    	var counties = $("#select_counties").val();
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
						if(result_content.area[i].aid == area){	//萬里區
							drop_down_area += "<option selected value=" +  result_content.area[i].aid + ">" + result_content.area[i].areaName + "</option>";
							continue;
						}
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
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
		}
    }
	
	//區域改變修改學校
	function school() {
		//清空
		$("#sf_link").html("");
		$("#dropDown_school").html("");
    	
		var school = getCookie("school");
		var area = $("#select_area").val();
    	var countiesName = $("#select_counties").find('option:selected').text();
		var areaName = $("#select_area").find('option:selected').text();
		
    	var request_data =	{
				 "method":"customerQuerySchool",
	 				"args":{
	 					"aid": area
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			
			if(result_content.resStatus == 1){	//success
				var drop_down_school = "<select id=\"select_school\" onChange=\"menu()\">";
					drop_down_school += "<option value=\"0\">請選擇</option>";
					for(var i=0; i<result_content.school.length; i++) {
						var sch_Name1 = result_content.school[i].schoolName.replace(countiesName+"立","");
						var sch_Name0 = sch_Name1.replace(countiesName,"");
						var sch_Name = sch_Name0.replace(areaName,"");
						if(result_content.school[i].sid == school) {	//市立萬里國中
							drop_down_school += "<option selected value=" +  result_content.school[i].sid + ">" + sch_Name + "</option>";
							continue;
						}
						drop_down_school += "<option value=" +  result_content.school[i].sid + ">" + sch_Name + "</option>";
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
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
		}
    }
	// TODO
	//學校改變年月
	function menu() {
		var sid = <%=sessionSid%> != 0 ? <%=sessionSid%> : $("#select_school").val();
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
								if(i==0){
							drop_down_date += "<option selected value=" +  result_content.date[i].yearMonth + ">" + result_content.date[i].yearMonth + "</option>";
							continue;
						}
						drop_down_date += "<option value=" +  result_content.date[i].yearMonth + ">" + result_content.date[i].yearMonth + "</option>";
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

		// 員生消費合作社
		// 當學校名稱下拉有改變時，則依此學校id做查詢。
		var sfsid = $("#select_school").val()!=0 ? $("#select_school").val() : sid;
		$("#sf_link").html("");
		var request_data =	{
				 "method":"customerQuerySF",
	 				"args":{
	 					"pageNum":1,
	 				    "pageLimit":200,
	 					"sid" : sfsid
	 				}
				};

		//var response_obj = call_rest_api(request_data);
		console.log();
		if(!$.browser.msie){
			var apUrl = "/cateringservice/rest/API/";
			var response_obj = ajaxCallJsonp(url+apUrl, request_data, function(datas) {
				var data = datas.data;
				//console.log("index.jsp..........."+JSON.stringify(data));
				if( data !== undefined ) {
					if(data.resStatus === 1){
						var result_content = data;
						if(result_content.totalNum>0){
							$("#sf_link").html("");
							$("#sf_link").append("<a href=\"../custom/customerQuerySF/"+sfsid+"/\" target=\"_blank\" \">員生消費合作社</a>");
						}else{
							$("#sf_link").html("");
						}
					}
				}

			});
		}	
    }
	
	//查詢某月學校的供餐廠商
	function queryMenu(queryWay) {
		var c = document.getElementById("select_counties");
		var a = document.getElementById("select_area");
		var s = document.getElementById("select_school");
		var ym = document.getElementById("select_yearMonth");
		
		// 改判斷方式跳出提醒 edit by Joshua 2014/10/06
		if(queryWay === 'manual'){
			// 縣市判斷 edit by Joshua 2014/10/06
			cid = c.options[c.selectedIndex].value;
			
			if(cid == 0){ 
				alert("您尚未選擇縣市別");
				return false;
			}
			
			// 市/區別判斷
			aid = a.options[a.selectedIndex].value;
		
			if(aid == 0){
				alert("您尚未選擇市/區別");
				return false;	
			}
			
			// 學校判斷
			sid = s.options[s.selectedIndex].value;
			
			if(sid == 0){
				alert("您尚未選擇學校");
				return false;	
			}
			
			// 日期判斷
			query_date = ym.options[ym.selectedIndex].value;
			if(query_date == 0){
				alert("您尚未選擇日期");
				return false;	
			}
		} 
		setCookie("counties", cid);
		setCookie("area", aid);
		setCookie("school", sid);
		setCookie("yearMonth", query_date);
		
		$("#calendar").html("");
		
    	var request_data =	{
				 "method":"customerQueryCateringBySchoolAndDate_v2",
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
				var m = 0;
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
			            //20150507 shine add 依菜單種類不同,標示不同的顏色
			            switch(event.menuType){
			            	case 0:
			            		element.css("background-color","rgb(134, 95, 125)");
			            		m = m | 1;   //001
			            		break;
			            	case 1:
			            		//不改顏色,使用預設的 #567b71
			            		m = m | 2;   //010
			            		break;
			            	case 2:
			            		element.css("background-color","rgb(63, 98, 173)");
			            		m = m | 4;   //100
			            		break;
			            }
			        },
			        
			        eventAfterAllRender: function(view){
			        	//20150507 shine add 增加顏色對應菜單種類的標示
			        	//判斷有2種以上的餐時才顯示
			        	if(m == 3 || m >= 5){
				        	var marker_div = "";
				        	marker_div += "<div class='marker'> ";
				        	marker_div += "	<div class='marker_breakfast'> ";
				        	marker_div += "		<div></div> ";
				        	marker_div += "		<span>早點</span> ";
				        	marker_div += "	</div> ";
				        	marker_div += "	<div class='marker_lunch'> ";
				        	marker_div += "		<div></div> ";
				        	marker_div += "		<span>午餐</span> ";
				        	marker_div += "	</div> ";
				        	marker_div += "	<div class='marker_dessert'> ";
				        	marker_div += "		<div></div> ";
				        	marker_div += "		<span>點心</span> ";
				        	marker_div += "	</div> ";
				        	marker_div += "</div> ";
				        	
				        	$(".fc-header-right").append($(marker_div));
			        	}
			        },
			        
			        eventClick: function(event) {
				        if (event.mid) {
				        	//20150504 shine mod 加傳menuType
				        	query_detail_data(event.mid, 1,event.menuType);
				        }
				    }
				});
				
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
			  };
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
	//$("#msg").html("查詢中..............");
	if(qNo.length < 6 || qNo.length > 6 ){
	$(label).html("");
		$(label).html("<input type=\"hidden\" value=\"CAS序號格式錯誤:"+qNo+"\">");
		//alert("CAS序號格式錯誤");
	}else{
	 $.get("http://data.coa.gov.tw:8080/od/data/api/eir06/?$format=json&$top=500&$skip=0&$filter=pno+like+"+qNo,function(data){
		 if(data.responseText != ''){
			 $(label).html(data.responseText);
				// alert(data.responseText);
				var jsonContent=$(label).find("p").html();
				//alert(jsonContent);
				//alert($.parseJSON(jsonContent));
				var myJsonObj = $.parseJSON(jsonContent);
				//alert(myJsonObj);
				//alert(myJsonObj.name);
				//if (!data.responseText){
				if(myJsonObj==""){			
					//alert("CAS序號填寫有誤"+data.responseText);
					$(label).html("<input type=\"hidden\" value=\"CAS序號追尋沒有資料:"+qNo+"\">");
					//alert("CAS序號追尋沒有資料");
				}else{
					//$(label).html("<a class = \"fancybox fancybox.iframe\" href=\"http://en.cas.org.tw/en/product/01.php?pno="+qNo+"\">CAS</a>");
					 $(label).html("<a target=\"_blank\" href=\"http://en.cas.org.tw/en/product/01.php?pno="+qNo+"\">CAS</a>");
				}
		 }
		else{
			$(label).html("<input type=\"hidden\" value=\"CAS序號追尋沒有資料:"+qNo+"\">");	
		}
		
	 }).fail(function (){
			$(label).html("<input type=\"hidden\" value=\"CAS公開資訊站無法連線\">");//alert("無法連接到CAS頁面")
		 });}
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
		//一次查詢太多會造成DDOS攻擊  目前先直接return 不查詢
		//return;
		//var url = 'http://175.98.115.19:12002/fcloudSOAP/index.do?action=querySourceId&companyId=07569627&sourceName=%E5%8D%A1%E8%BF%AA%E9%82%A3%E9%A6%99%E8%8A%8B%E8%84%86%E7%89%87%E5%8E%9F%E5%91%B362G';
		
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
	function query_detail_data(mid , check, menuType){
		var response_obj;
		//if(menuType == 1){
			var request_data =	{
				 "method":"customerQueryMenuDetailInfo_v2",
	 				"args":{
	 					"mid" : mid
	 				}
				};
			response_obj = call_rest_api(request_data);
		/* 
		}else{
			//http://localhost/offered/meal?SchoolId=64740168&period=2015/05/01,2015/05/01			
			var apUrl = "/meal/" + mid; 
			response_obj = call_rest_api({},url+apUrl,"GET");
			response_obj.result_content = response_obj.data;
		} 
		*/
		
		
		
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
			
				console.log("menuType ==> " + result_content.menuType);
				
				//20150501 shine add 依菜單種類顯示不同的標題
				var menuType = result_content.menuType;
				var menuTitle = "";
				
				if(menuType == 0){
					menuTitle = "早點";
				}else if(menuType == 1){
					menuTitle = "營養午餐";
				}else if(menuType == 2){
					menuTitle = "點心";
				}
				
				//標題
				var new_div = "<div id=\"MAIN_SECOND_BAR\" class=\"GRA_ABR h_40px lh_40\"><div class=\"SECOND_TXT\">" + result_content.schoolName 
					+" : "+ result_content.date + " " + menuTitle + "</div></div><div id=\"MAIM_LINER_A\"></div>";
				
				//fda trace test
			    
				
				//營養份量
				new_div += "<div id=\"TAB_TYP_A\" class=\"\">";
					new_div += "<div class=\"TAB_TY_A\" >";
						new_div += "<table width=\"290\" border=\"1\">";
							new_div += "<tr>";
								new_div += "<td colspan=\"2\" align=\"center\" bgcolor=\"#678948\" class=\"TIT_A\">食物份數</td>";
							new_div += "</tr>";
							new_div += "<tr bgcolor=\"#fffff4\">";
							new_div += "<td align=\"right\">全榖根莖類</td>";
							new_div += "<td align=\"left\">"+result_content.nutrition.mainFood +"份</td>";
						new_div += "</tr>";
		
							new_div += "<tr  bgcolor=\"#ffffff\">";
							new_div += "<td align=\"right\">豆魚肉蛋類</td>";
							new_div += "<td align=\"left\">"+ result_content.nutrition.meatBeans +"份</td>";
						new_div += "</tr>";
						
							new_div += "<tr bgcolor=\"#ffffff\">";
							new_div += "<td align=\"right\">低脂乳品類</td>";
							new_div += "<td align=\"left\">"+ result_content.nutrition.milk +"份</td>";
						new_div += "</tr>";
	
							new_div += "<tr bgcolor=\"#ffffff\">";
							new_div += "<td align=\"right\">蔬菜類</td>";
							new_div += "<td align=\"left\">"+ result_content.nutrition.vegetable +"份</td>";
						new_div += "</tr>";
						
							new_div += "<tr bgcolor=\"#fffff4\">";
							new_div += "<td align=\"right\">水果類</td>";
							new_div += "<td align=\"left\">"+ result_content.nutrition.fruit +"份</td>";								
						new_div += "</tr>";
		
							new_div += "<tr bgcolor=\"#fffff4\">";
							new_div += "<td align=\"right\" >油脂與堅果種子類</td>";
							new_div += "<td align=\"left\">"+ result_content.nutrition.oil +"份</td>";
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
							new_div += "<td id=fdaName align=\"left\">"+ result_content.supplierInfo.supplierName +"</td>";
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
							new_div += "<td id=fdaName align=\"left\">"+ result_content.supplierInfo.supplierName +"</td>";
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
						new_div += "<td align=\"right\">餐食負責人</td>";
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
				
				//show fdaInfo--test
				if(result_content.supplierInfo.fdaCompanyId != null){
					createFdaInfo(result_content.supplierInfo , 'fdaName');
					var fdamethod = 'showCertInfo("fdaName")';
					var fdalink = "<a id='fdalink'; style='font-size:12px;' href='#' onclick='"+fdamethod+";return false;'><b>FDA</b></a>&nbsp";
					$("#fdaName").prepend(fdalink);
				}
				
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
				
				//20150501 shine mod 根據菜單類別來決定要顯示什麼資料				
				if(menuType == 0){  //早餐
					$("a[href='#tabs-1']").html("早點內容");
					var lunch_content_div = "<div class=\"MENU_BOX\"><ul>";
					for(var k=0; k<result_content.lunchContent.length; k++){
						// if(k%3 == 0 && k!=0) { lunch_content_div += "<tr>";	var count = 0;}
							lunch_content_div +="<li class='showModeli'>";<%--//Ric 20150105 加入改css tag --%>
							if(result_content.lunchContent[k].image){
									lunch_content_div +="<a href='#' onclick='nextTab()' ><img src='" + result_content.lunchContent[k].image + "'  width='150' height='130' class='showMode' /></a>";}<%--//Ric 20150105 加入改css tag --%>
									lunch_content_div +="<div class=\"menu_tit_txt\">" + result_content.lunchContent[k].category + "</div>";
									lunch_content_div +="<div class=\"menu_ini_txt\">" + result_content.lunchContent[k].foodName + "</div>";
									
							lunch_content_div +="</li>";
							//count ++;
						//if(count%3 == 0 || result_content.lunchContent.length == (k+1)) { lunch_content_div += "</tr>"; }
					}
						
					lunch_content_div +="</ul><div style=\"text-align:center;\">菜色圖片為示意圖</div></div>";
					$("#lunch_content").append(lunch_content_div);
				}else if(menuType == 1){   //午餐
					$("a[href='#tabs-1']").html("午餐內容");
					var lunch_content_div = "<div class=\"MENU_BOX\"><ul>";
					for(var k=0; k<result_content.lunchContent.length; k++){
						// if(k%3 == 0 && k!=0) { lunch_content_div += "<tr>";	var count = 0;}
							lunch_content_div +="<li class='showModeli'>";<%--//Ric 20150105 加入改css tag --%>
							if(result_content.lunchContent[k].image){
									lunch_content_div +="<a href='#' onclick='nextTab()' ><img src='" + result_content.lunchContent[k].image + "'  width='150' height='130' class='showMode' /></a>";}<%--//Ric 20150105 加入改css tag --%>
									lunch_content_div +="<div class=\"menu_tit_txt\">" + result_content.lunchContent[k].category + "</div>";
									lunch_content_div +="<div class=\"menu_ini_txt\">" + result_content.lunchContent[k].foodName + "</div>";
									
							lunch_content_div +="</li>";
							//count ++;
						//if(count%3 == 0 || result_content.lunchContent.length == (k+1)) { lunch_content_div += "</tr>"; }
					}
						
					lunch_content_div +="</ul><div style=\"text-align:center;\">菜色圖片為示意圖</div></div>";
					$("#lunch_content").append(lunch_content_div);
				}else if(menuType == 2){  //點心
					$("a[href='#tabs-1']").html("點心內容");
					var lunch_content_div = "<div class=\"MENU_BOX\"><ul>";
					for(var k=0; k<result_content.lunchContent.length; k++){
						// if(k%3 == 0 && k!=0) { lunch_content_div += "<tr>";	var count = 0;}
							lunch_content_div +="<li class='showModeli'>";<%--//Ric 20150105 加入改css tag --%>
							if(result_content.lunchContent[k].image){
									lunch_content_div +="<a href='#' onclick='nextTab()' ><img src='" + result_content.lunchContent[k].image + "'  width='150' height='130' class='showMode' /></a>";}<%--//Ric 20150105 加入改css tag --%>
									lunch_content_div +="<div class=\"menu_tit_txt\">" + result_content.lunchContent[k].category + "</div>";
									lunch_content_div +="<div class=\"menu_ini_txt\">" + result_content.lunchContent[k].foodName + "</div>";
									
							lunch_content_div +="</li>";
							//count ++;
						//if(count%3 == 0 || result_content.lunchContent.length == (k+1)) { lunch_content_div += "</tr>"; }
					}
						
					lunch_content_div +="</ul><div style=\"text-align:center;\">菜色圖片為示意圖</div></div>";
					$("#lunch_content").append(lunch_content_div);
				}

				//食材資訊
				var food_info_div = "<div class=\"TAB_TY_C\"><table class=\"component TAB_TY_C\">";
					food_info_div += "<tr>";
					food_info_div += "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">菜色</td>" +
					 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">原料</td>" +
				 	 /* "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">品牌</td>" + */
				 	 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">商品名稱</td>" +
				 	 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">製造商</td>" +
					 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">驗證標章</td>" +
					 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">供應商名稱</td>";
					food_info_div += "</tr>";
					
					for(var i=0; i<result_content.foodInfo.length; i++){
						//認證標章追朔
						if(result_content.foodInfo[i].authenticate != "" && result_content.foodInfo[i].authenticateId != ""){
							QueryCertificate(result_content.foodInfo[i].authenticate,result_content.foodInfo[i].authenticateId,i);}
					
						
						food_info_div += "<tr>";
						food_info_div += "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += result_content.foodInfo[i].foodName + "</td>" +
										 "<td  id=\"materialName"+i+"\" align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += result_content.foodInfo[i].material + "</td>" +
										 "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += result_content.foodInfo[i].productName + "</td>" +
									 	 "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += result_content.foodInfo[i].manufacture + "</td>" +
										 "<td  id=\"materialCert"+i+"\" align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += "<label id=\"auth"+i+"\"></label><div id=\"result"+i+"\"></div></td>" +
										 "<td  id=\"fdaSource"+i+"\" align=\"center\" valign=\"middle\" ";if(i%2 == 1){food_info_div +=" class='componetContent'>"; }else if(i%2 == 0){food_info_div +=" class='componetContent2'>" ;} food_info_div += result_content.foodInfo[i].source + "</td>";
						food_info_div += "</tr>";
						
						//CAS認證資訊
						if(result_content.foodInfo[i].ingredientCertificateObject != null){
							//console.log(result_content.foodInfo[i].ingredientCertificateObject);
							createCasCertInfo(result_content.foodInfo[i].ingredientCertificateObject,'materialCert'+i);
						}
					}
				
				food_info_div +="</table></div>";
				$("#food_info").append(food_info_div);
				
				//fda--test
				for(var k=0; k<result_content.foodInfo.length; k++){
					if(result_content.foodInfo[k].fdaCompanyId != null){
						createFdaInfo(result_content.foodInfo[k],'fdaSource'+k);
						var fdamethod = 'showCertInfo("fdaSource' +k+ '")';
						var fdalink = "<a id='fdalink'; style='font-size:12px;' href='#' onclick='"+fdamethod+";return false;'><b>FDA</b></a>&nbsp";
						$('#fdaSource'+k).prepend(fdalink);
						console.log($('food_info'))
					}
				}
				
				//調味料
				var seasoning_div = "<div class=\"TAB_TY_C\"><table class=\"component\">";
					seasoning_div += "<tr>";
						seasoning_div += "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">原料</td>" +
					 	 /* "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">品牌</td>" + */
					 	 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">商品名稱</td>" +
					 	 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">製造商</td>" +
						 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">驗證標章</td>" +
						 "<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"componentTitle TIT_A\">來源</td>";
					seasoning_div += "</tr>";
					
					for(var j=0; j<result_content.seasoning.length; j++){
						//認證標章追朔
						if(result_content.seasoning[j].authenticate != "" && result_content.seasoning[j].authenticateId != ""){
							QueryCertificate(result_content.seasoning[j].authenticate,result_content.seasoning[j].authenticateId,"S"+j);}

						seasoning_div += "<tr>";
						seasoning_div += "<td id=\"seasoningName"+j+"\"  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2'>" ;} seasoning_div +=  result_content.seasoning[j].material + "</td>" +
						/* "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2'>" ;} seasoning_div += result_content.seasoning[j].brand + "</td>" + */
						"<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2'>" ;} seasoning_div += result_content.seasoning[j].productName + "</td>" +
						"<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2'>" ;} seasoning_div += result_content.seasoning[j].manufacture + "</td>" +
						"<td  id=\"seasoningCert"+j+"\" align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2'>" ;} seasoning_div +=  "<label id=\"authS"+j+"\"></label><div id=\"resultS"+j+"\"></div></td>" +
						"<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent'>"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2'>" ;} seasoning_div +=  result_content.seasoning[j].source + "</td>";
						seasoning_div += "</tr>";
						
						//CAS認證資訊
						if(result_content.seasoning[j].ingredientCertificateObject != null){
							//console.log(result_content.foodInfo[i].ingredientCertificateObject);
							createCasCertInfo(result_content.seasoning[j].ingredientCertificateObject,'seasoningCert'+j);
						}
					}
					
				seasoning_div +="</table></div>";
				$("#seasoning_info").append(seasoning_div);
				
				//20140425 Raymond 如地區選擇台北市  不顯試認證標章資訊
				if($("#select_counties").val() != "17"){
					//assign a 給有CAS資訊的食材
					for(var i=0;i<result_content.foodInfo.length;i++){
						if(result_content.foodInfo[i].ingredientCertificateObject != null){
							switch (result_content.foodInfo[i].ingredientCertificateObject.certType){
								case "CAS":
									var method = 'showCertInfo("materialCert' +i+ '")';
									var link = "<a style='font-size:15px;' href='#' onclick='"+method+";return false;'><u> CAS </u></a>";
									$('#materialCert' + i).append(link);
									break;
								case "SN":
									var url = 'http://shennong.ideas.iii.org.tw/ShenNongResumeWeb/ResumeSlideShow_workAround.aspx?e='+result_content.foodInfo[i].ingredientCertificateObject.certNo;
									//20151214 修改神農介接顯示方式 --chu--
									createSNDialog(url, 'materialCert'+i);
									//var link = "<a style='font-size:15px;' target='_blank' href='"+url+"'><u>神農履歷</u></a>";--original--
									var method = 'showSNDialog("materialCert' +i+ '")';
									var link = "<a style='font-size:15px;' href='#' onclick ='"+method+";return false;'><u>神農履歷</u></a>";
									$('#materialCert' + i).append(link);
									break;
							}
						}
					}
					//assign a 給有CAS資訊的調味料
					for(var i=0;i<result_content.seasoning.length;i++){
						if(result_content.seasoning[i].ingredientCertificateObject != null){
							switch (result_content.seasoning[i].ingredientCertificateObject.certType){//20151216 調味料新增神農履歷 --chu--
								case "CAS":
									var method = 'showCertInfo("seasoningCert' +i+ '")';
									var link = "<a style='font-size:15px;' href='#' onclick='"+method+";return false;'><u> CAS </u></a>";
									$('#seasoningCert' + i).append(link);
									break;
								case "SN":
									var url = 'http://shennong.ideas.iii.org.tw/ShenNongResumeWeb/ResumeSlideShow_workAround.aspx?e='+result_content.seasoning[i].ingredientCertificateObject.certNo;
									//20151214 修改神農介接顯示方式 --chu--
									createSNDialog(url, 'seasoningCert'+i);
									//var link = "<a style='font-size:15px;' target='_blank' href='"+url+"'><u>神農履歷</u></a>";--original--
									var method = 'showSNDialog("seasoningCert' +i+ '")';
									var link = "<a style='font-size:15px;' href='#' onclick ='"+method+";return false;'><u>神農履歷</u></a>";
									$('#seasoningCert' + i).append(link);
									break;
							}
						}
					}
				}
				
				<%--////20150105 Ric hide image --%>
				if(!result_content.showMode){
					$(".showMode").attr("height","0");
					$(".showModeli").css("height","50px");
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
		//var formatter = new SimpleDateFormat("yyyy/MM/dd");
		//var menudate = result_content.date.substring(0,4)+"/"+result_content.date.substring(4,6)+"/"+result_content.date.substring(6,8);
		var menudate = new Date(result_content.date.substring(0,4)+"/"+result_content.date.substring(4,6)+"/"+result_content.date.substring(6,8));
		var nowdate = new Date(result_content.nowdate);
		
		//若尚未登入，未來日期則不顯示菜色與食材
		if(result_content.userName==null && menudate.getTime()>nowdate.getTime()){
				$("#tabheaders-2").hide();
				$("#tabheaders-3").hide();
				//$("#tabs-2").hide();
				//$("#tabs-3").hide();
				//$("#tabs-1").show();
				//$("#tabheaders-1").show();
				//$("#tabs").tabs().data("0");
				$("#tabs").tabs("option", "active", 0 );
			}else{
				$("#tabheaders-2").show();
				$("#tabheaders-3").show();
				//$("#tabs-2").show();
				//$("#tabs-3").show();
				//$("#tabheaders-1").selected();
				$("#tabs").tabs("option", "active", 0 );
			}
		
		$("#controller").show("slow");
		//側區塊
// 		$("#tabs").tabs({
// 	        activate: function(event, ui){
// 	             alert(ui.newTab.index());               
// 	        },
// 		});
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
	
	//fdaInfo--test
	function createFdaInfo(fdaInfo,serno){
		var fdaDiv = '';
		fdaDiv += "<div class=\"TAB_TY_A\" id=\""+ "div" + serno +"\" title=\"非登不可認證廠商資訊\" align=\"center\">";
		fdaDiv += "<table  border=\"1\">";
			
			fdaDiv += "<tr bgcolor=\"#fffff4\">";
			fdaDiv += "<td align=\"right\">認證編號</td>";
			fdaDiv += "<td align=\"left\">"+fdaInfo.fdaCompanyId +"</td>";
		fdaDiv += "</tr>";

		fdaDiv += "<tr bgcolor=\"#ffffff\">";
			fdaDiv += "<td align=\"right\">公司名稱</td>";
			fdaDiv += "<td align=\"left\">"+ fdaInfo.fdaCompanyName +"</td>";
		fdaDiv += "</tr>";
		
		fdaDiv += "<tr bgcolor=\"#ffffff\">";
			fdaDiv += "<td align=\"right\">公司統一編號</td>";
			fdaDiv += "<td align=\"left\">"+ fdaInfo.fdaCompanyBusinessId +"</td>";
		fdaDiv += "</tr>";
			
		fdaDiv += "<tr bgcolor=\"#ffffff\">";
			fdaDiv += "<td align=\"right\">地址</td>";
			fdaDiv += "<td align=\"left\">"+ fdaInfo.fdaCompanyAddress +"</td>";
		fdaDiv += "</tr>";

		fdaDiv += "<tr bgcolor=\"#ffffff\">";
			fdaDiv += "<td align=\"right\">登錄項目</td>";
			fdaDiv += "<td align=\"left\">"+ fdaInfo.fdaCompanyRegType +"</td>";
		fdaDiv += "</tr>";
		
		fdaDiv += "</table>";
		fdaDiv += "</div>";
		
		$("#fda_info").append(fdaDiv);

	}

	
	function showCertInfo(linkid){
		$(".ui-dialog-content").dialog("close");
		$("#div"+linkid).dialog({
			width:360,
			maxWidth:400,
			minWidth:360
		});
	}
	//20151214 修改神農介接顯示方式 --chu--
	function createSNDialog(url, serno){
		var snDiv = "<div id='dialog"+ serno +"' title=\"神農履歷\">"+"<iframe id="+"'snIframe'"+"src='"+url+"' width='180%' height='180%' marginWidth='0' marginHeight='0' frameBorder='0' scrolling='auto' style="+"'transform: scale(0.58);transform-origin: 0 0;'"+"></iframe></div>";
		$('#sn').append(snDiv);
	}
	function showSNDialog(linkid){
		$(".ui-dialog-content").dialog("close");
		$("#dialog"+linkid).dialog({
			width: 600,	
			height: 450,
		}).css({overflow: 'hidden',padding: '1px'});
	}
	
	$(document).ready(function(){	
		//calendar();	//full screen calendar
		//detailData();
		counties();	//drop down counties
		area();
		school();
		menu();
		queryMenu('default');
		
		$("#tabs").hide();
		//$('.fancybox').fancybox();
		
		//showCertInfo();
	});

</script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">菜單查詢</div>
		<div class="contents-wrap">
	        <div class="section-wrap has-border">
	          <div class="form-horizontal kitchen-info">         	
				<div id="dss">
					<div>
						<table class="lh_30" width="100%">
							<tr>
								<td colspan="8"></td>
							</tr>
							<tr class="h_40px lh_40">
								<td align="left" valign="middle" nowrap="nowrap">&nbsp;&nbsp;縣市別:&nbsp;</td>
								<td align="left" valign="middle"><span id="dropDown_counties"></span></td>
								<td align="left" valign="middle" nowrap="nowrap">&nbsp;&nbsp;市/區別:&nbsp;</td>
			
								<td align="left" valign="middle"><span id="dropDown_area"></span></td>
								<td align="left" valign="middle">&nbsp;&nbsp;學校名稱:&nbsp;</td>
			
								<td align="left" valign="middle"><span id="dropDown_school"></span></td>
								<td align="left" valign="middle">&nbsp;&nbsp;年/月:&nbsp;</td>
								<td align="left" valign="middle"><span id="dropDown_yearMonth"></span></td>
			
							</tr>
							<tr class="h_40px lh_40">
								<td colspan="8" align="center" class="BT_IN_BBTER">
									<div id="sf_link" style="font-weight: bold;"></div>
								</td>
								<td colspan="8" align="center">
									<div style="font-weight: bold;">
										<a href="#" class="btn btn-primary" style="margin:0" onclick="queryMenu('manual')" onkeypress="queryProduct()">查詢</a>
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
						<li id="tabheaders-1" ><a href="#tabs-1">午餐內容</a></li>
						<li id="tabheaders-2" ><a href="#tabs-2">食材資訊</a></li>
						<li id="tabheaders-3" ><a href="#tabs-3">調味料</a></li>
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
	          </div>
	        </div><!-- End of .section-wrap -->        
	    </div>
       
	<!-- 舊團膳套版 -->
	<div style="display:none">
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
					<td align="left" valign="middle"><span id="dropDown_counties"></span></td>
					<td align="left" valign="middle" nowrap="nowrap">&nbsp;&nbsp;市/區別:&nbsp;</td>

					<td align="left" valign="middle"><span id="dropDown_area"></span></td>
					<td align="left" valign="middle">&nbsp;&nbsp;學校名稱:&nbsp;</td>

					<td align="left" valign="middle"><span id="dropDown_school"></span></td>
					<td align="left" valign="middle">&nbsp;&nbsp;年/月:&nbsp;</td>
					<td align="left" valign="middle"><span id="dropDown_yearMonth"></span></td>

				</tr>
				<tr class="h_40px lh_40">
					<td colspan="8" align="center" class="BT_IN_BBTER">
						<div id="sf_link" style="font-weight: bold;"></div>
					</td>
					<td colspan="8" align="center" class="BT_IN_BBTER">
						<div style="font-weight: bold;">
							<a href="#" onclick="queryMenu('manual')" onkeypress="queryProduct()">查詢</a>
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
			<li id="tabheaders-1" ><a href="#tabs-1">午餐內容</a></li>
			<li id="tabheaders-2" ><a href="#tabs-2">食材資訊</a></li>
			<li id="tabheaders-3" ><a href="#tabs-3">調味料</a></li>
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
	<div id="sn" style="display: none"></div>
	<div id="fda_info" style="display: none"></div>
</body>
</html>
