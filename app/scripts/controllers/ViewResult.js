/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */

angular.module('yapp')
// .controller('ViewResultCtrl', function($scope, $location) {
//   $scope.Projid = $stateParams.currentPid;
//   $scope.ProjName = $stateParams.currentProj;)}
// )
.directive('tabset', function () {
  return {
    restrict: 'E',
    replace: true,
    transclude: true,
    controller: function($scope,$stateParams) {
      console.log($stateParams);
    
      $scope.Projid = $stateParams.currentPid;
      $scope.state = $stateParams.state;
      $scope.quesnum = $stateParams.quesnum;
      $scope.thisProject = $stateParams.thisProject;
      $scope.ProjName = $stateParams.currentProj;
      $scope.templateUrl = '';
      var tabs = $scope.tabs = [];
      var controller = this;
 
      this.selectTab = function (tab) {
        angular.forEach(tabs, function (tab) {
          tab.selected = false;
        });
        tab.selected = true;
      };
 
      this.setTabTemplate = function (templateUrl) {
        $scope.templateUrl = templateUrl;
      }
 
      this.addTab = function (tab) {
        if (tabs.length == 0) {
          controller.selectTab(tab);
        }
        tabs.push(tab);
      };
    },
    template:
      '<div class="row-fluid">' +
        '<div class="row-fluid">' +
          '<div class="nav nav-tabs" ng-transclude></div>' +
        '</div>' +
        '<div class="row-fluid">' +
          '<ng-include src="templateUrl">' +
        '</ng-include></div>' +
      '</div>'
  };
})
.directive('tab', function () {
  return {
    restrict: 'E',
    replace: true,
    require: '^tabset',
    scope: {
      title: '@',
      templateUrl: '@'
    },
    link: function(scope, element, attrs, tabsetController) {
      tabsetController.addTab(scope);
 
      scope.select = function () {
        tabsetController.selectTab(scope);
      }
 
      scope.$watch('selected', function () {
        if (scope.selected) {
          tabsetController.setTabTemplate(scope.templateUrl);
        }
      });
    },
    template:
      '<li ng-class="{active: selected}">' +
        '<a href="" ng-click="select()">{{ title }}</a>' +
      '</li>'
  };
});
