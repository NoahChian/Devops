/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('SetProjConfCtrl', function($scope, $location) {

    $scope.submit = function() {

      $location.path('/dashboard');

      return false;
    };
    $scope.Select = "是";
    $scope.subjects = [
    '是','否'
      ];
    $scope.dropboxitemselected = function(item){
      $scope.Select = item;
      return false;
    };
    $scope.results = {
    	"first":{
    		"A":"A1234","B":"需求規格書","C":"","D":"2015/03/05","E":"2015/03/05","F":"2015/03/05","G":"x"
    	},
	    "second":{
	    	"A":"B2234","B":"設計規格書","C":"","D":"2015/06/01","E":"2015/06/16","F":"2015/06/30","G":"x"
	    },
	    "third":{
	    	"A":"C3234","B":"程式碼","C":"","D":"2015/09/01","E":"2015/09/15","F":"2015/09/30","G":"x"
	    },
	    "fourth":{
	    	"A":"D2578","B":"附件","C":"","D":"2015/10/01","E":"2015/10/15","F":"2015/10/30","G":"x"
	    }
    };

  });