/**
 * Created by null on 06.07.14.
 */
var mapScript = require('./google-maps-service.js');
var service = require('./service.js');
var controller = require('./controller.js');
var configurator = require('../core/configurator.js');

var cfg = {
    moduleName: 'rentApp.rentSearch',
    moduleDependencies: ['ui.router', 'rent-search/rent-search-form.html', 'rent-search/rent-search-view.html'],

    ctlName: 'RentSearchController',
    stateName: 'rentSearchState',
    stateConfig: {
        url: '/rent-search',
        templateUrl: 'rent-search/rent-search-view.html',
        controller: 'RentSearchController'
    },

    serviceName: 'RentSearchService',
    service: service(),

    /**
     * Google maps and angularjs http://www.simplecoding.org/google-maps-angularjs-pozicionirovanie-karty.html
     * @param $log
     * @param $scope
     * @param rentSearchService
     * @param navigationService
     */
    controller: controller()
};

configurator().configure(cfg);