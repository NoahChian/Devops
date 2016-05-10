/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('tab2', function($scope, $location,$stateParams,$http,MyVar) {

  	$scope.Projid = $stateParams.currentPid;
    $scope.ProjName = $stateParams.currentProj;
    $scope.results = [];
    console.log($stateParams.thisProject);
    run();

    function run(){
     // console.log(MyVar.url);
          $http(
                     {
                         method: 'GET',
                         url: 'http://'+MyVar.BackApiUrl+'/TodoService/verdiary/search/findByProjectid?projectid='+$stateParams.thisProject, 
                     }).then(function (response) {        
                        console.log(response);
                        if(!(Object.getOwnPropertyNames(response.data).length === 0)){
                          var len = Object.keys(response.data._embedded.verdiary).length;  
                          //console.log(response.data._embedded.results);
                          var object = response.data._embedded.verdiary;
                          console.log(object);
                          for(var i=0;i<len;i++){                             //找尋每一筆成果
                              $scope.results[i] = object[i];
                              console.log($scope.results[i]);
                          }
                        }
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    });

    };

    
  });