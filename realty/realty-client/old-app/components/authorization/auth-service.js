var validator = require('../core/validator.js');

function AuthService($http, $resource, $rootScope, $log, $cookies) {
    'use strict';

    //validator().checkUndefinedBatch([$http, $resource, $rootScope, $log, $cookies]);

    var logPrefix = "auth_service ";
    var authorization = {};

//    authorization.fbUser = null;
    authorization.username = $cookies.username && $cookies.username !== 'null' ? $cookies.username : null;
    authorization.token = $cookies.token && $cookies.token !== 'null' ? $cookies.token : null;

    authorization.EVENT_AUTH_STATE_CHANGED = 'EVENT_AUTH_STATE_CHANGED';
    authorization.EVENT_USERNAME_CHANGED = 'EVENT_USERNAME_CHANGED';

    authorization.setUsername = function (val) {
        authorization.username = val;
        if (val === null) {
            $log.info('Username is null, deleting cookie');
            delete $cookies.username;
        } else {
            $cookies.username = val;
        }
    };

    authorization.setToken = function (val) {
        authorization.token = val;
        if (val === null) {
            $log.info('Token is null, deleting cookie');
            delete $cookies.token;
        } else {
            $cookies.token = val;
        }
    };

    authorization.authStateChanged = function () {
        $log.info(logPrefix + "Broadcasting event " + authorization.EVENT_AUTH_STATE_CHANGED);
        $rootScope.$broadcast(authorization.EVENT_AUTH_STATE_CHANGED, {authorized: authorization.isAuthorized()});
    };


//    authorization.setFacebookUser = function (val) {
//        authorization.fbUser = val;
//        $log.info(logPrefix + "Broadcasting event " + authorization.EVENT_AUTH_STATE_CHANGED);
//        $rootScope.$broadcast(authorization.EVENT_AUTH_STATE_CHANGED);
//    };
    authorization.isAuthorized = function () {
        return authorization.token !== null;
    };

    authorization.loginWithFacebook = function () {
        $log.info("login with FB");
        FB.login(function (response) {
                // handle the response
                $log.debug("Response after login:");
                $log.debug(response);

                if (response.authResponse) {
                    $log.info(logPrefix + 'Welcome!  Fetching your information.... ');
                    var data = {
                        "facebook_id": response.authResponse.userID,
                        "access_token": response.authResponse.accessToken
                    };
                    $log.info(data);
                    $log.info("JSON:");
                    $log.info(angular.toJson(data));

                    $.blockUI();
                    $http({
                        method: 'POST',
                        url: '/rest/auth/facebook',
                        data: angular.toJson(data)
                    }).
                        success(function (data, status, headers, config) {
                            $log.info("Success!");
                            $log.info("Status:" + status);
                            $log.info("Data:");
                            $log.info(data);

                            authorization.setToken(data.token);
                            authorization.setUsername(data.username);
                            authorization.authStateChanged();
                            // this callback will be called asynchronously
                            // when the response is available
                            //console.log('Successful sending ajax request');
                            $.unblockUI();
                        }).
                        error(function (data, status, headers, config) {
                            $log.info("Error!");
                            $log.info(data);
                            $log.info(status);
                            $log.info(headers);
                            $log.info(config);
                            authorization.setToken(null);
                            authorization.setUsername(null);
                            authorization.authStateChanged();
                            $.unblockUI();
                            // called asynchronously if an error occurs
                            // or server returns response with an error status.
                            //console.log('Error sending ajax request. Status: ' + status);
                            //$scope.greeting = "raz dva";
                        });


//                    authorization.setFacebookUser(true);
//                    FB.api('/me', function (response) {
//                        $log.info(logPrefix + 'Good to see you, ' + response.name + '.');
//                    });
                } else {
                    $log.info(logPrefix + 'User cancelled login or did not fully authorize.');
                    authorization.logout();
                }
            },
            //see other permissions: https://developers.facebook.com/docs/facebook-login/permissions/v2.0
            {scope: 'public_profile, email'}
        );

    };

    authorization.__base64Encode = function (data) {
        //  discuss at: http://phpjs.org/functions/base64_encode/
        // original by: Tyler Akins (http://rumkin.com)
        // improved by: Bayron Guevara
        // improved by: Thunder.m
        // improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
        // improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
        // improved by: Rafał Kukawski (http://kukawski.pl)
        // bugfixed by: Pellentesque Malesuada
        //   example 1: base64_encode('Kevin van Zonneveld');
        //   returns 1: 'S2V2aW4gdmFuIFpvbm5ldmVsZA=='
        //   example 2: base64_encode('a');
        //   returns 2: 'YQ=='
        //   example 3: base64_encode('✓ à la mode');
        //   returns 3: '4pyTIMOgIGxhIG1vZGU='

        var b64 = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
        var o1, o2, o3, h1, h2, h3, h4, bits, i = 0,
            ac = 0,
            enc = '',
            tmp_arr = [];

        if (!data) {
            return data;
        }

        data = unescape(encodeURIComponent(data));

        do {
            // pack three octets into four hexets
            o1 = data.charCodeAt(i++);
            o2 = data.charCodeAt(i++);
            o3 = data.charCodeAt(i++);

            bits = o1 << 16 | o2 << 8 | o3;

            h1 = bits >> 18 & 0x3f;
            h2 = bits >> 12 & 0x3f;
            h3 = bits >> 6 & 0x3f;
            h4 = bits & 0x3f;

            // use hexets to index into b64, and append result to encoded string
            tmp_arr[ac++] = b64.charAt(h1) + b64.charAt(h2) + b64.charAt(h3) + b64.charAt(h4);
        } while (i < data.length);

        enc = tmp_arr.join('');

        var r = data.length % 3;

        return (r ? enc.slice(0, r - 3) : enc) + '==='.slice(r || 3);
    };


    authorization.logout = function () {
        var token = authorization.token;
        var username = authorization.username;

        var data = {"token": token};

        $.blockUI();
        $http({
            method: 'DELETE',
            url: '/rest/auth',
            data: angular.toJson(data),
            headers: {
                "Authorization": "Basic " + authorization.__base64Encode(username + ":" + token),
                "Content-Type": "application/json"
            }
        }).success(function (data, status, headers, config) {
            $log.info("Success!");
            $log.info("Status:" + status);
            $log.info("Data:");
            $log.info(data);

            authorization.setToken(null);
            authorization.setUsername(null);
            authorization.authStateChanged();
            $.unblockUI();
            // this callback will be called asynchronously
            // when the response is available
            //console.log('Successful sending ajax request');
        }).
            error(function (data, status, headers, config) {
                $log.info("Error!");
                $log.info(data);
                $log.info(status);
                $log.info(headers);
                $log.info(config);
                authorization.setToken(null);
                authorization.setUsername(null);
                authorization.authStateChanged();
//                alert(status);
                $.unblockUI();

//                authorization.setToken(null);
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                //console.log('Error sending ajax request. Status: ' + status);
                //$scope.greeting = "raz dva";
            });

//        authorization.setFacebookUser(null);
    };

    authorization.loginWithVK = function () {

        $log.info("login with VK");

        function authInfo(response) {
            $log.info('VK response:');
            $log.info(response);
            if (response.session) {
                $log.info('VK response session:');
                $log.info(response.session);
                $log.info('user: ' + response.session.mid);
                $log.info('secret id: ' + response.session.sid);
                $log.info('sig: ' + response.session.sig);

                $.blockUI();

                var data = {"vk_id": response.session.mid, "access_token": response.session.sig};

                $http({
                    method: 'POST',
                    url: '/rest/auth/vk',
                    data: angular.toJson(data)
                }).
                    success(function (data, status, headers, config) {
                        $log.info("Success!");
                        $log.info("Status:" + status);
                        $log.info("Data:");
                        $log.info(data);

                        authorization.setToken(data.token);
                        authorization.setUsername(data.username);
                        authorization.authStateChanged();
                        // this callback will be called asynchronously
                        // when the response is available
                        //console.log('Successful sending ajax request');
                        $.unblockUI();
                    }).
                    error(function (data, status, headers, config) {
                        $log.info("Error!");
                        $log.info(data);
                        $log.info(status);
                        $log.info(headers);
                        $log.info(config);
                        authorization.setToken(null);
                        authorization.setUsername(null);
                        authorization.authStateChanged();
                        $.unblockUI();
                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                        //console.log('Error sending ajax request. Status: ' + status);
                        //$scope.greeting = "raz dva";
                    });

            } else {
                $log.info('not authorized in VK');
                $.blockUI();
                /*VK.Auth.login(function (response) {
                 if (response.session) {
                 // Пользователь успешно авторизовался
                 if (response.settings) {
                 // Выбранные настройки доступа пользователя, если они были запрошены
                 }
                 } else {
                 // Пользователь нажал кнопку Отмена в окне авторизации
                 }
                 $.unblockUI();
                 });*/
            }
            $.unblockUI();
        }

        $.blockUI();

        VK.Auth.getLoginStatus(authInfo);
    };

    authorization.logoutWithFacebook = function () {
        FB.logout(function (response) {
            // user is now logged out
            $log.info(logPrefix + "Logging out...");
            authorization.logout();
        });
    };

    return authorization;
}

module.exports = function () {
    return AuthService;
};
