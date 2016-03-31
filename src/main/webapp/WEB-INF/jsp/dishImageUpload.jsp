<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Spring MVC Multiple File Upload</title>
    <!-- 
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
 
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
-->
<script src="../../js/webtoolkit.js"></script>

<script src="../../js/upload/jquery-1.9.1.min.js"></script>
<script src="../../js/upload/vendor/jquery.ui.widget.js"></script>
<script src="../../js/upload/jquery.iframe-transport.js"></script>
<script src="../../js/upload/jquery.fileupload.js"></script>

<!-- bootstrap just to have good looking page -->
<script src="../../js/upload/bootstrap/js/bootstrap.min.js"></script>
<link href="../../css/upload/bootstrap.css" rel="stylesheet" type="text/css" />

<!-- we code these -->
<link href="../../css/upload/dropzone.css" type="text/css" rel="stylesheet" />
<script src="../../js/upload/myuploadfunction.js"></script>

</head>
<body>

<h2>菜色圖片上傳</h2>
<div style="width:100%;">

	<input id="fileupload" type="file" name="files[]" data-url="/cateringservice/file/controller/Upload" multiple>
	
	<div id="dropzone" class="fade well"  style="width:80%;">請將圖片檔拖放至此處(一次請勿超過10個檔案 檔案名稱以菜色名稱命名  如:炒青菜.jpg)</div>
	
	<div id="progress" class="progress">
    	<div class="bar" style="width: 0%;"></div>
	</div>

	<table id="uploaded-files" class="table" style="width:100%;">
		<tr >
			<th>檔名</th>
			<th>檔案大小</th>
			<th>檔案類型</th>
			<th>狀態</th>
		</tr>
	</table>
	
</div>

</body>
</html>
