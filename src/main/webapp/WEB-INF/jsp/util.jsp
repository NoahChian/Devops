<%@ pagelanguage="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="parameter.jsp" %>
<script>
	function util_check_date_range(objDate_start,objDate_end){
		var diffSecs=objDate_end.getTime()-objDate_start.getTime()+60000;
		
		if (diffSecs<0){
			alert("結束日期不得小於起始日期!");
			return false;
		}else if(diffSecs/ (1000 * 60 * 60 * 24)>ENV_QUERY_DAYS_LIMIT){
			alert("查詢區間不得超過90日!");
			return false;
		}else{
			return true;
		}
	}
</script>