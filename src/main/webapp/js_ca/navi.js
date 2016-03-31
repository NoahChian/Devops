var MSG = new MsgsProcessing();

//20150603 shine add 增加瀏覽器判斷
$.browser = {};
$.browser.mozilla = /firefox/.test(navigator.userAgent.toLowerCase());
$.browser.webkit = /webkit/.test(navigator.userAgent.toLowerCase());
$.browser.opera = /opera/.test(navigator.userAgent.toLowerCase());
$.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());

function showLoginCompany() {
	$("#loginCompany").show();
	$("#loginGoverment").hide();
	$("#nologinArea").hide();
}

function showLoginGoverment() {
	$("#loginGoverment").show();
	$("#loginCompany").hide();
	$("#nologinArea").hide();
}

function cancelLogin() {
	$("#loginCompany").hide();
	$("#loginGoverment").hide();
	$("#nologinArea").show();
}
function changePassDiv() {
	$("#changePass").show();
}
function cancelChangePass() {
	$("#changePass").hide();
}
function ShowMailDiv() {
	$("#changeMail").show();
}
function cancelChangeMail() {
	$("#changeMail").hide();
}
function submitChangeMail(uName){
	var newMail = $("#mail").val();
	var request_data = {
			"method" : "UpdateMail",
			"args" : {
				"username" : uName,
				"usertype" : '009',
				"email" : newMail
			}
		};
	if (newMail.length > 0) {
		if (!validateEmail(newMail)) {
			MSG.alertMsgs("check", "E-Mail格式錯誤!" , 0);
			//alert("E-Mail格式錯誤!");
			return false;
		}
	}
	
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) { // API呼叫正確
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) {
			cancelChangeMail();
			alert("新郵件："+newMail+"儲存成功！");
			$("#mail").val(newMail);
		} else {
			alert("儲存失敗：" + result_content.msg);
			return 0;
		}
	} else {
		alert("發生無法預期的錯誤，錯誤訊息為：" + response_obj.msg);
		return 0;
	}
}

/**
 * 按下enter鍵仍呼叫loginIn()
 */
//document.onkeydown=function(){
//    if(window.event.keyCode=='13'){
//    	loginIn();
//    }
//}


/**
 * 系統登入(原平台 & NodeJs平台)
 */
function loginIn() {
	var username = $("[aria-expanded='true']").siblings().find("input[name='usename']").val();
	var userpwd = $("[aria-expanded='true']").siblings().find("input[name='userpwd']").val();
	var md5pwd = $.md5(userpwd);
	console.log("username:"+username+", userpwd:"+userpwd+", md5pwd:"+md5pwd);
	var _url01 = url + "/login/";

	if($.browser.mozilla){
		console.log("mozilla");
		//20150804 shine mod 改用iframe方式作login
		//20150527 shine mod 因使用form submit在firefox中會沒作用,所以改其他方式 (#4485) 
		var loginData = {"username":username,"password":md5pwd};
//		var res = ajaxCallJsonp(_url01, loginData, function(data){
			$("#MAINi").empty();
			$('<iframe name="nodeIFrame" id="nodeIFrame" frameborder="0" style="overflow:hidden; width:100%; height:1500px;display:none"  />').appendTo("#MAINi");
			$("#nodeIFrame").attr("src", _url01 + "?username=" + username + "&password=" + md5pwd);
						
			$("#nodeIFrame").load(function(){
				//因要確保nodejs端登錄後才繼續登錄原系統,所以放在這邊			
				$("#loginInForm").submit();
			});						
//		},false);
	}else{		
		// 組iframe & form tag，並登入至nodeJs平台。
		console.log("webkit");
		$('<iframe name="nodeIfrTemp"/>').appendTo("body");
		var form = $("<form action='"+_url01+"' method='POST' target='nodeIfrTemp'>" +
				"<input type='text' name='username' id='username' value='"+username+"'>" +
				"<input type='password' name='password' id='password' value='"+md5pwd+"'>" +
				"</form>");
		form.submit();
		
		$("#loginInForm").submit();
	}	
}

/**
 * 系統登出(原平台 & NodeJs平台)
 */
function logout() {
	var _url01 = url + "/logout/";
	if($.browser.mozilla){
		//20150528 shine mod 因使用form submit在firefox中會沒作用,所以改其他方式 (#4493)
		var res = ajaxCallJsonp(_url01, {}, function(data){
			$("#MAINi").empty();
			$('<iframe name="nodeIFrame" id="nodeIFrame" frameborder="0" style="overflow:hidden; width:100%; height:1500px;display:none"  />').appendTo("#MAINi");
			$("#nodeIFrame").attr("src", _url01);
					
			//呼叫登出API後導回首頁
			$.ajax({
				url: "http://" + window.location.hostname + ":" + window.location.port + "/cateringservice/web/logout/",
				method: "GET",			
				success: function( jqXHR, res ) {
					window.location.href = "http://" + window.location.hostname + "/";
				},
				fail: function( jqXHR, textStatus ) {
					window.location.href = "http://" + window.location.hostname + "/";
				}
			});
		},false);		
	}else{
		// 組iframe & form tag，並登出至nodeJs平台。
		$('<iframe name="nodeIfrTemp"/>').appendTo("body")
		var form = $("<form action='"+_url01+"' method='GET' target='nodeIfrTemp'></form>");
		form.submit();
		
//		ajaxCallJsonp("http://" + window.location.hostname + ":" + window.location.port + "/cateringservice/web/logout/",{},function(data){
//			alert('b');
//			window.location.href = "http://" + window.location.hostname + "/";
//		},false,"GET");	
		
		//呼叫登出API後導回首頁
		$.ajax({
			url: "http://" + window.location.hostname + ":" + window.location.port + "/cateringservice/web/logout/",
			method: "GET",			
			success: function( jqXHR, res ) {
				window.location.href = "http://" + window.location.hostname + "/";
			},
			fail: function( jqXHR, textStatus ) {
				window.location.href = "http://" + window.location.hostname + "/";
			}
		});
	}	
}

/**
 * 公告訊息
 */
function news() {
//	console.log("news running");
	var _url01 = url + "/news/";
//	console.log(_url01);
	$.ajax({
//		url: "http://" + window.location.hostname + ":" + window.location.port + "/news/",
		url: _url01,
		method: "GET",			
		success: function( jqXHR, res ) {
//			console.log("news success");
//			console.log(jqXHR.data);
//			window.location.href = "http://" + window.location.hostname + "/";

			//http://localhost:8080/news.html
			
//			$('#dialogNews').DataTable();			
			
			$("#dialogNews").dialog({
//		        autoOpen: false,
		        minWidth:600,
		        minHeight: 500,
		        width: 600,
		        height: 500,
			    create: function(event, ui) { 
			        var widget = $(this).dialog("widget");
			        $(".ui-dialog-titlebar-close").addClass("ui-button-icon-primary ui-icon ui-icon-closethick");
			     }
			});
			
			list_News(jqXHR, ["項次","類型","標題","前往"], "tableNews");

//			$("#dialogNews").html();
			
			//list 的link
//			var link = "http://" + window.location.hostname + ":" + window.location.port + "/news.html";
//			window.open(link);
			
//			var link = "http://" + window.location.hostname + ":" + window.location.port + "/news-detail.html?newsId=9";
//			window.open(link,"_blank");
		},
		fail: function( jqXHR, textStatus ) {
//			console.log("news fail");
//			window.location.href = "http://" + window.location.hostname + "/";
		}
	});
	
//	var link = "/news-detail.html?newsId=9";
//	window.open(link,"_blank");
	
//	var _url01 = url + "/logout/";
//	if($.browser.mozilla){
//		//20150528 shine mod 因使用form submit在firefox中會沒作用,所以改其他方式 (#4493)
//		var res = ajaxCallJsonp(_url01, {}, function(data){
//			$("#MAINi").empty();
//			$('<iframe name="nodeIFrame" id="nodeIFrame" frameborder="0" style="overflow:hidden; width:100%; height:1500px;display:none"  />').appendTo("#MAINi");
//			$("#nodeIFrame").attr("src", _url01);
//					
//			//呼叫登出API後導回首頁
//			$.ajax({
//				url: "http://" + window.location.hostname + ":" + window.location.port + "/cateringservice/web/logout/",
//				method: "GET",			
//				success: function( jqXHR, res ) {
//					window.location.href = "http://" + window.location.hostname + "/";
//				},
//				fail: function( jqXHR, textStatus ) {
//					window.location.href = "http://" + window.location.hostname + "/";
//				}
//			});
//		},false);		
//	}else{
//		// 組iframe & form tag，並登出至nodeJs平台。
//		$('<iframe name="nodeIfrTemp"/>').appendTo("body")
//		var form = $("<form action='"+_url01+"' method='GET' target='nodeIfrTemp'></form>");
//		form.submit();
//		
//		//呼叫登出API後導回首頁
//		$.ajax({
//			url: "http://" + window.location.hostname + ":" + window.location.port + "/cateringservice/web/logout/",
//			method: "GET",			
//			success: function( jqXHR, res ) {
//				window.location.href = "http://" + window.location.hostname + "/";
//			},
//			fail: function( jqXHR, textStatus ) {
//				window.location.href = "http://" + window.location.hostname + "/";
//			}
//		});
//	}	
}

function loginOut() {
	window.location.href = "login.do?action=logOut";
}
$(document).ready(function() {
//	$("#loginInForm input").keypress(function(e) {
//		if (e.keyCode == 13) {
//			$(this).closest("form").submit();
//		}
//	});
	ko.applyBindings(new securityPwdCheck());
});

//20150903 竚寧 add 安全密碼檢查機制
function securityPwdCheck() {
    var self = this;
    self.password = ko.observable("");
    self.pwdConfirm = ko.observable("");
    
    self.pwdRule1 = ko.computed(function () {
        //至少一個英文字母
        return self.password().match(/[A-Za-z]/);
    });
    self.pwdRule2 = ko.computed(function () {
        //至少一個數字
        return self.password().match(/[0-9]/);
    });
    self.pwdRule3 = ko.computed(function () {
        //至少8個字元
        return self.password().length > 7;
    });
    self.allPass = ko.computed(function () {
        return self.pwdRule1() && self.pwdRule2() && self.pwdRule3();
    });
    self.match = ko.computed(function () {
    	if((self.pwdConfirm().length > 0 && self.password() == self.pwdConfirm()) && self.allPass()){
    		$("#changePassSubmit").css("display", "inline-block");
    	}
    	else{
    		$("#changePassSubmit").css("display", "none");
    	}
        return self.pwdConfirm().length > 0 &&
               self.password() == self.pwdConfirm();
    });
}

// 20140604 Raymond 忘記密碼
function showForgotPwd() {
	$("#forgotPwd").dialog({
		closeOnEscape : false,
		open : function(event, ui) {
			$(".ui-dialog-titlebar-close").hide();
		},
		width : 'auto',
		height : 'auto'
	});
}

// 20140604 Raymond 關閉忘記密碼視窗
function cancelResetPwd() {
	$('.forgotPwd').val('');
	$("#forgotPwd").dialog("close");
}

// 送出忘記密碼請求
function postResetPwd() {
	if($('#userid').val() === ''){
		alert('請輸入帳號');
		return;
	}
		
	
	if($('#useremail').val() === ''){
		alert('請輸入E-Mail');
		return;
	}
		
	
	var request_data = {
		"method" : "addApplyForgotPassword",
		"args" : {
			"username" : $('#userid').val(),
			"email" : $('#useremail').val()
		}
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) { // API呼叫正確
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) {
			cancelResetPwd();
			alert("請至您的電子郵件信箱收取確認信件,並依照步驟重新設定您的密碼!");
		} else {
			alert("申請失敗：" + result_content.msg);
			return 0;
		}
	} else {
		alert("發生無法預期的錯誤，錯誤訊息為：" + response_obj.msg);
		return 0;
	}
}

/**
 * 幼兒園選單列之按鈕
 */
function preSchoolMenuButton(menuItem) {
//	$("#MAINi").empty();
//	$('<iframe name="nodeIFrame" id="nodeIFrame" frameborder="0" style="overflow:hidden; width:100%; height:1500px" />').appendTo("#MAINi");
//	$("#nodeIFrame").attr("src", url + "/#/" + menuItem + "/");
	
	//20150605 竚寧mod 在iframe載入前先出現banner
	$("#MAINi").empty();
	$('<div id="iframeBanner" class="md-toolbar-tools" style="background-color: #BEE9DC;    font-size: 16px;    width: 100%;    color: #506e35;    text-shadow: none;    height: 60px;   "><span></span></div>').appendTo("#MAINi");
	$('<iframe name="nodeIFrame" id="nodeIFrame" frameborder="0" style="overflow:hidden; width:100%; height:1500px;display:none"  />').appendTo("#MAINi");
	
	if(menuItem.startsWith("http://")){
		$("#nodeIFrame").attr("src", menuItem);
	}else if(menuItem.endsWith(".html")){
        $("#nodeIFrame").attr("src", menuItem);
	}else{
		$("#nodeIFrame").attr("src", url + "/#/" + menuItem + "/");
	}
	
	$("#nodeIFrame").load(function(){
		$("#iframeBanner").hide();
		$("#nodeIFrame").show();
	});
	
	
}

//檢核email格式
function validateEmail(sEmail) {
	var filter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
	if (filter.test(sEmail)) {
		return true;
	}
	else {
		return false;
	}
}


function list_News(result_content,header,tableName){
	//清單內容
	$("#" + tableName).empty();
	var html = "";
	// var thCount=header.length;
	html += "<thead><tr align='center'>";
	for ( var item in header) {
		html += "<th>" + header[item] + "</th/>";
	}
	html += "</tr></thead>";
	var td_class="";
	for(var i in result_content.data) {
		if(i%2==0){
			td_class =  "align='center'";
		}else{

			td_class =  "align='center'";
		}
		
		var newsId = "";
		var newsTitle = "";
		var categoryName = "";
		
		if(result_content.data[i].newsId!=null){newsId=result_content.data[i].newsId;}
		if(result_content.data[i].newsTitle!=null){newsTitle=result_content.data[i].newsTitle;}
		if(result_content.data[i].categoryName!=null){categoryName=result_content.data[i].categoryName;}
		
		var link = "http://" + window.location.hostname + ":" + window.location.port + "/frontend/news-detail.html?newsId="+newsId;
//		window.open(link,"_blank");
		
		//	["項次","類型","標題","公告期間","最後更新者",'功能操作']
		var strTR = "<tr>";
		strTR += "<td width=\"10%\"" + td_class +  ">" + newsId + "</td>";
		strTR += "<td width=\"15%\"" + td_class +  ">" + categoryName + "</td>";
		strTR += "<td width=\"70%\"" + td_class +  "onclick=window.open(\""+ link +"\")>" + toShortString(newsTitle,20) + "</td>";
		strTR += "<td width=\"5%\"><button class=\"btn btn-primary\" style=\"min-width:48%\" onclick=window.open(\""+ link +"\");><i class=\"fa fa-arrow-circle-right\"></i></button> ";
//		strTR += "<button class=\"btn btn-primary\" style=\"min-width:48%\" onclick=removeNews(" + newsId + ")><i class=\"fa fa-trash-o\"></i></button></td>";
		strTR += "</tr>";
		html += strTR;
	}
	$("#" + tableName).html(html);
	
	
//	$('td[dt="group1"]').rowspan();
	
	$("#" + tableName).hide();
	$("#" + tableName).show();
	
//	$("#" + tableName).show("slow");
//	$("#MAIN_TITLE_BAR").show("slow");
//	$("#tiltleAct").show("slow"); //匯出功能
}

function toShortString(str,limitlength){
	var shortStr="";
	str = str.replace(/(<([^>]+)>)/ig,"");
	if(str.length > limitlength){
		shortStr = "<div title='"+str+"'>" + str.substring(0,limitlength) +"...</div>";
	}
	else{
		shortStr = "<div title='"+str+"'>" + str + "</div>";
	}
	return shortStr;
}