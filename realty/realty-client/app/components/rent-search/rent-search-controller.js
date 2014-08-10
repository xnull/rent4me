/**
 * Created by null on 06.07.14.
 */
var rentSearchModule = (function () {
    'use strict';

    var cfg = {
        moduleName: 'rentApp.rentSearch',
        moduleDependencies: ['ui.router'],

        services: [
            {
                serviceName: 'rentSearchService',
                service: SearchService
            },
            {
                serviceName: 'rentSearchMapService',
                service: rentSearchMapModule.googleMapService
            }
        ],

        ctlName: 'RentSearchController',
        stateName: 'rentSearchState',
        stateConfig: {
            url: '/rent-search',
            templateUrl: 'components/rent-search/rent-search-view.html',
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
    function controller($log, $scope, rentSearchMapService, navigationService) {
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


    function SearchService($log, $resource) {

        var searchRequest = {
            floorsNumber: 1,
            metro: 'begovaya',
            address: 'kremlin'
        };

        var searchResponse = [
            {
                id: 1,
                lat: 55.752020,
                lng: 37.617526
            }
        ];

        /**
         * Send request to the server
         * @param searchRequest
         */
        function searchByParams(searchRequest) {
            var User = $resource('/rest/rent-search/:userId', {userId:'@id'});
            User.get({userId:123}, function(u, getResponseHeaders){
                u.abc = true;
                u.$save(function(u, putResponseHeaders) {
                    //u => saved user object
                    //putResponseHeaders => $http header getter
                });
            });
        }


        return {
            searchByParams: searchByParams
        };
    }

    return {
        init: init,
        ctl: controller,
        searchService: SearchService,
        googleMapsService: rentSearchMapModule.googleMapService
    };
})();

rentSearchModule.init();