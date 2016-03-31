<%@ page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- tooltip顯示設定 -->
<style type="text/css">
div:hover:before {
	content: attr(data-tooltip);
	background: #ffff14;
	color: #4f4f4f;
	position: absolute;
	top: -40px;
	left: -65px;
}
</style>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/listAcceptSchoolKitchenWaitApproveBySchool.js"></script>
<script src="../../js_ca/listSfSchoolproductsetWaitApproveBySchool.js"></script>
</head>
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
        var kitchenId = <%=kitchenId%>;
        var schoolId = <%=schoolId%>;
    </script>
    <div class="contents-title">待審核項目清單</div>
    <div class="contents-wrap">
        <span id="acceptSchoolKitchenWaitApproveBySchoolList" name="acceptSchoolKitchenWaitApproveBySchoolList"></span>
        <span id="sfSchoolproductsetWaitApproveBySchoolList" name="sfSchoolproductsetWaitApproveBySchoolList"></span>
    </div>
    <!-- End of #listForm -->
    </div>
</body>
</html>
