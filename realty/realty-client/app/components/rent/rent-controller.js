var rentService = require('./rent-service.js');

var rentModule = (function () {
    'use strict';

    var cfg = {
        moduleName: 'rentApp.rent',
        moduleDependencies: ['ui.router'],

        ctlName: 'RentController',
        serviceName: 'RentService',
        stateName: 'rentState',
        stateConfig: {
            url: '/rent',
            templateUrl: 'components/rent/rent-view.html',
            controller: 'RentController'
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
        angularModule.factory(cfg.serviceName, rentService(cfg.serviceName));
    }

    /**
     * dynamic pages https://egghead.io/lessons/angularjs-using-resource-for-data-models
     * @param $log
     * @param $scope
     * @param rentService
     * @param navigationService
     */
    function controller($log, $scope, rentService, navigationService) {
        $log.debug('Rent controller: execution');

        $scope.putForRent = function () {
            $log.debug(angular.toJson($scope.rentData));
            rentService.putForRent();
            //$scope.$watch('rentData', rentService.putForRent);
        };

        $(function () {
            navigationService.setRent();
        });
    }

    return {
        init: init,
        ctl: controller,
        srv: rentService(cfg.serviceName)
    };
})();

rentModule.init();
