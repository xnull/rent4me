/**
 * Created by dionis on 03/12/14.
 */
//var Dispatcher = require('flux/lib/Dispatcher');
var AppDispatcher = require('../dispatcher/AppDispatcher');

var EventEmitter = require('rent4meEmitter');
var AuthConstants = require('../constants/AuthConstants');

var Cookies = require('../common/Cookies');
var Utils = require('../common/Utils');

var assign = require('object-assign');

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

function hasCredentials() {
    return _username && _token;
}

function getAuthHeader() {
    if (hasCredentials()) {
        return Utils.__base64Encode(_username + ":" + _token);
    } else {
        return null;
    }
}

function eraseStoredData() {
    Cookies.deleteCookie("rent4me_uname");
    Cookies.deleteCookie("rent4me_token");
    _username = null;
    _token = null;
    _fbUserId = null;
    _fbAccessToken = null;
}


var CHANGE_EVENT = 'change';

//var AuthStore = assign({}, EventEmitter.prototype, {
var AuthStore = assign({}, EventEmitter.prototype, {
    emitChange: function () {
        this.emit(CHANGE_EVENT);
    },

    getFbId: getFbId,

    getVkId: getVkId,

    getUsernameTokenPair: function () {
        return {
            username: _username,
            token: _token
        };
    },

    getFbUserIdAccessTokenPair: function () {
        return {
            fb_id: _fbUserId,
            fb_access_token: _fbAccessToken
        }
    },

    hasCredentials: hasCredentials,

    getAuthHeader: getAuthHeader,

    /**
     * @param {function} callback
     */
    addChangeListener: function (callback) {
        this.on(CHANGE_EVENT, callback);
    },
    /**
     * @param {function} callback
     */
    removeChangeListener: function (callback) {
        this.removeListener(CHANGE_EVENT, callback);
    }
});

// Register to handle all updates
AppDispatcher.register(function (payload) {
    var action = payload.action;
//    console.log('Payload received in User dispatcher');
//    console.log("Action:");
//    console.log(action);
//    console.log("User:");
//    console.log(userObject);

    switch (action.actionType) {
        case AuthConstants.AUTH_AUTHENTICATED_WITH_VK:
            _username = action.data.username;
            _token = action.data.token;
            break;

        case AuthConstants.AUTH_AUTHENTICATED_WITH_FACEBOOK:
            _username = action.data.username;
            _token = action.data.token;

            break;

        case AuthConstants.AUTH_AUTHENTICATED_WITH_BACKEND:
            _username = action.data.username;
            _token = action.data.token;

            break;

        case AuthConstants.AUTH_FB_AUTH_STATE_CHANGED:
            _fbUserId = action.response.authResponse.userID;
            _fbAccessToken = action.response.authResponse.accessToken;
            break;

        case AuthConstants.AUTH_AUTHENTICATION_ERROR:
            eraseStoredData();

            break;

        case AuthConstants.AUTH_RE_STORE_USERNAME_AND_TOKEN_FROM_COOKIES:
            _username = Cookies.getCookie("rent4me_uname");
            _token = Cookies.getCookie("rent4me_token");
            break;

        case AuthConstants.AUTH_STORE_USERNAME_AND_TOKEN_IN_COOKIES:
            if (hasCredentials()) {
                Cookies.setCookieTemp("rent4me_uname", _username);
                Cookies.setCookieTemp("rent4me_token", _token);
            }
            break;

        case AuthConstants.AUTH_USER_LOGGED_OUT:
            eraseStoredData();

            break;

        default:
            return false;
    }


    // This often goes in each case that should trigger a UI change. This store
    // needs to trigger a UI change after every view action, so we can make the
    // code less repetitive by putting it here. We need the default case,
    // however, to make sure this only gets called after one of the cases above.
    AuthStore.emitChange();
    return true; // No errors. Needed by promise in Dispatcher.
});

module.exports = AuthStore;