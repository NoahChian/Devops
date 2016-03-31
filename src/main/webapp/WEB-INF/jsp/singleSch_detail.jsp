<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>

	//設置一全域變數來記錄畫面的調味料table總數量
	var stable_count = 0;
	var stable_status = new Array();		//紀錄table是否有刪除的情況

	//用來儲存上個網頁用get傳進來的數值。		
	var mid = "";	
	var menu_date = "";
	var schoolId = "";


	/*
	 * 2014/06/19 Ric 上傳菜色照片
	 */
	var client = new XMLHttpRequest();
	var tagname = "";
	function upload(menuDate, ingredientId, supplierCompanyId, lotNumber,
			stockDate, filetag, ingredientCategory) {
		tagname = "file" + filetag;
		var file = document.getElementById(tagname);
		if (!$("#" + tagname).val()) {
			MSG.alertMsgs('check', '請選擇檔案', 0);
			return false;
		}
		/* Create a FormData instance */
		var formData = new FormData();
		/* Add the file */
		formData.append("file", file.files[0]);
		formData.append("func", "inspect_v2|" + menuDate + "|" + ingredientId + "|"
				+ supplierCompanyId + "|" + lotNumber + "|" + stockDate + "|" + ingredientCategory);
		formData.append("overWrite", "0");
		client.open("post", "/cateringservice/file/upload", false);
		client.send(formData); /* Send to server */
		// } 20140625 Ric改同步處理
		/* Check the response status */
		// client.onreadystatechange = function()
		// {
		if (client.readyState == 4 && client.status == 200) {
			var obj = JSON.parse(client.responseText);
			if (obj.retStatus == "1") {
				// MSG.alertMsgs("check", "檔案上傳成功", 0);
				// $("#pictr"+filetag).remove();
				// tr set id
				tagname = "";
				return ("檔案上傳成功");
			} else {
				// MSG.alertMsgs("check", "檔案上傳失敗，原因為" + obj.retMsg, 0);
				return ("檔案上傳失敗，" + obj.retMsg);
			}
		}
	}
	function uploadOneFile(menuDate, ingredientId, supplierCompanyId, lotNumber,
			stockDate, filetag, ingredientCategory) {
		tagname = "file" + filetag;
		var runFile = new Object;
		runFile.file = [];
		runFile.ingredientName = []; // 菜單日期
		runFile.menuDate = []; // 菜單日期
		runFile.ingredientId = []; // 食材ID
		runFile.supplierCompanyid = []; // 供應商統一編號
		runFile.stockDate = []; // 食材進貨日期
		runFile.lotNumber = []; // 食材批號
		runFile.ingredientCategory = []; //食材或調味料
		if ($("#" + tagname)[0].files[0] != null) {
			runFile.file[0] = filetag;
			runFile.ingredientName[0] = $("#s_name" + filetag).val();
			runFile.menuDate[0] = menuDate;
			runFile.ingredientId[0] = ingredientId;
			runFile.supplierCompanyid[0] = supplierCompanyId;
			runFile.stockDate[0] = stockDate;
			runFile.lotNumber[0] = lotNumber;
			runFile.ingredientCategory = ingredientCategory;
			uploadFileProcessing(runFile);
		} else {
			MSG.alertMsgs("check", "沒有選擇檔案", 0);
		}
	}
	function uploadFileProcessing(runFile) {
		MSG.alertMsgs("loading", "檔案檢查與上傳...", 0);
		var uploadMsgs = "";
		for (var i = 0; i < runFile.file.length; i++) {
			uploadMsgs += runFile.ingredientName[i]+ ":"+ upload(runFile.menuDate[i], runFile.ingredientId[i],
							runFile.supplierCompanyid[i], runFile.lotNumber[i],
							runFile.stockDate[i], runFile.file[i], runFile.ingredientCategory[i]) + "<br>";
		}
		$(".blockUIImgs").hide();
		$(".blockUIMsgs").html(uploadMsgs);
		$(".blockUIMsgs")
				.after(
						'<input type="button" onclick="$.unblockUI();location.reload();" value="確定">');
		// MSG.alertMsgs("check", uploadMsgs, 0);
		$("#uploadAllPic").val("上傳已選取之檔案");
	}

	function deleteFile(menuDate, ingredientId, supplierCompanyId, lotNumber, stockDate) {
		var request_data = {
			"method" : "deleteIngredientInspectionFile",
			"args" : {
				"menuDate" : menuDate,
				"ingredientId" : ingredientId,
				"supplierCompanyId" : supplierCompanyId,
				"stockDate" : stockDate,
				"lotNumber" : lotNumber
			}
		};
		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) {
			var result_content = response_obj.result_content;
			if (result_content.resStatus == "1") { // success
				MSG.alertMsgs("checkAndReload", "檔案刪除成功 " + result_content.msg, 0);
				return;
			} else {
				MSG.alertMsgs("check", "檔案處理異常：" + result_content.msg, 0);
				return;
			}
		} else {
			MSG.alertMsgs("check", "連線錯誤：" + response_obj.error_msg, 0);
			return;
		}
	}


	/**
	 * 查詢該業者的供應商名稱列表
	 * 
	 * @param	
	 * @return	
	 * @author	Eason
	 * @date	2013/12/18
	 */
	function query_supply_name()
	{
		/// 1.查詢供應商名稱列表 ///
		var request_data =	{
								"method":"querySupplyOptionList",
								"args":{}
							};
		
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1)
		{	//API呼叫正確
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1)
			{	//查詢供應商名稱正確
				/// 2.回傳供應商名稱列表 ///
				return result_content.supplyNameOption;
				
			}
			else
			{
				alert("查詢供應商名稱發生錯誤，訊息為：" + result_content.msg);
				return 0;
			}
		}
		else
		{
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			return 0;
		}
		
	}




	/**
	 * 顯示本日所有菜色資訊
	 *
	 * @param	result_content	菜單中的所有菜色資訊
	 * @return	
	 * @author	Eason
	 * @date	2013/12/19
	 */
	function show_oneday_menu(result_content)
	{
		//alert(result_content.dishs.length);
		var menu_table = '<div  class="TAB_TY_B"><table class=\"table table-bordered table-striped\" width="100%" ><thead><tr><td colspan="2" align="center">['+ schoolName +']<b>本日所有菜色維護</b></td></tr></thead>';
		
		menu_table +=	"<tr>"+
							'<td width="25%">供餐日期</td>'+
							'<td><input type="text" size="35" value ="'+ menu_date +'" disabled></td>'+
						"</tr>";
						
		for(var i =0; i<result_content.dishs.length; i++)
		{ var bgc ="";
			menu_table +=	"<tr>"+
			'<td width="25%"' + bgc +'><span style="color:red;float:right">*</span>菜色</td>'+
			'<td ' + bgc +'><input type="text" size="35" value ="'+ result_content.dishs[i].name +' " disabled>'+
			'<span style="float:right;" ><a class=\"btn btn-primary\" style=\"margin:0\" href="../singleSch_modify/?dishId='+ result_content.dishs[i].dishId +'&name=' + result_content.dishs[i].name +'&menuDate='+ menu_date+'&schoolId='+ schoolId+'&schoolName='+ schoolName +'">食材維護</a></span></td>'+
			"</tr>";
		}
		menu_table +=	"</table></div>"; 
		
		
		//將動態產生的table加上去
		$("#menu").append(menu_table);
		
	}

	/**
	 * 查詢並顯示本日所有菜色資訊
	 * 			
	 * @param	
	 * @return	
	 * @author	Eason
	 * @date	2014/01/06
	 */
	function query_oneday_menu()
	{
		/// 1.查詢菜單內容 ///
		var request_data =	{
								"method":"querySchoolIngredientListByDate",
								"args":{
									"menuDate":menu_date,
									"schoolId":schoolId
								}
							};
		
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1)
		{	//API呼叫正確
			var result_content = response_obj.result_content;
			//if(result_content.resStatus == 1)
			if(result_content.resStatus == 1)//先解API BUG
			{	//查詢菜單正確
				/// 2.顯示查詢到的菜單資訊 ///
				show_oneday_menu(result_content);
			}
			else
			{
				alert("查詢業者期間的菜單發生錯誤，訊息為：" + result_content.msg);
				return 0;
			}
		}
		else
		{
			alert("查詢業者期間的菜單發生無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			return 0;
		}
	}


	/**
	 * 動態刪除調味料欄位的資訊，但還不會真的更新資料庫，等到使用者點選確認修改才會一次把資料更新進後端資料庫
	 * 
	 * @param	欲刪除的table名稱
	 * @return	
	 * @author	Eason
	 * @date	2013/12/26
	 */
	function delete_stable(stable_num)
	{
		//alert(stable_num);
		
		///1.隱藏table///
		$("#s_table"+stable_num).hide();
		
		///2.將table的狀態設為已刪除///
		stable_status[stable_num] = 0;
	}

	/**
	 * 更新菜色的調味料資訊
	 * 
	 * @param	
	 * @return	
	 * @author	Eason
	 * @date	2013/12/21
	 */
	function update_seasonings()
	{
		if(checkSpecialCharacters()){
			return;
		}
		//用來存放資訊的陣列
		var seasonings_array = new Array(); 
		
		//alert("食材資料修改完成");
		//alert($("#supply_name1").find("option:selected").text());
		
		/// 1.取得填寫的資料(不包含已經被刪除的TABLE)並且檢查資料是否必填欄位都有填寫 ///
		for(var i = 0 ; i< stable_count; i++)
		{
			//檢查這個table是否有被刪除
			if(stable_status[i] == 1)
			{
				var seasoning = new Object();			//用來存放一個調味料資訊的物件
				var ingredientAttribute = new Object();			//用來存放一個食材屬性的物件
				
				seasoning.name = $("#s_name"+i).val();
				if(seasoning.name == "")
				{
					alert("未填寫第"+ (i +1)+"項調味料的名稱!")
					return 0;
				}
				seasoning.productName = $("#s_productName"+i).val();
				seasoning.supplyName = $("#s_supply_name"+i).find("option:selected").text();
				seasoning.manufacturer = $("#s_manufacturer"+i).val();
				seasoning.brand = $("#s_brand"+i).val();
				seasoning.label = $("#s_label"+i).find("option:selected").text();
				seasoning.authenticateId = $("#s_authenticateId"+i).val();
				//如果使用者有填驗證標章，則一定要填驗證編號
				if(seasoning.authenticateId == "" && seasoning.label !="")
				{
					alert("未填寫調味料的驗證編號!")
					return 0;
				}
				seasoning.stockDate = $("#s_stockDate"+i).val();
				seasoning.productDate = $("#s_productDate"+i).val();
				seasoning.validDate = $("#s_validDate"+i).val();
				seasoning.ingredientQuantity = $("#s_ingredientQuantity"+i).val();
				seasoning.ingredientLotNum = $("#s_ingredientLotNum"+i).val();
				seasoning.ingredientBatchId = $("#ingredientBatchId"+i).val();
				seasoning.ingredientId = $("#ingredientId"+i).val();
				ingredientAttribute.gmbean = $("#s_gmbean"+i).val();
				ingredientAttribute.gmcorn = $("#s_gmcorn"+i).val();
				ingredientAttribute.psfood = $("#s_psfood"+i).val();
				seasoning.ingredientAttribute = ingredientAttribute;
				//加入該菜色的食材資訊中
				seasonings_array.push(seasoning);
			}
		}
		//alert(JSON.stringify(seasonings_array));
		/// 2.將要更新的資料傳回後台 ///
			
		
		var request_data =	{
								"method":"updateSchoolSeasoningDetail",
								"args":{
										"menuDate" : menu_date,		
										"seasoningsCount" : seasonings_array.length,
										"seasonings" : seasonings_array,
										"schoolId" : schoolId
								}
							};
		//alert(JSON.stringify(request_data));
		
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1)
		{	//API呼叫正確
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1)
			{	//修改調味料資訊成功
				
				alert("修改成功!");
				window.location.reload();	//重新整理畫面
			}
			else
			{
				alert("修改調味料資訊發生錯誤，訊息為：" + result_content.msg);
				return 0;
			}
		}
		else
		{
			alert("修改調味料資訊發生無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			return 0;
		}
		
		
	}


	/**
	 * 新增一個調味料的空白table，讓業者進行新增填寫
	 * 
	 * @param	
	 * @return	
	 * @author	Eason
	 * @date	2013/12/21
	 */
	function add_seasoning()
	{
		//alert("add_ingredients");
		var supply_name_options = query_supply_name();
		//如果廠商查詢就出錯了，就不顯示了
		if (supply_name_options == 0)
		{
			alert("查詢供應商列表錯誤，無法新增食材");
			return 0;
		}
		
		var seasoning_table =  '<table class=\"table table-bordered table-striped\" width="100%" id="s_table'+ stable_count +'">';

		//alert(result_content.seasoningsCount);
		seasoning_table	+=	'<tr>'+
								'<td width="20%" align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>調味料名稱</td>'+
								'<td align="left" valign="absmiddle">'+
								'<input type="text" id="s_name'+ stable_count +'" size="30" value="" class="max255"></td>'+		
									'<td width="20%" align="left" nowrap="nowrap"  class="tmpV001">產品名稱</td>'+
							 		'<td align="left" valign="middle" ><input  class="max255" id="s_productName'+ stable_count +'" type="text" size="30" value=""></td>'+				
							'</tr>';
							
		//產生供應商名稱的下拉式選單
		seasoning_table += "<tr>"+
								'<td   align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>供應商名稱</td>'+
								'<td  ><select name="s_supply_name" id="s_supply_name'+ stable_count +'" >';
		for(var key in supply_name_options)
		{
			seasoning_table +='<option>'+ supply_name_options[key].supplyName +'</option>';
		}
		seasoning_table +=		"</select></td>"+
									 '<td  width="20%" align="left" nowrap="nowrap" class="tmpV001">製造商名稱</td>'+
					 				 '<td  align="left" valign="middle">'+
					 			////20140730 Ric  因應更版 改為顯示
					 		 		//'<input  id="s_brand'+ stable_count +'" type="hidden" value="">'+
					 	 		 	 '<input  class="max255" id="s_manufacturer'+ stable_count +'" type="text" size="30" value=""></td>'
					 	 		 	+
					 	 			////20140730 Ric  因應更版 改為顯示
									// '<td  width="20%" align="left" nowrap="nowrap">品牌</td>'+
					 				// '<td  align="left" valign="middle">'+
					 	 		 	// '<input  class="max255" id="s_brand'+ stable_count +'" type="text" size="30" value=""></td>'
					 	 		 	 //更版修改結束
							"</tr>";
		//產生驗證標章的下拉式選單
		seasoning_table += "<tr>"+
								'<td align="left" nowrap="nowrap">驗證標章</td>'+
								'<td ><select name="s_label" id="s_label'+ stable_count +'" >'+
								'<option></option>'+//目前新增食材的驗證標章選項先寫死只有吉園圃、產銷履歷、有機、CAS、GMP，預設為空
								'<option>CAS</option>'+	
								'<option>CAS_ORGANIC</option>'+	
								'<option>GAP</option>'+	
								'<option>GMP</option>'+	
								'<option>HALAL</option>'+	
								'<option>HACCP</option>'+	
								'<option>HEALTH</option>'+	
								'<option>ISO22000</option>'+	
								'<option>ISO9001</option>'+	
								'<option>MILK</option>'+	
								'<option>TAP</option>'+	
								//更新驗證標章清單20140903
								"</select></td>"+
								'<td width="20%" align="left" nowrap="nowrap">驗證編號</td>'+
								'<td align="left" valign="absmiddle" >'+
								'<input type="text" id="s_authenticateId'+ stable_count +'" size="30" value="" class="max45"></td>'+						
							'</tr>';
		
		seasoning_table	+=	'<tr>'+
								'<td   width="20%" align="left" nowrap="nowrap">進貨日期</td>'+
								'<td   align="left" valign="absmiddle" >'+
								'<input type="text" id="s_stockDate'+ stable_count +'" size="30" value="" class="dateMode"></td>'+						
								'<td  width="20%" align="left" nowrap="nowrap"  class="tmpV001">重量</td>'+
								'<td  align="left" valign="middle">'+ 
								'<input style="width: 150px;" class="max255 numOnly" id="s_ingredientQuantity'+ stable_count +'" type="text" size="30" value="">'+
								'<select name="unit" id="unit'+ stable_count +'"><option>公斤</option></select></td>';
								
							////20140730 Ric  因應更版 更改位置	
								//'<td  width="20%" align="left" nowrap="nowrap" >批號</td>'+
						 		//'<td  align="left" valign="middle">'+ 
						 		//'<input class="max255 numOnly" id="s_ingredientLotNum'+ stable_count +'" type="text" size="30" value=""></td>'
						 		//更版修改結束
						 		
							'</tr>';
							
		seasoning_table	+=	'<tr>'+
								'<td width="20%" align="left" nowrap="nowrap">生產日期</td>'+
								'<td align="left" valign="absmiddle">'+
								'<input type="text" id="s_productDate'+ stable_count +'" size="30" value="" class="max10"></td>'+						
								'<td width="20%" align="left" nowrap="nowrap">有效日期</td>'+
								'<td align="left" valign="absmiddle">'+
								'<input type="text" id="s_validDate'+ stable_count +'" size="30" value="" class="max10"></td>'+						
							'</tr>';
						////20140730 Ric  因應更版 更改位置					
		seasoning_table	+=	'<tr>'+
								'<td  width="20%" align="left" nowrap="nowrap">批號</td>'+
								'<td  align="left" valign="absmiddle" colspan="3">'+
								'<input type="text" id="s_ingredientLotNum'+ stable_count +'" size="30" value="" class="max255"></td>'+						
							'</tr>'; 
		seasoning_table	+=	"<tr>";
		seasoning_table += '<td width="20%" align="left" nowrap="nowrap"  class="tmpV001">產品屬性</td>';
		seasoning_table += "<td align='left' valign='middle' colspan='3'>"
	 			+ "<input type='checkbox' class = 'i_gmbean' id='s_gmbean" + stable_count+ "' value='0'> "
	 			+ "<label for='s_gmbean"+ stable_count+ "'>基改黃豆</label> "
	 			+ "<input type='checkbox' class = 'i_gmcorn' id='s_gmcorn"+ stable_count+ "' value='0'> "
	 			+ "<label for='s_gmcorn"+ stable_count+ "'>基改玉米</label> "
	 			+ "<input type='checkbox' class = 'i_psfood' id='s_psfood"+ stable_count+ "' value='0'> "
	 			+ "<label for='s_psfood"+ stable_count+ "'>加工品</label> ";
	 			seasoning_table += '</td>';
			seasoning_table += "</tr>";
		seasoning_table += 	"<tr><td></td>"+
								'<td align="right"  colspan="3"  class="BT_IN_BBTER"><input type="button" value="刪除本項調味料" onclick="delete_stable('+ stable_count +')"/></td>'+
							"</tr>";
							
		//用一個不顯示的元素來儲存ingredientBatchId，以方便之後進行新增、刪除、修改使用
		seasoning_table	+=  '<input id="ingredientBatchId'+ stable_count +'" type="hidden" value="">';
		seasoning_table	+=  '<input id="ingredientId'+ stable_count +'" type="hidden" value="">';
		seasoning_table +=	"</table>"; 
		
		//將動態產生的table加上去
		$("#seasoning").append(seasoning_table);

		// 設定check box內容值
		$('input[type="checkbox"]').change(function() {
			this.value = this.checked ? 1 : 0;
		});
		//載入 calendar 套件
		var s_selector_stock_date="#s_stockDate"+stable_count;
		var s_selector_product_date="#s_productDate"+stable_count;
		var s_selector_valid_date="#s_validDate"+stable_count;
		$( s_selector_stock_date ).datepicker({ dateFormat: "yy/mm/dd" });	//調味料進貨日期
		$( s_selector_product_date ).datepicker({ dateFormat: "yy/mm/dd" });	//調味料生產日期
		$( s_selector_valid_date ).datepicker({ dateFormat: "yy/mm/dd" });	//調味料有效日期
		/*
		*20140630 Ric
		* Setting maxlength and readonly
		*/
		setInputLength();
		//記錄整個畫面調味料的table數量與狀態
		stable_status[stable_count] = 1;	//1代表這張TABLE存再沒有被刪除 
		stable_count++;
	}

	/**
	 * 顯示本日所有調味料資訊
	 *
	 * @param	result_content	今天所有調味料資訊
	 * @return	
	 * @author	Eason
	 * @date	2014/01/06
	 */
	function show_seasoning(result_content)
	{
		/// 對每一樣調味料產生一個table來進顯示 ///
		for(var i = 0 ; i<result_content.seasoningsCount; i++)
		{
			var check_gmbean = "";
	 		var check_gmcorn = "";
	 		var check_psfood = "";
	 		if (result_content.seasonings[i].ingredientAttribute.gmbean == "1") {check_gmbean = "checked";}
	 		if (result_content.seasonings[i].ingredientAttribute.gmcorn == "1") {check_gmcorn = "checked";}
	 		if (result_content.seasonings[i].ingredientAttribute.psfood == "1") {check_psfood = "checked";}
			var seasoning_table =  '<table class=\"table table-bordered table-striped\" width="100%" id="s_table'+ stable_count +'">';
			
			//alert(result_content.seasoningsCount);
			seasoning_table	+=	'<tr>'+
									'<td width="20%" align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>調味料名稱</td>'+
									'<td align="left" valign="absmiddle">'+
									'<input type="text" id="s_name'+ stable_count +'" size="30" value="'+ result_content.seasonings[i].name +'" class="max255"></td>'+		
									'<td width="20%" align="left" nowrap="nowrap"  class="tmpV001">產品名稱</td>'+
							 		'<td align="left" valign="middle" ><input  class="max255" id="s_productName'+ stable_count +'" type="text" size="30" value="' + result_content.seasonings[i].productName + '"></td>'+
								'</tr>';
								
			//產生供應商名稱的下拉式選單
			seasoning_table += "<tr>"+
									'<td   align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>供應商名稱</td>'+
									'<td  ><select name="s_supply_name" id="s_supply_name'+ stable_count +'" >'+
										'<option>'+ result_content.seasonings[i].supplyName +'</option>';	//預設欄位是廠商輸入的供應商名稱
			for(var key in result_content.seasonings[i].supplyNameOption)
			{
				//選項中不再顯示將預設的供應商選項，避免重複
				if(result_content.seasonings[i].supplyName != result_content.seasonings[i].supplyNameOption[key])
					seasoning_table +='<option>'+ result_content.seasonings[i].supplyNameOption[key] +'</option>';
			}
			seasoning_table +=		'</select></td>'+
									 '<td  width="20%" align="left" nowrap="nowrap"  class="tmpV001">製造商名稱</td>'+
					 				 '<td  align="left" valign="middle">'+
					 			////20140730 Ric  因應更版 改為顯示
					 		 		// '<input  id="brand'+ stable_count +'" type="hidden" value="' + result_content.seasonings[i].brand + '">'+
					 	 		 	 '<input  class="max255" id="s_manufacturer'+ stable_count +'" type="text" size="30" value="' + result_content.seasonings[i].manufacturer + '"></td>'
					 	 		 	+
					 	 			////20140730 Ric  因應更版 改為顯示
									// '<td  width="20%" align="left" nowrap="nowrap">品牌</td>'+
					 				// '<td  align="left" valign="middle">'+
					 	 		 	// '<input  class="max255" id="s_brand'+ stable_count +'" type="text" size="30" value="' + result_content.seasonings[i].brand + '"></td>'
					 	 		 	 //更版修改結束
								"</tr>";
			
			//產生驗證標章的下拉式選單
			seasoning_table += "<tr>"+
									'<td align="left" nowrap="nowrap">驗證標章</td>'+
									'<td><select name="s_label" id="s_label'+ stable_count +'" >'+
										'<option>'+ result_content.seasonings[i].label +'</option>';	//預設欄位是廠商輸入的驗證標章
			for(var key in result_content.seasonings[i].labelOption)
			{
				//選項中不再顯示將預設的驗證標章，避免重複
				if(result_content.seasonings[i].label != result_content.seasonings[i].labelOption[key])
					seasoning_table +='<option>'+ result_content.seasonings[i].labelOption[key] +'</option>';
			} 
			seasoning_table +=		"</select></td>"+
									'<td width="20%" align="left" nowrap="nowrap">驗證編號</td>'+
									'<td align="left" valign="absmiddle">'+
									'<input type="text" id="s_authenticateId'+ stable_count +'" size="30" value="'+ result_content.seasonings[i].authenticateId +'" class="max45"></td>'+	
								"</tr>";				
			
			seasoning_table	+=	'<tr>'+
									'<td  width="20%" align="left" nowrap="nowrap">進貨日期</td>'+
									'<td   align="left" valign="absmiddle">'+
									'<input type="text" id="s_stockDate'+ stable_count +'" size="30" value="'+ result_content.seasonings[i].stockDate +'" class="dateMode dateAD"></td>'+	
									'<td  width="20%" align="left" nowrap="nowrap"  class="tmpV001">重量</td>'+
							 		'<td  align="left" valign="middle">'+ 
							 		'<input style="width: 150px;" class="max255 numOnly" id="s_ingredientQuantity'+ stable_count +'" type="text" size="30" value="' + result_content.seasonings[i].ingredientQuantity + '">'+
							 		'<select name="unit" id="unit'+ stable_count +'"><option>公斤</option></select></td>';
							 		////20140730 Ric  因應更版 更改位置	
									//'<td  width="20%" align="left" nowrap="nowrap" >批號</td>'+
							 		//'<td  align="left" valign="middle">'+ 
							 		//'<input class="max255 numOnly" id="s_ingredientLotNum'+ stable_count +'" type="text" size="30" value="' + result_content.seasonings[i].ingredientLotNum + '"></td>'
							 		//更版修改結束
							 		
								'</tr>';
								
			seasoning_table	+=	'<tr>'+
									'<td width="20%" align="left" nowrap="nowrap">生產日期</td>'+
									'<td align="left" valign="absmiddle">'+
									'<input type="text" id="s_productDate'+ stable_count +'" size="30" value="'+ result_content.seasonings[i].productDate +'" class="max10 dateAD"></td>'+	
									'<td width="20%" align="left" nowrap="nowrap">有效日期</td>'+
									'<td  align="left" valign="absmiddle">'+
									'<input type="text" id="s_validDate'+ stable_count +'" size="30" value="'+ result_content.seasonings[i].validDate +'" class="max10 dateAD"></td>'+						
								'</tr>';
							////20140730 Ric  因應更版 更改位置						
			 seasoning_table	+=	'<tr>'+
									'<td   width="20%" align="left" nowrap="nowrap">批號</td>'+
									'<td   align="left" valign="absmiddle" colspan="3">'+
									'<input type="text" id="s_ingredientLotNum'+ stable_count +'" size="30" value="'+ result_content.seasonings[i].ingredientLotNum +'" class="max255"></td>'+						
								'</tr>'; 

			seasoning_table	+=	"<tr>";
			seasoning_table += '<td width="20%" align="left" nowrap="nowrap"  class="tmpV001">產品屬性</td>';
			seasoning_table += "<td align='left' valign='middle' colspan='3'>"
		 			+ "<input type='checkbox' class = 'i_gmbean' id='s_gmbean" + stable_count+ "'value='"+ result_content.seasonings[i].ingredientAttribute.gmbean+ "' "+ check_gmbean+ "> "
		 			+ "<label for='s_gmbean"+ stable_count+ "'>基改黃豆</label> "
		 			+ "<input type='checkbox' class = 'i_gmcorn' id='s_gmcorn"+ stable_count+ "'value='"+ result_content.seasonings[i].ingredientAttribute.gmcorn+ "' "+ check_gmcorn+ "> "
		 			+ "<label for='s_gmcorn"+ stable_count+ "'>基改玉米</label> "
		 			+ "<input type='checkbox' class = 'i_psfood' id='s_psfood"+ stable_count+ "'value='"+ result_content.seasonings[i].ingredientAttribute.psfood+ "' "+ check_psfood+ "> "
		 			+ "<label for='s_psfood"+ stable_count+ "'>加工品</label> ";
		 			seasoning_table += '</td>';
 			seasoning_table += "</tr>";
 		
 			seasoning_table += "<tr>";
	 			seasoning_table += '<td  width="20%" align="left" nowrap="nowrap">檢驗報告</td>';
	 			seasoning_table += '<td  align="left" valign="middle" colspan="3">';
	 			seasoning_table += "<input style='float: left;' type= 'file' id='file"+ stable_count+ "' name='file"+ stable_count+ "' />"
	 				+ "<span style='line-height:30px;'>"
	 				+ "<a class=\"btn btn-primary\" style=\"margin:0\" href='javascript:uploadOneFile(\""+ menu_date 
					+ "\",\"" + result_content.seasonings[i].ingredientId + "\",\""
					+ result_content.seasonings[i].supplierCompanyId + "\",\"" 
					+ result_content.seasonings[i].ingredientLotNum + "\",\""
					+ result_content.seasonings[i].stockDate + "\",\"" + i + "\", \"seasoning\");'>上傳檔案</a> ";
	 			if (result_content.seasonings[i].reportFileName) {
	 				seasoning_table += "<a class=\"btn btn-primary\" style=\"margin:0\" href='"+ result_content.seasonings[i].reportFileName+ "' target='_blank'>下載檔案</a> " 
	 						+ "<a class=\"btn btn-primary\" style=\"margin:0\" href='javascript:deleteFile(\""+ menu_date + "\",\"" 
							+ result_content.seasonings[i].ingredientId + "\",\""
							+ result_content.seasonings[i].supplierCompanyId + "\",\"" 
							+ result_content.seasonings[i].ingredientLotNum + "\",\""
							+ result_content.seasonings[i].stockDate + "\");'>刪除</a></span>";
	 			}
 				seasoning_table += '</td>';
 			seasoning_table += "</tr>";
			seasoning_table += 	"<tr><td></td>"+
									'<td  colspan="3" align="right"><input class=\"btn btn-primary\" style=\"margin:0\" type="button" value="刪除本項調味料" onclick="delete_stable('+ stable_count +')"/></td>'+
								"</tr>";
			//用一個不顯示的元素來儲存ingredientBatchId，以方便之後進行新增、刪除、修改使用
			seasoning_table	+=  '<input id="ingredientBatchId'+ stable_count +'" type="hidden" value="' + result_content.seasonings[i].ingredientBatchId + '">';
			seasoning_table	+=  '<input id="ingredientId'+ stable_count +'" type="hidden" value="' + result_content.seasonings[i].ingredientId + '">';
			seasoning_table +=	"</table>"; 
			//將動態產生的table加上去
			$("#seasoning").append(seasoning_table);

			// 設定check box內容值
			$('input[type="checkbox"]').change(function() {
				this.value = this.checked ? 1 : 0;
			});
			//載入 calendar 套件
			var s_selector_stock_date="#s_stockDate"+stable_count;
			var s_selector_product_date="#s_productDate"+stable_count;
			var s_selector_valid_date="#s_validDate"+stable_count;
			$( s_selector_stock_date ).datepicker({ dateFormat: "yy/mm/dd" });	//調味料進貨日期
			$( s_selector_product_date ).datepicker({ dateFormat: "yy/mm/dd" });	//調味料生產日期
			$( s_selector_valid_date ).datepicker({ dateFormat: "yy/mm/dd" });	//調味料有效日期
			/*
			*20140630 Ric
			* Setting maxlength and readonly
			*/
			setInputLength();			
			//記錄整個畫面調味料的table數量與狀態
			stable_status[stable_count] = 1;	//1代表這張TABLE存再沒有被刪除 
			stable_count++;
			
		}
		
		
		
	} 

	 
	 
	/**
	 * 取得查詢並顯示今天的調味料資訊
	 * 
	 * @param	
	 * @return	
	 * @author	Eason
	 * @date	2014/01/06
	 */
	 function query_seasoning()
	{
	/// 1.查詢菜單內容 ///
		var request_data =	{
								"method":"querySchoolSeasoningDetail",
								"args":{
									"menuDate":menu_date,
									"schoolId":schoolId
								}
							};
		
		var response_obj = call_rest_api(request_data);
		if(response_obj.result == 1)
		{	//API呼叫正確
			var result_content = response_obj.result_content;
			if(result_content.resStatus == 1)
			{	//查詢調味料正確
				
				/// 2.顯示查詢到的調味料資訊 ///
				show_seasoning(result_content);
			}
			else
			{
				alert("查詢調味料發生錯誤，訊息為：" + result_content.msg);
				return 0;
			}
		}
		else
		{
			alert("查詢調味料發生無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			return 0;
		}
		
	}

	/**
	 * 取得URL中用GET傳遞的bid參數
	 * 
	 * @param	要從URL取得的變數名稱
	 * @return	從URL取得的變數值
	 * @author	Eason
	 * @date	2013/12/12
	 */
	 function get_param(arg_str){
		
		var url=window.location.toString(); //取得當前網址
		var str=""; //參數中等號左邊的值
		var str_value=""; //參數中等號右邊的值
		 
		if(url.indexOf("?")!=-1) //如果網址有"?"符號
		{
			var ary=url.split("?")[1].split("&");
			//取得"?"右邊網址後利用"&"分割字串存入ary陣列 ["a=1","b=2","c=3"]
			for(var i in ary){
				//取得陣列長度去跑迴圈，如:網址有三個參數，則會跑三次
				str=ary[i].split("=")[0];
				//取得參數"="左邊的值存入str變數中
				if (str == arg_str)
				{
					str_value = ary[i].split("=")[1];
					return str_value;
				}
			}
			//如果都沒有拿到這參數，則回傳錯誤
			if(str_value == "")
			{
				str_value = "get_param_ERROR";
				return str_value;
			}
		}
	}

		/*** 使用 tabs 套件 ***/
		$(function() {
			$( "#tabs" ).tabs();
		});
		
		/**
		2014/10/29  add by Joshua
		輸入欄位過濾掉特殊字元
		*/
// 		document.onchange = checkSpecialCharacters;
		function checkSpecialCharacters(){
			var inputFields = $("table[id^='s_table']").find("input[type=text]");
			var inputFieldsCount = $("table[id^='s_table']").find("input[type=text]").length;
			for(i = 0; i < inputFieldsCount ; i++){
				if(!isValidStr(inputFields[i].value)){
					alert('欄位名稱不可包含特殊字元 \" > < \' % ; &');
					return true;
				}
			}
		}		

		//add by Joshua 2014/11/10 特殊字元判斷
		function isValidStr(str){
			return !/[%&\[\]\\';|\\"<>]/g.test(str);
		}
		
		$(document).ready(function(){	
			/*改變在header的標題(會員管理)顏色為已選取的樣式*/
			//$("#excel_upload").hide();
			//alert(get_param("bid"));
			
			/// 1.取得上個網頁用GET傳進來的參數 ///
			
			// 用來儲存上個網頁用get傳進來的數值。		
			//mid = get_param("mid");			
			menu_date = get_param("menuDate");	
			schoolId = get_param("schoolId");
			schoolName = get_param("schoolName");
			
			//檢查是否有抓取到參數
			if(menu_date== "get_param_ERROR"||schoolId== "get_param_ERROR"||schoolName== "get_param_ERROR")
			{
				alert ("讀取GET參數發生錯誤！");
				return 0;
			}
			//mid = parseInt(mid);				//把mid從字串轉回數字
			schoolName = decodeURI(decodeURI(schoolName));	//因為菜單名稱為中文所以在這邊解碼
			var sTitle="["+schoolName+"]<b>本日所有調味料維護</b>"
			$("#sTitle").append(sTitle);
			/// 2.查詢該日期的菜色資訊，並進行顯示 ///
			query_oneday_menu();
			
			/// 3.查詢該日期的調味料資料，並進行顯示 ///
			query_seasoning();
			//show_sensoning();
			
		});
	</script>
	</head>
	<body>
	<!-- 四點設計套版 -->
	<div class="contents-title">食材資料維護</div>
		<div class="contents-wrap">
			<input class="btn btn-primary" style="margin: 0 0 10px 0" type="button" align="right" value="回上一頁" onclick="history.go(-1);return false"/>
	        <div class="section-wrap has-border">
	          <div class="form-horizontal kitchen-info">         	
				<div id="tabs">
					<ul>
						<li><a href="#menu">食材資料維護</a></li>
						<li><a href="#seasoning_modify">調味料資料維護</a></li>
					</ul>
					
					<div id="menu">
					</div>
				
					<div id="seasoning_modify" class="TAB_TY_D">
						<table class="table table-bordered table-striped" width="100%" >
							<thead>
								<tr>
									<td colspan="2" align="center">
									<span id="sTitle"></span>
									</td>
								</tr>
							</thead>
							<tr>
								<td colspan="2" align="right">
									<input class="btn btn-primary" style="margin:0" type="button" id="addGmpSrc" class="addGmpSrcClass" value="增加調味料" onclick="add_seasoning()">	
								</td>
							</tr>
							
							<tr id="gmpSrcTable_{0}">
								<td colspan="2">
									<div id="seasoning" class="TAB_TY_D"></div>
								<div align="center"><input class="btn btn-primary" style="margin-top: 8px" type="button" id="just_id" value="確認修改" onclick="update_seasonings()">	</div>
								</td>
							</tr>
							
						</table>	
					</div>
				</div>
	          </div>
	        </div><!-- End of .section-wrap -->        
	    </div>
       
	<!-- 舊團膳套版 -->
	<div style="display:none">
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
		<div class="TITLE_TXT flo_l">食材資料維護</div>
	</div>
		<div align="right"  class="BT_IN_BBTER">
		<input type="button" align="right" value="回上一頁" onclick="history.go(-1);return false"/>
		</div>
		<div id="tabs">
			<ul>
				<li><a href="#menu">食材資料維護</a></li>
				<li><a href="#seasoning_modify">調味料資料維護</a></li>
			</ul>
			
			<div id="menu">
			
			</div>
			
			
			<div id="seasoning_modify" class="TAB_TY_D">
			
			
				<table class="" width="100%" >
					<tr>
						<td colspan="2" align="center" bgcolor="#FFF">
						<span id="sTitle"></span>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFF" colspan="2" align="right" class="BT_IN_BBTER">
							<input type="button" id="addGmpSrc" class="addGmpSrcClass" value="增加調味料" onclick="add_seasoning()">	
						</td>
					</tr>
					
					<tr bgcolor="#FFF" id="gmpSrcTable_{0}">
						<td colspan="2">
							<div id="seasoning" class="TAB_TY_D"></div>
						<div align="center" class="BT_IN_BBTER"><input  type="button" id="just_id" value="確認修改" onclick="update_seasonings()">	</div>
						</td>
					</tr>
					
				</table>	
				
			</div>
		</div>
	</div>
	</body>
</html>

