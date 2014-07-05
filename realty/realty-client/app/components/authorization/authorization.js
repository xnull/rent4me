var authorizationController = function ($scope, $log, authorizationService) {
    var log_prefix = "auth_ctrl ";

    $scope.auth_info = {
        authorized: authorizationService.isAuthorized()
    };


//    $scope.authorized = authorizationService.isAuthorized();

    $log.info(log_prefix + "Authorized? " + $scope.auth_info.authorized);

    var _self = this;

    $log.info(log_prefix + "Binding for scope event: " + authorizationService.EVENT_AUTH_STATE_CHANGED);
    $scope.$on(authorizationService.EVENT_AUTH_STATE_CHANGED, function () {
        $log.info(log_prefix + "Received event: " + authorizationService.EVENT_AUTH_STATE_CHANGED);

        $scope.$apply(function () {
            var authorized = authorizationService.isAuthorized();
            $log.info(log_prefix + "Setting authorized to " + authorized);
            $scope.auth_info.authorized = authorized;
        });
    });

//    $scope.$watch(authorizationService.EVENT_AUTH_STATE_CHANGED, function(){
//        $log.info(log_prefix+"Received event on watch: "+authorizationService.EVENT_AUTH_STATE_CHANGED);
//        var authorized = authorizationService.isAuthorized();
//        $log.info(log_prefix+"Setting authorized to(on watch) "+authorized);
//        $scope.auth_info.authorized = authorized;
//    });

    this.loginWithFacebook = authorizationService.loginWithFacebook;
    this.logout = authorizationService.logoutWithFacebook;

};


var authorizationService = function ($rootScope, $log) {
    var log_prefix = "auth_service ";
    var authorization = {};

    authorization.fbUser = null;
    authorization.EVENT_AUTH_STATE_CHANGED = 'EVENT_AUTH_STATE_CHANGED';

    authorization.setFacebookUser = function (val) {
        authorization.fbUser = val;
        $log.info(log_prefix + "Broadcasting event " + authorization.EVENT_AUTH_STATE_CHANGED);
        $rootScope.$broadcast(authorization.EVENT_AUTH_STATE_CHANGED);
    };
    authorization.isAuthorized = function () {
        return authorization.fbUser !== null;
    };
    authorization.loginWithFacebook = function () {
        $log.info("login with FB");
        FB.login(function (response) {
                // handle the response
                console.log("Response after login:");
                console.log(response);

                if (response.authResponse) {
                    $log.info(log_prefix + 'Welcome!  Fetching your information.... ');
                    authorization.setFacebookUser(true);
                    FB.api('/me', function (response) {
                        $log.info(log_prefix + 'Good to see you, ' + response.name + '.');
                    });
                } else {
                    $log.info(log_prefix + 'User cancelled login or did not fully authorize.');
                    authorization.setFacebookUser(null);
                }
            },
            //see other permissions: https://developers.facebook.com/docs/facebook-login/permissions/v2.0
            {scope: 'public_profile, email'}
        );

    };
    authorization.logoutWithFacebook = function () {
        FB.logout(function (response) {
            // user is now logged out
            $log.info(log_prefix + "Logging out...");
            authorization.setFacebookUser(null);
        });
    };

    return  authorization;
};
