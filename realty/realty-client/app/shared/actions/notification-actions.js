/**
 * Created by dionis on 04/12/14.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var Constants = require('../constants/notification-constants');
var Store = require('../stores/ChatStore');
var BlockUI = require('../common/BlockUI');
var WebSocketUtil = require('../common/WebSocketUtil');
var AuthStore = require('../stores/AuthStore');
var assign = require('object-assign');
var JSON = require('JSON2');

var Ajax = require('../common/Ajax');

var NotificationActions = {

    subscribe: function() {
        var self = this;
        var wsAuthMessage = AuthStore.getWSAuthMessage();
        if(wsAuthMessage) {

            var _multiplexer = WebSocketUtil.getMultiplexer();
            var messagesChannel = _multiplexer.channel('notifications');

            messagesChannel.onmessage = function(evt) {
                console.log('received data on messages channel');
                console.log(evt.data);
                AppDispatcher.handleViewAction({
                    actionType: Constants.NEW_NOTIFICATION_RECEIVED,
                    notification: JSON.parse(evt.data)
                });
            }
        }
    },

    unSubscribe: function() {
       /*if(_socket) {
           _socket.close();
           _socket = null;
       }*/
    },

    loadAllNotifications: function () {
        BlockUI.blockUI();

        //var limit = Store.getLimit();
        //var offset = Store.getOffset();
        var promise = new Promise(function(resolve, reject){
            Ajax
                .GET('/rest/users/me/notifications/unread')
                .authorized()
                .onSuccess(function (data) {
                    BlockUI.unblockUI();
                    resolve(data);
                })
                .onError(function (xhr, status, err) {
                    BlockUI.unblockUI();
                    reject("Unable to load notifications");
                })
                .execute();
        }).then(function(data){
                AppDispatcher.handleViewAction({
                    actionType: Constants.NOTIFICATIONS_LOADED,
                    notifications: data
                });
            }, function(err){
                console.error(err);
            });
        return promise;
    }
};

module.exports = NotificationActions;