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
    'ngAnimate',
  ])

  .config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.when('/dashboard','/dashboard/ProjList')
    .when('/CreateAccount','/dashboard/CreateAccount');
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
          .state('CreateAccount', {
            url: '/CreateAccount',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/CreateAccount.html',
            controller: 'CreateAccountCtrl'
          })
          .state('ResultList', { 
            url: '/ResultList',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/ResultList.html',
            controller: 'ResultListCtrl'
          })
          .state('NewResult', { 
            url: '/NewResult',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/NewResult.html',
            controller: 'NewResultCtrl'
          })
          .state('ViewResult', { 
            url: '/ViewResult',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/ViewResult.html',
            //controller: 'ViewResultCtrl'
          })
          .state('ProjList', { 
            url: '/ProjList',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/ProjList.html',
            controller: 'ProjListCtrl'
          })
          .state('SetProjConf', { 
            url: '/SetProjConf',
            parent: 'dashboard',
            templateUrl: 'views/dashboard/SetProjConf.html',
            controller: 'SetProjConfCtrl'
          });



  });
