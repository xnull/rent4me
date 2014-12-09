/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var ApartmentConstants = require('../constants/ApartmentConstants');
var Auth = require('../common/Auth');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');

var ApartmentActions = {
    /**
     * @param {object} obj
     */
    createApartment: function (obj) {
        BlockUI.blockUI();

        var data = assign({}, obj);

        $.ajax({
            url: '/rest/users/apartment',
            type: 'POST',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            beforeSend: function (request) {
                request.setRequestHeader("Authorization", "Basic " + Auth.getAuthHeader());
            },
            success: function (data) {
//                console.log("Success!");
//                console.log("Data:");
//                console.log(data);

                AppDispatcher.handleViewAction({
                    actionType: ApartmentConstants.APARTMENT_CREATE,
                    apartment: data
                });

                BlockUI.unblockUI();
            },
            error: function (xhr, status, err) {
                BlockUI.unblockUI();
            }
        });
    },

    /**
     * @param {object} obj
     */
    updateApartmentRentInfo: function (obj) {
        BlockUI.blockUI();

        var data = assign({}, obj);

        $.ajax({
            url: '/rest/users/apartment',
            type: 'PATCH',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            beforeSend: function (request) {
                request.setRequestHeader("Authorization", "Basic " + Auth.getAuthHeader());
            },
            success: function (data) {
//                console.log("Success!");
//                console.log("Data:");
//                console.log(data);

                AppDispatcher.handleViewAction({
                    actionType: ApartmentConstants.APARTMENT_UPDATE,
                    apartment: data
                });

                BlockUI.unblockUI();
            },
            error: function (xhr, status, err) {
                BlockUI.unblockUI();
            }
        });
    },

    /**
     * @param {object} obj
     */
    sendApartmentRentInfo: function (obj) {
        BlockUI.blockUI();

        var data = assign({}, obj);

        $.ajax({
            url: '/rest/users/apartment/data_change_request',
            type: 'POST',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            beforeSend: function (request) {
                request.setRequestHeader("Authorization", "Basic " + Auth.getAuthHeader());
            },
            success: function (data) {
//                console.log("Success!");
//                console.log("Data:");
//                console.log(data);

                AppDispatcher.handleViewAction({
                    actionType: ApartmentConstants.APARTMENT_CHANGE_REQUEST_CREATED,
                    apartment: data
                });

                BlockUI.unblockUI();
            },
            error: function (xhr, status, err) {
                BlockUI.unblockUI();
            }
        });
    },

    loadMyApartment: function() {
        BlockUI.blockUI();

        $.ajax({
            url: '/rest/users/apartment',
            dataType: 'json',
            type: 'GET',
            beforeSend: function (request) {
                request.setRequestHeader("Authorization", "Basic " + Auth.getAuthHeader());
            },
            success: function (data) {
//                console.log("Success!");
//                console.log("Data:");
//                console.log(data);

                AppDispatcher.handleViewAction({
                    actionType: ApartmentConstants.APARTMENT_LOADED,
                    apartment: data
                });

                BlockUI.unblockUI();
            },
            error: function (xhr, status, err) {
                if (xhr.status == '404') {
                    AppDispatcher.handleViewAction({
                        actionType: ApartmentConstants.APARTMENT_LOADED,
                        apartment: {}
                    });
//                    that.setState({data: {}})
                } else {
//                        console.error('/rest/apartment', status, err.toString());
                    alert('Service unavailable');
                }
                BlockUI.unblockUI();
            }
        });
    },

    deleteMyApartment: function() {
        BlockUI.blockUI();

        $.ajax({
            url: '/rest/users/apartment',
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader("Authorization", "Basic " + Auth.getAuthHeader());
            },
            success: function (data) {
//                console.log("Success!");
//                console.log("Data:");
//                console.log(data);

                AppDispatcher.handleViewAction({
                    actionType: ApartmentConstants.APARTMENT_DESTROY,
                    apartment: {}
                });

                BlockUI.unblockUI();
            },
            error: function (xhr, status, err) {
                BlockUI.unblockUI();
            }
        });
    }
};

module.exports = ApartmentActions;
