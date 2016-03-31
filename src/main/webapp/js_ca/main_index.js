var page_cond={
		"county":0,
		"area":0,
		"sid":0,
		"date":0
};
var obj_ingre={
		foodName:"菜色",
		material:"原料",
		brand:"品牌",
		ingredientName:"原料",
		authenticate:"認證標章",
		authenticateId:"認證標章編號",
		supplierCompanyId:"供應商編號",
		source:"供應商名稱",
		ingredientCertificateObject:"認證物件"
	};

var obj_ingre_certificate={};
var obj_season_certificate={};
var util=new CateringServiceUtil();
var cookie_counties;
var cookie_area;
var cookie_school;

var array_month=['一月','二月', '三月', '四月', '五月', '六月', '七月','八月', '九月', '十月', '十一月', '十二月'];

$(document).ready(function(){	
	initCalendar();
	if (qCountyId!=""){		
		$("#dropDown_counties").append("<option selected value='"+qCountyId+"'>"+qCountyName+"</option>");
		$("#dropDown_counties").attr("disabled",true);
		getArea(qCountyId);
	}else if (qSchoolId!=""){
		$("#dropDown_school").append("<option selected value='"+qSchoolId+"'>"+qSchoolName+"</option>");
		$("#dropDown_counties").attr("disabled",true);
		$("#dropDown_area").attr("disabled",true);
		getMenuYearMonth(qSchoolId);
		//queryMenu();
	}else{
		setDefaultQueryOption();
		//queryMenu();
	}
	queryMenu();
	
	$("#tabs").show();
	$('.fancybox').fancybox();
	
	//tabs元件
	$( "#tabs" ).tabs();
	$(".nexttab").click(function() {
		$( "#tabs" ).tabs();
		$( "#tabs" ).tabs( "option", "active", 0 );
	});
});
function showCalendar(){
	$("#dss").show("slow");
	$("#calendar").show("slow");	
	$("#query_detail_info").hide("slow");
	//$("#tabs").hide("slow");
	//$("#controller").hide("slow");
}


function showMenuDetail(){
	//$( "#tabs" ).tabs( "option", "active", 0 );
	$("#dss").hide("slow");
	$("#calendar").hide("slow");	
	$("#query_detail_info").show("slow");
	//$("#tabs").show("slow");
	//$("#controller").show("slow");
}

function initCalendar(){	
	$('#calendar').fullCalendar({
		header: {
			left: '',		//prev,next today
			center: 'title',
			right: ''		//month,agendaWeek,agendaDay
		},
		editable: false,
		monthNames:array_month,
		events:[]
		
	});
}

//查下拉選單onchange動作
function setQueryOptions(obj){
	//dropDown_counties  dropDown_area  dropDown_school  dropDown_yearMonth
	var ddlvalue="";
	var obj_id="";
	if (obj!=null){
		obj_id=obj.id;
		ddlvalue=$("#"+obj_id).val();
	}else{
		obj_id="dropDown_counties";
		if (cookie_counties!=""){
			ddlvalue=cookie_counties;
		}else{
			ddlvalue=19;
		}
		
		//ddlvalue="19";
	}
	switch(obj_id){
		case "dropDown_counties":
			//if (ddlvalue!=page_cond.county){
				getArea(ddlvalue);
			//}
			page_cond.county=ddlvalue;
			break;
		case "dropDown_area":
			//if (ddlvalue!="0"){
				getSchool(ddlvalue);
			//}
			page_cond.area=ddlvalue;
			break;
		case "dropDown_school":
			//if (ddlvalue!=page_cond.sid){
				getMenuYearMonth(ddlvalue);
			//}
			page_cond.sid=ddlvalue;
			break;
		case "dropDown_yearMonth":
			//no action
			break;
		default:
			queryMenu();
	
	}
	
	
}

//縣市下拉選單
function getCounties(counties){	
	
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
			util.setDropdownlist(result_content.counties, "dropDown_counties", "countiesName", "cid");
			//$("#dropDown_counties").val(0);
			$("#dropDown_counties").val(counties);
			getArea($("#dropDown_counties").val());
			
			//清理後面相依內容
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
	}

}
//區域下拉選單
function getArea(countyId) {

	var request_data =	{
			 "method":"customerQueryArea",
 				"args":{
 					"cid" : countyId
 				}
			};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success			
			util.setDropdownlist(result_content.area, "dropDown_area", "areaName", "aid");
			$("#dropDown_area").append("<option value=\"0\">請選擇</option>");
			$("#dropDown_area").val(cookie_area);
				
			//清理後面相依內容
			$("#dropDown_school").empty();
			$("#dropDown_yearMonth").empty();
			getSchool($("#dropDown_area").val());
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
	}
}


//學校下拉選單
function getSchool(areaId) {
	if (areaId=="0"){
		$("#dropDown_school").empty();
		getMenuYearMonth(0);
		return;
	}
	var request_data =	{
			 "method":"customerQuerySchool",
 				"args":{
 					"aid": areaId
 				}
			};
	var response_obj = call_rest_api(request_data);
	var countiesName=$("#dropDown_counties :selected").text();
	var areaName=$("#dropDown_area :selected").text();	
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success
			//去除前贅字
			for(var i=0; i<result_content.school.length; i++) {
				var tmpName="";
				tmpName=result_content.school[i].schoolName;
				tmpName= tmpName.replace(countiesName+"立","").replace(countiesName,"").replace(areaName,"");
				result_content.school[i].schoolName=tmpName;
			}
		
			util.setDropdownlist(result_content.school, "dropDown_school", "schoolName", "sid");
			$("#dropDown_school").append("<option value=\"0\">請選擇</option>");
			$("#dropDown_school").val(cookie_school);
			//清理後面相依內容
			$("#dropDown_yearMonth").empty();
			getMenuYearMonth($("#dropDown_school").val());
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
	}
}

//年月下拉選單
function getMenuYearMonth(sid) {
	
	/*var yearMonth = getCookie("yearMonth");
	if(yearMonth == ""){
		yearMonth = "2014/03";
	}
*/
	$("#dropDown_yearMonth").empty();
	if (sid=="0"){	
		return;
	}
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
			util.setDropdownlist(result_content.date, "dropDown_yearMonth", "yearMonth", "yearMonth");
			//$("#dropDown_yearMonth").append("<option value=\"0\">請選擇</option>");
			//$("#dropDown_yearMonth").val(0);
			//if (result_content.date.length>0){
			//	queryMenu();
			//}
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
	//dropDown_counties  dropDown_area  dropDown_school  dropDown_yearMonth
	//initCalendar();
	var counties = $("#dropDown_counties").val();
	var area = $("#dropDown_area").val();
	var school = $("#dropDown_school").val();
	var yearMonth = $("#dropDown_yearMonth").val();	
	var query_date =  $("#dropDown_yearMonth").val();

	if(school == 0 || school==null ||query_date==null){
		//show empty calender if no cookie
		var schoolcookie = getCookie("school");
		if(schoolcookie != ""){
			alert("您尚未選擇學校或此學校無菜單");
			return false;
		}
	} else if (query_date == 0) {
		return false;
	}
	//dropDown_counties  dropDown_area  dropDown_school  dropDown_yearMonth

	//set cookie
	setCookie("counties", counties);
	setCookie("area", area);
	setCookie("school", school);
	setCookie("yearMonth", yearMonth);
	cookie_counties = counties;
	cookie_area = area;
	cookie_school = school;

	var request_data =	{
			 "method":"customerQueryCateringBySchoolAndDate_v2",
 				"args":{
 					"sid" : school,
 					"date" : query_date
 				}
			};
	var response_obj = call_rest_api(request_data);
	
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1){	//success	
			$('#calendar').fullCalendar('destroy');
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
				monthNames:array_month,
				
				eventRender: function(event, element) {
		            element.attr('title', event.tip);
		        },
				
		        eventClick: function(event) {
			        if (event.mid) {
			        	query_detail_data(event.mid, 1);
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
//初始化下拉選單值(cookie值)
function setDefaultQueryOption(){

	cookie_counties= getCookie("counties");
	if(cookie_counties == ""){
		cookie_counties = 19;
		cookie_area = 131374;
		cookie_school = 1837478;
	}else{
		cookie_counties = getCookie("counties");
		cookie_area = getCookie("area");
		cookie_school = getCookie("school");
	}
	//setQueryOptions();
	getCounties(cookie_counties);	//drop down counties
	if ($("#dropDown_yearMonth").val()!=null ||$("#dropDown_yearMonth").val()!=0 ){
		queryMenu();
	}
	//getArea(counties);
	//getSchool(area);
	//getMenuYearMonth(school);
	
	//dropDown_counties  dropDown_area  dropDown_school  dropDown_yearMonth

}

function getCookie(name) {
	if(typeof String.prototype.trim !== 'function') {
		  String.prototype.trim = function() {
		    return this.replace(/^\s+|\s+$/g, ''); 
		  };
		}
	name = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0; i<ca.length; i++) {
		var c = ca[i].trim();
	  	if (c.indexOf(name)==0) return c.substring(name.length,c.length);
	}
	return "";
}

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

//食品雲資料查詢
function getTwfoodtrace(companyId,productName,dom){
	var url = fcloudQuerySourceidUrl + '&companyId='+companyId+'&sourceName='+productName;
	$.get(url,function(result){
		if(result.responseText != ''){
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


//打開認證dialog
function showCertInfo(obj,rowIndex){
	//var obj_ingre_certificate={};
	//var obj_season_certificate={};
	if (obj.className=="ingre"){
		if (typeof(obj_ingre_certificate[rowIndex])!="undefined"){
			var data=obj_ingre_certificate[rowIndex];
			setCertInfo(data);
			
			$(".ui-dialog-content").dialog("close");
			$("#div_certification").dialog();
		}
	}
}
//塞認證資料
function setCertInfo(data){
	var tag_id="#tb_certInfo_";
	
	for(var k in data){
		$(tag_id+k).html(data[k]);
	}
}


//設定六大營養素
function setNutritionData(data){
	var tag_id="#tb_nutrition_";
	
	for(var k in data){
		$(tag_id+k).html(data[k]);
	}
}

//設定供餐者資訊
function setSupplierData(data){
	var kitchenType=data["kitchenType"];
	var tag_id="#tb_kitchen_";
	for(var k in data){
		$(tag_id+k).html(data[k]);
	}
	if (kitchenType=="006"){
		$(".class_supplier005").css("display","none");
		$(".class_supplier006").css("display","");
	}else{
		$(".class_supplier005").css("display","");
		$(".class_supplier006").css("display","none");
	}
}
//前一日後一日按鈕
function showChangeDayButton(before,after){
	$("#btn_before_day").hide();
	$("#btn_after_day").hide();
	/*if(before != 0) {//前一日按鈕			
		$("#btn_before_day").show();
	}
	if(after != 0){//後一日按鈕			
		$("#btn_after_day").show();
	}*/
	$("#controller").append("");
	//控制項(上一頁、前一日、後一日)
	var controller_div = "<div class=\"w_100ps h_44px tal_ch GOBK_BBT\"><a href=\"#\"  onclick=\"showCalendar()\" >回前頁</a> ";
	if(before != 0) {
		controller_div += "<a href=\"#\" onclick=\"query_detail_data(" + before + ",0" +")\">前一日</a> ";
	}
	if(after != 0){
		controller_div += "<a href=\"#\" onclick=\"query_detail_data(" + after + ",0" + ")\">下一日</a></div><div id=\"MAIM_LINER_A\" ></div>";
	}
	$("#controller").html(controller_div);
}
//組合午餐菜色內容
function showDish(data){
	$("#ul_dish").empty();
	var templateString=
		'<li><a href="#" onclick="nextTab()" >'+
			'<img src="@src@"/></a>'+
			'<div class="menu_tit_txt">@dishname@</div>'+
			'<div class="menu_ini_txt">@foodname@</div>'+
		'</a></li>';
	
	for(var i in data){
		var dish=data[i];
		var tagString="";
		if(dish.image){
			tagString=templateString.replace("@src@", dish.image);
			tagString=tagString.replace("@dishname@",dish.category);
			tagString=tagString.replace("@foodname@",dish.foodName);
			$("#ul_dish").append(tagString);
		}
		
	}
}

//塞食材資料表格
//組合食材內容
function setIngredientData(data,type){
	
	var obj_th={
		"all":["foodName","material","authenticate","brand","source"],
		"t17":["foodName","material","brand","source"]
	};
	var myth=[];
	var mydata=[];
	var srcType="";
	if (typeof(type)=="undefined"){
		srcType="all";
	}else{
		srcType=type;
	}
	
	//組資料
	for(var i in data){
		var item=data[i];
		var tmp_array=[];
		for(var k in obj_th[srcType]){
			var data_key=obj_th[srcType][k];
			//showCertInfo
			if (data_key=="authenticate" && item[data_key]!="" ){
				if (item["ingredientCertificateObject"] == null || item["ingredientCertificateObject"]["certNo"]==null){
					tmp_array.push("");
				}else{
					tmp_array.push("<a class='ingre' href='#' onclick='showCertInfo(this,"+i+");return false;'>CAS</a>");
					obj_ingre_certificate[i]=item["ingredientCertificateObject"];
				}
			}
			//show二階查詢
			else if(data_key=="material" && item.material!=""){
				if($.inArray(item.supplierCompanyId,fcloudCounpanyIdList) < 0){
					tmp_array.push(item.material);
				}else{
					var id = item.supplierCompanyId + "_" + i;
					tmp_array.push("<span id='"+ id +"' class='fcloudGet' >"+ item.material +"</span>");
				}
			}			
			else{
				tmp_array.push(item[data_key]);
			}

		}
		mydata.push(tmp_array);
	}
	//組標題
	for(var i in obj_th[type]){
		myth[i]=obj_ingre[obj_th[type][i]];
	}
	
	util.setTableWithHeader(mydata, myth, "tb_detail_ingre");
}

//Assign 二階查詢 hiperlink 給食材
function getFcloudData(dom){
	var supplierCompanyId = dom.attr('id').split('_')[0];
	var material = dom.text();
	var url = fcloudQuerySourceidUrl + '&companyId='+supplierCompanyId+'&sourceName='+material;
    var link = '';
	var tmpProdName = material;

	if (supplierCompanyId === '70390831') {
		if (material === '腿排丁')
			tmpProdName = '(基底)排丁';
		if (material === '雞翅')
			tmpProdName = '三節翅W6';
		if (material === '胸丁')
			tmpProdName = '(基底)雞丁';
	}
	if (supplierCompanyId === '64217504') {
		if (material === '青江菜')
			tmpProdName = '青江段';
	}
	var url = fcloudQuerySourceidUrl + '&companyId='+supplierCompanyId+'&sourceName='+tmpProdName;
	
	$.get(url,function(result){
		if(result.responseText != ''){
			console.log(result.responseText);
			$(dom).html(result.responseText);
			var foodId = $(dom).find("p").html();
			if(foodId!='-1'){
				var link = "<a target='_blank' style='font-size:15px;' href='"+fcloudProductLinkUrl+"&sourceId="+foodId+"&companyId="+supplierCompanyId+"&searchType=1'><u>" + material + "</u></a>";
				$(dom).html(link);
			}
			else{
				$(dom).html(material);
			}
			
		}
	}).fail(function (){
		console.log('fail');
	});
	
//    requestCrossDomain(url, function(result) {
//    	console.log(result);
//    	var tmpDiv = $( '<div></div>' );
//		tmpDiv.html(result.responseText);
//		var foodId = tmpDiv.find("p").html();
//		console.log(foodId);
//		if (foodId != '-1') {
//			link = "<a target='_blank' style='font-size:15px;' href='" + fcloudProductLinkUrl + "&sourceId=" + foodId + "&companyId=" + supplierCompanyId + "&searchType=1'><u>" + material + "</u></a>";
//			dom.html(link);
//		} 
//    });
}

//塞調味料資料表格
function setSeasonData(data,type){
	var obj_th={
		"all":["material","brand","authenticate","source"],
		"t17":["ingredientName","brand","source"]
	};
	var myth=[];
	var mydata=[];
	var srcType="";
	if (typeof(type)=="undefined"){
		srcType="all";
	}else{
		srcType=type;
	}
	//組資料
	for(var i in data){
		var item=data[i];
		var tmp_array=[];
		for(var k in obj_th[srcType]){
			var data_key=obj_th[srcType][k];
			//showCertInfo
			if (data_key=="authenticate" && item[data_key]!=""){
				if (item["ingredientCertificateObject"]==null){
					tmp_array.push("");
				}else{
					tmp_array.push("<a class='season' href='#' onclick='showCertInfo(this,"+i+");return false;'>CAS</a>");
					obj_season_certificate[i]=item["ingredientCertificateObject"];
				}
			}else{
				tmp_array.push(item[data_key]);
			}
		}
		mydata.push(tmp_array);
	}
	//組標題
	for(var i in obj_th[type]){
		myth[i]=obj_ingre[obj_th[type][i]];
	}
	
	util.setTableWithHeader(mydata, myth, "tb_detail_seasoning");
}


//加入認證標章追溯
function setCertification(data,tableName,columnIndex){
	//obj_ingre_certificate
	for(var i=0; i<data.length; i++){
		//CAS認證資訊
		if(data[i].ingredientCertificateObject != null){
			obj_ingre_certificate[i]=data.ingredientCertificateObject;
			//createCasCertInfo(data.ingredientCertificateObject,'materialCert'+i);
		}
		
	}
}

//查詢月曆中的某菜單
function query_detail_data(mid , check){
	var request_data =	{
			 "method":"customerQueryMenuDetailInfo_v2",
 				"args":{
 					"mid" : mid
 				}
			};
	var response_obj = call_rest_api(request_data);

	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		//console.log("前一日:"+result_content.midBefore);
		//console.log("後一日:"+result_content.midAfter);
		if(result_content.resStatus == 1){			
			//標題
			$("#lbl_kitchen_text").html(result_content.schoolName+":"+result_content.date+"營養午餐");
			
			//設定營養份量
			setNutritionData(result_content.nutrition);

			//設定供餐者資訊
			setSupplierData(result_content.supplierInfo);

			//設定顯示控制項(上一頁、前一日、後一日)
			showChangeDayButton(result_content.midBefore,result_content.midAfter);
			
			//設定午餐內容
			showDish(result_content.lunchContent);

			//設定食材資訊
			if ($("#dropDown_counties").val()=="17"){
				setIngredientData(result_content.foodInfo,"t17");
			}else{
				setIngredientData(result_content.foodInfo,"all");
			}
						
			//調味料
			if ($("#dropDown_counties").val()=="17"){
				//setSeasonData(result_content.seasoning,"t17");
				getSeasoning(mid);
			}else{
				setSeasonData(result_content.seasoning,"all");
			}
			
			//組二階資訊
			$('.fcloudGet').each(function(){
			    getFcloudData($(this));
			});
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg + " 可能為連線忙碌。");
	}
	
	showMenuDetail();
}




//查臺北調味料
function getSeasoning(mid){
	var request_data =	{
			 "method":"customerQueryMenuDetailInfov3",
 				"args":{
 					"mid":mid
 				}
			};
	var response_obj = call_rest_api(request_data);
	var result_content=response_obj.result_content;
	
	if(result_content.resStatus == 1){
		setSeasonData(result_content.result,"t17");

	}
}

//Cross domain with YQL
function requestCrossDomain( site, callback ) {
	
	// If no url was passed, exit.
	if ( !site ) {
		alert('No site was passed.');
		return false;
	}
	
	// Take the provided url, and add it to a YQL query. Make sure you encode it!
	var yql = 'http://query.yahooapis.com/v1/public/yql?q=' + encodeURIComponent('select * from html where url="' + site + '"') + '&format=xml&callback=cbFunc';
	
	// Request that YSQL string, and run a callback function.
	// Pass a defined function to prevent cache-busting.
	$.ajaxSettings.async = false; 
	$.getJSON( yql, cbFunc );
	
	function cbFunc(data) {
	// If we have something to work with...
	if ( data.results[0] ) {
		// Strip out all script tags, for security reasons.
		// BE VERY CAREFUL. This helps, but we should do more. 
		data = data.results[0].replace(/<script[^>]*>[\s\S]*?<\/script>/gi, '');
		
		// If the user passed a callback, and it
		// is a function, call it, and send through the data var.
		if ( typeof callback === 'function') {
			callback(data);
		}
	}
	// Else, Maybe we requested a site that doesn't exist, and nothing returned.
	else throw new Error('Nothing returned from getJSON.');
	}
}


