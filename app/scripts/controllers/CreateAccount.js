
/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('CreateAccountCtrl',function($scope, $state,$http) {
    $scope.id = null;
    $scope.firstname = null;
    $scope.secondname = null;
    $scope.email = null;
    $scope.pwd = null;
    $scope.pwd2 = null;

    $scope.create = function(){
	    if($scope.id==null){
	    	alert("請輸入帳號");
	    	return true;
	    }else if($scope.firstname==null){
	    	alert("請輸入姓氏");
	    	return true;
	    }else if($scope.lastname==null){
	    	alert("請輸入名字");
	    	return true;
	    }else if($scope.email==null){
	    	alert("請輸入電子郵件");
	    	return true;
	    }else if($scope.pwd==null){
	    	alert("請輸入密碼");
	    	return true;
	    }else if($scope.pwd2==null){
	    	alert("請輸入密碼確認");
	    	return true;
	    }
	    if($scope.pwd != $scope.pwd2)
	    {
	    	alert("兩次輸入的密碼不同")
	    	$scope.pwd = null;
    		$scope.pwd2 = null;
    		return true;
	    }
	    if($scope.pwd.search(/[\W]/g) != -1){
        console.log('key error');
        alert("密碼只可為數字跟英文");
        $scope.pwd = null;
     		$scope.pwd2 = null;
                return true;
      }

      //send json to database
       var context = '{"account":"'+$scope.id+'","name":"'+$scope.firstname+'","lastname":"'+$scope.lastname+'","mail":"'+$scope.email+'","pwd":"'+$scope.pwd+'"}';
       console.log(context);

      $http(
                 {
                     method: 'POST',
                     url: 'http://140.92.142.9:8081/TodoService/users',
                     headers: { 
                     	'cache-control': 'no-cache',
					    'content-type': 'application/json',
				
					 },
					   
					 data: JSON.parse(context),
					 json: true 
                 }
             ).then(function (response) {
                        console.log('this is a response');
                        console.log(response);                 
                      
                    }, 
                    function (err) {
                        console.log(err);
                        console.log('this is a error');
                       
                   }
             );


	 };

	
	}
 );