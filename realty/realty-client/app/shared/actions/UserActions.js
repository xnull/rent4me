/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var UserConstants = require('../constants/UserConstants');
var Auth = require('../common/Auth');
var assign = require('object-assign');

var UserActions = {
    /**
     * @param {object} obj
     */
    save: function (obj) {
        $.blockUI();

        var data = assign({}, obj);

        $.ajax({
            url: '/rest/users/me',
            type: 'PUT',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            beforeSend: function (request) {
                request.setRequestHeader("Authorization", "Basic " + Auth.getAuthHeader());
            },
            success: function (data) {
                console.log("Success!");
                console.log("Data:");
                console.log(data);

                AppDispatcher.handleViewAction({
                    actionType: UserConstants.USER_PROFILE_SAVE,
                    user: obj
                });

                $.unblockUI();
            },
            error: function (xhr, status, err) {
                $.unblockUI();
            }
        });
    },

    loadMyProfile: function() {
        $.blockUI();

        $.ajax({
            url: '/rest/users/me',
            dataType: 'json',
            type: 'GET',
            beforeSend: function (request) {
                request.setRequestHeader("Authorization", "Basic " + Auth.getAuthHeader());
            },
            success: function (data) {
                console.log("Success!");
                console.log("Data:");
                console.log(data);

                AppDispatcher.handleViewAction({
                    actionType: UserConstants.USER_PROFILE_LOADED,
                    user: data
                });

                $.unblockUI();
            },
            error: function (xhr, status, err) {
                $.unblockUI();
            }
        });
    }
};

module.exports = UserActions;
