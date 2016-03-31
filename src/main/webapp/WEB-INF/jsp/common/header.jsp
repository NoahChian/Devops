<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.iii.ideas.catering_service.util.CateringServiceUtil" %>
<%
	String url=request.getContextPath();
	String domainName=request.getServerName();
	String headerId="HEADER";
	//if (!"lunch.twfoodtrace.org.tw".equals(domainName)){
	//	headerId="HEADER_DEMO";
	//}
	
	String uType = "";
	String uName = "";
	String account = "";
	String StrName = "";
	String roletype = "";
	String county = "";
	if (session.getAttribute("account") != null) {
		uType = (String) session.getAttribute("uType");
		uName = (String) session.getAttribute("uName");
		account = (String) session.getAttribute("account");
		StrName = (String) session.getAttribute("StrName");
		roletype = (String) session.getAttribute("roletype");
		county = (String) session.getAttribute("county");
	}

	// 判斷是國中國小還是幼兒園
	String uNameType = CateringServiceUtil.getSchoolLevel(uName);
	
	// 顯示『自設』還是『受供餐』
	String displayNaviWel = "";
	if( (roletype.equals("kCom") || roletype.equals("kSch")) && !"007".equals(uType) && !"009".equals(uType) ) {
		displayNaviWel = "食品業者 | 學校廚房";
	} else if( roletype.equals("kSch") && "007".equals(uType) ) {
		displayNaviWel = "受供餐學校";
	} else if("009".equals(uType)) {
		displayNaviWel = "員生消費合作社";
	} else if(roletype.equals("kGov1") || roletype.equals("kGov2")) {
		displayNaviWel = "主管機關";
	} else if(roletype.equals("kSys") || roletype.equals("admin")) {
		displayNaviWel = "系統管理員";
	} else if(uNameType.equals("SENIOR_HIGH_SCHOOL")){  //20151020 shine add 增加高中及大專身份判斷
		displayNaviWel = "高級中學";
	} 
	if(uType.equals("101")){  
		displayNaviWel = "大專院校";
	}else if(uType.equals("102")){
		displayNaviWel = "統包商業者";
	}else if(uType.equals("103")){
		displayNaviWel = "餐廳業者";
	}
%>
<body>
<header>
    <div class="container-fluid">
      <div class="row">
        <div class="col-xs-6">
          <a href="/frontend/" class="logo">校園食材登錄平臺 <small>Campus Food Ingredients Registration Platform</small></a>
        </div>
          
        <div class="col-xs-6 text-right">
          <ul class="signin">
            <li class="news">
              <a href="#" onclick="news()">公告訊息</a>
            </li>
            <%
				//加入非他校供應的判斷 20140501 KC 
				if (!roletype.equals("")) {
			%>
			<li class="signin-industry dropdown">
              <a href="#" class="dropdown-toggle" style="text-align:left;" data-toggle="dropdown">【<%=displayNaviWel%>】<br><%=StrName%> 您好! <i class="fa fa-caret-down"></i></a>
              <div class="dropdown-menu">
                <form action="<%=request.getContextPath()%>/web/main/changePass/" method="post" id="changePassForm" name="changePassForm">
                  <a href="http://175.98.115.58/docs/SchoolQueryAccount.pdf" target="_blank" class="btn btn-primary" style="display:none !important">操作教學檔下載</a>
                  <a href="#" onclick="changePassDiv()" class="btn btn-primary">修改密碼</a>
                  <!--  修改密碼 -->
					<div id="changePass" class="NAV_ZONE_B" style="display: none; margin: 10px 0 0 0;">
						<%-- <form action="<%=request.getContextPath()%>/web/main/changePass/" method="post" id="changePassF_new" name="changePassF_new"> --%>
							<input type="hidden" name="logintype" value="kitchen">
					
							<div class="LOGIN_US_PS" style="margin-left: 15px;">
								舊密碼： <input class="user max255" name="old" style="width: 110px; line-height:0" type="password">
							</div>
							<div class="LOGIN_US_PS" style="margin-left: 15px;">
								新密碼： <input class="user max255" name="newpass" style="width: 110px; line-height:0" type="password" data-bind="value: password, valueUpdate: 'keyup', css: { accept: allPass() }">
							</div>
							<div class="LOGIN_US_PS">
								確認密碼： <input class="user max255" name="newCheck" style="width: 110px; line-height:0" type="password" data-bind="value: pwdConfirm, valueUpdate: 'keyup', css: { accept: match() }">
							</div>
							<ul class="rules">
						        <li data-bind="css: { pass: pwdRule1() }">至少一個英文字母</li>
						        <li data-bind="css: { pass: pwdRule2() }">至少一個數字</li>
						        <li data-bind="css: { pass: pwdRule3() }">至少8個字元</li>
						    </ul>
							<div class="GOBK_BBT" style="line-height:0;">
								<a href="#" onclick="cancelChangePass()" style="padding: 16px 9px;">取 消</a>
								<a id="changePassSubmit" href="javascript:{changePassForm.submit();}" style="padding: 16px 9px; display:none;">送出</a>
							</div>
						<!-- </form> -->
					</div>
                  <a href="#" onclick="logout()" class="btn btn-primary">登 出</a>
                </form>
              </div>
            </li>
            <%
				}
          	%>
          	<%
				//	int userType = (Integer) session.getAttribute("usertype");
				if (!roletype.equals("kCom") && !roletype.equals("kSch") && !roletype.equals("kGov1") && !roletype.equals("kGov2") && !roletype.equals("admin") && !roletype.equals("kSys") && !roletype.equals("kSHOP") && account.equals("")) {
			%>
            <li class="signin-industry dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">登 入 <i class="fa fa-caret-down"></i></a>
              <div class="dropdown-menu">
                <form action="<%=request.getContextPath()%>/web/main/login/" method="post" id="loginInForm" name="loginInForm">
                  <input type="hidden" name="logintype" value="kitchen">	
                  <input type="text" name="usename" class="form-control" placeholder="帳號" />
                  <input type="password" name="userpwd" class="form-control" placeholder="密碼" />
                  <!-- <input type="submit" class="btn btn-primary" value="登入" /> -->
                  <a href="javascript:loginIn();" class="btn btn-primary">登 入</a>
                </form>
              </div>
            </li>
          	<%
				}
          	%>
          	<li style="display:none;">
          	  <div id="dialogNews" title="公告訊息">
			  	<table id="tableNews" class="table-bordered table-striped" width="100%"></table>
			  </div>
			</li>
            <li class="authorities-login dropdown" style="display:none;">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">主管機關登入 <i class="fa fa-caret-down"></i></a>
              <div class="dropdown-menu">
                <form action="<%=request.getContextPath()%>/web/main/login/" method="post" id="loginInForm2" name="loginInForm2">
                  <input type="hidden" name="logintype" value="govern">
                  <input type="text" name="usename" class="form-control" placeholder="帳號" />
                  <input type="password" name="userpwd" class="form-control" placeholder="密碼" />
                  <!-- <input onclick="javascript:{loginIn();loginInForm2.submit();}" type="submit" class="btn btn-primary" value="登入" /> -->
                  <a href="javascript:{loginIn();loginInForm2.submit();}" class="btn btn-primary">登 入</a>
                </form>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div><!-- End of .container-fluid -->
  </header>
  </body>
	<%-- <div id="WRAPPER">

		<div id="<%=headerId%>" onclick="location.href='<%=url%>/web/main/';">
			<!--  <div class="HEADER_TXT">團膳系統</div> -->
		</div>
	</div> --%>