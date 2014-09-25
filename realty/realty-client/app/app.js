/**
 * Application configuration
 */
require('../node_modules/client/angular-resource/angular-resource.js');
require('../node_modules/client/angular-ui-router/release/angular-ui-router.js');
require('../node_modules/client/google-maps-utility-library-v3/markerclusterer/src/markerclusterer.js');
require('../node_modules/client/angular-cookies/angular-cookies.js');
require('../node_modules/client/blockui/jquery.blockUI.js');

require('./components/index/index-controller.js');
require('./components/rent/rent-controller.js');
require('./components/navigation/navigation-controller.js');
require('./components/login/login-controller.js');
require('./components/authorization/authorization.js');
require('./components/register/register-controller.js');
require('./components/rent-search/rent-search-controller.js');
require('./components/personal/personal-controller.js');
require('./components/renter-search/renter-search-controller.js');
require('./components/apartment-info/apartment-info-controller.js');
require('./main.js');

var rentApplication = (function () {
    'use strict';

    var appName = 'rentApp';

    var moduleDependencies = [
        'html.templates',
        'ui.router', 'ngResource', 'ngCookies', /*, 'facebook'*/
        'rentApp.index', 'rentApp.rent', 'rentApp.auth', 'rentApp.navigation', 'rentApp.login',
        'rentApp.register', 'rentApp.rentSearch', 'rentApp.renterSearch', 'rentApp.personal', 'rentApp.apartmentInfo'
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
        },

        applicationName: appName
    };
})();

rentApplication.init();
module.exports = rentApplication;
