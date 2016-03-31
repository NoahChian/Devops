<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<link href="../../css/integration.css" rel="stylesheet" type="text/css" />
<script>
		var request_data =	{
				 "method":"SystemMaintainSwitch",
	 				
				};
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			var message = "<p>"+result_content.msg+"</p>";
			//$("#message").append(message);
			alert(message);
		}
		</script>
<title>維護頁面</title>
</head>
<body>
</body>
</html>