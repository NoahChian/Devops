<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/ingredientproperty.js"></script>
</head>
<body>
	<!-- 四點設計套版 -->
	<div class="contents-title">食材檢驗報告與屬性</div>
		<div class="contents-wrap">
	        <div class="section-wrap has-border">
	          <div class="form-horizontal kitchen-info">
	             <form method="post" id="selectFunction">
	             	<div class="col-xs-12" style="margin-bottom: 10px">
	                   <div class="inline-wrap datetimepicker-start">
	                        <input type="hidden" class="form-control inline datetimepicker" id="start_date">
	                   </div>
	                   <h6 style="float: left; margin-top: 26px;">請輸入菜單日期查詢：</h6>&nbsp;
	                   <div class="inline-wrap datetimepicker-end">
	                    	<input type="text" class="form-control inline datetimepicker" id="end_date">
	                   </div>
	                   <input class="btn btn-primary" style="margin: 0" onclick="queryList()" id="search" value="查詢" type="button">	
	                </div> 
					
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
			<div class="TITLE_TXT flo_l">食材檢驗報告與屬性</div>
		</div>
		<form method="post" id="selectFunction">
			<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B">
				<table width="100%">
					<tbody>
						<tr>
							<td>查詢條件</td>
							<td id="tdQueryCond">
								<div id="divQueryCond" class=" ">
									<span class="class_countMenuNonIngre class_statistic class_orgi">
										<input type="hidden" id="start_date" />菜單日期&nbsp;<input type="text" id="end_date" />
									</span> 
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" style="text-align: center;">
								<div class="dis_intb mgl_10 BT_IN_BBTER">
									<input onclick="queryList()" id="search" value="查詢" type="button">
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