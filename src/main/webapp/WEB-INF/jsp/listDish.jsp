<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.iii.ideas.catering_service.dao.Dish" %>
<%@ page import="org.iii.ideas.catering_service.dao.Ingredient" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
<%
	if(request.getAttribute("responseMap") != null)
	{
		HashMap<String,Object> responseMap = (HashMap<String,Object>)request.getAttribute("responseMap");
		//List<Dish> dishList = (List<Dish>)responseMap.get("dishList");
		
		//增加訊息回傳alert, 有錯誤直接跳轉  20140329 KC
		if (responseMap.get("errorMsg")!=null){
			String errorhtml="";
			errorhtml="<script>alert('"+responseMap.get("errorMsg").toString()+"');history.go(-1); </script>";
			out.write(errorhtml);
		}
		
		List<Object[]> dishList=(List<Object[]>)responseMap.get("dishList");
		//List<Ingredient> ingredientList = (List<Ingredient>)responseMap.get("ingredientList");
		
		String search_DishName =(String)session.getAttribute("searchDishName");
		if(search_DishName != null)
		{ search_DishName = session.getAttribute("searchDishName").toString(); }
		else{search_DishName="";}
		String search_IngredientName =(String)session.getAttribute("searchIngredientName");
		if(search_IngredientName != null)
		{ search_IngredientName = session.getAttribute("searchIngredientName").toString(); }
		else{search_IngredientName="";}
		if(dishList != null)
		{
			int allCount = (Integer)responseMap.get("allCount");
%>
<script>
var oform = document.forms["searchForm"];
</script>
<div style="display:none;position: absolute; top:0; left:0; height:100%; width:100%;opacity: 0.5; z-index:9999; background-color:#666;text-align:center; color:#fff; line-height:500px;" id="uploading" >檔案寫入中... 請勿進行操作。</div>
<!-- 四點設計套版 -->
<div class="contents-title">菜色資料維護</div>
	<div class="contents-wrap">
       <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="addNewDish()">新增</button>
       <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="showUpload()">EXCEL上傳</button>
       <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="export_excel()">EXCEL下載</button>
       <button class="btn btn-primary" style="margin: 0px 10px 10px 0px;" onclick="window.location.href='../../images/files/dishExcelExample.xlsx'">下載範例檔</button>
       <!-- Excel 上傳 -->
		<div id="excel_upload" class="TAB_TY_B" style="display: none;">
			<form method="post" action="/cateringservice/file/upload" enctype="multipart/form-data">
				<input type="hidden" id="func" name="func" value="supplier" /> <input type="hidden" id="overWrite" name="func" value="0" />
				<table class="component">
					<tr>
						<td class="componetContent2 componentContentRightLine">選擇檔案</td>
						<td class="componetContent2"><input id="file" type="file" name="excelFile"> (上傳檔案格式為Excel檔)</td>
						<td class="componetContent2"><input class="btn btn-primary" onclick="upload()" value='上傳檔案'></td>
					</tr>
					
				</table>
			</form>
		</div>
          <div class="section-wrap has-border">
            <div class="form-horizontal">
              <div class="form-group">
              	<form name="searchForm" method="post" action="../manageDish/?action=searchForm">
	                <div class="col-xs-12">
	                  <input name="dishName" id="dishName" value="<%=search_DishName%>" type="text" class="form-control flo_l" style="width:30%; margin-right:10px" placeholder="請輸入菜色名稱查詢" />
	                  <input name="ingrediantName" id="ingrediantName" value="<%=search_IngredientName%>" type="text" class="form-control flo_l" style="width:30%; margin-right:10px" placeholder="請輸入食材名稱查詢" />
	                  <button type="submit" class="btn btn-primary" style="margin-top: 0px" onclick="return true;">查詢</button>	
	                </div>  
                </form>
              </div><!-- End of .form-group -->
              	<div class="TITLE_TXT flo_r" style="color:#6b7788; padding-top: 6px;">查詢結果*(20筆/頁) - 菜色筆數:<%=allCount%></div>   
				<div id="menu_list" class="TAB_TY_D">
					<table width="100%" class="table table-bordered table-striped">
							<%
							if(dishList.size() != 0)
							{
								int dishListLengh = dishList.size();
							%>
								<thead><tr>		
									<td align="center">菜色名稱</td>					
									<td align="center">食材名稱</td>
									<td align="center">是否上傳菜色圖片</td>
									<td align="center">選項</td>				
								</tr></thead>
							<%
								for(int i=0;i<dishListLengh;i++)
								{
									Object[] obj=dishList.get(i);
									obj[0]=String.valueOf(obj[0]);
									obj[1]=String.valueOf(obj[1]);
									obj[2]=String.valueOf(obj[2]);
									obj[3]=String.valueOf(obj[3]);
									%>
									<tr>
										
										<td width="30%" align="center">
											<div id="DishName<%=obj[0]%>" >
												<%=obj[1]%>
											</div>
										</td>
										<td width="30%" align="center" >
											<div id="IngrediantName<%=obj[0]%>">
												<%=obj[3]%>
											</div>
										</td>
										<td width="10%" align="center">
											<%
											if(obj[2].equals(obj[0]))
											{
											%>
												<i class="fa fa-check-square-o"></i>
											<%
											}else{
											%>
												<i class="fa fa-square-o"></i>
											<%
											}
											%>
											
										</td>
										<td width="20%" align="center">
										<button class="btn btn-primary" style="min-width:48%; margin-top:0px" onclick="javascript:onEdit('<%=obj[0]%>')"><i class="fa fa-pencil"></i></button>
										<button class="btn btn-primary" style="min-width:48%; margin-top:0px" onclick="javascript:onDelete('<%=obj[0]%>')"><i class="fa fa-trash-o"></i></button>
											
										</td>
										
										
										
									</tr>
									<% 
								}
							}
							else
							{
							%>
								<thead><tr><td>目前尚無菜色</td><tr></thead>
							<% 
							}
							%>
						</table></div>
					
					
					<nav class="page-nav">
						
							<%
							String tmpNowPage = (String)responseMap.get("nowPage");
								int pageCounts = (Integer)responseMap.get("pageCounts");					
								int nowPage = Integer.parseInt(tmpNowPage); 
								int runMin = (Integer)responseMap.get("runMin");
								int runMax = (Integer)responseMap.get("runMax");
								/*
								String url_dish="";
								String url_ingre="";
								String url_param="";
								if (!search_DishName.isEmpty()){
									url_dish="dishName="+search_DishName;
								}
								if (!search_IngredientName.isEmpty()){
									url_ingre="ingrediantName"+search_IngredientName;
								}
								
								if (!url_dish.isEmpty() && !url_ingre.isEmpty()){
									url_param="&"+url_dish+"&"+url_ingre;
								}else{
									url_param="&"+url_dish+url_ingre;			
								}
								if (url_param.equals("&")){
									url_param="";
								}
								*/
								%>
								<ul class="pager">
								<%
								if(pageCounts >= 1)
								{
								%>
									<li class="previous"><a href="../manageDish/?searchPage=1"><i class="fa fa-chevron-left"></i> 第一頁</a></li>
								<% 	
								}
													
								if(nowPage != 1 && dishList.size() != 0)
								{
								%>
									<li class="previous"><a href="../manageDish/?searchPage=<%=(nowPage-1)%>"><i class="fa fa-chevron-left"></i> 上一頁</a></li>
					            <% 
								}
								if(pageCounts >= 1)
								{
								%>
									<li class="next"><a href="../manageDish/?searchPage=<%=pageCounts%>">最末頁 <i class="fa fa-chevron-right"></i></a></li>
								<% 
								}
								
								if(nowPage != pageCounts && dishList.size() != 0)
								{
								%>
									<li class="next"><a href="../manageDish/?searchPage=<%=(nowPage+1)%>">下一頁 <i class="fa fa-chevron-right"></i></a></li>
								<% 
								}
								%>
								</ul>
								<%
								for(int i=runMin;i<=runMax;i++)
								{	
								%>
								<ul class="pagination">
									<li
									<%
									if(nowPage == i)
									{
									%>
									 class="active"
									<%
									}
									%>
									><a href="../manageDish/?searchPage=<%=i%>"><%=i%></a></li>
								</ul>
							  <%} %>									
					</nav>
            </div><!-- End of .form-horizontal -->
          </div><!-- End of .section-wrap -->
	            
    </div>
       
<!-- 舊團膳套版 -->
<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30" style="display:none">
		<div class="TITLE_TXT flo_l">菜色資料維護</div>
		<div class="TITLE_TXT_BBT FL_R">
			<a href="#" onClick="addNewDish()">新增菜色</a>
			<a href="#" onClick="showUpload()">excel上傳</a>
			<a href="#" onClick="export_excel()">excel下載</a>
			<a href="../../images/files/dishExcelExample.xlsx" >下載範例檔</a>
		</div>
</div>
<div id="MAIN_SECOND_BAR" class="h_35px lh_35 GRA_ABR" style="display:none">
		<form name="searchForm" method="post" action="../manageDish/?action=searchForm">
			<div class="dis_intb SECOND_TXT_INP">請輸入查詢條件︰</div>
			<div class="dis_intb LOGIN_US_PS">
				菜色名稱&nbsp;<input class="max255" type="text" name="dishName" id="dishName" value="<%=search_DishName%>">
			</div><div class="dis_intb LOGIN_US_PS">
				食材名稱&nbsp;<input class="max255" type="text" name="ingrediantName" id="ingrediantName" value="<%=search_IngredientName%>">
			</div>
			<div class="dis_intb mgl_10 BT_IN_BBTER">
				&nbsp;&nbsp;<input type="submit"  onclick="return true;" value="查詢"
					type="button">
			</div>
		</form>
	</div>
		
	<div id="excel_upload" class="TAB_TY_B" style="display:none">
		<form method= "post" action ="/cateringservice/file/upload" enctype="multipart/form-data" >
			<input type="hidden" id="func" name ="func" value="Menu"/>
			<input type="hidden" id="overWrite" name ="func" value="0"/>
    		<table class="component">
				<tr>
					<td class="componetContent2 componentContentRightLine">選擇檔案</td>
					<td class="componetContent2">
						<input id="file" type="file" name="excelFile">    (上傳檔案格式為Excel檔)
					</td>
				</tr>
				<tr>
					<td colspan="2" class="BT_IN_BBTER" bgcolor="#ececec" align="center">
						<input type="button" onclick="upload()" value="上傳檔案">
					</td>
				</tr>
			</table>

		</form>
	</div>	
		
	<br>	  
	
	
		<%
		}
	}
%>

<form name="theForm" action="../manageDish/" method="post">
<input type="hidden" name="dishid" value="" id="dishid" />
<input type="hidden" name="action" value="" id="action" />
</form>


<script type="text/javascript">
var MSG= new MsgsProcessing();
function addNewDish()
 {
	document.getElementById("action").value = "createDish";
	document.theForm.submit();
 }
 
$(document).ready(function(){
	$("#excel_upload").hide();
});


function onEdit(id){
		
		document.getElementById("dishid").value = id;
		document.getElementById("action").value = "editDish";
		document.theForm.submit();

}

function onDelete(id){

	if(confirm("是否真的要刪除這筆資料？")){
	
		document.getElementById("dishid").value = id;
		document.getElementById("action").value = "delDish";
		document.theForm.submit();
	}
}

function export_excel(){
	//var schoolId = $("#export").attr("schoolId");
	//var startDate = $("#export").attr("startDate");
	//var endDate = $("#export").attr("endDate");
	
	//startDate = startDate.replace("/", "-");
	//startDate = startDate.replace("/", "-");
	
	//endDate = endDate.replace("/", "-");
	//endDate = endDate.replace("/", "-");
	
	var link = "/cateringservice/rest/API/XLS/vegetable";
	
	//alert(link);
	
	window.open(link,"_blank");
}

var client = new XMLHttpRequest();
function upload(){
	var file = document.getElementById("file");
    if(!$("#file").val()){
    	MSG.alertMsgs('check', '請選擇檔案', 0);
			    	return false;
			    }/* Create a FormData instance */
    var formData = new FormData();
	   MSG.alertMsgs('loading', '', 0);
    /* Add the file */ 
    formData.append("file", file.files[0]);
    //formData.append("func", $( "#func" ).val());
    //formData.append("overWrite", $("#overWrite").val());
    formData.append("func", "vegetable");
    formData.append("overWrite", "0");
    client.open("post","/cateringservice/file/upload", true);
    client.send(formData);  /* Send to server */ 
}
   
/* Check the response status */  
client.onreadystatechange  = function() {
	if (client.readyState == 4 && client.status == 200) {

	    //alert(client.responseText);
		
		var obj = JSON.parse(client.responseText);

         
        if(obj.retStatus == 1){
    	    if (obj.retMsg==""){
	    	 	   MSG.alertMsgs('checkAndReload', '檔案上傳成功', 0);
    	    }else{
				MSG.alertMsgs('checkAndReload', obj.retMsg, 0);
    	    }
        	 $("#file").val("");
        	 $("#overWrite").attr("value", 0);
        	 //location.reload();
        	 //location.assign("../manageDish/");
        } else if (obj.retStatus == 0) {
 	 	   MSG.alertMsgs('check', '檔案上傳失敗，原因為' + obj.retMsg , 0);
        	 $("#file").val("");
        	 $("#overWrite").attr("value", 0);
        } /*else if (obj.retStatus == -1) {
			var answer = confirm("菜單重複，請問是否要覆蓋？");
        	if (answer) {
        		$("#overWrite").attr("value", 1);
        		upload();
        	} else {
        		$("#file").val("");
        		$("#overWrite").attr("value", 0);
        		return;
        	}
    	}*/
    }
	
	$("#excel_upload").hide("slow");
	$("#MAIN_SECOND_BAR").show("slow");
}

function showUpload(){
	$("#excel_upload").show("slow");
	$("#MAIN_SECOND_BAR").hide();
}

</script>

</body>
</html>