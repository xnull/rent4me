/**
 * Created by null on 28.09.14.
 */

var validator = require('../core/validator.js');

function AuthCtl($scope, $log, $cookies, AuthService) {
    "use strict";

    //validator().checkUndefinedBatch([$scope, $log, $cookies, AuthService]);

    var logPrefix = "authCtrl ";

    $scope.authInfo = {
        authorized: AuthService.isAuthorized()
    };


//    $scope.authorized = AuthService.isAuthorized();

    $log.info(logPrefix + "Authorized? " + $scope.authInfo.authorized);

    //var _self = this;

    $log.info(logPrefix + "Binding for scope event: " + AuthService.EVENT_AUTH_STATE_CHANGED);
    $scope.$on(AuthService.EVENT_AUTH_STATE_CHANGED, function (event, args) {
        $log.info(logPrefix + "Received event: " + AuthService.EVENT_AUTH_STATE_CHANGED);

//        $scope.$apply(
//            function () {
        $log.info('args');
        $log.info(args);

        $log.info(logPrefix + "Setting authorized to " + args.authorized);
        $scope.authInfo.authorized = args.authorized;
//        $scope.$apply();
//        });
    });

//    $scope.$watch(AuthService.EVENT_AUTH_STATE_CHANGED, function(){
//        $log.info(logPrefix+"Received event on watch: "+AuthService.EVENT_AUTH_STATE_CHANGED);
//        var authorized = AuthService.isAuthorized();
//        $log.info(logPrefix+"Setting authorized to(on watch) "+authorized);
//        $scope.authInfo.authorized = authorized;
//    });

    this.loginWithFacebook = AuthService.loginWithFacebook;
    this.loginWithVK = AuthService.loginWithVK;
    this.logout = AuthService.logout;

}

module.exports = function () {
    return AuthCtl;
};