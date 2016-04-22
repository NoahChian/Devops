/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('ConversionCtrl', function($scope, $location) {

    $scope.hour = "00";
    $scope.min = "00";
    $scope.state = '執行中';

    $scope.start = function() {
      $scope.state ='執行中';
      return false;
    };

    $scope.stop = function() {
       $scope.state ='停止';
      return false;
    };
    $scope.dropboxitemselected = function(item){
      $scope.hour = item;
      return false;
    };
    $scope.dropboxitemselected2 = function(item){
      $scope.min = item;
      return false;
    };
    $scope.hours = [
      '00','01','02','03','04','05','06','07','08','09','10'
      ,'11','12','13','14','15','16','17','18','19','20'
      ,'21','22','23'
    ];
    $scope.mins = [
      '00','10','20','30','40','50',
    ];
  });