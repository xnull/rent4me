var componentsCfg = [
    {
        stateName: 'indexState',
        stateConfig: {
            url: '/',
            templateUrl: 'components/index/index-view.html',
            controller: 'IndexController'
        }
    },
    {
        stateName: 'rentState',
        stateConfig: {
            url: '/rent',
            templateUrl: 'components/rent/rent-view.html',
            controller: 'RentController'
        }
    },
    {
        stateName: 'loginState',
        stateConfig: {
            url: '/login',
            templateUrl: 'components/login/login-view.html',
            controller: 'LoginController'
        }
    },
    {
        stateName: 'registerState',
        stateConfig: {
            url: '/register',
            templateUrl: 'components/register/register-view.html'
        }
    },
    {
        stateName: 'rentSearchState',
        stateConfig: {
            url: '/rent-search',
            templateUrl: 'components/rent-search/rent-search-view.html'
            //controller: 'RentSearchController'
        }
    },
    {
        stateName: 'rentSearchState.searchForm',
        //url: '/rent-search/search',
        stateConfig: {
            views: {
                "rentSearchForm": {
                    templateUrl: 'rent-search-form.html'
                }
            }
        }
    },
    {
        stateName: 'renterSearchState',
        stateConfig: {
            url: '/renter-search',
            templateUrl: 'components/renter-search/renter-search-view.html',
            controller: 'RenterSearchController'
        }
    },
    {
        stateName: 'personalState',
        stateConfig: {
            url: '/personal',
            templateUrl: 'components/personal/personal-view.html',
            controller: 'PersonalController'
        }
    }
];

/**
 * Application configuration
 */
var appDependencies = [
    'ui.router', 'ngResource', 'ngCookies', /*, 'facebook'*/
    'rentApp.index', 'rentApp.rent', 'rentApp.auth', 'rentApp.navigation', 'rentApp.login',
    'rentApp.register', 'rentApp.rentSearch', 'rentApp.renterSearch', 'rentApp.personal'
];
var rentApplication = angular.module('rentApp', appDependencies);
var logger = angular.injector(['rentApp', 'ng']).get('$log');

rentApplication.config(function ($stateProvider, $httpProvider, $urlRouterProvider) {
    'use strict';

    logger.debug('Configure angular application');

    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];

    for (var i = 0; i < componentsCfg.length; i++) {
        var cfg = componentsCfg[i];
        $stateProvider.state(cfg.stateName, cfg.stateConfig);
    }

    $urlRouterProvider.otherwise('/');
});

logger.debug('Run angular application');
rentApplication.run(defaultSetup);
