/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var AuthConstants = require('../constants/AuthConstants');
var AuthStore = require('../stores/AuthStore');
var BlockUI = require('../common/BlockUI');
var Ajax = require('../common/Ajax');
var Utils = require('../common/Utils');
var assign = require('object-assign');

var AuthActions = {
    /**
     *
     */
    loginWithVK: function () {
        var vkAppId = AuthStore.getVkId();
        var redirectHost = Utils.isLocalhost() ? "http://localhost:8888/dev" : (Utils.isProduction() ? "http://rent4.me" :  "http://dev.rent4.me" ) ;
        var redirectUrl = redirectHost + "/vk_auth_return_page";
        var permissions = "email";
        document.location.href="https://oauth.vk.com/authorize?client_id="+vkAppId+"&scope="+permissions+"&redirect_uri="+redirectUrl+"&response_type=code&v=5.27";
    },

    authOnBackendWithVK: function (vkAccessCode) {
        BlockUI.blockUI();

        var that = this;

        var data = {"code": vkAccessCode};

        console.log('data to send');
        console.log(data);

        Ajax
            .POST('/rest/auth/vk')
            .withJsonBody(data)
            .onSuccess(function (data) {
                console.log("Success!");
                console.log("Data:");
                console.log(data);

                AppDispatcher.handleViewAction({
                    actionType: AuthConstants.AUTH_AUTHENTICATED_WITH_VK,
                    data: data
                });

                BlockUI.unblockUI();

                that.storeUsernameAndTokenInCookies();

                Utils.navigateToPersonal();
            })
            .onError(function (xhr, status, err) {
                //                    console.error('/rest/apartment', status, err.toString());
                console.log("Error!");

                BlockUI.unblockUI();

                AppDispatcher.handleViewAction({
                    actionType: AuthConstants.AUTH_AUTHENTICATION_ERROR
                });

                alert("Не удалось авторизоваться через VK");
                Utils.navigateToStart();
            })
            .execute();
    },

    statusChangeCallback: function (response) {
        console.log(response);
        if (response.status === 'connected') {

            AppDispatcher.handleViewAction({
                actionType: AuthConstants.AUTH_FB_AUTH_STATE_CHANGED,
                response: response
            });

            this.authOnBackendWithFacebook();
        } else if (response.status === 'not_authorized') {
            // The person is logged into Facebook, but not your app.
//            document.getElementById('status').innerHTML = 'Please log ' +
//                    'into this app.';
        } else {
            // The person is not logged into Facebook, so we're not sure if
            // they are logged into this app or not.
//            document.getElementById('status').innerHTML = 'Please log ' +
//                    'into Facebook.';
        }
    },

    authOnBackendWithFacebook: function () {
        BlockUI.blockUI();

        var that = this;

        var fbUserIdAccessTokenPair = AuthStore.getFbUserIdAccessTokenPair();

        var data = {"facebook_id": fbUserIdAccessTokenPair.fb_id, "access_token": fbUserIdAccessTokenPair.fb_access_token};

        console.log('data to send');
        console.log(data);

        Ajax
            .POST('/rest/auth/facebook')
            .withJsonBody(data)
            .onSuccess(function (data) {
                console.log("Success!");
                console.log("Data:");
                console.log(data);

                AppDispatcher.handleViewAction({
                    actionType: AuthConstants.AUTH_AUTHENTICATED_WITH_FACEBOOK,
                    data: data
                });

                BlockUI.unblockUI();

                that.storeUsernameAndTokenInCookies();

                Utils.navigateToPersonal();
            })
            .onError(function (xhr, status, err) {
                //                    console.error('/rest/apartment', status, err.toString());
                console.log("Error!");

                BlockUI.unblockUI();

                AppDispatcher.handleViewAction({
                    actionType: AuthConstants.AUTH_AUTHENTICATION_ERROR
                });

                alert("Не удалось авторизоваться через Facebook");
                Utils.navigateToStart();
            })
            .execute();
    },

    checkLoginState: function () {
        var that = this;

        FB.getLoginStatus(function(response) {
            that.statusChangeCallback(response);
        });
    },

    loginWithFB: function () {
        console.log('Atttttentione');
        console.log(FB);

        var that = this;

        var opts = {
            scope: 'public_profile,user_friends,email'
        };
        FB.login(function(response) {
            that.statusChangeCallback(response);
        }, opts);
    },

    restoreUsernameAndTokenFromCookies: function () {
        AppDispatcher.handleViewAction({
            actionType: AuthConstants.AUTH_RE_STORE_USERNAME_AND_TOKEN_FROM_COOKIES
        });
    },

    storeUsernameAndTokenInCookies: function () {
        AppDispatcher.handleViewAction({
            actionType: AuthConstants.AUTH_STORE_USERNAME_AND_TOKEN_IN_COOKIES
        });
    },

    logoutOnBackend: function () {
        if(AuthStore.hasCredentials()) {
            var usernameTokenPair = AuthStore.getUsernameTokenPair();
            console.log('Username token pair: ');
            console.log(usernameTokenPair);
            var token = usernameTokenPair.token;
            var data = {"token" : token};
            console.log('data: ');
            console.log(data);

            BlockUI.blockUI();

            Ajax
                .DELETE('/rest/auth')
                .authorized()
                .withJsonBody(data)
                .onSuccess(function (data) {
                    console.log("Success!");
                    console.log("Data:");

                    AppDispatcher.handleViewAction({
                        actionType: AuthConstants.AUTH_USER_LOGGED_OUT
                    });

                    BlockUI.unblockUI();

                    Utils.navigateToStart();
                })
                .onError(function (xhr, status, err) {
                    AppDispatcher.handleViewAction({
                        actionType: AuthConstants.AUTH_AUTHENTICATION_ERROR
                    });

//                    console.error('/rest/apartment', status, err.toString());
                    console.log("Error!");
                    BlockUI.unblockUI();
                })
                .execute();
        }
    }

};

module.exports = AuthActions;
