var loginModule = angular.module('rentApp.login', []);

loginModule.controller('LoginController', function ($scope, $log, loginService, navigationService, authorizationService) {
    'use strict';

    $log.debug('Login controller execution');

    $scope.login = loginService.login;

    $scope.navService = navigationService;

    $scope.authorized = authorizationService.isAuthorized();

    $scope.$on(authorizationService.EVENT_AUTH_STATE_CHANGED, function (event, args) {

        $log.info('Event');
        $log.info(event);
        $log.info('args');
        $log.info(args);
        $log.info("login_controller, setting to " + args.authorized);
//        var authorized = authorizationService.isAuthorized();
        $scope.authorized = args.authorized;
    });


    $scope.loginWithFacebook = authorizationService.loginWithFacebook;
    $scope.loginWithVK = authorizationService.loginWithVK;
    $scope.logout = authorizationService.logout;


    $(function () {
        navigationService.setLogin();
    });
//    $scope.greeting = loginService.hello();
});

loginModule.factory('loginService', function ($http) {
    "use strict";

    return {
        login: function () {
            $http({method: 'POST', url: 'login'}).
                success(function (data, status, headers, config) {
                    // this callback will be called asynchronously
                    // when the response is available
                    //console.log('Successful sending ajax request');
                }).
                error(function (data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    //console.log('Error sending ajax request. Status: ' + status);
                    //$scope.greeting = "raz dva";
                });
        }
    };
});
