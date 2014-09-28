var rentService = require('./rent-service.js');
var configurator = require('../core/configurator.js');

var cfg = {
    moduleName: 'rentApp.rent',
    moduleDependencies: ['ui.router', 'rent/rent-view.html'],

    ctlName: 'RentController',
    serviceName: 'RentService',
    stateName: 'rentState',
    stateConfig: {
        url: '/rent',
        templateUrl: 'rent/rent-view.html',
        controller: 'RentController'
    },

    service: rentService(),

    /**
     * dynamic pages https://egghead.io/lessons/angularjs-using-resource-for-data-models
     * @param $log
     * @param $scope
     * @param RentService
     * @param navigationService
     */
    controller: function controller($log, $scope, RentService, navigationService) {
        $log.debug('Rent controller: execution');

        $scope.putForRent = function () {
            $log.debug(angular.toJson($scope.rentData));
            RentService.putForRent();
            //$scope.$watch('rentData', rentService.putForRent);
        };

        $(function () {
            navigationService.setRent();
        });
    }
};

configurator().configure(cfg);
