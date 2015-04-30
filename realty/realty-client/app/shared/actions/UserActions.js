/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var UserConstants = require('../constants/UserConstants');
var UserStore = require('../stores/UserStore');
var BlockUI = require('rent4meBlockUI');
var assign = require('object-assign');
var _ = require('underscore');
var AuthActions = require('./AuthActions');
var AuthStore = require('../stores/AuthStore');

var Ajax = require('../common/Ajax');

var _myProfileIsLoading = false;

var UserActions = {

    registerOnBackendWithEmailAndPassword: function (name, email, password, phone) {

        var data = {"name": name, "email": email, "password": password, "phone": phone};
        console.log('data: ');
        console.log(data);

        var self = this;

        BlockUI.blockUI();

        Ajax
            .POST('/rest/users')
            .withJsonBody(data)
            .onSuccess(function (data) {
                console.log("Success!");
                console.log("Data:");
                console.log(data);

                //AppDispatcher.handleViewAction({
                //    actionType: AuthConstants.AUTH_AUTHENTICATED_WITH_BACKEND,
                //    data: data
                //});

                BlockUI.unblockUI();

                AuthActions.loginOnBackendWithEmailAndPassword(email, password);


                //that.storeUsernameAndTokenInCookies();

                //Utils.navigateToPersonal();
            })
            .onError(function (xhr, status, err) {

                var errors = [];

                if(xhr.status == 409) {
                    errors.push({key: 'E-mail', value: 'E-mail уже занят'});
                } else {
                    errors.push({key: 'Ошибка сервера', value: 'У нас произошла ошибка и мы уже работаем, что бы исправить ее'});
                }

                AppDispatcher.handleViewAction({
                    actionType: UserConstants.USER_SERVER_ERROR,
                    errors: errors
                });

//                    console.error('/rest/apartment', status, err.toString());
                console.log("Error!");
                BlockUI.unblockUI();
            })
            .execute();

    },

    /**
     * @param {object} obj
     */
    save: function (obj) {
        BlockUI.blockUI();

        var data = assign({}, obj);

        Ajax
            .PUT('/rest/users/me')
            .authorized()
            .withJsonBody(data)
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: UserConstants.USER_PROFILE_SAVE,
                    user: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    },


    loadMyProfilePromise: function () {
        if(!AuthStore.hasCredentials()) return new Promise(function(resolve, reject){
            reject("Not authorized. Can not load my profile");
        });
        BlockUI.blockUI();
        _myProfileIsLoading = true;
        var promise = new Promise(function(resolve, reject){
            Ajax
                .GET('/rest/users/me')
                .authorized()
                .onSuccess(function (data) {
                    _myProfileIsLoading = false;
                    BlockUI.unblockUI();
                    resolve(data);
                })
                .onError(function (xhr, status, err) {
                    _myProfileIsLoading = false;
                    BlockUI.unblockUI();
                    reject("Couldn't load my profile");
                })
                .execute();
        });
        return promise;
    },

    loadMyProfile: function () {
        var loadMyProfilePromise = this.loadMyProfilePromise();
        loadMyProfilePromise.then(function(data){
            AppDispatcher.handleViewAction({
                actionType: UserConstants.USER_PROFILE_LOADED,
                user: data
            });
        });
    },

    loadMyProfileIfNotLoaded: function () {
        if (_myProfileIsLoading) {
            console.log('My profile is loading. Skipping loading.');
            return;
        }
        var myProfile = UserStore.getMyProfile();
        if (_.isEmpty(myProfile)) {
            console.log('My profile is empty. Loading.');
            console.log(myProfile);
            this.loadMyProfile();
        }
    }
};

module.exports = UserActions;
