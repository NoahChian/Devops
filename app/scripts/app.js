'use strict';

/**
 * @ngdoc overview
 * @name yapp
 * @description
 * # yapp
 *
 * Main module of the application.
 */
angular
  .module('yapp', [
    'ui.router',
    'ngAnimate'
  ])

  .config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.when('/dashboard','/dashboard/ProjList');
    //$urlRouterProvider.when('/CreateAccount','/CreateAccount');
    $urlRouterProvider.otherwise('/login');

    $stateProvider
      .state('base', {
        abstract: true,
        url: '',
        templateUrl: 'views/base.html'
      })
        .state('login', {
          url: '/login',
          parent: 'base',
          templateUrl: 'views/login.html',
          controller: 'LoginCtrl'
        })
        .state('CreateAccount', {
            url: '/CreateAccount',
            parent: 'base',
            templateUrl: 'views/CreateAccount.html',
            controller: 'CreateAccountCtrl'
          })
        .state('dashboard', {
          url: '/dashboard',
          parent: 'base',
          templateUrl: 'views/dashboard.html',
          controller: 'DashboardCtrl'
        })
          .state('SetMail',{           
            url: '/SetMail',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/SetMail.html',
            controller: 'SetMailCtrl'
          })
          .state('Expediting', {
            url: '/Expediting',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/Expediting.html',
            controller: 'ExpeditingCtrl'
          })
          .state('Conversion', {
            url: '/Conversion',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/Conversion.html',
            controller: 'ConversionCtrl'
          })
          .state('ResultList', { 
            url: '/ResultList?currentProj&currentPid&state&quesnum&thisProject',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/ResultList.html',
            controller: 'ResultListCtrl',
            //params: {param:null,}
          })
          .state('NewResult', { 
            url: '/NewResult?currentProj&currentPid&state&quesnum&thisProject',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/NewResult.html',
            controller: 'NewResultCtrl'
          })
          .state('ModifyResult', { 
            url: '/ModifyResult?currentProj&currentPid&state&quesnum&thisProject&redmineIssueId',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/ModifyResult.html',
            controller: 'ModifyResultCtrl'
          })
          .state('ViewResult', { 
            url: '/ViewResult?currentProj&currentPid&state&quesnum&thisProject',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/ViewResult.html',
           // controller: 'ViewResultCtrl'
          })
          .state('ProjList', { 
            url: '/ProjList',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/ProjList.html',
            controller: 'ProjListCtrl'
          })
          .state('ResultVersionSet', { 
            url: '/ResultVersionSet?resultId&thisDevopsProjectId',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/ResultVersionSet.html',
            controller: 'ResultVersionSetCtrl'
          })
          .state('SetProjConf', { 
            url: '/SetProjConf',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/SetProjConf.html',
            controller: 'SetProjConfCtrl'
          });

        



  }).service('MyVar',function(){
  this.name = null;
  this.currentProj = null;
  this.currentPid = null;
  this.state = null;
  this.quesnum = null;

  this.BackApiUrl = "140.92.144.34:8081";
  this.redmineApiUrl = "140.92.144.26";
  this.FrontUrl = "140.92.144.33:9000";
/*
  this.BackApiUrl = "140.92.142.9:8081";
  this.redmineApiUrl = "140.92.144.26";
  this.FrontUrl = "140.92.142.9:9000";
*/
});

