<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/missingCase.js"></script>
</head>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">缺漏資料查詢</div>
		<div class="contents-wrap">
	        <div class="section-wrap has-border">
	          <div class="form-horizontal kitchen-info">
	             <form method="post" id="selectFunction">
	             	<div class="col-xs-12" style="margin-bottom: 10px">
	                    <h6>請輸入日期區間查詢：</h6>
		             	<div class="inline-wrap datetimepicker-start" style="width: 30%">
	                        <input type="text" class="form-control inline datetimepicker" id="start_date">
	                    </div>
	                    <div class="inline-wrap datetimepicker-to" style="margin-bottom: 2px">to</div>
	                    <div class="inline-wrap datetimepicker-end" style="width: 30%; margin-right: 5px;">
	                    	<input type="text" class="form-control inline datetimepicker" id="end_date">
	                    </div>
	                    <input class="btn btn-primary" style="margin: 0 0 2px 0" onclick="checkingMissingCase()" id="search" value="查詢" type="button">	
	                </div> 
				</form>
				<div id="query_list">
					<table id="resultTable" class="table table-bordered table-striped"></table>
					<table id="resultTable1" class="table table-bordered table-striped"></table>
					<table id="resultTable2" class="table table-bordered table-striped"></table>
					<table id="resultTable3" class="table table-bordered table-striped"></table>
				</div>
	          </div>
	        </div><!-- End of .section-wrap -->        
	    </div>
       
	<!-- 舊團膳套版 -->
	<div style="display:none">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
			<div class="TITLE_TXT flo_l">缺漏資料查詢</div>
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
										日期(開始)&nbsp;<input type="text" id="start_date"  class="dateMode" />
									</span> <span class="class_countMenuNonIngre class_statistic">
										日期(結束)&nbsp;<input type="text" id="end_date"  class="dateMode"/>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" style="text-align: center;">
								<div class="dis_intb mgl_10 BT_IN_BBTER">
									<input onclick="checkingMissingCase()" id="search" value="查詢"
										type="button">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
		<div id="query_list">
			<table id="resultTable" class="tableCss2"></table>
			<table id="resultTable1" class="tableCss2"></table>
			<table id="resultTable2" class="tableCss2"></table>
			<table id="resultTable3" class="tableCss2"></table>
		</div>
	</div>
</body>
</html>