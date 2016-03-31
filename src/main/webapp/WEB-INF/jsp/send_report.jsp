<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<script>
var timerID1 = 0;
var timerID2 = 0;
var product_timer_string="";


function product_coupon_statistics_timer(store_id)
{
	clearTimeout(timerID2); 
	
	var request_data = {"service_token":"iK89Odju", "args":{"storeId":store_id, "countingItem":"sent"}};
	var response_obj = call_rest_api("coupon","countingByProduct",request_data);
	
	if(response_obj.result == 1) {
		var chartObj1 = getChartFromId("myChartIdChartContainer2");
	    chartObj1.setDataXML(response_obj.result_content.chartData);
	    timerID2 = setTimeout(product_timer_string, 5000);
	} else {
		alert("查詢優惠券送出統計資料錯誤! 錯誤訊息為：" + response_obj.error_msg);
	}
	
}

function show_product_chart(store_id)
{	
	clearTimeout(timerID2);
	product_timer_string = "product_coupon_statistics_timer('"+store_id+"');";
	$("#chartContainer2").show();
	var request_data = {"service_token":"iK89Odju", "args":{"storeId":store_id, "countingItem":"sent"}};
	var response_obj = call_rest_api("coupon","countingByProduct",request_data);
	
	if(response_obj.result == 1) {
		if(response_obj.result_content.resStatus) {
			var chartObj1 = getChartFromId("myChartIdChartContainer2");
			if(chartObj1==null) {
				draw_chart(response_obj.result_content.chartData, "ChartContainer2");
				timerID2 = setTimeout(product_timer_string, 5000);
			}else{
				product_coupon_statistics_timer(store_id);
			}
		} else {
			alert("請確認是否有產品資料!");
		}
	} else {
		alert("查詢優惠券送出統計資料錯誤! 錯誤訊息為：" + response_obj.error_msg);
	}
	
}



function store_coupon_statistics_timer()
{
	clearTimeout(timerID1); 
	
	var request_data = {"service_token":"iK89Odju", "args":{"countingItem":"sent"}};
	var response_obj = call_rest_api("coupon","countingByStore",request_data);
	
	if(response_obj.result == 1) {
		var chartObj1 = getChartFromId("myChartIdChartContainer1");
	    chartObj1.setDataXML(response_obj.result_content.chartData);
	    timerID1 = setTimeout("store_coupon_statistics_timer();", 5000);
	} else {
		alert("查詢優惠券送出統計資料錯誤! 錯誤訊息為：" + response_obj.error_msg);
	}
	
}


function draw_chart(chartData, chartDiv)
{
	
	var myChart = new FusionCharts( "Column3D",
			"myChartId"+chartDiv, "750", "350", "0", "1" );
	myChart.setXMLData(chartData);
	myChart.render(chartDiv);
}

$(document).ready(function() {

	$("#reports").addClass("selected");
	$("#chartContainer2").hide();

	var request_data = {"service_token":"iK89Odju", "args":{"countingItem":"sent"}};
	var response_obj = call_rest_api("coupon","countingByStore",request_data);
	
	if(response_obj.result == 1) {
		if(response_obj.result_content.resStatus == 1) {
			draw_chart(response_obj.result_content.chartData, "ChartContainer1");
			timerID1 = setTimeout("store_coupon_statistics_timer();", 5000);
			//timerID1 = setInterval("store_coupon_statistics_timer();", 5000);
		} else {
			alert("請確認是否有商家資料!");
		}
	} else {
		alert("查詢優惠券送出統計資料錯誤! 錯誤訊息為：" + response_obj.error_msg);
	}
	
	
});

</script>
<div id="sub-header">
	<h2>
		<b>各項營運報表</b>
	</h2>
</div>
<div id="body">
	<div id="body">            
		<div id="NoSidebar-content">
            <div class="box">
				
            </div>
        </div>
        <!-- 
		<img src="https://chart.googleapis.com/chart?chs=500x400&cht=p&chco=FFCC33|C8FF75&chd=e:RSO7RlZT&chdl=%E6%96%B9%E6%A1%88A|%E6%96%B9%E6%A1%88B|%E6%96%B9%E6%A1%88C|%E6%96%B9%E6%A1%88D&chp=0.1&chl=%E6%96%B9%E6%A1%88A|%E6%96%B9%E6%A1%88B|%E6%96%B9%E6%A1%88C|%E6%96%B9%E6%A1%88D&chtt=%E4%BF%83%E9%8A%B7%E6%96%B9%E6%A1%88%E4%BD%BF%E7%94%A8%E6%AF%94%E4%BE%8B&chts=000000,15.5" width="500" height="400" alt="促銷方案使用比例" />
		<img src="https://chart.googleapis.com/chart?chs=500x400&cht=p&chco=FFCC33|C8FF75&chd=e:RSO7RlZT&chdl=%E6%96%B9%E6%A1%88A|%E6%96%B9%E6%A1%88B|%E6%96%B9%E6%A1%88C|%E6%96%B9%E6%A1%88D&chp=0.1&chl=%E6%96%B9%E6%A1%88A|%E6%96%B9%E6%A1%88B|%E6%96%B9%E6%A1%88C|%E6%96%B9%E6%A1%88D&chtt=%E4%BF%83%E9%8A%B7%E6%96%B9%E6%A1%88%E4%BD%BF%E7%94%A8%E6%AF%94%E4%BE%8B&chts=000000,15.5" width="500" height="400" alt="促銷方案使用比例" />
		<img src="https://chart.googleapis.com/chart?chxr=0,0,46&chxt=y&chs=300x225&cht=lc&chco=3D7930&chd=s:Xhiugtqi&chg=14.3,-1,1,1&chls=2,4,0&chm=B,C5D4B5BB,0,0,0&chtt=%E6%B6%88%E8%B2%BB%E5%88%86%E4%BD%88%E5%9C%96" width="300" height="225" alt="消費分佈圖" />
		<img src="https://chart.googleapis.com/chart?chs=300x150&cht=p3&chco=7777CC|76A4FB|3399CC|3366CC&chd=s:Uf9a&chdl=January|February|March|April" width="300" height="150" alt="" />
		<img src="https://chart.googleapis.com/chart?chs=300x150&cht=p3&chco=7777CC|76A4FB|3399CC|3366CC&chd=s:Uf9a&chdl=January|February|March|April" width="300" height="150" alt="" />
		<img src="https://chart.googleapis.com/chart?chs=300x150&cht=p3&chco=7777CC|76A4FB|3399CC|3366CC&chd=s:Uf9a&chdl=January|February|March|April" width="300" height="150" alt="" />
		<img src="https://chart.googleapis.com/chart?chs=300x150&cht=p3&chco=7777CC|76A4FB|3399CC|3366CC&chd=s:Uf9a&chdl=January|February|March|April" width="300" height="150" alt="" />
		-->
		<div id="ChartContainer1"></div>
		<div id="ChartContainer2"></div>
		<div class="clear"></div>
    </div>
</div>