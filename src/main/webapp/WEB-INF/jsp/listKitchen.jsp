<%@page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="util.jsp" %> <!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js/jquery.twbsPagination.min.js"></script>

<%

		String uType = "";
		String uName = "";
		String roletype = "";
		String countyType = "";
		Integer countyNum = 0;
		if(session.getAttribute("account")!=null){
		
		uType = (String) session.getAttribute("uType");
		uName = (String) session.getAttribute("uName");
		roletype = (String) session.getAttribute("roletype");
		countyType = AuthenUtil.getCountyTypeByUsername(uName);
		countyNum = AuthenUtil.getCountyNumByUsername(uName);
		}
%>
<script>
 
var row = 300;


function add_kitchen_detail() {
	$("#kitchen_list").hide("slow");
	$("#query_rule").hide();
	$("#query_bar").hide();
	$("#newAdd").hide();
	$("#pagination").hide();
			var new_div = "<div id=\"queryList\" class=\"TAB_TY_B\">";
					new_div += " <form name=\"theForm\"><table width=\"100%\" class=\"component\">";
					 new_div += " <tbody>";
					new_div += " <tr>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">資料顯示型態</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" colspan=\"3\" >"
							+ " <select name=\"kitchenType\" id=\"kitchenType\" >"
							+ "<option value=\"0\">請選擇</option>";
							new_div +=  "</select></td>";
					new_div += " </tr>";
						new_div += " <tr>";
							new_div += " <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"80px\">名稱 *</td>";
							new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"230px\">";
							new_div += " <input type=\"text\" name=\"Name\" id=\"Name\" size=\"30\" value=\"\" class=\"max255\">		</td>";
							new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"120px\">單位編號(統編) *</td>";
							new_div += " <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >";
							new_div += " <input type=\"text\" name=\"companyId\" id=\"companyId\" size=\"30\" value=\"\"class=\"max40\"></td>";
							new_div += " </tr>";
						new_div += " <tr>";
							new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" width=\"80px\">Email *</td>";
							new_div += " <td  bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" width=\"230px\">";
							new_div += " <input type=\"text\" name=\"email\" id=\"email\" size=\"30\" value=\"\" class=\"max255\">		</td>";
							new_div += " <td  bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" width=\"120px\">負責人 </td>";
							new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" >";
							new_div += "  <input type=\"text\" name=\"ownner\" id=\"ownner\" size=\"30\" value=\"\" class=\"max255\">		</td>";
						new_div += " </tr>";
						new_div += " <tr>";
							new_div += " <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"80px\">地址 *</td>";
							new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"230px\">";
							new_div += " <input type=\"text\" name=\"Address\" id=\"Address\" size=\"30\" value=\"\" class=\"max255\">		</td>";
							new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"120px\">電話 *</td>";
							new_div += " <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >";
							new_div += "  <input type=\"text\" name=\"Tel\" id=\"Tel\" size=\"30\" value=\"\" class=\"max255\">		</td>";
						new_div += " </tr>";
					//if(result_content.kitchenType=="005"){ 
					new_div += " <tr>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">傳真 *</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"
							+ " <input type=\"text\" name=\"Fax\" id=\"Fax\" size=\"30\" value=\"\" class=\"max255\">		</td>";
					
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">餐食負責人*</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"
							+ " <input type=\"text\" name=\"Nutritionist\" id=\"Nutritionist\" size=\"30\" value=\"\" class=\"max255\"></td>";
					new_div += " </tr>";
					new_div += " <tr>";
					new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >督導主管*</td>";
					new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >"
						+ " <input type=\"text\" name=\"Manager\" id=\"Manager\" size=\"30\" value=\"\" class=\"max255\">		</td>";
				
					new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >督導主管E-Mail*</td>";
					new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >"
						+ " <input type=\"text\" name=\"ManagerEmail\" id=\"ManagerEmail\" size=\"30\" value=\"\" class=\"max255\">		</td>";
				new_div += " </tr>";
					 new_div += " <tr>";
						new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >衛管人員</td>";
						new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >"
							+ " <input type=\"text\" name=\"Chef\" id=\"Chef\" size=\"30\" value=\"\" class=\"max255\">		</td>";
					
						new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >食品技師</td>";
						new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >"
							+ " <input type=\"text\" name=\"Qualifier\" id=\"Qualifier\" size=\"30\" value=\"\" class=\"max255\">		</td>";
					new_div += " </tr>";
					/* 20140630 Ric : No HACCP number
					new_div += " <tr>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">HACCP認證號</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" colspan=\"3\" >"
							+ " <input type=\"text\" name=\"HACCP\" id=\"HACCP\" size=\"30\" value=\"\">		</td>";
					new_div += " </tr>";
					*/
					//}else{ 
					/* new_div += " <tr>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">傳真 *</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"
							+" <input type=\"text\" name=\"Fax\" id=\"Fax\" size=\"30\" value=\""+result_content.fax+"\">		</td>";
					
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">營養師/午餐秘書*</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"
							+ " <input type=\"text\" name=\"Nutritionist\" id=\"Nutritionist\" size=\"30\" value=\""+result_content.nutritionist+"\">		</td>";
					new_div += " </tr>";
					}*/

					new_div += " <tr>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">供應之學校 *</td>";
						new_div += " <td colspan=\"3\"  bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">";
						/*	new_div += " <table width=\"100%\" >";
								new_div += " <tr>";
									new_div += " <td>縣市別:					</td>";
									new_div += " <td><span  name=\"dropdown_City\" id=\"dropdown_City\" ></td>";
							  new_div += " </tr>";
								new_div += " <tr>";
									new_div += " <td>市/區別:					</td>";
									new_div += " <td><span  name=\"dropdown_Area\" id=\"dropdown_Area\" ></td>";
								new_div += " </tr>";
								new_div += " <tr>";
									new_div += " <td>學校名稱:					</td>";
									new_div += " <td><span  name=\"dropdown_School\" id=\"dropdown_School\" ></td>";
								new_div += " </tr>";
							new_div += " </table>		</td>";*/
							new_div += " 請先新增供餐廚房，再以「修改」功能設定供應學校		</td>";
					//	new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine BT_IN_BBTER\" >"
					//	  + " <input type=\"button\" name=\"btnAddSchool\" id=\"btnAddSchool\" value=\"新增學校\" onclick=\"addSchool()\">";
					//	new_div += " </td>";
					 new_div += "  </tr>";
					
					/* new_div += " <tr><td colspan=\"4\"  class=\"TAB_TY_B\">";
					new_div += " <table width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  id=\"customFields\">";
					new_div += " <tr>";
						new_div += " <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\">"
						+ " 	序號</td>";
						new_div += " <td colspan=\"2\" bgcolor=\"#edf2e5\" class=\"componentContentRightLine\">";
						+ " 	學校名稱</td>";
						new_div += "     <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\">";
						+ " 	選項</td>";
					new_div += " </tr>";
					
					new_div += " </td></tr></table>	";
					new_div += " <tr>";*/
						new_div += " <td colspan=\"4\">";
							new_div += " <div align=\"center\">"
							+ "  <input class='btn btn-primary' type=\"button\" value=\"確認\" id=\"update_kitchen_button\" onClick=\"addKitchen()\">"
						   + " </div></td>";
					new_div += " </tr>";
				new_div += " </table></form>";
				//}
			$("#queryList").html("");
			$("#queryList").append(new_div);
			$("#queryList").show();
			 /*
			*20140630 Ric
			* Setting maxlength and readonly
			*/
			setInputLength();
			$("#goBack").show();
			//city();	//drop down counties
			//area();
			//school();
			QueryUserTypeList();
		
}
function query_kitchen_detail(kitchenId) {
	$("#kitchen_list").hide("slow");
	$("#query_rule").hide();
	$("#query_bar").hide();
	$("#newAdd").hide();
	$("#pagination").hide();
	var request_data =	{
			 "method":"queryKitchenDetail",
 				"args":{	
 					"kitchenId":kitchenId
	 			}
			};
	
	var response_obj = call_rest_api(request_data);
	
	if(response_obj.result == 1)
	{
		var result_content = response_obj.result_content;
		if(result_content.resStatus == "1"){	//success
			var new_div = "<div id=\"queryList\" class=\"TAB_TY_B\">";
					//for(var i = 0 ; i<result_content.length; i++)					{
					new_div += " <form name=\"theForm\"><table width=\"100%\" class=\"component\">";
					 new_div += " <tbody>";
// 					new_div += " <tr>";
// 						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">資料顯示型態</td>";
// 						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" colspan=\"3\" >"
// 							+ " <select name=\"kitchenType\" id=\"kitchenType\" >"
// 							+ "<option value=\""+result_content.kitchenType+"\">";
// 							if(result_content.kitchenType=="005"){new_div +="團膳業者"
// 							+"</option><option value=\"006\">自設廚房(006)</option></select>";}
// 							else{new_div +="自立廚房"
// 							+"</option><option value=\"005\">團膳業者(005)</option></select>";}
// 							new_div +=  "</td>";
// 					new_div += " </tr>";
						new_div += " <tr>";
							new_div += " <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"80px\">名稱 *</td>";
							new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"230px\">";
							new_div += " "+ result_content.kitchenName + "</td>";
							//new_div += " <input type=\"text\" name=\"Name\" id=\"Name\" size=\"30\" value=\""+ result_content.kitchenName + "\">		</td>";
							new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"120px\">單位編號(統編) *</td>";
							new_div += " <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >";
							new_div += " "+ result_content.companyId +"</td>";
							//new_div += "  <input type=\"text\" name=\"companyId\" id=\"companyId\" size=\"30\" value=\""+ result_content.companyId +"\">		</td>";
							new_div += " </tr>";
						new_div += " <tr>";
							new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" width=\"80px\">Email *</td>";
							new_div += " <td  bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" width=\"230px\">";
							new_div += " <input type=\"text\" name=\"email\" id=\"email\" size=\"30\" value=\""+ result_content.email + "\" class=\"max255\">		</td>";
							new_div += " <td  bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" width=\"120px\">負責人 </td>";
							new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" >";
							new_div += "  <input type=\"text\" name=\"ownner\" id=\"ownner\" size=\"30\" value=\""+ result_content.ownner +"\" class=\"max255\">		</td>";
						new_div += " </tr>";
						new_div += " <tr>";
							new_div += " <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"80px\">地址 *</td>";
							new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"230px\">";
							new_div += " <input type=\"text\" name=\"Address\" id=\"Address\" size=\"30\" value=\""+ result_content.address + "\" class=\"max255\">		</td>";
							new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" width=\"120px\">電話 *</td>";
							new_div += " <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >";
							new_div += "  <input type=\"text\" name=\"Tel\" id=\"Tel\" size=\"30\" value=\""+ result_content.tel +"\" class=\"max255\">		</td>";
						new_div += " </tr>";
					//if(result_content.kitchenType=="005"){ 
					new_div += " <tr>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">傳真 *</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"
							+ " <input type=\"text\" name=\"Fax\" id=\"Fax\" size=\"30\" value=\""+result_content.fax+"\" class=\"max255\">		</td>";
					
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">餐食負責人*</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"
							+ " <input type=\"text\" name=\"Nutritionist\" id=\"Nutritionist\" size=\"30\" value=\""+result_content.nutritionist+"\" class=\"max255\"></td>";
					new_div += " </tr>";
					new_div += " <tr>";
					new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >督導主管*</td>";
					new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >"
						+ " <input type=\"text\" name=\"Manager\" id=\"Manager\" size=\"30\" value=\""+result_content.manager+"\" class=\"max255\">		</td>";
				
					new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >督導主管E-Mail*</td>";
					new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >"
						+ " <input type=\"text\" name=\"ManagerEmail\" id=\"ManagerEmail\" size=\"30\" value=\""+result_content.manageremail+"\" class=\"max255\">		</td>";
				new_div += " </tr>";
					 new_div += " <tr>";
						new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >衛管人員</td>";
						new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >"
							+ " <input type=\"text\" name=\"Chef\" id=\"Chef\" size=\"30\" value=\""+result_content.chef+"\" class=\"max255\">		</td>";
					
						new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >食品技師</td>";
						new_div += " <td  bgcolor=\"#edf2e5\" class=\"componentContentRightLine\" >"
							+ " <input type=\"text\" name=\"Qualifier\" id=\"Qualifier\" size=\"30\" value=\""+result_content.qualifier+"\" class=\"max255\">		</td>";
					new_div += " </tr>";
					/* 20140630 Ric No HACCP Number
					new_div += " <tr>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">HACCP認證號</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" colspan=\"3\" >"
							+ " <input type=\"text\" name=\"HACCP\" id=\"HACCP\" size=\"30\" value=\""+result_content.haccp+"\">		</td>";
					new_div += " </tr>";
					*/
					//}else{ 
					/* new_div += " <tr>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">傳真 *</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"
							+" <input type=\"text\" name=\"Fax\" id=\"Fax\" size=\"30\" value=\""+result_content.fax+"\">		</td>";
					
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">營養師/午餐秘書*</td>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"
							+ " <input type=\"text\" name=\"Nutritionist\" id=\"Nutritionist\" size=\"30\" value=\""+result_content.nutritionist+"\">		</td>";
					new_div += " </tr>";
					}*/

					new_div += " <tr>";
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">供應之學校 *</td>";
						new_div += " <td colspan=\"2\"  bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">";
							new_div += " <table width=\"100%\" >";
								new_div += " <tr>";
									new_div += " <td>縣市別:					</td>";
									new_div += " <td><span  name=\"dropdown_City\" id=\"dropdown_City\" ></td>";
							  new_div += " </tr>";
								new_div += " <tr>";
									new_div += " <td>市/區別:					</td>";
									new_div += " <td><span  name=\"dropdown_Area\" id=\"dropdown_Area\" ></td>";
								new_div += " </tr>";
								new_div += " <tr>";
									new_div += " <td>學校名稱:					</td>";
									new_div += " <td><span  name=\"dropdown_School\" id=\"dropdown_School\" ></td>";
								new_div += " </tr>";
							new_div += " </table>		</td>";
					
						new_div += " <td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"
						  + " <input type=\"button\" class='btn btn-primary' name=\"btnAddSchool\" id=\"btnAddSchool\" value=\"新增學校\" onclick=\"addSchool("+kitchenId+")\">";
						new_div += " </td>";
					 new_div += "  </tr>";
					
					new_div += " <tr><td colspan=\"4\"  class=\"TAB_TY_B\">";
					new_div += " <table width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  id=\"customFields\">";
					new_div += " <tr>";
						new_div += " <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\">"
						+ " 	序號</td>";
						new_div += " <td colspan=\"2\" bgcolor=\"#edf2e5\" class=\"componentContentRightLine\">";
						+ " 	學校名稱</td>";
						new_div += "     <td bgcolor=\"#edf2e5\" class=\"componentContentRightLine\">";
						+ " 	選項</td>";
					new_div += " </tr>";
								
					listLength = result_content.school.length;
					for(var j=0;j<listLength;j++)
					{
					
						new_div += " <tr valign=\"top\" align=\"left\">";
							new_div += " <td  width=\"86px\"";
							if(j%2 == 1){
							new_div += "class='componetContent componentContentLeftLine' >";
							}else if(j%2 == 0){
							new_div +="class='componetContent2 componentContentLeftLine' >";
							}
							new_div +="<input type=\"hidden\" readonly=\"readonly\" id=\"customFieldValue\" name=\"customFieldValue\" value=\""+ result_content.school[j].sid+"\" placeholder=\"Input Name\" style=\"border: none;\"/>"+ result_content.school[j].sid+" </td>";
							new_div += " <td colspan=\"2\"";
							if(j%2 == 1){
							new_div += "class='componetContent componentContentLeftLine' >";
							}else if(j%2 == 0){
							new_div +="class='componetContent2 componentContentLeftLine' >";
							}
							new_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.school[j].schoolName+"\" placeholder=\"Input Value\" style=\"border: none;\" />"+ result_content.school[j].schoolName+"</td>";
							new_div += " <td  width=\"245px\"";
							if(j%2 == 1){
							new_div += "class='componetContent componentContentRightLine' >";
							}else if(j%2 == 0){
							new_div +="class='componetContent2 componentContentRightLine' >";
							}
							new_div +="<a href=\"#\" class='btn btn-primary' onclick=\"deleteSchool($(this),"+kitchenId+","+ result_content.school[j].sid +")\" style='margin-top: 0px;'>刪除</a></td>";
						new_div += " </tr>";
					}
					new_div += " </td></tr></table>	";
					new_div += " <tr>";
						new_div += " <td colspan=\"4\">";
							new_div += " <div align=\"center\">"
							+ "  <input type=\"button\" class='btn btn-primary' value=\"確認\" id=\"update_kitchen_button\" onClick=\"updateKitchen()\" kid=\""+ result_content.kitchenId +"\">"
						   + " </div></td>";
					new_div += " </tr>";
				new_div += " </table></form>";
				//}
			$("#queryList").html("");
			$("#queryList").append(new_div);
			$("#queryList").show();
			 /*
			*20140630 Ric
			* Setting maxlength and readonly
			*/
			setInputLength();
			$("#goBack").show();
			city();	//drop down counties
			area();
			school();
		} else {
			alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			return;
		}
	} else{
		alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		return;
	}
}
	function deleteSchool(element, kid, sid){
		var answer = confirm("請問是否要刪除？");
    	if (answer) {
    		//request
    		var request_data =	{
    							 "method":"deleteSchoolKitchen",
    				 				"args":{	
    				 					"kitchenIdSC":kid,
    				 					"schoolIdSC": sid
    					 			}
    							};
    		var response_obj = call_rest_api(request_data);
    		
    		if(response_obj.result == 1)
    		{
    			var result_content = response_obj.result_content;
    			if(result_content.resStatus == 1){	//success
    				
    				alert("刪除成功");
					$(element).parent().parent().remove();
    			} else {
    				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
    			}
    		} else{
    			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
    		}
    	} else {
    		return;
    	}
		
	}

	//Ric  Search school by contry area
	var drop_down_default = "<select name=\"City\" id=\"City\"><option value=\"0\">請選擇</option></select>";
	
	//縣市
	function city(){
		/* var request_data =	{
				 "method":"customerQueryCounties",
	 				"args":{
	 					"condition":1
	 				}
				};
		 */
		var request_data ={
			"method":"queryCounties",
			"args":{
				"condition": <%=countyNum%>
			}
		};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				var drop_down_counties = "<select name=\"City\" id=\"City\" onChange=\"area()\">";
					drop_down_counties += "<option value=\"0\">請選擇</option>";
					for(var i=0; i<result_content.counties.length; i++) {
						
						drop_down_counties += "<option value=" + result_content.counties[i].cid + ">" + result_content.counties[i].countiesName + "</option>";
					}
				drop_down_counties += "</select>";
				$("#dropdown_City").append(drop_down_counties);
				$("#dropdown_Area").append(drop_down_default);
				$("#dropdown_School").append(drop_down_default);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}

	}
	
	
	//縣市改變修改區域
	function area() {
		//alert("aa");
    	var counties = $("#City").val();
    	//alert(counties);
    	var request_data =	{
				 "method":"customerQueryArea",
	 				"args":{
	 					"cid" : counties
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				var drop_down_area = "<select name=\"Area\" id=\"Area\"  onChange=\"school()\">";
					drop_down_area += "<option value=\"0\">請選擇</option>";
					for(var i=0; i<result_content.area.length; i++) {
						
						drop_down_area += "<option value=" +  result_content.area[i].aid + ">" + result_content.area[i].areaName + "</option>";
					}
				drop_down_area += "</select>";
				
				//清空
				$("#dropdown_Area").html("");
				$("#dropdown_School").html("");
				
				$("#dropdown_Area").append(drop_down_area);
				$("#dropdown_School").append(drop_down_default);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
    }
	
	//區域改變修改學校
	function school() {
    	var area = $("#Area").val();
    	
    	//alert(area);
    	
    	var request_data =	{
				 "method":"customerQuerySchool",
	 				"args":{
	 					"aid": area
	 				}
				};
		var response_obj = call_rest_api(request_data);
		
		//alert(response_obj.toString());
		
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			
			//alert(result_content.resStatus);
			
			if(result_content.resStatus == 1){	//success
				var drop_down_school = "<select name=\"School\" id=\"School\"  >";
					drop_down_school += "<option value=\"0\">請選擇</option>";
					for(var i=0; i<result_content.school.length; i++) {
						
						drop_down_school += "<option value=" +  result_content.school[i].sid + ">" + result_content.school[i].schoolName + "</option>";
					}
				drop_down_school += "</select>";
				
				//清空
				$("#dropdown_School").html("");
				
				$("#dropdown_School").append(drop_down_school);
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
    }
	
	function checkSchool()
	{

		if(($('#School').find(":selected").text() == ""||$('#School').find(":selected").text() == "請選擇"))
		{
			alert("請選擇學校!");
			return false;
		}
		return true;
	}
	function addSchool(kid)
	 {
		if(checkSchool() == false)
			return false;
		else
		
	  		//加上寫入Schoo;
		var sid =  $('#School').find(":selected").val();
		var request_data =	{
				 "method":"addSchoolKitchen",
	 				"args":{	
	 					"kitchenIdSC": kid,	
	 					"schoolIdSC":sid
		 			}
				};		
		var response_obj = call_rest_api(request_data);

		//response
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				alert("新增學校成功");
				$("#customFields").append('<tr valign="top" align="left"  bgcolor=\"#DBFFB8\" ><td colspan="2" class="componentContentLeftLine"><input type="hidden" readonly="readonly" id="customFieldValue" name="customFieldValue" value="' + $('#School').find(":selected").val() + '" placeholder="Input Name" />' + $('#School').find(":selected").val() + '</td><td class=""> <input type="hidden" id="customFieldName" readonly="readonly" name="customFieldName" value="' + $('#School').find(":selected").text() + '" placeholder="Input Value" />' + $('#School').find(":selected").text() + ' </td><td  class="componentContentRightLine BT_IN_BBTER"> <a href="#" class="remCF" onclick="deleteSchool($(this),'+kid+',' + sid +')">刪除</button></td></tr>');
		
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	  	return true;
	 }
	 function updateKitchen()
	{
		if(($("#email").val() == "") 
		 ||($("#Address").val() == "") 		|| ($("#Tel").val() == "") 
		 || ($("#Fax").val() == "") 	|| ($("#Nutritionist").val() == ""))
		{
			alert("請輸入必要欄位!");
			return;
		}		
		var school_array = new Array();
		var myForm = document.forms.theForm;
		var myControls = myForm.elements['customFieldValue'];
		var myControls2 = myForm.elements['customFieldName'];
		if(myControls){for(var i=0 ; i<myControls.length; i++){
		var school = new Object();
		school.sid = myControls[i].value;
		school.schoolName = myControls2[i].value;
		school_array.push(school);
		}}
		
		var kitchenId = $("#update_kitchen_button").attr("kid");
		
		var request_data =	{
				 "method":"updateKitchen",
	 				"args":{	
	 					"kitchenId": kitchenId,
	 					//"kitchenName": $("#Name").val(),
	 					"kitchenType": $("#kitchenType").val(),
	 					"address": $("#Address").val(),
	 					"tel": $("#Tel").val(),
	 					"ownner": $("#ownner").val(),
	 					"fax": $("#Fax").val(),
	 					"nutritionist": $("#Nutritionist").val(),
	 					"chef": $("#Chef").val(),
	 					"qualifier": $("#Qualifier").val(),
	 					"haccp": "",
	 					//"insurement": $("#side5").val(),
	 					"companyId": $("#companyId").val(),
	 					"email": $("#email").val(),
	 					"school": school_array
		 			}
				};
		
		
		var response_obj = call_rest_api(request_data);

		//response
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				alert("廚房修改成功");
				window.location.reload();
			} else {
				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}
	 function addKitchen(){
			if( ($("#Name").val() == "") || ($("#companyId").val() == "") || ($("#email").val() == "") 
					 ||($("#Address").val() == "") 		|| ($("#Tel").val() == "") 
					 || ($("#Fax").val() == "") 	|| ($("#Nutritionist").val() == "")
					 || ($("#Manager").val() == "") || ($("#ManagerEmail").val() == ""))
					{
						alert("請輸入必要欄位!");
						return;
					}		
					/* var school_array = new Array();
					var myForm = document.forms.theForm;
					var myControls = myForm.elements['customFieldValue'];
					var myControls2 = myForm.elements['customFieldName'];
					if(myControls){for(var i=0 ; i<myControls.length; i++){
					var school = new Object();
					school.sid = myControls[i].value;
					school.schoolName = myControls2[i].value;
					school_array.push(school);
					}} */
					
					
					
					var request_data =	{
							 "method":"addKitchen",
				 				"args":{	
				 					//"kitchenId": kitchenId,
				 					"kitchenName": $("#Name").val(),
				 					"kitchenType": $("#kitchenType").val(),
				 					"address": $("#Address").val(),
				 					"tel": $("#Tel").val(),
				 					"ownner": $("#ownner").val(),
				 					"fax": $("#Fax").val(),
				 					"nutritionist": $("#Nutritionist").val(),
				 					"chef": $("#Chef").val(),
				 					"qualifier": $("#Qualifier").val(),
				 					"haccp": "",
				 					//"insurement": $("#side5").val(),
				 					"companyId": $("#companyId").val(),
				 					"email": $("#email").val(),
				 					"manager": $("#Manager").val(),
				 					"manageremail":$("#ManagerEmail").val()
				 					//, "school": school_array
					 			}
							};
					
					
					var response_obj = call_rest_api(request_data);

					//response
					if(response_obj.result == 1)
					{
						var result_content = response_obj.result_content;
						if(result_content.resStatus == 1){	//success
							alert("廚房新增成功");
							location.assign("../listKitchen/");
						} else {
							alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
						}
					} else{
						alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
					}
				}
				
function deleteKitchen(kid){
		var answer = confirm("請問是否要啟用/停用此廚房？"); //暫時直接用/帶過。
    	if (answer) {
    		//request
    		var request_data =	{
    							 "method":"deleteKitchen",
    				 				"args":{	
    				 					"kitchenId":kid
    					 			}
    							};
    		var response_obj = call_rest_api(request_data);
    		
    		if(response_obj.result == 1)
    		{
    			var result_content = response_obj.result_content;
    			if(result_content.resStatus == 1){	//success
    				
    				alert("刪除成功");
					window.location.reload();
    			} else {
    				alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
    			}
    		} else{
    			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
    		}
    	} else {
    		return;
    	}

	}
	
function show_kitchen(result_content)
{			
	var total_page = parseInt(result_content.kitchenList.length / row);
	var add = result_content.kitchenList.length % row;
	
	if(add > 0) total_page++;
	
	var element=document.getElementById("query_rule");
	element.innerHTML = "資料筆數：" + result_content.kitchenList.length + " |共 "+total_page+"頁";
	
	var new_table = "";
	
	var new_head = "<table width='100%' class='table table-bordered table-striped'>";
	new_head += "<thead><tr>";
	new_head +=	"<td align=\"center\" valign=\"middle\">廚房名稱</td>" +
	//"<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"TIT_A\">電話</td>" +
				"<td align=\"center\" valign=\"middle\">統一編號</td>" +
	//"<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"TIT_A\">Email</td>" +
	//"<td align=\"center\" valign=\"middle\" bgcolor=\"#678948\" class=\"TIT_A\">供餐學校</td>" +
				"<td align=\"center\" valign=\"middle\">選項</td>";
	new_head += "</thead></tr>";
	
	var divData = new Array;
	var index;
	for(var i = 0 ; i<result_content.kitchenList.length; i++)
	{
		
		if(i %2 ==0)
		{
			new_table += "<tr>";
			new_table += "<td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"+ result_content.kitchenList[i].kitchenName +"&nbsp;</td>" +	
						 //"<td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"+ result_content.kitchenList[i].tel +"</td>" +
						 "<td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"+ result_content.kitchenList[i].companyId +"</td>" +
						 //"<td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\">"+ result_content.kitchenList[i].email +"</td>" +
						/*  "<td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\"><select>";
						 for(var key in result_content.kitchenList[i].schoolList)
							{
								new_table +='<option>'+ result_content.kitchenList[i].schoolList[key] +'</option>';
							} 
						 new_table +="</select></td>" + */
						 "<td bgcolor=\"#FFFFFF\" class=\"componentContentRightLine\" align='center'>"+
						 //"<a href=\"#\"  onclick=query_kitchen_detail('"+result_content.kitchenList[i].kitchenId+"')>編輯</a>"+
						 "<a href=\"#\" data-toggle='tooltip' title='編輯' class='btn btn-primary' style='min-width:48%; margin:0; margin-right:1px' onclick=query_kitchen_detail("+result_content.kitchenList[i].kitchenId +")><i class='fa fa-pencil'></i></a>";
			if(result_content.kitchenList[i].enable==1){			 
				new_table += "<a href=\"#\" data-toggle='tooltip' title='停用' class='btn btn-primary' style='min-width:48%; margin:0' onclick = deleteKitchen("+result_content.kitchenList[i].kitchenId + ")><i class='fa fa-times'></i></a></td>";
			}else{
				new_table += "<a href=\"#\" data-toggle='tooltip' title='啟用' class='btn btn-primary' style='min-width:48%; margin:0' onclick = deleteKitchen("+result_content.kitchenList[i].kitchenId + ")><i class='fa fa-check'></i></a></td>";
			}
						 //"<a href=\"#\" onclick=deleteKitchen(" + result_content.kitchenList[i].kitchenId +")>刪除</a></td>";
			new_table += "</tr>";
		}
		else
		{
			new_table += "<tr>";
			new_table += "<td bgcolor=\"#ececec\" class=\"componentContentRightLine\">"+ result_content.kitchenList[i].kitchenName +"&nbsp;</td>" +	
						 //"<td bgcolor=\"#ececec\" class=\"componentContentRightLine\">"+ result_content.kitchenList[i].tel +"</td>" +
						 "<td bgcolor=\"#ececec\" class=\"componentContentRightLine\">"+ result_content.kitchenList[i].companyId +"</td>" +
						 //"<td bgcolor=\"#ececec\" class=\"componentContentRightLine\">"+ result_content.kitchenList[i].email +"</td>" +
						/*  "<td bgcolor=\"#ececec\" class=\"componentContentRightLine\"><select>";
						 for(var key in result_content.kitchenList[i].schoolList)
							{
								new_table +='<option>'+ result_content.kitchenList[i].schoolList[key] +'</option>';
							} 
						 new_table +="</select></td>" + */
						 "<td bgcolor=\"#ececec\" class=\"componentContentRightLine\" align='center'>"+
						 //"<a href=\"#\"  onclick=query_kitchen_detail('"+result_content.kitchenList[i].kitchenId+"')>編輯</a>"+
						 //"<a href=\"#\" onclick=deleteKitchen(" + result_content.kitchenList[i].kitchenId +")>刪除</a></td>";
						 "<a href=\"#\" data-toggle='tooltip' title='編輯' class='btn btn-primary' style='min-width:48%; margin:0; margin-right:1px' onclick=query_kitchen_detail("+result_content.kitchenList[i].kitchenId+") ><i class='fa fa-pencil'></i></a>";
			if(result_content.kitchenList[i].enable==1){			 
				new_table += "<a href=\"#\" data-toggle='tooltip' title='停用' class='btn btn-primary' style='min-width:48%; margin:0' onclick = deleteKitchen("+result_content.kitchenList[i].kitchenId + ")><i class='fa fa-times'></i></a></td>";
			}else{
				new_table += "<a href=\"#\" data-toggle='tooltip' title='啟用' class='btn btn-primary' style='min-width:48%; margin:0' onclick = deleteKitchen("+result_content.kitchenList[i].kitchenId + ")><i class='fa fa-check'></i></a></td>";
			}
						 //"<a href=\"#\" data-toggle='tooltip' title='刪除' class='btn btn-primary' style='min-width:48%; margin:0' onclick = deleteKitchen(" + result_content.kitchenList[i].kitchenId +")><i class='fa fa-trash-o'></i></a></td>";
			new_table += "</tr>";
		}
		if((i+1) % row == 0){
			index = Math.floor((i+1) / row);
			divData[index] = new_head+ new_table+ "<table>";
			new_table = " "; 
		}
		if((i+1) == result_content.kitchenList.length && result_content.kitchenList.length % row != 0){
			divData[total_page] = new_head+ new_table+ "<table>";
			new_div = " ";
		}
		
	}
	if(result_content.kitchenList.length>0){
		pagination(divData, total_page);		
	}else{
		alert("查無資料");
		return;
	}
}

function pagination(divData, total_page){
	$('#pagination').twbsPagination({
        totalPages: total_page,
        visiblePages: 7,
        onPageClick: function(e, p){
        	$("#kitchen_list").html("");
    		$("#kitchen_list").append(divData[p]);
        }
    });	
}

function query_kitchen()
{
	$("#newAdd").html(""+"<a href=\"#\" style='margin:0' onclick=add_kitchen_detail()  >新增</a>");
	var kitchenName = $("#QueryKitchenName").val();
	var companyId = $("#QueryCompanyId").val();
	$("#kitchen_list").html("");
	//clean data of the pagination plugin
	$('#pagination').empty();
	$('#pagination').removeData("twbs-pagination");
	$('#pagination').unbind("page");
	
	/// 3.查詢菜單內容 ///
	var request_data =	{
							"method":"queryKitchenList",
							"args":{
								"kitchenName": kitchenName ,
								"companyId": companyId
							}
						};
	
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	//API呼叫正確
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1)
		{	
			show_kitchen(result_content);
		}
		else
		{
			alert("查詢供餐廚房發生錯誤，訊息為：" + result_content.msg);
			return 0;
		}
	}
	else
	{
		alert("查詢供餐廚房發生無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		return 0;
	}
	
}		

function QueryUserTypeList(kitchenType){
	var request_data =	{
			"method":"QueryUserTypeList",
			"args":{
				"type": "Kitchen"
			}
		};
	var response_obj = call_rest_api(request_data);
	if(response_obj.result == 1)
	{	//API呼叫正確
		var result_content = response_obj.result_content;
		if(result_content.resStatus == 1)
		{	
			console.log(result_content.usertypelist);
			var newOpt;
			for(var i=0;i<result_content.usertypelist.length;i++){
				newOpt+= "<option value="+result_content.usertypelist[i].id+">"+result_content.usertypelist[i].name+"</option>";
			}
			$("#kitchenType").append(newOpt);
		}
		else
		{
			alert("查詢供餐廚房發生錯誤，訊息為：" + result_content.msg);
			return 0;
		}
	}
	else
	{
		alert("查詢供餐廚房發生無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		return 0;
	}
}
function goBack(){
	$("#goBack").hide();
	$("#kitchen_list").show("slow");
	$("#queryList").hide();
	$("#no_kitchen").hide();
	$("#query_rule").show("slow");
	$("#query_bar").show("slow");
	$("#newAdd").show("slow");
	$("#pagination").show("slow");
}
$(document).ready(function(){	
	$("#queryList").hide();
	$("#goBack").hide();
	query_kitchen();
});
</script>
</head>
<body>
<!-- 四點設計套版 -->
	<div class="contents-title">供餐廚房資訊</div>
      <div class="contents-wrap">
      	<div id="query_bar" style="font-size: medium;font-weight: bolder;">
			<tr>
				<td>廚房名稱︰</td>
				<td><input type="text" id="QueryKitchenName" name="QueryKitchenName" class="searchInput max50"></td>
				&nbsp;
				<td>統一編號︰</td>
				<td><input type="text" id="QueryCompanyId" name="QueryCompanyId" class="searchInput max50"></td>
				&nbsp;&nbsp;
				<td>
					<button style="margin-top: 0px;" class="btn btn-primary" onclick="query_kitchen()">查詢</button>
				</td>
			</tr>
		</div>
      	<h5 class="section-head with-border" style="height:35px;">
	      	<div id="query_rule" class="TITLE_TXT flo_l"></div>
			<div class="TITLE_TXT_BBT FL_R">
				<div id="newAdd"></div>
				<div id="goBack"><a href="#" style='margin:0' onclick='goBack()' >回前頁</a></div>
			</div>
		</h5>
		<div align="right">
			<ul class="pagination-sm" id="pagination">
			</ul>
      	</div>
      	<div id="kitchen_list" class="TAB_TY_D"></div>			
		<div id="queryList" class="TAB_TY_B"></div>  
      </div>
        
    <!-- 舊團膳套版 -->
    <div style="display:none;"> 
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
				<div class="TITLE_TXT flo_l ">供餐廚房資訊</div>
				<div id="query_rule" class="TITLE_TXT flo_l"></div>
				<div class="TITLE_TXT_BBT FL_R">
					<div id="page"></div>
					<div id="goBack"><a href="#" onclick=goBack() >回前頁</a>
					</div>
				</div>
		</div>	
			<div id="kitchen_list1" class="TAB_TY_D"></div>		
			<div id="queryList" class="TAB_TY_B"></div>
	</div>
</body>
</html>