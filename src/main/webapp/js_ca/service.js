var util=new CateringServiceUtil();

$(document).ready(function(){	
	$("#basicData").hide();
	$("#tbList").hide();
});


function queryResult(action){
	$("#tbList").html("");
	if ($("#txtAccount").val()==""){
		alert("請輸入帳號");
		$("#basicData").hide();
		return;
	}
	var request_data =	{
			 "method":"serviceQueryLog",
				"args":{
					"action":action,
					"account":$("#txtAccount").val(),
					"content":{}
				}
			};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;		
		if(result_content.resStatus == 1){	//success
			var ikey=2;
			for(var k in result_content.logs){
				var path=result_content.logs[k][ikey];
				if (path!="0" && path!="1" && path!="-1"){
					result_content.logs[k][ikey]="<a class='btn btn-primary' style='margin:0' href='/cateringservice/file/SYSFILE/"+path +"' ><i class='fa fa-file-text-o'></i> 檔案</a>";
				}
			}
			if(result_content.logs.length >0){
				util.setTableWithHeader(result_content.logs, ["時間","動作","狀態碼","訊息"], "tbList");
			}
			setAccount(result_content.accountInfo);

		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	}
	$("#accountName").css("display", "block");
	$("#cateringSch").css("display", "block");
	
	$("#tbList").find("tr").eq(0).find("th").eq(0).css("width","20%");
	$("#tbList").find("tr").eq(0).find("th").eq(1).css("width","20%");
	$("#basicData").show("slow");
	$("#tbList").show("slow");

}



//設定帳戶資訊
function setAccount(data){
	var tag_id="#tb_account_";
	
	for(var k in data){
		$(tag_id+k).html(data[k]);
	}
}

function logging(){
	if ($("#txtAccount").val()=="" ||$("#txtQA").val()=="" ){
		alert("請輸入帳號及問題描述");
		return;
	}
	
	
	var request_data =	{
			 "method":"serviceQueryLog",
				"args":{
					"action":"logging",
					"account":$("#txtAccount").val(),
					"content":{
						"qa_type":$("#ddl_QA_type").val(),
						"qa_description":$("#txtQA").val(),
						"qa_username":$("#tb_account_uname").html()
						
					}
				}
			};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;		
		if(result_content.resStatus == 1){	//success
			util.setTableWithHeader(result_content.result, [], "tbList");
			alert("客服記錄儲存成功");
			initStatus();
		} else {
			//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			alert("客服記錄儲存失敗");
		}
	}
	
}
function initStatus(){
	$("#txtAccount").val("");
	$("#txtQA").val("");
	
}
