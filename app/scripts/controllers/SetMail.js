/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('SetMailCtrl', function($scope, $location) {

    $scope.submit = function() {

      $location.path('/dashboard');

      return false;
    };
    $scope.Select = "smpt";
    $scope.login = "login";
    $scope.subjects = [
    'smtp','pop3','imap'
      ];
    $scope.dropboxitemselected = function(item){
      $scope.Select = item;
      return false;
    };
    $scope.dropboxitemselected2= function(item){
      $scope.login = item;
      return false;
    };
    $scope.logins = [
    	'login','no'
    ];

  });