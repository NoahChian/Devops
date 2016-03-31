<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.HashMap" %>
 
<%
String qCountyId="";
String qCountyName="";
String qSchoolId="";
String qSchoolName="";
String basepath="../../";
String mData = "";

if(request.getAttribute("responseMap") != null){
	HashMap<String,String> responseMap = (HashMap<String,String>)request.getAttribute("responseMap");
	//增加訊息回傳alert, 有錯誤直接跳轉  
	if (responseMap.get("errorMsg")!=null){
		String errorhtml="";
		errorhtml="<script>alert('"+responseMap.get("errorMsg").toString()+"');history.go(-1); </script>";
		out.write(errorhtml);
	}
	if (responseMap.get("countyId")!=null){
		qCountyId=(String) responseMap.get("countyId");
	}
	if (responseMap.get("schoolId")!=null){
		qSchoolId=(String) responseMap.get("schoolId");
	}
	if (responseMap.get("countyName")!=null){
		qCountyName=(String) responseMap.get("countyName");
	}
	if (responseMap.get("schoolName")!=null){
		qSchoolName=(String) responseMap.get("schoolName");
	}
	if (responseMap.get("basepath")!=null){
		basepath=(String) responseMap.get("basepath");
	}

	if (responseMap.get("mData")!=null){
		//String testData[] = responseMap.get("mData").toString().split("|");
		//System.out.println("school.jsp mData=="+testData.length);
		mData = responseMap.get("mData");
	}
}
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script>
var qCountyId="<%=qCountyId %>";
var qCountyName="<%=qCountyName %>";
var qSchoolId="<%=qSchoolId %>";
var qSchoolName="<%=qSchoolName %>";
var basepath="<%=basepath%>";
var mData = "<%=mData%>";

</script>
<!-- full screen calendar -->
<!-- script src="../../js/jquery-ui.min.js"></script-->
<script src="<%=basepath%>js/jquery.xdomainajax.js"></script>
<link href="<%=basepath%>css/fullcalendar.css" rel="stylesheet" type="text/css" />
<script src="<%=basepath%>js/fullcalendar.js"></script>
<script src="<%=basepath%>js/system-setting.js"></script>
<!-- <script src="<%=basepath%>js_ca/common.js"></script> -->
<script src="<%=basepath%>js_ca/main_index.js"></script>

<style>
#ul_dish img{width:150px; height:130px;}
</style>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
</head>
<body>
	<div id="MAIN_TITLE_BAR" class="GRE_ABR h_40px lh_40">
		<div class="TITLE_TXT"><a href="http://lunch.twfoodtrace.org.tw/" target="_blank">食材登錄平臺菜單查詢</a></div>
	</div>
	<div id="dss" class="GRA_ABR">
	<div class="SECOND_TXT">
		<table width="100%">
	    	<tr>
	      		<td colspan="8"></td>
	     	</tr>                  
			<tr class="h_40px lh_40">         
<!-- 				<td>縣市別:</td> -->
<!-- 				<td> -->
<!-- 					<select id="dropDown_counties" onchange="setQueryOptions(this)"></select> -->
<!-- 				</td> -->
<!-- 				<td>市/區別:</td> -->
				
<!-- 				<td align="left" valign="middle"> -->
<!-- 					<select id="dropDown_area" onchange="setQueryOptions(this)"></select> -->
<!-- 				</td> 		                         -->
				<td>學校名稱:</td>
	
				<td align="left" valign="middle">
					<select id="dropDown_school" onchange="setQueryOptions(this)"></select>
				</td>
				<td>年/月:</td>
				<td>
					<select id="dropDown_yearMonth" onchange="setQueryOptions(this)"></select>
	        	</td>		                        
			</tr>
	    	<tr>
	      		<td  colspan="8" class="BT_IN_BBTER" align="center" >  
					<div style="font-weight:bold;" ><input type="button" onclick="queryMenu()" value="查詢"/></div>
				</td> 
	     	</tr>                  
		</table>
	</div>
	</div>
	
	<!-- 月曆畫面 -->
	<div id='calendar'></div>
	
	<!-- 供餐資訊 -->
	<div id = "query_detail_info" style="display:none;">
		<div id="MAIN_SECOND_BAR" class="GRA_ABR h_40px lh_40">
			<div class="SECOND_TXT"><span id="lbl_kitchen_text"></span> </div><div id="MAIM_LINER_A"></div>	
		</div>
		
		<div id="TAB_TYP_A" class="">
			<!-- 營養份量 -->
			<div class="TAB_TY_A">
				<table id="tb_detail_nutrition" width="290" border="1"  class="tableCss2" style="width:350px;">
					<tr>
						<td colspan="2" align="center" bgcolor="#678948" class="TIT_A">營養份量</td>				
					</tr>
					<tr>
						<td style="width:200px;">全榖根莖類</td>
						<td><span id="tb_nutrition_mainFood"></span>份</td>
					</tr>
					<tr>
						<td>蔬菜類</td>
						<td><span id="tb_nutrition_vegetable"></span>份</td>
					</tr>
					<tr>
						<td>油脂與堅果種子類</td>
						<td><span id="tb_nutrition_oil"></span>份</td>
					</tr>
					<tr>
						<td>蛋豆魚肉類</td>
						<td ><span id="tb_nutrition_meatBeans"></span>份</td>
					</tr>
					<tr>
						<td>水果類</td>
						<td><span  id="tb_nutrition_fruit"></span>份</td>
					</tr>
					<tr>
						<td>乳品類</td>
						<td><span  id="tb_nutrition_milk"></span>份</td>
					</tr>		
					<tr>
						<td>熱量</td>
						<td><span  id="tb_nutrition_calories"></span>卡</td>
					</tr>	
				</table>
			</div>
			<!-- 供餐者資訊 -->
			<div class="TAB_TY_A">
				<table id="tb_detail_kitchen"  border="1"  class="tableCss2" style="width:350px;">
				<tr>
						<td colspan="2" align="center" bgcolor="#678948" class="TIT_A">供餐者資訊</td>				
					</tr>
					<tr>
						<td  style="width:100px;">
							<span class="class_supplier006">供應學校</span>
							<span class="class_supplier005">供應商</span>
						</td>
						<td><span id="tb_kitchen_supplierName"></span></td>
					</tr>
					<tr>
						<td>
							<span class="class_supplier006">學校地址</span>
							<span class="class_supplier005">供應商地址</span>
						</td>
						<td><span  id="tb_kitchen_supplierAddress"></span></td>
					</tr>
					<tr>
						<td>
							<span class="class_supplier006">學校電話</span>
							<span class="class_supplier005">供應商電話</span>
						</td>
						<td><span  id="tb_kitchen_supplierPhone"></span></td>
					</tr>
					<tr>
						<td>
							<span class="class_supplier006">營養師/<br>午餐秘書</span>
							<span class="class_supplier005">營養師</span>
						</td>
						<td ><span id="tb_kitchen_dietitians"></span></td>
					</tr>
					<tr  class="class_supplier005">
						<td>
							<span>衛管人員</span>
						</td>
						<td><span  id="tb_kitchen_supplierLeader"></span></td>
					</tr>
					<tr  class="class_supplier005">
						<td>
							<span>品質認證體系</span>
						</td>
						<td><span  id="tb_kitchen_authenticate"></span></td>
					</tr>													
				
				
				</table>
			</div>
		</div>

	<!-- 控制前一頁/後一頁的panel -->
	<div id='controller'></div>

	
	<!-- 當日菜單內容資訊 -->
	<div id="tabs">
		<ul>
		    <li><a href="#tabs-1">午餐內容</a></li>
		   	<li><a href="#tabs-2">食材資訊</a></li>
		    <li><a href="#tabs-3">調味料</a></li>
		</ul>	
		<!-- 午餐內容 -->  
	  	<div id="tabs-1" >
	    	<div id="lunch_content">
		    	<div class="MENU_BOX">
		    		<ul id="ul_dish"></ul>
		    		<div style="text-align:center;">菜色圖片為示意圖</div>
		    	</div>
	    	</div>
	  	</div>
	  	<!-- 食材資訊 -->
	  	<div id="tabs-2">
	    	<div id="food_info">
	    		<div class="TAB_TY_C">	    		
		    		<table id="tb_detail_ingre" class="component TAB_TY_C tableCss2">
		    		
		    		</table>
	    		</div>
	    	</div>
	  	</div>
	  	<!-- 調味料 -->
	  	<div id="tabs-3">
	   		<div id="seasoning_info">
	   			<div class="TAB_TY_C">		    		
		    		<table id="tb_detail_seasoning" class="component TAB_TY_C tableCss2">		    		
		    		</table>
	    		</div>
	   		
	   		
	   		</div>
	   	</div>
	</div>
	
	<!-- 追溯認證 -->
	<div id="div_certification" style="display:none;" title="CAS認證廠商資訊">
	<div class="TAB_TY_A" title="CAS認證廠商資訊">
		<table id="" border="1" width="290">
		<tr>
			<td style="width:100px;">認證編號</td>		
			<td><span id="tb_certInfo_certNo"></span></td>		
		</tr>
		<tr>
			<td>公司名稱</td>
			<td><span id="tb_certInfo_companyName"></span></td>
		</tr>
		<tr>
			<td>公司統一編號</td>
			<td><span id="tb_certInfo_companyId"></span></td>
		</tr>
		<tr>
			<td>負責人</td>
			<td><span id="tb_certInfo_director"></span></td>
		</tr>
		<tr>
			<td>地址</td>
			<td ><span id="tb_certInfo_address"></span></td>
		</tr>
		<tr>
			<td>連絡電話</td>
			<td><span  id="tb_certInfo_tel"></span></td>
		</tr>
		<tr>
			<td>傳真</td>
			<td><span  id="tb_certInfo_fax"></span></td>
		</tr>	
		<tr>
			<td>網站</td>
			<td><span  id="tb_certInfo_website"></span></td>
		</tr>	
	</table>
	</div>
	</div>
</div>
<div id="FOOTERx"><span class="FOOTER_JO_FG"><a href="http://lunch.twfoodtrace.org.tw/" target="_blank" >食材登錄平臺</a>由財團法人資訊工業策進會維護，本系統有任何問題，請<a href="mailto:catstory@iii.org.tw">聯絡管理人員</a>。</span></div>


<script>
<%--
// 2014.12.30 Steven 增加
// 為mobile時則自動導到『午餐內容』詳細資料畫面
--%>
var aryMData = mData.split("|");
var aryMData_mType = aryMData[0];
var aryMData_mid = aryMData[1];
var aryMData_school = aryMData[2];
var aryMData_date = aryMData[3];
//setCookie("mid", aryMData_mid);
if( aryMData_mType=='mobile' ) {
	var formatDate = aryMData_date.replace(/-/, "/");
	var strFormatDate = formatDate.substring(0, formatDate.lastIndexOf("-"));

	$("#dropDown_school").append("<option selected value='"+aryMData_school+"'>"+aryMData_school+"</option>");
	$("#dropDown_yearMonth").append("<option selected value='"+strFormatDate+"'>"+strFormatDate+"</option>");

	query_detail_data(aryMData_mid, 1);
	//console.log('[school.jsp], mid=='+aryMData_mid);
}
</script>

</body>
</html>