'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('LoginCtrl', function($scope, $location, $http) {
    $scope.name = null;
    $scope.pwd = null;
    
    $scope.submit = function() {
      var name = $scope.name;
      var pwd = $scope.pwd ;
      if(name == null)
      { 
        alert("請輸入帳號");
        return true;
      }
      if(pwd == null)
      { 
        alert("請輸入密碼");
        return true;
      }
      check($scope.name,$scope.pwd);
   
      
    };
    $scope.create = function() {
    	
      $location.path('/CreateAccount');
      console.log("test");
      return false;
    };

    function check(id,pwd){
      
    

      //send json to database
      $http(
             {
                 method: 'GET',
                 url: 'http://140.92.142.9:8081/TodoService/users/search/findByAccount?account='+id,
                 headers: { 
                  'cache-control': 'no-cache',
                  'content-type': 'application/json',
                  },
                 json: true 
              }
             ).then(function (response) {
                        console.log('this is a response');
                    //   console.log(response.data._embedded);
                        if(response.data._embedded!= null)
                        {
                          if(response.data._embedded.users[0].pwd == pwd){
                            $location.path('/dashboard');
                            return false; 
                          }
                          else 
                          {
                            alert("帳號密碼錯誤");
                            return  true;
                          }             
                        }else
                        {
                          console.log("no this account");
                          alert("無此帳號");
                          return  true;
                        }
                    }, 
                    function (err) {
                       console.log(err);
                       console.log('this is a error');
                       alert("資料庫存取有問題，請檢查網路或聯絡管理員");
                       return  true;
                   }
             );
    }


  });
