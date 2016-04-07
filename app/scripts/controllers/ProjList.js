 /**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('ProjListCtrl', function($scope, $location,$http) {
    $scope.obj=[];
    $scope.count = 0;
    $scope.offset = 0;
    activate();
    function activate() {
             console.log('run activate.');
//             var token = btoa('160317' + ':' + 'noahjian+1203');
  //           $http.defaults.headers.common['Authorization'] = 'Basic ' + token;
           //  $http.defaults.withCredentials = true;
             $http(
                 {
                     method: 'GET',
                    // crossDomain: true,
                     url: 'http://140.92.144.26/projects.json?limit=100&status=0&key=f69bf52b02b565b2bdd354ebd208b87eb8c620d9', //+ '?callback=JSON_CALLBACK'
                     //datatype: "jsonp",
                    // jsonp: "callback",
                    // jsonpCallback: "handler"

                 }
             ).then(function (response) {
                        console.log('this is a response');
                        //console.log(response);  
                        $scope.count = response.data.totol_count;
                        var len = Object.keys(response.data.projects).length;  
                        for (var i=0,j=0; i<len; i++) {
                            //$scope.deployStatus[i] = '';
                            if(response.data.projects[i].custom_fields[6].value == 1)
                            {
                                $scope.obj[j] = response.data.projects[i];
                                j++;
                            }
                          //  obj.filename = getFileName(obj.uri);
                        }
                       // console.log($scope.deployStatus);
                        //var temp = respose.data.results;                        
                        //$scope.results = response.data.results;
                       // console.log($scope.obj);
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    }
             ) 
    }
  

    $scope.show = function (test){
        //console.log("test");
       // console.log(test.custom_fields[6].value);
         if(test.custom_fields[6].value == 1){
             var context = '<a ui-sref="ResultList" >'+test.name+'</a>';
        //     '<td>是</td>'+
        //     '<td><span></td>'+
        //     '<td><span></td>'+
        //     '<td><span></td>'+
        //     '<td><a ui-sref="SetProjConf" ></a><span></td>';
            console.log(context);
            return context;

         }else
         {

         }
    }

    $scope.submit = function() {

      $location.path('/dashboard');

      return false;
    };



    // $scope.results = {
    // 	"first":{
    // 		"A":"A1234","B":"是","C":"2015/03/05","D":"2015/03/05","E":"2015/03/05","F":"操作"
    // 	},
	   //  "second":{
	   //  	"A":"B2234","B":"是","C":"2015/06/01","D":"2015/06/16","E":"2015/06/30","F":"操作"
	   //  },
	   //  "third":{
	   //  	"A":"C3234","B":"是","C":"2015/09/01","D":"2015/09/15","E":"2015/09/30","F":"操作"
	   //  },
	   //  "fourth":{
	   //  	"A":"D2578","B":"是","C":"2015/10/01","D":"2015/10/15","E":"2015/10/30","F":"操作"
	   //  }
    // };

  });