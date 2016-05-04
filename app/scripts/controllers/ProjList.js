 /**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('ProjListCtrl', function($scope, $location,$http,MyVar) {
    $scope.year = ["105"];
    $scope.obj=[];                      //uesd to show in html
    var obj_devops=[];                  //redmine projects
    var new_devops=[];                  //used to new when connet two db
    var update_devops=[];               //uesd to update when ...
    var backup_devops=[];               //used to back up show ,from the devops db    
    $scope.count = 0;
    $scope.offset = 0;
    $scope.findname = null;
    var lens= 0;  //proj_devops
    var lens2 =0;   //proj_redmine
    proj_devops();
    function proj_redmine(x) {
            console.log("search redmine projects ,offest = "+x);
             $http(
                 {
                     method: 'GET',
                     url: 'http://'+MyVar.redmineApiUrl+'/projects/owner-ben/memberships.json&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', 
                 }
             ).then(function (response) {
                     //   console.log(response);  
                        lens2 = response.data.total_count;
                        var len = Object.keys(response.data.projects).length;  
                        for (var i=x; i<len+x; i++) {
                         
                                obj_devops[i] = response.data.projects[i-x];
                 
                        }
                        if(lens2>(len+x))
                            proj_redmine(x+len);
                        else
                        {
                            compare();
                           // console.log(obj_devops);
                        }
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    }
             ) 
    }

    function proj_devops(obj_b){
        console.log('search devops projects');
        $http(
                 {
                     method: 'GET',
                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/projects',
               
                 }
             ).then(function (response) {
                        console.log('seach projects ok');
                      //  console.log(response);
                       if(Object.getOwnPropertyNames(response.data).length === 0)
                        return false;
                         var len = Object.keys(response.data._embedded.projects).length;  
                        for (var i=0; i<len; i++) {
                                $scope.obj[i] = response.data._embedded.projects[i];
                        }                      
                        backup_devops = $scope.obj; 
                        secondconnect();
                    }, 
                    function (err) {
                        if(err.status!=200)
                            alert("與DevOps連接失敗");
                        console.log(err);
                        console.log('this is a error2');                 
                   }
             );
    }
  
    $scope.connetdatabase = function(){         //update two database
        $scope.obj=[];
        backup_devops = []; //devop 
        obj_devops =[];     //redmine 
        new_devops =[];     //need to new 
        update_devops =[];      //after compare , need to update 
        console.log("start connet");
        $http(                                  // to get devops projects
                 {
                     method: 'GET',
                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/projects',       
               
                 }
             ).then(function (response) {
                    console.log('seach devops projects ok');
                //    console.log(response);
                if(Object.getOwnPropertyNames(response.data).length === 0)
                       { 
                        backup_devops = [];
                        proj_redmine(0);
                        lens = 0;
                        return false;
                       }
                     var len = Object.keys(response.data._embedded.projects).length;  
                    for (var i=0; i<len; i++) {
                            backup_devops[i] = response.data._embedded.projects[i];
                    }
                    lens = Object.keys(backup_devops).length;
                    proj_redmine(0);
           
                }, 
                function (err) {
                    if(err.status!=200)
                        alert("與DevOps連接失敗");
                    console.log(err);
                    console.log('this is a error');
                        
                }
            );
    }

    function compare(){
        console.log("start compare");
        var x=0,y=0;
        for (var i=0; i<lens2; i++) {                 //search proj_redmine and proj_devops
       //     console.log("compare "+i);
            var key = false;                            //if redmine has but devops no , new it    , key used to 
            if(obj_devops[i].custom_fields[6].value==1){  //check if it need to create list
                for(var j=0;j<lens;j++){               // if remine have and devops also too, update it.

                    if(backup_devops[j].projname == obj_devops[i].name && backup_devops[j].disable != true) //比對obj_devops[i],如果找到了devops裡有此專案,而且在devops上不是disable ,代表應該更新他
                    {

                        var s1 = (obj_devops[i].custom_fields[7].value==null)?(""):('"'+obj_devops[i].custom_fields[7].value+'"');
                        var s2 = (obj_devops[i].custom_fields[8].value==null)?(""):('"'+obj_devops[i].custom_fields[8].value+'"');
                       

                
                        console.log(obj_devops[i]);
                     //   console.log("find project , now to update ");
                        key = true;                         //remember we find project

                        update_devops[x] = '{"id":"'+backup_devops[j].id+'","state":"'+backup_devops[j].state+'","projname":"'+obj_devops[i].name+'","pre_sentdate":'+  //注意 id從我們的資料庫取得, projid從redmine上取得
                        s1+',"pre_finishdate":'+
                        s2+'}';

                        console.log(update_devops[x].state);
                        
                        x++;
                        break;
                    }
                }
                if(key==false){     //所有devops內都沒有  代表應該新建立
                //    console.log("new");

                    var s1 = (obj_devops[i].custom_fields[7].value==null)?'""':('"'+obj_devops[i].custom_fields[7].value+'"');
                    var s2 = (obj_devops[i].custom_fields[8].value==null)?'""':('"'+obj_devops[i].custom_fields[8].value+'"');
                

                    new_devops[y] = '{"projid":"'+obj_devops[i].id+'","projname":"'+obj_devops[i].name+'","pre_sentdate":'+
                    s1+',"pre_finishdate":'+
                    s2+'}';

                    y++;
                }
                key = false;  
            }
        }

       // console.log(update_devops);
       // console.log(new_devops);
        var bulksave = covert(update_devops,new_devops);
      //  console.log(bulksave);
        $http(
                                {
                                     method: 'POST',
                                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/bulksave',
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
        };

    function secondconnect(){
        var key = Object.keys($scope.obj).length;
       
        for(var i=0;i<key;i++){
            search(i);
        }
    }    

    function search(i){                 //搜查每個專案的版本１．０的成果清單審查單並顯示
         var status = 14;
        $http(
                 {
                     method: 'GET',
                     url: 'http://'+MyVar.redmineApiUrl+'/issues.json?project_id='+$scope.obj[i].projid+'&status_id='+status+'&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', 
                 }
             ).then(function (response) {
                        //console.log(response);  
                        if(response.data.total_count!=0){
                        //  console.log(response.data.total_count);  
                            for(var x=0;x<response.data.total_count;x++){
                                   // console.log(response.data.issues[x].tracker.name);  
                                if(response.data.issues[x].tracker.name=="成果清單審查單"){
                                    var k = Object.keys(response.data.issues[x].custom_fields).length;  //有幾個欄位
                                    //  console.log(k);  
                                    for(var j=0;j<k;j++){
                                      //  console.log(response.data.issues[x]);
                                        if(response.data.issues[x].custom_fields[j].name=="版次"&&response.data.issues[x].custom_fields[j].value=="1.0"){
                                           // console.log("find : i="+i);
                                           

                                            $http(
                                            {
                                                method: 'GET',
                                                url: 'http://'+MyVar.redmineApiUrl+'/issues/'+response.data.issues[x].id+'.json?include=journals&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9'
                                            }
                                                ).then(
                                                function (response2){
                                                  //  console.log(response2.data);
                                                    var k2 = Object.keys(response2.data.issue.journals).length;                     //有幾次修改歷程
                                                    for(var l=0;l<k2;l++){
                                                        var k3 = Object.keys(response2.data.issue.journals[l].details).length;      
                                                        for(var m=0;m<k3;m++){
                                                          //  console.log(x);
                                                            if(response2.data.issue.journals[l].details[m].new_value==status){      // 更改後的新狀態為我們制定的已核決狀態
                                                //                console.log(response2.data.issue.journals[l].created_on);
                                                
                                                                $scope.obj[i].act_finishdate = response2.data.issue.journals[l].created_on;
                                                                $scope.obj[i].act_finishdate = $scope.obj[i].act_finishdate.match("(.*)T")[1];
                                                                backup_devops = $scope.obj;
                                                            }
                                                        }
                                                    }
                                                  //  console.log('success');
                                                   // console.log(response2);
                                                },
                                                function (err2){
                                                    console.log(err2);
                                                    console.log('this is a error');
                                                    console.log(err.stack);
                                            });
                                               // break;
                                        }
                                    }
                                }
                            }
                        }
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    }
             ) 

    }



    function covert(update,newobj){
        
        var key = Object.keys(update).length;
    
        var result = '{"oldprojects":[';
        for(var i=0;i<key;i++){
            if(i==0){result = result;}
            else {result = result+',';}
            result = result + update[i];        //json字串串連              
        }

        result = result + '],"newprojects":[';
        key = Object.keys(newobj).length;
        for(var i=0;i<key;i++){
            if(i==0){result = result;}
            else result = result + ',';
            result = result + newobj[i];          //json string plus
        }
        result = result + ']}'
        return result;
    }

    function update_database(){
             $http(
                 {
                     method: 'GET',
                    // crossDomain: true,
                     //url: 'http://140.92.144.26/projects.json?limit=100&status=0&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', //+ '?callback=JSON_CALLBACK'
                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/projects'
                     //datatype: "jsonp",
                    // jsonp: "callback",
                    // jsonpCallback: "handler"

                 }
             ).then(function (response) {
                        console.log('this is a response');
                   //     console.log(response);  
                        $scope.count = Object.keys(response.data._embedded.projects).length;
                        var len = Object.keys(response.data._embedded.projects).length;  
                        for (var i=0; i<len; i++) {
                                $scope.obj[i] = response.data._embedded.projects[i];
                        }
                   
                        $location.path('/dashboard');
                 //       console.log($scope.obj);
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    }
             ) 
    }

    $scope.find = function (){
        console.log("run in find");
        if($scope.findname==null||$scope.findname==""){
     //       console.log(backup_devops);
            $scope.obj = backup_devops;
            return false;
        }
        var obj_b = $scope.obj;  
       // console.log(obj_b);
        $scope.obj = [];
       // console.log($scope.obj);
        var len = Object.keys(obj_b).length;     
        console.log(len);
        for(var i=0,j=0;i<len;i++){
            console.log(obj_b[i].projname);
            console.log($scope.findname);
            if(obj_b[i].projname.match($scope.findname)!=null)
            {
         //       var date = new Date(obj_b[i].pre_sentdate);
         //       var setdate = new Date(1911+parseInt($scope.year));
         //       if(date-setdate>0){
                    $scope.obj[j] = obj_b[i];
                    j++;
         //       }
            }
        }

    }

    $scope.goProj = function(x,y,z,w,s){
        MyVar.currentProj = x;
        MyVar.state = y;
        MyVar.quesnum = z;
        MyVar.currentPid = w ;

      //  console.log($scope.obj);
        $location.path('/dashboard/ResultList').search({currentProj:x,state:y,quesnum:z,currentPid:w,pre_finishdate:s});
    }



  });