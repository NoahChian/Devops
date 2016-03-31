<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.HashMap" %>

<style>
#page1{
background: linear-gradient(#ffffff, #c0d8e0) repeat scroll 0 0 #f9f9f9;
    color: #333;
    font-size: 14px;
}
.w_90ps{
width: 90%;
}
#CALENDAR{
padding:0.05em 0;
}
.txt_h_01 {height: 13px; line-height:13px;}
.search_re_tit02 {
	color: #48bf84;
    font-size: 14px;
    font-weight: bold;
    line-height: 16px;
    padding: 6px 0px 0px 0px;
    text-align: center;
    text-shadow: 1px 1px 0 #fff;
}
.search_re02 ul li span {
	display: block;
	font-size: 12px;
	font-weight: bold;
	border: solid 2px #00d6ef;
	border-bottom: solid 2px #FFF;
	border-radius: 10px 10px 0 0;
	color: #00aeef;
	background: #FFF;
	padding: 8px 10px;
	margin: 19px 5px 0 5px;
}
.search_re03{
margin: 10px auto;
}
.seafod_batxt02 {
 	border-bottom: 1px dashed #ccc;
    font-size: 14px;
    height: 14px;
    line-height: 14px;
    margin: 5px auto 10px;
	text-align: left;
	padding: 3px 0 0 15px;
}
.dishLi {
    color: #476c59;
}
</style>

<%
String qCountyId="";
String qCountyName="";
String qSchoolId="";
String qSchoolName="";
String basepath="../../";
String Kitchen="";
String qSchoolCode = "";
if(request.getAttribute("responseMap") != null){
	HashMap<String,String> responseMap = (HashMap<String,String>)request.getAttribute("responseMap");
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
	if (responseMap.get("basepath")!=null){
		basepath=(String) responseMap.get("basepath");
	}
	if (responseMap.get("kitchenId")!=null){
		Kitchen=(String) responseMap.get("kitchenId");
	}
	if (responseMap.get("schoolCode")!=null){
		qSchoolCode=(String) responseMap.get("schoolCode");
	}
}
%>

<script>
var qCountyId="<%=qCountyId %>";
var qCountyName="<%=qCountyName %>";
var qSchoolId="<%=qSchoolId %>";
var qSchoolName="<%=qSchoolName %>";
var basepath="<%=basepath%>";
var Today=new Date();
var date = Today.getFullYear()+"-"+ (Today.getMonth()+1) +"-"+Today.getDate();

var array_month=['一月','二月', '三月', '四月', '五月', '六月', '七月','八月', '九月', '十月', '十一月', '十二月'];
function query_detail_data(mid){
	//var mid = $("#Kitchen").val();
	//查詢菜單
	var request_data =	{
			 "method":"customerQueryMenuDetailInfo",
 				"args":{
 					"mid" :mid
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
			$("#school_and_date").html("");
			$("#query_supply_info").html("");
			if(result_content.schoolName == "" || result_content.schoolName == null ){
			var new_Titlte = "<div id=\"MAIN_SECOND_BAR\"  class=\"tal_ch search_re_tit02\">請選擇學校<br><a href=\"#\"  onclick=\"javascript:location.href='../main/'\" >回前頁</a></div><div class=\"bw_line\"></div>";
			$("#school_and_date").append(new_Titlte);
			}else{
			//標題
			var new_Titlte = "<div id=\"MAIN_SECOND_BAR\"  class=\"tal_ch search_re_tit02\">" + result_content.schoolName 
				+"<div class=\"bw_line\"></div>";
			$("#school_and_date").append(new_Titlte);
			}
			//控制項(上一頁、前一日、後一日)
// 			var controller_div = "<div class=\"h_94px lh_94 tal_ch pf_bbter\"><a href=\"#\"  onclick=\"javascript:location.href='../main/'\" >回前頁</a>&nbsp;";
			var controller_div = "<div class=\"h_94px lh_94 tal_ch pf_bbter\">";
			if(result_content.midBefore != 0) {
				controller_div += "<a href=\"#\" onclick=\"query_detail_data(" + result_content.midBefore  +")\">前一日</a>&nbsp;";
			}
			if(result_content.midAfter != 0){
				controller_div += "<a href=\"#\" onclick=\"query_detail_data(" + result_content.midAfter  + ")\">下一日</a></div><div class=\"bw_line\" ></div>";
			}
			$("#controller").append(controller_div);
			
			//午餐內容
			var lunch_content_div = " ";
				for(var k=0; k<result_content.lunchContent.length; k++){
					// if(k%3 == 0 && k!=0) { lunch_content_div += "<tr>";	var count = 0;}
						lunch_content_div +=" ";
						//lunch_content_div += "<script> $(document).ready(function() { $('#show_MD-"+k+"').click(function() {$('#detail-"+k+"').slideToggle(\"fast\");$('#menu-"+k+"').slideToggle(\"fast\");}); }); <\/script>";
						if(result_content.lunchContent[k].image){lunch_content_div +="<div><div id=\"menu-"+k+"\"class=\"seafod_pix tabs-1\" style=\"display:none;\"><img src=\"" + result_content.lunchContent[k].image + "\"  width=\"150\" height=\"130\" /></div></div>";}
						lunch_content_div +="<div class=\"tabs-1 w_100ps tal_ch seafod_batxt02 txt_h_01\" id=\"show_MD-"+k+"\"><span class='dishLi'>" + result_content.lunchContent[k].category + "：</span>";
						lunch_content_div +="<b>" + result_content.lunchContent[k].foodName + "</b></div>";
						var lunch_content_detail ="<div><div id=\"detail-"+k+"\" class=\"TAB_TY_A tal_ch\"  style=\"display:none;\" ><table class=\"component TAB_TY_C\"  width=\"100%\">";
						lunch_content_detail += "<tr>";
							lunch_content_detail += "<td align=\"center\" valign=\"middle\" bgcolor=\"#669db1\" class=\"componentTitle TIT_A sea_title\">原料</td>" +
						 	 "<td align=\"center\" valign=\"middle\" bgcolor=\"#669db1\" class=\"componentTitle TIT_A sea_title\">品牌</td>" +
							 "<td align=\"center\" valign=\"middle\" bgcolor=\"#669db1\" class=\"componentTitle TIT_A sea_title\">認證標章</td>" +
							 "<td align=\"center\" valign=\"middle\" bgcolor=\"#669db1\" class=\"componentTitle TIT_A sea_title\">供應商名稱</td>";
							lunch_content_detail += "</tr>";
						
						for(var i=0; i<result_content.foodInfo.length; i++){
							if(result_content.foodInfo[i].foodName==result_content.lunchContent[k].foodName){
							lunch_content_detail += "<tr>";
							lunch_content_detail += "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){lunch_content_detail +=" bgcolor=\"#ecf2f3\">"; }else if(i%2 == 0){lunch_content_detail +=" bgcolor=\"#ffffff\">" ;} lunch_content_detail += result_content.foodInfo[i].material + "</td>" +
						 	 "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){lunch_content_detail +=" bgcolor=\"#ecf2f3\">"; }else if(i%2 == 0){lunch_content_detail +=" bgcolor=\"#ffffff\">" ;} lunch_content_detail += result_content.foodInfo[i].brand + "</td>" +
							 "<td  id=\"materialCert"+i+"\" align=\"center\" valign=\"middle\" ";if(i%2 == 1){lunch_content_detail +=" bgcolor=\"#ecf2f3\">"; }else if(i%2 == 0){lunch_content_detail +=" bgcolor=\"#ffffff\">" ;} lunch_content_detail +=  "</td>" +
							 "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){lunch_content_detail +=" bgcolor=\"#ecf2f3\">"; }else if(i%2 == 0){lunch_content_detail +=" bgcolor=\"#ffffff\">" ;} lunch_content_detail += result_content.foodInfo[i].source + "</td>";
							 lunch_content_detail += "</tr>";
							 }
							//因mschool不讀取cas資訊，固註解掉。 modify by ellis 20150217
							//CAS認證資訊
// 							if(result_content.foodInfo[i].ingredientCertificateObject != null){
// 								//console.log(result_content.foodInfo[i].ingredientCertificateObject);
// 								if($("#divmaterialCert"+i).length == 0){
// 									createCasCertInfo(result_content.foodInfo[i].ingredientCertificateObject,'materialCert'+i);
// 								}
// 							}
						}
						lunch_content_detail += "</table></div></div>";
						lunch_content_div +=lunch_content_detail+"";

						//count ++;
					//if(count%3 == 0 || result_content.lunchContent.length == (k+1)) { lunch_content_div += "</tr>"; }
				}
			//lunch_content_div +="<div class=\"h_60px lh_60 tal_ch atch_txt tabs-1\">菜色圖片為示意圖</div>";
			$("#lunch_content").append(lunch_content_div);

<%-- 2014.12.29 Steven拿掉此區顯示開始 --%>
<%--
/*--------+++
			//調味料
			var seasoning_div = "<div class=\"tabs-3 TAB_TY_A tal_ch search_tab_box\"><table class=\"component TAB_TY_C\" width=\"90%\">";
			seasoning_div += "<tr>";
				seasoning_div += "<td align=\"center\" valign=\"middle\" bgcolor=\"#669db1\" class=\"componentTitle TIT_A sea_title\">原料</td>" +
			 	 "<td align=\"center\" valign=\"middle\" bgcolor=\"#669db1\" class=\"componentTitle TIT_A sea_title\">品牌</td>" +
				 "<td align=\"center\" valign=\"middle\" bgcolor=\"#669db1\" class=\"componentTitle TIT_A sea_title\">認證標章</td>" +
				 "<td align=\"center\" valign=\"middle\" bgcolor=\"#669db1\" class=\"componentTitle TIT_A sea_title\">來源</td>";
			seasoning_div += "</tr>";
			
			for(var j=0; j<result_content.seasoning.length; j++){
				seasoning_div += "<tr>";
				seasoning_div += "<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent' bgcolor=\"#ecf2f3\">"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2' bgcolor=\"#ffffff\">" ;} seasoning_div +=  result_content.seasoning[j].material + "</td>" +
				"<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent' bgcolor=\"#ecf2f3\">"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2' bgcolor=\"#ffffff\">" ;} seasoning_div += result_content.seasoning[j].brand + "</td>" +
				"<td  id=\"seasoningCert"+j+"\" align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent' bgcolor=\"#ecf2f3\">"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2' bgcolor=\"#ffffff\">" ;} seasoning_div += "</td>" +
				"<td  align=\"center\" valign=\"middle\" ";if(i%2 == 1){seasoning_div +=" class='componetContent' bgcolor=\"#ecf2f3\">"; }else if(i%2 == 0){seasoning_div +=" class='componetContent2' bgcolor=\"#ffffff\">" ;} seasoning_div +=  result_content.seasoning[j].source + "</td>";
				seasoning_div += "</tr>";
				
				//CAS認證資訊
				if(result_content.seasoning[j].ingredientCertificateObject != null){
					//console.log(result_content.foodInfo[i].ingredientCertificateObject);
					createCasCertInfo(result_content.seasoning[i].ingredientCertificateObject,'seasoningCert'+i);
				}
			}
			
		seasoning_div +="</table></div><div class=\"tabs-3 bw_line\"></div>";	
			$("#seasoning_info").append(seasoning_div);
			
			//營養份量
			var new_div = "<div class=\"tabs-4 TAB_TY_A tal_ch search_tab_box\">";
				new_div += "";
					new_div += "<table width=\"90%\" border=\"1\">";
					//	new_div += "<tr>";
					//		new_div += "<td colspan=\"2\" align=\"center\" bgcolor=\"#669db1\" class=\"TIT_A sea_title\">營養份量</td>";
					//	new_div += "</tr>";
						new_div += "<tr bgcolor=\"#ffffff\">";
						new_div += "<td align=\"right\" width=\"50%\" >全榖根莖類：	</td>";
						new_div += "<td align=\"left\" width=\"50%\">"+result_content.nutrition.mainFood +"份</td>";
					new_div += "</tr>";

					new_div += "<tr bgcolor=\"#ecf2f3\">";
						new_div += "<td align=\"right\" width=\"50%\">蔬菜類：	</td>";
						new_div += "<td align=\"left\" width=\"50%\">"+ result_content.nutrition.vegetable +"份</td>";
					new_div += "</tr>";

					new_div += "<tr bgcolor=\"#ffffff\">";
						new_div += "<td align=\"right\"  width=\"50%\">油脂與堅果種子類：	</td>";
						new_div += "<td align=\"left\" width=\"50%\">"+ result_content.nutrition.oil +"份</td>";
					new_div += "</tr>";
					
					new_div += "<tr  bgcolor=\"#ecf2f3\">";
						new_div += "<td align=\"right\" width=\"50%\">蛋豆魚肉類：	</td>";
						new_div += "<td align=\"left\" width=\"50%\">"+ result_content.nutrition.meatBeans +"份</td>";
					new_div += "</tr>";

					new_div += "<tr bgcolor=\"#ffffff\">";
						new_div += "<td align=\"right\" width=\"50%\">水果類：	</td>";
						new_div += "<td align=\"left\" width=\"50%\">"+ result_content.nutrition.fruit +"份</td>";								
						new_div += "</tr>";
			
					new_div += "<tr bgcolor=\"#ecf2f3\">";
						new_div += "<td align=\"right\" width=\"50%\">乳品類：	</td>";
						new_div += "<td align=\"left\" width=\"50%\">"+ result_content.nutrition.milk +"份</td>";
					new_div += "</tr>";
		
					new_div += "<tr bgcolor=\"#ffffff\">";
						new_div += "<td align=\"right\" width=\"50%\">熱量：	</td>";			
						new_div += "<td align=\"left\" width=\"50%\">"+ result_content.nutrition.calories +"大卡</td>";								
					new_div += "</tr>";
					new_div += "</table>";
				new_div += "</div>";
			new_div += "<div class=\"tabs-4 bw_line\"></div>";
		$("#query_detail_info").append(new_div); 	
		//供餐者資訊
		var  new_Sup = "<div class=\"tabs-5 TAB_TY_A tal_ch search_tab_box\">";
			new_Sup += "<table width=\"90%\" border=\"1\">";
					if(result_content.supplierInfo.kitchenType=="006"){
					//new_Sup += "<tr>";
					//	new_Sup += " <td colspan=\"2\" align=\"center\" bgcolor=\"#669db1\" class=\"TIT_A sea_title\">供餐者資訊</td>";
					//new_Sup += "</tr>";
					new_Sup += "<tr>";
						new_Sup += " <td colspan=\"2\" align=\"center\" bgcolor=\"#669db1\" class=\"TIT_A\">供餐者資訊</td>";
					new_Sup += "</tr>";
		
					new_Sup += "<tr bgcolor=\"#ffffff\">";
						new_Sup += "<td align=\"right\">供應學校：</td>";
						new_Sup += "<td align=\"left\">"+ result_content.supplierInfo.supplierName +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor=\"#ecf2f3\">";
						new_Sup += "<td align=\"right\">學校地址：</td>";
						new_Sup += "<td align=\"left\">"+ result_content.supplierInfo.supplierAddress +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor=\"#ffffff\">";
						new_Sup += "<td align=\"right\">學校電話：</td>";
						new_Sup += "<td align=\"left\">"+ result_content.supplierInfo.supplierPhone +"</td>";
					new_Sup += "</tr>";		
						
					new_Sup += "<tr bgcolor=\"#ecf2f3\">";
						new_Sup += "<td align=\"right\">營養師/午餐秘書：</td>";
						new_Sup += "<td align=\"left\">"+ result_content.supplierInfo.dietitians +"</td>";
					new_Sup += "</tr>";
					
								
					}else{ //業者與其他
							new_Sup += "<tr>";
							new_Sup += " <td colspan=\"2\" align=\"center\" bgcolor=\"#669db1\" class=\"TIT_A\">供餐者資訊</td>";
						new_Sup += "</tr>";
					new_Sup += "<tr bgcolor=\"#ffffff\">";
						new_Sup += "<td align=\"right\">供應商：</td>";
						new_Sup += "<td align=\"left\">"+ result_content.supplierInfo.supplierName +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor=\"#ecf2f3\">";
						new_Sup += "<td align=\"right\">供應商地址：</td>";
						new_Sup += "<td align=\"left\">"+ result_content.supplierInfo.supplierAddress +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor=\"#ffffff\">";
						new_Sup += "<td align=\"right\">供應商電話：</td>";
						new_Sup += "<td align=\"left\">"+ result_content.supplierInfo.supplierPhone +"</td>";
					new_Sup += "</tr>";
		
					new_Sup += "<tr bgcolor=\"#ecf2f3\">";
						new_Sup += "<td align=\"right\">衛管人員：</td>";
						new_Sup += "<td align=\"left\">"+ result_content.supplierInfo.supplierLeader +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor=\"#ffffff\">";
						new_Sup += "<td align=\"right\">營養師：</td>";
						new_Sup += "<td align=\"left\">"+ result_content.supplierInfo.dietitians +"</td>";
					new_Sup += "</tr>";
					
					new_Sup += "<tr bgcolor=\"#ecf2f3\">";
						new_Sup += "<td align=\"right\">品質認證體系：</td>";
						new_Sup += "<td align=\"left\">"+ result_content.supplierInfo.authenticate +"</td>";
					new_Sup += "</tr>";
					}
				new_Sup += "</table>";
			new_Sup += "</div>";
		new_Sup += "</div><div  class=\"tabs-5 bw_line\"></div>";
		$("#query_supply_info").append(new_Sup);
+++--------*/
--%>
			
			//assign a 給有CAS資訊的食材
			for(var i=0;i<result_content.foodInfo.length;i++){
				if(result_content.foodInfo[i].ingredientCertificateObject != null){
					var method = 'openDialog("materialCert' +i+ '")';
					//var link = "<a style='font-size:15px;' href='#' onclick='"+method+";return false;'><u> CAS </u></a>";
					var link = "<a href='#' data-theme='b' onclick='"+method+";return false;' data-rel='dialog' >CAS</a>";
					$('#materialCert' + i).append(link);
				}
			}	
					
			//assign a 給有CAS資訊的調味料
			for(var i=0;i<result_content.seasoning.length;i++){
				if(result_content.seasoning[i].ingredientCertificateObject != null){
					var method = 'openDialog("seasoningCert' +i+ '")';
					//var link = "<a style='font-size:15px;' href='#' onclick='"+method+";return false;'><u> CAS </u></a>";
					var link = "<a href='#' data-theme='b' onclick='"+method+";return false;' data-rel='dialog' >CAS</a>";
					$('#seasoningCert' + i).append(link);
				}
			}

		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
	
	//$("#dss").hide("slow");
	//$("#CALENDAR").hide("slow");
	
	//$("#query_detail_info").show("slow");
	$("#tabs").show("slow");
	$("#controller").show("slow");

}

/*function setCookie(name,value) {
	var d = new Date();
	d.setTime(d.getTime()+(365*24*60*60*1000));
	var expires = "expires="+d.toGMTString();
	document.cookie = name+"="+value+"; "+expires;
}*/
//---------+++
<%--
/**
 * 查看詳細資料
 * by Steven  2014.12.29
 */
--%>
function queryDetail() {
	var mid = $("#Kitchen").val();
	var school = <%=qSchoolId %>;
	var schoolCode = "<%=qSchoolCode%>";
	var yearMonth = $("#CALENDAR").val();	
	var query_date =  $("#CALENDAR").val();

	var mData = "mobile|" + mid + "|" + school + "|" + yearMonth;
	$("#mData").val(mData);

  	document.theForm.action = "/cateringservice/web/custom/school/"+schoolCode+"/";
  	document.theForm.submit();
}
//+++---------

function chengeKitchen(){
	var mid = $("#Kitchen").val();
	query_detail_data(mid);
}
function query_todaymenu(targetDate){
	var date = $("#CALENDAR").val();
	var request_data = {//查詢當天日期所有菜單
			"method" : "customerQueryKitchenBySchoolAndDate",
			"args" : {
				"sid" : qSchoolId,
				"date" : date
			}
		};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1){
		var result_content = response_obj.result_content;
		$("#school_and_date").html("");
		$("#KitchenList").html("");
		$("#lunch_content").html("");
		$("#food_info").html("");
		$("#seasoning_info").html("");
		$("#controller").html("");
		$("#query_detail_info").html("");
		$("#school_and_date").html("");
		$("#query_supply_info").html("");
		if(result_content.kitchen.length == 0){
			var new_Titlte = "<div id=\"MAIN_SECOND_BAR\"  class=\"tal_ch search_re_tit02\">" + qSchoolName
			+" : <br>"+ date + " 本日無菜單</div><div class=\"bw_line\"></div>";
			$("#school_and_date").append(new_Titlte);
			$("#queryDetailDiv").hide();
// 			$("#seasoning_click").hide();
// 			$("#query_detail_click").hide();
// 			$("#query_supply_click").hide();
		}else{
			$("#queryDetailDiv").show();
			var mid = result_content.kitchen[0].mid; //預設抓第一間供應商
			var kitchen_select = "<select name='Kitchen' id='Kitchen' onChange='chengeKitchen()' >";
			
			for (var i = 0; i < result_content.kitchen.length; i++) {
				kitchen_select += "<option value=" + result_content.kitchen[i].mid + ">" + result_content.kitchen[i].kitchenName + " 供餐</option>";
			}
			kitchen_select += "</select>";
			$("#KitchenList").append(kitchen_select);
			query_detail_data(mid);
// 			$("#seasoning_click").hide();
// 			$("#query_detail_click").hide();
// 			$("#query_supply_click").hide();
		}
		
	}
}
function todatDaty() {
	//預設跳出今日
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth() + 1; //January is 0!
	var yyyy = today.getFullYear();
	if (dd < 10) {
		dd = '0' + dd
	}
	if (mm < 10) {
		mm = '0' + mm
	}
	today = yyyy + '-' + mm + '-' + dd;
	$("#CALENDAR").val(today);
	$("#CALENDAR").attr("value", today);
	//kitchen();
	//showButton();
}
$(document).ready(function(){	
		todatDaty();
		query_todaymenu();
		
		$('#seasoning_click').click(function() {
			$('#seasoning_info').slideToggle("fast");
		});
		$('#query_detail_click').click(function() {
			$('#query_detail_info').slideToggle("fast");
		});
		$('#query_supply_click').click(function() {
			$('#query_supply_info').slideToggle("fast");
		});
	
	});
</script>

<div class="over_hi">
<form id="theForm" name="theForm" action="" method="post" target="_blank">
	<input id="mData" name="mData" type="hidden" />
 
	<div id="school_and_date"></div>
	
	<div id="" class=" w_90ps tal_ch search_re02 w_639ch">
		<ul>
			<li ><input id="CALENDAR" type="date" size="12" name="CALENDAR" onChange="query_todaymenu()" data-role="date" data-options='{"mode": "calbox", "calTodayButton": true}' /></li>
		</ul>
	</div>
			
	<div id="tabs" class="tal_ch w_639ch">
		<div id="KitchenList" class=" w_90ps tal_ch search_re03 w_639ch"></div>
		<div class='bw_line'></div>
		<div id="lunch_content"></div>
		<%-- 2014.12.29 Steven拿掉此區顯示 --%>
		<%-- <div id="seasoning_click" class=" w_100ps tal_ch search_re w_639ch">
			<ul>
				<li><span>調味料</span></li>
			</ul>
		</div>
		<div id="seasoning_info" style="display: none;"></div>
		<div id="query_detail_click" class=" w_100ps tal_ch search_re w_639ch">
			<ul>
				<li><span>營養份量</span></li>
			</ul>
		</div>
		<div id="query_detail_info" style="display: none;"></div>
		<div id="query_supply_click" class=" w_100ps tal_ch search_re w_639ch">
			<ul>
				<li><span>供餐者資訊</span></li>
			</ul>
		</div>
		<div id="query_supply_info" style="display: none;"></div> --%>
	</div>

	<%-- <div id="" class="condition_data_liner"></div>
	<div id="controller"></div>
	<div id="cert_info" style="display: none"></div> --%>
	
	<div id="queryDetailDiv" class=" w_90ps tal_ch w_639ch search_re02"><input type="button" onClick="queryDetail();" value="查看詳細資料" /></div>
</form>
</div>
	