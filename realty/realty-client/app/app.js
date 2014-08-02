/**
 * Application configuration
 */
var scriptLoader = (function () {
    'use strict';

    var scripts = [
        'components/index/index-controller.js',
        'components/rent/rent-controller.js',
        'components/navigation/navigation-controller.js',
        'components/login/login-controller.js',
        'components/authorization/authorization.js',
        'components/register/register-controller.js',
        'components/rent-search/rent-search-controller.js',
        'components/personal/personal-controller.js',
        'components/renter-search/renter-search-controller.js',
        'main.js'
    ];

    function loadScript(url) {
        document.write('<' + 'script src="' + url + '"' + ' type="text/javascript"><' + '/script>');
    }

    function loadScripts() {
        for (var i = 0; i < scripts.length; ++i) {
            loadScript(scripts[i]);
        }
    }

    return{
        loadScripts: loadScripts
    };
})();

scriptLoader.loadScripts();

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
        },

        applicationName: appName
    };
})();

rentApplication.init();
