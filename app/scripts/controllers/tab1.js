/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('tab1', function($scope, $location) {

   

    $scope.results = {
      "first":{
        "A":"v1.0","D":"需求規格書","C":"A1234","B":"2015/03/05","E":"2015/03/05"
      },
      "second":{
        "A":"v1.0","D":"設計規格書","C":"B2244","B":"2015/06/01","E":"2015/06/16"
      },
      "third":{
        "A":"v1.1","D":"需求規格書","C":"A1235","B":"2015/09/01","E":"2015/09/15"
      },
      "fourth":{
        "A":"v1.1","D":"設計規格書","C":"B2234","B":"2015/10/01","E":"2015/10/15"
      }
    };
  }).controller('tab2', function($scope, $location) {


     $scope.results = {
      "first":{
        "E":"A1234","C":"需求規格書","B":"930708","A":"2015/03/05","F":"A1235","D":"成果編號"
      }
    };

  });