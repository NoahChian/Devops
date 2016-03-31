<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.iii.ideas.catering_service.dao.Dish"%>
<%@ page import="org.iii.ideas.catering_service.dao.Ingredient"%>
<%@ page import="org.iii.ideas.catering_service.dao.Supplier"%>
<%@ page import="org.iii.ideas.catering_service.util.CateringServiceUtil"%>
<%@ page language="java" import="java.net.URLEncoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<%
	Dish dish = null;
	
	List<Supplier> supplierList = null;
	List<Ingredient> ingredientList = null;
	List<Dish> dishList = null;
	
	String supplierJs = "";
	
	if(request.getAttribute("responseMap") != null)
	{
		HashMap<String,Object> responseMap = (HashMap<String,Object>)request.getAttribute("responseMap");
		supplierList = (List<Supplier>)responseMap.get("supplierList");
		dishList = (List<Dish>)responseMap.get("dishList");
		dish = (Dish)responseMap.get("dish");
		
		for(int i = 0; i < dishList.size(); i++){
			if(dish != null){
				if(dishList.get(i).getDishName().equals(dish.getDishName())){
					dishList.remove(i);
					break;
				}
			}
		}
		ingredientList = (List<Ingredient>)responseMap.get("ingredientList");
		supplierJs = "<select name='Supplier' id='Supplier' width='100%'>";
		supplierJs +="<option value='' selected >請選擇</option>";
		for(int i=0; i<supplierList.size(); i++)
		{	
			supplierJs += "<option value='" + supplierList.get(i).getId().getSupplierId() + "'>" + supplierList.get(i).getSupplierName() + "</option>";
		}
		
		supplierJs += "</select>";
	}
%>

<script>
$(document).ready(function(){
	<% if(!CateringServiceUtil.isNull(dish)){ %>
		checkFile(<%=dish.getDishId()%>);
	<% } %>
	if(document.getElementById("IngredientName")){
		//document.getElementById("DishName").readOnly=true;
		//$("#DishName").prop('readonly', true);
	}
});
	
	function addNewDish()
	 {
		if(checkSpecialCharacters()){
			return;
		}
		if(checkInput() == false)
			return;
	  	document.theForm.action = "../manageDish/?action=addDish";
	  	document.theForm.submit();
	  	alert("儲存成功");
	  	return true;
	 }

	function addIngredient()
	 {
<%-- 		$("#customFields").append("<tr><td><table width=\"100%\" bgcolor=\"#DBFFB8\" ><tr><td  class=\"componentContentLeftLine\">食材名稱</td><td colspan='2'  class=\"componentContentLeftLine\"><input class='max255' type='text' name='IngredientName' id='IngredientName' size='30'></td><td  class=\" BT_IN_BBTER \"><a href=\"#\" onclick=\"$(this).parent().parent().parent().parent().parent().parent().remove();\">取消</a></td></tr><tr><td class=\"componentContentLeftLine\">供應商名稱</td><td class=\"componentContentLeftLine\"><%=supplierJs%></td><td class=\"componentContentLeftLine\">品牌</td><td  class=\"componentContentLeftLine\"><input class='max255' type='text' name='Brand' id='Brand' size='30'></td></tr></table></td></tr>"); --%>
		$("#customFields").append("<tr><td><table class=\"table table-bordered table-striped\" width=\"100%\"><tr><td>食材名稱 *</td><td><input class='max255' type='text' name='IngredientName' id='IngredientName' size='30'></td><td>產品名稱</td><td align=\"left\" valign=\"middle\"><input class=\"max255\" id=\"ProductName\" name=\"ProductName\" type=\"text\" size=\"30\"></td></tr><tr><td class=\"componentContentLeftLine\">供應商名稱</td><td class=\"componentContentLeftLine\"><%=supplierJs%></td><td class=\"componentContentLeftLine\">製造商名稱</td><td  class=\"componentContentLeftLine\"><input class='max255' type='text' name='Manufacturer' id='Manufacturer' size='30'></td></tr><tr><td class=\"BT_IN_BBTER\" colspan=\"4\"><button class=\"btn btn-primary\" style=\"margin-top: 0; float:right\" onclick=\"$(this).parent().parent().parent().parent().parent().remove();\">取消本項食材</button></td></tr></table></td></tr>");
		/*
		*20140630 Ric
		* Setting maxlength and readonly
		*/
		setInputLength();
	 }
	
	function checkDishInput(Dname){
		if(!isValidStr(Dname)){
			alert('菜色名稱不可包含特殊字元 \" > < \' % ; &');
			return;
		}
		
		var currentdish = new Array();
		var j =0;
		<%
    	if(dishList != null)
		{ for(int i=0; i<dishList.size(); i++)
			{	%>currentdish[<%=i%>]="<%=dishList.get(i).getDishName()%>";j=<%=i%>;<%	}}
	 %>
		for (var i = 0; i < j; i++)  {
			if(currentdish[i]==Dname){
				alert("菜色: ["+ currentdish[i]+ "] 已在菜色資料庫中。");	
				document.getElementById('DishName').value='';
				document.getElementById('DishName').focus();
				return;
			}
		}
	}
	
	function checkInput()
	{
		for (var i = 0; i < document.theForm.elements.length; i++ ) {
			if(document.theForm.elements[i].id == 'IngredientName' && document.theForm.elements[i].value == '')
			{
				alert("請輸入食材名稱!");
				return false;
			}	
			// 修正#12185 : 菜色內之食材非為固定廠商供應，應由食材維護時再作選擇
			/* if(document.theForm.elements[i].id == 'Supplier' && document.theForm.elements[i].value == '')
			{
				alert("請選擇供應商!");
				return false;
			}	*/
			/* if(document.theForm.elements[i].id == 'Brand' && document.theForm.elements[i].value == '')
			{
				alert("請輸入品牌!");
				return false;
			}	*/
       }
	
		if($("#DishName").val() == "")
		{
			alert("請輸入菜色名稱!");
			return false;
		}
		if(!document.getElementById("IngredientName")){
			alert("請至少輸入一項食材!");
			return false;
		}	
			
		return true;
	}
	var client = new XMLHttpRequest();
	   function uploadImg(){
	      var file = document.getElementById("file");
	      /* Create a FormData instance */
	      var formData = new FormData();
	      /* Add the file */ 
	      formData.append("file", file.files[0]);
	      formData.append("func", $( "#func" ).val());
	      formData.append("overWrite", "0");
	      client.open("post","/cateringservice/file/upload", true);
	      client.send(formData);  /* Send to server */ 
	   }
	   /* Check the response status */  
	   client.onreadystatechange  = function() 
	   {
	      if (client.readyState == 4 && client.status == 200)  {
		         
				var obj = JSON.parse(client.responseText);
		         
		         if(obj.retStatus == "1"){
		        	  if(!document.getElementById("IngredientName")){
		    			  alert("檔案上傳成功 \n此菜色並無食材，請您至少新增一項食材。");
		    		  }else{
		        	 alert("檔案上傳成功");}
	        	 var kitchenpic = "<img src=\"/cateringservice/file/SHOW/dishid|<%=session.getAttribute("account")%>|<% if(dish != null){%><%=dish.getDishId()%><% } %>\"  width=\"150\" height=\"130\" />";
	        	 $("#dishpic_o").hide();
				 $("#dishpic").html("");
				 $("#dishpic").append(kitchenpic);
// 				 window.location.reload();
	         } else {
	        	 alert("檔案上傳失敗，原因為" + obj.retMsg);
	         }
	      }
	   }
	   

	 //20140715 Ric 檢查圖片是否存在
	 function checkFile(dishId){
	 	var request_data =	{
	 			 "method":"checkFileExist",
	 				"args":{	
	 					"pageName":"dish",
	 					"dishId": dishId
	 				}
	 			};
	 	var response_obj = call_rest_api(request_data);
	 	if(response_obj.result == 1)
	 	{
	 		var result_content = response_obj.result_content;
	 		if(result_content.resStatus == 1){	//success
	 		$("#deleteFile").show();
	 		} else {
	 			//MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
	 		}
	 	} else{
	 		//MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);
	 	}
	 }
	 //20140715 Ric 刪除圖片
	 function deleteFile(dishId){
	 	var request_data =	{
	 			 "method":"deleteFile",
	 				"args":{	
	 					"pageName":"dish",
	 					"dishId": dishId,
	 					"mode" : "rename"
	 				}
	 			};
	 	var response_obj = call_rest_api(request_data);
	 	if(response_obj.result == 1)
	 	{
	 		var result_content = response_obj.result_content;
	 		if(result_content.resStatus == 1){	//success
	 			MSG.alertMsgs("checkAndReload", "檔案刪除成功" + result_content.msg, 0);
	 		} else {
	 			MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
	 		}
	 	} else{
	 		MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);
	 	}
	 }
	 
	 //add by Joshua 2014/10/23  ban input field press enter
	 function disableEnter(evt){
		 var evt = (evt) ? evt : ((event) ? event : null); 
		 var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
		 if ((evt.keyCode == 13) && (node.type=="text"))  {return false;}
	 }

	//add by Joshua 2014/11/10 特殊字元判斷
	 function isValidStr(str){
		return !/[%&\[\]\\';|\\"<>]/g.test(str);
	 }

	/**
	2014/10/27  add by Joshua
	輸入欄位判斷特殊字元
	*/
// 	document.onchange = checkSpecialCharacters;
	function checkSpecialCharacters(){
		var dishname = $("#DishName").val();
		if(!isValidStr(dishname)){
			alert('菜色欄位名稱不可包含特殊字元 \" > < \' % ; &');
			return true;
		}
		var inputFields = $("table[id^='customFields']").find("input[type=text]");
		var inputFieldsCount = $("table[id^='customFields']").find("input[type=text]").length;
		for(i = 0; i < inputFieldsCount ; i++){
			if(!isValidStr(inputFields[i].value)){
				alert('欄位名稱不可包含特殊字元 \" > < \' % ; &');
				return true;
			}
		}
	}	 
</script>
</head>
<body>
	<!-- 四點設計套版 -->
	<div class="contents-title">菜色資料維護</div>
		<div class="contents-wrap">
			<button class="btn btn-primary" onclick="window.location.href='../manageDish/'">回前頁</button>
			<input type="hidden" id="supplierId">
			<h5 class="section-head with-border">新增與修改菜色資料</h5>
		         <div class="section-wrap has-border kitchen-info">
		           <div class="container-fluid">
		             <div class="row">
		               <form method="post" action="/cateringservice/file/upload" enctype="multipart/form-data">
							<table width="100%" class="table table-bordered table-striped">
								<tr>
									<td width="100px">菜色圖檔</td>
									<td>
										<%
											if (dish != null) {
										%><img class="flo_l" name="dishpic_o" id="dishpic_o" src="/cateringservice/file/SHOW/dishid|<%=session.getAttribute("account")%>|<%=dish.getDishId()%>" width="150" height="130" /> 
										   <span name="dishpic" id="dishpic"><br> 
											   <input type="hidden" id="func" name="func" value="dishid|<%=dish.getDishId()%>" />
											   <input type="hidden" id="overWrite" name="overWrite" value="0" />
											   <input type="file" id="file" name="file" /> 
											   <span class="btn btn-primary" id='deleteFile' style='display:none !important;'>
											      <a href="#" onclick="deleteFile(<%=dish.getDishId()%>)" style="color:#fff">刪除</a>
											   </span>
											   <span class="btn btn-primary">
											      <a href="#" onclick="uploadImg();" style="color:#fff">上傳圖檔</a>
										   	   </span>
										   <br><span style="color: #666;">顯示比例：150*130(px) 格式：jpg, png, gif。大小：小於3MB。</span> <%
										 	} else {
										 %> 請先新增食材後再以 [修改] 功能新增圖片<%
										 	}
										 %>
									</td>
								</tr>
							</table>
						</form>
						<form name="theForm" action="" method="post" enctype="multipart/form-data">
							<table width="100%" class="table table-bordered table-striped">
								<tr style="background-color: #EBEBEB;">
									<td width="100px">菜色名稱 *</td>
									<td><input onkeypress="return disableEnter(event)" class="max255" style="color:#6b7788;" type="text" name="DishName" id="DishName" size="30" onchange="checkDishInput(this.value)" value="<%if (dish != null) {%><%=dish.getDishName()%><%}%>">
										<div align="right"></div></td>
								</tr>
							</table>
							<input type="hidden" name="dishid" value="<%if (dish != null) {%><%=dish.getDishId()%><%}%>" id="dishid" /> <input type="hidden" name="kitchenId" value="<%if (dish != null) {%><%=dish.getKitchenId()%><%}%>" id="kitchenId" />
							<div class="TAB_TY_B">
								<br><input class="btn btn-primary" value="新增食材" style="margin-bottom: 10px;" onClick="addIngredient()" readonly>
								<table id="customFields" name="customFields" class="" width="100%">
									<%
										if (ingredientList != null) {
											int listLength = 0;
											listLength = ingredientList.size();
											for (int i = 0; i < listLength; i++) {
									%>
									<tr>
										<td><table class="table table-bordered table-striped" width="100%">
												<tr>
													<td>食材名稱 *</td>
													<td><input class="max255" type="text" name="IngredientName" id="IngredientName" size="30" value="<%=ingredientList.get(i).getIngredientName()%>"></td>
													<td>產品名稱</td>
													<td align="left" valign="middle" ><input  class="max255" id="ProductName" name="ProductName" type="text" size="30" value="<%=ingredientList.get(i).getProductName()%>"></td>
					<!-- 								<td class="BT_IN_BBTER"><a href="#" onclick="$(this).parent().parent().parent().parent().parent().parent().remove();">刪除</a></td> -->
					
												</tr>
					
												<tr>
													<td class="">供應商名稱</td>
													<td class="BT_IN_BBTER"><select name="Supplier" id="Supplier" width="100%">
					                                        					<option value="" selected></option>
															<%
																if (supplierList != null) {
																			for (int j = 0; j < supplierList.size(); j++) {
																				if (supplierList.get(j).getId().getSupplierId().equals(ingredientList.get(i).getSupplierId())) {
															%>
															<option value="<%=supplierList.get(j).getId().getSupplierId()%>" selected><%=supplierList.get(j).getSupplierName()%></option>
															<%
																} else {
															%>
															<option value="<%=supplierList.get(j).getId().getSupplierId()%>"><%=supplierList.get(j).getSupplierName()%></option>
															<%
																}
					
																			}
																		}
															%>
													</select></td>
													<td class="componentContentLeftLine">製造商名稱</td>
													<td class="componentContentLeftLine"><input class="max255" type="text" name="Manufacturer" id="Manufacturer" size="30" value="<%=ingredientList.get(i).getManufacturer()%>"></td>
												</tr>
												<tr>
													<td class="BT_IN_BBTER" colspan="4"><button class="btn btn-primary" style="margin-top: 0; float:right" onclick="$(this).parent().parent().parent().parent().parent().remove();">刪除本項食材</button></td>
												</tr>
											</table></td>
									</tr>
									
									
									<%
										}
										}
									%>
								</table>
					
								<table width="100%" class="table table-bordered table-striped">
									<tr>
										<td align="center"><input class="btn btn-primary" value="確認送出" style="margin-top:0" onClick="addNewDish()" readonly></td>
									</tr>
					
								</table>
							</div>
						</form>
		             </div><!-- End of .row -->
		           </div><!-- End of .container-fluid -->
		         </div><!-- End of .section-wrap -->
		</div>
	<!-- 舊團膳套版 -->		
	<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30" style="display:none">
		<div class="TITLE_TXT flo_l">新增與修改菜色資料</div>
		<span class="TITLE_TXT_BBT"><a href="../manageDish/" style="float: right;">回前頁</a></span>
	</div>
	<div id="menu_list" class="TAB_TY_B" style="display:none">
		<form method="post" action="/cateringservice/file/upload" enctype="multipart/form-data">
			<table width="100%" class="">
				<tr>
					<td bgcolor="#FFFFFF" width="100px">菜色圖檔</td>
					<td bgcolor="#FFFFFF">
						<%
							if (dish != null) {
						%><img name="dishpic_o" id="dishpic_o" src="/cateringservice/file/SHOW/dishid|<%=session.getAttribute("account")%>|<%=dish.getDishId()%>" width="150" height="130" /> <span class='BT_IN_BBTER' id='deleteFile' style='display: none;'><a
							href="#" onclick="deleteFile(<%=dish.getDishId()%>)">刪除</a></span> <span name="dishpic" id="dishpic"><br> <input type="hidden" id="func" name="func" value="dishid|<%=dish.getDishId()%>" /> <input type="hidden" id="overWrite" name="overWrite"
							value="0" /> <input type="file" id="file" name="file" /> <span class="BT_IN_BBTER"><input type="button" value="上傳" onclick="uploadImg();" /></span><span style="color: #666;">顯示比例：150*130(px) 格式：jpg, png, gif。大小：小於3MB。</span> <%
 	} else {
 %> 請先新增食材後再以 [修改] 功能新增圖片<%
 	}
 %>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<%-- <form name="theForm" action="" method="post" enctype="multipart/form-data" style="display:none">
		<input type="hidden" name="dishid" value="<%if (dish != null) {%><%=dish.getDishId()%><%}%>" id="dishid" /> <input type="hidden" name="kitchenId" value="<%if (dish != null) {%><%=dish.getKitchenId()%><%}%>" id="kitchenId" />
		<div id="menu_list" class="TAB_TY_B">
			<table width="100%" class="">
				<tr>
					<td width="100px" bgcolor="#edf2e5">菜色名稱 *</td>
					<td bgcolor="#edf2e5"><input onkeypress="return disableEnter(event)" class="max255" type="text" name="DishName" id="DishName" size="30" onchange="checkDishInput(this.value)" value="<%if (dish != null) {%><%=dish.getDishName()%><%}%>">
						<div align="right"></div></td>
				</tr>

			</table>
		</div>
		<br>
		<div class="TAB_TY_B">
			<table id="customFields" name="customFields" width="100%">

				<tr>
					<td colspan="4" align="center" class="BT_IN_BBTER"><input type="button" value="新增食材" onClick="addIngredient()"></td>
				</tr>
				<%
					if (ingredientList != null) {
						int listLength = 0;
						listLength = ingredientList.size();
						for (int i = 0; i < listLength; i++) {
				%>
				<tr>
					<td><table width="100%" <%if (i % 2 == 1) {
						out.print(" bgcolor='#edf2e5'");
					} else if (i % 2 == 0) {
						out.print(" bgcolor='#FFFFFF' ");
					}%>>
							<tr>
								<td>食材名稱*</td>
								<td><input class="max255" type="text" name="IngredientName" id="IngredientName" size="30" value="<%=ingredientList.get(i).getIngredientName()%>"></td>
								<td>產品名稱</td>
								<td align="left" valign="middle" ><input  class="max255" id="ProductName" name="ProductName" type="text" size="30" value="<%=ingredientList.get(i).getProductName()%>"></td>
<!-- 								<td class="BT_IN_BBTER"><a href="#" onclick="$(this).parent().parent().parent().parent().parent().parent().remove();">刪除</a></td> -->

							</tr>

							<tr>
								<td class="">供應商名稱</td>
								<td class="BT_IN_BBTER"><select name="Supplier" id="Supplier" width="100%">

										<%
											if (supplierList != null) {
														for (int j = 0; j < supplierList.size(); j++) {
															if (supplierList.get(j).getId().getSupplierId().equals(ingredientList.get(i).getSupplierId())) {
										%>
										<option value="<%=supplierList.get(j).getId().getSupplierId()%>" selected><%=supplierList.get(j).getSupplierName()%></option>
										<%
											} else {
										%>
										<option value="<%=supplierList.get(j).getId().getSupplierId()%>"><%=supplierList.get(j).getSupplierName()%></option>
										<%
											}

														}
													}
										%>
								</select></td>
								<td class="componentContentLeftLine">製造商名稱</td>
								<td class="componentContentLeftLine"><input class="max255" type="text" name="Manufacturer" id="Manufacturer" size="30" value="<%=ingredientList.get(i).getManufacturer()%>"></td>
							</tr>
							<tr>
								<td class="BT_IN_BBTER" colspan="4"><input type="button" value="刪除本項食材" onclick="$(this).parent().parent().parent().parent().parent().remove();"></td>
							</tr>
						</table></td>
				</tr>
				
				
				<%
					}
					}
				%>
			</table>

			<table width="100%" class="BT_IN_BBTER">
				<tr>
					<td align="center"><input type="button" value="確認" onClick="addNewDish()"></td>
				</tr>

			</table>
		</div>
	</form> --%>

</body>
</html>