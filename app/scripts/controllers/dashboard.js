'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('DashboardCtrl',function($scope, $state,MyVar) {
    $scope.$state = $state;
 //  $scope.name = MyVar.name;

	function getCookie(cname) {
	    var name = cname + "=";
	    var ca = document.cookie.split(';');
	    for(var i = 0; i <ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0)==' ') {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length,c.length);
	        }
	    }
	    return "";
	}
    $scope.loginname = getCookie("name");
    
    if($scope.loginname==null)
    {
        alert("請先登入");
        window.location.href = 'http://'+MyVar.FrontUrl;
  	}
  });
