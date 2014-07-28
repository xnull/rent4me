/**
 * Application configuration
 */
var moduleDependencies = [
    'ui.router', 'ngResource', 'ngCookies', /*, 'facebook'*/
    'rentApp.index', 'rentApp.rent', 'rentApp.auth', 'rentApp.navigation', 'rentApp.login',
    'rentApp.register', 'rentApp.rentSearch', 'rentApp.renterSearch', 'rentApp.personal'
];
var rentApplication = angular.module('rentApp', moduleDependencies);
var logger = angular.injector(['rentApp', 'ng']).get('$log');

rentApplication.config(function ($stateProvider, $httpProvider, $urlRouterProvider) {
    'use strict';

    logger.debug('Configure angular application');

    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];

    $urlRouterProvider.otherwise('/');
});

logger.debug('Run angular application');
rentApplication.run(defaultSetup);
