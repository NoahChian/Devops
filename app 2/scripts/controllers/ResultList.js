/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('ResultListCtrl', function($scope, $location,MyVar,$http) {
    $scope.results = [];
    $scope.ProjNam = MyVar.currentProj;
    $scope.ProjState = MyVar.state;
    $scope.QuesNum = MyVar.quesnum;
  	
    $scope.submit = function() {

      $location.path('/dashboard');

      return false;
    };

    run();

    function run(){
     // console.log(MyVar.url);
          $http(
                     {
                         method: 'GET',
                         url: 'http://'+MyVar.BackApiUrl+'/TodoService/results/search/findByProjid?projid='+MyVar.currentPid, 
                     }).then(function (response) {        
                        //console.log(response);
                        if(response.data!={}){
                          var len = Object.keys(response.data._embedded.results).length;  
                          console.log(len);
                          var object = response.data._embedded.results;
                          for(var i=0;i<len;i++){
                              $scope.results[i] = object[i];
                              if($scope.results[i].act_finish==null)
                                update(i);
                              getTicket(i);
                          }
                        }
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                        console.log(err.stack);
                    });

    };

    update = function(i){
      console.log("update "+i);
    }
    getTicket = function(i){
      console.log("connet db "+i);
    }

   

  });