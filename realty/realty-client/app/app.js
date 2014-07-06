var appConfiguration = [
    {
        ctl: indexController,
        service: indexService,
        ctlName: 'IndexController',
        serviceName: 'indexService',
        route: '/',
        template: 'components/index/index-view.html'
    },
    {
        ctl: rentController,
        service: rentService,
        ctlName: 'RentController',
        serviceName: 'rentService',
        route: '/rent',
        template: 'components/rent/rent-view.html'
    },
    {
        ctl: loginController,
        service: loginService,
        ctlName: 'LoginController',
        serviceName: 'loginService',
        route: '/login',
        template: 'components/login/login-view.html'
    },
    {
        ctl: registerController,
        service: registerService,
        ctlName: 'RegisterController',
        serviceName: 'registerService',
        route: '/register',
        template: 'components/register/register-view.html'
    }
];
var AUTHORIZATION_CONTROLLER_NAME = 'AuthorizationController';
var AUTHORIZATION_SERVICE_NAME = 'authorizationService';


/*appConfiguration.push({
 ctl: ,
 service: ,
 ctlName: 'SearchRentController',
 serviceName: 'searchRentService',
 route: '/search-rent',
 template: 'components/search-rent/search-rent-view.html'
 });*/

var rentApplication = angular.module('project', ['ngRoute', 'ngResource'/*, 'facebook'*/]);

rentApplication.config(function ($routeProvider) {
    'use strict';

    for (var i = 0; i < appConfiguration.length; i++) {
        var cfg = appConfiguration[i];
        console.log("CtlName: " + cfg.ctlName + ", serviceName: " + cfg.serviceName + ", route: " + cfg.route);
        $routeProvider.when(cfg.route, {
            controller: cfg.ctlName,
            templateUrl: cfg.template
        })
    }

    $routeProvider.otherwise({
        redirectTo: '/'
    });
});

for (var i = 0; i < appConfiguration.length; i++) {
    var cfg = appConfiguration[i];
    rentApplication.controller(cfg.ctlName, cfg.ctl);
    rentApplication.factory(cfg.serviceName, cfg.service);
}
rentApplication.controller(AUTHORIZATION_CONTROLLER_NAME, authorizationController);
rentApplication.factory(AUTHORIZATION_SERVICE_NAME, authorizationService);
rentApplication.controller('NavigationController', navigationController);
rentApplication.factory('navigationService', navigationService);

/*rentApplication.config(['FacebookProvider', function(FacebookProvider) {
 // Here you could set your appId through the setAppId method and then initialize
 // or use the shortcut in the initialize method directly.
 FacebookProvider.init('270007246518198');
 }]);*/

//TODO: clean up this shit
var authFuction = function ($rootScope, $window, authorizationService, $log) {
    'use strict';
    // $rootScope.fbUser = {};

    // This is called with the results from from FB.getLoginStatus().
    function statusChangeCallback(response) {
        $log.debug('statusChangeCallback');
        $log.debug(response);

        var innerHTML;
        // The response object is returned with a status field that lets the
        // app know the current login status of the person.
        // Full docs on the response object can be found in the documentation
        // for FB.getLoginStatus().
        if (response.status === 'connected') {
            // Logged into your app and Facebook.
            $log.debug('Welcome!  Fetching your information.... ');

            authorizationService.setFacebookUser(true);
            FB.api('/me', function (response) {
                $log.debug('Successful login for: ' + response.name);
                $log.debug(response);
//                document.getElementById('status').innerHTML =
//                    'Thanks for logging in, ' + response.name + '!';
            });
        } else if (response.status === 'not_authorized') {
            authorizationService.setFacebookUser(null);
            // The person is logged into Facebook, but not your app.
            innerHTML = 'Please log into this app.';
            $log.debug(innerHTML);
//                document.getElementById('status').innerHTML = innerHTML;
        } else {
            authorizationService.setFacebookUser(null);
            // The person is not logged into Facebook, so we're not sure if
            // they are logged into this app or not.
            innerHTML = 'Please log into Facebook.';
            $log.debug(innerHTML);
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
    $.getScript('//connect.facebook.net/en_UK/all.js', function () {
        FB.init({
            appId: '270007246518198',
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

        FB.getLoginStatus(function (response) {
            statusChangeCallback(response);
        });
//            $('#loginbutton,#feedbutton').removeAttr('disabled');

    });

};
//rentApplication.run(authFuction);