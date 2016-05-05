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
    $scope.id = $stateParams.thisProject;
    $scope.ProjState = $stateParams.state;
   // console.log($stateParams.state);
    $scope.quesnum =  $stateParams.quesnum;

    $scope.QuesNum = [];
    $scope.version = "1.0";
    $scope.hidden =true;
    $scope.hidden2 =true;
    $scope.hidden3 =false;
    var QA = [];
  	var member = [];
    var lastversiondate="";
    run();


    $scope.showemail=function(){
      alert('{subject":"[成果提醒]您擔任成果'+ $scope.results[0].resultid +'之撰稿人，應於'+$scope.results[0].pre_sent+'前送出審查，請注意送出時限！'+
        '","assigned_to_id":"'+$scope.results[0].editor+
        '","description":"'+$scope.results[0].editor+'同仁您好：'+
        '<br>　　提醒您，您擔任成果'+$scope.results[0].resultid+'之撰稿人，'+
        '<br>此成果應於'+$scope.results[0].pre_sent+'前送出審查，請注意時限！'+
        '<br>' +
        '<br>◎撰寫格式及模版，請參考「所內公告文件/品質服務/表單」。'+
        '<br>◎若撰寫格式有任何困難，請洽詢中心規格審查人：陳木百睿,'+
        '<br>◎若撰寫內容有任何困難，請洽詢專案負責人：陳巧盈'+
        '<br>◎若有任何其他困難，請洽詢品推小組：古靜怡'+
        '<br>' +
        '<br>您負責之成果明細資訊如下：'+
        '<br>專案代號：'+$scope.Projid+
        '<br>專案名稱：'+$scope.ProjNam+
        '<br>成果編號：'+$scope.results[0].resultid+
        '<br>成果名稱：'+$scope.results[0].resultname+
        '<br>撰稿人：'+$scope.results[0].editor+
        '<br>預計交付日：'+$scope.results[0].pre_sent+
        '<br>應送出審查日：'+$scope.results[0].pre_veriify+
        '"'+
        '}}'
      );
        var json = '{"state":"已送出郵件"}';                                            
        $http(
                    {
                         method: 'PATCH',
                         url: 'http://'+MyVar.BackApiUrl+'/TodoService/projects/'+$scope.id,
                         headers: { 
                            'cache-control': 'no-cache',
                            'content-type': 'application/json',
                        
                         },                                   
                         data: JSON.parse(json),
                         json: true 
                     }
                 ).then(function (response) {
                            console.log(response.data);
                          

                        }, 
                        function (err) {
                            if(err.status==409)
                                alert("同步失敗");
                            console.log(err);
                            console.log('this is a error');
                       }
        )
    };
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
                              console.log($scope.results[i]);
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
      if(QA[0]==null)
      {  
        alert("專案未建立所QA角色成員，請先建立專案的所QA再執行送出審查");
        return false;
      }
      console.log($scope.QuesNum[0]);
      if($scope.QuesNum[0] != null)
      { 
        console.log($scope.version);
        var first = $scope.version.split(".")[0];
        var second = $scope.version.split(".")[1];
        second = (parseInt(second)+1);
        var thisyear = new Date().getFullYear();
        
        if(thisyear > lastversiondate.getFullYear())
        {
          first =(parseInt(first)+1);
          second = 0;
        }
        $scope.version = first+'.'+second;
       // $scope.version = $scope.version.match("(.*).")[1]+($scope.version.match("(.*).")[1]+ 1);
      }
      var gettime = new Date();
      var json = '{"issue":{"project_id":'+$scope.Projid+
        ',"subject":"「'+ $scope.ProjNam +'」成果清單'+'V'+$scope.version+'送審'+
        '","assigned_to_id":'+QA[0]+
        ',"tracker_id":6'+
        ',"custom_fields":[{"id":46,"value":"'+$scope.version+
        '"}],"description":"「'+ $scope.ProjNam +'」成果清單網址'+
        '<br>http://'+MyVar.FrontUrl+'/#/dashboard/ResultList?currentProj='+$stateParams.currentProj+'&state='+$stateParams.state+'&quesnum='+$stateParams.quesnum+
        '&currentPid='+$stateParams.currentPid+'&thisProject='+$stateParams.thisProject+
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
      var mail = '{subject":"「'+ $scope.ProjNam +'」成果清單'+'V'+$scope.version+'待送審'+
        '","assigned_to_id":"'+QA[1]+
        '","description":"'+QA[1]+'同仁您好：'+
          '<br>　您有一筆成果清單審查單待處理，請登入「IDEAS專案管理系統」進行審查，謝謝！'+
          '<br>'+
          '<br>'+$scope.ProjNam+'專案成果清單網址：http://'+ MyVar.FrontUrl+'/#/dashboard/ResultList?currentProj='+$stateParams.currentProj+'&state='+$stateParams.state+'&quesnum='+$stateParams.quesnum+
          '&currentPid='+$stateParams.currentPid+'&thisProject='+$stateParams.thisProject+
          '<br>「IDEAS專案管理系統」網址：http://'+MyVar.FrontUrl+
          '<br>審查單送出時間：'+ gettime +
          '<br> '+
          '<br>如系統無法連線或無法登入，請洽MIS陳旻瑋(#2140)。'+
          '<br>如操作上有問題，請洽DevOps推動小組蔡明達(#2247)/歐世文(#2268)。'+
        '"'+
        '}}';


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
                    console.log(response);
                    var json = '{"ques_number":'+response.data.issue.id+',"state":"尚未寄出郵件"}';
                    $stateParams.quesnum = response.data.issue.id;
                    console.log($scope.id);
                    $http(
                                {
                                     method: 'PATCH',
                                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/projects/'+$scope.id,
                                     headers: { 
                                        'cache-control': 'no-cache',
                                        'content-type': 'application/json',
                                    
                                     },                                   
                                     data: JSON.parse(json),
                                     json: true 
                                 }
                             ).then(function (response) {
                                        console.log(response.data);
                                        getResultlistissue();
                                        alert(mail);

                                    }, 
                                    function (err) {
                                        if(err.status==409)
                                            alert("同步失敗");
                                        console.log(err);
                                        console.log('this is a error');
                                   }
                    )
     



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
          QA[0]=member[i].user.id;
          QA[1]=member[i].user.name;
          
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
         var status = '*';
         var responseSum =[];
         searchIssue(0);
         function searchIssue(offset){
            $http(
                 {
                     method: 'GET',
                     url: 'http://'+MyVar.redmineApiUrl+'/issues.json?project_id='+$scope.Projid+'&status_id='+status+'&offset='+offset+'&limit=100&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', 
                 }
             ).then(function (response) {
                       if((offset+100) < response.data.total_count)
                       {
                                for(var x=offset;x<offset+100;x++){
                                    responseSum[offset+x] = response.data.issues[x];
                                }
                            searchIssue(offset+100);    
                       }else{
                          for(var x=offset;x<offset+response.data.total_count;x++){
                                    responseSum[offset+x] = response.data.issues[x];
                                }
                            updataState(responseSum);
                       }
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    }
            ); 
        }
        function updataState(responseSum){
          $scope.QuesNum = [];
            if(Object.keys(responseSum).length!=0){       //如果有找到任何issue
           //   console.log(response.data.total_count);
                for(var x=0;x<Object.keys(responseSum).length;x++){
                 // console.log("1"+responseSum[x].tracker.name);
                    if(responseSum[x].tracker.name=="成果清單審查單"){       //如果有issue是叫做成果清單審查單
                       // $scope.QuesNum[y] = response.data.issues[x];
                       // y++;
                       // console.log($scope.QuesNum);
                        var k = Object.keys(responseSum[x].custom_fields).length;  //有幾個欄位
                        //  console.log(k);  
                        for(var j=0;j<k;j++){
                            if(responseSum[x].custom_fields[j].name=="版次"){     
                               $scope.QuesNum.push({version:responseSum[x].custom_fields[j].value,status:responseSum[x].status.name,id:responseSum[x].id});
                               //console.log($stateParams.quesnum);
                               //console.log(response.data.issues[x].id);
                               
                               if($stateParams.quesnum == responseSum[x].id)
                               {
                                 // console.log(response.data.issues[x]);
                                  lastversiondate = new Date(responseSum[x].updated_on.match("(.*)T")[1]);
                                 // console.log(lastversiondate);
                                  
                                  $scope.version = responseSum[x].custom_fields[j].value;
                                 // console.log("version change "+$stateParams.quesnum+":"+$scope.version);
                                  if(responseSum[x].status.name=="已審查待核決"||responseSum[x].status.name=="已送出待審查")
                                    {
                                      $scope.ProjState = "已送審";
                                      $scope.hidden =false;
                                      $scope.hidden2 =false;
                                    }
                                  else if(responseSum[x].status.name=="已核決")
                                    {
                                      $scope.ProjState = "已核決";
                                      $scope.hidden =true;
                                      $scope.hidden2 =true;
                                      $scope.hidden3 =true;
                                    }
                                  else if(responseSum[x].status.name=="已退回待修正")
                                  {
                                    $scope.ProjState = "已退回";
                                    $scope.hidden =true;
                                    $scope.hidden2 =true;
                                  }
                                                
                               } 
                            }
                        }
                    }
                }
  //                            console.log($scope.QuesNum);
            }
        }     

      
    }


    getTicket = function(i){
        console.log("connet db "+i);



    }
    $scope.modifyResult = function (result){

      $location.path('/dashboard/ModifyResult').search({currentProj:$stateParams.currentProj,quesnum:$stateParams.quesnum,state:$scope.ProjState,currentPid:$stateParams.currentPid,thisProject:$stateParams.thisProject,redmineIssueId:result.redmineIssueId});
      console.log(result);
    }
   

  });