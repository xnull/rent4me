/**
 * Created by null on 13.07.14.
 */
var renterSearchModuleCfg = {
    ctlName: 'RenterSearchController',
    serviceName: 'renterSearchService',
    stateName: 'renterSearchState',
    stateConfig: {
        url: '/renter-search',
        templateUrl: 'components/renter-search/renter-search-view.html',
        controller: 'RenterSearchController'
    }
};

var renterSearchModule = angular.module('rentApp.renterSearch', ['ui.router']);

renterSearchModule.config(function ($stateProvider) {
    'use strict';
    $stateProvider.state(renterSearchModuleCfg.stateName, renterSearchModuleCfg.stateConfig);
});

renterSearchModule.controller(renterSearchModuleCfg.ctlName, function ($log, $scope, renterSearchService, navigationService) {
    "use strict";

    $log.debug('Renter search controller initialization');

    $(function () {
        navigationService.setRenterSearch();
    });
});

renterSearchModule.factory(renterSearchModuleCfg.serviceName, function ($log) {
    "use strict";

    $log.debug('Renter search service init');
});
