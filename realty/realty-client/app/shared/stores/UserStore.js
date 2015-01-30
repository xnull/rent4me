/**
 * Created by dionis on 03/12/14.
 */
//var Dispatcher = require('flux/lib/Dispatcher');
var AppDispatcher = require('../dispatcher/AppDispatcher');

var EventEmitter = require('rent4meEmitter');
var UserConstants = require('../constants/UserConstants');

var assign = require('object-assign');

var _users = {};
var _me = {};
var _serverErrors = {};


var CHANGE_EVENT = 'change';
var SERVER_ERROR = 'error';

var saveMyProfile = function (myProfile) {
    //copy props
    _me = assign({}, _me, myProfile);
};

var getMyProfile = function () {
    //copy props
    return assign({}, _me);
};

//var UserStore = assign({}, EventEmitter.prototype, {
var UserStore = assign({}, EventEmitter.prototype, {
    emitChange: function () {
        this.emit(CHANGE_EVENT);
    },

    emitServerError: function () {
        this.emit(SERVER_ERROR);
    },

    getMyProfile: function () {
        return getMyProfile();
    },

    getServerErrors: function () {
        return _serverErrors;
    },

    saveMyProfile: function (myProfile) {
        saveMyProfile(myProfile);
    },

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
    },

    /**
     * @param {function} callback
     */
    addServerErrorListener: function (callback) {
        this.on(SERVER_ERROR, callback);
    },
    /**
     * @param {function} callback
     */
    removeServerErrorListener: function (callback) {
        this.removeListener(SERVER_ERROR, callback);
    }
});

// Register to handle all updates
AppDispatcher.register(function (payload) {
    var action = payload.action;
//    console.log('Payload received in User dispatcher');
    var userObject = assign({}, action.user || {});
//    console.log("Action:");
//    console.log(action);
//    console.log("User:");
//    console.log(userObject);

    switch (action.actionType) {
        case UserConstants.USER_PROFILE_SAVE:
            saveMyProfile(userObject);
            break;
        case UserConstants.USER_PROFILE_LOADED:
            console.log('My profile loaded');
            console.log(userObject);
            saveMyProfile(userObject);
            break;

        case UserConstants.USER_SERVER_ERROR:
            _serverErrors = action.errors || [];
            UserStore.emitServerError();
            return true;
            break;
        default:
            return false;
    }


    // This often goes in each case that should trigger a UI change. This store
    // needs to trigger a UI change after every view action, so we can make the
    // code less repetitive by putting it here. We need the default case,
    // however, to make sure this only gets called after one of the cases above.
    UserStore.emitChange();
    return true; // No errors. Needed by promise in Dispatcher.
});

module.exports = UserStore;