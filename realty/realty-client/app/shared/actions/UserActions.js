/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var UserConstants = require('../constants/UserConstants');
var UserStore = require('../stores/UserStore');
var BlockUI = require('rent4meBlockUI');
var assign = require('object-assign');
var _ = require('underscore');

var Ajax = require('../common/Ajax');

var AuthActions = {
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

    loadMyProfile: function () {
        BlockUI.blockUI();

        Ajax
            .GET('/rest/users/me')
            .authorized()
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: UserConstants.USER_PROFILE_LOADED,
                    user: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    },

    loadMyProfileIfNotLoaded: function () {
        var myProfile = UserStore.getMyProfile();
        if (_.isEmpty(myProfile)) {
            console.log('My profile is empty. Loading.');
            console.log(myProfile);
            this.loadMyProfile();
        }
    }
};

module.exports = AuthActions;
