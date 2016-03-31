	var MSG= new MsgsProcessing();
	function query_kitchen_detail(kitchenId){
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
			if(result_content.resStatus == "1"){
			
				var type = result_content.kitchenType;	//型態
				var kitchenName = result_content.kitchenName;	//名稱
				var companyId = result_content.companyId;	//統一編號/代碼
				var email = result_content.email;	//電子信箱
				var ownner = result_content.ownner;	//負責人
				var address = result_content.address;	//地址
				var tel = result_content.tel;	//電話
				var fax = result_content.fax;	//傳真
				var nutritionist = result_content.nutritionist;	//營養師/午餐秘書
				var chef = result_content.chef;	//衛管人員
				var qualifier = result_content.qualifier;	//食品技師
				var haccp = result_content.haccp;	//HACCP認證號
				var insurement = result_content.insurement;	//營養小常識
				var schoolCode = result_content.schoolCode;	//學校代號
				var manager = result_content.manager;	//督導主管
				var manageremail = result_content.manageremail;	//督導主管mail
				
				 
				switch (type) {
					case "005":
						$("#ktype").html("");
						$("#ktype").html("團膳業者logo圖 *");
						$('.k005tr').show();
						$('.k006n7tr').hide();
						$("#iframetitle").hide();
						$("#iframe").hide();
						break;
					case "006":
						$("#ktype").html("");
						$("#ktype").html("自設廚房logo圖 *");
						$('.k005tr').hide();
						$('.k006n7tr').show();
						$('.k006tr').show();
						$('#comIDlabel').text('學校編號');
//						$('.nutritionistType').text('營養師/午餐祕書 *');
						break;
					case "007":
						$("#ktype").html("");
						$("#ktype").html("學校logo圖 *");
						$("#kitchenTitle").html("");
						$("#kitchenTitle").html("學校基本資料設定 *");
						$('#offerschool').show();
						$('#offerschooltitle').show();
						$('.k006tr').hide();
						$('.k005tr').hide();
						$('.k006n7tr').show();
						$('.nok007tr').hide();
						$('#comIDlabel').text('學校編號');
						break;
					case "101": //學校
					case "102": //統包商
					case "103": //餐廳
						kitchenName = $("#_StrName").val();   //顯示用戶名稱而非廚房名稱
						break;
					default:
						break;
				}
				
				//20141227 Ric 設定iFrame and LINK
				var pageURL =  $("#pageURL").val(); //取出網址
				if(schoolCode=="null"){
					$('.k006n7tr').hide();
				}else{
					$("#schoolLink").val("http://"+pageURL+"/web/custom/school/"+schoolCode+"/");
					$("#schooliFrame").val("<iframe src='http://"+pageURL+"/web/custom/school/"+schoolCode+"/' width='830' height='850' scrolling='no' frameborder='0'></iframe>");
					$("#schoolSiFrame").val("<iframe src='http://"+pageURL+"/web/custom/mschool/"+schoolCode+"/' width='200' height='400' scrolling='no' frameborder='0'></iframe>");
				}
				$("#KitchenId").val(kitchenId);
				$("#KitchenName").val(kitchenName);
				$("#CompanyId").val(companyId);
				$("#kitchenType").val(type);
				$("#kname").html("");$("#kname").html(kitchenName);
				$("#comID").html("");$("#comID").html(companyId);
				$("#Email").val(email);
				$("#Ownner").val(ownner);
				$("#Address").val(address);
				$("#Tel").val(tel);
				$("#Fax").val(fax);
				$("#Nutritionist").val(nutritionist);
				$("#Chef").val(chef);
				$("#Qualifier").val(qualifier);
				$("#Insurement").val(insurement);
				$("#Manager").val(manager);
				$("#ManagerEmail").val(manageremail);
				
				var new_div = "";
				new_div += " <table class=\"table table-bordered table-striped\" width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  id=\"customFields\">";
						new_div += " <thead><tr>";
							new_div += " <td class=\"col-xs-2 text-left\">"
							+ " 	學校代碼</td>";
							new_div += " <td colspan=\"2\" class=\"col-xs-5 text-center\">"
							+ " 	學校名稱</td>";
							new_div += " <td class=\"col-xs-2 text-right\">"
							+ " 	供應人數(份數)</td>";
							new_div += " <td class=\"col-xs-2 text-right\">"
							+ " 	供餐現況</td>";
							new_div += "<td class=\"col-xs-3 text-right\">"
							+ " 	選項</td>";
						new_div += " </tr></thead>";
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
							new_div +="<input type=\"hidden\" readonly=\"readonly\" id=\"customFieldValue\" name=\"customFieldValue\" value=\""+ result_content.school[j].sid+"\" placeholder=\"Input Name\" style=\"border: none;\"/>"+ result_content.school[j].schoolCode+" </td>";
							new_div += " <td colspan=\"2\"";
						if(	j%2 == 1){
							new_div += "class='componetContent componentContentLeftLine' >";
						}else if(j%2 == 0){
							new_div +="class='componetContent2 componentContentLeftLine' >";
						}
							new_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.school[j].schoolName+"\" placeholder=\"Input Value\" style=\"border: none;\" />"+ result_content.school[j].schoolName+"</td>";
							new_div += " <td  width=\"100px\"";

						if(j%2 == 1){
							new_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
						}else if(j%2 == 0){
							new_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
						}
						new_div += '<input id="quantity_'+result_content.school[j].sid+'" type="text" class="form-control" style="width:50px;" value=" ';
						new_div += result_content.school[j].quantity +'" /></td>';							
						new_div += " <td  width=\"100px\"";
						if(j%2 == 1){
							new_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
						}else if(j%2 == 0){
							new_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
						}
						if(result_content.school[j].offered){
							new_div += " <div><input type='checkbox' onchange=\"updateQuantity("+result_content.school[j].sid+");\" id='offer' checked>&nbsp供餐</div></td>";
						}else{
							new_div += " <div><input type='checkbox' onchange=\"updateQuantity("+result_content.school[j].sid+");\" id='offer'>&nbsp供餐</div></td>";
						}
						new_div += " <td  width=\"100px\"";
						if(j%2 == 1){
							new_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
						}else if(j%2 == 0){
							new_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
						}
						new_div += "<button onclick=\"deleteSchool($(this),"+kitchenId+","+ result_content.school[j].sid +")\" class=\"btn btn-primary\"><i class=\"fa fa-trash-o\"></i>刪除</button> <button onclick=\"updateQuantity("+result_content.school[j].sid+");\" class=\"btn btn-primary\"><i class=\"fa fa-pencil\"></i>更新</button></td>";
//						new_div +="<a href=\"#\" onclick=\"deleteSchool($(this),"+kitchenId+","+ result_content.school[j].sid +")\">刪除</a> <a href=\"#\" onclick=\"updateQuantity("+result_content.school[j].sid+");\">更新 </a></td>";
					new_div += " </tr>";
				}
				new_div += " </td></tr></table>	";
					
				$("#schoolList").html("");
				$("#schoolList").append(new_div);
				$("#schoolList").show();
				
				//被供餐清單
				var request_data2 = {
						"method":"QueryOfferedList", 
						"args": {} 
					};
				var response_obj2 = call_rest_api(request_data2);
				if(response_obj2.result == 1){
					var result_content2 = response_obj2.result_content;
					if(result_content2.resStatus == "1"){
						var newTab = "<table class='table table-bordered table-striped' width='100%' border='0' cellspacing='0' id='tableSchooled'>";
							newTab += "<thead><tr>";
							newTab += "<td class='col-xs-2' style='text-align: center;'>廚房代碼</td>";
							newTab += "<td class='col-xs-2' style='text-align: center;'>廚房名稱</td>";
							newTab += "<td class='col-xs-2' style='text-align: center;'>上次供餐日</td>";
							newTab += "<td class='col-xs-2' style='text-align: center;'>選項</td>";
							newTab += "</tr></thead>";
						for(var i=0;i<result_content2.kitchenBO.length;i++){
							newTab += "<tr>";
							if(i%2 == 1){
								newTab += "<td class='componetContent componentContentLeftLine' style='text-align: center;'>"+ result_content2.kitchenBO[i].companyId +"</td>";
								newTab += "<td class='componetContent componentContentLeftLine' style='text-align: center;'>"+ result_content2.kitchenBO[i].kitchenName +"</td>";
								newTab += "<td class='componetContent componentContentLeftLine' style='text-align: center;'>"+ result_content2.kitchenBO[i].lastOfferedDate +"</td>";
								newTab += "<td class='componetContent componentContentLeftLine' style='text-align: center;'>"
									+ "<button onclick='deleteSchool($(this)"+","+ result_content2.kitchenBO[i].kitchenId +","+result_content2.kitchenBO[i].schoolId+")' class='btn btn-primary' style='margin-top: 0px;'><i class=\"fa fa-trash-o\"></i>刪除</button></td>";
							}else{
								newTab += "<td class='componetContent2 componentContentLeftLine' style='text-align: center;'>"+ result_content2.kitchenBO[i].companyId +"</td>";
								newTab += "<td class='componetContent2 componentContentLeftLine' style='text-align: center;'>"+ result_content2.kitchenBO[i].kitchenName +"</td>";
								newTab += "<td class='componetContent2 componentContentLeftLine' style='text-align: center;'>"+ result_content2.kitchenBO[i].lastOfferedDate +"</td>";
								newTab += "<td class='componetContent2 componentContentLeftLine' style='text-align: center;'>"
									+ "<button onclick='deleteSchool($(this)"+","+ result_content2.kitchenBO[i].kitchenId +","+result_content2.kitchenBO[i].schoolId+")' class='btn btn-primary' style='margin-top: 0px;'><i class=\"fa fa-trash-o\"></i>刪除</button></td>";
							}
							newTab += "</tr>";
						}
						newTab += "</table>";
						$("#schooled").html(newTab);
					}else{
						var newTab = "<div><h6><b>無資料</b></h6></div>";
						$("#schooled").html(newTab);
					}
				}else{
					var newTab = "<div><h6><b>無資料</b></h6></div>";
					$("#schooled").html(newTab);
				}
				
				$("#goBack").show();
				city();	//drop down counties
				area();
				school();
				checkFile();
				setInputLength();
				showAcceptSchoolKitchenWaitApproveByKitchenIdList();
			}
		}

	}

	// 顯示acceptSchoolKitchen中待審核的資料,只有當有待審核的資料才顯示表格
	function showAcceptSchoolKitchenWaitApproveByKitchenIdList(){
		var request_data = {
				"method" : "queryAcceptSchoolKitchenWaitApproveByKitchen",
				"args" : {
					"kitchenId" : kitchenId
				}
		};
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
			listLength = result_content.askList.length;
			var ask_div = "";
			if(result_content.askList.length > 0){
				ask_div += "<h4>待審核清單</h4>";
				ask_div += " <table class=\"table table-bordered table-striped\" width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  id=\"askCustomFields\">";
				ask_div += " <thead><tr>";
				ask_div += " <td class=\"col-xs-2 text-center\">"
				+ " 	學校代碼</td>";
				ask_div += " <td colspan=\"2\" class=\"col-xs-5 text-center\">"
				+ " 	學校名稱</td>";
				ask_div += " <td class=\"col-xs-2 text-center\">"
				+ " 	供應人數(份數)</td>";	
				ask_div += "<td class=\"col-xs-2 text-center\">"
				+ " 	狀態</td>";
				ask_div += "<td class=\"col-xs-2 text-center\">"
				+ " 	選項</td>";
				ask_div += " </tr></thead>";
				for(var j=0;j<listLength;j++){
					ask_div += " <tr valign=\"top\" align=\"left\">";
					ask_div += " <td  width=\"86px\"";
					if(j%2 == 1){
						ask_div += "class='componetContent componentContentLeftLine' >";
					}else if(j%2 == 0){
						ask_div +="class='componetContent2 componentContentLeftLine' >";
					}
					ask_div +="<input type=\"hidden\" readonly=\"readonly\" id=\"customFieldValue\" name=\"customFieldValue\" value=\""+ result_content.askList[j].schoolId+"\" placeholder=\"Input Name\" style=\"border: none;\"/><center>"+ result_content.askList[j].schoolCode+"</center></td>";
					ask_div += " <td colspan=\"2\"";
					if(	j%2 == 1){
						ask_div += "class='componetContent componentContentLeftLine' >";
					}else if(j%2 == 0){
						ask_div +="class='componetContent2 componentContentLeftLine' >";
					}
					ask_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.askList[j].schoolName+"\" placeholder=\"Input Value\" style=\"border: none;\" /><center>"+ result_content.askList[j].schoolName+"</center></td>";
					ask_div += " <td  width=\"100px\"";
					if(j%2 == 1){
						ask_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
					}else if(j%2 == 0){
						ask_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
					}
					ask_div += '<input id="quantityAsk_'+result_content.askList[j].schoolId+'" readonly=\"readonly\" type="text" class="form-control" style="width:50px;" value=" ';
					ask_div += result_content.askList[j].quantity +'" /></td>';	
					ask_div += " <td  width=\"100px\"";
					if(	j%2 == 1){
						ask_div += "class='componetContent componentContentLeftLine' >";
					}else if(j%2 == 0){
						ask_div +="class='componetContent2 componentContentLeftLine' >";
					}
					ask_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.askList[j].status+"\" placeholder=\"Input Value\" style=\"border: none;\" /><center>"+ result_content.askList[j].status+"</center></td>";
					ask_div += " <td  width=\"100px\"";	
					if(j%2 == 1){
						ask_div += "class='componetContent componentContentRightLine BT_IN_BBTER' >";
					}else if(j%2 == 0){
						ask_div +="class='componetContent2 componentContentRightLine BT_IN_BBTER' >";
					}
					ask_div += "<button onclick=\"deleteAcceptSchoolKitchen($(this),"+result_content.askList[j].id +")\" class=\"btn btn-primary\"><i class=\"fa fa-trash-o\"></i>刪除</button> </td>";
					ask_div += " </tr>";
				}
				ask_div += " </td></tr></table>	";
			}
			$("#acceptSchoolKitchenWaitApproveByKitchenIdList").html("");
			$("#acceptSchoolKitchenWaitApproveByKitchenIdList").append(ask_div);
			$("#acceptSchoolKitchenWaitApproveByKitchenIdList").show();
		}
	}
	
	function showIframe(tag){
		$('#iFramePreviewTd').html("");
		$('#iFramePreview').show();
		$('#iFramePreviewTd').html($('#'+tag).val());
	}
	function hideIframe(){
		$('#iFramePreview').hide();
	}
//20140715 Ric 檢查圖片是否存在
function checkFile(){
	var request_data =	{
			 "method":"checkFileExist",
				"args":{	
					"pageName":"kitchen",
					"dishId": ""
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
function deleteFile(){
	var request_data =	{
			 "method":"deleteFile",
				"args":{	
					"pageName":"kitchen",
					"dishId": "",
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
					MSG.alertMsgs("check", "刪除成功" , 0);
					//alert("刪除成功");
					$(element).parent().parent().remove();
				} else {
					MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
					//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
				}
			} else{
				MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);
				//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			}
		} else {
			return;
		}
		
	}

	// 刪除acceptSchoolKitchen中的資料
	function deleteAcceptSchoolKitchen(element, id) {
		var answer = confirm("請問是否要刪除？");
		if (answer) {
			// request
			var request_data = {
					"method" : "deleteAcceptschoolkitchen",
					"args" : {
						"acceptschoolkitchenId" : id
					}
			};
			
			var response_obj = call_rest_api(request_data);
			if(response_obj.result == 1){
				var result_content = response_obj.result_content;
				if(result_content.resStatus == 1){	//success
					MSG.alertMsgs("check", "刪除成功" , 0);
					$(element).parent().parent().remove();
					showAcceptSchoolKitchenWaitApproveByKitchenIdList();
				} else {
					MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
				}
			} else{
				MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);
			}
		} else {
			return;
		}
	}
	
	//Ric  Search school by contry area
	var drop_down_default = "<select class=\"form-control\" name=\"City\" id=\"City\"><option value=\"0\">請選擇</option></select>";


	//縣市
	function city() {
		var request_data = {
			"method" : "customerQueryCounties",
			"args" : {
				"condition" : 1
			}
		};
		var response_obj = call_rest_api(request_data);

		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { //success
				var drop_down_counties = "<select class=\"form-control\" name=\"City\" id=\"City\" onChange=\"area()\">";
				drop_down_counties += "<option value=\"0\">請選擇</option>";
				for (var i = 0; i < result_content.counties.length; i++) {

					drop_down_counties += "<option value=" + result_content.counties[i].cid + ">" + result_content.counties[i].countiesName + "</option>";
				}
				drop_down_counties += "</select>";
				$("#dropdown_City").html("");
				$("#dropdown_City").append(drop_down_counties);
				$("#dropdown_Area").append(drop_down_default);
				$("#dropdown_School").append(drop_down_default);

			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else {
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);
			//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}

	}

	//縣市改變修改區域
	function area() {
		//alert("aa");
		var counties = $("#City").val();
		//alert(counties);
		var request_data = {
			"method" : "customerQueryArea",
			"args" : {
				"cid" : counties
			}
		};
		var response_obj = call_rest_api(request_data);

		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { //success
				var drop_down_area = "<select class=\"form-control\" name=\"Area\" id=\"Area\"  onChange=\"school()\">";
				drop_down_area += "<option value=\"0\">請選擇</option>";
				for (var i = 0; i < result_content.area.length; i++) {

					drop_down_area += "<option value=" +  result_content.area[i].aid + ">" + result_content.area[i].areaName + "</option>";
				}
				drop_down_area += "</select>";

				//清空
				$("#dropdown_Area").html("");
				$("#dropdown_School").html("");

				$("#dropdown_Area").append(drop_down_area);
				$("#dropdown_School").append(drop_down_default);
			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else {
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);
			//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
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
			var drop_down_school = "<select class=\"form-control\" name=\"School\" id=\"School\"  >";
				drop_down_school += "<option value=\"0\">請選擇</option>";
				for(var i=0; i<result_content.school.length; i++) {
					
					drop_down_school += "<option value=" +  result_content.school[i].sid + ">" + result_content.school[i].schoolName + "</option>";
				}
			drop_down_school += "</select>";
			
			//清空
			$("#dropdown_School").html("");
			
			$("#dropdown_School").append(drop_down_school);
		} else {
			MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
			//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
		}
	} else{
		MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);
		//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
	}
	}

	function checkSchool()
	{
		if(($('#School').find(":selected").text() == ""||$('#School').find(":selected").text() == "請選擇"))
		{
			MSG.alertMsgs("check", "請選擇學校!" , 0);
			return false;
		}
		if(!parseInt($("#txtQuantity").val())> 0)
		{
			MSG.alertMsgs("check", "請設定供應份數!" , 0);
			return false;
		}
	return true;
	}
	function addSchool()
	{
		if(checkSchool() == false)
			return false;
		else

			//加上寫入Schoo;
		var sid =  $('#School').find(":selected").val();
		//20140901 Ric 新增顯示school Code
		var schoolCode =  $('#School').find(":selected").val();
		var request_data =	{
				 "method":"updateSchoolKitchen",
					"args":{	
						"kitchenIdSC": kitchenId,	
						"schoolIdSC":sid,
						"skQuantity":$("#txtQuantity").val(),
						"act":"add"
					}
				};		
		var response_obj = call_rest_api(request_data);
		var userName = $("#_uName").val();
		userName = userName.substring(2);

		// 查詢學校是否開啟審核流程的開關,如啟用審核,則必需進行審核,如停用審核或查無資料,則直接新增供餐廚房
		var isNeedApprove;
		var req_data = {
				"method" : "queryAcceptSwitch",
				"args" : {
					"schoolId" : sid,
					"acceptType" : "kitchenVerify"
				}
			};
		var resp_obj = call_rest_api(req_data);
		if (resp_obj.result == 1) {
			var result = resp_obj.result_content;
			listLength = result.acceptswitchList.length;
		}
		if(listLength > 0){
			status = result.acceptswitchList[0].status;
			if("1" == status){ // Status 狀態 0:停用 ,1:啟用
				isNeedApprove = "true";
			}
		}
		//response
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success				
				if(userName != sid && "true" == isNeedApprove){
					MSG.alertMsgs("check", "已送出申請單" , 0);
					showAcceptSchoolKitchenWaitApproveByKitchenIdList();
				} else { // 供餐廚房當設定供餐給自己時, 應不需審核而自動通過 + 供餐給學校如學校停用審核也不需審核而自動通過
					MSG.alertMsgs("check", "已寫入資料庫，新增學校成功" , 0);
					$("#customFields").append('<tr valign="top" align="left"  bgcolor=\"#DBFFB8\" ><td colspan="2" class="componentContentLeftLine">'
							+' <input type="hidden" readonly="readonly" id="customFieldValue" name="customFieldValue" value="' 
							+ $('#School').find(":selected").val() + '" placeholder="Input Name" />' + result_content.schoolCode + '</td><td class="">' 
							+'<input type="hidden" id="customFieldName" readonly="readonly" name="customFieldName" value="'
							+ $('#School').find(":selected").text() + '" placeholder="Input Value" />' + $('#School').find(":selected").text() 
							+ ' </td><td><input id="quantity_'+sid+'" type="text" value="'+$("#txtQuantity").val()+'" class="form-control" style="width:50px;" /></td> '
							+ ' <td  width=\"100px\" class=\'componetContent componentContentRightLine BT_IN_BBTER\' > ' 
							+ '<div><input type=\'checkbox\' onclick=\"updateQuantity("+result_content.school[j].sid+");\" id=\'offer\' checked>&nbsp供餐</div></td>'
							+ ' <td  class="componentContentRightLine BT_IN_BBTER"> <button class="btn btn-primary" onclick="deleteSchool($(this),'
							+kitchenId+',' + sid +')"><i class=\"fa fa-trash-o\"></i>刪除</button>'
							+' <button class="btn btn-primary" onclick=\"updateQuantity('+sid+');\"><i class=\"fa fa-pencil\"></i>更新</button></td></tr>');
				}
			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
			}
		} else{
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);
		}
		return true;
	}
	function updateKitchen()
	{
		if (($("#Address").val() == "") || ($("#Tel").val() == "") || ($("#Fax").val() == "") || ($("#Nutritionist").val() == "") || ($("#Manager").val() == "") || ($("#Email").val() == "") || ($("#ManagerEmail").val() == "")) {
			MSG.alertMsgs("check", "請輸入必要欄位!" , 0);
			//alert("請輸入必要欄位!");
			return false;
		}
		if($("#kitchenType").val() == "005"){
		//食品技師 或 衛管人員擇一填寫即可  20140424 Raymond
			if (($("#Chef").val() == "") && ($("#Qualifier").val() == "")) {
				MSG.alertMsgs("check", "食品技師或衛管人員請擇一輸入!" , 0);
				//alert("食品技師或衛管人員請擇一輸入!");
				return false;
			}
		}
		if (($("#Insurement").val().length >= 251)) {
			MSG.alertMsgs("check", "營養小常識請輸入250字以內!" , 0);
			//alert("營養小常識請輸入250字以內!");
			return false;
		}
	
		//2014506 Raymond 先不檢核email欄位
		if ($("#Email").val().length > 0) {
			if (!validateEmail($("#Email").val())) {
				MSG.alertMsgs("check", "E-Mail格式錯誤!" , 0);
				//alert("E-Mail格式錯誤!");
				return false;
			}
		}
		
		if ($("#ManagerEmail").val().length > 0) {
			if (!validateEmail($("#ManagerEmail").val())) {
				MSG.alertMsgs("check", "督導人員E-Mail格式錯誤!" , 0);
				//alert("E-Mail格式錯誤!");
				return false;
			}
		}
//		var school_array = new Array();
//		var myForm = document.forms.theForm;
//		var myControls = myForm.elements['customFieldValue'];
//		var myControls2 = myForm.elements['customFieldName'];
//		if(myControls){for(var i=0 ; i<myControls.length; i++){
//		var school = new Object();
//		school.sid = myControls[i].value;
//		school.schoolName = myControls2[i].value;
//		school_array.push(school);
//		}}
		
		//var kitchenId = $("#update_kitchen_button").attr("kid");
		
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
						"haccp": $("#HACCP").val(),
						"insurement": $("#Insurement").val(),
						"companyId": $("#companyId").val(),
						"email": $("#Email").val(),
						"manager": $("#Manager").val(),
						"manageremail": $("#ManagerEmail").val()
					}
				};
		
		
		var response_obj = call_rest_api(request_data);

		//response
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				MSG.alertMsgs("checkAndReload", "資料修改成功", 0);
				//alert("廚房修改成功");
				//window.location.reload();
			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 0);
				//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 0);
			//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
	}
		
	//檢核email格式
	function validateEmail(sEmail) {
		var filter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
		if (filter.test(sEmail)) {
			return true;
		}
		else {
			return false;
		}
	}

	function addNewKitchen() {
		if (checkInput() == false)
			return;
		document.theForm.action = "../manageKitchen/?action=updateKitchen";
		document.theForm.submit();
		MSG.alertMsgs("check", "更新成功", 0);
				//alert("更新成功");
		return true;
	}
	var client = new XMLHttpRequest();
	function upload() {
		var file = document.getElementById("file");
		/* Create a FormData instance */
		var formData = new FormData();
		/* Add the file */
		formData.append("file", file.files[0]);
		formData.append("func", $("#func").val());
		formData.append("overWrite", "0");
		client.open("post", "/cateringservice/file/upload", true);
		client.send(formData); /* Send to server */
	}
	/* Check the response status */
	client.onreadystatechange = function() {
		if (client.readyState == 4 && client.status == 200) {

			var obj = JSON.parse(client.responseText);

			if (obj.retStatus == "1") {
				alert("檔案上傳成功");
//				var kitchenpic = "<img src=\"/cateringservice/file/SHOW/kitchen\" width=\"150\" height=\"150\" />";
//				$("#kitchenpic_o").hide();
//				$("#kitchenpic").html("");
//				$("#kitchenpic").append(kitchenpic);
//				$("#kitchenpic").show();
				window.location.reload();
//				document.getElementById('t1').contentDocument.location.reload(true);
			} else {
				alert("檔案上傳失敗，原因為" + obj.retMsg);
			}
		}
	}
	//更新供餐份量
	function updateQuantity(sid){
		var kid=kitchenId;
		var quantity=$("#quantity_"+sid).val();
		//var offered = $("#offer").prop("checked");
		var offered =  document.getElementById("offer").checked
		console.log(offered);
		//alert(quantity);
		if (quantity=="" || quantity=="0" ||quantity==null){
			alert("供餐份量不得空白或為0"+"#quantity_"+sid);
			return;
		}
		
		var request_data =	{
				 "method":"updateSchoolKitchen",
					"args":{	
						"kitchenIdSC": kid,	
						"schoolIdSC":sid,
						"skQuantity":quantity,
						"act":"update",
						"offered" : offered
					}
				};		
		var response_obj = call_rest_api(request_data);

		//response
		if(response_obj.result == 1)
		{
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1){	//success
				MSG.alertMsgs("showmsg", result_content.schoolName+"：資料更新完畢" , 1);
				//alert("新增學校成功");
			} else {
				MSG.alertMsgs("check", "資料發生錯誤，訊息為：" + result_content.msg, 1);
				//alert("呼叫API發生錯誤，訊息為：" + result_content.msg);
			}
		} else{
			MSG.alertMsgs("check", "連線發生無法預期的錯誤，訊息為：" + response_obj.error_msg, 1);
			//alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
		}
		return true;	
	}

	// 檢視供餐申請記錄
	function qryHalfYearAcceptSchoolKitchen() {
		var request_data = {
				"method" : "queryHalfYearAcceptSchoolKitchen",
				"args" : {
					"kitchenId" : kitchenId
				}
		};
		
		var halfYearAsk_div = "";					
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1){
			var result_content = response_obj.result_content;
			listLength = result_content.halfyearacceptschoolkitchenList.length;
			
			halfYearAsk_div += " <table class=\"table table-bordered table-striped\" width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  id=\"halfYearAskCustomFields\">";
			halfYearAsk_div += " <thead><tr>";
			halfYearAsk_div += " <td class=\"col-xs-1 text-center\">"
			+ " 	序號</td>";
			halfYearAsk_div += " <td colspan=\"2\" class=\"col-xs-3 text-center\">"
			+ " 	學校名稱</td>";
			halfYearAsk_div += "<td class=\"text-center\" width=\"9%\">"
			+ " 	狀態</td>";
			halfYearAsk_div += "<td class=\"text-center\" width=\"9%\">"
			+ " 	動作</td>";
			halfYearAsk_div += "<td class=\"text-center\" width=\"16%\">"
			+ " 	申請日期</td>";									
			halfYearAsk_div += " <td class=\"text-center\" width=\"10%\">"
			+ " 	最後更新者</td>";	
			halfYearAsk_div += "<td class=\"text-center\" width=\"16%\">"
			+ " 	最後更新日期</td>";	
			halfYearAsk_div += " </tr></thead>";	
			
			for(var j=0;j<listLength;j++){				
					halfYearAsk_div += " <tr valign=\"top\" align=\"left\">";
					halfYearAsk_div += " <td width=\"6%\" ";
					if(j%2 == 1){
						halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
					}else if(j%2 == 0){
						halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
					}
					halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\" id=\"customFieldValue\" name=\"customFieldValue\" value=\""+ result_content.halfyearacceptschoolkitchenList[j].id+"\" placeholder=\"Input Name\" style=\"border: none;\"/>"+ result_content.halfyearacceptschoolkitchenList[j].id+" </td>";
					halfYearAsk_div += " <td colspan=\"2\" ";
					if(	j%2 == 1){
						halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
					}else if(j%2 == 0){
						halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
					}
					halfYearAsk_div +="	<input type=\"hidden\" id=\"customFieldName\" readonly=\"readonly\" name=\"customFieldName\" value=\""+ result_content.halfyearacceptschoolkitchenList[j].schoolName+"\" placeholder=\"Input Value\" style=\"border: none;\" />"+ result_content.halfyearacceptschoolkitchenList[j].schoolName+"</td>";
					halfYearAsk_div += " <td width=\"9%\" ";
					if(j%2 == 1){
						halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
					}else if(j%2 == 0){
						halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
					}
					halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearacceptschoolkitchenList[j].status+" </td>";
					halfYearAsk_div += " <td width=\"9%\" ";
					if(j%2 == 1){
						halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
					}else if(j%2 == 0){
						halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
					}
					halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearacceptschoolkitchenList[j].action+" </td>";
					halfYearAsk_div += " <td width=\"16%\" ";
					if(j%2 == 1){
						halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
					}else if(j%2 == 0){
						halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
					}
					halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearacceptschoolkitchenList[j].createDateTime+" </td>";
					halfYearAsk_div += " <td width=\"10%\" ";
					if(j%2 == 1){
						halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
					}else if(j%2 == 0){
						halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
					}
					halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearacceptschoolkitchenList[j].modifyUser+" </td>";
					halfYearAsk_div += " <td width=\"16%\" ";
					if(j%2 == 1){
						halfYearAsk_div += "class='componetContent componentContentLeftLine' >";
					}else if(j%2 == 0){
						halfYearAsk_div +="class='componetContent2 componentContentLeftLine' >";
					}
					halfYearAsk_div +="<input type=\"hidden\" readonly=\"readonly\"/>"+ result_content.halfyearacceptschoolkitchenList[j].modifyDateTime+" </td>";
					
					halfYearAsk_div += " </tr>";
				}
									
			halfYearAsk_div += " </td></tr></table>	";						
		}
		
		$("#halfYearAskList").html("");
		$("#halfYearAskList").append(halfYearAsk_div);
		$("#halfYearAskList").show();
		$("#acceptSchoolKitchenModal").modal();
	}
	
	$(document).ready(function() {
		$('.haccpInput').hide();
		query_kitchen_detail(kitchenId);
		$('#iFramePreview').hide();
		city(); //drop down counties
		area();
		school();
	});
