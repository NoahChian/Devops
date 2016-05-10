 /**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('ResultVersionSetCtrl', function($scope, $location,$http,MyVar,$stateParams) {
    $scope.project = {};
    $scope.result = {};
    $scope.issue = {};
    function getRedmineIssue(issueId){
        $http({
            method: 'GET',
            url: 'http://'+MyVar.redmineApiUrl+'/issues/'+issueId+'.json?&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9'
        }).then(function (response){
            console.log(response);
            $scope.issue = response.data.issue;
        },function (err){
            console.log(err);
        });
    }
    function setInit(resultId){
        $http({
            method: 'GET',
            url: 'http://'+MyVar.BackApiUrl+'/TodoService/results/'+resultId
        }).then(function (response){
            console.log(response.data);
            $scope.result = response.data;
            $scope.resultId = response.data.resultid;
            $scope.resultName = response.data.resultname;
            getProject(response.data.projid);
            getRedmineIssue(response.data.redmineIssueId);
        },function (err){
            console.log(err);
        });
    }

    function getProject(projectId){
        $http({
            method: 'GET',
            url: 'http://'+MyVar.redmineApiUrl+'/projects/'+projectId+'.json?key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9'
        }).then(function (response){
            console.log(response);
            $scope.project = response.data.project;
            $scope.projectName = response.data.project.name;
        },function (err){
            console.log(err);
        });
    }
    function checkdate(x) {
        if(x==null)
            return true;
        var re = /^(\d{4})(\/|-)(\d{1,2})(\/|-)(\d{1,2})$/;
        if(regs = x.match(re)) {
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

    function createRedmineIssue(json){
        $http({
            method: 'POST',
            url: 'http://'+MyVar.redmineApiUrl+'/issues.json?key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9',            
            headers: {
                'content-type': 'application/json, text/plain, * / *',
            },
            data: JSON.parse(json),
            json: true ,
        }).then(function (response){
            console.log($scope.result);
            $scope.result.redmineIssues = $scope.result.redmineIssues+','+response.data.issue.id;
            var jsonUpdata = '{"pre_sent":"'+$scope.preSentdate+'","pre_veriify":"'+$scope.preExmaindate+'","version":"'+$scope.newVersion+'","redmineIssueId":"'+response.data.issue.id+'","redmineIssues":"'+ $scope.result.redmineIssues+'"}';
            updataDevopsResult(jsonUpdata);
        },function (err){
            console.log(err);
        });
    }
    function updataDevopsResult(jsonUpdata){
        $http({
            method: 'PATCH',
            url: 'http://'+MyVar.BackApiUrl+'/TodoService/results/'+$stateParams.resultId,
            headers: { 
                'cache-control': 'no-cache',
                'content-type': 'application/json',
            },
            data: JSON.parse(jsonUpdata),
            json: true ,
        }).then(function (response){
            console.log(response);
            alert("已送出改版審查");
            $http({
                method: 'GET',
                url: 'http://'+MyVar.BackApiUrl+'/TodoService/projects/'+$stateParams.thisDevopsProjectId,
            }).then(function (response){
                var proj = response.data; 
                $location.path('dashboard/ResultList').search({currentProj:proj.projname,currentPid:proj.projid,state:proj.state,quesnum:proj.ques_number,thisProject:proj.id});
            },function (err){
                console.log(err);
            })
        },function (err){
            console.log(err);
        });
    }

    $scope.submit = function (){
        if(checkdate($scope.preSentdate)){
            alert("預計交付日期錯誤");
            return false;
        }
        if(checkdate($scope.preExmaindate)){
            alert("預計審查日期錯誤");
            return false;
        }
        var x1 = new Date($scope.preSentdate);
        var x2 = new Date($scope.preExmaindate); 
        if(x1<x2){
            alert("審查日期不可比交付日期晚");
            return false;
        }
        if($scope.newVersion < $scope.result.version){
           alert("版本不可以比前一版本小");
            return false;
        }
        //put redmine issue json text 
        var json = '{"issue":{"project_id":'+$scope.result.projid+  
        ',"subject":"'+$scope.result.resultname+
        '","assigned_to_id":'+$scope.issue.assigned_to.id+
        ',"tracker_id":7'+
        ',"custom_fields":[{"id":46,"value":"'+$scope.newVersion+'"},'+//版本
        '{"id":48,"value":"'+$scope.result.securityclass+'"},'+//機密
        '{"id":49,"value":"'+ $scope.result.submit+'"},'+  //是否上傳  
        '{"id":50,"value":"'+ $scope.result.upload+'"},'+//是否上傳
        '{"id":47,"value":"'+ $scope.result.resultid+'"},'+//編號 
        '{"id":51,"value":"'+ $scope.result.editor+'"}'+//撰稿人
        ']}}';
        console.log(json);
        createRedmineIssue(json); 
    }
    $scope.cancel = function(){
        console.log($stateParams.thisDevopsProjectId);
            $http({
                method: 'GET',
                url: 'http://'+MyVar.BackApiUrl+'/TodoService/projects/'+$stateParams.thisDevopsProjectId,
            }).then(function (response){
                console.log(response);
                var proj = response.data; 
                $location.path('dashboard/ResultList').search({currentProj:proj.projname,currentPid:proj.projid,state:proj.state,quesnum:proj.ques_number,thisProject:proj.id});
            },function (err){
                console.log(err);
            })
    }


    setInit($stateParams.resultId);
  });