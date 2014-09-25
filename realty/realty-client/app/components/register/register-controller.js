var registerModule = (function () {
    'use strict';

    var cfg = {
        moduleName: 'rentApp.register',
        moduleDependencies: ['ui.router'],

        ctlName: 'RegisterController',
        serviceName: 'registerService',
        stateName: 'registerState',
        stateConfig: {
            url: '/register',
            templateUrl: 'register/register-view.html'
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

    function controller($scope, registerService, navigationService, $log) {
        $log.debug('Register controller execution');

        $scope.register = registerService.register;

        $(function () {
            navigationService.setRegister();
        });
//    $scope.greeting = registerService.hello();
    }

    function service($http) {
        return {
            register: function () {
                $http({method: 'POST', url: 'register'}).
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

registerModule.init();

