/**
 * angular module initialization and route setup
 */

/**
 * Set up our mappings between URLs, templates, and controllers
  */
function rentRouteConfig($routeProvider) {
    $routeProvider.
        when('/vi', { controller: RentController, templateUrl: 'components/rent/rent-view.html'}).
        otherwise({ redirectTo: '/'});
}

var rentModule = angular.module('RentModule', []);
rentModule.config(rentRouteConfig);

function RentController($scope) {

}