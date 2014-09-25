/**
 * Created by null on 13.07.14.
 */
var renterSearchModule = (function () {
    'use strict';

    var cfg = {
        moduleName: 'rentApp.renterSearch',
        moduleDependencies: ['ui.router'],

        ctlName: 'RenterSearchController',
        serviceName: 'renterSearchService',
        stateName: 'renterSearchState',
        stateConfig: {
            url: '/renter-search',
            templateUrl: 'renter-search/renter-search-view.html',
            controller: 'RenterSearchController'
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

    function controller($log, $scope, renterSearchService, navigationService) {
        $log.debug('Renter search controller initialization');

        $(function () {
            navigationService.setRenterSearch();
        });
    }

    function service($log) {
        $log.debug('Renter search service init');
    }

    return {
        init: init,
        ctl: controller,
        srv: service
    };
})();

renterSearchModule.init();
