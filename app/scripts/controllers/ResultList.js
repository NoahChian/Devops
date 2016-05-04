/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('ResultListCtrl', function($scope, $location,MyVar,$http,$stateParams  ) {
   // console.log($stateParams);
    $scope.results = [];
    $scope.Projid = $stateParams.currentPid;
    $scope.ProjNam = $stateParams.currentProj;
    $scope.pre_finishdate = $stateParams.pre_finishdate;
    console.log( $stateParams.pre_finishdate);
    $scope.ProjState = $stateParams.state;
    $scope.QuesNum = [];
    $scope.version = "1.0";

    
    var QA = "";
  	var member = [];

    run();

    function run(){
     // console.log(MyVar.url);
          $http(
                     {
                         method: 'GET',
                         url: 'http://'+MyVar.BackApiUrl+'/TodoService/results/search/findByProjid?projid='+$stateParams.currentPid, 
                     }).then(function (response) {        
                        //console.log();
                        if(!(Object.getOwnPropertyNames(response.data).length === 0)){
                          var len = Object.keys(response.data._embedded.results).length;  
                          //console.log(response.data._embedded.results);
                          var object = response.data._embedded.results;
                          for(var i=0;i<len;i++){                             //找尋每一筆成果
                              $scope.results[i] = object[i];
                            //  console.log($scope.results[i]);
                              if($scope.results[i].act_finish=="")          //如果實際完成日是空白  則更新他
                                update(i);    
  //                              getTicket(i);                                   //得到審查單編號
                          }
                        }
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    });

    };

    function update(i){
     //   var key = Object.keys($scope.results).length;
        var status = 14;
      //  for(var i=0;i<key;i++){
     //   console.log("update "+i);
           $http(
                 {
                     method: 'GET',
                     url: 'http://'+MyVar.redmineApiUrl+'/issues.json?project_id='+$scope.results[i].projid+'&status_id='+status+'&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', 
                 }
             ).then(function (response) {
                    //    console.log("update"+i);  
                        //if()
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    }
             ) 

        //}
        
    }    


  $scope.submitResultTicket= function(){
    console.log("here");
    if(confirm("是否確認送出審查?送出後不可再修改！")){
      console.log("yes");
      checkQA();
      if(QA=="")
      {  
        alert("專案未建立所QA角色成員，請先建立專案的所QA再執行送出審查");
        return false;
      }
      var json = '{"issue":{"project_id":'+$scope.Projid+
        ',"subject":"「'+ $scope.ProjNam +'」成果清單'+'V'+$scope.version+'送審'+
        '","assigned_to_id":'+QA+
        ',"tracker_id":6'+
        ',"custom_fields":[{"id":46,"value":"'+$scope.version+
        '"}],"description":"'+'A4FG1422穿戴式互動體驗科技計畫」成果清單網址'+
        '<br>http://xxx.xxx.xxx.xxx/xxxxxx'+
        '<br>審查重點：'+
        '<br>成果清單建立，係依據簽約版計畫書、契約或合作備忘錄。'+
        '<br>◎成果名稱是否與成果「類別」歸屬相符->依據本會技術、原型、著作成果保管辦法'+
        '<br>◎成果歸屬->依據雙方簽訂之契約'+
        '<br>◎科專計畫簽約用印前參閱黃皮書；簽約用印後參閱綠皮書。'+
        '<br>請特別注意成果編碼之第１碼，N→非本會所有／I→本會所有／C→雙方共有。'+
        '<br>成果類別代碼(成果編碼第11碼)之認定，請參閱「技術、原型、著作成果保管辦法」之規定。'+
        '","status_id":15'+
        '}}';
        //console.log(JSON.parse(json));
      
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
      ).then(function (response){
                    console.log($scope);
/*                    var json = '{"projid":"'+obj_devops[i].id+'",}';
                    $http(
                                {
                                     method: 'PATCH',
                                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/'+,
                                     headers: { 
                                        'cache-control': 'no-cache',
                                        'content-type': 'application/json',
                                    
                                     },                                   
                                     data: JSON.parse(bulksave),
                                     json: true 
                                 }
                             ).then(function (response) {
                                   //     console.log(response.data);
                                        var len = Object.keys(response.data).length;  
                                        for (var i=0; i<len; i++) {
                                                $scope.obj[i] = response.data[i];
                                        }                      
                                        backup_devops = $scope.obj; 
                                        secondconnect();
                                    }, 
                                    function (err) {
                                        if(err.status==409)
                                            alert("同步失敗");
                                        console.log(err);
                                        console.log('this is a error');
                                   }
                    )
  */      



    //    location.reload();
      },function (err){
        console.log(err);
      });
      

    }else{
      console.log("no");
    }
  }

  function checkQA(){
   
 //   console.log(member);
    var key = Object.keys(member).length;
    for(var i=0;i<key;i++){
      var key2 = Object.keys(member[i].roles).length;
      for(var j=0;j<key2;j++)
      {
        if(member[i].roles[j].name=="所QA")
        {  
          QA=member[i].user.id;
          break;
        }
      }
    }
  }

    getmember(); 
    function getmember(){
        $http(
                {
                     method: 'GET',
                     url: 'http://'+MyVar.redmineApiUrl+'/projects/'+$scope.Projid+'/memberships.json?key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', 
                }
            ).then(function (response) {                                   
                                        member = response.data.memberships;                      
                                    }, 
                                    function (err) {
                                        if(err.status==409)
                                            alert("同步失敗");
                                        console.log(err);
                                        console.log('this is a error');
                                   }
                )
    };
    getResultlistissue();
    function getResultlistissue(){

        $http(
                 {
                     method: 'GET',
                     url: 'http://'+MyVar.redmineApiUrl+'/issues.json?project_id='+$scope.Projid+'&status_id=*&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', 
                 }
             ).then(function (response) {
             // console.log("in");
                        if(response.data.total_count!=0){       //如果有找到任何issue
                            for(var x=0;x<response.data.total_count;x++){
                                if(response.data.issues[x].tracker.name=="成果清單審查單"){       //如果有issue是叫做成果清單審查單
                                   // $scope.QuesNum[y] = response.data.issues[x];
                                   // y++;
                                   // console.log($scope.QuesNum);
                                    var k = Object.keys(response.data.issues[x].custom_fields).length;  //有幾個欄位
                                    //  console.log(k);  
                                    for(var j=0;j<k;j++){
                                        if(response.data.issues[x].custom_fields[j].name=="版次"){     
                                           $scope.QuesNum.push({version:response.data.issues[x].custom_fields[j].value,status:response.data.issues[x].status.name,id:response.data.issues[x].id});
                                           //console.log($stateParams.quesnum);
                                           //console.log(response.data.issues[x].id);
                                           
                                           if($stateParams.quesnum == response.data.issues[x].id)
                                           {
                                              $scope.version = response.data.issues[x].custom_fields[j].value;
                                              if(response.data.issues[x].status.name=="已審查待核決"||response.data.issues[x].status.name=="已送出待審查")
                                                {
                                                  $scope.ProjState = "已送審";
                                                }
                                              else if(response.data.issues[x].status.name=="已核決")
                                                {
                                                  $scope.ProjState = "已核決";
                                                }
                                              else if(response.data.issues[x].status.name=="已退回待修正")
                                              {
                                                $scope.ProjState = "已退回";
                                              }
                                                            
                                           } 
                                        }
                                    }
                                }
                            }
//                            console.log($scope.QuesNum);
                        }
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    }
             ) 

      
    }


    getTicket = function(i){
        console.log("connet db "+i);




    }

   

  });