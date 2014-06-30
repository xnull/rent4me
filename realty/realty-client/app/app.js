'use strict';

console.log('Create an angular app module');
var rentApplication = angular.module('project', ['ngRoute']);

var RENT_CONTROLLER_NAME = 'RentController';
var RENT_SERVICE_NAME = 'rentService';
var LOGIN_CONTROLLER_NAME = 'LoginController';
var LOGIN_SERVICE_NAME = 'loginService';
var REGISTER_CONTROLLER_NAME = 'RegisterController';
var REGISTER_SERVICE_NAME = 'registerService';

rentApplication.config(function ($routeProvider) {
    console.log('Configure the angular app routes');
    $routeProvider.
        when('/rent', {
            controller: RENT_CONTROLLER_NAME,
            templateUrl: 'components/rent/rent-view.html'
        })
        .when('/login', {
            controller: RENT_CONTROLLER_NAME,
            templateUrl: 'components/login/login-view.html'
        })
        .when('/register', {
            controller: REGISTER_CONTROLLER_NAME,
            templateUrl: 'components/register/register-view.html'
        })
        .otherwise({
            redirectTo: '/'
        });
});

rentApplication.controller(RENT_CONTROLLER_NAME, rentController);
rentApplication.controller(LOGIN_CONTROLLER_NAME, loginController);
rentApplication.controller(REGISTER_CONTROLLER_NAME, registerController);
rentApplication.factory(RENT_SERVICE_NAME, rentService);
rentApplication.factory(LOGIN_SERVICE_NAME, loginService);
rentApplication.factory(REGISTER_SERVICE_NAME, registerService);
