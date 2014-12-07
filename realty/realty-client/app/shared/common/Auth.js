//var $ = require('jquery');
var Cookies = require('./Cookies');

var JSON = require('JSON2');

var Utils = require('./Utils');
var BlockUI = require('./BlockUI');


var _username = null;
var _token = null;
var _fbUserId = null;
var _fbAccessToken = null;


function getFbId() {
    var isLocalhost = Utils.isLocalhost();

    var fbAppId;
    if (isLocalhost) {
        fbAppId = '271375949714661';
    } else {
        fbAppId = '270007246518198';
    }

    return fbAppId;
}

function getVkId() {
    var vkAppId;

    vkAppId = '4463597';


    return vkAppId;
}

function loginWithVK() {
    var vkAppId = getVkId();
    var redirectHost = Utils.isLocalhost() ? "http://localhost:8888/dev" : (Utils.isProduction() ? "http://rent4.me" :  "http://rent4.me/dev" ) ;
    var redirectUrl = redirectHost + "/vk_auth_return_page";
    var permissions = "email";
    document.location.href="https://oauth.vk.com/authorize?client_id="+vkAppId+"&scope="+permissions+"&redirect_uri="+redirectUrl+"&response_type=code&v=5.27";
}

function authOnBackendWithVK(vkAccessCode) {
    BlockUI.blockUI();

    var data = {"code": vkAccessCode};

    console.log('data to send');
    console.log(data);

    $.ajax({
        url: '/rest/auth/vk',
        dataType: 'json',
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        beforeSend: function (request) {
//                request.setRequestHeader("Authorization", "Basic " + Auth.getAuthHeader());
        },
        success: function (data) {
            console.log("Success!");
            console.log("Data:");
            console.log(data);

            _username = data.username;
            _token = data.token;

            storeUsernameAndTokenInCookies();

            BlockUI.unblockUI();

            Utils.navigateToPersonal();
        },
        error: function (xhr, status, err) {
//                    console.error('/rest/apartment', status, err.toString());
            console.log("Error!");
            _username = null;
            _token = null;
            _fbUserId = null;
            _fbAccessToken = null;
            BlockUI.unblockUI();
            alert("Не удалось авторизоваться через VK");
            Utils.navigateToStart();
        }
    });
}

function statusChangeCallback(response) {
    console.log(response);
    if (response.status === 'connected') {


        _fbUserId = response.authResponse.userID;
        _fbAccessToken = response.authResponse.accessToken;

        authOnBackendWithFacebook();
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
}

function authOnBackendWithFacebook() {
    BlockUI.blockUI();

    var data = {"facebook_id": _fbUserId, "access_token": _fbAccessToken};

    console.log('data to send');
    console.log(data);

    $.ajax({
        url: '/rest/auth/facebook',
        dataType: 'json',
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        beforeSend: function (request) {
//                request.setRequestHeader("Authorization", "Basic " + Auth.getAuthHeader());
        },
        success: function (data) {
            console.log("Success!");
            console.log("Data:");
            console.log(data);

            _username = data.username;
            _token = data.token;

            storeUsernameAndTokenInCookies();

            BlockUI.unblockUI();

            Utils.navigateToPersonal();
        },
        error: function (xhr, status, err) {
//                    console.error('/rest/apartment', status, err.toString());
            console.log("Error!");
            _username = null;
            _token = null;
            _fbUserId = null;
            _fbAccessToken = null;
            BlockUI.unblockUI();
            alert("Не удалось авторизоваться через Facebook");
            Utils.navigateToStart();
        }
    });
}

function checkLoginState() {
    FB.getLoginStatus(function(response) {
        statusChangeCallback(response);
    });
}

function loginWithFB() {
    var opts = {
        scope: 'public_profile,user_friends,email'
    };
    FB.login(function(response) {
        statusChangeCallback(response);
    }, opts);
}

function restoreUsernameAndTokenFromCookies() {
    _username = Cookies.getCookie("rent4me_uname");
    _token = Cookies.getCookie("rent4me_token");
}

function storeUsernameAndTokenInCookies() {
    if(hasCredentials()) {
        Cookies.setCookieTemp("rent4me_uname", _username) ;
        Cookies.setCookieTemp("rent4me_token", _token);
    }
}

function logoutOnBackend() {
    if(hasCredentials()) {
        var data = {"token" : _token};
        BlockUI.blockUI();
        $.ajax({
            url: '/rest/auth',
//                dataType: 'json',
            type: 'DELETE',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            beforeSend: function (request) {
                request.setRequestHeader("Authorization", "Basic " + getAuthHeader());
            },
            success: function (data) {
                console.log("Success!");
                console.log("Data:");

                Cookies.deleteCookie("rent4me_uname");
                Cookies.deleteCookie("rent4me_token");
                _username = null;
                _token = null;
                _fbUserId = null;
                _fbAccessToken = null;

                BlockUI.unblockUI();

                Utils.navigateToStart();
            },
            error: function (xhr, status, err) {
//                    console.error('/rest/apartment', status, err.toString());
                console.log("Error!");
                BlockUI.unblockUI();
            }
        });
    }
}

function hasCredentials() {
    return _username && _token;
}

function getAuthHeader(){
    if(hasCredentials()) {
        return Utils.__base64Encode(_username+":"+_token);
    } else {
        return null;
    }
}

/**
 * Created by dionis on 28/11/14.
 */
var Auth = {
    getFbId: getFbId,
    getVkId: getVkId,
    loginWithVK: loginWithVK,
    authOnBackendWithVK: authOnBackendWithVK,
    statusChangeCallback: statusChangeCallback,
    authOnBackendWithFacebook: authOnBackendWithFacebook,
    checkLoginState: checkLoginState,
    loginWithFB: loginWithFB,
    restoreUsernameAndTokenFromCookies: restoreUsernameAndTokenFromCookies,
    storeUsernameAndTokenInCookies: storeUsernameAndTokenInCookies,
    logoutOnBackend: logoutOnBackend,
    hasCredentials: hasCredentials,
    getAuthHeader: getAuthHeader
};

module.exports = Auth;