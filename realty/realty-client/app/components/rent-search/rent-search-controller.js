/**
 * Created by null on 06.07.14.
 */
var mapScript = require('./google-maps-service.js');
var rentSearchService = require('./rent-search-service.js');

var rentSearchModule = (function () {
    'use strict';

    var cfg = {
        moduleName: 'rentApp.rentSearch',
        moduleDependencies: ['ui.router'],

        services: [
            {
                serviceName: 'RentSearchService',
                service: rentSearchService('RentSearchService')
            },
            {
                serviceName: 'RentSearchMapService',
                service: mapScript.googleMapService
            }
        ],

        ctlName: 'RentSearchController',
        stateName: 'rentSearchState',
        stateConfig: {
            url: '/rent-search',
            templateUrl: 'rent-search/rent-search-view.html',
            controller: 'RentSearchController'
        }
    };

    function init() {
        var angularModule = angular.module(cfg.moduleName, cfg.moduleDependencies);
        var angularLogger = angular.injector([cfg.moduleName, 'ng']).get('$log');

        angularLogger.debug('Loading "' + cfg.moduleName + '" module');

        angularModule.config(function ($stateProvider) {
            $stateProvider.state(cfg.stateName, cfg.stateConfig);
        });

        angularModule.controller(cfg.ctlName, controller);

        cfg.services.forEach(function (service) {
            angularModule.factory(service.serviceName, service.service);
        });
    }

    /**
     * Google maps and angularjs http://www.simplecoding.org/google-maps-angularjs-pozicionirovanie-karty.html
     * @param $log
     * @param $scope
     * @param rentSearchService
     * @param navigationService
     */
    function controller($log, $scope, RentSearchMapService, navigationService) {
        $log.debug('Rent search controller initialization');

        $scope.apartment = {};
        $scope.search = function () {
            $log.debug($scope.apartment);
        };

        /* Dropdown menu see commented component on view
         $(function() {
         // Setup drop down menu
         $('.dropdown-toggle').dropdown();

         // Fix input element click problem
         $('.dropdown input, .dropdown label').click(function(e) {
         e.stopPropagation();
         });
         });*/


        $(function () {
            navigationService.setRentSearch();
        });
    }

    return {
        init: init,
        ctl: controller,
        searchService: rentSearchService('RentSearchService'),
        googleMapsService: mapScript.googleMapService
    };
})();

rentSearchModule.init();