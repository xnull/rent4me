'use strict';

//console.log('Create an angular app module');
var rentApplication = angular.module('project', ['ngRoute'/*, 'facebook'*/]);

var INDEX_CONTROLLER_NAME = 'IndexController';
var INDEX_SERVICE_NAME = 'indexService';

var RENT_CONTROLLER_NAME = 'RentController';
var RENT_SERVICE_NAME = 'rentService';

var LOGIN_CONTROLLER_NAME = 'LoginController';
var LOGIN_SERVICE_NAME = 'loginService';

var FB_LOGIN_CONTROLLER_NAME = 'FBLoginController';
var FB_LOGIN_SERVICE_NAME = 'fbLoginService';

var REGISTER_CONTROLLER_NAME = 'RegisterController';
var REGISTER_SERVICE_NAME = 'registerService';

rentApplication.config(function ($routeProvider) {
//    console.log('Configure the angular app routes');
    $routeProvider
        .when('/', {
            controller: INDEX_CONTROLLER_NAME,
            templateUrl: 'components/index/index-view.html'
        })
        .when('/rent', {
            controller: RENT_CONTROLLER_NAME,
            templateUrl: 'components/rent/rent-view.html'
        })
        .when('/login', {
            controller: LOGIN_CONTROLLER_NAME,
            templateUrl: 'components/login/login-view.html'
        })
        .when('/fb_login', {
            controller: FB_LOGIN_CONTROLLER_NAME,
            templateUrl: 'components/fb_login/fb_login-view.html'
        })
        .when('/register', {
            controller: REGISTER_CONTROLLER_NAME,
            templateUrl: 'components/register/register-view.html'
        })
        .otherwise({
            redirectTo: '/'
        });
});

/*rentApplication.config(['FacebookProvider', function(FacebookProvider) {
    // Here you could set your appId through the setAppId method and then initialize
    // or use the shortcut in the initialize method directly.
    FacebookProvider.init('270007246518198');
}]);*/

rentApplication.controller('NavigationController', navigationController);
rentApplication.factory('navigationService', navigationService);

rentApplication.controller(INDEX_CONTROLLER_NAME, indexController);
rentApplication.factory(INDEX_SERVICE_NAME, indexService);

rentApplication.controller(RENT_CONTROLLER_NAME, rentController);
rentApplication.controller(LOGIN_CONTROLLER_NAME, loginController);
rentApplication.controller(FB_LOGIN_CONTROLLER_NAME, fbLoginController);
rentApplication.controller(REGISTER_CONTROLLER_NAME, registerController);

rentApplication.factory(RENT_SERVICE_NAME, rentService);
rentApplication.factory(LOGIN_SERVICE_NAME, loginService);
rentApplication.factory(FB_LOGIN_SERVICE_NAME, fbLoginService);
rentApplication.factory(REGISTER_SERVICE_NAME, registerService);
