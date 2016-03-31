<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js_ca/common.js"></script>
<script>
	var MSG = new MsgsProcessing();
	//設置全域變數
	var table_count = 0; //記錄畫面的table總數量
	var table_status = new Array(); //紀錄table是否有刪除的情況
	var mid = ""; //紀錄Get傳進來的參數
	var name = ""; //紀錄Get傳進來的參數
	var dishId = ""; //紀錄GET傳進來的菜色ID
	var menuDate = ""; //紀錄GET傳進來的菜單日期

	/**
	2014/10/27  add by Joshua
	輸入欄位過濾掉特殊字元
	*/
// 	document.onchange = checkSpecialCharacters;
	function checkSpecialCharacters(){
		var inputFields = $("table[id^='table']").find("input[type=text]");
		var inputFieldsCount = $("table[id^='table']").find("input[type=text]").length;
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
	
	/*
	 * 2014/06/19 Ric 上傳菜色照片
	 */
	var client = new XMLHttpRequest();
	var tagname = "";
	function upload(menuDate, ingredientId, supplierCompanyId, lotNumber, stockDate, filetag, ingredientCategory) {
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
		formData.append("func", "inspect_v2|" + menuDate + "|" + ingredientId + "|" + supplierCompanyId + "|" + lotNumber + "|" + stockDate + "|" + ingredientCategory);
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
	function uploadOneFile(menuDate, ingredientId, supplierCompanyId, lotNumber, stockDate, filetag, ingredientCategory) {
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
			runFile.ingredientName[0] = $("#name" + filetag).val();
			runFile.menuDate[0] = menuDate;
			runFile.ingredientId[0] = ingredientId;
			runFile.supplierCompanyid[0] = supplierCompanyId;
			runFile.stockDate[0] = stockDate;
			runFile.lotNumber[0] = lotNumber;
			runFile.ingredientCategory[0] = ingredientCategory;
			uploadFileProcessing(runFile);
		} else {
			MSG.alertMsgs("check", "沒有選擇檔案", 0);
		}
	}
	function uploadFileProcessing(runFile) {
		MSG.alertMsgs("loading", "檔案檢查與上傳...", 0);
		var uploadMsgs = "";
		for (var i = 0; i < runFile.file.length; i++) {
			uploadMsgs += runFile.ingredientName[i] + ":" + upload(runFile.menuDate[i], runFile.ingredientId[i], runFile.supplierCompanyid[i], runFile.lotNumber[i], runFile.stockDate[i], runFile.file[i], runFile.ingredientCategory[i]) + "<br>";
		}
		$(".blockUIImgs").hide();
		$(".blockUIMsgs").html(uploadMsgs);
		$(".blockUIMsgs").after('<input type="button" onclick="$.unblockUI();location.reload();" value="確定">');
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
	 * 取得URL中用GET傳遞的bid參數
	 * 
	 * @param	要從URL取得的變數名稱
	 * @return	從URL取得的變數值
	 * @author	Eason
	 * @date	2013/12/16
	 */
	function get_param(arg_str) {

		var url = window.location.toString(); //取得當前網址
		var str = ""; //參數中等號左邊的值
		var str_value = ""; //參數中等號右邊的值

		if (url.indexOf("?") != -1) //如果網址有"?"符號
		{
			var ary = url.split("?")[1].split("&");
			//取得"?"右邊網址後利用"&"分割字串存入ary陣列 ["a=1","b=2","c=3"]
			for ( var i in ary) {
				//取得陣列長度去跑迴圈，如:網址有三個參數，則會跑三次
				str = ary[i].split("=")[0];
				//取得參數"="左邊的值存入str變數中
				if (str == arg_str) {
					str_value = ary[i].split("=")[1];
					return str_value;
				}
			}
			//如果都沒有拿到這參數，則回傳錯誤
			if (str_value == "") {
				str_value = "get_param_ERROR";
				return str_value;
			}
		}
	}

	/**
	 * 動態刪除食材欄位的資訊，但還不會真的更新資料庫，等到使用者點選確認修改才會一次把資料更新進後端資料庫
	 * 
	 * @param	欲刪除的table名稱
	 * @return	
	 * @author	Eason
	 * @date	2013/12/26
	 */
	function delete_table(table_num) {
		//alert(table_num);

		///1.隱藏table///
		$("#table" + table_num).hide();

		///2.將table的狀態設為已刪除///
		table_status[table_num] = 0;
	}

	/**
	 * 顯示傳入菜色的所有食材
	 * 
	 * @param	該菜色所有食材資訊的物件
	 * @return	
	 * @author	Eason
	 * @date	2013/12/17
	 */
	function show_ingredients(result_content) {
		//alert(result_content.resStatus);

		/// 對每一樣食材產生一個table來進顯示 ///
		for (var i = 0; i < result_content.ingredientsCount; i++) {
			//alert(result_content.ingredients[i].name);
			//20140722 Ric For Ingredient Property
			var check_gmbean = "";
			var check_gmcorn = "";
			var check_psfood = "";
			if (result_content.ingredients[i].ingredientAttribute.gmbean == "1") {
				check_gmbean = "checked";
			}
			if (result_content.ingredients[i].ingredientAttribute.gmcorn == "1") {
				check_gmcorn = "checked";
			}
			if (result_content.ingredients[i].ingredientAttribute.psfood == "1") {
				check_psfood = "checked";
			}
			var new_table = '<table class=\"table table-bordered table-striped\" width=\"100%\" id="table' + i + '">';
			new_table += "<tr>";
			new_table += '<td width="20%" align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>食材名稱</td>';
			new_table += '<td align="left" valign="middle" ><input  class="max255" id="name'+ i +'" type="text" size="30" value="' + result_content.ingredients[i].name + '"></td>';
			//產品名稱
			new_table += '<td width="20%" align="left" nowrap="nowrap" class="tmpV001">產品名稱</td>';
			new_table += '<td align="left" valign="middle" ><input  class="max255" id="productName'+ i +'" type="text" size="30" value="' + result_content.ingredients[i].productName + '"></td>';
			new_table += "</tr>";

			//產生供應商名稱的下拉式選單
			new_table += "<tr>";
			new_table += '<td align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>供應商名稱</td>';
			new_table += '<td ><select name="supply_name" id="supply_name'+ i +'" >';
			new_table += '<option>' + result_content.ingredients[i].supplyName + '</option>'; //預設欄位是廠商輸入的供應商名稱
			for ( var key in result_content.ingredients[i].supplierOption) {
				//選項中不再顯示將預設的供應商選項，避免重複
				if (result_content.ingredients[i].supplyName != result_content.ingredients[i].supplierOption[key].supplierName)
					new_table += '<option>' + result_content.ingredients[i].supplierOption[key].supplierName + '</option>';
			}
			new_table += "</select></td>";
			//製造商名稱
			new_table += '<td width="20%" align="left" nowrap="nowrap" class="tmpV001">製造商名稱</td>';
			new_table += '<td align="left" valign="middle">'
			////20140730 Ric  因應更版 改為顯示
			//+ '<input  id="brand'+ i +'" type="hidden" value="' + result_content.ingredients[i].brand + '">'
			+ '<input  class="max255" id="manufacturer'+ i +'" type="text" size="30" value="' + result_content.ingredients[i].manufacturer + '"></td>';

			////20140730 Ric  因應更版 改為顯示
			//new_table += '<td width="20%" align="left" nowrap="nowrap">品牌</td>';
			//new_table += '<td align="left" valign="middle">'
			//+ '<input  class="max255" id="brand'+ i +'" type="text" size="30" value="' + result_content.ingredients[i].brand + '"></td>';
			//更版修改結束
			new_table += "</tr>";

			//產生驗證標章的下拉式選單
			new_table += "<tr>";
			new_table += '<td align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>驗證標章</td>';
			new_table += '<td><select name="label"  id="label'+ i +'" >';
			new_table += '<option>' + result_content.ingredients[i].label + '</option>'; //預設欄位是廠商輸入的驗證標章
			for ( var key in result_content.ingredients[i].labelOption) {
				//選項中不再顯示將預設的驗證標章選項，避免重複
				if (result_content.ingredients[i].label != result_content.ingredients[i].labelOption[key])
					new_table += '<option>' + result_content.ingredients[i].labelOption[key] + '</option>';
			}
			new_table += "</select></td>";
			//驗證編號
			new_table += '<td width="20%" align="left" nowrap="nowrap">驗證編號</td>';
			new_table += '<td align="left" valign="middle"><input class="max45" id="authenticateId'+ i +'"  type="text" size="30" value="' + result_content.ingredients[i].authenticateId + '"></td>';
			new_table += "</tr>";

			new_table += "<tr>";
			new_table += '<td ><span style="color:red;float:right">*</span>進貨日期</td>';
			new_table += '<td ><input size="30" type="text" id="stock_date'+ i +'" name="productManufactureDate" class="dateAD dateMode" value="'+ result_content.ingredients[i].stockDate +'"></td>';
			//重量
			new_table += '<td width="20%" align="left" nowrap="nowrap" class="tmpV001">重量</td>';
			//20141013_Raymond_增加重量單位
			new_table += '<td align="left" valign="middle"><input style="width: 150px;" class="max10 numOnly" id="ingredientQuantity'+ i +'" type="text" size="30" value="' + result_content.ingredients[i].ingredientQuantity + '">';
			new_table += '<select name="unit" id="unit'+ table_count +'"><option>公斤</option></select></td>';
			////20140730 Ric  因應更版 更改位置	
			//new_table += '<td width="20%" align="left" nowrap="nowrap">批號</td>';
			//new_table += '<td align="left" valign="middle"><input class="max255" id="ingredientLotNum'+ i +'" type="text" size="30" value="' + result_content.ingredients[i].ingredientLotNum + '"></td>';
			//更版修改結束
			new_table += "</tr>";

			new_table += "<tr>";
			new_table += "<td>生產日期</td>";
			new_table += '<td><input size="30" type="text" id="product_date'+ i +'" name="productManufactureDate" class="dateAD max10" value="'+ result_content.ingredients[i].productDate +'"></td>';
			new_table += "<td>有效日期</td>";
			new_table += '<td><input size="30" type="text" id="valid_date'+ i +'" name="productManufactureDate" class="dateAD max10" value="'+ result_content.ingredients[i].validDate +'"></td>';
			new_table += "</tr>";
			////20140730 Ric  因應更版 更改位置		
			new_table += "<tr>";
			new_table += '<td width="20%" align="left" nowrap="nowrap">批號</td>';
			new_table += '<td align="left" valign="middle" colspan="3"><input class="max255" id="ingredientLotNum'+ i +'" type="text" size="30" value="' + result_content.ingredients[i].ingredientLotNum + '"></td>';
			new_table += "</tr>";

			new_table += "<tr>";
			new_table += '<td width="20%" align="left" nowrap="nowrap" class="tmpV001">產品屬性</td>';
			new_table += "<td align='left' valign='middle' colspan='3'>" + "<input type='checkbox' class = 'i_gmbean' id='i_gmbean" + i+ "'value='"+ result_content.ingredients[i].ingredientAttribute.gmbean+ "' "+ check_gmbean+ "> "
					+ "<label for='i_gmbean"+ i+ "'>基改黃豆</label> " + "<input type='checkbox' class = 'i_gmcorn' id='i_gmcorn"+ i+ "'value='"+ result_content.ingredients[i].ingredientAttribute.gmcorn+ "' "+ check_gmcorn+ "> "
					+ "<label for='i_gmcorn"+ i+ "'>基改玉米</label> " + "<input type='checkbox' class = 'i_psfood' id='i_psfood"+ i+ "'value='"+ result_content.ingredients[i].ingredientAttribute.psfood+ "' "+ check_psfood+ "> "
					+ "<label for='i_psfood"+ i+ "'>加工品</label> ";
			new_table += '</td>';
			new_table += "</tr>";

			new_table += "<tr>";
			new_table += '<td width="20%" align="left" nowrap="nowrap">檢驗報告</td>';
			new_table += '<td align="left" valign="middle" colspan="3">';
			new_table += "<input style='float: left;' type= 'file' id='file"+ i+ "' name='file"+ i+ "' />" + "<span style='line-height:30px;'>" + "<a class=\"btn btn-primary\" style=\"margin:0\" href='javascript:uploadOneFile(\"" + menuDate + "\",\"" + result_content.ingredients[i].ingredientId
					+ "\",\"" + result_content.ingredients[i].supplierCompanyId + "\",\"" + result_content.ingredients[i].ingredientLotNum + "\",\"" + result_content.ingredients[i].stockDate + "\",\"" + i + "\",\"ingredient\");'>上傳檔案</a> ";
			if (result_content.ingredients[i].reportFileName) {
				new_table += "<a class=\"btn btn-primary\" style=\"margin:0\" href='"+ result_content.ingredients[i].reportFileName+ "' target='_blank'>下載檔案</a> " + "<a class=\"btn btn-primary\" style=\"margin:0\" href='javascript:deleteFile(\"" + menuDate + "\",\"" + result_content.ingredients[i].ingredientId + "\",\""
						+ result_content.ingredients[i].supplierCompanyId + "\",\"" + result_content.ingredients[i].ingredientLotNum + "\",\"" + result_content.ingredients[i].stockDate + "\");'>刪除</a></span>";
			}

			new_table += '</td>';
			new_table += "</tr>";

			new_table += "<tr><td></td>";
			new_table += '<td align="right" colspan="3"><input class=\"btn btn-primary\" style=\"margin:0\" type="button" value="刪除本項食材" onclick="delete_table(' + i + ')"/></td>';
			new_table += "</tr>";

			new_table += "</table>";

			//用一個不顯示的元素來儲存ingredientBatchId，以方便之後進行新增、刪除、修改使用
			new_table += '<input id="ingredientBatchId'+ i +'" type="hidden" value="' + result_content.ingredients[i].ingredientBatchId + '">' + '<input id="ingredientId'+ i +'" type="hidden" value="' + result_content.ingredients[i].ingredientId + '">';

			//將動態產生的table加上去
			$("#ingredients").append(new_table);
			// 設定check box內容值
			$('input[type="checkbox"]').change(function() {
				this.value = this.checked ? 1 : 0;
			});
			//載入 calendar 套件
			var selector_stock_date = "#stock_date" + i;
			var selector_product_date = "#product_date" + i;
			var selector_valid_date = "#valid_date" + i;
			$(selector_stock_date).datepicker({
				dateFormat : "yy/mm/dd"
			}); //食材進貨日期
			$(selector_product_date).datepicker({
				dateFormat : "yy/mm/dd"
			}); //食材生產日期
			$(selector_valid_date).datepicker({
				dateFormat : "yy/mm/dd"
			}); //食材有效日期
			/*
			 *20140630 Ric
			 * Setting maxlength and readonly
			 */
			setInputLength();
			//記錄整個畫面的table數量與狀態
			table_status[table_count] = 1; //1代表這張TABLE存再沒有被刪除 
			//alert(table_status[table_count]);
			table_count++;

			//alert(table_count);
		}

	}

	/**
	 * 新增一個食材的空白table，讓業者進行食材的新增填寫
	 * 
	 * @param	
	 * @return	
	 * @author	Eason
	 * @date	2013/12/18
	 */
	function add_ingredients() {
		//alert("add_ingredients");
		var supply_name_options = query_supply_name();
		//如果廠商查詢就出錯了，就不顯示了
		if (supply_name_options == 0) {
			alert("查詢供應商列表錯誤，無法新增食材");
			return 0;
		}

		//alert(result_content.ingredients[i].name);
		var new_table = '<table class=\"table table-bordered table-striped\" width="100%" id="table' + table_count + '">';
		new_table += "<tr>";
		new_table += '<td width="20%" align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>食材名稱</td>';
		new_table += '<td align="left" valign="middle" ><input class="max255" id="name'+ table_count +'"  type="text" size="30"></td>';
		//產品名稱
		new_table += '<td width="20%" align="left" nowrap="nowrap" class="tmpV001">產品名稱</td>';
		new_table += '<td align="left" valign="middle" ><input  class="max255" id="productName'+ table_count +'" type="text" size="30" ></td>';
		new_table += "</tr>";

		//產生供應商名稱的下拉式選單
		new_table += "<tr>";
		new_table += '<td align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>供應商名稱</td>';
		new_table += '<td ><select name="supply_name"  id="supply_name'+ table_count +'" >';

		for ( var key in supply_name_options)
			new_table += '<option>' + supply_name_options[key].supplyName + '</option>';

		new_table += "</select></td>";
		//製造商名稱
		new_table += '<td width="20%" align="left" nowrap="nowrap"  class="tmpV001">製造商名稱</td>';
		new_table += '<td align="left" valign="middle">'
		////20140730 Ric  因應更版 改為顯示
		//	 + '<input  id="brand'+ table_count +'" type="hidden" value="">'
		+ '<input  class="max255" id="manufacturer'+ table_count +'" type="text" size="30" ></td>';
		////20140730 Ric  因應更版 改為顯示
		//new_table += '<td width="20%" align="left" nowrap="nowrap">品牌</td>';
		//new_table += '<td align="left" valign="middle">'
		// + '<input  class="max255" id="brand'+ table_count +'" type="text" size="30" value=""></td>';
		//更版修改結束
		new_table += "</tr>";

		//產生驗證標章的下拉式選單
		new_table += "<tr>";
		new_table += '<td align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>驗證標章</td>';
		new_table += '<td ><select name="label" id="label'+ table_count +'" >';
		new_table += '<option></option>'; //目前新增食材的驗證標章選項先寫死只有吉園圃、產銷履歷、有機、CAS、GMP，預設是空值
		new_table += '<option>CAS</option>';
		new_table += '<option>CAS_ORGANIC</option>';
		new_table += '<option>GAP</option>';
		new_table += '<option>GMP</option>';
		new_table += '<option>HALAL</option>';
		new_table += '<option>HACCP</option>';
		new_table += '<option>HEALTH</option>';
		new_table += '<option>ISO22000</option>';
		new_table += '<option>ISO9001</option>';
		new_table += '<option>MILK</option>';
		new_table += '<option>TAP</option>';
		new_table += '<option>SN</option>';
		//更新驗證標章清單20140903
		new_table += "</select></td>";
		new_table += '<td width="20%" align="left" nowrap="nowrap">驗證編號</td>';
		new_table += '<td align="left" valign="middle"><input class="max45" id="authenticateId'+ table_count +'" type="text" size="30"></td>';
		new_table += "</tr>";

		new_table += "<tr>";
		new_table += '<td bgcolor="#ececec"><span style="color:red;float:right">*</span>進貨日期</td>';
		new_table += '<td bgcolor="#ececec"><input type="Rtext" size="30" id="stock_date'+ table_count +'" name="productManufactureDate" class="dateAD dateMode"></td>';
		//20141013_Raymond_增加重量單位
		new_table += '<td width="20%" align="left" nowrap="nowrap"  class="tmpV001">重量</td>';
		new_table += '<td align="left" valign="middle"><input style="width: 150px;" class="max255 numOnly" id="ingredientQuantity'+ table_count +'" type="text" size="30">';
		new_table += '<select name="unit" id="unit'+ table_count +'"><option>公斤</option></select></td>';

		////20140730 Ric  因應更版 更改位置	
		//new_table += '<td width="20%" align="left" nowrap="nowrap">批號</td>';
		//new_table += '<td align="left" valign="middle"><input class="max255" id="ingredientLotNum'+ table_count +'" type="text" size="30" value=""></td>';
		//更版修改結束
		new_table += "</tr>";

		new_table += "<tr>";
		new_table += "<td>生產日期</td>";
		new_table += '<td><input type="text" size="30" id="product_date'+ table_count +'" name="productManufactureDate" class="dateAD max10"></td>';
		new_table += "<td>有效日期</td>";
		new_table += '<td><input type="text" size="30" id="valid_date'+ table_count +'" name="productManufactureDate" class="dateAD max10" ></td>';
		new_table += "</tr>";
		////20140730 Ric  因應更版 更改位置
		new_table += "<tr>";
		new_table += '<td width="20%" align="left" nowrap="nowrap">批號</td>';
		new_table += '<td align="left" valign="middle" colspan="3"><input id="ingredientLotNum'+ table_count +'" type="text" size="30"  class="max255"></td>';
		new_table += "</tr>";

		new_table += "<tr>";
		new_table += '<td width="20%" align="left" nowrap="nowrap"  class="tmpV001">產品屬性</td>';
		new_table += "<td align='left' valign='middle' colspan='3'>" + "<input type='checkbox' class = 'i_gmbean' id='i_gmbean" + table_count+ "'value='0'> " + "<label for='i_gmbean"+ table_count+ "'>基改黃豆</label> "
				+ "<input type='checkbox' class = 'i_gmcorn' id='i_gmcorn"+ table_count+ "'value='0'> " + "<label for='i_gmcorn"+ table_count+ "'>基改玉米</label> " + "<input type='checkbox' class = 'i_psfood' id='i_psfood"+ table_count+ "'value='0'> "
				+ "<label for='i_psfood"+ table_count+ "'>加工品</label> ";
		new_table += '</td>';
		new_table += "</tr>";
		/*
		new_table += "<tr>";
		new_table += '<td align="left" nowrap="nowrap">上傳檢驗報告</td>';
		new_table += '<td align="left" colspan="2"><input type="file" name="examFile">(上傳檔案格式為JPG、PDF檔)</td>';
		new_table += "</tr>";
		 */

		new_table += "<tr><td></td>";
		new_table += '<td align="right"  colspan="3"><input class=\"btn btn-primary\" style=\"margin:0\" type="button" value="刪除本項食材" onclick="delete_table(' + table_count + ')"/></td>';
		new_table += "</tr>";

		//新增食材的批號為空，用來當作新增的意思
		new_table += '<input id="ingredientBatchId'+ table_count +'" type="hidden" value="">';
		new_table += '<input id="ingredientId'+ table_count +'" type="hidden" value="">';

		new_table += "</br></table>";

		//用一個不顯示的元素來儲存ingredientBatchId，以方便之後進行新增、刪除、修改使用
		//新增食材的批號為空，用來當作新增的意思
		//new_table += '<input style="display:none" id="ingredientBatchId'+ table_count +'" type="text" value="">';

		//將動態產生的table加上去
		$("#ingredients").append(new_table);
		// 設定check box內容值
		$('input[type="checkbox"]').change(function() {
			this.value = this.checked ? 1 : 0;
		});
		//載入 calendar 套件
		var selector_stock_date = "#stock_date" + table_count;
		var selector_product_date = "#product_date" + table_count;
		var selector_valid_date = "#valid_date" + table_count;
		$(selector_stock_date).datepicker({
			dateFormat : "yy/mm/dd"
		}); //食材進貨日期
		$(selector_product_date).datepicker({
			dateFormat : "yy/mm/dd"
		}); //食材生產日期
		$(selector_valid_date).datepicker({
			dateFormat : "yy/mm/dd"
		}); //食材有效日期
		/*
		 *20140630 Ric
		 * Setting maxlength and readonly
		 */
		setInputLength();
		//記錄整個畫面的table數量與狀態
		table_status[table_count] = 1; //1代表這張TABLE存再沒有被刪除 
		//alert(table_status[table_count]);
		table_count++;

	}

	/**
	 * 更新菜色的食材資訊
	 * 
	 * @param	
	 * @return	
	 * @author	Eason
	 * @date	2013/12/18
	 */
	function update_ingredients() {
		if(checkSpecialCharacters()){
			return;
		}
		//用來存放食材資訊的陣列
		var ingredients_array = new Array();

		//alert($("#supply_name1").find("option:selected").text());

		/// 1.取得填寫的資料(不包含已經被刪除的table)並且檢查資料是否必填欄位都有填寫 ///
		for (var i = 0; i < table_count; i++) {
			//檢查這個table是否有被刪除
			if (table_status[i] == 1) {
				if (($("#product_date" + i)).val() != '') {
					if (!validDateFormat($("#product_date" + i))) { //日期驗證    2014/10/17 Joshua
						// 驗證不過就return
						return;
					}
				}
				if (($("#valid_date" + i)).val() != '') {
					if (!validDateFormat($("#valid_date" + i))) {
						// 驗證不過就return
						return;
					}
				}
				var ingredient = new Object(); //用來存放一個食材資訊的物件
				var ingredientAttribute = new Object(); //用來存放一個食材屬性的物件

				ingredient.name = $("#name" + i).val();
				if (ingredient.name == "") {
					//alert("未填寫第"+ (i +1)+"項食材的食材名稱!")
					alert("未填寫食材名稱!")
					return 0;
				}
				ingredient.productName = $("#productName" + i).val();
				ingredient.supplyName = $("#supply_name" + i).find("option:selected").text();

				ingredient.manufacturer = $("#manufacturer" + i).val();
				ingredient.brand = $("#brand" + i).val();
				/*if(ingredient.brand == "")
				{
					alert("未填寫第"+ (i +1)+"項食材的品牌!")
					return 0;
				}*/

				ingredient.label = $("#label" + i).find("option:selected").text();

				ingredient.authenticateId = $("#authenticateId" + i).val();
				//如果使用者有填驗證標章，則依定要填驗證編號
				if (ingredient.authenticateId == "" && ingredient.label != "") {
					alert("未填寫項食材的驗證編號!")
					return 0;
				}

				ingredient.stockDate = $("#stock_date" + i).val();
				if (ingredient.stockDate == "") {
					alert("未填寫食材的進貨日期!")
					return 0;
				}

				ingredient.productDate = $("#product_date" + i).val();

				ingredient.validDate = $("#valid_date" + i).val();

				ingredient.ingredientLotNum = $("#ingredientLotNum" + i).val();
				/*
				if(ingredient.ingredientLotNum == "")
				{
					alert("未填寫第"+ (i +1)+"項食材的批號!")
					return 0;
				}*/

				ingredient.ingredientQuantity = $("#ingredientQuantity" + i).val();
				ingredient.ingredientBatchId = $("#ingredientBatchId" + i).val();
				ingredient.ingredientId = $("#ingredientId" + i).val();

				//處理食材屬性 
				ingredientAttribute.gmbean = $("#i_gmbean" + i).val();
				ingredientAttribute.gmcorn = $("#i_gmcorn" + i).val();
				ingredientAttribute.psfood = $("#i_psfood" + i).val();
				ingredient.ingredientAttribute = ingredientAttribute;
				//加入該菜色的食材資訊中
				ingredients_array.push(ingredient);

				//table_id = "table"+i;
				//var table = document.getElementById("table"+i);

				//name_value = $("#name"+i).val();		//食材名稱
			}
		}
		//alert(JSON.stringify(ingredients_array));
		/// 2.將要更新的資料傳回後台 ///

		var request_data = {
			"method" : "updateIngredientDetailv2",
			"args" : {
				"dishId" : dishId,
				"menuDate" : menuDate,
				"ingredientsCount" : ingredients_array.length,
				"ingredients" : ingredients_array
			}
		};
		//alert(JSON.stringify(request_data));

		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) { //API呼叫正確
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { //修改食材資訊成功

				alert("修改成功!");
				window.location.reload(); //重新整理畫面
			} else {
				alert("修改食材資訊發生錯誤，訊息為：" + result_content.msg);
				return 0;
			}
		} else {
			alert("修改食材資訊發生無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			return 0;
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
	function query_supply_name() {
		/// 1.查詢供應商名稱列表 ///
		var request_data = {
			"method" : "querySupplyOptionList",
			"args" : {}
		};

		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) { //API呼叫正確
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { //查詢供應商名稱正確
				/// 2.回傳供應商名稱列表 ///
				return result_content.supplyNameOption;

			} else {
				alert("查詢供應商名稱發生錯誤，訊息為：" + result_content.msg);
				return 0;
			}
		} else {
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			return 0;
		}

	}

	/**
	 * 查詢該菜色的所有食材並顯示
	 * 
	 * @return	
	 * @author	Eason
	 * @date	2014/01/06
	 */
	function query_ingredient_detail() {
		// alert(name);
		//alert(batchdataId);

		/// 1.顯示菜色的名稱 ///
		$("#get_name").text(name);

		/// 2.查詢菜色全部食材 ///
		var request_data = {
			"method" : "QueryIngredientDetailv2",
			"args" : {
				"dishId" : dishId,
				"menuDate" : menuDate
			}
		};

		var response_obj = call_rest_api(request_data);
		if (response_obj.result == 1) { //API呼叫正確
			var result_content = response_obj.result_content;
			if (result_content.resStatus == 1) { //查詢菜色食材正確
				/// 3.顯示查詢到的食材資訊 ///
				show_ingredients(result_content);

			} else {
				alert("查詢菜色食材發生錯誤，訊息為：" + result_content.msg);
				return 0;
			}
		} else {
			alert("無法預期的錯誤，錯誤訊息為：" + response_obj.error_msg);
			return 0;
		}

	}

	$(document).ready(function() {

		/// 1.取得上個網頁用GET傳進來的參數 ///
		// 用來儲存上個網頁用get傳進來的數值。		
		name = get_param("name");
		//mid = get_param("mid");
		dishId = get_param("dishId");
		menuDate = get_param("menuDate");

		//檢查是否有抓取到參數
		if (dishId == "get_param_ERROR" || name == "get_param_ERROR" || menuDate == "get_param_ERROR") {
			alert("讀取GET參數發生錯誤！");
			return 0;
		}

		name = decodeURI(decodeURI(name)); //因為菜單名稱為中文所以在這邊解碼
		//mid = parseInt(mid);				//把mid從字串轉回數字

		/// 2.顯示已經輸入的食材 ///

		query_ingredient_detail();

	});
</script>
</head>


<body>
<!-- 四點設計套版 -->
	<div class="contents-title">食材資料維護</div>
		<div class="contents-wrap">
	        <div class="section-wrap has-border">
	        	<div style="float: left">
					<input class="btn btn-primary" style="margin:0 0 10px" type="button" value="回上一頁" onclick="history.go(-1);return false" />
				</div> 
				<div style="float: right">
					<input class="btn btn-primary" style="margin:0 0 10px" type="button" id="add0" value="增加食材" onclick="add_ingredients()">
				</div>
	          <div class="form-horizontal kitchen-info">         	
				<table class="table table-bordered table-striped" width="100%">
					<thead>
						<tr>
							<td align="left" nowrap="nowrap">菜色</td>
							<td id="get_name" align="left"></td>
						</tr>
					</thead>
					<tr id="sourceTable_{0}">
						<td colspan="2">
							<div id="ingredients"></div>
							<div align="center">
								<input class="btn btn-primary" style="margin-top: 8px" type="button" id="just_id" value="確認修改" onclick="update_ingredients()">
							</div> 
						</td>
					</tr>
				</table>
	          </div>
	        </div><!-- End of .section-wrap -->        
	    </div>
       
	<!-- 舊團膳套版 -->
	<div style="display:none">
	<div id="writeSource" class="TAB_TY_D">
		<!--  <div id="writeSource" style="display:none">-->
		<div id="MAIN_TITLE_BAR" class="GRE_ABR h_30px lh_30">
			<div class="TITLE_TXT flo_l">食材資料維護</div>
		</div>
		<table class="" width="100%">
			<tr>
				<td colspan="2" align="right">
					<div style="float: left" class="BT_IN_BBTER">
						<input type="button" id="add0" value="增加食材" onclick="add_ingredients()">
					</div>
					<div style="float: right" class="BT_IN_BBTER">
						<input type="button" value="回上一頁" onclick="history.go(-1);return false" />
					</div> <!-- 
						<div style="float: right">
							<input type="button" value="上一步" onclick="changeTags('writeProduct')">
						</div>
						 -->
				</td>
			</tr>
			<tr>
				<td align="left" nowrap="nowrap">菜色</td>
				<td id="get_name" align="left"></td>
			</tr>

			<tr id="sourceTable_{0}">
				<td colspan="2">
					<div id="ingredients"></div> <!--  
					<table width="100%">
						<tr>
							<td width="20%" align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>食材名稱</td>
							<td align="left" valign="middle" colspan="2"><input type="text" size="50" value="燕麥"></td>
						</tr>
						<tr>
							<td align="left" nowrap="nowrap"><span style="color:red;float:right">*</span>供應商名稱</td>
							<td align="left" colspan="2">
		            		<select>
		                   		<option >第一家食品公司</option>
		                   		<option >第二家食品公司</option>
		            		</select> 
							<span></span>
							</td>
						</tr>
						<tr>
							<td align="left" nowrap="nowrap">品牌</td>
							<td align="left" colspan="2">
							<input type="text" id="supplyCompanyName_{0}" name="supplyCompanyName_{0}" value="第一家">
							<input type="hidden" id="supplyCompanyId_{0}" name="supplyCompanyId_{0}" size="35" class="supplyCompanySelectNone">
							</td>
						</tr>
						<tr>
							<td align="left" nowrap="nowrap">驗證標章</td>
							<td align="left" colspan="2">
		          <select>
		               <option value=''></option>
		               <option value='HACCP'>CAS</option>
		               <option value='HACCP'>HACCP</option>
		          </select>
							</td>
						</tr>
						<tr>
							<td align="left" nowrap="nowrap">驗證編號</td>
							<td align="left" colspan="2">
							<input type="text" id="supplyCompanyName_{0}" name="supplyCompanyName_{0}" value="">
							<input type="hidden" id="supplyCompanyId_{0}" name="supplyCompanyId_{0}" size="35" class="supplyCompanySelectNone">
							</td>
						</tr>
						<tr>	
							<td>進貨日期</td>				
							<td>
							<input type="text" id="stock_date" name="productManufactureDate" class="dateAD">
							<span></span>
					    	</td>
						</tr>
						<tr>	
							<td>生產日期</td>				
							<td>
							<input type="text" id="product_date" name="productManufactureDate" class="dateAD">
							<span></span>
					    	</td>
						</tr>
						<tr>	
							<td>有效日期</td>				
							<td>
							<input type="text" id="valid_date" name="productManufactureDate" class="dateAD">
							<span></span>
					    	</td>
						</tr>
						<tr>	
							<td>批號</td>				
							<td>
							<input type="text" >
							<span></span>
					    	</td>
						</tr>
						<tr>
							<td align="left" nowrap="nowrap">上傳檢驗報告</td>
							<td align="left" colspan="2">
							<input type="file" name="examFile">(上傳檔案格式為JPG、PDF檔)
							</td>
						</tr>
		        
					</table>
					-->
					<div align="center" class="BT_IN_BBTER">
						<input type="button" id="just_id" value="確認修改" onclick="update_ingredients()">
					</div>
				</td>
			</tr>



		</table>

	</div>
	<script type="text/javascript">
	<!-- Justin: Add this for rendering on Firefox -->
		//if(myjQuery.browser.mozilla)
		//document.write('<br>');
	</script>
</div>
</body>
</html>