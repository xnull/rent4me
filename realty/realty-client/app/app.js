'use strict';

//console.log('Create an angular app module');
var rentApplication = angular.module('project', ['ngRoute'/*, 'facebook'*/]);

var INDEX_CONTROLLER_NAME = 'IndexController';
var INDEX_SERVICE_NAME = 'indexService';

var RENT_CONTROLLER_NAME = 'RentController';
var RENT_SERVICE_NAME = 'rentService';

var LOGIN_CONTROLLER_NAME = 'LoginController';
var LOGIN_SERVICE_NAME = 'loginService';

var AUTHORIZATION_CONTROLLER_NAME = 'AuthorizationController';
var AUTHORIZATION_SERVICE_NAME = 'authorizationService';

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

rentApplication.controller(AUTHORIZATION_CONTROLLER_NAME, authorizationController);
rentApplication.factory(AUTHORIZATION_SERVICE_NAME, authorizationService);

rentApplication.controller(RENT_CONTROLLER_NAME, rentController);
rentApplication.controller(LOGIN_CONTROLLER_NAME, loginController);

rentApplication.controller(REGISTER_CONTROLLER_NAME, registerController);
rentApplication.factory(RENT_SERVICE_NAME, rentService);
rentApplication.factory(LOGIN_SERVICE_NAME, loginService);
rentApplication.factory(REGISTER_SERVICE_NAME, registerService);

//TODO: clean up this shit
rentApplication.run(
    function ($rootScope, $window, authorizationService) {

//        $rootScope.fbUser = {};

        // This is called with the results from from FB.getLoginStatus().
        function statusChangeCallback(response) {
            console.log('statusChangeCallback');
            console.log(response);
            // The response object is returned with a status field that lets the
            // app know the current login status of the person.
            // Full docs on the response object can be found in the documentation
            // for FB.getLoginStatus().
            if (response.status === 'connected') {
                // Logged into your app and Facebook.
                console.log('Welcome!  Fetching your information.... ');

                authorizationService.setFacebookUser(true);
                FB.api('/me', function (response) {
                    console.log('Successful login for: ' + response.name);
                    console.log(response);
//                document.getElementById('status').innerHTML =
//                    'Thanks for logging in, ' + response.name + '!';
                });
            } else if (response.status === 'not_authorized') {
                authorizationService.setFacebookUser(null);
                // The person is logged into Facebook, but not your app.
                var innerHTML = 'Please log ' +
                    'into this app.';
                console.log(innerHTML);
//                document.getElementById('status').innerHTML = innerHTML;
            } else {
                authorizationService.setFacebookUser(null);
                // The person is not logged into Facebook, so we're not sure if
                // they are logged into this app or not.
                var innerHTML = 'Please log ' +
                    'into Facebook.';
                console.log(innerHTML);
//                document.getElementById('status').innerHTML = innerHTML;
            }
        }

        // This function is called when someone finishes with the Login
        // Button.  See the onlogin handler attached to it in the sample
        // code below.
        function checkLoginState() {
            FB.getLoginStatus(function (response) {
                statusChangeCallback(response);
            });
        }

//270007246518198

        // Load the SDK asynchronously

//        $.ajaxSetup({ cache: true });
        $.getScript('//connect.facebook.net/en_UK/all.js', function(){
            FB.init({
//                appId: '271375949714661', //for local development:
                appId: '270007246518198',//for deployment
                cookie: true,  // enable cookies to allow the server to access
                // the session
                xfbml: true,  // parse social plugins on this page
                version: 'v2.0' // use version 2.0
            });

            // Now that we've initialized the JavaScript SDK, we call
            // FB.getLoginStatus().  This function gets the state of the
            // person visiting this page and can return one of three states to
            // the callback you provide.  They can be:
            //
            // 1. Logged into your app ('connected')
            // 2. Logged into Facebook, but not your app ('not_authorized')
            // 3. Not logged into Facebook and can't tell if they are logged into
            //    your app or not.
            //
            // These three cases are handled in the callback function.

            FB.getLoginStatus(function(response) {
                statusChangeCallback(response);
            });
//            $('#loginbutton,#feedbutton').removeAttr('disabled');

        });

    });