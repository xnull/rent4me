/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var ApartmentConstants = require('../constants/ApartmentConstants');
var BlockUI = require('../common/BlockUI');
var assign = require('object-assign');
var JSON = require('JSON2');
var Ajax = require('../common/Ajax');

var ApartmentActions = {
    /**
     * @param {object} obj
     */
    createApartment: function (obj) {
        BlockUI.blockUI();

        var data = assign({}, obj);

        Ajax
            .POST('/rest/users/apartment')
            .authorized()
            .withJsonBody(data)
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: ApartmentConstants.APARTMENT_CREATE,
                    apartment: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    },

    /**
     * @param {object} obj
     */
    updateApartmentRentInfo: function (obj) {
        BlockUI.blockUI();

        var data = assign({}, obj);

        Ajax
            .PATCH('/rest/users/apartment')
            .authorized()
            .withJsonBody(data)
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: ApartmentConstants.APARTMENT_UPDATE,
                    apartment: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    },

    /**
     * @param {object} obj
     */
    sendApartmentRentInfo: function (obj) {
        BlockUI.blockUI();

        var data = assign({}, obj);

        Ajax
            .POST('/rest/users/apartment/data_change_request')
            .authorized()
            .withJsonBody(data)
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: ApartmentConstants.APARTMENT_CHANGE_REQUEST_CREATED,
                    apartment: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    },

    loadMyApartment: function() {
        BlockUI.blockUI();

        Ajax
            .GET('/rest/users/apartment')
            .authorized()
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: ApartmentConstants.APARTMENT_LOADED,
                    apartment: data
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                if(xhr.status == '404'){
                    AppDispatcher.handleViewAction({
                        actionType: ApartmentConstants.APARTMENT_LOADED,
                        apartment: {}
                    });
                }
                BlockUI.unblockUI();
            })
            .execute();
    },

    deleteMyApartment: function() {
        BlockUI.blockUI();

        Ajax
            .DELETE('/rest/users/apartment')
            .authorized()
            .onSuccess(function (data) {
                AppDispatcher.handleViewAction({
                    actionType: ApartmentConstants.APARTMENT_DESTROY,
                    apartment: {}
                });

                BlockUI.unblockUI();
            })
            .onError(function (xhr, status, err) {
                BlockUI.unblockUI();
            })
            .execute();
    }
};

module.exports = ApartmentActions;
