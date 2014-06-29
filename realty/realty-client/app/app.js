'use strict';

console.log('Create an angular app module');
var rentApplication = angular.module('project', ['ngRoute']);

var RENT_CONTROLLER_NAME = 'RentController';
var RENT_SERVICE_NAME = 'rentService';

rentApplication.config(function ($routeProvider) {
    console.log('Configure the angular app routes');
    $routeProvider.
        when('/rent', {
            controller: RENT_CONTROLLER_NAME,
            templateUrl: 'components/rent/rent-view.html'
        }).otherwise({
            redirectTo: '/'
        });
});

rentApplication.controller(RENT_CONTROLLER_NAME, rentController);
rentApplication.factory(RENT_SERVICE_NAME, rentService);
