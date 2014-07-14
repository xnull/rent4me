/**
 * Created by null on 13.07.14.
 */
var renterSearchController = function ($log, $scope, renterSearchService, navigationService) {
    "use strict";

    $log.debug('Renter search controller initialization');

    $(function () {
        navigationService.setRenterSearch();
    });
};

var renterSearchService = function ($log) {
    "use strict";
};
