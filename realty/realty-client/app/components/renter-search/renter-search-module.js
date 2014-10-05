/**
 * Created by null on 13.07.14.
 */
var configurator = require('../core/configurator.js');

function controller($log, $scope, renterSearchService, navigationService) {
    $log.debug('Renter search controller initialization');d

    $(function () {
        navigationService.setRenterSearch();
    });
}

function service($log) {
    $log.debug('Renter search service init');
}

var cfg = {
    moduleName: 'rentApp.renterSearch',
    moduleDependencies: ['ui.router', 'renter-search/renter-search-view.html'],

    ctlName: 'RenterSearchController',
    serviceName: 'renterSearchService',
    stateName: 'renterSearchState',
    stateConfig: {
        url: '/renter-search',
        templateUrl: 'renter-search/renter-search-view.html',
        controller: 'RenterSearchController'
    },

    controller: controller,
    service: service
};

configurator().configure(cfg);