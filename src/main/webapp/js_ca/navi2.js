var MSG = new MsgsProcessing();

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

function loginIn() {
	$("#loginInForm").submit();
}

function loginOut() {
	window.location.href = "login.do?action=logOut";
}

$(document).ready(function() {
	showMenuTree();
});

// 20140604 Raymond 忘記密碼
function showForgotPwd() {
	alert('usename:'+$('#usename').val());
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

// 查詢登入者權限 add by Joshua 2014/10/1
function showMenuTree(){
	var request_data = {
			"method" : "queryNaviAuthority"
	};
	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) { // API呼叫正確
		var result_content = response_obj.result_content;
		if (result_content.resStatus == 1) {
			var roleType = $("#roletype").val();
			$("#afterLogin").show();
			$("#beforeLogin").hide();
			// 食品業者 | 學校廚房
			if(('kCom' === roleType || 'kSch' === roleType) && '007' !== result_content.userType){
				$("#funcMenu").show();
				$("#govLogin").hide();
				document.getElementById('downloadPath').innerHTML = '<a href="http://175.98.115.58/docs/SystemUserManualppt.pdf" target="_blank">操作教學檔下載</a>';
			}
			// 主管機關
			if('kGov1' === roleType || 'kGov2' === roleType || 'admin' === roleType){
				$("#authorities").show();
				$("#comLogin").hide();
				if('kGov1' === roleType){
					document.getElementById('downloadPath').innerHTML = '<a href="http://175.98.115.58/docs/AdminUserManual_edu.pdf" target="_blank">操作教學檔下載</a>';	
				}else if('kGov2' === roleType){
					document.getElementById('downloadPath').innerHTML = '<a href="http://175.98.115.58/docs/AdminUserManual_health.pdf" target="_blank">操作教學檔下載</a>';
				}else{
					$("#downloadPath").hide();
				}
			}
//			document.getElementById('orgName').innerHTML = result_content.username;
			var menuList = '<div class="NAV_ZONE_A"> ' ;
			for(var i = 0; i < result_content.menuTree.length; i++){
				if(result_content.menuTree[i].parents == 0){
					menuList += '<div class="NAV_ZONE_ATIT"> ';
					menuList += result_content.menuTree[i].name;
				} else{
					menuList += '<div class="NAV_ZONE_AMIN"> ';
					menuList += '<a href="'+result_content.menuTree[i].url+'">'+result_content.menuTree[i].name+'</a>';
				}
				menuList += '</div> ';
			}
			menuList += ' </div> ';
			$("#menuDetail").append(menuList);
		} 
	} else {
		alert("發生無法預期的錯誤，錯誤訊息為：" + response_obj.msg);
		return 0;
	}	
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
