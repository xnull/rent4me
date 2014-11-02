var configurator = require('../core/configurator.js');
var validator = require('../core/validator.js');

function controller($scope, $log, loginService, navigationService, AuthService) {
    $log.debug('Login controller execution');

    validator.checkUndefinedBatch([$scope, $log, loginService, navigationService, AuthService]);

    $scope.login = loginService.login;

    $scope.navService = navigationService;

    $scope.authorized = AuthService.isAuthorized();

    $scope.$on(AuthService.EVENT_AUTH_STATE_CHANGED, function (event, args) {

        $log.info('Event');
        $log.info(event);
        $log.info('args');
        $log.info(args);
        $log.info("login_controller, setting to " + args.authorized);
//        var authorized = AuthService.isAuthorized();
        $scope.authorized = args.authorized;
    });


    $scope.loginWithFacebook = AuthService.loginWithFacebook;
    $scope.loginWithVK = AuthService.loginWithVK;
    $scope.logout = AuthService.logout;


    $(function () {
        navigationService.setLogin();
    });
//    $scope.greeting = loginService.hello();
}

function service($http) {
    //validator().checkUndefined($http);

    return {
        login: function () {
            $http({method: 'POST', url: 'login'}).
                success(function (data, status, headers, config) {
                    // this callback will be called asynchronously
                    // when the response is available
                    //console.log('Successful sending ajax request');
                }).
                error(function (data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    //console.log('Error sending ajax request. Status: ' + status);
                    //$scope.greeting = "raz dva";
                });
        }
    };
}

var cfg = {
    moduleName: 'rentApp.login',
    moduleDependencies: ['ui.router', 'login/login-view.html'],

    ctlName: 'LoginController',
    serviceName: 'loginService',
    stateName: 'loginState',
    stateConfig: {
        url: '/login',
        templateUrl: 'login/login-view.html',
        controller: 'LoginController'
    },

    service: service,
    controller: controller
};

configurator().configure(cfg);