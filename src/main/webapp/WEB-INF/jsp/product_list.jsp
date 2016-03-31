<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<script type="text/javascript">
/**
 * 產生店家與商品資訊
 * 
 * @param	store, product資料庫中所有促銷套餐的資訊列表
 * @return	none
 * @author	Titan
 * @date	2013/06/27
 */
function create_list(product_list)
{
	
	//alert(store_list[0]["store_name"]);
	
	/*** 對列表中的每一個內容都新增一個顯示區域(DIV) ***/
	for(var i=0; i < product_list.length; i++)
	{
		//alert(product_list[i].product_obj.length);
		
		if(product_list[i].product_obj.length == 0){
			continue;
		}
		
		/*** 1.宣告一個新的DIV，並且設定DIV內容為促銷方案內容，且ID為該方案在資料庫中的PID ***/
			//新增標題，在標題的部分加入點選後改變下方區域顯示或隱藏的動作
		var new_div ="<h3 onclick=\"toggle_div('"+product_list[i]["sid"]+"')\"><u>"+product_list[i]["storeName"]+"</u></h3>";
		
		/*** 2.宣告一個新的DIV，並且設定DIV內容為促銷方案內容，且ID為該方案在資料庫中的PID ***/	
		/***   做為辨識該DIV的ID，用來改變這個DIV的顯示或隱藏***/
		
		for(var j=0; j < product_list[i].product_obj.length; j++)
		{
		new_div += "<div id="+product_list[i]["sid"]+">";
		//new_div += "<fieldset><legend>詳細資訊</legend>";
		new_div += 	"<fieldset><legend>"+product_list[i].product_obj[j]["productName"]+"</legend>"+
						"<table><tbody>"+
							"<tr><td><img src=\""+product_list[i].product_obj[j].image_obj[0]["imagePath"]+"\"></td>"+
								//"<td><img src=\""+product_list[i].product_obj[j].image_obj[1]["imagePath"]+"\"></td>"+
							"</tr>"+
							"<tr>"+
								"<table><tbody>"+
									"<tr><th>餐點編號</th><td>"+product_list[i].product_obj[j]["pid"]+"</td></tr>"+
									"<tr><th>產品名稱</th><td>"+product_list[i].product_obj[j]["productName"]+"</td></tr>"+
									"<tr><th>產品價格</th><td>"+product_list[i].product_obj[j]["originalPrice"]+"</td></tr>"+
									"<tr><th>產品描述</th><td>"+product_list[i].product_obj[j]["productDescription"]+"</td></tr>"+
								"</tbody></table>"+
							"</tr>"+
						"</tbody></table>"+
					"</fieldset>";
		}
		//var b_cycle = billingCycleMap[promotions_list[i]["billingCycleType"]].chinese;	//查詢帳單週期代號所代表的參數
		//var resource_array = promotions_list[i]["packageRscList"];						//資源列表
						
		/*
		//列出套餐中設定的每一個資源項目
		for(var j=0; j<resource_array.length; j++)
		{
			new_div += 	"<fieldset><legend>商品資訊"+ (j+1) +" </legend>"+
						"<table><tbody>"+
						"<tr><td></td>"+
						"<td>"+
						"<table><tbody>"+
							"<tr><th>名稱</th><td>"+resource_array[j]["rscObj"]["resourceName"]+"</td></tr>"+
							"<tr><th>價格 ID</th><td>"+resource_array[j]["rscObj"]["resourceId"]+"</td></tr>"+
							"<tr><th>描述</th><td>"+resource_array[j]["number"]+"</td></tr>"+
						"</tbody></table>"+
						"</td><tr>"+
						"</tbody></table>"+
						"</fieldset>";
		}
		new_div += "</fieldset>";
		*/
		//加入刪除本促銷活動的按鈕，點下去會呼叫後端REST API刪除此促銷活動
		//new_div += "<p align=\"right\"><input type=\"button\" value=\"刪除此促銷活動\" onclick=\"delete_promotion('"+promotions_list[i]["packageId"]+"')\">";
		new_div += "</p></div>";
		
		/*** 3.將新增的資源計價內容附加上去 ***/
		$("#promotions_list").append(new_div);
		
		/*** 4.將預設的顯示方式設為隱藏 ***/
		$("#"+product_list[i]["sid"]).hide();
	}
}

$(document).ready(function() {

	/*** 改變在header的標題(促銷方案管理)顏色為已選取的樣式 ***/
	$("#coupon").addClass("selected");

	/*** 查詢促銷方案列表 ***/
	var request_data = {"service_token":"iK89Odju", "args":{"onlyquery":"1"}};
	var response_obj = call_rest_api("product","query",request_data);
	
	/*** 動態將查詢到的列表顯示出來 ***/
	if(response_obj.result == 1) {
		//alert(response_obj.result_content.store_array[0]["storeName"]);
		create_list(response_obj.result_content.store_obj);
	} else {
		alert("查詢商家資訊列表時出現錯誤! 錯誤訊息為：" + response_obj.error_msg);
	}
});
</script>

<div id="sub-header">
	<h2>
		<b>商家資訊列表</b>
	</h2>
</div>

<div id="body">
	<div id="NoSidebar-content">
		<div class="box">

			<div id=promotions_list></div>
			<br>
			<br>
			<!-- 
			<input type="button" value="新增促銷方案" onclick="location.href='../create_promotion/'" >
			-->
			<br> <br> <br> <br> <br> <br> <br>
			<br> <br> <br> <br> <br> <br> <br>
			<br>

		</div>
	</div>
	
	<div class="clear"></div>
</div>