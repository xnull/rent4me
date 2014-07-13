var componentsCfg = [
    {
        ctl: indexController,
        service: indexService,
        ctlName: 'IndexController',
        serviceName: 'indexService',
        route: '/',
        template: 'components/index/index-view.html'
    },
    {
        ctl: rentController,
        service: rentService,
        ctlName: 'RentController',
        serviceName: 'rentService',
        route: '/rent',
        template: 'components/rent/rent-view.html'
    },
    {
        ctl: loginController,
        service: loginService,
        ctlName: 'LoginController',
        serviceName: 'loginService',
        route: '/login',
        template: 'components/login/login-view.html'
    },
    {
        ctl: registerController,
        service: registerService,
        ctlName: 'RegisterController',
        serviceName: 'registerService',
        route: '/register',
        template: 'components/register/register-view.html'
    },
    {
        ctl: rentSearchController,
        service: rentSearchService,
        ctlName: 'RentSearchController',
        serviceName: 'rentSearchService',
        route: '/rent-search',
        template: 'components/rent-search/rent-search-view.html'
    },
    {
        ctl: renterSearchController,
        service: renterSearchService,
        ctlName: 'RenterSearchController',
        serviceName: 'renterSearchService',
        route: '/renter-search',
        template: 'components/renter-search/renter-search-view.html'
    },
    {
        ctl: personalController,
        service: personalService,
        ctlName: 'PersonalController',
        serviceName: 'personalService',
        route: '/personal',
        template: 'components/personal/personal-view.html'
    }
];

var backendComponents = [
    {
        ctl: authorizationController,
        service: authorizationService,
        ctlName: 'AuthorizationController',
        serviceName: 'authorizationService'
    },
    {
        ctl: navigationController,
        service: navigationService,
        ctlName: 'NavigationController',
        serviceName: 'navigationService'
    }
];

/**
 * Application configuration
 */

var rentApplication = angular.module('project', ['ngRoute', 'ngResource', 'ngCookies'/*, 'facebook'*/]);

rentApplication.config(function ($routeProvider, $httpProvider) {
    'use strict';

    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];


    for (var i = 0; i < componentsCfg.length; i++) {
        var cfg = componentsCfg[i];
        $routeProvider.when(cfg.route, {
            controller: cfg.ctlName,
            templateUrl: cfg.template
        });
    }

    $routeProvider.otherwise({
        redirectTo: '/'
    });
});

for (var i = 0; i < componentsCfg.length; i++) {
    rentApplication.controller(componentsCfg[i].ctlName, componentsCfg[i].ctl);
    rentApplication.factory(componentsCfg[i].serviceName, componentsCfg[i].service);
}

for (var i = 0; i < backendComponents.length; i++) {
    rentApplication.controller(backendComponents[i].ctlName, backendComponents[i].ctl);
    rentApplication.factory(backendComponents[i].serviceName, backendComponents[i].service);
}

rentApplication.run(defaultSetup);
