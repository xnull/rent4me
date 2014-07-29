/**
 * Application configuration
 */
var rentApplication = (function () {
    'use strict';

    var appName = 'rentApp';

    var moduleDependencies = [
        'ui.router', 'ngResource', 'ngCookies', /*, 'facebook'*/
        'rentApp.index', 'rentApp.rent', 'rentApp.auth', 'rentApp.navigation', 'rentApp.login',
        'rentApp.register', 'rentApp.rentSearch', 'rentApp.renterSearch', 'rentApp.personal'
    ];

    var angularApplication = angular.module(appName, moduleDependencies);
    var angularLogger = angular.injector([appName, 'ng']).get('$log');

    return {
        init: function () {
            angularApplication.config(function ($stateProvider, $httpProvider, $urlRouterProvider) {
                angularLogger.debug('Configure angular application');

                $httpProvider.defaults.useXDomain = true;
                delete $httpProvider.defaults.headers.common['X-Requested-With'];

                $urlRouterProvider.otherwise('/');
            });

            angularApplication.run(defaultSetup);
        }
    };
})();

rentApplication.init();
