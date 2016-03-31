<%@page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="util.jsp" %> <!-- include js的自訂公用函式    20140219 KC -->

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<%
	String uType = "";
	String uName = "";
	String roletype = "";
	String countyType = "";
	int sid = 0;
	if(session.getAttribute("account")!=null){
		uType = (String) session.getAttribute("uType");
		uName = (String) session.getAttribute("uName");
		roletype = (String) session.getAttribute("roletype");
		countyType = AuthenUtil.getCountyTypeByUsername(uName);
		sid = AuthenUtil.getSchoolIdByUsername(uName.substring(0, 7));
	}
%>

<script src="../../js_ca/common.js"></script>
<script src="../../js/nodeJsSrv_util.js" type="text/javascript"></script>
<script src="../../js_ca/manageListSchoolProductSet.js" type="text/javascript"></script>
<style type="text/css">
#gotop {
    display: none;
    position: fixed;
    right: 20px;
    bottom: 20px;    
    padding: 10px 15px;    
    font-size: 20px;
    color: white;
    cursor: pointer;
}
</style>

</head>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">商品上下架管理</div>
		<div class="contents-wrap">
	         <div class="section-wrap has-border kitchen-info">
	           <div class="container-fluid">
	             <div class="row">
	             	<input type="hidden" id="sid" value="<%=sid%>">
					<h6 class="inline-wrap" style="margin-top: 6px">請輸入產品名稱查詢︰</h6>
					<div class="dis_intb LOGIN_US_PS inline-wrap">
						&nbsp;<input type="text" id="productName" name="productName" style="height: 30px" class="hasDatepicker max255" maxlength="255" onkeydown="changeSelPage()" autocomplete="off" placeholder="ex：牛奶">
					</div>
					<h6 class="inline-wrap" style= "margin-top: 6px">&nbsp;商品狀態:&nbsp;</h6>
					<select class="inline-wrap" style="height: 30px; margin-left: 5px;margin-top: 6px;border: 1px solid #67c6a7;" id="ProductState">
	        			<option value= 0>全部</option>
						<option value= 1>上架</option>
						<option value= 2>下架</option>
						<option value= 3>未登錄</option>	
	        		</select>
					<div class="dis_intb mgl_10 inline-wrap">
						&nbsp;&nbsp;<input class='btn btn-primary' style='margin:0' type="button" id="search" value="查詢" onclick="redirectNodeJs()">
						&nbsp;&nbsp;<input class='btn btn-primary' style='margin:0' type="button" id="search" value="商品上架申請記錄" onclick="qryHalfYearSfSchoolproductset()">
					</div>
					
					<h5 class="section-head with-border" style="height:35px; margin-bottom: 1px;">
						<div id="query_list">
							<div id="query_rule" class="TITLE_TXT flo_l"></div>
							<div class="TITLE_TXT flo_l "><span id="page" ></span></div>
							<div class="TITLE_TXT_BBT FL_R" id="displaySubmitBtnModify"></div>
						</div>
					</h5>
					<table id="resultTable" class="table table-bordered table-striped"></table>
					<div id="gotop"><img name="gotopImg" id="gotopImg" src="../../images/up.png" width="48px" height="48px" /></div>
	             </div><!-- End of .row -->
	           </div><!-- End of .container-fluid -->
	         </div><!-- End of .section-wrap -->
	         
	         <!-- 商品上架申請記錄檢視 Modal -->
             <div class="modal fade" id="sfSchoolproductsetModal" role="dialog">
                <div class="modal-dialog">
                <div class="modal-lg">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <div class="contents-title">商品上架申請記錄檢視</div>
                        </div>
                        <div class="modal-body">
                            <span id="halfYearAskList" name="halfYearAskList">
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary"
                                data-dismiss="modal">確定</button>
                        </div>
                    </div>
                    </div>
                </div>
             </div>
            
		</div>
	<!-- 舊團膳套版 -->
	<div style="display:none">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
		<div class="TITLE_TXT flo_l">商品上下架管理</div>
		<input type="hidden" id="sid" value="<%=sid%>">
		</div>
		
		<%--<form method="post" id="selectFunction"></form>--%>
		<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B">
			<div class="h_35px lh_35 GRA_ABR" id="MAIN_SECOND_BAR">
				<!-- form name="searchForm" method="post" action="../listSupplier/"-->
					<div class="dis_intb SECOND_TXT_INP">請輸入查詢條件︰</div>
					<div class="dis_intb LOGIN_US_PS">
						產品名稱&nbsp;<input type="text" id="productName" name="productName" class="hasDatepicker max255" maxlength="255" onkeydown="changeSelPage()" autocomplete="off" placeholder="ex：牛奶">
					</div>
					<div class="dis_intb mgl_10 BT_IN_BBTER">
						&nbsp;&nbsp;<input type="button" id="search" value="查詢" onclick="redirectNodeJs()">
					</div>
				<!-- /form--->
			</div>
		</div>
		
		<%--
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
			<div class="TITLE_TXT flo_l">導向功能測試</div>
		</div>
		<div id="MAIN_SECOND_BAR" class="lh_35 GRA_ABR TAB_TY_B">
			<div class="h_35px lh_35 GRA_ABR" id="MAIN_SECOND_BAR">
				<!-- form name="searchForm" method="post" action="../listSupplier/"-->
					<div class="dis_intb SECOND_TXT_INP">可導的連結︰</div>
					<div class="dis_intb mgl_10 BT_IN_BBTER">
						&nbsp;&nbsp;<input type="button" id="querySfProductList" value="querySfProductList" onclick="redirectNodeJs(this.id)">
						&nbsp;&nbsp;<input type="button" id="updateSfProductList" value="updateSfProductList" onclick="redirectNodeJs(this.id)">
						&nbsp;&nbsp;<input type="button" id="customerQuerySF" value="customerQuerySF" onclick="redirectNodeJs(this.id)">
					</div>
				<!-- /form--->
			</div>
		</div>
		 --%>
		<div id="query_list">
			<div class="GRE_ABR h_30px lh_30" id="MAIN_TITLE_BAR">
				<div id="query_rule" class="TITLE_TXT flo_l"></div>
				<div class="TITLE_TXT flo_l "><span id="page" ></span></div>
				<div class="TITLE_TXT_BBT FL_R" id="displaySubmitBtnModify"></div>
			</div>
			<table id="resultTable" class="component"></table>
		</div>
		
		<div id="gotop"><img name="gotopImg" id="gotopImg" src="../../images/up.png" width="48px" height="48px" /></div>
	</div>
</body>
</html>
