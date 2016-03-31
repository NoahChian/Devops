var mainUrl = "../main/";

$(document).ready(function() {
	validatUrlInfo();
});

//取得URL值
function get_param(arg_str) {

	var url = window.location.toString(); // 取得當前網址
	var str = ""; // 參數中等號左邊的值
	var str_value = ""; // 參數中等號右邊的值

	if (url.indexOf("?") != -1) // 如果網址有"?"符號
	{
		var ary = url.split("?")[1].split("&");
		// 取得"?"右邊網址後利用"&"分割字串存入ary陣列 ["a=1","b=2","c=3"]
		for ( var i in ary) {
			// 取得陣列長度去跑迴圈，如:網址有三個參數，則會跑三次
			str = ary[i].split("=")[0];
			// 取得參數"="左邊的值存入str變數中
			if (str == arg_str) {
				str_value = ary[i].split("=")[1];
				return str_value;
			}
		}
		// 如果都沒有拿到這參數，則回傳錯誤
		if (str_value == "") {
			str_value = "get_param_ERROR";
			return str_value;
		}
	}
}

//驗證URL值
function validatUrlInfo() {
	var username = get_param('u');
	var email = get_param('e');
	var ts = get_param('t');

	if (username === 'get_param_ERROR' || email === 'get_param_ERROR' || ts === 'get_param_ERROR') {
		alert("資料傳遞錯誤,請洽系統服務廠商");
		window.location.href = mainUrl;
		return 0;
	}

	var request_data = {
		"method" : "updateResetUserPassword",
		"args" : {
			"username" : username,
			"email" : email,
			"ts" : ts,
			"actType" : "validat"
		}
	};

	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) { // API呼叫正確
		var result_content = response_obj.result_content;
		if (result_content.resStatus != 1) {
			alert(result_content.msg);
			window.location.href = mainUrl;
			return 0;
		}
	} else {
		alert("發生無法預期的錯誤，錯誤訊息為：" + response_obj.msg);
		window.location.href = mainUrl;
		return 0;
	}
}

//更新User password
function updateUserPwd() {
	var username = get_param('u');
	var email = get_param('e');
	var ts = get_param('t');

	if (username === 'get_param_ERROR' || email === 'get_param_ERROR' || ts === 'get_param_ERROR') {
		alert("資料傳遞錯誤,請洽系統服務廠商");
		window.location.href = mainUrl;
		return 0;
	}

	if (!$('#pwd').val()) {
		alert("請輸入密碼!");
		return 0;
	}

	if (!$('#confPwd').val()) {
		alert("請輸入密碼驗證!");
		return 0;
	}

	if ($('#pwd').val() != $('#confPwd').val()) {
		alert("密碼與密碼驗證不相同!");
		return 0;
	}

	var request_data = {
		"method" : "updateResetUserPassword",
		"args" : {
			"username" : username,
			"email" : email,
			"ts" : ts,
			"password" : $('#pwd').val(),
			"valPassword" : $('#confPwd').val(),
			"actType" : "update"
		}
	};

	var response_obj = call_rest_api(request_data);
	if (response_obj.result == 1) { // API呼叫正確
		var result_content = response_obj.result_content;
		if (result_content.resStatus != 1) {
			alert(result_content.msg);
			return 0;
		} else {
			alert(result_content.msg);
			window.location.href = mainUrl;
			return 0;
		}
	} else {
		alert("發生無法預期的錯誤，錯誤訊息為：" + response_obj.msg);
		return 0;
	}
}