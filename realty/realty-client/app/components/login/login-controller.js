var loginModule = (function () {
    'use strict';

    var cfg = {
        moduleName: 'rentApp.login',
        moduleDependencies: ['ui.router'],

        ctlName: 'LoginController',
        serviceName: 'loginService',
        stateName: 'loginState',
        stateConfig: {
            url: '/login',
            templateUrl: 'login/login-view.html',
            controller: 'LoginController'
        }
    };

    var angularModule = angular.module(cfg.moduleName, cfg.moduleDependencies);
    var angularLogger = angular.injector([cfg.moduleName, 'ng']).get('$log');

    function init() {
        angularLogger.debug('Loading "' + cfg.moduleName + '" module');

        angularModule.config(function ($stateProvider) {
            $stateProvider.state(cfg.stateName, cfg.stateConfig);
        });

        angularModule.controller(cfg.ctlName, controller);
        angularModule.factory(cfg.serviceName, service);
    }

    function controller($scope, $log, loginService, navigationService, authorizationService) {
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
    }

    function service($http) {
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
    }

    return {
        init: init,
        ctl: controller,
        srv: service
    };
})();

loginModule.init();

