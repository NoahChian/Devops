<%@ page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js_ca/common.js"></script>
<script src="../../js/nodeJsSrv_util.js"></script>
<script src="../../js_ca/auditRecords.js"></script>
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
<script>
    $(document).ready(function() {
        initDate();
    });
</script>
<body>
<%    
    String kitchenId = (String) session.getAttribute("account");
    String uName = "";
    int sid = 0;
    String schoolId = "";    
    if (kitchenId != null) {
        uName = (String) session.getAttribute("uName");
        sid = AuthenUtil.getSchoolIdByUsername(uName.substring(0, 7));
        schoolId = Integer.toString(sid);
    }
%>
<script>    
    var schoolId = <%=schoolId%>;
</script>
    <div class="contents-title">審核記錄查詢報表</div>
    <div class="contents-wrap">
        <table class="table-bordered table-striped" width="100%">
            <tbody>
                <tr>
                    <td style="padding: 10px;" align="center" width="20%">審核項目</td>
                    <td style="padding: 10px;">
                       <select id="approveItem">
                            <option value="all">全部</option>
                            <option value="acceptschoolkitchen">供餐廚房審核</option>
                            <option value="sfschoolproductset">合作社商品上架審核</option>
                       </select>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 10px;" align="center" width="20%">審核狀態</td>
                    <td style="padding: 10px;">
                       <select id="approveStatus">
                            <option value="all">全部</option>
                            <option value="approved">審核通過</option>
                            <option value="waitApprove">審核中</option>
                            <option value="rejected">審核否決</option>
                       </select>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 10px;" align="center" width="20%">申請日期</td>
                    <td style="padding: 10px;">
                       日期(開始)&nbsp;
                       <div class="inline-wrap datetimepicker-start" style="width: 30%">
                            <input type="text" class="form-control inline datetimepicker hasDatepicker" id="start_date">
                       </div>  
                       日期(結束)&nbsp;
                       <div class="inline-wrap datetimepicker-end" style="width: 30%">
                            <input type="text" class="form-control inline datetimepicker hasDatepicker" id="end_date">
                       </div>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 10px;" colspan="4" align="center">
                       <input class="btn btn-primary" style="margin: 0" type="button" onclick='queryApproveRecords()' value="查詢">
                    </td>
                </tr>
            </tbody>
        </table>
        <span id="queryacceptschoolkitchenList" name="queryacceptschoolkitchenList"></span>
        <span id="sepreatespace" name="sepreatespace"></span>
        <span id="querysfschoolproductsetList" name="querysfschoolproductsetList"></span>
        <div id="gotop"><img name="gotopImg" id="gotopImg" src="../../images/up.png" width="48px" height="48px" /></div>
    </div>
</body>
</html>
