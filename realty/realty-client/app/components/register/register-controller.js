var registerModuleCfg = {
    ctlName: 'RegisterController',
    serviceName: 'registerService',
    stateName: 'registerState',
    stateConfig: {
        url: '/register',
        templateUrl: 'components/register/register-view.html'
    }
};

var registerModule = angular.module('rentApp.register', ['ui.router']);

registerModule.config(function ($stateProvider) {
    'use strict';
    $stateProvider.state(registerModuleCfg.stateName, registerModuleCfg.stateConfig);
});

registerModule.controller(registerModuleCfg.ctlName, function ($scope, registerService, navigationService, $log) {
    'use strict';

    $log.debug('Register controller execution');
    $scope.register = registerService.register;

    $(function () {
        navigationService.setRegister();
    });
//    $scope.greeting = registerService.hello();
});

registerModule.factory(registerModuleCfg.serviceName, function ($http) {
    "use strict";

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
});

