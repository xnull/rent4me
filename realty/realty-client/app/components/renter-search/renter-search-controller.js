/**
 * Created by null on 13.07.14.
 */
var renterSearchModule = angular.module('rentApp.renterSearch', []);

renterSearchModule.controller('RenterSearchController', function ($log, $scope, renterSearchService, navigationService) {
    "use strict";

    $log.debug('Renter search controller initialization');

    $(function () {
        navigationService.setRenterSearch();
    });
});

renterSearchModule.factory('renterSearchService', function ($log) {
    "use strict";

    $log.debug('Renter search service init');
});
