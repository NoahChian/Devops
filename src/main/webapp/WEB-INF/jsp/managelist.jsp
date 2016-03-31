<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/managelist.js" type="text/javascript"></script>
</head>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">管理功能清單</div>
		<div class="contents-wrap">
	        <div class="section-wrap has-border">
	          <div class="form-horizontal kitchen-info">
	          	<form method="post" id="selectFunction">
				  <table class="table-bordered table-striped" width="100%" >
					 <tbody>
						<tr>
							<td style="padding: 10px;" align="center" width = "10%">功能選擇</td>
							<td style="padding: 10px;">
							   <select id="ddlQueryType" onChange="showmode();">
								   <option  value="countSchoolMenu">今日有菜單學校清單</option>
								   <option  value="countKitchenMenu">今日有菜單廚房清單</option>
							   		<option  value="countMenuNonIngre">有菜單無食材</option>
							  
					
									<!-- option value="kitchen005List">團膳業者總數(DB內有設定的)</option>
									<option value="kitchen006List">學校廚房總數(DB內有設定的)</option-->
									<!-- <option value="queryCountBySchool">月曆查詢次數統計(依學校)</option>
									<option value="queryCountByDate">月曆查詢次數統計(依日期)</option>	 -->
									
									<!-- option value="dishCount">個別廚房的菜色量統計</option-->
									<!-- option value="ingredientCount">個別廚房的食材量統計</option-->
<!-- 							   		<option value="schoolCount">菜單量統計(依據學校小計)</option> -->
<!-- 									<option value="kitchenCount">菜單量統計(依據廚房小計)</option>				 -->
										   
							   </select>
							</td>
						</tr>
						<tr>
							<td style="padding: 10px;">查詢條件</td>
							<td style="padding: 10px;" id="tdQueryCond">
								<div id="divQueryCond" class="lh_35 ">
										<div style="display:none;">
											學校<select id="ddlSchool" ></select>
										</div>
										<span class="class_countMenuNonIngre class_statistic">
											切換範圍&nbsp;
											<select id="ddlDateRange" onChange="change_date_range()">							
												<option value="7days">最近7天</option>
												<option value="30days">最近30天</option>
												<option value="this.month">本月</option>	
												<option value="season1">第一季</option>
												<option value="season2">第二季</option>
												<option value="season3">第三季</option>
												<option value="season4">第四季</option>
											</select>
										</span>
										<div class="class_countMenuNonIngre class_statistic class_orgi inline-wrap">
											日期(開始)&nbsp;<input type="text" id="start_date" class="datetimepicker" />
										</div>
										<div class="class_countMenuNonIngre class_statistic inline-wrap">
											日期(結束)&nbsp;<input type="text" id="end_date" class="datetimepicker"/>
										</div>
				
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" style="text-align:center; padding: 10px;">
								<input class="btn btn-primary" style="margin: 0" onclick="runFunction()" id="search" value="查詢" type="button">
							</td>
						</tr>
					 </tbody>
				  </table>	
				</form>
					
				<div id="query_list">
					<table id="resultTable" class="table table-bordered table-striped"></table>
				</div>
		      </div>
	        </div><!-- End of .section-wrap -->        
	    </div>
       
	<!-- 舊團膳套版 -->
	<div style="display:none">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
		<div class="TITLE_TXT flo_l">管理功能清單</div>
		</div>
		<form method="post" id="selectFunction">
		<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B">
			<table width="100%" >
			 <tbody>
				<tr>
				<td align="center" width = "10%">功能選擇</td>
				<td>
				   <select id="ddlQueryType" onChange="showmode();">
					   <option  value="countSchoolMenu">今日有菜單學校清單</option>
					   <option  value="countKitchenMenu">今日有菜單廚房清單</option>
				   		<option  value="countMenuNonIngre">有菜單無食材</option>
				  
		
						<!-- option value="kitchen005List">團膳業者總數(DB內有設定的)</option>
						<option value="kitchen006List">學校廚房總數(DB內有設定的)</option-->
						<!-- <option value="queryCountBySchool">月曆查詢次數統計(依學校)</option>
						<option value="queryCountByDate">月曆查詢次數統計(依日期)</option>	 -->
						
						<!-- option value="dishCount">個別廚房的菜色量統計</option-->
						<!-- option value="ingredientCount">個別廚房的食材量統計</option-->
<!-- 				   		<option value="schoolCount">菜單量統計(依據學校小計)</option> -->
<!-- 						<option value="kitchenCount">菜單量統計(依據廚房小計)</option>				 -->
							   
				   </select>
				</td>
				<tr>
				<tr>
					<td>查詢條件</td>
					<td id="tdQueryCond">
						<div id="divQueryCond" class="lh_35 ">
								<div style="display:none;">
									學校<select id="ddlSchool" ></select>
								</div>
								<span class="class_countMenuNonIngre class_statistic">
									切換範圍&nbsp;
									<select id="ddlDateRange" onChange="change_date_range()">							
										<option value="7days">最近7天</option>
										<option value="30days">最近30天</option>
										<option value="this.month">本月</option>	
										<option value="season1">第一季</option>
										<option value="season2">第二季</option>
										<option value="season3">第三季</option>
										<option value="season4">第四季</option>
									</select>
								</span>
								<span class="class_countMenuNonIngre class_statistic class_orgi">
									日期(開始)&nbsp;<input type="text" id="start_date" class="dateMode" />
								</span>
								<span class="class_countMenuNonIngre class_statistic">
									日期(結束)&nbsp;<input type="text" id="end_date" class="dateMode"/>
								</span>
		
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center;">
						<div class="dis_intb mgl_10 BT_IN_BBTER" >
							<input onclick="runFunction()" id="search" value="查詢" type="button">
						</div>
					</td>
				</tr>
			 </tbody>
			</table>
		</div>
		
		</form>
		
		<div id="query_list">
			<table id="resultTable" class="tableCss2"></table>
		</div>
</div>
</body>
</html>
