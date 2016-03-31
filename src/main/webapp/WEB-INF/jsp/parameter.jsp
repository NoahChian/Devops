<%@ pagelanguage="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="org.iii.ideas.catering_service.util.CateringServiceUtil" %>
<script>
var ENV_QUERY_DAYS_LIMIT='<%=CateringServiceUtil.getConfig("query_days_limit")%>';
var ENV_UPLOAD_DAY_LIMIT='<%=CateringServiceUtil.getConfig("excel_limit_time")%>';
</script>
