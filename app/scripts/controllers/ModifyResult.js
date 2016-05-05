
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

  console.log($stateParams.redmineIssueId);
	$scope.pre_date = "";
	$scope.number = ""
	$scope.who = ""
  if($scope.state!="已送審")
  { 
    $scope.hidden4 = true;
    $scope.hidden5 = true;
  }else
  {
    $scope.hidden4 = false;
    $scope.hidden5 = false;
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
  function setData(redmineIssueId){
      $http(
                 {
                     method: 'GET',
                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/results/search/findByRedmineIssueId?redmineIssueId='+redmineIssueId
                } 
             ).then(function (response)  { 
                      var result = response.data._embedded.results[0];
                      console.log(result);
                      $scope.SecretLv = result.securityclass;
                      $scope.needSubmit = result.submit;       
                      $scope.upload = result.upload;
                      $scope.name = result.resultname;
                      $scope.deadline = result.pre_sent;
                      $scope.number = result.resultid;
                      $scope.act_finish = result.act_finish;
                      select(result.editor);
                      function select(editor){
                          edit = editor.split(',');
                      }
                      getAssigned(redmineIssueId);
                      function getAssigned(redmineIssueId){
                      //  console.log("in");
                        $http(
                          {
                            method: 'GET',
                            url: 'http://'+MyVar.redmineApiUrl+'/issues/'+redmineIssueId+'.json?key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', 
                          }
                          ).then(function (response){
                              $scope.assigned = response.data.issue.assigned_to.name;
                              getmember();

                          },function (err){
                              console.log(err);
                          }); 
                      }
                    },
                    function (err) {
                      if(err.status==409)
                        alert("上傳devopsDB失敗");
                        console.log(err);
                        console.log('this is a error');
                   }
        );
    

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


  	
	$scope.assigneds = [];
	$scope.assigned = "無";

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

    	var json = '{"issue":{"project_id":'+$scope.Projid+
    		',"subject":"'+$scope.name+
    		'","assigned_to_id":'+id+
    		',"tracker_id":7'+
    		',"custom_fields":[{"id":46,"value":"v1.0"},'+//版本
    		'{"id":48,"value":"'+$scope.SecretLv+'"},'+//機密
    		'{"id":49,"value":"'+ $scope.needSubmit+'"},'+	//是否上傳	
    		'{"id":50,"value":"'+ $scope.upload+'"},'+//是否上傳
    		'{"id":47,"value":"'+ $scope.number+'"},'+//編號 
    		'{"id":51,"value":"'+ finalEditor+'"}'+//撰稿人
    		']}}';
   
   //	var json ='{"issue":{"project_id":206,"subject":"465q6","assigned_to_id":20,"tracker_id":7,"custom_fields":[{"id":46,"value":"v1.0"},{"id":48,"value":"機密級"},{"id":49,"value":"否"},{"id":50,"value":"是"},{"id":47,"value":"a1234567891011"},{"id":51,"value":"歐 世文"}]}}';

		     $http(
                {
                     method: 'POST',
                     url: 'http://'+MyVar.redmineApiUrl+'/issues.json?key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9',
                  
                      headers: {
					    'content-type': 'application/json, text/plain, * / *',
					 },
					 data: JSON.parse(json),
					 json: true ,
        	        }
                             ).then(function (response) {
                                     	//alert("新增成功");
                                     	insertDevopsDB(response.data.issue);
                                    }, 
                                    function (err) {
                                        if(err.status==409)
                                            alert("上傳redmine失敗");
                                        console.log(err);
                                        console.log('this is a error');
                                   }
                    )
        

/*    	console.log($scope.number);
    	console.log($scope.name);
    	console.log($scope.deadline);
    	console.log($scope.SecretLv);
    	console.log($scope.needSubmit);
    	console.log($scope.upload);
    	console.log($scope.pre_date);
    	console.log($scope.selectedName);
    	console.log($scope.who);
  */		


    }
	function insertDevopsDB(json){
		console.log(json);
		var data = '{"projid":'+json.project.id+
		',"redmineIssueId":'+json.id+
		',"resultid":"'+json.custom_fields[21].value+
		'","resultname":"'+json.subject+
		'","submit":"'+json.custom_fields[23].value+
		'","upload":"'+json.custom_fields[24].value+
		'","securityclass":"'+json.custom_fields[22].value+
		'","editor":"'+json.custom_fields[25].value+
		'","pre_sent":"'+$scope.deadline+
		'","pre_veriify":"'+$scope.pre_date+
		'","act_finish":"'+
		'","disable":'+false+
		'}';

	    $http(
                 {
                     method: 'POST',
                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/results',
                     headers: { 
                     	'cache-control': 'no-cache',
					    'content-type': 'application/json',
					
					 },
					   
					 data: JSON.parse(data),
					 json: true 
                 }
             ).then(function (response) {
                        createVersion(JSON.parse(data));
//                        alert("新增成功");      
 //						$location.path('/dashboard/ResultList');
                      
                    }, 
                    function (err) {
                    	if(err.status==409)
                    		alert("上傳devopsDB失敗");
                        console.log(err);
                        console.log('this is a error');
                    
                   }
        );
	}

	function createVersion(response){
		console.log(response);
		var time = new Date();
		var data = '{"version":"v1.0'+
		'","createtime":"'+time.getFullYear()+'/'+time.getMonth()+'/'+time.getDate()+' '+time.getHours()+':'+time.getMinutes()+
		'","editor":"'+response.editor+
		'","pre_sentdate":"'+response.pre_sent+
		'","projectid":'+response.projid+
		',"resultid":"'+response.resultid+
		'","resultname":"'+response.resultname+
		'","securityclass":"'+response.securityclass+
		'"}';
//		console.log(data);
	    $http(
                 {
                     method: 'POST',
                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/verfull',
                     headers: { 
                     	'cache-control': 'no-cache',
					    'content-type': 'application/json',
					
					 },
					   
					 data: JSON.parse(data),
					 json: true 
                 }
             ).then(function (response) {
                     //  console.log(response);
                          alert("新增成功");      
 						$location.path('/dashboard/ResultList');
                      
                    }, 
                    function (err) {
                    	if(err.status==409)
                    		alert("上傳devopsDB失敗");
                        console.log(err);
                        console.log('this is a error');
                    
                   }
        );

	}




});



