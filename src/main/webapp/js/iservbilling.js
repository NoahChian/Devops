/**
 * 此為iServBilling UI中共同需要用到的javascript function
 * 以及公用的參數設定
 *
 * 提供的function有	:
 * 1.test()
 * 2.call_rest_api()
 * 
 * 
 * @author Eason
 * date：2013/05/15
 */
 
/**
 * 共用參數
 * @author 	Eason
 * @date	2013/05/15
 */
//iServBilling的後台REST API位置
//var iServBilling_rest_url = "http://175.98.115.21:15288/billing/rest/";
var iServBilling_rest_url = "/cateringservice/rest/API/";
//var iServBilling_rest_url = "http://175.98.115.21:14298/oneclickshoppingwall/rest/";

//本服務的存取token但尚未實做
var service_token = "iK89Odju";

 
 /**
 * 用以測試
 * @param	none
 * @return	none
 * @author 	Eason
 * @date	2013/04/25
 */
function test(){
	
	alert(iServBilling_rest_url);
}

/**
 * 所有用來呼叫iServbilling 後台REST API的動作，都會透過這個function來進行呼叫
 * 其中，後台REST API的位置(iServBilling_rest_url)與控制本服務操作權限的token(service_token)
 * 需要先進行設置
 * 
 * ***在這裡調用後端API的方式為同步呼叫，非ajax預設的非同步呼叫。請注意!***
 * 
 * @param	resource		要操作的資源
 * @param	method			操作的方式 和resource兩個決定所要呼叫的API URL
 * @param   request_data	POST回RESTAPI的資料
 * @return	response_obj 	以物件傳回呼叫REST API的操作結果
 * @author 	Eason
 * @date	2013/05/16
 */
function call_rest_api(request_data){

	//宣告傳回值
	var response_obj = new Object;
	
	//alert(iServBilling_rest_url+resource+'/'+method+'/');
	
	/*在這裡我關閉非同步的呼叫後端API(加上async: false)，改為同步的方式呼叫。
	 *才可以確保最終回傳參數(response_obj)的一致性。
	 *就是不會在ajax還沒做完就先return了
	 *ajax預設是非同步調用，搞了我兩個小時 */
	$.ajax(
	{
		url: iServBilling_rest_url,
		//url: "http://175.98.115.21:15288/billing/rest/web/test/",
		//url: 'http://175.98.115.17:80/MUST/rest/Device/list',
		type: 'POST',
		dataType: 'json',
		contentType: 'application/json',
		data: JSON.stringify(request_data),
		async: false,//關閉非同步的調用方式，改為同步	
		error: function(xhr)
		{
			//alert('呼叫rest api發生錯誤，請確認網路狀況與iserverbilling的後臺狀態');
			alert('網路連線忙碌中，請稍候再試');
		},
		/*** 3. ***/
		success: function(response) 
		{
			//將response轉成JSON格式
			
			var response_json = JSON.stringify(response);
			//alert(response_json);
			
			//將response_json再轉成物件
			response_obj = JSON.parse(response_json);
			//alert(response_obj.result_content.test_result);
	
		}
	});
	
	return response_obj;
}

/**
 * 改變傳入ID之區域的顯示狀況。
 *
 * @param	要被改變顯示狀況的區域(div)的id
 * @return	none
 * @author	Eason
 * @date	2013/05/06
 */
function toggle_div(div_id)
{
	//將傳入ID的區塊做顯示和隱藏的切換 參數為切換的速度
	$("#"+div_id).toggle("slow");
}