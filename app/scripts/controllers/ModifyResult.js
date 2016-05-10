
/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('ModifyResultCtrl', function($scope,MyVar,$stateParams,$http,$location) {
  //	console.log($stateParams);
  //	return false;
  	$scope.Projid = $stateParams.currentPid;
  	$scope.ProjName = $stateParams.currentProj;
  	$scope.quesnum = $stateParams.quesnum;
  	$scope.state = $stateParams.state;
  	$scope.name = $stateParams.name;
  	$scope.thisProject = $stateParams.thisProject;
    
  $scope.assigneds = [];
  $scope.assigned = "無";
  $scope.hidden5 =false;

  $scope.important = {};



  console.log($stateParams.redmineIssueId);
	$scope.pre_date = "";
	$scope.number = ""
	$scope.who = ""


  if($scope.state!="已送審")
  {
    $scope.btncancel = "取消";
    $scope.justview = false;
    $scope.hidden4 = true;
  }else
  {
    console.log("test");
    $scope.btncancel = "返回";
    $scope.justview = true;
    $scope.hidden4 = false;
  }


	$scope.result = function (x){
		var date = new Date(x);
		date.setDate(date.getDate()-14);
		$scope.pre_date = date.getFullYear()+'/'+(date.getMonth()+1)+'/'+date.getDate();
		return $scope.pre_date;
	}	
  $scope.selectedName = [];
  $scope.selectYetName = [];
  $scope.selectYetNameID = [];
  var edit = []
  setData($stateParams.redmineIssueId);
  function setData(IssueId){
      $http(
                 {
                     method: 'GET',
                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/results/'+IssueId
                } 
             ).then(function (response)  { 
                      var result = response.data;
                      console.log(result);
                      $scope.resultBackup = response.data;
                      $scope.SecretLv = result.securityclass;
                      $scope.needSubmit = result.submit;       
                      $scope.upload = result.upload;
                      $scope.name = result.resultname;
                      $scope.deadline = result.pre_sent;
                      $scope.number = result.resultid;
                      $scope.act_finish = result.act_finish;
                      $scope.assigned = result.assigned;
                      $scope.version = result.version;
                      $scope.redmineIssues = result.redmineIssues;
                      $scope.important.SecretLv = result.securityclass;
                      $scope.important.deadline = result.pre_sent;
                      $scope.important.name = result.resultname;
                      $scope.important.number = result.resultid;

                      select(result.editor);
                      function select(editor){
                          edit = editor.split(',');
                      }
                      if($scope.state!="已送審"){

                        if(result.redmineIssueId!=null&&result.redmineIssueId!="")
                          {

                            checkIssueClose(result.redmineIssueId);                            
                          }
                      }
                       getmember();


                    },
                    function (err) {
                      if(err.status==409)
                        alert("上傳devopsDB失敗");
                        console.log(err);
                        console.log('this is a error');
                   }
        );
    

  }

  function checkIssueClose(Issue){
    console.log(Issue)
    $http(
    {
           method: 'GET',
           url: 'http://'+MyVar.redmineApiUrl+'/issues/'+Issue+'.json?key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9'
        } 
      ).then(function (response){
       //  console.log("here");
        console.log(response);
        if(response.data.issue.status.name=="New"||response.data.issue.status.name=="待送出審查")
        {

          $scope.justview = false;
          
        }else{

            if(response.data.issue.status.name=="Closed")
              $scope.hidden5 =true;
            console.log("cant fix");
            $scope.btncancel = "返回";
            $scope.justview = true;
            $scope.hidden4 = false;
        }
      },function (err){
        console.log(err);
      }); 
  }


  $scope.SecretLv = "普通級";
	$scope.subjects = [
	'普通級','密級','機密級'
  	];
    $scope.needSubmit = '是';
	$scope.submits = [
	'是','否'
  	];
  	$scope.upload = '是';
	$scope.uploads = [
		'是','否'
  	];




	$scope.dropboxitemselected2 = function(item){
		$scope.needSubmit = item;
		return false;
	};
	$scope.dropboxitemselected3 = function(item){
		$scope.upload = item;
		return false;
	};

	$scope.dropboxitemselected4 = function(item){
		$scope.assigned = item;
		return false;
	};

	$scope.dropboxitemselected = function(item){
		$scope.SecretLv = item;
		return false;
	};

	$scope.left = function(item){
		if($scope.multipleSelect != undefined){
			var key = Object.keys(item).length;
			for(var i=0;i<key;i++){
				$scope.selectedName.push(item[i].toString());
				var L = $scope.selectYetName.length;
				for(var x = 0;x<L;x++){
					if($scope.selectYetName[x]==item[i])
						$scope.selectYetName.splice(x,1);
				}
			}
			$scope.multipleSelect = undefined;
		}
	}
	$scope.right = function(item){
		if($scope.multipleSelect1 != undefined){
			var key = Object.keys(item).length;
			for(var i=0;i<key;i++){
				$scope.selectYetName.push(item[i].toString());
				var L = $scope.selectedName.length;
					for(var x = 0;x<L;x++){
					if($scope.selectedName[x]==item[i])
						$scope.selectedName.splice(x,1);
				}
			}
			$scope.multipleSelect1 = undefined;
		}
	}


	function getmember(){
 		$http(
                {
                     method: 'GET',
                     url: 'http://'+MyVar.redmineApiUrl+'/projects/'+$scope.Projid+'/memberships.json?limit=100&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', 
               }
                             ).then(function (response) {
                                        var objects = response.data.memberships;
                                        var key = Object.keys(objects).length;
                                        for(var i=0;i<key;i++){
                                          var find = 0
                                        	for(t=0;t<Object.keys(edit).length;t++)  
                                          {  
                                            if(objects[i].user.name==edit[t])
                                            {  
                                              $scope.selectedName.push(objects[i].user.name);
                                              find = 1;
                                              var L = edit.length;
                                              for(var x = 0;x<L;x++){
                                                if(edit[x]==objects[i].user.name)
                                                  edit.splice(x,1);
                                              }
                                              break;
                                            }
                                          }
                                          if(find==0)
                                          {
                                            $scope.selectYetName.push(objects[i].user.name);
                                            $scope.selectYetNameID.push({'name':objects[i].user.name.toString(),'id':objects[i].user.id.toString()});
                                          }

                                        	$scope.assigneds.push(objects[i].user.name);
                                        }
									    // console.log($scope.selectYetNameID);
                                              var L = edit.length;
                                              for(var x = 0;x<L;x++){
                                                if(x>0)
                                                  $scope.who = $scope.who+','+edit[x];
                                                else
                                                  $scope.who = edit[x];  
                                              }
                                                                	
                                    }, 
                                    function (err) {
                                        if(err.status==409)
                                            alert("同步失敗");
                                        console.log(err);
                                        console.log('this is a error');
                                   }
                    )
        };

    function checkdate(x) {
    	var re = /^(\d{4})(\/|-)(\d{1,2})(\/|-)(\d{1,2})$/;

	
      if(regs = x.match(re)) {
        console.log(regs);
        // year value between 1902 and 2016
        if(regs[1] < 1902 ) {
          return true;
        }
        
        // month value between 1 and 12
        if(regs[3] < 1 || regs[3] > 12) {
          return true;
        }
        // day value between 1 and 31
        if(regs[5] > 0) {
        	if(regs[3]==2||regs[3]==02)
        	{
        		//console.log("in");
        		if(regs[1]%4==0){
        			if(regs[5]>29)
        				return true;
        		}else{
        		if(regs[5]>28)
        			return true;
				}
        	}else if(regs[3]==4||regs[3]==6||regs[3]==9||regs[3]==11)
        	{
        		if(regs[5]>30)
        			return true;
      		}else 
      		{
      			if(regs[5]>31)
      				return true;
      		}
        }
      } else {
      //  alert("Invalid date format: " + x);
        return true;
      }
      var now = new Date();
      var day = new Date(x);
      if(now > day){
      	alert("不能今天以前");
      	return true;
      }
	return false;    	
}	 
    $scope.submit = function() {

    	
    	if($scope.number==undefined||$scope.number==""){
    	//	console.log($scope.number);
    		alert("成果編號必填");
    		return false;
		}	
		if($scope.number.length<13||$scope.number.length>14){
			alert("成果編號長度為13~14個字");
    		return false;	
		}
    	if($scope.name==undefined||$scope.name==""){
    		alert("成果名稱必填");
    		return false;
    	}
      	if(checkdate($scope.deadline)){
    		alert("預計交付日期錯誤");
    		return false;
    	}

    	if($scope.assigned=="無"){
    		alert("承辦人不可以為無");
    		return false;
    	}
    	if(($scope.selectedName.length == 0)&&$scope.who==""){
    		alert("撰稿人不可以都沒有");
    		return false;
    	}
    	var id ;
    	for(var i=0;i<Object.keys($scope.selectYetNameID).length;i++)
    		if($scope.selectYetNameID[i].name==$scope.assigned)
    			id = $scope.selectYetNameID[i].id;

    	var finalEditor = $scope.selectedName+','+$scope.who;
      
      var json = '{"projid":'+$scope.Projid+
      ',"resultid":"'+$scope.number+
      '","resultname":"'+$scope.name+
      '","submit":"'+$scope.needSubmit+
      '","upload":"'+$scope.upload+
      '","securityclass":"'+$scope.SecretLv+
      '","editor":"'+finalEditor+
      '","assigned":"'+$scope.assigned+
      '","pre_sent":"'+$scope.deadline+
      '","pre_veriify":"'+$scope.pre_date+
      '","version":"'+$scope.version+
      '","act_finish":"'+
      '","disable":'+false+
      '}';



      updateDevopsDB(json);


    }
	function updateDevopsDB(json){
		

	    $http(
                 {
                     method: 'PATCH',
                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/results/'+$stateParams.redmineIssueId,
                     headers: { 
                     	'cache-control': 'no-cache',
					    'content-type': 'application/json',
					
					 },
					   
					 data: JSON.parse(json),
					 json: true 
                 }
             ).then(function (response) {
                      //  createVersion(JSON.parse(data));
                      alert("修改成功");      

                      modifyLog(JSON.parse(json));
                     
                      
                    }, 
                    function (err) {
                    	if(err.status==409)
                    		alert("上傳devopsDB失敗");
                        console.log(err);
                        console.log('this is a error');
                    
                   }
        );
	}


  function changestate(ProjState){                                          
      var json = '{"state":"'+ProjState+'"}';
      $http(
                  {
                       method: 'PATCH',
                       url: 'http://'+MyVar.BackApiUrl+'/TodoService/projects/'+$scope.thisProject,
                       headers: { 
                          'cache-control': 'no-cache',
                          'content-type': 'application/json',
                      
                       },                                   
                       data: JSON.parse(json),
                       json: true 
                   }
               ).then(function (response) {
                          console.log("狀態改變");
                          console.log(response.data);
                      }, 
                      function (err) {
                          if(err.status==409)
                              alert("同步失敗");
                          console.log(err);
                          console.log('this is a error');
                     }
      )

  }

	function modifyLog(response){
		console.log(response);
		console.log($scope.resultBackup);

    if(response.resultname!=$scope.resultBackup.resultname)
      setVerDate(2,response.resultname,$scope.resultBackup.resultname);
  
    if(response.resultid!=$scope.resultBackup.resultid)
      setVerDate(1,response.resultid,$scope.resultBackup.resultid);
  
    if(response.pre_sent!=$scope.resultBackup.pre_sent)
      setVerDate(3,response.pre_sent,$scope.resultBackup.pre_sent);
  
    if(response.securityclass!=$scope.resultBackup.securityclass)
      setVerDate(4,response.securityclass,$scope.resultBackup.securityclass);
    
    if(response.submit!=$scope.resultBackup.submit)
      setVerDate(5,response.submit,$scope.resultBackup.submit);
   
    if(response.upload!=$scope.resultBackup.upload)
      setVerDate(6,response.upload,$scope.resultBackup.upload);
    
    if(response.editor!=$scope.resultBackup.editor)
      setVerDate(7,response.editor,$scope.resultBackup.editor);
  
    if(response.assigned!=$scope.resultBackup.assigned)
      setVerDate(8,response.assigned,$scope.resultBackup.assigned);
  
  //  if(response.assigned!=$scope.resultBackup.assigned)
  //    setVerDate(9,response.assigned,$scope.resultBackup.assigned);
  



    if($scope.important.SecretLv==$scope.SecretLv&&$scope.important.deadline==$scope.deadline&&$scope.important.name==$scope.name&&$scope.important.number==$scope.number)
      $location.path('/dashboard/ResultList');
    else
    {
      if(confirm("因已修改到重要欄位，需要再將成果清單送出審查，請點按「將成果清單送出審查")){
        changestate("編輯中且未送審");
        $location.path('/dashboard/ResultList');
      }
    }
	}

  function setVerDate(index,now,old){
    var time = new Date();
    var filed;
    switch(index){
      case 1:
        filed = "成果編號";
      break
      case 2:
        filed = "成果名稱";
      break
      case 3:
        filed = "預計交付日";
      break
      case 4:
        filed = "機密等級";
      break
      case 5:
        filed = "是否交付";
      break
      case 6:
        filed = "是否上傳";
      break
      case 7:
        filed = "撰稿人";
      break
      case 8:
        filed = "承辦人";
      break      
    }
    var json = '{"date":"'+time.getFullYear()+'/'+(time.getMonth()+1)+'/'+time.getDate()+' '+time.getHours()+':'+time.getMinutes()+'",'+
               '"editor":"'+$scope.loginname+'",'+
               '"projectid":"'+$scope.thisProject+'",'+
               '"premodify":"'+old+'",'+
               '"aftermodify":"'+now+'",'+
               '"modifyresult":"'+$scope.name+'",'+
               '"modifyfield":"'+filed+                  
               '"}';
    console.log(json);
    $http(
       {
           method: 'POST',
           url: 'http://'+MyVar.BackApiUrl+'/TodoService/verdiary',
           headers: { 
              'cache-control': 'no-cache',
              'content-type': 'application/json',
          
            },                                   
           data: JSON.parse(json),
           json: true 
      }
    ).then(function (response){
      console.log(response);
    },function (err){
      console.log(err);
    });
  }




  $scope.reExmain = function(){
    console.log("exmain");
    $location.path('/dashboard/ResultVersionSet').search({resultId:$stateParams.redmineIssueId,thisDevopsProjectId:$scope.thisProject});

  }




});



