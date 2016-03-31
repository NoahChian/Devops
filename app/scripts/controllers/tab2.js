/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('tab2', function($scope, $location) {


     $scope.results = {
      "first":{
        "E":"A1234","C":"需求規格書","B":"930708","A":"2015/03/05","F":"A1235","D":"成果編號"
      }
    };
  });