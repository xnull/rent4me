/**
 * Application configuration
 */
require('../node_modules/client/angular-resource/angular-resource.js');
require('../node_modules/client/angular-ui-router/release/angular-ui-router.js');
require('../node_modules/client/google-maps-utility-library-v3/markerclusterer/src/markerclusterer.js');
require('../node_modules/client/angular-cookies/angular-cookies.js');
require('../node_modules/client/blockui/jquery.blockUI.js');

var indexModule = require('./components/index/index-module.js');
var rentModule = require('./components/rent/rent-module.js');
require('./components/navigation/navigation-module.js');
var loginModule = require('./components/login/login-module.js');
require('./components/authorization/auth-module.js');
var registerModule = require('./components/register/register-module.js');
var rentSearchModule = require('./components/rent-search/module.js');
var personalModule = require('./components/personal/personal-module.js');
var renterSearchModule = require('./components/renter-search/renter-search-module.js');
var apartmentModule = require('./components/apartment-info/module.js');
var mainModule = require('./main.js');

var rentApplication = (function () {
    'use strict';

    var appName = 'rentApp';

    var moduleDependencies = [
        'ui.router', 'ngResource', 'ngCookies', /*, 'facebook'*/
        'rentApp.index', 'rentApp.rent', 'rentApp.auth', 'rentApp.navigation', 'rentApp.login',
        'rentApp.register', 'rentApp.rentSearch', 'rentApp.renterSearch', 'rentApp.personal', 'rentApp.apartmentInfo'
    ];

    var angularApplication = angular.module(appName, moduleDependencies);
    var angularLogger = angular.injector([appName, 'ng']).get('$log');

    return {
        init: function () {
            angularLogger.debug('Init rent app');

            angularApplication.config(function ($stateProvider, $httpProvider, $urlRouterProvider) {
                angularLogger.debug('Configure rent application');

                $httpProvider.defaults.useXDomain = true;
                delete $httpProvider.defaults.headers.common['X-Requested-With'];

                $urlRouterProvider.otherwise('/');
            });

            angularApplication.run(mainModule());
        },

        applicationName: appName
    };
})();

rentApplication.init();
