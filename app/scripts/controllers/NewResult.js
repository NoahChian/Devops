
/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('NewResultCtrl', function($scope) {

  
    $scope.SecretLv = "密";
	$scope.subjects = [
	'密','機密'
  	];
	$scope.dropboxitemselected = function(item){
		$scope.SecretLv = item;
		return false;
	};

	$scope.SeledName = [
	'王大明',
	'張小芳',
	'簡大頭',
	'李三','陳五','菜八',
  	];
  	$scope.SelyetName = [
	'陳大春','留德華','張學有','王紅','小李子',
  	];

});



