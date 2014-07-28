var rentModuleCfg = {
    ctlName: 'RentController',
    serviceName: 'rentService',
    stateName: 'rentState',
    stateConfig: {
        url: '/rent',
        templateUrl: 'components/rent/rent-view.html',
        controller: 'RentController'
    }
};

var rentModule = angular.module('rentApp.rent', ['ui.router']);

rentModule.config(function ($stateProvider) {
    'use strict';
    $stateProvider.state(rentModuleCfg.stateName, rentModuleCfg.stateConfig);
});

/**
 * dynamic pages https://egghead.io/lessons/angularjs-using-resource-for-data-models
 * @param $log
 * @param $scope
 * @param rentService
 * @param navigationService
 */
rentModule.controller(rentModuleCfg.ctlName, function ($log, $scope, rentService, navigationService) {
    "use strict";
    $log.debug('Rent controller: execution');

    $scope.putForRent = function () {
        $log.debug(angular.toJson($scope.rentData));
        rentService.putForRent($scope);
        //$scope.$watch('rentData', rentService.putForRent);
    };

    $(function () {
        navigationService.setRent();
    });
});

rentModule.factory(rentModuleCfg.serviceName, function ($resource, $log) {
    "use strict";

    return {
        hello: function () {
            return 'hey hello';
        },

        putForRent: function ($scope) {
            $log.debug('Rent service: put for rent');

            var putResource = $resource('putForRent');
            putResource.save(angular.toJson($scope.rentData));

            putResource.query(function (response) {
                $scope.rentData = response;
            });
        }
    };
});
