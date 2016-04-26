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
                     url: 'http://'+MyVar.redmineApiUrl+'/projects.json?limit=100&offset='+x+'&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', 
                 }
             ).then(function (response) {
                        console.log(response);  
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
                        console.log(response);
                         var len = Object.keys(response.data._embedded.projects).length;  
                        for (var i=0; i<len; i++) {
                                $scope.obj[i] = response.data._embedded.projects[i];
                        }                      
                        backup_devops = $scope.obj; 
                    }, 
                    function (err) {
                        if(err.status!=200)
                            alert("與DevOps連接失敗");
                        console.log(err);
                        console.log('this is a error');                 
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
                    console.log(response);
                     var len = Object.keys(response.data._embedded.projects).length;  
                    for (var i=0; i<len; i++) {
                            backup_devops[i] = response.data._embedded.projects[i];
                    }
                    lens = Object.keys(backup_devops).length;
                    proj_redmine(0);
                /*    $http(
                     {  
                         method: 'GET',                                                                 // to get redmine projects
                         url: 'http://140.92.144.26/projects.json?limit=100&status=0&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', 
                     }).then(function (response) {        
                        $scope.count = response.data.totol_count;
                        var len = Object.keys(response.data.projects).length;  
                        for (var i=0,j=0; i<len; i++) {
                            if(response.data.projects[i].custom_fields[6].value == 1)
                            {
                                $scope.obj[j] = response.data.projects[i];
                                j++;
                            }
                        }
                        console.log("search redmine projects ok");
                        lens2= Object.keys($scope.obj).length  
                        console.log(lens2);
                        compare();
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    });*/
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
                
                        //console.log(obj_devops[i].custom_fields[7].value + ":" + obj_devops[i].id);
                        console.log("find project , now to update ");
                        key = true;                         //remember we find project

                        update_devops[x] = '{"id":"'+backup_devops[j].id+'","projid":"'+obj_devops[i].id+'","projname":"'+obj_devops[i].name+'","pre_sentdate":'+  //注意 id從我們的資料庫取得, projid從redmine上取得
                        s1+',"pre_finishdate":'+
                        s2+'}';

               //         console.log(update_devops[x]);
                        /*
                        $http(
                                {
                                     method: 'PATCH',
                                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/projects/'+backup_devops[j].id,
                                     headers: { 
                                        'cache-control': 'no-cache',
                                        'content-type': 'application/json',                               
                                     },                                   
                                     data: JSON.parse(update_devops[x]),
                                     json: true 
                                 }
                             ).then(function (response) {
                                        console.log('update success');
                                        console.log(response);
                                    }, 
                                    function (err) {
                                        if(err.status==409)
                                            alert("同步失敗");
                                        console.log(err);
                                        console.log('this is a error');
                                   }
                        );*/
                        x++;
                        break;
                    }
                }
                if(key==false){     //所有devops內都沒有  代表應該新建立
                    console.log("new");

                    var s1 = (obj_devops[i].custom_fields[7].value==null)?'""':('"'+obj_devops[i].custom_fields[7].value+'"');
                    var s2 = (obj_devops[i].custom_fields[8].value==null)?'""':('"'+obj_devops[i].custom_fields[8].value+'"');
                

                    new_devops[y] = '{"projid":"'+obj_devops[i].id+'","projname":"'+obj_devops[i].name+'","pre_sentdate":'+
                    s1+',"pre_finishdate":'+
                    s2+'}';
          //          console.log(new_devops[y]);
                 /*   $http(
                                {
                                     method: 'POST',
                                     url: 'http://'+MyVar.BackApiUrl+'/TodoService/projects',
                                     headers: { 
                                        'cache-control': 'no-cache',
                                        'content-type': 'application/json',
                                    
                                     },                                   
                                     data: JSON.parse(new_devops[y]),
                                     json: true 
                                 }
                             ).then(function (response) {
                                        console.log('new it success');
                                        console.log(response);
                                    }, 
                                    function (err) {
                                        if(err.status==409)
                                            alert("同步失敗");
                                        console.log(err);
                                        console.log('this is a error');
                                        $scope.pwd = null;
                                        $scope.pwd2 = null;
                                   }
                    );*/
                    y++;
                }
                key = false;  
            }
        }

       // console.log(update_devops);
       // console.log(new_devops);
        var bulksave = covert(update_devops,new_devops);
        console.log(bulksave);
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
                                        console.log(response.data);
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
                                        $scope.pwd = null;
                                        $scope.pwd2 = null;
                                   }
                    )
        };

    function secondconnect(){

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
                        console.log(response);  
                        $scope.count = Object.keys(response.data._embedded.projects).length;
                        var len = Object.keys(response.data._embedded.projects).length;  
                        for (var i=0; i<len; i++) {
                                $scope.obj[i] = response.data._embedded.projects[i];
                        }
                   
                        $location.path('/dashboard');
                        console.log($scope.obj);
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

    $scope.goProj = function(x,y,z,w){
        MyVar.currentProj = x;
        MyVar.state = y;
        MyVar.quesnum = z;
        MyVar.currentPid = w ;
        $location.path('/dashboard/ResultList');
    }




  });