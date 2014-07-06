var loginController = function ($scope, $log, loginService, navigationService, authorizationService) {
    'use strict';

    $log.debug('Login controller execution');

    $scope.login = loginService.login;

    $scope.navService = navigationService;

    $scope.isAuthorized = authorizationService.isAuthorized;

    $scope.$on(authorizationService.EVENT_AUTH_STATE_CHANGED, function () {
        $scope.apply(
            function () {
                var authorized = authorizationService.isAuthorized();
                $log.info("login_controller, setting to " + authorized);
                $scope.isAuthorized = authorized;
            });
    });


    $scope.loginWithFacebook = authorizationService.loginWithFacebook;
    $scope.logout = authorizationService.logoutWithFacebook;


    $(function () {
        navigationService.setLogin();
    });
//    $scope.greeting = loginService.hello();
};

var loginService = function ($http) {
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
};

