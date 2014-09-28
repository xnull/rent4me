/**
 * Created by null on 28.09.14.
 */
var googleMaps = require('./google-maps-service.js');

function controller($log, $scope, navigationService) {
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

module.exports = function () {
    return controller;
};