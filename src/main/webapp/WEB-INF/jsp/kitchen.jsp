<%@page import="org.iii.ideas.catering_service.util.AuthenUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@include file="util.jsp" %> <!-- include js的自訂公用函式    20140219 KC -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js_ca/common.js"></script>
<script src="../../js_ca/kitchen.js"></script>
</head>
<body>
	<%
	String kitchenId = (String) session.getAttribute("account");
	String serverName=request.getServerName();
	String url=request.getContextPath();
	String uType = (String) session.getAttribute("uType");
	String title = "廚房";
	String logoTitle = "自設廚房";
	
	boolean isCollege = false;
	
	if(uType.equals("101")){
		isCollege = true;
		title = "學校";
		logoTitle = "";
	}else if(uType.equals("102")){
		isCollege = true;
		title = "統包商";
		logoTitle = "";
	}else if(uType.equals("103")){
		isCollege = true;
		title = "餐廳";
		logoTitle = "";
	}

%>
	<script>
var kitchenId =  <%=kitchenId%>;
</script>

<%
//統包商或餐廳不進入本頁, 直接導到菜單登錄
if(uType.equals("102") || uType.equals("103")){
	out.println("<script>preSchoolMenuButton('restaurantmenu')</script>");	
}
%>
	<!-- 四點設計套版 -->
	<div class="contents-title"><%=title%>基本資料管理</div> 
        <div class="contents-wrap">
        	<h5 class="section-head with-border"><%=title%>基本資料設定</h5>
	          <div class="section-wrap has-border kitchen-info">
	            <div class="container-fluid">
	              <div class="row">
	                <div class="col-xs-3">
	                  <h6><%=logoTitle%>logo圖 *</h6>
	                  <div class="kitchen-info__logo">
	                    <!-- <img src="../../images/icon_kitchen.png" alt="" /> -->
	                    
	                    <img name="kitchenpic_o" id="kitchenpic_o" src="/cateringservice/file/SHOW/kitchen" width="150" height="150" />
	                    <span name="kitchenpic" id="kitchenpic"><br>
	                    <h6>格式：jpg, png, gif。<br>大小：小於3MB。</h6>
	                    <input type="hidden" id="func" name="func" value="Kitchen" /> 
	                    <input type="hidden" id="kitchenType" name="kitchenType" value="" /> 
	                    <input type="hidden" id="Ownner" name="Ownner" value="" /> 
	                    <input type="hidden" id="CompanyId" name="CompanyId" value="" /> 
	                    
	                    <input type="file" id="file" name="file" />
	                    <span id='deleteFile' style='display:none;' >
	                    	<button class="btn btn-primary" value="刪除" onClick="deleteFile()">刪除目前<%=title%>LOGO圖</button>
	                    	<!-- <a href="#" onclick="deleteFile()">刪除</a> -->
	                    </span>
	                    <button class="btn btn-primary" value="上傳" onClick="upload()">上傳<%=title%>LOGO圖</button>
	                    	
	                    <!-- <label class="custom-uploader" for="upload-kitchen-logo">
	                    	 <input type="button" value="上傳" onclick="upload()" />
	                    </label> -->
	                    <!-- <input class="custom-uploader" type="file" id="upload-kitchen-logo" name="upload-kitchen-logo" /> -->
	                  </div>
	                </div>
	
	                <div class="col-xs-6">
	                  <div class="form-horizontal">
	                    <div class="form-group">
	                      <label class="col-xs-4 control-label">名稱</label>
	                      <div class="col-xs-8">
	                        <p class="form-control-static"><span  name="kname" id="kname"></p>
	                      </div>
	                    </div><!-- End of .form-group -->
	
	                    <div class="form-group" style="display:<%=isCollege?"none":""%>">
	                      <label id="comIDlabel"  class="col-xs-4 control-label">廚房編號</label>
	                      <div class="col-xs-8">
	                        <p class="form-control-static"><span  name="comID" id="comID"></p>
	                      </div>
	                    </div><!-- End of .form-group -->
	
	                    <div class="form-group">
	                      <label class="col-xs-4 control-label">地址 *</label>
	                      <div class="col-xs-8">
	                        <p class="form-control-static"><input class="max255" type="text" name="Address" id="Address" size="30" value=""></p>
	                      </div>
	                    </div><!-- End of .form-group -->
	
	                    <div class="form-group">
	                      <label class="col-xs-4 control-label">電話 *</label>
	                      <div class="col-xs-8">
	                        <p class="form-control-static"><input class="max255" type="text" name="Tel" id="Tel" size="30" value=""></p>
	                      </div>
	                    </div><!-- End of .form-group -->
	
	                    <div class="form-group">
	                      <label class="col-xs-4 control-label">傳真 *</label>
	                      <div class="col-xs-8">
	                        <p class="form-control-static"><input class="max255" type="text" name="Fax" id="Fax" size="30" value=""></p>
	                      </div>
	                    </div><!-- End of .form-group -->
	
	                    <div class="form-group">
	                      <label class="col-xs-4 control-label">餐食負責人 *</label>
	                      <div class="col-xs-8">
	                        <p class="form-control-static"><input class="max255" type="text" name="Nutritionist" id="Nutritionist" size="30" value=""></p>
	                      </div>
	                    </div><!-- End of .form-group -->
						
	                    <div class="form-group">
	                      <label class="col-xs-4 control-label">E-Mail*</label>
	                      <div class="col-xs-8">
	                        <p class="form-control-static"><input class="max255" type="text" name="Email" id="Email" size="30" value=""></p>
	                      </div>
	                    </div><!-- End of .form-group -->
	
						<div class="form-group">
	                      <label class="col-xs-4 control-label">督導主管 *</label>
	                      <div class="col-xs-8">
	                        <p class="form-control-static"><input class="max255" type="text" name="Manager" id="Manager" size="30" value=""></p>
	                      </div>
	                    </div><!-- End of .form-group -->
	                    
						<div class="form-group">
	                      <label class="col-xs-4 control-label">督導主管E-Mail *</label>
	                      <div class="col-xs-8">
	                        <p class="form-control-static"><input class="max255" type="text" name="ManagerEmail" id="ManagerEmail" size="30" value=""></p>
	                      </div>
	                    </div><!-- End of .form-group -->
	                    <div class="k005tr">
		                    <div class="form-group">
		                      <label class="col-xs-4 control-label">衛管人員</label>
		                      <div class="col-xs-8">
		                        <p class="form-control-static"><input class="max255" type="text" name="Chef" id="Chef" size="30" value=""></p>
		                      </div>
		                    </div><!-- End of .form-group -->
		                    
							<div class="form-group">
		                      <label class="col-xs-4 control-label">食品技師</label>
		                      <div class="col-xs-8">
		                        <p class="form-control-static"><input class="max255" type="text" name="Qualifier" id="Qualifier" size="30" value=""></p>
		                      </div>
		                    </div><!-- End of .form-group -->
	                    </div>
	                    <!-- <div class="form-group">
	                      <label class="col-xs-4 control-label">E-Mail</label>
	                      <div class="col-xs-8">
	                        備用 Template：點擊「更改」button 後直接切換為輸入模式
	                        
	                        <button class="btn btn-primary">
	                          <i class="fa fa-check"></i>
	                          確定
	                        </button>
	                        <input type="text" class="form-control" placeholder="Input value" />
	                       
	                      </div>
	                    </div> --><!-- End of .form-group -->
	
	                    <div class="form-group">
	                      <label class="col-xs-4 control-label">營養小常識</label>
	                      <div class="col-xs-8">
	                        <textarea name="Insurement" id="Insurement" class="form-control" rows="8" placeholder="250字"></textarea>
	                      </div>
	                    </div><!-- End of .form-group -->
	
	                    <div class="form-group">
	                      <div class="col-xs-8 col-xs-offset-4">
	                        <button class="btn btn-primary" value="儲存廚房基本資料" onClick="updateKitchen()">儲存更改資料</button>
	                      </div>
	                    </div><!-- End of .form-group -->
	                  </div><!-- End of .form-horizontal -->
	                </div>
	
	                <div class="col-xs-3">
	                  <!-- Form validation 預留空間 -->
	                </div>
	              </div><!-- End of .row -->
	            </div><!-- End of .container-fluid -->
	          </div><!-- End of .section-wrap -->
	          
	          <!-- 供餐申請記錄檢視 Modal -->
              <div class="modal fade" id="acceptSchoolKitchenModal" role="dialog">
                    <div class="modal-dialog">
                        <div class="modal-lg">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">                                                  
                                <div class="contents-title">供餐申請記錄檢視</div>
                            </div>
                            <div class="modal-body">
                                <span id="halfYearAskList" name="halfYearAskList">
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" data-dismiss="modal">確定</button>
                            </div>                         
                        </div>
                       </div>
                    </div>
              </div>
	          
	          <div style="display:<%=isCollege?"none":""%>">
	          <h5 id='offerschooltitle' class="section-head with-border">供餐對象(學校)設定</h5>
	          <div id='offerschool' class="section-wrap has-border kitchen-target">
	            <div class="container-fluid">
	              <div class="row">
	                <div class="col-xs-12">
	                  <table class="table table-bordered table-striped" style="display:none;">
	                    <thead>
	                      <tr>
	                        <th class="col-xs-2 text-left">學校代碼</th>
	                        <th class="col-xs-5 text-center">學校名稱</th>
	                        <th class="col-xs-2 text-right">供應人數(份數)</th>
	                        <th class="col-xs-3 text-right">選項</th>
	                      </tr>
	                    </thead>
	
	                    <tbody>
	                      <tr>
	                        <td class="text-left">014676</td>
	                        <td class="text-center">新北市深坑區深坑國民小學</td>
	                        <td class="text-right">1</td>
	                        <td class="text-right">
	                          <button class="btn btn-primary">
	                            <i class="fa fa-pencil"></i>
	                            更新
	                          </button>
	
	                          <button class="btn btn-primary">
	                            <i class="fa fa-trash-o"></i>
	                            刪除
	                          </button>
	                        </td>
	                      </tr>
	                    </tbody>
	                  </table>
	
	                  <div class="section-wrap has-border">
	                    <div class="form-horizontal">
	                      <div class="form-group">
	                        <label class="col-xs-2 control-label">縣市別</label>
	                        <div class="col-xs-4">
	                          <span name="dropdown_City" id="dropdown_City">
	                        </div>
	                        <label class="col-xs-2 control-label">市/區別</label>
	                        <div class="col-xs-4">
	                          <span name="dropdown_Area" id="dropdown_Area">
	                        </div>
	                      </div><!-- End of .form-group -->
	
	                      <div class="form-group">
	                        <label class="col-xs-2 control-label">學校名稱</label>
	                        <div class="col-xs-4">
	                          <span name="dropdown_School" id="dropdown_School">
	                        </div>
	                        <label class="col-xs-2 control-label">供應人數(份數)</label>
	                        <div class="col-xs-4">
	                          <input id="txtQuantity" type="text" class="form-control" style="width:50px; display:inline;" />人數(份數)
	                        </div>
	                      </div><!-- End of .form-group -->
	
	                      <div class="form-group">
	                        <div class="col-xs-4 col-xs-offset-2">
	                          <button class="btn btn-primary" name="btnAddSchool" id="btnAddSchool" value="新增學校" onclick="addSchool()">
	                            <i class="fa fa-plus-square"></i>
	                            新增學校
	                          </button>
	                        </div>
	                        <button class="btn btn-primary col-md-offset-2" name="btnqryHalfYearAcceptSchoolKitchen" id="btnqryHalfYearAcceptSchoolKitchen" value="供餐申請記錄" onclick="qryHalfYearAcceptSchoolKitchen()">
                                供餐申請記錄
                            </button>
	                      </div><!-- End of .form-group -->
	                    </div><!-- End of .form-horizontal -->
	                  </div><!-- End of .section-wrap -->
	                  <span id="schoolList" name="schoolList">
	                  
	                </div>
	              </div><!-- End of .row -->
	            </div><!-- End of .container-fluid -->
	          </div><!-- End of .section-wrap -->
	          <h5 id='' class="section-head with-border" style="margin-top:10px;">被供餐廚房清單</h5>
	          <div class="section-wrap has-border">
	          	<span id="schooled"></span>
	          </div>	
	          <!-- 顯示acceptSchoolKitchen中待審核的資料,只有當有待審核的資料才顯示表格 -->
	          <span id="acceptSchoolKitchenWaitApproveByKitchenIdList" name="acceptSchoolKitchenWaitApproveByKitchenIdList"></span>
	          
	          <h5 id="iframetitle" class="section-head with-border" style="display:none">校園菜單載入</h5>
	          <div id="iframe" class="section-wrap has-border kitchen-target" style="display:none">
	            <div class="container-fluid">
	              <div class="row">
	                <div class="col-xs-12">
	                  <table class="table table-bordered table-striped">
	                    <thead>
	                      <tr>
	                        <th class="col-xs-9 text-left">網址/程式碼</th>
	                        <th class="col-xs-3 text-center">使用說明</th>
	                      </tr>
	                    </thead>
	
	                    <tbody>
	                      <tr>
	                        <td class="text-left">
	                        	<textarea class="form-control" rows="3" id="schoolLink"></textarea>
	                        </td>
	                        <td class="text-center">複製左方網址，即可透過此連結直接前往學校菜單頁面。</td>
	                      </tr>
	                      <tr>
	                        <td class="text-left">
	                        	<textarea class="form-control" rows="6" id="schooliFrame"></textarea>
	                        </td>
	                        <td class="text-center">複製左方程式碼，將此段程式碼放置於校園網頁中即可嵌入。(全版面 830px*850px)</td>
	                      </tr>
	                      <tr>
	                        <td class="text-left">
	                        	<textarea class="form-control" rows="6" id="schoolSiFrame"></textarea>
	                        </td>
	                        <td class="text-center">複製左方程式碼，將此段程式碼放置於校園網頁中即可嵌入。(小版面 200px*400px)</td>
	                      </tr>
	                    </tbody>
	                  </table>

	                </div>
	              </div><!-- End of .row -->
	            </div><!-- End of .container-fluid -->
	          </div><!-- End of .section-wrap -->
</body>
</html>
